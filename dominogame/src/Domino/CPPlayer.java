package Domino;

import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class CPPlayer extends Player {
	int cpnum;
	CPPlayer (int num){
		cpnum=num;
	}
	void printCPTiles() {
		for(int k=0;k<ptList.size();k++) {
			System.out.print("(?,?) / ");
		}
		System.out.printf("---%d개\n", ptList.size());
	}
	
	@Override
	void Plyerturn(DominoUI domino, Scanner scan) {
		Random rand=new Random();
		int i;
		System.out.printf("\t\t\t\t\t\t\t\t\t\t\t\t============CP%d 차례============\n\t\t\t\t\t\t\t\t\t\t\t\t", cpnum);	
		updateInfo(domino,"상대 차례");
		printCPTiles();															//상대는 항상 낼 수 있는 타일 중 합이 가장 큰 타일은 냄
		Tile putTile=null;
		int flag=0;
		/*if(domino.gameTile.size()==0) {
			System.out.printf("\t\t\t\t\t\t\t\t\t\t\t\t");
			Tile h=ptList.get(0);
			for(Tile t:ptList) {
				if(h.tilesum()<t.tilesum()) h=t;
			}
			ptList.get(ptList.indexOf(h)).print();
			System.out.println("를 냅니다");
			domino.gameTile.add(ptList.remove(ptList.indexOf(h)));
			domino.setleftright();	
			return;
		}*/
		for(Tile t:ptList) {
			if(t.tilematches(domino.mostleft, domino.mostright)) flag=1;
		}
		int drawtimes=0;
		if (flag == 0) {
			System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t낼 수 있는 타일이 없어 타일을 뽑습니다.");
			while (true) {
				if (domino.TileList.size() == 0) {
					System.out.printf("\t\t\t\t\t\t\t\t\t\t\t\t더이상 뽑을 수 있는 타일이 없습니다.\n\t\t\t\t\t\t\t\t\t\t\t\t턴을 넘깁니다--남은 타일:%d\n", ptList.size());
					updateInfo(domino,"상대 차례 : 더 이상 뽑을 타일이 없어 턴을 넘깁니다.");
					domino.waiting();
					break;
				}
				Tile newTile = domino.TileList.remove(0);
				drawtimes++;
				updateInfo(domino,"상대 차례 : 드로우! "+drawtimes+"장");
				System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t(?,?) 드로우");
				ptList.add(newTile);
				setCPPane(domino);
				drawCount(domino);
				if (newTile.tilematches(domino.mostleft, domino.mostright)) {
					System.out.printf("\t\t\t\t\t\t\t\t\t\t\t\t드로우 끝\n\t\t\t\t\t\t\t\t\t\t\t\t");
					printCPTiles();
					flag = 1;
					break;
				}
				domino.waiting();
			}
		}
		if (flag == 1) { // 낼 타일 고르기(여러개면 합이 가장 큰 타일을 냄)
			updateInfo(domino, "상대 차례");
			ArrayList<Tile> canPut = new ArrayList<Tile>();
			for (Tile t : ptList) {
				if (t.tilematches(domino.mostleft, domino.mostright))
					canPut.add(t);
			}
			Tile highest = canPut.get(0);
			for (Tile t : canPut) {
				if (highest.tilesum() < t.tilesum())
					highest = t;
			}
			putTile = ptList.remove(ptList.indexOf(highest)); // 합이 제일 큰 타일의 인덱스를 찾고 그 인데스의 타일을 제거하고 꺼내옴
			System.out.printf("\t\t\t\t\t\t\t\t\t\t\t\t");
			putTile.print();
			System.out.printf("를 냅니다.\n\t\t\t\t\t\t\t\t\t\t\t\t");

			domino.waiting();
			int putpossible = domino.putnum(putTile);
			if (putpossible == 2) {
				int putside = 0;
				if (putside <= 0) {
					putTile.print();
					System.out.printf("를 왼쪽에 둡니다.\n");
					domino.putnewTile(putTile, 0);
				} else {
					putTile.print();
					System.out.printf("를 오른쪽에 둡니다.\n");
					domino.putnewTile(putTile, 1);
				}
			} else if (putpossible == 1) {
				if (domino.mostleft == putTile.leftnum || domino.mostleft == putTile.rightnum) {
					putTile.print();
					System.out.printf("를 왼쪽에 둡니다.\n");
					domino.putnewTile(putTile, 0);
				} else if (domino.mostright == putTile.leftnum || domino.mostright == putTile.rightnum) {
					putTile.print();
					System.out.printf("를 오른쪽에 둡니다.\n");
					domino.putnewTile(putTile, 1);
				}
			}
			setCPPane(domino);
			putTileimg(domino, highest);
			domino.setleftright();
			domino.waiting();
			domino.endcount[cpnum] = 0;
			return;
		}
		domino.endcount[cpnum]=1;
	}
	
	private void putTileimg(DominoUI domino, Tile t) {
		int gtsizew=domino.mtsizew/2;
		int gtsizeh=domino.mtsizeh/2;
		int rotatec=0;
		String tname=t.imagename;
		System.out.printf("%s, 왼%d, 오%d\n", tname, t.leftnum, t.rightnum);
		BufferedImage newimg=Imgmgr.loadImage(tname);

		System.out.println("=================!!!#7894561");
		
		/*if(domino.firsttile==null) {							CP선공 시 구현
			toput=new JLabel(putimgicon);
			toput.setBounds(600, 350, 100, 50);
			domino.firsttile=ptList.get(p);
		}*/
		if(t.leftnum==domino.mostleft||t.rightnum==domino.mostleft) {				//왼쪽에 붙이기
			if(t.leftnum==domino.mostleft) {
				rotatec+=2;
				System.out.println("회전!");
			}
			if(domino.lgtile>=6) rotatec+=3;				//위->아래
			if(domino.lgtile>=7) rotatec+=3;				//왼->오른
			if(domino.lgtile>=18) rotatec+=1;				//위->아래
			if(domino.lgtile>=19) rotatec+=1;				//오른->왼
			
			
			BufferedImage rotatedimg=Imgmgr.rotateimg(tname, rotatec);
			BufferedImage resizedimg=null;
			if(rotatec%2==0) resizedimg=Imgmgr.resizeImage(rotatedimg, gtsizew, gtsizeh);
			else resizedimg=Imgmgr.resizeImage(rotatedimg, gtsizeh, gtsizew);
			ImageIcon puticon=new ImageIcon(resizedimg);
			JLabel toput = new JLabel(puticon);
			
			if (domino.lgtile>=1&&domino.lgtile<6) {		//오른->왼
				System.out.printf("(왼쪽)둔 곳: %d, %d\n", 475 - domino.lgtile * gtsizew, 200);
				toput.setBounds(475 - domino.lgtile * gtsizew, 200, gtsizew, gtsizeh);
			} else if(domino.lgtile>=6&&domino.lgtile<7){	//위->아래
				System.out.printf("(왼쪽)둔 곳: %d, %d\n", 225, 225);
				toput.setBounds(225, 225, gtsizeh, gtsizew);
			}
			else if(domino.lgtile>=7&&domino.lgtile<18){	//왼->오른
				System.out.printf("(왼쪽)둔 곳: %d, %d\n", 250+(domino.lgtile-9)*gtsizew, 250);
				toput.setBounds(250+(domino.lgtile-7)*gtsizew, 250, gtsizew, gtsizeh);
			}
			else if(domino.lgtile>=18&&domino.lgtile<19) {	//위->아래
				toput.setBounds(775, 275, gtsizeh, gtsizew);
			}
			else if(domino.lgtile>=19) {					//오른->왼
				toput.setBounds(725-(domino.lgtile-19)*gtsizew, 300, gtsizew, gtsizeh);
			}
			
			domino.lgtile++;
			domino.gamepane.add(toput);
			// 레이아웃 갱신 후 화면 업데이트
	        domino.mainFrame.revalidate(); // 레이아웃 재계산
	        domino.mainFrame.repaint();    // 화면을 다시 그리기
	        return;
		}
		else if(t.leftnum==domino.mostright||t.rightnum==domino.mostright) {				//오른쪽에 붙이기
			if(t.rightnum==domino.mostright) {
				rotatec+=2;
				System.out.println("회전!");
			}
			
			if(domino.rgtile>=6) rotatec+=3;				//아래->위
			if(domino.rgtile>=7) rotatec+=3;				//오른->왼
			if(domino.rgtile>=18) rotatec+=1;				//아래->위
			if(domino.rgtile>=19) rotatec+=1;				//왼->오른
			
			BufferedImage rotatedimg=Imgmgr.rotateimg(tname, rotatec);
			BufferedImage resizedimg=null;
			if(rotatec%2==0) resizedimg=Imgmgr.resizeImage(rotatedimg, gtsizew, gtsizeh);
			else resizedimg=Imgmgr.resizeImage(rotatedimg, gtsizeh, gtsizew);
			ImageIcon puticon=new ImageIcon(resizedimg);
			JLabel toput=new JLabel(puticon);
			
			if (domino.rgtile>=1&&domino.rgtile<6) {		//왼->오른
				System.out.printf("(오른쪽)둔 곳: %d, %d\n", 475 + domino.rgtile * gtsizew, 200);
				toput.setBounds(475 + domino.rgtile * gtsizew, 200, gtsizew, gtsizeh);
			}
			else if (domino.rgtile >= 6 && domino.rgtile < 7) { // 아래->위
				System.out.printf("(오른쪽)둔 곳: %d, %d\n", 750, 150);
				toput.setBounds(750, 150, gtsizeh, gtsizew);
			} 
			else if (domino.rgtile >= 7 && domino.rgtile < 18) { // 오른->왼
				System.out.printf("(오른쪽)둔 곳: %d, %d\n", 700 - (domino.rgtile - 7) * gtsizew, gtsizeh);
				toput.setBounds(700 - (domino.rgtile - 7) * gtsizew, 150, gtsizew, gtsizeh);
			} 
			else if (domino.rgtile >= 18 && domino.rgtile < 19) { // 아래->위
				toput.setBounds(200, 100, gtsizeh, gtsizew);
			} 
			else if (domino.rgtile >= 19) { // 오른->왼
				toput.setBounds(225 + (domino.rgtile - 19) * gtsizew, 100, gtsizew, gtsizeh);
			}
			
			domino.rgtile++;
			domino.gamepane.add(toput);
			// 레이아웃 갱신 후 화면 업데이트
	        domino.mainFrame.revalidate(); // 레이아웃 재계산
	        domino.mainFrame.repaint(); 	// 화면을 다시 그리기
	        return;
		}
		
		
		
		// 레이아웃 갱신 후 화면 업데이트
        domino.mainFrame.revalidate(); // 레이아웃 재계산
        domino.mainFrame.repaint();    // 화면을 다시 그리기
	}

	void setCPPane(DominoUI domino) {
		Component[] components = domino.CPpane.getComponents();
		if(components.length!=0)domino.CPpane.remove(components[components.length-1]);
		JLabel cpTiles=new JLabel("X"+ptList.size());
		cpTiles.setBounds(560,40,domino.mtsizeh,domino.mtsizeh);
        cpTiles.setFont(domino.font);
        domino.CPpane.add(cpTiles);
        domino.CPpane.revalidate();
        domino.CPpane.repaint();
	}
	
}
