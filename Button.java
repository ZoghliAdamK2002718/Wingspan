import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Button {
    protected int x1;
    protected int y1;
    protected int x2;
    protected int y2;
    protected int width;
    protected int height;
    protected static ArrayList<Button> all = new ArrayList<Button>();
    protected boolean clickable, display;
    protected BufferedImage image;
    protected String name = "NONE";
    protected String state = "Normal";

    //Constructor    name    state       image        clickable   display      x       y        x+width   y+height
    public Button(String n, String s, BufferedImage i, boolean c, boolean d, int xOne, int yOne, int xTwo, int yTwo) {
        name = n;
        state = s;
        image = i;
        clickable = c;
        display = d;
        // Scale positions to actual pixels
        x1 = x(xOne);
        y1 = y(yOne);
        x2 = x(xTwo);
        y2 = y(yTwo);
        width = x2 - x1;
        height = y2 - y1;
        all.add(this);
    }

    /*
     * THESE METHODS ARE OVERRIDDEN IN CHILD CLASSES
     * ONLY GENERIC BUTTON CODE SHOULD BE WRITTEN HERE
     */

    //Each button knows how to draw itself based on the "name" and "state" Strings
    public void paint(Graphics g) 
    {
        if(!display) return;
        switch(name)
        {
            case "temp":
            g.drawString(name,(x1+x2)/2,(y1+y2)/2);
            break;
            case "GO":
            g.setColor(Color.BLACK);
            g.drawRect(x1, y1, width, height);
            g.setColor(Color.YELLOW);
            g.drawString("GO",x1,y1);
            break;
            case "Previous Player":
                g.setColor(Color.BLACK);
                g.drawString("Previous Player",x1,y1);
                g.setColor(Color.yellow);
                g.drawRect(x1, y1, width, height);
            break;
            case "Next Player":
                g.setColor(Color.BLACK);
                g.drawString("Next Player",x1,y1);
                g.setColor(Color.yellow);
                g.drawRect(x1, y1, width, height);
            break;
            case "Birdfeeder and Deck":
                g.setColor(Color.BLACK);
                g.drawString("Birdfeeder and Deck",x1,y1);
                g.setColor(Color.yellow);
                g.drawRect(x1, y1, width, height);
            break;
            case "Action Token":
                g.setColor(Color.BLUE);
                g.drawRect(x1, y1, width, height);
            break;
        }
        
    }

    
    public void click() {
        if (!clickable) return; 
        //only respond to clicks if clickable is true
            System.out.println("Button " + name + " clicked.");
        switch (name) 
        {
            //Write specific click behavior for different button names here
            case "Previous Player":
            int index = (Player.currentPlayerIndex-1);
            if(index<0) index = 3; //gets the index of the previous player. When it is not the Player's turn, the display flag for hand and other things will be turned off
            Panel.setScreen(Player.players().get(index).playerGetScreenDisplay());//display that Player's screen
            break;//The issue with the code is that it uses current player instead of the player that owns the button

            case "Next Player":
            Panel.setScreen(Player.players().get((Player.currentPlayerIndex+1)%4).playerGetScreenDisplay());//display the next person's screen
            break;

            case "Birdfeeder and Deck":
            Panel.setScreen(Panel.miscellaneousScreen);//display the miscelanious screen
            break;

            

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
        y1 = y(y);
        x2 = x1 + this.getWidth();
        y2 = y1 + this.getHeight();
    }
    
    //Check if a given x,y coordinate is within the button's bounds
    public boolean inBounds(int x, int y) {
        return (x >= x1 && x <= x2 && y >= y1 && y <= y2);
    }

    public static ArrayList<Button> getAll() {
        return all;
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
     * Only scale the x and y values in the Button class
	 */
    public static int x(int in)
	{
			return in * Frame.getPanel().getWidth() / 1000;
	}
	public static int y(int in)
	{
			return in * Frame.getPanel().getHeight() / 1000;
		
	}
}
