package Gyro;

import Sensor.Sensor;

public class Gyro extends Sensor{
	private byte Adress;

	public Gyro (byte NewAdress)
	{
		this.Adress = NewAdress;
	}
}
