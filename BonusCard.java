import java.util.*;

public class BonusCard {
private String name, ability, scoringType;
private TreeMap<Integer,Integer> birdScore;
private ArrayList<String> keyWordList;
private int player;
   
    public BonusCard(String n, String a, String sT, TreeMap<Integer,Integer> bS, ArrayList<String> kWL, int p) {
            name = n;
            ability = a;
            scoringType = sT;
            birdScore = bS;
            keyWordList = kWL;
            player = p;
        }
}
