package smtplearning;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class Logger {
	public static void log(String message) throws IOException {
		FileWriter fw = new FileWriter("D:\\MasterThesis\\sutinfo.yaml", true);
		PrintWriter out = new PrintWriter(fw, true);
		Logger log = new Logger();
		out.write("inputInterfaces:\n");
		log.input(message);
		out.write("outputInterfaces:\n");
		log.output(message);

	}

	public void input(String message) throws IOException {
		FileWriter fw = new FileWriter("D:\\MasterThesis\\sutinfo.yaml", true);
		PrintWriter out = new PrintWriter(fw, true);
		if (message.charAt(0) == 'I') {
			out.write(" " + message + "\n");
			out.close();
		}
	}

	public void output(String message) throws IOException {
		FileWriter fw = new FileWriter("D:\\MasterThesis\\sutinfo.yaml", true);
		PrintWriter out = new PrintWriter(fw, true);
		if (message.charAt(0) == 'O') {
			out.write(" " + message + "\n");
			out.close();
		}
	}
}