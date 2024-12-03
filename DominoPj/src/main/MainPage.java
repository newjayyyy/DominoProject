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
	private static MainPage mainPage; // 프레임이 어차피 1개니까 static으로 선언함

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
		// 커스텀 JPanel 생성 (배경 이미지를 그리기 위해 오버라이드)
	    JPanel panel = new JPanel() {
	        private Image backgroundImage = new ImageIcon("imgs/background.png").getImage(); // 배경 이미지 설정

	        @Override
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);
	            // 배경 이미지 그리기
	            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
	        }
	    };

	    panel.setLayout(null);

		// 이미지 아이콘 설정
		//ImageIcon dominoIcon = new ImageIcon("imgs/dominomain.png");
		//JLabel iconLabel = new JLabel(dominoIcon);
		//iconLabel.setBounds(60, 210, 280, 280);
		//panel.add(iconLabel);
		ImageIcon gameDescriptionIcon = new ImageIcon("imgs/rule.png");
		ImageIcon rankingIcon = new ImageIcon("imgs/ranking.png");
		ImageIcon myPageIcon = new ImageIcon("imgs/mypage.png");
		ImageIcon logoutIcon = new ImageIcon("imgs/logout.png");
		ImageIcon gameOverIcon = new ImageIcon("imgs/gameover.png");
		ImageIcon DrawIcon = new ImageIcon("imgs/Draw.png");
		ImageIcon All3Icon = new ImageIcon("imgs/All3.png");
		ImageIcon All5Icon = new ImageIcon("imgs/All5.png");
		//ImageIcon BlockIcon = new ImageIcon("imgs/Block.png"); 	//블록이미지 받으면

		// 게임 설명 버튼
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
		lblGameDescription.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		lblGameDescription.setForeground(Color.white);
		lblGameDescription.setBounds(55, 110, 80, 20);
		panel.add(lblGameDescription);
		
		// 랭킹 버튼
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
		lblRanking.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		lblRanking.setForeground(Color.white);
		lblRanking.setBounds(145, 110, 80, 20);
		panel.add(lblRanking);
		
		// 마이페이지 버튼
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
		lblMyPage.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		lblMyPage.setForeground(Color.white);
		lblMyPage.setBounds(235, 110, 80, 20);
		panel.add(lblMyPage);

		// 로그아웃 버튼
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
		lblLogout.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		lblLogout.setForeground(Color.white);
		lblLogout.setBounds(845, 110, 80, 20);
		panel.add(lblLogout);
		
		// 게임 모드 버튼
	    JButton btnDraw = new JButton();
	    btnDraw.setIcon(DrawIcon);
	    btnDraw.setBounds(700, 280, 220, 70);
	    panel.add(btnDraw);
	    btnDraw.setBorderPainted(false);
	    btnDraw.setContentAreaFilled(false);
	    btnDraw.setFocusPainted(false);

	    JButton btnAll3 = new JButton();
	    btnAll3.setIcon(All3Icon);
	    btnAll3.setBounds(700, 360, 220, 70);
	    panel.add(btnAll3);
	    btnAll3.setBorderPainted(false);
	    btnAll3.setContentAreaFilled(false);
	    btnAll3.setFocusPainted(false);

	    JButton btnAll5 = new JButton();
	    btnAll5.setIcon(All5Icon);
	    btnAll5.setBounds(700, 440, 220, 70);
	    panel.add(btnAll5);
	    btnAll5.setBorderPainted(false);
	    btnAll5.setContentAreaFilled(false);
	    btnAll5.setFocusPainted(false);
	    
	    JButton btnBlock = new JButton();
	    //btnBlock.setIcon(BlockIcon);	//이미지 받으면
	    btnBlock.setBounds(700, 520, 220, 70);
	    panel.add(btnBlock);

	    btnDraw.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            // 다이얼로그 생성 및 표시
	            JDialog dialog = new JDialog(mainPage, "게임 시작", true);
	            dialog.setSize(300, 150);
	            dialog.setLocationRelativeTo(mainPage);
	            dialog.setLayout(new BorderLayout());

	            // 메시지 표시
	            JLabel messageLabel = new JLabel("게임을 시작하시겠습니까?", JLabel.CENTER);
	            dialog.add(messageLabel, BorderLayout.CENTER);

	            // 버튼 패널 생성
	            JPanel buttonPanel = new JPanel();
	            JButton startButton = new JButton("Start");
	            JButton cancelButton = new JButton("Cancel");
	            buttonPanel.add(startButton);
	            buttonPanel.add(cancelButton);
	            dialog.add(buttonPanel, BorderLayout.SOUTH);

	            // Start 버튼 동작
	            startButton.addActionListener(new ActionListener() {
	                public void actionPerformed(ActionEvent e) {
	                    dialog.dispose(); // 다이얼로그 닫기

	                    // 게임 실행 로직
	                    new SwingWorker<Void, Void>() {
	                        @Override
	                        protected Void doInBackground() throws Exception {
	                            // 메인 창 숨기기
	                            // setVisible(false);

	                            // 일반모드 클릭 시 mainPanel이 '게임'으로 전환
	                            DominoUI gameDrawPanel = new DominoUI();
	                            int result = gameDrawPanel.Playdraw(mainPage, gameDrawPanel, mainPanel, cardLayout);

	                            // 데이터 처리
	                            LoginForm.myData.win += DominoUI.win;
	                            LoginForm.myData.loss += DominoUI.lose;
	                            System.out.println(DominoUI.win + "승 " + DominoUI.lose + "패");

	                            // setVisible(true);
	                            return null;
	                        }
	                    }.execute();
	                }
	            });

	            // Cancel 버튼 동작
	            cancelButton.addActionListener(new ActionListener() {
	                public void actionPerformed(ActionEvent e) {
	                    dialog.dispose(); // 다이얼로그 닫기
	                }
	            });

	            dialog.setVisible(true); // 다이얼로그 표시
	        }
	    });

	    btnAll3.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	            // 다이얼로그 생성 및 표시
	            JDialog dialog = new JDialog(mainPage, "게임 시작", true);
	            dialog.setSize(300, 150);
	            dialog.setLocationRelativeTo(mainPage);
	            dialog.setLayout(new BorderLayout());

	            // 메시지 표시
	            JLabel messageLabel = new JLabel("게임을 시작하시겠습니까?", JLabel.CENTER);
	            dialog.add(messageLabel, BorderLayout.CENTER);

	            // 버튼 패널 생성
	            JPanel buttonPanel = new JPanel();
	            JButton startButton = new JButton("Start");
	            JButton cancelButton = new JButton("Cancel");
	            buttonPanel.add(startButton);
	            buttonPanel.add(cancelButton);
	            dialog.add(buttonPanel, BorderLayout.SOUTH);

	            // Start 버튼 동작
	            startButton.addActionListener(new ActionListener() {
	                public void actionPerformed(ActionEvent e) {
	                    dialog.dispose(); // 다이얼로그 닫기
				new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						// 메인 창 숨기기
						//setVisible(false);
						
						//일반모드 클릭시 mainPanel이 '게임'으로 넘어감 
						DominoUI gameAll3Panel = new DominoUI();
						int result=gameAll3Panel.PlayAll3(mainPage,gameAll3Panel,mainPanel,cardLayout);
						
						//데이터 처리 수정해야하는 부분
						LoginForm.myData.win+=DominoUI.win;
						LoginForm.myData.loss+=DominoUI.lose;
						System.out.println(DominoUI.win+"승"+DominoUI.lose+"패");
						//setVisible(true);
						return null;
					}

					
				}.execute();

			}
	    });
	         // Cancel 버튼 동작
	            cancelButton.addActionListener(new ActionListener() {
	                public void actionPerformed(ActionEvent e) {
	                    dialog.dispose(); // 다이얼로그 닫기
	                }
	            });

	            dialog.setVisible(true); // 다이얼로그 표시
	        }
	    });

	    btnAll5.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	            // 다이얼로그 생성 및 표시
	            JDialog dialog = new JDialog(mainPage, "게임 시작", true);
	            dialog.setSize(300, 150);
	            dialog.setLocationRelativeTo(mainPage);
	            dialog.setLayout(new BorderLayout());

	            // 메시지 표시
	            JLabel messageLabel = new JLabel("게임을 시작하시겠습니까?", JLabel.CENTER);
	            dialog.add(messageLabel, BorderLayout.CENTER);

	            // 버튼 패널 생성
	            JPanel buttonPanel = new JPanel();
	            JButton startButton = new JButton("Start");
	            JButton cancelButton = new JButton("Cancel");
	            buttonPanel.add(startButton);
	            buttonPanel.add(cancelButton);
	            dialog.add(buttonPanel, BorderLayout.SOUTH);

	            // Start 버튼 동작
	            startButton.addActionListener(new ActionListener() {
	                public void actionPerformed(ActionEvent e) {
	                    dialog.dispose(); // 다이얼로그 닫기
				// 일반모드 창 생성 및 표시
				
				new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						// 메인 창 숨기기
						//setVisible(false);
						
						//일반모드 클릭시 mainPanel이 '게임'으로 넘어감 
						DominoUI gameAll5Panel = new DominoUI();
						int result=gameAll5Panel.PlayAll5(mainPage,gameAll5Panel,mainPanel,cardLayout);
						
						//데이터 처리 수정해야하는 부분
						LoginForm.myData.win+=DominoUI.win;
						LoginForm.myData.loss+=DominoUI.lose;
						System.out.println(DominoUI.win+"승"+DominoUI.lose+"패");
						//setVisible(true);
						return null;
					}

					
				}.execute();

			}
	    });
	            // Cancel 버튼 동작
	            cancelButton.addActionListener(new ActionListener() {
	                public void actionPerformed(ActionEvent e) {
	                    dialog.dispose(); // 다이얼로그 닫기
	                }
	            });

	            dialog.setVisible(true); // 다이얼로그 표시
	        }
	    });
	    btnBlock.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	            // 다이얼로그 생성 및 표시
	            JDialog dialog = new JDialog(mainPage, "게임 시작", true);
	            dialog.setSize(300, 150);
	            dialog.setLocationRelativeTo(mainPage);
	            dialog.setLayout(new BorderLayout());

	            // 메시지 표시
	            JLabel messageLabel = new JLabel("게임을 시작하시겠습니까?", JLabel.CENTER);
	            dialog.add(messageLabel, BorderLayout.CENTER);

	            // 버튼 패널 생성
	            JPanel buttonPanel = new JPanel();
	            JButton startButton = new JButton("Start");
	            JButton cancelButton = new JButton("Cancel");
	            buttonPanel.add(startButton);
	            buttonPanel.add(cancelButton);
	            dialog.add(buttonPanel, BorderLayout.SOUTH);

	            // Start 버튼 동작
	            startButton.addActionListener(new ActionListener() {
	                public void actionPerformed(ActionEvent e) {
	                    dialog.dispose(); // 다이얼로그 닫기
				
				new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						// 메인 창 숨기기
						//setVisible(false);
						
						//일반모드 클릭시 mainPanel이 '게임'으로 넘어감 
						DominoUI gameBlockPanel = new DominoUI();
						int result=gameBlockPanel.Playblock(mainPage,gameBlockPanel,mainPanel,cardLayout);
						
						//데이터 처리 수정해야하는 부분
						LoginForm.myData.win+=DominoUI.win;
						LoginForm.myData.loss+=DominoUI.lose;
						System.out.println(DominoUI.win+"승"+DominoUI.lose+"패");
						//setVisible(true);
						return null;
					}

					
				}.execute();

			}
	    });
	    // Cancel 버튼 동작
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose(); // 다이얼로그 닫기
            }
        });

        dialog.setVisible(true); // 다이얼로그 표시
    }
});

		// 게임 종료 버튼
		JButton btnExit = new JButton();
		btnExit.setIcon(gameOverIcon);
		btnExit.setBounds(750, 600, 125, 50);
		panel.add(btnExit);
		btnExit.setBorderPainted(false);
	    btnExit.setContentAreaFilled(false);
	    btnExit.setFocusPainted(false);
		btnExit.addActionListener(e -> showExitConfirmation());
		
		return panel;
	}

	private int showLogoutConfirmation() {
		int response = JOptionPane.showConfirmDialog(this, "로그아웃하시겠습니까?", "로그아웃", JOptionPane.YES_NO_OPTION);
		if (response == JOptionPane.YES_OPTION) {
			return 1;
		} else {
			return 0;
		}

	}

	private void showExitConfirmation() {
		int response = JOptionPane.showConfirmDialog(this, "게임을 종료하시겠습니까?", "게임 종료", JOptionPane.YES_NO_OPTION);
		if (response == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	private static void showGameModeDialog(String mode) {
	    JDialog dialog = new JDialog();
	    dialog.setTitle(mode + " 모드 선택");
	    dialog.setSize(600, 450);
	    dialog.setLocationRelativeTo(null);
	    dialog.setLayout(new BorderLayout());

	    String additionalMessageText = "";
	    String messageText = mode + " 모드로 게임을 시작하시겠습니까?";

	    switch (mode) {
	        case "드로우":
	            additionalMessageText = "드로우 모드는 일반 모드이며 최대 60점을 얻으면 종료합니다.";
	            break;
	        case "ALL3":
	            additionalMessageText = "ALL3 모드는 3의 배수를 맞추는 이벤트 모드이며 최대 90점을 얻으면 종료합니다.";
	            break;
	        case "ALL5":
	            additionalMessageText = "ALL5 모드는 5의 배수를 맞추는 이벤트 모드이며 최대 100점을 얻으면 종료합니다.";
	            break;
	        case "블록":
	        	additionalMessageText = "블록 모드는 드로우가 없는 이벤트 모드이며 최대 30점을 얻으면 종료합니다.";
	            break;
	        default:
	            break;
	    }

	    // 메시지와 이미지를 표시할 패널
	    JPanel contentPanel = new JPanel();
	    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

	    // 위쪽 메시지
	    JLabel message = new JLabel(messageText, SwingConstants.CENTER);
	    message.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
	    message.setAlignmentX(Component.CENTER_ALIGNMENT);
	    contentPanel.add(message);
	    contentPanel.add(Box.createRigidArea(new Dimension(0, 10))); // 간격 추가

	    // 이미지 삽입
	    ImageIcon imageIcon = new ImageIcon("imgs/gamestart.png"); // 이미지 경로를 적절히 수정하세요
	    JLabel imageLabel = new JLabel(imageIcon);
	    imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    contentPanel.add(imageLabel);
	    contentPanel.add(Box.createRigidArea(new Dimension(0, 10))); // 간격 추가

	    // 아래쪽 메시지
	    JLabel additionalMessage = new JLabel(additionalMessageText, SwingConstants.CENTER);
	    additionalMessage.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
	    additionalMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
	    contentPanel.add(additionalMessage);

	    dialog.add(contentPanel, BorderLayout.CENTER);

	    // 버튼 패널
	    JPanel buttonPanel = new JPanel();
	    JButton btnStart = new JButton("시작");
	    JButton btnCancel = new JButton("취소");

	    btnStart.addActionListener(e -> {
	        dialog.dispose();
	        //moveToGamePage(mode);
	    });

	    btnCancel.addActionListener(e -> dialog.dispose());

	    buttonPanel.add(btnStart);
	    buttonPanel.add(btnCancel);
	    dialog.add(buttonPanel, BorderLayout.SOUTH);

	    dialog.setVisible(true);
	}


/*	//게임페이지로 전환
	private static void moveToGamePage(String mode) {
	    if (dominoUI == null) {
	        System.err.println("Error: dominoUI is not initialized.");
	        return;
	    }

	    // DominoUI의 모드 전환
	    System.out.println("Switching to game mode: " + mode);
	    dominoUI.showMode(mode);

	    // CardLayout으로 DominoGame 패널 표시
	    cardLayout.show(mainPanel, "DominoGame");
	}
*/


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