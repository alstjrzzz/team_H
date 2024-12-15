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
    
    protected Motion currentMotion;
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
    	
    	switch (currentMotion) {
    	
    	case Motion.IDLE:
    		
    		break;
    	case Motion.ATTACK:
    		
    		motionTimer.scheduleAtFixedRate(new TimerTask() {
    			Character thisCharacter = Character.this;
                int currentFrame = 0;
                long startTime = System.currentTimeMillis();

                @Override
                public void run() {
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    int interval = cardMotionTimes.get(currentCard.getName())[0];
                    int duration = cardMotionTimes.get(currentCard.getName())[1];

                    // 모션이 끝났으면 타이머를 취소
                    if (elapsedTime > duration) {
                        motionTimer.cancel();
                        return;
                    }
                    
                    int x, y;
                    if (thisCharacter == gameState.getMyCharacter()) {
                    	
                    	if (gameState.getClientNumber() == 1) {
                    		x = gameState.getMyPosition()[0] * FieldPanel.gridClient1X + FieldPanel.gridStartX;
                    		y = gameState.getMyPosition()[1] * FieldPanel.gridClient1Y + FieldPanel.gridStartY;
                    	} else {
                    		x = gameState.getMyPosition()[0] * FieldPanel.gridClient2X + FieldPanel.gridStartX;
                    		y= gameState.getMyPosition()[1] * FieldPanel.gridClient2Y + FieldPanel.gridStartY;
                    	}
                    } else {
                    	if (gameState.getClientNumber() == 1) {
                    		x = gameState.getEnemyPosition()[0] * FieldPanel.gridClient1X + FieldPanel.gridStartX;
                    		y = gameState.getEnemyPosition()[1] * FieldPanel.gridClient1Y + FieldPanel.gridStartY;
                    	} else {
                    		x = gameState.getEnemyPosition()[0] * FieldPanel.gridClient2X + FieldPanel.gridStartX;
                    		y= gameState.getEnemyPosition()[1] * FieldPanel.gridClient2Y + FieldPanel.gridStartY;
                    	}
                    }
                    
                    // 모션 프레임 갱신
                    if (elapsedTime % interval == 0) {
                        currentFrame = (currentFrame + 1) % cardMotions.get(currentCard.getName()).length;
                        playingGameScreen.repaint(x, y
                        		, cardMotions.get(currentCard.getName())[currentFrame].getWidth()
                        		, cardMotions.get(currentCard.getName())[currentFrame].getHeight());
                    }
                }
            }, 0, cardMotionTimes.get(currentCard.getName())[1]);
    		
    		
    		// 이펙트는 override 해서 추가
    		
    		break;
    	case Motion.MOVE:
    		
    		break;
    	case Motion.GUARD:
    		
    		break;
    	case Motion.HIT:
    		
    		break;
    	case Motion.DEAD:
    		
    		break;
    	default:
    		
    	
    	}
    	
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
}
