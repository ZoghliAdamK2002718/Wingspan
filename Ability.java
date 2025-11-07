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
    put("keep",new ArrayList<String>(Arrays.asList()));
    put("look",new ArrayList<String>(Arrays.asList()));
    put("roll",new ArrayList<String>(Arrays.asList("rodent","fish")));//add a return statement for the key word
    put("draw",new ArrayList<String>(Arrays.asList()));//
    put("gain",new ArrayList<String>(Arrays.asList()));//
    put("look",new ArrayList<String>(Arrays.asList("egg","card","hand","seed","fish")));
    put("lay",new ArrayList<String>(Arrays.asList("eggs","another","this","any","each","ground","cavity","burrow","bowl","platform","pendant","cup","scrape","clutch")));
    put("tuck",new ArrayList<String>(Arrays.asList("NUM")));//

}};
}
