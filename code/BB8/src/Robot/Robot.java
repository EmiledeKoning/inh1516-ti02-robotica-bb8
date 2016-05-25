package Robot;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import Acceleration.Acceleration;
import Altitude.Altitude;
import Echo.Echo;
import Gyro.Gyro;
import Magnetometer.Magnetometer;
import Temperature.Temperature;

public class Robot {

	public static void main(String[] args) throws UnknownHostException {
		InetAddress addr = InetAddress.getLocalHost();
		if (addr.getHostName() == "BB8_Head"){
			Init_Head();
		}
		else if (addr.getHostName() == "BB8_Body"){
			Init_Body();
		}
	}

	private static void Init_Head(){
		Properties prop = new Properties();
    	InputStream input = null;
    	
    	try {

    		input = new FileInputStream("/BB8/BB8.config");

    		// load a properties file
    		prop.load(input);

    		// get the adress value of each sensor from the config file and create a instance of each sensor
    		Gyro GyroSensor = new Gyro(Byte.parseByte(prop.getProperty("Gyro_Addr")));
    		Acceleration AccelerationSensor = new Acceleration(Byte.parseByte(prop.getProperty("Acc_Addr")));
    		Altitude AltitudeSensor = new Altitude(Byte.parseByte(prop.getProperty("Alt_Addr")));
    		// NOT A I2C thing!! Echo EchoSensor = new Echo(Byte.parseByte(prop.getProperty("Echo_Addr")));
    		Magnetometer MagnetometerSensor = new Magnetometer(Byte.parseByte(prop.getProperty("Magn_Addr")));
    		Temperature TemperatureSensor = new Temperature(Byte.parseByte(prop.getProperty("Temp_Addr")));

    		System.out.println(prop.getProperty(""));

    	} catch (IOException ex) {
    		ex.printStackTrace();
    	} finally {
    		if (input != null) {
    			try {
    				input.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    	}
		
	}
	private static void Init_Body(){
		Properties prop = new Properties();
    	InputStream input = null;
    	
    	try {

    		input = new FileInputStream("/BB8/BB8.config");

    		// load a properties file
    		prop.load(input);

    		// get the adress value of each motor(hat) from the config file and create a instance of each motor
    		

    		System.out.println(prop.getProperty(""));

    	} catch (IOException ex) {
    		ex.printStackTrace();
    	} finally {
    		if (input != null) {
    			try {
    				input.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    	}
	}
	public static void Shutdown() throws IOException{
		Runtime.getRuntime().exec("sudo shutdown now");
	}
	public static void Reboot() throws IOException{
		Runtime.getRuntime().exec("sudo reboot");
	}
}
