// SelectCardScreen.java

package view;

import java.util.List;

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

import javax.swing.Timer;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import controller.GameController;
import model.Card;
import model.GameState;

public class SelectCardScreen extends JPanel {
	
	private GameState gameState;
	private GameController gameController;
	
	private JPanel healthPanel = new JPanel();
	private JPanel selectCardPanel = new JPanel();
	private JPanel fieldPanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	private JPanel selectedCardPanel = new JPanel();
	private Image backgroundImage; // 배경 이미지 객체
	private int backgroundY; // 이미지의 y 좌표
    private Timer animationTimer; // 애니메이션 타이머
    private Card[] slotCards = new Card[3]; // 슬롯별 카드 상태를 저장
    private int selectedSlotIndex = -1; // 선택된 슬롯 인덱스 (-1은 슬롯이 선택되지 않은 상태를 의미)

    
	public SelectCardScreen(GameState gameState, GameController gameController) {
		
		this.gameState = gameState;
		this.gameController = gameController;
		// 배경 이미지 로드
        backgroundImage = new ImageIcon("res/img/카드선택_배경화면.png").getImage();
        backgroundY = 0; // 초기 Y 위치
        startAnimation(); // 애니메이션 시작
        
		splitPanel();
		drawHealthPanel();
		drawSelectCardPanel();
		drawFieldPanel();
		drawButtonPanel();
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
		
		healthPanel.setBackground(Color.cyan);
		healthPanel.add(new JLabel("<player1 health bar>  <turn>  <player1 health bar>"));
		healthPanel.setPreferredSize(new Dimension((int)gameState.getDimension().getWidth()
				, (int)(gameState.getDimension().getHeight() * 1 / 10)));
	}
	// 애니메이션 시작 메서드
    private void startAnimation() {
	        animationTimer = new Timer(20, e -> {
	            // y 좌표 업데이트
	            backgroundY -= 1; // 위로 이동 (속도는 1 픽셀)
	
	            // 화면 밖으로 나가면 다시 아래로 위치시킴
	            if (backgroundY + getHeight() < 0) {
	                backgroundY = getHeight();
	            }
	
	            // 패널 다시 그리기
	            repaint();
	        });
        animationTimer.start();
    }
    // 중앙에 위치한 가로100% 세로 60% 비율의 카드선택칸
    public void drawSelectCardPanel() {
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                int panelWidth = getWidth();
                int panelHeight = getHeight();

                g.drawImage(backgroundImage, 0, backgroundY, panelWidth, panelHeight, this);
                g.drawImage(backgroundImage, 0, backgroundY - panelHeight, panelWidth, panelHeight, this);
            }
        };

        animationTimer = new Timer(40, e -> {
            int panelHeight = backgroundPanel.getHeight();
            backgroundY -= 0.2;
            if (backgroundY <= 0) {
                backgroundY = panelHeight;
            }
            backgroundPanel.repaint();
        });
        animationTimer.start();

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new java.awt.GridLayout(1, 4, 10, 10));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100)); // 상단, 하단, 좌측, 우측 마진 설정
        cardPanel.setOpaque(false);

        if (gameState.getMyCharacter() != null) {
            List<Card> commonCards = gameState.getMyCharacter().getCommonCards();

            for (Card card : commonCards) {
                String imagePath = gameState.getMyCharacter().getCardImagePath(card.getName());
                ImageIcon cardIcon = new ImageIcon(imagePath);

                // 카드 이름, 버튼, 하단 정보를 포함하는 패널 생성
                JPanel cardContainer = new JPanel();
                cardContainer.setLayout(new BorderLayout());
                cardContainer.setOpaque(false); // 배경 투명

                // 카드 이름 레이블
                JLabel cardLabel = new JLabel(card.getName(), JLabel.CENTER);
                cardLabel.setForeground(Color.BLACK); // 텍스트 색상 설정
                cardLabel.setFont(new Font("Arial", Font.BOLD, 14)); // 폰트 설정

                // 카드 버튼 생성
                JButton cardButton = new JButton(cardIcon) {
                    @Override
                    public Dimension getPreferredSize() {
                        return new Dimension(cardIcon.getIconWidth(), cardIcon.getIconHeight());
                    }
                };
                cardButton.setContentAreaFilled(false);
                cardButton.setFocusPainted(false);
                cardButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

                // 마우스 호버 시 테두리 효과
                cardButton.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        cardButton.setBorder(BorderFactory.createLineBorder(Color.RED, 2)); // 호버 시 빨간 테두리
                    }

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        cardButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // 원래 테두리
                    }
                });

                // 카드 클릭 시 슬롯에 추가
                cardButton.addActionListener(e -> {
                    System.out.println(card.getName());
                    if (selectedSlotIndex != -1) {
                        JPanel slotContainer = (JPanel) fieldPanel.getComponent(0);
                        JButton slotButton = (JButton) slotContainer.getComponent(selectedSlotIndex);

                        if (slotCards[selectedSlotIndex] == null) {
                            slotCards[selectedSlotIndex] = card;
                            slotButton.setIcon(new ImageIcon(gameState.getMyCharacter().getCardImagePath(card.getName())));
                            gameState.getSelectedCardList().add(card);
                            System.out.println("카드 추가 " + (selectedSlotIndex + 1));
                            selectedSlotIndex = -1;
                        } else {
                            System.out.println("Slot " + (selectedSlotIndex + 1) + " is already occupied.");
                        }
                    } else {
                        boolean cardAdded = false;
                        for (int i = 0; i < slotCards.length; i++) {
                            if (slotCards[i] == null) {
                                JPanel slotContainer = (JPanel) fieldPanel.getComponent(0);
                                JButton slotButton = (JButton) slotContainer.getComponent(i);

                                slotCards[i] = card;
                                slotButton.setIcon(new ImageIcon(gameState.getMyCharacter().getCardImagePath(card.getName())));
                                gameState.getSelectedCardList().add(card);
                                System.out.println("카드 추가 " + (i + 1));
                                cardAdded = true;
                                break;
                            }
                        }
                        if (!cardAdded) {
                            System.out.println("꽉참.");
                        }
                    }
                });

                // 하단 데미지 및 영역 정보 패널 생성
                JPanel cardBottomPanel = new JPanel(new BorderLayout());
                cardBottomPanel.setOpaque(false);

                // 데미지 텍스트
                String damageText = "DM " + (card.getDamage() > 0 ? card.getDamage() : "00");
                JLabel damageLabel = new JLabel(damageText, JLabel.CENTER);
                damageLabel.setForeground(Color.RED);
                damageLabel.setFont(new Font("Arial", Font.BOLD, 12));

                // 영역 이미지
                String areaImagePath = "res/areas/area_icon.png"; // 예제: 영역 이미지 경로
                ImageIcon areaIcon = new ImageIcon(areaImagePath);
                JLabel areaLabel = new JLabel(areaIcon, JLabel.CENTER);

                // 하단 패널에 추가
                cardBottomPanel.add(damageLabel, BorderLayout.WEST); // 왼쪽에 데미지
                cardBottomPanel.add(areaLabel, BorderLayout.CENTER); // 중앙에 영역 이미지

                // 레이블, 버튼, 하단 정보를 카드 컨테이너에 추가
                cardContainer.add(cardLabel, BorderLayout.NORTH); // 상단에 카드 이름
                cardContainer.add(cardButton, BorderLayout.CENTER); // 중앙에 카드 버튼
                cardContainer.add(cardBottomPanel, BorderLayout.SOUTH); // 하단에 데미지와 영역 이미지

                // 카드 컨테이너를 cardPanel에 추가
                cardPanel.add(cardContainer);
            }
        } else {
            cardPanel.add(new JLabel("슈퍼맨만 구현함."));
        }

        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.add(cardPanel, BorderLayout.CENTER);
        selectCardPanel = backgroundPanel;
        add(selectCardPanel, BorderLayout.CENTER);
    }


	
	// 왼쪽하단에 위치한 가로40% 세로30% 비율의 필드확인칸
    public void drawFieldPanel() {
        fieldPanel.setBackground(Color.green);
        fieldPanel.setLayout(new BorderLayout());
        JPanel slotContainer = new JPanel();
        slotContainer.setOpaque(false);
        slotContainer.setLayout(new java.awt.GridLayout(1, 3, 10, 10));
        slotContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton[] slotButtons = new JButton[3];
        for (int i = 0; i < slotButtons.length; i++) {
            int index = i;

            // 버튼 생성
            JButton slotButton = new JButton("<html><center>" +
                    (i + 1) + "<br>PLACE CARD HERE</center></html>");
            slotButton.setHorizontalTextPosition(SwingConstants.CENTER);
            slotButton.setVerticalTextPosition(SwingConstants.CENTER);
            slotButton.setFont(new Font("Arial", Font.PLAIN, 10));
            slotButton.setPreferredSize(new Dimension(100, 150));
            slotButton.setContentAreaFilled(false);
            slotButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

            // 슬롯 클릭 이벤트
            slotButton.addActionListener(e -> {
                if (slotCards[index] != null) {
                    // 슬롯에 카드가 있으면 제거
                    System.out.println("카드 삭제 " + (index + 1));
                    gameState.getSelectedCardList().remove(slotCards[index]); // 상태에서 제거
                    slotCards[index] = null; // 슬롯 비우기
                    slotButton.setIcon(null); // 슬롯 이미지 제거
                    slotButton.setText("<html><center>" +
                            (index + 1) + "<br>PLACE CARD HERE</center></html>"); // 텍스트 복원
                } else {
                    // 슬롯이 비어 있으면 선택 상태로 설정
                    selectedSlotIndex = index;
                    System.out.println("슬롯 " + (index + 1) + " 선택됨.");
                }
            });

            // 마우스 호버 이벤트 추가
            slotButton.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    slotButton.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2)); // 호버 시 파란 테두리
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    slotButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // 원래 테두리
                }
            });

            slotButtons[i] = slotButton;
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
		
		buttonPanel.setBackground(Color.red);
		Button continueButton = new Button("ready");
		continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameController.handleAction("FIGHT");
            }
        });
		buttonPanel.add(continueButton);
		buttonPanel.setPreferredSize(new Dimension((int)(gameState.getDimension().getWidth() * 2 / 10)
														, (int)(gameState.getDimension().getHeight() * 3 / 10)));
	}
	
	// 우측하단에 위치한 가로40% 세로30% 비율의 선택된카드보여주는칸
	public void drawSelectedCardPanel() {
		
		selectedCardPanel.setBackground(Color.white);
		selectedCardPanel.add(new JLabel("<selected card 1, 2, 3>"));
		selectedCardPanel.setPreferredSize(new Dimension((int)(gameState.getDimension().getWidth() * 4 / 10)
														, (int)(gameState.getDimension().getHeight() * 3 / 10)));
	}
	
	
}