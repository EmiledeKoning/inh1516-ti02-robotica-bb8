package Echo;

import Sensor.Sensor;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import com.pi4j.wiringpi.SoftPwm;

import java.text.DecimalFormat;
import java.text.Format;



public class Echo extends Sensor{
	  private static int PIN_NUMBER = 1;
	  private final static Format DF22 = new DecimalFormat("#0.00");
	  private final static double SOUND_SPEED = 34300;           // in cm, 343 m/s
	  private final static double DIST_FACT   = SOUND_SPEED / 2; // round trip
	  private final static int MIN_DIST       = 5;
	  
	  private final static long BETWEEN_LOOPS = 500L;
	  private final static long MAX_WAIT      = 500L;
	  
	  private final static boolean DEBUG = false;
	  
	  public static void main(String[] args)
	    throws InterruptedException
	  {
		initPins();
	    System.out.println("GPIO Control - Range Sensor HC-SR04.");
	    System.out.println("Will stop is distance is smaller than " + MIN_DIST + " cm");

	    // create gpio controller
	    final GpioController gpio = GpioFactory.getInstance();

	    final GpioPinDigitalOutput trigPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_15, "Trig", PinState.LOW);
	    final GpioPinDigitalInput  echoPin = gpio.provisionDigitalInputPin(RaspiPin.GPIO_18, "Echo");
	    
	    final GpioPinDigitalOutput ledRed  = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "Red",   PinState.LOW);
	    final GpioPinDigitalOutput ledGreen   = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_14, "Green",   PinState.LOW);
	    
	    final GpioPinDigitalOutput[] ledArray = new GpioPinDigitalOutput[] { ledRed, ledGreen };

	    Runtime.getRuntime().addShutdownHook(new Thread()
	    {
	      public void run()
	      {
	        System.out.println("Oops!");
	        for (int i=0; i<ledArray.length; i++)
	          ledArray[i].low();
	        gpio.shutdown();
	        System.out.println("Exiting nicely.");
	      }       
	    });
	    
	    System.out.println("Waiting for the sensor to be ready (2s)...");
	    Thread.sleep(2000L);
	    Thread mainThread = Thread.currentThread();

	    boolean go = true;
	    System.out.println("Looping until the distance is less than " + MIN_DIST + " cm");
	    while (go)
	    {
	      boolean ok = true;
	      double start = 0d, end = 0d;
	      if (DEBUG) System.out.println("Triggering module.");
	      TriggerThread trigger = new TriggerThread(mainThread, trigPin, echoPin);
	      trigger.start();
	      try 
	      {
	        synchronized (mainThread)
	        {
	          long before = System.currentTimeMillis();
	          mainThread.wait(MAX_WAIT);
	          long after = System.currentTimeMillis();
	          long diff = after - before;
	          if (DEBUG) System.out.println("MainThread done waiting (" + Long.toString(diff) + " ms)");
	          if (diff >= MAX_WAIT)
	          {
	            ok = false;
	            if (true || DEBUG) System.out.println("...Resetting.");
	            if (trigger.isAlive())
	              trigger.interrupt();
	          }
	        }
	      }
	      catch (Exception ex)
	      {
	        ex.printStackTrace();
	        ok = false;
	      }
	      
	      if (ok)
	      {      
	        start = trigger.getStart();
	        end = trigger.getEnd();
	        if (DEBUG) System.out.println("Measuring...");
	        if (end > 0 && start > 0)
	        {
//	        double pulseDuration = (end - start) / 1000000000d; // in seconds
	          double pulseDuration = (end - start) / 10E9; // in seconds
	          double distance = pulseDuration * DIST_FACT;
	          if (distance < 1000) // Less than 10 meters
	            System.out.println("Distance: " + DF22.format(distance) + " cm."); // + " (" + pulseDuration + " = " + end + " - " + start + ")");
	          if (distance > 0 && distance < MIN_DIST)
	            go = false;
	          else
	          {
	            if (distance < 0)
	              System.out.println("Dist:" + distance + ", start:" + start + ", end:" + end);
	            for (int i=0; i<ledArray.length; i++)
	            {
	              if (distance < ((i+1) * 10))
	              {
	                
	      			for (int p = 0; p <= 100; p++) {
	    				// softPwmWrite(int pin, int value)
	    				// This updates the PWM value on the given pin. The value is
	    				// checked to be in-range and pins
	    				// that haven't previously been initialized via softPwmCreate
	    				// will be silently ignored.
	    				SoftPwm.softPwmWrite(PIN_NUMBER, p);
	    				Thread.sleep(25);
	    			}
	    			// fade LED to fully OFF
	    			for (int p = 100; p >= 0; p--) {
	    				SoftPwm.softPwmWrite(PIN_NUMBER, p);
	    				Thread.sleep(25);
	    			}
	              }
	              else
	                ledArray[i].low();            
	            }
	            try { Thread.sleep(BETWEEN_LOOPS); } catch (Exception ex) {}
	          }
	        }
	        else
	        {
	          System.out.println("Hiccup!");
	      //  try { Thread.sleep(2000L); } catch (Exception ex) {}
	        }
	      }
	    }
	    System.out.println("Done.");
	    for (int i=0; i<ledArray.length; i++)
	      ledArray[i].low();

	    trigPin.low(); // Off

	    gpio.shutdown();
	    System.exit(0);
	  }
	  
	  private static class TriggerThread extends Thread
	  {
	    private GpioPinDigitalOutput trigPin = null;
	    private GpioPinDigitalInput echoPin = null;
	    private Thread caller = null;
	    
	    private double start = 0D, end = 0D;
	    
	    public TriggerThread(Thread parent, GpioPinDigitalOutput trigger, GpioPinDigitalInput echo)
	    {
	      this.trigPin = trigger;
	      this.echoPin = echo;
	      this.caller = parent;
	    }
	    
	    public void run()
	    {
	      trigPin.high();
	      // 10 microsec (10000 ns) to trigger the module  (8 ultrasound bursts at 40 kHz) 
	      // https://www.dropbox.com/s/615w1321sg9epjj/hc-sr04-ultrasound-timing-diagram.png
	      try { Thread.sleep(0, 10000); } catch (Exception ex) { ex.printStackTrace(); } 
	      trigPin.low();
	      
	      // Wait for the signal to return
	      while (echoPin.isLow())
	        start = System.nanoTime();
	      // There it is
	      while (echoPin.isHigh())
	        end = System.nanoTime();
	      
	      synchronized (caller) { caller.notify(); }
	    }    
	    
	    public double getStart() { return start; }
	    public double getEnd() { return end; }
	  }
	
	  private static void initPins(){
			// initialize wiringPi library, this is needed for PWM
			//Gpio.wiringPiSetup();
		  
			// softPwmCreate(int pin, int value, int range)
			// the range is set like (min=0 ; max=100)
			SoftPwm.softPwmCreate(PIN_NUMBER, 0, 100);
	  }
	  
	private byte Adress;

	public Echo (byte NewAdress)
	{
		this.Adress = NewAdress;
	}
}
