package smtplearning;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.sql.Timestamp;

import sut.interfaces.InputAction;
import sut.interfaces.SutInterface;

public class SUT extends Thread implements SutInterface {

	private SMTP smtpServer;
	private BufferedReader sockinLL;
	private PrintWriter sockoutLL;
	private Socket sockLL;
	private static boolean verbose = true;
	private static int portNumber = 7892;

	public SUT( Socket paramSocket, BufferedReader paramBufferedReader, PrintWriter paramPrintWriter) {
		this.sockLL = paramSocket;
		this.sockinLL = paramBufferedReader;
		this.sockoutLL = paramPrintWriter;
	}

	public static void globalOut(String paramString) {
	}

	public static void handleArgs(String[] paramArrayOfString) {
		for (int i = 0; i < paramArrayOfString.length; i++)
			if ("--verbose".equals(paramArrayOfString[i])) {
				verbose = true;
			} else if ("-v".equals(paramArrayOfString[i])) {
				verbose = true;
			} else if ("--port".equals(paramArrayOfString[i])) {
				if (i == paramArrayOfString.length - 1) {
					System.err.println("Missing argument for --port.");
					printUsage();
					System.exit(-1);
				}
				try {
					portNumber = new Integer(paramArrayOfString[(++i)]).intValue();
				} catch (NumberFormatException localNumberFormatException) {
					System.err.println("Error parsing argument for --port. Must be integer. " + paramArrayOfString[i]);
					System.exit(-1);
				}
			}
	}

	public static void printUsage() {
		System.out.println(" options:");
		System.out.println("    --port n         use tcp port n to listen on for incoming connections");
		System.out.println("    -v|--verbose     verbose mode");
	}

	public static void main(String[] paramArrayOfString) {
		handleArgs(paramArrayOfString);

		String str = "\n";
		str = str + "SUT simulation socketserver";
		str = str + "\n-> listening at port : " + portNumber;
		if (verbose)
			str = str + "\n-> verbose mode : ON";
		else {
			str = str + "\n-> verbose mode : OFF";
		}
		str = str + "\n-> the server has a timeout of 30 seconds";
		str = str + "\n   note: to prevent unnecessary servers to keep on running";
		System.out.println(str + "\n");
		try {
			int i = portNumber;

			ServerSocket localServerSocket = new ServerSocket(i);
			localServerSocket.setSoTimeout(300000000);
			while (true) {
				Socket localSocket;
				try {
					localSocket = localServerSocket.accept();
				} catch (SocketTimeoutException localSocketTimeoutException) {
					localServerSocket.close();
					System.err.println(localSocketTimeoutException.getMessage());
					break;
				}
				InputStream localInputStream = localSocket.getInputStream();
				BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream));
				OutputStream localOutputStream = localSocket.getOutputStream();
				PrintWriter localPrintWriter = new PrintWriter(new OutputStreamWriter(localOutputStream));

				System.out.println("New client...");
				SUT localMain = new SUT(localSocket, localBufferedReader, localPrintWriter);
				new Thread(localMain).start();
			}

		} catch (IOException localIOException) {
			System.out.println("IOException in main ...");
			localIOException.printStackTrace();
		}
	}
//connection to the smtp server
	private OutputAction connect() {
		OutputAction oa = null;
		if (smtpServer == null) {
			smtpServer = new SMTP("192.168.222.1", 25);
			try {
				if (smtpServer.connect())
					oa = new OutputAction("OOK");
				else
					oa = new OutputAction("ONOK");
			} catch (IOException ex) {
				System.out.println(ex);
				oa = new OutputAction("ONOK");
			}
		} else {
			smtpServer.close();
			smtpServer = new SMTP("192.168.222.1", 25);
			oa = new OutputAction("OOK");
			try {
				if (smtpServer.connect())
					oa = new OutputAction("OOK");
				else
					oa = new OutputAction("ONOK");
			} catch (IOException ex) {
				System.out.println(ex);
				oa = new OutputAction("ONOK");
			}
		}
		return oa;

	}
