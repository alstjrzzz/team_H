// PlayingGameScreen.java

package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Timer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import controller.GameController;
import model.GameState;

public class PlayingGameScreen extends JPanel {
   
   private GameState gameState;
   private GameController gameController;
   
   private JPanel healthPanel = new JPanel();
   private JPanel fieldPanel = new FieldPanel();  // FieldPanel을 사용
   private JPanel cardPanel = new JPanel();
    private JLabel healthLabel, characterLabel, timerLabel, victoryCountLabel;
    private JProgressBar healthBar;
    private Timer gameTimer;
    private int timeLeft = 60;  // 게임 타이머 (초)
    private int victoryCount = 0;  // 승리 횟수

   public PlayingGameScreen(GameState gameState, GameController gameController) {
      
      this.gameState = gameState;
      this.gameController = gameController;
      
      splitPanel();
      drawHealthPanel();
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
   
   
   public void drawSelectedCardPanel() {
      
      cardPanel.setBackground(Color.white);
      cardPanel.add(new JLabel("<player1 selected card 1, 2, 3>      <player2 selected card 3, 2, 1>"));
      cardPanel.setPreferredSize(new Dimension((int)(gameState.getDimension().getWidth())
                                          , (int)(gameState.getDimension().getHeight() * 2 / 10)));
   }
}
