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
       
        int x, y;
        if (this == gameState.getMyCharacter()) {
            x = gameState.getMyPosition()[0] * PlayingGameScreen.gridWidth + PlayingGameScreen.gridStartX;
            y = gameState.getMyPosition()[1] * PlayingGameScreen.gridHeight + PlayingGameScreen.gridStartY;
        } else {
            x = gameState.getEnemyPosition()[0] * PlayingGameScreen.gridWidth + PlayingGameScreen.gridStartX;
            y = gameState.getEnemyPosition()[1] * PlayingGameScreen.gridHeight + PlayingGameScreen.gridStartY;
        }

        switch (currentMotion) {
            case ATTACK:
               System.out.println("start animateMotion: ATTACK");
               break;
            case MOVE:
               BufferedImage[] motionImages = cardMotions.get(currentCard.getName());
               System.out.println("start animateMotion: MOVE");
                animateMotion(g, motionImages, x, y, playingGameScreen, cardMotionTimes.get(currentCard.getName()));
                break;
            case GUARD:
               System.out.println("start animateMotion: GUARD");
               break;
            case HIT:
               System.out.println("start animateMotion: HIT");
               break;
            case DEAD:
                animateMotion(g, characterMotions.get(Motion.DEAD), x, y, playingGameScreen, cardMotionTimes.get(currentCard.getName()));
                System.out.println("start animateMotion: DEAD");
               break;
            case IDLE:
               System.out.println("start animateMotion: IDLE");
               break;
            default:
               System.out.println("start animateMotion: default");
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
   
   public Card getCurrentCard() {
      return currentCard;
   }

   public Map<Motion, int[]> getCharacterMotionTimes() {
      return characterMotionTimes;
   }

   public Map<String, int[]> getCardMotionTimes() {
      return cardMotionTimes;
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
   
   
   
   private void animateMotion(Graphics g, BufferedImage[] images, int x, int y, JPanel playingGameScreen, int[] motionTimes) {
       if (motionTimes == null || motionTimes.length != 2) {
           System.err.println("Invalid motion times for animation.");
           return;
       }

       System.out.println(x +" "+y);
       
       int interval = motionTimes[0]; // 모션 간격 (ms)
       int duration = motionTimes[1]; // 전체 애니메이션 지속 시간 (ms)
       final long startTime = System.currentTimeMillis(); // 애니메이션 시작 시간
       final int[] frameIndex = {0}; // 현재 프레임 인덱스

       // 이전 타이머가 존재하면 종료
       if (motionTimer != null) {
           motionTimer.stop();
       }

       // 새로운 Swing 타이머 생성
       motionTimer = new javax.swing.Timer(interval, e -> {
           long elapsedTime = System.currentTimeMillis() - startTime;

           System.out.println("Elapsed Time: " + elapsedTime + " / Duration: " + duration);

           // 애니메이션 종료 조건
           if (elapsedTime > duration) {
               motionTimer.stop(); // 타이머 종료
               System.out.println("Timer stopped");
               return;
           }

           // 프레임 인덱스 업데이트
           frameIndex[0] = (frameIndex[0] + 1) % images.length;

           // JPanel의 repaint 호출
           playingGameScreen.repaint(); // JPanel 전체를 다시 그림
       });

       motionTimer.start(); // 타이머 시작
   }

}

