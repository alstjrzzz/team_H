// EndGameScreen.java

package view;

import javax.swing.JPanel;

import controller.GameController;
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
		
		// drawResultPanel();
		// drawRestartButtonPanel();
	}
	
	
	
	public void drawResultPanel() {
		// 망재앙 스크린 그려줘잉
		if (gameState.getMyHealth() <= 0 && gameState.getEnemyHealth() <= 0) {
			System.out.println("무승부");
		} else if (gameState.getMyHealth() <= 0) {
			System.out.println("패배");
		} else if (gameState.getEnemyHealth() <= 0) {
			System.out.println("승리");
		}
	}
	
	
	public void drawRestartButtonPanel() {
		
		// gameController = new GameController(); 이거 하면 재시작함. 버튼 누르면 실행되게 할 것
	}
}
