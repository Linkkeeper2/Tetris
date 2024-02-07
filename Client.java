// A Java program for a ClientSide
import java.io.*;
import java.net.*;

public class Client {
	// initialize socket and input output streams
	private Socket socket = null;
	private DataInputStream input = null;
	public DataOutputStream out = null;

	// constructor to put ip address and port
	public Client(String address, int port)
	{

		// establish a connection
		try {
			socket = new Socket(address, port);

			System.out.println("Connected");

			// takes input from terminal
			input = new DataInputStream(System.in);

			// sends output to the socket
			out = new DataOutputStream(
				socket.getOutputStream());
		}

		catch (UnknownHostException u) {
			System.out.println(u);
		}

		catch (IOException i) {
			System.out.println(i);
		}
	}

	public void close() {
		// close the connection
		try {

			input.close();

			out.close();

			socket.close();
		}

		catch (IOException i) {

			System.out.println(i);
		}
	}
}

