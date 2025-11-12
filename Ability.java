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
// Game context - these should be set when ability is activated
private static Player currentPlayer;
private static ArrayList<Player> allPlayers;
private static Panel gamePanel;
 
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


// Constructor that takes raw ability string and player (matches your test data)
public Ability(String rA, int currentPlayer) {
    this(rA, null, null, null);
}

public Ability(String rA, String tT, String tN, ArrayList<String> a) {
    rawAbility = rA;
    
    // Parse trigger type from raw ability if not provided
    if(tT == null && rA != null) {
        if(rA.toUpperCase().contains("WHEN PLAYED")) {
            triggerType = "brown";
            triggerName = "WHEN PLAYED";
        } else if(rA.toUpperCase().contains("WHEN ACTIVATED")) {
            triggerType = "brown";
            triggerName = "WHEN ACTIVATED";
        } else if(rA.toUpperCase().contains("GAME END")) {
            triggerType = "end";
            triggerName = "GAME END";
        } else {
            triggerType = "pink";
            triggerName = "WHEN";
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
                refinedAbility.add(cleaned);
            }
        }
    }
    
    ability = refinedAbility;
    players = new ArrayList<>();
    inputs = new HashMap<>();
    
    // Add to pink triggers if this is a pink ability
    if(triggerType != null && triggerType.equalsIgnoreCase("pink")) {
        if(pinkTriggers == null) {
            pinkTriggers = new ArrayList<>();
        }
        pinkTriggers.add(this);
    }
}
// don't touch this - Anirudh
public boolean execute()
{

    return false;
}

// Set game context before executing abilities
public static void setGameContext(Player current, ArrayList<Player> all, Panel panel) {
    currentPlayer = current;
    allPlayers = all;
    gamePanel = panel;
}

// Parse and execute the ability based on the parsed tokens
public void executeAbility() {
    if(ability == null || ability.isEmpty()) {
        System.out.println("No ability to execute");
        return;
    }
    
    System.out.println("Executing ability: " + rawAbility);
    System.out.println("Parsed tokens: " + ability);
    
    // Basic parsing logic - look for main action keywords
    for(int i = 0; i < ability.size(); i++) {
        String token = ability.get(i).toLowerCase();
        
        switch(token) {
            case "play":
                // Look for habitat keyword after "play"
                String habitat = findHabitat(i);
                if(habitat != null) {
                    play(habitat);
                }
                break;
                
            case "gain":
                // Parse gain parameters
                executeGain(i);
                break;
                
            case "draw":
                // Parse draw parameters
                executeDraw(i);
                break;
                
            case "lay":
                // Parse lay parameters
                executeLay(i);
                break;
                
            case "tuck":
                // Parse tuck parameters
                executeTuck(i);
                break;
                
            case "cache":
                // Parse cache parameters
                executeCache(i);
                break;
                
            case "discard":
                // Parse discard parameters
                executeDiscard(i);
                break;
                
            case "roll":
                // Parse roll parameters
                executeRoll(i);
                break;
        }
    }
}

// Helper method to find habitat in ability tokens
private String findHabitat(int startIndex) {
    for(int i = startIndex; i < ability.size(); i++) {
        String token = ability.get(i).toLowerCase();
        if(token.equals("forest") || token.equals("wetland") || token.equals("grassland")) {
            return token;
        }
        // Handle "this bird's habitat"
        if(token.equals("this") && i + 1 < ability.size() && 
           ability.get(i + 1).toLowerCase().contains("bird")) {
            // Get habitat from the bird that has this ability
            if(bird != null && bird.getHabitats() != null && bird.getHabitats().length > 0) {
                return bird.getHabitats()[0];
            }
        }
    }
    return null;
}

// Helper methods to execute specific abilities
private void executeGain(int startIndex) {
    // TODO: Parse amount, food type, and source from tokens
    String amount = "1";
    String food = "wild";
    String source = "supply";
    
    for(int i = startIndex; i < ability.size(); i++) {
        String token = ability.get(i);
        if(token.matches("\\d+")) {
            amount = token;
        }
        if(token.equalsIgnoreCase("rodent") || token.equalsIgnoreCase("fish") || 
           token.equalsIgnoreCase("seed") || token.equalsIgnoreCase("invertebrate") || 
           token.equalsIgnoreCase("fruit")) {
            food = token;
        }
    }
    
    gain(food, source, amount);
}

