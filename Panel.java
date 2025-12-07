import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;
import java.util.Collections;
import java.util.Random;
import java.util.List;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;


public class Panel extends JPanel implements MouseListener, MouseMotionListener{
public static ArrayList<Button> currentScreen = new ArrayList<Button>();
BufferedImage bg;
private int cardH = 278;
private int cardW = 140;
private int tokenSize = 150;
HashMap<String, BufferedImage> miscpics = new HashMap<String, BufferedImage>();
HashMap<String, Bird> birdcards = new HashMap<String, Bird>();
//ArrayList<Player> players = new ArrayList<Player>();
ArrayList<String> playerNames = new ArrayList<String>();
//Player player1;
private boolean namesEntered = false;
//private int currentPlayerIndex = 0;
private boolean startingComplete = false;
private ArrayList<JTextField> nameFields = new ArrayList<JTextField>();
private ArrayList<ItemRef> selected = new ArrayList<ItemRef>();
private ArrayList<Slot> selectionSlots = new ArrayList<Slot>();
private ArrayList<TokenItem> tokenItems = new ArrayList<TokenItem>();
private Bird hoverBird = null;
private String hoverTokenName = null;
private ItemRef hoverSlotItem = null;
private int placeholderCardCounter = 1;
private ArrayList<Bird> loadedBirds = new ArrayList<Bird>();
private ArrayList<Bird> birdDeck = new ArrayList<Bird>();
private ArrayList<Bird> faceUpBirds = new ArrayList<Bird>();
private Random rng = new Random();
public static ArrayList<Button> miscellaneousScreen = new ArrayList<Button>();
public Panel()
{
    setSize(1600,960);
   
   
    addMouseListener(this);
    addMouseMotionListener(this);


   
}
public void loadInitialImages()
{
    try
    {
        BufferedImage invertebretoken = ImageIO.read(Panel.class.getResource("/Images/invertebratetoken.png"));
        if(invertebretoken != null) {
            invertebretoken = trimTransparent(invertebretoken);
            miscpics.put("invertebratetoken", invertebretoken);
        }
        BufferedImage goButton = ImageIO.read(Panel.class.getResource("/Images/gobutton.png"));
        if(goButton != null) {
            goButton = trimTransparent(goButton);
            miscpics.put("gobutton", goButton);
        }
        BufferedImage wheattoken = ImageIO.read(Panel.class.getResource("/Images/wheattoken.png"));
        if(wheattoken != null) {
            wheattoken = trimTransparent(wheattoken);
            miscpics.put("wheattoken", wheattoken);
        }
        BufferedImage fishtoken = ImageIO.read(Panel.class.getResource("/Images/fishtoken.png"));
        if(fishtoken != null) {
            fishtoken = trimTransparent(fishtoken);
            miscpics.put("fishtoken", fishtoken);
        }
        BufferedImage foodtoken = ImageIO.read(Panel.class.getResource("/Images/foodtoken.png"));
        if(foodtoken != null) {
            foodtoken = trimTransparent(foodtoken);
            miscpics.put("foodtoken", foodtoken);
        }
        BufferedImage rattoken = ImageIO.read(Panel.class.getResource("/Images/rattoken.png"));
        if(rattoken != null) {
            rattoken = trimTransparent(rattoken);
            miscpics.put("rattoken", rattoken);
        BufferedImage board = ImageIO.read(Panel.class.getResource("/Images/Board.png"));
        miscpics.put("board", board);
        BufferedImage goBtnImg = ImageIO.read(Panel.class.getResource("/Images/gobutton.png"));
        miscpics.put("goBtnImg", goBtnImg);
        }
    }
    catch (Exception e)
    {
        System.out.println("ERROR loading images:");
        e.printStackTrace();
    }
}

public void loadBirds(List<Bird> birds) {
    loadedBirds.clear();
    if (birds != null) {
        loadedBirds.addAll(copyBirds(birds));
    }
    rebuildDeckFromLoaded();
}

private ArrayList<Bird> prepareStartingHand() {
    if (birdDeck.isEmpty()) {
        rebuildDeckFromLoaded();
    }
    ArrayList<Bird> hand = new ArrayList<Bird>();
    int needed = 5;
    for (int i = 0; i < needed; i++) {
        Bird drawn = drawTopOfDeck();
        if (drawn != null) {
            hand.add(cloneBird(drawn));
        }
    }
    return hand;
}

private ArrayList<Bird> copyBirds(List<Bird> birds) {
    ArrayList<Bird> copy = new ArrayList<Bird>();
    if (birds == null) return copy;
    for (Bird b : birds) {
        copy.add(cloneBird(b));
    }
    return copy;
}

private Bird cloneBird(Bird b) {
    if (b == null) return null;
    String[] habitats = b.getHabitats() != null ? Arrays.copyOf(b.getHabitats(), b.getHabitats().length) : new String[0];
    TreeMap<String, Integer> costs = b.getCosts() != null ? new TreeMap<String, Integer>(b.getCosts()) : new TreeMap<String, Integer>();
    TreeMap<String, Integer> cache = b.getCache() != null ? new TreeMap<String, Integer>(b.getCache()) : new TreeMap<String, Integer>();
    ArrayList<Bird> prey = b.getPrey() != null ? new ArrayList<Bird>(b.getPrey()) : new ArrayList<Bird>();
    return new Bird(
        b.getName(),
        b.getSciName(),
        b.getNestType(),
        habitats,
        b.getAbility(),
        costs,
        cache,
        b.getPoints(),
        b.getEggCount(),
        b.getEggCapacity(),
        b.getWingspan(),
        prey,
        b.hasBonusCard(),
        b.isFlocking(),
        b.getLocation(),
        null, // leave image null for now
        0,
        0
    );
}

private Bird makeBird(String name, String sci, String nest, String abilityText, String[] habitats, TreeMap<String,Integer> costs, int points, int eggCapacity, int wingspan) {
    Ability ability = abilityText != null ? new Ability(abilityText, null, null, null) : null;
    TreeMap<String,Integer> costCopy = costs != null ? new TreeMap<String,Integer>(costs) : new TreeMap<String,Integer>();
    return new Bird(name, sci, nest, habitats, ability, costCopy, new TreeMap<String,Integer>(), points, 0, eggCapacity, wingspan, new ArrayList<Bird>(), false, false, null, null, 0, 0);
}

private TreeMap<String,Integer> cost(Object... entries) {
    TreeMap<String,Integer> map = new TreeMap<String,Integer>();
    if (entries == null) return map;
    for (int i = 0; i + 1 < entries.length; i += 2) {
        Object key = entries[i];
        Object val = entries[i+1];
        if (key instanceof String && val instanceof Integer) {
            map.put((String)key, (Integer)val);
        }
    }
    return map;
}

private void rebuildDeckFromLoaded() {
    birdDeck.clear();
    faceUpBirds.clear();
    ArrayList<Bird> source = new ArrayList<Bird>();
    if (loadedBirds != null && !loadedBirds.isEmpty()) {
        source.addAll(copyBirds((List<Bird>)loadedBirds));
    } else {
        source.addAll(defaultBirds());
    }
    if (source.isEmpty()) return;
    Collections.shuffle(source, rng);
    birdDeck.addAll(source);
    ensureFaceUp();
}

private void ensureFaceUp() {
    while (faceUpBirds.size() < 3 && !birdDeck.isEmpty()) {
        faceUpBirds.add(drawTopOfDeck());
    }
}

private Bird drawTopOfDeck() {
    if (birdDeck.isEmpty()) return null;
    return birdDeck.remove(0);
}

private void activateHabitatAbilities(String habitat) {
    if (habitat == null) return;
    HashMap<String, ArrayList<Spot>> board = Player.getCurrentPlayerBoard();
    if (board == null) return;
    ArrayList<Spot> spots = board.get(capitalizeHabitat(habitat));
    if (spots == null) return;
    // Activate birds right-to-left (mimicking action cube movement)
    for (int i = spots.size() - 1; i >= 0; i--) {
        Spot s = spots.get(i);
        if (s == null) continue;
        Bird b = s.getBird();
        if (b != null && b.getAbility() != null) {
            b.getAbility().execute();
        }
    }
}

private String capitalizeHabitat(String habitat) {
    if (habitat == null || habitat.isEmpty()) return habitat;
    return habitat.substring(0,1).toUpperCase() + habitat.substring(1).toLowerCase();
}

private Bird selectBirdFromMarket(String title) {
    ensureFaceUp();
    ArrayList<String> options = new ArrayList<String>();
    for (Bird b : faceUpBirds) {
        options.add(b.getName());
    }
    options.add("Draw from deck (face-down)");
    String[] optionArr = options.toArray(new String[0]);
    String choice = (String) JOptionPane.showInputDialog(this, title, "Draw Bird", JOptionPane.PLAIN_MESSAGE, null, optionArr, optionArr[0]);
    if (choice == null) return null;
    int idx = options.indexOf(choice);
    if (idx >= 0 && idx < faceUpBirds.size()) {
        Bird chosen = faceUpBirds.remove(idx);
        Bird replacement = drawTopOfDeck();
        if (replacement != null) {
            faceUpBirds.add(replacement);
        }
        return chosen;
    }
    // face-down draw
    return drawTopOfDeck();
}

private ArrayList<Bird> defaultBirds() {
    ArrayList<Bird> list = new ArrayList<Bird>();

    // Play an additional bird
    list.add(makeBird("Downy Woodpecker","Picoides pubescens","Cavity","WHEN PLAYED: Play an additional bird in your forest. Pay its normal cost.",new String[]{"forest"},cost("invertebrate",1,"seed",1,"fruit",1),3,2,30));
    list.add(makeBird("Eastern Bluebird","Sialia sialis","Cavity","WHEN PLAYED: Play an additional bird in your grassland. Pay its normal cost.",new String[]{"grassland"},cost("invertebrate",1,"fruit",1),4,5,33));
    list.add(makeBird("Great Blue Heron","Ardea herodias","Platform","WHEN PLAYED: Play an additional bird in your wetland. Pay its normal cost.",new String[]{"wetland"},cost("invertebrate",1,"fish",1),5,2,183));
    list.add(makeBird("Great Egret","Ardea alba","Platform","WHEN PLAYED: Play an additional bird in your wetland. Pay its normal cost.",new String[]{"wetland"},cost("fish",2,"rodent",1),7,3,130));
    list.add(makeBird("House Wren","Troglodytes aedon","Cavity","WHEN PLAYED: Play an additional bird in this bird's habitat. Pay its normal cost.",new String[]{"forest","grassland"},cost("invertebrate",1),1,5,15));
    list.add(makeBird("Mountain Bluebird","Sialia currucoides","Cavity","WHEN PLAYED: Play an additional bird in your grassland. Pay its normal cost.",new String[]{"grassland"},cost("invertebrate",1,"fruit",1),4,5,36));
    list.add(makeBird("Red-Eyed Vireo","Vireo olivaceus","Wild","WHEN PLAYED: Play an additional bird in your forest. Pay its normal cost.",new String[]{"forest"},cost("invertebrate",1,"fruit",1),3,2,25));
    list.add(makeBird("Ruby-Crowned Kinglet","Regulus calendula","Bowl","WHEN PLAYED: Play an additional bird in your forest. Pay its normal cost.",new String[]{"forest"},cost("invertebrate",1,"seed",1,"fruit",1),2,3,20));
    list.add(makeBird("Savannah Sparrow","Passerculus sandwichensis","Ground","WHEN PLAYED: Play an additional bird in your grassland. Pay its normal cost.",new String[]{"grassland"},cost("invertebrate",1,"seed",1),2,3,18));
    list.add(makeBird("Tufted Titmouse","Baeolophus bicolor","Cavity","WHEN PLAYED: Play an additional bird in your forest. Pay its normal cost.",new String[]{"forest"},cost("invertebrate",1,"seed",1,"fruit",1),2,3,25));

    // Rightmost move
    list.add(makeBird("Bewick's Wren","Thryomanes bewickii","Cavity","WHEN ACTIVATED: If this bird is to the right of all other birds in its habitat, move it to another habitat.",new String[]{"forest","wetland","grassland"},cost("invertebrate",2,"seed",1),4,2,18));
    list.add(makeBird("Blue Grosbeak","Passerina caerulea","Bowl","WHEN ACTIVATED: If this bird is to the right of all other birds in its habitat, move it to another habitat.",new String[]{"forest","grassland","wetland"},cost("invertebrate",1,"seed",2),4,3,28));
    list.add(makeBird("Chimney Swift","Chaetura pelagica","Wild","WHEN ACTIVATED: If this bird is to the right of all other birds in its habitat, move it to another habitat.",new String[]{"forest","grassland","wetland"},cost("invertebrate",2),3,2,36));
    list.add(makeBird("Common Nighthawk","Chordeiles minor","Ground","WHEN ACTIVATED: If this bird is to the right of all other birds in its habitat, move it to another habitat.",new String[]{"forest","grassland","wetland"},cost("invertebrate",2),3,2,56));
    list.add(makeBird("Lincoln's Sparrow","Melospiza lincolnii","Ground","WHEN ACTIVATED: If this bird is to the right of all other birds in its habitat, move it to another habitat.",new String[]{"wetland","grassland"},cost("invertebrate",1,"seed",1),3,2,20));
    list.add(makeBird("Yellow-Breasted Chat","Icteria virens","Bowl","WHEN ACTIVATED: If this bird is to the right of all other birds in its habitat, move it to another habitat.",new String[]{"wetland","grassland","forest"},cost("invertebrate",1,"fruit",2),5,3,25));
    list.add(makeBird("Song Sparrow","Melospiza melodia","Bowl","WHEN ACTIVATED: If this bird is to the right of all other birds in its habitat, move it to another habitat.",new String[]{"forest","grassland","wetland"},cost("invertebrate",1,"seed",1,"fruit",1),0,5,20));
    list.add(makeBird("White-Crowned Sparrow","Zonotrichia leucophrys","Ground","WHEN ACTIVATED: If this bird is to the right of all other birds in its habitat, move it to another habitat.",new String[]{"wetland","grassland","forest"},cost("invertebrate",1,"seed",1),2,5,25));

    // Keep bonus card
    list.add(makeBird("Atlantic Puffin","Fratercula arctica","Wild","WHEN PLAYED: Draw 2 new bonus cards and keep 1.",new String[]{"wetland"},cost("fish",3),8,1,53));
    list.add(makeBird("Bell's Vireo","Vireo bellii","Wild","WHEN PLAYED: Draw 2 new bonus cards and keep 1.",new String[]{"grassland","forest"},cost("invertebrate",2),4,2,18));
    list.add(makeBird("California Condor","Gymnogyps californianus","Ground","WHEN PLAYED: Draw 2 new bonus cards and keep 1.",new String[]{"forest","grassland","wetland"},cost("no-food",0),1,2,277));
    list.add(makeBird("Cassin's Finch","Haemorhous cassinii","Bowl","WHEN PLAYED: Draw 2 new bonus cards and keep 1.",new String[]{"forest"},cost("seed",1,"fruit",1),4,3,30));
    list.add(makeBird("Cerulean Warbler","Setophaga cerulea","Bowl","WHEN PLAYED: Draw 2 new bonus cards and keep 1.",new String[]{"forest"},cost("invertebrate",1,"seed",1),4,2,20));
    list.add(makeBird("Chestnut-Collared Longspur","Calcarius ornatus","Ground","WHEN PLAYED: Draw 2 new bonus cards and keep 1.",new String[]{"grassland"},cost("invertebrate",1,"seed",2),5,4,25));
    list.add(makeBird("Greater Prairie Chicken","Tympanuchus cupido","Ground","WHEN PLAYED: Draw 2 new bonus cards and keep 1.",new String[]{"grassland"},cost("invertebrate",1,"seed",2),5,4,71));
    list.add(makeBird("King Rail","Rallus elegans","Platform","WHEN PLAYED: Draw 2 new bonus cards and keep 1.",new String[]{"wetland"},cost("invertebrate",1,"fish",1,"wild",1),4,6,51));
    list.add(makeBird("Painted Bunting","Passerina ciris","Bowl","WHEN PLAYED: Draw 2 new bonus cards and keep 1.",new String[]{"grassland"},cost("invertebrate",1,"seed",2),5,4,23));
    list.add(makeBird("Red-Cockaded Woodpecker","Picoides borealis","Cavity","WHEN PLAYED: Draw 2 new bonus cards and keep 1.",new String[]{"forest"},cost("invertebrate",1,"fruit",1),4,2,36));
    list.add(makeBird("Roseate Spoonbill","Platalea ajaja","Platform","WHEN PLAYED: Draw 2 new bonus cards and keep 1.",new String[]{"wetland"},cost("invertebrate",1,"seed",1,"fish",1),6,2,127));
    list.add(makeBird("Spotted Owl","Strix occidentalis","Cavity","WHEN PLAYED: Draw 2 new bonus cards and keep 1.",new String[]{"forest"},cost("rodent",1),5,2,102));
    list.add(makeBird("Sprague's Pipit","Anthus spragueii","Ground","WHEN PLAYED: Draw 2 new bonus cards and keep 1.",new String[]{"grassland"},cost("invertebrate",1,"seed",1),3,3,25));
    list.add(makeBird("Whooping Crane","Grus americana","Ground","WHEN PLAYED: Draw 2 new bonus cards and keep 1.",new String[]{"wetland"},cost("wild",3),6,1,221));
    list.add(makeBird("Wood Stork","Mycteria americana","Platform","WHEN PLAYED: Draw 2 new bonus cards and keep 1.",new String[]{"wetland"},cost("fish",1,"rodent",1,"wild",1),6,2,155));

    // Look / tuck if small wingspan
    list.add(makeBird("Barred Owl","Strix varia","Cavity","WHEN ACTIVATED: Look at a card from the deck. If less than 75cm, tuck it behind this bird. If not, discard it.",new String[]{"forest"},cost("rodent",1),3,2,107));
    list.add(makeBird("Greater Roadrunner","Geococcyx californianus","Platform","WHEN ACTIVATED: Look at a card from the deck. If less than 50cm, tuck it behind this bird. If not, discard it.",new String[]{"grassland"},cost("invertebrate",1,"rodent",1,"wild",1),7,2,56));
    list.add(makeBird("Cooper's Hawk","Accipiter cooperii","Platform","WHEN ACTIVATED: Look at a card from the deck. If less than 75cm, tuck it behind this bird. If not, discard it.",new String[]{"forest"},cost("invertebrate",1,"rodent",1),3,2,79));
    list.add(makeBird("Golden Eagle","Aquila chrysaetos","Platform","WHEN ACTIVATED: Look at a card from the deck. If less than 100cm, tuck it behind this bird. If not, discard it.",new String[]{"grassland"},cost("rodent",3),8,1,201));
    list.add(makeBird("Great Horned Owl","Bubo virginianus","Platform","WHEN ACTIVATED: Look at a card from the deck. If less than 100cm, tuck it behind this bird. If not, discard it.",new String[]{"forest"},cost("rodent",3),8,2,112));
    list.add(makeBird("Northern Harrier","Circus cyaneus","Platform","WHEN ACTIVATED: Look at a card from the deck. If less than 75cm, tuck it behind this bird. If not, discard it.",new String[]{"wetland","grassland"},cost("rodent",1),3,2,109));
    list.add(makeBird("Peregrine Falcon","Falco peregrinus","Platform","WHEN ACTIVATED: Look at a card from the deck. If less than 100cm, tuck it behind this bird. If not, discard it.",new String[]{"wetland","grassland"},cost("rodent",2,"wild",1),5,2,104));
    list.add(makeBird("Red-Shouldered Hawk","Buteo lineatus","Platform","WHEN ACTIVATED: Look at a card from the deck. If less than 75cm, tuck it behind this bird. If not, discard it.",new String[]{"forest"},cost("rodent",1),3,2,102));
    list.add(makeBird("Red-Tailed Hawk","Buteo jamaicensis","Platform","WHEN ACTIVATED: Look at a card from the deck. If less than 75cm, tuck it behind this bird. If not, discard it.",new String[]{"grassland","wetland"},cost("rodent",2),5,2,124));
    list.add(makeBird("Swainson's Hawk","Buteo swainsoni","Platform","WHEN ACTIVATED: Look at a card from the deck. If less than 75cm, tuck it behind this bird. If not, discard it.",new String[]{"grassland"},cost("invertebrate",1,"rodent",1),5,2,130));

    // Roll dice cache
    list.add(makeBird("American Kestrel","Falco sparverius","Cavity","WHEN ACTIVATED: Roll all dice not in birdfeeder. If any are rodent, cache 1 rodent from the supply on this bird.",new String[]{"grassland"},cost("invertebrate",1,"rodent",1),5,3,56));
    list.add(makeBird("Barn Owl","Tyto alba","Cavity","WHEN ACTIVATED: Roll all dice not in birdfeeder. If any are rodent, cache 1 rodent from the supply on this bird.",new String[]{"forest","wetland","grassland"},cost("rodent",2),5,4,107));
    list.add(makeBird("Anhinga","Anhinga anhinga","Platform","WHEN ACTIVATED: Roll all dice not in birdfeeder. If any are fish, cache 1 fish from the supply on this bird.",new String[]{"wetland"},cost("fish",3),6,2,114));
    list.add(makeBird("Black Skimmer","Rynchops niger","Ground","WHEN ACTIVATED: Roll all dice not in birdfeeder. If any are fish, cache 1 fish from the supply on this bird.",new String[]{"wetland"},cost("fish",2),6,2,112));
    list.add(makeBird("Burrowing Owl","Athene cunicularia","Wild","WHEN ACTIVATED: Roll all dice not in birdfeeder. If any are rodent, cache 1 rodent from the supply on this bird.",new String[]{"grassland"},cost("invertebrate",1,"rodent",1),5,4,53));
    list.add(makeBird("Common Merganser","Mergus merganser","Cavity","WHEN ACTIVATED: Roll all dice not in birdfeeder. If any are fish, cache 1 fish from the supply on this bird.",new String[]{"wetland"},cost("fish",1,"wild",1),5,4,86));
    list.add(makeBird("Eastern Screech Owl","Megascops asio","Cavity","WHEN ACTIVATED: Roll all dice not in birdfeeder. If any are rodent, cache 1 rodent from the supply on this bird.",new String[]{"forest"},cost("invertebrate",1,"rodent",1),4,2,51));
    list.add(makeBird("Ferruginous Hawk","Buteo regalis","Platform","WHEN ACTIVATED: Roll all dice not in birdfeeder. If any are rodent, cache 1 rodent from the supply on this bird.",new String[]{"grassland"},cost("rodent",2),6,2,142));
    list.add(makeBird("Mississippi Kite","Ictinia mississippiensis","Platform","WHEN ACTIVATED: Roll all dice not in birdfeeder. If any are rodent, cache 1 rodent from the supply on this bird.",new String[]{"grassland"},cost("invertebrate",1,"rodent",1),4,1,79));
    list.add(makeBird("Snowy Egret","Egretta thula","Platform","WHEN ACTIVATED: Roll all dice not in birdfeeder. If any are fish, cache 1 fish from the supply on this bird.",new String[]{"wetland"},cost("invertebrate",1,"fish",1),4,2,104));
    list.add(makeBird("White-Faced Ibis","Plegadis chihi","Platform","WHEN ACTIVATED: Roll all dice not in birdfeeder. If any are fish, cache 1 fish from the supply on this bird.",new String[]{"wetland"},cost("invertebrate",2,"fish",1),8,2,91));
    list.add(makeBird("Willet","Tringa semipalmata","Ground","WHEN ACTIVATED: Roll all dice not in birdfeeder. If any are fish, cache 1 fish from the supply on this bird.",new String[]{"wetland"},cost("invertebrate",1,"fish",1),4,2,66));

    // Discard for resources/cards
    list.add(makeBird("American Crow","Corvus brachyrhynchos","Platform","WHEN ACTIVATED: Discard 1 egg from any of your other birds to gain 1 wild from the supply.",new String[]{"forest","grassland","wetland"},cost("wild",1),4,2,99));
    list.add(makeBird("Black-Crowned Night-Heron","Nycticorax nycticorax","Platform","WHEN ACTIVATED: Discard 1 egg from any of your other birds to gain 1 wild from the supply.",new String[]{"wetland"},cost("invertebrate",1,"fish",1,"rodent",1),9,2,112));
    list.add(makeBird("Black Tern","Chlidonias niger","Wild","WHEN ACTIVATED: Draw 1 card. If you do, discard 1 card from your hand at the end of your turn.",new String[]{"wetland"},cost("invertebrate",1,"fish",1),4,2,61));
    list.add(makeBird("Black-Bellied Whistling Duck","Dendrocygna autumnalis","Cavity","WHEN ACTIVATED: Discard 1 seed to tuck 2 cards from the deck behind this bird.",new String[]{"wetland"},cost("seed",2),2,5,76));
    list.add(makeBird("Killdeer","Charadrius vociferus","Ground","WHEN ACTIVATED: Discard 1 egg to draw 2 cards.",new String[]{"wetland","grassland"},cost("invertebrate",1,"seed",1),1,3,46));
    list.add(makeBird("Double-Crested Cormorant","Phalacrocorax auritus","Platform","WHEN ACTIVATED: Discard 1 fish to tuck 2 cards from the deck behind this bird.",new String[]{"wetland"},cost("fish",1,"wild",1),3,3,132));
    list.add(makeBird("Franklin's Gull","Leucophaeus pipixcan","Wild","WHEN ACTIVATED: Discard 1 egg to draw 2 cards.",new String[]{"wetland","grassland"},cost("fish",1,"wild",1),3,2,91));
    list.add(makeBird("Fish Crow","Corvus ossifragus","Platform","WHEN ACTIVATED: Discard 1 egg from any of your other birds to gain 1 wild from the supply.",new String[]{"wetland","grassland","forest"},cost("fish",1,"wild",1),6,2,91));
    list.add(makeBird("Sandhill Crane","Antigone canadensis","Ground","WHEN ACTIVATED: Discard 1 seed to tuck 2 cards from the deck behind this bird.",new String[]{"wetland","grassland"},cost("seed",2,"wild",1),5,1,196));

    // Lay eggs / on-turn/once-between-turns
    list.add(makeBird("American Avocet","Recurvirostra americana","Ground","ONCE BETWEEN TURNS: When another player takes the 'lay eggs' action, lay 1 egg on another bird with a ground nest.",new String[]{"wetland"},cost("invertebrate",2,"seed",1),6,2,79));
    list.add(makeBird("Ash-Throated Flycatcher","Myiarchus cinerascens","Cavity","WHEN PLAYED: Lay 1 egg on each of your birds with a cavity nest.",new String[]{"grassland"},cost("invertebrate",1,"fruit",1),4,4,30));
    list.add(makeBird("Baird's Sparrow","Ammodramus bairdii","Ground","WHEN ACTIVATED: Lay 1 egg on any bird.",new String[]{"grassland"},cost("invertebrate",1,"seed",1),3,2,23));
    list.add(makeBird("Bobolink","Dolichonyx oryzivorus","Ground","WHEN PLAYED: Lay 1 egg on each of your birds with a ground nest.",new String[]{"grassland"},cost("invertebrate",1,"seed",2),4,3,30));
    list.add(makeBird("Bronzed Cowbird","Molothrus aeneus","Bowl","ONCE BETWEEN TURNS: When another player takes the 'lay eggs' action, lay 1 egg on a bird with a bowl nest.",new String[]{"grassland"},cost("invertebrate",1,"seed",1),5,3,36));
    list.add(makeBird("Brown-Headed Cowbird","Molothrus ater","Bowl","ONCE BETWEEN TURNS: When another player takes the 'lay eggs' action, lay 1 egg on a bird with a bowl nest.",new String[]{"grassland"},cost("seed",1),3,2,30));
    list.add(makeBird("California Quail","Callipepla californica","Ground","WHEN ACTIVATED: Lay 1 egg on this bird.",new String[]{"grassland","forest"},cost("invertebrate",1,"seed",2),3,6,36));
    list.add(makeBird("Cassin's Sparrow","Peucaea cassinii","Ground","WHEN ACTIVATED: Lay 1 egg on any bird.",new String[]{"grassland"},cost("invertebrate",1,"seed",1),3,2,20));
    list.add(makeBird("Chipping Sparrow","Spizella passerina","Bowl","WHEN ACTIVATED: Lay 1 egg on any bird.",new String[]{"grassland","forest"},cost("invertebrate",1,"seed",1),1,3,23));
    list.add(makeBird("Grasshopper Sparrow","Ammodramus savannarum","Ground","WHEN ACTIVATED: Lay 1 egg on any bird.",new String[]{"grassland"},cost("invertebrate",1,"seed",1),2,2,20));
    list.add(makeBird("Inca Dove","Columbina inca","Platform","WHEN PLAYED: Lay 1 egg on each of your birds with a platform nest.",new String[]{"grassland"},cost("seed",2),2,4,28));
    list.add(makeBird("Lazuli Bunting","Passerina amoena","Bowl","WHEN ACTIVATED: All players lay 1 egg on any 1 bowl bird. You may lay 1 egg on 1 additional bowl bird.",new String[]{"grassland"},cost("invertebrate",1,"seed",1,"fruit",1),4,4,23));
    list.add(makeBird("Mourning Dove","Zenaida macroura","Platform","WHEN ACTIVATED: Lay 1 egg on this bird.",new String[]{"grassland","wetland"},cost("seed",1),0,5,46));
    list.add(makeBird("Northern Bobwhite","Colinus virginianus","Ground","WHEN ACTIVATED: Lay 1 egg on this bird.",new String[]{"grassland"},cost("seed",3),5,6,33));
    list.add(makeBird("Pileated Woodpecker","Dryocopus pileatus","Cavity","WHEN ACTIVATED: All players lay 1 egg on any 1 cavity bird. You may lay 1 egg on 1 additional cavity bird.",new String[]{"forest"},cost("invertebrate",1,"fruit",1),4,2,74));
    list.add(makeBird("Western Meadowlark","Sturnella neglecta","Ground","WHEN ACTIVATED: All players lay 1 egg on any 1 ground bird. You may lay 1 egg on 1 additional ground bird.",new String[]{"grassland"},cost("invertebrate",1,"seed",1),2,4,38));
    list.add(makeBird("Say's Phoebe","Sayornis saya","Bowl","WHEN PLAYED: Lay 1 egg on each of your birds with a bowl nest.",new String[]{"grassland"},cost("invertebrate",3),5,3,33));

    // Tuck
    list.add(makeBird("American Coot","Fulica americana","Platform","WHEN ACTIVATED: Tuck 1 card from your hand behind this bird. If you do, draw 1 card.",new String[]{"wetland"},cost("seed",1,"wild",1),3,5,61));
    list.add(makeBird("American Robin","Turdus migratorius","Bowl","WHEN ACTIVATED: Tuck 1 card from your hand behind this bird. If you do, draw 1 card.",new String[]{"forest","grassland"},cost("invertebrate",1,"fruit",1),1,4,43));
    list.add(makeBird("American White Pelican","Pelecanus erythrorhynchos","Ground","WHEN ACTIVATED: Discard 1 fish to tuck 2 cards from the deck behind this bird.",new String[]{"wetland"},cost("fish",2),5,1,274));
    list.add(makeBird("Barn Swallow","Hirundo rustica","Wild","WHEN ACTIVATED: Tuck 1 card from your hand behind this bird. If you do, draw 1 card.",new String[]{"grassland"},cost("invertebrate",1),1,3,38));
    list.add(makeBird("Black-Bellied Whistling Duck","Dendrocygna autumnalis","Cavity","WHEN ACTIVATED: Discard 1 seed to tuck 2 cards from the deck behind this bird.",new String[]{"wetland"},cost("seed",2),2,5,76));
    list.add(makeBird("Horned Lark","Eremophila alpestris","Ground","ONCE BETWEEN TURNS: When another player plays a bird in their grassland, tuck 1 card from your hand behind this bird.",new String[]{"grassland"},cost("invertebrate",1,"seed",1),5,4,30));
    list.add(makeBird("House Finch","Haemorhous mexicanus","Bowl","WHEN ACTIVATED: Tuck 1 card from your hand behind this bird. If you do, draw 1 card.",new String[]{"forest","grassland"},cost("seed",1,"fruit",1),3,6,25));
    list.add(makeBird("Pine Siskin","Spinus pinus","Bowl","WHEN ACTIVATED: Tuck 1 card from your hand behind this bird. If you do, gain 1 seed from the supply.",new String[]{"forest"},cost("seed",2),3,2,23));
    list.add(makeBird("Purple Martin","Progne subis","Cavity","WHEN ACTIVATED: Tuck 1 card from your hand behind this bird. If you do, draw 1 card.",new String[]{"wetland","grassland"},cost("invertebrate",1),2,3,46));
    list.add(makeBird("Pygmy Nuthatch","Sitta pygmaea","Cavity","WHEN ACTIVATED: Tuck 1 card from your hand behind this bird. If you do, gain 1 invertebrate or seed from the supply.",new String[]{"forest"},cost("invertebrate",1,"seed",1),2,4,20));
    list.add(makeBird("Yellow-Headed Blackbird","Xanthocephalus xanthocephalus","Bowl","WHEN ACTIVATED: Tuck 1 card from your hand behind this bird. If you do, you may also lay 1 egg on this bird.",new String[]{"wetland","grassland"},cost("invertebrate",1,"seed",1),4,3,38));
    list.add(makeBird("Yellow-Rumped Warbler","Setophaga coronata","Bowl","WHEN ACTIVATED: Tuck 1 card from your hand behind this bird. If you do, draw 1 card.",new String[]{"forest"},cost("invertebrate",1,"seed",1,"fruit",1),1,4,23));

    // Draw cards
    list.add(makeBird("American Bittern","Botaurus lentiginosus","Platform","WHEN ACTIVATED: Player(s) with the fewest birds in their wetland draw 1 card.",new String[]{"wetland"},cost("invertebrate",1,"fish",1),7,2,107));
    list.add(makeBird("American Oystercatcher","Haematopus palliatus","Ground","WHEN PLAYED: Draw cards equal to the number of players +1. Each player selects 1, you keep the extra.",new String[]{"wetland"},cost("invertebrate",2),5,2,81));
    list.add(makeBird("Black-Necked Stilt","Himantopus mexicanus","Ground","WHEN PLAYED: Draw 2 cards.",new String[]{"wetland"},cost("invertebrate",2),4,2,74));
    list.add(makeBird("Black Tern","Chlidonias niger","Wild","WHEN ACTIVATED: Draw 1 card. If you do, discard 1 card at end of turn.",new String[]{"wetland"},cost("invertebrate",1,"fish",1),4,2,61));
    list.add(makeBird("Mallard","Anas platyrhynchos","Ground","WHEN ACTIVATED: Draw 1 card.",new String[]{"wetland"},cost("invertebrate",1,"seed",1),0,3,89));
    list.add(makeBird("Killdeer","Charadrius vociferus","Ground","WHEN ACTIVATED: Discard 1 egg to draw 2 cards.",new String[]{"wetland","grassland"},cost("invertebrate",1,"seed",1),1,3,46));
    list.add(makeBird("Northern Shoveler","Spatula clypeata","Ground","WHEN ACTIVATED: All players draw 1 card from the deck.",new String[]{"wetland"},cost("invertebrate",1,"seed",2),7,4,76));
    list.add(makeBird("Purple Gallinule","Porphyrio martinicus","Platform","WHEN ACTIVATED: All players draw 1 card from the deck.",new String[]{"wetland"},cost("seed",1,"fruit",1,"wild",1),7,4,56));
    list.add(makeBird("Pied-Billed Grebe","Podilymbus podiceps","Platform","WHEN ACTIVATED: Draw 2 cards. If you do, discard 1 card from your hand at the end of your turn.",new String[]{"wetland"},cost("invertebrate",1,"fish",1),0,4,41));
    list.add(makeBird("Ruddy Duck","Oxyura jamaicensis","Platform","WHEN ACTIVATED: Draw 2 cards. If you do, discard 1 card from your hand at the end of your turn.",new String[]{"wetland"},cost("invertebrate",1,"seed",1),0,5,48));
    list.add(makeBird("Wood Duck","Aix sponsa","Cavity","WHEN ACTIVATED: Draw 2 cards. If you do, discard 1 card from your hand at the end of your turn.",new String[]{"wetland","forest"},cost("seed",2,"fruit",1),4,4,76));

    // Trade
    list.add(makeBird("Green Heron","Butorides virescens","Platform","WHEN ACTIVATED: Trade 1 wild for any other type from the supply.",new String[]{"wetland"},cost("invertebrate",1,"fish",1),4,3,66));

    // All players gain
    list.add(makeBird("Baltimore Oriole","Icterus galbula","Wild","WHEN ACTIVATED: All players gain 1 fruit from the supply.",new String[]{"forest"},cost("invertebrate",1,"fruit",2),9,2,30));
    list.add(makeBird("Eastern Phoebe","Sayornis phoebe","Wild","WHEN ACTIVATED: All players gain 1 invertebrate from the supply.",new String[]{"forest","grassland","wetland"},cost("invertebrate",1),3,4,28));
    list.add(makeBird("Osprey","Pandion haliaetus","Platform","WHEN ACTIVATED: All players gain 1 fish from the supply.",new String[]{"wetland"},cost("fish",1),5,2,160));
    list.add(makeBird("Red Crossbill","Loxia curvirostra","Bowl","WHEN ACTIVATED: All players gain 1 seed from the supply.",new String[]{"forest"},cost("seed",2),6,2,28));
    list.add(makeBird("Scissor-Tailed Flycatcher","Tyrannus forficatus","Bowl","WHEN ACTIVATED: All players gain 1 invertebrate from the supply.",new String[]{"grassland"},cost("invertebrate",2,"fruit",1),8,2,38));
    list.add(makeBird("Spotted Sandpiper","Actitis macularius","Ground","WHEN ACTIVATED: All players draw 1 card from the deck.",new String[]{"wetland"},cost("invertebrate",1),5,2,38));
    list.add(makeBird("Wilson's Snipe","Gallinago delicata","Ground","WHEN ACTIVATED: All players draw 1 card from the deck.",new String[]{"wetland"},cost("invertebrate",1),5,2,41));
    list.add(makeBird("Western Meadowlark","Sturnella neglecta","Ground","WHEN ACTIVATED: All players lay 1 egg on any 1 ground bird. You may lay 1 egg on 1 additional ground bird.",new String[]{"grassland"},cost("invertebrate",1,"seed",1),2,4,38));

    // Gain food
    list.add(makeBird("Acorn Woodpecker","Melanerpes formicivorus","Cavity","WHEN ACTIVATED: Gain 1 seed from the birdfeeder, if available. You may cache it on this bird.",new String[]{"forest"},cost("seed",3),5,4,46));
    list.add(makeBird("American Goldfinch","Spinus tristis","Bowl","WHEN PLAYED: Gain 3 seed from the supply.",new String[]{"grassland"},cost("seed",2),3,3,23));
    list.add(makeBird("Bald Eagle","Haliaeetus leucocephalus","Platform","WHEN PLAYED: Gain all fish that are in the birdfeeder.",new String[]{"wetland"},cost("fish",2,"rodent",1),9,2,203));
    list.add(makeBird("Blue Jay","Cyanocitta cristata","Bowl","WHEN ACTIVATED: Gain 1 seed from the birdfeeder, if available. You may cache it on this bird.",new String[]{"forest"},cost("seed",1,"wild",1),3,2,41));
    list.add(makeBird("Baltimore Oriole","Icterus galbula","Wild","WHEN ACTIVATED: All players gain 1 fruit from the supply.",new String[]{"forest"},cost("invertebrate",1,"fruit",2),9,2,30));
    list.add(makeBird("Indigo Bunting","Passerina cyanea","Bowl","WHEN ACTIVATED: Gain 1 invertebrate or fruit from the birdfeeder, if available.",new String[]{"grassland","forest"},cost("invertebrate",1,"seed",1,"fruit",1),5,3,20));
    list.add(makeBird("Northern Cardinal","Cardinalis cardinalis","Bowl","WHEN ACTIVATED: Gain 1 fruit from the supply.",new String[]{"forest"},cost("seed",1,"fruit",1),3,5,30));
    list.add(makeBird("Red-Bellied Woodpecker","Melanerpes carolinus","Cavity","WHEN ACTIVATED: Gain 1 seed from the birdfeeder, if available. You may cache it on this bird.",new String[]{"forest"},cost("invertebrate",1,"seed",1),1,3,41));
    list.add(makeBird("Rose-Breasted Grosbeak","Pheucticus ludovicianus","Bowl","WHEN ACTIVATED: Gain 1 seed or fruit from the birdfeeder, if available.",new String[]{"forest"},cost("invertebrate",1,"seed",1,"fruit",1),6,3,33));
    list.add(makeBird("Western Tanager","Piranga ludoviciana","Bowl","WHEN ACTIVATED: Gain 1 invertebrate or fruit from the birdfeeder, if available.",new String[]{"forest"},cost("invertebrate",2,"fruit",1),6,2,30));
    list.add(makeBird("Yellow-Bellied Sapsucker","Sphyrapicus varius","Cavity","WHEN ACTIVATED: Gain 1 invertebrate from the supply.",new String[]{"forest"},cost("invertebrate",1),3,3,41));
    list.add(makeBird("Spotted Towhee","Pipilo maculatus","Ground","WHEN ACTIVATED: Gain 1 seed from the supply.",new String[]{"grassland"},cost("invertebrate",1,"seed",1),0,4,28));
    list.add(makeBird("Steller's Jay","Cyanocitta stelleri","Bowl","WHEN ACTIVATED: Gain 1 seed from the birdfeeder, if available. You may cache it on this bird.",new String[]{"forest"},cost("seed",2,"wild",1),5,2,48));

    return list;
}
    public void realStartingScreen(Graphics g)
    {
        int panelWidth = getWidth();
        int panelHeight = getHeight();
       
        // Draw big title at top
        g.setColor(new Color(50, 50, 50));
        g.setFont(new Font("Arial", Font.BOLD, 48));
        FontMetrics fm = g.getFontMetrics();
        String title = "WINGSPAN";
        int titleWidth = fm.stringWidth(title);
        g.drawString(title, (panelWidth - titleWidth) / 2, 100);
       
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        String subtitle = "Enter Player Names";
        int subtitleWidth = g.getFontMetrics().stringWidth(subtitle);
        g.drawString(subtitle, (panelWidth - subtitleWidth) / 2, 140);
       
        // Calculate center positions - use actual pixel positions
        int fieldWidth = 400;
        int fieldHeight = 40;
        int startX = (panelWidth - fieldWidth) / 2;
        int startY = 200;
        int spacing = 60;
       
        // Draw white rectangles behind text fields
        g.setColor(Color.WHITE);
        for (int i = 0; i < 4; i++) {
            g.fillRect(startX, startY + i * spacing, fieldWidth, fieldHeight);
        }
       
        // Create text fields (only once)
        if (nameFields.isEmpty()) {
            setLayout(null);
            for (int i = 0; i < 4; i++) {
                JTextField nameField = new JTextField();
                nameField.setBounds(startX, startY + i * spacing, fieldWidth, fieldHeight);
                nameField.setFont(new Font("Arial", Font.PLAIN, 18));
                nameField.setForeground(Color.BLACK);
                nameField.setBackground(Color.WHITE);
                nameField.setOpaque(true);
                nameField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                this.add(nameField);
                nameFields.add(nameField);
            }
            this.revalidate();
        } else {
            // Update text field positions if window size changed
            for (int i = 0; i < nameFields.size(); i++) {
                nameFields.get(i).setBounds(startX, startY + i * spacing, fieldWidth, fieldHeight);
            }
        }
       
        // Draw placeholder text if fields are empty
        g.setColor(Color.GRAY);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        for (int i = 0; i < nameFields.size(); i++) {
            if (nameFields.get(i).getText().isEmpty() && !nameFields.get(i).hasFocus()) {
                g.drawString("Player " + (i + 1), startX + 10, startY + 25 + i * spacing);
            }
        }
       
        // Create GO button below text fields, centered (only once)
        if (currentScreen.isEmpty()) {
            int btnW = fieldWidth;
            int btnH = 50;
            int btnX = startX; // align with text fields
            int btnY = startY + 4 * spacing + 10;
            int bx1 = btnX * 1000 / Math.max(1, getWidth());
            int by1 = btnY * 1000 / Math.max(1, getHeight());
            int bx2 = (btnX + btnW) * 1000 / Math.max(1, getWidth());
            int by2 = (btnY + btnH) * 1000 / Math.max(1, getHeight());
            Button goBtn = new Button("GO", "normal", miscpics.get("goBtnImg"), true, true, bx1, by1, bx2, by2);
            currentScreen.add(goBtn);
        }
       
        // Draw button
        for (Button btn : currentScreen) {
            btn.paint(g);
        }
    }
@Override
    public void paint(Graphics g)
{
    super.paint(g);
   
        if(!namesEntered) {
            // paint solid bg for name screen
            g.setColor(new Color(200, 220, 235));
            g.fillRect(0, 0, getWidth(), getHeight());
            realStartingScreen(g);
        } else if(!startingComplete) {
            // paint background for bird selection
            if (bg != null) {
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
            } else {
                g.setColor(new Color(200, 220, 235));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
            startingScreen(g, Player.currentPlayerIndex);
        } else {
            // paint background for game
            if (bg != null) {
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
                playerBoardScreen(g, Player.currentPlayerIndex);
                // setScreen(Player.players().get(0).playerGetScreenDisplay());
               /*  if(currentPlayerIndex==0){
                    setScreen(PlayerOneScreen);
                }
                if(currentPlayerIndex==1){
                    setScreen(PlayerTwoScreen)
                }
                if(currentPlayerIndex==2){
                    PlayerThreeScreen(g);  //to-do change formating
                }
                if(currentPlayerIndex==3){
                    PlayerFourScreen(g);  
                }*/
            } else {
                g.setColor(new Color(200, 220, 235));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
            for(int i=0;i<currentScreen.size();i++)
            {
                currentScreen.get(i).paint(g);
            }
        }
   
}

    public void playerBoardScreen(Graphics g, int pI)
    {
        Rectangle boardArea = getBoardArea();
        g.drawImage(miscpics.get("board"), boardArea.x, boardArea.y, boardArea.width, boardArea.height, null);

        // Ensure spots are laid out to match the current board size
        HashMap<String, ArrayList<Spot>> board = Player.getCurrentPlayerBoard();
        layoutBoardSpots(board, boardArea);

        if(board != null) {
            if(board.containsKey("Forest")) {
                for(Spot spot : board.get("Forest")) {
                    spot.paint(g);
                }
            }
            if(board.containsKey("Grassland")) {
                for(Spot spot : board.get("Grassland")) {
                    spot.paint(g);
                }
            }
            if(board.containsKey("Wetland")) {
                for(Spot spot : board.get("Wetland")) {
                    spot.paint(g);
                }
            }
        }

        drawActionCubeHints(g, board);

        int eggs = getTotalEggs(Player.players().get(pI));
        drawEggCounter(g, eggs);

        displayPlayerHand(g, pI);
        displayPlayerFood(g, pI);
    }
    public void displayPlayerHand(Graphics g, int pI)
    {
        for(int i = 0;i<Player.players().get(pI).playerGetHand().size();i++)
        {
            Bird b = Player.players().get(pI).playerGetHand().get(i);
            int dx = getWidth() - (cardW + 20) * (Player.players().get(pI).playerGetHand().size() - i);
            int dy = getHeight() - cardH - 50;
            drawBirdCard(g, b, dx, dy, cardW, cardH);
        }
    }
    public void displayPlayerFood(Graphics g, int pI)
    {
        TreeMap<String, Integer> food = Player.players().get(pI).playerGetFood();
        int x = 20;
        int y = getHeight() - 200;
        int size = 60;
        int spacing = 70;
        
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Food:", x, y - 10);
        
        int index = 0;
        for (Map.Entry<String, Integer> entry : food.entrySet()) {
            String foodType = entry.getKey();
            int count = entry.getValue();
            
            // Map food type back to token image name
            String tokenName = mapFoodTypeToToken(foodType);
            BufferedImage tokenImg = miscpics.get(tokenName);
            
            if (tokenImg != null && count > 0) {
                g.drawImage(tokenImg, x + index * spacing, y, size, size, null);
                // Draw count
                g.setColor(Color.WHITE);
                g.fillOval(x + index * spacing + size - 20, y + size - 20, 20, 20);
                g.setColor(Color.BLACK);
                g.drawString("" + count, x + index * spacing + size - 15, y + size - 5);
                index++;
            }
        }
    }

    private Rectangle getBoardArea() {
        int width = Math.max(600, getWidth() - 600);
        int height = Math.max(400, getHeight() - 250);
        return new Rectangle(25, 25, width, height);
    }

    private void layoutBoardSpots(HashMap<String, ArrayList<Spot>> board, Rectangle boardArea) {
        if (board == null || boardArea == null) return;
        for (Map.Entry<String, ArrayList<Spot>> entry : board.entrySet()) {
            ArrayList<Spot> spots = entry.getValue();
            if (spots == null) continue;
            for (Spot spot : spots) {
                if (spot != null) {
                    spot.layoutInBoard(boardArea, cardW, cardH);
                }
            }
        }
    }

    private int getTotalEggs(Player player) {
        if (player == null) return 0;
        int eggs = 0;
        HashMap<String, ArrayList<Spot>> board = player.playerGetBoard();
        if (board != null) {
            for (ArrayList<Spot> spots : board.values()) {
                if (spots == null) continue;
                for (Spot s : spots) {
                    if (s != null && s.getBird() != null) {
                        eggs += s.getBird().getEggCount();
                    }
                }
            }
        }
        eggs += player.getStoredEggs();
        return eggs;
    }

    private void drawActionCubeHints(Graphics g, HashMap<String, ArrayList<Spot>> board) {
        if (board == null) return;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (String habitat : Arrays.asList("Forest", "Grassland", "Wetland")) {
            ArrayList<Spot> spots = board.get(habitat);
            if (spots == null || spots.isEmpty()) continue;

            Spot target = null;
            for (Spot s : spots) {
                if (!s.isOccupied()) {
                    target = s;
                    break;
                }
            }
            if (target == null) {
                target = spots.get(spots.size() - 1);
            }

            drawActionCube(g2, target, habitat);
        }
    }

    private void drawActionCube(Graphics2D g2, Spot target, String habitat) {
        if (target == null) return;
        Color base = getHabitatColor(habitat);
        int pad = Math.max(4, target.getWidth() / 20);

        g2.setColor(new Color(base.getRed(), base.getGreen(), base.getBlue(), 70));
        g2.fillRoundRect(target.x1 + pad, target.y1 + pad, target.getWidth() - pad * 2, target.getHeight() - pad * 2, 12, 12);
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(new Color(base.getRed(), base.getGreen(), base.getBlue(), 140));
        g2.drawRoundRect(target.x1 + pad, target.y1 + pad, target.getWidth() - pad * 2, target.getHeight() - pad * 2, 12, 12);

        int cubeSize = Math.max(22, Math.min(36, Math.min(target.getWidth(), target.getHeight()) / 4));
        int cubeX = target.x1 + target.getWidth() - cubeSize - pad;
        int cubeY = target.y1 + pad;
        g2.setColor(new Color(base.getRed(), base.getGreen(), base.getBlue(), 220));
        g2.fillRoundRect(cubeX, cubeY, cubeSize, cubeSize, 6, 6);
        g2.setColor(new Color(0, 0, 0, 160));
        g2.drawRoundRect(cubeX, cubeY, cubeSize, cubeSize, 6, 6);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, Math.max(10, cubeSize / 3)));
        g2.drawString("A", cubeX + cubeSize / 3, cubeY + (int)(cubeSize * 0.72));
    }

