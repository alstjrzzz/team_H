package model;

import java.awt.Dimension;
import java.util.LinkedList;

import model.Character.Character;

import model.Character.SuperMan;

public class GameState {

    private Dimension dimension;
    private LinkedList<Card> selectedCardList;
    private LinkedList<Card> enemySelectedCardList;
    private Character myCharacter;
    private Character enemyCharacter;
    private int myHealth;
    private int enemyHealth;
    private int[] myPosition;
    private int[] enemyPosition;
    private int clientNumber;
    private LinkedList<String> characterList;

    
    public GameState(Dimension dimension) {
    	
        this.dimension = dimension;
        selectedCardList = new LinkedList<>();
        
        fillCharacterList();
    }

    public void fillCharacterList() {
    	
    	characterList = new LinkedList<>();
    	
    	characterList.add("SuperMan");
    	// ...
    }
    
    public Character createCharacter(String character) {
        switch (character) {
            case "SuperMan":
                return new SuperMan();
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
    
    
}
