package client;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class FileHandle {
	private static PrintStream printStream;
	private static String filename = "users.txt";
	
	public FileHandle() {
		
	}
	
	public static void writeUser(String username, String pass) throws FileNotFoundException {
		printStream = new PrintStream(new FileOutputStream(filename, true));
		printStream.append(username + "," + pass + "\n");

        printStream.flush();
        printStream.close();
	}
}
