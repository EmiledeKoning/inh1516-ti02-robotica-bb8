package Accelerometer;

import com.pi4j.io.i2c.I2CDevice;

import Sensor.Sensor; 

public class Accelerometer extends Sensor{
	private byte Address;
	public static I2CDevice Accelerometer;
	
	public Accelerometer(byte newAddress) 
	{
		this.Address = newAddress;
	}

}
