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

public class Cygnus extends Character {
   
   
    public Cygnus() {
       
        name = "Cygnus";
        maxHealth = 100;
        
        try {
            sprite = ImageIO.read(new File("res/character/Cygnus.png"));
            sprite = TransformColorToTransparency(sprite, new Color(43, 28, 125));
            if (sprite == null) {
                 System.err.println("sprite가 초기화되지 않았습니다. initCardMotions를 실행할 수 없습니다.");
                 return;
             }
            
            // skillEffect = ImageIO.read(new File(""));
            logo = ImageIO.read(new File("res/character/Cygnus_face.png"));
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
             /*name:*/ "Skill1", 
             /*category:*/ "ATTACK", 
             /*range:*/ new LinkedList<>() {{
                      add(new int[]{0, -1});}}, 
             /*value:*/ 50, 
             /*priority:*/ 2));
       
       cardList.add(new Card(
             /*name:*/ "Skill2", 
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
            cardImage.put("Skill1", ImageIO.read(super_PunchImage));
            cardImage.put("Skill2", ImageIO.read(Flying_StrikeImage));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



   @Override
   public void initMotions() {
      
      motions = new HashMap<>();
      BufferedImage[] tempArr = new BufferedImage[8];
      for (int i = 0; i < 8; i++) {
         tempArr[i] = sprite.getSubimage(i * 146, 11, 146, 204);
      }
      motions.put("IDLE", tempArr.clone());
      
      motions.put("HIT", null);
      
      tempArr = new BufferedImage[29];
      for (int i = 0; i < 29; i++) {
         tempArr[i] = sprite.getSubimage(i * 262, 9024, 262, 317);
      }
      motions.put("DEAD", null);
      motions.put("GUARD", null);
      
      tempArr = new BufferedImage[8];
      for (int i = 0; i < 8; i++) {
         tempArr[i] = sprite.getSubimage(i * 146, 226, 146, 204);
      }
      motions.put("Move Up", tempArr.clone());
      
      tempArr = new BufferedImage[8];
      for (int i = 0; i < 8; i++) {
         tempArr[i] = sprite.getSubimage(i * 146, 226, 146, 204);
      }
      motions.put("Move Down", tempArr.clone());
      
      tempArr = new BufferedImage[8];
      for (int i = 0; i < 8; i++) {
         tempArr[i] = sprite.getSubimage(i * 146, 226, 146, 204);
      }
      motions.put("Move Left", tempArr.clone());
      
      for (int i = 0; i < 8; i++) {
         tempArr[i] = flipHorizontally(tempArr[i]);
      }
      motions.put("Move Right", tempArr.clone());
      
      tempArr = new BufferedImage[29];
      for (int i = 0; i < 29; i++) {
         tempArr[i] = sprite.getSubimage(i * 226, 442, 226, 226);
      }
      motions.put("Skill1", tempArr.clone());
      motions.put("Skill2", null);
   }
   protected BufferedImage TransformColorToTransparency(BufferedImage image, Color c1) {
        final int r1 = c1.getRed();
        final int g1 = c1.getGreen();
        final int b1 = c1.getBlue();
       
        ImageFilter filter = new RGBImageFilter() {
            public int filterRGB(int x, int y, int rgb) {
               int r = ( rgb & 0xFF0000 ) >> 16;
               int g = ( rgb & 0xFF00 ) >> 8;
               int b = ( rgb & 0xFF );
               if( r == r1 && g == g1 && b == b1) {
                  return rgb & 0xFFFFFF;
               }
               return rgb;
            }
         };
       
         ImageProducer ip = new FilteredImageSource( image.getSource(), filter );
         Image img = Toolkit.getDefaultToolkit().createImage(ip);
         BufferedImage dest = new BufferedImage(img.getWidth(null), 
               img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
         Graphics2D g = dest.createGraphics();
         g.drawImage(img, 0, 0, null);
         g.dispose();
         return dest;
      }
}