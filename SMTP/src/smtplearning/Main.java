package smtplearning;

import java.util.Scanner;

public class Main {
	public static void main(String args[]) {
		Scanner reader = new Scanner(System.in); // Reading from System.in
		System.out.println("Enter host name: ");
		String host = reader.nextLine();
		Scanner reader1 = new Scanner(System.in);
		System.out.println("Enter port number: ");
		int port = reader1.nextInt();
		// System.out.println("Enter recipient: ");
		// String recipient = reader.next();
		// System.out.println("Enter sender: ");
		// String sender = reader.next();
		// System.out.println("Enter Subject: ");
		// String subject = reader.next();
		// System.out.println("Enter message: ");
		// StringBuilder a = new StringBuilder();
		// String message=reader.next();
		// while (message.equals(".")) {
		// a.append(message);
		// }
		SUT t1 = new SUT(host, port);

		// SMTP t1 = new SMTP(host, port);
		try {
			t1.start();

		} catch (Exception e) {
			// t1.close();
			System.out.println("Can not send e-mail!");
			e.printStackTrace();
		}
		// t1.close();

	}
}