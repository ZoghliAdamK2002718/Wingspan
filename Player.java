import java.util.*;

public class Player {
private ArrayList<Bird> hand;
private TreeMap<String,Integer> food;
private ArrayList<BonusCard> bonus;
private HashMap<String, ArrayList<Spot>> board;
private ArrayList<Button> tokens;

    public Player(ArrayList<Bird> h, TreeMap<String,Integer> f, ArrayList<BonusCard> b, HashMap<String, ArrayList<Spot>> bo, ArrayList<Button> t) {
        hand = h;
        food = f;
        bonus = b;
        board = bo;
        tokens = t;
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
    

}
