package servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import cliente.Cliente;

public class Servidor extends Thread {
	ArrayList<String> users;
	int puerto, total = 0;
	ServerSocket serverSocket;
	ArrayList<Socket> sockets;
	
	public Servidor(String puerto) throws UnknownHostException, IOException {
		serverSocket = new ServerSocket(Integer.parseInt(puerto));
		sockets = new ArrayList<Socket>();
		users = new ArrayList<String>();
	}
	
	@Override
	public void run() {
		System.out.println("--El servidor escucha");
		
		while(true) {
			try {
				sockets.add(serverSocket.accept());
				System.out.println("--Llegó un cliente!");
				total = sockets.size();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//Quiero guardar el usuario
			Runnable conocer = new Runnable() {
				
				@Override
				public void run() {
					try {
						DataInputStream dataIn = new DataInputStream( sockets.get(total-1).getInputStream());
			        	users.add(dataIn.readUTF());
			        	System.out.println("--Nuevo cliente: "+users.get(total-1));
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			};
			conocer.run();
				
			try {
				sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("--Complete un ciclo de escuha de cliente");
		}
	       
	}
	
	public void enviar(String usr, String texto) throws IOException {
		for(Socket s : sockets)
			new DataOutputStream(s.getOutputStream()).writeUTF(usr+": "+texto);
		System.out.println("--envie de "+usr+": "+texto);
	}
	
	public static void main(String[] args) {
		String puerto;
		Scanner sc = new Scanner(System.in);
		Servidor servidor = null;

		do {
			System.out.print("Ingrese número de puerto: ");
			puerto = sc.next();
			
			try {
				servidor = new Servidor(puerto);
			} catch (UnknownHostException e) {
				System.out.println("Error, no se encontró el host");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("Error de E/S");
				e.printStackTrace();
			}
		}while(servidor==null);
		
		servidor.start();
		
		Socket s;
		while(true) {
			for(int i = 0;i<servidor.sockets.size();i++) {
				s = servidor.sockets.get(i);
				try {
					DataInputStream dIn = new DataInputStream(s.getInputStream());
					if(dIn.available()>0) {
						System.out.println("Hay mensaje de "+servidor.users.get(i));
						servidor.enviar(servidor.users.get(i), dIn.readUTF());
					}
				} catch (IOException e) {
					System.out.println("Error de E/S leyendo a: "+servidor.users.get(i));
					e.printStackTrace();
				}
			}
		}
	}
	
}
