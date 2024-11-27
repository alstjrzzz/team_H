package view;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
	
	public MainFrame() {
		
		setTitle("청계전설");
		setSize(800, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void setScreen(JPanel newScreen) {
        getContentPane().removeAll();
        add(newScreen);
        revalidate();
        repaint();
    }
}
