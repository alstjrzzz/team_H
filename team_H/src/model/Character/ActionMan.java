package model.Character;

import java.util.ArrayList;

import model.Card;

public class ActionMan extends Character {

	public ActionMan() {
		super(null, null);
		super.health = 100;
		addUniqueCard();
	}


	// 고유 카드 추가
	public void addUniqueCard() {
		
		// 액션빔
		cardList.add(new Card(
			"actionBeam", 
			"ATTACK", 
			new ArrayList<>() {{
				add(new int[] {1, 0});
				add(new int[] {2, 0});
				add(new int[] {3, 0});
				add(new int[] {4, 0});
				add(new int[] {5, 0});
			}},
			50
		));
		
		// 액션펀치
		cardList.add(new Card(
			"actionPunch", 
			"ATTACK", 
			new ArrayList<>() {{
				add(new int[] {0, 0});
				add(new int[] {1, 0});
				add(new int[] {1, -1});
				add(new int[] {1, 1});
			}},
			50
		));
}
}
