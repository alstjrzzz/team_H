package model;

import java.awt.Dimension;
import java.util.LinkedList;


import model.Character.Character;
import model.Character.Cygnus;
import model.Character.Doraemon;
import model.Character.Finn;

import model.Character.Zoro;

public class GameState {

    private Dimension dimension;
    private LinkedList<Card> selectedCardList;
    private LinkedList<Card> enemySelectedCardList;
    private Character myCharacter;
    private Character enemyCharacter;
    private int myHealth;
    private int enemyHealth;
    private int[] myPosition;      // 내 캐릭터 위치
    private int[] enemyPosition;   // 상대 캐릭터 위치
    private boolean myCharacterIsFlip;
    private boolean enemyCharacterIsFlip;
    private int clientNumber;
    private LinkedList<String> characterList;

    
    public GameState(Dimension dimension) {
       
        this.dimension = dimension;
        selectedCardList = new LinkedList<>();
        
        fillCharacterList();
    }

    public void fillCharacterList() {
       
       characterList = new LinkedList<>();
      
       characterList.add("Doraemon");
       characterList.add("Zoro");
       characterList.add("Cygnus");
       characterList.add("Ace");
       // ...
    }
    
    public Character createCharacter(String character) {
        switch (character) {
            case "Doraemon":
                Doraemon doraemon = new Doraemon();
                doraemon.initCardImage(); // 카드 이미지 초기화
                return doraemon;
            case "Zoro":
                Zoro zoro = new Zoro();
                zoro.initCardImage(); // 카드 이미지 초기화
                return zoro;
            case "Cygnus":
            	Cygnus cygnus = new Cygnus();
            	cygnus.initCardImage(); // 카드 이미지 초기화
                return cygnus;
            case "Finn":
            	Finn finn = new Finn();
            	finn.initCardImage(); // 카드 이미지 초기화
                return finn;
            default:
                return null;
        }
    }


    public Dimension getDimension() {
        return dimension;
    }

    public void clearSelectedCardList() {
        selectedCardList = new LinkedList<>();
    }

    public void setSelectedCardList(LinkedList<Card> selectedCardList) {
        this.selectedCardList = selectedCardList;
    }

    public LinkedList<Card> getSelectedCardList() {
        return selectedCardList;
    }

    public LinkedList<Card> getEnemySelectedCardList() {
        return enemySelectedCardList;
    }

    public void setEnemySelectedCardList(LinkedList<Card> enemySelectedCardList) {
        this.enemySelectedCardList = enemySelectedCardList;
    }

    public Character getMyCharacter() {
        return myCharacter;
    }

    public void setMyCharacter(Character myCharacter) {
        this.myCharacter = myCharacter;
    }

    public Character getEnemyCharacter() {
        return enemyCharacter;
    }

    public void setEnemyCharacter(Character enemyCharacter) {
        this.enemyCharacter = enemyCharacter;
    }

   public int getClientNumber() {
      return clientNumber;
   }

   public void setClientNumber(int clientNumber) {
      this.clientNumber = clientNumber;
   }


   public int[] getMyPosition() {
      return myPosition;
   }


   public void setMyPosition(int[] myPosition) {
      this.myPosition = myPosition;
   }


   public int[] getEnemyPosition() {
      return enemyPosition;
   }


   public void setEnemyPosition(int[] enemyPosition) {
      this.enemyPosition = enemyPosition;
   }

   public LinkedList<String> getCharacterList() {
      return characterList;
   }

   public int getMyHealth() {
      return myHealth;
   }

   public void setMyHealth(int myHealth) {
      this.myHealth = myHealth;
   }

   public int getEnemyHealth() {
      return enemyHealth;
   }

   public void setEnemyHealth(int enemyHealth) {
      this.enemyHealth = enemyHealth;
   }

   public boolean isMyCharacterIsFlip() {
      return myCharacterIsFlip;
   }

   public void setMyCharacterIsFlip(boolean myCharacterIsFlip) {
      this.myCharacterIsFlip = myCharacterIsFlip;
   }

   public boolean isEnemyCharacterIsFlip() {
      return enemyCharacterIsFlip;
   }

   public void setEnemyCharacterIsFlip(boolean enemyCharacterIsFlip) {
      this.enemyCharacterIsFlip = enemyCharacterIsFlip;
   }

   
    
    
}
