package mypackage;

public class Main  {
	public static void main (String args[]){
		SMTP t1 = new SMTP( "onyx.ttu.ee", 25, "shubhardwaj1@gmail.com",
	             "shubham.bhardwaj@ttu.ee",  "test",  "message");
		t1.start();
		
	}
}