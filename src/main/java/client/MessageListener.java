package client;

public interface MessageListener {
	public void onMessage(String fromUsername, String msgBody);
}
