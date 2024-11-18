package domino;

import javax.swing.*;
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

		// 다른 페이지 패널 추가
		JPanel rankingPagePanel = createRankingPagePanel();
		mainPanel.add(rankingPagePanel, "RankingPage");

		JPanel gameDescriptionPanel = createGameDescriptionPanel();
		mainPanel.add(gameDescriptionPanel, "GameDescriptionPage");

		JPanel myPagePanel = createMyPagePanel();
		mainPanel.add(myPagePanel, "MyPage");

		JPanel normalModePanel = createNormalModePanel();
		mainPanel.add(normalModePanel, "NormalModePage");

		JPanel challengeModePanel = createChallengeModePanel();
		mainPanel.add(challengeModePanel, "ChallengeModePage");

		JPanel loginPagePanel = createLoginPagePanel();
		mainPanel.add(loginPagePanel, "LoginPage");
	}

	private JPanel createMainPagePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(Color.white);

		// 이미지 아이콘 설정
		ImageIcon dominoIcon = new ImageIcon("domino.jpg");
		JLabel iconLabel = new JLabel(dominoIcon);
		iconLabel.setBounds(60, 210, 480, 480);
		panel.add(iconLabel);
		ImageIcon gameDescriptionIcon = new ImageIcon("description.jpg");
		ImageIcon rankingIcon = new ImageIcon("ranking.jpg");
		ImageIcon myPageIcon = new ImageIcon("mypage.jpg");
		ImageIcon logoutIcon = new ImageIcon("logout.jpg");

		// 버튼 생성 및 설정
		RoundButton btnGameDescription = new RoundButton("");
		btnGameDescription.setIcon(gameDescriptionIcon);
		btnGameDescription.setBounds(60, 30, 70, 70);
		panel.add(btnGameDescription);
		btnGameDescription.addActionListener(e -> cardLayout.show(mainPanel, "GameDescriptionPage"));

		JLabel lblGameDescription = new JLabel("게임설명", SwingConstants.CENTER);
		lblGameDescription.setBounds(55, 110, 80, 20);
		panel.add(lblGameDescription);

		RoundButton btnRanking = new RoundButton("");
		btnRanking.setIcon(rankingIcon);
		btnRanking.setBounds(150, 30, 70, 70);
		panel.add(btnRanking);
		btnRanking.addActionListener(e -> cardLayout.show(mainPanel, "RankingPage"));

		JLabel lblRanking = new JLabel("랭킹", SwingConstants.CENTER);
		lblRanking.setBounds(145, 110, 80, 20);
		panel.add(lblRanking);

		RoundButton btnMyPage = new RoundButton("");
		btnMyPage.setIcon(myPageIcon);
		btnMyPage.setBounds(240, 30, 70, 70);
		panel.add(btnMyPage);
		btnMyPage.addActionListener(e -> cardLayout.show(mainPanel, "MyPage"));

		JLabel lblMyPage = new JLabel("마이페이지", SwingConstants.CENTER);
		lblMyPage.setBounds(235, 110, 80, 20);
		panel.add(lblMyPage);

		RoundButton btnLogout = new RoundButton("");
		btnLogout.setIcon(logoutIcon);
		btnLogout.setBounds(850, 30, 70, 70);
		panel.add(btnLogout);
		btnLogout.addActionListener(e -> showLogoutConfirmation());

		JLabel lblLogout = new JLabel("로그아웃", SwingConstants.CENTER);
		lblLogout.setBounds(845, 110, 80, 20);
		panel.add(lblLogout);

		JButton btnNormalMode = new JButton("일반 모드");
		btnNormalMode.setBackground(Color.BLUE);
		btnNormalMode.setForeground(Color.WHITE);
		btnNormalMode.setBounds(660, 340, 220, 70);
		panel.add(btnNormalMode);
		btnNormalMode.addActionListener(e -> cardLayout.show(mainPanel, "NormalModePage"));

		JButton btnChallengeMode = new JButton("도전 모드");
		btnChallengeMode.setBackground(Color.RED);
		btnChallengeMode.setForeground(Color.WHITE);
		btnChallengeMode.setBounds(660, 420, 220, 70);
		panel.add(btnChallengeMode);
		btnChallengeMode.addActionListener(e -> cardLayout.show(mainPanel, "ChallengeModePage"));

		JButton btnExit = new JButton("게임 종료");
		btnExit.setBackground(Color.DARK_GRAY);
		btnExit.setForeground(Color.WHITE);
		btnExit.setBounds(710, 500, 120, 50);
		panel.add(btnExit);
		btnExit.addActionListener(e -> showExitConfirmation());

		return panel;
	}

	private void showLogoutConfirmation() {
		int response = JOptionPane.showConfirmDialog(this, "로그아웃하시겠습니까?", "로그아웃", JOptionPane.YES_NO_OPTION);
		if (response == JOptionPane.YES_OPTION) {
			cardLayout.show(mainPanel, "LoginPage");
		}
	}

	private void showExitConfirmation() {
		int response = JOptionPane.showConfirmDialog(this, "게임을 종료하시겠습니까?", "게임 종료", JOptionPane.YES_NO_OPTION);
		if (response == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	private JPanel createRankingPagePanel() {
		JPanel panel = new JPanel();
		panel.add(new JLabel("랭킹 페이지"));
		return panel;
	}

	private JPanel createGameDescriptionPanel() {
		JPanel panel = new JPanel();
		panel.add(new JLabel("게임 설명 페이지"));
		return panel;
	}

	private JPanel createMyPagePanel() {
		JPanel panel = new JPanel();
		panel.add(new JLabel("마이페이지"));
		return panel;
	}

	private JPanel createNormalModePanel() {
		JPanel panel = new JPanel();
		panel.add(new JLabel("일반 모드 페이지"));
		return panel;
	}

	private JPanel createChallengeModePanel() {
		JPanel panel = new JPanel();
		panel.add(new JLabel("도전 모드 페이지"));
		return panel;
	}

	private JPanel createLoginPagePanel() {
		JPanel panel = new JPanel();
		panel.add(new JLabel("로그인 페이지"));
		return panel;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			MainPage mainPage = new MainPage();
			mainPage.setVisible(true);
		});
	}
}
