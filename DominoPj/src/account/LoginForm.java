package account;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import main.MainPage;

public class LoginForm extends JFrame {
	static ArrayList<User> userData = new ArrayList<>();

	public void readAll(String filename) {
		Scanner filein = openFile(filename);
		User u = null;
		while (filein.hasNext()) {
			u = new User();
			u.read(filein);
			userData.add(u);
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

	private JLabel idLabel;
	private JLabel pwLabel;
	private JTextField idField;
	private JPasswordField pwField;
	private JButton loginbutton;
	private JButton joinButton;
	private JButton findButton;
	public void showLoginForm() {
		
		MainPage mainPageFrame = new MainPage();
		readAll("login.txt");
		setupLoginFrame();
		addListners(mainPageFrame);
		setTitle("Dominos");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(330, 430));
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}

	public ImageIcon changeImgSize(ImageIcon imgIcon, int w, int h) {
		Image img = imgIcon.getImage();
		Image changeImg = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
		ImageIcon changeIcon = new ImageIcon(changeImg);
		return changeIcon;
	}

	void setButtonImg(JButton button, ImageIcon img1, ImageIcon img2) {
		button.setRolloverIcon(img1);
		button.setPressedIcon(img2);
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setFocusPainted(false);
	}

	public void setupLoginFrame() {
		idLabel = new JLabel("아이디", JLabel.CENTER);
		pwLabel = new JLabel("비밀번호", JLabel.CENTER);

		idField = new JTextField(10);
		pwField = new JPasswordField(10);

		ImageIcon loginImg = changeImgSize(new ImageIcon("imgsLogin/loginButton.png"), 100, 25);
		ImageIcon loginImg2 = changeImgSize(new ImageIcon("imgsLogin/loginButton2.png"), 100, 25);
		ImageIcon loginImg3 = changeImgSize(new ImageIcon("imgsLogin/loginButton3.png"), 100, 25);
		ImageIcon joinImg = changeImgSize(new ImageIcon("imgsLogin/joinButton.png"), 80, 25);
		ImageIcon joinImg2 = changeImgSize(new ImageIcon("imgsLogin/joinButton2.png"), 80, 25);
		ImageIcon joinImg3 = changeImgSize(new ImageIcon("imgsLogin/joinButton3.png"), 80, 25);
		ImageIcon findImg = changeImgSize(new ImageIcon("imgsLogin/findButton.png"), 80, 25);
		ImageIcon findImg2 = changeImgSize(new ImageIcon("imgsLogin/findButton2.png"), 80, 25);
		ImageIcon findImg3 = changeImgSize(new ImageIcon("imgsLogin/findButton3.png"), 80, 25);

		loginbutton = new JButton(loginImg);
		joinButton = new JButton(joinImg);
		findButton = new JButton(findImg);

		JPanel northPanel = new JPanel();
		JLabel titleLabel = new JLabel("로그인");
		northPanel.add(titleLabel);

		JPanel centerPanel = new JPanel();
		JLabel imgLabel = new JLabel();
		ImageIcon dominoImg = new ImageIcon("imgsLogin/domino2.png");
		imgLabel.setIcon(dominoImg);
		JPanel dataPanel = new JPanel(new GridLayout(2, 2, 10, 5));
		dataPanel.add(idLabel);
		dataPanel.add(idField);
		dataPanel.add(pwLabel);
		dataPanel.add(pwField);
		JPanel loginPanel = new JPanel();
		setButtonImg(loginbutton, loginImg2, loginImg3);
		loginPanel.add(loginbutton);
		centerPanel.add(imgLabel);
		centerPanel.add(dataPanel);
		centerPanel.add(loginPanel);

		JPanel southPanel = new JPanel();
		setButtonImg(joinButton, joinImg2, joinImg3);
		southPanel.add(joinButton);
		setButtonImg(findButton, findImg2, findImg3);
		southPanel.add(findButton);

		add(northPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
	}

	public void addListners(JFrame mainFrame) {
		
		joinButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				JoinForm join = new JoinForm(LoginForm.this);
				join.showJoinForm();
				idField.setText("");
				pwField.setText("");
			}
		});

		loginbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean loggedIn = false;
				for (User list : userData) {
					if (list.id.equals(idField.getText()) && list.pw.equals(pwField.getText())) {
						JOptionPane.showMessageDialog(null, "로그인 성공");
						loggedIn = true;
						dispose();
						
						//mainPage 다시 보이기
						mainFrame.setVisible(true);
						break;
					}
				}
				if (!loggedIn) {
					JOptionPane.showMessageDialog(null, "아이디 또는 비밀번호가 잘못 되었습니다.");
				}
			}
		});
		
		findButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				FindForm find = new FindForm(LoginForm.this);
				find.showJoinForm();
				idField.setText("");
				pwField.setText("");
			}
		});
	}

	/*public static void main(String[] args) {
		
		LoginForm loginMain = new LoginForm();
		loginMain.showLoginForm();
	}*/
}
