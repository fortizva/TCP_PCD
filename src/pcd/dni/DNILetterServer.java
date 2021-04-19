package pcd.dni;

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

public class DNILetterServer implements Runnable {
	// Letras asociadas a resultado de DNI
	private final static String DNI_CHARS = "TRWAGMYFPDXBNJZSQVHLCKE";
	// Límite de conexiones (N<0 == Infinity)
	private final static int CONNECTIONS = 10;

	private static Ventana win;
	private static int port = 9875, counter;
	private static ServerSocket server;

	private int id;
	private Socket socket;

	public DNILetterServer(Socket socket, int id) {
		this.socket = socket;
		this.id = id;
	}

	public static void main(String[] args) {
		// Creamos una ventana para el terminal del servidor
		win = new Ventana("DNI Letter Server");
		win.addText("Se ha iniciado \"DNI Letter Server\"");
		win.addText("> Mostrando peticiones entrantes");
		counter = CONNECTIONS;

		try {
			// Abrimos el socket TCP
			server = new ServerSocket(port);
			win.addText("Escuchando el puerto " + server.getLocalPort() + "");
			while (counter > 0) {
				// Esperamos conexión
				Socket s = server.accept();

				// Conexión recibida, llamando thread
				counter--;
				Thread client = new Thread(new DNILetterServer(s, (CONNECTIONS - counter)));
				client.start();
			}

			// Detenemos la escucha
			win.addText("Se han realizado el máximo número de conexiones, finalizando listener...");
			server.close();
		} catch (SocketException e) {
			System.err.println("Error al abrir el socket: " + e);
			System.exit(-1);
		} catch (IOException e) {
			System.err.println("Error al transmitir datos: " + e);
			System.exit(-1);
		}

	}

	@Override
	public void run() {
		try {
			// Conexión recibida, creamos el flujo I/O
			win.addText("Conexión " + this.id + " entrante desde: " + socket.getInetAddress());
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// Espera recibir el DNI
			String lastDni = in.readLine();
			
			// Calcular letra correspondiente
			win.addText("[" + this.id + "] Recibida petición para: " + lastDni);
			try {
				char lastLetter = calculateLetter(Integer.parseInt(lastDni));
				win.addText("[" + this.id + "] Calculada letra \'" + lastLetter + "\'para " + lastDni);
				// Enviamos la letra
				out.println(lastLetter);
				out.flush();
			} catch (NumberFormatException e) {
				win.addText("[" + this.id + "] DNI \"" + lastDni + "\" inválido, forzando desconexión");
			} finally {
				// Terminamos la conexión
				win.addText("[" + this.id + "] Finalizando conexión...");
				socket.close();
			}
		} catch (IOException e) {
			win.addText("Error al transmitir datos a conexión " + this.id + " (Ver log)");
			System.err.println("Error al transmitir datos a conexión " + this.id + ": " + e);
		}
	}

	private static char calculateLetter(int DNI) {
		return DNI_CHARS.charAt(DNI % 23);
	}

}
