// EndGameScreen.java

package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.io.FileInputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import controller.GameController;
import javazoom.jl.player.Player;
import model.GameState;
import network.NetworkManager;

public class EndGameScreen extends JPanel {

	private MainFrame mainFrame;
	private GameState gameState;
	private GameController gameController;
	private NetworkManager networkManager;
	
	public EndGameScreen(MainFrame mainFrame, GameState gameState, GameController gameController, NetworkManager networkManager) {
		
		this.mainFrame = mainFrame;
		this.gameState = gameState;
		this.gameController = gameController;
		this.networkManager = networkManager;
		
		setLayout(new BorderLayout());

	    drawResultPanel();
	
	}
	

	public void drawResultPanel() {
	    JLabel backgroundLabel = new JLabel();
	    backgroundLabel.setLayout(new BorderLayout()); // 레이아웃 설정

	 // 결과 텍스트 설정
	    JLabel resultLabel = new JLabel() {
	        @Override
	        protected void paintComponent(Graphics g) {
	            // 텍스트 커스터마이징
	            String text = getText();
	            if (text != null) {
	                g.setFont(new Font("궁서", Font.BOLD, 64));
	                // 그림자 효과
	                g.setColor(java.awt.Color.BLACK);
	                int textWidth = g.getFontMetrics().stringWidth(text);
	                int textHeight = g.getFontMetrics().getHeight();
	                int x = (getWidth() - textWidth) / 2;
	                int y = (getHeight() + textHeight) / 2 - 10; // 텍스트 중앙 배치
	                g.drawString(text, x + 3, y + 3); // 그림자 위치
	                // 텍스트 색상
	                g.setColor(java.awt.Color.YELLOW);
	                g.drawString(text, x, y); // 텍스트 위치
	            }
	        }
	    };

	    resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    resultLabel.setVerticalAlignment(SwingConstants.CENTER);

	    // 게임 결과에 따라 텍스트 설정
	    if (gameState.getMyHealth() <= 0 && gameState.getEnemyHealth() <= 0) {
	        backgroundLabel.setIcon(new ImageIcon("res/img/무승부.png")); // 무승부 이미지 경로
	        gameController.playBGM("res/sound/bgm/무승부_브금.mp3");
	        resultLabel.setText("DRAW");
	    } else if (gameState.getMyHealth() <= 0) {
	        backgroundLabel.setIcon(new ImageIcon("res/img/패배.png")); // 패배 이미지 경로
	        gameController.playBGM("res/sound/bgm/패배_브금.mp3");
	        new Thread(() -> {
	            try (FileInputStream fis = new FileInputStream("res/sound/sfx/패배효과음.mp3")) {
	                new Player(fis).play();
	            } catch (Exception e) {
	                System.err.println("Error playing sound: " + e.getMessage());
	            }
	        }).start();
	        resultLabel.setText("LOSE");
	    } else if (gameState.getEnemyHealth() <= 0) {
	        backgroundLabel.setIcon(new ImageIcon("res/img/승리.jpg")); // 승리 이미지 경로
	        gameController.playBGM("res/sound/bgm/승리_브금.mp3");
	        new Thread(() -> {
	            try (FileInputStream fis = new FileInputStream("res/sound/sfx/승리효과음.mp3")) {
	                new Player(fis).play();
	            } catch (Exception e) {
	                System.err.println("Error playing sound: " + e.getMessage());
	            }
	        }).start();
	        resultLabel.setText("WIN");
	    }

	    // 텍스트를 배경 이미지 위에 추가
	    backgroundLabel.add(resultLabel, BorderLayout.CENTER);

	    // RESTART 버튼 설정
	    JButton restartButton = new JButton("RESTART") {
	        @Override
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);
	            if (getModel().isRollover()) { // 마우스가 올라간 상태
	                g.setColor(java.awt.Color.WHITE); // 밑줄 색상
	                int y = getHeight() - 3; // 버튼의 아래쪽에서 3px 위에 밑줄 위치
	                g.fillRect(0, y, getWidth(), 2); // 밑줄 그리기
	            }
	        }
	    };

	    // 버튼 스타일 설정
	    restartButton.setFont(new Font("궁서", Font.BOLD, 24));
	    restartButton.setForeground(java.awt.Color.WHITE);  // 텍스트 색상
	    restartButton.setFocusPainted(false);              // 포커스 테두리 제거
	    restartButton.setContentAreaFilled(false);         // 버튼 배경 제거
	    restartButton.setBorderPainted(false);             // 버튼 테두리 제거
	    restartButton.setOpaque(false);                    // 완전히 투명

	    // 기존 리스너 유지
	    restartButton.addActionListener(e -> {
	        gameController.isConnected = false;
	        // 소켓 닫기
	        networkManager.closeSocket();

	        // 게임 컨트롤러 초기화
	        gameController.restartProgram();

	        // 화면 갱신
	        mainFrame.revalidate();
	        mainFrame.repaint();
	    });

	    // 새로운 패널 생성
	    JPanel buttonPanel = new JPanel();
	    buttonPanel.setOpaque(false); // 배경 투명
	    buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 100)); // 중앙 정렬 및 하단 마진
	    buttonPanel.add(restartButton);

	    // 버튼 패널을 배경 이미지 아래쪽에 추가
	    backgroundLabel.add(buttonPanel, BorderLayout.SOUTH);

	    // 배경 라벨을 패널에 추가
	    add(backgroundLabel, BorderLayout.CENTER);
	}
	
}
