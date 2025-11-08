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
private int tokenSize = 100;
HashMap<String, BufferedImage> miscpics = new HashMap<String, BufferedImage>();
HashMap<String, Bird> birdcards = new HashMap<String, Bird>();
ArrayList<Player> players = new ArrayList<Player>();
Player player1;
public Panel()
{
	setSize(1600,960);
	
	
	addMouseListener(this);

	
}
public void loadInitialImages()
{
	try
	{
		BufferedImage invertebretoken = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		invertebretoken = ImageIO.read(Panel.class.getResource("/Images/invertebretoken.png"));
		miscpics.put("invertebretoken", invertebretoken);
		BufferedImage wheattoken = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		wheattoken = ImageIO.read(Panel.class.getResource("/Images/wheattoken.png"));
		miscpics.put("wheattoken", wheattoken);
	}
	catch (IOException e)
	{
		System.out.println("ERROR loading images:");
		e.printStackTrace();
	}
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
	
	startingScreen(g, 0);
	
}


	public void startingScreen(Graphics g, int playerIndex)
	{
		/*ArrayList<Bird> hand = players.get(playerIndex).playerGetHand();
		for(int i =0;i<5;i++)
		{
			g.drawImage(hand.get(i).getImage(), 100 + (i*160), 40, cardW, cardH, null);		
		}*/
		g.drawImage(miscpics.get("wheattoken"), 100, getHeight()-200, tokenSize, tokenSize, null);
		g.drawImage(miscpics.get("invertebretoken"), 250, getHeight()-200, tokenSize, tokenSize, null);

		
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
	
	/*	TRY TO INITIALIZE OBJECTS HERE SO THAT THERE ARE NO POINTER EXCEPTIONS
	 * 
	 *
	 */
	public void addNotify() {
	    super.addNotify();
	    requestFocus();
	loadInitialImages();
	Bird bird1 = null, bird2 = null, bird3 = null, bird4 = null, bird5 = null;
	try
	{
		System.out.println("Loading images...");
		bg = ImageIO.read(Panel.class.getResource("/Images/wgsbg.jpg"));
		System.out.println("Background loaded: " + (bg != null));
		// Position birds horizontally across the screen with spacing
		int startX = 50;  // Start 50 pixels from the left
		int spacing = 160;  // Space between cards
		int startY = 100;  // Start 100 pixels from the top
		
		bird1 = new Bird("Acadian Flycatcher", "Empidonax virescens", "cavity", new String[]{"forest", "wetland"}, null, null, null, 0, 0, 0, 0, null, false, false, null, ImageIO.read(Panel.class.getResource("/birds/acadianflycatcher.jpg")), startX, startY);
		bird2 = new Bird("Song Sparrow", "Melospiza melodia", "ground", new String[]{"grassland", "wetland", "plains"}, null, null, null, 0, 0, 0, 0, null, false, false, null, ImageIO.read(Panel.class.getResource("/birds/songsparrow.jpg")), startX + spacing, startY);
		bird3 = new Bird("Mallard", "Anas platyrhynchos", "nest on ground", new String[]{"wetland"}, null, null, null, 0, 0, 0, 0, null, false, false, null, ImageIO.read(Panel.class.getResource("/birds/mallard.jpg")), startX + spacing * 2, startY);
		bird4 = new Bird("Red-tailed Hawk", "Buteo jamaicensis", "stick", new String[]{"forest", "grassland", "plains"}, null, null, null, 0, 0, 0, 0, null, false, false, null, ImageIO.read(Panel.class.getResource("/birds/redtailedhawk.jpg")), startX + spacing * 3, startY);
		bird5 = new Bird("Great Horned Owl", "Bubo virginianus", "stick", new String[]{"forest", "wetland", "grassland"}, null, null, null, 0, 0, 0, 0, null, false, false, null, ImageIO.read(Panel.class.getResource("/birds/greathornedowl.jpg")), startX + spacing * 4, startY);
		System.out.println("All images loaded successfully!");
		
		
	}
	catch (IOException e)
	{
		System.out.println("ERROR loading images:");
		e.printStackTrace();
	}
	
	/*birdcards.put("Acadian Flycatcher", bird1);
	birdcards.put("Song Sparrow", bird2);
	birdcards.put("Mallard", bird3);
	birdcards.put("Red-tailed Hawk", bird4);
	birdcards.put("Great Horned Owl", bird5);
	*/

	
	
	

	player1 = new Player(new ArrayList<Bird>(),
                     new TreeMap<String,Integer>(),
                     new ArrayList<BonusCard>(),
                     new HashMap<String, ArrayList<Spot>>(),
                     new ArrayList<Button>());

player1.playerSetHand(new ArrayList<>(Arrays.asList(bird1, bird2, bird3, bird4, bird5)));
players.add(player1);

//if you are panicking that I made permenant renderings of the birds, don't worry
	//just comment the bottom line of code to remove them from the screen
currentScreen.addAll(player1.playerGetHand());
	    repaint();
	}
}