    private Color getHabitatColor(String habitat) {
        if (habitat == null) return new Color(60, 60, 60);
        switch (habitat.toLowerCase()) {
            case "forest":
                return new Color(46, 160, 80);
            case "grassland":
                return new Color(220, 190, 40);
            case "wetland":
                return new Color(40, 140, 230);
            default:
                return new Color(60, 60, 60);
        }
    }

    private void updateSpotClickability(HashMap<String, ArrayList<Spot>> board) {
        if (board == null) return;
        
        // First, disable all spots
        for (ArrayList<Spot> spots : board.values()) {
            if (spots == null) continue;
            for (Spot s : spots) {
                s.setClickable(false);
            }
        }
        
        // Then, enable only the leftmost unoccupied spot in each habitat
        for (ArrayList<Spot> spots : board.values()) {
            if (spots == null || spots.isEmpty()) continue;
            
            for (Spot s : spots) {
                if (!s.isOccupied()) {
                    s.setClickable(true);
                    break; // Only the first unoccupied one in this habitat row
                }
            }
        }
    }

    private void resolveHabitatAction(Spot spot) {
        if (spot == null) return;
        Player player = Player.players().get(Player.currentPlayerIndex);
        if (player == null) return;
        String habitat = spot.getHabitat().toLowerCase();
        if (habitat.equals("forest")) {
            doForestAction(player, spot);
        } else if (habitat.equals("grassland") || habitat.equals("plains")) {
            doGrasslandAction(player, spot);
        } else if (habitat.equals("wetland")) {
            doWetlandAction(player, spot);
        }
    }

