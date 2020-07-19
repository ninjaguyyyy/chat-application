package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class ServerWorker extends Thread {

	private final Socket clientSocket;
	private String login;
	private final Server server;
	private OutputStream out;
	private HashSet<String> topics = new HashSet<String>();

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
	// send for chi: msg <user> text...
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
					
				} else if("msg".equalsIgnoreCase(cmd)){
					String[] tokenMsg = StringUtils.split(line, null, 3);
					handleMessage(tokenMsg);
					
				} else if ("join".equalsIgnoreCase(cmd)) {
					handleJoin(tokens);
					
				} else if ("leave".equalsIgnoreCase(cmd)) {
					handleLeave(tokens);
					
				} else {
					String msg = "unknown " + cmd + "\n";
					out.write(msg.getBytes());
				}
			}
			
		}
		
		clientSocket.close();
	}
	
	private void handleLeave(String[] tokens) {
		if(tokens.length > 1) {
			String topic = tokens[1];
			topics.add(topic);
		}
	}

	public boolean isMemberOfTopic(String topic) {
		return topics.contains(topic);
	}
	
	private void handleJoin(String[] tokens) {
		if(tokens.length > 1) {
			String topic = tokens[1];
			topics.add(topic);
		}
		
	}

	// format: msg <user> body...
	// format: msg #<topic> body...
	private void handleMessage(String[] tokens) throws IOException {
		String sendTo = tokens[1];
		String body = tokens[2];
		
		boolean isTopic = sendTo.charAt(0) == '#';
		
		List<ServerWorker> workers = server.getWorkers();
		
		String onlineMsg = "offline " + login + "\n";
		for(ServerWorker worker: workers) {
			if(isTopic) {
				if(worker.isMemberOfTopic(sendTo)) {
					String outMsg = "msg " + sendTo +": " + login + " " + body + "\n";
					worker.send(outMsg);
				}
			} else {
				if(sendTo.equalsIgnoreCase(worker.getLogin())) {
					String outMsg = "msg " + login + " " + body + "\n";
					worker.send(outMsg);
				}
			}
			
		}
	}

	private void handleLogoff() throws IOException {
		server.removeWorker(this);
		List<ServerWorker> workers = server.getWorkers();
		// send: current user logout -> all user
		String onlineMsg = "offline " + login + "\n";
		for(ServerWorker worker: workers) {
			if(!login.equals(worker.getLogin())) {
				worker.send(onlineMsg);
			}
			
		}
		clientSocket.close();
		
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
