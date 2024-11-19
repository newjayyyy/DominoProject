package game;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

public class Imgmgr {
	public static BufferedImage rotateimg(String imgname,int rotationType) {
		BufferedImage orgimg=loadImage(imgname);
		int angle = 0;
        switch (rotationType%4) {
            case 1: 
                angle = 90;   // 90도 회전
                break;
            case 2: 
                angle = 180;  // 180도 회전
                break;
            case 3: 
                angle = 270;  // 270도 회전
                break;
            case 0: 
            default:
                angle = 0;    // 0도 회전 (변경 없음)
                break;
        }
        BufferedImage rotatedimg=rotateImage(orgimg,angle);
        
        return rotatedimg;
	}
	
	public static BufferedImage rotateimg(Image orgimg, int rotationType) {
	    int angle = 0;
	    switch (rotationType % 4) {
	        case 1:
	            angle = 90;   // 90도 회전
	            break;
	        case 2:
	            angle = 180;  // 180도 회전
	            break;
	        case 3:
	            angle = 270;  // 270도 회전
	            break;
	        case 0:
	        default:
	            angle = 0;    // 0도 회전 (변경 없음)
	            break;
	    }
	    // Image를 BufferedImage로 변환하여 회전 처리
	    BufferedImage bufferedImage = toBufferedImage(orgimg);
	    BufferedImage rotatedimg = rotateImage(bufferedImage, angle);

	    return rotatedimg;
	}
	
	public static BufferedImage rotateimg(BufferedImage orgimg, int rotationType) {
	    int angle = 0;
	    switch (rotationType % 4) {
	        case 1:
	            angle = 90;   // 90도 회전
	            break;
	        case 2:
	            angle = 180;  // 180도 회전
	            break;
	        case 3:
	            angle = 270;  // 270도 회전
	            break;
	        case 0:
	        default:
	            angle = 0;    // 0도 회전 (변경 없음)
	            break;
	    }
	    // 회전된 이미지를 반환
	    BufferedImage rotatedimg = rotateImage(orgimg, angle);

	    return rotatedimg;
	}
	
	public static BufferedImage loadImage(String filePath) {
        try {
            // 이미지 파일을 BufferedImage로 읽어들입니다.
            ImageIcon imageIcon = new ImageIcon(filePath);
            Image image = imageIcon.getImage();
            return resizeImage(toBufferedImage(image),200,100);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

	public static BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}

		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = bufferedImage.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return bufferedImage;
	}

	public static BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
		// 새로운 크기의 BufferedImage를 생성합니다.
		BufferedImage resizedImage = new BufferedImage(width, height, originalImage.getType());

		// Graphics2D를 사용하여 이미지 크기 조정
		Graphics2D g2d = resizedImage.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(originalImage, 0, 0, width, height, null); // 크기 조정
		g2d.dispose();

		return resizedImage;
	}
	
	
	
	public static BufferedImage rotateImage(BufferedImage image, int angle) {
        

        int width = image.getWidth();
        int height = image.getHeight();

        double radians = Math.toRadians(angle);
        int newWidth = (int) Math.abs(width * Math.cos(radians)) + (int) Math.abs(height * Math.sin(radians));
        int newHeight = (int) Math.abs(height * Math.cos(radians)) + (int) Math.abs(width * Math.sin(radians));

        // 회전된 이미지를 담을 새로운 BufferedImage 생성
        BufferedImage rotatedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotatedImage.createGraphics();

        // 회전의 중심을 이미지의 중심으로 설정
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.rotate(Math.toRadians(angle), newWidth / 2, newHeight / 2);
        g2d.drawImage(image, (newWidth - width) / 2, (newHeight - height) / 2, null);
        g2d.dispose();

        return rotatedImage;
    }
	
	
	
	public static BufferedImage transparentimg(BufferedImage image) {
	    // 고정된 투명도 값 설정 (예: 50% 투명도)
	    float alpha = 0.5f;
	    
	    // 이미지 크기와 타입을 가져오기
	    int width = image.getWidth();
	    int height = image.getHeight();
	    
	    // 투명도를 적용한 새로운 BufferedImage 생성
	    BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = newImage.createGraphics();
	    
	    // 알파 합성 설정 (고정된 투명도)
	    AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
	    g2d.setComposite(alphaComposite);
	    
	    // 원본 이미지를 새로운 이미지에 그리기
	    g2d.drawImage(image, 0, 0, null);
	    g2d.dispose();
	    
	    return newImage;
	}
}
