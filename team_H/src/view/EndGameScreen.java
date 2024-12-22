// EndGameScreen.java

package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.FileInputStream;

import javax.swing.ImageIcon;
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
	    drawRestartButtonPanel();
	}
	
	
	
	public void drawResultPanel() {
		 JLabel resultLabel = new JLabel();
	        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);

	        if (gameState.getMyHealth() <= 0 && gameState.getEnemyHealth() <= 0) {
	            resultLabel.setIcon(new ImageIcon("res/img/무승부.jpg")); // 무승부 이미지 경로
	            
	            gameController.playBGM("res/sound/bgm/무승부_브금.mp3"); // 캐릭터 선택창 브금
	            new Thread(() -> {
	                try (FileInputStream fis = new FileInputStream("res/sound/sfx/무승부.mp3")) {
	                    new Player(fis).play(); // FileInputStream을 바로 사용
	                } catch (Exception e) {
	                    System.err.println("Error playing sound: " + e.getMessage());
	                }
	            }).start();
	            resultLabel.setText("무승부");
	        } else if (gameState.getMyHealth() <= 0) {
	            resultLabel.setIcon(new ImageIcon("res/img/패배.jpg")); // 패배 이미지 경로
	            gameController.playBGM("res/sound/bgm/패배_브금.mp3"); // 캐릭터 선택창 브금
	            new Thread(() -> {
	                try (FileInputStream fis = new FileInputStream("res/sound/sfx/패배효과음.mp3")) {
	                    new Player(fis).play(); // FileInputStream을 바로 사용
	                } catch (Exception e) {
	                    System.err.println("Error playing sound: " + e.getMessage());
	                }
	            }).start();
	            resultLabel.setText("패배");
	        } else if (gameState.getEnemyHealth() <= 0) {
	            resultLabel.setIcon(new ImageIcon("res/img/승리.jpg")); // 승리 이미지 경로
	            gameController.playBGM("res/sound/bgm/승리_브금.mp3"); // 캐릭터 선택창 브금
	            new Thread(() -> {
	                try (FileInputStream fis = new FileInputStream("res/sound/sfx/승리효과음.mp3")) {
	                    new Player(fis).play(); // FileInputStream을 바로 사용
	                } catch (Exception e) {
	                    System.err.println("Error playing sound: " + e.getMessage());
	                }
	            }).start();
	            resultLabel.setText("승리");
	        }

	        resultLabel.setFont(new Font("Arial", Font.BOLD, 24));
	        resultLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
	        resultLabel.setHorizontalTextPosition(SwingConstants.CENTER);

	        add(resultLabel, BorderLayout.CENTER);
	}
	
	
	public void drawRestartButtonPanel() {
		
		// networkManager.closeSocket(); 이것도 버튼 누를때 같이 실행해야됌 ㅋ
		// gameController = new GameController(); 이거 하면 재시작함. 버튼 누르면 실행되게 할 것
	}
}
