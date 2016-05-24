package services.draw;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class DrawTest extends JFrame{
	
	private static final long serialVersionUID = -767027438190961502L;
	
	BufferedImage img;
	
	public DrawTest(BufferedImage img) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1074, 1074);
		setLocation(100, 100);
		this.img = img;
		setVisible(true);
		
	};
	
	@Override
	public void paint(Graphics g) {
		super.paintComponents(g);
		g.drawImage(img, 20, 50, null);
	}

}
