package Sensor;
import java.io.*;
import java.nio.file.Files;

import org.omg.CORBA.PUBLIC_MEMBER;

import com.pi4j.device.Device;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.jni.I2C;

import Acceleration.Acceleration;
import Accelerometer.Accelerometer;
import Gyro.Gyro;
import Magnetometer.Magnetometer;

//The default i2c addresses of BerryIMU are;

//0xE1 for the magnetometer and accelerometer
//0x6A for the gyroscope
//0x77 for the pressure sensor



public class OpenBus {
	
	private static I2CBus bus;
	//LSM9DS0 Gyro
	public final static int GYR_ADDRESS = 0x6A;
	public final static int ACC_ADDRESS = 0x1E;
	//LSM9DS0 Gyro Registers
	private static final int CTRL_REG1_G = 0x20;
	private static final int CTRL_REG4_G = 0x23;
	//private static final int OUT_X_L_G = 0x28;
	
	//LSM9DS0 Accel Registers
	private final static int CTRL_REG1_XM = 0x20;
	private static final int CTRL_REG2_XM = 0x21;
	
	
	//private static final int OUT_X_H_G = 0x29;
	
	private static byte[] data = new byte[6];
	
	public static void main(String[] args) throws IOException{
		//constr
	/*	OpenBus myFile = new OpenBus();
		OpenBus specSens = new OpenBus();
		
		//open up the i2c bus
		myFile.openFile("/dev/i2c-1");
		//Specify the sensor to use
		//Get it from (BB8-)Body*/
//		myFile.closeFile();
		
		try
		{
		bus = I2CFactory.getInstance(I2CBus.BUS_1);
		Accel();
		Thread.sleep(300);
		Gyro();
		Thread.sleep(300);
		Magneto();
	    
		
		}
		catch(IOException e)
		{
			System.err.println(e.getMessage());
		}
		//String specifiedsensor = "???";
		/*while (true) {
			int xlg = Gyro.Gyro.read(OUT_X_L_G, data, 0, 6);
			if(xlg < 6) 
			{
				System.out.println("Error reading data, < " + data.length + "bytes");
			}*/
		
		
		
	}
	
		public static void Accel()
		{
			// Enable accelerometer.
			Accelerometer.Accelerometer = bus.getDevice(ACC_ADDRESS);
		    Accelerometer.Accelerometer.write(CTRL_REG1_XM, (byte) 0b01100111); //  z,y,x axis enabled, continuous update,  100Hz data rate
		    Accelerometer.Accelerometer.write(CTRL_REG2_XM, (byte) 0b00100000); // +/- 16G full scale
		    
		    Thread.sleep(300);
			
		    // Read 6 bytes of data
			// xAccl lsb, xAccl msb, yAccl lsb, yAccl msb, zAccl lsb, zAccl msb
			data[0] = (byte)Accelerometer.Accelerometer.read(0x28);
			data[1] = (byte)Accelerometer.Accelerometer.read(0x29);
			data[2] = (byte)Accelerometer.Accelerometer.read(0x2A);
			data[3] = (byte)Accelerometer.Accelerometer.read(0x2B);
			data[4] = (byte)Accelerometer.Accelerometer.read(0x2C);
			data[5] = (byte)Accelerometer.Accelerometer.read(0x2D);
		    
			// Convert the data
			int xAccl = ((data[1] & 0xFF) * 256 + (data[0] & 0xFF)) ;
			if(xAccl > 32767)
			{
				xAccl -= 65536;
			}	

			int yAccl = ((data[3] & 0xFF) * 256 + (data[2] & 0xFF)) ;
			if(yAccl > 32767)
			{
				yAccl -= 65536;
			}

			int zAccl = ((data[5] & 0xFF) * 256 + (data[4] & 0xFF)) ;
			if(zAccl > 32767)
			{
				zAccl -= 65536;
			}
			
			System.out.printf("Acceleration in X-Axis : %d |%n", xAccl);
			System.out.printf("Acceleration in Y-Axis : %d |%n", yAccl);
			System.out.printf("Acceleration in Z-Axis : %d |\n", zAccl);
		}
	
		public static void Gyro()
		{
			//Enable Gyro
			Gyro.Gyro = bus.getDevice(GYR_ADDRESS);
			Gyro.Gyro.write(CTRL_REG1_G, (byte) 0b00001111); // Normal power mode, all axes enabled
			Gyro.Gyro.write(CTRL_REG4_G, (byte) 0b00110000); // Continuous update, 2000 dps full scale
			
			Thread.sleep(300);
			
			// Read 6 bytes of data
			// xGyro lsb, xGyro msb, yGyro lsb, yGyro msb, zGyro lsb, zGyro msb
			byte[] data = new byte[6];
			data[0] = (byte)Gyro.Gyro.read(0x28);
			data[1] = (byte)Gyro.Gyro.read(0x29);
			data[2] = (byte)Gyro.Gyro.read(0x2A);
			data[3] = (byte)Gyro.Gyro.read(0x2B);
			data[4] = (byte)Gyro.Gyro.read(0x2C);
			data[5] = (byte)Gyro.Gyro.read(0x2D);
			

			// Convert the data
			int xGyro = ((data[1] & 0xFF) * 256 + (data[0] & 0xFF)) ;
			if(xGyro > 32767)
			{
				xGyro -= 65536;
			}
			int yGyro = ((data[3] & 0xFF) * 256 + (data[2] & 0xFF)) ;
			if(yGyro > 32767)
			{
				yGyro -= 65536;
			}
			int zGyro = ((data[5] & 0xFF) * 256 + (data[4] & 0xFF)) ;
			if(zGyro > 32767)
			{
				zGyro -= 65536;
			}
			
			System.out.printf("X-axis Of Rotation : %d |%n", xGyro);
			System.out.printf("Y-axis Of Rotation : %d |%n", yGyro);
			System.out.printf("Z-axis Of Rotation : %d |\n", zGyro);
			
		}
		
