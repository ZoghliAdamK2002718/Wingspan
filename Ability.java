import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.TreeMap;


public class Ability {
private String rawAbility/*the ability put into the constructor */, triggerType/*pink or brown */,triggerName/*name of the object */;
private ArrayList<String> ability;/*what is used to read and execute the ability */
private ArrayList<Integer> players;/*the players that will execute the ability functions */
private static ArrayList<Ability> pinkTriggers;/*all pink abilities in the game that are active*/
private HashMap<Class,ArrayList<Object>> inputs;/*what is returned by the ability function */
private Bird bird;/* the bird containing this ability object */
private Spot spot;/* the spot containing this ability object */
private Player player;/* the player containing this ability object */
 
private final TreeMap<String,ArrayList<String>> keyWords = new TreeMap<String,ArrayList<String>>() {{/*all the key words for the ability. If you touch this without knowing exactly what you are doing, I will do a record breaking crash out ^w^ */
    put("play",new ArrayList<String>(Arrays.asList("this", "forest", "wetland", "grassland")));
    put("gain",new ArrayList<String>(Arrays.asList("NUM","any","all","or","die","rodent","fish","seed","invertibrate","fruit","wild","supply","available","birdfeeder")));// add a return statement for the key word
    put("right",new ArrayList<String>(Arrays.asList()));
    put("keep",new ArrayList<String>(Arrays.asList()));//use either the bonus card or bird card from the variable set
    put("look",new ArrayList<String>(Arrays.asList("NUM")));
    put("roll",new ArrayList<String>(Arrays.asList("rodent","fish")));//add a return statement for the key word
    put("draw",new ArrayList<String>(Arrays.asList("equal","NUM","bonus","deck")));//
    put("lay",new ArrayList<String>(Arrays.asList(,"another","this","any","each","ground","cavity","burrow","bowl","platform")));
    put("tuck",new ArrayList<String>(Arrays.asList("NUM","hand","deck","it")));//
    put("discard",new ArrayList<String>(Arrays.asList("NUM","egg","other","this","card","seed","fish","rodent","it","end")));
    put("players",new ArrayList<String>(Arrays.asList("selects")));//
    put("You",new ArrayList<String>(Arrays.asList()));// You has to be capitalized
    put("selects",new ArrayList<String>(Arrays.asList()));//runs players, and removes the cards that other players choose
    put("Each",new ArrayList<String>(Arrays.asList()));// runs players, and has to be capitalized
    put("cache",new ArrayList<String>(Arrays.asList("it","rodent","fish","seed","supply")));// add a return statement for the key word
}};

public Ability(String rA, String tT, String tN, ArrayList<String> a) {
    // Parse trigger type from raw ability if not provided
    if(tT == null && rA != null) {
        if(rA.toUpperCase().contains("WHEN PLAYED")) {
            triggerType = "white";
            triggerName = "WHEN PLAYED";
        } else if(rA.toUpperCase().contains("WHEN ACTIVATED")) {
            triggerType = "brown";
            triggerName = "WHEN ACTIVATED";
        } else if(rA.toUpperCase().contains("GAME END")) {
            triggerType = "end";
            triggerName = "GAME END";
        } else if(ra.toUpperCase().contains("ONCE BETWEEN TURNS")) {
            triggerType = "pink";
            String[] pinkNames = {"eggs\"","their wetland","predator","thier forest","their grassland","food\""};
            for(int i=0;i<pinkNames.length;i++)
            {
                if(ra.contains(pinkNames[i]))
                tN = pinkNames[i];
                ra = ra.substring(pinkNames[i]);
            }
        }
    } else {
        triggerType = tT;
        triggerName = tN;
    }
    
    // Process the ability text into tokens
    ArrayList<String> refinedAbility = new ArrayList<>();
    if(a != null) {
        refinedAbility = a;
    } else if(rA != null) {
        // Extract just the ability part (after the colon)
        String abilityText = rA;
        if(rA.contains(":")) {
            abilityText = rA.substring(rA.indexOf(":") + 1).trim();
        }
        
        // Split and clean
        String[] words = abilityText.split("\\s+");
        for(String word : words) {
            // Remove punctuation but keep the word
            String cleaned = word.replaceAll("[.,;:]", "").trim();
            if(!cleaned.isEmpty()) {
                if(!cleaned.equals("You")&&!cleaned.equals("Each")) {
                    cleaned = cleaned.toLowerCase();
                }
                refinedAbility.add(cleaned);
            }
        }
    }
    
    ability = refinedAbility;
    players = new ArrayList<>();
    inputs = new HashMap<>();
    inputs.put(String.class, new ArrayList<Object>(Arrays.asList("")));
    inputs.put(Bird.class, new ArrayList<Object>());
    inputs.put(BonusCard.class, new ArrayList<Object>());
    inputs.put(Integer.class, new ArrayList<Object>());
    
    // Add to pink triggers if this is a pink ability
    if(triggerType != null && triggerType.equalsIgnoreCase("pink")) {
        if(pinkTriggers == null) {
            pinkTriggers = new ArrayList<>();
        }
        pinkTriggers.add(this);
    }
}

@SuppressWarnings("unchecked")
public void execute()
{
triggerName+="\ttriggered for the round";
 for(int i=0;i<ability.size();i++)//Read each word in the ability
 {
    String currentString = ability.get(i);//get current word
    if(currentString != null && keyWords.containsKey(currentString))//if the word corresponds to a method
    {
        String method = currentString;//set method to current word
        //get parameters for method
        if(i<ability.size()-1)
        currentString = ability.get(++i);//move to next word
        ArrayList<String> params = keyWords.get(method);//get parameters for method
        while(!keyWords.containsKey(currentString)&& i<ability.size())//while the current word is not a method and there are still words left
        {
            currentString = ability.get(i++);//move to next word
            if(params.contains(currentString))//if the current word is a valid parameter
            {
                //valid parameter
                inputs.get(String.class).add(currentString);//add to inputs hashMap
            }
            else if(params.contains("NUM")&&currentString.matches("\\d+"))//if the current word is a valid parameter and is a number
            {
                //valid parameter
                inputs.get(Integer.class).add(Integer.parseInt(currentString));//add to inputs hashMap
            }
        }
    
    
        //call method with parameters
        switch(method) 
        {
            case "play":
                play((String)inputs.get(String.class).get(0));/*input the habitat */
                break;
            case "gain":
            String food = "";/*inputs to the gain method */
            String source = "";
            String amount = "";
            ArrayList<String> keys = keyWords.get("gain");//all possible keywords for gain
            for(int j=0;j<keys.size();j++)//sort the found keywords into their respective variables
            {
                if(inputs.get(String.class).contains(keys.get(j)))
                {
                    if(keys.get(j).equals("rodent")||keys.get(j).equals("fish")||keys.get(j).equals("seed")||keys.get(j).equals("invertibrate")||keys.get(j).equals("fruit")||keys.get(j).equals("wild")||keys.get(j).equals("or")||keys.get(j).equals("die"))
                    {
                        food += keys.get(j);
                        inputs.get(String.class).remove(keys.get(j));//remove from inputs so it doesn't get added to amount/source
                    }
                    else if(keys.get(j).equals("supply")||keys.get(j).equals("birdfeeder"))
                    {
                        source = keys.get(j);
                        inputs.get(String.class).remove(keys.get(j));//remove from inputs so it doesn't get added to amount/source
                    }
                    else if(keys.get(j).equals("any")||keys.get(j).equals("all"))
                    {
                        amount = keys.get(j);
                        inputs.get(String.class).remove(keys.get(j));//remove from inputs so it doesn't get added to amount/source
                    }
                    else if(inputs.get(Integer.class).size()>0)
                    {
                        amount = Integer.toString((Integer)inputs.get(Integer.class).get(0));
                        inputs.get(Integer.class).clear();//remove from inputs so it doesn't get added to amount/source
                    } else {
                       inputs.get(String.class).remove(keys.get(j));//remove from inputs so it doesn't get added to amount/source
                    }
                }
            
            }
            inputs.get(String.class).add(gain(food,source,amount));//call gain method and add what was gained for future methods
                break;//end of gain case
            case "right":
                right();//all the birds do the same thing with this keyword
                break;
            case "keep":
                ArrayList<Bird> birds = new ArrayList<Bird>();
                birds.addAll((ArrayList<Bird>)(Object)inputs.get(Bird.class));//get birds from inputs
                ArrayList<BonusCard> bonusCards = new ArrayList<BonusCard>();
                bonusCards.addAll((ArrayList<BonusCard>)(Object)inputs.get(BonusCard.class));//get bonus cards from inputs
                String cardType = "";//determine which type of card to keep
                if(inputs.get(String.class).contains("bonus"))
                {
                    cardType = "bonus";
                }
                else
                {
                    cardType = "bird";
                }
                keep(cardType, birds, bonusCards);//call keep method
                break;
            case "look":
                int number = 0;
                if(inputs.get(Integer.class).size() > 0)//determine if the number of cards is specified
                {
                    number = (Integer)inputs.get(Integer.class).get(0);
                    inputs.get(Integer.class).clear();//remove from inputs so it doesn't get added to number
                }
                look("deck", number);//call look method
                break;
            case "roll":
                String foodType = "";
                if(inputs.get(String.class).size() > 0)
                {
                    foodType = (String)inputs.get(String.class).get(0);
                    inputs.get(String.class).clear();//remove from inputs so it doesn't get added to foodType
                }
                roll(foodType);//call roll method
                break;
            case "draw":
                String deckType = "deck";
                String num = "0";
                String habitat = "";
                if(inputs.get(String.class).contains("bonus"))
                {
                    deckType = "bonus";
                    inputs.get(String.class).remove("bonus");//remove from inputs so it doesn't get added to deckType
                }
                else
                {
                    inputs.get(String.class).remove("deck");//remove from inputs so it doesn't get added to deckType
                }
               
                if(inputs.get(Integer.class).size() > 0)
                {
                    num = String.valueOf((Integer)inputs.get(Integer.class).get(0));
                    inputs.get(Integer.class).clear();//remove from inputs so it doesn't get added to number
                }
                else if(inputs.get(String.class).contains("equal"))
                {
                    //determine number equal to birds in habitat
                    //num = number of birds in habitat
                    num = "equal";
                    inputs.get(String.class).remove("equal");//remove from inputs so it doesn't get added to deckType
                }
                if(inputs.get(String.class).size() > 0)
                {
                    habitat = (String)inputs.get(String.class).get(0);
                    inputs.get(String.class).clear();//remove from inputs so it doesn't get added to habitat
                }

                draw(deckType,num);//call draw method
                break;
            case "lay":
                String nestType = "";
                int numEggs = 0;
                ArrayList<String> layKeys = keyWords.get("lay");//all possible keywords for lay
                for(int j=0;j<layKeys.size();j++)//sort the found keywords into their respective variables
                {
                    if(inputs.get(String.class).contains(layKeys.get(j)))
                    {
                        if(layKeys.get(j).equals("ground")||layKeys.get(j).equals("cavity")||layKeys.get(j).equals("burrow")||layKeys.get(j).equals("bowl")||layKeys.get(j).equals("platform"))
                        {
                            nestType = layKeys.get(j);
                            inputs.get(String.class).remove(layKeys.get(j));//remove from inputs so it doesn't get added to nestType
                        }
                        else
                        {
                            inputs.get(String.class).remove(layKeys.get(j));//remove from inputs so it doesn't get added to nestType
                        }
                    }
                }
                if(inputs.get(Integer.class).size() > 0)
                {
                    numEggs = (Integer)inputs.get(Integer.class).get(0);
                    inputs.get(Integer.class).clear();//remove from inputs so it doesn't get added to numEggs
                }
                lay(nestType, numEggs);//call lay method
                break;
            case "tuck":
                String tuckSource = "";
                String tuckAmount = "";
                ArrayList<String> tuckKeys = keyWords.get("tuck");//all possible keywords for lay
                for(int j=0;j<tuckKeys.size();j++)//sort the found keywords into their respective variables
                {
                    if(inputs.get(String.class).contains(tuckKeys.get(j)))
                    {
                        if(tuckKeys.get(j).equals("hand")||tuckKeys.get(j).equals("deck")||tuckKeys.get(j).equals("it"))
                        {
                            tuckSource = tuckKeys.get(j);
                            inputs.get(String.class).remove(tuckKeys.get(j));//remove from inputs so it doesn't get added to nestType
                        }
                        else if(tuckKeys.contains("NUM")&&currentString.matches("\\d+"))
                        {
                            tuckAmount = tuckKeys.get(j);
                            inputs.get(Integer.class).remove(tuckKeys.get(j));//remove from inputs so it doesn't get added to nestType
                        }
                        else
                        {
                            inputs.get(String.class).remove(tuckKeys.get(j));//remove from inputs so it doesn't get added to nestType
                        }
                    }
                }
               
                tuck(tuckSource,tuckAmount,(Bird)inputs.get(Bird.class).get(0));//call tuck method
                break;
            case "discard":
                /*String itemType, String location, String amount, Bird birdInput */
                String discardType = null, discardLocation = null, discardAmount = null;
                ArrayList<String> discardKeys = keyWords.get("discard");//all possible keywords for lay
                for(int j=0;j<discardKeys.size();j++)//sort the found keywords into their respective variables
                {
                    if(inputs.get(String.class).contains(discardKeys.get(j)))
                    {
                        if(discardKeys.get(j).equals("egg")||discardKeys.get(j).equals("card")||discardKeys.get(j).equals("seed")||discardKeys.get(j).equals("it"))
                        {
                            discardType = discardKeys.get(j);
                            inputs.get(String.class).remove(discardKeys.get(j));//remove from inputs so it doesn't get added to nestType
                        }
                        else if(discardKeys.contains("NUM")&&currentString.matches("\\d+"))
                        {
                            discardAmount = discardKeys.get(j);
                            inputs.get(String.class).remove(discardKeys.get(j));//remove from inputs so it doesn't get added to nestType

                        }
                        else if(discardKeys.contains("other")||discardKeys.contains("this")||discardKeys.contains("end"))
                        {
                            discardAmount = discardKeys.get(j);
                            inputs.get(String.class).remove(discardKeys.get(j));//remove from inputs so it doesn't get added to nestType
                        }
                    }
                }
               
                discard(discardType, discardLocation, discardAmount, (Bird)inputs.get(Bird.class).get(0));//call discard method
            case "players":
                String playerInput = "";
                ArrayList<String> playersKeys = keyWords.get("players");//all possible keywords for players
                for(int j=0;j<playersKeys.size();j++)//sort the found keywords into their respective variables
                {
                        if(playersKeys.get(j).equals("selects"))
                        {
                            playerInput = playersKeys.get(j);
                            inputs.get(String.class).remove(playersKeys.get(j));//remove from inputs so it doesn't get added to nestType
                        }

                }
                players(playerInput);
            break;
            case "You":
                you();
            break;
            case "selects":
                selects();
            break;
            case "Each":
                Each();
            break;
            case "cache":
             String cacheInput = "";
             ArrayList<String> cacheKeys = keyWords.get("cache");//all possible keywords for players
                for(int j=0;j<cacheKeys.size();j++)//sort the found keywords into their respective variables
                {
                        if(cachekeys.contains())//
                        {
                            cacheInput = cacheKeys.get(j);
                            inputs.get(String.class).remove(cacheKeys.get(j));//remove from inputs so it doesn't get added to nestType
                        }

                }
                players(playerInput);
            break;
        } /*To-do Pink ability implementation.*/

    } else {}
 }
}
/*
 * ABILITY METHODS:
 *  I have provided skeletons for all the ability methods that will be needed for the game
 * Each method has a description of what it does, its parameters, return type, and keywords that can be used within the method
 * These methods are all static, and will be called by parsing the ability strings of birds and bonus cards
 * Implement the logic for each ability method as per the game rules
 * These methods used auto-fill, so I may make some changes to the parameters or return types later. Pwease don't get angwy ):
 *
 * Important Notes:
 *  NUM: wherever NUM is mentioned in the keywords or parameters, it represents an integer value that will be provided when the ability is executed in the String amount
 *  If you need to add more parameters to the methods please let me know so I can update the ability parsing system accordingly
 *  If you need to clarificatiion on what an ability method should do, first go to the list of birds that I sent, and use CRTL+F to find what bird abilities use that method to get a better understanding. Then ask me if you still need help
 *  In order to change which Players the Ability is activated for, change the players ArrayList within the Ability object to reflect the order of players
 */






/* plays a bird from the player's hand that could be placed in the habitat specified
 *  
 * @param habitat the habitat in which the bird is to be played
 * @return void
 *
 * keywords:
 *  this: the habitat of the bird containing this ability
 *  forest, wetland, grassland: the habitat in which the bird is to be played
 */
public static void play(String habitat) {

//run the below code to execute the pink triggers
for(int i=0;i<pinkTriggers.size();i++)
    {
        if(pinkTriggers.get(i).equals("thier "+habitat))
        pinkTriggers.get(i).execute();

    }
}


/* gains food from the supply specified
 *  
 * @param food the food to be gained
 * @param source the source from which the food is gained
 * @param amount the amount of food to be gained
 * @return what was gained by the player
 *
 * keywords:
 *    amount:
 *  NUM: the number of food items to be gained  
 *  any: any number of food specified from the bird feeder  
 *  all: all food specified from the bird feeder  
 *    food:
 *  rodent, fish, seed, invertebrate, fruit, wild: specific food types
 *  or: choose between two food types specified
 *   source:
 *  supply: gain food from the supply
 *  birdfeeder: gain food from the birdfeeder
 *  
 */
public static String gain(String food, String source, String amount) {
    return "";

// run the code below to trigger the pink abilities
    for(int i=0;i<pinkTriggers.size();i++)
    {
        if(pinkTriggers.get(i).equals("food\""))
        pinkTriggers.get(i).execute();

    }
}


/*
 * if a bird is the rightmost bird in its habitat, allow the bird to move to another one
 * @param void
 * @return void
 */
public static void right() {


}


/* keeps a card specified from the variable set
 *  
 * @param cardType the type of card to be kept
 * @return void
 *
 * keywords:
 *  bonus: keep a bonus card
 *  bird: keep a bird card
 */
public static void keep(String cardType, ArrayList<Bird> birds, ArrayList<BonusCard> bonusCards) {
   
}


/* looks at a number of cards from the deck specified
 *  
 * @param deckType the type of deck to look at
 * @return void
 *
 * keywords:
 *  number: the number of cards to look at
 *      NUM: the number of cards to look at from the deck specified
 * deckType:
 *  deck: look at cards from the bird card deck
 */
public static void look(String deckType, int number) {
   
}


/* rolls a die for food specified
 *  
 * @param foodType the type of food to roll for
 * @return what was rolled
 *
 * keywords:
 *  rodent, fish: specific food types
 */
public static String roll(String foodType) {
    return "";
}


/* draws a number of cards from the deck specified
 *  
 * @param deckType the type of deck to draw from
 * @return void
 *
 * keywords:
 *amount: the number of cards to draw
 *  equal: draw equal to the number of birds in the habitat specified
 *  NUM: the number of cards to draw
 * deckType: the type of deck to draw from
 *  bonus: draw from the bonus card deck
 *  deck: draw from the bird card deck
 * 
 */
public static void draw(String deckType, String amount) {
   
}


/* lays a number of eggs on birds in the habitat specified
 *  
 * @param habitat the habitat in which to lay eggs
 * @return void
 *
 * keywords:
 *  NUM: the number of eggs to lay
 *  another: lay on another bird with the specified nest type
 *  this: lay on the bird containing this ability
 *  any: lay on any bird
 *  each: lay on each bird with the specified nest type
 *    nest types:
 *  ground, cavity, burrow, bowl, platform: lay on birds with the specified nest type
 */
public static void lay(String nestType, int number) {


/* check for pink bird
    run the for loop below to trigger the corrosponding pink abilitys.
 */
    for(int i=0;i<pinkTriggers.size();i++)
    {
        if(pinkTriggers.get(i).equals("eggs\""))
        pinkTriggers.get(i).execute();

    }
}


/* tucks a number of cards from the variable set
 *  
 * @param source the source from which to tuck cards
 * @param amount the number of cards to tuck
 * @return void
 *
 * keywords:
 *  amount:
 *  NUM: the number of cards to tuck
 *  source:
 *  hand: tuck from the player's hand
 *  deck: tuck from the bird card deck
 *  it: tuck birdInput
 *     
 */
public static void tuck(String source, String amount, Bird birdInput ) {
   
   // Run the code below to trigger the pink abilities
   for(int i=0;i<pinkTriggers.size();i++)
    {
        if(pinkTriggers.get(i).equals("predator"))
        pinkTriggers.get(i).execute();

    }
}


/* discards a number of items
 *  
 * @param itemType the type of item to discard
 * @param amount the number of items to discard
 * @return void
 *
 * keywords:
    amount:
 *  NUM: the number of items to discard
    itemType:
 *  egg: discard eggs
    card: discard bird cards
 *  seed, fish, rodent: discard food items
 *  it: use birdInput
 *  location:
 *  other: discard from a bird not containing this ability
 *  this: discard the item containing this ability
 *  end: discard at the end of the turn
 */
public static void discard(String itemType, String location, String amount, Bird birdInput ) {


}


/*
 * sets the arrayList of players to execute the ability to contain all players
 * @param void
 * @return void
 * keywords:
 * selects: each player selects the option specified in the ability
 */
public static void players(String selects) {
   
}


/* Each player executes the ability, starting with whom the player chooses
 *  
 * @param void
 * @return void
 *
 * keywords:
 *  Each: each player executes the ability
 *  You: the player executing the ability
 */
public static void Each() {


}


/* caches food specified
 *  
 * @param foodType the type of food to cache
 * @return what was cached
 *
 * keywords:
 *  foodType:
 *  it: cache food specified by the ability
 *  rodent, fish, seed: specific food types
 *  supply: cache from the supply
 *  amount:
 *  "NUM"
 */
public static String cache(String foodType, String amount) {
    return "";


}






public static ArrayList<Ability> getPinkTriggers() {
    return pinkTriggers;
}
public String getRawAbility() {
    return rawAbility;
}


public String getTriggerType() {
    return triggerType;


}


public String getTriggerName() {
    return triggerName;
}


public ArrayList<String> getAbility() {
    return ability;
}


public ArrayList<Integer> getPlayers() {
    return players;
}
public HashMap<Class,ArrayList<Object>> getInputs() {
    return inputs;
}
public void setInputs(HashMap<Class,ArrayList<Object>> i) {
    inputs = i;
}
public Bird getBird() {
    return bird;
}
public void setBird(Bird b) {
    bird = b;
}
public Spot getSpot() {
    return spot;
}
public void setSpot(Spot s) {
    spot = s;
}
public Player getPlayer() {
    return player; 
}
public void setPlayer(Player p) {
    player = p;
}
}
