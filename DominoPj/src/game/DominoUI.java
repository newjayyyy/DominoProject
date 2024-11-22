package game;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
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
import javax.swing.border.Border;

import main.MainPage;








public class DominoUI extends JPanel{
	
	
	Scanner scan=new Scanner(System.in);
	Random rand=new Random();
	ArrayList<Tile> TileList=new ArrayList<Tile>();
	ArrayList<Tile> gameTile=new ArrayList<Tile>();
	ArrayList<CPPlayer> CPPlayerList=new ArrayList<CPPlayer>();
	Player me=new Player();
	CPPlayer CP=new CPPlayer(1);
	int endcount[]=new int[2];
	int mostleft=-1, mostright=-1;
	static boolean stopgame=true;
	
	Font font = new Font("Arial", Font.PLAIN, 15);  // "PLAIN", 일반 글씨, 크기 30
	Font krfont=new Font("맑은 고딕", Font.BOLD, 30);
	Font minikrfont=new Font("맑은 고딕", Font.PLAIN,10);
	Font infofont=new Font("맑은 고딕", Font.BOLD, 20);
	private static final Object lock1 = new Object();  // 동기화를 위한 객체
	
	void waiting() {
		int wt=1;												//기다릴 시간 wt초
		try {
			Thread.sleep(wt*500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//수정
	void gameset() {
		mypane=setupMyPanel();
		Drpane=setupDrawPanel();
		CPpane=setupCPPanel();
		infopane=setupInfoPanel();
		waiting();
		makeTile();
		Collections.shuffle(TileList);
		makePlayers();
		PlayersDraws();
	}
	void gamestart() {
		gameLoop:
		while(true) {
			me.Plyerturn(this, scan);
			waiting();
			printgame();
			if(me.ptList.size()==0) {
				break;
			}
			if(endtest()) break;
			for(CPPlayer cp:CPPlayerList) {
				waiting();
				cp.Plyerturn(this, scan);
				printgame();
				if(cp.ptList.size()==0) {
					break gameLoop;
				}
				if(endtest()) break gameLoop;
			}
		}
	}
	void gameend(JPanel mainPanel,CardLayout cardLayout) {
		int winner;
		boolean endcase=false;
		String gameresult;
		me.sum=0;
		CP.sum=0;
		me.makesum();
		CP.makesum();
		if(me.ptList.size()!=0&&CP.ptList.size()!=0) {
			me.updateInfo(this, "게임 종료 (모든 플레이어가 낼 수 있는 타일이 없어 종료됩니다.)");
		}
		else me.updateInfo(this, "게임 종료");
		if (me.ptList.size() == 0 || me.sum < CP.sum) {
			winner = 0;
			gameresult="승리";
			win=win+1;
			if(me.ptList.size()!=0) endcase=true;
		} else if (CP.ptList.size() == 0 || me.sum > CP.sum) {
			winner = 1;
			gameresult="패배";
			lose=lose+1;
			if(CP.ptList.size()!=0) endcase=true;
		} else {
			winner = -1;
			gameresult="동점";
		}
		
		JLabel resultLabel = new JLabel(gameresult);
		resultLabel.setFont(krfont);
		resultLabel.setBounds(475, 300, 60, 60);
		
		switch(winner) {
		case 0:
			resultLabel.setForeground(Color.blue);
			resultLabel.setBackground(Color.WHITE);
			resultLabel.setOpaque(true);
			break;
		case 1:
			resultLabel.setForeground(Color.red);
			resultLabel.setBackground(Color.WHITE);
			resultLabel.setOpaque(true);
			break;
		default:
			resultLabel.setForeground(Color.green);
		}
		gamepane.add(resultLabel);
		
		if(endcase) {
			String mytsum="내 타일의 합 : "+me.sum;
			String CPtsum="CP 타일의 합 : "+CP.sum;
			JLabel mysumlb=new JLabel(mytsum);
			JLabel CPsumlb=new JLabel(CPtsum);
			mysumlb.setFont(minikrfont);
			CPsumlb.setFont(minikrfont);
			mysumlb.setBounds(575,310,90,10);
			CPsumlb.setBounds(575,335,90,10);
			gamepane.add(mysumlb);
			gamepane.add(CPsumlb);
		}
		if (modenum == 0) {
			JButton replay = new JButton("다시하기");
			replay.setFont(minikrfont);
			replay.setBackground(Color.white);
			replay.setOpaque(true);
			replay.setBounds(455, 380, 100, 20);
			replay.addActionListener(new ActionListener() {
			    @Override
			    public void actionPerformed(ActionEvent e) {
			        // 버튼 클릭 시 호출될 메소드
			        synchronized (lock1) {
			            lock1.notify();  // lock1의 모니터를 획득하고 notify 호출
			        }
			        stopgame=false;
			        reset(mainPanel,cardLayout);
			    }
			});
			gamepane.add(replay);
			gamepane.revalidate();
			gamepane.repaint();
		}
		
		
		String endmsg="돌아가기";
		if(modenum==1) {
			endmsg="계속하기";
			if(winner==0) me.score+=CP.sum;
		}
		
		JButton outgame = new JButton(endmsg);
		
		outgame.setFont(minikrfont);
		outgame.setBackground(Color.white);
		outgame.setOpaque(true);

		outgame.setBounds(455, 420, 100, 20);
		gamepane.add(outgame);
		gamepane.revalidate();
		gamepane.repaint();

		

		outgame.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        // 버튼 클릭 시 호출될 메소드
		        synchronized (lock1) {
		            lock1.notify();  // lock1의 모니터를 획득하고 notify 호출
		        }
		        stopgame = true;
		    }
		});

		// 이 부분은 이벤트가 발생하기 전, 즉 대기 상태로 시작
		synchronized (lock1) {
		    try {
		        System.out.println("프로그램이 잠깐 멈췄습니다... 다시하기 또는 돌아가기");
		        lock1.wait();  // 이 시점에서 대기
		    } catch (InterruptedException e) {
		        e.printStackTrace();
		    }
		}
		
	}
	
