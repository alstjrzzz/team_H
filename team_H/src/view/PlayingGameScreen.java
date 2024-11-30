// PlayingGameScreen.java

package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.GameController;
import model.GameState;

public class PlayingGameScreen extends JPanel {
	
	private GameState gameState;
	private GameController gameController;
	
	private JPanel healthPanel = new JPanel();
	private JPanel fieldPanel = new JPanel();
	private JPanel cardPanel = new JPanel();

	public PlayingGameScreen(GameState gameState, GameController gameController) {
		
		this.gameState = gameState;
		this.gameController = gameController;
		
		splitPanel();
		drawHealthPanel();
		drawFieldPanel();
		drawSelectedCardPanel();
	}
	
	
	public void splitPanel() {
		
		setLayout(new BorderLayout());
		
		add(healthPanel, BorderLayout.NORTH);
        add(fieldPanel, BorderLayout.CENTER);
        add(cardPanel, BorderLayout.SOUTH);
	}
	
	
	// 상단에 위치한 가로100% 세로10% 비율의 상태칸
	public void drawHealthPanel() {
		
		healthPanel.setBackground(Color.cyan);
		healthPanel.add(new JLabel("<player1 health bar>  <turn>  <player1 health bar>"));
		healthPanel.setPreferredSize(new Dimension((int)gameState.getDimension().getWidth()
													, (int)(gameState.getDimension().getHeight() * 1 / 10)));
	}
	
	
	public void drawFieldPanel() {

		fieldPanel.setBackground(Color.green);
		fieldPanel.add(new JLabel("field state"));
		fieldPanel.setPreferredSize(new Dimension((int)(gameState.getDimension().getWidth())
														, (int)(gameState.getDimension().getHeight() * 7 / 10)));
	}
	
	
	public void drawSelectedCardPanel() {
		
		cardPanel.setBackground(Color.white);
		cardPanel.add(new JLabel("<player1 selected card 1, 2, 3>      <player2 selected card 3, 2, 1>"));
		cardPanel.setPreferredSize(new Dimension((int)(gameState.getDimension().getWidth())
														, (int)(gameState.getDimension().getHeight() * 2 / 10)));
	}
}
