package model.Character;

import java.util.ArrayList;
import java.util.List;

import model.Card;

public abstract class Character {

	protected String name;
	protected List<Card> cardList;
	protected int health;
	
	public Character(String name) {
		this.name = name;
		this.cardList = new ArrayList<>();
		addCommonCard();
	}
	
	
	// 공용 카드 추가
	private void addCommonCard() {
		
		// 위로 이동 카드
		cardList.add(new Card(
			"moveUp", 
			"MOVE", 
			new ArrayList<>() {{
				add(new int[] {0, -1});
			}},
			0
		));
		
		// 아래로 이동 카드
		cardList.add(new Card(
			"moveDown", 
			"MOVE", 
			new ArrayList<>() {{
				add(new int[] {0, 1});
			}},
			0
		));
		
		// 왼쪽으로 이동 카드
		cardList.add(new Card(
			"moveLeft", 
			"MOVE", 
			new ArrayList<>() {{
				add(new int[] {-1, 0});
			}},
			0
		));
		
		// 오른쪽으로 이동 카드
		cardList.add(new Card(
			"moveRight", 
			"MOVE", 
			new ArrayList<>() {{
				add(new int[] {1, 0});
			}},
			0
		));
	}
	
	
	
	private void addUniqueCard() {
		
	}
}