	boolean endtest() {
		if (testArray()) {
			me.makesum();
			System.out.printf("내 타일의 합:%d\n", me.sum);
			for(CPPlayer cp:CPPlayerList) {
				cp.makesum();
				System.out.printf("CP%d의 타일의 합:%d\n", 1,cp.sum);
			}
			System.out.println();
			Player winner=me;
			int winidx=-1;
			for(CPPlayer cp:CPPlayerList) {
				if(winner.sum>cp.sum) {
					winner=cp;
					winidx=CPPlayerList.indexOf(cp);
				}
			}
			return true;
		}
		return false;
	}
	boolean testArray() {
		for(int i:endcount) {
			if(i==0) return false;
		}
		return true;
	}
	
	void makeTile() {
		Tile t=null;
		for(int l=0;l<7;l++) {
			for(int h=l;h<7;h++) {
				t=new Tile();
				t.leftnum=l;
				t.rightnum=h;
				t.imagename="imgsgame/Tileimg"+l+h+".png";
				TileList.add(t);
			}
		}
	}
	void printgame() {
		System.out.printf("\n\n\t\t\t\t\t\t============게임판============\n\t\t\t\t\t\t");
		for(Tile t:gameTile) {
			t.printgametile();
		}
		System.out.printf("\n\n\t\t\t\t\t\t남은 뽑을 수 있는 타일 수 : %d\n\t\t\t\t\t\t좌측값:%d 우측값:%d\n", TileList.size(), mostleft, mostright);
	}
	void makePlayers() {
		CPPlayerList.add(CP);
	}
	void PlayersDraws() {
		for(int i=0;i<7;i++) {
			me.ptList.add(TileList.remove(0));
			CP.ptList.add(TileList.remove(0));
			me.setMyPane(this);
			me.updateInfo(this,"드로우!");
			CP.setCPPane(this);
			me.drawCount(this);
			waiting();
		}
		
	}
	void setleftright(){
		mostleft=gameTile.get(0).leftnum;
		mostright=gameTile.get(gameTile.size()-1).rightnum;
	}
	
