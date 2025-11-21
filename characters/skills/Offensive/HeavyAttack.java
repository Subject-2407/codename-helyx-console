package characters.skills.Offensive;

import characters.Monster;
import characters.Player;
import characters.skills.OffensiveSkill;
import game.PVE;

public class HeavyAttack extends OffensiveSkill {
    public HeavyAttack(String name, String description, int manaCost, int coolDown, int bonusHitChance, double damageMultiplier) {
        super(name, description, manaCost, coolDown, bonusHitChance, damageMultiplier);
    }

    @Override
    public void use(Player player, Monster monster) {
        if (PVE.playerHitChance(player, monster, bonusHitChance) == 0) {
            System.out.println("> You tried to use " + this.name + " on " + monster.getName() + " but missed!");
            PVE.monsterAttackTurn(monster, player);
            return;
        } 
        int playerBaseDamage = player.attack();

        double skillDamage = playerBaseDamage * this.getDamageMultiplier();
        int totalDamage = playerBaseDamage + (int)skillDamage;

        this.processBattle(player, monster, totalDamage);
    }    
}
