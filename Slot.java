import java.awt.Rectangle;

class Slot {
    final Rectangle bounds;
    final ItemRef item;
    Slot(Rectangle b, ItemRef i) { this.bounds = b; this.item = i; }
}
