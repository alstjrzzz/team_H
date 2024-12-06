// SelectCharacterScreen.java

package view;

import java.awt.Button;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import controller.GameController;
import model.GameState;
import java.awt.Dimension;
import javax.swing.JButton;

public class SelectCharacterScreen extends JPanel {
    private ImageIcon Character_backgroundImg;
    private ImageIcon targetCharacterImage; // 이동 대상 캐릭터 이미지
    private ImageIcon selectedCharacterImage; // 현재 보여지는 캐릭터 이미지
    private int slideX; // 슬라이드 애니메이션의 X 좌표
    private boolean isSliding; // 애니메이션 진행 중 여부
    private Timer slideTimer; // 애니메이션 타이머
    private GameController gameController; // GameController 참조
    private String selectedCharacterName = null; // 선택된 캐릭터 이름 저장
    private GameState gameState; // GameState 참조
    
    public SelectCharacterScreen(GameState gameState, GameController gameController) {
    	this.gameState = gameState; // GameState 저장
        setSize(new Dimension(850, 600));
        this.gameController = gameController; // GameController 저장
        Character_backgroundImg = new ImageIcon("res/img/캐릭터 선택창 배경2.jpg");
        
        // 슬라이드 초기 값 설정
        slideX = -300;
        isSliding = false;

        // ready_panel 메서드 호출
        ready_penel();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 기본 배경 이미지 그리기
        if (Character_backgroundImg != null) {
            g.drawImage(Character_backgroundImg.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
        
        // 선택된 캐릭터 이미지 크기 조절
        if (selectedCharacterImage != null) {
            int x = slideX; // 슬라이드 위치
            int y = 75;    // 고정된 Y 위치
            int width = 250;
            int height = 250;

            // 특정 이미지에 대해 크기 확대
            if (selectedCharacterImage.getDescription().contains("zed_background.png")) {
                width = 500;
                height = 500;
            } else if (selectedCharacterImage.getDescription().contains("project_yi_background.png")) {
                width = 500;
                height = 500;
            } else if (selectedCharacterImage.getDescription().contains("superman_background.png")) {
                width = 500;
                height = 500;
            }
            else if (selectedCharacterImage.getDescription().contains("Ginzo_background.jpg")) {
                width = 500;
                height = 550;
            }
            else if (selectedCharacterImage.getDescription().contains("MartianManhunter_background.png")) {
                width = 450;
                height = 500;
            }

            // 확대된 크기로 선택된 캐릭터 이미지 그리기
            g.drawImage(selectedCharacterImage.getImage(), x, y, width, height, this);
        }
    }

    public void startSlideAnimation(ImageIcon newImage) {
        if (isSliding) return; // 애니메이션이 진행 중이면 중복 실행 방지

        // 선택된 이미지 업데이트
        selectedCharacterImage = newImage;
        slideX = -300; // 초기 X 좌표 (왼쪽 화면 밖)
        isSliding = true;

        slideTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                slideX += 10; // X 좌표를 점진적으로 증가
                if (slideX >= 0) { // 목표 위치에 도달하면 애니메이션 종료
                    slideX = 0;
                    isSliding = false;
                    slideTimer.stop();
                }
                repaint();
            }
        });
        slideTimer.start(); // 타이머 시작
    }

    public void ready_penel() {
        ImageIcon Character1 = new ImageIcon("res/character/superman_face.png");
        ImageIcon Character2 = new ImageIcon("res/character/zed.png");
        ImageIcon Character3 = new ImageIcon("res/character/masterYi.jpg");
        ImageIcon Character4 = new ImageIcon("res/character/Ginzo_face.png");
        ImageIcon Character5 = new ImageIcon("res/character/MartianManhunter_face.png");
        	
        
        ImageIcon Character1_background = new ImageIcon("res/character/superman_background.png");
        ImageIcon Character2_background = new ImageIcon("res/character/zed_background.png");
        ImageIcon Character3_background = new ImageIcon("res/character/project_yi_background.png");
        ImageIcon Character4_background = new ImageIcon("res/character/Ginzo_background.jpg");
        ImageIcon Character5_background = new ImageIcon("res/character/MartianManhunter_background.png");
        
        setLayout(null);
        
        // 선택 버튼
        Button ready_button = new Button("선택");
        ready_button.setBounds(332, 465, 38, 23);
        add(ready_button);
        ready_button.setFont(new Font("HY견고딕", Font.BOLD, 12));
        // 선택 버튼 클릭 이벤트
        ready_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(selectedCharacterName != null) {
            		gameController.handleAction("PLAY_GAME");
            	}
            	else {
            		System.out.println("캐릭터 선택해라");
            	}
            }
        });
        
        // 취소 버튼
        Button cancel_button = new Button("취소");
        cancel_button.setBounds(492, 465, 38, 23);
        add(cancel_button);
        // 취소 버튼 클릭 이벤트
        cancel_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedCharacterName = null; // 선택된 캐릭터 초기화
                System.out.println(selectedCharacterName);
            }
        });
        
        cancel_button.setFont(new Font("HY견고딕", Font.BOLD, 12));
        JButton Character_Choice_1 = new JButton(Character1);
        Character_Choice_1.setBounds(157, 229, 100, 100);
        add(Character_Choice_1);

        JButton Character_Choice_2 = new JButton(Character2);
        Character_Choice_2.setBounds(257, 229, 100, 100);
        add(Character_Choice_2);

        JButton Character_Choice_3 = new JButton(Character3);
        Character_Choice_3.setBounds(357, 229, 100, 100);
        add(Character_Choice_3);
        
        JButton Character_Choice_4 = new JButton(Character4);
        Character_Choice_4.setBounds(457, 229, 100, 100);
        add(Character_Choice_4);
        
        JButton Character_Choice_5 = new JButton(Character5);
        Character_Choice_5.setBounds(557, 229, 100, 100);
        add(Character_Choice_5);
        
        
        
        
        // 각 캐릭터 버튼에 클릭 이벤트 추가
        Character_Choice_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	gameState.setMyCharacter(gameState.createCharacter("Superman"));
            	selectedCharacterName = "Superman"; // 선택된 캐릭터 이름 저장
                // 선택된 캐릭터 이미지를 먼저 설정
                selectedCharacterImage = Character1_background;
                repaint(); // 즉시 변경 사항 반영
                startSlideAnimation(Character1_background); // 슬라이드 애니메이션 시작
                System.out.println(selectedCharacterName);
                
                
            }
        });

        Character_Choice_2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	gameState.setMyCharacter(gameState.createCharacter("Zed"));
            	selectedCharacterName = "Zed"; // 선택된 캐릭터 이름 저장
                selectedCharacterImage = Character2_background;
                repaint();
                startSlideAnimation(Character2_background);
                System.out.println(selectedCharacterName);
            }
        });

        Character_Choice_3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	gameState.setMyCharacter(gameState.createCharacter("MasterYi"));
            	selectedCharacterName = "MasterYi"; // 선택된 캐릭터 이름 저장
                selectedCharacterImage = Character3_background;
                repaint();
                startSlideAnimation(Character3_background);
                System.out.println(selectedCharacterName);
            }
        });
        
        Character_Choice_4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	gameState.setMyCharacter(gameState.createCharacter("Ginzo"));
            	selectedCharacterName = "Ginzo"; // 선택된 캐릭터 이름 저장
                selectedCharacterImage = Character4_background;
                repaint();
                startSlideAnimation(Character4_background);
                System.out.println(selectedCharacterName);
            }
        });
        
        Character_Choice_5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	gameState.setMyCharacter(gameState.createCharacter("MartianManhunter"));
            	selectedCharacterName = "MartianManhunter"; // 선택된 캐릭터 이름 저장
                selectedCharacterImage = Character5_background;
                repaint();
                startSlideAnimation(Character5_background);
                System.out.println(selectedCharacterName);
            }
        });

        // 버튼 위치가 화면 크기에 맞춰 조정되도록 재배치
        revalidate();
        repaint();
    }
}

