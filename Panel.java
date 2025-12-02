import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;


public class Panel extends JPanel implements MouseListener, MouseMotionListener{
public static ArrayList<Button> currentScreen = new ArrayList<Button>();
BufferedImage bg;
private int cardH = 278;
private int cardW = 140;
private int tokenSize = 150;
HashMap<String, BufferedImage> miscpics = new HashMap<String, BufferedImage>();
HashMap<String, Bird> birdcards = new HashMap<String, Bird>();
//ArrayList<Player> players = new ArrayList<Player>();
ArrayList<String> playerNames = new ArrayList<String>();
//Player player1;
private boolean namesEntered = false;
//private int currentPlayerIndex = 0;
private boolean startingComplete = false;
private ArrayList<JTextField> nameFields = new ArrayList<JTextField>();
private ArrayList<ItemRef> selected = new ArrayList<ItemRef>();
private ArrayList<Slot> selectionSlots = new ArrayList<Slot>();
private ArrayList<TokenItem> tokenItems = new ArrayList<TokenItem>();
private Bird hoverBird = null;
private String hoverTokenName = null;
private ItemRef hoverSlotItem = null;
public static ArrayList<Button> miscellaneousScreen = new ArrayList<Button>();
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
        BufferedImage goButton = ImageIO.read(Panel.class.getResource("/Images/gobutton.png"));
        if(goButton != null) {
            goButton = trimTransparent(goButton);
            miscpics.put("gobutton", goButton);
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
        BufferedImage board = ImageIO.read(Panel.class.getResource("/Images/Board.png"));
        miscpics.put("board", board);
        BufferedImage goBtnImg = ImageIO.read(Panel.class.getResource("/Images/gobutton.png"));
        miscpics.put("goBtnImg", goBtnImg);
        }
    }
    catch (Exception e)
    {
        System.out.println("ERROR loading images:");
        e.printStackTrace();
    }
}
    public void realStartingScreen(Graphics g)
    {
        int panelWidth = getWidth();
        int panelHeight = getHeight();
       
        // Draw big title at top
        g.setColor(new Color(50, 50, 50));
        g.setFont(new Font("Arial", Font.BOLD, 48));
        FontMetrics fm = g.getFontMetrics();
        String title = "WINGSPAN";
        int titleWidth = fm.stringWidth(title);
        g.drawString(title, (panelWidth - titleWidth) / 2, 100);
       
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        String subtitle = "Enter Player Names";
        int subtitleWidth = g.getFontMetrics().stringWidth(subtitle);
        g.drawString(subtitle, (panelWidth - subtitleWidth) / 2, 140);
       
        // Calculate center positions - use actual pixel positions
        int fieldWidth = 400;
        int fieldHeight = 40;
        int startX = (panelWidth - fieldWidth) / 2;
        int startY = 200;
        int spacing = 60;
       
        // Draw white rectangles behind text fields
        g.setColor(Color.WHITE);
        for (int i = 0; i < 4; i++) {
            g.fillRect(startX, startY + i * spacing, fieldWidth, fieldHeight);
        }
       
        // Create text fields (only once)
        if (nameFields.isEmpty()) {
            setLayout(null);
            for (int i = 0; i < 4; i++) {
                JTextField nameField = new JTextField();
                nameField.setBounds(startX, startY + i * spacing, fieldWidth, fieldHeight);
                nameField.setFont(new Font("Arial", Font.PLAIN, 18));
                nameField.setForeground(Color.BLACK);
                nameField.setBackground(Color.WHITE);
                nameField.setOpaque(true);
                nameField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                this.add(nameField);
                nameFields.add(nameField);
            }
            this.revalidate();
        } else {
            // Update text field positions if window size changed
            for (int i = 0; i < nameFields.size(); i++) {
                nameFields.get(i).setBounds(startX, startY + i * spacing, fieldWidth, fieldHeight);
            }
        }
       
        // Draw placeholder text if fields are empty
        g.setColor(Color.GRAY);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        for (int i = 0; i < nameFields.size(); i++) {
            if (nameFields.get(i).getText().isEmpty() && !nameFields.get(i).hasFocus()) {
                g.drawString("Player " + (i + 1), startX + 10, startY + 25 + i * spacing);
            }
        }
       
        // Create GO button below text fields, centered (only once)
        if (currentScreen.isEmpty()) {
            // Calculate y position: startY=200, 4 fields with spacing=60, so last field at 200+3*60=380, ends at 420
            int buttonY = 460; // Below all text fields
            // Use 0-1000 scale, centered at 500 - shorter width (200px) and taller height (50px)
            Button goBtn = new Button("GO", "normal", miscpics.get("goBtnImg"), true, true,
                getWidth()/2 - 100, getHeight()-110, getWidth()/2 + 100, getHeight()-60);
            currentScreen.add(goBtn);
        }
       
        // Draw button
        for (Button btn : currentScreen) {
            btn.paint(g);
        }
    }
