package client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;

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
	
	public static boolean checkLogin(String username, String pass) throws IOException {
		BufferedReader bin = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
		String line;
		line = bin.readLine();
		while((line = bin.readLine()) != null) {
			String[] splitedLine = line.split(",");
			if(username.equals(splitedLine[0])) {
				if(pass.equals(splitedLine[1])) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static String encodeFileToBase64Binary(File file) throws IOException {
	    byte[] encoded = org.apache.commons.codec.binary.Base64.encodeBase64(FileUtils.readFileToByteArray(file));
	    return new String(encoded, StandardCharsets.US_ASCII);
	}
}
