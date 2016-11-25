package mypackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class Tomte extends Thread {
	public static final int SOCKET_READ_TIMEOUT = 60 * 1000;
	private int port;
	protected ServerSocket ss;
	protected Socket socket;
	protected BufferedReader in;
	protected OutputStreamWriter out;
	
	public Tomte(int port, Socket socket, BufferedReader in, OutputStreamWriter out){
		this.port= port;
		this.socket=socket;
		this.in= in;
		this.out= out;
	}

	protected void connect()  {
		try{
		ss = new ServerSocket(port);
		ss.setSoTimeout(SOCKET_READ_TIMEOUT);		
		while(true){
			try{
				socket = ss.accept();
			}
			 catch (SocketTimeoutException localSocketTimeoutException) {
		          socket.close();
		          break;
			 }
			in = new BufferedReader (new InputStreamReader(socket.getInputStream()));
			PrintStream ps = new PrintStream(socket.getOutputStream());
			System.out.println("New client...");
			String rd = in.readLine();
		}
		}
		catch (IOException localIOException)
	    {
	      System.out.println("IOException ...");
	      localIOException.printStackTrace();
	    }
	
	}
	public void run(){
		System.out.println("Starting TOMTE client...");
	    try
	    {String str1;
	      while ((str1 = this.in.readLine()) != null)
	      {
	    	  InputAction localInputAction = new InputAction(str1);
	          OutputAction localOutputAction = new OutputAction(localInputAction);
	        //  String str2 = localOutputAction.getValuesAsString();
	      }
	      try
	      {
	        this.socket.close();
	        this.in.close();
	        this.out.close();
	      } 
	      catch (SocketException localSocketException)
	      { 
	    	  System.out.println("Server closed connection");
	      }
	      }
	      catch (IOException localIOException2) {
	        localIOException2.printStackTrace();
	      }
	      System.out.println("Closing client...");
	}
	   
	
}
