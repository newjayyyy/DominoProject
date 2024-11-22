package RankingTable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import account.LoginForm;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RankingTable extends JFrame{

    private String userID; // 외부 클래스에서 전달받을 UserID
    private JPanel rankingTablePanel;
	/*
	 * public static void main(String[] args) { // 대충 아무거나 적어둔 것(나중에 외부에서 입력 받으면 수정할
	 * 부분) String externalUserID = "domino5";
	 * 
	 * SwingUtilities.invokeLater(() -> { RankingTable rankingTable = new
	 * RankingTable(); rankingTable.createAndShowGUI(null); }); }
	 */

    public JPanel createRankingTable(JPanel mainPanel,CardLayout cardLayout) {
        // 외부에서 전달받은 UserID 설정
        userID = LoginForm.myData.id;
        rankingTablePanel = new JPanel(new BorderLayout());
        /*setTitle("Ranking Table");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        */

        // 테이블 모델 정의
        String[] columnNames = {"Ranking", "ID", "Tier", "Score", "Attempts", "Successes"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // 데이터 읽기 및 추가
        List<Player> players = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("login.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                String id = parts[0].trim();
                int attempts = Integer.parseInt(parts[5].trim());
                int successes = Integer.parseInt(parts[6].trim());

                // 점수 계산 및 티어 결정
                int score = Integer.parseInt(parts[4].trim());
                String tier = determineTier(score);

                // 플레이어 객체에 저장
                players.add(new Player(id, tier, score, attempts, successes));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 점수 기준 내림차순으로 정렬
        players.sort(Comparator.comparingInt(Player::getScore).reversed());

        // 테이블에 데이터 추가
        Player matchingPlayer = null;
        for (Player player : players) {
            model.addRow(new Object[]{0, player.getId(), player.getTier(), player.getScore(), player.getAttempts(), player.getSuccesses()});
            if (player.getId().equals(userID)) {
                matchingPlayer = player; // 입력된 UserID와 일치하는 플레이어 저장
            }
        }

        // 순위 갱신
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(i + 1, i, 0); // Ranking 컬럼(0번 인덱스) 업데이트
        }

        // 빈 row 추가
        model.addRow(new Object[]{"", "", "", "", "", ""});

        // 입력된 UserID와 동일한 플레이어의 row를 복사하여 테이블 아래에 추가
        if (matchingPlayer != null) {
            model.addRow(new Object[]{
                    "Mydata", matchingPlayer.getId(), matchingPlayer.getTier(),
                    matchingPlayer.getScore(), matchingPlayer.getAttempts(), matchingPlayer.getSuccesses()
            });
        }

        // JTable 생성
        JTable table = new JTable(model);
        table.getColumn("Tier").setCellRenderer(new TierColor()); // Tier 컬러 렌더링
        if (matchingPlayer != null) {
            table.setDefaultRenderer(Object.class, new MydataRenderer(model.getRowCount() - 1)); // 마지막 row 강조
        }

        JScrollPane tableScrollPane = new JScrollPane(table);

        // 버튼 추가
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // 닫기 버튼
        JButton closeButton = new JButton("랭킹 화면 닫기");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "MainPage"); // 메인 페이지로 돌아가기
            }
        });

        // 점수 설명 이미지 버튼
        JButton showImageButton = new JButton("점수 설명 보기");
        showImageButton.addActionListener(e -> showScoreExplanation());

        bottomPanel.add(showImageButton);
        bottomPanel.add(closeButton);

        // 메인 레이아웃 설정
        //rankingTablePanel.setLayout(new BorderLayout());
        rankingTablePanel.add(tableScrollPane, BorderLayout.CENTER);
        rankingTablePanel.add(bottomPanel, BorderLayout.SOUTH);

        // 창 크기 설정
        setSize(1000, 800);
        //setLocationRelativeTo(null);
        return rankingTablePanel;
    }
    
    // 점수 설명 이미지를 표시하는 메서드
    private void showScoreExplanation() {
        
        //magePanel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JDialog imageDialog = new JDialog(this, true);
        imageDialog.setSize(500, 350);
        imageDialog.setLayout(new BorderLayout());
        
        // 이미지 패널 생성
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BorderLayout());
        // 이미지 로드 및 표시
        JLabel imageLabel = new JLabel("Image Placeholder", SwingConstants.CENTER);
		ImageIcon imageIcon = new ImageIcon("image/RankingTable_image.png"); // 이미지 경로
		imageLabel.setIcon(imageIcon);
		imageLabel.setText(null);

		imagePanel.add(imageLabel);
        
		//이미지가 팝업창 형식으로 뜬다.
        imageDialog.add(imagePanel);
        imageDialog.setLocationRelativeTo(this); // 부모 창 기준 중앙에 표시
        imageDialog.setVisible(true);

        //imageFrame.pack();
        //imagePanel.setLocationRelativeTo(null);
        //imageFrame.setVisible(true);
    }

    private static int calculateScore(int successes, int attempts) {
        return attempts == 0 ? 0 : (int) ((double) successes / attempts * 100);
    }

    private static String determineTier(int score) {
        if (score >= 2500) {
            return "Diamond";
        } else if (score >= 2000) {
            return "Platinum";
        } else if (score >= 1500) {
            return "Gold";
        } else if (score >= 1000) {
            return "Silver";
        } else {
            return "Bronze";
        }
    }

    // 플레이어 클래스
    static class Player {
        private final String id;
        private final String tier;
        private final int score;
        private final int attempts;
        private final int successes;

        public Player(String id, String tier, int score, int attempts, int successes) {
            this.id = id;
            this.tier = tier;
            this.score = score;
            this.attempts = attempts;
            this.successes = successes;
        }

        public String getId() {
            return id;
        }

        public String getTier() {
            return tier;
        }

        public int getScore() {
            return score;
        }

        public int getAttempts() {
            return attempts;
        }

        public int getSuccesses() {
            return successes;
        }
    }

    // Tier에 맞는 색상 지정
    static class TierColor extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (value != null) {
                String tier = value.toString();
                switch (tier) {
                    case "Diamond":
                        cell.setBackground(new Color(185, 242, 255));
                        break;
                    case "Platinum":
                        cell.setBackground(Color.GREEN);
                        break;
                    case "Gold":
                        cell.setBackground(Color.YELLOW);
                        break;
                    case "Silver":
                        cell.setBackground(Color.LIGHT_GRAY);
                        break;
                    case "Bronze":
                        cell.setBackground(new Color(205, 127, 50));
                        break;
                    default:
                        cell.setBackground(Color.WHITE);
                        break;
                }
            }

            return cell;
        }
    }

    // Mydata row를 회색으로 렌더링
    static class MydataRenderer extends DefaultTableCellRenderer {
        private final int MydataRow;

        public MydataRenderer(int MydataRow) {
            this.MydataRow = MydataRow;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (row == MydataRow) {
                cell.setBackground(Color.LIGHT_GRAY); // Highlight row를 회색으로 설정
            } else {
                cell.setBackground(Color.WHITE); // 기본 배경색
            }
            return cell;
        }
    }
}