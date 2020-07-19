package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class ServerWorker extends Thread {

	private final Socket clientSocket;
	private String login;
	private final Server server;
	private OutputStream out;

	public ServerWorker(Server server, Socket clientSocket) {
		this.server = server;
		this.clientSocket = clientSocket;
	}
	
	@Override
	public void run() {
		try {
			handleConnectThread();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// login for cmd: login <user> <pass>
	private void handleConnectThread() throws IOException, InterruptedException {
		InputStream in = clientSocket.getInputStream();
		out = clientSocket.getOutputStream();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		
		while((line = reader.readLine()) != null) {
			String[] tokens = StringUtils.split(line);
			if(tokens != null && tokens.length > 0) {
				String cmd = tokens[0];
				if("quit".equalsIgnoreCase(cmd) || "logout".equalsIgnoreCase(cmd)) {
					handleLogoff();
					break;
				} else if("login".equalsIgnoreCase(cmd)) {
					handleLogin(out, tokens);
					
				} else {
					String msg = "unknown " + cmd + "\n";
					out.write(msg.getBytes());
				}
			}
			
		}
		
		clientSocket.close();
	}
	
	private void handleLogoff() throws IOException {
		List<ServerWorker> workers = server.getWorkers();
		// send: current user logout -> all user
		String onlineMsg = "offline " + login + "\n";
		for(ServerWorker worker: workers) {
			if(!login.equals(worker.getLogin())) {
				worker.send(onlineMsg);
			}
			
		}
//		clientSocket.close();
		
	}

	public String getLogin() {
		return login;
	}
	
	private void handleLogin(OutputStream out, String[] tokens) throws IOException {
		if(tokens.length == 3) {
			String login = tokens[1];
			String pass = tokens[2];
			if((login.equals("guest") && pass.equals("guest")) || (login.equals("chi") && pass.equals("chi"))) {
				String msg = "login ok \n";
				out.write(msg.getBytes());
				this.login = login;
				System.out.println("Chuoi login: " + login);
				
				
				List<ServerWorker> workers = server.getWorkers();
				
				// send: all online -> current user
				for(ServerWorker worker: workers) {
					if(worker.getLogin() != null) {
						if(!login.equals(worker.getLogin())) {
							String msg2 = "online " + worker.getLogin() + "\n";
							send(msg2);
						}
						
					}
					
				}
				
				// send: current user -> all online
				String onlineMsg = "online " + login + "\n";
				for(ServerWorker worker: workers) {
					if(!login.equals(worker.getLogin())) {
						worker.send(onlineMsg);
					}
					
				}
				
			} else {
				String msg = "login error";
				out.write(msg.getBytes());
			}
		}
	}

	private void send(String msg) throws IOException {
		if(login != null) {
			out.write(msg.getBytes());
		}
	}

}
