package game;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.Border;

import account.LoginForm;
import main.MainPage;

public class DominoUI extends JPanel {
	final int mtsizew = 100;
	final int mtsizeh = 50;

	Scanner scan = new Scanner(System.in);
	Random rand = new Random();
	ArrayList<Tile> TileList = new ArrayList<Tile>();
	ArrayList<Tile> gameTile = new ArrayList<Tile>();
	ArrayList<Tile> gTiletb = new ArrayList<Tile>();
	Player me = new Player(mtsizew, mtsizeh);
	CPPlayer CP = new CPPlayer(1, mtsizew, mtsizeh);
	int firstplayer;
	int endcount[] = new int[2];
	int mostleft = -1, mostright = -1, mosttop = -1, mostbot = -1;
	int edgesum;
	int goalscore;
	static boolean stopgame = true;
	boolean canputtb = false;
	boolean puttop = false;
	boolean putbot = false;

	Font font = new Font("Arial", Font.PLAIN, 15); // "PLAIN", 일반 글씨, 크기
	Font PIfont = new Font("맑은 고딕", Font.BOLD, 15);
	Font krfont = new Font("맑은 고딕", Font.BOLD, 30);
	Font minikrfont = new Font("맑은 고딕", Font.PLAIN, 10);
	Font infofont = new Font("맑은 고딕", Font.BOLD, 20);
	private static final Object lock1 = new Object(); // 동기화를 위한 객체

	static JPanel gamePanel;//game에서의 메인 패널.
	
