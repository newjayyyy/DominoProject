package mypage;

import java.util.Scanner;

public class Log {
	String mod;
	String winLoss;
	String round;
	String time;
	String scoreChange;
	public void read(Scanner scan) {
		mod = scan.next();
		winLoss = scan.next();
		round = scan.next();
		time = scan.next();
		scoreChange = scan.next();		
	}
	public String getMod() {
		// TODO Auto-generated method stub
		return mod;
	}
	public String getWinLoss() {
		// TODO Auto-generated method stub
		return winLoss;
	}
	public String getRound() {
		// TODO Auto-generated method stub
		return round;
	}
	public String getTime() {
		// TODO Auto-generated method stub
		return time;
	}
	public String getScoreChange() {
		// TODO Auto-generated method stub
		return scoreChange;
	}

}