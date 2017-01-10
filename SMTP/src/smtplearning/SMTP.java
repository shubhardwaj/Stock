package smtplearning;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.Timestamp;


public class SMTP {

	public static final int SOCKET_READ_TIMEOUT = 600 * 1000;

	private String hostname;
	private int port;

	protected Socket smtpSocket;
	protected BufferedReader in;
	protected OutputStreamWriter out;

	public SMTP(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
	}
	// establishing a coonection to the smtp server
	protected synchronized boolean connect() throws IOException {
		boolean res = false;
		OutputAction oa = null;
		try {
			smtpSocket = new Socket(hostname, port);
			smtpSocket.isConnected();
			smtpSocket.setSoTimeout(SOCKET_READ_TIMEOUT);
			in = new BufferedReader(new InputStreamReader(smtpSocket.getInputStream()));
			out = new OutputStreamWriter(smtpSocket.getOutputStream());
			if (in.readLine() == null && !(smtpSocket.isConnected())) {
				res = false;
				oa = new OutputAction("ONOK");
				System.out.println(oa);
			} else
				res = true;
			oa = new OutputAction("OOK");
		} catch (IOException ex) {
			System.out.println("Can't connect to " + hostname);
		}
		return res;
	}

	// for closing connection
	public void close() {
		try {
			in.close();
			out.close();
			smtpSocket.close();
		} catch (Exception ex) {
			// Ignore the exception. Probably the socket is not open.
			System.out.println(ex);
		}
	}

	// for sending the mesage to smtp server and get response
	protected synchronized String sendCommand(InputAction a) throws IOException, InterruptedException {
		out.write(a + "\n");
		out.flush();
		String response = getResponse();
		return response;
	}

	// getting response code from response 
	protected synchronized String getResponse() throws IOException, InterruptedException {
		String response = "";
		String line;

		while (!((line = in.readLine()) == null)) {
			try
			{
			    String filename= "SMTPLog.log";
			    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			    FileWriter fw = new FileWriter(filename,true); //the true will append the new data
			    fw.write(timestamp+"--------------"+line+"\n");//appends the string to the file
			    fw.close();
			}
			catch(IOException ioe)
			{
			    System.err.println("IOException: " + ioe.getMessage());
			}
			String arr[] = line.split(" ", 2);
			String a = arr[0];
			response = a;
			break;
		}

		return response;

	}

	// checking the response coming from the smtp server
	protected boolean checkServerResponse(String response) throws IOException {
		boolean resp;

		if (response.charAt(0) != '2') {
			resp = false;
		} else {
			resp = true;
		}
		return resp;
	}
	// helo command for the smtp server
	protected synchronized String ehlo() throws IOException {
		String str = "";
		try {
			InputAction helo = new InputAction("helo mail.shubham.personal");
			str = sendCommand(helo);
		} catch (Exception e) {
			System.out.println(e + "exception n ehlo");
		}
		return str;
	}
//mail command as input to smtp server
	protected synchronized String mail() throws IOException {
		String str = "";
		try {
			InputAction mail = new InputAction("mail from: root@mail.shubham.personal");
			str = sendCommand(mail);
			} catch (Exception e) {
			System.out.println(e + "exception in mail");
		}
		return str;
	}

	protected synchronized String rcpt() throws IOException {
		String str = "";
		try {
			InputAction rcpt = new InputAction("rcpt to: root@mail.shubham.personal");
			str = sendCommand(rcpt);
		} catch (Exception e) {
			System.out.println(e + "exception in rcpt");
		}
		return str;
	}
//data command to send as input to smtp server
	protected synchronized String data() throws IOException {
		String str = "";
		try {
			InputAction rcpt = new InputAction("data");
			str = sendCommand(rcpt);
		} catch (Exception e) {
			System.out.println(e + "exception in rcpt");
		}
		return str;
	}
//dot command to terminate the data command send to smtp server
	protected synchronized String dot() throws IOException {
		String str = "";
		try {
			InputAction rcpt = new InputAction(".");
			str = sendCommand(rcpt);
			} catch (Exception e) {
			System.out.println(e + "exception in dot");
		}
		return str;
	}
// sending reset command to smtp server
	protected synchronized String rset() throws IOException {
		String str = "";
		try {
			InputAction rcpt = new InputAction("rset");
			str = sendCommand(rcpt);
			} catch (Exception e) {
			System.out.println(e + "exception in rset");
		}
		return str;
	}
//quit command description
	protected synchronized String quit() throws IOException {
		String str = "";
		try {
			InputAction rcpt = new InputAction("quit");
			str = sendCommand(rcpt);
		} catch (Exception e) {
			System.out.println(e + "exception in quit");
		}
		return str;
	}

}
