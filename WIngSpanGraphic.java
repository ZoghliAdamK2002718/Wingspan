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
        int screen=1;
        int turn=1;
    BufferedImage left,right,P1,P2,Crown,Board;
public WingSpanGraphic()
{
try {
            // Load from a file
            left = ImageIO.read(WingSpanGraphic.class.getResource("/Images/Arrow_Left.png"));
            right = ImageIO.read(WingSpanGraphic.class.getResource("/Images/Arrow_Right.png"));
            P1=ImageIO.read(WingSpanGraphic.class.getResource("/Images/P1.png"));
            P2=ImageIO.read(WingSpanGraphic.class.getResource("/Images/P2.png"));
            Crown=ImageIO.read(WingSpanGraphic.class.getResource("/Images/Crown.png"));
            Board=ImageIO.read(WingSpanGraphic.class.getResource("/Images/Board.png"));
            System.out.println("Image loaded successfully!");
        } catch (IOException e) {
            System.err.println("Error loading image: " + e.getMessage());
        }
              addMouseListener(this);  
    }
public void paint(Graphics g)
{
super.paint(g);
if(screen==1){
    if(turn==1){
g.drawImage(Crown,0,340,30,40,null);
    }
g.drawImage(P1,0,400,50,50,null);
}
else if(screen==2){
    if(turn==2){
g.drawImage(Crown,0,340,30,40,null);
    }
g.drawImage(P2,0,400,50,50,null);
}
else if(screen==3){
}
if(screen<3){
    g.drawString("actions:", 50, 100);
    g.drawImage(Board,150,50,850,300,null);
}
g.drawImage(left,0,200,50,50,null);
g.drawImage(right,1120,200,50,50,null);
g.setColor(Color.BLUE);
}
public void mousePressed(MouseEvent e){
}
public void mouseReleased(MouseEvent e){
}
public void mouseExited(MouseEvent e){}
public void mouseEntered(MouseEvent e){
}
public void mouseClicked(MouseEvent e){
    int x=e.getX();
    int y= e.getY();
    System.out.println("loc is ("+x+"'"+y+")");
    if(e.getButton()==e.BUTTON1){
        if(x>=1120&&y>=200&&x<=1180&&y<250){
         screen=screen+1;
        }
        if(x>=0&&y>=200&&x<=50&&y<250){
            screen=screen-1;
        }
    }
    if(screen>=4){
        screen=1;
    }
    else if(screen==0){
        screen=3;
    }
    System.out.println(screen);
            repaint();
}
}




