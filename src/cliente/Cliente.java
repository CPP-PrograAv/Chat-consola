package cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente extends Thread{
	String user,ip;
	int puerto;
	Socket socket;
	DataInputStream entrada;
	DataOutputStream salida;
	
	public Cliente(String user, String ip, String puerto) throws UnknownHostException, IOException {
		this.user = user;
		this.ip = ip;
		this.puerto = Integer.parseInt(puerto);
		socket = new Socket(ip,this.puerto);
		entrada = new DataInputStream( socket.getInputStream());
		salida = new DataOutputStream( socket.getOutputStream());
		
	}
	
	@Override
	public void run() {
		System.out.println("++El cliente escucha");
		while(true) {
			try {
				System.out.println(entrada.readUTF());
			} catch (IOException e) {
				System.out.println("Error leyendo el mensaje");
				e.printStackTrace();
			}
		}
	       
	}
	
	public void enviar(String texto) throws IOException {
		salida.writeUTF(texto);
	}
	
	public static void main(String[] args) {
		String user,ip,puerto;
		Scanner sc = new Scanner(System.in);
		Cliente cliente = null;
		
		System.out.print("Ingrese nombre de usuario: ");
		user = sc.next();
		do {
			System.out.print("Ingrese dirección ip: ");
			ip = sc.next();
			System.out.print("Ingrese número de puerto: ");
			puerto = sc.next();
			
			
			try {
				cliente = new Cliente(user,ip,puerto);
			} catch (UnknownHostException e) {
				System.out.println("Error, no se encontró la dirección");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("Error de E/S");
				e.printStackTrace();
			}
		}while(cliente==null);
		
		cliente.start();
		
		while(true) {
			try {
				System.out.println("Mensaje a enviar: ");
				cliente.enviar(sc.next());
			} catch (IOException e) {
				System.out.println("Error al enviar el mensaje");
				e.printStackTrace();
			}
			try {
				sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