// providing input from the client and send to the SMTP
	public OutputAction sendInput(InputAction inputAction) {
		OutputAction oa = null;
		switch (inputAction.getMethodName()) {
		case "IConnect":
			break;
		case "IEHLO":
			try {
				String p = smtpServer.ehlo();
				if (smtpServer == null) {
					oa = new OutputAction("O" + p);
				} else {
					if (p.contains("250")) {

						oa = new OutputAction("O" + p);
					} else
						oa = new OutputAction("O" + p);
				}
			} catch (Exception e) {
				System.out.println(e);
			}
			break;
		case "IMAIL":
			try {
				String p = smtpServer.mail();
				if (smtpServer == null) {
					oa = new OutputAction("O" + p);
				} else {
					if (p.contains("250"))
						oa = new OutputAction("O" + p);
					else
						oa = new OutputAction("O" + p);
				}
			} catch (Exception e) {
				System.out.println(e);
			}
			break;
		case "IRCPT":
			try {
				String p = smtpServer.rcpt();
				if (smtpServer == null) {
					oa = new OutputAction("O" + p);
				} else {
					if (p == "250" || p == "251")
						oa = new OutputAction("O" + p);
					// else if (p.contains("251"))
					// oa = new OutputAction("O" + p);
					else
						oa = new OutputAction("O" + p);
				}
			} catch (Exception e) {
				System.out.println(e);
			}
			break;
		case "IDATA":
			try {

				String p = smtpServer.data();
				if (smtpServer == null) {
					oa = new OutputAction("O" + p);
				} else {
					if (p.contains("354")) {
						String r = smtpServer.dot();
						if (smtpServer == null) {
							oa = new OutputAction("O" + r);
						} else {
							if (r.contains("250"))
								oa = new OutputAction("O" + r);
							else
								oa = new OutputAction("O" + r);
						}
					} else
						oa = new OutputAction("O" + p);
				}

			} catch (Exception e) {
				System.out.println(e);
			}
			break;
		
		case "IRSET":
			try {
				String p = smtpServer.rset();
				if (smtpServer == null) {
					oa = new OutputAction("O" + p);
				} else {
					if (p.contains("250"))
						oa = new OutputAction("O" + p);
					else
						oa = new OutputAction("O" + p);
				}
			} catch (Exception e) {
				System.out.println(e);
			}
			break;
		case "IQUIT":
			try {
				String p = smtpServer.quit();
				if (smtpServer == null) {
					oa = new OutputAction("O" + p);
				} else {
					if (p.contains("221")) {
						oa = new OutputAction("O" + p);
					} else
						oa = new OutputAction("O" + p);
					connect();
				}
			} catch (Exception e) {
				System.out.println(e);
			}
			break;
		}
		return oa;
	}
//reseting the thread
	public void reset() {
		Thread t = new Thread();
		t.start();
	}

	public void run() {
		System.out.println("Starting client...");
		System.out.println("Connecting: " + connect().toString());
		try {
			String str1;
			System.out.println("input: ");
			while ((str1 = this.sockinLL.readLine()) != null) {
				//removing the ascii coming in the input while 
				// reading line from socket
				str1 = str1.replaceAll("[^\\x30-\\x7A]", "");
				try {
					String filename = "SMTPLog.log";
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					FileWriter fw = new FileWriter(filename, true);
					if (verbose) {
						System.out.println("input: " + str1);
						fw.write(timestamp+"--------------" + str1 + "\n");
						//fw.close();
					}
					if (str1.equals("reset")) {
						if (verbose) {
							System.out.println("reset sut");
						}
						this.sendReset();
					} else {
						InputAction localInputAction = new smtplearning.InputAction(str1);
						OutputAction localOutputAction = this.sendInput(localInputAction);
						String str2 = localOutputAction.getValuesAsString();
						if (verbose) {
							System.out.println("output: " + str2);
							fw.write(timestamp+ "--------------" + str2 + "\n");
							fw.close();
						}
						this.sockoutLL.println(str2);
						this.sockoutLL.flush();
					}
				} catch (IOException ioe) {
					System.err.println("IOException: " + ioe.getMessage());
				}
			}
		} catch (SocketException localSocketException) {
			System.out.println("Server closed connection");
		} catch (Exception localIOException1) {
			localIOException1.printStackTrace();
			System.out.println("reset...");
		}
		try {
			this.sockinLL.close();
			this.sockoutLL.close();
			this.sockLL.close();
		} catch (IOException localIOException2) {
			localIOException2.printStackTrace();
		}
		System.out.println("Closing client...");
	}
@Override
	public void sendReset() {
		reset();
	}

}