    private void doForestAction(Player player, Spot spot) {
        int idx = spot.getIndex();
        switch(idx) {
            case 0:
                JOptionPane.showMessageDialog(this, "Take 1 food from the bird feeder (placeholder until feeder is added).");
                grantFood(player, 1, "Choose a food to take:");
                break;
            case 1:
                grantFood(player, 1, "Gain 1 food.");
                handleCardForFoodTrade(player);
                break;
            case 2:
                grantFood(player, 2, "Gain 2 food.");
                break;
            case 3:
                grantFood(player, 2, "Gain 2 food.");
                handleCardForFoodTrade(player);
                break;
            case 4:
                grantFood(player, 3, "Gain 3 food.");
                break;
            default:
                break;
        }
    }

    private void handleCardForFoodTrade(Player player) {
        ArrayList<Bird> hand = player.playerGetHand();
        if (hand == null || hand.isEmpty()) {
            JOptionPane.showMessageDialog(this, "You need a card in hand to trade for extra food.");
            return;
        }
        int trade = JOptionPane.showConfirmDialog(this, "Trade 1 card from your hand for 1 extra food?", "Trade card for food", JOptionPane.YES_NO_OPTION);
        if (trade != JOptionPane.YES_OPTION) return;
        Bird discard = promptCardFromHand(player, "Choose a card to discard for food:");
        if (discard != null) {
            hand.remove(discard);
            grantFood(player, 1, "Choose the food gained from the trade:");
        }
    }

