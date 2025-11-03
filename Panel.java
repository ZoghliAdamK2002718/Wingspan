import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import javax.swing.*;

public class Panel extends JPanel implements MouseListener{
ArrayList<Button> currentScreen = new ArrayList<Button>();
	
public Panel()
{
	setSize(1600,960);
	addMouseListener(this);

}

	
@Override
	public void paint(Graphics g)
{
	super.paint(g);
	for(int i=0;i<currentScreen.size();i++)
	{
		currentScreen.get(i).paint(g);
	}
	
}



	public void mouseClicked(MouseEvent e) {
		
		for(int i=0;i<currentScreen.size();i++)
		{
			
			if(currentScreen.get(i).inBounds(e.getX(), e.getY()))
			{
				currentScreen.get(i).click();
			}
		}
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
			
	

	public void addNotify() {
	    super.addNotify();
	    requestFocus();
	//I reccomend making buttons here
	Button sample = new Button("default","normal",null,true,true,0,0,500,500);
	currentScreen.add(sample);
	    repaint();
	}
}




