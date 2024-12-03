package mypage;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import account.LoginForm;

public class Page extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JPanel myPagePanel;
	
	ArrayList<Log> logData = new ArrayList<>();

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
		userPane.setBounds(160, 280, 250, 50);
		myPagePanel.add(userPane);
		// 파일 입출력으로 파일 불러오면 전적(OO승 OO패)이 업데이트 되는 걸로 바꿀예정
		//myData에서 전적 출력
		JLabel recordPane = new JLabel(LoginForm.myData.printWinLoss());
		recordPane.setFont(new Font("Arial",Font.PLAIN, 30));
		recordPane.setBounds(160, 310, 250, 50);
		myPagePanel.add(recordPane);
		
		// 프로필 이미지 크기 조정
		ImageIcon icon = new ImageIcon("imgs/profile.png");
		Image img = icon.getImage();
		Image changeImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		
		//프로필 이미지 추가
		ImageIcon profileImg = new ImageIcon(changeImg);
		JLabel profile = new JLabel(profileImg);
		profile.setBounds(100, 80, 300, 300); // 프로필 이미지 위치 조정
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
		Image changegImg = gimg.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
		
		//티어 이미지 추가(지금은 gold만, 나중에 추가예정)
		ImageIcon gradeImg = new ImageIcon(changegImg);
		JLabel grade = new JLabel(gradeImg);
		grade.setBounds(80, 300, 350, 350); // 프로필 이미지 위치 조정
		myPagePanel.add(grade);

		ImageIcon backImg = new ImageIcon("imgs/Back.png");

		// back 버튼
		Image bImg = backImg.getImage();
		Image changeBImg = bImg.getScaledInstance(190, 60, Image.SCALE_SMOOTH);
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

		btnBack.setBounds(410, 600, 190, 60);
		
		//유저의 게임로그 읽기
		readAll("gamelog/"+LoginForm.myData.id+".txt");
		//전적테이블
		String[] columnNames = {"게임모드", "승패", "진행라운드", "플레이타임", "점수변동"};
		DefaultTableModel model = new DefaultTableModel(columnNames, 0);
		for (Log log : logData) {
            model.addRow(new Object[]{log.getMod(), log.getWinLoss(), log.getRound(), log.getTime(), log.getScoreChange()});   
        }
		JTable table = new JTable(model);
		JScrollPane tableScrollPane = new JScrollPane(table);
		myPagePanel.add(tableScrollPane);
        table.setFillsViewportHeight(true);
        tableScrollPane.setBounds(450, 150, 500, 400);
        
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100); // "게임모드" 열
        columnModel.getColumn(1).setPreferredWidth(50);  // "승패" 열
        columnModel.getColumn(2).setPreferredWidth(100); // "진행라운드" 열
        columnModel.getColumn(3).setPreferredWidth(150); // "플레이타임" 열
        columnModel.getColumn(4).setPreferredWidth(100); // "점수변동" 열

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER); // 가운데 정렬
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setCellRenderer(centerRenderer);
        }
        
		return myPagePanel;

	}
	public void readAll(String filename) {
		Scanner filein = openFile(filename);
		Log l = null;
		while (filein.hasNext()) {
			l = new Log();
			l.read(filein);
			logData.add(l);
		}
		filein.close();
	}
	
	public Scanner openFile(String filename) {
		Scanner filein = null;
		try {
			filein = new Scanner(new File(filename));
		} catch (Exception e) {
			System.out.println(filename + ": 파일 없음");
			System.exit(0);
		}
		return filein;
	}
}