    private void doGrasslandAction(Player player, Spot spot) {
        int idx = spot.getIndex();
        int baseEggs = 0;
        boolean allowFoodTrade = false;
        switch(idx) {
            case 0:
                baseEggs = 2;
                break;
            case 1:
                baseEggs = 2;
                allowFoodTrade = true;
                break;
            case 2:
                baseEggs = 3;
                break;
            case 3:
                baseEggs = 3;
                allowFoodTrade = true;
                break;
            case 4:
                baseEggs = 4;
                break;
            default:
                break;
        }
        if (baseEggs > 0) {
            placeEggsOnBoard(player, baseEggs);
        }
        if (allowFoodTrade) {
            if (!hasAnyFood(player)) {
                JOptionPane.showMessageDialog(this, "You need at least 1 food to trade for an extra egg.");
                return;
            }
            int choice = JOptionPane.showConfirmDialog(this, "Trade 1 random food you own for 1 extra egg?", "Food for egg", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                if (removeRandomFood(player)) {
                    placeEggsOnBoard(player, 1);
                } else {
                    JOptionPane.showMessageDialog(this, "No food available to trade.");
                }
            }
        }
    }

    private void doWetlandAction(Player player, Spot spot) {
        int idx = spot.getIndex();
        int cardsToDraw = 0;
        boolean allowTrade = false;
        switch(idx) {
            case 0:
                cardsToDraw = 1;
                break;
            case 1:
                cardsToDraw = 1;
                allowTrade = true;
                break;
            case 2:
                cardsToDraw = 2;
                break;
            case 3:
                cardsToDraw = 2;
                allowTrade = true;
                break;
            case 4:
                cardsToDraw = 3;
                break;
            default:
                break;
        }
        if (cardsToDraw > 0) {
            drawCards(player, cardsToDraw);
        }
        if (allowTrade) {
            ArrayList<Bird> hand = player.playerGetHand();
            if (hand == null || hand.isEmpty() || getTotalEggs(player) <= 0) {
                JOptionPane.showMessageDialog(this, "You need at least 1 egg and 1 card to trade for another card.");
                return;
            }
            int trade = JOptionPane.showConfirmDialog(this, "Trade 1 egg and 1 card for 1 extra card?", "Trade resources for card", JOptionPane.YES_NO_OPTION);
            if (trade == JOptionPane.YES_OPTION) {
                Bird discard = promptCardFromHand(player, "Choose a card to discard:");
                if (discard != null) {
                    boolean spent = spendEggs(player, 1);
                    if (spent) {
                        hand.remove(discard);
                        drawCards(player, 1);
                    } else {
                        JOptionPane.showMessageDialog(this, "Not enough eggs to spend.");
                    }
                }
            }
        }
    }

