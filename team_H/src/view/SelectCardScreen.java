// SelectCardScreen.java

package view;

import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import java.io.IOException;

import javax.swing.Timer;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.imageio.ImageIO;

import controller.GameController;
import model.Card;
import model.GameState;
import network.NetworkManager;




public class SelectCardScreen extends JPanel {
   
   private GameState gameState;
   private GameController gameController;
   private NetworkManager networkManager;
   
   private JPanel healthPanel = new JPanel();
   private JPanel selectCardPanel = new JPanel();
   private JPanel fieldPanel = new JPanel();
   private JPanel buttonPanel = new JPanel();
   private JPanel selectedCardPanel = new JPanel();
   
   private LinkedList<Card> selectedCardList;
   
   private Image backgroundImage; // 배경 이미지 객체
   private int backgroundY = 0; // 배경 이미지의 Y 좌표
   private Timer animationTimer; // 애니메이션 타이머
   private Card[] slotCards = new Card[3]; // 슬롯별 카드 상태를 저장
   private int selectedSlotIndex = -1; // 선택된 슬롯 인덱스 (-1은 슬롯이 선택되지 않은 상태를 의미)
   private Card slot1 = null;
   private Card slot2 = null;
   private Card slot3 = null;
   private JButton[] slotButtons;
   private Map<Card, JButton> cardButtonMap = new HashMap<>(); // 카드와 버튼 매핑
   private Map<Card, JPanel> cardContainerMap = new HashMap<>(); // 카드와 컨테이너 매핑
   
   public SelectCardScreen(GameState gameState, GameController gameController, NetworkManager networkManager) {
      
      this.gameState = gameState;
      this.gameController = gameController;
      this.networkManager = networkManager;
      
      selectedCardList = new LinkedList<>();
      selectedCardList.add(null);
      selectedCardList.add(null);
      selectedCardList.add(null);
      
      // 배경 이미지 로드
        backgroundImage = new ImageIcon("res/img/카드선택_배경화면.png").getImage();
        startAnimation(); // 애니메이션 시작
      
      splitPanel();
      drawHealthPanel();
      drawFieldPanel();
      drawButtonPanel();
      drawSelectCardPanel();
      drawSelectedCardPanel();
   }
   
