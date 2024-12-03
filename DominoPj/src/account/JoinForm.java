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
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class JoinForm extends JDialog {

	private LoginForm loginForm;

	private JLabel titleLabel;
	private JLabel idLabel;
	private JLabel pwLabel;
	private JLabel reLabel;

	private JTextField idTxt;
	private JTextField pwTxt;
	private JTextField reTxt;

	private JButton joinButton;
	private JButton cancelButton;
	private JPanel centerPanel;
	
	private JPanel joinForm;
	
	public JoinForm(LoginForm loginForm) {
		this.loginForm = loginForm;
	}
	
	/*void addUser(User u)throws IOException {
		PrintWriter fw = new PrintWriter(new FileWriter("login.txt", true));
		System.out.println();
		fw.println(u.id+" "+u.pw+" "+u.win+" "+u.loss+" "+u.score+" "+u.winNum+" "+u.tryNum+"\n");
		fw.close();
	}*/
	
	public JPanel showJoinForm(JFrame mainFrame,JPanel mainPanel,CardLayout cardLayout) {
		
		joinForm=new JPanel();
		joinForm=setupJoinFrame();
		addListeners(mainFrame,mainPanel,cardLayout);
		/*setTitle("Dominos");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setPreferredSize(new Dimension(330, 290));
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);*/
		return joinForm;
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

	private JPanel setupJoinFrame() {
		ImageIcon joinBtn = changeImgSize(new ImageIcon("imgsLogin/joinButton.png"), 90, 30);
		ImageIcon joinBtn2 = changeImgSize(new ImageIcon("imgsLogin/joinButton.png"), 90, 30);
		ImageIcon joinBtn3 = changeImgSize(new ImageIcon("imgsLogin/joinButton.png"), 90, 30);
		ImageIcon cancelBtn = changeImgSize(new ImageIcon("imgsLogin/cancelBtn.png"), 100, 25);
		ImageIcon cancelBtn2 = changeImgSize(new ImageIcon("imgsLogin/cancelBtn.png"), 100, 25);
		ImageIcon cancelBtn3 = changeImgSize(new ImageIcon("imgsLogin/cancelBtn.png"), 100, 25);

		titleLabel = new JLabel("회원가입", JLabel.CENTER);

		idLabel = new JLabel("아이디", JLabel.CENTER);
		pwLabel = new JLabel("비밀번호", JLabel.CENTER);
		reLabel = new JLabel("비밀번호 확인", JLabel.CENTER);

		idTxt = new JTextField(20);
		pwTxt = new JPasswordField(20);
		reTxt = new JPasswordField(20);

		joinButton = new JButton(joinBtn);
		cancelButton = new JButton(cancelBtn);

		JPanel northPanel = new JPanel();
		northPanel.add(titleLabel);

		JPanel centerPanel = new JPanel(new BorderLayout());

		JPanel dataPanel = new JPanel(new GridLayout(3, 2, 0, 75));
		dataPanel.add(idLabel);
		dataPanel.add(idTxt);
		dataPanel.add(pwLabel);
		dataPanel.add(pwTxt);
		dataPanel.add(reLabel);
		dataPanel.add(reTxt);

		JPanel joinPanel = new JPanel();
		setButtonImg(joinButton, joinBtn2, joinBtn3);
		joinPanel.add(joinButton);

		centerPanel.add(dataPanel, BorderLayout.CENTER);
		centerPanel.add(joinPanel, BorderLayout.SOUTH);

		JPanel southPanel = new JPanel();
		setButtonImg(cancelButton, cancelBtn2, cancelBtn3);
		southPanel.add(cancelButton);

		JPanel joinForm = new JPanel(new BorderLayout());
		joinForm.add(northPanel, BorderLayout.NORTH);
		joinForm.add(centerPanel, BorderLayout.CENTER);
		joinForm.add(southPanel, BorderLayout.SOUTH);

		// 외부 여백 설정
		joinForm.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 170, 10, 170));

		return joinForm;
	}

	public boolean isBlank() {
		boolean result = false;
		if (idTxt.getText().isEmpty()) {
			idTxt.requestFocus();
			return true;
		}
		if (String.valueOf(pwTxt.getText()).isEmpty()) {
			pwTxt.requestFocus();
			return true;
		}
		if (String.valueOf(reTxt.getText()).isEmpty()) {
			reTxt.requestFocus();
			return true;
		}
		return result;
	}

	private void addListeners(JFrame mainFrame,JPanel mainPanel,CardLayout cardLayout) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				//dispose();
				//loginForm.setVisible(true);
				cardLayout.show(mainPanel, "로그인페이지");
				mainFrame.setSize(330, 290);
		        mainFrame.setLocationRelativeTo(null); 
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				//dispose();
				cardLayout.show(mainPanel, "로그인페이지");
				mainFrame.setSize(600, 400);
		        mainFrame.setLocationRelativeTo(null); 
			}
		});

		joinButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (isBlank()) {
					JOptionPane.showMessageDialog(JoinForm.this, "모든 정보를 입력해주세요.");
				} else {
					for (User list : LoginForm.userData) {
						if (list.id.equals(idTxt.getText())) {
							JOptionPane.showMessageDialog(JoinForm.this, "이미 존재하는 아이디입니다.");
							idTxt.requestFocus();
							return;
						}
					}
					if (!pwTxt.getText().equals(reTxt.getText())) {
						JOptionPane.showMessageDialog(JoinForm.this, "비밀번호가 일치하지 않습니다.");
						pwTxt.requestFocus();
					} else {
						JOptionPane.showMessageDialog(JoinForm.this, "회원가입을 완료했습니다!");
						User u = new User();
						u.newUser(idTxt.getText(), pwTxt.getText());
						LoginForm.userData.add(u);
						LoginForm.saveUserData();
						//게임로그파일 생성
						try {
							PrintWriter fw = new PrintWriter(new FileWriter("gamelog/"+u.id+".txt", true));
						} catch (IOException e) {
							e.printStackTrace();
						}
						/*try {
							addUser(u);
						} catch (IOException e) {
							e.printStackTrace();
						}*/
						//dispose();
						//loginForm.setVisible(true);
						cardLayout.show(mainPanel, "로그인페이지");
						mainFrame.setSize(600, 400);
				        mainFrame.setLocationRelativeTo(null); 
					}
				}
			}
		});

	}

}
