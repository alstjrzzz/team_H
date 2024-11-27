package view;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javazoom.jl.player.Player;
import model.GameState;

import java.awt.Graphics;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JPasswordField;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import java.awt.BorderLayout;

//import javazoom.jl.player.Player; 처리예정

public class StartGameScreen extends JPanel {
    private ImageIcon backgroundGif;
    private JTextField textField;
    private JPasswordField passwordField;
    // private MP3Player bgmPlayer;
    
    public StartGameScreen(GameState gameState) {
       setLayout(null);
       JPanel panel = new JPanel();
       panel.setBackground(new Color(255, 255, 255));
       panel.setBounds(138, 199, 173, 69);
       add(panel);
       GridBagLayout loginPanel = new GridBagLayout();
       loginPanel.columnWidths = new int[] {70, 100};
       loginPanel.rowHeights = new int[] {2, 2};
       loginPanel.columnWeights = new double[]{0.0, 0.0};
       loginPanel.rowWeights = new double[]{0.0, 0.0};
       panel.setLayout(loginPanel);
       
       JLabel lblNewLabel_1 = new JLabel("Username");
       GridBagConstraints login_ID = new GridBagConstraints();
       login_ID.fill = GridBagConstraints.HORIZONTAL;
       login_ID.insets = new Insets(0, 0, 5, 5);
       login_ID.gridx = 0;
       login_ID.gridy = 0;
       panel.add(lblNewLabel_1, login_ID);
       
       textField = new JTextField();
       GridBagConstraints gbc_textField = new GridBagConstraints();
       gbc_textField.fill = GridBagConstraints.HORIZONTAL;
       gbc_textField.insets = new Insets(0, 0, 5, 0);
       gbc_textField.gridx = 1;
       gbc_textField.gridy = 0;
       panel.add(textField, gbc_textField);
       
       JLabel lblNewLabel = new JLabel("Password");
       GridBagConstraints login_password = new GridBagConstraints();
       login_password.fill = GridBagConstraints.HORIZONTAL;
       login_password.insets = new Insets(0, 0, 5, 5);
       login_password.gridx = 0;
       login_password.gridy = 1;
       panel.add(lblNewLabel, login_password);
       
       passwordField = new JPasswordField();
       GridBagConstraints gbc_passwordField = new GridBagConstraints();
       gbc_passwordField.insets = new Insets(0, 0, 5, 0);
       gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
       gbc_passwordField.gridx = 1;
       gbc_passwordField.gridy = 1;
       panel.add(passwordField, gbc_passwordField);
        // GIF 파일 로드
        backgroundGif = new ImageIcon("res/img/배경화면.gif");
        
        playBgm();
    }
    
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 배경을 흰색으로 초기화
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        // GIF 이미지 그리기
        if (backgroundGif != null) {
            g.drawImage(backgroundGif.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }
}