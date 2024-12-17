package model.Character;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import model.Card;

public class Finn extends Character {
	
	
    public Finn() {
    	
        name = "Finn";
        maxHealth = 100;
        
        try {
            sprite = ImageIO.read(new File("res/character/Finn.png"));
            if (sprite == null) {
                 System.err.println("sprite가 초기화되지 않았습니다. initCardMotions를 실행할 수 없습니다.");
                 return;
             }
            
            // skillEffect = ImageIO.read(new File(""));
            logo = ImageIO.read(new File("res/character/Finn_face.png"));
         } catch (IOException e) {
            e.printStackTrace();
         }
         
         initCardImage();
         initMotions();
    }
    
    
    
    // 슈퍼맨의 고유 카드 추가
    @Override
    public void addUniqueCard() {
        
    	cardList.add(new Card(
    			/*name:*/ "Super Punch", 
    			/*category:*/ "ATTACK", 
    			/*range:*/ new LinkedList<>() {{
    						add(new int[]{0, -1});}}, 
    			/*value:*/ 50, 
    			/*priority:*/ 2));
    	
    	cardList.add(new Card(
    			/*name:*/ "Flying Strike", 
    			/*category:*/ "ATTACK", 
    			/*range:*/ new LinkedList<>() {{
    						add(new int[]{0, -2});
    						add(new int[]{0, -3});}}, 
    			/*value:*/ 75, 
    			/*priority:*/ 2));
    }



    @Override
    public void initCardImage() {
        cardImage = new HashMap<>();


        try {
            // 카드 이미지 파일 경로를 절대 경로로 지정
            File moveImage = new File("res/card/superman_move.png");
            File super_PunchImage = new File("res/card/superman_move.png");
            File Flying_StrikeImage = new File("res/card/superman_move.png");

            cardImage.put("Move Up", ImageIO.read(moveImage));
            cardImage.put("Move Down", ImageIO.read(moveImage));
            cardImage.put("Move Left", ImageIO.read(moveImage));
            cardImage.put("Move Right", ImageIO.read(moveImage));
            cardImage.put("Super Punch", ImageIO.read(super_PunchImage));
            cardImage.put("Flying Strike", ImageIO.read(Flying_StrikeImage));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



	@Override
	public void initMotions() {
		
		motions = new HashMap<>();
		BufferedImage[] tempArr = new BufferedImage[12];
		for (int i = 0; i < 12; i++) {
			tempArr[i] = sprite.getSubimage(i * 48, 16, 48, 48);
		}
		motions.put("IDLE", tempArr.clone());
		
		motions.put("HIT", null);
		
		tempArr = new BufferedImage[20];
		for (int i = 0; i < 20; i++) {
			tempArr[i] = sprite.getSubimage(i * 64, 2016, 64, 48);
		}
		motions.put("DEAD", null);
		motions.put("GUARD", null);
		
		tempArr = new BufferedImage[14];
		for (int i = 0; i < 14; i++) {
			tempArr[i] = sprite.getSubimage(i * 48, 1440, 48, 48);
		}
		motions.put("Move Up", tempArr.clone());
		
		tempArr = new BufferedImage[10];
		for (int i = 0; i < 10; i++) {
			tempArr[i] = sprite.getSubimage(i * 48, 1568, 48, 48);
		}
		motions.put("Move Down", tempArr.clone());
		
		tempArr = new BufferedImage[12];
		for (int i = 0; i < 12; i++) {
			tempArr[i] = sprite.getSubimage(i * 48, 848, 48, 48);
		}
		motions.put("Move Right", tempArr.clone());
		
		for (int i = 0; i < 12; i++) {
			tempArr[i] = flipHorizontally(tempArr[i]);
		}
		motions.put("Move Left", tempArr.clone());
		
		tempArr = new BufferedImage[19];
		for (int i = 0; i < 19; i++) {
			tempArr[i] = sprite.getSubimage(i * 176, 3663, 176, 65);
		}
		motions.put("Skill1", tempArr.clone());
		motions.put("Skill2", null);
	}

}