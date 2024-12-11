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

public class SuperMan extends Character {
	
	
    public SuperMan() {
    	
        super();
        this.name = "SuperMan";
        this.maxHealth = 100;
        
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
    
}
