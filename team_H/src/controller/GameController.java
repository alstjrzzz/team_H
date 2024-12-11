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
	
	private PlayingGameScreen playingGameScreen;
	
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
		
		showPlayingGameScreen();
		
		for (int i = 0; i < 3; i++) {
			
			if (gameState.getSelectedCardList().get(i).getPriority()
					> gameState.getEnemySelectedCardList().get(i).getPriority()) {
				
				// 상대 i번째 카드 사용
				useCard(gameState.getEnemySelectedCardList().get(i), gameState.getEnemyCharacter());
				// 내 i번째 카드 사용
				useCard(gameState.getSelectedCardList().get(i), gameState.getMyCharacter());
				
			} else if (gameState.getSelectedCardList().get(i).getPriority()
					< gameState.getEnemySelectedCardList().get(i).getPriority()) {
				
				// 내 i번째 카드 사용
				useCard(gameState.getSelectedCardList().get(i), gameState.getMyCharacter());
				// 상대 i번째 카드 사용
				useCard(gameState.getEnemySelectedCardList().get(i), gameState.getEnemyCharacter());
				
			} else {
				
				if (gameState.getClientNumber() == 1) {
					
					// 내 i번째 카드 사용
					useCard(gameState.getSelectedCardList().get(i), gameState.getMyCharacter());
					// 상대 i번째 카드 사용
					useCard(gameState.getEnemySelectedCardList().get(i), gameState.getEnemyCharacter());
					
				} else {
					
					// 상대 i번째 카드 사용
					useCard(gameState.getEnemySelectedCardList().get(i), gameState.getEnemyCharacter());
					// 내 i번째 카드 사용
					useCard(gameState.getSelectedCardList().get(i), gameState.getMyCharacter());
					
				}
				
			}
		}
		
		// 카드 전부 사용하면 다음 카드 선택 페이지로 이동하는 버튼 setVisible = true, 서버와 연동해서 두 클라이언트가 동시에 카드 선택되도록 해야 함
		
	}
	
	
	// 게임종료여부 확인
	private boolean isGameOver() {
		
		return false;
	}
	
	
	// 게임 종료 로직
	private void gameOver() {
		
	}
	
	
	
	private void useCard(Card card, Character character) {
		
		switch (card.getCategory()) {
			
			case "MOVE":
				// 모션 실행
				character.setMotion("MOVE");
				playingGameScreen.repaint();
				
				// GameState 업데이트
				if (character == gameState.getMyCharacter()) {
					int[] newPosition = gameState.getMyPosition().clone();
					newPosition[0] += card.getRange().get(0)[0];
					newPosition[1] += card.getRange().get(0)[1];
					gameState.setMyPosition(newPosition.clone());
				} else {
					int[] newPosition = gameState.getEnemyPosition().clone();
					newPosition[0] += card.getRange().get(0)[0];
					newPosition[1] += card.getRange().get(0)[1];
					gameState.setEnemyPosition(newPosition.clone());
				}
				
				// 기본 자세로 변경
				character.setMotion("IDLE");
				character.setCurrentCard(card);
				playingGameScreen.repaint();
				break;
			case "ATTACK":
				character.setMotion("ATTACK");
				playingGameScreen.repaint();
				
				if (character == gameState.getMyCharacter()) { // 내 캐릭터가 공격함
					
					for (int i = 0; i < card.getRange().size(); i++) {
						int[] range = new int[2];
						if (!gameState.isMyCharacterIsFlip()) {
							range[0] = gameState.getMyPosition()[0] + card.getRange().get(i)[0];
							range[1] = gameState.getMyPosition()[1] + card.getRange().get(i)[1];
						} else {
							range[0] = gameState.getMyPosition()[0] - card.getRange().get(i)[0];
							range[1] = gameState.getMyPosition()[1] + card.getRange().get(i)[1];
						}
						
						if (range.equals(gameState.getEnemyPosition())) {
							gameState.setEnemyHealth(gameState.getEnemyHealth() - card.getValue());
							character.setMotion("IDLE");
							gameState.getEnemyCharacter().setMotion("HIT");
							playingGameScreen.repaint();
						}
					}
					gameState.getEnemyCharacter().setMotion("IDLE");
					playingGameScreen.repaint();
				} else {
					
					for (int i = 0; i < card.getRange().size(); i++) {
						int[] range = new int[2];
						if (!gameState.isEnemyCharacterIsFlip()) {
							range[0] = gameState.getEnemyPosition()[0] + card.getRange().get(i)[0];
							range[1] = gameState.getEnemyPosition()[1] + card.getRange().get(i)[1];
						} else {
							range[0] = gameState.getEnemyPosition()[0] - card.getRange().get(i)[0];
							range[1] = gameState.getEnemyPosition()[1] + card.getRange().get(i)[1];
						}
						
						if (range.equals(gameState.getEnemyPosition())) {
							gameState.setMyHealth(gameState.getMyHealth() - card.getValue());
							character.setMotion("IDLE");
							gameState.getMyCharacter().setMotion("HIT");
							playingGameScreen.repaint();
						}
					}
					gameState.getMyCharacter().setMotion("IDLE");
					playingGameScreen.repaint();
				}
				break;
			case "GUARD":
				
				break;
			default:
				
		}
	}
	
}
