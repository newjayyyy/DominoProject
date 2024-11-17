package domino;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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
	
	void addUser(String id, String pw)throws IOException {
		PrintWriter fw = new PrintWriter(new FileWriter("login.txt", true));
		System.out.println();
		fw.println(id+" "+pw);
		fw.close();
	}
	
	public void showJoinForm() {
		setupLoginFrame();
		setTitle("Dominos");
		addListeners();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setPreferredSize(new Dimension(330, 270));
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void setupLoginFrame() {
		titleLabel = new JLabel("회원가입", JLabel.CENTER);
		idLabel = new JLabel("아이디", JLabel.CENTER);
		pwLabel = new JLabel("비밀번호", JLabel.CENTER);
		reLabel = new JLabel("비밀번호 확인", JLabel.CENTER);

		idTxt = new JTextField(10);
		pwTxt = new JPasswordField(10);
		reTxt = new JPasswordField(10);

		joinButton = new JButton("   가입   ");
		cancelButton = new JButton("취소");

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
		joinPanel.add(joinButton);
		centerPanel.add(dataPanel);
		centerPanel.add(joinPanel);

		JPanel southPanel = new JPanel();
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
						LoginForm.userData.add(u);
						try {
							addUser(idTxt.getText(), pwTxt.getText());
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