    private void grantFood(Player player, int foodCount, String title) {
        for (int i = 0; i < foodCount; i++) {
            String foodType = promptFoodType(title);
            if (foodType == null) return;
            player.addFood(foodType, 1);
        }
    }

    private String promptFoodType(String message) {
        String[] foodOptions = {"grain", "invertebrate", "fish", "fruit", "rodent"};
        return (String) JOptionPane.showInputDialog(this, message, "Choose Food", JOptionPane.PLAIN_MESSAGE, null, foodOptions, foodOptions[0]);
    }

    private boolean hasAnyFood(Player player) {
        TreeMap<String, Integer> food = player.playerGetFood();
        if (food == null) return false;
        for (int count : food.values()) {
            if (count > 0) return true;
        }
        return false;
    }

    private boolean removeRandomFood(Player player) {
        TreeMap<String, Integer> food = player.playerGetFood();
        if (food == null) return false;
        ArrayList<String> available = new ArrayList<String>();
        for (Map.Entry<String, Integer> entry : food.entrySet()) {
            if (entry.getValue() != null && entry.getValue() > 0) {
                available.add(entry.getKey());
            }
        }
        if (available.isEmpty()) return false;
        Random rng = new Random();
        String chosen = available.get(rng.nextInt(available.size()));
        boolean spent = player.spendFood(chosen, 1);
        if (spent) {
            JOptionPane.showMessageDialog(this, "Traded 1 " + chosen + " for an egg.");
        }
        return spent;
    }

