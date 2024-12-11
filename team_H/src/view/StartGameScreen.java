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

import org.json.JSONException;
import org.json.JSONObject;

import controller.GameController;
import java.awt.Panel;
import java.awt.Toolkit;
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
    
    
    public StartGameScreen(GameState gameState, GameController gameController, NetworkManager networkManager) {
    	
    	this.gameState = gameState;
        this.gameController = gameController;
        this.networkManager = networkManager;
    }
    
    // 별다른 동작없이 모든 플레이어가 들어오면 자동으로 캐릭터 선택으로 넘어감
    
}