private void executeDraw(int startIndex) {
    int number = 1;
    String deckType = "deck";
    String habitat = null;
    
    for(int i = startIndex; i < ability.size(); i++) {
        String token = ability.get(i);
        if(token.matches("\\d+")) {
            number = Integer.parseInt(token);
        }
    }
    
    draw(deckType, number, habitat);
}

private void executeLay(int startIndex) {
    int number = 1;
    String nestType = "any";
    
    for(int i = startIndex; i < ability.size(); i++) {
        String token = ability.get(i);
        if(token.matches("\\d+")) {
            number = Integer.parseInt(token);
        }
        if(token.equalsIgnoreCase("ground") || token.equalsIgnoreCase("cavity") || 
           token.equalsIgnoreCase("bowl") || token.equalsIgnoreCase("platform")) {
            nestType = token;
        }
    }
    
    lay(nestType, number);
}

private void executeTuck(int startIndex) {
    String source = "hand";
    String amount = "1";
    
    for(int i = startIndex; i < ability.size(); i++) {
        String token = ability.get(i);
        if(token.matches("\\d+")) {
            amount = token;
        }
        if(token.equalsIgnoreCase("hand") || token.equalsIgnoreCase("deck")) {
            source = token;
        }
    }
    
    tuck(source, amount, bird);
}

private void executeCache(int startIndex) {
    String foodType = "wild";
    String amount = "1";
    
    for(int i = startIndex; i < ability.size(); i++) {
        String token = ability.get(i);
        if(token.matches("\\d+")) {
            amount = token;
        }
        if(token.equalsIgnoreCase("rodent") || token.equalsIgnoreCase("fish") || 
           token.equalsIgnoreCase("seed")) {
            foodType = token;
        }
    }
    
    cache(foodType, amount);
}

private void executeDiscard(int startIndex) {
    String itemType = "card";
    String amount = "1";
    
    for(int i = startIndex; i < ability.size(); i++) {
        String token = ability.get(i);
        if(token.matches("\\d+")) {
            amount = token;
        }
        if(token.equalsIgnoreCase("egg") || token.equalsIgnoreCase("card") || 
           token.equalsIgnoreCase("seed") || token.equalsIgnoreCase("fish") || 
           token.equalsIgnoreCase("rodent")) {
            itemType = token;
        }
    }
    
    discard(itemType, amount, bird);
}

private void executeRoll(int startIndex) {
    String foodType = "wild";
    
    for(int i = startIndex; i < ability.size(); i++) {
        String token = ability.get(i);
        if(token.equalsIgnoreCase("rodent") || token.equalsIgnoreCase("fish")) {
            foodType = token;
        }
    }
    
    roll(foodType);
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
    // TODO: Implement play bird logic
    // 1. Get current player
    // 2. Get player's hand
    // 3. Filter birds that can be placed in the specified habitat
    // 4. Allow player to select a bird from filtered list
    // 5. Place bird in the habitat (add to appropriate board location)
    // 6. Remove bird from hand
    // 7. Pay costs (food, eggs, cards)
    System.out.println("Playing a bird in habitat: " + habitat);
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
    // TODO: Implement gain food logic
    // Parse amount to determine how many food items
    int numFood = 0;
    if (amount.matches("\\d+")) {
        numFood = Integer.parseInt(amount);
    }
    
    // Handle different sources
    if (source.equalsIgnoreCase("birdfeeder")) {
        // Roll dice and add food from birdfeeder
        System.out.println("Gaining " + amount + " " + food + " from birdfeeder");
    } else if (source.equalsIgnoreCase("supply")) {
        // Take food directly from supply
        System.out.println("Gaining " + amount + " " + food + " from supply");
    }
    
    // Return what was gained for tracking
    return amount + " " + food;
}


/*
 * if a bird is the rightmost bird in its habitat, allow the bird to move to another one
 * @param void
 * @return void
 */
