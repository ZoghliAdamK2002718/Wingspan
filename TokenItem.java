import java.awt.image.BufferedImage;

class TokenItem {
    final String name;
    final int cx, cy, radius;
    final BufferedImage image;
    TokenItem(String n, int cx, int cy, int r, BufferedImage img) {
        this.name = n; this.cx = cx; this.cy = cy; this.radius = r; this.image = img;
    }
}
