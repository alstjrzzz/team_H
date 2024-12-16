package model.Character;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import model.Card;
import model.GameState;
import view.FieldPanel;
import view.PlayingGameScreen;

public abstract class Character {
	
	protected String name;
	protected int maxHealth;
    protected LinkedList<Card> cardList;
    protected BufferedImage sprite;
    protected BufferedImage skillEffect;
    protected BufferedImage logo;
    protected Map<String, BufferedImage> cardImage;
    
    protected Map<Motion, BufferedImage[]> characterMotions;
    protected Map<Motion, int[]> characterMotionTimes; // int[0]: 모션 간격, int[1]: 모션 지속 시간
    
    protected Map<String, BufferedImage[]> cardMotions;
    protected Map<String, int[]> cardMotionTimes;
    protected Map<String, BufferedImage[]> cardEffects;
    protected Map<String, int[]> cardEffectTimes;
    
    protected Motion currentMotion = Motion.IDLE;
    protected int currentSprite;
    protected Card currentCard;
    
    protected Timer motionTimer;
    protected Timer effectTimer;
    
    
    public enum Motion {
    	IDLE,		// 가만히 있을 때
    	ATTACK,		// 공격
    	MOVE,		// 걷기
    	GUARD,		// 막기
    	HIT,		// 피격
    	DEAD		// 죽음
    }
    
    public Character() {
    	
        this.cardList = new LinkedList<>();
        
        addCommonCard();
        addUniqueCard();
		initCardMotionTimes();
		initCharacterMotionTimes();
    }
    
    
    
    public void addCommonCard() {
    	
    	cardList.add(new Card(
    			/*name:*/ "Move Up", 
    			/*category:*/ "MOVE", 
    			/*range:*/ new LinkedList<>() {{
    						add(new int[]{0, -1});}}, 
    			/*value:*/ 0, 
    			/*priority:*/ 1));
    	
    	cardList.add(new Card(
    			/*name:*/ "Move Down", 
    			/*category:*/ "MOVE",
    			/*range:*/ new LinkedList<>() {{
    						add(new int[]{0, 1});}}, 
    			/*value:*/ 0, 
    			/*priority:*/ 1));
    	
    	cardList.add(new Card(
    			/*name:*/ "Move Left", 
    			/*category:*/ "MOVE", 
    			/*range:*/ new LinkedList<>() {{
    						add(new int[]{-1, 0});}}, 
    			/*value:*/ 0, 
    			/*priority:*/ 1));
    	
    	cardList.add(new Card(
    			/*name:*/ "Move Right", 
    			/*category:*/ "MOVE", 
    			/*range:*/ new LinkedList<>() {{
    						add(new int[]{1, 0});}}, 
    			/*value:*/ 0, 
    			/*priority:*/ 1));
    }



    public abstract void addUniqueCard();
    
    public abstract void initCardImage();
    
    public abstract void initCharacterMotions();
    
    public abstract void initCharacterMotionTimes();
    
    public abstract void initCardMotions();
    
    public abstract void initCardMotionTimes();
    
    public abstract void initCardEffects();
    
    public abstract void initCardEffectTimes();
    
    public void drawCharacter(Graphics g, GameState gameState, JPanel playingGameScreen) {
    	
        if (currentMotion == null || currentCard == null || cardMotions == null) {
            System.err.println("Animation draw failed: currentMotion or currentCard is null.");
            System.err.println("currentMotion: " + currentMotion);
            System.err.println("currentCard: " + currentCard);
            return;
        }

        BufferedImage[] motionImages = cardMotions.get(currentCard.getName());
        if (motionImages == null) {
            System.err.println("No motion images found for card: " + currentCard.getName());
            return;
        }

        int x, y;
        if (this == gameState.getMyCharacter()) {
            x = gameState.getMyPosition()[0] * FieldPanel.gridWidth + FieldPanel.gridStartX;
            y = gameState.getMyPosition()[1] * FieldPanel.gridHeight + FieldPanel.gridStartY;
        } else {
            x = gameState.getEnemyPosition()[0] * FieldPanel.gridWidth + FieldPanel.gridStartX;
            y = gameState.getEnemyPosition()[1] * FieldPanel.gridHeight + FieldPanel.gridStartY;
        }

        switch (currentMotion) {
            case ATTACK:
            case MOVE:
                animateMotion(g, motionImages, x, y, playingGameScreen, cardMotionTimes.get(currentCard.getName()));
                break;
            case GUARD:
            case HIT:
            case DEAD:
                animateMotion(g, motionImages, x, y, playingGameScreen, cardMotionTimes.get(currentCard.getName()));
                break;

            case IDLE:
            default:
                // 기본 상태: 첫 번째 이미지 그리기
                g.drawImage(motionImages[0], x, y, null);
                break;
        }
    }

    public BufferedImage getLogo() {
        return this.logo;
    }

    public Map<String, BufferedImage> getCardImage() {
        return this.cardImage;
    }

    
    public String getName() {
		return name;
	}
    
    public int getMaxHealth() {
		return maxHealth;
	}

	public LinkedList<Card> getCardList() {
        return cardList;
    }

	public void setCurrentCard(Card currentCard) {
		this.currentCard = currentCard;
	}
	
	public Map<Motion, int[]> getCharacterMotionTimes() {
		return characterMotionTimes;
	}

	public Map<String, int[]> getCardMotionTimes() {
		return cardMotionTimes;
	}

	public void setMotion(String motion) {
		
		switch (motion) {
		case "IDLE":
			this.currentMotion = Motion.IDLE;
			break;
		case "ATTACK":
			this.currentMotion = Motion.ATTACK;
			break;
		case "MOVE":
			this.currentMotion = Motion.MOVE;
			break;
		case "GUARD":
			this.currentMotion = Motion.GUARD;
			break;
		case "HIT":
			this.currentMotion = Motion.HIT;
			break;
		case "DEAD":
			this.currentMotion = Motion.DEAD;
			break;
		}
	}
	private void animateMotion(Graphics g, BufferedImage[] images, int x, int y, JPanel playingGameScreen, int[] motionTimes) {
	    if (motionTimes == null || motionTimes.length != 2) {
	        System.err.println("Invalid motion times for animation.");
	        return;
	    }

	    int interval = motionTimes[0]; // 모션 간격
	    int duration = motionTimes[1]; // 모션 전체 지속 시간
	    final long startTime = System.currentTimeMillis(); // 애니메이션 시작 시간
	    final int[] frameIndex = {0}; // 현재 프레임 인덱스

	    if (motionTimer != null) {
	        motionTimer.cancel(); // 기존 타이머 중지
	    }
	    motionTimer = new Timer();

	    motionTimer.scheduleAtFixedRate(new TimerTask() {
	        @Override
	        public void run() {
	            long elapsedTime = System.currentTimeMillis() - startTime;

	            System.out.println("Elapsed Time: " + elapsedTime + " / Duration: " + duration);
	            if (elapsedTime > duration) {
	                motionTimer.cancel(); // 지속 시간이 끝나면 타이머 종료
	                System.out.println("Timer cancelled");
	                return;
	            }

	            // 프레임 업데이트
	            frameIndex[0] = (frameIndex[0] + 1) % images.length;

	            // 이미지 그리기
	            playingGameScreen.repaint(x, y, images[frameIndex[0]].getWidth(), images[frameIndex[0]].getHeight());
	        }
	    }, interval, duration); // interval에 따라 프레임 갱신
	}

}
