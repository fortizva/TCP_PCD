package pcd.dni;

import java.io.*;
import java.net.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import pcd.util.Ventana;
import java.util.Scanner;

public class DNILetterClient {
	
	private int id;
	private Socket socket;

	public DNILetterServer(Socket socket, int id) {
		this.socket = socket;
		this.id = id;
	}

	public static void main(String[] args) {
		 
		String msg;
		Ventana v = new Ventana("DNI Letter Client"); //Cliente y servidor con ventanas independientes
		v.addText("Se ha iniciado \"DNI Letter Client\"");
		v.addText("> Esperando Números");
		
		Socket connection; // Socket para la comunicaci�n con el servidor. Hay un socket (enchufe) en cada uno de los extremos
		String host = "localhost"; // Identificador del host donde se ejecuta el servidor:  IP (p.e., 158.49.98.31)
		int port=9875; // Puerto por el que escucha el servidor
		//Solicitamos el DNI
		BufferedReader incoming;	
		Scanner sc = new Scanner(System.in);
        int numero;
        System.out.print("Por favor, ingrese su numero de DNI: ");
        numero = sc.nextInt();
        String DNInum = sc.nextLine();
        System.out.println("Su número es: " + DNInum);//Numeros puestos como string pero se puede quitar para dejarlo como int
		
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			v.addText("Conectando a " + host + " por el puerto " + port);	
			connection = new Socket(host, port); 
			v.addText("          ........... conectado... a " + connection.getInetAddress().getHostName());
		

		// Envío del DNI
			out.println(DNInum);
			out.flush();
		// Lectura de la letra del servidor
			msg=in.readLine();
			while (msg !=null) {
				v.addText(msg);
				msg = in.readLine();
			}
			v.addText("end");
			String letraDNI = in.readLine();
			System.out.println("Su letra es:" + letraDNI);
		// Cerramos el socket
			connection.close();
		} catch (IOException e) {
			System.err.println("Error de lectura del socket : " + e);
			System.exit(-1);
		}

		
	}

}