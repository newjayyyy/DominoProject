package game;

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
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import account.LoginForm;

public class Player {
	private static final Object lock = new Object(); // 동기화를 위한 객체
	int sum = 0;
	int score = 0;
	static int gtsizew;
	static int gtsizeh;
	static int leftturn;
	static int rightturn;
	static int topturn;
	static int botturn;
	static Tile firstdouble;
	ArrayList<Tile> ptList = new ArrayList<Tile>();

	public Player(int mtsizew, int mtsizeh) {
		Player.gtsizew = 80;
		Player.gtsizeh = 40;
	}

	void showtiles() {
		for (Tile t : ptList) {
			t.print();
			System.out.printf(" / ");
		}
	}

	void printpossibletile(int l, int r) {
		for (Tile t : ptList) {
			t.print();
			if (t.tilematches(l, r))
				System.out.printf("ㅇ");
			System.out.printf(" / ");
		}
	}

	void makesum() {
		for (Tile t : ptList) {
			sum += t.tilesum();
		}
	}

	private int p;
	int putpossible;
	int putwhere;

	void Plyerturn(DominoUI domino, Scanner scan) {

		int i;
		int wrongchoice = 0;
		System.out.println("============내 차례============");
		myturn(domino);
		Tile putTile = null;
		int flag = 0;
		if (domino.gameTile.size() == 0) {
			showtiles();
			System.out.printf("\n몇 번째 타일을 내겠습니까?(처음에는 아무거나 내기)");

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
		for (Tile t : ptList) {
			if (t.tilematches(domino.mostleft, domino.mostright)
					|| (domino.canputtb && t.tilematches(domino.mosttop, domino.mostbot)))
				flag = 1;
		}
		int drawtimes = 0;
		if (flag == 0 && domino.modenum != 1) {
			System.out.println("낼 수 있는 타일이 없어 타일을 뽑습니다.");
			while (true) {
				if (domino.TileList.size() == 0) {
					System.out.printf("더이상 뽑을 수 있는 타일이 없습니다.\n턴을 넘깁니다--내 타일:%d개\n", ptList.size());
					updateInfo(domino, "낼 수 있는 타일과 뽑을 수 있는 타일이 없어 턴을 넘깁니다.");
					domino.waiting(5);
					break;
				}
				drawCountfordraw(domino);
				setMyPanefordraw(domino);
				synchronized (lock) {
					try {
						System.out.println("프로그램이 잠깐 멈췄습니다...새로운 본 뽑기");
						lock.wait(); // 이 시점에서 대기
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				Tile newTile = domino.TileList.remove(0);
				newTile.print();
				drawtimes++;
				System.out.println(" 드로우");
				ptList.add(newTile);
				// 애니메이션
				if (newTile.tilematches(domino.mostleft, domino.mostright)
						|| (domino.canputtb && newTile.tilematches(domino.mosttop, domino.mostbot))) {
					System.out.println("드로우 끝");
					flag = 1;
					break;
				}
			}
		}
		if (flag == 1) {
			drawCount(domino);
			setMyPane(domino);
			printpossibletile(domino.mostleft, domino.mostright);
			System.out.printf("\n\n몇 번째 타일을 내겠습니까?(낼 수 있는 타일은 'ㅇ'으로 표시)\n");

			putpossible = -1;
			while (true) { // 낼 타일 고르기
				if (wrongchoice >= 3)
					kindsetMyPane(domino);
				synchronized (lock) {
					try {
						System.out.println("프로그램이 잠깐 멈췄습니다...낼 타일 고르기");
						lock.wait(); // 이 시점에서 대기
						System.out.println("라벨 클릭 후 p의 값: " + p); // 대기 후 p 값 출력
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (p == -1) {
					wrongchoice++;
					continue;
				}
				if (ptList.get(p).tilematches(domino.mostleft, domino.mostright)
						|| ptList.get(p).tilematches(domino.mosttop, domino.mostbot)) {
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

			while (true) {
				if (p != -1) {
					putTile = ptList.get(p);
					putpossible = domino.putnum(putTile);
					selectPutWhere(putpossible, putTile, domino);
				}
				synchronized (lock) {
					try {
						System.out.println("프로그램이 잠깐 멈췄습니다...낼 곳 고르기");
						lock.wait(); // 이 시점에서 대기
						System.out.println("선택완료"); // 대기 후 p 값 출력
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (putwhere == 0 || putwhere == 1 || putwhere == 2 || putwhere == 3)
					break;
			}
			p = ptList.indexOf(putTile);
			ptList.remove(p);
			domino.putnewTile(putTile, putwhere);
			if (!domino.canputtb && firstdouble != null
					&& !(domino.gameTile.get(0).leftnum == firstdouble.leftnum
							&& domino.gameTile.get(0).rightnum == firstdouble.rightnum)
					&& !(domino.gameTile.get(domino.gameTile.size() - 1).leftnum == firstdouble.leftnum
							&& domino.gameTile.get(domino.gameTile.size() - 1).rightnum == firstdouble.rightnum)) {
				domino.canputtb = true;
			}
			putTileimg(domino, putwhere, putTile);

			domino.endcount[0] = 0;
			domino.setleftright();
			putTile.print();
			notmyturn(domino);
			return;
		}
		domino.endcount[0] = 1;
		if (domino.modenum == 1) {
			updateInfo(domino, "낼 수 있는 타일과 뽑을 수 있는 타일이 없어 턴을 넘깁니다.");
		}
	}

	void setMyPane(DominoUI domino) {
		domino.imageList.clear();
		Component[] components = domino.mypane.getComponents();
		for (Component component : components) {
			if (component instanceof JLabel) {
				domino.mypane.remove(component); // JLabel인 컴포넌트 제거
			}
		}

		final JLabel[] selectedImage = new JLabel[1]; // 선택된 이미지를 추적할 배열 (크기를 증가시킬 이미지)

		for (int i = 0; i < ptList.size(); i++) {
			BufferedImage newimg = Imgmgr.loadImage(ptList.get(i).imagename);
			BufferedImage resizedimg = Imgmgr.resizeImage(newimg, domino.mtsizew, domino.mtsizeh);
			ImageIcon imgIcon = new ImageIcon(resizedimg);
			JLabel imgLabel = new JLabel(imgIcon);

			if (ptList.get(i).tilematches(domino.mostleft, domino.mostright)
					|| ptList.get(i).tilematches(domino.mosttop, domino.mostbot) || domino.gameTile.size() == 0) {
				imgLabel.addMouseListener(new ImageMouseListener(imgLabel, i, selectedImage, domino));
				imgLabel.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						// 라벨 클릭 시 대기 상태를 풀고, 값을 반환
						synchronized (lock) {
							lock.notify(); // 대기 중인 A 클래스를 깨운다
							p = domino.imageList.indexOf(imgLabel);

							Component[] components = domino.gamepane.getComponents();
							int totalComponents = components.length;
							int ifremove = (domino.canputtb) ? 4 : 2;
							if ((!domino.canputtb && totalComponents > domino.gameTile.size()) || (domino.canputtb
									&& totalComponents > (domino.gameTile.size() + domino.gTiletb.size() - 1))) {
								for (int i = totalComponents - 1; i >= totalComponents - ifremove && i >= 0; i--) {
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
			} else {
				imgLabel.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						updateInfo(domino, "낼 수 없는 타일입니다");
						synchronized (lock) {
							lock.notify(); // 대기 중인 A 클래스를 깨운다
							p = -1;
						}
					}

					public void mouseEntered(MouseEvent e) {
						imgLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					}

					public void mouseExited(MouseEvent e) {
						imgLabel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					}
				});
			}

			domino.imageList.add(imgLabel);

		}
		for (JLabel lb : domino.imageList) {
			domino.mypane.add(lb);
		}
		domino.mypane.revalidate();
		domino.mypane.repaint();
	}

	void kindsetMyPane(DominoUI domino) {
		domino.imageList.clear();
		Component[] components = domino.mypane.getComponents();
		for (Component component : components) {
			if (component instanceof JLabel) {
				domino.mypane.remove(component); // JLabel인 컴포넌트 제거
			}
		}

		final JLabel[] selectedImage = new JLabel[1]; // 선택된 이미지를 추적할 배열 (크기를 증가시킬 이미지)

		for (int i = 0; i < ptList.size(); i++) {
			BufferedImage newimg = Imgmgr.loadImage(ptList.get(i).imagename);
			if (!(ptList.get(i).tilematches(domino.mostleft, domino.mostright))
					&& !(ptList.get(i).tilematches(domino.mosttop, domino.mostbot)) && domino.gameTile.size() >= 1)
				newimg = Imgmgr.transparentimg(newimg);
			BufferedImage resizedimg = Imgmgr.resizeImage(newimg, domino.mtsizew, domino.mtsizeh);
			ImageIcon imgIcon = new ImageIcon(resizedimg);
			JLabel imgLabel = new JLabel(imgIcon);

			if (ptList.get(i).tilematches(domino.mostleft, domino.mostright)
					|| ptList.get(i).tilematches(domino.mosttop, domino.mostbot) || domino.gameTile.size() == 0) {
				imgLabel.addMouseListener(new ImageMouseListener(imgLabel, i, selectedImage, domino));
				imgLabel.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						// 라벨 클릭 시 대기 상태를 풀고, 값을 반환
						synchronized (lock) {
							lock.notify(); // 대기 중인 A 클래스를 깨운다
							p = domino.imageList.indexOf(imgLabel);

							Component[] components = domino.gamepane.getComponents();
							int totalComponents = components.length;
							int ifremove = (domino.canputtb) ? 4 : 2;
							if ((!domino.canputtb && totalComponents > domino.gameTile.size()) || (domino.canputtb
									&& totalComponents > (domino.gameTile.size() + domino.gTiletb.size() - 1))) {
								for (int i = totalComponents - 1; i >= totalComponents - ifremove && i >= 0; i--) {
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
		for (JLabel lb : domino.imageList) {
			domino.mypane.add(lb);
		}
		domino.mypane.revalidate();
		domino.mypane.repaint();
	}

	static int firstX, firstY, rX, rY, lX, lY, tX, tY, bX, bY, topfirstX, botfirstX;

	void selectPutWhere(int psb, Tile t, DominoUI domino) {
		firstX = domino.gamepane.getWidth() / 2 - gtsizew / 2;
		firstY = domino.gamepane.getHeight() / 2 - gtsizeh / 2;
		BufferedImage newimg = Imgmgr.loadImage(t.imagename);
		BufferedImage tspimg = Imgmgr.transparentimg(newimg); // 투명한 이미지
		JLabel ifl = maketspLabel(t, domino, 0);
		JLabel ifr = maketspLabel(t, domino, 1);
		JLabel ift = maketspLabel(t, domino, 2);
		JLabel ifb = maketspLabel(t, domino, 3);
		ifl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 라벨 클릭 시 대기 상태를 풀고, 값을 반환
				if (t.tilematches(domino.mostleft)) {
					synchronized (lock) {
						lock.notify(); // 대기 중인 A 클래스를 깨운다
						putwhere = 0;
						domino.gamepane.remove(ifl); // ifl없애기
						domino.gamepane.remove(ifr); // ifr없애기
						domino.gamepane.remove(ift); // ift없애기
						domino.gamepane.remove(ifb); // ifb없애기
						domino.gamepane.revalidate(); // 레이아웃 갱신
						domino.gamepane.repaint(); // 화면 업데이트
					}
				} else {
					updateInfo(domino, "둘 수 없는 곳입니다");
				}
			}

			public void mouseEntered(MouseEvent e) {
				ifl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}

			public void mouseExited(MouseEvent e) {
				ifl.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}

		});

		ifr.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 라벨 클릭 시 대기 상태를 풀고, 값을 반환
				if (t.tilematches(domino.mostright)) {
					synchronized (lock) {
						lock.notify(); // 대기 중인 A 클래스를 깨운다
						putwhere = 1;
						domino.gamepane.remove(ifl); // ifl없애기
						domino.gamepane.remove(ifr); // ifr없애기
						domino.gamepane.remove(ift); // ift없애기
						domino.gamepane.remove(ifb); // ifb없애기
						domino.gamepane.revalidate(); // 레이아웃 갱신
						domino.gamepane.repaint(); // 화면 업데이트

					}
				} else {
					updateInfo(domino, "둘 수 없는 곳입니다");
				}
			}

			public void mouseEntered(MouseEvent e) {
				ifr.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}

			public void mouseExited(MouseEvent e) {
				ifr.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});

		ift.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 라벨 클릭 시 대기 상태를 풀고, 값을 반환
				if (domino.canputtb && t.tilematches(domino.mosttop)) {
					synchronized (lock) {
						lock.notify(); // 대기 중인 A 클래스를 깨운다
						putwhere = 2;
						domino.gamepane.remove(ifl); // ifl없애기
						domino.gamepane.remove(ifr); // ifr없애기
						domino.gamepane.remove(ift); // ift없애기
						domino.gamepane.remove(ifb); // ifb없애기
						domino.gamepane.revalidate(); // 레이아웃 갱신
						domino.gamepane.repaint(); // 화면 업데이트
					}
				} else {
					updateInfo(domino, "둘 수 없는 곳입니다");
				}
			}

			public void mouseEntered(MouseEvent e) {
				ift.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}

			public void mouseExited(MouseEvent e) {
				ift.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}

		});

		ifb.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 라벨 클릭 시 대기 상태를 풀고, 값을 반환
				if (domino.canputtb && t.tilematches(domino.mostbot)) {
					synchronized (lock) {
						lock.notify(); // 대기 중인 A 클래스를 깨운다
						putwhere = 3;
						domino.gamepane.remove(ifl); // ifl없애기
						domino.gamepane.remove(ifr); // ifr없애기
						domino.gamepane.remove(ift); // ift없애기
						domino.gamepane.remove(ifb); // ifb없애기
						domino.gamepane.revalidate(); // 레이아웃 갱신
						domino.gamepane.repaint(); // 화면 업데이트
					}
				} else {
					updateInfo(domino, "둘 수 없는 곳입니다");
				}
			}

			public void mouseEntered(MouseEvent e) {
				ifb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}

			public void mouseExited(MouseEvent e) {
				ifb.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}

		});

		domino.gamepane.add(ifl);
		domino.gamepane.add(ifr);
		if (domino.canputtb) {
			domino.gamepane.add(ift);
			domino.gamepane.add(ifb);
		}
		domino.gamepane.revalidate(); // 레이아웃 갱신
		domino.gamepane.repaint(); // 화면 업데이트
		return;
	}

	JLabel maketspLabel(Tile t, DominoUI domino, int lr) {
		JLabel rtnLabel = null;
		BufferedImage newimg = Imgmgr.loadImage(t.imagename);
		BufferedImage tspimg = Imgmgr.transparentimg(newimg); // 투명한 이미지
		boolean isdouble = (t.leftnum == t.rightnum) ? true : false;
		Border border = new LineBorder(Color.black, 1);

		if (lr == 0) {
			int rotatec = 0;
			if (t.leftnum == domino.mostleft)
				rotatec += 2;
			if (isdouble)
				rotatec++;

			if (leftturn >= 1)
				rotatec += 3; // 위->아래
			if (leftturn >= 2)
				rotatec += 3; // 왼->오른
			BufferedImage rotatedimg = Imgmgr.rotateimg(tspimg, rotatec);
			BufferedImage resizedimg = null;
			if (rotatec % 2 == 0)
				resizedimg = Imgmgr.resizeImage(rotatedimg, gtsizew, gtsizeh);
			else
				resizedimg = Imgmgr.resizeImage(rotatedimg, gtsizeh, gtsizew);
			ImageIcon puticon = new ImageIcon(resizedimg);
			JLabel toput = new JLabel(" ");
			if (rotatec % 2 == 0)
				toput.setPreferredSize(new Dimension(gtsizew, gtsizeh));
			else
				toput.setPreferredSize(new Dimension(gtsizeh, gtsizew));
			toput.setBorder(border);
			if (leftturn == 0) { // 오른->왼
				if (isdouble)
					toput.setBounds(lX + gtsizeh, lY - gtsizeh / 2, gtsizeh, gtsizew);
				else
					toput.setBounds(lX, lY, gtsizew, gtsizeh);
			} else if (leftturn == 1) { // 위->아래
				if (isdouble)
					toput.setBounds(lX - gtsizeh / 2, lY, gtsizew, gtsizeh);
				else
					toput.setBounds(lX, lY, gtsizeh, gtsizew);
			} else if (leftturn == 2) { // 왼->오른
				if (isdouble)
					toput.setBounds(lX, lY - gtsizeh / 2, gtsizeh, gtsizew);
				else
					toput.setBounds(lX, lY, gtsizew, gtsizeh);
			}
			return toput;
		}

		else if (lr == 1) {
			int rotatec = 0;
			if (t.rightnum == domino.mostright)
				rotatec += 2;
			if (isdouble)
				rotatec++;

			if (rightturn >= 1)
				rotatec += 3; // 아래->위
			if (rightturn >= 2)
				rotatec += 3; // 오른->왼

			BufferedImage rotatedimg = Imgmgr.rotateimg(tspimg, rotatec);
			BufferedImage resizedimg = null;
			if (rotatec % 2 == 0)
				resizedimg = Imgmgr.resizeImage(rotatedimg, gtsizew, gtsizeh);
			else
				resizedimg = Imgmgr.resizeImage(rotatedimg, gtsizeh, gtsizew);
			ImageIcon puticon = new ImageIcon(resizedimg);
			JLabel toput = new JLabel(" ");
			if (rotatec % 2 == 0)
				toput.setPreferredSize(new Dimension(gtsizew, gtsizeh));
			else
				toput.setPreferredSize(new Dimension(gtsizeh, gtsizew));
			toput.setBorder(border);

			if (rightturn == 0) { // 왼->오른
				if (isdouble)
					toput.setBounds(rX, rY - gtsizeh / 2, gtsizeh, gtsizew);
				else
					toput.setBounds(rX, rY, gtsizew, gtsizeh);
			} else if (rightturn == 1) { // 아래->위
				if (isdouble)
					toput.setBounds(rX - gtsizeh / 2, rY + gtsizeh, gtsizew, gtsizeh);
				else
					toput.setBounds(rX, rY, gtsizeh, gtsizew);
			} else if (rightturn == 2) { // 오른->왼
				if (isdouble)
					toput.setBounds(rX + gtsizeh, rY - gtsizeh / 2, gtsizeh, gtsizew);
				else
					toput.setBounds(rX, rY, gtsizew, gtsizeh);
			}
			return toput;
		}

		else if (lr == 2) {
			int rotatec = 0;
			rotatec++;
			if (t.leftnum == domino.mosttop)
				rotatec += 2;
			if (isdouble)
				rotatec++;

			if (topturn >= 1)
				rotatec++;
			if (topturn >= 2)
				rotatec += 3;
			if (topturn >= 3)
				rotatec += 3;

			BufferedImage rotatedimg = Imgmgr.rotateimg(tspimg, rotatec);
			BufferedImage resizedimg = null;
			if (rotatec % 2 == 0)
				resizedimg = Imgmgr.resizeImage(rotatedimg, gtsizew, gtsizeh);
			else
				resizedimg = Imgmgr.resizeImage(rotatedimg, gtsizeh, gtsizew);
			ImageIcon puticon = new ImageIcon(resizedimg);
			JLabel toput = new JLabel(" ");
			if (rotatec % 2 == 0)
				toput.setPreferredSize(new Dimension(gtsizew, gtsizeh));
			else
				toput.setPreferredSize(new Dimension(gtsizeh, gtsizew));
			toput.setBorder(border);

			if (topturn == 0) {
				toput.setBounds(tX, tY, gtsizeh, gtsizew);
			} else if (topturn == 1) {
				if (isdouble)
					toput.setBounds(tX, tY - gtsizeh / 2, gtsizeh, gtsizew);
				else
					toput.setBounds(tX, tY, gtsizew, gtsizeh);
			} else if (topturn == 2) {
				if (isdouble)
					toput.setBounds(tX - gtsizeh / 2, tY + gtsizeh, gtsizew, gtsizeh);
				else
					toput.setBounds(tX, tY, gtsizeh, gtsizew);
			} else if (topturn == 3) {
				if (isdouble)
					toput.setBounds(tX + gtsizeh, tY - gtsizeh / 2, gtsizeh, gtsizew);
				else
					toput.setBounds(tX, tY, gtsizew, gtsizeh);
			}
			return toput;
		} else if (lr == 3) {
			int rotatec = 0;
			rotatec++;
			if (t.rightnum == domino.mostbot)
				rotatec += 2;
			if (isdouble)
				rotatec++;

			if (botturn >= 1)
				rotatec++;
			if (botturn >= 2)
				rotatec += 3;
			if (botturn >= 3)
				rotatec += 3;

			BufferedImage rotatedimg = Imgmgr.rotateimg(tspimg, rotatec);
			BufferedImage resizedimg = null;
			if (rotatec % 2 == 0)
				resizedimg = Imgmgr.resizeImage(rotatedimg, gtsizew, gtsizeh);
			else
				resizedimg = Imgmgr.resizeImage(rotatedimg, gtsizeh, gtsizew);
			ImageIcon puticon = new ImageIcon(resizedimg);
			JLabel toput = new JLabel(" ");
			if (rotatec % 2 == 0)
				toput.setPreferredSize(new Dimension(gtsizew, gtsizeh));
			else
				toput.setPreferredSize(new Dimension(gtsizeh, gtsizew));
			toput.setBorder(border);

			if (botturn == 0) {
				toput.setBounds(bX, bY, gtsizeh, gtsizew);
			} else if (botturn == 1) {
				if (isdouble)
					toput.setBounds(bX + gtsizeh, bY - gtsizeh / 2, gtsizeh, gtsizew);
				else
					toput.setBounds(bX, bY, gtsizew, gtsizeh);
			} else if (botturn == 2) {
				if (isdouble)
					toput.setBounds(bX - gtsizeh / 2, bY, gtsizew, gtsizeh);
				else
					toput.setBounds(bX, bY, gtsizeh, gtsizew);
			} else if (botturn == 3) {
				if (isdouble)
					toput.setBounds(bX, bY - gtsizeh / 2, gtsizeh, gtsizew);
				else
					toput.setBounds(bX, bY, gtsizew, gtsizeh);
			}
			return toput;
		}

		return rtnLabel;
	}

	protected void putTileimg(DominoUI domino, int putwhere, Tile putTile) {
		firstX = domino.gamepane.getWidth() / 2 - gtsizew / 2;
		firstY = domino.gamepane.getHeight() / 2 - gtsizeh / 2;
		int rotatec = 0;
		Tile t = putTile;
		Icon icon = domino.imageList.get(p).getIcon();
		ImageIcon imageIconInLabel = (ImageIcon) icon;
		Image image = imageIconInLabel.getImage();
		boolean isdouble = (putTile.leftnum == putTile.rightnum) ? true : false;
		if ((domino.modenum == 3 || domino.modenum == 5) && firstdouble == null && isdouble) {
			firstdouble = putTile;
			updateInfo(domino, "스피너 타일이 배치되었습니다.");
			if (domino.gameTile.size() == 0) {
				tX = firstX + gtsizeh / 2;
				tY = firstY - gtsizew - gtsizeh / 2;
				bX = firstX + gtsizeh / 2;
				bY = firstY + gtsizeh + gtsizeh / 2;
			} else if (putwhere == 0) {
				tX = lX + gtsizeh;
				tY = lY - gtsizew - gtsizeh / 2;
				bX = lX + gtsizeh;
				bY = lY + gtsizeh + gtsizeh / 2;
			} else if (putwhere == 1) {
				tX = rX;
				tY = rY - gtsizew - gtsizeh / 2;
				bX = rX;
				bY = rY + gtsizeh + gtsizeh / 2;
			}
			domino.gTiletb.add(putTile);
			topturn = 0;
			botturn = 0;
		}

		if (domino.gameTile.size() == 0) {
			if (isdouble) {
				BufferedImage rotatedimg = Imgmgr.rotateimg(image, 1);
				BufferedImage resizedimg = Imgmgr.resizeImage(rotatedimg, gtsizeh, gtsizew);
				ImageIcon putimg = new ImageIcon(resizedimg);
				JLabel toput = new JLabel(putimg);
				toput.setBounds(firstX + gtsizeh / 2, firstY - gtsizeh / 2, gtsizeh, gtsizew);
				domino.gamepane.add(toput);
				rX = firstX + gtsizew - gtsizeh / 2;
				rY = firstY;
				lX = firstX - gtsizew + gtsizeh / 2;
				lY = firstY;
			} else {

				Image scaledImage = image.getScaledInstance(gtsizew, gtsizeh, Image.SCALE_SMOOTH);
				ImageIcon putimgicon = new ImageIcon(scaledImage);
				JLabel toput = new JLabel(putimgicon);
				System.out.printf("둔 곳: 475, 200\n");
				toput.setBounds(firstX, firstY, gtsizew, gtsizeh);
				domino.gamepane.add(toput);

				rX = firstX + gtsizew;
				rY = firstY;
				lX = firstX - gtsizew;
				lY = firstY;
			}
			leftturn = 0;
			rightturn = 0;

			// 레이아웃 갱신 후 화면 업데이트
			domino.gamepane.revalidate(); // 레이아웃 재계산
			domino.gamepane.repaint(); // 화면을 다시 그리기
			return;
		}
		if (isdouble)
			rotatec++;
		if (putwhere == 0) { // 왼쪽에 붙이기
			if (t.leftnum == domino.mostleft) {
				rotatec += 2;
				System.out.println("회전!");
			}

			if (leftturn >= 1)
				rotatec += 3; // 위->아래
			if (leftturn >= 2)
				rotatec += 3; // 왼->오른

			BufferedImage rotatedimg = Imgmgr.rotateimg(image, rotatec);
			BufferedImage resizedimg = null;
			if (rotatec % 2 == 0)
				resizedimg = Imgmgr.resizeImage(rotatedimg, gtsizew, gtsizeh);
			else
				resizedimg = Imgmgr.resizeImage(rotatedimg, gtsizeh, gtsizew);
			ImageIcon puticon = new ImageIcon(resizedimg);
			JLabel toput = new JLabel(puticon);

			if (leftturn == 0) { // 오른->왼
				System.out.printf("(왼쪽)둔 곳: %d, %d\n", lX, lY);
				if (isdouble) {
					toput.setBounds(lX + gtsizeh, lY - gtsizeh / 2, gtsizeh, gtsizew);
					lX = lX - gtsizeh;
					if (lX < gtsizeh / 2) {
						lX = lX + gtsizew;
						leftturn = 1;
						lY = lY + gtsizeh + gtsizeh / 2;
					}
				} else {
					toput.setBounds(lX, lY, gtsizew, gtsizeh);
					lX = lX - gtsizew;
					if (lX < gtsizeh / 2) {
						lX = lX + gtsizew;
						leftturn = 1;
						lY = lY + gtsizeh;
					}
				}

			} else if (leftturn == 1) { // 위->아래
				System.out.printf("(왼쪽)둔 곳: %d, %d\n", lX, lY);
				if (isdouble) {
					toput.setBounds(lX - gtsizeh / 2, lY, gtsizew, gtsizeh);
					lY = lY + gtsizeh;
					if (lY + gtsizew > domino.gamepane.getHeight() - gtsizeh / 2) {
						lY = lY - gtsizeh;
						leftturn = 2;
						lX = lX + gtsizeh + gtsizeh / 2;
					}
				} else {
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
				if (isdouble) {
					toput.setBounds(lX, lY - gtsizeh / 2, gtsizeh, gtsizew);
					lX = lX + gtsizeh;
				} else {
					toput.setBounds(lX, lY, gtsizew, gtsizeh);
					lX = lX + gtsizew;
				}
			}

			domino.gamepane.add(toput);
			// 레이아웃 갱신 후 화면 업데이트
			domino.gamepane.revalidate(); // 레이아웃 재계산
			domino.gamepane.repaint(); // 화면을 다시 그리기
			return;
		}

		else if (putwhere == 1) { // 오른쪽에 붙이기
			if (t.rightnum == domino.mostright) {
				rotatec += 2;
				System.out.println("회전!");
			}

			if (rightturn >= 1)
				rotatec += 3; // 아래->위
			if (rightturn >= 2)
				rotatec += 3; // 오른->왼

			BufferedImage rotatedimg = Imgmgr.rotateimg(image, rotatec);
			BufferedImage resizedimg = null;
			if (rotatec % 2 == 0)
				resizedimg = Imgmgr.resizeImage(rotatedimg, gtsizew, gtsizeh);
			else
				resizedimg = Imgmgr.resizeImage(rotatedimg, gtsizeh, gtsizew);
			ImageIcon puticon = new ImageIcon(resizedimg);
			JLabel toput = new JLabel(puticon);

			if (rightturn == 0) { // 왼->오른
				System.out.printf("(오른쪽)둔 곳: %d, %d\n", rX, rY);
				if (isdouble) {
					toput.setBounds(rX, rY - gtsizeh / 2, gtsizeh, gtsizew);
					rX = rX + gtsizeh;
					if (rX + gtsizew > domino.gamepane.getWidth() - gtsizeh / 2) {
						rX = rX - gtsizeh;
						rightturn = 1;
						rY = rY - gtsizew - gtsizeh / 2;
					}
				} else {
					toput.setBounds(rX, rY, gtsizew, gtsizeh);
					rX = rX + gtsizew;
					if (rX + gtsizew > domino.gamepane.getWidth() - gtsizeh / 2) {
						rX = rX - gtsizeh;
						rightturn = 1;
						rY = rY - gtsizew;
					}
				}
			} else if (rightturn == 1) { // 아래->위
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
			// 레이아웃 갱신 후 화면 업데이트
			domino.gamepane.revalidate(); // 레이아웃 재계산
			domino.gamepane.repaint(); // 화면을 다시 그리기
			return;
		}

		else if (putwhere == 2) { // 위쪽에 붙이기
			if (!domino.puttop)
				domino.puttop = true;
			rotatec++;
			if (putTile.leftnum == domino.mosttop)
				rotatec += 2;

			if (topturn >= 1)
				rotatec++;
			if (topturn >= 2)
				rotatec += 3;
			if (topturn >= 3)
				rotatec += 3;

			BufferedImage rotatedimg = Imgmgr.rotateimg(image, rotatec);
			BufferedImage resizedimg = null;
			if (rotatec % 2 == 0)
				resizedimg = Imgmgr.resizeImage(rotatedimg, gtsizew, gtsizeh);
			else
				resizedimg = Imgmgr.resizeImage(rotatedimg, gtsizeh, gtsizew);
			ImageIcon puticon = new ImageIcon(resizedimg);
			JLabel toput = new JLabel(puticon);

			if (topturn == 0) { // 아래->위
				toput.setBounds(tX, tY, gtsizeh, gtsizew);
				tY = tY - gtsizew;
				if (tX >= firstX)
					topturn = 2;
				else if (tX < firstX) {
					topturn = 1;
					tY = tY + gtsizew;
					tX = tX + gtsizeh;
				}
			} else if (topturn == 1) { // 왼->오른 (왼쪽에서 시작할 때만)
				if (isdouble) {
					toput.setBounds(tX, tY - gtsizeh / 2, gtsizeh, gtsizew);
					tX = tX + gtsizeh;
					if (tX + gtsizew >= firstX) {
						tX = tX - gtsizeh;
						topturn = 2;
						tY = tY - gtsizew - gtsizeh / 2;
					}
				} else {
					toput.setBounds(tX, tY, gtsizew, gtsizeh);
					tX = tX + gtsizew;
					if (tX + gtsizew >= firstX) {
						tX = tX - gtsizeh;
						topturn = 2;
						tY = tY - gtsizew;
					}
				}
			} else if (topturn == 2) { // 아래->위
				if (isdouble) {
					toput.setBounds(tX - gtsizeh / 2, tY + gtsizeh, gtsizew, gtsizeh);
					tY = tY - gtsizeh;
					if (tY < gtsizeh + gtsizew) {
						tY = tY + gtsizew;
						tX = tX - gtsizew - gtsizeh / 2;
						topturn = 3;
					}
				} else {
					toput.setBounds(tX, tY, gtsizeh, gtsizew);
					tY = tY - gtsizew;
					if (tY < gtsizeh + gtsizew) {
						tY = tY + gtsizew;
						tX = tX - gtsizew;
						topturn = 3;
					}
				}
			} else if (topturn == 3) {
				if (isdouble) {
					toput.setBounds(tX + gtsizeh, tY - gtsizeh / 2, gtsizeh, gtsizew);
					tX = tX - gtsizeh;
				} else {
					toput.setBounds(tX, tY, gtsizew, gtsizeh);
					tX = tX - gtsizew;
				}
			}
			domino.gamepane.add(toput);
			// 레이아웃 갱신 후 화면 업데이트
			domino.gamepane.revalidate(); // 레이아웃 재계산
			domino.gamepane.repaint(); // 화면을 다시 그리기
			return;
		} else if (putwhere == 3) { // 아래쪽에 붙이기
			if (!domino.putbot)
				domino.putbot = true;
			rotatec++;
			if (putTile.rightnum == domino.mostbot)
				rotatec += 2;
			if (botturn >= 1)
				rotatec++;
			if (botturn >= 2)
				rotatec += 3;
			if (botturn >= 3)
				rotatec += 3;

			BufferedImage rotatedimg = Imgmgr.rotateimg(image, rotatec);
			BufferedImage resizedimg = null;
			if (rotatec % 2 == 0)
				resizedimg = Imgmgr.resizeImage(rotatedimg, gtsizew, gtsizeh);
			else
				resizedimg = Imgmgr.resizeImage(rotatedimg, gtsizeh, gtsizew);
			ImageIcon puticon = new ImageIcon(resizedimg);
			JLabel toput = new JLabel(puticon);

			if (botturn == 0) { // 위->아래
				toput.setBounds(bX, bY, gtsizeh, gtsizew);
				bY = bY + gtsizew;
				if (bX <= firstX + gtsizeh)
					botturn = 2;
				else {
					botturn = 1;
					bY = bY - gtsizeh;
					bX = bX - gtsizew;

				}
			} else if (botturn == 1) { // 오른->왼 (오른쪽에서 시작할 때만)
				if (isdouble) {
					toput.setBounds(bX + gtsizeh, bY - gtsizeh / 2, gtsizeh, gtsizew);
					bX = bX - gtsizeh;
					if (bX <= firstX + gtsizeh) {
						bX = bX + gtsizew;
						botturn = 2;
						bY = bY + gtsizeh + gtsizeh / 2;
					}
				} else {
					toput.setBounds(bX, bY, gtsizew, gtsizeh);
					bX = bX - gtsizew;
					if (bX <= firstX + gtsizeh) {
						bX = bX + gtsizew;
						botturn = 2;
						bY = bY + gtsizeh;
					}
				}
			} else if (botturn == 2) { // 위->아래
				if (isdouble) {
					toput.setBounds(bX - gtsizeh / 2, bY, gtsizew, gtsizeh);
					bY = bY + gtsizeh;
					if (bY + gtsizew > domino.gamepane.getHeight() - gtsizeh - gtsizew) {
						bY = bY - gtsizeh;
						botturn = 2;
						bX = bX + gtsizeh + gtsizeh / 2;
					}
				} else {
					toput.setBounds(bX, bY, gtsizeh, gtsizew);
					bY = bY + gtsizew;
					if (bY + gtsizew > domino.gamepane.getHeight() - gtsizeh - gtsizew) {
						bY = bY - gtsizeh;
						botturn = 3;
						bX = bX + gtsizeh;
					}
				}
			} else if (botturn == 3) {
				if (isdouble) {
					toput.setBounds(bX, bY - gtsizeh / 2, gtsizeh, gtsizew);
					bX = bX + gtsizeh;
				} else {
					toput.setBounds(bX, bY, gtsizew, gtsizeh);
					bX = bX + gtsizew;
				}
			}
			domino.gamepane.add(toput);
			// 레이아웃 갱신 후 화면 업데이트
			domino.gamepane.revalidate(); // 레이아웃 재계산
			domino.gamepane.repaint(); // 화면을 다시 그리기
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
		for (int i = 0; i < ptList.size(); i++) {
			BufferedImage newimg = Imgmgr.loadImage(ptList.get(i).imagename);
			BufferedImage resizedimg = Imgmgr.resizeImage(newimg, domino.mtsizew, domino.mtsizeh);
			ImageIcon imgIcon = new ImageIcon(resizedimg);
			JLabel imgLabel = new JLabel(imgIcon);
			imgLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					updateInfo(domino, "상대 차례입니다.");
				}

				public void mouseEntered(MouseEvent e) {
					imgLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}

				public void mouseExited(MouseEvent e) {
					imgLabel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			});
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
		for (Component component : components) {
			if (component instanceof JLabel) {
				domino.Drpane.remove(component); // JLabel인 컴포넌트 제거
			}
		}
		if (domino.modenum != 1) {
			BufferedImage drawimg = Imgmgr.loadImage("imgsgame/tileimgback.png");
			JLabel lb = new JLabel(new ImageIcon(drawimg));
			lb.setBounds((3 * domino.mtsizew + 10) / 2 - domino.mtsizew - 20, domino.mtsizeh / 2, domino.mtsizew,
					domino.mtsizeh);
			JLabel countdraw = new JLabel("X" + domino.TileList.size());
			countdraw.setBounds((3 * domino.mtsizew + 10) / 2 - 15, domino.mtsizeh / 2, domino.mtsizeh, domino.mtsizeh);
			countdraw.setFont(domino.font);
			JLabel drbt = new JLabel("draw!");
			drbt.setBackground(Color.black);
			drbt.setForeground(Color.white);
			drbt.setOpaque(true); // 불투명하게 만들어 배경색이 보이도록 설정
			// 텍스트를 가로와 세로로 모두 가운데 정렬
			drbt.setHorizontalAlignment(SwingConstants.CENTER); // 수평 가운데 정렬
			drbt.setVerticalAlignment(SwingConstants.CENTER); // 수직 가운데 정렬
			drbt.setBounds((3 * domino.mtsizew + 10) / 2 + 30, domino.mtsizeh / 2, domino.mtsizew, domino.mtsizeh);
			drbt.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					updateInfo(domino, "낼 수 있는 타일이 있어 뽑을 수 없습니다.");
				}

				public void mouseEntered(MouseEvent e) {
					drbt.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}

				public void mouseExited(MouseEvent e) {
					drbt.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			});
			domino.Drpane.add(lb);
			domino.Drpane.add(countdraw);
			domino.Drpane.add(drbt);
			domino.Drpane.revalidate();
			domino.Drpane.repaint();
		}
	}

	void drawCountfordraw(DominoUI domino) {
		Component[] components = domino.Drpane.getComponents();
		for (Component component : components) {
			if (component instanceof JLabel) {
				domino.Drpane.remove(component); // JLabel인 컴포넌트 제거
			}
		}
		if (domino.modenum != 1) {
			BufferedImage drawimg = Imgmgr.loadImage("imgsgame/tileimgback.png");
			JLabel lb = new JLabel(new ImageIcon(drawimg));
			lb.setBounds((3 * domino.mtsizew + 10) / 2 - domino.mtsizew - 20, domino.mtsizeh / 2, domino.mtsizew,
					domino.mtsizeh);
			JLabel countdraw = new JLabel("X" + domino.TileList.size());
			countdraw.setBounds((3 * domino.mtsizew + 10) / 2 - 15, domino.mtsizeh / 2, domino.mtsizeh, domino.mtsizeh);
			countdraw.setFont(domino.font);
			JLabel drbt = new JLabel("draw!");
			drbt.setBackground(Color.white);
			drbt.setForeground(Color.black);
			drbt.setOpaque(true); // 불투명하게 만들어 배경색이 보이도록 설정
			// 텍스트를 가로와 세로로 모두 가운데 정렬
			drbt.setHorizontalAlignment(SwingConstants.CENTER); // 수평 가운데 정렬
			drbt.setVerticalAlignment(SwingConstants.CENTER); // 수직 가운데 정렬
			drbt.setBounds((3 * domino.mtsizew + 10) / 2 + 30, domino.mtsizeh / 2, domino.mtsizew, domino.mtsizeh);
			drbt.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					updateInfo(domino, "타일을 뽑습니다.");
					synchronized (lock) {
						lock.notify(); // 대기 중인 A 클래스를 깨운다
						System.out.println("라벨 클릭됨, 대기 상태에서 깨어남");
					}
				}

				public void mouseEntered(MouseEvent e) {
					drbt.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}

				public void mouseExited(MouseEvent e) {
					drbt.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			});
			domino.Drpane.add(lb);
			domino.Drpane.add(drbt);
			domino.Drpane.add(countdraw);
			domino.Drpane.revalidate();
			domino.Drpane.repaint();
		}
	}

	void setMyPanefordraw(DominoUI domino) {
		domino.imageList.clear();

		Component[] components = domino.mypane.getComponents();
		for (Component component : components) {
			if (component instanceof JLabel) {
				domino.mypane.remove(component); // JLabel인 컴포넌트 제거
			}
		}

		final JLabel[] selectedImage = new JLabel[1]; // 선택된 이미지를 추적할 배열 (크기를 증가시킬 이미지)

		for (int i = 0; i < ptList.size(); i++) {
			BufferedImage newimg = Imgmgr.loadImage(ptList.get(i).imagename);
			BufferedImage resizedimg = Imgmgr.resizeImage(newimg, domino.mtsizew, domino.mtsizeh);
			ImageIcon imgIcon = new ImageIcon(resizedimg);
			JLabel imgLabel = new JLabel(imgIcon);

			imgLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					updateInfo(domino, "낼 수 있는 타일이 없습니다. 새로운 타일을 뽑으세요");
				}

				public void mouseEntered(MouseEvent e) {
					imgLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}

				public void mouseExited(MouseEvent e) {
					imgLabel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			});

			domino.imageList.add(imgLabel);

		}
		for (JLabel lb : domino.imageList) {
			domino.mypane.add(lb);
		}
		domino.mypane.revalidate();
		domino.mypane.repaint();
	}

	void updateInfo(DominoUI domino, String message) {
		Component[] components = domino.infopane.getComponents();
		int totalComponents = components.length;
		if (components.length >= 1)
			domino.infopane.remove(components[components.length - 1]);
		JLabel msglb = new JLabel(message);
		msglb.setFont(domino.infofont);
		msglb.setBounds((domino.gamePanel.getWidth() - 3 * domino.mtsizew - 20) / 2 - message.length() * 10, 10,
				message.length() * 20, 25);
		domino.infopane.add(msglb);
		domino.infopane.setVisible(true);
		/*
		 * Timer timer = new Timer(3000, e -> domino.infopane.setVisible(false)); // 3초
		 * 후에 패널 숨기기 timer.setRepeats(false); // 한 번만 실행 timer.start();
		 */
		domino.infopane.revalidate();
		domino.infopane.repaint();
	}

	void offgame(DominoUI domino) {
		domino.imageList.clear();

		Component[] components = domino.mypane.getComponents();
		for (Component component : components) {
			if (component instanceof JLabel) {
				domino.mypane.remove(component); // JLabel인 컴포넌트 제거
			}
		}
		for (int i = 0; i < ptList.size(); i++) {
			BufferedImage newimg = Imgmgr.loadImage(ptList.get(i).imagename);
			newimg = Imgmgr.transparentimg(newimg);
			BufferedImage resizedimg = Imgmgr.resizeImage(newimg, domino.mtsizew, domino.mtsizeh);
			ImageIcon imgIcon = new ImageIcon(resizedimg);
			JLabel imgLabel = new JLabel(imgIcon);

			domino.imageList.add(imgLabel);

		}
		for (JLabel lb : domino.imageList) {
			domino.mypane.add(lb);
		}
		domino.mypane.revalidate();
		domino.mypane.repaint();
	}

	void igotscore(DominoUI domino, int type) { // type==0이면 all3,all5에서 끝값일때, 1이면 각 라운드 끝나고
		String str = (type == 0) ? "+" + domino.edgesum : "+" + domino.CP.sum;
		JLabel gotscore = new JLabel(str);
		gotscore.setBounds(0, domino.gamepane.getHeight() - 40, str.length() * 20, 40);
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

	void myturn(DominoUI domino) {
		JPanel myturn = new JPanel();
		myturn.setLayout(null);
		myturn.setBackground(new Color(50, 50, 50, 100));
		myturn.setBounds(0, domino.gamepane.getHeight() * 4 / 7, domino.gamepane.getWidth(),
				domino.gamepane.getHeight() / 3);
		BufferedImage newimg = Imgmgr.loadImage("imgsgame/myturn.png");
		BufferedImage resizedimg = Imgmgr.resizeImage(newimg, 190, 48);
		ImageIcon imgicon = new ImageIcon(resizedimg);
		JLabel lb = new JLabel(imgicon);
		lb.setBounds(domino.gamepane.getWidth() / 2 - 95, myturn.getHeight() / 2 - 24, 190, 48);
		myturn.add(lb);
		domino.gamepane.add(myturn);
		domino.gamepane.setComponentZOrder(myturn, 0); // 0은 가장 앞에 위치
		domino.gamepane.revalidate();
		domino.gamepane.repaint();
		domino.waiting(2);
		domino.gamepane.remove(myturn);
		domino.gamepane.revalidate();
		domino.gamepane.repaint();

	}
}
