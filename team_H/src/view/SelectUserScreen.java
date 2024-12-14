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
        
        // playBgm(); //배경화면 음악 (넘 시끄러워서 잠깐 뺌)
        
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
        
        // 로고 gif 추가
        ImageIcon logoGif = new ImageIcon("res/img/gamelogo.gif");
        JLabel logoLabel = new JLabel(logoGif);
        // 로고 위치 설정
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 0, 10, 0); // 위 20px, 아래 10px 간격
        gbc.anchor = GridBagConstraints.CENTER; // 화면 중앙 정렬
        backgroundLabel.add(logoLabel, gbc); // 로고를 배치
        
        // 버튼 추가
        JButton multiplayerButton = new JButton("멀티플레이어 찾기");
        multiplayerButton.setFont(new Font("맑은 고딕", Font.BOLD, 22)); // 큰 폰트
        multiplayerButton.setForeground(Color.WHITE); // 흰색 텍스트
        multiplayerButton.setBackground(new Color(34, 139, 34)); // 어두운 초록색 기본 배경
        multiplayerButton.setFocusPainted(false); // 포커스 효과 제거
        multiplayerButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 100, 0), 2), // 어두운 초록색 테두리
            BorderFactory.createEmptyBorder(10, 20, 10, 20) // 내부 여백
        ));
        multiplayerButton.setOpaque(true);

        // 입체감 추가
        multiplayerButton.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        multiplayerButton.setBorderPainted(true);

        // 마우스 Hover 및 클릭 효과 추가
        multiplayerButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                multiplayerButton.setBackground(new Color(46, 160, 46)); // Hover 시 밝은 초록색
                multiplayerButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 128, 0), 3), // Hover 시 더 밝은 테두리
                    BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                multiplayerButton.setBackground(new Color(34, 139, 34)); // 기본 초록색 복원
                multiplayerButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 100, 0), 2), // 기본 테두리 복원
                    BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
            }

            public void mousePressed(java.awt.event.MouseEvent evt) {
                multiplayerButton.setBackground(new Color(0, 102, 0)); // 클릭 시 더 어두운 초록색
                multiplayerButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 70, 0), 4), // 클릭 시 두꺼운 테두리
                    BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                multiplayerButton.setBackground(new Color(46, 160, 46)); // Hover 상태 복원
            }
        });

        // 버튼 위치 설정
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(50, 0, 20, 0);
        backgroundLabel.add(multiplayerButton, gbc);
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

    /*
    public void playBgm() {
        Player bgmPlayer = null;
        String bgmFilePath = "res/sound/bgm/로그인창_브금.mp3";
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
    */
}
