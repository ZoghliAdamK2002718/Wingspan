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
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javafx.scene.shape.Circle;

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
private boolean namesEntered = false; // NEW: track if names have been entered
private ArrayList<ItemRef> selected = new ArrayList<ItemRef>();
private ArrayList<Slot> selectionSlots = new ArrayList<Slot>();
private ArrayList<TokenItem> tokenItems = new ArrayList<TokenItem>();
// hover state
private Bird hoverBird = null;
private String hoverTokenName = null; // token name from tokenItems
private ItemRef hoverSlotItem = null; // item under cursor inside selection box
private int currentPlayerIndex = 0;
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
	public void paint(Graphics g)
{
	super.paint(g);
	
	// Always paint background
	if (bg != null) {
		g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
	} else {
		g.setColor(new Color(200, 220, 235));
		g.fillRect(0, 0, getWidth(), getHeight());
	}
	
	for(int i=0;i<currentScreen.size();i++)
	{
		currentScreen.get(i).paint(g);
	}
	
	// MODIFIED: Only show startingScreen after names are entered
	if(!namesEntered) {
		// Draw title over background
		g.setFont(new Font("Century Gothic", Font.BOLD, 40));
		g.setColor(Color.WHITE);
		g.drawString("Wingspan - Enter Player Names", 420, 180);
		
		// Text fields are visible on top
	} else if(!startingComplete) {
		startingScreen(g, currentPlayerIndex);
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
    
    // Create standard Swing "Go!" button below the text fields
    JButton goButton = new JButton("Go!");
    goButton.setFont(new Font("Century Gothic", Font.BOLD, 24));
    goButton.setBounds(centerX + 125, startY + spacing * 4 + 20, 150, 60);
    goButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e)
        {
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
                    int spacing = 160;
                    int startY = 100;
                    
                    playerHand.add(new Bird("Acadian Flycatcher", "Empidonax virescens", "cavity", new String[]{"forest", "wetland"}, null, null, null, 0, 0, 0, 0, null, false, false, null, ImageIO.read(Panel.class.getResource("/birds/acadianflycatcher.jpg")), startX, startY));
                    playerHand.add(new Bird("Song Sparrow", "Melospiza melodia", "ground", new String[]{"grassland", "wetland", "plains"}, null, null, null, 0, 0, 0, 0, null, false, false, null, ImageIO.read(Panel.class.getResource("/birds/songsparrow.jpg")), startX + spacing, startY));
                    playerHand.add(new Bird("Mallard", "Anas platyrhynchos", "nest on ground", new String[]{"wetland"}, null, null, null, 0, 0, 0, 0, null, false, false, null, ImageIO.read(Panel.class.getResource("/birds/mallard.jpg")), startX + spacing * 2, startY));
                    playerHand.add(new Bird("Red-tailed Hawk", "Buteo jamaicensis", "stick", new String[]{"forest", "grassland", "plains"}, null, null, null, 0, 0, 0, 0, null, false, false, null, ImageIO.read(Panel.class.getResource("/birds/redtailedhawk.jpg")), startX + spacing * 3, startY));
                    playerHand.add(new Bird("Great Horned Owl", "Bubo virginianus", "stick", new String[]{"forest", "wetland", "grassland"}, null, null, null, 0, 0, 0, 0, null, false, false, null, ImageIO.read(Panel.class.getResource("/birds/greathornedowl.jpg")), startX + spacing * 4, startY));
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

            // Remove text fields and button
            Panel.this.remove(tf1);
            Panel.this.remove(tf2);
            Panel.this.remove(tf3);
            Panel.this.remove(tf4);
            Panel.this.remove(goButton);
            
            // Start the selection process for first player
            namesEntered = true;
            currentPlayerIndex = 0;
            selected.clear();
            
            Panel.this.revalidate();
            Panel.this.repaint();
        }
    });
    
    this.add(goButton);
    this.revalidate();
    this.repaint();
}
    public void startingScreen(Graphics g, int playerIndex)
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
        
        ArrayList<Bird> hand = players.get(playerIndex).playerGetHand();
        int topMargin = 60; // draw near the top of the screen
        for(int i = 0; i < 5; i++)
        {
            Bird b = hand.get(i);
            if (isSelected(b)) continue; // hide birds already kept
            b.setX(50 + i * (cardW + 20));
            b.setY(topMargin);
            Rectangle r = b.getBounds();
            int drawW = cardW;
            int drawH = cardH;
            int drawX = r.x;
            int drawY = r.y;
            if (b == hoverBird) {
                double scale = 1.12;
                drawW = (int)Math.round(cardW * scale);
                drawH = (int)Math.round(cardH * scale);
                drawX = r.x - (drawW - cardW)/2;
                drawY = r.y - (drawH - cardH)/2;
            }
            drawCard(g, b.getImage(), drawX, drawY, drawW, drawH);
        }
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Build/refresh token positions for hit testing and draw those not yet kept
        ensureTokenItems();
        for (TokenItem t : tokenItems) {
            if (isSelectedToken(t.name)) continue; // hide tokens already kept
            int size = tokenSize;
            int x = t.cx - size/2;
            int y = t.cy - size/2;
            if (t.name.equals(hoverTokenName)) {
                double scale = 1.18;
                int newSize = (int)Math.round(size * scale);
                x = t.cx - newSize/2;
                y = t.cy - newSize/2;
                size = newSize;
            }
            drawToken(g2, t.image, x, y, size);
        }

        // Draw selection box and selected items (top-right) sized for full cards
        int margin = 16;
        int slotW = cardW;
        int slotH = cardH;
        int gap = 12;
        int padding = 12;
        int labelH = 22;
        int boxW = 5 * slotW + 4 * gap + 2 * padding;
        int boxH = slotH + 2 * padding + labelH; // include label area
        int boxX = getWidth() - boxW - margin;
        int boxY = margin;
        g2.setColor(new Color(255,255,255,210));
        g2.fillRoundRect(boxX, boxY, boxW, boxH, 10, 10);
        g2.setColor(Color.DARK_GRAY);
        g2.drawRoundRect(boxX, boxY, boxW, boxH, 10, 10);
        g2.drawString("Keep (" + selected.size() + "/5)", boxX + padding, boxY + 16);

        selectionSlots.clear();
        int sx = boxX + padding;
        int sy = boxY + labelH + padding;
        for (int i = 0; i < 5; i++) {
            int x = sx + i * (slotW + gap);
            int y = sy;
            Rectangle slotRect = new Rectangle(x, y, slotW, slotH);
            g2.setColor(new Color(0,0,0,40));
            g2.drawRect(x, y, slotW, slotH);
            if (i < selected.size()) {
                ItemRef it = selected.get(i);
                boolean isHover = (hoverSlotItem == it);
                if (it.type == ItemRef.Type.TOKEN) {
                    int ts = Math.min(slotW, slotH) - 10;
                    if (isHover) ts = (int)Math.round(ts * 1.15);
                    drawToken(g2, miscpics.get(it.tokenName), x + (slotW - ts)/2, y + (slotH - ts)/2, ts);
                } else if (it.type == ItemRef.Type.BIRD && it.bird != null) {
                    BufferedImage bi = it.bird.getImage();
                    int dw = slotW;
                    int dh = slotH;
                    int dx = x;
                    int dy = y;
                    if (isHover) {
                        double scale = 1.12;
                        dw = (int)Math.round(slotW * scale);
                        dh = (int)Math.round(slotH * scale);
                        dx = x - (dw - slotW)/2;
                        dy = y - (dh - slotH)/2;
                    }
                    if (bi != null) g2.drawImage(bi, dx, dy, dw, dh, null);
                }
                selectionSlots.add(new Slot(slotRect, it));
            }
        }
        
        // Check if current player has completed selection
        if (selected.size() >= 5) {
            System.out.println("Player " + (playerIndex + 1) + " completed selection!");
            
            // Move to next player
            currentPlayerIndex++;
            selected.clear();
            
            // If all 4 players done, finish starting phase
            if (currentPlayerIndex >= 4) {
                startingComplete = true;
                System.out.println("All players have made their selections!");
            }
            
            repaint();
        }

        
    }
    public void drawCard(Graphics g, BufferedImage img, int x, int y, int width, int height)
    {
        g.drawImage(img, x, y, width, height, null);
    }
    // helper to normalize token rendering with consistent padding
    private void drawToken(Graphics2D g2, BufferedImage img, int x, int y, int size) {
        if (img == null) return;
        int pad = Math.max(2, (int)(size * 0.1)); 
        int inner = size - pad * 2;
        g2.drawImage(img, x + pad, y + pad, inner, inner, null);
    }

    // Helpers to check current selections
    private boolean isSelected(Bird b) {
        for (ItemRef it : selected) {
            if (it.type == ItemRef.Type.BIRD && it.bird == b) return true;
        }
        return false;
    }
    private boolean isSelectedToken(String name) {
        for (ItemRef it : selected) {
            if (it.type == ItemRef.Type.TOKEN && it.tokenName != null && it.tokenName.equals(name)) return true;
        }
        return false;
    }

    // Build token items (centers and radii) for hit-testing
    private void ensureTokenItems() {
        if (miscpics.isEmpty()) return;
        tokenItems.clear();
        int yTop = getHeight() - 300; // top-left of tokens row
        int cy = yTop + tokenSize/2;
        tokenItems.add(new TokenItem("wheattoken", 100 + tokenSize/2, cy, tokenSize/2, miscpics.get("wheattoken")));
        tokenItems.add(new TokenItem("invertebratetoken", 250 + tokenSize/2, cy, tokenSize/2, miscpics.get("invertebratetoken")));
        tokenItems.add(new TokenItem("fishtoken", 400 + tokenSize/2, cy, tokenSize/2, miscpics.get("fishtoken")));
        tokenItems.add(new TokenItem("foodtoken", 550 + tokenSize/2, cy, tokenSize/2, miscpics.get("foodtoken")));
        tokenItems.add(new TokenItem("rattoken", 700 + tokenSize/2, cy, tokenSize/2, miscpics.get("rattoken")));
    }

    // Simple item reference for selection box
    private static class ItemRef {
        enum Type { BIRD, TOKEN }
        final Type type;
        final Bird bird;
        final String tokenName;
        private ItemRef(Type t, Bird b, String tn) { this.type = t; this.bird = b; this.tokenName = tn; }
        static ItemRef token(String name) { return new ItemRef(Type.TOKEN, null, name); }
        static ItemRef bird(Bird b) { return new ItemRef(Type.BIRD, b, null); }
    }

    // Slot bounds for click-removal
    private static class Slot {
        final Rectangle bounds;
        final ItemRef item;
        Slot(Rectangle b, ItemRef i) { this.bounds = b; this.item = i; }
    }

    // Token drawable + hitbox info
    private static class TokenItem {
        final String name;
        final int cx, cy, radius;
        final BufferedImage image;
        TokenItem(String n, int cx, int cy, int r, BufferedImage img) {
            this.name = n; this.cx = cx; this.cy = cy; this.radius = r; this.image = img;
        }
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
        Point p = e.getPoint();

        if(!startingComplete && namesEntered && !players.isEmpty()){
            // Click inside selection box slots -> remove item
            for (int i = 0; i < selectionSlots.size(); i++) {
                Slot s = selectionSlots.get(i);
                if (s.bounds.contains(p)) {
                    selected.remove(s.item);
                    System.out.println("Removed from box: " + (s.item.type==ItemRef.Type.TOKEN ? s.item.tokenName : s.item.bird.getName()) + ". Now " + selected.size() + "/5");
                    repaint();
                    return;
                }
            }

            // Token circular hit-test (distance)
            for (TokenItem t : tokenItems) {
                if (isSelectedToken(t.name)) continue; // already kept
                int dx = p.x - t.cx;
                int dy = p.y - t.cy;
                if (dx*dx + dy*dy <= t.radius*t.radius) {
                    if (selected.size() < 5) {
                        selected.add(ItemRef.token(t.name));
                        System.out.println("Added token: " + t.name + ". Now " + selected.size() + "/5");
                    }
                    repaint();
                    return;
                }
            }

            // Bird rectangle hit-test - MODIFIED to use currentPlayerIndex
            ArrayList<Bird> hand = players.get(currentPlayerIndex).playerGetHand();
            for (Bird b : hand) {
                if (isSelected(b)) continue; // already kept
                Rectangle r = b.getBounds();
                if (r != null && r.contains(p)) {
                    if (selected.size() < 5) {
                        selected.add(ItemRef.bird(b));
                        System.out.println("Added bird: " + b.getName() + ". Now " + selected.size() + "/5");
                    }
                    repaint();
                    return;
                }
            }
        }

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
            Point p = e.getPoint();
            // Hover over selection box items
            ItemRef prevSlot = hoverSlotItem;
            hoverSlotItem = null;
            for (Slot s : selectionSlots) {
                if (s.bounds.contains(p)) { hoverSlotItem = s.item; break; }
            }

            // Hover over tokens
            String prevToken = hoverTokenName;
            hoverTokenName = null;
            ensureTokenItems();
            for (TokenItem t : tokenItems) {
                if (isSelectedToken(t.name)) continue;
                int dx = p.x - t.cx;
                int dy = p.y - t.cy;
                if (dx*dx + dy*dy <= t.radius*t.radius) { hoverTokenName = t.name; break; }
            }

            // Hover over birds - MODIFIED to use currentPlayerIndex
            Bird prevBird = hoverBird;
            hoverBird = null;
            ArrayList<Bird> hand = players.get(currentPlayerIndex).playerGetHand();
            for (Bird b : hand) {
                if (isSelected(b)) continue;
                Rectangle r = b.getBounds();
                if (r != null && r.contains(p)) { hoverBird = b; break; }
            }

            if (prevSlot != hoverSlotItem || prevToken != hoverTokenName || prevBird != hoverBird) {
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