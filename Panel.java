/*  Change the Panel so that the only things that are displayed are Button objects and things that inherit it
Only do global logic in the Panel class
this means that only logic in the Panel class should be coordinating between buttons and Player objects
 */
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;
import javax.swing.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Panel extends JPanel implements MouseListener, MouseMotionListener{
ArrayList<Button> currentScreen = new ArrayList<Button>();
BufferedImage bg;
private int cardH = 278;
private int cardW = 140;
private int tokenSize = 150;
HashMap<String, BufferedImage> miscpics = new HashMap<String, BufferedImage>();
HashMap<String, Bird> birdcards = new HashMap<String, Bird>();
ArrayList<Player> players = new ArrayList<Player>();
Player player1;
// starting selection state
private boolean startingComplete = false;
private boolean namesEntered = false;
private int selectedCount = 0;
// Button lists for starting screen
private ArrayList<Button> birdButtons = new ArrayList<Button>();
private ArrayList<Button> tokenButtons = new ArrayList<Button>();
private ArrayList<Button> selectionSlotButtons = new ArrayList<Button>();
private int currentPlayerIndex = 0;
private int lastInitializedPlayer = -1; // Track which player's buttons were last initialized
private boolean playerTransitioning = false; // Prevent multiple transitions
public Panel()
{
    setSize(1600,960);
    
    
    addMouseListener(this);
    addMouseMotionListener(this);

    
}
public void loadInitialImages()
{
    try
    {
        BufferedImage invertebretoken = ImageIO.read(Panel.class.getResource("/Images/invertebratetoken.png"));
        if(invertebretoken != null) {
            invertebretoken = trimTransparent(invertebretoken);
            miscpics.put("invertebratetoken", invertebretoken);
        }
        BufferedImage wheattoken = ImageIO.read(Panel.class.getResource("/Images/wheattoken.png"));
        if(wheattoken != null) {
            wheattoken = trimTransparent(wheattoken);
            miscpics.put("wheattoken", wheattoken);
        }
        BufferedImage fishtoken = ImageIO.read(Panel.class.getResource("/Images/fishtoken.png"));
        if(fishtoken != null) {
            fishtoken = trimTransparent(fishtoken);
            miscpics.put("fishtoken", fishtoken);
        }
        BufferedImage foodtoken = ImageIO.read(Panel.class.getResource("/Images/foodtoken.png"));
        if(foodtoken != null) {
            foodtoken = trimTransparent(foodtoken);
            miscpics.put("foodtoken", foodtoken);
        }
        BufferedImage rattoken = ImageIO.read(Panel.class.getResource("/Images/rattoken.png"));
        if(rattoken != null) {
            rattoken = trimTransparent(rattoken);
            miscpics.put("rattoken", rattoken);
        }
    }
    catch (Exception e)
    {
        System.out.println("ERROR loading images:");
        e.printStackTrace();
    }
}

@Override
	public void paintComponent(Graphics g)
{
	super.paintComponent(g);
	
	// Always paint background
	if (bg != null) {
		g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
	} else {
		g.setColor(new Color(200, 220, 235));
		g.fillRect(0, 0, getWidth(), getHeight());
	}
	
	// MODIFIED: Only show startingScreen after names are entered
	if(!namesEntered) {
		// Draw title over background
		g.setFont(new Font("Century Gothic", Font.BOLD, 40));
		g.setColor(Color.WHITE);
		g.drawString("Wingspan - Enter Player Names", 420, 180);
		
		// Draw the Go button and text fields are visible on top
		for(int i=0;i<currentScreen.size();i++)
		{
			currentScreen.get(i).paint(g);
		}
	} else if(!startingComplete) {
		// Draw the selection box background FIRST
		drawSelectionBoxBackground(g, currentPlayerIndex);
		
		// Then draw all buttons (birds, tokens, selection slots) ON TOP
		for(int i=0;i<currentScreen.size();i++)
		{
			currentScreen.get(i).paint(g);
		}
	} else {
		// Normal game mode - just draw buttons
		for(int i=0;i<currentScreen.size();i++)
		{
			currentScreen.get(i).paint(g);
		}
	}
	
}public void realStartingScreen()
{
    // Set null layout for absolute positioning
    this.setLayout(null);
    
    // Create 4 text fields for player names with better sizing
    JTextField tf1 = new JTextField("Player 1");
    JTextField tf2 = new JTextField("Player 2");
    JTextField tf3 = new JTextField("Player 3");
    JTextField tf4 = new JTextField("Player 4");
    
    // Make text fields larger and more visible with Century Gothic font
    Font tfFont = new Font("Century Gothic", Font.PLAIN, 20);
    tf1.setFont(tfFont);
    tf2.setFont(tfFont);
    tf3.setFont(tfFont);
    tf4.setFont(tfFont);
    
    // Position text fields vertically in the center with better spacing
    int centerX = 600;
    int startY = 250;
    int spacing = 70;
    int tfWidth = 400;
    int tfHeight = 50;
    
    tf1.setBounds(centerX, startY, tfWidth, tfHeight);
    tf2.setBounds(centerX, startY + spacing, tfWidth, tfHeight);
    tf3.setBounds(centerX, startY + spacing * 2, tfWidth, tfHeight);
    tf4.setBounds(centerX, startY + spacing * 3, tfWidth, tfHeight);
    
    // Add text fields to the panel
    this.add(tf1);
    this.add(tf2);
    this.add(tf3);
    this.add(tf4);
    
    // Create custom Button for "Go!" button
    Button goButton = new Button("go_button", "normal", null, true, true, 
                                  centerX + 125, startY + spacing * 4 + 20, 
                                  centerX + 125 + 150, startY + spacing * 4 + 20 + 60) {
        @Override
        public void paint(Graphics g) {
            if (!display) return;
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(new Color(70, 130, 180));
            g2.fillRoundRect(x1, y1, width, height, 15, 15);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Century Gothic", Font.BOLD, 24));
            FontMetrics fm = g2.getFontMetrics();
            String text = "Go!";
            int textX = x1 + (width - fm.stringWidth(text)) / 2;
            int textY = y1 + ((height - fm.getHeight()) / 2) + fm.getAscent();
            g2.drawString(text, textX, textY);
        }
        
        @Override
        public void click() {
            if (!clickable) return;
            
            String name1 = tf1.getText();
            String name2 = tf2.getText();
            String name3 = tf3.getText();
            String name4 = tf4.getText();
            
            System.out.println("Player names:");
            System.out.println("1: " + name1);
            System.out.println("2: " + name2);
            System.out.println("3: " + name3);
            System.out.println("4: " + name4);
            
            // Create 4 players with their names
            players.clear();
            for(int i = 0; i < 4; i++) {
                String playerName = (i == 0) ? name1 : (i == 1) ? name2 : (i == 2) ? name3 : name4;
                
                // Create 5 birds for each player
                ArrayList<Bird> playerHand = new ArrayList<>();
                try {
                    int startX = 50;
                    int birdSpacing = 160;
                    int startY = 100;
                    
                    playerHand.add(new Bird("Acadian Flycatcher", "Empidonax virescens", "cavity", new String[]{"forest", "wetland"}, null, null, null, 0, 0, 0, 0, null, false, false, null, ImageIO.read(Panel.class.getResource("/birds/acadianflycatcher.jpg")), startX, startY));
                    playerHand.add(new Bird("Song Sparrow", "Melospiza melodia", "ground", new String[]{"grassland", "wetland", "plains"}, null, null, null, 0, 0, 0, 0, null, false, false, null, ImageIO.read(Panel.class.getResource("/birds/songsparrow.jpg")), startX + birdSpacing, startY));
                    playerHand.add(new Bird("Mallard", "Anas platyrhynchos", "nest on ground", new String[]{"wetland"}, null, null, null, 0, 0, 0, 0, null, false, false, null, ImageIO.read(Panel.class.getResource("/birds/mallard.jpg")), startX + birdSpacing * 2, startY));
                    playerHand.add(new Bird("Red-tailed Hawk", "Buteo jamaicensis", "stick", new String[]{"forest", "grassland", "plains"}, null, null, null, 0, 0, 0, 0, null, false, false, null, ImageIO.read(Panel.class.getResource("/birds/redtailedhawk.jpg")), startX + birdSpacing * 3, startY));
                    playerHand.add(new Bird("Great Horned Owl", "Bubo virginianus", "stick", new String[]{"forest", "wetland", "grassland"}, null, null, null, 0, 0, 0, 0, null, false, false, null, ImageIO.read(Panel.class.getResource("/birds/greathornedowl.jpg")), startX + birdSpacing * 4, startY));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                
                Player newPlayer = new Player(playerHand,
                                             new TreeMap<String,Integer>(),
                                             new ArrayList<BonusCard>(),
                                             new HashMap<String, ArrayList<Spot>>(),
                                             new ArrayList<Button>());
                players.add(newPlayer);
            }

            // Remove text fields
            Panel.this.remove(tf1);
            Panel.this.remove(tf2);
            Panel.this.remove(tf3);
            Panel.this.remove(tf4);
            
            // Remove this button from currentScreen
            currentScreen.remove(this);
            
            // Start the selection process for first player
            namesEntered = true;
            currentPlayerIndex = 0;
            selectedCount = 0;
            
            Panel.this.revalidate();
            Panel.this.repaint();
        }
    };
    
    currentScreen.add(goButton);
    this.revalidate();
    this.repaint();
}
    private void drawSelectionBoxBackground(Graphics g, int playerIndex)
    {
        // Safety check
        if (players.isEmpty() || playerIndex >= players.size()) {
            return;
        }
        
        // Display current player's name at top
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.setColor(Color.BLACK);
        String playerName = "Player " + (playerIndex + 1);
        g2.drawString(playerName + "'s Turn - Select 5 items", 50, 30);
        
        // Initialize buttons if needed (first time or player changed)
        if (lastInitializedPlayer != playerIndex) {
            initializeStartingScreenButtons(playerIndex);
            lastInitializedPlayer = playerIndex;
        }
        
        // Draw selection box background - calculate actual pixel positions
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        
        // Calculate in 0-1000 scale then convert to actual pixels
        int margin = 10;
        int gap = 3;
        int padding = 8;
        int labelH = 15;
        int cardWScaled = 87;
        int cardHScaled = 289;
        int boxWScaled = 5 * cardWScaled + 4 * gap + 2 * padding;
        int boxXScaled = 1000 - boxWScaled - margin;
        int boxYScaled = margin;
        
        // Convert to actual pixels
        int boxX = boxXScaled * panelWidth / 1000;
        int boxY = boxYScaled * panelHeight / 1000;
        int boxW = boxWScaled * panelWidth / 1000;
        int boxH = (cardHScaled + 2 * padding + labelH) * panelHeight / 1000;
        
        g2.setColor(new Color(255,255,255,230));  // More opaque white background
        g2.fillRoundRect(boxX, boxY, boxW, boxH, 10, 10);
        g2.setColor(Color.DARK_GRAY);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(boxX, boxY, boxW, boxH, 10, 10);
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString("Keep (" + selectedCount + "/5)", boxX + (padding * panelWidth / 1000), boxY + 16);
    }
    
    private void transitionToNextPlayer()
    {
        System.out.println("Player " + (currentPlayerIndex + 1) + " completed selection!");
        
        // Move to next player
        currentPlayerIndex++;
        selectedCount = 0;
        lastInitializedPlayer = -1; // Reset to force re-initialization for next player
        playerTransitioning = false; // Reset flag
        
        // Clear selections for next player
        for (Button bb : birdButtons) {
            bb.setSelected(false);
        }
        for (Button tb : tokenButtons) {
            tb.setSelected(false);
        }
        for (Button ssb : selectionSlotButtons) {
            ssb.clearSlot();
        }
        
        // If all 4 players done, finish starting phase
        if (currentPlayerIndex >= 4) {
            startingComplete = true;
            System.out.println("All players have made their selections!");
            // Clean up buttons
            currentScreen.removeAll(birdButtons);
            currentScreen.removeAll(tokenButtons);
            currentScreen.removeAll(selectionSlotButtons);
        } else {
            // Initialize for next player
            System.out.println("Moving to player " + (currentPlayerIndex + 1));
            initializeStartingScreenButtons(currentPlayerIndex);
        }
        
        repaint();
    }
    
    private void initializeStartingScreenButtons(int playerIndex) {
        if (playerIndex >= players.size()) return;
        
        // Clear old buttons from currentScreen
        currentScreen.removeAll(birdButtons);
        currentScreen.removeAll(tokenButtons);
        currentScreen.removeAll(selectionSlotButtons);
        birdButtons.clear();
        tokenButtons.clear();
        selectionSlotButtons.clear();
        
        // ALL COORDINATES ARE IN 0-1000 SCALE (Button class scales them to actual pixels)
        // Calculate selection box dimensions in scaled coordinates
        int margin = 10;  // in 0-1000 scale
        int gap = 3;  // gap between cards in 0-1000 scale
        int padding = 8;
        int labelH = 15;
        int cardWScaled = 87;  // cardW (140) in 0-1000 scale: 140 * 1000 / 1600
        int cardHScaled = 289; // cardH (278) in 0-1000 scale: 278 * 1000 / 960
        int boxW = 5 * cardWScaled + 4 * gap + 2 * padding;
        int boxX = 1000 - boxW - margin;  // from right edge
        int boxY = margin;
        
        System.out.println("Initializing player " + (playerIndex + 1) + " buttons");
        System.out.println("Selection box at scaled x=" + boxX + ", y=" + boxY + ", width=" + boxW);
        
        // Create bird buttons - positioned below selection box (in 0-1000 scale)
        ArrayList<Bird> hand = players.get(playerIndex).playerGetHand();
        int topMargin = boxY + cardHScaled + 30;  // Position below the selection box
        int cardSpacing = 3;  // in 0-1000 scale
        for (int i = 0; i < hand.size() && i < 5; i++) {
            Bird b = hand.get(i);
            int x = 20 + i * (cardWScaled + cardSpacing);
            Button bb = new Button("bird_" + i, "normal", b.getImage(), true, true, x, topMargin, x + cardWScaled, topMargin + cardHScaled);
            bb.setBird(b);
            birdButtons.add(bb);
            currentScreen.add(bb);
        }
        
        // Create token buttons (in 0-1000 scale)
        int yTop = 740;  // bottom of screen in 0-1000 scale
        int tokenSizeScaled = 94;  // tokenSize (150) in 0-1000 scale: 150 * 1000 / 1600
        String[] tokenNames = {"wheattoken", "invertebratetoken", "fishtoken", "foodtoken", "rattoken"};
        int[] tokenXPositions = {63, 156, 250, 344, 438};  // in 0-1000 scale
        for (int i = 0; i < tokenNames.length; i++) {
            BufferedImage img = miscpics.get(tokenNames[i]);
            if (img != null) {
                Button tb = new Button("token_" + tokenNames[i], "normal", img, true, true, 
                    tokenXPositions[i], yTop, tokenXPositions[i] + tokenSizeScaled, yTop + tokenSizeScaled);
                tb.setTokenName(tokenNames[i]);
                tokenButtons.add(tb);
                currentScreen.add(tb);
            }
        }
        
        // Create selection slot buttons (in 0-1000 scale)
        int sx = boxX + padding;
        int sy = boxY + labelH + padding;
        for (int i = 0; i < 5; i++) {
            int x = sx + i * (cardWScaled + gap);
            Button ssb = new Button("slot_" + i, "empty", null, true, true, x, sy, x + cardWScaled, sy + cardHScaled);
            selectionSlotButtons.add(ssb);
            System.out.println("Created slot " + i + " at scaled x=" + x + ", y=" + sy);
        }
        // Add selection slots after other buttons
        currentScreen.addAll(selectionSlotButtons);
    }


    // trims fully transparent borders so different PNG paddings appear same size when scaled
    private BufferedImage trimTransparent(BufferedImage img) {
        if (img == null) return null;
        int w = img.getWidth();
        int h = img.getHeight();
        int minX = w, minY = h, maxX = -1, maxY = -1;
        final int alphaThreshold = 10;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int a = (img.getRGB(x, y) >>> 24) & 0xFF;
                if (a > alphaThreshold) {
                    if (x < minX) minX = x;
                    if (y < minY) minY = y;
                    if (x > maxX) maxX = x;
                    if (y > maxY) maxY = y;
                }
            }
        }
        if (maxX < minX || maxY < minY) return img;
        int tw = maxX - minX + 1;
        int th = maxY - minY + 1;
        try {
            return img.getSubimage(minX, minY, tw, th);
        } catch (Exception e) {
            return img;
        }
    }
    public void mouseClicked(MouseEvent e) {
        if(!startingComplete && namesEntered && !players.isEmpty()){
            // Check selection slot buttons first (for removal)
            for (Button ssb : selectionSlotButtons) {
                if (ssb.inBounds(e.getX(), e.getY()) && !ssb.isEmpty()) {
                    // Remove from selection
                    if (ssb.isBird()) {
                        Bird b = ssb.getBird();
                        // Find matching bird button and un-select it
                        for (Button bb : birdButtons) {
                            if (bb.getBird() == b) {
                                bb.setSelected(false);
                                break;
                            }
                        }
                    } else if (ssb.isToken()) {
                        String tokenName = ssb.getTokenName();
                        // Find matching token button and un-select it
                        for (Button tb : tokenButtons) {
                            if (tb.getTokenName() != null && tb.getTokenName().equals(tokenName)) {
                                tb.setSelected(false);
                                break;
                            }
                        }
                    }
                    ssb.clearSlot();
                    selectedCount--;
                    System.out.println("Removed from selection. Now " + selectedCount + "/5");
                    repaint();
                    return;
                }
            }
            
            // Check bird buttons
            for (Button bb : birdButtons) {
                if (bb.inBounds(e.getX(), e.getY()) && !bb.isSelected()) {
                    if (selectedCount < 5) {
                        bb.setSelected(true);
                        // Add to first empty slot
                        for (Button ssb : selectionSlotButtons) {
                            if (ssb.isEmpty()) {
                                Bird selectedBird = bb.getBird();
                                ssb.setBird(selectedBird);
                                ssb.setImage(selectedBird.getImage());
                                ssb.setState("has_bird");
                                selectedCount++;
                                System.out.println("Added bird: " + selectedBird.getName() + " to slot. Now " + selectedCount + "/5");
                                
                                // Check if this player is done selecting
                                if (selectedCount >= 5 && !playerTransitioning) {
                                    playerTransitioning = true;
                                    transitionToNextPlayer();
                                }
                                repaint();
                                return;
                            }
                        }
                    }
                    repaint();
                    return;
                }
            }
            
            // Check token buttons
            for (Button tb : tokenButtons) {
                if (tb.inBounds(e.getX(), e.getY()) && !tb.isSelected()) {
                    if (selectedCount < 5) {
                        tb.setSelected(true);
                        // Add to first empty slot
                        for (Button ssb : selectionSlotButtons) {
                            if (ssb.isEmpty()) {
                                ssb.setTokenName(tb.getTokenName());
                                ssb.setSecondaryImage(tb.image);
                                ssb.setImage(tb.image);
                                ssb.setState("has_token");
                                selectedCount++;
                                System.out.println("Added token: " + tb.getTokenName() + " to slot. Now " + selectedCount + "/5");
                                
                                // Check if this player is done selecting
                                if (selectedCount >= 5 && !playerTransitioning) {
                                    playerTransitioning = true;
                                    transitionToNextPlayer();
                                }
                                repaint();
                                return;
                            }
                        }
                    }
                    repaint();
                    return;
                }
            }
        }

        // General button click handling
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
    public void mouseMoved(MouseEvent e) {
        if (!startingComplete && namesEntered && !players.isEmpty()) {
            boolean changed = false;
            
            // Check hover over selection slots
            for (Button ssb : selectionSlotButtons) {
                boolean wasHovered = ssb.isHovered();
                boolean isNowHovered = ssb.inBounds(e.getX(), e.getY()) && !ssb.isEmpty();
                ssb.setHovered(isNowHovered);
                if (wasHovered != isNowHovered) changed = true;
            }
            
            // Check hover over bird buttons
            for (Button bb : birdButtons) {
                boolean wasHovered = bb.isHovered();
                boolean isNowHovered = bb.inBounds(e.getX(), e.getY()) && !bb.isSelected();
                bb.setHovered(isNowHovered);
                if (wasHovered != isNowHovered) changed = true;
            }
            
            // Check hover over token buttons
            for (Button tb : tokenButtons) {
                boolean wasHovered = tb.isHovered();
                boolean isNowHovered = tb.inBounds(e.getX(), e.getY()) && !tb.isSelected();
                tb.setHovered(isNowHovered);
                if (wasHovered != isNowHovered) changed = true;
            }
            
            if (changed) {
                repaint();
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // not used
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
        
        try
        {
            System.out.println("Loading images...");
            bg = ImageIO.read(Panel.class.getResource("/Images/wgsbg.jpg"));
            System.out.println("Background loaded: " + (bg != null));
        }
        catch (Exception e)
        {
            System.out.println("ERROR loading images:");
            e.printStackTrace();
        }
        
        currentScreen.clear();
        
        // Start with the player name entry screen
        realStartingScreen();
    }
}