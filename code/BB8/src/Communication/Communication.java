package Communication;

public class Communication {
	private byte[] PI_ADDR;
	private int PI_PORT;
	private byte[] PYTHON_ADDR;
	private int PYTHON_PORT;
	
	public void Communication(byte[] new_addr, int new_port)
	{
		this.PI_ADDR = new_addr;
		this.PI_PORT = new_port;
	}
	public void Communication(byte[] new_pi_addr, int new_pi_port, byte[] new_python_addr, int new_python_port)
	{
		this.PI_ADDR = new_pi_addr;
		this.PI_PORT = new_pi_port;
		this.PYTHON_ADDR = new_python_addr;
		this.PYTHON_PORT = new_python_port;
	}
	public boolean Message(char[] message){
		
		return false;
	}
}
