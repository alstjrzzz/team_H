// SelectCharacterScreen.java

package view;

import java.awt.Button;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import controller.GameController;
import model.Card;
import model.GameState;
import network.NetworkManager;

import java.awt.Dimension;
import javax.swing.JButton;

public class SelectCharacterScreen extends JPanel {
	
    private GameController gameController;
    private GameState gameState;
    private NetworkManager networkManager;
    
    private JPanel selectCharacterPanel;
    // 배경 이미지 추가
    private Image backgroundImage;
    
    public SelectCharacterScreen(GameState gameState, GameController gameController, NetworkManager networkManager) {
    	
    	this.gameState = gameState;
    	this.gameController = gameController;
    	this.networkManager = networkManager;
    	
        setSize(new Dimension(850, 600));

        selectCharacterPanel = new JPanel();
        
        // 배경 이미지 로드
        backgroundImage = new ImageIcon("res/img/캐릭터 선택창 배경.png").getImage();
        
        drawSelectCharacterPanel();
    }

    // 배경 이미지를 그리기 위한 paintComponent 오버라이드
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 배경 이미지 크기를 패널 크기에 맞게 조정
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
    
    public void drawSelectCharacterPanel() {
    	
        setLayout(null);
        
        // 선택 버튼
        Button ready_button = new Button("READY");
        ready_button.setBounds(332, 465, 38, 23);
        ready_button.addActionListener(e -> {
            if (ready_button.isEnabled() && gameState.getMyCharacter() != null) {
                networkManager.sendCharacterSelection();
                System.out.println("캐릭터 선택 : " + gameState.getMyCharacter().getName());
                ready_button.setEnabled(false);
            } else {
                System.out.println("캐릭터가 선택되지 않았거나 이미 준비 완료 상태입니다.");
            }
        });

        add(ready_button);
        
        
        // 캐릭터 버튼 자동 생성
        for (int i = 0; i < gameState.getCharacterList().size(); i++) {
        	
        	String character = gameState.getCharacterList().get(i);
        	
        	int buttonWidth = 100;
        	int buttonHeight = 100;
        	int gap = 30;
        	
        	JButton characterButton = new JButton(character);
        	characterButton.setBounds((int) (getWidth() * 0.1) + i * (buttonWidth + gap), (int) (getHeight() * 0.1), buttonWidth, buttonHeight);
        	characterButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    gameState.setMyCharacter(gameState.createCharacter(character));
                }
            });
        	add(characterButton);
        }
        
        
        // 버튼 위치가 화면 크기에 맞춰 조정되도록 재배치
        revalidate();
        repaint();
    }
}

