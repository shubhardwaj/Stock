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
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.ChangedCharSetException;

public class SMTP extends Thread {

	public static final int SOCKET_READ_TIMEOUT = 60 * 1000;

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
		// OutputAction oa = null;
		// Thread.sleep(10000);
		// System.out.println(in.readLine());
//		while ((line = in.readLine()) != null) {
//			if (line.isEmpty()) {
//				break;
//			} else if (checkServerResponse(line)) {
//				response = "OOK";
//				break;
//			} else {
//				response = "ONOK";
//				break;
//			}
//		}
//		return response;

		while (!((line = in.readLine()) == null))
			
			if (checkServerResponse(line)) {
				//System.out.println(line);
				response = "OOK";
				break;
			} else {
				response = "ONOK";
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

	private InputAction a;

	protected synchronized boolean ehlo() throws IOException {
		boolean res = false;
		OutputAction oa = null;
		try {
			InputAction helo = new InputAction("helo mail.shubham.personal");
			String str = sendCommand(helo);
			if (str == "OOK")
				res = true;
			else
				res = false;
		} catch (Exception e) {
			System.out.println(e);
		}
		return res;
	}

	protected synchronized boolean mail() throws IOException {
		boolean res = false;
		OutputAction oa = null;
		try {
			InputAction mail = new InputAction("mail from: root@mail.shubham.personal");
			String str = sendCommand(mail);
			if (str == "OOK")
				res = true;
			else
				res = false;
		} catch (Exception e) {
			System.out.println(e);
		}
		return res;
	}

	protected synchronized boolean rcpt() throws IOException {
		boolean res = false;
		OutputAction oa = null;
		try {
			InputAction rcpt = new InputAction("rcpt to: root@mail.shubham.personal");
			String str = sendCommand(rcpt);
			if (str == "OOK")
				res = true;
			else
				res = false;
		} catch (Exception e) {
			System.out.println(e);
		}
		return res;
	}

	protected synchronized boolean data() throws IOException {
		boolean res = false;
		OutputAction oa = null;
		try {
			InputAction mail = new InputAction("DATA");
			out.write(mail + "\n");
			out.flush();
			String line;
			String response = "";
			while (!((line = in.readLine()) == null)) {
			//	System.out.println(line);
				if (line.charAt(0) != '3') {
					response = "ONOK";
					break;
				} else {
					response = "OOK";
					break;
				}
			}
			if (response == "OOK")
				res = true;
			else
				res = false;
		} catch (Exception e) {
			System.out.println(e);
		}
		return res;
	}

	protected synchronized boolean dot() throws IOException {
		boolean res = false;
		OutputAction oa = null;
		try {
			InputAction rcpt = new InputAction(".");
			String str = sendCommand(rcpt);
			if (str == "OOK")
				res = true;
			else
				res = false;
		} catch (Exception e) {
			System.out.println(e);
		}
		return res;
	}

	protected synchronized boolean rset() throws IOException {
		boolean res = false;
		OutputAction oa = null;
		try {
			InputAction rcpt = new InputAction("rset");
			String str = sendCommand(rcpt);
			if (str == "OOK")
				res = true;
			else
				res = false;
		} catch (Exception e) {
			System.out.println(e);
		}
		return res;
	}

	protected synchronized boolean quit() throws IOException {
		boolean res = false;
		OutputAction oa = null;
		try {
			InputAction rcpt = new InputAction("quit");
			String str = sendCommand(rcpt);
			if (str == "OOK")
				res = true;
			else
				res = false;
		} catch (Exception e) {
			System.out.println(e);
		}
		return res;
	}

	public OutputAction IRCPT() {
		OutputAction oa = null;
		InputAction rec = new InputAction("rcpt to: root@testserver.com");
		try {
			Pattern pattern = Pattern.compile("^(rcpt to: <).*>$", Pattern.CASE_INSENSITIVE);
			Matcher match = pattern.matcher((CharSequence) rec);
			if (match.matches()) {
				String res = sendCommand(rec);
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
		InputAction ab = new InputAction(".");
		try {
			Pattern pattern = Pattern.compile("^(.)", Pattern.CASE_INSENSITIVE);
			Matcher match = pattern.matcher((CharSequence) ab);
			if (match.matches()) {
				String res = sendCommand(ab);
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