@Override
    public void paint(Graphics g)
{
    super.paint(g);
   
        if(!namesEntered) {
            // paint solid bg for name screen
            g.setColor(new Color(200, 220, 235));
            g.fillRect(0, 0, getWidth(), getHeight());
            realStartingScreen(g);
        } else if(!startingComplete) {
            // paint background for bird selection
            if (bg != null) {
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
            } else {
                g.setColor(new Color(200, 220, 235));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
            startingScreen(g, Player.currentPlayerIndex);
        } else {
            // paint background for game
            if (bg != null) {
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
                playerBoardScreen(g, Player.currentPlayerIndex);
                // setScreen(Player.players().get(0).playerGetScreenDisplay());
               /*  if(currentPlayerIndex==0){
                    setScreen(PlayerOneScreen);
                }
                if(currentPlayerIndex==1){
                    setScreen(PlayerTwoScreen)
                }
                if(currentPlayerIndex==2){
                    PlayerThreeScreen(g);  //to-do change formating
                }
                if(currentPlayerIndex==3){
                    PlayerFourScreen(g);  
                }*/
            } else {
                g.setColor(new Color(200, 220, 235));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
            for(int i=0;i<currentScreen.size();i++)
            {
                currentScreen.get(i).paint(g);
            }
        }
   
}

    public void playerBoardScreen(Graphics g, int pI)
    {
        g.drawImage(miscpics.get("board"), 25, 25, getWidth() - 600, getHeight() - 250, null);
        
        // Paint the spots on the board
        HashMap<String, ArrayList<Spot>> board = Player.getCurrentPlayerBoard();
        if(board != null) {
            // Paint Forest spots
            if(board.containsKey("Forest")) {
                for(Spot spot : board.get("Forest")) {
                    spot.paint(g);
                }
            }
            // Paint Grassland spots
            if(board.containsKey("Grassland")) {
                for(Spot spot : board.get("Grassland")) {
                    spot.paint(g);
                }
            }
            // Paint Wetland spots
            if(board.containsKey("Wetland")) {
                for(Spot spot : board.get("Wetland")) {
                    spot.paint(g);
                }
            }
        }
        
        displayPlayerHand(g, pI);
        displayPlayerFood(g, pI);
    }
    public void displayPlayerHand(Graphics g, int pI)
    {
        for(int i = 0;i<Player.players().get(pI).playerGetHand().size();i++)
        {
            Bird b = Player.players().get(pI).playerGetHand().get(i);
            g.drawImage(b.getImage(), getWidth() - (cardW + 20) * (Player.players().get(pI).playerGetHand().size() - i), getHeight() - cardH - 50, cardW, cardH, null);
        }
    }
    public void displayPlayerFood(Graphics g, int pI)
    {
        TreeMap<String, Integer> food = Player.players().get(pI).playerGetFood();
        int x = 20;
        int y = getHeight() - 200;
        int size = 60;
        int spacing = 70;
        
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Food:", x, y - 10);
        
        int index = 0;
        for (Map.Entry<String, Integer> entry : food.entrySet()) {
            String foodType = entry.getKey();
            int count = entry.getValue();
            
            // Map food type back to token image name
            String tokenName = mapFoodTypeToToken(foodType);
            BufferedImage tokenImg = miscpics.get(tokenName);
            
            if (tokenImg != null && count > 0) {
                g.drawImage(tokenImg, x + index * spacing, y, size, size, null);
                // Draw count
                g.setColor(Color.WHITE);
                g.fillOval(x + index * spacing + size - 20, y + size - 20, 20, 20);
                g.setColor(Color.BLACK);
                g.drawString("" + count, x + index * spacing + size - 15, y + size - 5);
                index++;
            }
        }
    }


    private void finishPlayerSelection() {
        // Store selected items in current player's hand
        ArrayList<Bird> keptBirds = new ArrayList<>();
        Player currentPlayer = Player.players().get(Player.currentPlayerIndex);
        for (ItemRef item : selected) {
            if (item.type == ItemRef.Type.BIRD && item.bird != null) {
                keptBirds.add(item.bird);
            } else if (item.type == ItemRef.Type.TOKEN) {
                // Map token image names to food types and add to player's food
                String foodType = mapTokenToFoodType(item.tokenName);
                currentPlayer.addFood(foodType, 1);
                String playerName = playerNames.get(Player.currentPlayerIndex);
                System.out.println(playerName + " kept token: " + item.tokenName + " (" + foodType + ")");
            }
        }
        currentPlayer.playerSetHand(keptBirds);
        String playerName = playerNames.get(Player.currentPlayerIndex);
        System.out.println(playerName + " selection complete. Kept " + keptBirds.size() + " birds.");
       
        // Move to next player
        selected.clear();
        Player.currentPlayerIndex++;
       
        if (Player.currentPlayerIndex >= 4) {
            Player.currentPlayerIndex = 0;
            startingComplete = true;
            System.out.println("players selected");
        }
    }
   
    public void startingScreen(Graphics g, int playerIndex)
    {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        String playerName = playerNames.get(playerIndex);
        g.drawString(playerName + " - Select 5 items to keep", 20, 30);
       
        ArrayList<Bird> hand = Player.players().get(playerIndex).playerGetHand();
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
        Graphics2D g2 = (Graphics2D) g;
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


    // Helper method to map token image names to food types
    private String mapTokenToFoodType(String tokenName) {
        switch(tokenName) {
            case "wheattoken": return "grain";
            case "invertebratetoken": return "invertebrate";
            case "fishtoken": return "fish";
            case "foodtoken": return "fruit";
            case "rattoken": return "rodent";
            default: return tokenName;
        }
    }
    
    // Helper method to map food types back to token image names
    private String mapFoodTypeToToken(String foodType) {
        switch(foodType) {
            case "grain": return "wheattoken";
            case "invertebrate": return "invertebratetoken";
            case "fish": return "fishtoken";
            case "fruit": return "foodtoken";
            case "rodent": return "rattoken";
            default: return foodType + "token";
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
       
        if (!namesEntered) {
            // Check if GO button clicked
            for (Button btn : currentScreen) {
                System.out.println("Checking button: " + btn.getName() + " at click (" + e.getX() + "," + e.getY() + ")");
                if (btn.inBounds(e.getX(), e.getY()) && btn.getName().equals("GO")) {
                    System.out.println("GO button clicked!");
                    // Create players with entered names
                    Player.players().clear();
                    playerNames.clear();
                    for (int i = 0; i < 4; i++) {
                        String name = nameFields.get(i).getText().trim();
                        if (name.isEmpty()) name = "Player " + (i + 1);
                        playerNames.add(name);
                     /*   Player player = new Player(new ArrayList<Bird>(),
                            new TreeMap<String,Integer>(),
                            new ArrayList<BonusCard>(),
                            new HashMap<String, ArrayList<Spot>>(),
                            new ArrayList<Button>());
                        Player.players.add(player);
                        System.out.println("Created player: " + name);*/
                    }
                    // Remove text fields and button
                    for (JTextField tf : nameFields) {
                        this.remove(tf);
                    }
                    currentScreen.clear();
                    namesEntered = true;
                    Player.currentPlayerIndex = 0;
                   
                    // Give each player the same starting hand of birds
                    Bird bird1 = null, bird2 = null, bird3 = null, bird4 = null, bird5 = null;
                    try {
                        int startX = 50;
                        int spacing = 160;
                        int startY = 60;
                        bird1 = new Bird("Acadian Flycatcher", "Empidonax virescens", "cavity", new String[]{"forest", "wetland"}, null, null, null, 0, 0, 0, 0, null, false, false, null, ImageIO.read(Panel.class.getResource("/birds/acadianflycatcher.jpg")), startX, startY);
                        bird2 = new Bird("Song Sparrow", "Melospiza melodia", "ground", new String[]{"grassland", "wetland", "plains"}, null, null, null, 0, 0, 0, 0, null, false, false, null, ImageIO.read(Panel.class.getResource("/birds/songsparrow.jpg")), startX + spacing, startY);
                        bird3 = new Bird("Mallard", "Anas platyrhynchos", "nest on ground", new String[]{"wetland"}, null, null, null, 0, 0, 0, 0, null, false, false, null, ImageIO.read(Panel.class.getResource("/birds/mallard.jpg")), startX + spacing * 2, startY);
                        bird4 = new Bird("Red-tailed Hawk", "Buteo jamaicensis", "stick", new String[]{"forest", "grassland", "plains"}, null, null, null, 0, 0, 0, 0, null, false, false, null, ImageIO.read(Panel.class.getResource("/birds/redtailedhawk.jpg")), startX + spacing * 3, startY);
                        bird5 = new Bird("Great Horned Owl", "Bubo virginianus", "stick", new String[]{"forest", "wetland", "grassland"}, null, null, null, 0, 0, 0, 0, null, false, false, null, ImageIO.read(Panel.class.getResource("/birds/greathornedowl.jpg")), startX + spacing * 4, startY);
                        for (Player player : Player.players()) {
                            player.playerSetHand(new ArrayList<>(Arrays.asList(bird1, bird2, bird3, bird4, bird5)));
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                   
                    this.revalidate();
                    repaint();
                    return;
                }
            }
            return;
        }


        if(!startingComplete){
            // Click inside selection box slots -> remove item
            for (int i = 0; i < selectionSlots.size(); i++) {
                Slot s = selectionSlots.get(i);
                if (s.bounds.contains(p)) {
                    selected.remove(s.item);
                    String playerName = playerNames.get(Player.currentPlayerIndex);
                    System.out.println(playerName + " removed: " + (s.item.type==ItemRef.Type.TOKEN ? s.item.tokenName : s.item.bird.getName()) + ". Now " + selected.size() + "/5");
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
                        String playerName = playerNames.get(Player.currentPlayerIndex);
                        System.out.println(playerName + " added token: " + t.name + ". Now " + selected.size() + "/5");
                        if (selected.size() >= 5) {
                            finishPlayerSelection();
                        }
                    }
                    repaint();
                    return;
                }
            }


            // Bird rectangle hit-test
            ArrayList<Bird> hand = Player.players().get(Player.currentPlayerIndex).playerGetHand();
            for (Bird b : hand) {
                if (isSelected(b)) continue; // already kept
                Rectangle r = b.getBounds();
                if (r != null && r.contains(p)) {
                    if (selected.size() < 5) {
                        selected.add(ItemRef.bird(b));
                        String playerName = playerNames.get(Player.currentPlayerIndex);
                        System.out.println(playerName + " added bird: " + b.getName() + ". Now " + selected.size() + "/5");
                        if (selected.size() >= 5) {
                            finishPlayerSelection();
                        }
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
        if (!startingComplete) {
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


            // Hover over birds
            Bird prevBird = hoverBird;
            hoverBird = null;
            ArrayList<Bird> hand = Player.players().get(Player.currentPlayerIndex).playerGetHand();
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
    public static void setScreen(ArrayList<Button> in) 
    {
        currentScreen = in;
    }
   
    /*  TRY TO INITIALIZE OBJECTS HERE SO THAT THERE ARE NO POINTER EXCEPTIONS
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
        System.out.println("All images loaded successfully!");
    }
    catch (Exception e)
    {
        System.out.println("ERROR loading images:");
        e.printStackTrace();
    }  
        miscellaneousScreen.add(new Button("temp","",null,false,true,0,0,1000,1000));




    // Players will be created through the name entry screen
    // Bird images will be loaded when players are created
    repaint();
    }
}
    
