package smtplearning;

import java.io.BufferedReader;
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
import java.util.List;

import sut.interfaces.InputAction;
import sut.interfaces.Parameter;
//import sut.interfaces.OutputActions;
import sut.interfaces.SutInterface;

public class SUT extends Thread implements SutInterface {
	// String res;
	public SUT() {
	}

	private SMTP smtpServer;
	private String host;
	private int port;
	private BufferedReader sockinLL;
	private PrintWriter sockoutLL;
	private Socket sockLL;
	private static boolean verbose = true;
	private static int portNumber = 7892;
	// replacing myclass with SUT
	private SUT paramSUT;

	public SUT(SUT paramSUT, Socket paramSocket, BufferedReader paramBufferedReader, PrintWriter paramPrintWriter) {
		this.paramSUT = paramSUT;
		this.sockLL = paramSocket;
		this.sockinLL = paramBufferedReader;
		this.sockoutLL = paramPrintWriter;
	}

	// public static void globalOut(String paramString) {
	// }

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
				SUT localMain = new SUT(new SUT(), localSocket, localBufferedReader, localPrintWriter);
				new Thread(localMain).start();
			}

		} catch (IOException localIOException) {
			System.out.println("IOException in main ...");
			localIOException.printStackTrace();
		}
	}

	@Override
	public OutputAction sendInput(InputAction inputAction) {
		OutputAction oa = null;
		switch (inputAction.getMethodName()) {
		case "IConnect":			
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
			}
			else
				oa= new OutputAction("ONOK");
			break;
		case "IEHLO":
			if (smtpServer == null) {
				oa = new OutputAction("ONOK");
			} else {
				try {
					if (smtpServer.ehlo())
						oa = new OutputAction("OOK");
					else
						oa = new OutputAction("ONOK");
				} catch (IOException ex) {
					oa= new OutputAction("ONOK");
				}
			}break;
		case "IMAIL":
			if (smtpServer == null) {
				oa = new OutputAction("ONOK");
			} else {
				try {
					if (smtpServer.mail())
						oa = new OutputAction("OOK");
					else
						oa = new OutputAction("ONOK");
				} catch (IOException ex) {
					oa= new OutputAction("ONOK");
				}
			}break;
		case "IRCPT":
			if (smtpServer == null) {
				oa = new OutputAction("ONOK");
			} else {
				try {
					if (smtpServer.rcpt())
						oa = new OutputAction("OOK");
					else
						oa = new OutputAction("ONOK");
				} catch (IOException ex) {
					oa= new OutputAction("ONOK");
				}
			}break;
		case "IDATA":
			if (smtpServer == null) {
				oa = new OutputAction("ONOK");
			} else {
				try {
					if (smtpServer.data())
						oa = new OutputAction("OOK");
					else
						oa = new OutputAction("ONOK");
				} catch (IOException ex) {
					oa= new OutputAction("ONOK");
				}
			}break;
		case "IDOT":
			if (smtpServer == null) {
				oa = new OutputAction("ONOK");
			} else {
				try {
					if (smtpServer.dot())
						oa = new OutputAction("OOK");
					else
						oa = new OutputAction("ONOK");
				} catch (IOException ex) {
					oa= new OutputAction("ONOK");
				}
			}break;
		case "IRSET":
			if (smtpServer == null) {
				oa = new OutputAction("ONOK");
			} else {
				try {
					if (smtpServer.rset())
						oa = new OutputAction("OOK");
					else
						oa = new OutputAction("ONOK");
				} catch (IOException ex) {
					oa= new OutputAction("ONOK");
				}
			}break;
		case "IQUIT":
			if (smtpServer == null) {
				oa = new OutputAction("ONOK");
			} else {
				try {
					if (smtpServer.quit())
						oa = new OutputAction("OOK");
					else
						oa = new OutputAction("ONOK");
				} catch (IOException ex) {
					oa= new OutputAction("ONOK");
				}
			}break;
			default:
				oa= new OutputAction("ONOK");
		}
		 return oa;
	}
	public void reset(){
		 System.out.println("reseting...");
	}

	public void run() {
		// sut.interfaces.InputAction paramInputAction;
		// sendInput(paramInputAction);
		System.out.println("Starting client...");
		try {
			String str1;
			System.out.println("input: ");
			while ((str1 = this.sockinLL.readLine()) != null) {
				str1 = str1.replaceAll("[^\\x30-\\x7A]", "");
				if (verbose)
					System.out.println("input: " + str1);

				if (str1.equals("reset")) {
					if (verbose)
						System.out.println("reset sut");
					this.sendReset();
				} else {
					InputAction localInputAction = new smtplearning.InputAction(str1);
					OutputAction localOutputAction = this.sendInput(localInputAction);
					String str2 = localOutputAction.getValuesAsString();
					if (verbose)
						System.out.println("output: " + str2);

					this.sockoutLL.println(str2);
					this.sockoutLL.flush();
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