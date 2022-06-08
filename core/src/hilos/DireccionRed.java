package hilos;

import java.net.InetAddress;

public class DireccionRed {
	
	private InetAddress ip;
	private int puerto;
	private int nroCliente;
	
	public DireccionRed(InetAddress ip, int puerto, int nroCliente) {
		this.ip = ip;
		this.puerto = puerto;
		this.nroCliente = nroCliente;
	}

	public InetAddress getIp() {
		return ip;
	}

	public int getPuerto() {
		return puerto;
	}

	public int getNroCliente() {
		return nroCliente;
	}

	public void setNroCliente(int id){
		this.nroCliente = id;
	}
}
