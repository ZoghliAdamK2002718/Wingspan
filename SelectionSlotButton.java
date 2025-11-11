import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class SelectionSlotButton extends Button {
    private Bird bird;
    private String tokenName;
    private BufferedImage tokenImage;
    private boolean isEmpty;
    private boolean isHovered;
    private int slotIndex;
    
    public SelectionSlotButton(int index, int x, int y, int width, int height) {
        super("slot_" + index, "empty", null, true, true, x, y, x + width, y + height);
        this.slotIndex = index;
        this.isEmpty = true;
        this.isHovered = false;
    }
    
    public void setContent(Bird b) {
        this.bird = b;
        this.tokenName = null;
        this.tokenImage = null;
        this.image = b.getImage();
        this.isEmpty = false;
        this.name = "slot_bird_" + slotIndex;
    }
    
    public void setContent(String tokenName, BufferedImage tokenImg) {
        this.bird = null;
        this.tokenName = tokenName;
        this.tokenImage = tokenImg;
        this.image = tokenImg;
        this.isEmpty = false;
        this.name = "slot_token_" + slotIndex;
    }
    
    public void clear() {
        this.bird = null;
        this.tokenName = null;
        this.tokenImage = null;
        this.image = null;
        this.isEmpty = true;
        this.name = "slot_" + slotIndex;
    }
    
    public boolean isEmpty() {
        return isEmpty;
    }
    
    public Bird getBird() {
        return bird;
    }
    
    public String getTokenName() {
        return tokenName;
    }
    
    public void setHovered(boolean hovered) {
        this.isHovered = hovered;
    }
    
    public boolean isHovered() {
        return isHovered;
    }
    
    public boolean isToken() {
        return !isEmpty && tokenName != null;
    }
    
    public boolean isBird() {
        return !isEmpty && bird != null;
    }
    
    public void paint(Graphics g) {
        if (display) {
            Graphics2D g2 = (Graphics2D)g;
            
            // Draw slot border
            g2.setColor(new Color(0, 0, 0, 40));
            g2.drawRect(x1, y1, width, height);
            
            if (!isEmpty) {
                if (isToken() && tokenImage != null) {
                    // Draw token centered in slot
                    int ts = Math.min(width, height) - 10;
                    if (isHovered) {
                        ts = (int)Math.round(ts * 1.15);
                    }
                    int pad = Math.max(2, (int)(ts * 0.1));
                    int inner = ts - pad * 2;
                    int centerX = x1 + (width - ts) / 2;
                    int centerY = y1 + (height - ts) / 2;
                    g2.drawImage((Image)tokenImage, centerX + pad, centerY + pad, inner, inner, null);
                } else if (isBird() && bird != null && bird.getImage() != null) {
                    // Draw bird card
                    int dw = width;
                    int dh = height;
                    int dx = x1;
                    int dy = y1;
                    if (isHovered) {
                        double scale = 1.12;
                        dw = (int)Math.round(width * scale);
                        dh = (int)Math.round(height * scale);
                        dx = x1 - (dw - width) / 2;
                        dy = y1 - (dh - height) / 2;
                    }
                    g2.drawImage((Image)bird.getImage(), dx, dy, dw, dh, null);
                }
            }
        }
    }
    
    public void click() {
        if (clickable && !isEmpty) {
            System.out.println("SelectionSlotButton clicked: removing " + 
                (isBird() ? bird.getName() : tokenName));
            // Panel will handle removal from selection
        }
    }
}
