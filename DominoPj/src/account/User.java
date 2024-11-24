package account;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class User {
	public String id;
	String pw;
	public int win;
	public int loss;
	public int score;
	public int winNum;
	public int tryNum;
	int index;
	
	public void read(Scanner scan) {
		id = scan.next();
		pw = scan.next();
		win = scan.nextInt();
		loss = scan.nextInt();
		score = scan.nextInt();
		winNum = scan.nextInt();
		tryNum = scan.nextInt();
	}
	
	public void newUser(String id, String pw) {
		this.id = id;
		this.pw = pw;
		win = 0;
		loss = 0;
		score = 500;
		winNum = 0;
		tryNum = 0;
	}
	
	public String printName() {
		return id;
	}
	
	public String printWinLoss() {
		return "win: "+win+" loss: "+loss;
	}
	
	public void printToTxt()throws IOException {
		PrintWriter fw = new PrintWriter(new FileWriter("login.txt", true));
		System.out.println();
		fw.printf("%s %s %s %s %s %s %s\n", id, pw, win, loss, score, winNum, tryNum);
		fw.close();
	}
	//게임종류, 모드, 승패, 진행라운드, 플레이타임, 점수변동을 입력받아 gamelog폴더에 아이디txt파일에 출력
	public void printGameLog(String type, String mod, String winLoss, String round, String time, String scoreChange)throws IOException {
		PrintWriter fw = new PrintWriter(new FileWriter("gamelog/"+id+".txt", true));
		System.out.println();
		fw.printf("%s %s %s %s %s %s\n", type, mod, winLoss, round, time, scoreChange);
		fw.close();
	}

}