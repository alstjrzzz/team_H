package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import model.Character.SuperMan;
import model.Character.Character_test.Motion;
import model.Character.Ginzo;

public class FieldPanel extends JPanel implements KeyListener {
    private final int rows = 3;  // 그리드 행 수
    private final int cols = 6;  // 그리드 열 수

    // 플레이어들의 위치
    private int player1X, player1Y;  
    private int player2X, player2Y;

    private Timer timer;

    // 각각의 캐릭터 생성
    private SuperMan player1 = new SuperMan();  
    private Ginzo player2 = new Ginzo();

    // 키 상태 추적
    private boolean[] keysPressed = new boolean[256];  // 키 상태 추적 배열

    public FieldPanel() {
        setOpaque(false);  // 패널의 배경을 투명하게 설정

        // 초기 위치 설정 (그리드의 첫 번째 열, 두 번째 행)
        player1X = 0;
        player1Y = 1;

        player2X = 5;  // player2는 마지막 열에서 시작
        player2Y = 1;

        // 캐릭터 애니메이션을 위한 타이머 설정 (300ms마다 repaint 호출)
        timer = new Timer(300, e -> {
            player1.updateAnimation();  // 플레이어1 애니메이션 업데이트
            player2.updateAnimation();  // 플레이어2 애니메이션 업데이트
            repaint();  // 화면 갱신
        });
        timer.start();  // 타이머 시작

        // 키 리스너 등록
        setFocusable(true);
        addKeyListener(this);
        requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        requestFocusInWindow();

        // 패널 크기에 맞춰서 셀 크기 계산
        int cellWidth = (getWidth() / cols) - 2;  // 화면 너비를 6등분하여 셀의 너비 결정
        int cellHeight = (int) ((getHeight() / rows) / 2.5);  // 화면 높이를 3등분하여 셀의 높이 결정

        // 그리드 그리기
        drawGrid(g, cellWidth, cellHeight);

        // 플레이어1 그리기
        drawPlayer(g, player1, player1X, player1Y, cellWidth, cellHeight);

        // 플레이어2 그리기
        drawPlayer(g, player2, player2X, player2Y, cellWidth, cellHeight);
    }

    private void drawPlayer(Graphics g, Object player, int x, int y, int cellWidth, int cellHeight) {
        BufferedImage playerImage = null;

        if (player instanceof SuperMan) {
            playerImage = ((SuperMan) player).getCurrentSprite();
        } else if (player instanceof Ginzo) {
            playerImage = ((Ginzo) player).getCurrentSprite();
        }

        if (playerImage != null) {
            int startX = (getWidth() - cols * cellWidth) / 2;
            int startY = getHeight() - (rows * cellHeight);
            
            int xPos = startX + (x * cellWidth);  // X 위치 계산
            int yPos = startY + (y * cellHeight) - cellHeight;  // Y 위치 계산

            g.drawImage(playerImage, xPos, yPos, null);
        }
    }

    private void drawGrid(Graphics g, int cellWidth, int cellHeight) {
        // Graphics2D로 캐스팅하여 점선, 선 두께 조절
        Graphics2D g2d = (Graphics2D) g;

        // 점선 스타일 설정
        float[] dashPattern = {10f, 5f};  // 점선 패턴 (10px 선, 5px 간격)
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0));

        // 그리드 그리기
        int startX = (getWidth() - cols * cellWidth) / 2;
        int startY = getHeight() - (rows * cellHeight);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                g2d.setColor(Color.WHITE); 
                g2d.drawRect(startX + col * cellWidth, startY + row * cellHeight, cellWidth, cellHeight); 
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // 필요 없음
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // 키 상태 업데이트
        keysPressed[e.getKeyCode()] = true;

        // 1픽셀 이동
        int moveAmount = 1;

        // Player1 (WASD 키)
        if (keysPressed[KeyEvent.VK_W] && player1Y > 0) {
            player1Y -= moveAmount;
            player1.setMotion(Motion.WALK);
        }
        if (keysPressed[KeyEvent.VK_S] && player1Y < getHeight() - 1) {
            player1Y += moveAmount;
            player1.setMotion(Motion.WALK);
        }
        if (keysPressed[KeyEvent.VK_A] && player1X > 0) {
            player1X -= moveAmount;
            player1.setMotion(Motion.WALK);
        }
        if (keysPressed[KeyEvent.VK_D] && player1X < getWidth() - 1) {
            player1X += moveAmount;
            player1.setMotion(Motion.WALK);
        }
        if (keysPressed[KeyEvent.VK_SPACE]) {
            player1.setMotion(Motion.ATTACK);
        }
        if (keysPressed[KeyEvent.VK_T]) {
            player1.setMotion(Motion.EVENT);
        }
        if (keysPressed[KeyEvent.VK_O]) {
            player1.setMotion(Motion.DEAD);
        }
        if (keysPressed[KeyEvent.VK_C]) {
            player1.setMotion(Motion.DEFENSE);
        }

        // Player2 (화살표 키)
        if (keysPressed[KeyEvent.VK_UP] && player2Y > 0) {
            player2Y -= moveAmount;
            player2.setMotion(Motion.WALK);
        }
        if (keysPressed[KeyEvent.VK_DOWN] && player2Y < getHeight() - 1) {
            player2Y += moveAmount;
            player2.setMotion(Motion.WALK);
        }
        if (keysPressed[KeyEvent.VK_LEFT] && player2X > 0) {
            player2X -= moveAmount;
            player2.setMotion(Motion.WALK);
        }
        if (keysPressed[KeyEvent.VK_RIGHT] && player2X < getWidth() - 1) {
            player2X += moveAmount;
            player2.setMotion(Motion.WALK);
        }
        if (keysPressed[KeyEvent.VK_ENTER]) {
            player2.setMotion(Motion.ATTACK);
        }
        if (keysPressed[KeyEvent.VK_Y]) {
            player2.setMotion(Motion.EVENT);
        }
        if (keysPressed[KeyEvent.VK_P]) {
            player2.setMotion(Motion.DEAD);
        }
        if (keysPressed[KeyEvent.VK_V]) {
            player2.setMotion(Motion.DEFENSE);
        }

        // 화면 갱신
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // 키 떼면 상태를 IDLE로 변경
        keysPressed[e.getKeyCode()] = false;
        player1.setMotion(Motion.IDLE);
        player2.setMotion(Motion.IDLE);
    }
}
