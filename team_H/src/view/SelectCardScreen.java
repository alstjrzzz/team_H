// SelectCardScreen.java

package view;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.GameController;
import model.GameState;

public class SelectCardScreen extends JPanel {
	
	private GameState gameState;
	private GameController gameController;
	
	private JPanel healthPanel = new JPanel();
	private JPanel selectCardPanel = new JPanel();
	private JPanel fieldPanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	private JPanel selectedCardPanel = new JPanel();
	

	public SelectCardScreen(GameState gameState, GameController gameController) {
		
		this.gameState = gameState;
		this.gameController = gameController;
		
		splitPanel();
		drawHealthPanel();
		drawSelectCardPanel();
		drawFieldPanel();
		drawButtonPanel();
		drawSelectedCardPanel();
	}
	
	// 스크린 전체의 구역을 나눕니다요
	public void splitPanel() {
		
		setLayout(new BorderLayout());
		
        add(healthPanel, BorderLayout.NORTH);
        add(selectCardPanel, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(fieldPanel, BorderLayout.WEST);
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(selectedCardPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
	}
	
	// 상단에 위치한 가로100% 세로10% 비율의 상태칸
	public void drawHealthPanel() {
		
		healthPanel.setBackground(Color.cyan);
		healthPanel.add(new JLabel("<player1 health bar>  <turn>  <player1 health bar>"));
		healthPanel.setPreferredSize(new Dimension((int)gameState.getDimension().getWidth()
				, (int)(gameState.getDimension().getHeight() * 1 / 10)));
	}
	
	// 중앙에 위치한 가로100% 세로 60% 비율의 카드선택칸
	public void drawSelectCardPanel() {
		
		selectCardPanel.setBackground(Color.gray); //나눈거 보려고 임시로 배경색넣음
		selectCardPanel.add(new JLabel("<select cards>"));
		selectCardPanel.setPreferredSize(new Dimension((int)gameState.getDimension().getWidth()
														, (int)(gameState.getDimension().getHeight() * 6 / 10)));
		
	}
	
	// 왼쪽하단에 위치한 가로40% 세로30% 비율의 필드확인칸
	public void drawFieldPanel() {
		
		fieldPanel.setBackground(Color.green);
		fieldPanel.add(new JLabel("field state"));
		fieldPanel.setPreferredSize(new Dimension((int)(gameState.getDimension().getWidth() * 4 / 10)
														, (int)(gameState.getDimension().getHeight() * 3 / 10)));
	}
	
	// 중앙하단에 위치한 가로20% 세로30% 비율의 카드선택완료버튼, 도움말버튼, 카드초기화버튼 등
	public void drawButtonPanel() {
		
		buttonPanel.setBackground(Color.red);
		Button continueButton = new Button("continue");
		continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameController.handleAction("FIGHT");
            }
        });
		buttonPanel.add(continueButton);
		buttonPanel.setPreferredSize(new Dimension((int)(gameState.getDimension().getWidth() * 2 / 10)
														, (int)(gameState.getDimension().getHeight() * 3 / 10)));
	}
	
	// 우측하단에 위치한 가로40% 세로30% 비율의 선택된카드보여주는칸
	public void drawSelectedCardPanel() {
		
		selectedCardPanel.setBackground(Color.white);
		selectedCardPanel.add(new JLabel("<selected card 1, 2, 3>"));
		selectedCardPanel.setPreferredSize(new Dimension((int)(gameState.getDimension().getWidth() * 4 / 10)
														, (int)(gameState.getDimension().getHeight() * 3 / 10)));
	}
	
	
}
