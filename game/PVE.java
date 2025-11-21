package game;

import characters.*;
import characters.skills.Skill;
import characters.skills.Utility.BuffSkill;
import registry.AchievementRegistry;
import utilities.*;

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;
import java.util.Scanner;

public class PVE {
    private static final Random random = new Random();
    private static Deque<String> actionHistory = new ArrayDeque<>();

    private static void waitMoment() {
        try { Thread.sleep(1000 + (1000 * random.nextInt(3))); } catch (InterruptedException e) {}
    }

    public static void battle(Player player, Monster monster) {
        Scanner scanner = player.getScanner();
        String hasWeapon = player.getEquippedWeapon() != null ? " (" + player.getEquippedWeapon().getName() + ")" : " (Fist)";
        ArrayList<Skill> playerSkills = player.getSkills();
        int turn = 0;
        actionHistory.clear();
        while (player.isAlive() && monster.isAlive()) {
            player.displayInfo();
            turn++;
            System.out.println("========== In Battle With ==========");
            System.out.println("||  " + UserInterface.WHITE + monster.getName() + "   (Turn " + turn + ")" + UserInterface.RESET);
            System.out.println("||  " + UserInterface.RED + "HP: " + monster.getHP() + "/" + monster.getMaxHP() + UserInterface.RESET + "  " + UserInterface.PURPLE + "Battle Power: " + monster.getBattlePower() + UserInterface.RESET);
            System.out.println("====================================");
            if (!actionHistory.isEmpty()) {
                String lastAction = actionHistory.pop();
                System.out.println(UserInterface.TAN + "Last Action: " + lastAction + UserInterface.RESET);
            }
            System.out.print("\n[F] Attack");
            System.out.print(" | [Q] Block");
            if (playerSkills.size() > 0) { System.out.println("\n"); } else System.out.print("\n"); 
            
            for (int i = 0; i < playerSkills.size(); i++) {
                Skill s = playerSkills.get(i);
                String skillCooldown = s.isInCooldown() ? " - In Cooldown" : "";
                String buffSkillStatus = "";
                if (s instanceof BuffSkill) {
                    BuffSkill buffSkill = (BuffSkill) s;
                    if (buffSkill.getBuffTimer() > 0) buffSkillStatus = " (Active)";
                }
                System.out.println("[" + (i + 1) + "] " + s.getName() + " (" + s.getManaCost() + " Mana)" + buffSkillStatus + skillCooldown);
            }

            System.out.print("\nEnter choice: ");
            String choice = scanner.nextLine().trim().toLowerCase();
            System.out.println();

            if (choice.matches("f")) { // attacking
                if (playerHitChance(player, monster, 0) == 1) {
                    int playerDamage = damageVariance(player.attack());
                    int damageDifference = monster.takeDamage(playerDamage);
                    String damageReduction = damageDifference > 0 ? "(-" + damageDifference + ")" : ""; 
                    System.out.println("> You hit the " + monster.getName() + " for " + playerDamage + damageReduction + " damage!" + hasWeapon);
                    actionHistory.push("Attack " + "(" + (playerDamage - damageDifference) + " damage)");
                } else {
                    System.out.println("> You missed your attack!");
                    actionHistory.push("Attack (Missed)");
                }

                monsterAttackTurn(monster, player);
                tickEndOfTurn(player);
            } else if (choice.matches("q")) { // blocking
                System.out.println("> Waiting opponent to attack..");
                waitMoment();
                if (monster.isAlive()) {
                    if (playerBlockChance(player, monster) == 1) {
                        if (monsterHitChance(monster, player) == 1) {
                            System.out.println("> The " + monster.getName() + " hits you!");
                            System.out.println("> You successfully blocked their attack!");
                            player.gainMana((int)(player.getMaxMana() * 0.05)); // successful block replenish 5% mana
                            actionHistory.push("Block (Successful)");
                        } else {
                            System.out.println("> The " + monster.getName() + " missed its attack!");
                        }
                    } else {
                        if (monsterHitChance(monster, player) == 1) {
                            int monsterRawDamage = damageVariance(monster.attack());
                            int damageDifference = player.takeDamage(monsterRawDamage);
                            String damageReduction = damageDifference > 0 ? "(-" + damageDifference + ")" : "";
                            System.out.println("> The " + monster.getName() + " hits you for " + monsterRawDamage + damageReduction + " damage!");
                            System.out.println("> You failed to block their attack!");
                            actionHistory.push("Block (Failed)");
                        } else {
                            System.out.println("> The " + monster.getName() + " missed its attack!");
                        }
                    }
                }
                if (!player.isAlive()) { break; }
                UserInterface.enterReturn(scanner);
                tickEndOfTurn(player);
            } else if (choice.matches("[1-" + playerSkills.size() + "]") && playerSkills.size() > 0) { // use skill
                int skillChoice = Integer.parseInt(choice);
                useSkill(player, monster, playerSkills.get(skillChoice - 1));
            } else { // wrong choice (turn wasted)
                System.out.println("> You wasted a turn..");
                actionHistory.push("Wasted a turn");
                waitMoment();
                if (monster.isAlive()) {
                    if (monsterHitChance(monster, player) == 1) {
                        int monsterRawDamage = damageVariance(monster.attack());
                        int playerDamageReduction = player.takeDamage(monsterRawDamage);
                        String playerReduction = playerDamageReduction > 0 ? "(-" + playerDamageReduction + ")" : ""; 
                        
                        System.out.println("> The " + monster.getName() + " hits you for " + monsterRawDamage + playerReduction + " damage!");
                        if (player.isAlive()) { UserInterface.enterReturn(scanner); }
                    } else {
                        System.out.println("> The " + monster.getName() + " missed its attack!");
                        UserInterface.enterReturn(scanner);
                    }
                }
                tickEndOfTurn(player);
            }
        }

        if (!player.isAlive()) {
            System.out.println("> You have been defeated..");
            UserInterface.enterReturn(scanner);
        } else {
            int gainXP = monster.getExpReward();
            int gainGold = monster.getGoldReward();
            player.gainXP(gainXP);
            player.gainGold(gainGold);
            player.gainMana((int)(player.getMaxMana() * 0.1));
            resetSkillsCooldown(playerSkills);
            player.resetBuffs();
            if (player.getMonsterScore() == 0) {
                player.unlockAchievement(AchievementRegistry.firstKill);
            }
            player.gainMonsterScore(monster.getBattlePower());
            System.out.println("> You defeated the " + monster.getName() + "! Received " + gainXP + " XP and found " + gainGold + " golds.");
            
            UserInterface.enterReturn(scanner);
        }
    }

