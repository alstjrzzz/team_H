// SelectuserScreen.java

package view;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

import controller.GameController;
import javazoom.jl.player.Player;
import model.GameState;
import network.NetworkManager;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

public class SelectUserScreen extends JPanel {

    private MainFrame mainFrame;
    private GameState gameState;
    private GameController gameController;
    private NetworkManager networkManager;

    public SelectUserScreen(MainFrame mainFrame, GameState gameState, GameController gameController, NetworkManager networkManager) {
        this.mainFrame = mainFrame;
        this.gameState = gameState;
        this.gameController = gameController;
        this.networkManager = networkManager;
        
        //playBgm(); //배경화면 음악
        
        initUI();
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
        
        // 로고 gif 추가
        ImageIcon logoGif = new ImageIcon("res/img/logo_bg.gif");
        JLabel logoLabel = new JLabel(logoGif);
        
        // 로고 위치 설정
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(100, 0, 50, 0); // 위 20px, 아래 10px 간격
        gbc.anchor = GridBagConstraints.CENTER; // 화면 중앙 정렬
        backgroundLabel.add(logoLabel, gbc); // 로고를 배치
        
        // 버튼 추가
        JButton multiplayerButton = new JButton("멀티플레이어 찾기") {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getModel().isRollover()) { // 마우스가 올라간 상태
                    g.setColor(Color.WHITE); // 밑줄 색상
                    int y = getHeight() - 3; // 버튼의 아래쪽에서 3px 위에 밑줄 위치
                    g.fillRect(0, y, getWidth(), 2); // 밑줄 그리기: (x, y, width, height)
                }
            }
        };
        multiplayerButton.setFont(new Font("궁서", Font.BOLD, 18));
        multiplayerButton.setForeground(Color.WHITE); // 텍스트 색상
        multiplayerButton.setFocusPainted(false);     // 포커스 테두리 제거
        multiplayerButton.setContentAreaFilled(false); // 버튼 배경 제거
        multiplayerButton.setBorderPainted(false);     // 버튼 테두리 제거
        multiplayerButton.setOpaque(false);            // 완전히 투명
        
        // 스포트라이트 효과를 위한 라벨
        JLabel spotlightLabel = new JLabel();
        spotlightLabel.setBackground(Color.WHITE);
        spotlightLabel.setOpaque(true);
        spotlightLabel.setVisible(false); // 기본 상태에서는 숨김
        
     	// 마우스 Hover 및 클릭 효과 추가
        multiplayerButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                spotlightLabel.setVisible(true);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                spotlightLabel.setVisible(false);
            }
        });

        // 버튼 및 스포트라이트 위치 설정
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(30, 0, 100, 0);
        backgroundLabel.add(multiplayerButton, gbc);

        // 스포트라이트 위치 설정
        gbc.gridy = 2; // 버튼 바로 아래 위치
        gbc.insets = new Insets(-5, 0, 0, 0); // 버튼과의 거리 조정
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 80; // 밑줄 길이
        backgroundLabel.add(spotlightLabel, gbc);
        
        // 버튼 클릭 이벤트
        multiplayerButton.addActionListener(e -> {
            System.out.println("멀티플레이어 찾기 버튼 클릭");
            gameController.showStartGameScreen(); // StartGameScreen으로 전환
        });
        

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

   
    public void playBgm() {
        Player bgmPlayer = null;
        String bgmFilePath = "res/sound/bgm/로그인창_브금2.mp3";
        try {
            FileInputStream fis = new FileInputStream(bgmFilePath);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bgmPlayer = new Player(bis);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        final Player player = bgmPlayer;
        new Thread() {
            public void run() {
                try {
                    player.play();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }.start();
    }
}