    private int placeEggsOnBoard(Player player, int eggsToPlace) {
        if (eggsToPlace <= 0) return 0;
        ArrayList<Bird> birds = player.getAllPlayedBirds();
        if (birds == null || birds.isEmpty()) {
            player.addStoredEggs(eggsToPlace);
            return eggsToPlace;
        }
        int eggsLeft = eggsToPlace;
        for (Bird b : birds) {
            if (b == null) continue;
            int capacity = b.getEggCapacity() > 0 ? b.getEggCapacity() - b.getEggCount() : eggsLeft;
            if (capacity <= 0) continue;
            int toPlace = Math.min(capacity, eggsLeft);
            b.addEggs(toPlace);
            eggsLeft -= toPlace;
            if (eggsLeft <= 0) break;
        }
        if (eggsLeft > 0) {
            player.addStoredEggs(eggsLeft);
        }
        return eggsToPlace;
    }

    private boolean spendEggs(Player player, int eggsNeeded) {
        if (eggsNeeded <= 0) return true;
        if (player == null) return false;
        if (getTotalEggs(player) < eggsNeeded) return false;
        int remaining = eggsNeeded;
        // spend stored eggs first
        int stored = player.getStoredEggs();
        if (stored > 0) {
            int use = Math.min(stored, remaining);
            player.spendStoredEggs(use);
            remaining -= use;
        }
        HashMap<String, ArrayList<Spot>> board = player.playerGetBoard();
        if (board == null) return false;
        for (ArrayList<Spot> spots : board.values()) {
            if (spots == null) continue;
            for (Spot s : spots) {
                Bird b = s.getBird();
                if (b == null) continue;
                if (remaining <= 0) break;
                int removed = b.removeEggs(remaining);
                remaining -= removed;
            }
        }
        return remaining <= 0;
    }

    private Bird promptCardFromHand(Player player, String title) {
        ArrayList<Bird> hand = player.playerGetHand();
        if (hand == null || hand.isEmpty()) return null;
        String[] options = new String[hand.size()];
        for (int i = 0; i < hand.size(); i++) {
            options[i] = hand.get(i).getName();
        }
        String choice = (String) JOptionPane.showInputDialog(this, title, "Choose Card", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (choice == null) return null;
        for (Bird b : hand) {
            if (b.getName().equals(choice)) {
                return b;
            }
        }
        return null;
    }

private void drawCards(Player player, int count) {
    for (int i = 0; i < count; i++) {
        Bird newCard = selectBirdFromMarket("Pick a card to draw (" + (count - i) + " remaining).");
        if (newCard != null) {
            player.playerGetHand().add(cloneBird(newCard));
        }
    }
}

private Bird promptCardChoice(String title) {
    return selectBirdFromMarket(title);
}

    private Bird makePlaceholderCard(String label) {
        String name = "Placeholder Card " + (placeholderCardCounter++) + " - " + label;
        return new Bird(name, name, "none", new String[]{"wild"}, null, new TreeMap<String,Integer>(), new TreeMap<String,Integer>(), 0, 0, 0, 0, new ArrayList<Bird>(), false, false, null, null, 0, 0);
    }

    private void handleSpotAction(Spot spot) {
        if (spot == null) return;

        String[] options = {"Claim habitat action", "Place a bird", "Cancel"};
        int choice = JOptionPane.showOptionDialog(this,
                "What would you like to do on this spot?",
                "Habitat Action",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 0) {
            if (spot.hasActionToken()) {
                JOptionPane.showMessageDialog(this, "Habitat action already used here. You can still place a bird.");
                return;
            }
            Player current = Player.players().get(Player.currentPlayerIndex);
            if (current.getRemainingTokens() <= 0) {
                JOptionPane.showMessageDialog(this, "No action tokens remaining for this player.");
                return;
            }
            spot.setActionToken(true);
            if (current.useActionToken()) {
                resolveHabitatAction(spot);
                activateHabitatAbilities(spot.getHabitat());
                advanceTurn();
            }
        } else if (choice == 1) {
            Player current = Player.players().get(Player.currentPlayerIndex);
            if (current.getRemainingTokens() <= 0) {
                JOptionPane.showMessageDialog(this, "No action tokens remaining for this player.");
                return;
            }
            if (spot.isOccupied()) {
                JOptionPane.showMessageDialog(this, "This spot already has a bird.");
                return;
            }
            ArrayList<Bird> hand = Player.players().get(Player.currentPlayerIndex).playerGetHand();
            if (hand == null || hand.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No birds in hand to place.");
                return;
            }
            String[] birdNames = hand.stream().map(Bird::getName).toArray(String[]::new);
            String selected = (String) JOptionPane.showInputDialog(this, "Choose a bird to place:", "Place Bird",
                    JOptionPane.PLAIN_MESSAGE, null, birdNames, birdNames[0]);
            if (selected == null) return;

            Bird chosen = null;
            for (Bird b : hand) {
                if (b.getName().equals(selected)) { chosen = b; break; }
            }
            if (chosen != null) {
                if (!canAffordBird(Player.players().get(Player.currentPlayerIndex), chosen)) {
                    JOptionPane.showMessageDialog(this, "Not enough food to play this bird.");
                    return;
                }
                payForBird(Player.players().get(Player.currentPlayerIndex), chosen);
                current.useActionToken();
                spot.setActionToken(true);
                spot.setBird(chosen);
                spot.setOccupied(true);
                chosen.setBounds(new Rectangle(spot.x1, spot.y1, spot.getWidth(), spot.getHeight()));
                hand.remove(chosen);
                JOptionPane.showMessageDialog(this, chosen.getName() + " placed in " + spot.getHabitat() + ".");
                activateHabitatAbilities(spot.getHabitat());
                advanceTurn();
            }
        }
    }

    private void advanceTurn() {
        Player.currentPlayerIndex = (Player.currentPlayerIndex + 1) % Player.players().size();
        repaint();
    }

    private void drawEggCounter(Graphics g, int eggCount) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int boxW = 150;
        int boxH = 60;
        int x = getWidth() - boxW - 20;
        int y = 20;

        g2.setColor(new Color(255, 255, 255, 210));
        g2.fillRoundRect(x, y, boxW, boxH, 10, 10);
        g2.setColor(new Color(120, 90, 40));
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(x, y, boxW, boxH, 10, 10);

        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.setColor(new Color(90, 60, 20));
        g2.drawString("Eggs", x + 12, y + 22);

        g2.setFont(new Font("Arial", Font.BOLD, 26));
        g2.setColor(new Color(50, 30, 10));
        g2.drawString(Integer.toString(eggCount), x + 12, y + 48);

        g2.setColor(new Color(240, 220, 180));
        int eggX = x + boxW - 40;
        int eggY = y + 18;
        g2.fillOval(eggX, eggY, 22, 30);
        g2.setColor(new Color(200, 170, 120));
        g2.drawOval(eggX, eggY, 22, 30);
    }

