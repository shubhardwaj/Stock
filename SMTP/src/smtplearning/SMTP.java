package smtplearning;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.ChangedCharSetException;

public class SMTP extends Thread {

	public static final int SOCKET_READ_TIMEOUT = 600*1000;

	private String hostname;
	private int port;
	
	protected Socket smtpSocket;
	protected BufferedReader in;
	protected OutputStreamWriter out;

	public SMTP() {
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
			smtpSocket.isConnected();
			smtpSocket.setSoTimeout(SOCKET_READ_TIMEOUT);
			in = new BufferedReader(new InputStreamReader(smtpSocket.getInputStream()));
			out = new OutputStreamWriter(smtpSocket.getOutputStream());
			if (in.readLine() == null && !(smtpSocket.isConnected())) {
				//System.out.println("Hostname to connect server do not found");
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
	// for logfile
	

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

	// for finally sending the mesage to smtp server
	protected synchronized String sendCommand(InputAction a) throws IOException, InterruptedException {
		out.write(a + "\n");
		out.flush();
		String response = getResponse();
		return response;
	}				

	// getting response code									
	protected synchronized String getResponse() throws IOException, InterruptedException {
		String response = "";
		String line;
		while (!((line = in.readLine()) == null))
		{ 
			String arr[]=line.split(" ",2);
			String a= arr[0];
			response= a;
			break;
// 			if (checkServerResponse(line)) {
//				response = a;
//				break;
//			} else {
//				System.out.println(line);
//				response = line;
//				break;
//			}
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

	//private InputAction a;

	protected synchronized String ehlo() throws IOException {
		boolean res = false;
		OutputAction oa = null;
		String str="" ;
		try {
			InputAction helo = new InputAction("helo mail.shubham.personal");
			str = sendCommand(helo);
//			if (str.contains("250"))
//				res = true;
//			else
//				res = false;
		} catch (Exception e) {
			System.out.println(e+"exception n ehlo");
		}
		return str;
	}

	protected synchronized String mail() throws IOException {
		boolean res = false;
		OutputAction oa = null;
		String str="";
		try {
			InputAction mail = new InputAction("mail from: root@mail.shubham.personal");
			str = sendCommand(mail);
//			if (str == "OOK")
//				res = true;
//			else
//				res = false;
		} catch (Exception e) {
			System.out.println(e+"exception in mail");
			
		}
		return str;
	}

	protected synchronized String rcpt() throws IOException {
		boolean res = false;
		OutputAction oa = null;
		String str ="";
		try {
			InputAction rcpt = new InputAction("rcpt to: root@mail.shubham.personal");
			str = sendCommand(rcpt);
//			if (str == "OOK")
//				res = true;
//			else
//				res = false;
		} catch (Exception e) {
			System.out.println(e+"exception in rcpt");
		}
		return str;
	}
	protected synchronized String data() throws IOException {
		boolean res = false;
		OutputAction oa = null;
		String str ="";
		try {
			InputAction rcpt = new InputAction("data");
			str = sendCommand(rcpt);
//			if (str == "OOK")
//				res = true;
//			else
//				res = false;
		} catch (Exception e) {
			System.out.println(e+"exception in rcpt");
		}
		return str;
	}

//	protected synchronized boolean data() throws IOException {
//		boolean res = false;
//		OutputAction oa = null;
//		try {
//			InputAction mail = new InputAction("DATA");
//			out.write(mail + "\n");
//			out.flush();
//			String line;
//			String response = "";
//			while (!((line = in.readLine()) == null)) {
//			//	System.out.println(line);
//				if (line.charAt(0) != '3') {
//					response = "ONOK";
//					break;
//				} else {
//					response = "OOK";
//					break;
//				}
//			}
//			if (response == "OOK")
//				res = true;
//			else
//				res = false;
//		} catch (Exception e) {
//			System.out.println(e+"exception in data");
//		}
//		return res;
//	}

	protected synchronized String dot() throws IOException {
		boolean res = false;
		OutputAction oa = null;
		String str ="";
		try {
			InputAction rcpt = new InputAction(".");
			str = sendCommand(rcpt);
//			if (str == "OOK")
//				res = true;
//			else
//				res = false;
		} catch (Exception e) {
			System.out.println(e+"exception in dot");
		}
		return str;
	}

	protected synchronized String rset() throws IOException {
		boolean res = false;
		OutputAction oa = null;
		String str ="";
		try {
			InputAction rcpt = new InputAction("rset");
			str = sendCommand(rcpt);
//			if (str == "OOK")
//				res = true;
//			else
//				res = false;
		} catch (Exception e) {
			System.out.println(e+"exception in rset");
		}
		return str;
	}

	protected synchronized String quit() throws IOException {
		boolean res = false;
		OutputAction oa = null;
		String str = "";
		try {
			InputAction rcpt = new InputAction("quit");
			str = sendCommand(rcpt);
//			if (str == "OOK")
//				res = true;
//			else
//				res = false;
		} catch (Exception e) {
			System.out.println(e+"exception in quit");
		}
		return str;
	}


}
