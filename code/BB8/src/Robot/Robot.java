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
	private Gyro gyroSensor;
	private Acceleration accelerationSensor;
	private Altitude altitudeSensor;
	private Magnetometer magnetometerSensor;
	private Temperature temperatureSensor;

	public static void main(String[] args) throws UnknownHostException{
		Robot BB8 = new Robot();
		BB8.init();
	}

	private void init() throws UnknownHostException{
		InetAddress addr = InetAddress.getLocalHost();
		if (addr.getHostName() == "BB8_Head"){
			Init_Head();
		}
		else if (addr.getHostName() == "BB8_Body"){
			Init_Body();
		}
	}
	private void Init_Head(){
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream("/BB8/BB8.config");

			// load a properties file
			prop.load(input);

			// get the address value of each sensor from the config file and create a instance of each sensor
			this.gyroSensor = new Gyro(Byte.parseByte(prop.getProperty("Gyro_Addr")));
			this.accelerationSensor = new Acceleration(Byte.parseByte(prop.getProperty("Acc_Addr")));
			this.altitudeSensor = new Altitude(Byte.parseByte(prop.getProperty("Alt_Addr")));
			// NOT A I2C thing!! Echo EchoSensor = new Echo(Byte.parseByte(prop.getProperty("Echo_Addr")));
			this.magnetometerSensor = new Magnetometer(Byte.parseByte(prop.getProperty("Magn_Addr")));
			this.temperatureSensor = new Temperature(Byte.parseByte(prop.getProperty("Temp_Addr")));

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
	}
	
	public static void Shutdown() throws IOException{
		Runtime.getRuntime().exec("sudo shutdown now");
	}
	public static void Reboot() throws IOException{
		Runtime.getRuntime().exec("sudo reboot");
	}
	public Gyro get_gyroSensor(){
		return this.gyroSensor;
	}
	public Acceleration get_accelerationSensor(){
		return this.accelerationSensor;
	}
	public Altitude get_altitudeSensor(){
		return this.altitudeSensor;
	}
	public Magnetometer get_magnetometerSensor(){
		return this.magnetometerSensor;
	}
	public Temperature get_temperatureSensor(){
		return this.temperatureSensor;
	}
}
