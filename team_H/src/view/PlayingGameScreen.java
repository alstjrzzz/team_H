package view;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import controller.GameController;
import model.GameState;

public class PlayingGameScreen extends JPanel {

    private GameState gameState;
    private GameController gameController;

    private JPanel healthPanel = new JPanel();   // 체력바와 관련된 패널
    private JPanel fieldPanel = new FieldPanel(); // 게임 필드
    private JPanel cardPanel = new JPanel();     // 카드 패널
    private JLabel healthLabel, characterLabel1, characterLabel2, timerLabel, victoryCountLabel1, victoryCountLabel2;
    private JProgressBar healthBar1, healthBar2; // 두 캐릭터의 체력바
    private Timer gameTimer;
    private int timeLeft = 60;  // 게임 타이머 (초)
    private int victoryCount1 = 0;  // 플레이어 1 승리 횟수
    private int victoryCount2 = 0;  // 플레이어 2 승리 횟수
    private ImageIcon gameBack, character1Image, character2Image;
    private ImageIcon ZedIcon;
    private ImageIcon MasterYiIcon;
    public PlayingGameScreen(GameState gameState, GameController gameController) {
        this.gameState = gameState;
        this.gameController = gameController;

        // 캐릭터 이미지 불러오기
        character1Image = new ImageIcon("res/character/zed.png");
        Image characterImage_Zed = character1Image.getImage();
        Image Zed_Face = characterImage_Zed.getScaledInstance(50, 60, Image.SCALE_SMOOTH);
        ZedIcon = new ImageIcon(Zed_Face);  
        character2Image = new ImageIcon("res/character/masterYi.jpg");
        Image characterImage_masterYi = character2Image.getImage();
        Image MasterYi_Face = characterImage_masterYi.getScaledInstance(50, 60, Image.SCALE_SMOOTH);
        MasterYiIcon = new ImageIcon(MasterYi_Face);  
        // 배경화면 불러오기
        gameBack = new ImageIcon("res/img/게임배경화면.png");
        
        splitPanel(); // 패널 분리
        drawHealthPanel();  // 체력바 및 관련 컴포넌트 그리기
        drawSelectedCardPanel();  // 카드 패널 그리기
        startGameTimer();  // 게임 타이머 시작
    }
    
    // 전체 패널 레이아웃을 분리하여 구성
    public void splitPanel() {
        setLayout(new BorderLayout());

        add(healthPanel, BorderLayout.NORTH);  // 상단: 체력바, 시간, 승리 횟수
        add(fieldPanel, BorderLayout.CENTER);  // 중간: 게임 필드
        add(cardPanel, BorderLayout.SOUTH);  // 하단: 카드 패널
    }

    // 상단 패널 구성 (체력바, 시간, 승리 횟수)
    public void drawHealthPanel() {
        healthPanel.setLayout(new BorderLayout());
        healthPanel.setOpaque(false);
        healthPanel.setPreferredSize(new Dimension(
            (int) gameState.getDimension().getWidth(),
            (int) (gameState.getDimension().getHeight() * 1 / 10)
        ));

        // 왼쪽 캐릭터 이미지
        characterLabel1 = new JLabel(ZedIcon);
        characterLabel1.setBorder(new EmptyBorder(5, 5, 5, 5));

        // 오른쪽 캐릭터 이미지
        characterLabel2 = new JLabel(MasterYiIcon);
        characterLabel2.setBorder(new EmptyBorder(5, 5, 5, 5));

        // 중앙에 체력바
        JPanel centerPanel = new JPanel(new BorderLayout());


        int healthBarWidth = 300; // 화면 너비의 80%로 설정 (예시)
        int healthBarHeight = 20; // 높이는 고정값으로 설정
        
        // 왼쪽 체력바
        healthBar1 = new JProgressBar(0, 100);
        healthBar1.setValue(80);  // 예시 값
        healthBar1.setBackground(Color.GRAY);
        healthBar1.setForeground(Color.RED);
        healthBar1.setPreferredSize(new Dimension(healthBarWidth, healthBarHeight));
        healthBar1.setStringPainted(true);
        
        // 오른쪽 체력바
        healthBar2 = new JProgressBar(0, 100);
        healthBar2.setValue(80);  // 예시 값
        healthBar2.setBackground(Color.GRAY);
        healthBar2.setForeground(Color.RED);
        healthBar2.setPreferredSize(new Dimension(healthBarWidth, healthBarHeight));
        healthBar2.setStringPainted(true);

        // 시간 표시
        timerLabel = new JLabel("Time: " + timeLeft + "s", JLabel.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timerLabel.setOpaque(false);
        // 승리 횟수 표시 (왼쪽과 오른쪽)
        victoryCountLabel1 = new JLabel("Player 1 Wins: " + victoryCount1);
        victoryCountLabel1.setFont(new Font("Arial", Font.BOLD, 16));
        victoryCountLabel1.setHorizontalAlignment(SwingConstants.LEFT);
        victoryCountLabel1.setOpaque(false);
        victoryCountLabel2 = new JLabel("Player 2 Wins: " + victoryCount2);
        victoryCountLabel2.setFont(new Font("Arial", Font.BOLD, 16));
        victoryCountLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
        victoryCountLabel2.setOpaque(false);
        // 체력바, 시간, 승리 횟수를 상단에 배치
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(characterLabel1, BorderLayout.WEST);  // 왼쪽 캐릭터
        topPanel.add(centerPanel, BorderLayout.CENTER);   // 체력바 및 시간
        topPanel.add(characterLabel2, BorderLayout.EAST);  // 오른쪽 캐릭터

        // 체력바와 시간 배치
        centerPanel.add(healthBar1, BorderLayout.WEST);
        centerPanel.add(timerLabel, BorderLayout.CENTER);
        centerPanel.add(healthBar2, BorderLayout.EAST);
        centerPanel.setOpaque(false);
        // 승리 횟수 배치
        JPanel victoryPanel = new JPanel(new BorderLayout());
        victoryPanel.add(victoryCountLabel1, BorderLayout.WEST);
        victoryPanel.add(victoryCountLabel2, BorderLayout.EAST);
        victoryPanel.setOpaque(false);
        healthPanel.add(victoryPanel, BorderLayout.SOUTH);
        healthPanel.add(topPanel, BorderLayout.CENTER);
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

    // 게임 타이머 업데이트
    public void startGameTimer() {
        gameTimer = new Timer(1000, e -> {
            if (timeLeft > 0) {
                timeLeft--;
                timerLabel.setText("Time: " + timeLeft + "s");
            } else {
                // 시간 초과 처리
            }
        });
        gameTimer.start();
    }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);  // 부모 클래스의 paintComponent 호출하여 기본 설정 처리

      // 배경 이미지 그리기
      if (gameBack != null) {
         g.drawImage(gameBack.getImage(), 0, 0, getWidth(), getHeight(), this);
      }
   }
}
