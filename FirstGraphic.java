import javax.swing.JFrame;
public class FirstGraphic extends JFrame{
private static final int WIDTH = 1200;
private static final int HEIGHT = 480;
public FirstGraphic(String framename)
{
super(framename);
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setSize(WIDTH,HEIGHT);
add(new WingSpanGraphic());
setVisible(true);
}
}
