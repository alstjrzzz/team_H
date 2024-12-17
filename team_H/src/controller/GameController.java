// GameController.java

package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import model.Character.Character;


import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

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
import view.SelectUserScreen;

public class GameController {
	
	private MainFrame mainFrame;
	private GameState gameState;
	private NetworkManager networkManager;
	
	private boolean isConnected = false; // 서버 연결 상태 확인
	
	private PlayingGameScreen playingGameScreen;
	private CountDownLatch characterLatch = new CountDownLatch(1); // 동기화 도구 추가
	private boolean isCardSelectionComplete = false;
	private CountDownLatch cardLatch;
	
	public GameController() {
		
		startProgram();
		
	}
	
	
	 // 프로그램 켰을 때 동작 !!
	 private void startProgram() {
	        mainFrame = new MainFrame();
	        gameState = new GameState(mainFrame.getSize());
	        networkManager = new NetworkManager(gameState, this);

	        // 초기 화면: SelectUserScreen 표시
	        showSelectUserScreen();
	        mainFrame.setVisible(true);
	        
	    }
	
	
	 // 서버 연결 후 게임 시작
	 public void startGame() {
		    if (!isConnected) {
		        new Thread(() -> {
		            try {
		                // 서버 연결
		                networkManager.connectToServer();

		                // 서버에 connect finish 송신
		                networkManager.sendConnectFinish();

		                // 서버로부터 모든 플레이어 준비 완료 입력 대기
		                System.out.println("Waiting for other player...");
		                String response = networkManager.receiveJson(); // 서버 응답 대기
		                System.out.println("서버로부터 수신된 데이터: " + response);

		                isConnected = true; // 연결 완료

		                // SelectCharacterScreen으로 전환
		                SwingUtilities.invokeLater(() -> {
		                	selectCharacter();
		                    
		                });

		            } catch (IOException | JSONException e) {
		                e.printStackTrace();
		            }
		        }).start();
		    }
		}



	 // 게임 진행
	 private void runGame() {
		    System.out.println("runGame() 호출됨");

		    if (gameState.getMyCharacter() != null) {
		        System.out.println("내 캐릭터가 설정되었습니다: " + gameState.getMyCharacter().getName());
		        while (true) {
		            System.out.println("selectCard() 호출 시작");

		            selectCard();
		            try {
		                cardLatch.await(); // 작업 완료 대기
		            } catch (InterruptedException e) {
		                Thread.currentThread().interrupt();
		                System.err.println("스레드 대기 중 인터럽트 발생: " + e.getMessage());
		            }

		            System.out.println("selectCard() 호출 완료");

		            fight();
		            if (isGameOver()) {
		                gameOver();
		                break;
		            }
		        }
		    } else {
		        System.out.println("내 캐릭터가 설정되지 않았습니다.");
		    }
		}
	
