package Sensor;
import java.io.*;
import java.nio.file.Files;

import com.pi4j.device.Device;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.jni.I2C;

import Gyro.Gyro;

//The default i2c addresses of BerryIMU are;

//0xE1 for the magnetometer and accelerometer
//0x6A for the gyroscope
//0x77 for the pressure sensor



public class OpenBus {
	
	private static I2CBus bus;
	//LSM9DS0 Gyro
	public final static int GYR_ADDRESS = 0x6A;
	//LSM9DS0 Gyro Registers
	private static final int CTRL_REG1_G = 0x20;
	private static final int CTRL_REG4_G = 0x23;
	private static final int OUT_X_L_G = 0x28;
	private static final int OUT_X_H_G = 0x29;
	
	private static byte[] bytes = new byte[6];
	
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
		Gyro.Gyro = bus.getDevice(GYR_ADDRESS);
		Gyro.Gyro.write(CTRL_REG1_G, (byte) 0b00001111); // Normal power mode, all axes enabled
		Gyro.Gyro.write(CTRL_REG4_G, (byte) 0b00110000); // Continuos update, 2000 dps full scal
		}
		catch(IOException e)
		{
			System.err.println(e.getMessage());
		}
		String specifiedsensor = "???";
		while (true) {
			int xlg = Gyro.Gyro.read(OUT_X_L_G, bytes, 0, 6);
			if(xlg < 6) 
			{
				System.out.println("Error reading data, < " + bytes.length + "bytes");
			}
		
		}
	}

	protected String filename;
	protected int fd;
	protected int closefd;
	
	public void openFile(String filename) throws IOException{
		this.filename = filename;
		fd = I2C.i2cOpen(filename);
		if (fd < 0) {
			throw new IOException("Cannot open file handle for " + filename + " got " + fd + " back.");
			}
		/*String line ;
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
		*/
		} // end openFile
	
	public void closeFile() throws IOException {
	    closefd = I2C.i2cClose(fd);
		System.out.println("File closed\n") ;
		/*in.close() ;*/
	    
	    } // end closeFile
	
	public void WriteToI2CSensor(String specifiedsensor)
	{
		if (specifiedsensor == "Gyro")
		{	//To instantiate/enable the gyro..
													//0x6A
			int writetoGyro = I2C.i2cWriteBytes(fd, devAdd, , size, offset, 0b01100111);
			
			//for less bytes(1), we can use WriteByte... more accessible
			
			/* Function to set current angle
			 * CurrentAngle = 0
			 * angle = CalculateAngle<- write-data
			 * SteerMotors till angle is reached
			 * |
			 *  --> while (currentAngle != angle)
			 *  {
			 *  steerMotors();
			 *  }
			 *  
			 * */
		}
		
		if (specifiedsensor == "Accel")
		{											 	//0xE1
			int writetoAccel = I2C.i2cWriteBytesDirect(fd, devAdd, size, offset, buff);
			//for less bytes(1), we can use WriteByte... more accessible
		}
	}
	
	public void ReadFromI2CSensor(String specifiedsensor)
	{
		if (specifiedsensor == "Gyro")
		{											//0x6A
			int readfromGyro = I2C.i2cReadBytesDirect(fd, devAdd, size, offset, buff);
			if (readfromGyro < 0)
			{
				System.out.println("Failed to read bytes from Gyro Sensor.");
			}
		}
		
		if (specifiedsensor == "Accel")
		{											 //0xE1							
			int readfromAccel = I2C.i2cReadBytesDirect(fd, devAdd, size, offset, buff);
			if (readfromAccel < 0)
			{
				System.out.println("Failed to read bytes from Acceleration Sensor.");
			}
		}
	}
}
