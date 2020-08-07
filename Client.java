import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		
		Socket socket = null;
		ObjectOutputStream outputStream = null;
		ObjectInputStream inputStream = null;
		int port = 9876;
		Scanner s = new Scanner(System.in);
		String currentWorkingDirectory = "myftp>";
		socket = new Socket("127.0.0.1", port);		
		String input = "";			
	
		outputStream = new  ObjectOutputStream(socket.getOutputStream());
		inputStream = new ObjectInputStream(socket.getInputStream());
		while(!input.equals("quit")) {
			System.out.print(currentWorkingDirectory);
			input = s.nextLine();
			String[] commandAndValue = input.split(" ");
				
			switch(commandAndValue[0]) {
			
				case "get"://client receidves file from server
					outputStream.writeObject(input);
					byte[] arr = new byte[inputStream.readInt()];
					
					FileOutputStream fos = new FileOutputStream(commandAndValue[1]);
					BufferedOutputStream bos = new BufferedOutputStream(fos);
					
					inputStream.read(arr,0,arr.length);
					bos.write(arr,0,arr.length);
				
					
					bos.close();
					fos.close();
					break;
		
				case "put"://client sends file to server
					outputStream.writeObject(input);
					File myfile = new File(commandAndValue[1]);
					arr = new byte[(int)myfile.length()];
					BufferedInputStream br = new BufferedInputStream(new FileInputStream(myfile));
					br.read(arr,0,arr.length);
					outputStream.writeInt((int)myfile.length());
					outputStream.write(arr,0,arr.length);	
					
					br.close();
					outputStream.flush();
					break;
	
				default:
					outputStream.writeObject(input);
					String message = (String) inputStream.readObject();
					System.out.println(message);
					if(message.equals("quit")) {
						System.out.println("GoodBye!");
						socket.close();
					}
			}
					
		}
			outputStream.close();
			inputStream.close();
			Thread.sleep(1);
		
			
		
		
	}

}
