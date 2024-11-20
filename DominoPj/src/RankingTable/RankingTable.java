package RankingTable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import main.MainPage;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RankingTable extends JFrame {
 
    /*public void RankingTable() {
        MainPage mainpageFrame = new MainPage();
        createAndShowGUI(mainpageFrame);
    }*/

    public void createAndShowGUI(JFrame mainFrame) {
        setTitle("Ranking Table");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 테이블 모델 정의
        String[] columnNames = {"Ranking", "ID", "Tier", "Score", "Attempts", "Successes"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // 데이터 읽기 및 추가
        List<Player> players = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String id = parts[0].trim();
                int attempts = Integer.parseInt(parts[1].trim());
                int successes = Integer.parseInt(parts[2].trim());

                // 점수 계산 및 티어 결정
                int score = calculateScore(successes, attempts);
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
        for (Player player : players) {
            model.addRow(new Object[]{0, player.getId(), player.getTier(), player.getScore(), player.getAttempts(), player.getSuccesses()});
        }

        // 순위 갱신 (점수 정렬 후 순위를 1, 2, 3 ...으로 업데이트)
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(i + 1, i, 0); // Ranking 컬럼(0번 인덱스) 업데이트
        }

        // JTable 생성
        JTable table = new JTable(model);
        table.getColumn("Tier").setCellRenderer(new TierColor());

        JScrollPane tableScrollPane = new JScrollPane(table);

        // 버튼 추가
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // 닫기 버튼
        JButton closeButton = new JButton("랭킹 화면 닫기");
        closeButton.addActionListener(e -> {
            dispose(); // 현재 창 닫기
            mainFrame.setVisible(true); // 메인 창 다시 표시
        });

        // 점수 계산식 버튼
        JButton helpButton = new JButton("점수 계산식에 대한 설명이 필요하신가요?");
        helpButton.setPreferredSize(new Dimension(300, 40));

        JLabel imageLabel = new JLabel();
        imageLabel.setVisible(false);

        // 이미지 로드
        try {
            ImageIcon rankingImage = new ImageIcon("image/RankingTable_image.png");
            imageLabel.setIcon(rankingImage);
        } catch (Exception e) {
            System.err.println("이미지를 로드할 수 없습니다: " + e.getMessage());
        }

        helpButton.addActionListener(e -> {
            boolean isVisible = imageLabel.isVisible();
            imageLabel.setVisible(!isVisible);
            helpButton.setText(isVisible ? "점수 계산식에 대한 설명이 필요하신가요?" : "설명 닫기");
        });
        bottomPanel.add(helpButton);
        bottomPanel.add(closeButton);
        bottomPanel.add(imageLabel);

        // 메인 레이아웃 설정
        setLayout(new BorderLayout());
        add(tableScrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // 창 크기 설정
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private static int calculateScore(int successes, int attempts) {
        return attempts == 0 ? 0 : (int) ((double) successes / attempts * 100);
    }

    private static String determineTier(int score) {
        if (score >= 95) {
            return "Diamond";
        } else if (score >= 85) {
            return "Platinum";
        } else if (score >= 75) {
            return "Gold";
        } else if (score >= 60) {
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
}
