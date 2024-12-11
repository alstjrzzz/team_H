package model.Character;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Map;
import model.Card;
import model.GameState;

public abstract class Character {
	
	protected String name;
	protected int maxHealth;
    protected LinkedList<Card> cardList;
    protected String sprite;
    protected String skillEffect;
    protected String logo;
    protected Map<String, BufferedImage> cardImage;
    
    protected Map<Motion, BufferedImage[]> characterMotions;
    protected Map<Motion, int[]> characterMotionTimes; // int[0]: 모션 간격, int[1]: 모션 지속 시간
    
    protected Map<String, BufferedImage[]> cardMotions;
    protected Map<String, int[]> cardMotionTimes;
    
    protected Motion currentMotion;
    protected int currentSprite;
    protected Card currentCard;
    
    
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
    
    public void drawCharacter(Graphics g, GameState gameState) {
    	
    	// 알아야하는 것: 위치한 배열의 인덱스, client number, flip 여부, 배열의 각 칸의 width height
    	
    	switch (currentMotion) {
    	case Motion.IDLE:
    		
    		break;
    	case Motion.ATTACK:
    		
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

	public String getLogo() {
		return logo;
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
