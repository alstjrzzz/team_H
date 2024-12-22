package view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;

import controller.GameController;
import model.Card;
import model.Character.Character;
import model.Character.Doraemon;
import model.GameState;
import network.NetworkManager;

public class PlayingGameScreen extends JPanel {

    private GameState gameState;
    private GameController gameController;
    private NetworkManager networkManager;
    private ImageIcon backgroundImage; // 움짤을 위한 ImageIcon

    
    public final static int gridRows = 3; // 그리드 행 수
    public final static int gridCols = 6; // 그리드 열 수

    public final static int gridWidth = 150; // 그리드의 각 셀 너비
    public final static int gridHeight = 60; // 높이

    public final static int gridStartX = (1000 - gridCols * gridWidth) / 2; // 그리드의 왼쪽 상단 모서리 좌표 x
    public final static int gridStartY = (700 - gridRows * gridHeight) / 2 + 125; // y
    
    public final static int gridClient1X = 0; // 셀 내에서 Client1(왼쪽)의 캐릭터가 서 있을 위치(왼쪽 아래를 가리킴)
    public final static int gridClient1Y = 0;

    public final static int gridClient2X = 75; // Client2(오른쪽) 수정필요
    public final static int gridClient2Y = 0;

    Character myCharacter;
    Character enemyCharacter;
    
    BufferedImage[] myMotions;
    int myCurrentFrame;
    int myFrameDelay;
    int myDuration;
    Timer myMotionTimer;
    
    BufferedImage[] enemyMotions;
    int enemyCurrentFrame;
    int enemyFrameDelay;
    int enemyDuration;
    Timer enemyMotionTimer;
    
    private JPanel healthPanel;
    private JPanel fieldPanel;
    private JPanel cardPanel;
    
    public PlayingGameScreen(GameState gameState, GameController gameController, NetworkManager networkManager) {

        this.gameState = gameState;
        this.gameController = gameController;
        this.networkManager = networkManager;

        // 움짤 배경화면 로드
        backgroundImage = new ImageIcon("res/img/gameback_kim.gif"); // 움짤 경로 지정

        healthPanel = new JPanel();
        fieldPanel = new JPanel();
        cardPanel = new JPanel(null);

        splitPanel();
        drawHealthPanel();
        drawSelectedCardPanel();

        revalidate();
        repaint();

        System.out.println("PlayingGameScreen initialized successfully");
    }

