// GameController.java

package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import model.Character.Character;


import javax.swing.JPanel;

import org.json.JSONArray;
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
			JSONArray jsonArray = new JSONArray(networkManager.receiveJson());
			LinkedList<Card> enemyCardList = new LinkedList<>();
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject cardJson = jsonArray.getJSONObject(i);
				
				JSONArray rangeArray = cardJson.getJSONArray("range");
		        List<int[]> range = new ArrayList<>();
		        for (int j = 0; j < rangeArray.length(); j++) {
		            JSONArray r = rangeArray.getJSONArray(j);
		            range.add(new int[]{r.getInt(0), r.getInt(1)});
		        }
				
				enemyCardList.add(new Card(
						cardJson.getString("name"),
						cardJson.getString("effect"),
						range,
						cardJson.getInt("damage")
						));
			}
			gameState.setEnemySelectedCardList(enemyCardList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	// 전투 로직
	private void fight() {
	    showPlayingGameScreen(); // 화면 전환

	    // 비동기로 카드 동작 실행
	    new Thread(() -> {
	        // 플레이어 카드와 적 카드 큐 가져오기
	        LinkedList<Card> playerCards = new LinkedList<>(gameState.getSelectedCardList());
	        LinkedList<Card> enemyCards = new LinkedList<>(gameState.getEnemySelectedCardList());

	        // 전투 실행 (동기화된 실행 흐름)
	        while (!playerCards.isEmpty() || !enemyCards.isEmpty()) {
	            // 플레이어 카드 실행
	            if (!playerCards.isEmpty()) {
	                Card playerCard = playerCards.poll();
	                executeCardAction(playerCard, true); // 플레이어 동작 실행
	            }

	            // 적 카드 실행
	            if (!enemyCards.isEmpty()) {
	                Card enemyCard = enemyCards.poll();
	                executeCardAction(enemyCard, false); // 적 동작 실행
	            }

	            // 게임 종료 조건 확인
	            if (isGameOver()) {
	                gameOver();
	                return;
	            }

	            // 각 카드 간의 간격 (1초 지연)
	            try {
	                Thread.sleep(1000);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    }).start(); // 비동기 실행
	}

	// 카드 동작 실행
	private void executeCardAction(Card card, boolean isPlayer) {
	    if (isPlayer) {
	        // 플레이어 카드 동작
	        System.out.println("Player executes: " + card.getName());
	        gameState.getMyCharacter().useCard(card); // 캐릭터가 카드 동작 수행
	    } else {
	        // 적 카드 동작
	        System.out.println("Enemy executes: " + card.getName());
	        gameState.getEnemyCharacter().useCard(card); // 적 캐릭터 동작
	    }
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
