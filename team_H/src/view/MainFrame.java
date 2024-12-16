//MainFrame.java

package view;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
	private Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize(); //컴퓨터 해상도 가져오는 코드
	
	public MainFrame() {
		
		setTitle("청계전설");
		setSize(1000,700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false); // 창 크기 조절 불가능 설정
	}
	
	public void setScreen(JPanel newScreen) {
        getContentPane().removeAll();
        add(newScreen);
        revalidate();
        repaint();
    }
	
	
}