    public static void useSkill(Player player, Monster monster, Skill skill) {
        if (!(player.getMana() >= skill.getManaCost())) {
            System.out.println("> You don't have enough mana to use this skill! You wasted a turn..");
            monsterAttackTurn(monster, player);
            tickEndOfTurn(player);
            actionHistory.push("Wasted a turn");
            return;
        }

        if (skill.isInCooldown()) {
            System.out.println("> Your skill is currently in cooldown! You wasted a turn..");
            monsterAttackTurn(monster, player);
            tickEndOfTurn(player);
            actionHistory.push("Wasted a turn");
            return;
        }

        for (Skill s : player.getSkills()) {
            if (s != skill) s.decreaseCooldown();
        }

        player.reduceMana(skill.getManaCost());
        player.decreaseBuffsTimer();
        skill.use(player, monster);
        skill.startCooldown();
        actionHistory.push(skill.getName());
    }

    private static void resetSkillsCooldown(ArrayList<Skill> playerSkills) {
        for(int i = 0; i < playerSkills.size(); i++) {
            playerSkills.get(i).resetCooldown();
        }
    }

    private static void decreaseSkillsCooldown(ArrayList<Skill> playerSkills) {
        for(int i = 0; i < playerSkills.size(); i++) {
            playerSkills.get(i).decreaseCooldown();
        }
    }

