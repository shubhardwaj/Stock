package mypackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.Socket;
import java.net.SocketException;

public class SMTP extends Thread{

	 public static final int SOCKET_READ_TIMEOUT = 60*1000;
	 
	    private String hostname;
	    private int port;
	    private String recipient;
	    private String sender;
	    private String message;
	 
	    protected Socket smtpSocket;
	    protected BufferedReader in;
	    protected OutputStreamWriter out;
	    
	    public SMTP(String hostname, int port, String recipient,
	            String sender, String subject, String message)
	        {
	            this.hostname = hostname;
	            this.port = port;
	            this.recipient = recipient;
	            this.message = message;
	            this.sender = sender;
	        }
	    //Connecting to smtp server
	    protected void connect() throws IOException
	    	    {
	    	        smtpSocket = new Socket(hostname, port);
	    	        smtpSocket.setSoTimeout(SOCKET_READ_TIMEOUT);
	    	        in = new BufferedReader(new InputStreamReader(smtpSocket.getInputStream()));
	    	        out = new OutputStreamWriter(smtpSocket.getOutputStream());
	    	    }
	    
	    //for sending command to the server
	    protected String sendCommand(String commandString) throws IOException
	    	    {
	    	        out.write(commandString + "\n");
	    	        out.flush();
	    	        String response = getResponse();
	    	        return response;
	    	    }
	    //getting response code
	    protected String getResponse()throws IOException
	    	     {
	    	        String response = "";
	    	 
	    	        String line;
	    	        do {
	    	            line = in.readLine();
	    	            if ((line == null) || (line.length() < 3)) {
	    	                // response should have 3- digit in response code
	    	                throw new IOException("Server not responding properly.");
	    	            }
	    	            response += line + "\n";
	    	        } while ((line.length() > 3) && (line.charAt(3) == '-'));
	    	 
	    	        return response;
	    	    }
	    //checking the response coming from the smtp server
	    protected void checkServerResponse(String response, char expectedResponseStart)
	    	    throws IOException
	    	    {
	    	        if (response.charAt(0) != expectedResponseStart)
	    	            throw new IOException(response);
	    	    }
	    	 
	    public void sendMessage() throws IOException
	    	    {
	    	        connect();
	    	 
	    	        // After connecting, the SMTP server will send a response string.
	    	        // Make sure it starts with a '2' (reponses in the 200's are positive).
	    	        String response = getResponse();
	    	        checkServerResponse(response,'2');
	    	 
	    	        // Introduce ourselves to the SMTP server with a polite "HELO host name"
	    	        sendCommand("HELO " + smtpSocket.getInetAddress().getHostAddress().toString());
	    	 
	    	        // Tell the server who this message is from
	    	        sendCommand("MAIL FROM: <" + sender + ">");
	    	 
	    	        // Now tell the server who we want to send a message to
	    	        sendCommand("RCPT TO: <" + recipient + ">");
	    	 
	    	        // Okay, now send the mail message. We expect a response beginning
	    	        // with '3' indicating that the server is ready for data.
	    	        sendCommand("DATA");
	    	 
	    	        
	    	 
	    	        BufferedReader msgBodyReader = new BufferedReader(new StringReader(message));
	    	        // Send each line of the message
	    	        String line;
	    	        while ((line=msgBodyReader.readLine()) != null) {
	    	            // If the line begins with a ".", put an extra "." in front of it.
	    	            if (line.startsWith("."))
	    	                out.write('.');
	    	            out.write(line + "\n");
	    	        }
	    	 
	    	        // A "." on a line by itself ends a message.
	    	        sendCommand(".");
	    	 
	    	        // Message is sent. Close the connection to the server
	    	        in.close();
	    	        out.close();
	    	        smtpSocket.close();
	    	    }

	    public void run() {
	      System.out.println("Starting SMTP client...");
	      	try
	      	{
	      		sendMessage();
	      	} 
	      	catch (SocketException localSocketException)
	      	{ 
	      		System.out.println("Server closed connection");
	      	} 
	      	catch (IOException localIOException1) 
	      	{
	      		localIOException1.printStackTrace();
	      		System.out.println("reset...");
	      	}
	      System.out.println("Closing client...");
	    }	    	 	
}
