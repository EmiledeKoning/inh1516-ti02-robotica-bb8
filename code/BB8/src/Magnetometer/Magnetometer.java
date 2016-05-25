package Magnetometer;

import Sensor.Sensor;

public class Magnetometer extends Sensor{
	private byte Adress;

	public Magnetometer (byte NewAdress)
	{
		this.Adress = NewAdress;
	}
}
