// SelectCardScreen.java

package view;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
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

import controller.GameController;
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
        // 배경을 가진 새로운 패널 생성
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // 패널의 높이와 너비를 가져옴
                int panelWidth = getWidth();
                int panelHeight = getHeight();

                // 배경 이미지를 두 번 그려 빈 공간 방지
                g.drawImage(backgroundImage, 0, backgroundY, panelWidth, panelHeight, this);
                g.drawImage(backgroundImage, 0, backgroundY - panelHeight, panelWidth, panelHeight, this);
            }
        };

        // 애니메이션 타이머에서 `selectCardPanel`의 크기 기반으로 처리
        animationTimer = new Timer(40, e -> { // 속도 조정
            // 패널 높이를 가져옴
            int panelHeight = backgroundPanel.getHeight();

            // y 좌표 업데이트
            backgroundY -= 0.2; // 위로 1픽셀 이동

            // 화면 밖으로 나가면 다시 아래로 위치시킴
            if (backgroundY <= 0) {
                backgroundY = panelHeight;
            }

            // 패널 다시 그리기
            backgroundPanel.repaint();
        });

        animationTimer.start();

        backgroundPanel.setLayout(new BorderLayout()); // 카드 선택용 레이아웃 설정
        backgroundPanel.setPreferredSize(new Dimension(
                (int) gameState.getDimension().getWidth(),
                (int) (gameState.getDimension().getHeight() * 6 / 10)
        ));

        // 카드 선택 레이블 추가
        backgroundPanel.add(new JLabel("<select cards>"), BorderLayout.CENTER);
        selectCardPanel = backgroundPanel; // 기존 selectCardPanel 대체
        add(selectCardPanel, BorderLayout.CENTER); // 중앙에 추가
    }
	
	// 왼쪽하단에 위치한 가로40% 세로30% 비율의 필드확인칸
    public void drawFieldPanel() {
        fieldPanel.setBackground(Color.green); // 전체 배경 설정
        fieldPanel.setLayout(new BorderLayout()); // 레이아웃 설정
        JPanel buttonContainer = new JPanel(); // 버튼을 배치할 컨테이너
        buttonContainer.setOpaque(false); // 버튼 배경과 통일되도록 투명하게 설정
        buttonContainer.setLayout(new java.awt.GridLayout(1, 3, 10, 0)); // 가로로 버튼 3개 배치
        
        // 상단 패딩 추가
        buttonContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // 버튼 배열 생성 (테두리 관리를 위해)
        JButton[] buttons = new JButton[3];

        // 첫 번째 버튼
        JButton button1 = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // 배경 색상
                g.setColor(new Color(200, 200, 200, 150)); // 흐린 배경색 (투명도 적용)
                g.fillRect(0, 0, getWidth(), getHeight());

                // 텍스트 렌더링
                g.setColor(new Color(100, 100, 100, 120)); // 흐릿한 텍스트 색상 (투명도 적용)
                g.setFont(g.getFont().deriveFont(30f)); // 텍스트 크기 조정
                String text = "01"; // 텍스트 설정
                int textWidth = g.getFontMetrics().stringWidth(text);
                int textHeight = g.getFontMetrics().getHeight();
                g.drawString(text, (getWidth() - textWidth) / 2, (getHeight() + textHeight / 3) / 2);
            }
        };
        buttons[0] = button1;

        // 두 번째 버튼
        JButton button2 = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // 배경 색상
                g.setColor(new Color(200, 200, 200, 150)); // 흐린 배경색 (투명도 적용)
                g.fillRect(0, 0, getWidth(), getHeight());

                // 텍스트 렌더링
                g.setColor(new Color(100, 100, 100, 120)); // 흐릿한 텍스트 색상 (투명도 적용)
                g.setFont(g.getFont().deriveFont(30f)); // 텍스트 크기 조정
                String text = "02"; // 텍스트 설정
                int textWidth = g.getFontMetrics().stringWidth(text);
                int textHeight = g.getFontMetrics().getHeight();
                g.drawString(text, (getWidth() - textWidth) / 2, (getHeight() + textHeight / 3) / 2);
            }
        };
        buttons[1] = button2;

        // 세 번째 버튼
        JButton button3 = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // 배경 색상
                g.setColor(new Color(200, 200, 200, 150)); // 흐린 배경색 (투명도 적용)
                g.fillRect(0, 0, getWidth(), getHeight());

                // 텍스트 렌더링
                g.setColor(new Color(100, 100, 100, 120)); // 흐릿한 텍스트 색상 (투명도 적용)
                g.setFont(g.getFont().deriveFont(30f)); // 텍스트 크기 조정
                String text = "03"; // 텍스트 설정
                int textWidth = g.getFontMetrics().stringWidth(text);
                int textHeight = g.getFontMetrics().getHeight();
                g.drawString(text, (getWidth() - textWidth) / 2, (getHeight() + textHeight / 3) / 2);
            }
        };
        buttons[2] = button3;

        // 버튼 공통 설정 및 클릭 이벤트
        for (int i = 0; i < buttons.length; i++) {
            JButton button = buttons[i];
            int index = i; // i 값을 고정
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false); // 기본 테두리 비활성화
            button.setFocusPainted(false); // 포커스 테두리 비활성화
            button.setPreferredSize(new Dimension(80, 80));

            // 클릭 이벤트: 테두리 동적 설정
            button.addActionListener(e -> {
                for (JButton btn : buttons) {
                    btn.setBorderPainted(false); // 모든 버튼 테두리 제거
                }
                button.setBorderPainted(true); // 클릭된 버튼 테두리 활성화
                button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // 테두리 스타일
                System.out.println("Button " + (index + 1) + " clicked"); // 고정된 index 사용
            });

            buttonContainer.add(button); // 버튼 추가
        }

        // 버튼 컨테이너를 중앙에 추가
        fieldPanel.add(buttonContainer, BorderLayout.CENTER);

        // 필드 패널 크기 설정
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