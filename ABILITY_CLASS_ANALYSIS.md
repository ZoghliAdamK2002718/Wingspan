# Ability Class Analysis and Recommendations

## Current Status: ⚠️ **PARTIALLY FUNCTIONAL - Requires Additional Work**

---

## What Works Now ✅

1. **Constructor Compatibility**: Now accepts the format from your test data:
   ```java
   new Ability("WHEN PLAYED: Play an additional bird in your forest. Pay its normal cost.", currentPlayer)
   ```

2. **Automatic Trigger Parsing**: Correctly identifies:
   - "WHEN PLAYED" → brown ability
   - "WHEN ACTIVATED" → brown ability
   - Other → pink ability

3. **Text Tokenization**: Splits ability text into words and removes punctuation:
   ```
   Input: "Play an additional bird in your forest. Pay its normal cost."
   Output: ["Play", "an", "additional", "bird", "in", "your", "forest", "Pay", "its", "normal", "cost"]
   ```

4. **Basic Parsing Framework**: Can identify main action keywords (play, gain, draw, lay, etc.)

5. **Habitat Detection**: Can extract habitats from ability text:
   - "your forest" → "forest"
   - "your grassland" → "grassland"
   - "this bird's habitat" → gets from bird's habitat array

---

## What Doesn't Work Yet ❌

### 1. **Game State Access**
The static methods can't access:
- Current player's hand
- Board state
- Decks
- Food supply
- Other players

**Solution Needed**: Pass game context to methods OR make them non-static instance methods

### 2. **Complex Ability Parsing**
Current parsing is very basic. It can't handle:
- "Play an **additional** bird" (doesn't know this is +1 to normal play action)
- "If this bird is to the right of all other birds" (conditional logic)
- "Pay its normal cost" (needs to calculate and charge costs)
- Multi-step abilities
- Abilities with "or" choices

### 3. **No UI Integration**
Methods just print to console. They need to:
- Display bird selection dialogs
- Show food/card choices
- Update visual game state
- Handle player input

### 4. **Missing Game Logic**
The TODO implementations need:
- Access to Player.playerGetHand() and playerSetHand()
- Access to board HashMap<String, ArrayList<Spot>>
- Deck management
- Cost calculation
- Egg placement logic
- Food token management

---

## Example: What Happens Now

For "WHEN PLAYED: Play an additional bird in your forest. Pay its normal cost.":

```
Executing ability: WHEN PLAYED: Play an additional bird in your forest. Pay its normal cost.
Parsed tokens: [Play, an, additional, bird, in, your, forest, Pay, its, normal, cost]
Playing a bird in habitat: forest
```

**Problems:**
- ✅ Correctly identifies "play" action
- ✅ Correctly extracts "forest" habitat
- ❌ Doesn't filter player's hand for forest birds
- ❌ Doesn't display bird selection UI
- ❌ Doesn't place bird on board
- ❌ Doesn't charge costs
- ❌ Doesn't know this is "additional" (doesn't count against normal action)

---

## Required Adjustments

### Priority 1: Critical for Basic Functionality

1. **Add Game Context to Methods**
   ```java
   public static void play(String habitat) {
       if(currentPlayer == null) return;
       
       ArrayList<Bird> hand = currentPlayer.playerGetHand();
       ArrayList<Bird> validBirds = new ArrayList<>();
       
       // Filter birds that can go in this habitat
       for(Bird b : hand) {
           for(String h : b.getHabitats()) {
               if(h.equalsIgnoreCase(habitat)) {
                   validBirds.add(b);
                   break;
               }
           }
       }
       
       // TODO: Show UI to select bird from validBirds
       // TODO: Place on board
       // TODO: Charge costs
   }
   ```

2. **Implement Cost Calculation**
   ```java
   private static void payCost(Bird bird, String habitat) {
       TreeMap<String,Integer> costs = bird.getCosts();
       // Get number of birds already in habitat
       // Charge food based on position
       // Charge eggs/cards if needed
   }
   ```

3. **Add Board Interaction**
   ```java
   private static void placeBirdOnBoard(Bird bird, String habitat) {
       HashMap<String, ArrayList<Spot>> board = currentPlayer.playerGetBoard();
       ArrayList<Spot> spots = board.get(habitat);
       
       // Find first empty spot
       for(Spot s : spots) {
           if(!s.isOccupied()) {
               s.setBird(bird);
               s.setOccupied(true);
               bird.setLocation(s);
               break;
           }
       }
   }
   ```

### Priority 2: Enhanced Parsing

4. **Detect Modifiers**
   - "additional" → doesn't count as player's main action
   - "another" → exclude the current bird
   - "all" vs "any" vs specific numbers

5. **Handle Conditionals**
   ```java
   // For "If this bird is to the right of all other birds..."
   private boolean isRightmost() {
       if(bird == null) return false;
       
       String habitat = bird.getLocation().getArea();
       int index = bird.getLocation().getIndex();
       
       HashMap<String, ArrayList<Spot>> board = currentPlayer.playerGetBoard();
       ArrayList<Spot> spots = board.get(habitat);
       
       // Check if any spots to the right are occupied
       for(int i = index + 1; i < spots.size(); i++) {
           if(spots.get(i).isOccupied()) {
               return false;
           }
       }
       return true;
   }
   ```

### Priority 3: UI Integration

6. **Create Selection Dialogs**
   - Show bird cards player can play
   - Food selection interface
   - Habitat choice buttons

7. **Visual Feedback**
   - Highlight valid spots for bird placement
   - Animate card movement
   - Update resource displays

---

## Testing Recommendations

Before using in the game:

1. **Unit Test Each Method**
   ```java
   // Set up test game state
   Ability.setGameContext(testPlayer, testPlayerList, testPanel);
   
   // Test play ability
   Ability testAbility = new Ability("WHEN PLAYED: Play an additional bird in your forest.", 0);
   testAbility.executeAbility();
   
   // Verify: bird was added to board, costs were paid, etc.
   ```

2. **Test Edge Cases**
   - What if player has no valid birds?
   - What if player can't afford costs?
   - What if habitat is full?

3. **Integration Test**
   - Play actual game with 2-3 birds with abilities
   - Verify abilities trigger at correct times
   - Check that "WHEN PLAYED" executes during play action
   - Check that "WHEN ACTIVATED" executes when row is activated

---

## Recommended Next Steps

1. **Complete ONE ability method fully** (suggest `play()`)
2. **Test it thoroughly** with real game state
3. **Create UI helper methods** for player selections
4. **Replicate pattern** for other ability methods
5. **Add more sophisticated parsing** as needed
6. **Handle edge cases** and error conditions

---

## Bottom Line

**Can it work? YES, with significant additional implementation.**

The current class has:
- ✅ Good foundation and structure
- ✅ Correct parsing of trigger types
- ✅ Basic tokenization working
- ❌ No actual game logic implemented
- ❌ No UI integration
- ❌ No access to game state

**Estimated work needed**: 15-25 hours to fully implement all ability methods with game logic and UI integration.
