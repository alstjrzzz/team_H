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
         logo = ImageIO.read(new File("res/character/doraemon_logo.jpg"));
      } catch (IOException e) {
         e.printStackTrace();
      }
      
      initCardImage();
      initCharacterMotions();
      initCharacterMotionTimes();
      initCardMotions();
      initCardMotionTimes();
      
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
   public void initCharacterMotions() {

      characterMotions = new HashMap<Character.Motion, BufferedImage[]>();
      
      characterMotions.put(Motion.IDLE, null);
      characterMotions.put(Motion.HIT, null);
      characterMotions.put(Motion.DEAD, null);
   }



   @Override
   public void initCharacterMotionTimes() {

      characterMotionTimes = new HashMap<Character.Motion, int[]>();
      
      characterMotionTimes.put(Motion.IDLE, new int[] {});
      characterMotionTimes.put(Motion.HIT, new int[] {});
      characterMotionTimes.put(Motion.DEAD, new int[] {});
   }



   @Override
   public void initCardMotions() {
      
      cardMotions = new HashMap<String, BufferedImage[]>();
      
      cardMotions.put("Move Up", new BufferedImage[] {
            sprite.getSubimage(71, 6, 88 - 71, 32 - 6),
            sprite.getSubimage(95, 6, 112 - 95, 32 - 6),
            sprite.getSubimage(119, 6, 136 - 119, 32 - 6)});
      
      cardMotions.put("Move Down", new BufferedImage[] {
            sprite.getSubimage(0, 6, 15, 32 - 6),
            sprite.getSubimage(24, 6, 39 - 24, 32 - 6),
            sprite.getSubimage(48, 6, 63 - 48, 32 - 6)});
      
      BufferedImage[] moveLeftMotions = new BufferedImage[] {
            sprite.getSubimage(144, 6, 159 - 144, 32 - 6),
            sprite.getSubimage(168, 6, 183 - 168, 32 - 6),
            sprite.getSubimage(192, 6, 207 - 192, 32 - 6)};
      cardMotions.put("Move Left", moveLeftMotions);
      
      BufferedImage[] moveRightMotions = new BufferedImage[] {
            flipHorizontally(moveLeftMotions[0]),
            flipHorizontally(moveLeftMotions[1]),
            flipHorizontally(moveLeftMotions[2])};
      cardMotions.put("Move Right", moveRightMotions);
      
      cardMotions.put("Air Cannon!", new BufferedImage[] {
            flipHorizontally(sprite.getSubimage(120, 190, 135 - 120, 215 - 190))});
      
      cardMotions.put("Bamboo Helicopter!", new BufferedImage[] {
            flipHorizontally(sprite.getSubimage(275, 347, 299 - 275, 374 - 347)),
            sprite.getSubimage(275, 347, 299 - 275, 374 - 347)});
      
      
   }



   @Override
   public void initCardMotionTimes() {

      cardMotionTimes = new HashMap<String, int[]>();
      
      cardMotionTimes.put("Move Up", new int[] {1500, 1500});
      cardMotionTimes.put("Move Down", new int[] {1500, 1500});
      cardMotionTimes.put("Move Left", new int[] {1500, 1500});
      cardMotionTimes.put("Move Right", new int[] {1500, 1500});
      
      cardMotionTimes.put("Air Cannon!", new int[] {1000, 1000});
      cardMotionTimes.put("Bamboo Helicopter!", new int[] {800, 1600});
         
   }
   
   
   
   @Override
   public void initCardEffects() {
      
      cardEffects = new HashMap<String, BufferedImage[]>();
      
      cardEffects.put("Air Cannon!", new BufferedImage[] {
            sprite.getSubimage(195, 191, 226 - 195, 222 - 191),
            sprite.getSubimage(242, 191, 273 - 242, 222 - 191),
            sprite.getSubimage(273, 191, 304 - 273, 222 - 191),
            sprite.getSubimage(305, 191, 336 - 305, 222 - 191),
            sprite.getSubimage(336, 191, 367 - 336, 222 - 191)});
   }
   
   
   @Override
   public void initCardEffectTimes() {
      
      cardEffectTimes = new HashMap<String, int[]>();
      
      cardEffectTimes.put("Air Cannon!", new int[] {200, 1000});
      
   }
   
   // BufferedImage를 수평으로 뒤집는 메서드
    public static BufferedImage flipHorizontally(BufferedImage image) {
        // 뒤집힌 이미지를 담을 새로운 BufferedImage 생성
        BufferedImage flippedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        
        // Graphics2D 객체 생성
        Graphics2D g2d = flippedImage.createGraphics();
        
        // AffineTransform을 사용해 수평으로 이미지를 뒤집기
        AffineTransform transform = AffineTransform.getScaleInstance(-1, 1); // 수평 뒤집기
        transform.translate(-image.getWidth(), 0); // 원래 이미지의 위치로 이동시킴
        
        // 뒤집힌 이미지를 그리기
        g2d.drawImage(image, transform, null);
        g2d.dispose();
        
        return flippedImage; // 뒤집힌 이미지 반환
    }
}
