import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;
import java.util.Collections;
import java.util.Random;
import java.util.List;
import java.util.stream.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import java.util.function.Predicate;


public class Panel extends JPanel implements MouseListener, MouseMotionListener, java.awt.event.KeyListener{
public static ArrayList<Button> currentScreen = new ArrayList<Button>();
BufferedImage bg;
private int cardH = 278;
private int cardW = 140;
private int tokenSize = 150;
HashMap<String, BufferedImage> miscpics = new HashMap<String, BufferedImage>();
HashMap<String, BufferedImage> birdImages = new HashMap<String, BufferedImage>();
HashMap<String, Bird> birdcards = new HashMap<String, Bird>();
//ArrayList<Player> players = new ArrayList<Player>();
ArrayList<String> playerNames = new ArrayList<String>();
//Player player1;
private boolean namesEntered = false;
private boolean showingControls = false;
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
private ArrayList<ZoomTarget> zoomButtons = new ArrayList<ZoomTarget>();
private ArrayList<ZoomTarget> tuckButtons = new ArrayList<ZoomTarget>();
private boolean zoomActive = false;
private Bird zoomBird = null;
private Rectangle zoomRect = null;
private boolean tuckViewActive = false;
private Bird tuckBird = null;
private Rectangle tuckRect = null;
private HashMap<String, BufferedImage> bonusImages = new HashMap<String, BufferedImage>();
private ArrayList<BonusCard> bonusDeck = new ArrayList<BonusCard>();
private ArrayList<BonusCard> bonusOffer = new ArrayList<BonusCard>();
private ArrayList<Rectangle> bonusRects = new ArrayList<Rectangle>();
private BonusCard chosenBonus = null;
private int bonusOfferPlayerIndex = -1;
private int currentRound = 1;
private final int maxRounds = 4;
private boolean roundOver = false;
private boolean gameFinished = false;
private ArrayList<int[]> roundScores = new ArrayList<int[]>();
private Rectangle nextRoundButton = new Rectangle();
public static ArrayList<Button> miscellaneousScreen = new ArrayList<Button>();
private ArrayList<String> feederDice = new ArrayList<String>();
private ArrayList<String> feederCup = new ArrayList<String>();
// Cache dimensions to rebuild start-screen buttons on resize
private int startScreenCachedW = -1;
private int startScreenCachedH = -1;
// Gate to ensure only one habitat action per turn
private boolean turnActionClaimed = false;
private static final String[] FEEDER_FACES = {
    "grain",
    "fruit",
    "invertebrate",
    "rodent",
    "fish",
    "grain_or_invertebrate"
};
private static final String[][] BONUS_DEFS = {
    {"anatomist","Birds with a body part in their name"},
    {"backyard_birder","Birds with any printed nest type"},
    {"bird_counter","Number of birds in your habitats"},
    {"bird_feeder","Birds that eat seed or fruit"},
    {"breeding_manager","Birds with egg capacity 4+."},
    {"cartographer","Habitats with at least 3 birds"},
    {"ecologist","Birds with 2+ habitats or wild"},
    {"enclosure_builder","Birds with cavity or platform nests"},
    {"falconer","Birds with rodent cost or wingspan 65+"},
    {"fishery_manager","Birds that eat fish"},
    {"food_web_expert","Birds with 2+ food types or wild"},
    {"forester","Birds in forest habitat"},
    {"historian","Birds with possessive/apostrophe name"},
    {"large_bird_specialist","Birds with wingspan 100+"},
    {"nest_box_builder","Birds with cavity nests"},
    {"omnivore_specialist","Birds with wild in cost"},
    {"oologist","Birds with egg capacity 3+"},
    {"passerine_specialist","Birds with wingspan < 30"},
    {"photographer","Birds worth 5+ points"},
    {"platform_builder","Birds with platform nests"},
    {"prairie_manager","Birds in grassland habitat"},
    {"rodentologist","Birds that eat rodent"},
    {"visionary_leader","Birds worth 7+ points"},
    {"viticulturalist","Birds that eat fruit"},
    {"wetland_scientist","Birds in wetland habitat"},
    {"wildlife_gardener","Birds with bowl or ground nests"}
};
public Panel()
{
    setSize(1600,960);
   
   
    addMouseListener(this);
    addMouseMotionListener(this);
    addKeyListener(this);
    setFocusable(true);


   
}
public void loadBirdImages() {
    try {
        // All 169 bird image names from the birds folder (excluding back_of_bird.png)
        String[] birdNames = {
            "acorn_woodpecker", "american_avocet", "american_bittern", "american_coot",
            "american_crow", "american_goldfinch", "american_kestrel", "american_oystercatcher",
            "american_redstart", "american_robin", "american_white_pelican", "american_woodcock",
            "anhinga", "anna_s_hummingbird", "ash-throated_flycatcher", "atlantic_puffin",
            "baird_s_sparrow", "bald_eagle", "baltimore_oriole", "barn_owl",
            "barn_swallow", "barred_owl", "barrow_s_goldeneye", "bell_s_vireo",
            "belted_kingfisher", "bewick_s_wren", "black-bellied_whistling-duck", "black-billed_magpie",
            "black-crowned_night-heron", "black-necked_stilt", "black_chinned_hummingbird", "black_skimmer",
            "black_tern", "black_vulture", "blue-gray_gnatcatcher", "blue-winged_warbler",
            "blue_grosbeak", "blue_jay", "bobolink", "brant",
            "brewer_s_blackbird", "broad_winged_hawk", "bronzed_cowbird", "brown-headed_cowbird",
            "brown_pelican", "burrowing_owl", "bushtit", "california_condor",
            "california_quail", "canada_goose", "canvasback", "carolina_chickadee",
            "carolina_wren", "cassin_s_finch", "cassin_s_sparrow", "cedar_waxwing",
            "cerulean_warbler", "chestnut-collared_longspur", "chihuahuan_raven", "chimney_swift",
            "chipping_sparrow", "clark_s_grebe", "clark_s_nutcracker", "common_grackle",
            "common_loon", "common_merganser", "common_nighthawk", "common_raven",
            "common_yellowthroat", "cooper_s_hawk", "dark-eyed_junco", "dickcissel",
            "double-crested_cormorant", "downy_woodpecker", "eastern_bluebird", "eastern_kingbird",
            "eastern_phoebe", "eastern_screech-owl", "ferruginous_hawk", "fish_crow",
            "forster_s_tern", "franklin_s_gull", "golden_eagle", "grasshopper_sparrow",
            "gray_catbird", "greater_prairie_chicken", "greater_roadrunner", "great_blue_heron",
            "great_crested_flycatcher", "great_egret", "great_horned_owl", "green_heron",
            "hermit_thrush", "hooded_merganser", "hooded_warbler", "horned_lark",
            "house_finch", "house_wren", "inca_dove", "indigo_bunting",
            "juniper_titmouse", "killdeer", "king_rail", "lincoln_s_sparrow",
            "loggerhead_shrike", "mallard", "mississippi_kite", "mountain_bluebird",
            "mountain_chickadee", "mourning_dove", "northern_bobwhite", "northern_cardinal",
            "northern_flicker", "northern_harrier", "northern_mockingbird", "northern_shoveler",
            "osprey", "painted_bunting", "painted_whitestart", "peregrine_falcon",
            "pileated_woodpecker", "pine_siskin", "prothonotary_warbler", "purple_gallinule",
            "purple_martin", "pygmy_nuthatch", "red-bellied_woodpecker", "red-breasted_merganser",
            "red-breasted_nuthatch", "red-cockaded_woodpecker", "red-tailed_hawk", "roseate_spoonbill",
            "ruby-crowned_kinglet", "sandhill_crane", "savannah_sparrow", "say_s_phoebe",
            "scaled_quail", "scissor-tailed_flycatcher", "snowy_egret", "song_sparrow",
            "spotted_owl", "spotted_sandpiper", "spotted_towhee", "sprague_s_pipit",
            "steller_s_jay", "swainson_s_hawk", "tree_swallow", "trumpeter_swan",
            "tufted_titmouse", "turkey_vulture", "vaux_s_swift", "violet-green_swallow",
            "western_meadowlark", "western_tanager", "white-breasted_nuthatch", "white-crested_sparrow",
            "white-faced_ibis", "white-throated_swift", "whooping_crane", "wild_turkey",
            "willet", "wilson_s_snipe", "wood_duck", "wood_stork",
            "yellow-bellied_sapsucker", "yellow-billed_cuckoo", "yellow-breasted_chat", "yellow-headed_blackbird",
            "yellow-rumped_warbler", "lazuli_bunting", "pied-billed_grebe", "ruddy_duck",
            "red_crossbill", "rose-breasted_grosbeak"
        };
        
        int successCount = 0;
        for (String birdName : birdNames) {
            try {
                BufferedImage img = ImageIO.read(Panel.class.getResource("/birds/" + birdName + ".png"));
                if (img == null) {
                    Path p1 = Paths.get("birds", birdName + ".png");
                    Path p2 = Paths.get("birds", birdName + ".PNG");
                    if (Files.exists(p1)) {
                        img = ImageIO.read(p1.toFile());
                    } else if (Files.exists(p2)) {
                        img = ImageIO.read(p2.toFile());
                    } else {
                        File[] matches = Paths.get("birds").toFile().listFiles((dir, name) -> name.toLowerCase().startsWith(birdName.toLowerCase()));
                        if (matches != null && matches.length > 0) {
                            img = ImageIO.read(matches[0]);
                        }
                    }
                }
                if (img != null) {
                    birdImages.put(birdName, img);
                    successCount++;
                }
            } catch (Exception e) {
                // Skip birds without images
            }
        }

    } catch (Exception e) {
        System.out.println("ERROR loading bird images:");
        e.printStackTrace();
    }
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
        if(invertebretoken == null) {
            Path p = Paths.get("Images","invertebratetoken.png");
            if(Files.exists(p)) {
                BufferedImage img = ImageIO.read(p.toFile());
                miscpics.put("invertebratetoken", trimTransparent(img));
            }
        }
        BufferedImage goButton = ImageIO.read(Panel.class.getResource("/Images/gobutton.png"));
        if(goButton != null) {
            goButton = trimTransparent(goButton);
            miscpics.put("gobutton", goButton);
        }
        if(goButton == null) {
            Path p = Paths.get("Images","gobutton.png");
            if(Files.exists(p)) {
                BufferedImage img = ImageIO.read(p.toFile());
                miscpics.put("gobutton", trimTransparent(img));
            }
        }
        BufferedImage wheattoken = ImageIO.read(Panel.class.getResource("/Images/wheattoken.png"));
        if(wheattoken != null) {
            wheattoken = trimTransparent(wheattoken);
            miscpics.put("wheattoken", wheattoken);
        }
        if(wheattoken == null) {
            Path p = Paths.get("Images","wheattoken.png");
            if(Files.exists(p)) {
                BufferedImage img = ImageIO.read(p.toFile());
                miscpics.put("wheattoken", trimTransparent(img));
            }
        }
        BufferedImage fishtoken = ImageIO.read(Panel.class.getResource("/Images/fishtoken.png"));
        if(fishtoken != null) {
            fishtoken = trimTransparent(fishtoken);
            miscpics.put("fishtoken", fishtoken);
        }
        if(fishtoken == null) {
            Path p = Paths.get("Images","fishtoken.png");
            if(Files.exists(p)) {
                BufferedImage img = ImageIO.read(p.toFile());
                miscpics.put("fishtoken", trimTransparent(img));
            }
        }
        BufferedImage foodtoken = ImageIO.read(Panel.class.getResource("/Images/foodtoken.png"));
        if(foodtoken != null) {
            foodtoken = trimTransparent(foodtoken);
            miscpics.put("foodtoken", foodtoken);
        }
        if(foodtoken == null) {
            Path p = Paths.get("Images","foodtoken.png");
            if(Files.exists(p)) {
                BufferedImage img = ImageIO.read(p.toFile());
                miscpics.put("foodtoken", trimTransparent(img));
            }
        }
        BufferedImage rattoken = ImageIO.read(Panel.class.getResource("/Images/rattoken.png"));
        if(rattoken != null) {
            rattoken = trimTransparent(rattoken);
            miscpics.put("rattoken", rattoken);
        }
        BufferedImage board = ImageIO.read(Panel.class.getResource("/Images/Board.png"));
        if(board != null) {
            miscpics.put("board", board);
        }
        BufferedImage goBtnImg = ImageIO.read(Panel.class.getResource("/Images/gobutton.png"));
        if(goBtnImg != null) {
            miscpics.put("goBtnImg", goBtnImg);
        }
        BufferedImage scoreboard = ImageIO.read(Panel.class.getResource("/Images/scoreboard.png"));
        if(scoreboard != null) {
            miscpics.put("scoreboard", scoreboard);
        }
        if(rattoken == null) {
            Path p = Paths.get("Images","rattoken.png");
            if(Files.exists(p)) {
                BufferedImage img = ImageIO.read(p.toFile());
                miscpics.put("rattoken", trimTransparent(img));
            }
        }
        if(!miscpics.containsKey("board")) {
            Path p = Paths.get("Images","Board.png");
            if(Files.exists(p)) {
                BufferedImage img = ImageIO.read(p.toFile());
                miscpics.put("board", img);
            }
        }
        if(!miscpics.containsKey("goBtnImg")) {
            Path p = Paths.get("Images","gobutton.png");
            if(Files.exists(p)) {
                BufferedImage img = ImageIO.read(p.toFile());
                miscpics.put("goBtnImg", img);
            }
        }
        if(!miscpics.containsKey("scoreboard")) {
            Path p = Paths.get("Images","scoreboard.png");
            if(Files.exists(p)) {
                BufferedImage img = ImageIO.read(p.toFile());
                miscpics.put("scoreboard", img);
            }
        }
    }
    catch (Exception e)
    {
        System.out.println("ERROR loading images:");
        e.printStackTrace();
    }
}

