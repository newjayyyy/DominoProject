package Domino;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;








public class DominoUI {
	
	Scanner scan=new Scanner(System.in);
	Random rand=new Random();
	ArrayList<Tile> TileList=new ArrayList<Tile>();
	ArrayList<Tile> gameTile=new ArrayList<Tile>();
	ArrayList<CPPlayer> CPPlayerList=new ArrayList<CPPlayer>();
	Player me=new Player();
	CPPlayer CP=new CPPlayer(1);
	int endcount[]=new int[2];
	int mostleft=-1, mostright=-1;
	Font font = new Font("Arial", Font.PLAIN, 15);  // "PLAIN", 일반 글씨, 크기 30
	void waiting() {
		int wt=1;												//기다릴 시간 wt초
		try {
			Thread.sleep(wt*500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void gameset() {
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
				t.imagename="imgs/Tileimg"+l+h+".png";
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
	boolean judgeSwitch(Tile putTile, int i) {				////////스위치가 발생하는지 테스트 하기
		if(i==0&&putTile.leftnum==mostleft) return true;
		if(i==1&&putTile.rightnum==mostright) return true;
		return false;
	}
	
	
	
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	final int mtsizew=100;
	final int mtsizeh=50;
	
	private static final Object lock=new Object();
	JPanel mypane;
	JPanel CPpane;
	JPanel Drpane;
	JPanel gamepane;
    ArrayList<JLabel> imageList = new ArrayList<>(); // 이미지 레이블 리스트
    
	public void startGUI() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
	
	static JFrame mainFrame=new JFrame("Domino");
	static DominoUI main=null;
	
	private void createAndShowGUI() {
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mypane = setupMyPanel();
		CPpane = setupCPPanel();
		Drpane = setupDrawPanel();
		gamepane= setupGamePanel();
		mainFrame.setPreferredSize(new Dimension(1000, 800));

	    // 레이아웃을 null로 설정하여 절대 위치 사용
	    mainFrame.setLayout(null);  // null 레이아웃으로 설정

	    // mypane의 위치와 크기 설정 (아래쪽에 배치)
	    mypane.setPreferredSize(new Dimension(1000, 250));
	    mypane.setBounds(0, 900, 1000, 250);  // (x, y, width, height)

	    // CPpane의 위치와 크기 설정 (위쪽에 배치)
	    CPpane.setPreferredSize(new Dimension(1000, 100));
	    CPpane.setBounds(0, 0, 1000, 100);  // (x, y, width, height)

	    // Drpane의 위치와 크기 설정 (오른쪽에 배치)
	    Drpane.setPreferredSize(new Dimension(100, 450));
	    Drpane.setBounds(1300, 0, 100, 450);  // (x, y, width, height)

	    // gamepane의 위치와 크기 설정 (가운데 배치)
	    gamepane.setPreferredSize(new Dimension(900, 450));
	    gamepane.setBounds(0, 150, 900, 450);  // (x, y, width, height)

	    // 각 패널을 프레임에 추가
	    mainFrame.add(mypane);
	    mainFrame.add(CPpane);
	    mainFrame.add(Drpane);
	    mainFrame.add(gamepane);
	    mainFrame.addComponentListener(new ComponentAdapter() {
	        @Override
	        public void componentResized(ComponentEvent e) {
	            adjustPanelPositions(mypane, CPpane, Drpane, gamepane);
	        }
	    });
		mainFrame.pack();
		mainFrame.setVisible(true);
		
	}
	public void adjustPanelPositions(JPanel mypane, JPanel CPpane, JPanel Drpane, JPanel gamepane) {
	    // 프레임의 현재 크기
		int frameWidth = mainFrame.getWidth();
	    int frameHeight = mainFrame.getHeight();

	    // 각 패널의 가로 위치만 중앙으로 정렬
	    int mypaneX = (frameWidth - 1000) / 2;  // mypane의 가로 중앙
	    mypane.setBounds(mypaneX, frameHeight - 250, 1000, 250);

	    int CPpaneX = (frameWidth - 1000) / 2;  // CPpane의 가로 중앙
	    CPpane.setBounds(CPpaneX, 0, 1000, 100);

	    int DrpaneX = (frameWidth - 1000)/2+900;  // Drpane은 오른쪽에 고정
	    Drpane.setBounds(DrpaneX, 0, 100, 450);

	    int gamepaneX = (frameWidth - 1000) / 2;  // gamepane의 가로 중앙
	    gamepane.setBounds(gamepaneX, 100, 900, 450);


	    // 레이아웃 업데이트
	    mainFrame.revalidate();
	    mainFrame.repaint();
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
	
	private JPanel setupCPPanel() {
		JPanel cppane=new JPanel();
		cppane.setPreferredSize(new Dimension(500,100));
		BufferedImage cptile=Imgmgr.loadImage("imgs/Tileimgback.png");
		JLabel cp1name=new JLabel("CP1");
		JLabel lb=new JLabel(new ImageIcon(cptile));
		JLabel cptiles=new JLabel("X"+CPPlayerList.get(0).ptList.size());
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
	private JPanel setupDrawPanel() {
		JPanel drawpane=new JPanel();
		drawpane.setPreferredSize(new Dimension(100,450));
		BufferedImage rotimg=Imgmgr.rotateimg("imgs/Tileimgback.png", 1);
		JLabel lb=new JLabel(new ImageIcon(rotimg));
		JLabel countdraw=new JLabel("X"+TileList.size());
		lb.setBounds(5, 250, mtsizeh, mtsizew);
		
		countdraw.setBounds(56, 275, mtsizeh, mtsizeh);
		countdraw.setFont(font);

		drawpane.add(lb);
		drawpane.add(countdraw);

		drawpane.setLayout(null);
		return drawpane;
	}
	private JPanel setupGamePanel() {
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
	
	public static void main(String[] args) {
		main=new DominoUI();
		main.gameset();
		main.startGUI();
		main.gamestart();
	}
}
