package items;

import characters.*;

public class Usable extends Item {
    private int type; // 1: adds health, 2: adds mana, 3: adds energy, 4: adds health mana energy simultaneously
    private int bonusPoints;

    public Usable(String name, String description, int price, int type, int bonus) {
        super(name, description, price);
        this.type = type;
        this.bonusPoints = bonus;
    }

    public int getType() {
        return type;
    }

    public int getBonusPoints() {
        return bonusPoints;
    }

    @Override
    public Usable clone() {
        return new Usable(name, description, price, type, bonusPoints);
    }

    @Override
    public void use(Player player) {
        player.useItem(this);
    }
    
}
