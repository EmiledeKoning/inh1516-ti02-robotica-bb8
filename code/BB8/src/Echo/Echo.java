package Echo;

import Sensor.Sensor;

public class Echo extends Sensor{
	private byte Adress;

	public Echo (byte NewAdress)
	{
		this.Adress = NewAdress;
	}
}
