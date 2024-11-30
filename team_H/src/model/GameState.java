//GameState.java

package model;

import java.awt.Dimension;
import java.util.LinkedList;

public class GameState {

	private Dimension dimension;
	private LinkedList<Card> selectedCardList;
	
	public GameState(Dimension dimension) {
		
		this.dimension = dimension;
		selectedCardList = new LinkedList<>();
		
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
}
