import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.*;

public class Bird extends Button {
  private String name, sciName, nestType;
private String[] habitats;
private Ability birdAbility;
private TreeMap<String,Integer> costs;
private TreeMap<String,Integer> cache;
private int points, eggCount, eggCapacity, wingspan;
private ArrayList<Bird> prey;
private boolean bonusCard, flocking;
private  Queue<Bird> deck = new LinkedList<>();
private Spot loc;
private BufferedImage image;
private int cardH = 278;
private int cardW = 180;
private Rectangle bounds;

  
  public Bird(String n, String sN, String nT, String[] h, Ability bA, TreeMap<String, Integer> co, TreeMap<String, Integer> ca, int po, int eC, int eCa, int s, ArrayList<Bird> p, boolean bc, boolean flo, Spot l, BufferedImage i, int x, int y)
  {

    super(n, "Normal", i, true, true, x, y, x + 180, y + 278);  // Using cardW and cardH for dimensions
    name = n;
    sciName = sN;
    nestType = nT;
    habitats = h;
    birdAbility = bA;
    costs = co;
    cache = ca;
    points = po; eggCount = eC; eggCapacity = eCa; wingspan = s;
    prey = p;
    bonusCard = bc; flocking = flo;
    loc = l;
    image = i;
    bounds = new Rectangle(x, y, 180, 278);
  }
  
  public Rectangle getBounds()
  {
    return bounds;
  }
  public void setX(int x)
  {
    bounds = this.getBounds();
    bounds.x = x;
    this.setBounds(bounds);
  }
  public void setY(int y)
  {
    bounds = this.getBounds();
    bounds.y = y;
    this.setBounds(bounds);
  }
  public void setBounds(Rectangle r)
  {
    if (r != null) this.bounds = new Rectangle(r);
  }
  public void paint(Graphics g) {
        if (image != null && bounds != null) {
            g.drawImage(image, bounds.x, bounds.y, bounds.width, bounds.height, null);
        } 

        //if an object is a bird, draw it here
        switch (name) {
        
          default:
            break;
        }
    }

  public void click()
  {
    //define what happens when a bird card is clicked
    switch (name) {
        
          default:
            break;
        }
  }
  public String getName()
  {
    return name;
  }
  public String getSciName()
  {
    return sciName;
  }
  public String getNestType()
  {
    return nestType;
  }
 public Bird drawDeck(){
    return deck.poll();
  }

  public String[] getHabitats()
  {
    return habitats;
  }
  public Ability getAbility()
  {
    return birdAbility;
  }
  public TreeMap<String, Integer> getCosts()
  {
    return costs;
  }
  public TreeMap<String, Integer> getCache()
  {
    return cache;
  }
  public int getPoints()
  {
    return points;
  }
  public int getEggCount()
  {
    return eggCount;
  }
  public int getEggCapacity()
  {
    return eggCapacity;
  }
  public int getWingspan()
  {
    return wingspan;
  }
  public ArrayList<Bird> getPrey()
  {
    return prey;
  }
  public boolean hasBonusCard()
  {
    return bonusCard;
  }
  public boolean isFlocking()
  {
    return flocking;
  }
  public Spot getLocation()
  {
    return loc;
  }
  public String getsN()
  {
    return sciName;
  }
  public String getnT()
  {
    return nestType;
  }
  public String[] getHabitat()
  {
    return habitats;
  }
  public Ability getbirdAbility()
  {
    return birdAbility;
  }
  public TreeMap<String,Integer> getcost()
  {
    return costs;
  }
  public BufferedImage getImage()
  {
    return image;
  }
}
