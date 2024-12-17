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

public class Zoro extends Character {
   
   
    public Zoro() {
       
        name = "Zoro";
        maxHealth = 100;
        
        try {
            sprite = ImageIO.read(new File("res/character/ZORO.png"));
            sprite = TransformColorToTransparency(sprite, new Color(0, 128, 128));
            if (sprite == null) {
                 System.err.println("sprite가 초기화되지 않았습니다. initCardMotions를 실행할 수 없습니다.");
                 return;
             }
            
            // skillEffect = ImageIO.read(new File(""));
            logo = ImageIO.read(new File("res/character/Zoro_face.png"));
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
      BufferedImage[] tempArr = new BufferedImage[4];
      for (int i = 0; i < 4; i++) {
         tempArr[i] = resizeImage(sprite.getSubimage(i * 123, 770, 123, 152), 90, 90, true);
      }
      motions.put("IDLE", tempArr.clone());
      
      motions.put("HIT", null);
      
      tempArr = new BufferedImage[3];
      for (int i = 0; i < 3; i++) {
         tempArr[i] = resizeImage(sprite.getSubimage(i * 177, 10049, 177, 122), 90, 90, true);
      }
      motions.put("DEAD", tempArr.clone());
      motions.put("GUARD", null);
      
      tempArr = new BufferedImage[8];
      for (int i = 0; i < 8; i++) {
         tempArr[i] = resizeImage(sprite.getSubimage(i * 145, 924,145,162), 90, 90, true);
      }
      motions.put("Move Up", tempArr.clone());
      
      tempArr = new BufferedImage[8];
      for (int i = 0; i < 8; i++) {
         tempArr[i] = resizeImage(sprite.getSubimage(i * 145, 924,145,162), 90, 90, true);
      }
      motions.put("Move Down", tempArr.clone());
      
      tempArr = new BufferedImage[8];
      for (int i = 0; i < 8; i++) {
         tempArr[i] = resizeImage(sprite.getSubimage(i * 145, 924,145,162), 90, 90, true);
      }
      motions.put("Move Left", tempArr.clone());
      
      for (int i = 0; i < 8; i++) {
         tempArr[i] = flipHorizontally(tempArr[i]);
      }
      motions.put("Move Right", tempArr.clone());
      
      motions.put("Skill1", null);
      motions.put("Skill2", null);
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