private void loadBonusCardImages() {
    try {
        bonusImages.clear();
        // First try loading bundled resources (works inside JAR)
        for(String[] def : BONUS_DEFS) {
            String key = def[0];
            BufferedImage img = null;
            try (InputStream in = Panel.class.getResourceAsStream("/bonuscards/" + key + ".png")) {
                if(in != null) {
                    img = ImageIO.read(in);
                }
            } catch (Exception readEx) {
                System.out.println("ERROR reading bundled bonus image " + key);
                readEx.printStackTrace();
            }
            if(img == null) {
                img = tryLoadBonusFromDisk(key);
            }
            if(img != null) {
                bonusImages.put(key, img);
            } else {
                System.out.println("BONUS IMAGE MISSING: " + key);
            }
        }
        // Also pick up any extra PNGs in the bonuscards folder (keeps dev flow intact)
        Path dir = Paths.get("bonuscards");
        if(Files.exists(dir)) {
            try (Stream<Path> paths = Files.list(dir)) {
                paths.forEach(p -> {
                    try {
                        if(p.getFileName().toString().toLowerCase().endsWith(".png")) {
                            String key = p.getFileName().toString().toLowerCase().replace(".png","").replace(" copy","");
                            if(!bonusImages.containsKey(key)) {
                                BufferedImage img = ImageIO.read(p.toFile());
                                bonusImages.put(key, img);
                            }
                        }
                    } catch (Exception ex) {
                        System.out.println("ERROR loading bonus image " + p.getFileName());
                    }
                });
            }
        }
        System.out.println("Loaded bonus images: " + bonusImages.keySet());
    } catch (Exception ex) {
        System.out.println("ERROR loading bonus images:");
        ex.printStackTrace();
    }
}

private BufferedImage tryLoadBonusFromDisk(String key) {
    try {
        Path dir = Paths.get("bonuscards");
        Path p1 = dir.resolve(key + ".png");
        Path p2 = dir.resolve(key + ".PNG");
        if(Files.exists(p1)) {
            return ImageIO.read(p1.toFile());
        }
        if(Files.exists(p2)) {
            return ImageIO.read(p2.toFile());
        }
        if(Files.exists(dir)) {
            try (Stream<Path> paths = Files.list(dir)) {
                Optional<Path> match = paths
                    .filter(p -> p.getFileName().toString().toLowerCase().startsWith(key.toLowerCase()))
                    .findFirst();
                if(match.isPresent()) {
                    return ImageIO.read(match.get().toFile());
                }
            }
        }
    } catch (Exception ex) {
        System.out.println("ERROR loading bonus image " + key + " from disk");
        ex.printStackTrace();
    }
    return null;
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

private void grantBonusChoice(Player player) {
    // unused with new visual picker
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
        b.getImage(), // preserve the image
        0,
        0
    );
}

private static class ZoomTarget {
    final Bird bird;
    final Rectangle button;
    ZoomTarget(Bird b, Rectangle r) { bird = b; button = r; }
}

private Bird makeBird(String name, String sci, String nest, String abilityText, String[] habitats, TreeMap<String,Integer> costs, int points, int eggCapacity, int wingspan) {
    Ability ability = abilityText != null ? new Ability(abilityText, null, null, null) : null;
    TreeMap<String,Integer> costCopy = costs != null ? new TreeMap<String,Integer>(costs) : new TreeMap<String,Integer>();
    
    // Convert bird name to image filename format (lowercase with underscores)
    String imageKey = nameToImageKey(name);
    BufferedImage birdImage = fetchBirdImage(imageKey);
    if(birdImage == null) {
        System.out.println("SKIPPING BIRD (missing image): " + name + " expected key: " + imageKey);
        return null;
    }
    
    return new Bird(name, sci, nest, habitats, ability, costCopy, new TreeMap<String,Integer>(), points, 0, eggCapacity, wingspan, new ArrayList<Bird>(), false, false, null, birdImage, 0, 0);
}

private BufferedImage fetchBirdImage(String imageKey) {
    if(imageKey == null) return null;
    BufferedImage cached = birdImages.get(imageKey);
    if(cached != null) return cached;
    // Try common filename cases
    Path p1 = Paths.get("birds", imageKey + ".png");
    Path p2 = Paths.get("birds", imageKey + ".PNG");
    try {
        if(Files.exists(p1)) {
            cached = ImageIO.read(p1.toFile());
        } else if(Files.exists(p2)) {
            cached = ImageIO.read(p2.toFile());
        } else {
            File[] matches = Paths.get("birds").toFile().listFiles((dir, name) -> name.toLowerCase().startsWith(imageKey.toLowerCase()));
            if (matches != null && matches.length > 0) {
                cached = ImageIO.read(matches[0]);
            }
        }
        if(cached != null) {
            birdImages.put(imageKey, cached);
        } else {
            System.out.println("BIRD IMAGE MISSING: " + imageKey);
        }
    } catch (Exception ex) {
        System.out.println("ERROR loading bird image for " + imageKey);
        ex.printStackTrace();
    }
    return cached;
}

