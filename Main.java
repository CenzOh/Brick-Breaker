package projectAlba;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Main  {

	private static boolean hasName = false;
	private PlayerName user;
	
	public static void main(String[] args) {
		

		JFrame obj = new JFrame();
		GamePlay gamePlay = new GamePlay();
		obj.setBounds(10,10,700,600);
		obj.setTitle("Brick Breaker");
		obj.setResizable(false);
		
		obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		obj.add(gamePlay);
		obj.setVisible(true);
		
		obj.setLocationRelativeTo(null); 
		//}
	}



}