   // 스크린 전체의 구역을 나눕니다요
   public void splitPanel() {
      
      setLayout(new BorderLayout());
      
        add(healthPanel, BorderLayout.NORTH);
        add(selectCardPanel, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(fieldPanel, BorderLayout.WEST);
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(selectedCardPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
   }
   
   
   
   // 상단에 위치한 가로100% 세로10% 비율의 상태칸
   public void drawHealthPanel() {
       healthPanel.setLayout(null);
       healthPanel.setPreferredSize(new Dimension(0, 70)); // 고정 높이를 늘림
       healthPanel.setOpaque(true);
       healthPanel.setBackground(new Color(236, 237, 215)); 
       healthPanel.setBorder(BorderFactory.createMatteBorder(4, 4, 0, 4, new Color(85, 0, 0))); // 하단과 오른쪽 테두리 추가
       
       int panelWidth = 950; // 패널의 예상 너비를 설정
       int panelHeight = 50;

       // Player 1의 캐릭터 로고
       JLabel player1Logo = new JLabel(new ImageIcon(gameState.getMyCharacter().getLogo()));
       player1Logo.setBounds(30, 10, panelHeight, panelHeight);
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
       player1NameLabel.setForeground(Color.black); // 검정 글씨로 이름 표시
       healthPanel.add(player1NameLabel);

       // Player 2의 캐릭터 로고
       JLabel player2Logo = new JLabel(new ImageIcon(gameState.getEnemyCharacter().getLogo()));
       player2Logo.setBounds(panelWidth - panelHeight - 10, 10, panelHeight, panelHeight);
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
       player2NameLabel.setForeground(Color.black); // 검정 글씨로 이름 표시
       healthPanel.add(player2NameLabel);
   }
   
   
   // 애니메이션 시작 메서드
   private void startAnimation() {
       animationTimer = new Timer(20, e -> {
           backgroundY -= 1; // 이동 속도 설정
           if (backgroundY <= -getHeight()) {
               backgroundY += getHeight(); // 이미지 연결
           }
           repaint(); // 패널 다시 그리기
       });
       animationTimer.start();
   }
   
   // 중앙에 위치한 가로100% 세로 60% 비율의 카드 선택 칸
   public void drawSelectCardPanel() {
       JPanel backgroundPanel = new JPanel() {
          @Override
          protected void paintComponent(Graphics g) {
              super.paintComponent(g);
              if (backgroundImage != null) {
                  int panelWidth = getWidth();
                  int panelHeight = getHeight();

                  // 이미지 반복을 위한 Y 좌표 계산
                  int currentY = backgroundY % panelHeight; // 패널 높이로 나눈 나머지

                  if (currentY > 0) {
                      currentY -= panelHeight; // 자연스럽게 이어지도록 조정
                  }

                  // 이미지를 두 번 그려서 끊김 없는 반복
                  g.drawImage(backgroundImage, 0, currentY, panelWidth, panelHeight, this);
                  g.drawImage(backgroundImage, 0, currentY + panelHeight, panelWidth, panelHeight, this);
              }
          }
       };
       
       backgroundPanel.setLayout(new BorderLayout());
       backgroundPanel.setOpaque(false);

       LinkedList<Card> cardList = gameState.getMyCharacter().getCardList();

       JPanel cardPanel = new JPanel();
       cardPanel.setLayout(new java.awt.GridLayout(1, cardList.size(), 10, 10));
       cardPanel.setBorder(BorderFactory.createEmptyBorder(125, 140, 125, 140));
       cardPanel.setOpaque(false);

       for (Card card : cardList) {
           BufferedImage cardImage = gameState.getMyCharacter().getCardImage().get(card.getName());
           ImageIcon cardIcon = cardImage != null ? new ImageIcon(cardImage) : null;

           // 카드 패널을 만들고 레이아웃을 설정
           JPanel cardContainer = new JPanel(new BorderLayout());
           cardContainer.setOpaque(false); // 투명 배경 유지
           
           // 카드 이름 라벨 추가
           JLabel cardNameLabel = new JLabel(card.getName());
           cardNameLabel.setFont(new Font("Arial", Font.BOLD, 12));
           cardNameLabel.setForeground(Color.BLACK); // 텍스트 색상
           cardNameLabel.setHorizontalAlignment(SwingConstants.CENTER); // 중앙 정렬
           cardContainer.add(cardNameLabel, BorderLayout.NORTH); // 카드 이름 라벨을 상단에 추가
           
           JButton cardButton = new JButton(cardIcon);
           cardButton.setContentAreaFilled(false);
           cardButton.setFocusPainted(false);
           cardButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

           // 카드 버튼과 카드 매핑 저장
           cardButtonMap.put(card, cardButton);
           
           // 마우스 호버 효과
           cardButton.addMouseListener(new java.awt.event.MouseAdapter() {
               @Override
               public void mouseEntered(java.awt.event.MouseEvent e) {
                   cardButton.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
               }

               @Override
               public void mouseExited(java.awt.event.MouseEvent e) {
                   cardButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
               }
           });

           // 카드 클릭 이벤트
           cardButton.addActionListener(e -> {
               if (selectedSlotIndex != -1) {
                   slotCards[selectedSlotIndex] = card;
                   JButton slotButton = slotButtons[selectedSlotIndex];
                   slotButton.setIcon(cardIcon);
                   slotButton.setText("");
                   cardContainer.setVisible(false); // 카드 컨테이너 숨김

                   switch (selectedSlotIndex) {
                       case 0 -> slot1 = card;
                       case 1 -> slot2 = card;
                       case 2 -> slot3 = card;
                   }
                   System.out.println("슬롯 " + (selectedSlotIndex + 1) + "에 카드 추가됨: " + card.getName());
                   selectedSlotIndex = -1;
               } else {
                   for (int i = 0; i < slotCards.length; i++) {
                       if (slotCards[i] == null) {
                           slotCards[i] = card;
                           JButton slotButton = slotButtons[i];
                           slotButton.setIcon(cardIcon);
                           slotButton.setText("");
                           cardContainer.setVisible(false); // 카드 컨테이너 숨김

                           switch (i) {
                               case 0 -> slot1 = card;
                               case 1 -> slot2 = card;
                               case 2 -> slot3 = card;
                           }
                           System.out.println("슬롯 " + (i + 1) + "에 카드 자동 추가됨: " + card.getName());
                           break;
                       }
                   }
               }
           });

           int damageValue = card.getValue();
           String damageText = damageValue > 0 ? "DM " + damageValue : "DM 00";
           JLabel damageLabel = new JLabel(damageText);
           damageLabel.setFont(new Font("Arial", Font.BOLD, 12));
           damageLabel.setForeground(Color.RED);
           damageLabel.setHorizontalAlignment(SwingConstants.CENTER);

           cardContainer.add(cardButton, BorderLayout.CENTER);
           cardContainer.add(damageLabel, BorderLayout.SOUTH);
           
           // 카드와 컨테이너 매핑 저장
           cardContainerMap.put(card, cardContainer);
           
           cardPanel.add(cardContainer);
       }

       backgroundPanel.add(cardPanel, BorderLayout.CENTER);
       selectCardPanel.setLayout(new BorderLayout());
       selectCardPanel.add(backgroundPanel, BorderLayout.CENTER);
       selectCardPanel.setBorder(BorderFactory.createLineBorder(new Color(85, 0, 0), 4));
       add(selectCardPanel, BorderLayout.CENTER);

       
       
       selectCardPanel.revalidate();
       selectCardPanel.repaint();
   }




   
   // 왼쪽 하단에 위치한 가로40% 세로30% 비율의 필드 확인 칸
   public void drawFieldPanel() {
       fieldPanel.setBackground(new Color(236, 237, 215));
       fieldPanel.setLayout(new BorderLayout());
       fieldPanel.setBorder(BorderFactory.createMatteBorder(0, 4, 4, 0, new Color(85, 0, 0))); // 테두리 추가
       
       JPanel slotContainer = new JPanel();
       slotContainer.setOpaque(false);
       slotContainer.setLayout(new java.awt.GridLayout(1, 3, 10, 10));
       slotContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

       slotButtons = new JButton[3]; // 슬롯 버튼 배열 초기화
       
       for (int i = 0; i < slotButtons.length; i++) {
           int index = i;

           JButton slotButton = new JButton("<html><center>" +
                   (index + 1) + "<br>PLACE CARD HERE</center></html>");
           slotButton.setHorizontalTextPosition(SwingConstants.CENTER);
           slotButton.setVerticalTextPosition(SwingConstants.CENTER);
           slotButton.setFont(new Font("Arial", Font.PLAIN, 10));
           slotButton.setPreferredSize(new Dimension(100, 150));
           slotButton.setContentAreaFilled(false);
           slotButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

           // 슬롯 클릭 이벤트
           slotButton.addActionListener(e -> {
               if (slotCards[index] != null) {
                  // 슬롯에서 카드 제거
                   Card removedCard = slotCards[index];
                   // 슬롯에 카드가 있으면 삭제
                   System.out.println("슬롯 " + (index + 1) + "에서 카드 삭제됨");
                   slotCards[index] = null;
                   slotButton.setIcon(null);
                   slotButton.setText("<html><center>" +
                           (index + 1) + "<br>PLACE CARD HERE</center></html>");
                   // 카드 컨테이너 다시 표시
                   JPanel correspondingContainer = cardContainerMap.get(removedCard);
                   if (correspondingContainer != null) {
                       correspondingContainer.setVisible(true);
                   }
                   switch (index) {
                       case 0 -> slot1 = null;
                       case 1 -> slot2 = null;
                       case 2 -> slot3 = null;
                   }
                   selectedSlotIndex = -1;
               } else {
                   // 빈 슬롯 선택
                   selectedSlotIndex = index;
                   System.out.println("슬롯 " + (index + 1) + " 선택됨");
               }
           });

           // 슬롯 마우스 호버 효과 추가
           slotButton.addMouseListener(new java.awt.event.MouseAdapter() {
               @Override
               public void mouseEntered(java.awt.event.MouseEvent e) {
                   slotButton.setBorder(BorderFactory.createLineBorder(Color.RED, 2)); // 호버 시 파란 테두리
               }

               @Override
               public void mouseExited(java.awt.event.MouseEvent e) {
                   slotButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // 원래 테두리 복원
               }
           });

           slotButtons[index] = slotButton;
           slotContainer.add(slotButton);
       }

       fieldPanel.add(slotContainer, BorderLayout.CENTER);
       fieldPanel.setPreferredSize(new Dimension(
               (int) (gameState.getDimension().getWidth() * 4 / 10),
               (int) (gameState.getDimension().getHeight() * 3 / 10)
       ));
       
       
   }









   
   // 중앙하단에 위치한 가로20% 세로30% 비율의 카드선택완료버튼, 도움말버튼, 카드초기화버튼 등
   public void drawButtonPanel() {
       buttonPanel.setBackground(new Color(236, 237, 215));
       buttonPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, new Color(85, 0, 0))); // 테두리 추가
       
       // 버튼 위에 들어갈 라벨 생성 (줄바꿈 처리)
       JLabel instructionLabel = new JLabel("<html><center>SELECT THREE CARDS<br>TO CREATE A COMBO!</center></html>");
       instructionLabel.setFont(new Font("Arial", Font.BOLD, 12)); // 폰트 설정
       instructionLabel.setForeground(new Color(0, 0, 0, 150)); // 글씨 색상 (투명도 설정)
       instructionLabel.setHorizontalAlignment(SwingConstants.CENTER); // 중앙 정렬
       instructionLabel.setOpaque(false); // 투명 배경 유지
       
       // 라벨에 위쪽 마진 추가
       instructionLabel.setBorder(BorderFactory.createEmptyBorder(55, 0, 0, 0)); // 위쪽 마진 10px
       
       // READY 버튼
       JButton readyButton = new JButton("READY");
       readyButton.setFont(new Font("Arial", Font.BOLD, 14)); // 텍스트 폰트 설정
       readyButton.setForeground(Color.WHITE); // 글씨 색상 (흰색)

       // 배경 색상 적용
       readyButton.setBackground(new Color(90, 90, 75)); // 버튼 배경색 설정
       readyButton.setOpaque(true); // 배경색이 보이게 설정

       // 테두리 설정 (검은색 테두리)
       readyButton.setBorder(BorderFactory.createCompoundBorder(
           BorderFactory.createLineBorder(new Color(40, 40, 30), 3), // 외곽 테두리
           BorderFactory.createEmptyBorder(5, 10, 5, 10) // 내부 여백
       ));

       // 마우스 호버 효과
       readyButton.addMouseListener(new java.awt.event.MouseAdapter() {
           @Override
           public void mouseEntered(java.awt.event.MouseEvent e) {
               readyButton.setBackground(new Color(70, 70, 60)); // 호버 시 더 어두운 배경색
           }

           @Override
           public void mouseExited(java.awt.event.MouseEvent e) {
               readyButton.setBackground(new Color(90, 90, 75)); // 원래 색상 복원
           }
       });

       // 버튼의 기본 스타일 제거
       readyButton.setFocusPainted(false); // 포커스 테두리 제거
       readyButton.setContentAreaFilled(true); // 내용 채우기 활성화
       readyButton.setBorderPainted(true); // 테두리 활성화
       readyButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               // 슬롯 변수(slot1, slot2, slot3)에 모두 카드가 있어야 전송 가능
               if (slot1 == null || slot2 == null || slot3 == null) {
                   System.out.println("모든 슬롯에 카드를 선택해야 합니다.");
                   return;
               }

               // 슬롯 변수를 LinkedList로 정리
               LinkedList<Card> finalCardList = new LinkedList<>();
               finalCardList.add(slot1); // 슬롯 1 카드
               finalCardList.add(slot2); // 슬롯 2 카드
               finalCardList.add(slot3); // 슬롯 3 카드

               // 선택된 카드 리스트를 게임 상태에 저장하고 서버로 전송
               gameState.setSelectedCardList(finalCardList);
               networkManager.sendCardSelection();

               System.out.println("선택된 카드 리스트가 서버로 전송되었습니다: " + finalCardList);

               // READY 버튼 비활성화
               readyButton.setEnabled(false);
           }
       });
       
       
       
       // READY 버튼 추가
       buttonPanel.add(instructionLabel);
       buttonPanel.add(readyButton);
       
       buttonPanel.setPreferredSize(new Dimension(
               (int) (gameState.getDimension().getWidth() * 2 / 10),
               (int) (gameState.getDimension().getHeight() * 3 / 10)
       ));
      

   }
   
   
   
   // 우측하단에 위치한 가로40% 세로30% 비율의 선택된 카드 보여주는칸
   public void drawSelectedCardPanel() {
      
      selectedCardPanel.setBackground(new Color(236, 237, 215));
      selectedCardPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 4, new Color(85, 0, 0))); // 하단과 오른쪽 테두리 추가
      selectedCardPanel.add(new JLabel("<selected card 1, 2, 3>"));
      selectedCardPanel.setPreferredSize(new Dimension((int)(gameState.getDimension().getWidth() * 4 / 10)
                                          , (int)(gameState.getDimension().getHeight() * 3 / 10)));
      
      
      selectedCardPanel.setLayout(null);

      int width = selectedCardPanel.getWidth();
      int height = selectedCardPanel.getHeight();
      
       int buttonWidth = 40;
       int buttonHeight = 100;
       int gap = 20;
       
       
       for (int i = 0; i < 3; i++) {
          
          final int index = i;
          JButton selectedCardButton;
          
          if (selectedCardList.get(i) == null) {
             selectedCardButton = new JButton("null");
             selectedCardButton.setEnabled(false);
          } else {
             selectedCardButton = new JButton(selectedCardList.get(i).getName());
             selectedCardButton.setBounds((int) (width * 0.1) + i * (buttonWidth + gap), (int) (height * 0.1), buttonWidth, buttonHeight);
             selectedCardButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        selectedCardList.set(index, null);
                        revalidate();
                        repaint();
                    }
                });
          }
          selectedCardPanel.add(selectedCardButton);
       }
       revalidate();
        repaint();
   }
   
   
}