private String nameToImageKey(String birdName) {
    // Convert "American Crow" to "american_crow"
    // Convert "Red-Tailed Hawk" to "red-tailed_hawk"
    return birdName.toLowerCase()
        .replace("'", "")
        .replace(" ", "_")
        .replace("'", "");
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

private void buildBonusDeck() {
    bonusDeck.clear();
    for(String[] def : BONUS_DEFS) {
        String key = def[0];
        String ability = def[1];
        BufferedImage img = bonusImages.get(key);
        if(img == null) continue;
        BonusCard bc = new BonusCard(prettyName(key), "", img, true, true, 0,0,0,0, ability, "count", new TreeMap<Integer,Integer>(), new ArrayList<String>(), null);
        bonusDeck.add(bc);
    }
    Collections.shuffle(bonusDeck, rng);
}

private String prettyName(String key) {
    String[] parts = key.split("_");
    StringBuilder sb = new StringBuilder();
    for(String p : parts) {
        if(p.isEmpty()) continue;
        sb.append(Character.toUpperCase(p.charAt(0))).append(p.substring(1)).append(" ");
    }
    return sb.toString().trim();
}

private void ensureBonusOffer(int playerIndex) {
    if(bonusOfferPlayerIndex == playerIndex && !bonusOffer.isEmpty()) return;
    bonusOffer.clear();
    bonusRects.clear();
    chosenBonus = null;
    bonusOfferPlayerIndex = playerIndex;
    if(bonusDeck.size() < 2) {
        buildBonusDeck();
    }
    for(int i=0;i<2 && !bonusDeck.isEmpty();i++) {
        bonusOffer.add(bonusDeck.remove(0));
    }
}

private void ensureFaceUp() {
    while (faceUpBirds.size() < 3 && !birdDeck.isEmpty()) {
        faceUpBirds.add(drawTopOfDeck());
    }
}

public Bird drawTopOfDeck() {
    if (birdDeck.isEmpty()) return null;
    return birdDeck.remove(0);
}

public int extractThreshold(String text) {
    if(text == null) return 0;
    Scanner sc = new Scanner(text.replaceAll("[^0-9 ]", " "));
    int last = 0;
    while(sc.hasNextInt()) {
        last = sc.nextInt();
    }
    sc.close();
    return last;
}

public String inferHabitatFromAbility(String text, Spot defaultSpot) {
    if(text == null) return defaultSpot != null ? defaultSpot.getHabitat() : "";
    if(text.contains("forest")) return "forest";
    if(text.contains("grassland")) return "grassland";
    if(text.contains("wetland")) return "wetland";
    if(text.contains("this bird")) return defaultSpot != null ? defaultSpot.getHabitat() : "";
    return "";
}

public void addFood(Player player, String type, int amount) {
    if(player == null || type == null || amount <= 0) return;
    player.addFood(type, amount);
    int idx = Player.players().indexOf(player);
    String name = (idx >= 0 && idx < playerNames.size()) ? playerNames.get(idx) : "Player";
    JOptionPane.showMessageDialog(this, name + " gained " + amount + " " + type + ".", "Ability", JOptionPane.INFORMATION_MESSAGE);
}

public void gainAllFish() {
    Player p = Player.players().get(Player.currentPlayerIndex);
    addFood(p, "fish", 3);
}

public void gainFoodFromFeeder(Player player, Bird bird, String type, boolean allowCache) {
    if(player == null) return;
    String foodType = type;
    if(type.contains("/")) {
        String[] options = type.split("/");
        foodType = (String)JOptionPane.showInputDialog(this, "Choose food type:", "Gain food", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if(foodType == null) return;
    }
    addFood(player, foodType, 1);
    if(allowCache && bird != null) {
        int cache = JOptionPane.showConfirmDialog(this, "Cache the " + foodType + " on " + bird.getName() + "?", "Cache?", JOptionPane.YES_NO_OPTION);
        if(cache == JOptionPane.YES_OPTION) {
            bird.cacheFood(foodType, 1);
            player.spendFood(foodType, 1);
        }
    }
}

public void promptAdditionalBirdPlay(Player player, String habitat) {
    if(player == null) return;
    if(habitat == null || habitat.isEmpty()) habitat = "any";
    ArrayList<Bird> hand = player.playerGetHand();
    if(hand == null || hand.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No birds in hand to play.");
        return;
    }
    ArrayList<Bird> options = new ArrayList<Bird>();
    for(Bird b : hand) {
        if("any".equals(habitat)) { options.add(b); continue; }
        for(String h : b.getHabitats()) {
            if(h.equalsIgnoreCase(habitat)) {
                options.add(b);
                break;
            }
        }
    }
    if(options.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No birds in hand that can live in " + habitat + ".");
        return;
    }
    String[] names = options.stream().map(Bird::getName).toArray(String[]::new);
    String choice = (String)JOptionPane.showInputDialog(this, "Choose a bird to play in " + habitat + ":", "Play additional bird", JOptionPane.PLAIN_MESSAGE, null, names, names[0]);
    if(choice == null) return;
    Bird chosen = null;
    for(Bird b : options) if(b.getName().equals(choice)) { chosen = b; break; }
    if(chosen == null) return;
    String targetHabitat = habitat;
    if("any".equalsIgnoreCase(habitat) || habitat.isEmpty()) {
        String[] optionsHab = chosen.getHabitats();
        if(optionsHab != null && optionsHab.length > 0) {
            targetHabitat = (String)JOptionPane.showInputDialog(this, "Choose habitat for " + chosen.getName(), "Habitat", JOptionPane.PLAIN_MESSAGE, null, optionsHab, optionsHab[0]);
            if(targetHabitat == null) return;
        }
    }
    Spot open = findOpenSpot(player, targetHabitat);
    if(open == null) {
        JOptionPane.showMessageDialog(this, "No open spots in " + targetHabitat + ".");
        return;
    }
    if(!canAttemptPay(player, chosen)) {
        JOptionPane.showMessageDialog(this, "Not enough food to play " + chosen.getName() + ".");
        return;
    }
    int eggCost = eggsForSlot(open.getIndex());
    if(getTotalEggs(player) < eggCost) {
        JOptionPane.showMessageDialog(this, "Not enough eggs for this slot (cost: " + eggCost + ").");
        return;
    }
    if(!payForBird(player, chosen)) return;
    if(!spendEggs(player, eggCost)) {
        JOptionPane.showMessageDialog(this, "Not enough eggs for this slot (cost: " + eggCost + ").");
        return;
    }
    open.setBird(chosen);
    open.setOccupied(true);
    chosen.setBounds(new Rectangle(open.x1, open.y1, open.getWidth(), open.getHeight()));
    chosen.setLocation(open);
    chosen.setX(open.x1);
    chosen.setY(open.y1);
    chosen.getAbility().setBird(chosen);
    chosen.getAbility().setSpot(open);
    chosen.getAbility().setPlayer(player);
    hand.remove(chosen);
    JOptionPane.showMessageDialog(this, chosen.getName() + " placed in " + habitat + " via ability.");
}

public void moveBirdIfRightmost(Player player, Bird b) {
    if(player == null || b == null) return;
    Spot currentSpot = findSpotForBird(player, b);
    if(currentSpot == null) return;
    ArrayList<Spot> row = player.playerGetBoard().get(currentSpot.getHabitat());
    if(row == null) return;
    for(int i = currentSpot.getIndex()+1; i < row.size(); i++) {
        if(row.get(i).isOccupied()) {
            JOptionPane.showMessageDialog(this, b.getName() + " is not the rightmost bird.");
            return;
        }
    }
    String[] habitats = {"forest","grassland","wetland"};
    String choice = (String)JOptionPane.showInputDialog(this, "Move " + b.getName() + " to which habitat?", "Move bird", JOptionPane.PLAIN_MESSAGE, null, habitats, habitats[0]);
    if(choice == null || choice.equalsIgnoreCase(currentSpot.getHabitat())) return;
    Spot target = findOpenSpot(player, choice);
    if(target == null) {
        JOptionPane.showMessageDialog(this, "No open spot in " + choice + ".");
        return;
    }
    currentSpot.setBird(null);
    currentSpot.setOccupied(false);
    target.setBird(b);
    target.setOccupied(true);
    b.setLocation(target);
    b.setBounds(new Rectangle(target.x1, target.y1, target.getWidth(), target.getHeight()));
    JOptionPane.showMessageDialog(this, b.getName() + " moved to " + choice + ".");
}

public Spot findSpotForBird(Player player, Bird b) {
    if(player == null || b == null) return null;
    HashMap<String, ArrayList<Spot>> board = player.playerGetBoard();
    for(ArrayList<Spot> spots : board.values()) {
        for(Spot s : spots) {
            if(s.getBird() == b) return s;
        }
    }
    return null;
}

public Spot findOpenSpot(Player player, String habitat) {
    if(player == null || habitat == null) return null;
    ArrayList<Spot> spots = player.playerGetBoard().get(capitalizeHabitat(habitat));
    if(spots == null) return null;
    for(Spot s : spots) {
        if(!s.isOccupied()) return s;
    }
    return null;
}

public void giveBonusCardChoice(Player player) {
    if(player == null) return;
    TreeMap<Integer,Integer> scoring = new TreeMap<Integer,Integer>();
    scoring.put(-1, 0);
    BonusCard bonus = new BonusCard("Bonus Card", "", null, false, true, 0, 0, 0, 0, "Drawn bonus", "threshold", scoring, new ArrayList<String>(), player);
    player.playerGetBonus().add(bonus);
    JOptionPane.showMessageDialog(this, "Bonus card added to your collection (placeholder scoring).");
}

public void sharedDrawAndKeepExtra(Player player) {
    if(player == null) return;
    int count = Player.players().size() + 1;
    drawCardsToHand(player, count, "Draw " + count + " cards");
    JOptionPane.showMessageDialog(this, "Cards drawn. Each other player should select one from your hand; keep the extra manually.");
}

public void drawCardsToHand(Player player, int count, String title) {
    if(player == null || count <= 0) return;
    for (int i = 0; i < count; i++) {
        Bird newCard = selectBirdFromMarket(title == null ? "Draw Bird" : title);
        if (newCard != null) {
            player.playerGetHand().add(cloneBird(newCard));
        }
    }
}

public void drawThenDiscard(Player player, int drawCount, int discardCount) {
    if(player == null) return;
    drawCardsToHand(player, drawCount, "Ability draw");
    for(int i = 0; i < discardCount; i++) {
        Bird discard = promptCardFromHand(player, "Choose a card to discard (ability requirement):");
        if(discard != null) {
            player.playerGetHand().remove(discard);
        }
    }
}

public boolean tuckFromHand(Player player, Bird target, int amount) {
    if(player == null || target == null || amount <= 0) return false;
    boolean success = false;
    for(int i = 0; i < amount; i++) {
        Bird chosen = promptCardFromHand(player, "Choose a card to tuck onto " + target.getName());
        if(chosen != null) {
            player.playerGetHand().remove(chosen);
            target.addTucked(chosen);
            success = true;
        }
    }
    return success;
}

public void predatorHunt(Bird hunter, int threshold) {
    if(hunter == null) return;
    Bird card = drawTopOfDeck();
    if(card == null) return;
    if(card.getWingspan() < threshold) {
        hunter.addTucked(card);
        JOptionPane.showMessageDialog(this, hunter.getName() + " tucked " + card.getName() + " (wingspan " + card.getWingspan() + ").");
    } else {
        birdDeck.add(card);
        Collections.shuffle(birdDeck, rng);
        JOptionPane.showMessageDialog(this, hunter.getName() + " failed to hunt (wingspan " + card.getWingspan() + "). Card shuffled back.");
    }
}

public void rollAndCache(Bird bird, String foodType) {
    if(bird == null) return;
    boolean success = new Random().nextBoolean();
    if(success) {
        bird.cacheFood(foodType, 1);
        JOptionPane.showMessageDialog(this, bird.getName() + " cached 1 " + foodType + ".");
    } else {
        JOptionPane.showMessageDialog(this, bird.getName() + " rolled, but no " + foodType + " showed.");
    }
}

public void discardEggForFood(Player player, Bird sourceBird, String foodType) {
    if(player == null) return;
    boolean spent = spendEggsExcluding(player, 1, sourceBird);
    if(spent) {
        addFood(player, foodType, 1);
    } else {
        JOptionPane.showMessageDialog(this, "No eggs available to discard.");
    }
}

private boolean spendEggsExcluding(Player player, int eggsNeeded, Bird exclude) {
    if(eggsNeeded <= 0 || player == null) return true;
    if(getTotalEggs(player) < eggsNeeded) return false;
    int remaining = eggsNeeded;
    for(ArrayList<Spot> spots : player.playerGetBoard().values()) {
        for(Spot s : spots) {
            Bird b = s.getBird();
            if(b == null || b == exclude) continue;
            if(remaining <= 0) break;
            int removed = b.removeEggs(remaining);
            remaining -= removed;
        }
    }
    if(remaining > 0 && exclude != null) {
        remaining -= exclude.removeEggs(remaining);
    }
    return remaining <= 0;
}

public void discardEggDraw(Player player, Bird sourceBird, int count) {
    if(discardEggs(player, sourceBird, 1)) {
        drawCardsToHand(player, count, "Draw after discarding egg");
    } else {
        JOptionPane.showMessageDialog(this, "Could not discard an egg.");
    }
}

public boolean discardEggs(Player player, Bird sourceBird, int count) {
    return spendEggsExcluding(player, count, sourceBird);
}

public void discardFoodAndTuck(Player player, Bird bird, String foodType, int tuckCount) {
    if(player == null || bird == null) return;
    if(!player.spendFood(foodType, 1)) {
        JOptionPane.showMessageDialog(this, "Not enough " + foodType + " to discard.");
        return;
    }
    for(int i = 0; i < tuckCount; i++) {
        Bird card = drawTopOfDeck();
        if(card != null) {
            bird.addTucked(card);
        }
    }
    JOptionPane.showMessageDialog(this, bird.getName() + " tucked " + tuckCount + " card(s).");
}

public void layEggOnBird(Bird bird, int count) {
    if(bird == null || count <= 0) return;
    Player owner = findOwnerOfBird(bird);
    if(owner != null) {
        owner.addStoredEggs(count);
    }
}

public void layEggsOnNest(Player player, String nestType, int amountEach) {
    if(player == null || nestType == null) return;
    int matches = 0;
    for(Bird b : player.getAllPlayedBirds()) {
        if(nestType.equalsIgnoreCase(b.getNestType())) {
            matches++;
        }
    }
    if(matches > 0) {
        player.addStoredEggs(matches * amountEach);
    }
}

public void layEggsOnAny(Player player, int eggs) {
    if(player == null) return;
    placeEggsOnBoard(player, eggs);
}

public void allPlayersLayEgg(String nestType, int eggs, boolean hostExtra) {
    for(Player p : Player.players()) {
        p.addStoredEggs(eggs);
    }
    if(hostExtra) {
        Player host = Player.players().get(Player.currentPlayerIndex);
        if(host != null) {
            host.addStoredEggs(eggs);
        }
    }
}

public void allPlayersGainFood(String type) {
    for(Player p : Player.players()) {
        p.addFood(type, 1);
    }
}

public void allPlayersDraw(int count) {
    for(Player p : Player.players()) {
        drawCardsToHand(p, count, "Global draw");
    }
}

public void tradeWildForAny(Player player) {
    if(player == null) return;
    if(!player.spendFood("wild", 1)) {
        JOptionPane.showMessageDialog(this, "No wild food to trade.");
        return;
    }
    String[] options = {"invertebrate","seed","fish","fruit","rodent"};
    String choice = (String)JOptionPane.showInputDialog(this, "Choose food to receive:", "Trade", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
    if(choice != null) {
        player.addFood(choice, 1);
    }
}

public void fewestWetlandDraw() {
    int min = Integer.MAX_VALUE;
    ArrayList<Player> targets = new ArrayList<Player>();
    for(Player p : Player.players()) {
        int count = 0;
        ArrayList<Spot> wetlands = p.playerGetBoard().get("Wetland");
        if(wetlands != null) {
            for(Spot s : wetlands) if(s.isOccupied()) count++;
        }
        if(count < min) {
            min = count;
            targets.clear();
            targets.add(p);
        } else if(count == min) {
            targets.add(p);
        }
    }
    for(Player p : targets) {
        drawCardsToHand(p, 1, "Fewest wetland draw");
    }
}

public void handlePinkTrigger(String text, Player owner, Bird b) {
    // Hook for between-turn abilities. Currently provide simple logging.
    System.out.println("Pink ability for " + (b != null ? b.getName() : "unknown bird") + ": " + text);
}

// Expose draw helper for other classes
public Bird drawFromDeck() {
    return drawTopOfDeck();
}

// ------------ end of ability helpers ------------

public int calculateScore(Player player) {
    if(player == null) return 0;
    int score = 0;
    for(Bird b : player.getAllPlayedBirds()) {
        score += b.getPoints();
        score += b.getEggCount();
        score += b.getCachedFoodCount();
        score += b.getTuckedCards();
    }
    for(BonusCard bc : player.playerGetBonus()) {
        score += scoreBonusCard(bc, player);
    }
    return score;
}

private int scoreBonusCard(BonusCard bc, Player player) {
    if(bc == null || player == null) return 0;
    String key = bc.getName().toLowerCase().replace(" ","_");
    int count = 0;
    ArrayList<Bird> birds = player.getAllPlayedBirds();
    switch(key) {
        case "anatomist":
            count = (int)birds.stream().filter(b -> hasBodyPart(b.getName())).count();
            break;
        case "backyard_birder":
            count = (int)birds.stream().filter(b -> !b.getNestType().equalsIgnoreCase("wild")).count();
            break;
        case "bird_counter":
            count = birds.size();
            break;
        case "bird_feeder":
            count = (int)birds.stream().filter(b -> b.getCosts().containsKey("seed") || b.getCosts().containsKey("fruit")).count();
            break;
        case "breeding_manager":
            count = (int)birds.stream().filter(b -> b.getEggCapacity() >= 4).count();
            break;
        case "cartographer":
            HashSet<String> full = new HashSet<String>();
            for(Bird b : birds) {
                full.addAll(Arrays.asList(b.getHabitats()));
            }
            count = full.size();
            break;
        case "ecologist":
            count = (int)birds.stream().filter(b -> b.getHabitats().length >= 2 || Arrays.asList(b.getHabitats()).contains("wild")).count();
            break;
        case "enclosure_builder":
            count = (int)birds.stream().filter(b -> b.getNestType().equalsIgnoreCase("cavity") || b.getNestType().equalsIgnoreCase("platform")).count();
            break;
        case "falconer":
            count = (int)birds.stream().filter(b -> b.getCosts().containsKey("rodent") || b.getWingspan() >= 65).count();
            break;
        case "fishery_manager":
            count = (int)birds.stream().filter(b -> b.getCosts().containsKey("fish")).count();
            break;
        case "food_web_expert":
            count = (int)birds.stream().filter(b -> b.getCosts().keySet().size() >= 2 || b.getCosts().containsKey("wild")).count();
            break;
        case "forester":
            count = (int)birds.stream().filter(b -> Arrays.asList(b.getHabitats()).contains("forest")).count();
            break;
        case "historian":
            count = (int)birds.stream().filter(b -> b.getName().contains("'")).count();
            break;
        case "large_bird_specialist":
            count = (int)birds.stream().filter(b -> b.getWingspan() >= 100).count();
            break;
        case "nest_box_builder":
            count = (int)birds.stream().filter(b -> b.getNestType().equalsIgnoreCase("cavity")).count();
            break;
        case "omnivore_specialist":
            count = (int)birds.stream().filter(b -> b.getCosts().containsKey("wild")).count();
            break;
        case "oologist":
            count = (int)birds.stream().filter(b -> b.getEggCapacity() >= 3).count();
            break;
        case "passerine_specialist":
            count = (int)birds.stream().filter(b -> b.getWingspan() < 30).count();
            break;
        case "photographer":
            count = (int)birds.stream().filter(b -> b.getPoints() >= 5).count();
            break;
        case "platform_builder":
            count = (int)birds.stream().filter(b -> b.getNestType().equalsIgnoreCase("platform")).count();
            break;
        case "prairie_manager":
            count = (int)birds.stream().filter(b -> Arrays.asList(b.getHabitats()).contains("grassland")).count();
            break;
        case "rodentologist":
            count = (int)birds.stream().filter(b -> b.getCosts().containsKey("rodent")).count();
            break;
        case "visionary_leader":
            count = (int)birds.stream().filter(b -> b.getPoints() >= 7).count();
            break;
        case "viticulturalist":
            count = (int)birds.stream().filter(b -> b.getCosts().containsKey("fruit")).count();
            break;
        case "wetland_scientist":
            count = (int)birds.stream().filter(b -> Arrays.asList(b.getHabitats()).contains("wetland")).count();
            break;
        case "wildlife_gardener":
            count = (int)birds.stream().filter(b -> b.getNestType().equalsIgnoreCase("bowl") || b.getNestType().equalsIgnoreCase("ground")).count();
            break;
        default:
            count = 0;
    }
    return count * 2; // simple 2 points per qualifying bird/habitat
}

private boolean hasBodyPart(String name) {
    String n = name.toLowerCase();
    String[] parts = {"eye","tail","wing","head","throat","bill","beak","crest","back","breast","crown","foot","leg","cheek"};
    for(String p : parts) if(n.contains(p)) return true;
    return false;
}

public void showScores() {
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < Player.players().size(); i++) {
        Player p = Player.players().get(i);
        String name = (i < playerNames.size()) ? playerNames.get(i) : ("Player " + (i+1));
        int total = calculateScore(p);
        sb.append(name).append(": ").append(total).append(" pts");
        sb.append(" (birds ")
            .append(p.getAllPlayedBirds().stream().mapToInt(Bird::getPoints).sum())
            .append(", eggs ")
            .append(p.getAllPlayedBirds().stream().mapToInt(Bird::getEggCount).sum())
            .append(", cache ")
            .append(p.getAllPlayedBirds().stream().mapToInt(Bird::getCachedFoodCount).sum())
            .append(", tucked ")
            .append(p.getAllPlayedBirds().stream().mapToInt(Bird::getTuckedCards).sum())
            .append(", bonus ")
            .append(p.playerGetBonus().stream().mapToInt(bc -> scoreBonusCard(bc, p)).sum())
            .append(")\n");
    }
    JOptionPane.showMessageDialog(this, sb.toString(), "Scores", JOptionPane.INFORMATION_MESSAGE);
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
            Ability ab = b.getAbility();
            ab.setBird(b);
            ab.setSpot(s);
            ab.setPlayer(Player.players().get(Player.currentPlayerIndex));
            if(ab.getTriggerType() == null || ab.getTriggerType().equalsIgnoreCase("brown")) {
                ab.execute();
            }
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
    list.add(makeBird("Downy Woodpecker","Picoides pubescens","Cavity","WHEN PLAYED: Play an additional bird in your forest. Pay its normal cost.",new String[]{"forest"},cost("invertebrate_or_grain_or_fruit",1),3,2,30));
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
    list.add(makeBird("Black Tern","Chlidonias niger","Wild","WHEN ACTIVATED: Draw 1 card. If you do, discard 1 card from your hand at the end of your turn.",new String[]{"wetland"},cost("invertebrate_or_fish",1),4,2,61));
    list.add(makeBird("Black-Bellied Whistling Duck","Dendrocygna autumnalis","Cavity","WHEN ACTIVATED: Discard 1 seed to tuck 2 cards from the deck behind this bird.",new String[]{"wetland"},cost("seed",2),2,5,76));
    list.add(makeBird("Killdeer","Charadrius vociferus","Ground","WHEN ACTIVATED: Discard 1 egg to draw 2 cards.",new String[]{"wetland","grassland"},cost("invertebrate_or_seed",1),1,3,46));
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
    list.add(makeBird("Black Tern","Chlidonias niger","Wild","WHEN ACTIVATED: Draw 1 card. If you do, discard 1 card at end of turn.",new String[]{"wetland"},cost("invertebrate_or_fish",1),4,2,61));
    list.add(makeBird("Mallard","Anas platyrhynchos","Ground","WHEN ACTIVATED: Draw 1 card.",new String[]{"wetland"},cost("invertebrate",1,"seed",1),0,3,89));
    list.add(makeBird("Killdeer","Charadrius vociferus","Ground","WHEN ACTIVATED: Discard 1 egg to draw 2 cards.",new String[]{"wetland","grassland"},cost("invertebrate_or_seed",1),1,3,46));
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

    // Remove any birds whose images were missing
    list.removeIf(Objects::isNull);
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
       
        // Create GO + Controls buttons; rebuild if size changed so they stay anchored
        if (currentScreen.isEmpty() || panelWidth != startScreenCachedW || panelHeight != startScreenCachedH) {
            currentScreen.clear();
            startScreenCachedW = panelWidth;
            startScreenCachedH = panelHeight;
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
            
            // Create Controls button in the bottom-right corner
            int controlsBtnW = 180;
            int controlsBtnH = 50;
            int controlsBtnMargin = 20;
            int controlsBtnX = panelWidth - controlsBtnW - controlsBtnMargin;
            int controlsBtnY = panelHeight - controlsBtnH - controlsBtnMargin;
            int cbx1 = controlsBtnX * 1000 / Math.max(1, getWidth());
            int cby1 = controlsBtnY * 1000 / Math.max(1, getHeight());
            int cbx2 = (controlsBtnX + controlsBtnW) * 1000 / Math.max(1, getWidth());
            int cby2 = (controlsBtnY + controlsBtnH) * 1000 / Math.max(1, getHeight());
            Button controlsBtn = new Button("CONTROLS", "normal", null, true, true, cbx1, cby1, cbx2, cby2);
            currentScreen.add(controlsBtn);
        }
       
        // Draw button
        for (Button btn : currentScreen) {
            btn.paint(g);
        }
    }
    
    public void controlsScreen(Graphics g) {
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        
        // Draw title at top
        g.setColor(new Color(50, 50, 50));
        g.setFont(new Font("Arial", Font.BOLD, 48));
        FontMetrics fm = g.getFontMetrics();
        String title = "CONTROLS";
        int titleWidth = fm.stringWidth(title);
        g.drawString(title, (panelWidth - titleWidth) / 2, 100);
        
        // Draw controls list
        g.setFont(new Font("Arial", Font.PLAIN, 22));
        g.setColor(new Color(40, 40, 40));
        
        String[] controls = {
            "Right click on birds to lay eggs",
            "Last resort: Press Z to skip turn",
            "Press E to trade 2 food (need at least 2) for any 1 food",
            "Press C to open the controls screen",
            "Click the magnifying glass to zoom in",
            "Click the T on the birds to see tucked birds"
        };
        
        int startY = 200;
        int lineSpacing = 50;
        
        for (int i = 0; i < controls.length; i++) {
            g.drawString(controls[i], 100, startY + i * lineSpacing);
        }
        
        // Draw Go Back button in bottom right
        int btnW = 180;
        int btnH = 50;
        int btnX = panelWidth - btnW - 40;
        int btnY = panelHeight - btnH - 40;
        
        g.setColor(new Color(70, 130, 180));
        g.fillRoundRect(btnX, btnY, btnW, btnH, 10, 10);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        fm = g.getFontMetrics();
        String btnText = "GO BACK";
        int textWidth = fm.stringWidth(btnText);
        int textHeight = fm.getAscent();
        g.drawString(btnText, btnX + (btnW - textWidth) / 2, btnY + (btnH + textHeight) / 2 - 2);
        
        // Store button bounds for click detection
        if (miscellaneousScreen.isEmpty() || !miscellaneousScreen.get(0).getName().equals("BACK_FROM_CONTROLS")) {
            miscellaneousScreen.clear();
            int bx1 = btnX * 1000 / Math.max(1, getWidth());
            int by1 = btnY * 1000 / Math.max(1, getHeight());
            int bx2 = (btnX + btnW) * 1000 / Math.max(1, getWidth());
            int by2 = (btnY + btnH) * 1000 / Math.max(1, getHeight());
            Button backBtn = new Button("BACK_FROM_CONTROLS", "normal", null, true, true, bx1, by1, bx2, by2);
            miscellaneousScreen.add(backBtn);
        }
    }

@Override
    public void paint(Graphics g)
{
    super.paint(g);
        zoomButtons.clear();
        tuckButtons.clear();

        if(showingControls) {
            g.setColor(new Color(200, 220, 235));
            g.fillRect(0, 0, getWidth(), getHeight());
            controlsScreen(g);
            return;
        }

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

        // overlay zoomed bird if active
        if(zoomActive && zoomBird != null && zoomBird.getImage() != null) {
            drawZoomOverlay((Graphics2D)g, zoomBird.getImage(), zoomBird.getName());
        }
        if(tuckViewActive && tuckBird != null) {
            drawTuckedOverlay((Graphics2D)g, tuckBird);
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

        drawScoreboard(g);
        int eggs = getTotalEggs(Player.players().get(pI));
        drawEggCounter(g, eggs);
        drawActionCubeCounter(g, Player.players().get(pI).getRemainingTokens());
        drawBoardZoomButtons((Graphics2D) g, board);
        drawTuckButtons((Graphics2D) g, board);

        displayPlayerHand(g, pI);
        displayPlayerFood(g, pI);
        drawBirdFeeder(g);
        drawPlayerBonusCards(g, Player.players().get(pI));
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

    private void drawBirdFeeder(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int size = 52;
        int padding = 10;
        int cols = 5;
        int x = 20;
        int y = getHeight() - 320;
        g2.setColor(new Color(245, 245, 245, 200));
        g2.fillRoundRect(x - 10, y - 24, cols * (size + padding) + 20, size + 48, 12, 12);
        g2.setColor(Color.DARK_GRAY);
        g2.drawRoundRect(x - 10, y - 24, cols * (size + padding) + 20, size + 48, 12, 12);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("Bird Feeder", x, y - 6);
        for(int i=0;i<feederDice.size();i++) {
            int dx = x + i * (size + padding);
            int dy = y;
            drawDieFace(g2, feederDice.get(i), dx, dy, size);
        }
        if(feederDice.isEmpty()) {
            g2.setColor(Color.RED.darker());
            g2.drawString("Feeder empty - will reroll", x, y + size / 2);
        }
    }

    private void drawDieFace(Graphics2D g2, String face, int x, int y, int size) {
        g2.setColor(new Color(230, 230, 230));
        g2.fillRoundRect(x, y, size, size, 10, 10);
        g2.setColor(Color.GRAY);
        g2.drawRoundRect(x, y, size, size, 10, 10);
        List<String> opts = dieOptions(face);
        int tokenSize = (opts.size() == 1) ? size - 10 : (size - 14) / 2;
        for(int i=0;i<opts.size();i++) {
            String token = mapFoodTypeToToken(opts.get(i));
            BufferedImage img = miscpics.get(token);
            int dx = x + 5 + (i * (tokenSize + 4));
            int dy = y + (size - tokenSize) / 2;
            if(img != null) {
                g2.drawImage(img, dx, dy, tokenSize, tokenSize, null);
            } else {
                g2.setColor(Color.LIGHT_GRAY);
                g2.fillOval(dx, dy, tokenSize, tokenSize);
                g2.setColor(Color.DARK_GRAY);
                g2.drawOval(dx, dy, tokenSize, tokenSize);
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

    private int eggsForSlot(int index) {
        switch(index) {
            case 0: return 0;
            case 1: return 1;
            case 2: return 1;
            case 3: return 2;
            default: return 2;
        }
    }

    private Player findOwnerOfBird(Bird bird) {
        if(bird == null) return null;
        for(Player p : Player.players()) {
            HashMap<String, ArrayList<Spot>> board = p.playerGetBoard();
            if(board == null) continue;
            for(ArrayList<Spot> spots : board.values()) {
                if(spots == null) continue;
                for(Spot s : spots) {
                    if(s != null && bird.equals(s.getBird())) {
                        return p;
                    }
                }
            }
        }
        return null;
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
                takeFoodFromFeeder(player, 1, null, "Take 1 food from the bird feeder.");
                break;
            case 1:
                takeFoodFromFeeder(player, 1, null, "Gain 1 food from the bird feeder.");
                handleCardForFoodTrade(player);
                break;
            case 2:
                takeFoodFromFeeder(player, 2, null, "Gain 2 food from the bird feeder.");
                break;
            case 3:
                takeFoodFromFeeder(player, 2, null, "Gain 2 food from the bird feeder.");
                handleCardForFoodTrade(player);
                break;
            case 4:
                takeFoodFromFeeder(player, 3, null, "Gain 3 food from the bird feeder.");
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
            takeFoodFromFeeder(player, 1, null, "Choose the food gained from the trade:");
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

    private void takeFoodFromFeeder(Player player, int foodCount, Set<String> allowedTypes, String prompt) {
        for(int i=0;i<foodCount;i++) {
            ensureFeederBeforeTake();
            if(maybeOfferFeederReroll()) {
                ensureFeederBeforeTake();
            }
            List<Integer> available = findFeederDice(allowedTypes);
            if(available.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No matching food available in the bird feeder.");
                return;
            }
            String[] choiceLabels = new String[available.size()];
            for(int c=0;c<available.size();c++) {
                int idx = available.get(c);
                choiceLabels[c] = describeDie(idx, feederDice.get(idx));
            }
            String choice = (String)JOptionPane.showInputDialog(this, prompt, "Bird Feeder", JOptionPane.PLAIN_MESSAGE, null, choiceLabels, choiceLabels[0]);
            if(choice == null) return;
            int pickedIdx = parseDieIndex(choice);
            if(pickedIdx < 0 || pickedIdx >= feederDice.size()) return;
            String face = feederDice.remove(pickedIdx);
            feederCup.add(face);
            String chosenFood = pickFoodFromFace(face, allowedTypes);
            if(chosenFood == null) {
                // Put the die back if the player cancels the choice
                feederDice.add(face);
                feederCup.remove(feederCup.size() - 1);
                return;
            }
            player.addFood(chosenFood, 1);
            refillFeederIfEmpty();
        }
    }

    private void ensureFeederBeforeTake() {
        if(feederDice.isEmpty()) {
            refillFeeder();
        }
    }

    private boolean maybeOfferFeederReroll() {
        if(feederDice.isEmpty()) return false;
        if(feederDice.size() == 1) {
            int choice = JOptionPane.showConfirmDialog(this, "Only one die remains. Reroll all 5 dice?", "Reroll feeder", JOptionPane.YES_NO_OPTION);
            if(choice == JOptionPane.YES_OPTION) {
                rerollFeederDice();
                return true;
            }
            return false;
        }
        // Only allow reroll when all dice match
        String first = feederDice.get(0);
        for(String face : feederDice) {
            if(!first.equals(face)) {
                return false;
            }
        }
        int choice = JOptionPane.showConfirmDialog(this, "All dice show " + faceLabel(first) + ". Reroll all 5 dice?", "Reroll feeder", JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.YES_OPTION) {
            rerollFeederDice();
            return true;
        }
        return false;
    }

    private List<Integer> findFeederDice(Set<String> allowedTypes) {
        List<Integer> indices = new ArrayList<Integer>();
        for(int i=0;i<feederDice.size();i++) {
            String face = feederDice.get(i);
            List<String> options = dieOptions(face);
            if(allowedTypes == null) {
                indices.add(i);
            } else {
                for(String opt : options) {
                    if(allowedTypes.contains(opt)) {
                        indices.add(i);
                        break;
                    }
                }
            }
        }
        return indices;
    }

    private String pickFoodFromFace(String face, Set<String> allowedTypes) {
        List<String> options = dieOptions(face);
        ArrayList<String> filtered = new ArrayList<String>();
        if(allowedTypes == null || allowedTypes.isEmpty()) {
            filtered.addAll(options);
        } else {
            for(String opt : options) {
                if(allowedTypes.contains(opt)) filtered.add(opt);
            }
        }
        if(filtered.isEmpty()) return null;
        if(filtered.size() == 1) return filtered.get(0);
        String[] opts = filtered.toArray(new String[0]);
        return (String)JOptionPane.showInputDialog(this, "Choose food from die:", "Choose food", JOptionPane.PLAIN_MESSAGE, null, opts, opts[0]);
    }

    private List<String> dieOptions(String face) {
        ArrayList<String> opts = new ArrayList<String>();
        switch(face) {
            case "grain_or_invertebrate":
                opts.add("grain");
                opts.add("invertebrate");
                break;
            case "fish_or_rodent":
                opts.add("fish");
                opts.add("rodent");
                break;
            default:
                opts.add(face);
                break;
        }
        return opts;
    }

    private String describeDie(int index, String face) {
        return "Die " + (index + 1) + ": " + faceLabel(face);
    }

    private String faceLabel(String face) {
        switch(face) {
            case "grain_or_invertebrate": return "Grain / Invertebrate";
            case "fish_or_rodent": return "Fish / Rodent";
            default: return capitalize(face);
        }
    }

    private int parseDieIndex(String label) {
        if(label == null) return -1;
        try {
            int start = label.indexOf("Die ");
            int colon = label.indexOf(":");
            if(start >= 0 && colon > start) {
                String num = label.substring(start + 4, colon).trim();
                return Integer.parseInt(num) - 1;
            }
        } catch (Exception ex) {
            return -1;
        }
        return -1;
    }

    private String capitalize(String s) {
        if(s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    private String normalizeFoodKey(String key) {
        if(key == null) return null;
        String k = key.toLowerCase(Locale.ROOT).trim();
        if(k.startsWith("seed") || k.startsWith("grain") || k.startsWith("wheat")) return "grain";
        if(k.startsWith("invert")) return "invertebrate";
        if(k.startsWith("rodent")) return "rodent";
        if(k.startsWith("fish")) return "fish";
        if(k.startsWith("fruit") || k.startsWith("berry")) return "fruit";
        if(k.startsWith("wild")) return "wild";
        if(k.equals("no-food")) return "no-food";
        return k;
    }

    private Map<String,Integer> buildNormalizedPool(Player player) {
        Map<String,Integer> pool = new HashMap<String,Integer>();
        if(player == null || player.playerGetFood() == null) return pool;
        for(Map.Entry<String,Integer> e : player.playerGetFood().entrySet()) {
            String norm = normalizeFoodKey(e.getKey());
            if(norm == null) continue;
            pool.put(norm, pool.getOrDefault(norm,0) + (e.getValue()==null?0:e.getValue()));
        }
        return pool;
    }

    private int countNonWildKeys(TreeMap<String,Integer> costs) {
        int count = 0;
        for(Map.Entry<String,Integer> e : costs.entrySet()) {
            String k = normalizeFoodKey(e.getKey());
            if(k == null) continue;
            if(k.equals("wild") || k.equals("no-food")) continue;
            count++;
        }
        return count;
    }

    private int totalFood(Map<String,Integer> pool) {
        int sum = 0;
        if(pool == null) return 0;
        for(Integer v : pool.values()) {
            sum += (v == null ? 0 : v);
        }
        return sum;
    }

    private void tradeTwoFoodForAny(Player player) {
        if(player == null) return;
        Map<String,Integer> pool = buildNormalizedPool(player);
        if(totalFood(pool) < 2) {
            JOptionPane.showMessageDialog(this, "You need at least 2 food to trade.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Trade 2 food for any 1 food?", "Trade food", JOptionPane.YES_NO_OPTION);
        if(confirm != JOptionPane.YES_OPTION) return;
        for(int i=0;i<2;i++) {
            String choice = chooseAnyFromPool(pool, "Choose food to trade (" + (2 - i) + " left):");
            if(choice == null) {
                JOptionPane.showMessageDialog(this, "Not enough food to trade.");
                return;
            }
            if(!spendFromPool(player, pool, choice)) {
                JOptionPane.showMessageDialog(this, "Not enough " + choice + " to trade.");
                return;
            }
        }
        String[] gainOpts = {"grain","invertebrate","rodent","fish","fruit","wild"};
        String gain = (String)JOptionPane.showInputDialog(this, "Choose food to gain:", "Gain food", JOptionPane.PLAIN_MESSAGE, null, gainOpts, gainOpts[0]);
        if(gain == null) return;
        player.addFood(gain, 1);
        JOptionPane.showMessageDialog(this, "Traded 2 food for 1 " + gain + ".");
    }

    private boolean hasOrCost(TreeMap<String,Integer> costs) {
        if(costs == null) return false;
        for(Map.Entry<String,Integer> e : costs.entrySet()) {
            if(!parseOrOptions(e.getKey()).isEmpty()) return true;
        }
        return false;
    }

    private List<String> parseOrOptions(String keyRaw) {
        if(keyRaw == null) return Collections.emptyList();
        ArrayList<String> out = new ArrayList<String>();

        // Break on explicit "_or_" separators first
        String lower = keyRaw.toLowerCase(Locale.ROOT);
        String[] primaryParts = lower.split("_or_");

        for(String part : primaryParts) {
            if(part == null || part.isEmpty()) continue;
            // Also split compact combos like "seed_or_fruit_invertebrate"
            String[] subParts = part.split("_");
            for(String sub : subParts) {
                if(sub == null || sub.isEmpty()) continue;
                if(sub.equals("or")) continue;
                String norm = normalizeFoodKey(sub);
                if(norm == null || norm.equals("no-food")) continue;
                if(!out.contains(norm)) out.add(norm);
            }
        }

        // If nothing matched but there are underscores, treat every token as an OR option.
        if(out.isEmpty() && lower.contains("_")) {
            String[] subParts = lower.split("_");
            for(String sub : subParts) {
                if(sub == null || sub.isEmpty() || sub.equals("or")) continue;
                String norm = normalizeFoodKey(sub);
                if(norm == null || norm.equals("no-food")) continue;
                if(!out.contains(norm)) out.add(norm);
            }
        }

        return out;
    }

    private boolean consumeFromPool(Map<String,Integer> pool, String type) {
        if(type == null || pool == null) return false;
        int have = pool.getOrDefault(type, 0);
        if(have <= 0) return false;
        pool.put(type, have - 1);
        return true;
    }

    private boolean spendFromPool(Player player, Map<String,Integer> pool, String type) {
        if(type == null || player == null) return false;
        if(!consumeFromPool(pool, type)) return false;
        player.spendFood(type, 1);
        return true;
    }

    private boolean canPayAnd(Map<String,Integer> pool, TreeMap<String,Integer> costs) {
        if(pool == null) return false;
        Map<String,Integer> sim = new HashMap<String,Integer>(pool);
        int wildSlots = costs.getOrDefault("wild", 0);
        ArrayList<Map.Entry<String,Integer>> orEntries = new ArrayList<Map.Entry<String,Integer>>();
        for (Map.Entry<String,Integer> e : costs.entrySet()) {
            List<String> opts = parseOrOptions(e.getKey());
            if(opts != null && !opts.isEmpty()) {
                orEntries.add(e);
                continue;
            }
            String key = normalizeFoodKey(e.getKey());
            if(key == null || key.equals("wild") || key.equals("no-food")) continue;
            int amt = e.getValue() == null ? 0 : e.getValue();
            for(int i=0;i<amt;i++) {
                if(consumeFromPool(sim, key)) continue;
                if(consumeFromPool(sim, "wild")) continue;
                return false;
            }
        }
        for(Map.Entry<String,Integer> e : orEntries) {
            List<String> opts = parseOrOptions(e.getKey());
            int amt = e.getValue() == null ? 0 : e.getValue();
            for(int i=0;i<amt;i++) {
                String choice = chooseAvailable(sim, opts);
                if(choice == null) return false;
                consumeFromPool(sim, choice);
            }
        }
        for(int i=0;i<wildSlots;i++) {
            String any = chooseAny(sim);
            if(any == null) return false;
            consumeFromPool(sim, any);
        }
        return true;
    }

    private boolean canPaySingle(Map<String,Integer> pool, TreeMap<String,Integer> costs) {
        if(pool == null) return false;
        if(!hasOrCost(costs)) return false;
        ArrayList<String> choices = collectChoiceTypes(costs);
        for(String c : choices) {
            int need = amountForChoice(costs, c);
            if(need <= 0) need = 1;
            Map<String,Integer> sim = new HashMap<String,Integer>(pool);
            boolean ok = true;
            for(int i=0;i<need;i++) {
                if(consumeFromPool(sim, c)) continue;
                if(consumeFromPool(sim, "wild")) continue;
                ok = false; break;
            }
            if(!ok) continue;
            int wildSlots = costs.getOrDefault("wild", 0);
            for(int i=0;i<wildSlots;i++) {
                String any = chooseAny(sim);
                if(any == null) { ok = false; break; }
                consumeFromPool(sim, any);
            }
            if(ok) return true;
        }
        return false;
    }

    private String chooseAvailable(Map<String,Integer> pool, List<String> opts) {
        for(String o : opts) {
            if(pool.getOrDefault(o,0) > 0) return o;
        }
        if(pool.getOrDefault("wild",0) > 0) return "wild";
        return null;
    }

    private String chooseAny(Map<String,Integer> pool) {
        for(Map.Entry<String,Integer> e : pool.entrySet()) {
            if(e.getValue() > 0) return e.getKey();
        }
        return null;
    }

    private ArrayList<String> collectChoiceTypes(TreeMap<String,Integer> costs) {
        ArrayList<String> out = new ArrayList<String>();
        for(Map.Entry<String,Integer> e : costs.entrySet()) {
            String raw = e.getKey();
            if(raw == null) continue;
            if(raw.equals("wild") || raw.equals("no-food")) continue;
            List<String> opts = parseOrOptions(raw);
            if(opts != null && !opts.isEmpty()) {
                for(String o : opts) if(!out.contains(o)) out.add(o);
            }
        }
        return out;
    }

    private int amountForChoice(TreeMap<String,Integer> costs, String choice) {
        String normChoice = normalizeFoodKey(choice);
        int amt = 0;
        for(Map.Entry<String,Integer> e : costs.entrySet()) {
            String raw = e.getKey();
            if(raw == null) continue;
            List<String> opts = parseOrOptions(raw);
            if(opts != null && !opts.isEmpty() && opts.contains(normChoice)) {
                amt = Math.max(amt, e.getValue() == null ? 0 : e.getValue());
            }
        }
        return amt;
    }

    private String chooseFromPool(Map<String,Integer> pool, List<String> opts, String prompt) {
        ArrayList<String> available = new ArrayList<String>();
        for(String o : opts) {
            if(pool.getOrDefault(o,0) > 0) available.add(o);
        }
        if(pool.getOrDefault("wild",0) > 0) available.add("wild");
        if(available.isEmpty()) return null;
        if(available.size() == 1) return available.get(0);
        String[] choices = available.toArray(new String[0]);
        return (String)JOptionPane.showInputDialog(this, prompt, "Pay cost", JOptionPane.PLAIN_MESSAGE, null, choices, choices[0]);
    }

    private String chooseAnyFromPool(Map<String,Integer> pool, String prompt) {
        ArrayList<String> available = new ArrayList<String>();
        for(Map.Entry<String,Integer> e : pool.entrySet()) {
            if(e.getValue() > 0) available.add(e.getKey());
        }
        if(available.isEmpty()) return null;
        if(available.size() == 1) return available.get(0);
        String[] choices = available.toArray(new String[0]);
        return (String)JOptionPane.showInputDialog(this, prompt, "Pay cost", JOptionPane.PLAIN_MESSAGE, null, choices, choices[0]);
    }

    private void initFeeder() {
        feederDice.clear();
        feederCup.clear();
        for(int i=0;i<5;i++) {
            feederDice.add(rollFeederDie());
        }
    }

    private void rerollFeederDice() {
        feederCup.clear();
        feederDice.clear();
        for(int i=0;i<5;i++) {
            feederDice.add(rollFeederDie());
        }
    }

    private void refillFeederIfEmpty() {
        if(feederDice.isEmpty()) {
            refillFeeder();
        }
    }

    private void refillFeeder() {
        feederCup.clear();
        feederDice.clear();
        for(int i=0;i<5;i++) {
            feederDice.add(rollFeederDie());
        }
        JOptionPane.showMessageDialog(this, "Bird feeder was empty. Rerolled all 5 dice.");
    }

    private String rollFeederDie() {
        int idx = rng.nextInt(FEEDER_FACES.length);
        return FEEDER_FACES[idx];
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

    public int placeEggsOnBoard(Player player, int eggsToPlace) {
        if (player == null || eggsToPlace <= 0) return 0;
        player.addStoredEggs(eggsToPlace);
        return eggsToPlace;
    }

    private boolean handleRightClickEggPlacement(Point p) {
        Player current = Player.players().get(Player.currentPlayerIndex);
        if(current == null) return false;
        if(turnActionClaimed) {
            JOptionPane.showMessageDialog(this, "You've already taken an action this turn.");
            return true;
        }
        if(current.getRemainingTokens() <= 0) {
            JOptionPane.showMessageDialog(this, "No action tokens remaining to lay eggs.");
            return true;
        }

        HashMap<String, ArrayList<Spot>> board = Player.getCurrentPlayerBoard();
        if (board == null) return false;
        for (ArrayList<Spot> spots : board.values()) {
            if (spots == null) continue;
            for (Spot s : spots) {
                Rectangle r = new Rectangle(s.x1, s.y1, s.getWidth(), s.getHeight());
                if (r.contains(p) && s.getBird() != null) {
                    if(promptPlaceEggsOnBird(s.getBird())) {
                        current.useActionToken();
                        turnActionClaimed = true;
                        checkRoundEndAfterAction();
                        if(!roundOver) advanceTurn(); else repaint();
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private boolean promptPlaceEggsOnBird(Bird bird) {
        if (bird == null) return false;
        int capacity = bird.getEggCapacity();
        int space = capacity > 0 ? Math.max(0, capacity - bird.getEggCount()) : 0;
        if (capacity > 0 && space <= 0) {
            JOptionPane.showMessageDialog(this, bird.getName() + " has no egg space left.");
            return false;
        }
        int maxPlace = capacity > 0 ? space : 10; // fallback cap
        String raw = JOptionPane.showInputDialog(this, "Place eggs on " + bird.getName() + " (1-" + maxPlace + "):", "1");
        if (raw == null) return false;
        int toPlace = 1;
        try { toPlace = Integer.parseInt(raw); } catch (Exception ex) {}
        if (toPlace < 1) toPlace = 1;
        if (capacity > 0 && toPlace > space) toPlace = space;
        bird.addEggs(toPlace);
        return true;
    }

    public boolean spendEggs(Player player, int eggsNeeded) {
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

    public Bird promptCardFromHand(Player player, String title) {
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

public void drawCards(Player player, int count) {
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
        if (gameFinished) {
            JOptionPane.showMessageDialog(this, "The game is over. Check the scoreboard for the winner.");
            return;
        }
        if (roundOver) {
            JOptionPane.showMessageDialog(this, "Round over. Click Next Round to continue.");
            return;
        }

        if (turnActionClaimed) {
            JOptionPane.showMessageDialog(this, "You've already taken an action this turn.");
            return;
        }

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
            Player current = Player.players().get(Player.currentPlayerIndex);
            if (current.getRemainingTokens() <= 0) {
                JOptionPane.showMessageDialog(this, "No action tokens remaining for this player.");
                return;
            }
            if (current.useActionToken()) {
                turnActionClaimed = true;
                resolveHabitatAction(spot);
                activateHabitatAbilities(spot.getHabitat());
                checkRoundEndAfterAction();
                if(!roundOver) {
                    advanceTurn();
                } else {
                    repaint();
                }
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
                if(!canPlaceInHabitat(chosen, spot)) {
                    JOptionPane.showMessageDialog(this, "This bird can only be placed in " + String.join("/", chosen.getHabitats()) + ".");
                    return;
                }
                if (!canAttemptPay(Player.players().get(Player.currentPlayerIndex), chosen)) {
                    JOptionPane.showMessageDialog(this, "Not enough food to play this bird.");
                    return;
                }
                int eggCost = eggsForSlot(spot.getIndex());
                if(getTotalEggs(Player.players().get(Player.currentPlayerIndex)) < eggCost) {
                    JOptionPane.showMessageDialog(this, "Not enough eggs for this slot (cost: " + eggCost + ").");
                    return;
                }
                if(!payForBird(Player.players().get(Player.currentPlayerIndex), chosen)) {
                    return;
                }
                if(!spendEggs(Player.players().get(Player.currentPlayerIndex), eggCost)) {
                    JOptionPane.showMessageDialog(this, "Not enough eggs for this slot (cost: " + eggCost + ").");
                    return;
                }
                current.useActionToken();
                turnActionClaimed = true;
                spot.setBird(chosen);
                spot.setOccupied(true);
                chosen.setBounds(new Rectangle(spot.x1, spot.y1, spot.getWidth(), spot.getHeight()));
                chosen.setLocation(spot);
                hand.remove(chosen);
                JOptionPane.showMessageDialog(this, chosen.getName() + " placed in " + spot.getHabitat() + ".");
                if(chosen.getAbility() != null) {
                    Ability ab = chosen.getAbility();
                    ab.setBird(chosen);
                    ab.setSpot(spot);
                    ab.setPlayer(current);
                    if(ab.getTriggerType() == null || "white".equalsIgnoreCase(ab.getTriggerType())) {
                        ab.execute();
                    }
                }
                activateHabitatAbilities(spot.getHabitat());
                checkRoundEndAfterAction();
                if(!roundOver) {
                    advanceTurn();
                } else {
                    repaint();
                }
            }
        }
    }

    private void drawPlayerBonusCards(Graphics g, Player player) {
        ArrayList<BonusCard> cards = player.playerGetBonus();
        if(cards == null || cards.isEmpty()) return;
        int sizeW = 110;
        int sizeH = 160;
        int x = getWidth() - sizeW - 32;
        int y = 160;
        int gap = 12;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        for(int i=0;i<cards.size();i++) {
            BonusCard bc = cards.get(i);
            BufferedImage img = bc.getImage();
            if(img != null) {
                g2.drawImage(img, x, y + i*(sizeH+gap), sizeW, sizeH, null);
            } else {
                g2.setColor(new Color(235,235,235));
                g2.fillRoundRect(x, y + i*(sizeH+gap), sizeW, sizeH, 8,8);
                g2.setColor(Color.DARK_GRAY);
                g2.drawRoundRect(x, y + i*(sizeH+gap), sizeW, sizeH, 8,8);
                g2.drawString(bc.getName(), x+8, y + i*(sizeH+gap)+20);
            }
        }
    }

    private void advanceTurn() {
        if(roundOver || gameFinished) {
            Player.currentPlayerIndex = 0;
        } else {
            Player.currentPlayerIndex = (Player.currentPlayerIndex + 1) % Player.players().size();
        }
        turnActionClaimed = false;
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

    private void drawBoardZoomButtons(Graphics2D g2, HashMap<String, ArrayList<Spot>> board) {
        if (board == null) return;
        for (ArrayList<Spot> spots : board.values()) {
            if (spots == null) continue;
            for (Spot s : spots) {
                Bird b = s.getBird();
                if (b == null || b.getImage() == null) continue;
                int btnSize = Math.max(16, s.getWidth() / 8);
                int bx = s.x1 + s.getWidth() - btnSize - 4;
                int by = s.y1 + 4;
                g2.setColor(new Color(0,0,0,140));
                g2.fillRoundRect(bx, by, btnSize, btnSize, 6, 6);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2f));
                g2.drawOval(bx + 4, by + 4, btnSize - 8, btnSize - 8);
                g2.drawLine(bx + btnSize - 6, by + btnSize - 6, bx + btnSize - 2, by + btnSize - 2);
                zoomButtons.add(new ZoomTarget(b, new Rectangle(bx, by, btnSize, btnSize)));
            }
        }
    }

    private void drawTuckButtons(Graphics2D g2, HashMap<String, ArrayList<Spot>> board) {
        if(board == null) return;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (ArrayList<Spot> spots : board.values()) {
            if (spots == null) continue;
            for (Spot s : spots) {
                Bird b = s.getBird();
                if (b == null) continue;
                if (b.getTuckedCards() <= 0) continue;
                int btnSize = Math.max(16, s.getWidth() / 10);
                int bx = s.x1 + 4;
                int by = s.y1 + 4;
                g2.setColor(new Color(0,0,0,160));
                g2.fillRoundRect(bx, by, btnSize, btnSize, 6, 6);
                g2.setColor(new Color(255, 255, 255));
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(bx, by, btnSize, btnSize, 6, 6);
                g2.drawString("T", bx + btnSize/2 - 4, by + btnSize/2 + 5);
                tuckButtons.add(new ZoomTarget(b, new Rectangle(bx, by, btnSize, btnSize)));
            }
        }
    }

    private void drawActionCubeCounter(Graphics g, int cubeCount) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int boxW = 150;
        int boxH = 60;
        int x = getWidth() - boxW - 20;
        int y = 90;

        g2.setColor(new Color(235, 245, 255, 215));
        g2.fillRoundRect(x, y, boxW, boxH, 10, 10);
        g2.setColor(new Color(40, 90, 140));
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(x, y, boxW, boxH, 10, 10);

        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("Action Cubes", x + 12, y + 22);

        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.drawString(cubeCount + "/8", x + 12, y + 46);

        int cubeSize = 18;
        int cubeX = x + boxW - cubeSize - 16;
        int cubeY = y + 16;
        g2.setColor(new Color(80, 140, 200));
        g2.fillRoundRect(cubeX, cubeY, cubeSize, cubeSize, 5, 5);
        g2.setColor(new Color(30, 60, 100));
        g2.drawRoundRect(cubeX, cubeY, cubeSize, cubeSize, 5, 5);
    }

    private void drawScoreboard(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int x = 20;
        int y = 20;
        int sbW = 260;
        int sbH = 180;
        BufferedImage sbImg = miscpics.get("scoreboard");
        if(sbImg != null) {
            g2.drawImage(sbImg, x, y, sbW, sbH, null);
        } else {
            g2.setColor(new Color(255, 255, 255, 200));
            g2.fillRoundRect(x, y, sbW, sbH, 12, 12);
            g2.setColor(new Color(60, 60, 60));
            g2.drawRoundRect(x, y, sbW, sbH, 12, 12);
        }

        if(gameFinished && !roundScores.isEmpty()) {
            int[] finals = roundScores.get(roundScores.size() - 1);
            int bestIdx = 0;
            int best = finals[0];
            for(int i = 1; i < finals.length; i++) {
                if(finals[i] > best) {
                    best = finals[i];
                    bestIdx = i;
                }
            }
            String winnerName = (bestIdx < playerNames.size()) ? playerNames.get(bestIdx) : "Player " + (bestIdx + 1);
            g2.setFont(new Font("Arial", Font.BOLD, 18));
            g2.setColor(new Color(90, 30, 30));
            g2.drawString("Winner: " + winnerName + " (" + best + ")", x, Math.max(18, y - 6));
        }

        g2.setColor(new Color(40, 40, 40));
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("Round " + currentRound + " / " + maxRounds, x + 14, y + 24);

        int[] latestScores = roundScores.isEmpty() ? null : roundScores.get(roundScores.size() - 1);
        int recordedRound = roundScores.size();
        int textY = y + 52;
        for(int i = 0; i < Player.players().size(); i++) {
            String name = (i < playerNames.size()) ? playerNames.get(i) : "Player " + (i + 1);
            int liveScore = calculateScore(Player.players().get(i));
            StringBuilder line = new StringBuilder(name + ": " + liveScore);
            if(latestScores != null) {
                line.append(" (R").append(recordedRound).append(": ").append(latestScores[i]).append(")");
            }
            g2.drawString(line.toString(), x + 14, textY);
            textY += 18;
        }

        int btnW = sbW;
        int btnH = 36;
        int btnX = x;
        int btnY = y + sbH + 10;
        nextRoundButton = new Rectangle(btnX, btnY, btnW, btnH);
        boolean canAdvance = roundOver && !gameFinished && currentRound < maxRounds;
        String btnText;
        if(gameFinished || currentRound >= maxRounds) {
            btnText = gameFinished ? "Game Complete" : "Final Round";
        } else if(roundOver) {
            btnText = "Next Round";
        } else {
            btnText = "Play out the round";
        }
        g2.setColor(canAdvance ? new Color(90, 160, 110) : new Color(190, 190, 190, 200));
        g2.fillRoundRect(btnX, btnY, btnW, btnH, 10, 10);
        g2.setColor(new Color(60, 60, 60));
        g2.drawRoundRect(btnX, btnY, btnW, btnH, 10, 10);
        g2.setFont(new Font("Arial", Font.BOLD, 15));
        g2.drawString(btnText, btnX + 12, btnY + 24);
    }

    private void recordRoundScores() {
        int[] scores = new int[Player.players().size()];
        for(int i = 0; i < Player.players().size(); i++) {
            scores[i] = calculateScore(Player.players().get(i));
        }
        if(roundScores.size() >= currentRound) {
            roundScores.set(currentRound - 1, scores);
        } else {
            roundScores.add(scores);
        }
    }

    private void clearActionTokens(HashMap<String, ArrayList<Spot>> board) {
        if(board == null) return;
        for(ArrayList<Spot> spots : board.values()) {
            if(spots == null) continue;
            for(Spot s : spots) {
                if(s != null) s.setActionToken(false);
            }
        }
    }

    private void checkRoundEndAfterAction() {
        boolean allOut = true;
        for(Player p : Player.players()) {
            if(p.getRemainingTokens() > 0) {
                allOut = false;
                break;
            }
        }
        if(allOut && !roundOver) {
            recordRoundScores();
            roundOver = true;
            if(currentRound >= maxRounds) {
                gameFinished = true;
            }
            Player.currentPlayerIndex = 0;
        }
    }

    private void startNextRound() {
        if(!roundOver || gameFinished || currentRound >= maxRounds) {
            return;
        }
        currentRound++;
        roundOver = false;
        for(Player p : Player.players()) {
            p.resetTokens(8);
            clearActionTokens(p.playerGetBoard());
        }
        Player.currentPlayerIndex = 0;
        repaint();
    }

public boolean canAttemptPay(Player player, Bird bird) {
    if (player == null || bird == null) return false;
    return hasFoodForCost(player, bird.getCosts());
}

    private boolean canPlaceInHabitat(Bird bird, Spot spot) {
        if (bird == null || spot == null) return false;
        String spotHab = spot.getHabitat() == null ? "" : spot.getHabitat().toLowerCase();
        String[] allowed = bird.getHabitats();
        if (allowed == null || allowed.length == 0) return false;
        for (String h : allowed) {
            if (h == null) continue;
            String hl = h.toLowerCase();
            if (hl.equals("wild") || hl.equals(spotHab)) return true;
        }
        return false;
    }

    private boolean hasFoodForCost(Player player, TreeMap<String,Integer> costs) {
        if (player == null || costs == null || costs.isEmpty() || costs.containsKey("no-food")) return true;
        Map<String,Integer> pool = buildNormalizedPool(player);
        if (pool.isEmpty()) return false;
        boolean hasOr = hasOrCost(costs);
        if(canPayAnd(new HashMap<String,Integer>(pool), costs)) return true;
        return hasOr && canPaySingle(new HashMap<String,Integer>(pool), costs);
    }

    private int requiredNonWild(TreeMap<String,Integer> costs) {
        int sum = 0;
        for (Map.Entry<String,Integer> e : costs.entrySet()) {
            if (e.getKey().equals("wild") || e.getKey().equals("no-food")) continue;
            sum += (e.getValue() == null ? 0 : e.getValue());
        }
        return sum;
    }

    public boolean payForBird(Player player, Bird bird) {
        if (player == null || bird == null) return false;
        TreeMap<String,Integer> costs = bird.getCosts();
        if (costs == null || costs.isEmpty() || costs.containsKey("no-food")) return true;
        if (!hasFoodForCost(player, costs)) {
            JOptionPane.showMessageDialog(this, "Not enough food to play this bird.");
            return false;
        }

        Map<String,Integer> pool = buildNormalizedPool(player);

        boolean hasOr = hasOrCost(costs);
        boolean multiTypes = countNonWildKeys(costs) > 1;
        int mode = 0;
        if(hasOr && multiTypes) {
            String[] opts = {"Pay printed (AND)", "Pay one listed type (OR)"};
            mode = JOptionPane.showOptionDialog(this,
                "Choose how to pay for " + bird.getName() + "\nPrinted cost: " + costs.toString(),
                "Food payment",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opts,
                opts[0]);
            if(mode < 0) mode = 0;
        }

        if(mode == 1) {
            ArrayList<String> choiceTypes = collectChoiceTypes(costs);
            if(choiceTypes.isEmpty()) mode = 0;
            else {
                String[] cOpts = choiceTypes.toArray(new String[0]);
                String picked = (String)JOptionPane.showInputDialog(this, "Choose one food type to pay:", "OR cost", JOptionPane.PLAIN_MESSAGE, null, cOpts, cOpts[0]);
                if(picked == null) return false;
                int need = amountForChoice(costs, picked);
                if(need <= 0) need = 1;
                for(int i=0;i<need;i++) {
                    if(spendFromPool(player, pool, picked)) continue;
                    if(spendFromPool(player, pool, "wild")) continue;
                    JOptionPane.showMessageDialog(this, "Not enough " + picked + " (or wild) to pay.");
                    return false;
                }
                int wildSlots = costs.getOrDefault("wild", 0);
                for(int i=0;i<wildSlots;i++) {
                    String choice = chooseAnyFromPool(pool, "Choose food to cover wild cost (" + (wildSlots - i) + " left):");
                    if(choice == null) {
                        JOptionPane.showMessageDialog(this, "Not enough food to pay the wild cost.");
                        return false;
                    }
                    spendFromPool(player, pool, choice);
                }
                return true;
            }
        }

        ArrayList<Map.Entry<String,Integer>> orEntries = new ArrayList<Map.Entry<String,Integer>>();
        for (Map.Entry<String,Integer> e : costs.entrySet()) {
            List<String> opts = parseOrOptions(e.getKey());
            if(opts != null && !opts.isEmpty()) {
                orEntries.add(e);
                continue;
            }
            String key = normalizeFoodKey(e.getKey());
            if(key == null) continue;
            if(key.equals("wild") || key.equals("no-food")) continue;
            int amt = e.getValue() == null ? 0 : e.getValue();
            for(int i=0;i<amt;i++) {
                if(spendFromPool(player, pool, key)) continue;
                if(spendFromPool(player, pool, "wild")) continue;
                JOptionPane.showMessageDialog(this, "Not enough " + key + " (or wild) to pay this cost.");
                return false;
            }
        }
        for(Map.Entry<String,Integer> e : orEntries) {
            List<String> opts = parseOrOptions(e.getKey());
            int amt = e.getValue() == null ? 0 : e.getValue();
            for(int i=0;i<amt;i++) {
                String choice = chooseFromPool(pool, opts, "Choose which food to pay (OR cost):");
                if(choice == null) {
                    JOptionPane.showMessageDialog(this, "Not enough food to cover the OR cost.");
                    return false;
                }
                if(!spendFromPool(player, pool, choice)) {
                    JOptionPane.showMessageDialog(this, "Not enough " + choice + " to pay.");
                    return false;
                }
            }
        }
        int wildSlots = costs.getOrDefault("wild", 0);
        for(int i=0;i<wildSlots;i++) {
            String choice = chooseAnyFromPool(pool, "Choose food to cover wild cost (" + (wildSlots - i) + " left):");
            if(choice == null) {
                JOptionPane.showMessageDialog(this, "Not enough food to pay the wild cost.");
                return false;
            }
            spendFromPool(player, pool, choice);
        }
        return true;
    }


    private void finishPlayerSelection() {
        // Store selected items in current player's hand
        ArrayList<Bird> keptBirds = new ArrayList<>();
        Player currentPlayer = Player.players().get(Player.currentPlayerIndex);
        if(chosenBonus == null) {
            JOptionPane.showMessageDialog(this, "Select a bonus card to continue.");
            return;
        }
        chosenBonus.setPlayer(currentPlayer);
        currentPlayer.playerGetBonus().add(chosenBonus);
        // return unused to deck
        for(BonusCard bc : bonusOffer) {
            if(bc != chosenBonus) bonusDeck.add(bc);
        }
        Collections.shuffle(bonusDeck, rng);
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
        bonusOffer.clear();
        bonusRects.clear();
        chosenBonus = null;
        Player.currentPlayerIndex++;

        if (Player.currentPlayerIndex >= 4) {
            Player.currentPlayerIndex = 0;
            startingComplete = true;
            System.out.println("players selected");
            for (Player p : Player.players()) {
                p.resetTokens(8);
                clearActionTokens(p.playerGetBoard());
            }
            currentRound = 1;
            roundOver = false;
            gameFinished = false;
            roundScores.clear();
            nextRoundButton = new Rectangle();
        }
    }
   
    private void maybeCompleteStartingSelection() {
        if(selected.size() >= 5) {
            if(chosenBonus == null) {
                JOptionPane.showMessageDialog(this, "Pick a bonus card before continuing.");
            } else {
                finishPlayerSelection();
            }
        }
    }
   
    public void startingScreen(Graphics g, int playerIndex)
    {
        ensureBonusOffer(playerIndex);
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


        // Draw bonus card choices
        bonusRects.clear();
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.setColor(Color.DARK_GRAY);
        int bonusW = 140;
        int bonusH = 200;
        int bonusGap = 24;
        int bonusMargin = 20;
        int totalW = bonusOffer.size() * bonusW + Math.max(0, bonusOffer.size() - 1) * bonusGap;
        int bonusX = Math.max(bonusMargin, getWidth() - totalW - bonusMargin);
        int bonusY = getHeight() - bonusH - bonusMargin;
        g2.drawString("Choose 1 Bonus Card", bonusX, bonusY - 12);
        for(int i=0;i<bonusOffer.size();i++) {
            BonusCard bc = bonusOffer.get(i);
            int dx = bonusX + i*(bonusW + bonusGap);
            int dy = bonusY;
            Rectangle rect = new Rectangle(dx, dy, bonusW, bonusH);
            bonusRects.add(rect);
            BufferedImage img = bc.getImage();
            if(img != null) {
                g2.drawImage(img, dx, dy, bonusW, bonusH, null);
            } else {
                g2.setColor(new Color(235,235,235));
                g2.fillRoundRect(dx, dy, bonusW, bonusH, 10,10);
                g2.setColor(Color.DARK_GRAY);
                g2.drawRoundRect(dx, dy, bonusW, bonusH, 10,10);
                g2.drawString(bc.getName(), dx+10, dy+20);
            }
            if(bc == chosenBonus) {
                g2.setColor(new Color(0, 140, 255));
                g2.setStroke(new BasicStroke(4f));
                g2.drawRoundRect(dx-2, dy-2, bonusW+4, bonusH+4, 12,12);
            }
        }

       
    }
    // Draw a bird card; if image is missing, render a simple placeholder with the bird name
    public void drawBirdCard(Graphics g, Bird bird, int x, int y, int width, int height)
    {
        if(bird != null && bird.getImage() != null) {
            g.drawImage(bird.getImage(), x, y, width, height, null);
            // draw zoom button
            int btnSize = Math.max(16, width / 8);
            int bx = x + width - btnSize - 4;
            int by = y + 4;
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0,0,0,140));
            g2.fillRoundRect(bx, by, btnSize, btnSize, 6, 6);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2f));
            g2.drawOval(bx + 4, by + 4, btnSize - 8, btnSize - 8);
            g2.drawLine(bx + btnSize - 6, by + btnSize - 6, bx + btnSize - 2, by + btnSize - 2);
            zoomButtons.add(new ZoomTarget(bird, new Rectangle(bx, by, btnSize, btnSize)));
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

    // Draw zoom overlay centered
    private void drawZoomOverlay(Graphics2D g2, BufferedImage img, String title) {
        int w = getWidth();
        int h = getHeight();
        g2.setColor(new Color(0,0,0,160));
        g2.fillRect(0,0,w,h);

        if(img == null) return;
        double maxScale = 0.85;
        int targetW = (int)(w * maxScale);
        int targetH = (int)(h * maxScale);
        double scale = Math.min((double)targetW / img.getWidth(), (double)targetH / img.getHeight());
        int drawW = (int)(img.getWidth() * scale);
        int drawH = (int)(img.getHeight() * scale);
        int dx = (w - drawW) / 2;
        int dy = (h - drawH) / 2;
        zoomRect = new Rectangle(dx, dy, drawW, drawH);

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(img, dx, dy, drawW, drawH, null);

        // title bar
        if(title != null) {
            g2.setColor(new Color(0,0,0,180));
            g2.fillRoundRect(dx, dy - 36, Math.max(120, drawW/2), 28, 10, 10);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.drawString(title, dx + 10, dy - 16);
        }
    }

    // Overlay showing tucked cards as a horizontal strip
    private void drawTuckedOverlay(Graphics2D g2, Bird bird) {
        int w = getWidth();
        int h = getHeight();
        g2.setColor(new Color(0,0,0,160));
        g2.fillRect(0,0,w,h);
        if(bird == null) return;
        ArrayList<Bird> tucked = bird.getTuckedList();
        int count = tucked == null ? 0 : tucked.size();
        int cardW = 140;
        int cardH = 90;
        int gap = 12;
        int totalW = count * cardW + Math.max(0, count-1) * gap;
        int dx = Math.max(20, (w - totalW) / 2);
        int dy = Math.max(80, h/2 - cardH/2);
        tuckRect = new Rectangle(dx - 20, dy - 40, totalW + 40, cardH + 80);

        g2.setColor(new Color(245,245,245,230));
        g2.fillRoundRect(tuckRect.x, tuckRect.y, tuckRect.width, tuckRect.height, 14, 14);
        g2.setColor(new Color(60,60,60));
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(tuckRect.x, tuckRect.y, tuckRect.width, tuckRect.height, 14, 14);

        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString("Tucked cards for " + bird.getName() + " (" + count + ")", tuckRect.x + 12, tuckRect.y + 28);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if(count == 0) {
            g2.setColor(new Color(120,120,120));
            g2.setFont(new Font("Arial", Font.PLAIN, 16));
            g2.drawString("No tucked cards.", dx, dy + cardH/2);
            return;
        }
        for(int i=0;i<count;i++) {
            int cx = dx + i * (cardW + gap);
            Bird t = tucked.get(i);
            g2.setColor(new Color(220,220,230));
            g2.fillRoundRect(cx, dy, cardW, cardH, 10, 10);
            g2.setColor(new Color(120,120,140));
            g2.drawRoundRect(cx, dy, cardW, cardH, 10, 10);
            if(t != null && t.getImage() != null) {
                BufferedImage img = t.getImage();
                double scale = Math.min((cardW-12)/(double)img.getWidth(), (cardH-12)/(double)img.getHeight());
                int drawW = (int)(img.getWidth()*scale);
                int drawH = (int)(img.getHeight()*scale);
                int px = cx + (cardW - drawW)/2;
                int py = dy + (cardH - drawH)/2;
                g2.drawImage(img, px, py, drawW, drawH, null);
            } else if(t != null) {
                g2.setColor(new Color(80,80,100));
                g2.setFont(new Font("Arial", Font.BOLD, 14));
                g2.drawString(t.getName(), cx + 8, dy + cardH/2);
            } else {
                g2.setColor(new Color(80,80,100));
                g2.setFont(new Font("Arial", Font.BOLD, 14));
                g2.drawString("Tucked card", cx + 12, dy + cardH/2);
            }
        }
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
        if(tuckViewActive) {
            if(tuckRect == null || !tuckRect.contains(p)) {
                tuckViewActive = false;
                tuckBird = null;
                tuckRect = null;
                repaint();
            }
            return;
        }
        if(zoomActive) {
            if(zoomRect == null || !zoomRect.contains(p)) {
                zoomActive = false;
                zoomBird = null;
                zoomRect = null;
                repaint();
            }
            return;
        }
        if (showingControls) {
            for (Button btn : miscellaneousScreen) {
                if (btn.inBounds(e.getX(), e.getY()) && btn.getName().equals("BACK_FROM_CONTROLS")) {
                    showingControls = false;
                    miscellaneousScreen.clear();
                    repaint();
                    return;
                }
            }
            // Ignore other clicks while controls are up
            return;
        }
        if (startingComplete && e.getButton() == MouseEvent.BUTTON3) {
            if (handleRightClickEggPlacement(p)) {
                repaint();
                return;
            }
        }
        if (!namesEntered) {
            // Check if GO or CONTROLS button clicked
            for (Button btn : currentScreen) {
                System.out.println("Checking button: " + btn.getName() + " at click (" + e.getX() + "," + e.getY() + ")");
                if (btn.inBounds(e.getX(), e.getY()) && btn.getName().equals("CONTROLS")) {
                    showingControls = true;
                    repaint();
                    return;
                }
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
            ensureBonusOffer(Player.currentPlayerIndex);
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

            // Bonus card selection
            for(int i=0;i<bonusRects.size() && i<bonusOffer.size();i++) {
                Rectangle r = bonusRects.get(i);
                if(r.contains(p)) {
                    chosenBonus = bonusOffer.get(i);
                    maybeCompleteStartingSelection();
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
                        maybeCompleteStartingSelection();
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
                    // check zoom buttons first
                    for(ZoomTarget z : zoomButtons) {
                        if(z.bird == b && z.button.contains(p)) {
                            zoomActive = true;
                            zoomBird = b;
                            repaint();
                            return;
                        }
                    }
                    if (selected.size() < 5) {
                        selected.add(ItemRef.bird(b));
                        String playerName = playerNames.get(Player.currentPlayerIndex);
                        System.out.println(playerName + " added bird: " + b.getName() + ". Now " + selected.size() + "/5");
                        maybeCompleteStartingSelection();
                    }
                    repaint();
                    return;
                }
            }
        }


        if (startingComplete) {
            for(ZoomTarget z : tuckButtons) {
                if(z.button.contains(p)) {
                    tuckViewActive = true;
                    tuckBird = z.bird;
                    repaint();
                    return;
                }
            }
            for(ZoomTarget z : zoomButtons) {
                if(z.button.contains(p)) {
                    zoomActive = true;
                    zoomBird = z.bird;
                    repaint();
                    return;
                }
            }
            if(nextRoundButton != null && nextRoundButton.width > 0 && nextRoundButton.height > 0 && nextRoundButton.contains(p)) {
                if(gameFinished || currentRound >= maxRounds) {
                    JOptionPane.showMessageDialog(this, "Game complete. Check the scoreboard.");
                } else if(roundOver) {
                    startNextRound();
                } else {
                    JOptionPane.showMessageDialog(this, "Finish using all action cubes before starting the next round.");
                }
                repaint();
                return;
            }
            HashMap<String, ArrayList<Spot>> board = Player.getCurrentPlayerBoard();
        if (board != null) {
            // Update clickable status: only leftmost unoccupied spot in each habitat
            updateSpotClickability(board);
            
            for (ArrayList<Spot> spots : board.values()) {
                if (spots == null) continue;
                for (Spot s : spots) {
                    Rectangle r = new Rectangle(s.x1, s.y1, s.getWidth(), s.getHeight());
                    if (r.contains(p) && s.getClickable()) {
                        // zoom button check for placed birds
                        for(ZoomTarget z : zoomButtons) {
                            if(z.button.contains(p)) {
                                zoomActive = true;
                                zoomBird = z.bird;
                                repaint();
                                return;
                            }
                        }
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
            ensureBonusOffer(Player.currentPlayerIndex);
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
    public void keyTyped(java.awt.event.KeyEvent e) {}

    @Override
    public void keyReleased(java.awt.event.KeyEvent e) {}

    @Override
    public void keyPressed(java.awt.event.KeyEvent e) {
        char c = e.getKeyChar();
        if (c == 'z' || c == 'Z') {
            if (startingComplete && !roundOver && !gameFinished) {
                Player current = Player.players().get(Player.currentPlayerIndex);
                if (current.getRemainingTokens() > 0) {
                    current.useActionToken();
                    checkRoundEndAfterAction();
                    if(!roundOver) {
                        advanceTurn();
                    } else {
                        repaint();
                    }
                } else {
                    advanceTurn();
                }
            }
            return;
        }
        if (c == 'e' || c == 'E') {
            if (startingComplete && !roundOver && !gameFinished) {
                Player current = Player.players().get(Player.currentPlayerIndex);
                tradeTwoFoodForAny(current);
                repaint();
            }
            return;
        }
        if (c == 'c' || c == 'C') {
            showingControls = true;
            repaint();
        }
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
    loadBirdImages();
    loadBonusCardImages();
    buildBonusDeck();
    initFeeder();
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
    public static void drawControlsScreen(Graphics g)
    {
        g.drawString("Control Screen",getWidth()/2 , 300);

    }
}