	boolean testsizeoff(int kwd) {
		if(kwd<=0||kwd>me.ptList.size()) return true;
		return false;
	}
	int putnum(Tile t) {
		int r=0;
		if(t.leftnum==t.rightnum) {
			if(mostleft==t.leftnum) r++;
			if(mostright==t.rightnum) r++;
		}
		else {
			if (mostleft == t.leftnum) r++;
			if (mostleft == t.rightnum) r++;
			if (mostright == t.leftnum) r++;
			if (mostright == t.rightnum) r++;
		}
		return r;
	}
	void putnewTile(Tile putTile, int i) {
		Tile t=new Tile();
		t.leftnum=putTile.leftnum;
		t.rightnum=putTile.rightnum;
		t.imagename=putTile.imagename;
		if(judgeSwitch(t,i)) t.switching();
		if(i==0) gameTile.add(0,t);
		else if(i==1) gameTile.add(t);
	}
	boolean judgeSwitch(Tile putTile, int i) {			
		if(i==0&&putTile.leftnum==mostleft) return true;
		if(i==1&&putTile.rightnum==mostright) return true;
		return false;
	}
	
	public void reset(JPanel mainPanel,CardLayout cardLayout) {
		TileList.clear();
		gameTile.clear();
		me.ptList.clear();
		me.sum=0;
		CP.ptList.clear();
		CP.sum=0;
		CPPlayerList.clear();
		imageList.clear();
		lgtile=0;
		rgtile=0;
	
		gamePanel=new JPanel();
		// 모든 컴포넌트 제거
	    
	    Container contentPane = gamePanel.getRootPane(); // gamePanel이 null이 아닌지 확인 후 접근
	    if (contentPane != null) {
	        contentPane.removeAll(); // 모든 컴포넌트를 제거
	        contentPane.revalidate(); // 레이아웃을 다시 계산
	        contentPane.repaint();    // 화면을 다시 그리기
	    }
	    /*contentPane.removeAll(); // 모든 컴포넌트를 제거
	    contentPane.revalidate(); // 레이아웃을 다시 계산
	    contentPane.repaint();    // 화면을 다시 그리기
	    //mainFrame.dispose();
	    //cardLayout.show(mainPanel, "게임");
	    gamePanel.add(contentPane);*/
	 
		gamePanel.repaint(); // UI 재그리기
		//mainPanel.add(gamePanel,"게임");
		//cardLayout.show(mainPanel, " 게임");
        //gamePanel.pack();    // 프레임 크기 다시 설정
        //gamePanel.setVisible(true); // 화면에 보이도록 설정
		
	}
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	final int mtsizew=100;
	final int mtsizeh=50;
	
	private static final Object lock=new Object();
	JPanel mypane;
	JPanel CPpane;
	JPanel Drpane;
	JPanel gamepane;
	JPanel infopane;
    ArrayList<JLabel> imageList = new ArrayList<>(); // 이미지 레이블 리스트
    
    //여기서 '게임'으로 화면이 전환됨
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
	
	//static JFrame mainFrame=new JFrame("Domino");
	static JPanel gamePanel; //game에서의 메인 패널. 
	static DominoUI main=null;
	
