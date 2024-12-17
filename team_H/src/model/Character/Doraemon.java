package model.Character;

import java.util.HashMap;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import model.Card;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Doraemon extends Character {
   
   public Doraemon() {
      
      name = "Doraemon";
      maxHealth = 100;
      try {
         sprite = ImageIO.read(new File("res/character/Doraemon.png"));
         if (sprite == null) {
              System.err.println("sprite가 초기화되지 않았습니다. initCardMotions를 실행할 수 없습니다.");
              return;
          }
         
         // skillEffect = ImageIO.read(new File(""));
         logo = ImageIO.read(new File("res/character/doraemon_logo.png"));
      } catch (IOException e) {
         e.printStackTrace();
      }
      
      initCardImage();
      
   }

   
   
   @Override
   public void addUniqueCard() {
      
      cardList.add(new Card(
             /*name:*/ "Air Cannon!", 
             /*category:*/ "ATTACK", 
             /*range:*/ new LinkedList<>() {{
                      add(new int[]{0, 0});
                      add(new int[]{1, 0});
                      add(new int[]{2, 0});
                      add(new int[]{3, 0});
                      add(new int[]{4, 0});}}, 
             /*value:*/ 30, 
             /*priority:*/ 2));
      
      cardList.add(new Card(
             /*name:*/ "Bamboo Helicopter!", 
             /*category:*/ "ATTACK", 
             /*range:*/ new LinkedList<>() {{
                      add(new int[]{0, 0});
                      add(new int[]{1, 0});
                      add(new int[]{2, 0});
                      add(new int[]{3, 0});}}, 
             /*value:*/ 25, 
             /*priority:*/ 2));
   }



   @Override
   public void initCardImage() {

      cardImage = new HashMap<>();

        try {
            // 카드 이미지 파일 경로를 절대 경로로 지정
            File moveImage = new File("res/card/doraemon_move.jpg");
            File Air_CannonImage = new File("res/card/air_cannon.png");
            File Bamboo_HelicopterImage = new File("res/card/bamboo_helicopter.png");

            cardImage.put("Move Up", ImageIO.read(moveImage));
            cardImage.put("Move Down", ImageIO.read(moveImage));
            cardImage.put("Move Left", ImageIO.read(moveImage));
            cardImage.put("Move Right", ImageIO.read(moveImage));
            cardImage.put("Air Cannon!", ImageIO.read(Air_CannonImage));
            cardImage.put("Bamboo Helicopter!", ImageIO.read(Bamboo_HelicopterImage));

        } catch (IOException e) {
            e.printStackTrace();
        }
   }



	@Override
	public void initMotions() {
		// TODO Auto-generated method stub
		
	}

   
   
   
}
