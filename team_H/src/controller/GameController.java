// GameController.java

package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
		                    showSelectCharacterScreen();
		                    new Thread(this::runGame).start(); // 게임 진행 시작
		                });

		            } catch (IOException | JSONException e) {
		                e.printStackTrace();
		            }
		        }).start();
		    }
		}



	 // 게임 진행
	 private void runGame() {
	        selectCharacter();
	        
	        if(gameState.getMyCharacter() != null) {
		        while (true) {
		            selectCard();
		            fight();
		            if (isGameOver()) {
		                gameOver();
		                break;
		            }
		        }
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
		                // 서버로부터 메시지 수신
		                String response = networkManager.receiveJson();
		                if (response == null) {
		                    System.out.println("서버 메시지를 수신하지 못했습니다. 다시 시도합니다...");
		                    Thread.sleep(500); // 잠시 대기 후 다시 시도
		                    continue;
		                }

		                JSONObject jsonResponse = new JSONObject(response);
		                String command = jsonResponse.getString("command");

		                if ("SEND_CHARACTER".equals(command)) {
		                    // 상대방 캐릭터 정보 설정
		                    String character = jsonResponse.getString("character");
		                    Character enemyCharacter = gameState.createCharacter(character);
		                    gameState.setEnemyCharacter(enemyCharacter);

		                    System.out.println("상대방 캐릭터 : " + character);

		                    // UI 스레드에서 카드 선택 화면으로 이동
		                    SwingUtilities.invokeLater(() -> {
		                        System.out.println("Show SelectCardScreen");
		                        showSelectCardScreen();
		                    });
		                    break; // 메시지 처리 완료 후 루프 종료
		                }
		            }
		        } catch (IOException | JSONException e) {
		            System.err.println("selectCharacter에서 예외 발생: " + e.getMessage());
		            e.printStackTrace();
		        } catch (InterruptedException e) {
		            Thread.currentThread().interrupt();
		            System.err.println("스레드가 인터럽트되었습니다: " + e.getMessage());
		        }
		    }).start();
		}


	
	
	// 카드 선택 로직
	private void selectCard() {
	    System.out.println("카드 선택 화면으로 이동");
	    showSelectCardScreen();

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

	                JSONObject jsonResponse = new JSONObject(response);
	                String command = jsonResponse.getString("command");
	                System.out.println("서버 명령 수신: " + command);

	                if ("SEND_CHARACTER".equals(command)) {
	                    String character = jsonResponse.getString("character");
	                    Character enemyCharacter = gameState.createCharacter(character);
	                    gameState.setEnemyCharacter(enemyCharacter);

	                    SwingUtilities.invokeLater(() -> {
	                        System.out.println("Show SelectCardScreen");
	                        showSelectCardScreen();
	                    });
	                    break;
	                }
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
		
		switch (card.getCategory()) {
			
			case "MOVE":
				// 모션 실행
				character.setMotion("MOVE");
				playingGameScreen.repaint();
				
				// 모션 끝날 때까지 대기
				Timer timer = new Timer(character.getCharacterMotionTimes().get(Character.Motion.MOVE)[1], e -> {
			        ((Timer) e.getSource()).stop();
			    });
			    timer.setRepeats(false);
			    timer.start();
				
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
				
				// 모션 끝날 때까지 대기
				timer = new Timer(character.getCardMotionTimes().get(card.getName())[1], e -> {
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
							character.setMotion("IDLE");
							gameState.getEnemyCharacter().setMotion("HIT");
							playingGameScreen.repaint();
							
							// 모션 끝날 때까지 대기
							timer = new Timer(character.getCharacterMotionTimes().get(Character.Motion.HIT)[1], e -> {
						        ((Timer) e.getSource()).stop();
						    });
						    timer.setRepeats(false);
						    timer.start();
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
							
							// 모션 끝날 때까지 대기
							timer = new Timer(character.getCharacterMotionTimes().get(Character.Motion.HIT)[1], e -> {
						        ((Timer) e.getSource()).stop();
						    });
						    timer.setRepeats(false);
						    timer.start();
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
