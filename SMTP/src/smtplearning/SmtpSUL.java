package smtplearning;

import java.io.IOException;

import de.learnlib.api.SUL;
import de.learnlib.api.SULException;
import net.automatalib.words.Alphabet;
import net.automatalib.words.impl.EnumAlphabet;

public class SmtpSUL implements SUL<SmtpInputs, String> {

	private int port=0;
	private String ip=null;
	
	SMTP smtpConnection;
	
	public SmtpSUL(String ip, int port) {
		this.port=port;
		this.ip=ip;
	}
	//Connect to SMTP server
	public void pre() {
		// TODO Auto-generated method stub
		this.connect();
		
	}
	
	public boolean  canFork() {
		return false;
	}
	
	//close connection to SMTP
	public void post() {
		if (smtpConnection != null )
    		smtpConnection.close();
		
	}
	// to step into SMTP server sending
	//input to the server and getting 
	// back the response and provide it to learner
	public String step(SmtpInputs in) throws SULException {
		try {
			if (smtpConnection == null) {
				System.err.println("step: connection lost. Resetting. Input: " + in.toString());
				connect();
			} else {
				System.err.println("step: input: "+ in.toString());
			}
		String response = null;
		switch (in) {
		case IEHLO:
			return smtpConnection.ehlo();
		case IMAIL:
			return smtpConnection.mail();
		case IRCPT:
			return smtpConnection.rcpt();
		case IDATA:
			response = smtpConnection.data();
			if(response.equals("354")){
				System.err.println("step: data dot: '"+ response+"'");
				return smtpConnection.dot();
			}
			System.err.println("step: data: '"+ response+"'");
			return response;
		case IQUIT:
			response = smtpConnection.quit();
			this.connect();
			return response;
		default:
			throw new SULException(null);
		}
		} catch (IOException ioe) {
			throw new SULException(ioe);
		}

	}

	private String connect() {
		String oa = null;
		if (smtpConnection == null) {
			smtpConnection = new SMTP(this.ip, this.port);
			try {
				if (smtpConnection.connect())
					oa = "OOK";
				else
					oa = "ONOK";
			} catch (IOException ex) {
				System.out.println(ex);
				oa = "ONOK";
			}
		} else {
			smtpConnection.close();
			smtpConnection = new SMTP(this.ip, this.port);
			oa = "OOK";
			try {
				if (smtpConnection.connect())
					oa = "OOK";
				else
					oa = "ONOK";
			} catch (IOException ex) {
				System.out.println(ex);
				oa = "ONOK";
			}
		}
		return oa;

	}

	public Alphabet<SmtpInputs> getInputs() {
		return new EnumAlphabet<SmtpInputs>(SmtpInputs.class,false);
	}

}