    public static void monsterAttackTurn(Monster monster, Player player) {
        if (monster.isAlive()) {
            waitMoment();
            if (monsterHitChance(monster, player) == 1) {
                int monsterRawDamage = damageVariance(monster.attack());
                int playerDamageReduction = player.takeDamage(monsterRawDamage);
                String playerReduction = playerDamageReduction > 0 ? "(-" + playerDamageReduction + ")" : ""; 
                
                System.out.println("> The " + monster.getName() + " hits you for " + monsterRawDamage + playerReduction + " damage!");
                if (player.isAlive()) { UserInterface.enterReturn(player.getScanner()); }
            } else {
                System.out.println("> The " + monster.getName() + " missed its attack!");
                UserInterface.enterReturn(player.getScanner());
            }
        }
    }

    private static void tickEndOfTurn(Player player) {
        decreaseSkillsCooldown(player.getSkills());
        player.decreaseBuffsTimer();
    }

    public static boolean attemptEscape(Player player, Monster monster) {
        // get both dexterity
        int playerDexterity = player.getDexterity() + player.getBuff("dexterity");
        int monsterDexterity = monster.getDexterity();

        // calculate escape chance
        int escapeChance = 50 + (playerDexterity - monsterDexterity) * 3;
        escapeChance = Math.max(10, Math.min(90, escapeChance));

        // roll it
        boolean escaped = random.nextInt(100) < escapeChance;

        if (escaped) {
            // bruise chance
            int bruiseChance = monsterDexterity / 2;
            if (random.nextInt(100) < bruiseChance) {
                int bruiseDamage = (int) (monster.attack() * (0.05 + random.nextDouble() * 0.10));
                int damageDifference = player.takeDamage(bruiseDamage);
                String damageReduction = damageDifference > 0 ? "(-" + damageDifference + ")" : "";
                System.out.println("> You escaped but the " + monster.getName() + " manages to bruise you! Took " + bruiseDamage + damageReduction + " damage.");
                UserInterface.enterReturn(player.getScanner());
            } else {
                System.out.println("> You escaped safely!");
                UserInterface.enterReturn(player.getScanner());
            }
            return true;
        } else {
            System.out.println("> You failed to escape!");
            UserInterface.enterReturn(player.getScanner());
            return false;
        }
    }

    public static int damageVariance(int damage) {
        int variance = (int)(damage * 0.1);
        return damage + (variance > 0 ? random.nextInt(variance + 1) - (variance / 2) : 0);
    }

    private static int playerBlockChance(Player player, Monster monster) {
        // get monster attack attribute
        int monsterAttack = monster.getBaseAttack();
        // get player attributes
        int playerDefense = player.getDefense() + player.getBuff("defense");
        int weaponDefense = player.getEquippedWeapon() != null ? player.getEquippedWeapon().getDefenseBonus() : 0;

        // calculate block chance
        double blockChance = 30 + ((weaponDefense + playerDefense) / (double) (monsterAttack + 5) * 40);

        // roll it
        boolean isBlocked = random.nextInt(100) < blockChance;
        return isBlocked ? 1 : 0;
    }

    public static int playerHitChance(Player player, Monster monster, int bonusHitChance) {
        // get both dexterity
        int playerDexterity = player.getDexterity() + player.getBuff("dexterity");
        int monsterDexterity = monster.getDexterity();

        // calculate hit chance
        int hitChance = 75 + (playerDexterity - monsterDexterity) * 2;
        hitChance = Math.max(10, Math.min(95, hitChance));

        // roll it
        if (random.nextInt(100) >= (hitChance + bonusHitChance)) {
            return 0;
        } else return 1;
    }

    private static int monsterHitChance(Monster monster, Player player) {
        // get both dexterity
        int monsterDexterity = monster.getDexterity();
        int playerDexterity = player.getDexterity() + player.getBuff("dexterity");

        // calculate hit chance
        int hitChance = 75 + (monsterDexterity - playerDexterity) * 2;
        hitChance = Math.max(10, Math.min(95, hitChance));
        
        // roll it
        if (random.nextInt(100) >= hitChance) {
            return 0;
        } else return 1;
    }

}
