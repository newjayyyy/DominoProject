package domino;

import java.util.Scanner;

public class User {
	String id;
	String pw;
	
	public void read(Scanner scan) {
		id = scan.next();
		pw = scan.next();
	}

}
