package pcd.dni;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class DNILetterClient {

	private static final String HOST = "localhost";
	private static final int PORT = 9875;

	private static Socket socket;

	public static void main(String[] args) {
		System.out.println("Se ha iniciado \"DNI Letter Client\"");
		System.out.println("> Esperando números");

		// Solicitamos el DNI
		int numero = -1;
		Scanner sc = new Scanner(System.in);
		System.out.println("Por favor, ingrese su numero de DNI: ");
		boolean nextInt = sc.hasNextInt();
		while (!nextInt || numero < 0) {
			System.out.println("La entrada no es un número válido");
			System.out.println("Por favor, ingrese su número de DNI: ");
			sc.nextLine();
			nextInt = sc.hasNextInt();
			if (nextInt)
				numero = sc.nextInt();
		}
		System.out.println("Su número es: " + numero);
		sc.close();

		try {
			// Conectamos con el server
			System.out.println("Conectando a " + HOST + " por el puerto " + PORT);
			socket = new Socket(HOST, PORT);
			// Creamos el flujo I/O
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println("Conectado a " + socket.getInetAddress().getHostName());

			// Envío del DNI
			out.println(numero);
			out.flush();

			// Lectura de la letra del servidor
			String msg = in.readLine();
			System.out.println("Su letra es: " + msg);
			System.out.println("Terminando conexión...");

			// Cerramos el socket
			socket.close();
			System.out.println("Fin de programa");

		} catch (IOException e) {
			System.err.println("Error de lectura del socket : " + e);
			System.exit(-1);
		}

	}

}