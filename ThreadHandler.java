import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class ThreadHandler extends Thread {
	 Socket socket;
	 int clientNo;	
	 String workingDirectory;
	public ThreadHandler(Socket inSocket, int count) throws ClassNotFoundException, IOException {
		socket = inSocket;
		clientNo = count;
		workingDirectory = System.getProperty("user.dir");//curemt workimg dirctory
	}
	@Override
	public void run() {
		// TODO Auto-generated constructor stub
			try {
				ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
				
			while(true) {
				//get command(request) from client
				String command = (String) inputStream.readObject();
		
				String[] commandAndValue = command.split(" ");
				if(commandAndValue[0].equals("quit")) {
					
					 outputStream.writeObject("quit");
					 inputStream.close();
					 outputStream.close();
					 socket.close();
					 break;
				}
					
				System.out.println("command recieved - > at client no ->"+clientNo + " - > " + command);
				
			
				workingDirectory.concat("/");
				switch(commandAndValue[0]) {
				
				case "get" : //Server send file to client
					File myfile = new File(workingDirectory+"/"+commandAndValue[1]);
					byte[] arr = new byte[(int)myfile.length()];
					
					BufferedInputStream br = new BufferedInputStream(new FileInputStream(myfile));
					br.read(arr,0,arr.length);
					System.out.println(arr.length);
					outputStream.writeInt((int)myfile.length());
					outputStream.write(arr,0,arr.length);	
					
					outputStream.flush();
					br.close();
					break;
					
				case "put" ://Server recieves file from client
					System.out.println("wait1");
					arr = new byte[inputStream.readInt()];
					
					FileOutputStream fos = new FileOutputStream(commandAndValue[1]);
					BufferedOutputStream bos = new BufferedOutputStream(fos);
					
					inputStream.read(arr,0,arr.length);
					bos.write(arr,0,arr.length);
					bos.close();
					fos.close();
					break;
				
				case "delete":
					 File file = new File(commandAndValue[1]);
				        if(file.delete()){
				        	outputStream.writeObject(commandAndValue[1]+ " File deleted");
				        }else 
				        	outputStream.writeObject("File" + commandAndValue[1] + "doesn't exists in project root directory");
					break;
					
				case "ls":
					
					File folder = new File(workingDirectory);
					File[] listOfFiles = folder.listFiles();
					String list = "";
					for (int i = 0; i < listOfFiles.length; i++) {
					  
					  list = list + listOfFiles[i].getName() + "\n";
				
					}
					outputStream.writeObject(list);
					break;
				
				case "cd":
						if(commandAndValue[1].equals(".."))
						{
            System.out.println("Inside cd..");
							int index = workingDirectory.lastIndexOf('/');
						    workingDirectory = workingDirectory.substring(0,index);
						    outputStream.writeObject("changing working directory");
						}
						else{
							workingDirectory = workingDirectory + "/" + commandAndValue[1];
							outputStream.writeObject("changing working directory");
						}
						
					break;
				case "mkdir":
					
					boolean fileCreated = new File(workingDirectory + "/" + commandAndValue[1]).mkdirs();
					if(fileCreated)
						outputStream.writeObject("Directory "+ commandAndValue[1] +" created successfully");
					else
						outputStream.writeObject("Opps! Directory cannot be created. May be directory name already exists");
					break;
				
				case "quit":
					 break;
					 
				case "pwd":
					outputStream.writeObject(workingDirectory);
					break;
				default:
					outputStream.writeObject("-bash: "+ command +" : command not found");
				
				}
				 
			
			}
			
			 
			
		
		}
		catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
