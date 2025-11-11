import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Button {
    protected int x1;
    protected int y1;
    protected int x2;
    protected int y2;
    protected int width;
    protected int height;
    protected static ArrayList<Button> all = new ArrayList<Button>();
    protected boolean clickable, display;
    protected BufferedImage image;
    protected String name = "NONE";
    protected String state = "Normal";
    
    // Additional fields for special button types
    protected Bird bird;              // For bird buttons
    protected String tokenName;       // For token buttons
    protected int centerX, centerY, radius;  // For circular token hit detection
    protected boolean isHovered;      // Hover state
    protected boolean isSelected;     // Selection state
    protected BufferedImage secondaryImage;  // For tokens in selection slots

    //Constructor    name    state       image        clickable   display      x       y        x+width   y+height
    public Button(String n, String s, BufferedImage i, boolean c, boolean d, int xOne, int yOne, int xTwo, int yTwo) {
        name = n;
        state = s;
        image = i;
        clickable = c;
        display = d;

        // Scale only position; keep width/height as absolute pixel sizes
        x1 = x(xOne);
        y1 = y(yOne);
        width = (xTwo - xOne);
        height = (yTwo - yOne);
        x2 = x1 + width;
        y2 = y1 + height;
        all.add(this);
        
        // Initialize special fields
        this.bird = null;
        this.tokenName = null;
        this.isHovered = false;
        this.isSelected = false;
        this.centerX = xOne + (xTwo - xOne) / 2;
        this.centerY = yOne + (yTwo - yOne) / 2;
        this.radius = Math.min(xTwo - xOne, yTwo - yOne) / 2;
        this.secondaryImage = null;
    }

    /*
     * THESE METHODS ARE OVERRIDDEN IN CHILD CLASSES
     * ONLY GENERIC BUTTON CODE SHOULD BE WRITTEN HERE
     */

    //Each button knows how to draw itself based on the "name" and "state" Strings
    public void paint(Graphics g) 
    {
        if (!display) return;
        
        Graphics2D g2 = (Graphics2D) g;
        
        // Handle bird buttons
        if (name.startsWith("bird_") && bird != null && bird.getImage() != null) {
            // Don't render if selected (it's in the selection box)
            if (isSelected) return;
            
            int drawW = width;
            int drawH = height;
            int drawX = x1;
            int drawY = y1;
            if (isHovered) {
                double scale = 1.12;
                drawW = (int)Math.round(width * scale);
                drawH = (int)Math.round(height * scale);
                drawX = x1 - (drawW - width) / 2;
                drawY = y1 - (drawH - height) / 2;
            }
            g2.drawImage(bird.getImage(), drawX, drawY, drawW, drawH, null);
            return;
        }
        
        // Handle token buttons (circular)
        if (name.startsWith("token_") && image != null) {
            // Don't render if selected (it's in the selection box)
            if (isSelected) return;
            
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int size = radius * 2;
            int x = centerX - size / 2;
            int y = centerY - size / 2;
            if (isHovered) {
                double scale = 1.18;
                int newSize = (int)Math.round(size * scale);
                x = centerX - newSize / 2;
                y = centerY - newSize / 2;
                size = newSize;
            }
            int pad = Math.max(2, (int)(size * 0.1));
            int inner = size - pad * 2;
            g2.drawImage(image, x + pad, y + pad, inner, inner, null);
            return;
        }
        
        // Handle selection slot buttons
        if (name.startsWith("slot_")) {
            // Draw slot border - slightly visible even when empty
            g2.setColor(new Color(100, 100, 100, 80));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRect(x1, y1, width, height);
            
            if (state.equals("has_token")) {
                BufferedImage tokenImg = (secondaryImage != null) ? secondaryImage : image;
                if (tokenImg != null) {
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    int ts = Math.min(width, height) - 20;
                    if (isHovered) ts = (int)Math.round(ts * 1.15);
                    int pad = Math.max(2, (int)(ts * 0.1));
                    int inner = ts - pad * 2;
                    int cx = x1 + (width - ts) / 2;
                    int cy = y1 + (height - ts) / 2;
                    g2.drawImage(tokenImg, cx + pad, cy + pad, inner, inner, null);
                }
            } else if (state.equals("has_bird")) {
                BufferedImage birdImg = (bird != null && bird.getImage() != null) ? bird.getImage() : image;
                if (birdImg != null) {
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    int dw = width - 4;
                    int dh = height - 4;
                    int dx = x1 + 2;
                    int dy = y1 + 2;
                    if (isHovered) {
                        double scale = 1.08;
                        dw = (int)Math.round((width - 4) * scale);
                        dh = (int)Math.round((height - 4) * scale);
                        dx = x1 + 2 - (dw - (width - 4)) / 2;
                        dy = y1 + 2 - (dh - (height - 4)) / 2;
                    }
                    g2.drawImage(birdImg, dx, dy, dw, dh, null);
                }
            }
            return;
        }
        
        // Default rendering
        if (image != null) {
            g.drawImage(image, x1, y1, getWidth(), getHeight(), null);
        } else {
            if(state.equals("normal"))
                g.setColor(Color.LIGHT_GRAY);
            else if(state.equals("abnormal"))
                g.setColor(Color.blue);
            g.fillRect(x1, y1, width, height);
            g.setColor(Color.BLACK);
            g.drawString(name, x1 + x(4), y1 + y(14));
        }
    }

    
    public void click() {
        if (clickable) 
        {//only respond to clicks if clickable is true
            System.out.println("Button " + name + " clicked.");
        switch (name) 
        {
            //Write specific click behavior for different button names here
            default:
                if(state.equals("normal"))
                state="abnormal";
                else if(state.equals("abnormal"))
                state="normal";
            break;
        }

        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    //Move the button to a new x,y position (top-left corner)
    public void move(int x, int y) {
        x1 = x(x);
        y1 = y(y);
        x2 = x1 + this.getWidth();
        y2 = y1 + this.getHeight();
    }
    
    //Check if a given x,y coordinate is within the button's bounds
    public boolean inBounds(int mx, int my) {
        // Token buttons use circular hit detection
        if (name.startsWith("token_")) {
            int dx = mx - centerX;
            int dy = my - centerY;
            return (dx * dx + dy * dy) <= (radius * radius);
        }
        // Default rectangular hit detection
        return (mx >= x1 && mx <= x2 && my >= y1 && my <= y2);
    }

    public static ArrayList<Button> getAll() {
        return all;
    }
    
    // Helper methods for special button types
    public void setBird(Bird b) {
        this.bird = b;
    }
    
    public Bird getBird() {
        return bird;
    }
    
    public void setTokenName(String tn) {
        this.tokenName = tn;
    }
    
    public String getTokenName() {
        return tokenName;
    }
    
    public void setHovered(boolean h) {
        this.isHovered = h;
    }
    
    public boolean isHovered() {
        return isHovered;
    }
    
    public void setSelected(boolean s) {
        this.isSelected = s;
    }
    
    public boolean isSelected() {
        return isSelected;
    }
    
    public void setSecondaryImage(BufferedImage img) {
        this.secondaryImage = img;
    }
    
    public boolean isEmpty() {
        return state.equals("empty");
    }
    
    public boolean isToken() {
        return state.equals("has_token");
    }
    
    public boolean isBird() {
        return state.equals("has_bird");
    }
    
    public void clearSlot() {
        this.bird = null;
        this.tokenName = null;
        this.secondaryImage = null;
        this.state = "empty";
    }



    public void setImage(BufferedImage i) {
        image = i;
    }

    public void setClickable(boolean c) {
        clickable = c;
    }

    public void setDisplay(boolean d) {
        display = d;
    }

    public void setName(String n) {
        name = n;
    }

    public String getName() {
        return name;
    }
    // sets if the button is clickable
    public boolean getClickable() {
        return clickable;
    }
    // sets if the button is displayable
    public boolean getDisplay() {
        return display;
    }

    public String getState() {
        return state;
    }

    public void setState(String s) {
        state = s;
    }

    /* Input: in - the x or y value to be scaled (0-1000)
	 * Output: the scaled value based on the current window size
     * Only scale the x and y values in the Button class
	 */
    public static int x(int in)
	{
			return in * Frame.getPanel().getWidth() / 1000;
	}
	public static int y(int in)
	{
			return in * Frame.getPanel().getHeight() / 1000;
		
	}
}

