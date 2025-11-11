import java.awt.Graphics;
import java.awt.Image;

public class BirdButton extends Button {
    private Bird bird;
    private boolean isSelected;
    private boolean isHovered;
    
    public BirdButton(Bird b, int x, int y, int width, int height) {
        super(b.getName(), "normal", b.getImage(), true, true, x, y, x + width, y + height);
        this.bird = b;
        this.isSelected = false;
        this.isHovered = false;
    }
    
    public Bird getBird() {
        return bird;
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
    
    public void paint(Graphics g) {
        if (display && image != null) {
            int drawW = width;
            int drawH = height;
            int drawX = x1;
            int drawY = y1;
            
            // Scale up when hovered
            if (isHovered) {
                double scale = 1.12;
                drawW = (int)Math.round(width * scale);
                drawH = (int)Math.round(height * scale);
                drawX = x1 - (drawW - width) / 2;
                drawY = y1 - (drawH - height) / 2;
            }
            
            g.drawImage((Image)image, drawX, drawY, drawW, drawH, null);
        }
    }
    
    public void click() {
        if (clickable && !isSelected) {
            System.out.println("BirdButton clicked: " + bird.getName());
            // Panel will handle adding to selection
        }
    }
}