    private boolean canAffordBird(Player player, Bird bird) {
        if (player == null || bird == null) return false;
        TreeMap<String,Integer> costs = bird.getCosts();
        if (costs == null || costs.isEmpty()) return true;
        TreeMap<String,Integer> food = player.playerGetFood();
        if (food == null) return false;
        int wildNeeded = costs.getOrDefault("wild", 0);
        int totalAvailable = 0;
        for (int v : food.values()) totalAvailable += v;
        // check specific costs
        for (Map.Entry<String,Integer> e : costs.entrySet()) {
            String type = e.getKey();
            int amt = e.getValue() == null ? 0 : e.getValue();
            if (type.equals("wild") || type.equals("no-food")) continue;
            int have = food.getOrDefault(type, 0);
            if (have < amt) {
                int deficit = amt - have;
                if (wildNeeded >= deficit) {
                    wildNeeded -= deficit;
                } else {
                    return false;
                }
            }
        }
        if (wildNeeded > 0) {
            if (totalAvailable < requiredNonWild(costs) + wildNeeded) return false;
        }
        return true;
    }

    private int requiredNonWild(TreeMap<String,Integer> costs) {
        int sum = 0;
        for (Map.Entry<String,Integer> e : costs.entrySet()) {
            if (e.getKey().equals("wild") || e.getKey().equals("no-food")) continue;
            sum += (e.getValue() == null ? 0 : e.getValue());
        }
        return sum;
    }

    private void payForBird(Player player, Bird bird) {
        if (player == null || bird == null) return;
        TreeMap<String,Integer> costs = bird.getCosts();
        if (costs == null || costs.isEmpty()) return;
        int wildNeeded = costs.getOrDefault("wild", 0);
        for (Map.Entry<String,Integer> e : costs.entrySet()) {
            String type = e.getKey();
            int amt = e.getValue() == null ? 0 : e.getValue();
            if (type.equals("wild") || type.equals("no-food")) continue;
            int spend = Math.max(0, amt);
            int have = player.playerGetFood().getOrDefault(type, 0);
            if (have < spend) {
                int useWild = spend - have;
                spend = have;
                wildNeeded -= useWild;
            }
            if (spend > 0) {
                player.spendFood(type, spend);
            }
        }
        if (wildNeeded > 0) {
            // spend wild from any available foods
            TreeMap<String,Integer> food = player.playerGetFood();
            for (String ft : new ArrayList<String>(food.keySet())) {
                while (wildNeeded > 0 && food.getOrDefault(ft,0) > 0) {
                    player.spendFood(ft, 1);
                    wildNeeded--;
                }
                if (wildNeeded <= 0) break;
            }
        }
    }


    private void finishPlayerSelection() {
        // Store selected items in current player's hand
        ArrayList<Bird> keptBirds = new ArrayList<>();
        Player currentPlayer = Player.players().get(Player.currentPlayerIndex);
        for (ItemRef item : selected) {
            if (item.type == ItemType.BIRD && item.bird != null) {
                keptBirds.add(item.bird);
            } else if (item.type == ItemType.TOKEN) {
                // Map token image names to food types and add to player's food
                String foodType = mapTokenToFoodType(item.tokenName);
                currentPlayer.addFood(foodType, 1);
                String playerName = playerNames.get(Player.currentPlayerIndex);
                System.out.println(playerName + " kept token: " + item.tokenName + " (" + foodType + ")");
            }
        }
        currentPlayer.playerSetHand(keptBirds);
        String playerName = playerNames.get(Player.currentPlayerIndex);
        System.out.println(playerName + " selection complete. Kept " + keptBirds.size() + " birds.");
       
        // Move to next player
        selected.clear();
        Player.currentPlayerIndex++;
       
        if (Player.currentPlayerIndex >= 4) {
            Player.currentPlayerIndex = 0;
            startingComplete = true;
            System.out.println("players selected");
            for (Player p : Player.players()) {
                p.resetTokens(8);
            }
        }
    }
   
