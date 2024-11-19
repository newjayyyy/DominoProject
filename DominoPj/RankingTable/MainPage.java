package RankingTable;

import javax.swing.*;

public class MainPage extends JFrame {

    public MainPage() {
        setTitle("Main Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(null);

        JButton btnRanking = new JButton("랭킹 보기");
        btnRanking.setBounds(100, 100, 200, 50);
        btnRanking.addActionListener(e -> {
            // 메인 창 숨기기
            setVisible(false);

            // RankingTable 창 생성 및 표시
            new RankingTable(this);
        });

        add(btnRanking);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainPage::new);
    }
}