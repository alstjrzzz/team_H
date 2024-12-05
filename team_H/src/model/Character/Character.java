package model.Character;

import java.util.ArrayList;
import java.util.List;

import model.Card;

public abstract class Character {

	protected String iconPath;
	protected String spritePath;
	protected List<Card> cardList;
	protected int health;
	
	public Character(String iconPath, String spritePath) {
		this.iconPath = iconPath;
		this.spritePath = spritePath;
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
	
	
	
	public abstract void addUniqueCard();


	public String getIconPath() {
		return iconPath;
	}


	public String getSpritePath() {
		return spritePath;
	}


	public int getHealth() {
		return health;
	}
	
	
}
