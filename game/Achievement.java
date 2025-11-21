package game;

public class Achievement {
    private String name;
    private String description;

    public Achievement(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Achievement)) return false;
        Achievement a = (Achievement) o;
        return name.equals(a.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}

