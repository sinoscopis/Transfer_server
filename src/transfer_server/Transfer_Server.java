package transfer_server;

import java.net.*;
import java.io.*;

 
/**
 * Demo Server: Contains a multi-threaded socket server sample code.
 */
public class Transfer_Server extends Thread
{ //Arbitrary port number
	final static int _transferPort = 44444;
	private Socket _socket = null;
	private Socket _transfersocket = null;
	private PrintWriter _out = null;
	private BufferedReader _in = null;
	private ServerSocket transferSocket = null;

	public static void main(String[] args) {
		new Transfer_Server().run();
	}
	public void run() {
		
		try {
			 transferSocket = new ServerSocket(_transferPort);
		
		} catch (IOException e) {
			System.err.println("Could not listen on port: " + _transferPort);
			System.exit(-1);
		}
		
		try {
			while(true){
				
				_transfersocket=transferSocket.accept();
				
				Runnable transferRequesthandler = new TransferRequestHandler(_transfersocket);
				
				new Thread(transferRequesthandler).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally { //In case anything goes wrong we need to close our I/O streams and sockets.
			try {
				_out.close();
				_in.close();
				_socket.close();
				_transfersocket.close();
			} catch(Exception e) { 
				System.out.println("Couldn't close I/O streams");
			}
		}
	}
}