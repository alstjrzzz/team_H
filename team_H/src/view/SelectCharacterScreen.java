// SelectCharacterScreen.java

package view;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import controller.GameController;
import model.Card;
import model.GameState;
import network.NetworkManager;

import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JLabel;

public class SelectCharacterScreen extends JPanel {
	
    private GameController gameController;
    private GameState gameState;
    private NetworkManager networkManager;
    
    private JPanel selectCharacterPanel;
    // 배경 이미지 추가
    private Image backgroundImage;
    
    // 캐릭터 버튼 이미지 변수
    private ImageIcon supermanIcon;
    private ImageIcon doraemonIcon;
    private ImageIcon zoroIcon;
    private ImageIcon CygnusIcon;
    private ImageIcon AceIcon;
    private ImageIcon LuffyIcon;
    private ImageIcon FinnIcon;
    
    // 캐릭터 배경 이미지 관련 변수
    private Map<String, Image> characterBackgrounds = new HashMap<>();
    private String currentCharacter = null; // 현재 선택된 캐릭터
    private int backgroundX = 0; // 배경 이미지 X 좌표 (애니메이션 시작 위치)
    private Timer animationTimer;
    
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 기본 배경 이미지 그리기
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        // 캐릭터 배경 이미지 애니메이션 그리기
        if (currentCharacter != null) {
            Image charBackground = characterBackgrounds.get(currentCharacter);
            if (charBackground != null) {
                // 캐릭터별 크기 조정
                int x = backgroundX;
                int y = 0;
                int width = getWidth();
                int height = getHeight();

                // 캐릭터별 크기 설정
                switch (currentCharacter) {
                    case "SuperMan":
                        width = 450;
                        height = 500;
                        y = (getHeight() - height); // 세로 가운데 정렬
                        break;
                    case "Doraemon":
                        width = 425;
                        height = 475;
                        y = (getHeight() - height); // 세로 가운데 정렬
                        break;
                    case "Zoro":
                        width = 450;
                        height = 500;
                        y = (getHeight() - height); // 세로 가운데 정렬
                        break;
                    case "Cygnus":
                        width = 500;
                        height = 500;
                        y = (getHeight() - height); // 세로 가운데 정렬
                        break;
                    case "Ace":
                        width = 450;
                        height = 520;
                        y = (getHeight() - height); // 세로 가운데 정렬
                        break;
                    case "Luffy":
                        width = 475;
                        height = 525;
                        y = (getHeight() - height); // 세로 가운데 정렬
                        break;
                    case "Finn":
                        width = 475;
                        height = 475;
                        y = (getHeight() - height); // 세로 가운데 정렬
                        break;
                    
                    // 다른 캐릭터 추가 시 여기에 case를 추가
                }

                // 캐릭터 배경 이미지 그리기
                g.drawImage(charBackground, x, y, width, height, this);
            }
        }
    }
    
    public void drawSelectCharacterPanel() {

        setLayout(null);
        
        // 캐릭터 이미지 로드
        supermanIcon = new ImageIcon("res/character/superman_face.png");
        doraemonIcon = new ImageIcon("res/character/Doraemon_face.png");
        zoroIcon = new ImageIcon("res/character/Zoro_face.png");
        CygnusIcon = new ImageIcon("res/character/Cygnus_face.png");
        AceIcon = new ImageIcon("res/character/Ace_face.png");
        LuffyIcon = new ImageIcon("res/character/Luffy_face.png");
        FinnIcon = new ImageIcon("res/character/Finn_face.png");
        
        // 캐릭터별 배경 이미지 로드
        characterBackgrounds.put("SuperMan", new ImageIcon("res/character/superman_background.png").getImage());
        characterBackgrounds.put("Doraemon", new ImageIcon("res/character/doraemon_background.png").getImage());
        characterBackgrounds.put("Zoro", new ImageIcon("res/character/Zoro_background2.png").getImage());
        characterBackgrounds.put("Cygnus", new ImageIcon("res/character/Cygnus_background.png").getImage());
        characterBackgrounds.put("Ace", new ImageIcon("res/character/Ace_background.png").getImage());
        characterBackgrounds.put("Luffy", new ImageIcon("res/character/Luffy_background.png").getImage());
        characterBackgrounds.put("Finn", new ImageIcon("res/character/Finn_background.png").getImage());
        
        // 수동으로 캐릭터 버튼 추가
        int buttonWidth = 100;
        int buttonHeight = 100;
        int gap = 30;

        // SuperMan 버튼
        JButton supermanButton = new JButton(supermanIcon);
        supermanButton.setBounds(100, 100, buttonWidth, buttonHeight);
        supermanButton.addActionListener(e -> {
            startAnimation("SuperMan"); // 애니메이션 시작
            gameState.setMyCharacter(gameState.createCharacter("SuperMan"));
            System.out.println("선택된 캐릭터: SuperMan");
        });
        add(supermanButton);
        
        // Doraemon 버튼
        JButton doraemonButton = new JButton(doraemonIcon);
        doraemonButton.setBounds(250, 100, buttonWidth, buttonHeight);
        doraemonButton.addActionListener(e -> {
            startAnimation("Doraemon"); // 애니메이션 시작
            gameState.setMyCharacter(gameState.createCharacter("Doraemon"));
            System.out.println("선택된 캐릭터: Doraemon");
        });
        add(doraemonButton);
        
        // Zoro 버튼
        JButton zoroButton = new JButton(zoroIcon);
        zoroButton.setBounds(400, 100, buttonWidth, buttonHeight);
        zoroButton.addActionListener(e -> {
            startAnimation("Zoro"); // 애니메이션 시작
            gameState.setMyCharacter(gameState.createCharacter("Zoro"));
            System.out.println("선택된 캐릭터: Zoro");
        });
        add(zoroButton);
        
        // Cygnus 버튼
        JButton CygnusButton = new JButton(CygnusIcon);
        CygnusButton.setBounds(550, 100, buttonWidth, buttonHeight);
        CygnusButton.addActionListener(e -> {
            startAnimation("Cygnus"); // 애니메이션 시작
            gameState.setMyCharacter(gameState.createCharacter("Cygnus"));
            System.out.println("선택된 캐릭터: Cygnus");
        });
        add(CygnusButton);
        
        // Ace 버튼
        JButton AceButton = new JButton(AceIcon);
        AceButton.setBounds(700, 100, buttonWidth, buttonHeight);
        AceButton.addActionListener(e -> {
            startAnimation("Ace"); // 애니메이션 시작
            gameState.setMyCharacter(gameState.createCharacter("Ace"));
            System.out.println("선택된 캐릭터: Ace");
        });
        add(AceButton);
        
        // Luffy 버튼
        JButton LuffyButton = new JButton(LuffyIcon);
        LuffyButton.setBounds(100, 250, buttonWidth, buttonHeight);
        LuffyButton.addActionListener(e -> {
            startAnimation("Luffy"); // 애니메이션 시작
            gameState.setMyCharacter(gameState.createCharacter("Luffy"));
            System.out.println("선택된 캐릭터: Luffy");
        });
        add(LuffyButton);
        
        // Finn 버튼
        JButton FinnButton = new JButton(FinnIcon);
        FinnButton.setBounds(250, 250, buttonWidth, buttonHeight);
        FinnButton.addActionListener(e -> {
            startAnimation("Finn"); // 애니메이션 시작
            gameState.setMyCharacter(gameState.createCharacter("Finn"));
            System.out.println("선택된 캐릭터: Finn");
        });
        add(FinnButton);
        // 새로운 캐릭터 버튼 추가 시 아래와 같은 형식으로 추가 가능
        // JButton newCharacterButton = new JButton("NewCharacter");
        // newCharacterButton.setBounds(400, 100, buttonWidth, buttonHeight);
        // newCharacterButton.addActionListener(createCharacterSelectListener("NewCharacter"));
        // add(newCharacterButton);

        
        
        
        
        // READY 버튼
        JButton readyButton = new JButton("READY") {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getModel().isRollover()) { // 마우스가 올라간 상태
                    g.setColor(Color.WHITE); // 밑줄 색상
                    int textHeight = g.getFontMetrics().getHeight(); // 텍스트 높이를 계산
                    int y = (getHeight() + textHeight) / 2 + 2; // 텍스트 바로 아래에 밑줄 위치
                    g.fillRect(10, y, getWidth() - 20, 2); // 밑줄 그리기: (x, y, width, height)
                }
            }
        };
        readyButton.setFont(new Font("궁서", Font.BOLD, 18)); // 버튼 폰트 설정
        readyButton.setForeground(Color.WHITE); // 텍스트 색상
        readyButton.setFocusPainted(false);     // 포커스 테두리 제거
        readyButton.setContentAreaFilled(false); // 버튼 배경 제거
        readyButton.setBorderPainted(false);     // 버튼 테두리 제거
        readyButton.setOpaque(false);            // 완전히 투명

        // 마우스 Hover 효과를 위한 라벨 (스포트라이트 효과)
        JLabel spotlightLabel = new JLabel();
        spotlightLabel.setBackground(Color.WHITE);
        spotlightLabel.setOpaque(true);
        spotlightLabel.setVisible(false); // 기본 상태에서는 숨김

        // 마우스 Hover 및 클릭 효과 추가
        readyButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                spotlightLabel.setVisible(true); // 스포트라이트 라벨 표시
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                spotlightLabel.setVisible(false); // 스포트라이트 라벨 숨김
            }
        });
        readyButton.setBounds(443, 500, 100, 50);
        // 버튼 클릭 시 이벤트
        readyButton.addActionListener(e -> {
            if (readyButton.isEnabled() && gameState.getMyCharacter() != null) {
                networkManager.sendCharacterSelection();
                System.out.println("캐릭터 선택 : " + gameState.getMyCharacter().getName());
                readyButton.setEnabled(false);
            } else {
                System.out.println("캐릭터가 선택되지 않았거나 이미 준비 완료 상태입니다.");
            }
        });
        
        add(readyButton);
        
        revalidate();
        repaint();
    }
    
    private void startAnimation(String characterName) {
        currentCharacter = characterName;
        backgroundX = -450; // 애니메이션 시작 위치

        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop(); // 이전 애니메이션 중지
        }

        animationTimer = new Timer(5, e -> { // 5ms 딜레이로 더 빠르게
            backgroundX += 15; // 한 번에 20px 이동
            repaint();

            if (backgroundX >= 0) {
                backgroundX = 0;
                animationTimer.stop();
            }
        });
        animationTimer.start();
    }

   
    // 캐릭터 선택 로직
    private ActionListener createCharacterSelectListener(String characterName) {
        return e -> {
            gameState.setMyCharacter(gameState.createCharacter(characterName));
            System.out.println("선택된 캐릭터: " + characterName);
        };
    }
}

