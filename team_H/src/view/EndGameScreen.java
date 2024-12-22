// EndGameScreen.java

package view;

import java.awt.BorderLayout;
import java.awt.Font;
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

	    JLabel resultLabel = new JLabel();
	    resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    resultLabel.setVerticalAlignment(SwingConstants.CENTER);
	    resultLabel.setFont(new Font("Arial", Font.BOLD, 64)); // 텍스트 크기 조정
	    resultLabel.setForeground(java.awt.Color.WHITE); // 텍스트 색상 설정

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

	    // 버튼을 배경 이미지 위에 추가
	    JButton restartButton = new JButton("RESTART");
	    restartButton.setFont(new Font("Arial", Font.BOLD, 24));
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

	    // 버튼은 배경 이미지 아래쪽에 위치
	    backgroundLabel.add(restartButton, BorderLayout.SOUTH);

	    // 배경 라벨을 패널에 추가
	    add(backgroundLabel, BorderLayout.CENTER);
	}
	
}
