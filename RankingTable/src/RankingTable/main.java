package RankingTable;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Main Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JLabel label = new JLabel("메인 화면입니다.", SwingConstants.CENTER);
        frame.add(label, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}