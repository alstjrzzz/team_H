package model;

import java.awt.Dimension;
import java.util.LinkedList;

import model.Character.Character;

import model.Character.SuperMan;
import model.Character.Zed;
import model.Character.MasterYi;
import model.Character.Ginzo;
import model.Character.MartianManhunter;

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
            case "Superman":
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
}