	//수정할 부분
	private JPanel createAndShowGUI() {
		//mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mypane = setupMyPanel();
		CPpane = setupCPPanel();
		Drpane = setupDrawPanel();
		gamepane= setupGamePanel();
		infopane= setupInfoPanel();
		//mainFrame.setPreferredSize(new Dimension(1500, 800));

	    // 레이아웃을 null로 설정하여 절대 위치 사용
	    gamePanel.setLayout(null);  // null 레이아웃으로 설정

	    // mypane의 위치와 크기 설정 (아래쪽에 배치)
	    mypane.setPreferredSize(new Dimension(1000, 180));
	    mypane.setBounds(0, 900, 1000, 180);
	    
	    // CPpane의 위치와 크기 설정 (위쪽에 배치)
	    CPpane.setPreferredSize(new Dimension(1000, 100));
	    CPpane.setBounds(0, 0, 1000, 100);  // (x, y, width, height)
	    
	    
	    // Drpane의 위치와 크기 설정 (오른쪽에 배치)
	    Drpane.setPreferredSize(new Dimension(100, 450));
	    Drpane.setBounds(1300, 0, 100, 450);// (x, y, width, height)
	    

	    // gamepane의 위치와 크기 설정 (가운데 배치)
	    gamepane.setPreferredSize(new Dimension(900, 450));
	    gamepane.setBounds(0, 150, 900, 450);  // (x, y, width, height)
	    
	    
	    infopane.setPreferredSize(new Dimension(1000,50));
	    infopane.setBounds(0, 730, 1000, 50);
	    

	    // gamePanel에 추가
	    gamePanel.add(mypane);
	    gamePanel.add(CPpane);
	    gamePanel.add(Drpane);
	    gamePanel.add(gamepane);
	    gamePanel.add(infopane);
	    
	    gamePanel.addComponentListener(new ComponentAdapter() {
	        @Override
	        public void componentResized(ComponentEvent e) {
	            adjustPanelPositions(mypane, CPpane, Drpane, gamepane, infopane);
	        }
	    });
		//gamePanel.pack();
		//mainFrame.setVisible(true);
	   
	    return gamePanel;
		
	}
	public void adjustPanelPositions(JPanel mypane, JPanel CPpane, JPanel Drpane, JPanel gamepane, JPanel infopane) {
	    // 프레임의 현재 크기
		int frameWidth = gamePanel.getWidth();
	    int frameHeight = gamePanel.getHeight();

	    // 각 패널의 가로 위치만 중앙으로 정렬
	    int mypaneX = (frameWidth - 1000) / 2;  // mypane의 가로 중앙
	    mypane.setBounds(mypaneX, 550, 1000, 180);

	    int CPpaneX = (frameWidth - 1000) / 2;  // CPpane의 가로 중앙
	    CPpane.setBounds(CPpaneX, 0, 1000, 100);

	    int DrpaneX = (frameWidth - 1000)/2+900;  // Drpane은 오른쪽에 고정
	    Drpane.setBounds(DrpaneX, 100, 100, 450);

	    int gamepaneX = (frameWidth - 1000) / 2;  // gamepane의 가로 중앙
	    gamepane.setBounds(gamepaneX, 100, 900, 450);

	    int infopaneX = (frameWidth-1000)/2;
	    infopane.setBounds(infopaneX, 730, 1000, 50);

	    // 레이아웃 업데이트
	    gamePanel.revalidate();
	    gamePanel.repaint();
	}
	
	
	
	
	