	public void showSelectUserScreen() {
        System.out.println("Show SelectUserScreen");
        mainFrame.setScreen(new SelectUserScreen(mainFrame, gameState, this, networkManager));
    }
	// 각 스크린을 mainFrame에 그리게 하는 동작 !!
	public void showStartGameScreen() {
		System.out.println("Show StartGameScreen");
        mainFrame.setScreen(new StartGameScreen(gameState, this, networkManager));
        // 서버 연결 및 게임 시작
        startGame();
    }
	public void showSelectCharacterScreen() {
		System.out.println("Show SelectCharacterScreen");
		mainFrame.setScreen(new SelectCharacterScreen(gameState, this, networkManager));
		
	}
	public void showPlayingGameScreen() {
		System.out.println("Show PlayingGameScreen");
		mainFrame.setScreen(playingGameScreen = new PlayingGameScreen(gameState, this, networkManager));
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

	    new Thread(() -> {
	        try {
	            while (true) {
	                String response = networkManager.receiveJson();
	                if (response == null) {
	                    System.out.println("서버 메시지를 수신하지 못했습니다. 다시 시도합니다...");
	                    Thread.sleep(500); // 잠시 대기 후 다시 시도
	                    continue;
	                }

	                JSONObject jsonResponse = new JSONObject(response);
	                String command = jsonResponse.getString("command");

	                // 내 캐릭터 설정
	                if ("CHARACTER_SELECT_FINISH".equals(command)) {
	                    String myCharacter = jsonResponse.getString("character");
	                    gameState.setMyCharacter(gameState.createCharacter(myCharacter));
	                    System.out.println("내 캐릭터가 설정되었습니다: " + myCharacter);

	                    characterLatch.countDown(); // 캐릭터 설정 완료 알림
	                }

	                // 상대방 캐릭터 설정
	                if ("SEND_CHARACTER".equals(command)) {
	                    String character = jsonResponse.getString("character");
	                    Character enemyCharacter = gameState.createCharacter(character);
	                    gameState.setEnemyCharacter(enemyCharacter);
	                    gameState.setEnemyHealth(enemyCharacter.getMaxHealth());

	                    System.out.println("상대방 캐릭터 : " + character);

	                    SwingUtilities.invokeLater(() -> {
	                    	new Thread(this::runGame).start(); // 게임 진행 시작
	                    });
	                    break; // 상대방 캐릭터 설정 완료 후 종료
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }).start();
	}



	
	
	// 카드 선택 로직
	private void selectCard() {
	    System.out.println("카드 선택 화면으로 이동");
	    showSelectCardScreen();

	    cardLatch = new CountDownLatch(1); // 동기화 도구 생성

	    new Thread(() -> {
	        try {
	            while (true) {
	                String response = networkManager.receiveJson();
	                if (response == null) {
	                    System.out.println("서버 응답을 받지 못했습니다.");
	                    Thread.sleep(500);
	                    continue;
	                }

	                System.out.println("수신된 메시지: " + response);

	                // JSON 데이터가 배열 형식임을 고려하여 JSONArray로 처리
	                JSONArray cardDataArray = new JSONArray(response); // 수정된 부분
	                LinkedList<Card> enemySelectedCards = new LinkedList<>();

	                for (int i = 0; i < cardDataArray.length(); i++) {
	                    JSONObject cardData = cardDataArray.getJSONObject(i);

	                    // JSON에서 range를 LinkedList<int[]>로 변환
	                    LinkedList<int[]> range = new LinkedList<>();
	                    JSONArray rangeArray = cardData.getJSONArray("range");
	                    for (int j = 0; j < rangeArray.length(); j++) {
	                        JSONArray singleRange = rangeArray.getJSONArray(j);
	                        int[] rangeCoords = { singleRange.getInt(0), singleRange.getInt(1) };
	                        range.add(rangeCoords);
	                    }

	                    // Card 객체 생성
	                    Card card = new Card(
	                        cardData.getString("name"),
	                        cardData.getString("category"),
	                        range,
	                        cardData.getInt("value"),
	                        cardData.getInt("priority")
	                    );
	                    enemySelectedCards.add(card);
	                }

	                // GameState의 enemySelectedCardList 업데이트
	                gameState.setEnemySelectedCardList(enemySelectedCards);
	                System.out.println("상대방 선택 카드 리스트 업데이트 완료:");

	                // 콘솔에 상대방 카드 리스트 출력
	                for (Card card : enemySelectedCards) {
	                    System.out.println("카드 이름: " + card.getName());
	                    System.out.println("카드 카테고리: " + card.getCategory());
	                    System.out.println("카드 우선순위: " + card.getPriority());
	                    System.out.println("카드 값: " + card.getValue());
	                    System.out.print("카드 범위: ");
	                    for (int[] range : card.getRange()) {
	                        System.out.print("[" + range[0] + ", " + range[1] + "] ");
	                    }
	                    System.out.println("\n------------------------");
	                }

	                cardLatch.countDown(); // 작업 완료 알림
	                break;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }).start();
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
		
	    if (card == null || character == null) {
	        System.err.println("Card or character is null in useCard!");
	        return;
	    }
	    
		switch (card.getCategory()) {
			
			case "MOVE":
				// 모션 실행
				character.setCurrentMotion("MOVE");
				character.setCurrentCard(card);
				playingGameScreen.drawMotion();
				
				// Timer 대신 CountDownLatch로 대기 구현
	            CountDownLatch latch = new CountDownLatch(1);

	            SwingUtilities.invokeLater(() -> {
	                try {
	                    Timer timer = new Timer(5000, e -> {
	                        ((Timer) e.getSource()).stop();
	                        latch.countDown(); // 작업 완료 신호
	                    });
	                    timer.setRepeats(false);
	                    timer.start();
	                } catch (Exception ex) {
	                    ex.printStackTrace();
	                    latch.countDown(); // 에러 발생 시에도 계속 진행
	                }
	            });

	            try {
	                latch.await(); // Timer 종료까지 대기
	            } catch (InterruptedException e) {
	                Thread.currentThread().interrupt();
	                System.err.println("Thread interrupted: " + e.getMessage());
	            }
			    
			    
				// GameState 업데이트
				if (character == gameState.getMyCharacter()) {
					int[] newPosition = gameState.getMyPosition().clone();
					if (newPosition[0] + card.getRange().get(0)[0] >= 0 && newPosition[0] + card.getRange().get(0)[0] <= 5)
						newPosition[0] += card.getRange().get(0)[0];
					if (newPosition[1] + card.getRange().get(0)[1] >= 0 && newPosition[1] + card.getRange().get(0)[1] <= 2)
						newPosition[1] += card.getRange().get(0)[1];
					gameState.setMyPosition(newPosition.clone());
				} else {
					int[] newPosition = gameState.getEnemyPosition().clone();
					if (newPosition[0] + card.getRange().get(0)[0] >= 0 && newPosition[0] + card.getRange().get(0)[0] <= 5)
						newPosition[0] += card.getRange().get(0)[0];
					if (newPosition[1] + card.getRange().get(0)[1] >= 0 && newPosition[1] + card.getRange().get(0)[1] <= 2)
						newPosition[1] += card.getRange().get(0)[1];
					gameState.setEnemyPosition(newPosition.clone());
				}
				
				// 기본 자세로 변경
				character.setCurrentMotion("IDLE");
				character.setCurrentCard(card);
				playingGameScreen.drawMotion();
				System.out.println("playing game screen repaint start 2");
				break;
			case "ATTACK":
				character.setCurrentMotion("ATTACK");
				character.setCurrentCard(card);
				playingGameScreen.drawMotion();
				
				// 모션 끝날 때까지 대기
				Timer timer = new Timer(5000, e -> {
			        ((Timer) e.getSource()).stop();
			    });
			    timer.setRepeats(false);
			    timer.start();
				
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
							character.setCurrentMotion("IDLE");
							gameState.getEnemyCharacter().setCurrentMotion("HIT");
							playingGameScreen.drawMotion();
							
							// 모션 끝날 때까지 대기
							timer = new Timer(5000, e -> {
						        ((Timer) e.getSource()).stop();
						    });
						    timer.setRepeats(false);
						    timer.start();
						}
					}
					gameState.getEnemyCharacter().setCurrentMotion("IDLE");
					character.setCurrentCard(card);
					playingGameScreen.drawMotion();
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
							character.setCurrentMotion("IDLE");
							gameState.getMyCharacter().setCurrentMotion("HIT");
							playingGameScreen.drawMotion();
							
							// 모션 끝날 때까지 대기
							timer = new Timer(5000, e -> {
						        ((Timer) e.getSource()).stop();
						    });
						    timer.setRepeats(false);
						    timer.start();
						}
					}
					gameState.getMyCharacter().setCurrentMotion("IDLE");
					character.setCurrentCard(card);
					playingGameScreen.drawMotion();
				}
				break;
			case "GUARD":
				
				break;
	        default:
	            System.err.println("Unknown card category: " + card.getCategory());
				
				
		}
	}
	
}
