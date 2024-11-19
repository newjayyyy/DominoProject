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

	private CardLayout cardLayout;
	private JPanel mainPanel;

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
		ImageIcon gameDescriptionIcon = new ImageIcon("description.jpg");
		ImageIcon rankingIcon = new ImageIcon("ranking.jpg");
		ImageIcon myPageIcon = new ImageIcon("mypage.jpg");
		ImageIcon logoutIcon = new ImageIcon("logout.jpg");

		//게임설명 버튼
		RoundButton btnGameDescription = new RoundButton("");
		btnGameDescription.setIcon(gameDescriptionIcon);
		btnGameDescription.setBounds(60, 30, 70, 70);
		panel.add(btnGameDescription);
		btnGameDescription.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 메인 창 숨기기
                setVisible(false);

                // GameRule 창 생성 및 표시
                GameRule gameRuleFrame = new GameRule(MainPage.this);
                gameRuleFrame.setVisible(true);
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
                // 메인 창 숨기기
                setVisible(false);

                // GameRule 창 생성 및 표시
               RankingTable rankingTableFrame=new RankingTable();
               rankingTableFrame.createAndShowGUI(MainPage.this);
               rankingTableFrame.setVisible(true);
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
                // 메인 창 숨기기
                setVisible(false);

                // mypage 창 생성 및 표시
                Page myPageFrame = new Page(MainPage.this);
                myPageFrame.setVisible(true);
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
                	// 메인 창 숨기기
                    setVisible(false);
        			LoginForm LoginFormFrame = new LoginForm();
        			LoginFormFrame.showLoginForm();
        			LoginFormFrame.setVisible(true);
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
                // 메인 창 숨기기
            	new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                       // 메인 창 숨기기
                       setVisible(false);

                       // 일반모드 창 생성 및 표시
                       DominoUI gameFrame = new DominoUI();
                       gameFrame.gameMain(MainPage.this);
                       return null;
                    }

                    @Override
                    protected void done()    {
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
		btnChallengeMode.addActionListener(e -> cardLayout.show(mainPanel, "ChallengeModePage"));

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
			MainPage mainPage = new MainPage();
			mainPage.setVisible(true);
		});
	}
}
