package view;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.awt.Color;

import javazoom.jl.player.Player;

public class StartGameScreen extends JPanel {
    private ImageIcon backgroundGif;
    // private MP3Player bgmPlayer;
    
    public StartGameScreen() {
        // GIF 파일 로드
        backgroundGif = new ImageIcon("res/img/배경화면.gif");
        
        playBgm(); // 음악 재생
        
        // 패널 크기 설정
        setLayout(null); // 다른 컴포넌트 추가 시 사용할 레이아웃
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