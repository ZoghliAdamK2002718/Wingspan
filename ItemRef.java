class ItemRef {
    final ItemType type;
    final Bird bird;
    final String tokenName;
    private ItemRef(ItemType t, Bird b, String tn) { this.type = t; this.bird = b; this.tokenName = tn; }
    static ItemRef token(String name) { return new ItemRef(ItemType.TOKEN, null, name); }
    static ItemRef bird(Bird b) { return new ItemRef(ItemType.BIRD, b, null); }
}