    public void splitPanel() {
        setLayout(new BorderLayout());

        healthPanel.setPreferredSize(new Dimension(0, 50)); // 고정 높이
        cardPanel.setPreferredSize(new Dimension(0, 100)); // 고정 높이
        fieldPanel.setOpaque(false);
        add(healthPanel, BorderLayout.NORTH); // 상단: 체력바
        add(fieldPanel, BorderLayout.CENTER); // 중간: 게임 필드
        add(cardPanel, BorderLayout.SOUTH); // 하단: 카드 패널
    }

    
    // drawMotion에서는 타이머를 이용해 repaint()를 호출하고, paintComponent에서는 그리는 동작만 수행합니다!!
    public void drawMotion() {
    	if (myMotionTimer != null && myMotionTimer.isRunning()) {
    	    myMotionTimer.stop();
    	}
    	if (enemyMotionTimer != null && enemyMotionTimer.isRunning()) {
    		enemyMotionTimer.stop();
    	}
    	
    	if (gameState.getMyCharacter().getCurrentMotion().equals("ATTACK")
    			|| gameState.getMyCharacter().getCurrentMotion().equals("MOVE")
    			|| gameState.getEnemyCharacter().getCurrentMotion().equals("ATTACK")
    			|| gameState.getEnemyCharacter().getCurrentMotion().equals("MOVE")) {
    		networkManager.sendStopPlease(5000);	// 공격이나 무빙은 오래걸리니까 8초 기둘
    	} else {
    		networkManager.sendStopPlease(1000);	// 맞은 모션이나 idle은 짧게 3초만 ㅋ
    	}
    	
    	// 내 캐릭터 그리기
    	myCharacter = gameState.getMyCharacter();
    	if (gameState.getClientNumber() == 1) {
    		myCharacter.setCurrentX(gridStartX + gameState.getMyPosition()[0] * gridWidth + gridClient1X);
    		myCharacter.setCurrentY(gridStartY + gameState.getMyPosition()[1] * gridHeight + gridClient1Y -60);
    	} else {
    		myCharacter.setCurrentX(gridStartX + gameState.getMyPosition()[0] * gridWidth + gridClient2X);
    		myCharacter.setCurrentY(gridStartY + gameState.getMyPosition()[1] * gridHeight + gridClient2Y-60);
    	}
    	// 이 주석 지우면 버그나는데 왜 그런지는 모르겠음 
		switch (myCharacter.getName()) {
        
        // Doraemon ------------------------------------------------------------	
        case "Doraemon":
            switch (myCharacter.getCurrentMotion()) {
            case "MOVE":
                switch (myCharacter.getCurrentCard().getName()) {
                case "Move Up":
                    // 시작 위치와 이동 거리 설정
                    int MU_startY = myCharacter.getCurrentY(); // 시작 위치 Y
                    int MU_endY = MU_startY - 60; // 목표 위치 (위쪽으로 60px 이동)

                    // 애니메이션 관련 설정
                    myMotions = myCharacter.getMotions().get(myCharacter.getCurrentCard().getName());
                    myFrameDelay = 200; // 각 프레임 간격
                    int MU_totalFrames = myMotions.length; // 애니메이션 총 프레임 수
                    myDuration = 7 * myFrameDelay * MU_totalFrames; // 애니메이션 총 시간

                    myCurrentFrame = 0;

                    // 이동 속도 계산
                    int MU_steps = 7 * MU_totalFrames; // 이동할 스텝 수
                    int MU_stepSize = (MU_endY - MU_startY) / MU_steps; // 한 스텝당 이동 거리

                    // 프레임별 애니메이션 실행
                    Timer myMotionTimer = new Timer(myFrameDelay, null);
                    myMotionTimer.addActionListener(e -> {
                        // 현재 프레임 업데이트
                        myCurrentFrame = (myCurrentFrame + 1) % MU_totalFrames;

                        // 현재 위치 업데이트
                        int currentY = myCharacter.getCurrentY();
                        if (Math.abs(currentY - MU_endY) > Math.abs(MU_stepSize)) {
                            myCharacter.setCurrentY(currentY + MU_stepSize); // Y 위치 업데이트
                        }

                        // 화면 갱신
                        repaint();
                    });

                    // 애니메이션 종료 시 처리
                    new Timer(myDuration, e -> {
                        myMotionTimer.stop();
                        myCharacter.setCurrentY(MU_endY); // 최종 위치 보정
                        ((Timer) e.getSource()).stop();
                    }).start();

                    myMotionTimer.start();

                    break;
                case "Move Down":
                    // 시작 위치와 이동 거리 설정
                    int MD_startY = myCharacter.getCurrentY(); // 시작 위치 Y
                    int MD_endY = MD_startY + 60; // 목표 위치 (아래쪽으로 60px 이동)

                    // 애니메이션 관련 설정
                    myMotions = myCharacter.getMotions().get(myCharacter.getCurrentCard().getName());
                    myFrameDelay = 200; // 각 프레임 간격
                    int MD_totalFrames = myMotions.length; // 애니메이션 총 프레임 수
                    myDuration = 7 * myFrameDelay * MD_totalFrames; // 애니메이션 총 시간

                    myCurrentFrame = 0;

                    // 이동 속도 계산
                    int MD_steps = 7 * MD_totalFrames; // 이동할 스텝 수
                    int MD_stepSize = (MD_endY - MD_startY) / MD_steps; // 한 스텝당 이동 거리

                    // 프레임별 애니메이션 실행
                    myMotionTimer = new Timer(myFrameDelay, null);
                    myMotionTimer.addActionListener(e -> {
                        // 현재 프레임 업데이트
                        myCurrentFrame = (myCurrentFrame + 1) % MD_totalFrames;

                        // 현재 위치 업데이트
                        int currentY = myCharacter.getCurrentY();
                        if (Math.abs(currentY - MD_endY) > Math.abs(MD_stepSize)) {
                            myCharacter.setCurrentY(currentY + MD_stepSize); // Y 위치 업데이트
                        }

                        // 화면 갱신
                        repaint();
                    });

                    // 애니메이션 종료 시 처리
                    new Timer(myDuration, e -> {
                        myMotionTimer.stop();
                        myCharacter.setCurrentY(MD_endY); // 최종 위치 보정
                        ((Timer) e.getSource()).stop();
                    }).start();

                    myMotionTimer.start();

                    break;

                case "Move Left":
                    // 시작 위치와 이동 거리 설정
                    int ML_startX = myCharacter.getCurrentX(); // 시작 위치 X
                    int ML_endX = ML_startX - 150; // 목표 위치 (왼쪽으로 150px 이동)

                    // 애니메이션 관련 설정
                    myMotions = myCharacter.getMotions().get(myCharacter.getCurrentCard().getName());
                    myFrameDelay = 200; // 각 프레임 간격
                    int ML_totalFrames = myMotions.length; // 애니메이션 총 프레임 수
                    myDuration = 7 * myFrameDelay * ML_totalFrames; // 애니메이션 총 시간

                    myCurrentFrame = 0;

                    // 이동 속도 계산
                    int ML_steps = 7 * ML_totalFrames; // 이동할 스텝 수
                    int ML_stepSize = (ML_endX - ML_startX) / ML_steps; // 한 스텝당 이동 거리

                    // 프레임별 애니메이션 실행
                    myMotionTimer = new Timer(myFrameDelay, null);
                    myMotionTimer.addActionListener(e -> {
                        // 현재 프레임 업데이트
                        myCurrentFrame = (myCurrentFrame + 1) % ML_totalFrames;

                        // 현재 위치 업데이트
                        int currentX = myCharacter.getCurrentX();
                        if (Math.abs(currentX - ML_endX) > Math.abs(ML_stepSize)) {
                            myCharacter.setCurrentX(currentX + ML_stepSize); // X 위치 업데이트
                        }

                        // 화면 갱신
                        repaint();
                    });

                    // 애니메이션 종료 시 처리
                    new Timer(myDuration, e -> {
                        myMotionTimer.stop();
                        myCharacter.setCurrentX(ML_endX); // 최종 위치 보정
                        ((Timer) e.getSource()).stop();
                    }).start();

                    myMotionTimer.start();

                    break;

                case "Move Right":
                    // 시작 위치와 이동 거리 설정
                    int MR_startX = myCharacter.getCurrentX(); // 시작 위치 X
                    int MR_endX = MR_startX + 150; // 목표 위치 (오른쪽으로 150px 이동)

                    // 애니메이션 관련 설정
                    myMotions = myCharacter.getMotions().get(myCharacter.getCurrentCard().getName());
                    myFrameDelay = 200; // 각 프레임 간격
                    int MR_totalFrames = myMotions.length; // 애니메이션 총 프레임 수
                    myDuration = 7 * myFrameDelay * MR_totalFrames; // 애니메이션 총 시간

                    myCurrentFrame = 0;

                    // 이동 속도 계산
                    int MR_steps = 7 * MR_totalFrames; // 이동할 스텝 수
                    int MR_stepSize = (MR_endX - MR_startX) / MR_steps; // 한 스텝당 이동 거리

                    // 프레임별 애니메이션 실행
                    myMotionTimer = new Timer(myFrameDelay, null);
                    myMotionTimer.addActionListener(e -> {
                        // 현재 프레임 업데이트
                        myCurrentFrame = (myCurrentFrame + 1) % MR_totalFrames;

                        // 현재 위치 업데이트
                        int currentX = myCharacter.getCurrentX();
                        if (Math.abs(currentX - MR_endX) > Math.abs(MR_stepSize)) {
                            myCharacter.setCurrentX(currentX + MR_stepSize); // X 위치 업데이트
                        }

                        // 화면 갱신
                        repaint();
                    });

                    // 애니메이션 종료 시 처리
                    new Timer(myDuration, e -> {
                        myMotionTimer.stop();
                        myCharacter.setCurrentX(MR_endX); // 최종 위치 보정
                        ((Timer) e.getSource()).stop();
                    }).start();

                    myMotionTimer.start();

                    break;
                }
                break;

            case "ATTACK":
                switch (myCharacter.getCurrentCard().getName()) {
                case "Air Cannon!":
                	myCharacter.playCardSound("Air Cannon!");
                    myMotions = myCharacter.getMotions().get(myCharacter.getCurrentCard().getName());
                    myFrameDelay = 200; // 각 프레임 간격
                    myDuration = myFrameDelay * myMotions.length * 8; // 애니메이션 총 시간

                    myCurrentFrame = 0;

                    myMotionTimer = new Timer(myFrameDelay, null);
                    myMotionTimer.addActionListener(e -> {
                        myCurrentFrame = (myCurrentFrame + 1) % myMotions.length;
                        repaint();
                    });

                    new Timer(myDuration, e -> {
                        myMotionTimer.stop();
                        ((Timer) e.getSource()).stop();
                    }).start();

                    myMotionTimer.start();
                    
                    break;
                    
            	case "Bamboo Helicopter!":
            		myCharacter.playCardSound("Bamboo Helicopter!");
           		    int MR_startX = myCharacter.getCurrentX(); // 시작 위치 X
        		    int MR_endX = MR_startX + 150*4; // 목표 위치 (오른쪽으로 150px 이동)

        		    // 애니메이션 관련 설정
        		    myMotions = myCharacter.getMotions().get(myCharacter.getCurrentCard().getName());
        		    myFrameDelay = 100; // 각 프레임 간격
        		    int MR_totalFrames = myMotions.length; // 애니메이션 총 프레임 수
        		    myDuration = 8 * myFrameDelay * MR_totalFrames; // 애니메이션 총 시간

        		    myCurrentFrame = 0;

        		    // 이동 속도 계산
        		    int MR_steps = 4 * MR_totalFrames; // 이동할 스텝 수
        		    int MR_stepSize = (MR_endX - MR_startX) / MR_steps; // 한 스텝당 이동 거리

        		    // 프레임별 애니메이션 실행
        		    myMotionTimer = new Timer(enemyFrameDelay, null);
        		    myMotionTimer.addActionListener(e -> {
        		        // 현재 프레임 업데이트
        		        myCurrentFrame = (myCurrentFrame + 1) % MR_totalFrames;

        		        // 현재 위치 업데이트
        		        int currentX = myCharacter.getCurrentX();
        		        if (Math.abs(currentX - MR_endX) > Math.abs(MR_stepSize)) {
        		            myCharacter.setCurrentX(currentX + MR_stepSize); // X 위치 업데이트
        		        }else if (Math.abs(currentX - MR_endX) < Math.abs(MR_stepSize)) {
        		        	myCharacter.setCurrentX(currentX - MR_stepSize);
        		        }

        		        // 화면 갱신
        		        repaint();
        		    });

        		    // 애니메이션 종료 시 처리
        		    new Timer(enemyDuration, e -> {
        		        myMotionTimer.stop();
        		        myCharacter.setCurrentX(MR_startX); // 최종 위치 보정
        		        ((Timer) e.getSource()).stop();
        		    }).start();

        		    myMotionTimer.start();

        		    break;
        		}

                
        	case "HIT":
        		
        		break;
        	case "DEAD":
        		
        		break;
        	case "IDLE":
        		
        		myMotions = myCharacter.getMotions().get("IDLE");
    			myFrameDelay = 200;	// 각 프레임 간격
    			myDuration = Integer.MAX_VALUE;	// 해당 모션의 총 시간
    			
    			myCurrentFrame = 0;
    			myMotionTimer = new Timer(myFrameDelay, null);
    			myMotionTimer.addActionListener(e -> {
    				myCurrentFrame = (myCurrentFrame + 1) % myMotions.length;
    			    repaint();
    			});
    			
    			new Timer(myDuration, e -> {
    				myMotionTimer.stop();
    			    ((Timer) e.getSource()).stop();
    			}).start();
    			
    			myMotionTimer.start();
    			
        		break;
        	}
        	
        	break;
        // Zoro ------------------------------------------------------------		
        case "Zoro":
        	switch (myCharacter.getCurrentMotion()) {
        	case "MOVE":
        		switch (myCharacter.getCurrentCard().getName()) {
        		case "Move Up":
        			
        			myMotions = myCharacter.getMotions().get(myCharacter.getCurrentCard().getName());
        			myFrameDelay = 200;	// 각 프레임 간격
        			myDuration = myFrameDelay * myMotions.length * 8;	// 해당 모션의 총 시간
        			
        			myCurrentFrame = 0;
        			myMotionTimer = new Timer(myFrameDelay, null);
        			myMotionTimer.addActionListener(e -> {
        				myCurrentFrame = (myCurrentFrame + 1) % myMotions.length;
        			    repaint();
        			});
        			
        			new Timer(myDuration, e -> {
        				myMotionTimer.stop();
        			    ((Timer) e.getSource()).stop();
        			}).start();
        			
        			myMotionTimer.start();
        			
        			break;
        		case "Move Down":

        			myMotions = myCharacter.getMotions().get(myCharacter.getCurrentCard().getName());
        			myFrameDelay = 200;	// 각 프레임 간격
        			myDuration = myFrameDelay * myMotions.length * 8;	// 해당 모션의 총 시간
        			
        			myCurrentFrame = 0;
        			myMotionTimer = new Timer(myFrameDelay, null);
        			myMotionTimer.addActionListener(e -> {
        				myCurrentFrame = (myCurrentFrame + 1) % myMotions.length;
        			    repaint();
        			});
        			
        			new Timer(myDuration, e -> {
        				myMotionTimer.stop();
        			    ((Timer) e.getSource()).stop();
        			}).start();
        			
        			myMotionTimer.start();
        			
        			break;
        		case "Move Left":

        			myMotions = myCharacter.getMotions().get(myCharacter.getCurrentCard().getName());
        			myFrameDelay = 200;	// 각 프레임 간격
        			myDuration = myFrameDelay * myMotions.length * 8;	// 해당 모션의 총 시간
        			
        			myCurrentFrame = 0;
        			myMotionTimer = new Timer(myFrameDelay, null);
        			myMotionTimer.addActionListener(e -> {
        				myCurrentFrame = (myCurrentFrame + 1) % myMotions.length;
        			    repaint();
        			});
        			
        			new Timer(myDuration, e -> {
        				myMotionTimer.stop();
        			    ((Timer) e.getSource()).stop();
        			}).start();
        			
        			myMotionTimer.start();
        			
        			break;
        		case "Move Right":

        			myMotions = myCharacter.getMotions().get(myCharacter.getCurrentCard().getName());
        			myFrameDelay = 200;	// 각 프레임 간격
        			myDuration = myFrameDelay * myMotions.length * 8;	// 해당 모션의 총 시간
        			
        			myCurrentFrame = 0;
        			myMotionTimer = new Timer(myFrameDelay, null);
        			myMotionTimer.addActionListener(e -> {
        				myCurrentFrame = (myCurrentFrame + 1) % myMotions.length;
        			    repaint();
        			});
        			
        			new Timer(myDuration, e -> {
        				myMotionTimer.stop();
        			    ((Timer) e.getSource()).stop();
        			}).start();
        			
        			myMotionTimer.start();
        			
        			break;
        		}
        		break;
        	case "ATTACK":
        		switch (myCharacter.getCurrentCard().getName()) {
        		case "Tiger Trap" :
        			myCharacter.playCardSound("Tiger Trap");
        			myMotions = myCharacter.getMotions().get(myCharacter.getCurrentCard().getName());
        			myFrameDelay = 200;	// 각 프레임 간격
        			myDuration = myFrameDelay * myMotions.length * 8;	// 해당 모션의 총 시간
        			
        			myCurrentFrame = 0;
        			myMotionTimer = new Timer(myFrameDelay, null);
        			myMotionTimer.addActionListener(e -> {
        				myCurrentFrame = (myCurrentFrame + 1) % myMotions.length;
        			    repaint();
        			});
        			
        			new Timer(myDuration, e -> {
        				myMotionTimer.stop();
        			    ((Timer) e.getSource()).stop();
        			}).start();
        			
        			myMotionTimer.start();
        		        			
        			break;
        		case "Onigiri" :
        			myCharacter.playCardSound("Onigiri");
        			break;
        		}
        		break;
        	case "HIT":
        		
        		break;
        	case "DEAD":
        		
        		break;
        	case "IDLE":
        		
        		myMotions = myCharacter.getMotions().get("IDLE");
    			myFrameDelay = 200;	// 각 프레임 간격
    			myDuration = Integer.MAX_VALUE;	// 해당 모션의 총 시간
    			
    			myCurrentFrame = 0;
    			myMotionTimer = new Timer(myFrameDelay, null);
    			myMotionTimer.addActionListener(e -> {
    				myCurrentFrame = (myCurrentFrame + 1) % myMotions.length;
    			    repaint();
    			});
    			
    			new Timer(myDuration, e -> {
    				myMotionTimer.stop();
    			    ((Timer) e.getSource()).stop();
    			}).start();
    			
    			myMotionTimer.start();
    			
        		break;
        	}
        	
        	break;
        	
        // Cygnus ------------------------------------------------------------	
        case "Cygnus":
        	switch (myCharacter.getCurrentMotion()) {
        	case "MOVE":
        		switch (myCharacter.getCurrentCard().getName()) {
        		case "Move Up":

        			myMotions = myCharacter.getMotions().get(myCharacter.getCurrentCard().getName());
        			myFrameDelay = 200;	// 각 프레임 간격
        			myDuration = myFrameDelay * myMotions.length * 8;	// 해당 모션의 총 시간
        			
        			myCurrentFrame = 0;
        			myMotionTimer = new Timer(myFrameDelay, null);
        			myMotionTimer.addActionListener(e -> {
        				myCurrentFrame = (myCurrentFrame + 1) % myMotions.length;
        			    repaint();
        			});
        			
        			new Timer(myDuration, e -> {
        				myMotionTimer.stop();
        			    ((Timer) e.getSource()).stop();
        			}).start();
        			
        			myMotionTimer.start();
        			
        			break;
        		case "Move Down":

        			myMotions = myCharacter.getMotions().get(myCharacter.getCurrentCard().getName());
        			myFrameDelay = 200;	// 각 프레임 간격
        			myDuration = myFrameDelay * myMotions.length * 8;	// 해당 모션의 총 시간
        			
        			myCurrentFrame = 0;
        			myMotionTimer = new Timer(myFrameDelay, null);
        			myMotionTimer.addActionListener(e -> {
        				myCurrentFrame = (myCurrentFrame + 1) % myMotions.length;
        			    repaint();
        			});
        			
        			new Timer(myDuration, e -> {
        				myMotionTimer.stop();
        			    ((Timer) e.getSource()).stop();
        			}).start();
        			
        			myMotionTimer.start();
        			
        			break;
        		case "Move Left":

        			myMotions = myCharacter.getMotions().get(myCharacter.getCurrentCard().getName());
        			myFrameDelay = 200;	// 각 프레임 간격
        			myDuration = myFrameDelay * myMotions.length * 8;	// 해당 모션의 총 시간
        			
        			myCurrentFrame = 0;
        			myMotionTimer = new Timer(myFrameDelay, null);
        			myMotionTimer.addActionListener(e -> {
        				myCurrentFrame = (myCurrentFrame + 1) % myMotions.length;
        			    repaint();
        			});
        			
        			new Timer(myDuration, e -> {
        				myMotionTimer.stop();
        			    ((Timer) e.getSource()).stop();
        			}).start();
        			
        			myMotionTimer.start();
        			
        			break;
        		case "Move Right":

        			myMotions = myCharacter.getMotions().get(myCharacter.getCurrentCard().getName());
        			myFrameDelay = 200;	// 각 프레임 간격
        			myDuration = myFrameDelay * myMotions.length * 8;	// 해당 모션의 총 시간
        			
        			myCurrentFrame = 0;
        			myMotionTimer = new Timer(myFrameDelay, null);
        			myMotionTimer.addActionListener(e -> {
        				myCurrentFrame = (myCurrentFrame + 1) % myMotions.length;
        			    repaint();
        			});
        			
        			new Timer(myDuration, e -> {
        				myMotionTimer.stop();
        			    ((Timer) e.getSource()).stop();
        			}).start();
        			
        			myMotionTimer.start();
        			
        			break;
        		}
        		break;
        	case "ATTACK":
        		switch (myCharacter.getCurrentCard().getName()) {
        		case "Galactic Burst":
        			myCharacter.playCardSound("Galactic Burst");
        			myMotions = myCharacter.getMotions().get(myCharacter.getCurrentCard().getName());
        			myFrameDelay = 200;	// 각 프레임 간격
        			myDuration = myFrameDelay * myMotions.length * 8;	// 해당 모션의 총 시간
        			
        			myCurrentFrame = 0;
        			myMotionTimer = new Timer(myFrameDelay, null);
        			myMotionTimer.addActionListener(e -> {
        				myCurrentFrame = (myCurrentFrame + 1) % myMotions.length;
        			    repaint();
        			});
        			
        			new Timer(myDuration, e -> {
        				myMotionTimer.stop();
        			    ((Timer) e.getSource()).stop();
        			}).start();
        			
        			myMotionTimer.start();
        			
        			break;
        		case "Phoenix Breath":
        			myCharacter.playCardSound("Phoenix Breath");
        			break;
        		}
        		break;
        	case "HIT":
        		
        		break;
        	case "DEAD":
        		
        		break;
        	case "IDLE":

    			myMotions = myCharacter.getMotions().get("IDLE");
    			myFrameDelay = 200;	// 각 프레임 간격
    			myDuration = myFrameDelay * myMotions.length * 8;	// 해당 모션의 총 시간
    			
    			myCurrentFrame = 0;
    			myMotionTimer = new Timer(myFrameDelay, null);
    			myMotionTimer.addActionListener(e -> {
    				myCurrentFrame = (myCurrentFrame + 1) % myMotions.length;
    			    repaint();
    			});
    			
    			new Timer(myDuration, e -> {
    				myMotionTimer.stop();
    			    ((Timer) e.getSource()).stop();
    			}).start();
    			
    			myMotionTimer.start();
    			
    			break;
        	}
        	
        	break;
        	
        	
        // Finn ------------------------------------------------------------	
        case "Finn":
            switch (myCharacter.getCurrentMotion()) {
            case "MOVE":
                switch (myCharacter.getCurrentCard().getName()) {
                case "Move Up":
                    // 시작 위치와 이동 거리 설정
                    int MU_startY = myCharacter.getCurrentY(); // 시작 위치 Y
                    int MU_endY = MU_startY - 60; // 목표 위치 (위쪽으로 60px 이동)

                    // 애니메이션 관련 설정
                    myMotions = myCharacter.getMotions().get(myCharacter.getCurrentCard().getName());
                    myFrameDelay = 100; // 각 프레임 간격
                    int MU_totalFrames = myMotions.length; // 애니메이션 총 프레임 수
                    myDuration = 5 * myFrameDelay * MU_totalFrames; // 애니메이션 총 시간

                    myCurrentFrame = 0;

                    // 이동 속도 계산
                    int MU_steps = 5 * MU_totalFrames; // 이동할 스텝 수
                    int MU_stepSize = (MU_endY - MU_startY) / MU_steps; // 한 스텝당 이동 거리

                    // 프레임별 애니메이션 실행
                    Timer myMotionTimer = new Timer(myFrameDelay, null);
                    myMotionTimer.addActionListener(e -> {
                        // 현재 프레임 업데이트
                        myCurrentFrame = (myCurrentFrame + 1) % MU_totalFrames;

                        // 현재 위치 업데이트
                        int currentY = myCharacter.getCurrentY();
                        if (Math.abs(currentY - MU_endY) > Math.abs(MU_stepSize)) {
                            myCharacter.setCurrentY(currentY + MU_stepSize); // Y 위치 업데이트
                        }

                        // 화면 갱신
                        repaint();
                    });

                    // 애니메이션 종료 시 처리
                    new Timer(myDuration, e -> {
                        myMotionTimer.stop();
                        myCharacter.setCurrentY(MU_endY); // 최종 위치 보정
                        ((Timer) e.getSource()).stop();
                    }).start();

                    myMotionTimer.start();

                    break;
                case "Move Down":
                    // 시작 위치와 이동 거리 설정
                    int MD_startY = myCharacter.getCurrentY(); // 시작 위치 Y
                    int MD_endY = MD_startY + 60; // 목표 위치 (아래쪽으로 60px 이동)

                    // 애니메이션 관련 설정
                    myMotions = myCharacter.getMotions().get(myCharacter.getCurrentCard().getName());
                    myFrameDelay = 100; // 각 프레임 간격
                    int MD_totalFrames = myMotions.length; // 애니메이션 총 프레임 수
                    myDuration = 6 * myFrameDelay * MD_totalFrames; // 애니메이션 총 시간

                    myCurrentFrame = 0;

                    // 이동 속도 계산
                    int MD_steps = 6 * MD_totalFrames; // 이동할 스텝 수
                    int MD_stepSize = (MD_endY - MD_startY) / MD_steps; // 한 스텝당 이동 거리

                    // 프레임별 애니메이션 실행
                    myMotionTimer = new Timer(myFrameDelay, null);
                    myMotionTimer.addActionListener(e -> {
                        // 현재 프레임 업데이트
                        myCurrentFrame = (myCurrentFrame + 1) % MD_totalFrames;

                        // 현재 위치 업데이트
                        int currentY = myCharacter.getCurrentY();
                        if (Math.abs(currentY - MD_endY) > Math.abs(MD_stepSize)) {
                            myCharacter.setCurrentY(currentY + MD_stepSize); // Y 위치 업데이트
                        }

                        // 화면 갱신
                        repaint();
                    });

                    // 애니메이션 종료 시 처리
                    new Timer(myDuration, e -> {
                        myMotionTimer.stop();
                        myCharacter.setCurrentY(MD_endY); // 최종 위치 보정
                        ((Timer) e.getSource()).stop();
                    }).start();

                    myMotionTimer.start();

                    break;

                case "Move Left":
                    // 시작 위치와 이동 거리 설정
                    int ML_startX = myCharacter.getCurrentX(); // 시작 위치 X
                    int ML_endX = ML_startX - 150; // 목표 위치 (왼쪽으로 150px 이동)

                    // 애니메이션 관련 설정
                    myMotions = myCharacter.getMotions().get(myCharacter.getCurrentCard().getName());
                    myFrameDelay = 100; // 각 프레임 간격
                    int ML_totalFrames = myMotions.length; // 애니메이션 총 프레임 수
                    myDuration = 4 * myFrameDelay * ML_totalFrames; // 애니메이션 총 시간

                    myCurrentFrame = 0;

                    // 이동 속도 계산
                    int ML_steps = 4 * ML_totalFrames; // 이동할 스텝 수
                    int ML_stepSize = (ML_endX - ML_startX) / ML_steps; // 한 스텝당 이동 거리

                    // 프레임별 애니메이션 실행
                    myMotionTimer = new Timer(myFrameDelay, null);
                    myMotionTimer.addActionListener(e -> {
                        // 현재 프레임 업데이트
                        myCurrentFrame = (myCurrentFrame + 1) % ML_totalFrames;

                        // 현재 위치 업데이트
                        int currentX = myCharacter.getCurrentX();
                        if (Math.abs(currentX - ML_endX) > Math.abs(ML_stepSize)) {
                            myCharacter.setCurrentX(currentX + ML_stepSize); // X 위치 업데이트
                        }

                        // 화면 갱신
                        repaint();
                    });

                    // 애니메이션 종료 시 처리
                    new Timer(myDuration, e -> {
                        myMotionTimer.stop();
                        myCharacter.setCurrentX(ML_endX); // 최종 위치 보정
                        ((Timer) e.getSource()).stop();
                    }).start();

                    myMotionTimer.start();

                    break;

                case "Move Right":
                    // 시작 위치와 이동 거리 설정
                    int MR_startX = myCharacter.getCurrentX(); // 시작 위치 X
                    int MR_endX = MR_startX + 150; // 목표 위치 (오른쪽으로 150px 이동)

                    // 애니메이션 관련 설정
                    myMotions = myCharacter.getMotions().get(myCharacter.getCurrentCard().getName());
                    myFrameDelay = 100; // 각 프레임 간격
                    int MR_totalFrames = myMotions.length; // 애니메이션 총 프레임 수
                    myDuration = 4 * myFrameDelay * MR_totalFrames; // 애니메이션 총 시간

                    myCurrentFrame = 0;

                    // 이동 속도 계산
                    int MR_steps = 4 * MR_totalFrames; // 이동할 스텝 수
                    int MR_stepSize = (MR_endX - MR_startX) / MR_steps; // 한 스텝당 이동 거리

                    // 프레임별 애니메이션 실행
                    myMotionTimer = new Timer(myFrameDelay, null);
                    myMotionTimer.addActionListener(e -> {
                        // 현재 프레임 업데이트
                        myCurrentFrame = (myCurrentFrame + 1) % MR_totalFrames;

                        // 현재 위치 업데이트
                        int currentX = myCharacter.getCurrentX();
                        if (Math.abs(currentX - MR_endX) > Math.abs(MR_stepSize)) {
                            myCharacter.setCurrentX(currentX + MR_stepSize); // X 위치 업데이트
                        }

                        // 화면 갱신
                        repaint();
                    });

                    // 애니메이션 종료 시 처리
                    new Timer(myDuration, e -> {
                        myMotionTimer.stop();
                        myCharacter.setCurrentX(MR_endX); // 최종 위치 보정
                        ((Timer) e.getSource()).stop();
                    }).start();

                    myMotionTimer.start();

                    break;
                }
                break;

            case "ATTACK":
                switch (myCharacter.getCurrentCard().getName()) {
                case "Sword Slash":
                	myCharacter.playCardSound("Sword Slash");
                    myMotions = myCharacter.getMotions().get(myCharacter.getCurrentCard().getName());
                    myFrameDelay = 100; // 각 프레임 간격
                    myDuration = myFrameDelay * myMotions.length * 4; // 애니메이션 총 시간

                    myCurrentFrame = 0;

                    myMotionTimer = new Timer(myFrameDelay, null);
                    myMotionTimer.addActionListener(e -> {
                        myCurrentFrame = (myCurrentFrame + 1) % myMotions.length;
                        repaint();
                    });

                    new Timer(myDuration, e -> {
                        myMotionTimer.stop();
                        ((Timer) e.getSource()).stop();
                    }).start();

                    myMotionTimer.start();

                    break;
                case "Stretch Punch":
                	myCharacter.playCardSound("Stretch Punch");
                    myMotions = myCharacter.getMotions().get(myCharacter.getCurrentCard().getName());
                    myFrameDelay = 100; // 각 프레임 간격
                    myDuration = myFrameDelay * myMotions.length * 4; // 애니메이션 총 시간

                    myCurrentFrame = 0;

                    myMotionTimer = new Timer(myFrameDelay, null);
                    myMotionTimer.addActionListener(e -> {
                        myCurrentFrame = (myCurrentFrame + 1) % myMotions.length;
                        repaint();
                    });

                    new Timer(myDuration, e -> {
                        myMotionTimer.stop();
                        ((Timer) e.getSource()).stop();
                    }).start();

                    myMotionTimer.start();

                    break;
                }
                break;

            case "HIT":
                // HIT 관련 처리
                myMotions = myCharacter.getMotions().get(myCharacter.getCurrentCard().getName());
                myFrameDelay = 100; // 각 프레임 간격
                myDuration = myFrameDelay * myMotions.length ; // 애니메이션 총 시간

                myCurrentFrame = 0;

                myMotionTimer = new Timer(myFrameDelay, null);
                myMotionTimer.addActionListener(e -> {
                    myCurrentFrame = (myCurrentFrame + 1) % myMotions.length;
                    repaint();
                });

                new Timer(myDuration, e -> {
                    myMotionTimer.stop();
                    ((Timer) e.getSource()).stop();
                }).start();

                myMotionTimer.start();

                break;

            case "DEAD":
                // DEAD 관련 처리
                myMotions = myCharacter.getMotions().get(myCharacter.getCurrentCard().getName());
                myFrameDelay = 200; // 각 프레임 간격
                myDuration = myFrameDelay * myMotions.length ; // 애니메이션 총 시간

                myCurrentFrame = 0;

                myMotionTimer = new Timer(myFrameDelay, null);
                myMotionTimer.addActionListener(e -> {
                    myCurrentFrame = (myCurrentFrame + 1) % myMotions.length;
                    repaint();
                });

                new Timer(myDuration, e -> {
                    myMotionTimer.stop();
                    ((Timer) e.getSource()).stop();
                }).start();

                myMotionTimer.start();

                break;
                
            case "IDLE":
                myMotions = myCharacter.getMotions().get("IDLE");
                myFrameDelay = 100; // 각 프레임 간격
                myDuration = Integer.MAX_VALUE; // 해당 모션의 총 시간

                myCurrentFrame = 0;

                myMotionTimer = new Timer(myFrameDelay, null);
                myMotionTimer.addActionListener(e -> {
                    myCurrentFrame = (myCurrentFrame + 1) % myMotions.length;
                    repaint();
                });

                new Timer(myDuration, e -> {
                    myMotionTimer.stop();
                    ((Timer) e.getSource()).stop();
                }).start();

                myMotionTimer.start();

                break;
            }

            break;

        	
        
        	
        default:
        	System.out.println("엄준식");
        	break;
		}
        
		
		
		
        
        // 상대 캐릭터 그리기
		enemyCharacter = gameState.getEnemyCharacter();
    	if (gameState.getClientNumber() == 1) {
    		enemyCharacter.setCurrentX(gridStartX + gameState.getEnemyPosition()[0] * gridWidth + gridClient2X);
    		enemyCharacter.setCurrentY(gridStartY + gameState.getEnemyPosition()[1] * gridHeight + gridClient2Y-60);
    	} else {
    		enemyCharacter.setCurrentX(gridStartX + gameState.getEnemyPosition()[0] * gridWidth + gridClient1X);
    		enemyCharacter.setCurrentY(gridStartY + gameState.getEnemyPosition()[1] * gridHeight + gridClient1Y-60);
    	}
        
        switch (enemyCharacter.getName()) {
        
        	
        // Doraemon ------------------------------------------------------------	
        case "Doraemon":
        	switch (enemyCharacter.getCurrentMotion()) {
        	case "MOVE":
        		switch (enemyCharacter.getCurrentCard().getName()) {
        		case "Move Up":
        		    // 시작 위치와 이동 거리 설정
        		    int MU_startY = enemyCharacter.getCurrentY(); // 시작 위치 Y
        		    int MU_endY = MU_startY - 60; // 목표 위치 (위쪽으로 60px 이동)

        		    // 애니메이션 관련 설정
        		    enemyMotions = enemyCharacter.getMotions().get(enemyCharacter.getCurrentCard().getName());
        		    enemyFrameDelay = 200; // 각 프레임 간격
        		    int MU_totalFrames = enemyMotions.length; // 애니메이션 총 프레임 수
        		    enemyDuration = 7 * enemyFrameDelay * enemyMotions.length; // 애니메이션 총 시간

        		    enemyCurrentFrame = 0;

        		    // 이동 속도 계산
        		    int MU_steps = 7 * MU_totalFrames; // 이동할 스텝 수
        		    int MU_stepSize = (MU_endY - MU_startY) / MU_steps; // 한 스텝당 이동 거리

        		    // 프레임별 애니메이션 실행
        		    enemyMotionTimer = new Timer(enemyFrameDelay, null);
        		    enemyMotionTimer.addActionListener(e -> {
        		        // 현재 프레임 업데이트
        		        enemyCurrentFrame = (enemyCurrentFrame + 1) % MU_totalFrames;

        		        // 현재 위치 업데이트
        		        int currentY = enemyCharacter.getCurrentY();
        		        if (Math.abs(currentY - MU_endY) > Math.abs(MU_stepSize)) {
        		            enemyCharacter.setCurrentY(currentY + MU_stepSize); // Y 위치 업데이트
        		        }

        		        // 화면 갱신
        		        repaint();
        		    });

        		    // 애니메이션 종료 시 처리
        		    new Timer(enemyDuration, e -> {
        		        enemyMotionTimer.stop();
        		        enemyCharacter.setCurrentY(MU_endY); // 최종 위치 보정
        		        ((Timer) e.getSource()).stop();
        		    }).start();

        		    enemyMotionTimer.start();

        		    break;
        		case "Move Down":
        		    // 시작 위치와 이동 거리 설정
        		    int MD_startY = enemyCharacter.getCurrentY(); // 시작 위치 Y
        		    int MD_endY = MD_startY + 60; // 목표 위치 (아래쪽으로 60px 이동)

        		    // 애니메이션 관련 설정
        		    enemyMotions = enemyCharacter.getMotions().get(enemyCharacter.getCurrentCard().getName());
        		    enemyFrameDelay = 200; // 각 프레임 간격
        		    int MD_totalFrames = enemyMotions.length; // 애니메이션 총 프레임 수
        		    enemyDuration = 7 * enemyFrameDelay * MD_totalFrames; // 애니메이션 총 시간

        		    enemyCurrentFrame = 0;

        		    // 이동 속도 계산
        		    int MD_steps = 7 * MD_totalFrames; // 이동할 스텝 수
        		    int MD_stepSize = (MD_endY - MD_startY) / MD_steps; // 한 스텝당 이동 거리

        		    // 프레임별 애니메이션 실행
        		    enemyMotionTimer = new Timer(enemyFrameDelay, null);
        		    enemyMotionTimer.addActionListener(e -> {
        		        // 현재 프레임 업데이트
        		        enemyCurrentFrame = (enemyCurrentFrame + 1) % MD_totalFrames;

        		        // 현재 위치 업데이트
        		        int currentY = enemyCharacter.getCurrentY();
        		        if (Math.abs(currentY - MD_endY) > Math.abs(MD_stepSize)) {
        		            enemyCharacter.setCurrentY(currentY + MD_stepSize); // Y 위치 업데이트
        		        }

        		        // 화면 갱신
        		        repaint();
        		    });

        		    // 애니메이션 종료 시 처리
        		    new Timer(enemyDuration, e -> {
        		        enemyMotionTimer.stop();
        		        enemyCharacter.setCurrentY(MD_endY); // 최종 위치 보정
        		        ((Timer) e.getSource()).stop();
        		    }).start();

        		    enemyMotionTimer.start();

        		    break;

        		case "Move Left":
        		    // 시작 위치와 이동 거리 설정
        		    int ML_startX = enemyCharacter.getCurrentX(); // 시작 위치 X
        		    int ML_endX = ML_startX - 150; // 목표 위치 (왼쪽으로 100px 이동)

        		    // 애니메이션 관련 설정
        		    enemyMotions = enemyCharacter.getMotions().get(enemyCharacter.getCurrentCard().getName());
        		    enemyFrameDelay = 200; // 각 프레임 간격
        		    int ML_totalFrames = enemyMotions.length; // 애니메이션 총 프레임 수
        		    enemyDuration = 7*enemyFrameDelay * ML_totalFrames; // 애니메이션 총 시간

        		    enemyCurrentFrame = 0;

        		    // 이동 속도 계산
        		    int ML_steps = 7*ML_totalFrames; // 이동할 스텝 수 (프레임 수와 동일)
        		    int ML_stepSize = (ML_endX - ML_startX) / ML_steps; // 한 스텝당 이동 거리

        		    // 프레임별 애니메이션 실행
        		    enemyMotionTimer = new Timer(enemyFrameDelay, null);
        		    enemyMotionTimer.addActionListener(e -> {
        		        // 현재 프레임 업데이트
        		        enemyCurrentFrame = (enemyCurrentFrame + 1) % ML_totalFrames;

        		        // 현재 위치 업데이트
        		        int currentX = enemyCharacter.getCurrentX();
        		        if (Math.abs(currentX - ML_endX) > Math.abs(ML_stepSize)) {
        		            enemyCharacter.setCurrentX(currentX + ML_stepSize); // X 위치 업데이트
        		        }

        		        // 화면 갱신
        		        repaint();
        		    });

        		    // 애니메이션 종료 시 처리
        		    new Timer(enemyDuration, e -> {
        		        enemyMotionTimer.stop();
        		        enemyCharacter.setCurrentX(ML_endX); // 최종 위치 보정
        		        ((Timer) e.getSource()).stop();
        		    }).start();

        		    enemyMotionTimer.start();

        		    break;

        		case "Move Right":
        		    // 시작 위치와 이동 거리 설정
        		    int MR_startX = enemyCharacter.getCurrentX(); // 시작 위치 X
        		    int MR_endX = MR_startX + 150; // 목표 위치 (오른쪽으로 150px 이동)

        		    // 애니메이션 관련 설정
        		    enemyMotions = enemyCharacter.getMotions().get(enemyCharacter.getCurrentCard().getName());
        		    enemyFrameDelay = 200; // 각 프레임 간격
        		    int MR_totalFrames = enemyMotions.length; // 애니메이션 총 프레임 수
        		    enemyDuration = 7 * enemyFrameDelay * MR_totalFrames; // 애니메이션 총 시간

        		    enemyCurrentFrame = 0;

        		    // 이동 속도 계산
        		    int MR_steps = 7 * MR_totalFrames; // 이동할 스텝 수
        		    int MR_stepSize = (MR_endX - MR_startX) / MR_steps; // 한 스텝당 이동 거리

        		    // 프레임별 애니메이션 실행
        		    enemyMotionTimer = new Timer(enemyFrameDelay, null);
        		    enemyMotionTimer.addActionListener(e -> {
        		        // 현재 프레임 업데이트
        		        enemyCurrentFrame = (enemyCurrentFrame + 1) % MR_totalFrames;

        		        // 현재 위치 업데이트
        		        int currentX = enemyCharacter.getCurrentX();
        		        if (Math.abs(currentX - MR_endX) > Math.abs(MR_stepSize)) {
        		            enemyCharacter.setCurrentX(currentX + MR_stepSize); // X 위치 업데이트
        		        }

        		        // 화면 갱신
        		        repaint();
        		    });

        		    // 애니메이션 종료 시 처리
        		    new Timer(enemyDuration, e -> {
        		        enemyMotionTimer.stop();
        		        enemyCharacter.setCurrentX(MR_endX); // 최종 위치 보정
        		        ((Timer) e.getSource()).stop();
        		    }).start();

        		    enemyMotionTimer.start();

        		    break;
        		}

        		break;
        		
        	case "ATTACK":
        		switch (enemyCharacter.getCurrentCard().getName()) {
        		case "Air Cannon!" :
        			enemyCharacter.playCardSound("Air Cannon!");
        			enemyMotions = enemyCharacter.getMotions().get(enemyCharacter.getCurrentCard().getName());
        			enemyFrameDelay = 200;	// 각 프레임 간격
        			enemyDuration = enemyFrameDelay * enemyMotions.length * 8;	// 해당 모션의 총 시간
        			
        			enemyCurrentFrame = 0;
        			enemyMotionTimer = new Timer(enemyFrameDelay, null);
        			enemyMotionTimer.addActionListener(e -> {
        				enemyCurrentFrame = (enemyCurrentFrame + 1) % enemyMotions.length;
        			    repaint();
        			});
        			
        			new Timer(enemyDuration, e -> {
        				enemyMotionTimer.stop();
        			    ((Timer) e.getSource()).stop();
        			}).start();
        			
        			enemyMotionTimer.start();
        			
        			break;
        		case "Bamboo Helicopter!":
        			enemyCharacter.playCardSound("Bamboo Helicopter!");
           		    int MR_startX = enemyCharacter.getCurrentX(); // 시작 위치 X
        		    int MR_endX = MR_startX + 150*4; // 목표 위치 (오른쪽으로 150px 이동)

        		    // 애니메이션 관련 설정
        		    enemyMotions = enemyCharacter.getMotions().get(enemyCharacter.getCurrentCard().getName());
        		    enemyFrameDelay = 100; // 각 프레임 간격
        		    int MR_totalFrames = enemyMotions.length; // 애니메이션 총 프레임 수
        		    enemyDuration = 8 * enemyFrameDelay * MR_totalFrames; // 애니메이션 총 시간

        		    enemyCurrentFrame = 0;

        		    // 이동 속도 계산
        		    int MR_steps = 4 * MR_totalFrames; // 이동할 스텝 수
        		    int MR_stepSize = (MR_endX - MR_startX) / MR_steps; // 한 스텝당 이동 거리

        		    // 프레임별 애니메이션 실행
        		    enemyMotionTimer = new Timer(enemyFrameDelay, null);
        		    enemyMotionTimer.addActionListener(e -> {
        		        // 현재 프레임 업데이트
        		        enemyCurrentFrame = (enemyCurrentFrame + 1) % MR_totalFrames;

        		        // 현재 위치 업데이트
        		        int currentX = enemyCharacter.getCurrentX();
        		        if (Math.abs(currentX - MR_endX) > Math.abs(MR_stepSize)) {
        		            enemyCharacter.setCurrentX(currentX + MR_stepSize); // X 위치 업데이트
        		        }else if (Math.abs(currentX - MR_endX) < Math.abs(MR_stepSize)) {
        		        	enemyCharacter.setCurrentX(currentX - MR_stepSize);
        		        }

        		        // 화면 갱신
        		        repaint();
        		    });

        		    // 애니메이션 종료 시 처리
        		    new Timer(enemyDuration, e -> {
        		        enemyMotionTimer.stop();
        		        enemyCharacter.setCurrentX(MR_startX); // 최종 위치 보정
        		        ((Timer) e.getSource()).stop();
        		    }).start();

        		    enemyMotionTimer.start();

        		    break;
        		}
        		break;
        		
        	case "HIT":
        		
        		break;
        	case "DEAD":
        		
        		break;
        	case "IDLE":

        		enemyMotions = enemyCharacter.getMotions().get("IDLE");
        		enemyFrameDelay = 200;	// 각 프레임 간격
        		enemyDuration = Integer.MAX_VALUE;	// 해당 모션의 총 시간
    			
        		enemyCurrentFrame = 0;
        		enemyMotionTimer = new Timer(myFrameDelay, null);
        		enemyMotionTimer.addActionListener(e -> {
        			enemyCurrentFrame = (enemyCurrentFrame + 1) % enemyMotions.length;
    			    repaint();
    			});
    			
    			new Timer(enemyDuration, e -> {
    				enemyMotionTimer.stop();
    			    ((Timer) e.getSource()).stop();
    			}).start();
    			
    			enemyMotionTimer.start();
    			
        		break;
        	}
        	
        	break;
        	
        // Zoro ------------------------------------------------------------		
        case "Zoro":
         	switch (enemyCharacter.getCurrentMotion()) {
        	case "MOVE":
        		switch (enemyCharacter.getCurrentCard().getName()) {
        		case "Move Up":

        			enemyMotions = enemyCharacter.getMotions().get(enemyCharacter.getCurrentCard().getName());
        			enemyFrameDelay = 200;	// 각 프레임 간격
        			enemyDuration = enemyFrameDelay * enemyMotions.length * 8;	// 해당 모션의 총 시간
        			
        			enemyCurrentFrame = 0;
        			enemyMotionTimer = new Timer(enemyFrameDelay, null);
        			enemyMotionTimer.addActionListener(e -> {
        				enemyCurrentFrame = (enemyCurrentFrame + 1) % enemyMotions.length;
        			    repaint();
        			});
        			
        			new Timer(enemyDuration, e -> {
        				enemyMotionTimer.stop();
        			    ((Timer) e.getSource()).stop();
        			}).start();
        			
        			enemyMotionTimer.start();
        			
        			break;
        		case "Move Down":

        			enemyMotions = enemyCharacter.getMotions().get(enemyCharacter.getCurrentCard().getName());
        			enemyFrameDelay = 200;	// 각 프레임 간격
        			enemyDuration = enemyFrameDelay * enemyMotions.length * 8;	// 해당 모션의 총 시간
        			
        			enemyCurrentFrame = 0;
        			enemyMotionTimer = new Timer(enemyFrameDelay, null);
        			enemyMotionTimer.addActionListener(e -> {
        				enemyCurrentFrame = (enemyCurrentFrame + 1) % enemyMotions.length;
        			    repaint();
        			});
        			
        			new Timer(enemyDuration, e -> {
        				enemyMotionTimer.stop();
        			    ((Timer) e.getSource()).stop();
        			}).start();
        			
        			enemyMotionTimer.start();
        			
        			break;
        		case "Move Left":

        			enemyMotions = enemyCharacter.getMotions().get(enemyCharacter.getCurrentCard().getName());
        			enemyFrameDelay = 200;	// 각 프레임 간격
        			enemyDuration = enemyFrameDelay * enemyMotions.length * 8;	// 해당 모션의 총 시간
        			
        			enemyCurrentFrame = 0;
        			enemyMotionTimer = new Timer(enemyFrameDelay, null);
        			enemyMotionTimer.addActionListener(e -> {
        				enemyCurrentFrame = (enemyCurrentFrame + 1) % enemyMotions.length;
        			    repaint();
        			});
        			
        			new Timer(enemyDuration, e -> {
        				enemyMotionTimer.stop();
        			    ((Timer) e.getSource()).stop();
        			}).start();
        			
        			enemyMotionTimer.start();
        			
        			break;
        		case "Move Right":

        			enemyMotions = enemyCharacter.getMotions().get(enemyCharacter.getCurrentCard().getName());
        			enemyFrameDelay = 200;	// 각 프레임 간격
        			enemyDuration = enemyFrameDelay * enemyMotions.length * 8;	// 해당 모션의 총 시간
        			
        			enemyCurrentFrame = 0;
        			enemyMotionTimer = new Timer(enemyFrameDelay, null);
        			enemyMotionTimer.addActionListener(e -> {
        				enemyCurrentFrame = (enemyCurrentFrame + 1) % enemyMotions.length;
        			    repaint();
        			});
        			
        			new Timer(enemyDuration, e -> {
        				enemyMotionTimer.stop();
        			    ((Timer) e.getSource()).stop();
        			}).start();
        			
        			enemyMotionTimer.start();
        			
        			break;
        		}
        		break;
        	case "ATTACK":
        		switch (enemyCharacter.getCurrentCard().getName()) {
        		// ... skill들 넣으셈
        		case "Tiger Trap" :
        			enemyCharacter.playCardSound("Tiger Trap");
        			enemyMotions = enemyCharacter.getMotions().get(enemyCharacter.getCurrentCard().getName());
        			enemyFrameDelay = 200;	// 각 프레임 간격
        			enemyDuration = enemyFrameDelay * enemyMotions.length * 8;	// 해당 모션의 총 시간
        			
        			enemyCurrentFrame = 0;
        			enemyMotionTimer = new Timer(enemyFrameDelay, null);
        			enemyMotionTimer.addActionListener(e -> {
        				enemyCurrentFrame = (enemyCurrentFrame + 1) % enemyMotions.length;
        			    repaint();
        			});
        			
        			new Timer(enemyDuration, e -> {
        				enemyMotionTimer.stop();
        			    ((Timer) e.getSource()).stop();
        			}).start();
        			
        			enemyMotionTimer.start();
        			
        			break;
        		case "Onigiri" :
        			enemyCharacter.playCardSound("Onigiri");
        			break;
        		}
        		break;
        	case "HIT":
        		
        		break;
        	case "DEAD":
        		
        		break;
        	case "IDLE":

        		enemyMotions = enemyCharacter.getMotions().get("IDLE");
        		enemyFrameDelay = 200;	// 각 프레임 간격
        		enemyDuration = Integer.MAX_VALUE;	// 해당 모션의 총 시간
    			
        		enemyCurrentFrame = 0;
        		enemyMotionTimer = new Timer(myFrameDelay, null);
        		enemyMotionTimer.addActionListener(e -> {
        			enemyCurrentFrame = (enemyCurrentFrame + 1) % enemyMotions.length;
    			    repaint();
    			});
    			
    			new Timer(enemyDuration, e -> {
    				enemyMotionTimer.stop();
    			    ((Timer) e.getSource()).stop();
    			}).start();
    			
    			enemyMotionTimer.start();
    			
        		break;
        	}
        	
        	break;
        	
        // Cygnus ------------------------------------------------------------	
        case "Cygnus":
        	switch (enemyCharacter.getCurrentMotion()) {
        	case "MOVE":
        		switch (enemyCharacter.getCurrentCard().getName()) {
        		case "Move Up":

        			enemyMotions = enemyCharacter.getMotions().get(enemyCharacter.getCurrentCard().getName());
        			enemyFrameDelay = 200;	// 각 프레임 간격
        			enemyDuration = enemyFrameDelay * enemyMotions.length * 8;	// 해당 모션의 총 시간
        			
        			enemyCurrentFrame = 0;
        			enemyMotionTimer = new Timer(enemyFrameDelay, null);
        			enemyMotionTimer.addActionListener(e -> {
        				enemyCurrentFrame = (enemyCurrentFrame + 1) % enemyMotions.length;
        			    repaint();
        			});
        			
        			new Timer(enemyDuration, e -> {
        				enemyMotionTimer.stop();
        			    ((Timer) e.getSource()).stop();
        			}).start();
        			
        			enemyMotionTimer.start();
        			
        			break;
        		case "Move Down":

        			enemyMotions = enemyCharacter.getMotions().get(enemyCharacter.getCurrentCard().getName());
        			enemyFrameDelay = 200;	// 각 프레임 간격
        			enemyDuration = enemyFrameDelay * enemyMotions.length * 8;	// 해당 모션의 총 시간
        			
        			enemyCurrentFrame = 0;
        			enemyMotionTimer = new Timer(enemyFrameDelay, null);
        			enemyMotionTimer.addActionListener(e -> {
        				enemyCurrentFrame = (enemyCurrentFrame + 1) % enemyMotions.length;
        			    repaint();
        			});
        			
        			new Timer(enemyDuration, e -> {
        				enemyMotionTimer.stop();
        			    ((Timer) e.getSource()).stop();
        			}).start();
        			
        			enemyMotionTimer.start();
        			
        			break;
        		case "Move Left":

        			enemyMotions = enemyCharacter.getMotions().get(enemyCharacter.getCurrentCard().getName());
        			enemyFrameDelay = 200;	// 각 프레임 간격
        			enemyDuration = enemyFrameDelay * enemyMotions.length * 8;	// 해당 모션의 총 시간
        			
        			enemyCurrentFrame = 0;
        			enemyMotionTimer = new Timer(enemyFrameDelay, null);
        			enemyMotionTimer.addActionListener(e -> {
        				enemyCurrentFrame = (enemyCurrentFrame + 1) % enemyMotions.length;
        			    repaint();
        			});
        			
        			new Timer(enemyDuration, e -> {
        				enemyMotionTimer.stop();
        			    ((Timer) e.getSource()).stop();
        			}).start();
        			
        			enemyMotionTimer.start();
        			
        			break;
        		case "Move Right":

        			enemyMotions = enemyCharacter.getMotions().get(enemyCharacter.getCurrentCard().getName());
        			enemyFrameDelay = 200;	// 각 프레임 간격
        			enemyDuration = enemyFrameDelay * enemyMotions.length * 8;	// 해당 모션의 총 시간
        			
        			enemyCurrentFrame = 0;
        			enemyMotionTimer = new Timer(enemyFrameDelay, null);
        			enemyMotionTimer.addActionListener(e -> {
        				enemyCurrentFrame = (enemyCurrentFrame + 1) % enemyMotions.length;
        			    repaint();
        			});
        			
        			new Timer(enemyDuration, e -> {
        				enemyMotionTimer.stop();
        			    ((Timer) e.getSource()).stop();
        			}).start();
        			
        			enemyMotionTimer.start();
        			
        			break;
        		}
        		break;
        	case "ATTACK":
        		switch (enemyCharacter.getCurrentCard().getName()) {
        		case "Galactic Burst":
        			enemyCharacter.playCardSound("Galactic Burst");
        			enemyMotions = enemyCharacter.getMotions().get(enemyCharacter.getCurrentCard().getName());
        			enemyFrameDelay = 200;	// 각 프레임 간격
        			enemyDuration = enemyFrameDelay * enemyMotions.length * 8;	// 해당 모션의 총 시간
        			
        			enemyCurrentFrame = 0;
        			enemyMotionTimer = new Timer(enemyFrameDelay, null);
        			enemyMotionTimer.addActionListener(e -> {
        				enemyCurrentFrame = (enemyCurrentFrame + 1) % enemyMotions.length;
        			    repaint();
        			});
        			
        			new Timer(enemyDuration, e -> {
        				enemyMotionTimer.stop();
        			    ((Timer) e.getSource()).stop();
        			}).start();
        			
        			enemyMotionTimer.start();
        			
        			break;
        		case "Phoenix Breath":
        			enemyCharacter.playCardSound("Phoenix Breath");
        			break;
        		}
        		break;
        	case "HIT":
        		
        		break;
        	case "DEAD":
        		
        		break;
        	case "IDLE":

    			enemyMotions = enemyCharacter.getMotions().get("IDLE");
    			enemyFrameDelay = 200;	// 각 프레임 간격
    			enemyDuration = enemyFrameDelay * enemyMotions.length * 8;	// 해당 모션의 총 시간
    			
    			enemyCurrentFrame = 0;
    			enemyMotionTimer = new Timer(enemyFrameDelay, null);
    			enemyMotionTimer.addActionListener(e -> {
    				enemyCurrentFrame = (enemyCurrentFrame + 1) % enemyMotions.length;
    			    repaint();
    			});
    			
    			new Timer(enemyDuration, e -> {
    				enemyMotionTimer.stop();
    			    ((Timer) e.getSource()).stop();
    			}).start();
    			
    			enemyMotionTimer.start();
    			
    			break;
        	}
        	
        	break;
        	
        	
        // Finn ------------------------------------------------------------	
        case "Finn":
        	switch (enemyCharacter.getCurrentMotion()) {
        	case "MOVE":
        		switch (enemyCharacter.getCurrentCard().getName()) {
        		case "Move Up":
        		    // 시작 위치와 이동 거리 설정
        		    int MU_startY = enemyCharacter.getCurrentY(); // 시작 위치 Y
        		    int MU_endY = MU_startY - 60; // 목표 위치 (위쪽으로 60px 이동)

        		    // 애니메이션 관련 설정
        		    enemyMotions = enemyCharacter.getMotions().get(enemyCharacter.getCurrentCard().getName());
        		    enemyFrameDelay = 100; // 각 프레임 간격
        		    int MU_totalFrames = enemyMotions.length; // 애니메이션 총 프레임 수
        		    enemyDuration = 5 * enemyFrameDelay * enemyMotions.length; // 애니메이션 총 시간

        		    enemyCurrentFrame = 0;

        		    // 이동 속도 계산
        		    int MU_steps = 5 * MU_totalFrames; // 이동할 스텝 수
        		    int MU_stepSize = (MU_endY - MU_startY) / MU_steps; // 한 스텝당 이동 거리

        		    // 프레임별 애니메이션 실행
        		    enemyMotionTimer = new Timer(enemyFrameDelay, null);
        		    enemyMotionTimer.addActionListener(e -> {
        		        // 현재 프레임 업데이트
        		        enemyCurrentFrame = (enemyCurrentFrame + 1) % MU_totalFrames;

        		        // 현재 위치 업데이트
        		        int currentY = enemyCharacter.getCurrentY();
        		        if (Math.abs(currentY - MU_endY) > Math.abs(MU_stepSize)) {
        		            enemyCharacter.setCurrentY(currentY + MU_stepSize); // Y 위치 업데이트
        		        }

        		        // 화면 갱신
        		        repaint();
        		    });

        		    // 애니메이션 종료 시 처리
        		    new Timer(enemyDuration, e -> {
        		        enemyMotionTimer.stop();
        		        enemyCharacter.setCurrentY(MU_endY); // 최종 위치 보정
        		        ((Timer) e.getSource()).stop();
        		    }).start();

        		    enemyMotionTimer.start();

        		    break;
        		case "Move Down":
        		    // 시작 위치와 이동 거리 설정
        		    int MD_startY = enemyCharacter.getCurrentY(); // 시작 위치 Y
        		    int MD_endY = MD_startY + 60; // 목표 위치 (아래쪽으로 60px 이동)

        		    // 애니메이션 관련 설정
        		    enemyMotions = enemyCharacter.getMotions().get(enemyCharacter.getCurrentCard().getName());
        		    enemyFrameDelay = 100; // 각 프레임 간격
        		    int MD_totalFrames = enemyMotions.length; // 애니메이션 총 프레임 수
        		    enemyDuration = 6 * enemyFrameDelay * MD_totalFrames; // 애니메이션 총 시간

        		    enemyCurrentFrame = 0;

        		    // 이동 속도 계산
        		    int MD_steps = 6 * MD_totalFrames; // 이동할 스텝 수
        		    int MD_stepSize = (MD_endY - MD_startY) / MD_steps; // 한 스텝당 이동 거리

        		    // 프레임별 애니메이션 실행
        		    enemyMotionTimer = new Timer(enemyFrameDelay, null);
        		    enemyMotionTimer.addActionListener(e -> {
        		        // 현재 프레임 업데이트
        		        enemyCurrentFrame = (enemyCurrentFrame + 1) % MD_totalFrames;

        		        // 현재 위치 업데이트
        		        int currentY = enemyCharacter.getCurrentY();
        		        if (Math.abs(currentY - MD_endY) > Math.abs(MD_stepSize)) {
        		            enemyCharacter.setCurrentY(currentY + MD_stepSize); // Y 위치 업데이트
        		        }

        		        // 화면 갱신
        		        repaint();
        		    });

        		    // 애니메이션 종료 시 처리
        		    new Timer(enemyDuration, e -> {
        		        enemyMotionTimer.stop();
        		        enemyCharacter.setCurrentY(MD_endY); // 최종 위치 보정
        		        ((Timer) e.getSource()).stop();
        		    }).start();

        		    enemyMotionTimer.start();

        		    break;

        		case "Move Left":
        		    // 시작 위치와 이동 거리 설정
        		    int ML_startX = enemyCharacter.getCurrentX(); // 시작 위치 X
        		    int ML_endX = ML_startX - 150; // 목표 위치 (왼쪽으로 100px 이동)

        		    // 애니메이션 관련 설정
        		    enemyMotions = enemyCharacter.getMotions().get(enemyCharacter.getCurrentCard().getName());
        		    enemyFrameDelay = 100; // 각 프레임 간격
        		    int ML_totalFrames = enemyMotions.length; // 애니메이션 총 프레임 수
        		    enemyDuration = 4*enemyFrameDelay * ML_totalFrames; // 애니메이션 총 시간

        		    enemyCurrentFrame = 0;

        		    // 이동 속도 계산
        		    int ML_steps = 4*ML_totalFrames; // 이동할 스텝 수 (프레임 수와 동일)
        		    int ML_stepSize = (ML_endX - ML_startX) / ML_steps; // 한 스텝당 이동 거리

        		    // 프레임별 애니메이션 실행
        		    enemyMotionTimer = new Timer(enemyFrameDelay, null);
        		    enemyMotionTimer.addActionListener(e -> {
        		        // 현재 프레임 업데이트
        		        enemyCurrentFrame = (enemyCurrentFrame + 1) % ML_totalFrames;

        		        // 현재 위치 업데이트
        		        int currentX = enemyCharacter.getCurrentX();
        		        if (Math.abs(currentX - ML_endX) > Math.abs(ML_stepSize)) {
        		            enemyCharacter.setCurrentX(currentX + ML_stepSize); // X 위치 업데이트
        		        }

        		        // 화면 갱신
        		        repaint();
        		    });

        		    // 애니메이션 종료 시 처리
        		    new Timer(enemyDuration, e -> {
        		        enemyMotionTimer.stop();
        		        enemyCharacter.setCurrentX(ML_endX); // 최종 위치 보정
        		        ((Timer) e.getSource()).stop();
        		    }).start();

        		    enemyMotionTimer.start();

        		    break;

        		case "Move Right":
        		    // 시작 위치와 이동 거리 설정
        		    int MR_startX = enemyCharacter.getCurrentX(); // 시작 위치 X
        		    int MR_endX = MR_startX + 150; // 목표 위치 (오른쪽으로 150px 이동)

        		    // 애니메이션 관련 설정
        		    enemyMotions = enemyCharacter.getMotions().get(enemyCharacter.getCurrentCard().getName());
        		    enemyFrameDelay = 100; // 각 프레임 간격
        		    int MR_totalFrames = enemyMotions.length; // 애니메이션 총 프레임 수
        		    enemyDuration = 4 * enemyFrameDelay * MR_totalFrames; // 애니메이션 총 시간

        		    enemyCurrentFrame = 0;

        		    // 이동 속도 계산
        		    int MR_steps = 4 * MR_totalFrames; // 이동할 스텝 수
        		    int MR_stepSize = (MR_endX - MR_startX) / MR_steps; // 한 스텝당 이동 거리

        		    // 프레임별 애니메이션 실행
        		    enemyMotionTimer = new Timer(enemyFrameDelay, null);
        		    enemyMotionTimer.addActionListener(e -> {
        		        // 현재 프레임 업데이트
        		        enemyCurrentFrame = (enemyCurrentFrame + 1) % MR_totalFrames;

        		        // 현재 위치 업데이트
        		        int currentX = enemyCharacter.getCurrentX();
        		        if (Math.abs(currentX - MR_endX) > Math.abs(MR_stepSize)) {
        		            enemyCharacter.setCurrentX(currentX + MR_stepSize); // X 위치 업데이트
        		        }

        		        // 화면 갱신
        		        repaint();
        		    });

        		    // 애니메이션 종료 시 처리
        		    new Timer(enemyDuration, e -> {
        		        enemyMotionTimer.stop();
        		        enemyCharacter.setCurrentX(MR_endX); // 최종 위치 보정
        		        ((Timer) e.getSource()).stop();
        		    }).start();

        		    enemyMotionTimer.start();

        		    break;
        		}

        		break;
        		
        	case "ATTACK":
        		switch (enemyCharacter.getCurrentCard().getName()) {
        		case "Sword Slash" :
        			enemyCharacter.playCardSound("Sword Slash");
        			enemyMotions = enemyCharacter.getMotions().get(enemyCharacter.getCurrentCard().getName());
        			enemyFrameDelay = 100;	// 각 프레임 간격
        			enemyDuration = enemyFrameDelay * enemyMotions.length * 4;	// 해당 모션의 총 시간
        			
        			enemyCurrentFrame = 0;
        			enemyMotionTimer = new Timer(enemyFrameDelay, null);
        			enemyMotionTimer.addActionListener(e -> {
        				enemyCurrentFrame = (enemyCurrentFrame + 1) % enemyMotions.length;
        			    repaint();
        			});
        			
        			new Timer(enemyDuration, e -> {
        				enemyMotionTimer.stop();
        			    ((Timer) e.getSource()).stop();
        			}).start();
        			
        			enemyMotionTimer.start();
        			
        			break;
        		case "Stretch Punch":
        			enemyCharacter.playCardSound("Stretch Punch");
        			enemyMotions = enemyCharacter.getMotions().get(enemyCharacter.getCurrentCard().getName());
        			enemyFrameDelay = 100;	// 각 프레임 간격
        			enemyDuration = enemyFrameDelay * enemyMotions.length * 4;	// 해당 모션의 총 시간
        			
        			enemyCurrentFrame = 0;
        			enemyMotionTimer = new Timer(enemyFrameDelay, null);
        			enemyMotionTimer.addActionListener(e -> {
        				enemyCurrentFrame = (enemyCurrentFrame + 1) % enemyMotions.length;
        			    repaint();
        			});
        			
        			new Timer(enemyDuration, e -> {
        				enemyMotionTimer.stop();
        			    ((Timer) e.getSource()).stop();
        			}).start();
        			
        			enemyMotionTimer.start();
        			
        			break;
        			
        		}
        		break;
        	case "HIT":
    			enemyMotions = enemyCharacter.getMotions().get(enemyCharacter.getCurrentCard().getName());
    			enemyFrameDelay = 150;	// 각 프레임 간격
    			enemyDuration = enemyFrameDelay * enemyMotions.length ;	// 해당 모션의 총 시간
    			
    			enemyCurrentFrame = 0;
    			enemyMotionTimer = new Timer(enemyFrameDelay, null);
    			enemyMotionTimer.addActionListener(e -> {
    				enemyCurrentFrame = (enemyCurrentFrame + 1) % enemyMotions.length;
    			    repaint();
    			});
    			
    			new Timer(enemyDuration, e -> {
    				enemyMotionTimer.stop();
    			    ((Timer) e.getSource()).stop();
    			}).start();
    			
    			enemyMotionTimer.start();
    			
    			break;

            case "DEAD":
                // DEAD 관련 처리
    			enemyMotions = enemyCharacter.getMotions().get(enemyCharacter.getCurrentCard().getName());
    			enemyFrameDelay = 200;	// 각 프레임 간격
    			enemyDuration = enemyFrameDelay * enemyMotions.length ;	// 해당 모션의 총 시간
    			
    			enemyCurrentFrame = 0;
    			enemyMotionTimer = new Timer(enemyFrameDelay, null);
    			enemyMotionTimer.addActionListener(e -> {
    				enemyCurrentFrame = (enemyCurrentFrame + 1) % enemyMotions.length;
    			    repaint();
    			});
    			
    			new Timer(enemyDuration, e -> {
    				enemyMotionTimer.stop();
    			    ((Timer) e.getSource()).stop();
    			}).start();
    			
    			enemyMotionTimer.start();
    			
    			break;
                
        	case "IDLE":

    			enemyMotions = enemyCharacter.getMotions().get("IDLE");
    			enemyFrameDelay = 100;	// 각 프레임 간격
    			enemyDuration = Integer.MAX_VALUE;	// 해당 모션의 총 시간
    			
    			enemyCurrentFrame = 0;
    			enemyMotionTimer = new Timer(enemyFrameDelay, null);
    			enemyMotionTimer.addActionListener(e -> {
    				enemyCurrentFrame = (enemyCurrentFrame + 1) % enemyMotions.length;
    			    repaint();
    			});
    			
    			new Timer(enemyDuration, e -> {
    				enemyMotionTimer.stop();
    			    ((Timer) e.getSource()).stop();
    			}).start();
    			
    			enemyMotionTimer.start();
    			
    			break;
        	}
        	
        	break;
        	
        	
        default:
        	System.out.println("엄준식");
        }
    }

    
    
    
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        if (backgroundImage != null) {
            g.drawImage(backgroundImage.getImage(), 0, 0, 1000, 570, this);
        } else {
            System.out.println("Background image is null.");
        }
        
