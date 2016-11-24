package mypackage;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Store;

public class Main implements Runnable {
	private MyClass sut;
	private BufferedReader sockinLL;
	private PrintWriter sockoutLL;
	private Socket socket;
	private static boolean verbose = false;
	private static int portNumber = 25;
	
//socket programing for smtp
	public static void main(String args[]) {
		try{
		int portnumber=25;
		String hostname ="mail.google.com";
		Socket smtp = new Socket(hostname, portNumber);
		DataInputStream input = new DataInputStream(smtp.getInputStream());
		DataOutputStream output = new DataOutputStream(smtp.getOutputStream());
		if(smtp!= null && input != null && output != null){
			try{
				output.writeBytes("command output from smtp");
			}
			catch (UnknownHostException e) {
	            System.err.println("unidentified: hostname");
	        } catch (IOException e) {
	            System.err.println("Couldn't get I/O for the connection to: hostname");
	        }
		}
		output.close();
		input.close();
		smtp.close();
		}
		catch (UnknownHostException e) {
            System.err.println("Trying to connect to unknown host: " + e);
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
		//for tomte
		try {
			int i = 9999;

			ServerSocket localServerSocket = new ServerSocket(i);
			localServerSocket.setSoTimeout(300000000);
			while (true) {
				Socket localSocket;
				try {
					localSocket = localServerSocket.accept();
				} catch (SocketTimeoutException localSocketTimeoutException) {
					localServerSocket.close();
					break;
				}
				InputStream localInputStream = localSocket.getInputStream();
				BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream));
				OutputStream localOutputStream = localSocket.getOutputStream();
				PrintWriter localPrintWriter = new PrintWriter(new OutputStreamWriter(localOutputStream));
				System.out.println("New client...");
//				Main localMain = new Main(new myclass(), localSocket, localBufferedReader, localPrintWriter);
//				new Thread(localMain).start();
			}
		} catch (IOException localIOException) {
			System.out.println("IOException in main ...");
			localIOException.printStackTrace();
		}
	}


	  public void run() {
	    System.out.println("Starting client...");
	    try
	    {
	      String str1;
	      while ((str1 = this.sockinLL.readLine()) != null)
	      {
	        if (verbose) System.out.println("input: " + str1);

	        if (str1.equals("reset")) {
	          if (verbose) System.out.println("reset sut");
	          this.sut.reset();
	        }
	        else
	        {
	          InputAction localInputAction = new InputAction(str1);
	          OutputAction localOutputAction = this.sut.processSymbol(localInputAction);
//	          String str2 = localOutputAction.getValuesAsString();
//	          if (verbose) System.out.println("output: " + str2);
//
//	          this.sockoutLL.println(str2);
	          this.sockoutLL.flush();
	        }
	      }
	    } catch (SocketException localSocketException) { System.out.println("Server closed connection");
	    } catch (IOException localIOException1) {
	      localIOException1.printStackTrace();
	      System.out.println("reset...");
	    }
	    try
	    {
	      this.sockinLL.close();
	      this.sockoutLL.close();
	      this.socket.close();
	    } catch (IOException localIOException2) {
	      localIOException2.printStackTrace();
	    }
	    System.out.println("Closing client...");
	  }
}