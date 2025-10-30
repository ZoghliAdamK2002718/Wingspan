import javax.swing.JFrame;
public class Frame extends JFrame{
private static final int WIDTH = 1200;
private static final int HEIGHT = 480;
private static Panel panel = new Panel();

public Frame(String framename)
{
super(framename);
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setSize(WIDTH,HEIGHT);
add(panel);
setVisible(true);
}
//used for the scaling methods in Button.java
public static Panel getPanel()
{
    return panel;
}

}