	void waiting() {
		int wt = 1; // 기다릴 시간 wt초
		try {
			Thread.sleep(wt * 500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*void waiting() {
	    new Thread(() -> {
	        try {
	            int wt = 5000; // 기다릴 시간(밀리초)
	            Thread.sleep(wt);
	            System.out.println("waiting() 완료");
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	    }).start();
	}*/

	void waiting(int t) {
		try {
			Thread.sleep(t * 500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void gameset() {
		if (modenum == 1) {
		    goalscore = 30;
		} else if (modenum == 2) {
		    goalscore = 60;
		} else if (modenum == 3) {
		    goalscore = 90;
		} else if (modenum == 5) {
		    goalscore = 100;
		} else {
		    System.out.println("modenum이 유효하지 않음: " + modenum);
		    return;
		}
	    //System.out.println("goalscore 값: " + goalscore);

	    makeTile();
	    //System.out.println("makeTile 실행 완료. TileList 크기: " + TileList.size());

	    if (TileList == null || TileList.isEmpty()) {
	        System.out.println("TileList가 초기화되지 않았거나 비어 있습니다.");
	        return;
	    }
		Collections.shuffle(TileList);
		//System.out.println("TileList 셔플 완료");

		PlayersDraws(); //여기서 실행버그생김. 
		System.out.println("PlayersDraws 실행 완료");
	}

	void gamestart() {
		System.out.println(firstplayer);
		if (firstplayer == 1) {
			waiting();
			CP.Plyerturn(this, scan);
			updateleftInfo();
			if ((modenum == 3 || modenum == 5) && edgesum % modenum == 0 && edgesum != 0) {
				CP.score += edgesum;
				me.updateInfo(this, "끝 값의 합이 " + modenum + "의 배수여서 점수를 얻습니다.(상대+" + edgesum + "점)");
				CP.CPgotscore(this,0);
			}
			updateleftInfo();
			printgame();
			waiting();
		}
		while (true) {
			System.out.printf("\n나%d / CP%d / %d\n", me.score, CP.score, edgesum);
			me.Plyerturn(this, scan);
			updateleftInfo();
			if ((modenum == 3 || modenum == 5) && edgesum % modenum == 0 && edgesum != 0) {
				me.score += edgesum;
				me.updateInfo(this, "끝 값의 합이 " + modenum + "의 배수여서 점수를 얻습니다.(나+" + edgesum + "점)");
				me.igotscore(this,0);
			}
			updateleftInfo();
			System.out.printf("\n나%d / CP%d / %d\n", me.score, CP.score, edgesum);
			waiting();
			printgame();
			if (endtest())
				break;
			waiting();
			CP.Plyerturn(this, scan);
			updateleftInfo();
			if ((modenum == 3 || modenum == 5) && edgesum % modenum == 0 && edgesum != 0) {
				CP.score += edgesum;
				me.updateInfo(this, "끝 값의 합이 " + modenum + "의 배수여서 점수를 얻습니다.(상대+" + edgesum + "점)");
				CP.CPgotscore(this,0);
			}
			updateleftInfo();
			System.out.printf("\n나%d / CP%d / %d\n", me.score, CP.score, edgesum);
			printgame();
			if (endtest())
				break;

		}
	}

	void gameend(JPanel mainPanel,CardLayout cardLayout) {
		me.offgame(this);
		int winner;
		boolean endcase = false;
		endcase = (me.score >= goalscore || CP.score >= goalscore) ? false : true;
		String gameresult;
		me.sum = 0;
		CP.sum = 0;
		me.makesum();
		CP.makesum();
		if (endcase) {
			if (me.ptList.size() != 0 && CP.ptList.size() != 0) {
				me.updateInfo(this, "게임 종료 (모든 플레이어가 낼 수 있는 타일이 없어 종료됩니다.)");

			} else
				me.updateInfo(this, "게임 종료");
			if (me.ptList.size() == 0 || me.sum < CP.sum) {
				winner = 0;
				gameresult = "승리";
				firstplayer = 0;
				me.score += CP.sum;
				me.updateInfo(this, "라운드 승리/"+CP.sum+"점을 얻습니다.");
				win = win + 1;
			} else if (CP.ptList.size() == 0 || me.sum > CP.sum) {
				winner = 1;
				gameresult = "패배";
				firstplayer = 1;
				CP.score += me.sum;
				me.updateInfo(this, "라운드 패배/상대가"+me.sum+"점을 얻습니다.");
				lose = lose + 1;
			} else {
				winner = -1;
				gameresult = "동점";
			}
			roundend(winner);
			if(winner==0) me.igotscore(this, 1);
			else CP.CPgotscore(this, 1);
			updateleftInfo();
		}
		else {
			if(me.score>=goalscore) me.updateInfo(this,LoginForm.myData.id+"이(가) 목표점수를 달성하여 승리하였습니다.");
			else if(CP.score>=goalscore) me.updateInfo(this, "CP이(가) 목표점수를 달성하여 패배하였습니다. ");
			waiting(4);
		}

	}

	boolean endtest() {
		System.out.println(goalscore);
		if (testArray()) {
			me.makesum();
			System.out.printf("내 타일의 합:%d\n", me.sum);
			CP.makesum();
			System.out.printf("CP의 타일의 합:%d\n", CP.sum);
			System.out.println();
			Player winner = me;
			int winidx = -1;
			if (winner.sum > CP.sum) {
				winner = CP;
			}
			return true;
		} else if (me.ptList.size() == 0)
			return true;
		else if (CP.ptList.size() == 0)
			return true;
		else if (me.score >= goalscore || CP.score >= goalscore)
			return true;
		return false;
	}

	boolean testArray() {
		for (int i : endcount) {
			if (i == 0)
				return false;
		}
		return true;
	}

	void makeTile() {
		Tile t = null;
		for (int l = 0; l < 7; l++) {
			for (int h = l; h < 7; h++) {
				t = new Tile();
				t.leftnum = l;
				t.rightnum = h;
				t.imagename = "imgsgame/Tileimg" + l + h + ".png";
				TileList.add(t);
			}
		}
	}

	void printgame() {
		System.out.printf("\n\n\t\t\t\t\t\t============게임판============%d%d\n\t\t\t\t\t\t", modenum, gTiletb.size());
		for (Tile t : gameTile) {
			t.printgametile();
		}
		if (gTiletb.size() >= 1) {
			System.out.println("\n\t\t\t\t\t\t상하:");
			for (Tile t : gTiletb) {
				t.printgametile();
			}
		}
		System.out.printf("\n\n\t\t\t\t\t\t남은 뽑을 수 있는 타일 수 : %d\n\t\t\t\t\t\t좌측값:%d 우측값:%d\n", TileList.size(),
				mostleft, mostright);
	}

	/////////////////'종료하기'후 게임실행 오류
	void PlayersDraws() {
		System.out.println("PlayersDraws 호출됨");
	    
	    // TileList 상태 확인
	    if (TileList == null || TileList.isEmpty()) {
	        System.out.println("TileList가 비어 있습니다. PlayersDraws를 실행할 수 없습니다.");
	        return;
	    }

	    // 플레이어 ptList 초기화 확인
	    if (me.ptList == null || CP.ptList == null) {
	        System.out.println("플레이어의 ptList가 초기화되지 않았습니다!");
	        return;
	    }
		for (int i = 0; i < 7; i++) {
			me.ptList.add(TileList.remove(0));
			CP.ptList.add(TileList.remove(0));
			me.setMyPane(this);
			CP.setCPPane(this); //게임하고 '종료하기'누른다음 다시 게임 실행할때 여기서 오류남
			// 상태 확인
	        System.out.println("플레이어에게 타일 분배 완료: me.ptList 크기: " + me.ptList.size() + ", CP.ptList 크기: " + CP.ptList.size());
			me.drawCount(this);
			waiting();
			
		}
		System.out.println("PlayersDraws 완료");
	}

	void setleftright() {
		mostleft = gameTile.get(0).leftnum;
		mostright = gameTile.get(gameTile.size() - 1).rightnum;
		edgesum = mostleft + mostright;
		if (gTiletb.size() != 0) {
			mosttop = gTiletb.get(0).leftnum;
			mostbot = gTiletb.get(gTiletb.size() - 1).rightnum;
		}
		if (puttop) 
			edgesum += mosttop;
		
		if (putbot) 
			edgesum += mostbot;
		
	}

	boolean testsizeoff(int kwd) {
		if (kwd <= 0 || kwd > me.ptList.size())
			return true;
		return false;
	}

	int putnum(Tile t) {
		int r = 0;
		if (t.leftnum == t.rightnum) {
			if (mostleft == t.leftnum)
				r++;
			if (mostright == t.rightnum)
				r++;
			if (mosttop == t.leftnum)
				r++;
			if (mostbot == t.rightnum)
				r++;
		} else {
			if (mostleft == t.leftnum)
				r++;
			else if (mostleft == t.rightnum)
				r++;
			if (mostright == t.leftnum)
				r++;
			else if (mostright == t.rightnum)
				r++;
			if (mosttop == t.leftnum)
				r++;
			else if (mosttop == t.rightnum)
				r++;
			if (mostbot == t.leftnum)
				r++;
			else if (mostbot == t.rightnum)
				r++;
		}
		return r;
	}

	void putnewTile(Tile putTile, int i) {
		Tile t = new Tile();
		t.leftnum = putTile.leftnum;
		t.rightnum = putTile.rightnum;
		t.imagename = putTile.imagename;
		if (judgeSwitch(t, i))
			t.switching();
		if (i == 0)
			gameTile.add(0, t);
		else if (i == 1)
			gameTile.add(t);
		else if (i == 2)
			gTiletb.add(0, t);
		else if (i == 3)
			gTiletb.add(t);
	}

	boolean judgeSwitch(Tile putTile, int i) {
		if (i == 0 && putTile.leftnum == mostleft)
			return true;
		if (i == 1 && putTile.rightnum == mostright)
			return true;
		if (i == 2 && putTile.leftnum == mosttop)
			return true;
		if (i == 3 && putTile.rightnum == mostbot)
			return true;
		return false;
	}

	//frame to panel
	public void reset(JPanel mainPanel,CardLayout cardLayout) {
		TileList.clear();
		gameTile.clear();
		gTiletb.clear();
		me.ptList.clear();
		me.sum = 0;
		CP.ptList.clear();
		CP.sum = 0;
		mostleft = -1;
		mostright = -1;
		mosttop = -1;
		mostbot = -1;
		edgesum = 0;
		canputtb = false;
		puttop = false;
		putbot = false;
		Player.firstdouble = null;
		imageList.clear();
		endcount[0] = 0;
		endcount[1] = 0;
		gamePanel=new JPanel();
		if (mypane != null)
			resetPanels();
		// mainFrame.dispose();
	
		gamePanel.repaint(); // UI 재그리기
		//gamePanel.setVisible(true);
		//mainFrame.pack(); // 프레임 크기 다시 설정
		//mainFrame.setVisible(true); // 화면에 보이도록 설정
	}

	public void resetPanels() {
		mypane.removeAll();
		CPpane.removeAll();
		Component[] components = Drpane.getComponents();
		int totalComponents = components.length;
		if (components.length > 1)
			Drpane.remove(components[components.length - 1]);
		gamepane.removeAll();
		//gamePanel.removeAll();
		
		gamePanel.revalidate(); //mainFrame
		gamePanel.repaint();
		updateleftInfo();

	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private static final Object lock = new Object();
	JPanel Rightpane;
	JPanel mypane;
	JPanel myhead;
	JPanel CPpane;
	JPanel CPhead;
	JPanel Drpane;
	JPanel gamepane;
	JPanel infopane;
	JPanel leftInfo;
	ArrayList<JLabel> imageList = new ArrayList<>(); // 이미지 레이블 리스트

	public void startGUI(MainPage mainPage,JPanel mainPanel,CardLayout cardLayout) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				gamePanel=createAndShowGUI();
				 mainPanel.add(gamePanel,"게임");
				 cardLayout.show(mainPanel,"게임");
				 mainPage.setSize(1500, 850);
				 mainPage.setLocationRelativeTo(null);
			}
		});
	}

	//static JFrame mainFrame = new JFrame("Domino");
	static DominoUI main = null;

	private JPanel createAndShowGUI() {
		//main.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Rightpane = setupRightpane();
		mypane = setupMyPanel();
		myhead = setupmyhead();
		CPpane = setupCPPanel();
		CPhead = setupCPhead();
		Drpane = setupDrawPanel();
		gamepane = setupGamePanel();
		infopane = setupInfoPanel();
		leftInfo=setupleftInfo();
		//gamePanel.setPreferredSize(new Dimension(1500, 820));
		int rightpnsX = gamePanel.getWidth() - 3 * mtsizew - 20;
		int rightpnsW = 3 * mtsizew + 10;
		// 레이아웃을 null로 설정하여 절대 위치 사용
		gamePanel.setLayout(null); // null 레이아웃으로 설정
		
		Rightpane.setPreferredSize(new Dimension(rightpnsX, 0));
		Rightpane.setBounds(rightpnsX, 0, rightpnsW, gamePanel.getHeight());
		
		mypane.setPreferredSize(new Dimension(rightpnsX, 390));
		mypane.setBounds(rightpnsX, 390, rightpnsW, 430); // (x, y, width, height)
		
		myhead.setPreferredSize(new Dimension(rightpnsX, 370));
		myhead.setBounds(rightpnsX, 370, rightpnsX, 20);

		CPpane.setPreferredSize(new Dimension(rightpnsX, 20));
		CPpane.setBounds(rightpnsX, 20, rightpnsW, 250); // (x, y, width, height)
		
		CPhead.setPreferredSize(new Dimension(rightpnsX, 20));
		CPhead.setBounds(rightpnsX, 0, rightpnsX, 20);

		Drpane.setPreferredSize(new Dimension(rightpnsX, 270));
		Drpane.setBounds(rightpnsX, 270, rightpnsW, mtsizeh * 2); // (x, y, width, height)

		gamepane.setPreferredSize(new Dimension(900, 450));
		gamepane.setBounds(0, 0, rightpnsX, gamePanel.getHeight() - 40); // (x, y, width, height)

		infopane.setPreferredSize(new Dimension(0, 0));
		infopane.setBounds(0, 0, 200, 200);
		
		leftInfo.setPreferredSize(new Dimension(mtsizew,gamePanel.getHeight()));
		leftInfo.setBounds(0, 0, mtsizew, gamePanel.getHeight());

		// 각 패널을 프레임에 추가
		Rightpane.add(mypane);
		Rightpane.add(myhead);
		Rightpane.add(CPpane);
		Rightpane.add(CPhead);
		Rightpane.add(Drpane);
		gamePanel.add(Rightpane);
		gamePanel.add(gamepane);
		gamePanel.add(infopane);
		gamePanel.add(leftInfo);
		//mainFrame.setComponentZOrder(whatcpput, 0);  // panel2를 가장 앞에 배치
		gamePanel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				adjustPanelPositions(Rightpane, mypane, myhead, CPpane, CPhead,  Drpane, gamepane, infopane, leftInfo);
			}
		});
		//mainFrame.pack();
		//mainFrame.setVisible(true);
		return gamePanel;

	}

	public void adjustPanelPositions(JPanel Rightpane,JPanel mypane, JPanel myhead, JPanel CPpane, JPanel CPhead, JPanel Drpane,
			JPanel gamepane, JPanel infopane, JPanel leftInfo) {
		// 프레임의 현재 크기
		int frameWidth = gamePanel.getWidth();
		int frameHeight = gamePanel.getHeight();
		int rightpnsX = frameWidth - 3 * mtsizew - 20;
		int rightpnsW = 3 * mtsizew + 10;
		// 각 패널의 가로 위치만 중앙으로 정렬
		Rightpane.setBounds(rightpnsX, 0, rightpnsW, frameHeight);
		
		mypane.setBounds(0, 390, rightpnsW, 430);

		myhead.setBounds(0, 370, rightpnsW, 20);

		CPpane.setBounds(0, 20, rightpnsW, 250);

		CPhead.setBounds(0, 0, rightpnsW, 20);
		
		Drpane.setBounds(0, 270, rightpnsW, mtsizeh * 2);

		gamepane.setBounds(mtsizew, 40, rightpnsX-mtsizew, frameHeight - 80);

		infopane.setBounds(mtsizew, 0, rightpnsX-mtsizew, 40);
		
		leftInfo.setBounds(0, 0, mtsizew, frameHeight);

		// 레이아웃 업데이트
		gamePanel.revalidate();
		gamePanel.repaint();
	}

	JPanel setupRightpane() {
		JPanel Rightpane=new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g); // 기존 배경 그리기
				g.setColor(Color.BLACK); // 실선 색 설정
				g.drawLine(0, 0, 0, getHeight()); // 왼쪽 테두리 선 그리기
			}
		};
		Rightpane.setLayout(null);
		return Rightpane;
	}
	
	JPanel setupMyPanel() {
		JPanel newmypane = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g); // 기존 배경 그리기
				g.setColor(Color.BLACK); // 실선 색 설정
				g.drawRect(0, 0, getWidth(), getHeight()); // 실선 테두리 그리기
			}
		};
		return newmypane;
	}

	JPanel setupCPPanel() {
		JPanel CPpane = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g); // 기존 배경 그리기
				g.setColor(Color.BLACK); // 실선 색 설정
				g.drawLine(0, 0, 0, getHeight());

				// 아래쪽 테두리 수평선
				g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
			}
		};
		return CPpane;
	}
	JPanel setupmyhead() {
		JPanel myhead=new JPanel();
		String str=LoginForm.myData.id;
		if(str==null) str="null";
		JLabel lb=new JLabel(str);
		myhead.setLayout(null);
		lb.setFont(PIfont);
		lb.setBounds((3*mtsizew+10)/2-str.length()*4, 0, str.length()*15, 20);
		lb.setForeground(Color.white);
		myhead.setBackground(Color.black);
		myhead.add(lb);
		return myhead;
	}
	JPanel setupCPhead() {
		JPanel CPhead=new JPanel();
		String str="CP";
		JLabel lb=new JLabel(str);
		CPhead.setLayout(null);
		lb.setFont(PIfont);
		lb.setBounds((3*mtsizew+10)/2-str.length()*4, 0, str.length()*15, 20);
		lb.setForeground(Color.white);
		CPhead.setBackground(Color.black);
		CPhead.add(lb);
		return CPhead;
	}

	JPanel setupDrawPanel() {
		JPanel drawpane = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g); // 기존 배경 그리기
				g.setColor(Color.BLACK); // 실선 색 설정
				g.drawLine(0, 0, 0, getHeight()); // 왼쪽 테두리 선 그리기
			}
		};
		drawpane.setPreferredSize(new Dimension(100, 450));
		BufferedImage drawimg = Imgmgr.loadImage("imgsgame/tileimgback.png");
		JLabel lb = new JLabel(new ImageIcon(drawimg));
		JLabel countdraw = new JLabel("X" + TileList.size());
		lb.setBounds((3 * mtsizew + 10) / 2 - mtsizew - 20, mtsizeh / 2, mtsizew, mtsizeh);
		JLabel drbt = new JLabel("draw!");
		drbt.setBackground(Color.black);
		drbt.setForeground(Color.white);
		drbt.setOpaque(true); // 불투명하게 만들어 배경색이 보이도록 설정
		// 텍스트를 가로와 세로로 모두 가운데 정렬
		drbt.setHorizontalAlignment(SwingConstants.CENTER); // 수평 가운데 정렬
		drbt.setVerticalAlignment(SwingConstants.CENTER); // 수직 가운데 정렬
		drbt.setBounds((3 * mtsizew + 10) / 2 + 30, mtsizeh / 2, mtsizew, mtsizeh);

		countdraw.setBounds((3 * mtsizew + 10) / 2 - 15, mtsizeh / 2, mtsizeh, mtsizeh);
		countdraw.setFont(font);

		drawpane.add(lb);
		drawpane.add(countdraw);
		drawpane.add(drbt);

		drawpane.setLayout(null);
		return drawpane;
	}

	JPanel setupGamePanel() {
		JPanel gamepane = new JPanel();
		gamepane.setLayout(null);
		return gamepane;
	}

	JPanel setupInfoPanel() {
		JPanel infopane = new JPanel();
		infopane.setPreferredSize(new Dimension(200, 200));
		infopane.setBackground(new Color(255, 255, 255));
		infopane.setLayout(null);
		

		/*
		 * Timer timer = new Timer(3000, e -> infopane.setVisible(false)); // 3초 후에 패널
		 * 숨기기 timer.setRepeats(false); // 한 번만 실행 timer.start();
		 */
		return infopane;
	}

	
	JPanel setupleftInfo() {
		JPanel leftInfo=new JPanel();
		leftInfo.setLayout(null);
		return leftInfo;
		
	}
	void updateleftInfo() {
		Component[] components = leftInfo.getComponents();
		for (Component component : components) {
			if (component instanceof JLabel) {
				leftInfo.remove(component); // JLabel인 컴포넌트 제거
			}
		}
		JLabel edgesumlb=new JLabel("끝 값의 총합");
		JLabel edgesumlb2=new JLabel(""+edgesum);
		edgesumlb.setBounds(0, leftInfo.getHeight()/2-mtsizew, mtsizew, 25);
		edgesumlb.setBackground(Color.black);
		edgesumlb.setForeground(Color.white);
		edgesumlb.setOpaque(true);
		edgesumlb.setHorizontalAlignment(JLabel.CENTER); // 가로 방향 가운데 정렬
		edgesumlb.setVerticalAlignment(JLabel.CENTER);   // 세로 방향 가운데 정렬
		edgesumlb2.setBounds(0, leftInfo.getHeight()/2-mtsizew+25, mtsizew, 30);
		edgesumlb2.setBackground(Color.black);
		edgesumlb2.setForeground(Color.white);
		edgesumlb2.setOpaque(true);
		edgesumlb2.setHorizontalAlignment(JLabel.CENTER); // 가로 방향 가운데 정렬
		edgesumlb2.setVerticalAlignment(JLabel.CENTER);   // 세로 방향 가운데 정렬
		edgesumlb.setFont(PIfont);
		edgesumlb2.setFont(infofont);
		if(modenum==3||modenum==5) {leftInfo.add(edgesumlb);	leftInfo.add(edgesumlb2);}
		
		JLabel CPscorelb=new JLabel("CP");
		JLabel CPscorelb2=new JLabel(""+CP.score);
		String username=LoginForm.myData.id;
		if(username==null) username="null";
		JLabel myscorelb=new JLabel(username);
		JLabel myscorelb2=new JLabel(me.score+"");
		
		CPscorelb.setBounds(0, 0, mtsizew, 40);
		CPscorelb2.setBounds(0, 40, mtsizew, 40);
		myscorelb.setBounds(0, leftInfo.getHeight()-120, mtsizew, 40);
		myscorelb2.setBounds(0, leftInfo.getHeight()-80, mtsizew, 40);
		
		CPscorelb.setBackground(Color.black);
		CPscorelb.setForeground(Color.white);
		CPscorelb2.setBackground(Color.black);
		CPscorelb2.setForeground(Color.white);
		myscorelb.setBackground(Color.black);
		myscorelb.setForeground(Color.white);
		myscorelb2.setBackground(Color.black);
		myscorelb2.setForeground(Color.white);
		
		CPscorelb.setOpaque(true);
		CPscorelb2.setOpaque(true);
		myscorelb.setOpaque(true);
		myscorelb2.setOpaque(true);
		
		CPscorelb.setFont(infofont);
		CPscorelb2.setFont(infofont);
		myscorelb2.setFont(infofont);
		if(username.length()>7)myscorelb.setFont(PIfont);
		else myscorelb.setFont(infofont);

		CPscorelb.setHorizontalAlignment(JLabel.CENTER); // 가로 방향 가운데 정렬
		CPscorelb.setVerticalAlignment(JLabel.CENTER);   // 세로 방향 가운데 정렬
		CPscorelb2.setHorizontalAlignment(JLabel.CENTER); // 가로 방향 가운데 정렬
		CPscorelb2.setVerticalAlignment(JLabel.CENTER);   // 세로 방향 가운데 정렬
		
		myscorelb.setHorizontalAlignment(JLabel.CENTER); // 가로 방향 가운데 정렬
		myscorelb.setVerticalAlignment(JLabel.CENTER);   // 세로 방향 가운데 정렬
		myscorelb2.setHorizontalAlignment(JLabel.CENTER); // 가로 방향 가운데 정렬
		myscorelb2.setVerticalAlignment(JLabel.CENTER);   // 세로 방향 가운데 정렬
		
		leftInfo.add(CPscorelb);
		leftInfo.add(CPscorelb2);
		leftInfo.add(myscorelb);
		leftInfo.add(myscorelb2);
		
		
		leftInfo.revalidate();
		leftInfo.repaint();
	}
	
	void roundend(int winner) {
		JPanel roundend=new JPanel();
		roundend.setBounds(0, 5, gamepane.getWidth(), gamepane.getHeight()-5);
		roundend.setBackground(new Color(50,50,50,50));
		roundend.setLayout(null);
		
		BufferedImage newimg=null;
		if(winner==0) newimg=Imgmgr.loadImage("imgsgame/roundwin.png");
		else if(winner==1) newimg=Imgmgr.loadImage("imgsgame/roundlose.png");
		else newimg=Imgmgr.loadImage("imgsgame/rounddraw.png");
		BufferedImage resizedimg=Imgmgr.resizeImage(newimg, 300, 200);
		ImageIcon imgicon=new ImageIcon(resizedimg);
		JLabel lb=new JLabel(imgicon);
		lb.setBounds(roundend.getWidth()/2-150, roundend.getHeight()/2-100, 300, 200);
		
		ImageIcon continueimg=new ImageIcon(Imgmgr.resizeImage("imgsgame/continue.png", 240, 40));
		JLabel continuelb=new JLabel(continueimg);
		ImageIcon giveupimg=new ImageIcon(Imgmgr.resizeImage("imgsgame/giveup.png", 240, 40));
		JLabel giveuplb=new JLabel(giveupimg);
		continuelb.setBounds(roundend.getWidth()/2-120, roundend.getHeight()/2+110, 240, 40);
		giveuplb.setBounds(roundend.getWidth()/2-120, roundend.getHeight()/2+160, 240, 40);
		continuelb.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				synchronized (lock1){
					lock1.notify();
				}
				stopgame=false;
			}

			public void mouseEntered(MouseEvent e) {
				continuelb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}

			public void mouseExited(MouseEvent e) {
				continuelb.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});
		giveuplb.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				int option = JOptionPane.showConfirmDialog(
			            null, 
			            "포기하시겠습니까? 포기 시 상대의 점수만큼 랭크점수가 하락합니다.", 
			            "포기", 
			            JOptionPane.YES_NO_OPTION
			        );
				 // "예"를 눌렀을 경우
		        if (option == JOptionPane.YES_OPTION) {
		            // 락을 풀고 stopgame을 true로 설정
		            synchronized (lock1) {
		                lock1.notify();
		            }
		            stopgame = true;
		        }
		        // "아니오"를 눌렀을 경우
		        else if (option == JOptionPane.NO_OPTION) {
		            return;
		        }
			}

			public void mouseEntered(MouseEvent e) {
				giveuplb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}

			public void mouseExited(MouseEvent e) {
				giveuplb.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});
		
		JLabel mytsum=new JLabel("내 타일의 합:"+me.sum);
		JLabel CPtsum=new JLabel("CP 타일의 합:"+CP.sum);
		JPanel tsum=new JPanel();
		mytsum.setFont(infofont);
		mytsum.setForeground(Color.black);
		CPtsum.setFont(infofont);
		CPtsum.setForeground(Color.black);
		tsum.setLayout(null);
		CPtsum.setBounds(0, 0, 160, 20);
		mytsum.setBounds(0, 30, 160, 20);
		tsum.setBounds(roundend.getWidth()/2+130, roundend.getHeight()/2+120, 160, 55);
		tsum.setBackground(Color.white);
		tsum.add(CPtsum);
		tsum.add(mytsum);
		
		roundend.add(lb);
		roundend.add(continuelb);
		roundend.add(giveuplb);
		roundend.add(tsum);
		gamepane.add(roundend);
		gamepane.setComponentZOrder(roundend, 0);
		gamepane.revalidate();
		gamepane.repaint();
		
		synchronized (lock1) {
			try {
				lock1.wait();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		gamepane.remove(roundend);
		gamepane.revalidate();
		gamepane.repaint();
		
		
	}
	void gameendui() {
		JPanel endpane=new JPanel();
		endpane.setLayout(null);
		endpane.setBackground(new Color(50,50,50,50));
		endpane.setBounds(0, 5, gamepane.getWidth(), gamepane.getHeight()-5);

		
		JLabel imglb=null;
		JLabel getpoint=null;
		if(stopgame) {
			ImageIcon imgicon=new ImageIcon(Imgmgr.resizeImage("imgsgame/gamelose.png", 800, 200));
			imglb=new JLabel(imgicon);
			getpoint=new JLabel("점수 -"+CP.score);
		}
		else if(me.score>CP.score) {
			ImageIcon imgicon=new ImageIcon(Imgmgr.resizeImage("imgsgame/gamewin.png", 800, 200));
			imglb=new JLabel(imgicon);
			getpoint=new JLabel("점수 +"+(me.score-CP.score));
		}
		else {
			ImageIcon imgicon=new ImageIcon(Imgmgr.resizeImage("imgsgame/gamelose.png", 800, 200));
			imglb=new JLabel(imgicon);
			getpoint=new JLabel("점수 "+(me.score-CP.score));
		}
		
		imglb.setBounds(endpane.getWidth()/2-400, endpane.getHeight()/2-100, 800, 200);
		ImageIcon tmimg=new ImageIcon(Imgmgr.resizeImage("imgsgame/backtomain.png", 240, 40));
		JLabel tomain=new JLabel(tmimg);
		tomain.setBounds(endpane.getWidth()/2-120, endpane.getHeight()/2+120, 240, 40);
		
		getpoint.setFont(infofont);
		getpoint.setBackground(Color.white);
		getpoint.setOpaque(true);
		getpoint.setBounds(endpane.getWidth()/2+140, endpane.getHeight()/2+125, 90, 30);
		
		tomain.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				synchronized (lock1){
					lock1.notify();
				}
			}
			public void mouseEntered(MouseEvent e) {
				tomain.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}

			public void mouseExited(MouseEvent e) {
				tomain.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});
		
		endpane.add(imglb);
		endpane.add(tomain);
		endpane.add(getpoint);
		gamepane.add(endpane);
		gamepane.setComponentZOrder(endpane, 0);
		gamepane.revalidate();
		gamepane.repaint();

		synchronized (lock1) {
			try {
				lock1.wait();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		gamepane.remove(endpane);
		gamepane.revalidate();
		gamepane.repaint();
		
	}

	//////////////////////////////////////////////////////////////////////////////////////
	public static int win;
	public static int lose;
	static int modenum = 5;

	public int PlayAll5(MainPage mainPage,DominoUI main,JPanel mainPanel,CardLayout cardLayout) {
		win = 0;
		lose = 0;
		goalscore = 100;
		modenum = 5;
		me.score = 0;
		CP.score = 0;
		stopgame=false;
		main = new DominoUI();
		main.firstplayer = rand.nextInt(2);								//순서 정하기
		System.out.println(main.firstplayer);
		main.startGUI(mainPage,mainPanel,cardLayout);
		while (main.me.score < goalscore && main.CP.score < goalscore) {
			main.reset(mainPanel,cardLayout);
			// mainFrame.dispose();
			main.gameset();  //버그버그버그 playerdraws()에서 오류확인
			main.gamestart();
			main.gameend(mainPanel,cardLayout);
			if (stopgame) {
				main.reset(mainPanel,cardLayout);
				
				break;
			}
		}
		main.gameendui();
		cardLayout.show(mainPanel,"MainPage");
		mainPage.setSize(1000, 800);
		mainPage.setLocationRelativeTo(null);

		//mainFrame.dispose();
		return me.score - CP.score;
	}

	public int PlayAll3(MainPage mainPage,DominoUI main,JPanel mainPanel,CardLayout cardLayout) {
		win = 0;
		lose = 0;
		modenum = 3;
		goalscore = 90;
		me.score = 0;
		CP.score = 0;
		stopgame=false;
		main = new DominoUI();
		main.firstplayer = rand.nextInt(2);								//순서 정하기
		System.out.println(main.firstplayer);
		main.startGUI(mainPage,mainPanel,cardLayout);
		while (main.me.score < goalscore && main.CP.score < goalscore) {
			System.out.println("게임 시작 버튼 클릭됨!");
			main.reset(mainPanel,cardLayout);
			// mainFrame.dispose();
			System.out.println("게임 시작 버튼 클릭됨!");
			main.gameset();
			System.out.println("게임 시작 버튼 클릭됨!");
			main.gamestart();
			System.out.println("게임 시작 버튼 클릭됨!");
			main.gameend(mainPanel,cardLayout);
			if (stopgame) {
				main.reset(mainPanel,cardLayout);
				break;
			}
		}
		main.gameendui();
		cardLayout.show(mainPanel,"MainPage");
		mainPage.setSize(1000, 800);
		mainPage.setLocationRelativeTo(null);
		//mainFrame.dispose();
		return me.score - CP.score;
	}

	public int Playdraw(MainPage mainPage,DominoUI main,JPanel mainPanel,CardLayout cardLayout) {
		win = 0;
		lose = 0;
		modenum = 2;
		goalscore = 60;
		me.score = 0;
		CP.score = 0;
		stopgame=false;
		main = new DominoUI();
		main.firstplayer = rand.nextInt(2);								//순서 정하기
		System.out.println(main.firstplayer);
		main.startGUI(mainPage,mainPanel,cardLayout);
		while (main.me.score < goalscore && main.CP.score < goalscore) {
			System.out.println("게임 시작 버튼 클릭됨!");
			main.reset(mainPanel,cardLayout);
			// mainFrame.dispose();
			System.out.println("게임 시작 버튼 클릭됨!");
			main.gameset();
			System.out.println("게임 시작 버튼 클릭됨!");
			main.gamestart();
			System.out.println("게임 시작 버튼 클릭됨!");
			main.gameend(mainPanel,cardLayout);
			if (stopgame) {
				main.reset(mainPanel,cardLayout);
				break;
			}
		}
		main.gameendui();
		cardLayout.show(mainPanel,"MainPage");
		mainPage.setSize(1000, 800);
		mainPage.setLocationRelativeTo(null);
		
		//mainFrame.dispose();
		return me.score - CP.score;
	}
	public int Playblock(MainPage mainPage,DominoUI main,JPanel mainPanel,CardLayout cardLayout) {
		win = 0;
		lose = 0;
		modenum = 1;
		goalscore = 30;
		me.score = 0;
		CP.score = 0;
		stopgame=false;
		main = new DominoUI();
		main.firstplayer = rand.nextInt(2);								//순서 정하기
		System.out.println(main.firstplayer);
		main.startGUI(mainPage,mainPanel,cardLayout);
		while (main.me.score < goalscore && main.CP.score < goalscore) {
			main.reset(mainPanel,cardLayout);
			// mainFrame.dispose();
			main.gameset();
			main.gamestart();
			main.gameend(mainPanel,cardLayout);
			if (stopgame) {
				main.reset(mainPanel,cardLayout);
				break;
			}
		}
		main.gameendui();
		cardLayout.show(mainPanel,"MainPage");
		mainPage.setSize(1000, 800);
		mainPage.setLocationRelativeTo(null);
		//mainFrame.dispose();
		return me.score - CP.score;
	}

	/*public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		DominoUI du = new DominoUI();
		int m = 5;
		// m=scan.nextInt();
		switch (m) {
		case 1:
			du.Playblock();
			break;
		case 2:
			du.Playdraw();
			break;
		case 3:
			du.PlayAll3();
			break;
		case 5:
			du.PlayAll5();
			break;
		}
	}*/
}
