package client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

public class Client {
	private String serverName;
	private int serverPort;
	private Socket socket;
	private InputStream serverIn;
	private OutputStream serverOut;
	private BufferedReader bufferdIn;
	private ArrayList<UserStatusListener> userStatusListeners = new ArrayList<UserStatusListener>();
	private ArrayList<MessageListener> messageListeners = new ArrayList<MessageListener>();
	
	public Client(String serverName, int serverPort) {
		super();
		this.serverName = serverName;
		this.serverPort = serverPort;
	}
	
	public static void main(String[] args) throws IOException {
	}

	public void msg(String sendTo, String body) throws IOException {
		String cmd = "msg " + sendTo + " " + body + "\n";
		serverOut.write(cmd.getBytes());
	}

	public void logout() throws IOException {
		String cmd = "logout\n";
		serverOut.write(cmd.getBytes());
	}

	public void startMessageReader() {
		Thread t = new Thread() {
			@Override
			public void run() {
				readMessageLoop();
			}
		};
		t.start();
	}
	
	public void readMessageLoop() {
		String line;
		try {
			while((line = bufferdIn.readLine()) != null) {
				String[] tokens = StringUtils.split(line);
				if(tokens != null && tokens.length > 0) {
					String cmd = tokens[0];
					if("online".equalsIgnoreCase(cmd)) {
						handleOnline(tokens);
					} else if("offline".equalsIgnoreCase(cmd)) {
						handleOffline(tokens);
					} else if("msg".equalsIgnoreCase(cmd)) {
						String[] tokenMsg = StringUtils.split(line, null, 3);
						handleMessage(tokenMsg);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	};
	
	public void handleMessage(String[] tokenMsg) {
		String username = tokenMsg[1];
		String msgBody = tokenMsg[2];
		
		for(MessageListener listener: messageListeners) {
			listener.onMessage(username, msgBody);
		}
	}

	public void handleOnline(String[] tokens) {
		String username = tokens[1];
		for(UserStatusListener listener: userStatusListeners) {
			listener.online(username);
		}
	}
	
	public void handleOffline(String[] tokens) {
		String username = tokens[1];
		for(UserStatusListener listener: userStatusListeners) {
			listener.offline(username);
		}
		
	}

	public boolean login(String username, String pass) throws IOException {
		String cmd = "login " + username + " " + pass + "\n";
		serverOut.write(cmd.getBytes());
		
		String res = bufferdIn.readLine();
		
		if("login ok".equalsIgnoreCase(res)) {
			startMessageReader();
			return true;
		} else {
			return false;
		}
	}

	public boolean connect() {
		try {
			this.socket = new Socket(serverName, serverPort);
			this.serverIn = socket.getInputStream();
			this.serverOut = socket.getOutputStream();
			this.bufferdIn = new BufferedReader(new InputStreamReader(serverIn));
			return true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void addUserStatusListener(UserStatusListener userStatusListener) {
		userStatusListeners.add(userStatusListener);
	}
	
	public void removeUserStatusListener(UserStatusListener userStatusListener) {
		userStatusListeners.remove(userStatusListener);
	}
	
	public void addMessageListener(MessageListener messageListener) {
		messageListeners.add(messageListener);
	}
	
	public void removeMessageListener(MessageListener messageListener) {
		messageListeners.remove(messageListener);
	}
}
