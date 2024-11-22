package main;

import javax.swing.*;

import RankingTable.RankingTable;
import account.LoginForm;
import game.DominoUI;
import mypage.Page;
import rule.GameRule;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPage extends JFrame {

	private static CardLayout cardLayout;
	private static JPanel mainPanel;
	private static MainPage mainPage; //프레임이 어차피 1개니까 static으로 선언함
	
	public MainPage() {
		// JFrame 설정
		setTitle("도미노 게임");
		setSize(1000, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		getContentPane().setBackground(Color.white);

		// CardLayout과 mainPanel 설정
		cardLayout = new CardLayout();
		mainPanel = new JPanel(cardLayout);
		add(mainPanel);

		// 메인 페이지 패널
		JPanel mainPagePanel = createMainPagePanel();
		mainPanel.add(mainPagePanel, "MainPage");
		
	}
	
	private JPanel createMainPagePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(Color.white);

		// 이미지 아이콘 설정
		ImageIcon dominoIcon = new ImageIcon("imgs/domino.jpg");
		JLabel iconLabel = new JLabel(dominoIcon);
		iconLabel.setBounds(60, 210, 480, 480);
		panel.add(iconLabel);
		ImageIcon gameDescriptionIcon = new ImageIcon("imgs/rule.png");
		ImageIcon rankingIcon = new ImageIcon("imgs/ranking.png");
		ImageIcon myPageIcon = new ImageIcon("imgs/mypage.png");
		ImageIcon logoutIcon = new ImageIcon("imgs/logout.png");

		//게임설명 버튼
		RoundButton btnGameDescription = new RoundButton("");
		btnGameDescription.setIcon(gameDescriptionIcon);
		btnGameDescription.setBounds(60, 30, 70, 70);
		panel.add(btnGameDescription);
		btnGameDescription.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent e) {
	              // GameRule 창 생성
	              GameRule gameRule = new GameRule();
	              JPanel gameRulePanel=gameRule.createGameRule(mainPanel, cardLayout);
	              //gameRuleFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 창 닫기 동작 설정
	              mainPanel.add(gameRulePanel,"게임설명");
	              cardLayout.show(mainPanel, "게임설명");
	              
	              /* GameRule 창의 이벤트 처리 -> 프레임을 하나로 합쳐서 이거 구현은 조금 어려울듯..?
	              gameRuleFrame.addWindowListener(new java.awt.event.WindowAdapter() {
	                  @Override
	                  public void windowClosing(java.awt.event.WindowEvent windowEvent) {
	                      // GameRule 창 닫을 때 메인 페이지 다시 표시
	                      setVisible(true);
	                  }
	              });*/
	          }
	      });

		JLabel lblGameDescription = new JLabel("게임설명", SwingConstants.CENTER);
		lblGameDescription.setBounds(55, 110, 80, 20);
		panel.add(lblGameDescription);
		
		//랭킹버튼
		RoundButton btnRanking = new RoundButton("");
		btnRanking.setIcon(rankingIcon);
		btnRanking.setBounds(150, 30, 70, 70);
		panel.add(btnRanking);
		btnRanking.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        // RankingTable 창 생성
		        RankingTable rankingTable = new RankingTable();
		        //rankingTableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 창 닫기 동작 설정
		        JPanel rankingTablePanel=rankingTable.createRankingTable(mainPanel, cardLayout);
		        mainPanel.add(rankingTablePanel,"랭킹");
	            cardLayout.show(mainPanel, "랭킹");

		        

		        /*RankingTable 창의 이벤트 처리
		        rankingTableFrame.addWindowListener(new java.awt.event.WindowAdapter() {
		            @Override
		            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		                // RankingTable 창 닫을 때 메인 페이지 다시 표시
		                setVisible(true);
		            }
		        });*/
		    }
		});

		JLabel lblRanking = new JLabel("랭킹", SwingConstants.CENTER);
		lblRanking.setBounds(145, 110, 80, 20);
		panel.add(lblRanking);
		
		//마이페이지 버튼
		RoundButton btnMyPage = new RoundButton("");
		btnMyPage.setIcon(myPageIcon);
		btnMyPage.setBounds(240, 30, 70, 70);
		panel.add(btnMyPage);
		btnMyPage.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        // Page 창 생성
		        Page myPage = new Page();
		        JPanel myPagePanel=myPage.createMyPage(mainPanel, cardLayout);
		        mainPanel.add(myPagePanel,"마이페이지");
		        cardLayout.show(mainPanel, "마이페이지");
		        

		        /* Page 창의 이벤트 처리
		        myPageFrame.addWindowListener(new java.awt.event.WindowAdapter() {
		            @Override
		            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		                // Page 창 닫을 때 메인 페이지 다시 표시
		                setVisible(true);
		            }
		        });*/
		    }
		});

		JLabel lblMyPage = new JLabel("마이페이지", SwingConstants.CENTER);
		lblMyPage.setBounds(235, 110, 80, 20);
		panel.add(lblMyPage);

		//로그아웃버튼
		RoundButton btnLogout = new RoundButton("");
		btnLogout.setIcon(logoutIcon);
		btnLogout.setBounds(850, 30, 70, 70);
		panel.add(btnLogout);
		btnLogout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int answer=showLogoutConfirmation();
                if(answer==1) {
                	
                	//패널로 변경하면서 수정한 부분
                	cardLayout.show(mainPanel, "로그인페이지");
                	mainPage.setSize(600, 400);
         	        mainPage.setLocationRelativeTo(null); 
         	        //버그 : 로그아웃 후 로그인 갈떄 텍스트 필드가 채워진 상태로 나옴
        			
                }
            }
		});

		JLabel lblLogout = new JLabel("로그아웃", SwingConstants.CENTER);
		lblLogout.setBounds(845, 110, 80, 20);
		panel.add(lblLogout);
		
		//일반모드 버튼
		JButton btnNormalMode = new JButton("일반 모드");
		btnNormalMode.setBackground(Color.BLUE);
		btnNormalMode.setForeground(Color.WHITE);
		btnNormalMode.setBounds(660, 340, 220, 70);
		panel.add(btnNormalMode);
		btnNormalMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 일반모드 창 생성 및 표시
				
				new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						// 메인 창 숨기기
						//setVisible(false);
						
						//일반모드 클릭시 mainPanel이 '게임'으로 넘어감 
						DominoUI gameNormalPanel = new DominoUI();
						double result=gameNormalPanel.PlayNormal(mainPage,gameNormalPanel,mainPanel,cardLayout);
						
						LoginForm.myData.win+=DominoUI.win;
						LoginForm.myData.loss+=DominoUI.lose;
						System.out.println(DominoUI.win+"승"+DominoUI.lose+"패");
						//setVisible(true);
						return null;
					}

					
				}.execute();

			}
            	
            
		});

		//도전모드 버튼
		JButton btnChallengeMode = new JButton("도전 모드");
		btnChallengeMode.setBackground(Color.RED);
		btnChallengeMode.setForeground(Color.WHITE);
		btnChallengeMode.setBounds(660, 420, 220, 70);
		panel.add(btnChallengeMode);
		btnChallengeMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						Object[] options= {"2","3","4","5","6","취소"};
						int choice=JOptionPane.showOptionDialog(null, "몇 연승에 도전하시겠습니까?", "도전모드", 
								JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,options, options[5]);
						int selectedValue =(choice==5)?-1:Integer.parseInt(options[choice].toString());
						
						System.out.println(selectedValue);
						if (selectedValue >= 2) {
							// 메인 창 숨기기
							//setVisible(false);
							// 'MainPage'->'게임'
							DominoUI gameChallengePanel = new DominoUI();
							int result=gameChallengePanel.PlayChallenge(mainPage,gameChallengePanel,selectedValue,mainPanel,cardLayout);
							//setVisible(true);
							
							if(result>=1) {
								LoginForm.myData.win+=selectedValue;
								LoginForm.myData.score+=(selectedValue-1)*50+result;
								LoginForm.myData.tryNum++;
								LoginForm.myData.winNum++;
							}else {
								LoginForm.myData.win+=-result;
								LoginForm.myData.loss++;
								LoginForm.myData.score+=-(selectedValue-1)*50;
								LoginForm.myData.tryNum++;
							}
						}
						System.out.printf("%d %d %d", LoginForm.myData.score, LoginForm.myData.winNum,LoginForm.myData.tryNum);
						return null;
					}

					
				}.execute();
			} 
		});

		//게임종료 버튼
		JButton btnExit = new JButton("게임 종료");
		btnExit.setBackground(Color.DARK_GRAY);
		btnExit.setForeground(Color.WHITE);
		btnExit.setBounds(710, 500, 120, 50);
		panel.add(btnExit);
		btnExit.addActionListener(e -> showExitConfirmation());

		return panel;
	}

	private int showLogoutConfirmation() {
		int response = JOptionPane.showConfirmDialog(this, "로그아웃하시겠습니까?", "로그아웃", JOptionPane.YES_NO_OPTION);
		if (response == JOptionPane.YES_OPTION) {
			return 1;
		}else {
			return 0;
		}
		
	}

	private void showExitConfirmation() {
		int response = JOptionPane.showConfirmDialog(this, "게임을 종료하시겠습니까?", "게임 종료", JOptionPane.YES_NO_OPTION);
		if (response == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}


	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			mainPage = new MainPage();
			mainPage.setLocationRelativeTo(null);
			mainPage.setVisible(true);
			
			//login 패널 먼저 나옴
			LoginForm loginForm = new LoginForm();
	        JPanel loginFormPanel=loginForm.showLoginForm(mainPage,mainPanel, cardLayout);
	        
	        mainPage.setSize(600, 400);
	        mainPage.setLocationRelativeTo(null);  
	        mainPanel.add(loginFormPanel,"로그인페이지");
	        cardLayout.show(mainPanel, "로그인페이지");
	        
		});
	}
}
