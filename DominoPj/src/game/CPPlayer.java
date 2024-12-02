package game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CPPlayer extends Player {
	int cpnum;
	CPPlayer (int num, int mtsizew, int mtsizeh){
		super(mtsizew, mtsizeh);
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
		printCPTiles();															//상대는 항상 낼 수 있는 타일 중 합이 가장 큰 타일은 냄
		Tile putTile=null;
		int flag=0;
		if(domino.gameTile.size()==0) {
			System.out.printf("\t\t\t\t\t\t\t\t\t\t\t\t");
			Tile h=ptList.get(0);
			for(Tile t:ptList) {
				if(h.tilesum()<t.tilesum()) h=t;
			}
			for(Tile t:ptList) if(t.leftnum==t.rightnum) h=t;
			for(Tile t:ptList) {
				if(t.tilesum()%domino.modenum==0) h=t;
			}
			for(Tile t:ptList) if(domino.modenum==3&&(t.leftnum==3&&t.rightnum==3)) h=t;
			for(Tile t:ptList) {
				if(domino.modenum==3&&(t.leftnum==6&&t.rightnum==6)) h=t;
				if(domino.modenum==5&&(t.leftnum==5&&t.rightnum==5)) h=t;
			}
			
			ptList.get(ptList.indexOf(h)).print();
			System.out.println("를 냅니다");
			whatcpput(domino,h);
			domino.gameTile.add(ptList.remove(ptList.indexOf(h)));
			setCPPane(domino);
			putTileimg(domino, h, -3);
			domino.setleftright();	
			return;
		}
		for(Tile t:ptList) {
			if(t.tilematches(domino.mostleft, domino.mostright)||(domino.canputtb&&t.tilematches(domino.mosttop,domino.mostbot))) flag=1;
		}
		int drawtimes=0;
		if (flag == 0&&domino.modenum!=1) {
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
				System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t(?,?) 드로우");
				ptList.add(newTile);
				//애니메이션
				domino.waiting(1);
				setCPPane(domino);
				drawCount(domino);
				if (newTile.tilematches(domino.mostleft, domino.mostright)||(domino.canputtb&&newTile.tilematches(domino.mosttop,domino.mostbot))) {
					System.out.printf("\t\t\t\t\t\t\t\t\t\t\t\t드로우 끝\n\t\t\t\t\t\t\t\t\t\t\t\t");
					printCPTiles();
					flag = 1;
					break;
				}
				domino.waiting();
			}
		}
		if (flag == 1) { // 낼 타일 고르기(여러개면 합이 가장 큰 타일을 냄)
			ArrayList<Tile> canPut = new ArrayList<Tile>();
			for (Tile t : ptList) {
				if (t.tilematches(domino.mostleft, domino.mostright))
					canPut.add(t);
				if(domino.canputtb&&t.tilematches(domino.mosttop, domino.mostbot))
					canPut.add(t);
			}
			Tile highest = canPut.get(0);
			for (Tile t : canPut) {
				if (highest.tilesum() < t.tilesum())
					highest = t;
			}
			for (Tile t:canPut) 
				if(t.leftnum==t.rightnum) highest=t;
			for(Tile t:canPut) {
				if(findbiggestscore(domino, highest)<findbiggestscore(domino, t)) highest=t;
			}
			if(firstdouble==null) {
				for(Tile t:canPut) 
					if(t.isdouble()) highest=t;
			}
			putTile = ptList.remove(ptList.indexOf(highest));
			System.out.printf("\t\t\t\t\t\t\t\t\t\t\t\t");
			putTile.print();
			System.out.printf("를 냅니다.\n\t\t\t\t\t\t\t\t\t\t\t\t");

			domino.waiting();
			int putpossible = domino.putnum(putTile);
			int putside=-1;
			
			ArrayList<Integer> selectput=new ArrayList<Integer>();
			if(putTile.tilematches(domino.mostleft)) selectput.add(0);
			if(putTile.tilematches(domino.mostright)) selectput.add(1);
			if(putTile.tilematches(domino.mosttop)&&domino.canputtb) selectput.add(2);
			if(putTile.tilematches(domino.mostbot)&&domino.canputtb) selectput.add(3);
			Collections.shuffle(selectput);
			if(domino.modenum==3||domino.modenum==5)putside=findcangetscore(domino,putTile);
			if(putside==-1) putside=selectput.get(0);
			
			domino.putnewTile(putTile, putside);
			if (!domino.canputtb&&firstdouble != null
					&& !(domino.gameTile.get(0).leftnum == firstdouble.leftnum
							&& domino.gameTile.get(0).rightnum == firstdouble.rightnum)
					&& !(domino.gameTile.get(domino.gameTile.size() - 1).leftnum == firstdouble.leftnum
							&& domino.gameTile.get(domino.gameTile.size() - 1).rightnum == firstdouble.rightnum)) {
				domino.canputtb = true;
			}
			setCPPane(domino);
			whatcpput(domino,highest);
			putTileimg(domino, highest, putside);
			domino.setleftright();
			domino.endcount[cpnum] = 0;
			return;
		}
		domino.endcount[cpnum]=1;
		cpcantput(domino);
	}
	
	int findcangetscore(DominoUI domino, Tile t) {
		int biggestscore=0;
		int rtn=-1;
		if (domino.gTiletb.size() == 0) {
			if (t.leftnum == domino.mostleft && (domino.edgesum - domino.mostleft + t.rightnum) % domino.modenum == 0
					&& domino.edgesum - domino.mostleft + t.rightnum > biggestscore) {
				biggestscore = domino.edgesum - domino.mostleft + t.rightnum;
				rtn = 0;
			}
			if (t.rightnum == domino.mostleft && (domino.edgesum - domino.mostleft + t.leftnum) % domino.modenum == 0
					&& domino.edgesum - domino.mostleft + t.leftnum > biggestscore) {
				biggestscore = domino.edgesum - domino.mostleft + t.leftnum;
				rtn = 0;
			}
			if (t.leftnum == domino.mostright && (domino.edgesum - domino.mostright + t.rightnum) % domino.modenum == 0
					&& domino.edgesum - domino.mostright + t.rightnum > biggestscore) {
				biggestscore = domino.edgesum - domino.mostright + t.rightnum;
				rtn = 1;
			}
			if (t.rightnum == domino.mostright && (domino.edgesum - domino.mostright + t.leftnum) % domino.modenum == 0
					&& domino.edgesum - domino.mostright + t.leftnum > biggestscore) {
				biggestscore = domino.edgesum - domino.mostright + t.leftnum;
				rtn = 1;
			}

		} else if (domino.canputtb) {
			// 각 면에 맞는 조건에서 가장 큰 점수를 추적
			if (t.leftnum == domino.mostleft && (domino.edgesum - domino.mostleft + t.rightnum) % domino.modenum == 0
					&& domino.edgesum - domino.mostleft + t.rightnum > biggestscore) {
				biggestscore = domino.edgesum - domino.mostleft + t.rightnum;
				rtn = 0;
			}

			if (t.rightnum == domino.mostleft && (domino.edgesum - domino.mostleft + t.leftnum) % domino.modenum == 0
					&& domino.edgesum - domino.mostleft + t.leftnum > biggestscore) {
				biggestscore = domino.edgesum - domino.mostleft + t.leftnum;
				rtn = 0;
			}

			if (t.leftnum == domino.mostright && (domino.edgesum - domino.mostright + t.rightnum) % domino.modenum == 0
					&& domino.edgesum - domino.mostright + t.rightnum > biggestscore) {
				biggestscore = domino.edgesum - domino.mostright + t.rightnum;
				rtn = 1;
			}

			if (t.rightnum == domino.mostright && (domino.edgesum - domino.mostright + t.leftnum) % domino.modenum == 0
					&& domino.edgesum - domino.mostright + t.leftnum > biggestscore) {
				biggestscore = domino.edgesum - domino.mostright + t.leftnum;
				rtn = 1;
			}
			if (domino.puttop) {
				if (t.leftnum == domino.mosttop && (domino.edgesum - domino.mosttop + t.rightnum) % domino.modenum == 0
						&& domino.edgesum - domino.mosttop + t.rightnum > biggestscore) {
					biggestscore = domino.edgesum - domino.mosttop + t.rightnum;
					rtn = 2;
				}

				if (t.rightnum == domino.mosttop && (domino.edgesum - domino.mosttop + t.leftnum) % domino.modenum == 0
						&& domino.edgesum - domino.mosttop + t.leftnum > biggestscore) {
					biggestscore = domino.edgesum - domino.mosttop + t.leftnum;
					rtn = 2;
				}
			}
			if (domino.putbot) {
				if (t.leftnum == domino.mostbot && (domino.edgesum - domino.mostbot + t.rightnum) % domino.modenum == 0
						&& domino.edgesum - domino.mostbot + t.rightnum > biggestscore) {
					biggestscore = domino.edgesum - domino.mostbot + t.rightnum;
					rtn = 3;
				}

				if (t.rightnum == domino.mostbot && (domino.edgesum - domino.mostbot + t.leftnum) % domino.modenum == 0
						&& domino.edgesum - domino.mostbot + t.leftnum > biggestscore) {
					biggestscore = domino.edgesum - domino.mostbot + t.leftnum;
					rtn = 3;
				}
			}
		}
		return rtn;
	}
	int findbiggestscore(DominoUI domino, Tile t) {
		int biggestscore=0;	
		if (domino.gTiletb.size() == 0) {
			if (t.leftnum == domino.mostleft && (domino.edgesum - domino.mostleft + t.rightnum) % domino.modenum == 0
					&& domino.edgesum - domino.mostleft + t.rightnum > biggestscore) {
				biggestscore = domino.edgesum - domino.mostleft + t.rightnum;
			}
			if (t.rightnum == domino.mostleft && (domino.edgesum - domino.mostleft + t.leftnum) % domino.modenum == 0
					&& domino.edgesum - domino.mostleft + t.leftnum > biggestscore) {
				biggestscore = domino.edgesum - domino.mostleft + t.leftnum;
			}
			if (t.leftnum == domino.mostright && (domino.edgesum - domino.mostright + t.rightnum) % domino.modenum == 0
					&& domino.edgesum - domino.mostright + t.rightnum > biggestscore) {
				biggestscore = domino.edgesum - domino.mostright + t.rightnum;
			}
			if (t.rightnum == domino.mostright && (domino.edgesum - domino.mostright + t.leftnum) % domino.modenum == 0
					&& domino.edgesum - domino.mostright + t.leftnum > biggestscore) {
				biggestscore = domino.edgesum - domino.mostright + t.leftnum;
			}

		} else if (domino.canputtb) {
			// 각 면에 맞는 조건에서 가장 큰 점수를 추적
			if (t.leftnum == domino.mostleft && (domino.edgesum - domino.mostleft + t.rightnum) % domino.modenum == 0
					&& domino.edgesum - domino.mostleft + t.rightnum > biggestscore) {
				biggestscore = domino.edgesum - domino.mostleft + t.rightnum;
			}

			if (t.rightnum == domino.mostleft && (domino.edgesum - domino.mostleft + t.leftnum) % domino.modenum == 0
					&& domino.edgesum - domino.mostleft + t.leftnum > biggestscore) {
				biggestscore = domino.edgesum - domino.mostleft + t.leftnum;
			}

			if (t.leftnum == domino.mostright && (domino.edgesum - domino.mostright + t.rightnum) % domino.modenum == 0
					&& domino.edgesum - domino.mostright + t.rightnum > biggestscore) {
				biggestscore = domino.edgesum - domino.mostright + t.rightnum;
			}

			if (t.rightnum == domino.mostright && (domino.edgesum - domino.mostright + t.leftnum) % domino.modenum == 0
					&& domino.edgesum - domino.mostright + t.leftnum > biggestscore) {
				biggestscore = domino.edgesum - domino.mostright + t.leftnum;
			}
			if (domino.puttop) {
				if (t.leftnum == domino.mosttop && (domino.edgesum - domino.mosttop + t.rightnum) % domino.modenum == 0
						&& domino.edgesum - domino.mosttop + t.rightnum > biggestscore) {
					biggestscore = domino.edgesum - domino.mosttop + t.rightnum;
				}

				if (t.rightnum == domino.mosttop && (domino.edgesum - domino.mosttop + t.leftnum) % domino.modenum == 0
						&& domino.edgesum - domino.mosttop + t.leftnum > biggestscore) {
					biggestscore = domino.edgesum - domino.mosttop + t.leftnum;
				}
			}
			if (domino.putbot) {
				if (t.leftnum == domino.mostbot && (domino.edgesum - domino.mostbot + t.rightnum) % domino.modenum == 0
						&& domino.edgesum - domino.mostbot + t.rightnum > biggestscore) {
					biggestscore = domino.edgesum - domino.mostbot + t.rightnum;
				}

				if (t.rightnum == domino.mostbot && (domino.edgesum - domino.mostbot + t.leftnum) % domino.modenum == 0
						&& domino.edgesum - domino.mostbot + t.leftnum > biggestscore) {
					biggestscore = domino.edgesum - domino.mostbot + t.leftnum;
				}
			}
		}
		return biggestscore;
	}
	
	private void putTileimg(DominoUI domino, Tile t, int putside) {
		firstX=domino.gamepane.getWidth()/2-gtsizew/2;
		firstY=domino.gamepane.getHeight()/2-gtsizeh/2;
		int rotatec=0;
		String tname=t.imagename;
		System.out.printf("%s, 왼%d, 오%d\n", tname, t.leftnum, t.rightnum);
		BufferedImage newimg=Imgmgr.loadImage(tname);
		boolean isdouble=(t.leftnum==t.rightnum)?true:false;
		if((domino.modenum==3||domino.modenum==5)&&firstdouble==null&&isdouble) {
			firstdouble=t;
			updateInfo(domino,"스피너 타일이 배치되었습니다.");
			if(putside==-3) {
				tX=firstX+gtsizeh/2;
				tY=firstY-gtsizew-gtsizeh/2;
				bX=firstX+gtsizeh/2;
				bY=firstY+gtsizeh+gtsizeh/2;
			}
			else if(putside==0) {
				tX=lX+gtsizeh;
				tY=lY-gtsizew-gtsizeh/2;
				bX=lX+gtsizeh;
				bY=lY+gtsizeh+gtsizeh/2;
			}
			else if(putside==1) {
				tX=rX;
				tY=rY-gtsizew-gtsizeh/2;
				bX=rX;
				bY=rY+gtsizeh+gtsizeh/2;
			}
			domino.gTiletb.add(t);
			topturn=0;
			botturn=0;
		}
		
		if(putside==-3) {							//CP선공 시 구현
			if(isdouble) {
				BufferedImage rotatedimg=Imgmgr.rotateimg(tname, 1);
				BufferedImage resizedimg=Imgmgr.resizeImage(rotatedimg, gtsizeh, gtsizew);
				ImageIcon putimg=new ImageIcon(resizedimg);
				JLabel toput=new JLabel(putimg);
				toput.setBounds(firstX+gtsizeh/2, firstY-gtsizeh/2, gtsizeh, gtsizew);
				domino.gamepane.add(toput);
				rX=firstX+gtsizew-gtsizeh/2;
				rY=firstY;
				lX=firstX-gtsizew+gtsizeh/2;
				lY=firstY;
			} else {
				BufferedImage newimg1=Imgmgr.loadImage(tname);
				BufferedImage resizedimg=Imgmgr.resizeImage(newimg1, gtsizew, gtsizeh);
				ImageIcon putimgicon = new ImageIcon(resizedimg);
				JLabel toput = new JLabel(putimgicon);
				System.out.printf("둔 곳: 475, 200\n");
				toput.setBounds(firstX, firstY, gtsizew, gtsizeh);
				domino.gamepane.add(toput);

				rX = firstX + gtsizew;
				rY = firstY;
				lX = firstX - gtsizew;
				lY = firstY;
			}
			leftturn=0;
			rightturn=0;
			
			//frame to panel
			// 레이아웃 갱신 후 화면 업데이트
	        domino.gamePanel.revalidate(); // 레이아웃 재계산
	        domino.gamePanel.repaint();// 화면을 다시 그리기
			return;
		}
		if(isdouble) rotatec++;
		if(putside==0) {				//왼쪽에 붙이기
			if(t.leftnum==domino.mostleft) {
				rotatec+=2;
			}
			
			else if(lY+gtsizew*2>domino.gamepane.getHeight()) leftturn=2;
			
			if(leftturn>=1) rotatec+=3;				//위->아래
			if(leftturn>=2) rotatec+=3;				//왼->오른
			
			
			BufferedImage rotatedimg=Imgmgr.rotateimg(tname, rotatec);
			BufferedImage resizedimg=null;
			if(rotatec%2==0) resizedimg=Imgmgr.resizeImage(rotatedimg, gtsizew, gtsizeh);
			else resizedimg=Imgmgr.resizeImage(rotatedimg, gtsizeh, gtsizew);
			ImageIcon puticon=new ImageIcon(resizedimg);
			JLabel toput = new JLabel(puticon);
			

			if (leftturn==0) {		//오른->왼
				System.out.printf("(왼쪽)둔 곳: %d, %d\n", lX,lY);
				if(isdouble) {
					toput.setBounds(lX+gtsizeh, lY-gtsizeh/2, gtsizeh, gtsizew);
					lX = lX - gtsizeh;
					if (lX < gtsizeh/2) {
						lX=lX+gtsizew;
						leftturn = 1;
						lY = lY + gtsizeh+gtsizeh/2;
					}
				} else {
					toput.setBounds(lX, lY, gtsizew, gtsizeh);
					lX = lX - gtsizew;
					if (lX < gtsizeh/2) {
						lX=lX+gtsizew;
						leftturn = 1;
						lY = lY + gtsizeh;
					}
				}
				
			} else if(leftturn==1){	//위->아래
				System.out.printf("(왼쪽)둔 곳: %d, %d\n", lX,lY);
				if(isdouble) {
					toput.setBounds(lX-gtsizeh/2, lY, gtsizew, gtsizeh);
					lY=lY+gtsizeh;
					if (lY + gtsizew > domino.gamepane.getHeight() - gtsizeh / 2) {
						lY = lY - gtsizeh;
						leftturn = 2;
						lX = lX + gtsizeh+gtsizeh/2;
					}
				}
				else {
					toput.setBounds(lX, lY, gtsizeh, gtsizew);
					lY = lY + gtsizew;
					if (lY + gtsizew > domino.gamepane.getHeight() - gtsizeh / 2) {
						lY = lY - gtsizeh;
						leftturn = 2;
						lX = lX + gtsizeh;
					}
				}
			} else if (leftturn == 2) { // 왼->오른
				System.out.printf("(왼쪽)둔 곳: %d, %d\n", lX, lY);
				if(isdouble) {
					toput.setBounds(lX, lY-gtsizeh/2, gtsizeh, gtsizew);
					lX=lX+gtsizeh;
				} else {
					toput.setBounds(lX, lY, gtsizew, gtsizeh);
					lX = lX + gtsizew;
				}
			}
			
			domino.gamepane.add(toput);
			//frame to panel
			// 레이아웃 갱신 후 화면 업데이트
	    domino.gamePanel.revalidate(); // 레이아웃 재계산
	    domino.gamePanel.repaint();    // 화면을 다시 그리기
	    return;
		}
		else if(putside==1) {				//오른쪽에 붙이기
			if(t.rightnum==domino.mostright) {
				rotatec+=2;
				System.out.println("회전!");
			}
			;
			
			if(rightturn>=1) rotatec+=3;				//아래->위
			if(rightturn>=2) rotatec+=3;				//오른->왼
			
			BufferedImage rotatedimg=Imgmgr.rotateimg(tname, rotatec);
			BufferedImage resizedimg=null;
			if(rotatec%2==0) resizedimg=Imgmgr.resizeImage(rotatedimg, gtsizew, gtsizeh);
			else resizedimg=Imgmgr.resizeImage(rotatedimg, gtsizeh, gtsizew);
			ImageIcon puticon=new ImageIcon(resizedimg);
			JLabel toput=new JLabel(puticon);
			
			if (rightturn==0) {		//왼->오른
				System.out.printf("(오른쪽)둔 곳: %d, %d\n", rX,rY);
				if(isdouble) {
					toput.setBounds(rX, rY-gtsizeh/2, gtsizeh, gtsizew);
					rX=rX+gtsizeh;
					if (rX + gtsizew > domino.gamepane.getWidth()-gtsizeh/2) {
						rX = rX - gtsizeh;
						rightturn = 1;
						rY = rY - gtsizew-gtsizeh/2;
					}
				}else {
					toput.setBounds(rX, rY, gtsizew, gtsizeh);
					rX = rX + gtsizew;
					if (rX + gtsizew > domino.gamepane.getWidth()-gtsizeh/2) {
						rX = rX - gtsizeh;
						rightturn = 1;
						rY = rY - gtsizew;
					}
				}
			}
			else if (rightturn==1) { // 아래->위
				System.out.printf("(오른쪽)둔 곳: %d, %d\n", rX, rY);
				if (isdouble) {
					toput.setBounds(rX - gtsizeh / 2, rY + gtsizeh, gtsizew, gtsizeh);
					rY = rY - gtsizeh;
					if (rY < gtsizeh / 2) {
						rY = rY + gtsizew;
						rX = rX - gtsizew - gtsizeh / 2;
						rightturn = 2;
					}
				} else {
					toput.setBounds(rX, rY, gtsizeh, gtsizew);
					rY = rY - gtsizew;
					if (rY < gtsizeh / 2) {
						rY = rY + gtsizew;
						rX = rX - gtsizew;
						rightturn = 2;
					}
				}
			} else if (rightturn == 2) { // 오른->왼
				System.out.printf("(오른쪽)둔 곳: %d, %d\n", rX, rY);
				if (isdouble) {
					toput.setBounds(rX + gtsizeh, rY - gtsizeh / 2, gtsizeh, gtsizew);
					rX = rX - gtsizeh;
				} else {
					toput.setBounds(rX, rY, gtsizew, gtsizeh);
					rX = rX - gtsizew;
				}
			} 
			
			domino.gamepane.add(toput);
			//frame to panel
			// 레이아웃 갱신 후 화면 업데이트
	    domino.gamePanel.revalidate(); // 레이아웃 재계산
	    domino.gamePanel.repaint(); 	// 화면을 다시 그리기
	    return;
		}
		else if(putside==2) {	//위쪽에 붙이기
			if(!domino.puttop) domino.puttop=true;
			rotatec++;
			if(t.leftnum==domino.mosttop) rotatec+=2;

			if(topturn>=1) rotatec++;
			if(topturn>=2) rotatec+=3;
			if(topturn>=3) rotatec+=3;
			
			BufferedImage rotatedimg=Imgmgr.rotateimg(tname, rotatec);
			BufferedImage resizedimg=null;
			if(rotatec%2==0) resizedimg=Imgmgr.resizeImage(rotatedimg, gtsizew, gtsizeh);
			else resizedimg=Imgmgr.resizeImage(rotatedimg, gtsizeh, gtsizew);
			ImageIcon puticon=new ImageIcon(resizedimg);
			JLabel toput=new JLabel(puticon);
			
			if(topturn==0) {	//아래->위
				toput.setBounds(tX, tY, gtsizeh, gtsizew);
				tY = tY - gtsizew;
				if(tX>=firstX) topturn=2;
				else if(tX<firstX){
					topturn=1;
					tY=tY+gtsizew;
					tX=tX+gtsizeh;
				}
			}
			else if(topturn==1) {	//왼->오른 (왼쪽에서 시작할 때만)
				if(isdouble) {
					toput.setBounds(tX, tY-gtsizeh/2, gtsizeh, gtsizew);
					tX=tX+gtsizeh;
					if (tX + gtsizew >= firstX) {
						tX = tX - gtsizeh;
						topturn = 2;
						tY = tY - gtsizew-gtsizeh/2;
					}
				}else {
					toput.setBounds(tX, tY, gtsizew, gtsizeh);
					tX = tX + gtsizew;
					if (tX + gtsizew >= firstX) {
						tX = tX - gtsizeh;
						topturn = 2;
						tY = tY - gtsizew;
					}
				}
			}
			else if(topturn==2) {	//아래->위 
				if (isdouble) {
					toput.setBounds(tX - gtsizeh / 2, tY + gtsizeh, gtsizew, gtsizeh);
					tY = tY - gtsizeh;
					if (tY < gtsizeh+gtsizew) {
						tY = tY + gtsizew;
						tX = tX - gtsizew - gtsizeh / 2;
						topturn = 3;
					}
				} else {
					toput.setBounds(tX, tY, gtsizeh, gtsizew);
					tY = tY - gtsizew;
					if (tY < gtsizeh+gtsizew) {
						tY = tY + gtsizew;
						tX = tX - gtsizew;
						topturn = 3;
					}
				}
			}
			else if(topturn==3) {
				if (isdouble) {
					toput.setBounds(tX + gtsizeh, tY - gtsizeh / 2, gtsizeh, gtsizew);
					tX = tX - gtsizeh;
				} else {
					toput.setBounds(tX, tY, gtsizew, gtsizeh);
					tX = tX - gtsizew;
				}
			}
			domino.gamepane.add(toput);
			//frame to panel
			// 레이아웃 갱신 후 화면 업데이트
	    domino.gamePanel.revalidate(); // 레이아웃 재계산
	    domino.gamePanel.repaint(); 	// 화면을 다시 그리기
	    return;
		}
		else if(putside==3) {		//아래에 붙이기
			if(!domino.putbot) domino.putbot=true;
			rotatec++;
			if(t.rightnum==domino.mostbot) rotatec+=2;
			if(botturn>=1) rotatec++;
			if(botturn>=2) rotatec+=3;
			if(botturn>=3) rotatec+=3;
			
			BufferedImage rotatedimg=Imgmgr.rotateimg(tname, rotatec);
			BufferedImage resizedimg=null;
			if(rotatec%2==0) resizedimg=Imgmgr.resizeImage(rotatedimg, gtsizew, gtsizeh);
			else resizedimg=Imgmgr.resizeImage(rotatedimg, gtsizeh, gtsizew);
			ImageIcon puticon=new ImageIcon(resizedimg);
			JLabel toput=new JLabel(puticon);
			
			if(botturn==0) {	//위->아래
				toput.setBounds(bX, bY, gtsizeh, gtsizew);
				bY = bY + gtsizew;
				if (bX<=firstX+gtsizeh) botturn=2;
				else {
					botturn=1;
					bY=bY-gtsizeh;
					bX=bX-gtsizew;
				
				}
			}
			else if(botturn==1) {	//오른->왼 (오른쪽에서 시작할 때만)
				if(isdouble) {
					toput.setBounds(bX+gtsizeh, bY-gtsizeh/2, gtsizeh, gtsizew);
					bX = bX - gtsizeh;
					if (bX <= firstX+gtsizeh) {
						bX=bX+gtsizew;
						botturn = 2;
						bY = bY + gtsizeh+gtsizeh/2;
					}
				} else {
					toput.setBounds(bX, bY, gtsizew, gtsizeh);
					bX = bX - gtsizew;
					if (bX <= firstX+gtsizeh) {
						bX=bX+gtsizew;
						botturn = 2;
						bY = bY + gtsizeh;
					}
				}
			}
			else if(botturn==2) {	//위->아래
				if(isdouble) {
					toput.setBounds(bX-gtsizeh/2, bY, gtsizew, gtsizeh);
					bY=bY+gtsizeh;
					if (bY + gtsizew > domino.gamepane.getHeight() - gtsizeh-gtsizew) {
						bY = bY - gtsizeh;
						botturn = 2;
						bX = bX + gtsizeh+gtsizeh/2;
					}
				}
				else {
					toput.setBounds(bX, bY, gtsizeh, gtsizew);
					bY = bY + gtsizew;
					if (bY + gtsizew > domino.gamepane.getHeight() - gtsizeh-gtsizew) {
						bY = bY - gtsizeh;
						botturn = 3;
						bX = bX + gtsizeh;
					}
				}
			}
			else if(botturn==3) {
				if(isdouble) {
					toput.setBounds(bX, bY-gtsizeh/2, gtsizeh, gtsizew);
					bX=bX+gtsizeh;
				} else {
					toput.setBounds(bX, bY, gtsizew, gtsizeh);
					bX = bX + gtsizew;
				}
			}
			domino.gamepane.add(toput);
			// 레이아웃 갱신 후 화면 업데이트
	    domino.gamePanel.revalidate(); // 레이아웃 재계산
	    domino.gamePanel.repaint(); 	// 화면을 다시 그리기
	    return;
		}
		
		
		
		
		// 레이아웃 갱신 후 화면 업데이트
        /*domino.mainFrame.revalidate(); // 레이아웃 재계산
        domino.mainFrame.repaint();*/    // 화면을 다시 그리기
	}
	void setCPPane(DominoUI domino) {
		
		Component[] components = domino.CPpane.getComponents();
        for (Component component : components) {
            if (component instanceof JLabel) {
                domino.CPpane.remove(component);  // JLabel인 컴포넌트 제거
            }
        }
        if(ptList.size()==9) {
        domino.CPpane.revalidate();
        domino.CPpane.repaint();
        }
        int ctsizew=domino.mtsizew;
        int ctsizeh=domino.mtsizeh;
		/*
		 * JLabel text=new JLabel("CP"); text.setFont(domino.font);
		 * text.setPreferredSize(new Dimension(domino.mtsizew*2+10,12));
		 * domino.CPpane.add(text);
		 */
        if(ptList.size()>=9) {
        	ctsizew=(int)ctsizew*3/4;
        	ctsizeh=(int)ctsizeh*3/4;
        }
        for(Tile t:ptList) {
        	BufferedImage newimg=Imgmgr.loadImage("imgsgame/Tileimgback.png");
            BufferedImage resizedimg=Imgmgr.resizeImage(newimg, ctsizew, ctsizeh);
            ImageIcon imgIcon=new ImageIcon(resizedimg);
            JLabel lb=new JLabel(imgIcon);
        	domino.CPpane.add(lb);
        }
        domino.CPpane.revalidate();
        domino.CPpane.repaint();
        
	}
	
	void whatcpput(DominoUI domino, Tile t) {
		JPanel newpane=new JPanel();
		Font cpputfont=new Font("맑은 고딕", Font.PLAIN,15);
		BufferedImage newimg=Imgmgr.rotateimg(t.imagename, 1);
		BufferedImage resizedimg=Imgmgr.resizeImage(newimg, gtsizeh, gtsizew);
		ImageIcon img=new ImageIcon(resizedimg);
		newpane.setLayout(null);
		newpane.setBounds(domino.gamepane.getWidth()-domino.mtsizew, 60, domino.mtsizew, domino.mtsizew+50);
		JLabel tile=new JLabel(img);
		tile.setBounds(domino.mtsizew/2-domino.mtsizeh/2, 0, domino.mtsizeh, domino.mtsizew);
		String str="를 냅니다.";
		JLabel text=new JLabel(str);
		text.setFont(cpputfont);
		text.setBounds(domino.mtsizew/2-str.length()*5, domino.mtsizew, str.length()*10+10, 30);
		newpane.setOpaque(false);
		newpane.add(tile);
		newpane.add(text);
		domino.gamepane.add(newpane);
        domino.gamepane.revalidate(); // 레이아웃 재계산
        domino.gamepane.repaint(); 	// 화면을 다시 그리기
		domino.waiting(4);
		domino.gamepane.remove(newpane);
        domino.gamepane.revalidate(); // 레이아웃 재계산
        domino.gamepane.repaint(); 	// 화면을 다시 그리기
	}
	void cpcantput(DominoUI domino) {
		ImageIcon imgicon=new ImageIcon(Imgmgr.resizeImage("imgsgame/cpcantput.png", domino.mtsizew+20, domino.mtsizew));
		JLabel lb=new JLabel(imgicon);
		lb.setBounds(domino.gamepane.getWidth()-domino.mtsizew-30, 60, domino.mtsizew+20, domino.mtsizew);
		domino.gamepane.add(lb);
        domino.gamepane.revalidate(); // 레이아웃 재계산
        domino.gamepane.repaint(); 	// 화면을 다시 그리기
		domino.waiting(4);
		domino.gamepane.remove(lb);
        domino.gamepane.revalidate(); // 레이아웃 재계산
        domino.gamepane.repaint(); 	// 화면을 다시 그리기
		
	}
	
	void CPgotscore(DominoUI domino, int type) {
		String str=(type==0)?"+"+domino.edgesum:"+"+domino.me.sum;
		JLabel gotscore=new JLabel(str);
		gotscore.setBounds(0, 0, str.length()*20, 40);
		gotscore.setBackground(Color.black);
		gotscore.setForeground(Color.white);
		gotscore.setFont(domino.infofont);
		gotscore.setOpaque(true);
		domino.gamepane.add(gotscore);
		domino.gamepane.revalidate(); // 레이아웃 재계산
		domino.gamepane.repaint(); // 화면을 다시 그리기
		domino.waiting(2);
		domino.gamepane.remove(gotscore);
		domino.gamepane.revalidate(); // 레이아웃 재계산
		domino.gamepane.repaint(); // 화면을 다시 그리기
	}
}
