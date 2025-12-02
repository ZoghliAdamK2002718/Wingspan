import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.TreeMap;
public class BonusCard extends Button{
private String name, ability, scoringType;
private TreeMap<Integer,Integer> birdScore;
private ArrayList<String> keyWordList;
private Player player;
   
    public BonusCard(String n, String s, BufferedImage i, boolean c, boolean d, int xOne, int yOne, int xTwo, int yTwo, String a, String sT, TreeMap<Integer,Integer> bS, ArrayList<String> kWL, Player p) {
            super(n, s, i, c, d, xOne, yOne, xTwo, yTwo);
            ability = a;
            scoringType = sT;
            birdScore = bS;
            keyWordList = kWL;
            player = p;
    }

    public void paint(Graphics g) {
        super.paint(g);
        switch(name) {//this was made by auto-fill
            case "Bird Collector":
                g.setFont(new Font("Arial", Font.BOLD, 16));
                g.setColor(Color.BLACK);
                g.drawString("Bird Collector", super.x1 + 10, super.y1 + 20);
                g.setFont(new Font("Arial", Font.PLAIN, 12));
                g.drawString("Score 1 point for each bird in your", super.x1 + 10, super.y1 + 40);
                g.drawString("habitat with a nest type matching", super.x1 + 10, super.y1 + 55);
                g.drawString("the nest type on this card.", super.x1 + 10, super.y1 + 70);
                break;
            // Add more cases for different bonus cards as needed
            default:
                g.setFont(new Font("Arial", Font.BOLD, 16));
                g.setColor(Color.BLACK);
                g.drawString(name, super.x1 + 10, super.y1 + 20);
                g.setFont(new Font("Arial", Font.PLAIN, 12));
                g.drawString("Ability: " + ability, super.x1 + 10, super.y1 + 40);
                g.drawString("Scoring Type: " + scoringType, super.x1 + 10, super.y1 + 55);
                break;
        }
    }

    public void click() {
       switch(name) {
           // Add more cases for different bonus cards as needed
           default:
               // Default action or no action
               break;
       }
    }

    public int score() {
        return player;
    }

}
