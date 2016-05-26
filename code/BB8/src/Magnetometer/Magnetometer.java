package Magnetometer;

import com.pi4j.io.i2c.I2CDevice;

import Sensor.Sensor;

public class Magnetometer extends Sensor{
	private byte Adress;
	public static I2CDevice Magnetometer;
	
	public Magnetometer (byte NewAdress)
	{
		this.Adress = NewAdress;
	}
}
