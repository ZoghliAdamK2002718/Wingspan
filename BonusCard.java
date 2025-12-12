import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeMap;

public class BonusCard extends Button {
    private String name, ability, scoringType;
    // positive numbers in the left Integer indicate minimum threshold,
    // and a -1 indicates per bird.
    // EX: 5 to 7 birds: 3 points, 8+ birds: 7 points → {5=3, 8=7}
    // while 1 point per bird → {-1=1}
    private TreeMap<Integer, Integer> birdScore;

    private ArrayList<String> keyWordList, refinedAbility;
    private Player player;

    public BonusCard(String n, String s, BufferedImage i, boolean c, boolean d,
                     int xOne, int yOne, int xTwo, int yTwo,
                     String a, String sT, TreeMap<Integer, Integer> bS,
                     ArrayList<String> kWL, Player p) {
        super(n, s, i, c, d, xOne, yOne, xTwo, yTwo);
        this.name = n;                 // IMPORTANT: actually store the name
        this.ability = a;
        this.refinedAbility = new ArrayList<>(Arrays.asList(ability.split(" ")));
        this.scoringType = sT;
        this.birdScore = bS;
        this.keyWordList = kWL;
        this.player = p;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        switch (name) {
            case "Bird Collector":
                g.setFont(new Font("Arial", Font.BOLD, 16));
                g.setColor(Color.BLACK);
                g.drawString("Bird Collector", super.x1 + 10, super.y1 + 20);
                g.setFont(new Font("Arial", Font.PLAIN, 12));
                g.drawString("Score 1 point for each bird in your", super.x1 + 10, super.y1 + 40);
                g.drawString("habitat with a nest type matching", super.x1 + 10, super.y1 + 55);
                g.drawString("the nest type on this card.", super.x1 + 10, super.y1 + 70);
                break;
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
        switch (name) {
            default:
                break;
        }
    }

    public Player getPlayer() {
        return player;
    }

    public BufferedImage getImage() {
        return super.image;
    }

    public String getAbilityText() {
        return ability;
    }

    public void setPlayer(Player p) {
        player = p;
    }

    public int score() {
        int count = 0;

        for (int i = 0; i < refinedAbility.size(); i++) {
            String word = refinedAbility.get(i);

            switch (word) {
                // 1) Cards that say "include ..." (Anatomist, Cartographer, Photographer, etc.)
                case "include" -> {
                    for (String keyWord : keyWordList) {
                        String key = keyWord.toLowerCase();
                        for (Bird b : player.getAllPlayedBirds()) {
                            if (b.getName().toLowerCase().contains(key)) {
                                count++;
                            }
                        }
                    }
                }

                // 2) Cards that use "with ..."
                case "with" -> {
                    // skip “with ... names / named”
                    if (refinedAbility.contains("names") || refinedAbility.contains("named")) break;

                    // nest-type cards: "Birds with bowl nests", "Birds with cavity nests", etc.
                    if (refinedAbility.contains("nests") || refinedAbility.contains("nest")) {
                        int idx = refinedAbility.indexOf("nests");
                        if (idx == -1) idx = refinedAbility.indexOf("nest");
                        if (idx > 0) {
                            String nestType = refinedAbility.get(idx - 1).toLowerCase();
                            for (Bird b : player.getAllPlayedBirds()) {
                                if (b.getNestType().equalsIgnoreCase(nestType) ||
                                    (nestType.equals("star") &&
                                     b.getNestType().equalsIgnoreCase("star"))) {
                                    count++;
                                }
                            }
                        }
                    }
                    // power-based: "Birds with a predator power", "Birds with a tucking power"
                    else if (refinedAbility.contains("power")) {
                        int idxPower = refinedAbility.indexOf("power");
                        String power = (idxPower > 0) ? refinedAbility.get(idxPower - 1).toLowerCase() : "";
                        for (Bird b : player.getAllPlayedBirds()) {
                            if (b.getPower().equalsIgnoreCase(power)) {
                                count++;
                            }
                        }
                    }
                    // Ecologist: "Birds in your habitat with the fewest birds."
                    else if (refinedAbility.contains("birds")) {
                        ArrayList<Integer> habitatCounts = new ArrayList<>();
                        for (ArrayList<Spot> s : player.playerGetBoard().values()) {
                            int c = 0;
                            for (Spot spot : s) {
                                if (spot.isOccupied()) c++;
                            }
                            habitatCounts.add(c);
                        }
                        if (!habitatCounts.isEmpty()) {
                            int min = Integer.MAX_VALUE;
                            for (int h : habitatCounts) {
                                if (h < min) min = h;
                            }
                            int total = 0;
                            for (int h : habitatCounts) {
                                if (h == min) total += h;
                            }
                            count = total;
                        }
                    }
                    // Wingspan cards: Large Bird Specialist / Passerine Specialist
                    else if (refinedAbility.contains("wingspans")) {
                        if (refinedAbility.contains("over")) {
                            for (Bird b : player.getAllPlayedBirds()) {
                                if (b.getWingspan() > 65) {
                                    count++;
                                }
                            }
                        }
                        if (refinedAbility.contains("less")) {
                            for (Bird b : player.getAllPlayedBirds()) {
                                if (b.getWingspan() <= 30) {
                                    count++;
                                }
                            }
                        }
                    }
                }

                // 3) Cards that use "eat ..."
                case "eat" -> {
                    // Food Web Expert: "eat only invertebrate"
                    if (refinedAbility.contains("only") && refinedAbility.contains("invertebrate")) {
                        for (Bird b : player.getAllPlayedBirds()) {
                            if (b.getCosts().keySet().size() == 1 &&
                                b.getCosts().containsKey("invertebrate")) {
                                count++;
                            }
                        }
                        break;
                    }

                    // Generic food-category cards
                    ArrayList<String> keys = new ArrayList<>(Arrays.asList("seed", "fish", "fruit", "wild", "rodent"));
                    String food = null;
                    for (String k : keys) {
                        if (refinedAbility.contains(k)) {
                            food = k;
                            break;
                        }
                    }
                    if (food != null) {
                        for (Bird b : player.getAllPlayedBirds()) {
                            if (b.getCosts().containsKey(food)) {
                                count++;
                            }
                        }
                    }
                }

                // 4) Cards that use "that ..."
                case "that" -> {
                    // "have at least N eggs laid" (Breeding Manager / Oologist-type logic)
                    if (refinedAbility.contains("laid")) {
                        int idxLaid = refinedAbility.indexOf("laid");
                        int th = 0;
                        try {
                            th = Integer.parseInt(refinedAbility.get(idxLaid - 2));
                        } catch (Exception ignored) {}
                        for (Bird b : player.getAllPlayedBirds()) {
                            if (b.getEggCount() >= th) {
                                count++;
                            }
                        }
                    }
                    // "can only live in forest/grassland/wetland"
                    else if (ability.contains("only live")) {
                        int idxLive = refinedAbility.indexOf("live");
                        String hab = (idxLive >= 0 && idxLive + 2 < refinedAbility.size())
                                ? refinedAbility.get(idxLive + 2)
                                : "";
                        for (Bird b : player.getAllPlayedBirds()) {
                            if (b.getHabitats().length == 1 &&
                                b.getHabitats()[0].equalsIgnoreCase(hab)) {
                                count++;
                            }
                        }
                    }
                }
            }
        }

        // 5) Apply scoring thresholds in birdScore
        int result = 0;
        for (Integer key : birdScore.keySet()) {
            if (key == -1) {
                // per-bird scoring
                result = count * birdScore.get(key);
            } else if (count >= key) {
                // best threshold wins, TreeMap is sorted ascending
                result = birdScore.get(key);
            }
        }
        return result;
    }
}
