package account;

import java.awt.BorderLayout;
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
	

	public JoinForm(LoginForm loginForm) {
		this.loginForm = loginForm;
	}
	
	void addUser(User u)throws IOException {
		PrintWriter fw = new PrintWriter(new FileWriter("login.txt", true));
		System.out.println();
		fw.println(u.id+" "+u.pw+" "+u.win+" "+u.loss+" "+u.score+" "+u.winNum+" "+u.tryNum);
		fw.close();
	}
	
	public void showJoinForm() {
		setupLoginFrame();
		setTitle("Dominos");
		addListeners();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setPreferredSize(new Dimension(330, 290));
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
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

	private void setupLoginFrame() {
		ImageIcon joinBtn = changeImgSize(new ImageIcon("imgsLogin/joinBtn.png"), 90, 30);
		ImageIcon joinBtn2 = changeImgSize(new ImageIcon("imgsLogin/joinBtn2.png"), 90, 30);
		ImageIcon joinBtn3 = changeImgSize(new ImageIcon("imgsLogin/joinBtn3.png"), 90, 30);
		ImageIcon cancelBtn = changeImgSize(new ImageIcon("imgsLogin/cancelBtn.png"), 70, 25);
		ImageIcon cancelBtn2 = changeImgSize(new ImageIcon("imgsLogin/cancelBtn2.png"), 70, 25);
		ImageIcon cancelBtn3 = changeImgSize(new ImageIcon("imgsLogin/cancelBtn3.png"), 70, 25);
		
		titleLabel = new JLabel("회원가입", JLabel.CENTER);
		idLabel = new JLabel("아이디", JLabel.CENTER);
		pwLabel = new JLabel("비밀번호", JLabel.CENTER);
		reLabel = new JLabel("비밀번호 확인", JLabel.CENTER);

		idTxt = new JTextField(10);
		pwTxt = new JPasswordField(10);
		reTxt = new JPasswordField(10);

		joinButton = new JButton(joinBtn);
		cancelButton = new JButton(cancelBtn);

		JPanel northPanel = new JPanel();
		northPanel.add(titleLabel);

		centerPanel = new JPanel();
		JPanel dataPanel = new JPanel(new GridLayout(3, 2, 10, 30));
		dataPanel.add(idLabel);
		dataPanel.add(idTxt);
		dataPanel.add(pwLabel);
		dataPanel.add(pwTxt);
		dataPanel.add(reLabel);
		dataPanel.add(reTxt);
		JPanel joinPanel = new JPanel();
		setButtonImg(joinButton, joinBtn2, joinBtn3);
		joinPanel.add(joinButton);
		centerPanel.add(dataPanel);
		centerPanel.add(joinPanel);

		JPanel southPanel = new JPanel();
		setButtonImg(cancelButton, cancelBtn2, cancelBtn3);
		southPanel.add(cancelButton);

		add(northPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);

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

	private void addListeners() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				dispose();
				loginForm.setVisible(true);
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				dispose();
				loginForm.setVisible(true);
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
						u.id = idTxt.getText();
						u.pw = pwTxt.getText();
						u.win = 0;
						u.loss = 0;
						u.score = 500;
						u.winNum = 0;
						u.tryNum = 0;
						LoginForm.userData.add(u);
						try {
							addUser(u);
						} catch (IOException e) {
							e.printStackTrace();
						}
						dispose();
						loginForm.setVisible(true);
					}
				}
			}
		});

	}

}
