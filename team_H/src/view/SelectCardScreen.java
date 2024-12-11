// SelectCardScreen.java

package view;

import java.util.LinkedList;
import java.util.List;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import controller.GameController;
import model.Card;
import model.GameState;
import network.NetworkManager;

public class SelectCardScreen extends JPanel {
	
	private GameState gameState;
	private GameController gameController;
	private NetworkManager networkManager;
	
	private JPanel healthPanel = new JPanel();
	private JPanel selectCardPanel = new JPanel();
	private JPanel fieldPanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	private JPanel selectedCardPanel = new JPanel();
	
	private LinkedList<Card> selectedCardList;

    
	public SelectCardScreen(GameState gameState, GameController gameController, NetworkManager networkManager) {
		
		this.gameState = gameState;
		this.gameController = gameController;
		this.networkManager = networkManager;
		
		selectedCardList = new LinkedList<>();
		selectedCardList.add(null);
		selectedCardList.add(null);
		selectedCardList.add(null);
		
		
		splitPanel();
		drawHealthPanel();
		drawFieldPanel();
		drawButtonPanel();
		drawSelectCardPanel();
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
    	
    	LinkedList<Card> cardList = gameState.getMyCharacter().getCardList();
    	
    	selectCardPanel.setLayout(null);

    	int width = selectCardPanel.getWidth();
    	int height = selectCardPanel.getHeight();
    	
    	int buttonWidth = 40;
    	int buttonHeight = 100;
    	int gap = 50;
    	
    	// 공용카드 버튼 생성
    	for (int i = 0; i < 4; i++) {
    		Card card = cardList.get(i);
    		JButton cardSelectButton = new JButton(card.getName());
    		cardSelectButton.setBounds((int) (width * 0.1) + i * (buttonWidth + gap), (int) (height * 0.1), buttonWidth, buttonHeight);
    		cardSelectButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (int j = 0; j < 3; j++) {
                    	if(selectedCardList.get(j) == null) {
                    		selectedCardList.set(j, card);
                    		for (int k = 0; k < 3; k++) {
                    			if (selectedCardList.get(k) == null) System.out.println(k+"번째 카드: null");
                    			else System.out.println(k+"번째 카드: "+selectedCardList.get(k).getName());
                    		}
                    		break;
                    	}
                    }
                }
            });
    		selectCardPanel.add(cardSelectButton);
    	}
    	
    	// 고유카드 버튼 생성
    	for (int i = 5; i < cardList.size(); i++) {
    		Card card = cardList.get(i);
    		JButton cardSelectButton = new JButton(card.getName());
    		cardSelectButton.setBounds((int) (width * 0.1) + i * (buttonWidth + gap), (int) (height * 0.4), buttonWidth, buttonHeight);
    		cardSelectButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (int j = 0; j < 3; j++) {
                    	if(selectedCardList.get(j) == null) {
                    		selectedCardList.set(j, card);
                    		break;
                    	}
                    }
                }
            });
    		selectCardPanel.add(cardSelectButton);
    	}
    }


	
	// 왼쪽하단에 위치한 가로40% 세로30% 비율의 필드확인칸
    public void drawFieldPanel() {
        fieldPanel.setBackground(Color.green);
        fieldPanel.setLayout(new BorderLayout());
        fieldPanel.setPreferredSize(new Dimension(
                (int) (gameState.getDimension().getWidth() * 4 / 10),
                (int) (gameState.getDimension().getHeight() * 3 / 10)
        ));
        
        
    }




	
	// 중앙하단에 위치한 가로20% 세로30% 비율의 카드선택완료버튼, 도움말버튼, 카드초기화버튼 등
	public void drawButtonPanel() {
		
		buttonPanel.setBackground(Color.red);
		Button readyButton = new Button("READY");
		readyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 카드 선택한게 3개일때만 동작, 카드리스트를 gameState에 저장하고 서버에 전송
            	for (int i = 0; i < 3; i++) {
            		if (selectedCardList.get(i) == null) return;
            	}
            	readyButton.setEnabled(false);
            	gameState.setSelectedCardList(selectedCardList);
            	networkManager.sendCardSelection();
            }
        });
		
		buttonPanel.add(readyButton);
		buttonPanel.setPreferredSize(new Dimension((int)(gameState.getDimension().getWidth() * 2 / 10)
														, (int)(gameState.getDimension().getHeight() * 3 / 10)));
	}
	
	
	
	// 우측하단에 위치한 가로40% 세로30% 비율의 선택된 카드 보여주는칸
	public void drawSelectedCardPanel() {
		
		selectedCardPanel.setBackground(Color.white);
		selectedCardPanel.add(new JLabel("<selected card 1, 2, 3>"));
		selectedCardPanel.setPreferredSize(new Dimension((int)(gameState.getDimension().getWidth() * 4 / 10)
														, (int)(gameState.getDimension().getHeight() * 3 / 10)));
		
		
		selectedCardPanel.setLayout(null);

		int width = selectedCardPanel.getWidth();
		int height = selectedCardPanel.getHeight();
		
    	int buttonWidth = 40;
    	int buttonHeight = 100;
    	int gap = 20;
    	
    	
    	for (int i = 0; i < 3; i++) {
    		
    		final int index = i;
    		JButton selectedCardButton;
    		
    		if (selectedCardList.get(i) == null) {
    			selectedCardButton = new JButton("null");
    			selectedCardButton.setEnabled(false);
    		} else {
    			selectedCardButton = new JButton(selectedCardList.get(i).getName());
    			selectedCardButton.setBounds((int) (width * 0.1) + i * (buttonWidth + gap), (int) (height * 0.1), buttonWidth, buttonHeight);
    			selectedCardButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        selectedCardList.set(index, null);
                        revalidate();
                        repaint();
                    }
                });
    		}
    		selectedCardPanel.add(selectedCardButton);
    	}
    	revalidate();
        repaint();
	}
	
	
}