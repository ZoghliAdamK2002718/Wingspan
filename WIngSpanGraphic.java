import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
public class WingSpanGraphic extends JPanel implements MouseListener
{
    BufferedImage left,right;
public WingSpanGraphic()
{
try {
            // Load from a file
            left = ImageIO.read(WingSpanGraphic.class.getResource("/Images/Arrow_Left.png"));
            right = ImageIO.read(WingSpanGraphic.class.getResource("/Images/Arrow_Right.png"));
            System.out.println("Image loaded successfully!");
        } catch (IOException e) {
            System.err.println("Error loading image: " + e.getMessage());
        }
              addMouseListener(this);  
    }
public void paint(Graphics g)
{
super.paint(g);
g.drawImage(left,0,200,50,50,null);
g.drawImage(right,1120,200,50,50,null);
g.setColor(Color.BLUE);
g.drawLine(0, 0, getWidth(), getHeight());
}
public void mousePressed(MouseEvent e){}
public void mouseReleased(MouseEvent e){}
public void mouseExited(MouseEvent e){}
public void mouseEntered(MouseEvent e){}
public void mouseClicked(MouseEvent e){
    int x=e.getX();
    int y= e.getY();
    System.out.println("loc is ("+x+"'"+y+")");
    if(e.getButton()==e.BUTTON1){
        if(x>=1120&&y>=200&&x<=1180&&y<250){
            System.out.println("Hi :D");
        }
        repaint();
    }
}
}




