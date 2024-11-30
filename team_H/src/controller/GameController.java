// GameController.java

package controller;

import javax.swing.JPanel;

import model.GameState;
import network.NetworkManager;
import view.EndGameScreen;
import view.MainFrame;
import view.PlayingGameScreen;
import view.SelectCardScreen;
import view.SelectCharacterScreen;
import view.StartGameScreen;

public class GameController {
	
	private MainFrame mainFrame = null;
	private GameState gameState = null;
	private NetworkManager networkManager = null;
	
	
	public GameController() {
		
		start();
	}
	
	
	// 프로그램 켰을 때 동작 !!
	public void start() {
		mainFrame = new MainFrame();
		gameState = new GameState(mainFrame.getSize());
		showStartGameScreen();
		mainFrame.setVisible(true);
		networkManager = new NetworkManager();
	}
	
	
	// 각 스크린을 mainFrame에 그리게 하는 동작 !!
	public void showStartGameScreen() {
        mainFrame.setScreen(new StartGameScreen(gameState, this));
    }
	public void showSelectCharacterScreen() {
		mainFrame.setScreen(new SelectCharacterScreen(gameState, this));
	}
	public void showPlayingGameScreen() {
		mainFrame.setScreen(new PlayingGameScreen(gameState));
	}
	public void showSelectCardScreen() {
		mainFrame.setScreen(new SelectCardScreen(gameState, this));
	}
	public void showEndGameScreen() {
		mainFrame.setScreen(new EndGameScreen(gameState));
	}
	
	
	
	// 스크린에서 발생한 이벤트를 감지하여 처리합니다 !!
	// 현재는 예시 코드
    public void handleAction(String action) {
        switch (action) {
            case "START_GAME":
            	showSelectCharacterScreen();
                break;
            case "PLAY_GAME":
            	showSelectCardScreen();
                break;
            case "FIGHT":
            	showPlayingGameScreen();
                break;
            default:
                System.out.println("ㅈ비상");
        }
    }
}
