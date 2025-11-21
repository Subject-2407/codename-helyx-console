package registry;

import java.util.ArrayList;

import items.Equipment;
import items.Item;
import items.Usable;

public class ItemRegistry {
    public static ArrayList<Item> emberforgeItems() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Equipment("Iron Sword", "A sword made of iron, much better than the wooden one.", 100, 1, 45, 7));
        items.add(new Equipment("Half-plate Armor", "Good balance of protection and mobility.", 30, 2, 0, 10));
        items.add(new Usable("Small Health Potion", "Restores 25 HP.",10, 1, 25));
        items.add(new Usable("Small Mana Potion", "Restores 5 Mana.", 10, 2, 5));
        items.add(new Usable("Small Energy Potion", "Restores 5 Energy.", 20, 3, 5));
        items.add(new Usable("Golden Potion", "Restores 50% of Max HP, MP, Energy.", 30, 4, 50));

        return items;
    }
}