    public void startingScreen(Graphics g, int playerIndex)
    {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        String playerName = playerNames.get(playerIndex);
        g.drawString(playerName + " - Select 5 items to keep", 20, 30);
       
        ArrayList<Bird> hand = Player.players().get(playerIndex).playerGetHand();
        int topMargin = 60; // draw near the top of the screen
        for(int i = 0; i < 5; i++)
        {
            Bird b = hand.get(i);
            if (isSelected(b)) continue; // hide birds already kept
            b.setX(50 + i * (cardW + 20));
            b.setY(topMargin);
            Rectangle r = b.getBounds();


            int drawW = cardW;
            int drawH = cardH;
            int drawX = r.x;
            int drawY = r.y;
            if (b == hoverBird) {
                double scale = 1.12;
                drawW = (int)Math.round(cardW * scale);
                drawH = (int)Math.round(cardH * scale);
                drawX = r.x - (drawW - cardW)/2;
                drawY = r.y - (drawH - cardH)/2;
            }
            drawBirdCard(g, b, drawX, drawY, drawW, drawH);
        }
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Build/refresh token positions for hit testing and draw those not yet kept
        ensureTokenItems();
        for (TokenItem t : tokenItems) {
            if (isSelectedToken(t.name)) continue; // hide tokens already kept
            int size = tokenSize;
            int x = t.cx - size/2;
            int y = t.cy - size/2;
            if (t.name.equals(hoverTokenName)) {
                double scale = 1.18;
                int newSize = (int)Math.round(size * scale);
                x = t.cx - newSize/2;
                y = t.cy - newSize/2;
                size = newSize;
            }
            drawToken(g2, t.image, x, y, size);
        }


        // Draw selection box and selected items (top-right) sized for full cards
        int margin = 16;
        int slotW = cardW;
        int slotH = cardH;
        int gap = 12;
        int padding = 12;
        int labelH = 22;
        int boxW = 5 * slotW + 4 * gap + 2 * padding;
        int boxH = slotH + 2 * padding + labelH; // include label area
        int boxX = getWidth() - boxW - margin;
        int boxY = margin;
        g2.setColor(new Color(255,255,255,210));
        g2.fillRoundRect(boxX, boxY, boxW, boxH, 10, 10);
        g2.setColor(Color.DARK_GRAY);
        g2.drawRoundRect(boxX, boxY, boxW, boxH, 10, 10);
        g2.drawString("Keep (" + selected.size() + "/5)", boxX + padding, boxY + 16);


        selectionSlots.clear();
        int sx = boxX + padding;
        int sy = boxY + labelH + padding;
        for (int i = 0; i < 5; i++) {
            int x = sx + i * (slotW + gap);
            int y = sy;
            Rectangle slotRect = new Rectangle(x, y, slotW, slotH);
            g2.setColor(new Color(0,0,0,40));
            g2.drawRect(x, y, slotW, slotH);
            if (i < selected.size()) {
                ItemRef it = selected.get(i);
                boolean isHover = (hoverSlotItem == it);
                if (it.type == ItemType.TOKEN) {
                    int ts = Math.min(slotW, slotH) - 10;
                    if (isHover) ts = (int)Math.round(ts * 1.15);
                    drawToken(g2, miscpics.get(it.tokenName), x + (slotW - ts)/2, y + (slotH - ts)/2, ts);
                } else if (it.type == ItemType.BIRD && it.bird != null) {
                    BufferedImage bi = it.bird.getImage();
                    int dw = slotW;
                    int dh = slotH;
                    int dx = x;
                    int dy = y;
                    if (isHover) {
                        double scale = 1.12;
                        dw = (int)Math.round(slotW * scale);
                        dh = (int)Math.round(slotH * scale);
                        dx = x - (dw - slotW)/2;
                        dy = y - (dh - slotH)/2;
                    }
                    if (bi != null) g2.drawImage(bi, dx, dy, dw, dh, null);
                }
                selectionSlots.add(new Slot(slotRect, it));
            }
        }


       
    }
    // Draw a bird card; if image is missing, render a simple placeholder with the bird name
    public void drawBirdCard(Graphics g, Bird bird, int x, int y, int width, int height)
    {
        if(bird != null && bird.getImage() != null) {
            g.drawImage(bird.getImage(), x, y, width, height, null);
            return;
        }
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(245, 245, 245));
        g2.fillRoundRect(x, y, width, height, 10, 10);
        g2.setColor(Color.DARK_GRAY);
        g2.drawRoundRect(x, y, width, height, 10, 10);
        String name = (bird != null && bird.getName()!=null) ? bird.getName() : "Unknown Bird";
        g2.setFont(new Font("Arial", Font.BOLD, Math.max(12, width/12)));
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(name);
        int maxTextWidth = width - 20;
        if(textWidth > maxTextWidth) {
            // crude wrap into two lines
            int breakPos = name.length() * maxTextWidth / textWidth;
            breakPos = Math.max(1, Math.min(name.length()-1, breakPos));
            String line1 = name.substring(0, breakPos);
            String line2 = name.substring(breakPos);
            int lineHeight = fm.getHeight();
            int startY = y + height/2 - lineHeight/2;
            g2.drawString(line1, x + 10, startY);
            g2.drawString(line2, x + 10, startY + lineHeight);
        } else {
            g2.drawString(name, x + (width - textWidth)/2, y + height/2);
        }
    }
    // helper to normalize token rendering with consistent padding
    private void drawToken(Graphics2D g2, BufferedImage img, int x, int y, int size) {
        if (img == null) return;
        int pad = Math.max(2, (int)(size * 0.1));
        int inner = size - pad * 2;
        g2.drawImage(img, x + pad, y + pad, inner, inner, null);
    }


    // Helpers to check current selections
    private boolean isSelected(Bird b) {
        for (ItemRef it : selected) {
            if (it.type == ItemType.BIRD && it.bird == b) return true;
        }
        return false;
    }
    private boolean isSelectedToken(String name) {
        for (ItemRef it : selected) {
            if (it.type == ItemType.TOKEN && it.tokenName != null && it.tokenName.equals(name)) return true;
        }
        return false;
    }


    // Build token items (centers and radii) for hit-testing
    private void ensureTokenItems() {
        if (miscpics.isEmpty()) return;
        tokenItems.clear();
        int yTop = getHeight() - 300; // top-left of tokens row
        int cy = yTop + tokenSize/2;
        tokenItems.add(new TokenItem("wheattoken", 100 + tokenSize/2, cy, tokenSize/2, miscpics.get("wheattoken")));
        tokenItems.add(new TokenItem("invertebratetoken", 250 + tokenSize/2, cy, tokenSize/2, miscpics.get("invertebratetoken")));
        tokenItems.add(new TokenItem("fishtoken", 400 + tokenSize/2, cy, tokenSize/2, miscpics.get("fishtoken")));
        tokenItems.add(new TokenItem("foodtoken", 550 + tokenSize/2, cy, tokenSize/2, miscpics.get("foodtoken")));
        tokenItems.add(new TokenItem("rattoken", 700 + tokenSize/2, cy, tokenSize/2, miscpics.get("rattoken")));
    }


    // Helper method to map token image names to food types
    private String mapTokenToFoodType(String tokenName) {
        switch(tokenName) {
            case "wheattoken": return "grain";
            case "invertebratetoken": return "invertebrate";
            case "fishtoken": return "fish";
            case "foodtoken": return "fruit";
            case "rattoken": return "rodent";
            default: return tokenName;
        }
    }
    
    // Helper method to map food types back to token image names
    private String mapFoodTypeToToken(String foodType) {
        switch(foodType) {
            case "grain": return "wheattoken";
            case "invertebrate": return "invertebratetoken";
            case "fish": return "fishtoken";
            case "fruit": return "foodtoken";
            case "rodent": return "rattoken";
            default: return foodType + "token";
        }
    }
    
    // trims fully transparent borders so different PNG paddings appear same size when scaled
    private BufferedImage trimTransparent(BufferedImage img) {
        if (img == null) return null;
        int w = img.getWidth();
        int h = img.getHeight();
        int minX = w, minY = h, maxX = -1, maxY = -1;
        final int alphaThreshold = 10;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int a = (img.getRGB(x, y) >>> 24) & 0xFF;
                if (a > alphaThreshold) {
                    if (x < minX) minX = x;
                    if (y < minY) minY = y;
                    if (x > maxX) maxX = x;
                    if (y > maxY) maxY = y;
                }
            }
        }
        if (maxX < minX || maxY < minY) return img;
        int tw = maxX - minX + 1;
        int th = maxY - minY + 1;
        try {
            return img.getSubimage(minX, minY, tw, th);
        } catch (Exception e) {
            return img;
        }
    }
    public void mouseClicked(MouseEvent e) {
        Point p = e.getPoint();
       System.out.println(p);
        if (!namesEntered) {
            // Check if GO button clicked
            for (Button btn : currentScreen) {
                System.out.println("Checking button: " + btn.getName() + " at click (" + e.getX() + "," + e.getY() + ")");
                if (btn.inBounds(e.getX(), e.getY()) && btn.getName().equals("GO")) {
                    System.out.println("GO button clicked!");
                    // Create players with entered names
                    Player.players().clear();
                    playerNames.clear();
                    for (int i = 0; i < 4; i++) {
                        String name = nameFields.get(i).getText().trim();
                        if (name.isEmpty()) name = "Player " + (i + 1);
                        playerNames.add(name);
                     /*   Player player = new Player(new ArrayList<Bird>(),
                            new TreeMap<String,Integer>(),
                            new ArrayList<BonusCard>(),
                            new HashMap<String, ArrayList<Spot>>(),
                            new ArrayList<Button>());
                        Player.players.add(player);
                        System.out.println("Created player: " + name);*/
                    }
                    // Remove text fields and button
                    for (JTextField tf : nameFields) {
                        this.remove(tf);
                    }
                    currentScreen.clear();
                    namesEntered = true;
                    Player.currentPlayerIndex = 0;
                   
                    // Give each player a unique random starting hand from the deck (images may be null for now)
                    if (birdDeck.isEmpty()) {
                        rebuildDeckFromLoaded();
                    }
                    for (Player player : Player.players()) {
                        ArrayList<Bird> startingHand = prepareStartingHand();
                        player.playerSetHand(startingHand);
                    }
                   
                    this.revalidate();
                    repaint();
                    return;
                }
            }
            return;
        }


        if(!startingComplete){
            // Click inside selection box slots -> remove item
            for (int i = 0; i < selectionSlots.size(); i++) {
                Slot s = selectionSlots.get(i);
                if (s.bounds.contains(p)) {
                    selected.remove(s.item);
                    String playerName = playerNames.get(Player.currentPlayerIndex);
                    System.out.println(playerName + " removed: " + (s.item.type==ItemType.TOKEN ? s.item.tokenName : s.item.bird.getName()) + ". Now " + selected.size() + "/5");
                    repaint();
                    return;
                }
            }


            // Token circular hit-test (distance)
            for (TokenItem t : tokenItems) {
                if (isSelectedToken(t.name)) continue; // already kept
                int dx = p.x - t.cx;
                int dy = p.y - t.cy;
                if (dx*dx + dy*dy <= t.radius*t.radius) {
                    if (selected.size() < 5) {
                        selected.add(ItemRef.token(t.name));
                        String playerName = playerNames.get(Player.currentPlayerIndex);
                        System.out.println(playerName + " added token: " + t.name + ". Now " + selected.size() + "/5");
                        if (selected.size() >= 5) {
                            finishPlayerSelection();
                        }
                    }
                    repaint();
                    return;
                }
            }


            // Bird rectangle hit-test
            ArrayList<Bird> hand = Player.players().get(Player.currentPlayerIndex).playerGetHand();
            for (Bird b : hand) {
                if (isSelected(b)) continue; // already kept
                Rectangle r = b.getBounds();
                if (r != null && r.contains(p)) {
                    if (selected.size() < 5) {
                        selected.add(ItemRef.bird(b));
                        String playerName = playerNames.get(Player.currentPlayerIndex);
                        System.out.println(playerName + " added bird: " + b.getName() + ". Now " + selected.size() + "/5");
                        if (selected.size() >= 5) {
                            finishPlayerSelection();
                        }
                    }
                    repaint();
                    return;
                }
            }
        }


        if (startingComplete) {
            HashMap<String, ArrayList<Spot>> board = Player.getCurrentPlayerBoard();
            if (board != null) {
                // Update clickable status: only leftmost unoccupied spot in each habitat
                updateSpotClickability(board);
                
                for (ArrayList<Spot> spots : board.values()) {
                    if (spots == null) continue;
                    for (Spot s : spots) {
                        Rectangle r = new Rectangle(s.x1, s.y1, s.getWidth(), s.getHeight());
                        if (r.contains(p) && s.getClickable()) {
                            handleSpotAction(s);
                            repaint();
                            return;
                        }
                    }
                }
            }
        }

        for(int i=0;i<currentScreen.size();i++)
        {
            if(currentScreen.get(i).inBounds(e.getX(), e.getY()))
            {
                currentScreen.get(i).click();
            }
        }
        repaint();
    }


    @Override
    public void mouseMoved(MouseEvent e) {
        if (!startingComplete) {
            Point p = e.getPoint();
            // Hover over selection box items
            ItemRef prevSlot = hoverSlotItem;
            hoverSlotItem = null;
            for (Slot s : selectionSlots) {
                if (s.bounds.contains(p)) { hoverSlotItem = s.item; break; }
            }


            // Hover over tokens
            String prevToken = hoverTokenName;
            hoverTokenName = null;
            ensureTokenItems();
            for (TokenItem t : tokenItems) {
                if (isSelectedToken(t.name)) continue;
                int dx = p.x - t.cx;
                int dy = p.y - t.cy;
                if (dx*dx + dy*dy <= t.radius*t.radius) { hoverTokenName = t.name; break; }
            }


            // Hover over birds
            Bird prevBird = hoverBird;
            hoverBird = null;
            ArrayList<Bird> hand = Player.players().get(Player.currentPlayerIndex).playerGetHand();
            for (Bird b : hand) {
                if (isSelected(b)) continue;
                Rectangle r = b.getBounds();
                if (r != null && r.contains(p)) { hoverBird = b; break; }
            }


            if (prevSlot != hoverSlotItem || prevToken != hoverTokenName || prevBird != hoverBird) {
                repaint();
            }
        }
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        // not used
    }


    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
       
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
       
    }


    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
       
    }


    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
       
    }
    public static void setScreen(ArrayList<Button> in) 
    {
        currentScreen = in;
    }
   
    /*  TRY TO INITIALIZE OBJECTS HERE SO THAT THERE ARE NO POINTER EXCEPTIONS
     *
     *
     */
    public void addNotify() {
        super.addNotify();
        requestFocus();
    loadInitialImages();
    rebuildDeckFromLoaded();
    try
    {
        System.out.println("Loading images...");
        bg = ImageIO.read(Panel.class.getResource("/Images/wgsbg.jpg"));
        System.out.println("Background loaded: " + (bg != null));
        System.out.println("All images loaded successfully!");
    }
    catch (Exception e)
    {
        System.out.println("ERROR loading images:");
        e.printStackTrace();
    }  
        miscellaneousScreen.add(new Button("temp","",null,false,true,0,0,1000,1000));




    // Players will be created through the name entry screen
    // Bird images will be loaded when players are created
    repaint();
    }
}
