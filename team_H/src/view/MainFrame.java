package view;

import javax.swing.JFrame;

public class MainFrame extends JFrame {
	public MainFrame() {
		setTitle("청계전설");
		setSize(800, 600);
		add(new StartGameScreen());
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
