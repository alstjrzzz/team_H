// StartGameScreen.java
	
package view;
	
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javazoom.jl.player.Player;
import model.GameState;
import network.NetworkManager;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.awt.Color;
import javax.swing.JPasswordField;
import java.awt.GridBagLayout;
import java.awt.Image;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.json.JSONException;
import org.json.JSONObject;

import controller.GameController;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import java.awt.Canvas;
	
public class StartGameScreen extends JPanel {
	
	private GameState gameState;
    private GameController gameController;
    private NetworkManager networkManager;
    private boolean isConnected = false;
    
    public StartGameScreen(GameState gameState, GameController gameController, NetworkManager networkManager) {
    	
    	this.gameState = gameState;
        this.gameController = gameController;
        this.networkManager = networkManager;
        
        initUI();
    }
    private void initUI() {
        setLayout(new BorderLayout()); // 전체 레이아웃을 BorderLayout으로 설정

        // gbc 설정
        GridBagConstraints gbc = new GridBagConstraints();

        // 배경 gif 추가
        ImageIcon gifIcon = new ImageIcon("res/img/오프닝배경1.gif");
        JLabel backgroundLabel = new JLabel();
        backgroundLabel.setLayout(new GridBagLayout()); // GridBagLayout 설정
        add(backgroundLabel, BorderLayout.CENTER);

        // "로딩 ..." 텍스트와 GIF를 함께 표시
        ImageIcon loadingGif = new ImageIcon("res/img/로딩.gif"); // 로딩 GIF
        JLabel loadingLabel = new JLabel();
        loadingLabel.setText("<html>다른 플레이어를 기다리는 중&nbsp;&nbsp;</html>"); // 텍스트 추가 (HTML로 띄어쓰기)
        loadingLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20)); // 텍스트 스타일
        loadingLabel.setForeground(Color.WHITE); // 텍스트 색상
        loadingLabel.setIcon(loadingGif); // 로딩 GIF 추가
        loadingLabel.setHorizontalTextPosition(JLabel.LEFT); // 텍스트를 왼쪽에 배치
        loadingLabel.setVerticalTextPosition(JLabel.CENTER); // 텍스트와 이미지 수직 정렬

        // 로고 위치 설정
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 0, 10, 0); // 위 20px, 아래 10px 간격
        gbc.anchor = GridBagConstraints.CENTER; // 화면 중앙 정렬
        backgroundLabel.add(loadingLabel, gbc); // 로고를 배치

        // 크기 계산 및 GIF 설정
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int newWidth = getWidth();
                int newHeight = getHeight();

                if (newWidth > 0 && newHeight > 0) { // 크기가 유효할 때만 처리
                    Image resizedImage = gifIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
                    backgroundLabel.setIcon(new ImageIcon(resizedImage));
                }
            }
        });

        // 초기 레이아웃 처리
        SwingUtilities.invokeLater(() -> {
            int initialWidth = getWidth();
            int initialHeight = getHeight();

            if (initialWidth > 0 && initialHeight > 0) {
                Image resizedImage = gifIcon.getImage().getScaledInstance(initialWidth, initialHeight, Image.SCALE_DEFAULT);
                backgroundLabel.setIcon(new ImageIcon(resizedImage));
            }
        });
    }

    
}
