import java.util.*;

public class Player {
private ArrayList<Bird> hand;
private TreeMap<String,Integer> food;
private ArrayList<BonusCard> bonus;
private HashMap<String, ArrayList<Spot>> board;
private ArrayList<Button> screenDisplay = new ArrayList<Button>();
private ArrayList<Button> tokens;
private int remainingTokens = 8;
private int storedEggs = 0;
private static ArrayList<Player> players = new ArrayList<Player>(Arrays.asList(new Player(new ArrayList<Bird>(),
                            new TreeMap<String,Integer>(),
                            new ArrayList<BonusCard>(),
                            new HashMap<String, ArrayList<Spot>>(),
                            new ArrayList<Button>(),0),new Player(new ArrayList<Bird>(),
                            new TreeMap<String,Integer>(),
                            new ArrayList<BonusCard>(),
                            new HashMap<String, ArrayList<Spot>>(),
                            new ArrayList<Button>(),1),new Player(new ArrayList<Bird>(),
                            new TreeMap<String,Integer>(),
                            new ArrayList<BonusCard>(),
                            new HashMap<String, ArrayList<Spot>>(),
                            new ArrayList<Button>(),2),new Player(new ArrayList<Bird>(),
                            new TreeMap<String,Integer>(),
                            new ArrayList<BonusCard>(),
                            new HashMap<String, ArrayList<Spot>>(),
                            new ArrayList<Button>(),3)));
public static int currentPlayerIndex = 0;

    public Player(ArrayList<Bird> h, TreeMap<String,Integer> f, ArrayList<BonusCard> b, HashMap<String, ArrayList<Spot>> bo, ArrayList<Button> t,int playerNumber) {
        hand = h;
        food = f;
        bonus = b;
        board = bo;
        tokens = t;
        storedEggs = 0;
        
        // Initialize board with spots if empty
        if(board.isEmpty()) {
            initializeBoard();
        }
        
        for(String k:board.keySet())
        {
            screenDisplay.addAll(board.get(k));
        }
        screenDisplay.add(new Button("Previous Player",""+playerNumber,null,true,true,10,890,110,990));
        screenDisplay.add(new Button("Next Player",""+playerNumber,null,true,true,890,890,990,990));
        screenDisplay.add(new Button("Birdfeeder and Deck",""+playerNumber,null,true,true,10,790,110,880));
        screenDisplay.add(new Button("Score",""+playerNumber,null,true,true,450,890,550,990));
        for(int i=0;i<8;i++)
        tokens.add(new Button("Action Token",""+i,null,true,true,100*i,100*i,10+(100*i),10+(100*i)));
    
        screenDisplay.addAll(tokens);
        System.out.println("Buttons have been made for player "+playerNumber);
}
    
    private void initializeBoard() {
        // Create spots for each habitat
        ArrayList<Spot> forestSpots = new ArrayList<>();
        ArrayList<Spot> grasslandSpots = new ArrayList<>();
        ArrayList<Spot> wetlandSpots = new ArrayList<>();
        
        // Create 5 spots for each habitat (indices 0-4)
        for(int i = 0; i < 5; i++) {
            forestSpots.add(new Spot("Forest", i, null));
            grasslandSpots.add(new Spot("Grassland", i, null));
            wetlandSpots.add(new Spot("Wetland", i, null));
        }
        
        board.put("Forest", forestSpots);
        board.put("Grassland", grasslandSpots);
        board.put("Wetland", wetlandSpots);
    } 
    private void updateSpotClickability(HashMap<String, ArrayList<Spot>> board) {
        for (ArrayList<Spot> spots : board.values()) {
            boolean foundUnoccupied = false;
            for (Spot s : spots) {
                if (!s.isOccupied() && !foundUnoccupied) {
                    s.setClickable(true);
                    foundUnoccupied = true;
                } else {
                    s.setClickable(false);
                }
            }
        }
    }
    public void playerSetHand(ArrayList<Bird> h) {
        hand = h;
    
}
    public void DrawHand(ArrayList<Bird> h) {
        hand.addAll(h);
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
        if(food == null) {
            food = new TreeMap<String,Integer>();
        }
        normalizeFoodMap();
        String key = normalizeFoodKey(foodType);
        if(key == null) return;
        food.put(key, food.getOrDefault(key, 0) + amount);
    }
    public boolean spendFood(String foodType, int amount) {
        if(foodType == null || amount <= 0) return false;
        if(food == null) {
            food = new TreeMap<String,Integer>();
        }
        normalizeFoodMap();
        String key = normalizeFoodKey(foodType);
        int current = food.getOrDefault(key, 0);
        if(current < amount) return false;
        int remaining = current - amount;
        if(remaining > 0) {
            food.put(key, remaining);
        } else {
            food.remove(key);
        }
        return true;
    }
    private String normalizeFoodKey(String key) {
        if(key == null) return null;
        String k = key.toLowerCase().trim();
        if(k.startsWith("seed") || k.startsWith("grain") || k.startsWith("wheat")) return "grain";
        if(k.startsWith("invert")) return "invertebrate";
        if(k.startsWith("rodent")) return "rodent";
        if(k.startsWith("fish")) return "fish";
        if(k.startsWith("fruit") || k.startsWith("berry")) return "fruit";
        if(k.startsWith("wild")) return "wild";
        if(k.equals("no-food")) return "no-food";
        return k;
    }
    private void normalizeFoodMap() {
        if(food == null || food.isEmpty()) return;
        TreeMap<String,Integer> normed = new TreeMap<String,Integer>();
        for(Map.Entry<String,Integer> e : food.entrySet()) {
            String norm = normalizeFoodKey(e.getKey());
            if(norm == null) continue;
            normed.put(norm, normed.getOrDefault(norm,0) + (e.getValue()==null?0:e.getValue()));
        }
        food = normed;
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
    public int getRemainingTokens() {
        return remainingTokens;
    }
    public boolean useActionToken() {
        if(remainingTokens <= 0) return false;
        remainingTokens--;
        return true;
    }
    public void resetTokens(int count) {
        remainingTokens = count;
    }
    public void addStoredEggs(int amount) {
        if(amount <= 0) return;
        storedEggs += amount;
    }
    public boolean spendStoredEggs(int amount) {
        if(amount <= 0) return false;
        if(storedEggs < amount) return false;
        storedEggs -= amount;
        return true;
    }
    public int getStoredEggs() {
        return storedEggs;
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
                            new ArrayList<Button>(),i));
            System.out.println(players.size());
            return players;
  
    }

    public static HashMap<String, ArrayList<Spot>> getCurrentPlayerBoard(){
        return players.get(currentPlayerIndex).playerGetBoard();
    }

    public ArrayList<Bird> getAllPlayedBirds(){
        ArrayList<Bird> played = new ArrayList<Bird>();
        for(ArrayList<Spot> spots: board.values()){
            for(Spot s: spots){
                if(s.getBird()!=null){
                    played.add(s.getBird());
                }
            }
        }
        return played;
    }


    

}
  
