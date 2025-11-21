package characters;

public abstract class NPC {
    protected String name;
    protected String prefixColor;
    protected String description;
    
    public NPC(String name, String prefixColor, String description) {
        this.name = name;
        this.prefixColor = prefixColor;
        this.description = description;
    }

    public String getName() { return name; }
    public String getPrefixColor() { return prefixColor; }
    public String getDescription() { return description; }
}
