import java.awt.Color;
import java.awt.Graphics;

public class Spot extends Button{
private Bird bird;
private String habitat;
private int index;
private boolean occupied, hasActionToken;
private Ability spotAbility;

    public Spot(String a, int i, Ability sa) {
        super("Spot "+a+" "+i, "normal", null, false, true, 0, 0, 0, 0);
        habitat = a;
        index = i;
        spotAbility = sa;
        occupied = false;
        hasActionToken = false;
        bird = null;
        
        // Set coordinates based on habitat and index
        setSpotCoordinates();
    }
    
    private void setSpotCoordinates() {
        // Board starts at (25, 25) and is (getWidth() - 600) x (getHeight() - 250)
        // Base dimensions: approximately 1000 x 710 for board
        // Each row has 5 spots, positioned horizontally
        
        int boardStartX = 25;
        int boardStartY = 25;
        
        // Spot dimensions - matching card size from Panel.java
        int spotWidth = 140;  // Width of each card spot (same as cardW)
        int spotHeight = 278; // Height of each card spot (same as cardH)
        
        // Row positions (Y coordinates) - adjusted for taller cards
        int forestRowY = boardStartY + 50;      // Top row
        int grasslandRowY = boardStartY + 360;  // Middle row
        int wetlandRowY = boardStartY + 670;    // Bottom row
        
        // Column positions (X coordinates) - spots are horizontally aligned
        // Starting X for first spot, with spacing between spots
        int startX = boardStartX + 250;  // After the habitat icons
        int spacing = 180;  // Wider space between spots to fit board
        
        // Calculate position based on habitat and index (0-4)
        int yPos = 0;
        switch(habitat.toLowerCase()) {
            case "forest":
                yPos = forestRowY;
                break;
            case "grassland":
                yPos = grasslandRowY;
                break;
            case "wetland":
                yPos = wetlandRowY;
                break;
            default:
                yPos = boardStartY;
        }
        
        int xPos = startX + (index * spacing);
        
        // Set the button coordinates
        x1 = xPos;
        y1 = yPos;
        x2 = xPos + spotWidth;
        y2 = yPos + spotHeight;
        width = spotWidth;
        height = spotHeight;
    }

    public void paint(Graphics g)
    {
        if(!super.display) return;
        
        // Draw the spot rectangle with a colored outline based on habitat
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
        
        // Draw filled background (semi-transparent white)
        g.setColor(new Color(255, 255, 255, 50));
        g.fillRect(x1, y1, width, height);
        
        // Draw thick colored outline
        g.setColor(outlineColor);
        for(int i = 0; i < 3; i++) {
            g.drawRect(x1 + i, y1 + i, width - 2*i, height - 2*i);
        }
        
        // Draw spot number
        g.setColor(Color.BLACK);
        g.drawString(habitat.substring(0, 1).toUpperCase() + (index + 1), x1 + 5, y1 + 15);
        
        // If occupied, draw the bird
        if(occupied && bird != null) {
            // TODO: Draw bird card here
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
}