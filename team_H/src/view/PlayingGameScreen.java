package view;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import controller.GameController;
import model.GameState;
import network.NetworkManager;

public class PlayingGameScreen extends JPanel {

    private GameState gameState;
    private GameController gameController;
    private NetworkManager networkManager;
    

    private JPanel healthPanel;
    private JPanel fieldPanel;
    private JPanel cardPanel;
    
    
    public PlayingGameScreen(GameState gameState, GameController gameController, NetworkManager networkManager) {
    	
        this.gameState = gameState;
        this.gameController = gameController;
        this.networkManager = networkManager;
        
        healthPanel = new JPanel();
        fieldPanel = new FieldPanel();
        cardPanel = new JPanel();

        splitPanel();
        drawHealthPanel();
        drawSelectedCardPanel();
        
        revalidate();
        repaint();
    }
    
    
    
    // 전체 패널 레이아웃을 분리하여 구성
    public void splitPanel() {
    	
        setLayout(new BorderLayout());

        add(healthPanel, BorderLayout.NORTH);  // 상단: 체력바
        add(fieldPanel, BorderLayout.CENTER);  // 중간: 게임 필드
        add(cardPanel, BorderLayout.SOUTH);  // 하단: 카드 패널
    }



    // 상단 패널 구성 (체력바)
    public void drawHealthPanel() {
    	
        healthPanel.setLayout(new BorderLayout());
        healthPanel.setOpaque(false);
        healthPanel.setPreferredSize(new Dimension(
            (int) gameState.getDimension().getWidth(),
            (int) (gameState.getDimension().getHeight() * 1 / 10)
        ));
        
        int width = healthPanel.getWidth();
        int height = healthPanel.getHeight();


        // 캐릭터 초상화 그리기, 지금은 캐릭터 이름만 넣어둠, 캐릭터 이미지 넣을 것
        JLabel characterLabel1;
        JLabel characterLabel2;
        if (gameState.getClientNumber() == 1) {
        	characterLabel1 = new JLabel(gameState.getMyCharacter().getName());
        	characterLabel1.setBounds(0, 0, height, height);
        	characterLabel2 = new JLabel(gameState.getEnemyCharacter().getName());
        	characterLabel2.setBounds(width - height, 0, height, height);
        } else {
        	characterLabel1 = new JLabel(gameState.getEnemyCharacter().getName());
        	characterLabel1.setBounds(0, 0, height, height);
        	characterLabel2 = new JLabel(gameState.getMyCharacter().getName());
        	characterLabel2.setBounds(width - height, 0, height, height);
        }
        healthPanel.add(characterLabel1);
        healthPanel.add(characterLabel2);
        
        
        // 체력바
        JProgressBar healthBar1;
        JProgressBar healthBar2;
        if (gameState.getClientNumber() == 1) {
        	healthBar1 = new JProgressBar(0, gameState.getMyCharacter().getMaxHealth());
        	healthBar1.setValue(gameState.getMyHealth());
        	
        	healthBar2 = new JProgressBar(0, gameState.getEnemyCharacter().getMaxHealth());
        	healthBar2.setValue(gameState.getEnemyHealth());
        } else {
        	healthBar1 = new JProgressBar(0, gameState.getEnemyCharacter().getMaxHealth());
        	healthBar1.setValue(gameState.getEnemyHealth());
        	
        	healthBar2 = new JProgressBar(0, gameState.getMyCharacter().getMaxHealth());
        	healthBar2.setValue(gameState.getMyHealth());
        }
        
        healthBar1.setBackground(Color.GRAY);
        healthBar1.setForeground(Color.RED);
        healthBar1.setBounds(getHeight(), 0, width/2 - height, height);
        healthBar1.setStringPainted(true);
        
        healthBar2.setBackground(Color.GRAY);
        healthBar2.setForeground(Color.RED);
        healthBar2.setBounds(getWidth()/2, 0, width/2 - height, height);
        healthBar2.setStringPainted(true);
        
        healthPanel.add(healthBar1);
        healthPanel.add(healthBar2);

    }

    
    // 카드 선택 패널 (예시)
    public void drawSelectedCardPanel() {
    	cardPanel.setOpaque(false); // 투명하게 만들기
        cardPanel.add(new JLabel("<player1 selected card 1, 2, 3>      <player2 selected card 3, 2, 1>"));
        cardPanel.setPreferredSize(new Dimension(
            (int) gameState.getDimension().getWidth(),
            (int) (gameState.getDimension().getHeight() * 2 / 10)
        ));
    }
    
    
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        gameState.getMyCharacter().drawCharacter(g, gameState, this);
        gameState.getEnemyCharacter().drawCharacter(g, gameState, this);
        
    }
}
