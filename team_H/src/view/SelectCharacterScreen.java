// SelectCharacterScreen.java

package view;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import controller.GameController;
import model.GameState;

public class SelectCharacterScreen extends JPanel {
	private ImageIcon Character_backgroundImg;
	private GameController gameController; // GameController 참조
	
	public SelectCharacterScreen(GameState gameState, GameController gameController) {
		this.gameController = gameController; // GameController 저장
		Character_backgroundImg = new ImageIcon("res/img/캐릭터 선택창 배경2.jpg");
		
		// ready_penel 메서드 호출
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
		setLayout(null);
		// 취소 버튼
        Button cancel_button = new Button("취소");
        cancel_button.setBounds(400, 450, 50, 52);
        add(cancel_button);
        cancel_button.setFont(new Font("HY견고딕", Font.BOLD, 12));

		// 선택 버튼
        Button ready_button = new Button("선택");
        ready_button.setBounds(500, 450, 50, 52);
        add(ready_button);
        ready_button.setFont(new Font("HY견고딕", Font.BOLD, 12));
        // 선택 버튼 클릭 이벤트
        ready_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // GameController를 통해 화면 전환
                gameController.handleAction("BACK_TO_MENU");
            }
        });
        revalidate();
        repaint();
        
	}
}
