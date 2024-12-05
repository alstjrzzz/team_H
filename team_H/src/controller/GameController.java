// GameController.java

package controller;

import java.io.IOException;
import java.util.LinkedList;

import model.Character.Character;
import model.Character.ActionMan;

import javax.swing.JPanel;

import org.json.JSONObject;

import model.Card;
import model.GameState;
import network.NetworkManager;
import view.EndGameScreen;
import view.MainFrame;
import view.PlayingGameScreen;
import view.SelectCardScreen;
import view.SelectCharacterScreen;
import view.StartGameScreen;

public class GameController {
	
	private MainFrame mainFrame;
	private GameState gameState;
	private NetworkManager networkManager;
	
	public GameController() {
		
		startProgram();
		startGame();
	}
	
	
	// 프로그램 켰을 때 동작 !!
	private void startProgram() {
		mainFrame = new MainFrame();
		gameState = new GameState(mainFrame.getSize());
		showStartGameScreen();
		mainFrame.setVisible(true);
		networkManager = new NetworkManager(gameState, this);
	}
	
	
	// 게임 진행 로직 !!
	private void startGame() {
		
		selectCharacter();
		
		while (true) {
			
			selectCard();
			fight();
			if (isGameOver()) gameOver();
		}
	}
	
	
	// 각 스크린을 mainFrame에 그리게 하는 동작 !!
	public void showStartGameScreen() {
        mainFrame.setScreen(new StartGameScreen(gameState, this));
    }
	public void showSelectCharacterScreen() {
		mainFrame.setScreen(new SelectCharacterScreen(gameState, this));
	}
	public void showPlayingGameScreen() {
		mainFrame.setScreen(new PlayingGameScreen(gameState, this));
	}
	public void showSelectCardScreen() {
		mainFrame.setScreen(new SelectCardScreen(gameState, this));
	}
	public void showEndGameScreen() {
		mainFrame.setScreen(new EndGameScreen(gameState));
	}
	
	
	// 캐릭터 선택 로직
	private void selectCharacter() {
		
		showSelectCharacterScreen();
		try {
			// 상대 캐릭터를 받아서 GameState에 저장
			JSONObject jsonResponse = new JSONObject(networkManager.receiveJson());
			String character = jsonResponse.getString("character");
			Character enemyCharacter = gameState.createCharacter(character);
			gameState.setEnemyCharacter(enemyCharacter);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	// 카드 선택 로직
	private void selectCard() {
		
		showSelectCardScreen();
		
		// 서버 타이머 시작
		
		try {
			// 상대 선택 카드를 받아서 GameState에 저장
			JSONObject jsonResponse = new JSONObject(networkManager.receiveJson());
			LinkedList<Card> cardList = new LinkedList<>();
			for (int i = 0; i < 3; i++) {
				// 의문 1. 카드 3개를 보내면 key가 똑같은게 3개씩인데 어떡함?
			}
			//gameState.setEnemySelectedCardList();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	// 전투 로직
	private void fight() {
		
	}
	
	
	// 게임종료여부 확인
	private boolean isGameOver() {
		
		return false;
	}
	
	
	// 게임 종료 로직
	private void gameOver() {
		
	}
	
	
	
	// 스크린에서 발생한 이벤트를 감지하여 처리합니다 !!
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
            case "CHARACTER_SELECT_FINISH":
            	networkManager.sendCharacterSelection();
            	break;
            case "CARD_SELECT_FINISH":
            	networkManager.sendCardSelection();
            	break;
            default:
                System.out.println("ㅈ비상");
        }
    }
}
