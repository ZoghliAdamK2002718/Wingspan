import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Button {
    private int x1, y1, x2, y2, width, height;
    private static ArrayList<Button> all = new ArrayList<Button>();
    private boolean clickable, display;
    private BufferedImage image;
    private String name = "NONE";
    public String state = "Normal";

    //Constructor    name    state       image        clickable   display      x1      y1        x2        y2
    public Button(String n, String s,BufferedImage i, boolean c, boolean d, int xOne, int yOne, int xTwo, int yTwo) {
        name = n;
        state = s;
        image = i;
        clickable = c;
        display = d;
        x1 = xOne;
        x2 = xTwo;
        y1 = yOne;
        y2 = yTwo;
        width = x2 - x1;
        height = y2 - y1;
         all.add(this);
    }

    //Each button knows how to draw itself based on the "name" and "state" Strings
    public void paint(Graphics g) 
    {
        if (display) {//This draws the button only if display is true
            if (image != null) {
                g.drawImage(image, x1, y1, getWidth(), getHeight(), null);
            }
            switch (name) {//Write specific drawing code for different button names here 
                default:
                    if(state.equals("normal"))
                    g.setColor(Color.LIGHT_GRAY);
                    else if(state.equals("abnormal"))
                    g.setColor(Color.blue);
                    g.fillRect(x(x1), y(y1), x(width), y(height));
                    g.setColor(Color.BLACK);
                    g.drawString(name, x(x1) + x(4), y(y1) + y(14));
                break;
                
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    //Move the button to a new x,y position (top-left corner)
    public void move(int x, int y) {
        x1 = x(x);
        x2 = x1 + x(this.getWidth());
        y1 = y(y);
        y2 = y1 + y(this.getHeight());
    }
    
    //Check if a given x,y coordinate is within the button's bounds
    public boolean inBounds(int x, int y) {
        return (x(x) >= x(x1) && x(x) <= x(x2) && y(y) >= y(y1) && y(y) <= y(y2));
    }

    public static ArrayList<Button> getAll() {
        return all;
    }


    public void click() {
        if (clickable) 
        {//only respond to clicks if clickable is true
            System.out.println("Button " + name + " clicked.");
        switch (name) 
        {
            //Write specific click behavior for different button names here
            default:
                if(state.equals("normal"))
                state="abnormal";
                else if(state.equals("abnormal"))
                state="normal";
            break;
        }

        }
    }

    public void setImage(BufferedImage i) {
        image = i;
    }

    public void setClickable(boolean c) {
        clickable = c;
    }

    public void setDisplay(boolean d) {
        display = d;
    }

    public void setName(String n) {
        name = n;
    }

    public String getName() {
        return name;
    }
    // sets if the button is clickable
    public boolean getClickable() {
        return clickable;
    }
    // sets if the button is displayable
    public boolean getDisplay() {
        return display;
    }

    public String getState() {
        return state;
    }

    public void setState(String s) {
        state = s;
    }

    /* Input: in - the x or y value to be scaled (0-1000)
	 * Output: the scaled value based on the current window size
	 */
    public static int x(int in)
	{
			System.out.println("Width: " + (Frame.getPanel().getWidth())*(in/1000));
			return in * Frame.getPanel().getWidth() / 1000;
	}
	public static int y(int in)
	{
			System.out.println("Width: " + (Frame.getPanel().getHeight())*(in/1000));
			return in * Frame.getPanel().getHeight() / 1000;
		
	}
}
