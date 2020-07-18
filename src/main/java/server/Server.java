package server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server {
	public static void main(String[] args) {
		System.out.println("hello");
		int port = 3006;
		try {
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(port);
			while(true) {
				final Socket clientSocket = serverSocket.accept();
				ServerWorker serverWorker = new ServerWorker(clientSocket);
				serverWorker.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
