package domino;

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
		readAll("login.txt");
		setupLoginFrame();
		addListners();
		setTitle("Dominos");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(330, 420));
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}

	public ImageIcon changeImgSize(ImageIcon imgIcon) {
		Image img = imgIcon.getImage();
		Image changeImg = img.getScaledInstance(100, 25, Image.SCALE_SMOOTH);
		ImageIcon changeIcon = new ImageIcon(changeImg);
		return changeIcon;
	}

	void setButtonImg(JButton button, ImageIcon img1, ImageIcon img2) {
		loginbutton.setRolloverIcon(img1);
		loginbutton.setPressedIcon(img2);
		loginbutton.setBorderPainted(false);
		loginbutton.setContentAreaFilled(false);
		loginbutton.setFocusPainted(false);
	}

	public void setupLoginFrame() {
		idLabel = new JLabel("아이디", JLabel.CENTER);
		pwLabel = new JLabel("비밀번호", JLabel.CENTER);

		idField = new JTextField(10);
		pwField = new JPasswordField(10);

		ImageIcon loginImg = changeImgSize(new ImageIcon("imgs/loginButton.png"));
		ImageIcon loginImg2 = changeImgSize(new ImageIcon("imgs/loginButton2.png"));
		ImageIcon loginImg3 = changeImgSize(new ImageIcon("imgs/loginButton3.png"));

		loginbutton = new JButton(loginImg);
		joinButton = new JButton("회원가입");
		findButton = new JButton("계정찾기");

		JPanel northPanel = new JPanel();
		JLabel titleLabel = new JLabel("로그인");
		northPanel.add(titleLabel);

		JPanel centerPanel = new JPanel();
		JLabel imgLabel = new JLabel();
		ImageIcon dominoImg = new ImageIcon("imgs/domino2.png");
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
		southPanel.add(joinButton);
		southPanel.add(findButton);

		add(northPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
	}

	public void addListners() {
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

	public static void main(String[] args) {
		LoginForm main = new LoginForm();
		main.showLoginForm();
	}
}
