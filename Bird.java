import java.util.*;

public class Bird {
  private String name, sciName, nestType;
private String[] habitats;
private Ability birdAbility;
private TreeMap<String,Integer> costs;
private TreeMap<String,Integer> cache;
private int points, eggCount, eggCapacity, size;
private ArrayList<Bird> prey;
private boolean bonusCard, flocking;
private static TreeSet<Bird> deck;
private Spot loc;
  
  public Bird(String n, String sN, String nT, String[] h, Ability bA, TreeMap<String, Integer> co, TreeMap<String, Integer> ca, int po, int eC, int eCa, int s, ArrayList<Bird> p, boolean bc, boolean flo, Spot l)
  {
    name = n;sciName = sN;nestType = nT;
    habitats = h;
    birdAbility = bA;
    costs = co;
    cache = ca;
    points = po; eggCount = eC; eggCapacity = eCa; size = s;
    prey = p;
    bonusCard = bc;
    flocking = flo;
    loc = l;
    

  }
  public String getName()
  {
    return name;
  }
  public String getsN()
  {
    return sciName;
  }
  public String getnT()
  {
    return nestType;
  }
  public String[] getHab()
  {
    return habitats;
  }
  public Ability getbirdAbility()
  {
    return birdAbility;
  }
  public 
}
