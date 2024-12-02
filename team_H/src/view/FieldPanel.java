package view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FieldPanel extends JPanel {
    private int characterX = 50;  // 캐릭터 X 좌표
    private int characterY = 200; // 캐릭터 Y 좌표
    private int characterWidth = 50; // 캐릭터 이미지 너비
    private int characterHeight = 50; // 캐릭터 이미지 높이

    private Timer timer;

    public FieldPanel() {
        // 캐릭터 애니메이션을 위한 타이머 설정
        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 캐릭터의 위치 업데이트
                characterX += 5;
                if (characterX > getWidth()) { // 화면 밖으로 나가면 다시 왼쪽으로
                    characterX = -characterWidth;
                }

                // 화면을 다시 그리기
                repaint();
            }
        });

        timer.start();  // 타이머 시작
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 배경 그리기
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, getWidth(), getHeight());

        // 캐릭터 그리기 (예: 원으로 표시)
        g.setColor(Color.RED);
        g.fillRect(characterX, characterY, characterWidth, characterHeight);
    }
}
