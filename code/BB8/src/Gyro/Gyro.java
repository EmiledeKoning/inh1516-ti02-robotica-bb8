package Gyro;

import com.pi4j.io.i2c.I2CDevice;

import Sensor.Sensor;

public class Gyro extends Sensor{
	private byte Address;
	public static I2CDevice Gyro;
	
	public Gyro(byte newAddress) 
	{
		this.Address = newAddress;
	}
}
