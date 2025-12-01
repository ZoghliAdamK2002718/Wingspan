import java.util.*;

public class Player {
private ArrayList<Bird> hand;
private TreeMap<String,Integer> food;
private ArrayList<BonusCard> bonus;
private HashMap<String, ArrayList<Spot>> board;
private ArrayList<Button> screenDisplay = new ArrayList<Button>();
private ArrayList<Button> tokens;
private static ArrayList<Player> players = new ArrayList<Player>(Arrays.asList(new Player(new ArrayList<Bird>(),
                            new TreeMap<String,Integer>(),
                            new ArrayList<BonusCard>(),
                            new HashMap<String, ArrayList<Spot>>(),
                            new ArrayList<Button>()),new Player(new ArrayList<Bird>(),
                            new TreeMap<String,Integer>(),
                            new ArrayList<BonusCard>(),
                            new HashMap<String, ArrayList<Spot>>(),
                            new ArrayList<Button>()),new Player(new ArrayList<Bird>(),
                            new TreeMap<String,Integer>(),
                            new ArrayList<BonusCard>(),
                            new HashMap<String, ArrayList<Spot>>(),
                            new ArrayList<Button>()),new Player(new ArrayList<Bird>(),
                            new TreeMap<String,Integer>(),
                            new ArrayList<BonusCard>(),
                            new HashMap<String, ArrayList<Spot>>(),
                            new ArrayList<Button>())));
public static int currentPlayerIndex = 0;

    public Player(ArrayList<Bird> h, TreeMap<String,Integer> f, ArrayList<BonusCard> b, HashMap<String, ArrayList<Spot>> bo, ArrayList<Button> t) {
        hand = h;
        food = f;
        bonus = b;
        board = bo;
        tokens = t;
        for(String k:board.keySet())
        {
            screenDisplay.addAll(board.get(k));
        }
        screenDisplay.add(new Button("Previous Player",""+currentPlayerIndex,null,true,true,10,890,110,990));
        screenDisplay.add(new Button("Next Player",""+currentPlayerIndex,null,true,true,890,890,990,990));
        screenDisplay.add(new Button("Birdfeeder and Deck","none",null,true,true,10,790,110,880));
        for(int i=0;i<8;i++)
        tokens.add(new Button("Action Token",""+i,null,true,true,100*i,100*i,10+(100*i),10+(100*i)));
        screenDisplay.addAll(tokens);
     
}

    public void playerSetHand(ArrayList<Bird> h) {
        hand = h;
    }
    public ArrayList<Bird> playerGetHand() {
        return hand;
    }
    public void playerSetFood(TreeMap<String,Integer> f) {
        food = f;
    }
    public TreeMap<String,Integer> playerGetFood() {
        return food;
    }
    public void addFood(String foodType, int amount) {
        food.put(foodType, food.getOrDefault(foodType, 0) + amount);
    }
    public void playerSetBonus(ArrayList<BonusCard> b) {
        bonus = b;
    }
    public ArrayList<BonusCard> playerGetBonus() {
        return bonus;
    }
    public void playerSetBoard(HashMap<String, ArrayList<Spot>> bo) {
        board = bo;
    }
    public HashMap<String, ArrayList<Spot>> playerGetBoard() {
        return board;
    }
    public void playerSetTokens(ArrayList<Button> t) {
        tokens = t;
    }
    public ArrayList<Button> playerGetTokens() {
        return tokens;
    }
    public ArrayList<Button> playerGetScreenDisplay(){
        return screenDisplay;
    }
    public void setScreenDisplay(ArrayList<Button> in){
        screenDisplay = in;
    }
    public static ArrayList<Player> players(){
            if(players.size()<4)
                for(int i=0;i<4;i++)
                players.add(new Player(new ArrayList<Bird>(),
                            new TreeMap<String,Integer>(),
                            new ArrayList<BonusCard>(),
                            new HashMap<String, ArrayList<Spot>>(),
                            new ArrayList<Button>()));
            System.out.println(players.size());
            return players;
  
    }
    

}
  
