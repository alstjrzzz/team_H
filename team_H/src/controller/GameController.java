// GameController.java

package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import model.Character.Character;


import javax.swing.JPanel;

import org.json.JSONArray;
import org.json.JSONException;
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
		networkManager = new NetworkManager(gameState, this);
		showStartGameScreen();
		mainFrame.setVisible(true);
		
		try {
			// 서버에 connect finish 송신
			networkManager.sendConnectFinish();
			
			// 서버로부터 모든 플레이어 준비 완료 입력 대기
			JSONObject json = new JSONObject(networkManager.receiveJson());
			System.out.println(json.toString());
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
	}
	
	
	// 게임 진행 로직 !!
	private void startGame() {
		
		selectCharacter();
		
		while (true) {
			
			selectCard();
			fight();
			try {
				networkManager.receiveJson();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (isGameOver()) gameOver();
		}
	}
	
	
	// 각 스크린을 mainFrame에 그리게 하는 동작 !!
	public void showStartGameScreen() {
		System.out.println("Show StartGameScreen");
        mainFrame.setScreen(new StartGameScreen(gameState, this, networkManager));
    }
	public void showSelectCharacterScreen() {
		System.out.println("Show SelectCharacterScreen");
		mainFrame.setScreen(new SelectCharacterScreen(gameState, this, networkManager));
	}
	public void showPlayingGameScreen() {
		System.out.println("Show PlayingGameScreen");
		mainFrame.setScreen(new PlayingGameScreen(gameState, this, networkManager));
	}
	public void showSelectCardScreen() {
		System.out.println("Show SelectCardScreen");
		mainFrame.setScreen(new SelectCardScreen(gameState, this, networkManager));
	}
	public void showEndGameScreen() {
		System.out.println("Show EndGameScreen");
		mainFrame.setScreen(new EndGameScreen(gameState));
	}
	
	
	// 캐릭터 선택 로직
	private void selectCharacter() {
		
		showSelectCharacterScreen();
		
		try {
			// 상대 캐릭터를 받아서 GameState에 저장
			JSONObject jsonResponse = new JSONObject(networkManager.receiveJson());
			System.out.println(jsonResponse.toString());
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
		
		try {
			// 상대 선택 카드를 받아서 GameState에 저장
			JSONArray jsonArray = new JSONArray(networkManager.receiveJson());
			LinkedList<Card> enemyCardList = new LinkedList<>();
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject cardJson = jsonArray.getJSONObject(i);
				
				JSONArray rangeArray = cardJson.getJSONArray("range");
		        LinkedList<int[]> range = new LinkedList<>();
		        for (int j = 0; j < rangeArray.length(); j++) {
		            JSONArray r = rangeArray.getJSONArray(j);
		            range.add(new int[]{r.getInt(0), r.getInt(1)});
		        }
				
				enemyCardList.add(new Card(
						cardJson.getString("name"),
						cardJson.getString("category"),
						range,
						cardJson.getInt("value"),
						cardJson.getInt("priority")
						));
			}
			gameState.setEnemySelectedCardList(enemyCardList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	// 전투 로직
	private void fight() {
		
		if (gameState.getMyPosition() == null) {
			if (gameState.getClientNumber() == 1) {
				gameState.setMyPosition(new int[] {0, 1});
				gameState.setEnemyPosition(new int[] {5, 1});
			}
			else {
				gameState.setMyPosition(new int[] {5, 1});
				gameState.setEnemyPosition(new int[] {0, 1});
			}
		}
		
		// 테스트, 여기서부터
		System.out.println("start fight");
		System.out.println("my character: " + gameState.getMyCharacter().getName());
		System.out.println("enemy character: " + gameState.getEnemyCharacter().getName());
		
		System.out.println("my start location: " + gameState.getMyPosition()[0] + "," + gameState.getMyPosition()[1]);
		System.out.println("enemy start location: " + gameState.getEnemyPosition()[0] + "," + gameState.getEnemyPosition()[1]);
		System.out.println();
		// 여기까지 지울것
		
		showPlayingGameScreen();
		
		for (int i = 0; i < 3; i++) {
			
			if (gameState.getSelectedCardList().get(i).getPriority()
					> gameState.getEnemySelectedCardList().get(i).getPriority()) {
				
				// 상대 i번째 카드 사용
				useCard(gameState.getEnemySelectedCardList().get(i));
				// 내 i번째 카드 사용
				useCard(gameState.getSelectedCardList().get(i));
				
			} else if (gameState.getSelectedCardList().get(i).getPriority()
					< gameState.getEnemySelectedCardList().get(i).getPriority()) {
				
				// 내 i번째 카드 사용
				useCard(gameState.getSelectedCardList().get(i));
				// 상대 i번째 카드 사용
				useCard(gameState.getEnemySelectedCardList().get(i));
				
			} else {
				
				if (gameState.getClientNumber() == 1) {
					
					// 내 i번째 카드 사용
					useCard(gameState.getSelectedCardList().get(i));
					// 상대 i번째 카드 사용
					useCard(gameState.getEnemySelectedCardList().get(i));
					
				} else {
					
					// 상대 i번째 카드 사용
					useCard(gameState.getEnemySelectedCardList().get(i));
					// 내 i번째 카드 사용
					useCard(gameState.getSelectedCardList().get(i));
					
				}
				
			}
		}
		
	}
	
	
	// 게임종료여부 확인
	private boolean isGameOver() {
		
		return false;
	}
	
	
	// 게임 종료 로직
	private void gameOver() {
		
	}
	
	
	
	private void useCard(Card card) {
		
		switch (card.getCategory()) {
			/*
			case "MOVE":
				
				break;
			case "ATTACK":
				
				break;*/
			default:
				// 테스트, 위에 주석 풀고 여기서부터
				System.out.println("card name:" + card.getName());
				System.out.println("card category: " + card.getCategory());
				System.out.print("card range: ");
				for (int i = 0; i < card.getRange().size(); i++) {
					System.out.print("[" + card.getRange().get(i)[0] + "," + card.getRange().get(i)[1] + "] ");
				}
				System.out.println("card value: " + card.getValue());
				System.out.println("card priority: " + card.getPriority());
				System.out.println();
				// 여기까지 지워!!
		}
	}
	
	// 스크린에서 발생한 이벤트를 감지하여 처리합니다 !!
	/*
    public void handleAction(String action) {
    	
        switch (action) {
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
    */
}
