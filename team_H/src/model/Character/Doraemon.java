package model.Character;

import java.util.HashMap;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import model.Card;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Doraemon extends Character {
   
   public Doraemon() {
      
      name = "Doraemon";
      maxHealth = 100;
      try {
         sprite = ImageIO.read(new File("res/character/Doraemon.png"));
         sprite = TransformColorToTransparency(sprite, new Color(173, 217, 186));
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
      initMotions();
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
      
      motions = new HashMap<>();
      BufferedImage[] tempArr = new BufferedImage[8];
      for (int i = 0; i < 8; i++) {
         tempArr[i] = resizeImage(sprite.getSubimage(i * 32, 144, 32, 48), 75, 75, true);
      }
      motions.put("IDLE", tempArr.clone());
      
      motions.put("HIT", null);
      
      tempArr = new BufferedImage[8];
      for (int i = 0; i < 8; i++) {
         tempArr[i] = resizeImage(sprite.getSubimage(i * 32, 192, 32, 48), 75, 75, true);
      }
      motions.put("DEAD", null);
      motions.put("GUARD", null);
      
      tempArr = new BufferedImage[3];
      for (int i = 0; i < 3; i++) {
         tempArr[i] = resizeImage(sprite.getSubimage(i * 32, 48, 32, 48), 75, 75, true);
      }
      motions.put("Move Up", tempArr.clone());
      
      tempArr = new BufferedImage[3];
      for (int i = 0; i < 3; i++) {
         tempArr[i] = resizeImage(sprite.getSubimage(i * 32, 0, 32, 48), 75, 75, true);
      }
      motions.put("Move Down", tempArr.clone());
      
      tempArr = new BufferedImage[3];
      for (int i = 0; i < 3; i++) {
         tempArr[i] = resizeImage(sprite.getSubimage(i * 32, 96, 32, 48), 75, 75, true);
      }
      motions.put("Move Left", tempArr.clone());
      
      for (int i = 0; i < 3; i++) {
         tempArr[i] = flipHorizontally(tempArr[i]);
      }
      motions.put("Move Right", tempArr.clone());
      
      tempArr = new BufferedImage[3];
      for (int i = 0; i < 3; i++) {
         tempArr[i] = resizeImage(sprite.getSubimage(i * 32, 656, 32, 48), 75, 75, true);
      }
      motions.put("Skill1", tempArr.clone());
      
      tempArr = new BufferedImage[8];
      for (int i = 0; i < 8; i++) {
         tempArr[i] = resizeImage(sprite.getSubimage(i * 48, 592, 48, 64), 75, 75, true);
      }
      motions.put("Skill2", tempArr.clone());
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
     
}
