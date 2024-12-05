package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import model.Character.SuperMan;
import model.Character.SuperMan.Motion;

public class FieldPanel extends JPanel implements KeyListener {
    private final int rows = 3;  // 그리드 행 수
    private final int cols = 6;  // 그리드 열 수
    private int x;  // 슈퍼맨의 현재 X 위치
    private int y;  // 슈퍼맨의 현재 Y 위치
    private Timer timer;
    private SuperMan superman = new SuperMan();  // SuperMan 객체 생성

    public FieldPanel() {
        setOpaque(false);  // 패널의 배경을 투명하게 설정

        // 초기 위치 설정 (그리드의 첫 번째 열, 두 번째 행)
        x = 0;
        y = 1;

        // 캐릭터 애니메이션을 위한 타이머 설정 (100ms마다 repaint 호출)
        timer = new Timer(200, e -> {
            superman.updateAnimation();  // 애니메이션 업데이트
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

        // 슈퍼맨 그리기
        drawSuperman(g, cellWidth, cellHeight);
    }

    private void drawSuperman(Graphics g, int cellWidth, int cellHeight) {
        // 슈퍼맨의 현재 스프라이트 이미지 가져오기
        BufferedImage supermanImage = superman.getCurrentSprite();
        if (supermanImage != null) {
            // 슈퍼맨의 위치를 그리드에 맞게 계산
            int startX = (getWidth() - cols * cellWidth) / 2; 
            int startY = getHeight() - (rows * cellHeight) ; 
            
            int xPos = startX + x ;  // X 위치 계산
            int yPos = startY + y  - cellHeight;  // Y 위치 계산

            // 이미지를 그린다
            g.drawImage(supermanImage, xPos, yPos, null);
        }
    }

    private void drawGrid(Graphics g, int cellWidth, int cellHeight) {
        // Graphics2D로 캐스팅하여 점선, 선 두께 조절
        Graphics2D g2d = (Graphics2D) g;

        // 점선 스타일 설정
        float[] dashPattern = {10f, 5f};  // 점선 패턴 (10px 선, 5px 간격)
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0));

        // 그리드 그리기
        int startX = (getWidth() - cols * cellWidth) / 2; // 중앙 정렬을 위한 X 좌표
        int startY = getHeight() - (rows * cellHeight); // 하단 정렬을 위한 Y 좌표 (10px 여백)

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                g2d.setColor(Color.WHITE); // 셀의 테두리 색
                g2d.drawRect(startX + col * cellWidth, startY + row * cellHeight, cellWidth, cellHeight); // 셀 그리기
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // 필요 없음
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // 1픽셀 이동
        int moveAmount = 1;  // 1픽셀씩 이동

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:  // 위로 이동
                if (y > 0) y -= moveAmount;
                superman.setMotion(Motion.WALK);
                break;
            case KeyEvent.VK_DOWN:  // 아래로 이동
                if (y < getHeight() - 1) y += moveAmount;
                superman.setMotion(Motion.WALK);
                break;
            case KeyEvent.VK_LEFT:  // 왼쪽으로 이동
                if (x > 0) x -= moveAmount;
                superman.setMotion(Motion.WALK);
                break;
            case KeyEvent.VK_RIGHT:  // 오른쪽으로 이동
                if (x < getWidth() - 1) x += moveAmount;
                superman.setMotion(Motion.WALK);
                break;
            case KeyEvent.VK_SPACE:  // 공격
                superman.setMotion(Motion.ATTACK);
                break;
        }

        // 화면 갱신
        repaint();
    }


    @Override
    public void keyReleased(KeyEvent e) {
        // 키를 떼면 가만히 있는 상태로 전환
        superman.setMotion(Motion.IDLE);
    }
}
