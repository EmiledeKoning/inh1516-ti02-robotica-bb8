package Temperature;

import Sensor.Sensor;

public class Temperature extends Sensor{
	private byte Adress;

	public Temperature (byte NewAdress)
	{
		this.Adress = NewAdress;
	}
}
