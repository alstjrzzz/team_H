package model.Character;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SuperMan {
    public enum Motion {
        IDLE,      // 가만히 있을 때
        ATTACK,    // 공격
        WALK,      // 걷기
        DEAD       // 죽음
    }

    private BufferedImage sprite;               // 스프라이트 시트
    private Map<Motion, BufferedImage[]> motions;    // 모션별 스프라이트 배열
    private Motion currentMotion = Motion.IDLE;      // 현재 모션
    private int currentSprite = 0;                   // 현재 스프라이트 인덱스

    public SuperMan() {
        loadImage();
        loadMotions(); // 모션 로드 추가
    }

    private void loadImage() {
        try {
            this.sprite = ImageIO.read(new File("res/character/Superman.png"));
            this.sprite = TransformColorToTransparency(this.sprite, new Color(0, 255, 6));  // 배경 투명 처리
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
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

    private void loadMotions() {
        motions = new HashMap<>();

        // 각 모션에 대해 스프라이트 크기와 스프라이트 이미지 배열을 정의
        motions.put(Motion.IDLE, loadMotionSprites(0, 4, 0, 150, 90, 110));  // 가만히 있을 때
        motions.put(Motion.WALK, loadMotionSprites(1, 4, 4, 270, 98, 106));  // 걷기
        motions.put(Motion.ATTACK, loadMotionSprites(2, 2, 2, 385, 120, 100));  // 공격
        motions.put(Motion.DEAD, loadMotionSprites(3, 3, 0, 2040, 130, 100));  // 죽음
    }

    private BufferedImage[] loadMotionSprites(int row, int count, int startX, int startY, int width, int height) {
        BufferedImage[] sprites = new BufferedImage[count];

        for (int i = 0; i < count; i++) {
            sprites[i] = sprite.getSubimage(startX + i*width, startY, width, height);
        }

        return sprites;
    }

    public void setMotion(Motion motion) {
        if (currentMotion != motion) {
            currentMotion = motion;
            currentSprite = 0;  // 새로운 모션을 시작할 때 스프라이트 인덱스 초기화
        }
    }

    public BufferedImage getCurrentSprite() {
        BufferedImage[] sprites = motions.get(currentMotion);
        if (sprites != null) {
            currentSprite = (currentSprite + 1) % sprites.length;  // 애니메이션이 반복되도록 인덱스 갱신
            return sprites[currentSprite];
        }
        return null;
    }

    public void updateAnimation() {
        // 애니메이션 갱신
    }


}
