package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.border.EmptyBorder;
import controller.GameController;
import model.Card;
import model.GameState;
import network.NetworkManager;

public class PlayingGameScreen extends JPanel {

    private GameState gameState;
    private GameController gameController;
    private NetworkManager networkManager;
    private BufferedImage backgroundImage;
    
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
    
    private JPanel healthPanel;
    private JPanel fieldPanel;
    private JPanel cardPanel;
    
    
    public PlayingGameScreen(GameState gameState, GameController gameController, NetworkManager networkManager) {
    	
        this.gameState = gameState;
        this.gameController = gameController;
        this.networkManager = networkManager;
        
        try {
            // Load the background image
            backgroundImage = ImageIO.read(new File("res/img/게임배경화면.png")); // Replace with your image path
        } catch (IOException e) {
            e.printStackTrace();
        }
        healthPanel = new JPanel();
        fieldPanel = new JPanel();
        cardPanel = new JPanel(null);

        splitPanel();
        drawHealthPanel();
        drawSelectedCardPanel();
        
        revalidate();
        repaint();
        
        System.out.println("PlayingGameScreen initialized successfully");
    }
    
    
    
    public void splitPanel() {
        setLayout(new BorderLayout());

        healthPanel.setPreferredSize(new Dimension(0, 50)); // 고정 높이
        cardPanel.setPreferredSize(new Dimension(0, 100)); // 고정 높이

        add(healthPanel, BorderLayout.NORTH);  // 상단: 체력바
        add(fieldPanel, BorderLayout.CENTER);  // 중간: 게임 필드
        add(cardPanel, BorderLayout.SOUTH);  // 하단: 카드 패널
    }

    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, 1000, 700, this);
        }

        drawDashedGrid(g2d, 3, 6, 100, 100);
        System.out.println("playing game screen repaint() !!!");
        gameState.getMyCharacter().drawCharacter(g, gameState, this);
        gameState.getEnemyCharacter().drawCharacter(g, gameState, this);
        
        
    }
    
    
    public void drawHealthPanel() {
        healthPanel.setLayout(null);
        healthPanel.setPreferredSize(new Dimension(0, 50)); // 고정 높이 설정
        healthPanel.setOpaque(false);

        int panelWidth = 800; // 패널의 예상 너비를 설정
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
    
    private void drawDashedGrid(Graphics2D g2d, int rows, int cols, int cellWidth, int cellHeight) {
        // Configure dashed line and color
        float[] dash = {5.0f, 5.0f}; // Dashed pattern
        BasicStroke dottedLine = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        g2d.setStroke(dottedLine);
        g2d.setColor(Color.WHITE);

        for (int i = 0; i <= rows; i++) {
            int y = i * cellHeight;
            g2d.drawLine(0, y, cols * cellWidth, y); // Horizontal lines
        }

        for (int j = 0; j <= cols; j++) {
            int x = j * cellWidth;
            g2d.drawLine(x, 0, x, rows * cellHeight); // Vertical lines
        }
    }
    
    
}