        // 그리드 그리기
        drawDashedGrid(g2d, 3, 6, 150, 60);
        
        // 체력바 그리기
        drawHealthPanel();
        
        // 캐릭터 그리기
        
        if (enemyCharacter == null || myCharacter == null) return;
        
        // 내 캐릭터 그리기
        switch (myCharacter.getName()) {
        
        	
        // Doraemon ------------------------------------------------------------	
        case "Doraemon":
         	switch (myCharacter.getCurrentMotion()) {
        	case "MOVE":
        		switch (myCharacter.getCurrentCard().getName()) {
        		case "Move Up":
        			if (myMotions != null) {
        		        BufferedImage currentImage = myMotions[myCurrentFrame];
        		        g.drawImage(currentImage, myCharacter.getCurrentX(), myCharacter.getCurrentY(), null);
        			}
        			break;
        		case "Move Down":
        			if (myMotions != null) {
        		        BufferedImage currentImage = myMotions[myCurrentFrame];
        		        g.drawImage(currentImage, myCharacter.getCurrentX(), myCharacter.getCurrentY(), null);
        			}
        			break;
        		case "Move Left":
        			if (myMotions != null) {
        		        BufferedImage currentImage = myMotions[myCurrentFrame];
        		        g.drawImage(currentImage, myCharacter.getCurrentX(), myCharacter.getCurrentY(), null);
        			}
        			break;
        		case "Move Right":
        			if (myMotions != null) {
        		        BufferedImage currentImage = myMotions[myCurrentFrame];
        		        g.drawImage(currentImage, myCharacter.getCurrentX(), myCharacter.getCurrentY(), null);
        			}
        			break;
        		}
        		break;
        	case "ATTACK":
        		switch (myCharacter.getCurrentCard().getName()) {
        		// ... skill들 넣으셈
        		case "Air Cannon!":
        			if (myMotions != null) {
        				
        		        BufferedImage currentImage = myMotions[myCurrentFrame];
        		        g.drawImage(currentImage, myCharacter.getCurrentX(), myCharacter.getCurrentY(), null);
        			}
        			break;
        		case "Bamboo Helicopter!":
        			if (myMotions != null) {
        		        BufferedImage currentImage = myMotions[myCurrentFrame];
        		        g.drawImage(currentImage, myCharacter.getCurrentX(), myCharacter.getCurrentY(), null);
        			}
        			break;
        		}
        		break;
        	case "HIT":
        		
        		break;
        	case "DEAD":
        		
        		break;
        	case "IDLE":
        		if (myMotions != null) {
    		        BufferedImage currentImage = myMotions[myCurrentFrame];
    		        if(myCharacter.getCurrentX() >= 500) {
    		        g.drawImage(flipHorizontally(currentImage), myCharacter.getCurrentX(), myCharacter.getCurrentY(), null);
    			}else
    				g.drawImage(currentImage, myCharacter.getCurrentX(), myCharacter.getCurrentY(), null);
        		}
    				
        		break;
        	}
        	
        	break;
        	
        // Zoro ------------------------------------------------------------		
        case "Zoro":
        	switch (myCharacter.getCurrentMotion()) {
        	case "MOVE":
        		switch (myCharacter.getCurrentCard().getName()) {
        		case "Move Up":
        			if (myMotions != null) {
        		        BufferedImage currentImage = myMotions[myCurrentFrame];
        		        g.drawImage(currentImage, myCharacter.getCurrentX(), myCharacter.getCurrentY(), null);
        			}
        			break;
        		case "Move Down":
        			if (myMotions != null) {
        		        BufferedImage currentImage = myMotions[myCurrentFrame];
        		        g.drawImage(currentImage, myCharacter.getCurrentX(), myCharacter.getCurrentY(), null);
        			}
        			break;
        		case "Move Left":
        			if (myMotions != null) {
        		        BufferedImage currentImage = myMotions[myCurrentFrame];
        		        g.drawImage(currentImage, myCharacter.getCurrentX(), myCharacter.getCurrentY(), null);
        			}
        			break;
        		case "Move Right":
        			if (myMotions != null) {
        		        BufferedImage currentImage = myMotions[myCurrentFrame];
        		        g.drawImage(currentImage, myCharacter.getCurrentX(), myCharacter.getCurrentY(), null);
        			}
        			break;
        		}
        		break;
        	case "ATTACK":
        		switch (myCharacter.getCurrentCard().getName()) {
        		// ... skill들 넣으셈
        		case "Skill1":
        			if (myMotions != null) {
        		        BufferedImage currentImage = myMotions[myCurrentFrame];
        		        g.drawImage(currentImage, myCharacter.getCurrentX(), myCharacter.getCurrentY(), null);
        			}
        			break;
        		}
        		break;
        	case "HIT":
        		
        		break;
        	case "DEAD":
        		
        		break;
        	case "IDLE":
        		if (myMotions != null) {
    		        BufferedImage currentImage = myMotions[myCurrentFrame];
    		        if(myCharacter.getCurrentX() >= 500) {
    		        	g.drawImage(flipHorizontally(currentImage), myCharacter.getCurrentX(), myCharacter.getCurrentY(), null);	
    		        }else
    		        g.drawImage(currentImage, myCharacter.getCurrentX(), myCharacter.getCurrentY(), null);
    			}
        		break;
        	}
        	
        	break;
        	
        // Cygnus ------------------------------------------------------------	
        case "Cygnus":
        	switch (myCharacter.getCurrentMotion()) {
        	case "MOVE":
        		switch (myCharacter.getCurrentCard().getName()) {
        		case "Move Up":
        			if (myMotions != null) {
        		        BufferedImage currentImage = myMotions[myCurrentFrame];
        		        g.drawImage(currentImage, myCharacter.getCurrentX(), myCharacter.getCurrentY(), null);
        			}
        			break;
        		case "Move Down":
        			if (myMotions != null) {
        		        BufferedImage currentImage = myMotions[myCurrentFrame];
        		        g.drawImage(currentImage, myCharacter.getCurrentX(), myCharacter.getCurrentY(), null);
        			}
        			break;
        		case "Move Left":
        			if (myMotions != null) {
        		        BufferedImage currentImage = myMotions[myCurrentFrame];
        		        g.drawImage(currentImage, myCharacter.getCurrentX(), myCharacter.getCurrentY(), null);
        			}
        			break;
        		case "Move Right":
        			if (myMotions != null) {
        		        BufferedImage currentImage = myMotions[myCurrentFrame];
        		        g.drawImage(currentImage, myCharacter.getCurrentX(), myCharacter.getCurrentY(), null);
        			}
        			break;
        		}
        		break;
        	case "ATTACK":
        		switch (myCharacter.getCurrentCard().getName()) {
        		case "Skill1":
        			if (myMotions != null) {
        		        BufferedImage currentImage = myMotions[myCurrentFrame];
        		        g.drawImage(currentImage, myCharacter.getCurrentX(), myCharacter.getCurrentY(), null);
        			}
        			break;
        		}
        		break;
        	case "HIT":
        		
        		break;
        	case "DEAD":
        		
        		break;
        	case "IDLE":
        		if (myMotions != null) {
    		        BufferedImage currentImage = myMotions[myCurrentFrame];
    		        if(myCharacter.getCurrentX() >= 500) {
    		        	g.drawImage(flipHorizontally(currentImage), myCharacter.getCurrentX(), myCharacter.getCurrentY(), null);	
    		        }else
    		        g.drawImage(currentImage, myCharacter.getCurrentX(), myCharacter.getCurrentY(), null);
    			}
        		break;
        	}
        	
        	break;
        	
        	
        // Finn ------------------------------------------------------------	
        case "Finn":
        	switch (myCharacter.getCurrentMotion()) {
        	case "MOVE":
        		switch (myCharacter.getCurrentCard().getName()) {
        		case "Move Up":
        			if (myMotions != null) {
        		        BufferedImage currentImage = myMotions[myCurrentFrame];
        		        g.drawImage(currentImage, myCharacter.getCurrentX(), myCharacter.getCurrentY(), null);
        			}
        			break;
        		case "Move Down":
        			if (myMotions != null) {
        		        BufferedImage currentImage = myMotions[myCurrentFrame];
        		        g.drawImage(currentImage, myCharacter.getCurrentX(), myCharacter.getCurrentY(), null);
        			}
        			break;
        		case "Move Left":
        			if (myMotions != null) {
        		        BufferedImage currentImage = myMotions[myCurrentFrame];
        		        g.drawImage(currentImage, myCharacter.getCurrentX(), myCharacter.getCurrentY(), null);
        			}
        			break;
        		case "Move Right":
        			if (myMotions != null) {
        		        BufferedImage currentImage = myMotions[myCurrentFrame];
        		        g.drawImage(currentImage, myCharacter.getCurrentX(), myCharacter.getCurrentY(), null);
        			}
        			break;
        		}
        		break;
        	case "ATTACK":
        		switch (myCharacter.getCurrentCard().getName()) {
        		case "Sword Slash":
        			if (myMotions != null) {
        		        BufferedImage currentImage = myMotions[myCurrentFrame];
        		        g.drawImage(currentImage, myCharacter.getCurrentX(), myCharacter.getCurrentY(), null);
        			}
        			break;
        		case "Stretch Punch":
        			if (myMotions != null) {
        		        BufferedImage currentImage = myMotions[myCurrentFrame];
        		        g.drawImage(currentImage, myCharacter.getCurrentX(), myCharacter.getCurrentY(), null);
        			}
        			break;
        			
        		}
        		break;
        	case "HIT":
    			if (myMotions != null) {
    		        BufferedImage currentImage = myMotions[myCurrentFrame];
    		        g.drawImage(currentImage, myCharacter.getCurrentX(), myCharacter.getCurrentY(), null);
    			}
        		break;
        		
        	case "DEAD":
    			if (myMotions != null) {
    		        BufferedImage currentImage = myMotions[myCurrentFrame];
    		        g.drawImage(currentImage, myCharacter.getCurrentX(), myCharacter.getCurrentY(), null);
    			}
        		break;
        		
        	case "IDLE":
        		if (myMotions != null) {
    		        BufferedImage currentImage = myMotions[myCurrentFrame];
    		        if(myCharacter.getCurrentX() >= 500) {
    		        	g.drawImage(flipHorizontally(currentImage), myCharacter.getCurrentX(), myCharacter.getCurrentY(), null);	
    		        }else
    		        g.drawImage(currentImage, myCharacter.getCurrentX(), myCharacter.getCurrentY(), null);
    			}
        		break;
        	}
        	
        	break;
        	
		}


