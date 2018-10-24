package servidor;

import java.net.Socket;

public class Usuario extends Thread {
	String name;
	Socket socket;
	
	public Usuario(String name, Socket socket) {
		this.name = name;
		this.socket = socket;
	}
	
	@Override
	public void run() {
		//gestionar la interaccion con el servidor
		//recibir y reenviar al servidor.
		
	}

}
