package game;

import java.util.ArrayList;
import java.util.List;

import characters.Monster;

class MonsterNode {
    Monster monster;
    MonsterNode left, right;

    public MonsterNode(Monster monster) {
        this.monster = monster;
        left = right = null;
    }
}

public class MonsterCodex {
    private MonsterNode root;

    public void insert(Monster monster) {
        root = insertRec(root, monster);
    }

    private MonsterNode insertRec(MonsterNode root, Monster monster) {
        if (root == null) {
            return new MonsterNode(monster);
        }

        if (monster.getBattlePower() < root.monster.getBattlePower()) {
            root.left = insertRec(root.left, monster);
        } else if (monster.getBattlePower() > root.monster.getBattlePower()) {
            root.right = insertRec(root.right, monster);
        } else { // if equal bp, insert to right child anyway
            root.right = insertRec(root.right, monster);
        }

        return root;
    }

    public List<Monster> searchBPRange(int min, int max) {
        List<Monster> result = new ArrayList<>();
        searchRangeRec(root, min, max, result);
        return result;
    }

    private void searchRangeRec(MonsterNode root, int min, int max, List<Monster> result) {
        if (root == null) return;

        if (root.monster.getBattlePower() > min) {
            searchRangeRec(root.left, min, max, result);
        }

        if (root.monster.getBattlePower() >= min && root.monster.getBattlePower() <= max) {
            result.add(root.monster);
        }

        if (root.monster.getBattlePower() < max) {
            searchRangeRec(root.right, min, max, result);
        }
    }
}
