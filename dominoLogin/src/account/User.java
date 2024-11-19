package domino;

import java.util.Scanner;

public class User {
	String id;
	String pw;
	int win;
	int loss;
	int score;
	int winNum;
	int tryNum;
	
	public void read(Scanner scan) {
		id = scan.next();
		pw = scan.next();
		win = scan.nextInt();
		loss = scan.nextInt();
		score = scan.nextInt();
		winNum = scan.nextInt();
		tryNum = scan.nextInt();
	}

}
