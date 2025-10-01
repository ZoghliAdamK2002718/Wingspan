import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
public class WIngSpanGraphic extends JPanel
{
public WIngSpanGraphic()
{

}
public void paint(Graphics g)
{
super.paint(g);
g.setColor(Color.BLUE);
g.drawLine(0, 0, getWidth(), getHeight());
}
}