		public static void Magneto()
		{
			Magnetometer.Magnetometer = bus.getDevice(ACC_ADDRESS);
			// Read 6 bytes of data
			// xMag lsb, xMag msb, yMag lsb, yMag msb, zMag lsb, zMag msb
			data[0] = (byte)Magnetometer.Magnetometer.read(0x08);
			data[1] = (byte)Magnetometer.Magnetometer.read(0x09);
			data[2] = (byte)Magnetometer.Magnetometer.read(0x0A);
			data[3] = (byte)Magnetometer.Magnetometer.read(0x0B);
			data[4] = (byte)Magnetometer.Magnetometer.read(0x0C);
			data[5] = (byte)Magnetometer.Magnetometer.read(0x0D);

			int xMag = ((data[1] & 0xFF) * 256 + (data[0] & 0xFF));
			if(xMag > 32767)
			{
				xMag -= 65536;
			}	

			int yMag = ((data[3] & 0xFF) * 256 + (data[2] & 0xFF)) ;
			if(yMag > 32767)
			{
				yMag -= 65536;
			}

			int zMag = ((data[5] & 0xFF) * 256 + (data[4] & 0xFF)) ;
			if(zMag > 32767)
			{
				zMag -= 65536;
			}
			
			System.out.printf("Magnetic field in X-Axis : %d |%n", xMag);
			System.out.printf("Magnetic field in Y-Axis : %d |%n", yMag);
			System.out.printf("Magnetic field in Z-Axis : %d |\n", zMag);
		}
}
	
	
	
/*	}

	protected String filename;
	protected int fd;
	protected int closefd;
	
	public void openFile(String filename) throws IOException{
		this.filename = filename;
		fd = I2C.i2cOpen(filename);
		if (fd < 0) {
			throw new IOException("Cannot open file handle for " + filename + " got " + fd + " back.");
			}
		String line ;
		 int numLines ;
		 // open input file
		 FileReader reader = new FileReader(filename);
		 numLines = 0 ;
		 // read each line from the file
		 line = in.readLine() ; // read first
		 while (line != null)
		 {
		 numLines++ ;
		 System.out.println(line) ; // print current
		 line = in.readLine() ; // read next line
		 }
		  System.out.println(numLines + "lines read from file") ;
		
		} // end openFile
	
	public void closeFile() throws IOException {
	    closefd = I2C.i2cClose(fd);
		System.out.println("File closed\n") ;
		in.close() ;
	    
	    } // end closeFile
	
	public void WriteToI2CSensor(String specifiedsensor)
	{
		if (specifiedsensor == "Gyro")
		{	//To instantiate/enable the gyro..
													//0x6A
//			int writetoGyro = I2C.i2cWriteBytes(fd, devAdd, , size, offset, 0b01100111);
			
			//for less bytes(1), we can use WriteByte... more accessible
			
			 * Function to set current angle
			 * CurrentAngle = 0
			 * angle = CalculateAngle<- write-data
			 * SteerMotors till angle is reached
			 * |
			 *  --> while (currentAngle != angle)
			 *  {
			 *  steerMotors();
			 *  }
			 *  
			 * 
		}
		
		if (specifiedsensor == "Accel")
		{											 	//0xE1
		//	int writetoAccel = I2C.i2cWriteBytesDirect(fd, devAdd, size, offset, buff);
			//for less bytes(1), we can use WriteByte... more accessible
		}
	}
	
	public void ReadFromI2CSensor(String specifiedsensor)
	{
		if (specifiedsensor == "Gyro")
		{											//0x6A
		//	int readfromGyro = I2C.i2cReadBytesDirect(fd, devAdd, size, offset, buff);
			if (readfromGyro < 0)
			{
				System.out.println("Failed to read bytes from Gyro Sensor.");
			}
		}
		
		if (specifiedsensor == "Accel")
		{											 //0xE1							
		//	int readfromAccel = I2C.i2cReadBytesDirect(fd, devAdd, size, offset, buff);
			if (readfromAccel < 0)
			{
				System.out.println("Failed to read bytes from Acceleration Sensor.");
			}
		}
	}
	*/
}