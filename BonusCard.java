import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;
public class BonusCard extends Button{
private String name, ability, scoringType;
private TreeMap<Integer,Integer> birdScore; // positive numbers in the left Integer indicate minnimum threshold, and a -1 indicates per bird. EX: 5 to 7 birds: 3 point 8+ birds:7 point would have {5=3, 8=7} while 1 point per bird would have {-1=1}

private ArrayList<String> keyWordList, refinedAbility;
private Player player;
   
    public BonusCard(String n, String s, BufferedImage i, boolean c, boolean d, int xOne, int yOne, int xTwo, int yTwo, String a, String sT, TreeMap<Integer,Integer> bS, ArrayList<String> kWL, Player p) {
            super(n, s, i, c, d, xOne, yOne, xTwo, yTwo);
            ability = a;
            refinedAbility = new ArrayList<String>(Arrays.asList(ability.split(" ")));
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

    public Player getPlayer(){
        return player;
    }
    public int score() {
        /*Planning space for Anirudh. Can add comments if you want but make sure ot look at the list of birds doc first
        Algorithm plan:
        1.see if a card has the word "include". If so, count the birds the player has that has the listed terms in the name
        2.see if the card description has the word "with", but does not include the word "names". If so, move on to the branches to see what atttributes of the birds to check
        3.see if the card description has the word "that". If so, move on to the that branches "live", or "eat" accordingly
        4.disregard the cards that don't have these key words as they would just waste time

        thought process:

        the key word "with" narrows the number of possible cards to just 13, so searching for key words within that would be more efficient to create and run
        we will disregard the bonus cards that don't have any of the key words above because they would require a lot of effort to create and we don't have that much time
        
        will add comments explaining the code later
        */
       int count=0;
       for(int i=0;i<refinedAbility.size();i++)//iterates through the ability to find certain key words and counts the number of birds that qualify for the bonu card
        {
           String word=refinedAbility.get(i);
           switch(word)//check what key word the word it
           {
               case "include" -> 
                {
                   for(int j=0;j<keyWordList.size();j++)//see if the name has any of the cases specified in the bonus card
                   {
                       String keyWord=keyWordList.get(j);
                       for(Bird b:player.getAllPlayedBirds())
                       {
                           if(b.getName().toLowerCase().contains(keyWord.toLowerCase()))
                           {
                               count++;
                           }
                       }
                   }
                }
                case "with" ->
                 {
                    if(refinedAbility.contains("names")||refinedAbility.contains("named")) break;
                     if(refinedAbility.contains("nests"))//looking at nest type
                     {
                          String nestType="";
                          
                          for(Bird b:player.playerGetHand())
                          {
                            if(b.getNestType().toLowerCase().equals(nestType.toLowerCase()))
                            {
                                 count++;
                            }
                          }
                     }
                     else if(refinedAbility.contains("power"))
                     {
                        String power = refinedAbility.get(4);
                        for(Bird b:player.getAllPlayedBirds())
                       {
                           if(b.getPower().equals(power))
                           {
                               count++; 
                           }
                       }
                     }
                     else if(refinedAbility.contains("birds"))
                     {
                        TreeSet<Integer> habitatSize = new TreeSet<Integer>();
                        for(ArrayList<Spot> s:player.playerGetBoard().values())
                        {
                            int c = 0;
                            Iterator<Spot> sIter = s.iterator();
                            while(sIter.hasNext()&&sIter.next().isOccupied())
                            c++;
                        habitatSize.add(c);
                        }
                        count = habitatSize.getFirst();
                     }
                     else if(refinedAbility.contains("wingspans"))
                     {
                        if(refinedAbility.contains("over"))
                         for(Bird b:player.getAllPlayedBirds())
                       {
                           if(b.getWingspan()>65)
                            count++;
                       }
                        if(refinedAbility.contains("less"))
                         for(Bird b:player.getAllPlayedBirds())
                       {
                           if(b.getWingspan()<30)
                            count++;
                       }   
                     }
                }
                case "eat" ->
                {
                    ArrayList<String> keys = new ArrayList<String>(Arrays.asList("seed, fish, fruit, wild, rodent, wild"));
                    Iterator<String> iterKeys = keys.iterator();
                    String food = iterKeys.next();
                    while(iterKeys.hasNext()&&!refinedAbility.contains(food))
                    {
                        food = iterKeys.next();
                    }
                    for(Bird b:player.getAllPlayedBirds())
                    {
                           if(b.getCosts().containsKey(food))
                            count++;
                           else if(b.getCosts().keySet().size()==1&&b.getCosts().keySet().contains("invertibrate"))
                           {
                            count++;
                           }
                    }  
                }
                case "that" ->
                {
                    if(refinedAbility.contains("laid"))
                    {
                        int th = Integer.parseInt(refinedAbility.get(refinedAbility.indexOf("laid")-2));//th stands for threshold, as in the threshold for being counted toward the score
                         for(Bird b:player.getAllPlayedBirds())
                       {
                           if(b.getEggCount()>th)
                            count++;
                       }   
                    }
                    else if(ability.contains("only live"))
                    {
                        String hab = refinedAbility.get(refinedAbility.indexOf("live")+2);
                        for(Bird b:player.getAllPlayedBirds())
                       {
                           if(b.getHabitats().length==1&&b.getHabitats()[0].equals(hab))
                            count++;
                       } 
                    }
                }
    
            }

        }
         //make the scoring system
Iterator<Integer> scoreIter = birdScore.keySet().iterator();
 while(scoreIter.hasNext())
  { int key = scoreIter.next();
   if(key==-1)
//per bird case 
{ 
    return count*birdScore.get(key);
} else if(count>=key)
//threshold case 
{ 
    return birdScore.get(key); 
} 
} 
return 0; 
} 
}
