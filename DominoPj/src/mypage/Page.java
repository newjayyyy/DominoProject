package mypage;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextPane;

import account.LoginForm;

public class Page extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JPanel myPagePanel;
	

	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Page frame = new Page();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/


	public JPanel createMyPage(JPanel mainPanel,CardLayout cardLayout) {
		
		myPagePanel=new JPanel();
		myPagePanel.setLayout(null);
		setSize(1000,800);
	
		JLabel headPane = new JLabel("MyPage");
		headPane.setFont(new Font("Arial", Font.BOLD, 60));
		headPane.setHorizontalAlignment(JLabel.CENTER); // 수평 중앙 정렬
		headPane.setVerticalAlignment(JLabel.TOP);
		headPane.setBounds(207, 50, 600, 100);
		myPagePanel.add(headPane, BorderLayout.NORTH);
		
		// 파일 입출력으로 파일 불러오면 아이디 표시되는 걸로 바꿀 예정
		//myData에서 이름 출력
		JLabel userPane = new JLabel(LoginForm.myData.printName());
		userPane.setFont(new Font("Arial",Font.PLAIN, 30));
		userPane.setBounds(160, 380, 250, 50);
		myPagePanel.add(userPane);
		// 파일 입출력으로 파일 불러오면 전적(OO승 OO패)이 업데이트 되는 걸로 바꿀예정
		//myData에서 전적 출력
		JLabel recordPane = new JLabel(LoginForm.myData.printWinLoss());
		recordPane.setFont(new Font("Arial",Font.PLAIN, 30));
		recordPane.setBounds(160, 430, 250, 50);
		myPagePanel.add(recordPane);
		
		// 프로필 이미지 크기 조정
		ImageIcon icon = new ImageIcon("imgs/profile.jpeg");
		Image img = icon.getImage();
		Image changeImg = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
		
		//프로필 이미지 추가
		ImageIcon profileImg = new ImageIcon(changeImg);
		JLabel profile = new JLabel(profileImg);
		profile.setBounds(100, 100, 300, 300); // 프로필 이미지 위치 조정
		myPagePanel.add(profile);
		
		//티어 이미지 크기 조정
		String tier=null;
		int score=LoginForm.myData.score;
		if(score<1000) tier="imgs/grade_bronze.png";
		else if(score>=1000&&score<1500) tier="imgs/grade_silver.png";
		else if(score>=1500&&score<2000) tier="imgs/grade_gold.png";
		else if(score>=2000&&score<2500) tier="imgs/grade_platinum.png";
		else if(score>=2500) tier="imgs/grade_diamond.png";
		ImageIcon gicon = new ImageIcon(tier);
		Image gimg = gicon.getImage();
		Image changegImg = gimg.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
		
		//티어 이미지 추가(지금은 gold만, 나중에 추가예정)
		ImageIcon gradeImg = new ImageIcon(changegImg);
		JLabel grade = new JLabel(gradeImg);
		grade.setBounds(500, 150, 350, 350); // 프로필 이미지 위치 조정
		myPagePanel.add(grade);

		ImageIcon backImg = new ImageIcon("imgs/Back.png");

		// back 버튼
		Image bImg = backImg.getImage();
		Image changeBImg = bImg.getScaledInstance(130, 60, Image.SCALE_SMOOTH);
		ImageIcon changeBackImg = new ImageIcon(changeBImg);
		JButton btnBack = new JButton(changeBackImg);
		btnBack.setContentAreaFilled(false);
		btnBack.setBorderPainted(false);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cardLayout.show(mainPanel, "MainPage");
			}
		});
		myPagePanel.add(btnBack);

		btnBack.setBounds(440, 570, 130, 60);

		return myPagePanel;

	}
}
