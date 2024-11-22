package account;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.BoxLayout;
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
	public static ArrayList<User> userData = new ArrayList<>();
	public static User myData = new User();

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
	
	private JPanel loginForm;

	public JPanel showLoginForm(JFrame mainFrame,JPanel mainPanel,CardLayout cardLayout) {

		loginForm=new JPanel();
		if (userData.isEmpty())
			readAll("login.txt");
		loginForm=setupLoginFrame();
		
		//loginForm.setLayout(new BoxLayout(loginForm, BoxLayout.Y_AXIS));
		addListners(mainFrame,mainPanel,cardLayout);
		/*setTitle("Dominos");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(330, 430));
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);*/
		
		return loginForm;
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

	public JPanel setupLoginFrame() {
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

		
		loginForm.add(northPanel, BorderLayout.NORTH);
		loginForm.add(centerPanel, BorderLayout.CENTER);
		loginForm.add(southPanel, BorderLayout.SOUTH);
		
		return loginForm;
	}

	public void addListners(JFrame mainFrame,JPanel mainPanel,CardLayout cardLayout) {

		joinButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//setVisible(false);
				JoinForm join = new JoinForm(LoginForm.this);
				JPanel joinFormPanel=join.showJoinForm(mainFrame,mainPanel,cardLayout);
				mainPanel.add(joinFormPanel,"회원가입페이지");
				cardLayout.show(mainPanel, "회원가입페이지");
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
						// id와 pw가 일치하면 현재 객체를 myData에 저장
						myData = list;
						myData.index = userData.indexOf(list);
						//dispose();
						
						//id, pw 빈칸으로 만들기
						idField.setText("");
						pwField.setText("");

						// mainPage 다시 보이기
						cardLayout.show(mainPanel, "MainPage");
						mainFrame.setSize(1000,800);
						mainFrame.setLocationRelativeTo(null);  
						
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
				JPanel findFormPanel=find.showFindForm(mainFrame,mainPanel,cardLayout);
				mainPanel.add(findFormPanel,"회원가입페이지");
				cardLayout.show(mainPanel, "회원가입페이지");
				idField.setText("");
				pwField.setText("");
			}
		});

		/* 화면 종료시 login.txt에 저장하는 함수 호출 후 종료
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				saveUserData();
				dispose();
			}
		});*/

		saveUserData(); //////////////////////////////////버그: 패널 전환 시 login.txt에 데이터 저장이 되야하는데 안됨
		
		   

	}
	
	//userData에 myData저장 후 userData를 login.txt에 출력
	public void saveUserData() {
		//userData.set(myData.index, myData);
		try {
			new FileWriter("login.txt", false).close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (User list : userData) {
			try {
				list.printToTxt();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	//userData에 myData저장
	/*   public void saveMyData() {
	      userData.set(myData.index, myData);
	   }*/
	/*
	 * public static void main(String[] args) {
	 * 
	 * LoginForm loginMain = new LoginForm(); loginMain.showLoginForm(); }
	 */
}