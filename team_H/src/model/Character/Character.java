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
        addCommonCard();  // 공용 카드 추가
        addUniqueCard();  // 고유 카드 추가
    }
    // 공용 카드 가져오기
    public List<Card> getCommonCards() {
        return cardList;
    }
    
    // 공용 카드 추가 (추상 메서드)
    public abstract void addCommonCard();

    // 고유 카드 추가 (추상 메서드)
    public abstract void addUniqueCard();
    
    // 카드 동작을 처리하기 위한 추상 메서드
    public abstract void useCard(Card card);
    
    public abstract void updateAnimation();
    
    public abstract String getCardImagePath(String cardName);
    
    public List<Card> getCardList() {
        return cardList;
    }

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
