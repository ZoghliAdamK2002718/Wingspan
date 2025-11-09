import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.TreeMap;

public class Ability {
private String rawAbility, triggerType,triggerName;
private ArrayList<String> ability;
private ArrayList<Integer> players;
private static ArrayList<Ability> pinkTriggers;
private HashMap<Class,ArrayList<Object>> inputs;
private final TreeMap<String,ArrayList<String>> keyWords = new TreeMap<String,ArrayList<String>>() {{
    put("play",new ArrayList<String>(Arrays.asList("this", "forest", "wetland", "grassland")));
    put("right",new ArrayList<String>(Arrays.asList()));
    put("keep",new ArrayList<String>(Arrays.asList()));//use either the bonus card or bird card from the variable set
    put("look",new ArrayList<String>(Arrays.asList()));
    put("roll",new ArrayList<String>(Arrays.asList()));//add a return statement for the key word
    put("draw",new ArrayList<String>(Arrays.asList("equal","NUM","bonus")));//
    put("gain",new ArrayList<String>(Arrays.asList("NUM","rodent","fish","seed","invertibrate","fruit","wild","supply","available")));// add a return statement for the key word
    put("look",new ArrayList<String>(Arrays.asList("egg","card","hand","seed","fish")));
    put("lay",new ArrayList<String>(Arrays.asList("eggs","another","this","any","each","ground","cavity","burrow","bowl","platform","pendant","cup","scrape","clutch")));
    put("tuck",new ArrayList<String>(Arrays.asList("NUM","hand","deck","it")));//
    put("discard",new ArrayList<String>(Arrays.asList("NUM","egg","card","seed","fish","rodent")));
    put("all",new ArrayList<String>(Arrays.asList()));// does not interupt the flow of other key words if the boolean is true
    put("you",new ArrayList<String>(Arrays.asList()));
    put("cache",new ArrayList<String>(Arrays.asList("NUM","it","rodent","fish","seed","invertibrate","fruit","wild","supply","available")));
    put("selects",new ArrayList<String>(Arrays.asList()));//runs all, and removes the cards that other players choose

}};
}
