package view;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import controller.GameController;
import model.Card;
import model.GameState;
import network.NetworkManager;

public class PlayingGameScreen extends JPanel {

    private GameState gameState;
    private GameController gameController;
    private NetworkManager networkManager;
    private ImageIcon backgroundImage; // 움짤을 위한 ImageIcon

    public final static int gridRows = 3; // 그리드 행 수
    public final static int gridCols = 6; // 그리드 열 수

    public final static int gridStartX = 100; // 그리드의 왼쪽 상단 모서리 좌표 x
    public final static int gridStartY = 200; // y

    public final static int gridWidth = 100; // 그리드의 각 셀 너비
    public final static int gridHeight = 100; // 높이

    public final static int gridClient1X = 0; // 셀 내에서 Client1(왼쪽)의 캐릭터가 서 있을 위치(왼쪽 아래를 가리킴)
    public final static int gridClient1Y = 0;

    public final static int gridClient2X = 80; // Client2(오른쪽) 수정필요
    public final static int gridClient2Y = 0;

    private JPanel healthPanel;
    private JPanel fieldPanel;
    private JPanel cardPanel;
    
    public PlayingGameScreen(GameState gameState, GameController gameController, NetworkManager networkManager) {

        this.gameState = gameState;
        this.gameController = gameController;
        this.networkManager = networkManager;

        // 움짤 배경화면 로드
        backgroundImage = new ImageIcon("res/img/gameback_kim.gif"); // 움짤 경로 지정

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
        fieldPanel.setOpaque(false);
        add(healthPanel, BorderLayout.NORTH); // 상단: 체력바
        add(fieldPanel, BorderLayout.CENTER); // 중간: 게임 필드
        add(cardPanel, BorderLayout.SOUTH); // 하단: 카드 패널
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        if (backgroundImage != null) {
            g.drawImage(backgroundImage.getImage(), 0, 0, 1000, 570, this);
        } else {
            System.out.println("Background image is null.");
        }
        
        // 그리드 및 캐릭터 그리기
        drawDashedGrid(g2d, 3, 6, 150, 60);
        System.out.println("Playing game screen repaint() !!!");
        gameState.getMyCharacter().drawCharacter(g, gameState, this);
        gameState.getEnemyCharacter().drawCharacter(g, gameState, this);
    }

    public void drawHealthPanel() {
        healthPanel.setLayout(null);
        healthPanel.setPreferredSize(new Dimension(0, 70)); // 고정 높이를 늘림
        healthPanel.setOpaque(false);

        int panelWidth = 950; // 패널의 예상 너비를 설정
        int panelHeight = 50;

        // Player 1의 캐릭터 로고
        JLabel player1Logo = new JLabel(new ImageIcon(gameState.getMyCharacter().getLogo()));
        player1Logo.setBounds(30, 0, panelHeight, panelHeight);
        healthPanel.add(player1Logo);

        // Player 1의 체력바
        JProgressBar player1HealthBar = new JProgressBar(0, gameState.getMyCharacter().getMaxHealth());
        player1HealthBar.setValue(gameState.getMyHealth());
        player1HealthBar.setBounds(90, 10, panelWidth / 2 - 100, 30);
        player1HealthBar.setForeground(Color.RED);
        healthPanel.add(player1HealthBar);

        // Player 1의 이름
        JLabel player1NameLabel = new JLabel(gameState.getMyCharacter().getName());
        player1NameLabel.setBounds(90, 40, panelWidth / 2 - 100, 20);
        player1NameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        player1NameLabel.setForeground(Color.WHITE); // 흰색 글씨로 이름 표시
        healthPanel.add(player1NameLabel);

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

        // Player 2의 이름
        JLabel player2NameLabel = new JLabel(gameState.getEnemyCharacter().getName());
        player2NameLabel.setBounds(panelWidth / 2 + 20, 40, panelWidth / 2 - 100, 20);
        player2NameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        player2NameLabel.setForeground(Color.WHITE); // 흰색 글씨로 이름 표시
        healthPanel.add(player2NameLabel);
    }


    public void drawSelectedCardPanel() {
        cardPanel.removeAll();
        cardPanel.setLayout(new BorderLayout()); // BorderLayout으로 배치

        // Player 1의 카드 패널 (왼쪽)
        JPanel player1CardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        player1CardPanel.setBackground(Color.BLACK);
        JLabel player1Label = new JLabel("Player 1 Cards");
        player1Label.setForeground(Color.WHITE);
        player1CardPanel.add(player1Label);

        for (Card card : gameState.getSelectedCardList()) {
            ImageIcon cardImage = new ImageIcon(gameState.getMyCharacter().getCardImage().get(card.getName()));
            if (cardImage != null) {
                player1CardPanel.add(new JLabel(cardImage));
            }
        }

        // Player 2의 카드 패널 (오른쪽)
        JPanel player2CardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        player2CardPanel.setBackground(Color.BLACK);
        JLabel player2Label = new JLabel("Player 2 Cards");
        player2Label.setForeground(Color.WHITE);
        player2CardPanel.add(player2Label);

        for (Card card : gameState.getEnemySelectedCardList()) {
            ImageIcon cardImage = new ImageIcon(gameState.getEnemyCharacter().getCardImage().get(card.getName()));
            if (cardImage != null) {
                player2CardPanel.add(new JLabel(cardImage));
            }
        }

        // 중앙 빈 패널
        JPanel emptyCenterPanel = new JPanel();
        emptyCenterPanel.setBackground(Color.BLACK);

        // 패널 추가
        cardPanel.add(player1CardPanel, BorderLayout.WEST);
        cardPanel.add(emptyCenterPanel, BorderLayout.CENTER); // 중앙 빈 공간
        cardPanel.add(player2CardPanel, BorderLayout.EAST);

        cardPanel.revalidate();
        cardPanel.repaint();
    }


    private void drawDashedGrid(Graphics2D g2d, int rows, int cols, int cellWidth, int cellHeight) {
        int gridWidth = cols * cellWidth; // 전체 그리드 너비
        int gridHeight = rows * cellHeight; // 전체 그리드 높이
        int xOffset = (getWidth() - gridWidth) / 2; // 화면 너비를 기준으로 중앙 정렬
        int yOffset = (getHeight() - gridHeight) / 2 + 125; // 화면 높이를 기준으로 중앙 정렬

        float[] dash = {5.0f, 5.0f}; // Dashed pattern
        BasicStroke dottedLine = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        g2d.setStroke(dottedLine);
        g2d.setColor(Color.WHITE);

        // 가로선 그리기
        for (int i = 0; i <= rows; i++) {
            int y = yOffset + i * cellHeight;
            g2d.drawLine(xOffset, y, xOffset + gridWidth, y);
        }

        // 세로선 그리기
        for (int j = 0; j <= cols; j++) {
            int x = xOffset + j * cellWidth;
            g2d.drawLine(x, yOffset, x, yOffset + gridHeight);
        }
    }
}
