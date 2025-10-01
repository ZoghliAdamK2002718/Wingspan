import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.image.*;
import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
public class WIngSpanGraphic extends JPanel
{
public WIngSpanGraphic()
{
    private BufferedImage c1;
try {
    c1=ImageIO.read(new File("AbbottsBooty"));
} catch (Exception e) {
}
}
public void paint(Graphics g)
{
super.paint(g);
g.setColor(Color.BLUE);
g.drawLine(0, 0, getWidth(), getHeight());
}
}




