import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.AlphaComposite;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Panel extends JPanel implements MouseListener{
ArrayList<Button> currentScreen = new ArrayList<Button>();
BufferedImage bg;
public Panel()
{
	setSize(1600,960);
	try
	{
		bg = ImageIO.read(Panel.class.getResource("/Images/wgsbg.jpg"));
	}
	catch (IOException e)
	{
		e.printStackTrace();
	}

	addMouseListener(this);

	
}

	
@Override
	public void paint(Graphics g)
{
	super.paint(g);
	// paint background
	g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
	for(int i=0;i<currentScreen.size();i++)
	{
		currentScreen.get(i).paint(g);
	}
	
}



	public void mouseClicked(MouseEvent e) {
		
		for(int i=0;i<currentScreen.size();i++)
		{
			
			if(currentScreen.get(i).inBounds(e.getX(), e.getY()))
			{
				currentScreen.get(i).click();
			}
		}
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
			
	

	public void addNotify() {
	    super.addNotify();
	    requestFocus();
	//I reccomend making buttons here
	Button sample = new Button("default","normal",null,true,true,0,0,500,500);
	currentScreen.add(sample);
	    repaint();
	}
}




