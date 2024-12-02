// SelectCharacterScreen.java

package view;

import java.awt.Button;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import controller.GameController;
import model.GameState;
import java.awt.Dimension;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class SelectCharacterScreen extends JPanel {
    private ImageIcon Character_backgroundImg;
    private GameController gameController; // GameController 참조
    
    public SelectCharacterScreen(GameState gameState, GameController gameController) {
    	setSize(new Dimension(850, 600));
        this.gameController = gameController; // GameController 저장
        Character_backgroundImg = new ImageIcon("res/img/캐릭터 선택창 배경2.jpg");
        
        // ready_panel 메서드 호출
        ready_penel();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // GIF 이미지 그리기
        if (Character_backgroundImg != null) {
            g.drawImage(Character_backgroundImg.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }

    public void ready_penel() {
    	ImageIcon Character1 = new ImageIcon("res/character/superman_face.png");
    	ImageIcon Character2 = new ImageIcon("res/character/zed.png");
    	ImageIcon Character3 = new ImageIcon("res/character/masterYi.jpg");
        setLayout(null);
        
        Button ready_button = new Button("선택");
        ready_button.setBounds(332, 465, 38, 23);
        add(ready_button);
        ready_button.setFont(new Font("HY견고딕", Font.BOLD, 12));
        
                // 선택 버튼 클릭 이벤트
                ready_button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // GameController를 통해 화면 전환
                        gameController.handleAction("PLAY_GAME");
                    }
                });
        
        Button cancel_button = new Button("취소");
        cancel_button.setBounds(492, 465, 38, 23);
        add(cancel_button);
        cancel_button.setFont(new Font("HY견고딕", Font.BOLD, 12));
        
        JButton Character_Choice_1 = new JButton(Character1);
        Character_Choice_1.setBounds(378, 229, 100, 100);
        add(Character_Choice_1);
        
        JButton Character_Choice_2 = new JButton(Character2);
        Character_Choice_2.setBounds(569, 229, 100, 100);
        add(Character_Choice_2);
        
        JButton Character_Choice_3 = new JButton(Character3);
        Character_Choice_3.setBounds(157, 229, 100, 100);
        add(Character_Choice_3);
        
        // 버튼 위치가 화면 크기에 맞춰 조정되도록 재배치
        revalidate();
        repaint();
    }
}
