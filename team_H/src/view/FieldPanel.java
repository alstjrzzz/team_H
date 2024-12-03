package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class FieldPanel extends JPanel implements KeyListener {
    private final int rows = 3; // 그리드 행 수
    private final int cols = 6; // 그리드 열 수
    private final int cellHeight = 50; // 각 셀의 높이
    private Timer timer;

    public FieldPanel() {
    	 setOpaque(false);  // 패널의 배경을 투명하게 설정
        // 캐릭터 애니메이션을 위한 타이머 설정
        timer = new Timer(100, e -> repaint());
        timer.start(); // 타이머 시작
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Graphics2D로 캐스팅하여 점선, 선 두께 조절
        Graphics2D g2d = (Graphics2D) g;

        // 점선 스타일 설정
        float[] dashPattern = { 10f, 5f };  // 점선 패턴 (10px 선, 5px 간격)
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0));

        int cellWidth = (getWidth() / cols) - 2;  // 화면 너비를 6등분하여 셀의 너비 결정
        // 화면의 하단 중앙으로 그리드 위치 계산
        int startX = (getWidth() - cols * cellWidth) / 2; // 중앙 정렬을 위한 X 좌표
        int startY = getHeight() - (rows * cellHeight) - 10; // 하단 정렬을 위한 Y 좌표 (10px 여백)

        // 그리드 그리기
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

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // 필요 없음
    }
}
