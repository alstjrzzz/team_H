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
import javax.swing.Timer;

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
    private JLabel loadingLabel; // 로딩 텍스트 레이블
    private int dotCount = 0;    // 점 개수를 추적하는 변수
    
    public StartGameScreen(GameState gameState, GameController gameController, NetworkManager networkManager) {
    	
    	this.gameState = gameState;
        this.gameController = gameController;
        this.networkManager = networkManager;
        
        initUI();
        startLoadingAnimation(); // 애니메이션 시작
    }
    private void initUI() {
        setLayout(new BorderLayout()); // 전체 레이아웃을 BorderLayout으로 설정

        // gbc 설정
        GridBagConstraints gbc = new GridBagConstraints();

        // 배경 gif 추가
        ImageIcon gifIcon = new ImageIcon("res/img/오프닝배경2.gif");
        JLabel backgroundLabel = new JLabel();
        backgroundLabel.setLayout(new GridBagLayout()); // GridBagLayout 설정
        add(backgroundLabel, BorderLayout.CENTER);

        // "로딩 ..." 텍스트 레이블
        loadingLabel = new JLabel();
        loadingLabel.setText("다른 플레이어를 기다리는 중 "); // 초기 텍스트
        loadingLabel.setFont(new Font("궁서", Font.BOLD, 18)); // 텍스트 스타일
        loadingLabel.setForeground(Color.WHITE); // 텍스트 색상
        
        backgroundLabel.add(loadingLabel); // 화면 중앙에 배치


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
    private void startLoadingAnimation() {
        // Timer를 사용해 일정 시간마다 텍스트 갱신
        Timer timer = new Timer(500, e -> { // 500ms(0.5초)마다 실행
            dotCount = (dotCount + 1) % 4; // 점 개수는 0, 1, 2, 3 순환
            String dots = "";
            for (int i = 0; i < dotCount; i++) {
                dots += "."; // 점 추가
            }
            loadingLabel.setText("다른 플레이어를 기다리는 중 " + dots);
        });
        timer.start(); // 타이머 시작
    }

    
}
