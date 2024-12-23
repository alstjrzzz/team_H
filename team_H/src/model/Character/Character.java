package model.Character;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.Timer;
import javax.swing.JPanel;

import model.Card;
import model.GameState;
import view.PlayingGameScreen;

public abstract class Character {
   
   protected String name;
   public static int maxHealth;
    protected LinkedList<Card> cardList;
    protected BufferedImage sprite;
    protected Map<String, BufferedImage[]> skillEffect;
    protected BufferedImage logo;
    protected Map<String, BufferedImage> cardImage;
    protected Map<String, BufferedImage[]> motions;
    
    protected String currentMotion = "IDLE";
    protected Card currentCard;
    protected int currentX;
    protected int currentY;
    
   

    
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
    
    public abstract void initMotions();
    
    public abstract void initSkillEffect();
    
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

   public void setCurrentMotion(String currentMotion) {
		this.currentMotion = currentMotion;
	}

   public abstract void playCardSound(String cardName);

   public LinkedList<Card> getCardList() {
        return cardList;
    }

   public void setCurrentCard(Card currentCard) {
      this.currentCard = currentCard;
   }
   
   public Card getCurrentCard() {
      return currentCard;
   }


   public BufferedImage getSprite() {
	return sprite;
}



public void setCurrentX(int currentX) {
	this.currentX = currentX;
}



public void setCurrentY(int currentY) {
	this.currentY = currentY;
}



public Map<String, BufferedImage[]> getSkillEffect() {
	return skillEffect;
}



public Map<String, BufferedImage[]> getMotions() {
	return motions;
}

public int getCurrentX() {
	return currentX;
}



public int getCurrentY() {
	return currentY;
}


	public String getCurrentMotion() {
		return currentMotion;
	}
	   
	// BufferedImage를 수평으로 뒤집는 메서드
    public static BufferedImage flipHorizontally(BufferedImage image) {
        // 뒤집힌 이미지를 담을 새로운 BufferedImage 생성
        BufferedImage flippedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        
        // Graphics2D 객체 생성
        Graphics2D g2d = flippedImage.createGraphics();
        
        // AffineTransform을 사용해 수평으로 이미지를 뒤집기
        AffineTransform transform = AffineTransform.getScaleInstance(-1, 1); // 수평 뒤집기
        transform.translate(-image.getWidth(), 0); // 원래 이미지의 위치로 이동시킴
        
        // 뒤집힌 이미지를 그리기
        g2d.drawImage(image, transform, null);
        g2d.dispose();
        
        return flippedImage; // 뒤집힌 이미지 반환
    }

}

