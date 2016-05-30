package Robot;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import Accelerometer.Accelerometer;
import Altitude.Altitude;
import Body.Body;
import Communication.Communication;
import DC_Motor.DC_Motor;
import Echo.Echo;
import Gyro.Gyro;
import Head.Head;
import Magnetometer.Magnetometer;
import Stepper_Motor.Stepper_Motor;
import Temperature.Temperature;

public class Robot {
	private Gyro _gyroSensor;
	private Accelerometer _AccelerometerSensor;
	private Altitude _altitudeSensor;
	private Magnetometer _magnetometerSensor;
	private Temperature _temperatureSensor;
	private Head _BB8_Head;
	private Body _BB8_Body;
	private Communication _communication; 
	private DC_Motor _X_Motor;
	private DC_Motor _Y_Motor;
	private Stepper_Motor _headSpin;
	private Stepper_Motor _bodySpin;
	private Stepper_Motor _bodyTilt;
	private Robot _BB8;

	public static void main(String[] args) throws UnknownHostException{
		Robot BB8 = new Robot();
		BB8.init(BB8);
	}

	private void init(Robot newBB8) throws UnknownHostException{
		this._BB8 = newBB8; 
		InetAddress addr = InetAddress.getLocalHost();
		if (addr.getHostName() == "BB8_Head"){
			Init_Head();
		}
		else if (addr.getHostName() == "BB8_Body"){
			Init_Body();
		}
	}
	private void Init_Head(){
		_BB8_Head = new Head(_BB8);
		_communication = new Communication(_BB8) 
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream("/BB8/BB8.config");

			// load a properties file
			prop.load(input);

			// get the address value of each sensor from the config file and create a instance of each sensor
			this._gyroSensor = new Gyro(Byte.parseByte(prop.getProperty("Gyro_Addr")));
			this._AccelerometerSensor = new Accelerometer(Byte.parseByte(prop.getProperty("Acc_Addr")));
			this._altitudeSensor = new Altitude(Byte.parseByte(prop.getProperty("Alt_Addr")));
			// NOT A I2C thing!! Echo EchoSensor = new Echo(Byte.parseByte(prop.getProperty("Echo_Addr")));
			this._magnetometerSensor = new Magnetometer(Byte.parseByte(prop.getProperty("Magn_Addr")));
			this._temperatureSensor = new Temperature(Byte.parseByte(prop.getProperty("Temp_Addr")));
	
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
	public Gyro gyroSensor(){
		return this._gyroSensor;
	}
	public Accelerometer AccelerometerSensor(){
		return this._AccelerometerSensor;
	}
	public Altitude altitudeSensor(){
		return this._altitudeSensor;
	}
	public Magnetometer magnetometerSensor(){
		return this._magnetometerSensor;
	}
	public Temperature temperatureSensor(){
		return this._temperatureSensor;
	}
	public Head BB8_Head(){
		return this._BB8_Head;
	}
	public Body BB8_Body(){
		return this._BB8_Body;
	}
	public Communication communication(){
		return this._communication;
	} 
	public DC_Motor X_Motor(){
		return this._X_Motor;
	}
	public DC_Motor Y_Motor(){
		return this._Y_Motor;
	}
	public Stepper_Motor headSpin(){
		return this._headSpin;
	}
	public Stepper_Motor bodySpin(){
		return this._bodySpin;
	}
	public Stepper_Motor bodyTilt(){
		return this._bodyTilt;
	}
}
