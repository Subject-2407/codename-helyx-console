package registry;

import java.util.ArrayList;

import locations.Place;
import locations.places.Dungeon;
import locations.places.Forest;
import locations.places.Guild;
import locations.places.Library;
import locations.places.Shop;
import locations.places.Tavern;
import locations.places.Kingdom;

public class PlaceRegistry {
    public static ArrayList<Place> eldoriaPlaces() {
        ArrayList<Place> places = new ArrayList<>();
        places.add(new Tavern("Moonlit Ember", "A warm and elegant inn. (Restore health, mana, energy for 50 gold)", 50));
        places.add(new Shop("Emberforge", "We sell best quality equipments and items! (Shop)", ItemRegistry.emberforgeItems()));
        places.add(new Guild("Stormbringers", "A guild known for its powerful warriors who bring chaos to battle. (Guild)", ClassRegistry.beginnerClasses()));
        places.add(new Library("Tomekeep's Library", "A vault of ancient lore, guarded by the mysterious Tomekeep. (Library)", LiteratureRegistry.tomekeepLibrary()));
        places.add(new Forest("Eldertree", "Home to a massive ancient tree said to contain untold wisdom. (Forest)", 1, EventRegistry.eldertreeEvents()));
        places.add(new Kingdom("Eldoria Kingdom", "A bustling capital with markets and alleys. (Roam)", 1, EventRegistry.kingdomEvents()));
        places.add(new Dungeon("Churenim Cavern", "A deep cavern where the cries of the lost echo eternally. (Dungeon)", 1, MonsterRegistry.churenimMonsters()));
        

        return places;
    }
}
