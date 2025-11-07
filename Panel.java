import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
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
private int cardH = 278;
private int cardW = 140;
ArrayList<BufferedImage> birdpics;
ArrayList<Player> players = new ArrayList<Player>();
Player player1;
public Panel()
{
	setSize(1600,960);
	Bird bird1 = null, bird2 = null, bird3 = null, bird4 = null, bird5 = null;
	try
	{
		System.out.println("Loading images...");
		bg = ImageIO.read(Panel.class.getResource("/Images/wgsbg.jpg"));
		System.out.println("Background loaded: " + (bg != null));
		bird1 = new Bird("Acadian Flycatcher", "Empidonax virescens", "cavity", new String[]{"forest", "wetland"}, null, null, null, 0, 0, 0, 0, null, false, false, null, ImageIO.read(Panel.class.getResource("/birds/acadianflycatcher.jpg")));
		bird2 = new Bird("Song Sparrow", "Melospiza melodia", "ground", new String[]{"grassland", "wetland", "plains"}, null, null, null, 0, 0, 0, 0, null, false, false, null, ImageIO.read(Panel.class.getResource("/birds/songsparrow.jpg")));
		bird3 = new Bird("Mallard", "Anas platyrhynchos", "nest on ground", new String[]{"wetland"}, null, null, null, 0, 0, 0, 0, null, false, false, null, ImageIO.read(Panel.class.getResource("/birds/mallard.jpg")));
		bird4 = new Bird("Red-tailed Hawk", "Buteo jamaicensis", "stick", new String[]{"forest", "grassland", "plains"}, null, null, null, 0, 0, 0, 0, null, false, false, null, ImageIO.read(Panel.class.getResource("/birds/redtailedhawk.jpg")));
		bird5 = new Bird("Great Horned Owl", "Bubo virginianus", "stick", new String[]{"forest", "wetland", "grassland"}, null, null, null, 0, 0, 0, 0, null, false, false, null, ImageIO.read(Panel.class.getResource("/birds/greathornedowl.jpg")));
		System.out.println("All images loaded successfully!");
	}
	catch (IOException e)
	{
		System.out.println("ERROR loading images:");
		e.printStackTrace();
	}
	player1 = new Player(new ArrayList<Bird>(), new TreeMap<String,Integer>(), new ArrayList<BonusCard>(), new HashMap<String, ArrayList<Spot>>(), new ArrayList<Button>());
	player1.playerSetHand(new ArrayList<>(Arrays.asList(bird1, bird2, bird3, bird4, bird5)));
	players.add(player1);

	/*
	currentScreen.add(bird1);
	currentScreen.add(bird2);
	currentScreen.add(bird3);
	currentScreen.add(bird4);
	currentScreen.add(bird5);
	*/
	addMouseListener(this);

	
}

@Override
	public void paint(Graphics g)
{
	super.paint(g);
	// paint background
	/*
	g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
	for(int i=0;i<currentScreen.size();i++)
	{
		currentScreen.get(i).paint(g);
	}
	*/
	startingScreen(g, 0);
	
}


	public void startingScreen(Graphics g, int playerIndex)
	{
		ArrayList<Bird> hand = players.get(playerIndex).playerGetHand();
		for(int i =0;i<5;i++)
		{
			g.drawImage(hand.get(i).getImage(), 100 + (i*160), 40, cardW, cardH, null);		
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
	//Button bird1 = new Button("default","normal",null,true,true,0,0,500,500);
	//currentScreen.add(new Button("default","normal",null,true,true,200,200,500,500));
	currentScreen.add(new Button("default","normal",null,true,true,0,0,500,500));
	currentScreen.add(new Button("P1.png","normal",null,true,true,0,0,500,500));
	    repaint();
	}
}




