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
 
private final TreeMap<String,ArrayList<String>> keyWords = new TreeMap<String,ArrayList<String>>() {{/*all the key words for the ability. If you touch this without knowing exactly what you are doing, I will do a record breaking crash out ^w^ */
    put("play",new ArrayList<String>(Arrays.asList("this", "forest", "wetland", "grassland")));
    put("gain",new ArrayList<String>(Arrays.asList("NUM","any","all","or","die","rodent","fish","seed","invertibrate","fruit","wild","supply","available","birdfeeder")));// add a return statement for the key word
    put("right",new ArrayList<String>(Arrays.asList()));
    put("keep",new ArrayList<String>(Arrays.asList()));//use either the bonus card or bird card from the variable set
    put("look",new ArrayList<String>(Arrays.asList("NUM")));
    put("roll",new ArrayList<String>(Arrays.asList("rodent","fish")));//add a return statement for the key word
    put("draw",new ArrayList<String>(Arrays.asList("equal","NUM","bonus","deck")));//
    put("lay",new ArrayList<String>(Arrays.asList("eggs","another","this","any","each","ground","cavity","burrow","bowl","platform")));
    put("tuck",new ArrayList<String>(Arrays.asList("NUM","hand","deck","it")));//
    put("discard",new ArrayList<String>(Arrays.asList("NUM","egg","other","this","card","seed","fish","rodent","it","end")));
    put("players",new ArrayList<String>(Arrays.asList("selects")));//
    put("You",new ArrayList<String>(Arrays.asList()));// You has to be capitalized
    put("selects",new ArrayList<String>(Arrays.asList()));//runs players, and removes the cards that other players choose
    put("Each",new ArrayList<String>(Arrays.asList()));// runs players, and has to be capitalized
    put("cache",new ArrayList<String>(Arrays.asList("it","rodent","fish","seed","supply")));// add a return statement for the key word
}};


public Ability(String rA, String tT, String tN, ArrayList<String> a) {
    rawAbility = rA;
    triggerName = tN;
    ability = a;
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
 *  NUM: wherever NUM is mentioned in the keywords or parameters, it represents an integer value that will be provided when the ability is executed
 *  If you need to add more parameters to the methods please let me know so I can update the ability parsing system accordingly
 *  If you need to clarificatiion on what an ability method should do, first go to the list of birds that I sent, and use CRTL+F to find what bird abilities use that method to get a better understanding
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
public static void keep(String cardType, Bird bird1, Bird bird2, BonusCard bonus1, BonusCard bonus2) {
   
}


/* looks at a number of cards from the deck specified
 *  
 * @param deckType the type of deck to look at
 * @return void
 *
 * keywords:
 *  NUM: the number of cards to look at
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
 *  equal: draw equal to the number of birds in the habitat specified
 *  NUM: the number of cards to draw
 *  bonus: draw from the bonus card deck
 *  deck: draw from the bird card deck
 */
public static void draw(String deckType, int number, String habitat) {
   
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


}


/* tucks a number of cards from the variable set
 *  
 * @param source the source from which to tuck cards
 * @param amount the number of cards to tuck
 * @return void
 *
 * keywords:
 *  NUM: the number of cards to tuck
 *  hand: tuck from the player's hand
 *  deck: tuck from the bird card deck
 *  it: there is no source
 */
public static void tuck(String source, String amount, Bird birdInput ) {
   
}


/* discards a number of items
 *  
 * @param itemType the type of item to discard
 * @param amount the number of items to discard
 * @return void
 *
 * keywords:
 *  NUM: the number of items to discard
 *  egg: discard eggs
 *  other: discard from other players
 *  this: discard the item containing this ability
 *  card: discard bird cards
 *  seed, fish, rodent: discard food items
 *  it: there is no item type
 *  end: discard at the end of the turn
 */
public static void discard(String itemType, String amount, Bird birdInput ) {


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
 *  it: cache food specified by the ability
 *  rodent, fish, seed: specific food types
 *  supply: cache from the supply
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
}

