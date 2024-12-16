package model.Character;

import java.awt.Graphics;
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
    protected BufferedImage skillEffect;
    protected BufferedImage logo;
    protected Map<String, BufferedImage> cardImage;
    
    protected Motion currentMotion = Motion.IDLE;
    protected int currentSprite;
    protected Card currentCard;
    
    
    public enum Motion {
       IDLE,      // 가만히 있을 때
       ATTACK,      // 공격
       MOVE,      // 걷기
       GUARD,      // 막기
       HIT,      // 피격
       DEAD      // 죽음
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
   
   public Card getCurrentCard() {
      return currentCard;
   }


   public void setCurrentMotion(String motion) {
      
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


	
	public Motion getCurrentMotion() {
		return currentMotion;
	}
	   
   

}

