package server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ServerMain {
	public static void main(String[] args) {
		System.out.println("hello");
		int port = 3006;
		Server server = new Server(port);
		server.start();
	}
	
	
}
