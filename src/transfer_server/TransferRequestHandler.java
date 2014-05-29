package transfer_server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;

public class TransferRequestHandler implements Runnable{
		private Socket _socket = null;
		private DataOutputStream dout = null;
		private DataInputStream din = null;
 
		public TransferRequestHandler(Socket socket) {
			_socket = socket;
		}
 
		public void run() {
			System.out.println("Client connected to socket: " + _socket.toString());
 
			try {
				din=new DataInputStream(_socket.getInputStream());
				dout=new DataOutputStream(_socket.getOutputStream());
 
				System.out.println("FTP Client Connected ...");
				while(true)
				{
					try
					{
					System.out.println("Waiting for Command Server...");
					String Command=din.readUTF();
					if(Command.compareTo("GET")==0)
					{
						System.out.println("\tGET Command Received ...");
						SendFile();
						break;
					}
					else if(Command.compareTo("DISCONNECT")==0)
					{
						System.out.println("\tDisconnect Command Received ...");
						System.exit(1);
					}
					}
					catch(Exception ex)
					{
						break;
					}
				}
				
			}
			catch(Exception ex)
			{
			}		
		}
 
		
		void SendFile() throws Exception
		{		
			String filename=din.readUTF();
			String file_path = "C:\\Users\\Alberto\\workspace\\Transfer_server\\Content\\"+filename;
			File f=new File(file_path);			
			if(!f.exists())
			{
				dout.writeUTF("File Not Found");
				return;
			}
			else
			{
				dout.writeUTF("READY");
				FileInputStream fin=new FileInputStream(f);
				int ch;
				do
				{
					ch=fin.read();
					dout.writeUTF(String.valueOf(ch));
				}
				while(ch!=-1);	
				fin.close();
				dout.writeUTF("bytes Send Successfully");							
			}
		}
	}