import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
	public DataOutputStream out = null;
	public PrintWriter output;
	public BufferedReader input;
	public String name = "";
	public Socket socket = null;
	
	public Client(String host, int port)
	{
		try {
			socket = new Socket(host, port);
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream(), true);
			ClientThread clientThread = new ClientThread(socket);

			clientThread.start();
			
			Scanner scanner = new Scanner(System.in);

            if (name.equals("")) {
                System.out.println("Enter your name: ");
			    this.name = scanner.nextLine();
                scanner.close();
            }

			scanner.close();
		} catch (Exception e) {
			System.out.println("Exception occured in client: " + e.getStackTrace());
		}
	}
}
