package rule;

import java.awt.BorderLayout;
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

public class GameRule extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private int index; //현재 인덱스
	private static final String[] IMAGES= {
		"imgs/rule_1.jpg",
		"imgs/rule_2.jpg",
		"imgs/rule_3.jpg",
		"imgs/rule_4.jpg",
		"imgs/rule_5.jpg",
		"imgs/rule_6.jpg"
	};

	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameRule frame = new GameRule();
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	//이미지 크기 조정 함수
	public Image setScale(int index) {
		ImageIcon icon = new ImageIcon(IMAGES[index]);
		Image img=icon.getImage();
		Image changeImg=img.getScaledInstance(750,500,Image.SCALE_SMOOTH);
		return changeImg;
	}
	
	
	public GameRule() {
		
		contentPane=new JPanel();
		contentPane.setLayout(null);
		
		ImageIcon changeIcon = new ImageIcon(setScale(0));
		JLabel lb = new JLabel(changeIcon);
		lb.setBounds(125, 50, 750, 500);
		contentPane.add(lb);
		
		//버튼 이미지
		ImageIcon prevImg=new ImageIcon("imgs/prev.png");
		ImageIcon nextImg=new ImageIcon("imgs/next.png");
		ImageIcon backImg=new ImageIcon("imgs/Back.png");
		
		//prev버튼 
		Image pImg=prevImg.getImage();
		Image changePImg=pImg.getScaledInstance(40,40,Image.SCALE_SMOOTH);
		ImageIcon changePrevImg=new ImageIcon(changePImg);
		JButton btnPrev = new JButton(changePrevImg);
		btnPrev.setBorderPainted(false);
		btnPrev.setContentAreaFilled(false);
		btnPrev.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			if(index > 0) {                                   
			index--;  
			}else {                                            
			index = IMAGES.length -1;                          
			}
			Image currentImg=setScale(index);
			lb.setIcon(new ImageIcon(currentImg));
			
			}
			});
		btnPrev.setBounds(390, 580, 40, 40);
		contentPane.add(btnPrev);
		
		//next 버튼
		Image nImg=nextImg.getImage();
		Image changeNImg=nImg.getScaledInstance(40,40,Image.SCALE_SMOOTH);
		ImageIcon changeNextImg=new ImageIcon(changeNImg);
		JButton btnNext = new JButton(changeNextImg);

		btnNext.setVisible(true);
		btnNext.setBorderPainted(false);
		btnNext.setContentAreaFilled(false);
		btnNext.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			if(index < IMAGES.length -1) {                    
			index++;  
			}else {
				index=0;
			}
			Image currentImg=setScale(index);
			lb.setIcon(new ImageIcon(currentImg));
			}
		});
		btnNext.setBounds(580, 580, 40, 40);
		contentPane.add(btnNext);
		
		//back 버튼
		Image bImg=backImg.getImage();
		Image changeBImg=bImg.getScaledInstance(130,60,Image.SCALE_SMOOTH);
		ImageIcon changeBackImg=new ImageIcon(changeBImg);
		JButton btnBack = new JButton(changeBackImg);
		btnBack.setContentAreaFilled(false);
		btnBack.setBorderPainted(false);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnBack.setBounds(440, 570, 130, 60);
		contentPane.add(btnBack);
		
		
		setTitle("게임설명");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setSize(1000,800);
		add(contentPane);
		
	}
}
