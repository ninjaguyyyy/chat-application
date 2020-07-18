package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class ServerWorker extends Thread {

	private Socket clientSocket;

	public ServerWorker(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	
	@Override
	public void run() {
		try {
			handleConnectThread();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void handleConnectThread() throws IOException, InterruptedException {
		InputStream in = clientSocket.getInputStream();
		OutputStream out = clientSocket.getOutputStream();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		
		while((line = reader.readLine()) != null) {
			String[] tokens = StringUtils.split(line);
			if(tokens != null && tokens.length > 0) {
				String cmd = tokens[0];
				if("quit".equalsIgnoreCase(cmd)) {
					break;
				} else {
					String msg = "unknown " + cmd + "\n";
					out.write(msg.getBytes());
				}
			}
			
		}
		
		clientSocket.close();
	}

}
