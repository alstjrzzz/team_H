package model.Character;

import javax.imageio.ImageIO;
import javax.swing.*;

import javazoom.jl.player.Player;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import model.Card;

public class Finn extends Character {
   
	private int character_size = 100;
	private int character_skill_size = 300;
	private Map<String, String> cardSounds;
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
            logo = ImageIO.read(new File("res/character/Finn_logo.png"));
         } catch (IOException e) {
            e.printStackTrace();
         }
         
         initCardImage();
         initMotions();
         initCardSounds();
    }
    
    
    
    // 핀의 고유카드 추가
    @Override
    public void addUniqueCard() {
        
       cardList.add(new Card(
             /*name:*/ "Sword Slash", 
             /*category:*/ "ATTACK", 
             /*range:*/ new LinkedList<>() {{
		                 add(new int[]{0, 0});
		                 add(new int[]{1, 0});}},  
             /*value:*/ 40, 
             /*priority:*/ 2));
       
       cardList.add(new Card(
             /*name:*/ "Stretch Punch", 
             /*category:*/ "ATTACK", 
             /*range:*/ new LinkedList<>() {{
		                 add(new int[]{0, 0});
		                 add(new int[]{1, 0});}}, 
             /*value:*/ 35, 
             /*priority:*/ 2));
    }



    @Override
    public void initCardImage() {
        cardImage = new HashMap<>();


        try {
            // 카드 이미지 파일 경로를 절대 경로로 지정
            File moveImage = new File("res/card/finn_move.png");
            File sword_SlashImage = new File("res/card/finn_swordSlash.jpg");
            File stretch_PunchImage = new File("res/card/finn_stretchPunch.png");

            cardImage.put("Move Up", ImageIO.read(moveImage));
            cardImage.put("Move Down", ImageIO.read(moveImage));
            cardImage.put("Move Left", ImageIO.read(moveImage));
            cardImage.put("Move Right", ImageIO.read(moveImage));
            cardImage.put("Sword Slash", ImageIO.read(sword_SlashImage));
            cardImage.put("Stretch Punch", ImageIO.read(stretch_PunchImage));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void initCardSounds() {
        cardSounds = new HashMap<>();
        cardSounds.put("Sword Slash", "res/sound/sfx/finn_swordSlash.mp3");
        cardSounds.put("Stretch Punch", "res/sound/sfx/finn_stretchPunch.mp3");
    }

    @Override
    public void playCardSound(String cardName) {
        String soundPath = cardSounds.get(cardName);
        if (soundPath != null) {
            new Thread(() -> {
                try (FileInputStream fis = new FileInputStream(soundPath)) {
                    Player player = new Player(fis);
                    player.play();
                } catch (FileNotFoundException e) {
                    System.err.println("Sound file not found: " + soundPath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            System.err.println("Sound for card " + cardName + " not found.");
        }
    }


   @Override
   public void initMotions() {
      
      motions = new HashMap<>();
      BufferedImage[] tempArr = new BufferedImage[12];
      for (int i = 0; i < 12; i++) {
         tempArr[i] = resizeImage(sprite.getSubimage(i * 48, 16, 48, 48), character_size, character_size, true);
      }
      motions.put("IDLE", tempArr.clone());
      
      tempArr = new BufferedImage[12];
      for (int i = 0; i < 12; i++) {
         tempArr[i] = resizeImage(sprite.getSubimage(i * 64, 1036, 64, 48), character_size, character_size, true);
      }
      motions.put("HIT", tempArr.clone());
      
      tempArr = new BufferedImage[20];
      for (int i = 0; i < 20; i++) {
         tempArr[i] = resizeImage(sprite.getSubimage(i * 64, 2016, 64, 48), character_size, character_size, true);
      }
      motions.put("DEAD", tempArr.clone());
      
      motions.put("GUARD", null);
      
      tempArr = new BufferedImage[10];
      for (int i = 4; i < 14; i++) {
         tempArr[i-4] = resizeImage(sprite.getSubimage(i * 48, 1440, 48, 48), character_size, character_size, true);
      }
      motions.put("Move Up", tempArr.clone());
      
      tempArr = new BufferedImage[10];
      for (int i = 0; i < 10; i++) {
         tempArr[i] = resizeImage(sprite.getSubimage(i * 48, 1568, 48, 48), character_size, character_size, true);
      }
      motions.put("Move Down", tempArr.clone());
      
      tempArr = new BufferedImage[12];
      for (int i = 0; i < 12; i++) {
         tempArr[i] = resizeImage(sprite.getSubimage(i * 48, 848, 48, 48), character_size, character_size, true);
      }
      motions.put("Move Right", tempArr.clone());
      
      for (int i = 0; i < 12; i++) {
         tempArr[i] = flipHorizontally(tempArr[i]);
      }
      motions.put("Move Left", tempArr.clone());
      
      tempArr = new BufferedImage[19];
      for (int i = 0; i < 19; i++) {
         tempArr[i] = resizeImage(sprite.getSubimage(i * 176, 3663, 176, 65), 350, 350, true);
      }
      motions.put("Sword Slash", tempArr.clone());
      
      tempArr = new BufferedImage[19];
      for (int i = 0; i < 19; i++) {
         tempArr[i] = resizeImage(sprite.getSubimage(i * 112, 3408, 112, 48), character_skill_size, character_skill_size, true);
      }
      motions.put("Stretch Punch", tempArr.clone());
   }
   
  
   
    private  BufferedImage resizeImage(BufferedImage image, int newWidth, int newHeight, boolean keepRatio) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        if (keepRatio) {
            // 비율 유지
            double ratio;
            if (newWidth > 0 && newHeight == 0) {
                // 너비 기준
                ratio = (double) newWidth / imageWidth;
                newHeight = (int) (imageHeight * ratio);
            } else if (newHeight > 0 && newWidth == 0) {
                // 높이 기준
                ratio = (double) newHeight / imageHeight;
                newWidth = (int) (imageWidth * ratio);
            } else if (newWidth > 0 && newHeight > 0) {
                // 비율을 유지하면서 목표 너비, 높이 중 작은 쪽에 맞춤
                double ratioWidth = (double) newWidth / imageWidth;
                double ratioHeight = (double) newHeight / imageHeight;
                ratio = Math.min(ratioWidth, ratioHeight);
                newWidth = (int) (imageWidth * ratio);
                newHeight = (int) (imageHeight * ratio);
            } else {
                throw new IllegalArgumentException("Width or height must be greater than 0.");
            }
        } else {
            // 비율 무시
            if (newWidth <= 0 || newHeight <= 0) {
                throw new IllegalArgumentException("Both width and height must be greater than 0 when keepRatio is false.");
            }
        }

        // 이미지 리사이징
        Image resizedImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = newImage.createGraphics();
        g2d.drawImage(resizedImage, 0, 0, null);
        g2d.dispose();

        return newImage;
    }



	@Override
	public void initSkillEffect() {
		// TODO Auto-generated method stub
		
	}
}