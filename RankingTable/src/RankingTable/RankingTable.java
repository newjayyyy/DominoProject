package RankingTable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RankingTable {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RankingTable::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Ranking Table");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 테이블 모델 정의
        String[] columnNames = {"Ranking", "ID", "Tier", "Score", "Attempts", "Successes"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // 데이터 읽기 및 추가
        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            String line;
            int ranking = 1;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String id = parts[0].trim();
                int attempts = Integer.parseInt(parts[1].trim());
                int successes = Integer.parseInt(parts[2].trim());

                // 점수 계산 및 티어 결정
                int score = calculateScore(successes, attempts);
                String tier = determineTier(score);

                // 테이블에 추가
                model.addRow(new Object[]{ranking++, id, tier, score, attempts, successes});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // JTable 생성
        JTable table = new JTable(model);
        table.getColumn("Tier").setCellRenderer(new TierColor());
        JScrollPane tableScrollPane = new JScrollPane(table);

        // 버튼 추가
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // 점수 계산식 버튼
        JButton helpButton = new JButton("점수 계산식에 대한 설명이 필요하신가요?");
        helpButton.setPreferredSize(new Dimension(300, 40));

        JLabel imageLabel = new JLabel();
        imageLabel.setVisible(false);

        // 이미지 로드
        try {
            ImageIcon RankingTable_image = new ImageIcon(RankingTable.class.getResource("/image/RankingTable_image.png"));
            imageLabel.setIcon(RankingTable_image);
        } catch (Exception e) {
            System.err.println("이미지를 로드할 수 없습니다: " + e.getMessage());
        }

        helpButton.addActionListener(e -> {
            // 버튼 클릭 시 이미지 표시 상태에 따라 동작 변경
            boolean isVisible = imageLabel.isVisible();
            imageLabel.setVisible(!isVisible);
            helpButton.setText(isVisible ? "점수 계산식에 대한 설명이 필요하신가요?" : "설명 닫기");
        });
        
        
        
        
        // 메인 화면으로 돌아가기 버튼
        JButton mainMenuButton = new JButton("메인 화면으로 돌아가기");
        mainMenuButton.setPreferredSize(new Dimension(250, 40));
        mainMenuButton.addActionListener(e -> {
            frame.dispose(); // 현재 RankingTable 창 닫기
            main.main(new String[]{}); // main 클래스의 main 메서드 호출하여 새로운 화면 띄우기
        });

        
        
        
        
        
        // 패널에 버튼 추가
        bottomPanel.add(helpButton);
        bottomPanel.add(mainMenuButton);
        bottomPanel.add(imageLabel);

        // 메인 레이아웃 설정
        frame.setLayout(new BorderLayout());
        frame.add(tableScrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // 창 크기 설정
        frame.setSize(800, 600);
        frame.setVisible(true);
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