public static void right() {
    // TODO: Implement right bird logic
    // 1. Check if bird is rightmost in its habitat
    // 2. If yes, allow player to move it to another habitat
    // 3. Update bird location
    System.out.println("Checking if bird is rightmost and can move");
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
    // TODO: Implement keep card logic
    // 1. Determine if keeping bird or bonus card based on cardType
    // 2. Allow player to choose which card to keep from options provided
    // 3. Add chosen card to player's collection
    // 4. Discard unchosen cards
    System.out.println("Keeping a " + cardType + " card");
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
    // TODO: Implement look at cards logic
    // 1. Get the specified deck (bird or bonus)
    // 2. Reveal top 'number' cards from the deck
    // 3. Display cards to player without adding to hand
    // 4. Return cards to top of deck in same order
    System.out.println("Looking at " + number + " cards from " + deckType + " deck");
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
    // TODO: Implement roll dice logic
    // 1. Roll a die (simulate random food from birdfeeder)
    // 2. If foodType is specified, roll until that food type is obtained
    // 3. Return the food that was rolled
    System.out.println("Rolling for " + foodType);
    return foodType;
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
    // TODO: Implement draw cards logic
    // 1. Get the specified deck (bird or bonus)
    // 2. If number is "equal", count birds in specified habitat
    // 3. Draw the appropriate number of cards
    // 4. Add cards to player's hand
    System.out.println("Drawing " + number + " cards from " + deckType + " deck");
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
    // TODO: Implement lay eggs logic
    // 1. Determine target birds based on nestType keyword
    // 2. If "this", lay eggs on the bird with this ability
    // 3. If "another", lay on another bird with specified nest type
    // 4. If "any", allow player to choose any bird
    // 5. If "each", lay on each bird matching the nest type
    // 6. Add eggs to selected bird(s) up to their egg capacity
    System.out.println("Laying " + number + " eggs on birds with nest type: " + nestType);
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
public static void tuck(String source, String amount, Bird birdInput) {
    // TODO: Implement tuck cards logic
    // 1. Parse amount to determine how many cards to tuck
    // 2. Determine source (hand, deck, or specific card)
    // 3. Allow player to select cards from the source
    // 4. Place selected cards under the bird (tucked cards)
    // 5. Update bird's tucked card count for end-game scoring
    System.out.println("Tucking " + amount + " cards from " + source);
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
public static void discard(String itemType, String amount, Bird birdInput) {
    // TODO: Implement discard logic
    // 1. Parse amount to determine how many items to discard
    // 2. Determine item type (egg, card, food)
    // 3. Handle special keywords like "other", "this", "end"
    // 4. If "other", affect other players
    // 5. If "this", discard from the bird with this ability
    // 6. If "end", mark for discard at end of turn
    // 7. Remove items from player's resources
    System.out.println("Discarding " + amount + " " + itemType);
}


/*
 * sets the arrayList of players to execute the ability to contain all players
 * @param void
 * @return void
 * keywords:
 * selects: each player selects the option specified in the ability
 */
public static void players(String selects) {
    // TODO: Implement players logic
    // 1. Get all players in the game
    // 2. If "selects" keyword is present, each player will make a selection
    // 3. Add all player indices to the players ArrayList for ability execution
    System.out.println("Setting up ability for all players with option: " + selects);
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
    // TODO: Implement Each player logic
    // 1. Get all players in the game
    // 2. Allow current player to choose starting player
    // 3. Execute ability for each player in turn order
    // 4. Continue until all players have executed the ability
    System.out.println("Executing ability for each player");
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
    // TODO: Implement cache food logic
    // 1. Determine the food type to cache
    // 2. If "it", use food type from previous ability action
    // 3. Take food from supply or birdfeeder
    // 4. Place food on the bird card as cached food
    // 5. Cached food counts for end-game scoring
    System.out.println("Caching " + amount + " " + foodType + " on this bird");
    return foodType;
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

// Test method to demonstrate parsing
public static void main(String[] args) {
    // Test with your example abilities
    Ability ability1 = new Ability("WHEN PLAYED: Play an additional bird in your forest. Pay its normal cost.", 0);
    System.out.println("\n--- Test 1: Downy Woodpecker ---");
    System.out.println("Raw: " + ability1.getRawAbility());
    System.out.println("Trigger Type: " + ability1.getTriggerType());
    System.out.println("Trigger Name: " + ability1.getTriggerName());
    System.out.println("Parsed Tokens: " + ability1.getAbility());
    ability1.executeAbility();
    
    Ability ability2 = new Ability("WHEN ACTIVATED: If this bird is to the right of all other birds in its habitat, move it to another habitat.", 0);
    System.out.println("\n--- Test 2: Bewick's Wren ---");
    System.out.println("Raw: " + ability2.getRawAbility());
    System.out.println("Trigger Type: " + ability2.getTriggerType());
    System.out.println("Trigger Name: " + ability2.getTriggerName());
    System.out.println("Parsed Tokens: " + ability2.getAbility());
    ability2.executeAbility();
}
}

