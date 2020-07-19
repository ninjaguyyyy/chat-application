package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {
	private int serverPort;
	private ArrayList<ServerWorker> workers = new ArrayList<ServerWorker>();

	public List<ServerWorker> getWorkers() {
		return workers;
	}

	public Server(int port) {
		this.serverPort = port;
	}
	
	@Override
	public void run() {
		try {
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(serverPort);
			while(true) {
				final Socket clientSocket = serverSocket.accept();
				ServerWorker serverWorker = new ServerWorker(this, clientSocket);
				workers.add(serverWorker);
				serverWorker.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removeWorker(ServerWorker serverWorker) {
		workers.remove(serverWorker);
		
	}
}
