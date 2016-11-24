package mypackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.Socket;

public class Tomte {
	 public static final int SOCKET_READ_TIMEOUT = 15*1000;
	 
	    private String host;
	    private int port;
	    private String recipient;
	    private String sender;
	    private String subject;
	    private String message;
	 
	    protected Socket smtpSocket;
	    protected BufferedReader in;
	    protected OutputStreamWriter out;
	    
	 
	    //creating session 
	    public Tomte(String host, int port, String recipient,
	            String sender, String subject, String message)
	        {
	            this.host = host;
	            this.port = port;
	            this.recipient = recipient;
	            this.message = message;
	            this.sender = sender;
	            this.subject = subject;
	        }
	    //Connecting to smtp server
	    protected void connect() throws IOException
	    	    {
	    	        smtpSocket = new Socket(host, port);
	    	        smtpSocket.setSoTimeout(SOCKET_READ_TIMEOUT);
	    	        in = new BufferedReader(new InputStreamReader(smtpSocket.getInputStream()));
	    	        out = new OutputStreamWriter(smtpSocket.getOutputStream());
	    	    }
	    
	    //sending command to the server
	    protected String sendCommand(String commandString) throws IOException
	    	    {
	    	        out.write(commandString + "\n");
	    	        out.flush();
	    	        String response = getResponse();
	    	        return response;
	    	    }
	    protected String getResponse()throws IOException
	    	    {
	    	        String response = "";
	    	 
	    	        String line;
	    	        do {
	    	            line = in.readLine();
	    	            if ((line == null) || (line.length() < 3)) {
	    	                // SMTP response lines should at the very least have a 3-digit number
	    	                throw new IOException("Bad response from server.");
	    	            }
	    	            response += line + "\n";
	    	        } while ((line.length() > 3) && (line.charAt(3) == '-'));
	    	 
	    	        return response;
	    	    }
	    protected void doCommand(String commandString, char expectedResponseStart)
	    	    throws IOException
	    	    {
	    	        String response = sendCommand(commandString);
	    	        checkServerResponse(response, expectedResponseStart);
	    	    }
	    protected void checkServerResponse(String response, char expectedResponseStart)
	    	    throws IOException
	    	    {
	    	        if (response.charAt(0) != expectedResponseStart)
	    	            throw new IOException(response);
	    	    }
	    	 
	    public void sendMessage()
	    	    throws IOException
	    	    {
	    	        connect();
	    	 
	    	        // After connecting, the SMTP server will send a response string.
	    	        // Make sure it starts with a '2' (reponses in the 200's are positive).
	    	        String response = getResponse();
	    	        checkServerResponse(response,'2');
	    	 
	    	        // Introduce ourselves to the SMTP server with a polite "HELO localhostname"
	    	        doCommand("HELO " + smtpSocket.getLocalAddress().toString(), '2');
	    	 
	    	        // Tell the server who this message is from
	    	        doCommand("MAIL FROM: <" + sender + ">", '2');
	    	 
	    	        // Now tell the server who we want to send a message to
	    	        doCommand("RCPT TO: <" + recipient + ">", '2');
	    	 
	    	        // Okay, now send the mail message. We expect a response beginning
	    	        // with '3' indicating that the server is ready for data.
	    	        doCommand("DATA", '3');
	    	 
	    	        
	    	 
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
	    	        doCommand(".", '2');
	    	 
	    	        // Message is sent. Close the connection to the server
	    	        in.close();
	    	        out.close();
	    	        smtpSocket.close();
	    	    }
	    	 
	    	
}
