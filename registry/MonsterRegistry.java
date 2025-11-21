package registry;

import characters.*;
import game.MonsterCodex;

public class MonsterRegistry {
    public static MonsterCodex churenimMonsters() {
        MonsterCodex monsters = new MonsterCodex();
        monsters.insert(new Monster("Goblin", 30, 5, 8, 2, 10, 10));
        monsters.insert(new Monster("Slime", 50, 3, 5, 5, 12, 8));
        monsters.insert(new Monster("Wolf", 40, 7, 12, 3, 15, 12));
        monsters.insert(new Monster("Orc", 80, 12, 7, 6, 25, 20));
        monsters.insert(new Monster("Bandit", 60, 10, 14, 4, 20, 18));
        monsters.insert(new Monster("Skeleton", 70, 9, 9, 5, 22, 22));
        monsters.insert(new Monster("Dark Knight", 150, 22, 10, 12, 50, 50));
        monsters.insert(new Monster("Troll", 120, 18, 5, 10, 35, 30));

        return monsters;
    }
}

