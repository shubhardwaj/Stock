package smtplearning;

import java.io.IOException;
import java.util.Scanner;

import sut.interfaces.InputAction;
//import sut.interfaces.OutputActions;
import sut.interfaces.SutInterface;

public class SUT extends Thread implements SutInterface {
//	String res;
	private SMTP smtpServer;
	private String host;
	private int port;
	public SUT(String host, int port){
		this.host = host;
		this.port=port;
	}
	
	@Override
	public OutputAction sendInput(InputAction inputAction) {

		switch (inputAction.getMethodName()) {
		case "IConnect":
			//smtpServer = new SMTP("onyx.ttu.ee", 25);
			// make it run in a separate thread ..
			OutputAction oa = null;
			try {
				if (smtpServer.connect())
					oa = new OutputAction("OOK");
				else
					oa = new OutputAction("ONOK");
				return oa;
			} catch (IOException ex) {
				System.out.println(ex);
			}
		case "IEHLO":
			smtpServer.IEHLO();
			return smtpServer.IEHLO();

		case "IMAIL":
			smtpServer.IMAIL();
			break;
		case "IRCPT":
			smtpServer.IRCPT();
			break;
		case "IDATA":
			smtpServer.IDATA();
			break;
		case "IDOT":
			smtpServer.IDOT();
			break;
		case "IQUIT":
			smtpServer.IQUIT();
			break;

		}

		return null;
	}
	Thread t1 = new Thread(new Runnable() {
	     public void run() {
	    	 sut.interfaces.InputAction paramInputAction= null;
	    	 sendInput(paramInputAction);
	     }
	}); 

	@Override
	public void sendReset() {
		// TODO Auto-generated method stub

	}
}