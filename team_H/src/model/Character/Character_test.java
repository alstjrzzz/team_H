package model.Character;

import java.awt.image.BufferedImage;
import java.util.Map;

// Character 클래스: 모든 캐릭터의 기본 클래스
public abstract class Character_test {
    protected int health;                  // 체력
    protected Map<Motion, BufferedImage[]> motions;  // 모션별 스프라이트
    protected Motion currentMotion = Motion.IDLE;    // 현재 모션
    protected int currentSprite = 0;               // 현재 스프라이트 인덱스
    protected boolean flipHorizontal = false;       // 반전 여부

    public enum Motion {
        IDLE,      // 가만히 있을 때
        ATTACK,    // 공격
        DEFENSE,   // 막기
        WALK,      // 걷기
        DEAD,      // 죽음
        EVENT      // 도발
    }

    public Character_test(int initialHealth) {
        this.health = initialHealth;
    }

    // 체력 감소
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    // 체력 회복
    public void heal(int amount) {
        this.health += amount;
    }

    // 현재 체력 반환
    public int getHealth() {
        return health;
    }

    // 모션 변경
    public void setMotion(Motion motion) {
        if (currentMotion != motion) {
            currentMotion = motion;
            currentSprite = 0;  // 새로운 모션을 시작할 때 스프라이트 인덱스 초기화
        }
    }

    // 현재 모션의 스프라이트 이미지 반환
    public BufferedImage getCurrentSprite() {
        BufferedImage[] sprites = motions.get(currentMotion);
        if (sprites != null) {
            currentSprite = (currentSprite + 1) % sprites.length;
            BufferedImage spriteImage = sprites[currentSprite];

            // 좌우 반전 처리
            if (flipHorizontal) {
                return flipImage(spriteImage);
            }
            return spriteImage;
        }
        return null;
    }

    // 이미지를 좌우 반전 처리
    private BufferedImage flipImage(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage flippedImage = new BufferedImage(width, height, img.getType());
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                flippedImage.setRGB(width - 1 - x, y, img.getRGB(x, y));
            }
        }
        return flippedImage;
    }
    
    

    // 자식 클래스에서 구현해야 할 스프라이트 로드 메서드
    protected abstract void loadMotions();

    // 자식 클래스에서 구현해야 할 스프라이트 이미지 로드 메서드
    protected abstract void loadImage();
}
