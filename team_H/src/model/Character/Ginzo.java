package model.Character;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Ginzo extends Character_test {

    public Ginzo() {
        super(100);  // 초기 체력 100으로 설정
        loadImage();  // 이미지 로딩
        loadMotions();  // 모션 로딩
	}

	private BufferedImage sprite;

    @Override
    protected void loadImage() {
        try {
            // 스프라이트 시트 로드
            this.sprite = ImageIO.read(new File("res/character/Ginzo.png"));
            this.sprite = TransformColorToTransparency(this.sprite, new Color(255, 255, 255));  // 배경을 투명하게 처리
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 색상을 투명한 색으로 바꾸는 필터 메서드
    protected BufferedImage TransformColorToTransparency(BufferedImage image, Color c1) {
		  final int r1 = c1.getRed();
		  final int g1 = c1.getGreen();
		  final int b1 = c1.getBlue();
		 
		  ImageFilter filter = new RGBImageFilter() {
				public int filterRGB(int x, int y, int rgb) {
					int r = ( rgb & 0xFF0000 ) >> 16;
					int g = ( rgb & 0xFF00 ) >> 8;
					int b = ( rgb & 0xFF );
					if( r == r1 && g == g1 && b == b1) {
						return rgb & 0xFFFFFF;
					}
					return rgb;
				}
			};
		 
			ImageProducer ip = new FilteredImageSource( image.getSource(), filter );
			Image img = Toolkit.getDefaultToolkit().createImage(ip);
			BufferedImage dest = new BufferedImage(img.getWidth(null), 
					img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = dest.createGraphics();
			g.drawImage(img, 0, 0, null);
			g.dispose();
			return dest;
	}

    @Override
    protected void loadMotions() {
        motions = new HashMap<>();

        motions.put(Motion.IDLE, loadMotionSprites(0, 7, 0, 0, 110, 117));  // 가만히 있을 때
        motions.put(Motion.WALK, loadMotionSprites(1, 9, 0, 150, 33, 40));  // 걷기
        motions.put(Motion.ATTACK, loadMotionSprites(2, 11, 0, 425, 35, 78));  // 공격
        motions.put(Motion.DEAD, loadMotionSprites(3, 10, 0, 85, 34, 44));  // 죽음
        motions.put(Motion.DEFENSE, loadMotionSprites(4, 4, 0, 795, 31, 45));  // 막기
        motions.put(Motion.EVENT, loadMotionSprites(5, 19, 0, 550, 30, 43));  // 도발
    }

    private BufferedImage[] loadMotionSprites(int row, int count, int startX, int startY, int width, int height) {
        BufferedImage[] sprites = new BufferedImage[count];

        for (int i = 0; i < count; i++) {
            sprites[i] = sprite.getSubimage(startX + (i * width), startY, width, height);
        }

        return sprites;
    }

	public void updateAnimation() {
		// TODO Auto-generated method stub
		
	}
}
