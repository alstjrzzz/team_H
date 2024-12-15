package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import model.Character.SuperMan;

public class FieldPanel extends JPanel {
	
	public final static int gridRows = 3;  // 그리드 행 수
    public final static int gridCols = 5;  // 그리드 열 수
    
    public final static int gridStartX = 100;	// 그리드의 왼쪽 상단 모서리 좌표 x
    public final static int gridStartY = 100;	// y

    public final static int gridWidth = 100;	// 그리드의 각 셀 너비
    public final static int gridHeight = 100;	// 높이
    
    public final static int gridClient1X = 0;	// 셀 내에서 Client1(왼쪽)의 캐릭터가 서 있을 위치(왼쪽 아래를 가리킴)
    public final static int gridClient1Y = 0;
    
    public final static int gridClient2X = 80;	// Client2(오른쪽) 수정필요
    public final static int gridClient2Y = 0;
    
    
    public FieldPanel() {
    	
    	
    }


	public int getGridRows() {
		return gridRows;
	}


	public int getGridCols() {
		return gridCols;
	}


	public int getGridStartX() {
		return gridStartX;
	}


	public int getGridStartY() {
		return gridStartY;
	}


	public int getGridWidth() {
		return gridWidth;
	}


	public int getGridHeight() {
		return gridHeight;
	}


	public int getGridClient1X() {
		return gridClient1X;
	}


	public int getGridClient1Y() {
		return gridClient1Y;
	}


	public int getGridClient2X() {
		return gridClient2X;
	}


	public int getGridClient2Y() {
		return gridClient2Y;
	}
    
    
}
