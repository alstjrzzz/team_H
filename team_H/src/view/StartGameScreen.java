package view;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javazoom.jl.player.Player;
import model.GameState;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.awt.Color;
import javax.swing.JPasswordField;
import java.awt.GridBagLayout;
import java.awt.Image;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.Button;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import java.awt.Canvas;

//import javazoom.jl.player.Player; 처리예정

public class StartGameScreen extends JPanel{
    private ImageIcon backgroundGif;
    private ImageIcon logoGif;
    // private MP3Player bgmPlayer;
    
    public StartGameScreen(GameState gameState) {
        // GIF 파일 로드
        backgroundGif = new ImageIcon("res/img/배경화면.gif");
        playBgm();//배경화면 음악
        
        login_panel(); //로그인 패널
        
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
    public void main_page() {
       
    }
    
    
    
    public void login_panel() {
        setLayout(null);

        // 패널 생성
        JPanel panel = new JPanel();
        panel.setOpaque(false); // 투명 설정
        panel.setBounds(300, 450, 175, 52);
        add(panel);

        // 패널 레이아웃 설정
        GridBagLayout loginPanel = new GridBagLayout();
        loginPanel.columnWidths = new int[] {70, 100};
        loginPanel.rowHeights = new int[] {2, 2};
        loginPanel.columnWeights = new double[]{0.0, 0.0};
        loginPanel.rowWeights = new double[]{0.0, 0.0};
        panel.setLayout(loginPanel);

        // 아이디 입력 필드
        JLabel lblNewLabel_1 = new JLabel("Username");
        lblNewLabel_1.setFont(new Font("HY견고딕", Font.PLAIN, 12));
        lblNewLabel_1.setForeground(new Color(255, 255, 255));
        GridBagConstraints login_ID = new GridBagConstraints();
        login_ID.fill = GridBagConstraints.HORIZONTAL;
        login_ID.insets = new Insets(0, 0, 5, 5);
        login_ID.gridx = 0;
        login_ID.gridy = 0;
        panel.add(lblNewLabel_1, login_ID);

        JTextField textField = new JTextField();
        GridBagConstraints gbc_textField = new GridBagConstraints();
        gbc_textField.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField.insets = new Insets(0, 0, 5, 0);
        gbc_textField.gridx = 1;
        gbc_textField.gridy = 0;
        panel.add(textField, gbc_textField);

        // 비밀번호 입력 필드
        JLabel lblNewLabel = new JLabel("Password");
        lblNewLabel.setFont(new Font("HY견고딕", Font.PLAIN, 12));
        lblNewLabel.setForeground(new Color(255, 255, 255));
        lblNewLabel.setBackground(new Color(255, 255, 255));
        GridBagConstraints login_password = new GridBagConstraints();
        login_password.fill = GridBagConstraints.HORIZONTAL;
        login_password.insets = new Insets(0, 0, 5, 5);
        login_password.gridx = 0;
        login_password.gridy = 1;
        panel.add(lblNewLabel, login_password);

        JPasswordField passwordField = new JPasswordField();
        GridBagConstraints gbc_passwordField = new GridBagConstraints();
        gbc_passwordField.insets = new Insets(0, 0, 5, 0);
        gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
        gbc_passwordField.gridx = 1;
        gbc_passwordField.gridy = 1;
        panel.add(passwordField, gbc_passwordField);

        // 로그인 버튼
        Button button = new Button("login");
        button.setBounds(481, 450, 50, 52);
        add(button);
        button.setFont(new Font("HY견고딕", Font.BOLD, 12));
        
        //로고
        logoGif = new ImageIcon("res/img/gamelogo.gif");
        JLabel Main_logo = new JLabel(logoGif);
        Main_logo.setBounds(266, 95, 300, 300);
        add(Main_logo);

        // 버튼 클릭 이벤트
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = textField.getText();
                String password = new String(passwordField.getPassword());

                // 아이디와 비밀번호를 확인
                if (username.equals("admin") && password.equals("1234")) {
                    // 로그인 성공: 패널과 버튼 제거
                    remove(panel);
                    remove(button);
                    remove(Main_logo);
                    // 화면 갱신
                    revalidate();
                    repaint();

                    // 성공 메시지 출력
                    JOptionPane.showMessageDialog(null, "Login Successful!");
                } else {
                    // 로그인 실패: 오류 메시지 출력
                    JOptionPane.showMessageDialog(null, "Invalid Username or Password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        button.setActionCommand("loginbutton");
        
    }
    
   protected BufferedImage TransformColorToTransparency(BufferedImage image, Color c1) {
        final int r1 = c1.getRed();
        final int g1 = c1.getGreen();
        final int b1 = c1.getBlue();
       
        ImageFilter filter = new RGBImageFilter() {
            public int filterRGB(int x, int y, int rgb) {
               int r = ( rgb & 0xFF0000 ) >> 16;
               int g = ( rgb & 0xFF00 ) >> 8;
               int b = ( rgb & 0xFF );
               if( r == r1 && g == g1 && b == b1) {
                  return rgb & 0xFFFFFF;
               }
               return rgb;
            }
         };
       
         ImageProducer ip = new FilteredImageSource( image.getSource(), filter );
         Image img = Toolkit.getDefaultToolkit().createImage(ip);
         BufferedImage dest = new BufferedImage(img.getWidth(null), 
               img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
         Graphics2D g = dest.createGraphics();
         g.drawImage(img, 0, 0, null);
         g.dispose();
         return dest;
      }
}