	JPanel setupMyPanel() {
	    JPanel newmypane = new JPanel() {
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g); // 기존 배경 그리기
	            g.setColor(Color.BLACK); // 실선 색 설정
	            g.drawRect(0, 0, getWidth(), 0); // 실선 테두리 그리기
	        }
	    };
	    return newmypane;
	}
	

	int rgtile=0;
	int lgtile=0;
	
	JPanel setupCPPanel() {
		JPanel cppane=new JPanel();
		cppane.setPreferredSize(new Dimension(500,100));
		BufferedImage cptile=Imgmgr.loadImage("imgsgame/Tileimgback.png");
		JLabel cp1name=new JLabel("CP");
		JLabel lb=new JLabel(new ImageIcon(cptile));
		JLabel cptiles=new JLabel("X"+CP.ptList.size());
		lb.setBounds(450,40,mtsizew,mtsizeh);

		cp1name.setBounds(490,-10,mtsizeh,mtsizeh);
		cp1name.setFont(font);
		cptiles.setBounds(560,40,mtsizeh,mtsizeh);
        cptiles.setFont(font);
        
        
        cppane.add(cp1name,BorderLayout.CENTER);
		cppane.add(lb);
		cppane.add(cptiles);
		cppane.setLayout(null);
		return cppane;
	}
	JPanel setupDrawPanel() {
		JPanel drawpane=new JPanel();
		drawpane.setPreferredSize(new Dimension(100,450));
		BufferedImage rotimg=Imgmgr.rotateimg("imgsgame/Tileimgback.png", 1);
		JLabel lb=new JLabel(new ImageIcon(rotimg));
		JLabel countdraw=new JLabel("X"+TileList.size());
		lb.setBounds(5, 150, mtsizeh, mtsizew);
		
		countdraw.setBounds(56, 175, mtsizeh, mtsizeh);
		countdraw.setFont(font);

		drawpane.add(lb);
		drawpane.add(countdraw);

		drawpane.setLayout(null);
		return drawpane;
	}
	JPanel setupGamePanel() {
		JPanel gamepane=new JPanel(){
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g); // 기존 배경 그리기
	            g.setColor(Color.black); // 실선 색 설정
	            int margin = 1; // 여백을 주려면 이 값을 변경
	            g.drawRect(0, 1, getWidth() - 2 * margin, getHeight() - 2 * margin);
	        }
	    };
	    gamepane.setLayout(null);
		return gamepane;
	}
	JPanel setupInfoPanel() {
		JPanel infopane=new JPanel() {
			protected void paintComponent(Graphics g) {
		        super.paintComponent(g); // JPanel의 기본 페인팅을 호출하여 배경을 그리도록 함
		        g.setColor(Color.BLACK); // 선 색을 검정색으로 설정
		        int margin = 1; // 선의 두께를 1로 설정

		        // 위쪽에만 선을 그리기 (x1, y1, x2, y2)
		        g.drawLine(0, margin, getWidth(), margin); // (0, margin)에서 (getWidth(), margin)까지 선을 그림
		    }
		};
		infopane.setBackground(Color.white);
		infopane.setPreferredSize(new Dimension(1000,50));
		
		String modest=null;
		if(modenum==0)modest="일반모드";
		else if(modenum==1) modest="도전모드("+(win+1)+"/"+ChallengeGoal+")";
		JLabel mode=new JLabel(modest);
		mode.setFont(infofont);
		mode.setBounds(5, 0, modest.length()*20, 50);
		
		
		infopane.add(mode);
		infopane.setLayout(null);
		return infopane;
	}
	public static int win;
	public static int lose;
	static int modenum=1;
	
	//일반모드
	public double PlayNormal(MainPage mainPage,DominoUI main,JPanel mainPanel,CardLayout cardLayout) {
		win=0;
		lose=0;
		modenum=0;
		while(true) {
			main.reset(mainPanel,cardLayout);
			//mainFrame.dispose();
		    main.startGUI(mainPage,mainPanel,cardLayout); 
			main.gameset();
			main.gamestart();
			main.gameend(mainPanel,cardLayout);
			if (stopgame)
				break;
		}
		//mainFrame.dispose();
		cardLayout.show(mainPanel,"MainPage");
		mainPage.setSize(1000, 800);
		mainPage.setLocationRelativeTo(null);
		String str=win+"."+lose;
		return Double.parseDouble(str);
	}
	
	static int ChallengeGoal;
	public int PlayChallenge(MainPage mainPage,DominoUI main,int goal,JPanel mainPanel,CardLayout cardLayout) {
		modenum=1;
		win=0;
		lose=0;
		ChallengeGoal=goal;
		me.score=0;
		String msg=String.format("%d연승에 도전합니다", goal);
		JOptionPane.showMessageDialog(null, msg, "도전모드", JOptionPane.INFORMATION_MESSAGE);
		for(int i=1;i<=goal;i++) {
			//게임 시작전에 리셋
			main = new DominoUI();
			main.reset(mainPanel,cardLayout);
			//mainFrame.dispose();
			//여기서부터 게임시작
			main.startGUI(mainPage,mainPanel,cardLayout);
			main.gameset();
			main.gamestart();
			main.gameend(mainPanel,cardLayout);
			if(lose>=1) {
				msg=String.format("도전실패(-%d점)", (goal-1)*50);
				JOptionPane.showMessageDialog(null, msg, "도전모드", JOptionPane.INFORMATION_MESSAGE);
				//mainFrame.dispose();
				cardLayout.show(mainPanel, "MainPage");
				mainPage.setSize(1000, 800);
				mainPage.setLocationRelativeTo(null);
				return -win;
			}
		}
		msg=String.format("도전성공(+%d점)", (goal-1)*50);
		JOptionPane.showMessageDialog(null, msg, "도전모드", JOptionPane.INFORMATION_MESSAGE);
		//mainFrame.dispose();
		cardLayout.show(mainPanel, "MainPage");
		mainPage.setSize(1000, 800);
		mainPage.setLocationRelativeTo(null);
		
		return me.score;
	}
	

}
