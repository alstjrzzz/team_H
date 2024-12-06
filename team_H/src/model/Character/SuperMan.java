package model.Character;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Card;

public class SuperMan extends Character {
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
        super("res/character/superman_face.png", "res/character/superman_sprite.png");
        this.health = 100; // 슈퍼맨의 기본 체력 설정
        addCommonCard();   // 공용 카드 추가
        addUniqueCard();   // 고유 카드 추가
        loadImage();       // 이미지 로드
        loadMotions();     // 모션 로드
    }
    
    // 슈퍼맨의 공용 카드 추가
    @Override
    public void addCommonCard() {
        cardList = new ArrayList<>();
        cardList.add(new Card("Move Up", "MOVE", new ArrayList<>() {{
            add(new int[]{0, -1});
        }}, 0));
        cardList.add(new Card("Move Down", "MOVE", new ArrayList<>() {{
            add(new int[]{0, 1});
        }}, 0));
        cardList.add(new Card("Move Left", "MOVE", new ArrayList<>() {{
            add(new int[]{-1, 0});
        }}, 0));
        cardList.add(new Card("Move Right", "MOVE", new ArrayList<>() {{
            add(new int[]{1, 0});
        }}, 0));
    }
    @Override
    public String getCardImagePath(String cardName) {
        switch (cardName) {
            case "Move Up":
                return "res/cards/superman_move.png";
            case "Move Down":
                return "res/cards/superman_move.png";
            case "Move Left":
                return "res/cards/superman_move.png";
            case "Move Right":
                return "res/cards/superman_move.png";
            default:
                return "res/cards/default_card.png";
        }
    }
    
    // 슈퍼맨의 고유 카드 추가
    @Override
    public void addUniqueCard() {
        
        cardList.add(new Card("Super Punch", "ATTACK", new ArrayList<>() {{
            add(new int[]{0, -1}); // 범위 설정 예제
        }}, 50));

        cardList.add(new Card("Flying Strike", "ATTACK", new ArrayList<>() {{
            add(new int[]{0, -2});
            add(new int[]{0, -3});
        }}, 75));
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
                int r = (rgb & 0xFF0000) >> 16;
                int g = (rgb & 0xFF00) >> 8;
                int b = (rgb & 0xFF);
                if (r == r1 && g == g1 && b == b1) {
                    return rgb & 0xFFFFFF;
                }
                return rgb;
            }
        };

        ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
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
            sprites[i] = sprite.getSubimage(startX + i * width, startY, width, height);
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
