
public class Spot extends Button{
private Bird bird;
private String area;
private int index;
private boolean occupied, hasActionToken;
private Ability spotAbility;

    public Spot(String a, int i, Ability sa) {
        super("Spot "+a+" "+i, "normal", null, true, true, 0, 0, 0, 0);
        area = a;
        index = i;
        spotAbility = sa;
        occupied = false;
        hasActionToken = false;
        bird = null;
    }

    public Bird getBird() {
        return bird;
    }

    public void setBird(Bird b) {
        bird = b;
    }

    public String getArea() {
        return area;
    }

    public int getIndex() {
        return index;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean o) {
        occupied = o;
    }

    public boolean hasActionToken() {
        return hasActionToken;
    }

    public void setActionToken(boolean h) {
        hasActionToken = h;
    }

    public Ability getSpotAbility() {
        return spotAbility;
    }
}