		// 상대 캐릭터 그리기
        switch (enemyCharacter.getName()) {
        
       
        	
        // Doraemon ------------------------------------------------------------	
        case "Doraemon":
          	switch (enemyCharacter.getCurrentMotion()) {
        	case "MOVE":
        		switch (enemyCharacter.getCurrentCard().getName()) {
        		case "Move Up":
        			if (enemyMotions != null) {
        		        BufferedImage currentImage = enemyMotions[enemyCurrentFrame];
        		        g.drawImage(currentImage, enemyCharacter.getCurrentX(), enemyCharacter.getCurrentY(), null);
        			}
        			break;
        		case "Move Down":
        			if (enemyMotions != null) {
        		        BufferedImage currentImage = enemyMotions[enemyCurrentFrame];
        		        g.drawImage(currentImage, enemyCharacter.getCurrentX(), enemyCharacter.getCurrentY(), null);
        			}
        			break;
        		case "Move Left":
        			if (enemyMotions != null) {
        		        BufferedImage currentImage = enemyMotions[enemyCurrentFrame];
        		        g.drawImage(currentImage, enemyCharacter.getCurrentX(), enemyCharacter.getCurrentY(), null);
        			}
        			break;
        		case "Move Right":
        			if (enemyMotions != null) {
        		        BufferedImage currentImage = enemyMotions[enemyCurrentFrame];
        		        g.drawImage(currentImage, enemyCharacter.getCurrentX(), enemyCharacter.getCurrentY(), null);
        			}
        			break;
        		}
        		break;
        	case "ATTACK":
        		switch (enemyCharacter.getCurrentCard().getName()) {
        		// ... skill들 넣으셈
        		case "Air Cannon!":
        			if (enemyMotions != null) {
        		        BufferedImage currentImage = enemyMotions[enemyCurrentFrame];
        		        g.drawImage(currentImage, enemyCharacter.getCurrentX(), enemyCharacter.getCurrentY(), null);
        			}
        			break;
        		}
    		case "Bamboo Helicopter!":
    			if (enemyMotions != null) {
    		        BufferedImage currentImage = enemyMotions[enemyCurrentFrame];
    		        g.drawImage(currentImage, enemyCharacter.getCurrentX(), enemyCharacter.getCurrentY(), null);
    			}
    			break;
        	case "HIT":
        		
        		break;
        	case "DEAD":
        		
        		break;
        	case "IDLE":
        		if (enemyMotions != null) {
    		        BufferedImage currentImage = enemyMotions[enemyCurrentFrame];
    		        if(enemyCharacter.getCurrentX() >= 500) {
    		        	g.drawImage(flipHorizontally(currentImage), enemyCharacter.getCurrentX(), enemyCharacter.getCurrentY(), null);
    		        }else
    		        	g.drawImage(currentImage, enemyCharacter.getCurrentX(), enemyCharacter.getCurrentY(), null);
    			}
    			break;
        	}
        	
        	break;
        	
        // Zoro ------------------------------------------------------------		
        case "Zoro":
        	switch (enemyCharacter.getCurrentMotion()) {
        	case "MOVE":
        		switch (enemyCharacter.getCurrentCard().getName()) {
        		case "Move Up":
        			if (enemyMotions != null) {
        		        BufferedImage currentImage = enemyMotions[enemyCurrentFrame];
        		        g.drawImage(currentImage, enemyCharacter.getCurrentX(), enemyCharacter.getCurrentY(), null);
        			}
        			break;
        		case "Move Down":
        			if (enemyMotions != null) {
        		        BufferedImage currentImage = enemyMotions[enemyCurrentFrame];
        		        g.drawImage(currentImage, enemyCharacter.getCurrentX(), enemyCharacter.getCurrentY(), null);
        			}
        			break;
        		case "Move Left":
        			if (enemyMotions != null) {
        		        BufferedImage currentImage = enemyMotions[enemyCurrentFrame];
        		        g.drawImage(currentImage, enemyCharacter.getCurrentX(), enemyCharacter.getCurrentY(), null);
        			}
        			break;
        		case "Move Right":
        			if (enemyMotions != null) {
        		        BufferedImage currentImage = enemyMotions[enemyCurrentFrame];
        		        g.drawImage(currentImage, enemyCharacter.getCurrentX(), enemyCharacter.getCurrentY(), null);
        			}
        			break;
        		}
        		break;
        	case "ATTACK":
        		switch (enemyCharacter.getCurrentCard().getName()) {
        		// ... skill들 넣으셈
        		case "Skill1":
        			if (enemyMotions != null) {
        		        BufferedImage currentImage = enemyMotions[enemyCurrentFrame];
        		        g.drawImage(currentImage, enemyCharacter.getCurrentX(), enemyCharacter.getCurrentY(), null);
        			}
        			break;
        		}
        		break;
        	case "HIT":
        		
        		break;
        	case "DEAD":
        		
        		break;
        	case "IDLE":
        		if (enemyMotions != null) {
    		        BufferedImage currentImage = enemyMotions[enemyCurrentFrame];
    		        if(enemyCharacter.getCurrentX() >= 500) {
    		        	g.drawImage(flipHorizontally(currentImage), enemyCharacter.getCurrentX(), enemyCharacter.getCurrentY(), null);
    		        }else
    		        	g.drawImage(currentImage, enemyCharacter.getCurrentX(), enemyCharacter.getCurrentY(), null);
    			}
    			break;
        	}
        	
        	break;
        	
        // Cygnus ------------------------------------------------------------	
        case "Cygnus":
        	switch (enemyCharacter.getCurrentMotion()) {
        	case "MOVE":
        		switch (enemyCharacter.getCurrentCard().getName()) {
        		case "Move Up":
        			if (enemyMotions != null) {
        		        BufferedImage currentImage = enemyMotions[enemyCurrentFrame];
        		        g.drawImage(currentImage, enemyCharacter.getCurrentX(), enemyCharacter.getCurrentY(), null);
        			}
        			break;
        		case "Move Down":
        			if (enemyMotions != null) {
        		        BufferedImage currentImage = enemyMotions[enemyCurrentFrame];
        		        g.drawImage(currentImage, enemyCharacter.getCurrentX(), enemyCharacter.getCurrentY(), null);
        			}
        			break;
        		case "Move Left":
        			if (enemyMotions != null) {
        		        BufferedImage currentImage = enemyMotions[enemyCurrentFrame];
        		        g.drawImage(currentImage, enemyCharacter.getCurrentX(), enemyCharacter.getCurrentY(), null);
        			}
        			break;
        		case "Move Right":
        			if (enemyMotions != null) {
        		        BufferedImage currentImage = enemyMotions[enemyCurrentFrame];
        		        g.drawImage(currentImage, enemyCharacter.getCurrentX(), enemyCharacter.getCurrentY(), null);
        			}
        			break;
        		}
        		break;
        	case "ATTACK":
        		switch (enemyCharacter.getCurrentCard().getName()) {
        		case "Skill1":
        			if (enemyMotions != null) {
        		        BufferedImage currentImage = enemyMotions[enemyCurrentFrame];
        		        g.drawImage(currentImage, enemyCharacter.getCurrentX(), enemyCharacter.getCurrentY(), null);
        			}
        			break;
        		}
        		break;
        	case "HIT":
        		
        		break;
        	case "DEAD":
        		
        		break;
        	case "IDLE":
        		if (enemyMotions != null) {
    		        BufferedImage currentImage = enemyMotions[enemyCurrentFrame];
    		        if(enemyCharacter.getCurrentX() >= 500) {
    		        	g.drawImage(flipHorizontally(currentImage), enemyCharacter.getCurrentX(), enemyCharacter.getCurrentY(), null);
    		        }else
    		        	g.drawImage(currentImage, enemyCharacter.getCurrentX(), enemyCharacter.getCurrentY(), null);
    			}
    			break;
        	}
        	
        	break;
        	
        
        	
        // Finn ------------------------------------------------------------	
        case "Finn":
        	switch (enemyCharacter.getCurrentMotion()) {
        	case "MOVE":
        		switch (enemyCharacter.getCurrentCard().getName()) {
        		case "Move Up":
        			if (enemyMotions != null) {
        		        BufferedImage currentImage = enemyMotions[enemyCurrentFrame];
        		        g.drawImage(currentImage, enemyCharacter.getCurrentX(), enemyCharacter.getCurrentY(), null);
        			}
        			break;
        		case "Move Down":
        			if (enemyMotions != null) {
        		        BufferedImage currentImage = enemyMotions[enemyCurrentFrame];
        		        g.drawImage(currentImage, enemyCharacter.getCurrentX(), enemyCharacter.getCurrentY(), null);
        			}
        			break;
        		case "Move Left":
        			if (enemyMotions != null) {
        		        BufferedImage currentImage = enemyMotions[enemyCurrentFrame];
        		        g.drawImage(currentImage, enemyCharacter.getCurrentX(), enemyCharacter.getCurrentY(), null);
        			}
        			break;
        		case "Move Right":
        			if (enemyMotions != null) {
        		        BufferedImage currentImage = enemyMotions[enemyCurrentFrame];
        		        g.drawImage(currentImage, enemyCharacter.getCurrentX(), enemyCharacter.getCurrentY(), null);
        			}
        			break;
        		}
        		break;
        	case "ATTACK":
        		switch (enemyCharacter.getCurrentCard().getName()) {
        		case "Sword Slash":
        			if (enemyMotions != null) {
        		        BufferedImage currentImage = enemyMotions[enemyCurrentFrame];
        		        g.drawImage(currentImage, enemyCharacter.getCurrentX(), enemyCharacter.getCurrentY(), null);
        			}
        			break;
        		case "Stretch Punch":
        			if (enemyMotions != null) {
        		        BufferedImage currentImage = enemyMotions[enemyCurrentFrame];
        		        g.drawImage(currentImage, enemyCharacter.getCurrentX(), enemyCharacter.getCurrentY(), null);
        			}
        			break;
        		}
        		break;
        	case "HIT":
    			if (enemyMotions != null) {
    		        BufferedImage currentImage = enemyMotions[enemyCurrentFrame];
    		        g.drawImage(currentImage, enemyCharacter.getCurrentX(), enemyCharacter.getCurrentY(), null);
    			}
        		break;
        		
        	case "DEAD":
    			if (enemyMotions != null) {
    		        BufferedImage currentImage = enemyMotions[enemyCurrentFrame];
    		        g.drawImage(currentImage, enemyCharacter.getCurrentX(), enemyCharacter.getCurrentY(), null);
    			}
        		break;
        		
        	case "IDLE":
        		if (enemyMotions != null) {
    		        BufferedImage currentImage = enemyMotions[enemyCurrentFrame];
    		        
    		        if(enemyCharacter.getCurrentX() >= 500) {
    		        	g.drawImage(flipHorizontally(currentImage), enemyCharacter.getCurrentX(), enemyCharacter.getCurrentY(), null);
    		        }else
    		        	g.drawImage(currentImage, enemyCharacter.getCurrentX(), enemyCharacter.getCurrentY(), null);
    			}
    			break;
        	}
        	
        	break;
        	
        
        }
    }

    
    
    
    public void drawHealthPanel() {
    	
        healthPanel.setLayout(null);
        healthPanel.setPreferredSize(new Dimension(0, 70)); // 고정 높이를 늘림
        healthPanel.setOpaque(true);
        healthPanel.setBackground(new Color(236, 237, 215));
        
        int panelWidth = 950; // 패널의 예상 너비를 설정
        int panelHeight = 50;

        
        if (gameState.getClientNumber() == 1) {
           	// Player 1의 캐릭터 로고
               JLabel GN_1_player1Logo = new JLabel(new ImageIcon(gameState.getMyCharacter().getLogo()));
               GN_1_player1Logo.setBounds(30, 10, panelHeight, panelHeight);
               healthPanel.add(GN_1_player1Logo);
           	
           	// Player 1의 체력바
           	JProgressBar GN_1_player1HealthBar = new JProgressBar(0, gameState.getMyCharacter().getMaxHealth());
    	        GN_1_player1HealthBar.setValue(gameState.getMyHealth());
    	        GN_1_player1HealthBar.setBounds(90, 10, panelWidth / 2 - 100, 30);
    	        GN_1_player1HealthBar.setForeground(Color.RED);
    	        healthPanel.add(GN_1_player1HealthBar);
    	        
    	        // Player 1의 이름
    	        JLabel GN_1_player1NameLabel = new JLabel("(you) "+gameState.getMyCharacter().getName());
    	        GN_1_player1NameLabel.setBounds(90, 40, panelWidth / 2 - 100, 20);
    	        GN_1_player1NameLabel.setHorizontalAlignment(SwingConstants.LEFT);
    	        GN_1_player1NameLabel.setForeground(Color.black); // 흰색 글씨로 이름 표시
    	        healthPanel.add(GN_1_player1NameLabel);
    	        
    	        
    	        // Player 2의 캐릭터 로고
    	        JLabel GN_1_player2Logo = new JLabel(new ImageIcon(gameState.getEnemyCharacter().getLogo()));
    	        GN_1_player2Logo.setBounds(panelWidth - panelHeight - 10, 10, panelHeight, panelHeight);
    	        healthPanel.add(GN_1_player2Logo);
    	        
    	        // Player 2의 체력바
    	        JProgressBar GN_1_player2HealthBar = new JProgressBar(0, gameState.getEnemyCharacter().getMaxHealth());
    	        GN_1_player2HealthBar.setValue(gameState.getEnemyHealth());
    	        GN_1_player2HealthBar.setBounds(panelWidth / 2 + 20, 10, panelWidth / 2 - 100, 30);
    	        GN_1_player2HealthBar.setForeground(Color.RED);
    	        healthPanel.add(GN_1_player2HealthBar);

    	        // Player 2의 이름
    	        JLabel GN_1_player2NameLabel = new JLabel("(enemy) " + gameState.getEnemyCharacter().getName());
    	        GN_1_player2NameLabel.setBounds(panelWidth / 2 + 20, 40, panelWidth / 2 - 100, 20);
    	        GN_1_player2NameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    	        GN_1_player2NameLabel.setForeground(Color.black); // 흰색 글씨로 이름 표시
    	        healthPanel.add(GN_1_player2NameLabel);
           }  else if(gameState.getClientNumber() == 2){
           	// Player 1의 캐릭터 로고
               JLabel player1Logo = new JLabel(new ImageIcon(gameState.getEnemyCharacter().getLogo()));
               player1Logo.setBounds(30, 10, panelHeight, panelHeight);
               healthPanel.add(player1Logo);
           	
               // Player 1의 체력바
           	JProgressBar player1HealthBar = new JProgressBar(0, gameState.getEnemyCharacter().getMaxHealth());
    	        player1HealthBar.setValue(gameState.getEnemyHealth());
    	        player1HealthBar.setBounds(90, 10, panelWidth / 2 - 100, 30);
    	        player1HealthBar.setForeground(Color.RED);
    	        healthPanel.add(player1HealthBar);
    	        
    	        // Player 1의 이름
    	        JLabel player1NameLabel = new JLabel("(enemy) " + gameState.getEnemyCharacter().getName());
    	        player1NameLabel.setBounds(90, 40, panelWidth / 2 - 100, 20);
    	        player1NameLabel.setHorizontalAlignment(SwingConstants.LEFT);
    	        player1NameLabel.setForeground(Color.black); // 흰색 글씨로 이름 표시
    	        healthPanel.add(player1NameLabel);
    	        
    	        
    	        // Player 2의 캐릭터 로고
    	        JLabel player2Logo = new JLabel(new ImageIcon(gameState.getMyCharacter().getLogo()));
    	        player2Logo.setBounds(panelWidth - panelHeight - 10, 10, panelHeight, panelHeight);
    	        healthPanel.add(player2Logo);
    	        
    	        // Player 2의 체력바
    	        JProgressBar player2HealthBar = new JProgressBar(0, gameState.getMyCharacter().getMaxHealth());
    	        player2HealthBar.setValue(gameState.getMyHealth());
    	        player2HealthBar.setBounds(panelWidth / 2 + 20, 10, panelWidth / 2 - 100, 30);
    	        player2HealthBar.setForeground(Color.RED);
    	        healthPanel.add(player2HealthBar);

    	        // Player 2의 이름
    	        JLabel player2NameLabel = new JLabel("(you) " + gameState.getMyCharacter().getName());
    	        player2NameLabel.setBounds(panelWidth / 2 + 20, 40, panelWidth / 2 - 100, 20);
    	        player2NameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    	        player2NameLabel.setForeground(Color.black); // 흰색 글씨로 이름 표시
    	        healthPanel.add(player2NameLabel);
           }
    }



  public void drawSelectedCardPanel() {
    cardPanel.removeAll();
    cardPanel.setLayout(new BorderLayout()); // BorderLayout으로 배치

    int imageWidth = 80;  // 이미지 너비
    int imageHeight = 80; // 이미지 높이

    // 공통 마진 설정
    int horizontalMargin = 30; // 양쪽 패널의 수평 마진
    int verticalMargin = 0;   // 양쪽 패널의 수직 마진

    if (gameState.getClientNumber() == 1) {
        // Player 1의 카드 패널 (왼쪽)
        JPanel player1CardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5)); // 간격 조정
        player1CardPanel.setBackground(Color.BLACK);
        player1CardPanel.setBorder(BorderFactory.createEmptyBorder(verticalMargin, horizontalMargin, verticalMargin, horizontalMargin)); // 마진 추가
        JLabel player1Label = new JLabel("<html>Your<br>Cards</html>");
        player1Label.setForeground(Color.WHITE);
        player1CardPanel.add(player1Label);

        for (Card card : gameState.getSelectedCardList()) {
            ImageIcon originalIcon = new ImageIcon(gameState.getMyCharacter().getCardImage().get(card.getName()));

            // 이미지 크기 조정
            Image scaledImage = originalIcon.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            JPanel cardPanelWithLabel = new JPanel(new BorderLayout());
            cardPanelWithLabel.setBackground(Color.BLACK);

            JLabel cardNameLabel = new JLabel(card.getName(), JLabel.CENTER);
            cardNameLabel.setForeground(Color.WHITE);

            JLabel cardImageLabel = new JLabel(scaledIcon);
            cardImageLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0)); // 이미지 위쪽 마진

            cardPanelWithLabel.add(cardNameLabel, BorderLayout.NORTH);
            cardPanelWithLabel.add(cardImageLabel, BorderLayout.CENTER);

            player1CardPanel.add(cardPanelWithLabel);
        }

        // Player 2의 카드 패널 (오른쪽)
        JPanel player2CardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5)); // 간격 조정
        player2CardPanel.setBackground(Color.BLACK);
        player2CardPanel.setBorder(BorderFactory.createEmptyBorder(verticalMargin, horizontalMargin, verticalMargin, horizontalMargin)); // 마진 추가
        JLabel player2Label = new JLabel("<html>Enemy<br>Cards</html>");
        player2Label.setForeground(Color.WHITE);
        player2CardPanel.add(player2Label);

        for (Card card : gameState.getEnemySelectedCardList()) {
            ImageIcon originalIcon = new ImageIcon(gameState.getEnemyCharacter().getCardImage().get(card.getName()));

            // 이미지 크기 조정
            Image scaledImage = originalIcon.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            JPanel cardPanelWithLabel = new JPanel(new BorderLayout());
            cardPanelWithLabel.setBackground(Color.BLACK);

            JLabel cardNameLabel = new JLabel(card.getName(), JLabel.CENTER);
            cardNameLabel.setForeground(Color.WHITE);

            JLabel cardImageLabel = new JLabel(scaledIcon);
            cardImageLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0)); // 이미지 위쪽 마진

            cardPanelWithLabel.add(cardNameLabel, BorderLayout.NORTH);
            cardPanelWithLabel.add(cardImageLabel, BorderLayout.CENTER);

            player2CardPanel.add(cardPanelWithLabel);
        }

        // 중앙 빈 패널
        JPanel emptyCenterPanel = new JPanel();
        emptyCenterPanel.setBackground(Color.BLACK);

        // 패널 추가
        cardPanel.add(player1CardPanel, BorderLayout.WEST);
        cardPanel.add(emptyCenterPanel, BorderLayout.CENTER);
        cardPanel.add(player2CardPanel, BorderLayout.EAST);

    } else if(gameState.getClientNumber() == 2){
        // Player 2와 Player 1 역할 전환
        JPanel player1CardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5)); // 간격 조정
        player1CardPanel.setBackground(Color.BLACK);
        player1CardPanel.setBorder(BorderFactory.createEmptyBorder(verticalMargin, horizontalMargin, verticalMargin, horizontalMargin)); // 마진 추가
        JLabel player1Label = new JLabel("<html>Enemy<br>Cards</html>");
        player1Label.setForeground(Color.WHITE);
        player1CardPanel.add(player1Label);

        for (Card card : gameState.getSelectedCardList()) {
            ImageIcon originalIcon = new ImageIcon(gameState.getMyCharacter().getCardImage().get(card.getName()));

            // 이미지 크기 조정
            Image scaledImage = originalIcon.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            JPanel cardPanelWithLabel = new JPanel(new BorderLayout());
            cardPanelWithLabel.setBackground(Color.BLACK);

            JLabel cardNameLabel = new JLabel(card.getName(), JLabel.CENTER);
            cardNameLabel.setForeground(Color.WHITE);

            JLabel cardImageLabel = new JLabel(scaledIcon);
            cardImageLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0)); // 이미지 위쪽 마진

            cardPanelWithLabel.add(cardNameLabel, BorderLayout.NORTH);
            cardPanelWithLabel.add(cardImageLabel, BorderLayout.CENTER);

            player1CardPanel.add(cardPanelWithLabel);
        }

        JPanel player2CardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5)); // 간격 조정
        player2CardPanel.setBackground(Color.BLACK);
        player2CardPanel.setBorder(BorderFactory.createEmptyBorder(verticalMargin, horizontalMargin, verticalMargin, horizontalMargin)); // 마진 추가
        JLabel player2Label = new JLabel("<html>Your<br>Cards</html>");
        player2Label.setForeground(Color.WHITE);
        player2CardPanel.add(player2Label);

        for (Card card : gameState.getEnemySelectedCardList()) {
            ImageIcon originalIcon = new ImageIcon(gameState.getEnemyCharacter().getCardImage().get(card.getName()));

            // 이미지 크기 조정
            Image scaledImage = originalIcon.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            JPanel cardPanelWithLabel = new JPanel(new BorderLayout());
            cardPanelWithLabel.setBackground(Color.BLACK);

            JLabel cardNameLabel = new JLabel(card.getName(), JLabel.CENTER);
            cardNameLabel.setForeground(Color.WHITE);

            JLabel cardImageLabel = new JLabel(scaledIcon);
            cardImageLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0)); // 이미지 위쪽 마진

            cardPanelWithLabel.add(cardNameLabel, BorderLayout.NORTH);
            cardPanelWithLabel.add(cardImageLabel, BorderLayout.CENTER);

            player2CardPanel.add(cardPanelWithLabel);
        }

        JPanel emptyCenterPanel = new JPanel();
        emptyCenterPanel.setBackground(Color.BLACK);

        cardPanel.add(player1CardPanel, BorderLayout.WEST);
        cardPanel.add(emptyCenterPanel, BorderLayout.CENTER);
        cardPanel.add(player2CardPanel, BorderLayout.EAST);
    }

    cardPanel.revalidate();
    cardPanel.repaint();
}



    private void drawDashedGrid(Graphics2D g2d, int rows, int cols, int cellWidth, int cellHeight) {
        int gridWidth = cols * cellWidth; // 전체 그리드 너비
        int gridHeight = rows * cellHeight; // 전체 그리드 높이
        int xOffset = (getWidth() - gridWidth) / 2; // 화면 너비를 기준으로 중앙 정렬
        int yOffset = (getHeight() - gridHeight) / 2 + 125; // 화면 높이를 기준으로 중앙 정렬

        float[] dash = {5.0f, 5.0f}; // Dashed pattern
        BasicStroke dottedLine = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        g2d.setStroke(dottedLine);
        g2d.setColor(Color.WHITE);

        // 가로선 그리기
        for (int i = 0; i <= rows; i++) {
            int y = yOffset + i * cellHeight;
            g2d.drawLine(xOffset, y, xOffset + gridWidth, y);
        }

        // 세로선 그리기
        for (int j = 0; j <= cols; j++) {
            int x = xOffset + j * cellWidth;
            g2d.drawLine(x, yOffset, x, yOffset + gridHeight);
        }
    }
    
    private static BufferedImage flipHorizontally(BufferedImage image) {
        // 뒤집힌 이미지를 담을 새로운 BufferedImage 생성
        BufferedImage flippedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        
        // Graphics2D 객체 생성
        Graphics2D g2d = flippedImage.createGraphics();
        
        // AffineTransform을 사용해 수평으로 이미지를 뒤집기
        AffineTransform transform = AffineTransform.getScaleInstance(-1, 1); // 수평 뒤집기
        transform.translate(-image.getWidth(), 0); // 원래 이미지의 위치로 이동시킴
        
        // 뒤집힌 이미지를 그리기
        g2d.drawImage(image, transform, null);
        g2d.dispose();
        
        return flippedImage; // 뒤집힌 이미지 반환
    }

}
