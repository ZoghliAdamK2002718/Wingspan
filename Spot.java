import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Spot extends Button{
private Bird bird;
private String habitat;
private int index;
private boolean occupied, hasActionToken;
private Ability spotAbility;

    public Spot(String a, int i, Ability sa) {
        super("Spot "+a+" "+i, "normal", null, true, true, 0, 0, 0, 0);
        habitat = a;
        index = i;
        spotAbility = sa;
        occupied = false;
        hasActionToken = false;
        bird = null;
        
        setSpotCoordinates();
    }
    
    private void setSpotCoordinates() {
        layoutInBoard(new Rectangle(25, 25, 1000, 710), 140, 278);
    }

    public void paint(Graphics g)
    {
        if(!super.display) return;
        
        Color outlineColor;
        switch(habitat.toLowerCase()) {
            case "forest":
                outlineColor = new Color(34, 139, 34);  // Forest green
                break;
            case "grassland":
                outlineColor = new Color(255, 215, 0);  // Gold/yellow
                break;
            case "wetland":
                outlineColor = new Color(30, 144, 255); // Dodger blue
                break;
            default:
                outlineColor = Color.BLACK;
        }
        
        g.setColor(new Color(255, 255, 255, 50));
        g.fillRect(x1, y1, width, height);
        
        g.setColor(outlineColor);
        for(int i = 0; i < 3; i++) {
            g.drawRect(x1 + i, y1 + i, width - 2*i, height - 2*i);
        }
        
        g.setColor(Color.BLACK);
        g.drawString(habitat.substring(0, 1).toUpperCase() + (index + 1), x1 + 5, y1 + 15);
        
        if(occupied && bird != null && bird.getImage() != null) {
            BufferedImage img = bird.getImage();
            int pad = Math.max(6, width / 20);
            int drawW = width - pad * 2;
            int drawH = height - pad * 2;
            g.drawImage(img, x1 + pad, y1 + pad, drawW, drawH, null);
        }
    }

    public void click() {
        if(!super.clickable) return;
    }

    public Bird getBird() {
        return bird;
    }

    public void setBird(Bird b) {
        bird = b;
    }

    public String getHabitat() {
        return habitat;
    }

    public int getIndex() {
        return index;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean o) {
        occupied = o;
    }

    public boolean hasActionToken() {
        return hasActionToken;
    }

    public void setActionToken(boolean h) {
        hasActionToken = h;
    }

    public Ability getSpotAbility() {
        return spotAbility;
    }

    public void layoutInBoard(Rectangle boardArea, int preferredW, int preferredH) {
        if (boardArea == null) return;

        // Cap sizes to fit within the board while keeping roughly card proportions
        int maxSpotWidth = Math.max(60, boardArea.width / 7); // leave room for margins
        int maxSpotHeight = Math.max(120, boardArea.height / 3);
        width = Math.min(preferredW, maxSpotWidth);
        height = Math.min(preferredH, maxSpotHeight);

        // Left margin to clear habitat icons (about 20-25% of the board)
        int leftMargin = (int)(boardArea.width * 0.22);
        int usableWidth = boardArea.width - leftMargin;
        int spacing = Math.max(10, (usableWidth - width * 5) / 4);
        int startX = boardArea.x + leftMargin;
        int xPos = startX + index * (width + spacing);

        // Row anchors expressed as a fraction of board height
        double anchor;
        switch (habitat.toLowerCase()) {
            case "forest":
                anchor = 0.18;
                break;
            case "grassland":
                anchor = 0.52;
                break;
            case "wetland":
                anchor = 0.86;
                break;
            default:
                anchor = 0.18;
        }
        int centerY = boardArea.y + (int)(boardArea.height * anchor);
        int yPos = centerY - height / 2;

        x1 = xPos;
        y1 = yPos;
        x2 = x1 + width;
        y2 = y1 + height;
    }
}
