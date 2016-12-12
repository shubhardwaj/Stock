package smtplearning;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMTP extends Thread {

	public static final int SOCKET_READ_TIMEOUT = 60 * 1000;

	private String hostname;
	private int port;
	private String recipient;
	private String sender;
	private String subject;
	private String message;

	protected Socket smtpSocket;
	protected BufferedReader in;
	protected OutputStreamWriter out;
	public SMTP(){}
	public SMTP(String hostname, int port, String recipient, String sender, String subject, String message) {
		this.hostname = hostname;
		this.port = port;
		this.recipient = recipient;
		this.message = message;
		this.subject = subject;
		this.sender = sender;
	}
public SMTP(SMTP smtp, Socket paramSocket, BufferedReader paramBufferedReader, PrintWriter paramPrintWriter){
		try{
		connect();
		}catch(IOException e){
			System.out.println(e);
		}
}
	public SMTP(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
	}
	
	protected synchronized boolean connect() throws IOException {
		boolean res = false;
		OutputAction oa = null;
		try {
			smtpSocket = new Socket(hostname, port);
			smtpSocket.setSoTimeout(SOCKET_READ_TIMEOUT);
			in = new BufferedReader(new InputStreamReader(smtpSocket.getInputStream()));
			out = new OutputStreamWriter(smtpSocket.getOutputStream());
			if (in.readLine() == null) {
				System.out.println("Hostname to connect server do not found");
				res = false;
				oa = new OutputAction("ONOK");
				System.out.println(oa);
			} else
				res = true;
			oa = new OutputAction("OOK");
			System.out.println(oa);
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
		}
	}

	// for finally sending the mesage to smtp server
	protected String sendCommand(InputAction a) throws IOException {
		out.write(a + "\n");
		System.out.println("command sent to server is I" + a);
		out.flush();
		Logger.log("I" + a);
		String response = getResponse();
		return response;
	}

	// getting response code
	protected String getResponse() throws IOException {
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

	// checking the response coming from the smtp server
	protected boolean checkServerResponse(String response) throws IOException {
		boolean resp;
		if (response.charAt(0) != 2) {
			// throw new IOException(response);
			resp = false;
		}

		else {
			resp = true;
		}

		return resp;
	}

//	public void sendMessage() throws IOException {
//		connect();
//		System.out.println("connection done");
//		getResponse();
//		// After connecting, the SMTP server will send a response string.
//		// Make sure it starts with a '2' (reponses in the 200's are positive).
//		// String response = getResponse();
//		// Introduce ourselves to the SMTP server with a polite "HELO host name"
//		String res = sendCommand("HELO " + smtpSocket.getInetAddress().getHostAddress().toString());
//		checkServerResponse(res);
//		// Tell the server who this message is from
//		sendCommand("MAIL FROM: <" + sender + ">");
//		checkServerResponse(res);
//
//		// Now tell the server who we want to send a message to
//		sendCommand("RCPT TO: <" + recipient + ">");
//		checkServerResponse(res);
//
//		// Okay, now send the mail message. We expect a response beginning
//		// with '3' indicating that the server is ready for data.
//		sendCommand("DATA \n" + "Subject:" + subject + "\n" + message);
//		checkServerResponse(res);
//		sendCommand(".");
//		checkServerResponse(res);
//		sendCommand("quit");
//		checkServerResponse(res);
//
//		BufferedReader msgBodyReader = new BufferedReader(new StringReader(message));
//		// Send each line of the message
//		String line;
//		while ((line = msgBodyReader.readLine()) != null) {
//			// If the line begins with a ".", put an extra "." in front of it.
//			if (line.startsWith("."))
//				out.write('.');
//			out.write(line + "\n");
//		}
//
//		// A "." on a line by itself ends a message.
//		sendCommand(".");
//
//		// Message is sent. Close the connection to the server
//		in.close();
//		out.close();
//		smtpSocket.close();
//	}

//	public void run() {
//		System.out.println("Starting SMTP client...");
//		try {
//			sendMessage();
//
//		} catch (SocketException localSocketException) {
//			System.out.println("Server closed connection");
//		} catch (IOException localIOException1) {
//			localIOException1.printStackTrace();
//			System.out.println("reset...");
//		}
//		System.out.println("Closing client...");
//	}
private InputAction a;
	public OutputAction IEHLO() {
		OutputAction oa = null;
		try {
			
			Pattern pattern = Pattern.compile("^(helo .*|ehlo .*)", Pattern.CASE_INSENSITIVE);
			Matcher match = pattern.matcher((CharSequence) a);
			if (match.matches()) {
				sendCommand(a);
				//checkServerResponse(a);
				oa = new OutputAction("OOK");

			} else {
				oa = new OutputAction("ONOK");

			}

		} catch (IOException ex) {
			System.out.println(ex);
		}
		return oa;
	}

	public OutputAction IMAIL() {
		OutputAction oa = null;
		try {
			Pattern pattern = Pattern.compile("^(mail from: <).*>$", Pattern.CASE_INSENSITIVE);
			Matcher match = pattern.matcher((CharSequence) a);
			if (match.matches()) {
				String res = sendCommand(a);
				if (checkServerResponse(res)) {
					oa = new OutputAction("OOK");

				} else {
					oa = new OutputAction("ONOK");
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return oa;
	}

	public OutputAction IRCPT() {
		OutputAction oa = null;
		try {
			Pattern pattern = Pattern.compile("^(rcpt to: <).*>$", Pattern.CASE_INSENSITIVE);
			Matcher match = pattern.matcher((CharSequence) a);
			if (match.matches()) {
				String res = sendCommand(a);
				if (checkServerResponse(res)) {
					oa = new OutputAction("OOK");

				} else {
					oa = new OutputAction("ONOK");
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return oa;
	}

	public OutputAction IDATA() {
		OutputAction oa = null;
		try {
			Pattern pattern = Pattern.compile("^(DATA)", Pattern.CASE_INSENSITIVE);
			Matcher match = pattern.matcher((CharSequence) a);
			if (match.matches()) {
				String res = sendCommand(a);
				checkServerResponse(res);
				oa = new OutputAction("OOK");

			} else {
				oa = new OutputAction("ONOK");

			}

		} catch (Exception e) {
			System.out.println(e);
		}
		return oa;
	}

	public OutputAction IDOT() {
		OutputAction oa = null;
		try {
			Pattern pattern = Pattern.compile("^(.)", Pattern.CASE_INSENSITIVE);
			Matcher match = pattern.matcher((CharSequence) a);
			if (match.matches()) {
				String res = sendCommand(a);
				if (checkServerResponse(res)) {
					oa = new OutputAction("OOK");

				} else {
					oa = new OutputAction("ONOK");
				}
			}

		} catch (Exception e) {
			System.out.println(e);
		}
		return oa;
	}

	public OutputAction IQUIT() {
		OutputAction oa = null;
		try {
			Pattern pattern = Pattern.compile("^(quit)", Pattern.CASE_INSENSITIVE);
			Matcher match = pattern.matcher((CharSequence) a);
			if (match.matches()) {
				String res = sendCommand(a);
				if (checkServerResponse(res)) {
					oa = new OutputAction("OOK");
					close();
				} else {
					oa = new OutputAction("ONOK");

				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return oa;
	}
}
