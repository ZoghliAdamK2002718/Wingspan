import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class TokenButton extends Button {
    private String tokenName;
    private int centerX;
    private int centerY;
    private int radius;
    private boolean isSelected;
    private boolean isHovered;
    
    public TokenButton(String tokenName, BufferedImage img, int centerX, int centerY, int size) {
        super("token_" + tokenName, "normal", img, true, true, 
              centerX - size/2, centerY - size/2, centerX + size/2, centerY + size/2);
        this.tokenName = tokenName;
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = size / 2;
        this.isSelected = false;
        this.isHovered = false;
    }
    
    public String getTokenName() {
        return tokenName;
    }
    
    public void setSelected(boolean selected) {
        this.isSelected = selected;
        this.display = !selected; // Hide when selected
    }
    
    public boolean isSelected() {
        return isSelected;
    }
    
    public void setHovered(boolean hovered) {
        this.isHovered = hovered;
    }
    
    public boolean isHovered() {
        return isHovered;
    }
    
    // Override inBounds to use circular hit detection
    public boolean inBounds(int mx, int my) {
        int dx = mx - centerX;
        int dy = my - centerY;
        return (dx * dx + dy * dy) <= (radius * radius);
    }
    
    public void paint(Graphics g) {
        if (display && image != null) {
            Graphics2D g2 = (Graphics2D)g;
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
            
            // Add padding like in original drawToken
            int pad = Math.max(2, (int)(size * 0.1));
            int inner = size - pad * 2;
            g2.drawImage((Image)image, x + pad, y + pad, inner, inner, null);
        }
    }
    
    public void click() {
        if (clickable && !isSelected) {
            System.out.println("TokenButton clicked: " + tokenName);
            // Panel will handle adding to selection
        }
    }
}
