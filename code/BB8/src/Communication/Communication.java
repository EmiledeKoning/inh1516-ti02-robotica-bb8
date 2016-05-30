package Communication;

import Robot.Robot;

public class Communication {
	private byte[] PI_ADDR;
	private int PI_PORT;
	private byte[] PYTHON_ADDR;
	private int PYTHON_PORT;
	private Robot BB8;
	
	public Communication(byte[] new_addr, int new_port, Robot newBB8)
	{
		this.BB8 = newBB8;
		this.PI_ADDR = new_addr;
		this.PI_PORT = new_port;
	}
	public Communication(byte[] new_pi_addr, int new_pi_port, byte[] new_python_addr, int new_python_port, Robot newBB8)
	{
		this.BB8 = newBB8;
		this.PI_ADDR = new_pi_addr;
		this.PI_PORT = new_pi_port;
		this.PYTHON_ADDR = new_python_addr;
		this.PYTHON_PORT = new_python_port;
	}
	public boolean newMessage(String message){
		if(true){return true;}//bla bla message sent successfull... ==true
		return false;//blabla message NOT send successfull.... ==False
	}
	private void Listner()
	{
		//TODO:hier luissteren naar inkomender berichten van de andere pi en/off het python programma
		//TODO:Iets met een switchcase.... messare bevat X, zoek de uitvoerende code op
	}
}
