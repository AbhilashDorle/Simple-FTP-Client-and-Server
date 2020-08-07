import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private static ServerSocket server;
	
	private static int port = 9876;
	
	 public static void main(String args[]) throws IOException, ClassNotFoundException {
		 
		 
		 server = new ServerSocket(port);//server
		 Socket socket = null;
		 int count = 0;
		 while(true) {
			 count++;
			 
			 System.out.println("Waiting for client request");
			 socket = server.accept();
			 System.out.println("Client" + count +"connected");
			 ThreadHandler thread = new ThreadHandler(socket,count);
			 thread.start();
			
		 }
	 }
}
