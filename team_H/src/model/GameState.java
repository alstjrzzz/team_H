//GameState.java

package model;

import java.awt.Dimension;
import java.util.LinkedList;

import model.Character.Character;
import model.Character.ActionMan;
/*
import model.Character.Superman;
import model.Character.Zed;
import model.Character.MasterYi;
import model.Character.Ginzo;
import model.Character.MartianManhunter;
*/
public class GameState {

	private Dimension dimension;
	private LinkedList<Card> selectedCardList;
	private LinkedList<Card> enemySelectedCardList;
	private Character myCharacter;
	private Character enemyCharacter;
	
	public GameState(Dimension dimension) {
		
		this.dimension = dimension;
		selectedCardList = new LinkedList<>();
	}

	
	public Character createCharacter(String character) {
		
		switch (character) {
			
			case "ActionMan":
				return new ActionMan();
		/*	case "Superman":
				return new Superman();
			case "Zed":
				return new Zed();
			case "MasterYi":
				return new MasterYi();
			case "Ginzo":
				return new Ginzo();
			case "MartianManhunter":
				return new MartianManhunter();
				*/
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
	
}
