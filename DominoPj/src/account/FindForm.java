package account;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class FindForm extends JDialog {

	private LoginForm loginForm;

	private JLabel titleLabel;
	private JLabel idLabel;

	private JTextField idTxt;

	private JButton findButton;
	private JButton cancelButton;
	private JPanel centerPanel;
	
	private JPanel findForm;

	public FindForm(LoginForm loginForm) {
		this.loginForm = loginForm;
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

	public JPanel showFindForm(JFrame mainFrame,JPanel mainPanel,CardLayout cardLayout) {
		findForm=new JPanel();
		findForm=setupFindFrame();
		addListeners(mainFrame,mainPanel,cardLayout);
		/*setTitle("Dominos");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setPreferredSize(new Dimension(330, 190));
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
		*/
		return findForm;
	}

	private JPanel setupFindFrame() {
	    JPanel findPanel = new JPanel();
	    findPanel.setLayout(new BoxLayout(findPanel, BoxLayout.Y_AXIS));

	    titleLabel = new JLabel("계정 찾기", JLabel.CENTER);
	    titleLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
	    findPanel.add(titleLabel);

	    findPanel.add(Box.createVerticalStrut(100));

	    JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
	    idLabel = new JLabel("아이디");
	    idTxt = new JTextField(10);
	    centerPanel.add(idLabel);
	    centerPanel.add(idTxt);
	    findPanel.add(centerPanel);

	    // 아이디 입력 필드와 버튼 사이의 간격 추가
	    findPanel.add(Box.createVerticalStrut(10));

	    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
	    findButton = new JButton(changeImgSize(new ImageIcon("imgsLogin/findButton.png"), 90, 30));
	    cancelButton = new JButton(changeImgSize(new ImageIcon("imgsLogin/cancelBtn.png"), 90, 25));

	    setButtonImg(findButton, 
	                 changeImgSize(new ImageIcon("imgsLogin/findButton.png"), 90, 30), 
	                 changeImgSize(new ImageIcon("imgsLogin/findButton.png"), 90, 30));
	    setButtonImg(cancelButton, 
	                 changeImgSize(new ImageIcon("imgsLogin/cancelBtn.png"), 90, 25), 
	                 changeImgSize(new ImageIcon("imgsLogin/cancelBtn.png"), 90, 25));

	    buttonPanel.add(findButton);
	    buttonPanel.add(cancelButton);


	    findPanel.add(buttonPanel);

	    // 전체 패널 외부 여백 설정
	    findPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

	    return findPanel;
	}

	public boolean isBlank() {
		boolean result = false;
		if (idTxt.getText().isEmpty()) {
			idTxt.requestFocus();
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
				mainFrame.setSize(600, 400);
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

		findButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (isBlank()) {
					JOptionPane.showMessageDialog(FindForm.this, "아이디를 입력해주세요.");
				} else {
					for (User list : LoginForm.userData) {
						if (list.id.equals(idTxt.getText())) {
							JOptionPane.showMessageDialog(FindForm.this, "비밀번호는 " + list.pw + " 입니다.");
							//dispose();
							//loginForm.setVisible(true);
							cardLayout.show(mainPanel, "로그인페이지");
							return;
						}
					}
					JOptionPane.showMessageDialog(FindForm.this, "존재하지 않는 아이디입니다.");
					idTxt.requestFocus();
				}
			}
		});

	}

}
