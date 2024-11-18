package Domino;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Player {
	private static final Object lock = new Object();  // 동기화를 위한 객체
	int sum=0;
	int score=0;
	ArrayList<Tile> ptList=new ArrayList<Tile>();
	
	void showtiles() {
		for(Tile t:ptList) {
			t.print();
			System.out.printf(" / ");
		}
	}
	
	void printpossibletile(int l, int r) {
		for(Tile t:ptList) {
			t.print();
			if(t.tilematches(l, r)) System.out.printf("ㅇ");
			System.out.printf(" / ");
		}
	}
	
	void makesum() {
		for(Tile t:ptList) {
			sum+=t.tilesum();
		}
	}
	
	private int p;
	int putpossible;
	int putwhere;
	void Plyerturn (DominoUI domino, Scanner scan) {
		
		int i;
		
		System.out.println("============내 차례============");
		Tile putTile=null;
		int flag=0;
		if(domino.gameTile.size()==0) {
			showtiles();
			System.out.printf("\n몇 번째 타일을 내겠습니까?(처음에는 아무거나 내기)");
			updateInfo(domino,"내 차례 : 낼 타일 고르기 (처음에는 아무거나)");
			
			setMyPane(domino);
			
			synchronized (lock) {
				try {
					System.out.println("프로그램이 잠깐 멈췄습니다...낼 타일 고르기");
					lock.wait(); // 이 시점에서 대기
					System.out.println("라벨 클릭 후 p의 값: " + p); // 대기 후 p 값 출력
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			

			ptList.get(p).print();
			System.out.printf("를 냅니다.\n");
			putTileimg(domino, 0, ptList.get(p));
			domino.gameTile.add(ptList.remove(p));
			domino.setleftright();
			notmyturn(domino);
			return;
		}
		for(Tile t:ptList) {
			if(t.tilematches(domino.mostleft, domino.mostright)) flag=1;
		}	
		int drawtimes=0;
		if (flag == 0) {
			System.out.println("낼 수 있는 타일이 없어 타일을 뽑습니다.");
			while (true) {
				if (domino.TileList.size() == 0) {
					System.out.printf("더이상 뽑을 수 있는 타일이 없습니다.\n턴을 넘깁니다--내 타일:%d개\n", ptList.size());
					updateInfo(domino,"내 차례 : 더 이상 뽑을 타일이 없어 턴을 넘깁니다.");
					domino.waiting();
					break;
				}
				Tile newTile = domino.TileList.remove(0);
				newTile.print();
				drawtimes++;
				System.out.println(" 드로우");
				updateInfo(domino,"내 차례 : 드로우! "+drawtimes+"장");
				ptList.add(newTile);
				setMyPane(domino);
				drawCount(domino);
				if (newTile.tilematches(domino.mostleft, domino.mostright)) {
					System.out.println("드로우 끝");
					flag = 1;
					break;
				}
				domino.waiting();
			}
		}
		if (flag == 1) {
			
			setMyPane(domino);
			printpossibletile(domino.mostleft, domino.mostright);
			System.out.printf("\n\n몇 번째 타일을 내겠습니까?(낼 수 있는 타일은 'ㅇ'으로 표시)\n");
			if(drawtimes>0) {
				updateInfo(domino,"내 차례 : 낼 타일 선택("+drawtimes+"장 드로우 됨)");
			}
			else updateInfo(domino,"내 차례 : 낼 타일 선택");
			
			
			putpossible=-1;
			while (true) { // 낼 타일 고르기
				int q = 0;
				System.out.printf("1~%d선택 : ", ptList.size());
				synchronized (lock) {
					try {
						System.out.println("프로그램이 잠깐 멈췄습니다...낼 타일 고르기");
						lock.wait(); // 이 시점에서 대기
						System.out.println("라벨 클릭 후 p의 값: " + p); // 대기 후 p 값 출력
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (ptList.get(p).tilematches(domino.mostleft, domino.mostright)) {
					ptList.get(p).print();
					System.out.println("는 낼 수 있는 타일입니다. ");
					putTile = ptList.get(p);
					break;

				}
				ptList.get(p).print();
				System.out.println("는 낼 수 없는 타일입니다.");
			}
			putpossible = domino.putnum(putTile);
			putwhere = -1;
			
			while(true) {
				System.out.println("\n p의 값"+p+"=================================");
				putTile=ptList.get(p);
				putpossible=domino.putnum(putTile);
				selectPutWhere(putpossible, putTile, domino);
				updateInfo(domino,"내 차례 : 낼 곳 또는 다른 타일 선택");
				synchronized (lock) {
					try {
						System.out.println("프로그램이 잠깐 멈췄습니다...낼 곳 고르기");
						lock.wait(); // 이 시점에서 대기
						System.out.println("선택완료"); // 대기 후 p 값 출력
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println(putwhere+":putwhere==========================");
				if(putwhere==0||putwhere==1) break;
			}
			ptList.remove(p);
			
			domino.putnewTile(putTile, putwhere);
			putTileimg(domino, putwhere, putTile);
			domino.endcount[0]=0;
			domino.setleftright();

			notmyturn(domino);
			return;
		}
		domino.endcount[0]=1;
	}
	
	
	

	void setMyPane(DominoUI domino) {
		domino.imageList.clear();
		
		Component[] components = domino.mypane.getComponents();
        for (Component component : components) {
            if (component instanceof JLabel) {
                domino.mypane.remove(component);  // JLabel인 컴포넌트 제거
            }
        }
        
     
	    final JLabel[] selectedImage = new JLabel[1]; // 선택된 이미지를 추적할 배열 (크기를 증가시킬 이미지)
	    
	    for(int i=0;i<ptList.size();i++) {
	    	BufferedImage newimg=Imgmgr.loadImage(ptList.get(i).imagename);
	    	if(!(ptList.get(i).tilematches(domino.mostleft, domino.mostright))&&domino.gameTile.size()>=1) newimg=Imgmgr.transparentimg(newimg);
	    	BufferedImage resizedimg=Imgmgr.resizeImage(newimg, domino.mtsizew, domino.mtsizeh);
	    	ImageIcon imgIcon=new ImageIcon(resizedimg);
	    	JLabel imgLabel=new JLabel(imgIcon);

	    	if(ptList.get(i).tilematches(domino.mostleft, domino.mostright)||domino.gameTile.size()==0) {
	    		imgLabel.addMouseListener(new ImageMouseListener(imgLabel, i, selectedImage, domino));
	    		imgLabel.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						// 라벨 클릭 시 대기 상태를 풀고, 값을 반환
						synchronized (lock) {
							lock.notify(); // 대기 중인 A 클래스를 깨운다
							p = domino.imageList.indexOf(imgLabel);
							if (putpossible == 1 || putpossible == 2) {
								Component[] components = domino.gamepane.getComponents();
								int totalComponents = components.length;
								for (int i = totalComponents - 1; i >= totalComponents - putpossible && i >= 0; i--) {
									domino.gamepane.remove(components[i]);
								}
								putwhere = -1;
								domino.gamepane.revalidate();
								domino.gamepane.repaint();
							}
							System.out.println("라벨 클릭됨, 대기 상태에서 깨어남");
						}

					}
				});
	    	}
	    	
	    	domino.imageList.add(imgLabel);
	        
	    }
        for(JLabel lb:domino.imageList) {
        	domino.mypane.add(lb);
        }
        domino.mypane.revalidate();
        domino.mypane.repaint();
	    
	}
	

	void selectPutWhere(int psb, Tile t, DominoUI domino) {
		BufferedImage newimg=Imgmgr.loadImage(t.imagename); 
		BufferedImage tspimg=Imgmgr.transparentimg(newimg);				//투명한 이미지
		if(psb==2) {
			JLabel ifr=maketspLabel(t,domino,1);
			JLabel ifl=maketspLabel(t,domino,0);
			ifr.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					// 라벨 클릭 시 대기 상태를 풀고, 값을 반환
					synchronized (lock) {
						lock.notify(); // 대기 중인 A 클래스를 깨운다
						putwhere=1;
						domino.gamepane.remove(ifl);	//ifl없애기
						domino.gamepane.revalidate(); // 레이아웃 갱신
			            domino.gamepane.repaint();    // 화면 업데이트
						
					}
				}
				public void mouseEntered(MouseEvent e) {
			        ifr.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			    }
				public void mouseExited(MouseEvent e) {
			        ifr.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			    }
			});
			
			ifl.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					// 라벨 클릭 시 대기 상태를 풀고, 값을 반환
					synchronized (lock) {
						lock.notify(); // 대기 중인 A 클래스를 깨운다
						putwhere=0;
						domino.gamepane.remove(ifr);	//ifr없애기
						domino.gamepane.revalidate(); // 레이아웃 갱신
			            domino.gamepane.repaint();    // 화면 업데이트
					}
				}
				public void mouseEntered(MouseEvent e) {
			        ifl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			    }
				public void mouseExited(MouseEvent e) {
			        ifl.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			    }
				
			});
			domino.gamepane.add(ifl);
			domino.gamepane.add(ifr);
			domino.gamepane.revalidate(); // 레이아웃 갱신
            domino.gamepane.repaint();    // 화면 업데이트
			return;
		}
		else if (psb == 1) {
			if (t.tilematches_right(domino.mostright)) {
				JLabel ifr = maketspLabel(t, domino, 1);
				ifr.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						// 라벨 클릭 시 대기 상태를 풀고, 값을 반환
						synchronized (lock) {
							lock.notify(); // 대기 중인 A 클래스를 깨운다
							putwhere=1;
							
						}
					}
					public void mouseEntered(MouseEvent e) {
				        ifr.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				    }
					public void mouseExited(MouseEvent e) {
				        ifr.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				    }
				});
				domino.gamepane.add(ifr);
				// 레이아웃 갱신 후 화면 업데이트
		        domino.mainFrame.revalidate(); // 레이아웃 재계산
		        domino.mainFrame.repaint();    // 화면을 다시 그리기
				return;
				
			} else if (t.tilematches_left(domino.mostleft)) {
				JLabel ifl = maketspLabel(t, domino, 0);
				ifl.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						// 라벨 클릭 시 대기 상태를 풀고, 값을 반환
						synchronized (lock) {
							lock.notify(); // 대기 중인 A 클래스를 깨운다
							putwhere=0;
						}
					}
					public void mouseEntered(MouseEvent e) {
				        ifl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				    }
					public void mouseExited(MouseEvent e) {
				        ifl.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				    }
				});
				domino.gamepane.add(ifl);
				// 레이아웃 갱신 후 화면 업데이트
		        domino.mainFrame.revalidate(); // 레이아웃 재계산
		        domino.mainFrame.repaint();    // 화면을 다시 그리기
				return;
			}
		}
	}
	
	JLabel maketspLabel(Tile t, DominoUI domino, int lr) {
		int gtsizew=domino.mtsizew/2;
		int gtsizeh=domino.mtsizeh/2;
		JLabel rtnLabel=null;
		BufferedImage newimg=Imgmgr.loadImage(t.imagename); 
		BufferedImage tspimg=Imgmgr.transparentimg(newimg);				//투명한 이미지
		if(lr==0) {
			int rotatec=0;
			if(t.leftnum==domino.mostleft) rotatec+=2;
			if(domino.lgtile>=6) rotatec+=3;				//위->아래
			if(domino.lgtile>=7) rotatec+=3;				//왼->오른
			if(domino.lgtile>=18) rotatec+=1;				//위->아래
			if(domino.lgtile>=19) rotatec+=1;				//오른->왼
			BufferedImage rotatedimg=Imgmgr.rotateimg(tspimg, rotatec);
			BufferedImage resizedimg=null;
			if(rotatec%2==0) resizedimg=Imgmgr.resizeImage(rotatedimg, gtsizew, gtsizeh);
			else resizedimg=Imgmgr.resizeImage(rotatedimg, gtsizeh, gtsizew);
			ImageIcon puticon=new ImageIcon(resizedimg);
			JLabel toput=new JLabel(puticon);
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
			return toput;
		}
		else if(lr==1) {
			int rotatec=0;
			if(t.rightnum==domino.mostright) rotatec+=2;
			if(domino.rgtile>=6) rotatec+=3;				//아래->위
			if(domino.rgtile>=7) rotatec+=3;				//오른->왼
			if(domino.rgtile>=18) rotatec+=1;				//아래->위
			if(domino.rgtile>=19) rotatec+=1;				//왼->오른
			
			BufferedImage rotatedimg=Imgmgr.rotateimg(tspimg, rotatec);
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
			return toput;
		}
		
		
		return rtnLabel;
	}
	
	
	protected void putTileimg(DominoUI domino, int putwhere, Tile putTile) {
		int gtsizew=(int)(domino.mtsizew/2);
		int gtsizeh=(int)(domino.mtsizeh/2);
		int rotatec=0;
		Tile t=putTile;
		Icon icon = domino.imageList.get(p).getIcon();
		ImageIcon imageIconInLabel = (ImageIcon) icon;
		Image image = imageIconInLabel.getImage();
		
		
		if(domino.gameTile.size()==0) {
			Image scaledImage = image.getScaledInstance(gtsizew, gtsizeh, Image.SCALE_SMOOTH);
			ImageIcon putimgicon=new ImageIcon(scaledImage);
			JLabel toput=new JLabel(putimgicon);
			System.out.printf("둔 곳: 475, 200\n");
			toput.setBounds(475, 200, gtsizew, gtsizeh);
			domino.gamepane.add(toput);
			
			domino.lgtile++;
			domino.rgtile++;
			
			// 레이아웃 갱신 후 화면 업데이트
	        domino.gamepane.revalidate(); // 레이아웃 재계산
	        domino.gamepane.repaint();    // 화면을 다시 그리기
			return;
		}
		
		if(putwhere==0) {				//왼쪽에 붙이기
			if(t.leftnum==domino.mostleft) {
				rotatec+=2;
				System.out.println("회전!");
			}
			if(domino.lgtile>=6) rotatec+=3;				//위->아래
			if(domino.lgtile>=7) rotatec+=3;				//왼->오른
			if(domino.lgtile>=18) rotatec+=1;				//위->아래
			if(domino.lgtile>=19) rotatec+=1;				//오른->왼
			
			
			BufferedImage rotatedimg=Imgmgr.rotateimg(image, rotatec);
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
	        domino.gamepane.revalidate(); // 레이아웃 재계산
	        domino.gamepane.repaint();    // 화면을 다시 그리기
	        return;
		}
		
		if(putwhere==1) {				//오른쪽에 붙이기
			if(t.rightnum==domino.mostright) {
				rotatec+=2;
				System.out.println("회전!");
			}
			
			if(domino.rgtile>=6) rotatec+=3;				//아래->위
			if(domino.rgtile>=7) rotatec+=3;				//오른->왼
			if(domino.rgtile>=18) rotatec+=1;				//아래->위
			if(domino.rgtile>=19) rotatec+=1;				//왼->오른
			
			BufferedImage rotatedimg=Imgmgr.rotateimg(image, rotatec);
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
	        domino.gamepane.revalidate(); // 레이아웃 재계산
	        domino.gamepane.repaint(); 	// 화면을 다시 그리기
	        return;
		}
		
	}

	void notmyturn(DominoUI domino) {
		domino.imageList.clear();

		Component[] components = domino.mypane.getComponents();
		for (Component component : components) {
			if (component instanceof JLabel) {
				domino.mypane.remove(component); // JLabel인 컴포넌트 제거
			}
		}

		int xpos = 0;
		int ypos = 5;
		int gap = 10;

		for (int i = 0; i < ptList.size(); i++) {
			BufferedImage newimg = Imgmgr.loadImage(ptList.get(i).imagename);
			newimg = Imgmgr.transparentimg(newimg);
			BufferedImage resizedimg = Imgmgr.resizeImage(newimg, domino.mtsizew, domino.mtsizeh);
			ImageIcon imgIcon = new ImageIcon(resizedimg);
			JLabel imgLabel = new JLabel(imgIcon);
			imgLabel.setBounds(xpos, ypos, domino.mtsizew, domino.mtsizeh);

			
			domino.imageList.add(imgLabel);

		}
		for (JLabel lb : domino.imageList) {
			domino.mypane.add(lb);
		}
		domino.mypane.revalidate();
		domino.mypane.repaint();
	}
	
	
	
	void drawCount(DominoUI domino) {
		Component[] components = domino.Drpane.getComponents();
		int totalComponents = components.length;
		if(components.length!=0)domino.Drpane.remove(components[components.length-1]);
		JLabel countdraw=new JLabel("X"+domino.TileList.size());
		countdraw.setBounds(56,175,domino.mtsizeh,domino.mtsizeh);
        countdraw.setFont(domino.font);
        domino.Drpane.add(countdraw);
        domino.Drpane.revalidate();
        domino.Drpane.repaint();
	}
	
	void updateInfo(DominoUI domino,String message) {
		Component[] components = domino.infopane.getComponents();
		int totalComponents = components.length;
		if(components.length>=3)domino.infopane.remove(components[components.length-1]);
		JLabel msglb=new JLabel(message);
		msglb.setFont(domino.infofont);
		msglb.setBounds(500-message.length()*10, 0, message.length()*20, 50);
		domino.infopane.add(msglb);
		domino.infopane.revalidate();
		domino.infopane.repaint();
	}

}
