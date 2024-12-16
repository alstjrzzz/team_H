package view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.border.EmptyBorder;
import controller.GameController;
import model.Card;
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


    public void drawHealthPanel() {
        healthPanel.setLayout(null);
        healthPanel.setOpaque(false);

        int panelWidth = getWidth();
        int panelHeight = 50;

        // Player 1의 캐릭터 로고
        JLabel player1Logo = new JLabel(new ImageIcon(gameState.getMyCharacter().getLogo()));
        player1Logo.setBounds(10, 0, panelHeight, panelHeight);
        healthPanel.add(player1Logo);

        // Player 1의 체력바
        JProgressBar player1HealthBar = new JProgressBar(0, gameState.getMyCharacter().getMaxHealth());
        player1HealthBar.setValue(gameState.getMyHealth());
        player1HealthBar.setBounds(70, 10, panelWidth / 2 - 100, 30);
        player1HealthBar.setForeground(Color.RED);
        healthPanel.add(player1HealthBar);

        // Player 2의 캐릭터 로고
        JLabel player2Logo = new JLabel(new ImageIcon(gameState.getEnemyCharacter().getLogo()));
        player2Logo.setBounds(panelWidth - panelHeight - 10, 0, panelHeight, panelHeight);
        healthPanel.add(player2Logo);

        // Player 2의 체력바
        JProgressBar player2HealthBar = new JProgressBar(0, gameState.getEnemyCharacter().getMaxHealth());
        player2HealthBar.setValue(gameState.getEnemyHealth());
        player2HealthBar.setBounds(panelWidth / 2 + 20, 10, panelWidth / 2 - 100, 30);
        player2HealthBar.setForeground(Color.RED);
        healthPanel.add(player2HealthBar);
    }


    public void drawSelectedCardPanel() {
        cardPanel.removeAll();
        cardPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Player 1의 카드 이미지 추가
        for (Card card : gameState.getSelectedCardList()) {
            BufferedImage cardImage = gameState.getMyCharacter().getCardImage().get(card.getName());
            if (cardImage != null) {
                cardPanel.add(new JLabel(new ImageIcon(cardImage)));
            }
        }

        // Player 2의 카드 이미지 추가
        for (Card card : gameState.getEnemySelectedCardList()) {
            BufferedImage cardImage = gameState.getEnemyCharacter().getCardImage().get(card.getName());
            if (cardImage != null) {
                cardPanel.add(new JLabel(new ImageIcon(cardImage)));
            }
        }
        cardPanel.revalidate();
        cardPanel.repaint();
    }
    
    @Override
    public void paintComponent(Graphics g) {
    	super.paintComponent(g); // 기본 페인트 작업 수행

        System.out.println("playing game screen repaint() !!!");
        if (gameState.getMyCharacter().getCurrentCard() == null) System.out.println("내 캐릭 현재 카드 null");
        else System.out.println("내 캐릭 현재 카드 null아님");
        if (gameState.getEnemyCharacter().getCurrentCard() == null) System.out.println("상대 캐릭 현재 카드 null");
        else System.out.println("상대 캐릭 현재 카드 null아님");
        
        drawHealthPanel();
        drawSelectedCardPanel();
        gameState.getMyCharacter().drawCharacter(g, gameState, this);
        gameState.getEnemyCharacter().drawCharacter(g, gameState, this);
        invalidate();
        revalidate();
        repaint();
    }
}
