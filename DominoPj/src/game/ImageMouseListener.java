package game;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ImageIcon;

import java.awt.Cursor;
import java.awt.Image;

public class ImageMouseListener extends MouseAdapter {
    private final JLabel imageLabel;  // 이벤트가 발생한 이미지 레이블
    private final int index;          // 이미지의 인덱스
    private final JLabel[] selectedImage;  // 선택된 이미지 레이블을 추적
    
    private static int DEFAULT_WIDTH;
    private static int DEFAULT_HEIGHT;
    private static int HOVER_WIDTH;
    private static int HOVER_HEIGHT;
    private static int CLICK_WIDTH;
    private static int CLICK_HEIGHT;

    // 생성자: imageLabel, fxpos, fypos, index, selectedImage를 받아서 초기화
    public ImageMouseListener(JLabel imageLabel,  int index, JLabel[] selectedImage, DominoUI du) {
        this.imageLabel = imageLabel;
        this.index = index;
        this.selectedImage = selectedImage;
        this.DEFAULT_WIDTH=du.mtsizew;
        this.DEFAULT_HEIGHT=du.mtsizeh;
        this.HOVER_WIDTH=(int)(du.mtsizew*1.2);
        this.HOVER_HEIGHT=(int)(du.mtsizeh*1.2);
        this.CLICK_WIDTH=(int)(du.mtsizew*1.3);
        this.CLICK_HEIGHT=(int)(du.mtsizeh*1.3);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // 마우스가 레이블 위에 올라갔을 때 크기를 증가
		/*
		 * imageLabel.setSize(HOVER_WIDTH, HOVER_HEIGHT); imageLabel.setIcon(new
		 * ImageIcon(((ImageIcon)
		 * imageLabel.getIcon()).getImage().getScaledInstance(HOVER_WIDTH, HOVER_HEIGHT,
		 * Image.SCALE_SMOOTH)));
		 */
        imageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // 마우스를 떼었을 때 원래 크기로 돌아가는데, 클릭된 이미지만 크기를 유지
		/*
		 * if (imageLabel != selectedImage[0]) { imageLabel.setSize(DEFAULT_WIDTH,
		 * DEFAULT_HEIGHT); imageLabel.setIcon(new ImageIcon(((ImageIcon)
		 * imageLabel.getIcon()).getImage().getScaledInstance(DEFAULT_WIDTH,
		 * DEFAULT_HEIGHT, Image.SCALE_SMOOTH))); }
		 */
        imageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // 클릭된 이미지를 크기를 증가시킴
        if (selectedImage[0] != null && selectedImage[0] != imageLabel) {
            // 이전에 클릭된 이미지를 원래 크기로 복원
            selectedImage[0].setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
            selectedImage[0].setIcon(new ImageIcon(((ImageIcon) selectedImage[0].getIcon()).getImage().getScaledInstance(DEFAULT_WIDTH, DEFAULT_HEIGHT, Image.SCALE_SMOOTH)));
        }

        // 선택된 이미지를 업데이트하고 크기를 키움
        selectedImage[0] = imageLabel;
        imageLabel.setSize(CLICK_WIDTH, CLICK_HEIGHT);
        imageLabel.setIcon(new ImageIcon(((ImageIcon) imageLabel.getIcon()).getImage().getScaledInstance(CLICK_WIDTH, CLICK_HEIGHT, Image.SCALE_SMOOTH)));
      
        
    }
}
