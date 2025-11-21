package registry;

import java.util.ArrayList;
import java.util.Random;

import characters.Inventory;
import game.Event.EventTree;
import items.Item;
import items.Usable;
import utilities.UserInterface;

public class EventRegistry {
    private static final Random random = new Random();
    private static final EventTree stopExploring = new EventTree("Stop exploring",true).addBranch(new EventTree("> Returning to main town...", false, player -> {player.setExploring(false);}));

    public static ArrayList<EventTree> eldertreeEvents() {
        ArrayList<EventTree> events = new ArrayList<>();
        // abandoned camp
        EventTree camp = new EventTree(
            "> You found an abandoned camp.\n> Ashes still warm, food half-eaten... whoever was here left in a hurry."
            , false);
        EventTree campTakeLoot = new EventTree("Take the loots",true);
        EventTree campDismiss = new EventTree("Keep walking", true);

        EventTree campLoot = new EventTree(
        "> You found some loots inside the camp.",
        false,
        player -> {
                Inventory inventory = player.getInventory();

                ArrayList<Item> loots = new ArrayList<>();
                loots.add(new Usable("Dried Meat", "Restores 7 HP", 10, 1, 7));
                loots.add(new Usable("Stale Bread", "Heals slightly (3 HP)", 5, 1, 3));
                loots.add(new Usable("Traveler's Stew", "Heals moderate HP (20 HP)", 25, 1, 20));

                Item itemFound = loots.get(random.nextInt(loots.size()));
                inventory.addItem(itemFound);
             }
        );

        camp.addBranch(campTakeLoot);
        camp.addBranch(campDismiss);
        camp.addBranch(stopExploring);

        campTakeLoot.addBranch(campLoot);

        // treasure
        EventTree treasure = new EventTree(
            "> You found a hidden treasure.\n> A moss-covered chest under twisted roots. Is it locked? Is it trapped?"
        , false);
        EventTree treasureTryOpen = new EventTree("Try to open the treasure",true);
        EventTree treasureDismiss = new EventTree("Keep walking", true);

        EventTree treasureTryOpen2 = new EventTree(
            "> You're trying to open the treasure chest...",
            false,
            player -> {
                int trapChance = random.nextInt(100);
                int golds = 10 + random.nextInt(100);
                if (trapChance < 60) {
                    System.out.println("> You opened the treasure chest without any problem and found " + golds + " golds.");
                    player.gainGold(golds);
                } else {
                    System.out.println("> It has a trap! Let's see if you can avoid it...");
                    UserInterface.delay();

                    int survivalRoll = random.nextInt(100);
                    int maxSurvivalChance = 80;
                    int survivabilityChance = Math.min((player.getDexterity() * maxSurvivalChance) / 100, maxSurvivalChance);

                    if (survivalRoll < survivabilityChance) {
                        System.out.println("> You managed to dodge the trap just in time!");
                        System.out.println("> You opened the treasure chest and found " + golds + " golds.");
                        player.gainGold(golds);
                    } else {
                        int damage = 5 + random.nextInt(15);
                        int reducedDamage = player.takeDamage(damage);
                        System.out.println("> You failed to dodge the trap and took " + damage + "(-" + reducedDamage + ") damage.");
                    }
                }
            });

        treasure.addBranch(treasureTryOpen);
        treasure.addBranch(treasureDismiss);
        treasure.addBranch(stopExploring);

        treasureTryOpen.addBranch(treasureTryOpen2);
        
        events.add(camp);
        events.add(treasure);

        return events;
    }

    public static ArrayList<EventTree> kingdomEvents() {
        ArrayList<EventTree> events = new ArrayList<>();
        // We'll construct choice-based branches for several casual kingdom events.

        // 1. Lucky coin (choice: pick up / leave)
        EventTree luckyRoot = new EventTree(
            "> You spot a shiny coin wedged between cobblestones."
        , false);
        EventTree luckyTake = new EventTree("Pick it up", true);
        EventTree luckyLeave = new EventTree("Leave it be", true);
        EventTree luckyTakeAct = new EventTree(
            "> You picked up the coin."
        , false, player -> {
            int golds = 3 + random.nextInt(10);
            System.out.println("> You pocket " + golds + " gold coins.");
            player.gainGold(golds);
        });
        EventTree luckyLeaveAct = new EventTree(
            "> You decide not to disturb it and move on."
        , false, player -> {
            System.out.println("> Sometimes leaving things be is the right choice.");
        });
        luckyRoot.addBranch(luckyTake);
        luckyRoot.addBranch(luckyLeave);
        luckyRoot.addBranch(stopExploring);
        luckyTake.addBranch(luckyTakeAct);
        luckyLeave.addBranch(luckyLeaveAct);

        // 2. Street performer (choice: give coin / just watch)
        EventTree perfRoot = new EventTree(
            "> A lively street performer draws a cheerful crowd. You stop to watch."
        , false);
        EventTree perfDonate = new EventTree("Give a coin", true);
        EventTree perfWatch = new EventTree("Just watch", true);
        EventTree perfDonateAct = new EventTree(
            "> You toss a coin into the performer's hat. They grin and reward you with a story that teaches you something."
        , false, player -> {
            int xp = 2 + random.nextInt(5);
            System.out.println("> The performance lifts your spirit. You gained " + xp + " XP.");
            player.gainXP(xp);
        });
        EventTree perfWatchAct = new EventTree(
            "> You watch quietly and enjoy the show."
        , false, player -> {
            int xp = 1 + random.nextInt(3);
            System.out.println("> The cheerful moment gives you a little experience (" + xp + " XP).");
            player.gainXP(xp);
        });
        perfRoot.addBranch(perfDonate);
        perfRoot.addBranch(perfWatch);
        perfRoot.addBranch(stopExploring);
        perfDonate.addBranch(perfDonateAct);
        perfWatch.addBranch(perfWatchAct);

        // 3. Lost child (choice: help / ignore)
        EventTree childRoot = new EventTree(
            "> A worried parent searches the crowd for a lost child."
        , false);
        EventTree childHelp = new EventTree("Help search", true);
        EventTree childIgnore = new EventTree("Keep walking", true);
        EventTree childHelpAct = new EventTree(
            "> Together you find the child and reunite them with their parent. The parent thanks you.",
            false, player -> {
                int xp = 2 + random.nextInt(3);
                System.out.println("> The parent rewards your kindness with " + xp + " XP.");
                player.gainXP(xp);
            }
        );
        EventTree childIgnoreAct = new EventTree(
            "> You decide not to get involved and continue on your way.",
            false, player -> {
                System.out.println("> The city moves on.");
            }
        );
        childRoot.addBranch(childHelp);
        childRoot.addBranch(childIgnore);
        childRoot.addBranch(stopExploring);
        childHelp.addBranch(childHelpAct);
        childIgnore.addBranch(childIgnoreAct);

        // 4. Pickpocket (choice: chase / do nothing)
        EventTree pickRoot = new EventTree(
            "> A sneaky hand tries to snatch your coin purse!"
        , false);
        EventTree pickChase = new EventTree("Chase the thief", true);
        EventTree pickIgnore = new EventTree("Ignore and stay safe", true);
        EventTree pickChaseAct = new EventTree(
            "> You run after the thief."
        , false, player -> {
            if (random.nextInt(100) < 50) {
                int recovered = 2 + random.nextInt(6);
                System.out.println("> You catch up and recover " + recovered + " gold! You feel a bit tired.");
                player.gainGold(recovered);
                player.reduceEnergy(1);
            } else {
                System.out.println("> The thief escapes and you lose some energy in the chase.");
                player.reduceEnergy(1);
            }
        });
        EventTree pickIgnoreAct = new EventTree(
            "> You keep your distance; sometimes safety matters more than coins."
        , false, player -> {
            System.out.println("> You avoided trouble.");
        });
        pickRoot.addBranch(pickChase);
        pickRoot.addBranch(pickIgnore);
        pickRoot.addBranch(stopExploring);
        pickChase.addBranch(pickChaseAct);
        pickIgnore.addBranch(pickIgnoreAct);

        // 5. Merchant bargain (choice: bargain / decline)
        EventTree merchRoot = new EventTree(
            "> A friendly merchant offers you a curious trinket in exchange for a short tale."
        , false);
        EventTree merchTell = new EventTree("Tell a tale", true);
        EventTree merchDecline = new EventTree("Politely decline", true);
        EventTree merchTellAct = new EventTree(
            "> You spin a short tale. The merchant is amused and gifts you a Trinket of Comfort.",
            false, player -> {
                player.getInventory().addItem(new Usable("Trinket of Comfort", "Restores 3 HP when used", 15, 1, 3));
                System.out.println("> The merchant smiles and hands you the trinket.");
            }
        );
        EventTree merchDeclineAct = new EventTree(
            "> You decline and continue on your way.",
            false, player -> {
                System.out.println("> The merchant nods and returns to his stall.");
            }
        );
        merchRoot.addBranch(merchTell);
        merchRoot.addBranch(merchDecline);
        merchRoot.addBranch(stopExploring);
        merchTell.addBranch(merchTellAct);
        merchDecline.addBranch(merchDeclineAct);

        // 6. Tavern gossip (choice: join / listen / leave)
        EventTree tavRoot = new EventTree(
            "> Over a cup of tea you hear rumors of distant places."
        , false);
        EventTree tavJoin = new EventTree("Join the table", true);
        EventTree tavListen = new EventTree("Listen quietly", true);
        EventTree tavLeave = new EventTree("Leave", true);
        EventTree tavJoinAct = new EventTree(
            "> You join the gossip and share a laugh. The tavern regulars thank you with a small tip.",
            false, player -> {
                int golds = 1 + random.nextInt(4);
                player.gainGold(golds);
                System.out.println("> You received " + golds + " gold from friendly regulars.");
            }
        );
        EventTree tavListenAct = new EventTree(
            "> You hear a rumor about a distant dungeon. Nothing immediate, but knowledge is power.",
            false, player -> {
                System.out.println("> The rumor lingers in your mind.");
            }
        );
        EventTree tavLeaveAct = new EventTree(
            "> You step away from the chatter and continue exploring.",
            false, player -> {}
        );
        tavRoot.addBranch(tavJoin);
        tavRoot.addBranch(tavListen);
        tavRoot.addBranch(tavLeave);
        tavRoot.addBranch(stopExploring);
        tavJoin.addBranch(tavJoinAct);
        tavListen.addBranch(tavListenAct);
        tavLeave.addBranch(tavLeaveAct);

        // 7. Guard asks for help (choice: help / refuse)
        EventTree guardRoot = new EventTree(
            "> A city guard asks you to help chase some stray animals from the market."
        , false);
        EventTree guardHelp = new EventTree("Help the guard", true);
        EventTree guardRefuse = new EventTree("Refuse", true);
        EventTree guardHelpAct = new EventTree(
            "> You help and the grateful guard rewards you.",
            false, player -> {
                int golds = 3 + random.nextInt(6);
                player.gainGold(golds);
                System.out.println("> The guard gives you " + golds + " gold in thanks.");
            }
        );
        EventTree guardRefuseAct = new EventTree(
            "> You refuse and move on.",
            false, player -> {}
        );
        guardRoot.addBranch(guardHelp);
        guardRoot.addBranch(guardRefuse);
        guardRoot.addBranch(stopExploring);
        guardHelp.addBranch(guardHelpAct);
        guardRefuse.addBranch(guardRefuseAct);

        // 8. Wallet (choice: return / hand to lost-and-found)
        EventTree walletRoot = new EventTree(
            "> You find a wallet on a bench. Do you try to return it?"
        , false);
        EventTree walletReturn = new EventTree("Find the owner", true);
        EventTree walletLostFound = new EventTree("Take to lost-and-found", true);
        EventTree walletReturnAct = new EventTree(
            "> You find the owner and return it. They reward your honesty.",
            false, player -> {
                int xp = 2 + random.nextInt(4);
                player.gainXP(xp);
                System.out.println("> The owner thanks you and gives you " + xp + " XP.");
            }
        );
        EventTree walletLostFoundAct = new EventTree(
            "> You leave the wallet with the lost-and-found. Feels like the right thing to do.",
            false, player -> {}
        );
        walletRoot.addBranch(walletReturn);
        walletRoot.addBranch(walletLostFound);
        walletRoot.addBranch(stopExploring);
        walletReturn.addBranch(walletReturnAct);
        walletLostFound.addBranch(walletLostFoundAct);

        // 9. Herb (choice: accept / decline)
        EventTree herbRoot = new EventTree(
            "> A herbalist offers you a strange herb for free."
        , false);
        EventTree herbAccept = new EventTree("Accept the herb", true);
        EventTree herbDecline = new EventTree("Decline politely", true);
        EventTree herbAcceptAct = new EventTree(
            "> The herb looks useful. The herbalist hands it to you.",
            false, player -> {
                player.getInventory().addItem(new Usable("Strange Herb", "Slightly restores energy (2)", 0, 3, 2));
                System.out.println("> You received a Strange Herb.");
            }
        );
        EventTree herbDeclineAct = new EventTree(
            "> You politely decline and continue your roam.",
            false, player -> {}
        );
        herbRoot.addBranch(herbAccept);
        herbRoot.addBranch(herbDecline);
        herbRoot.addBranch(stopExploring);
        herbAccept.addBranch(herbAcceptAct);
        herbDecline.addBranch(herbDeclineAct);

        // 10. Quiet bench (choice: rest / keep walking)
        EventTree benchRoot = new EventTree(
            "> You sit on a quiet bench and watch the city life pass by."
        , false);
        EventTree benchRest = new EventTree("Rest a while", true);
        EventTree benchWalk = new EventTree("Keep walking", true);
        EventTree benchRestAct = new EventTree(
            "> You rest a bit and feel refreshed.",
            false, player -> {
                int heal = 2 + random.nextInt(4);
                player.addHP(heal);
                System.out.println("> You recovered " + heal + " HP.");
            }
        );
        EventTree benchWalkAct = new EventTree(
            "> You continue exploring the city streets.",
            false, player -> {}
        );
        benchRoot.addBranch(benchRest);
        benchRoot.addBranch(benchWalk);
        benchRoot.addBranch(stopExploring);
        benchRest.addBranch(benchRestAct);
        benchWalk.addBranch(benchWalkAct);

        // Additional 5 events (11-15)
        // 11. Street duel (choice: intervene / watch)
        EventTree duelRoot = new EventTree(
            "> A heated argument escalates into a small duel between two locals."
        , false);
        EventTree duelIntervene = new EventTree("Intervene to stop it", true);
        EventTree duelWatch = new EventTree("Watch from afar", true);
        EventTree duelInterveneAct = new EventTree(
            "> You step between them and calm the situation. Grateful locals applaud.",
            false, player -> {
                int xp = 2 + random.nextInt(3);
                player.gainXP(xp);
                System.out.println("> Your diplomacy earned you " + xp + " XP.");
            }
        );
        EventTree duelWatchAct = new EventTree(
            "> The duel is brief and soon forgotten."
        , false, player -> {});
        duelRoot.addBranch(duelIntervene);
        duelRoot.addBranch(duelWatch);
        duelRoot.addBranch(stopExploring);
        duelIntervene.addBranch(duelInterveneAct);
        duelWatch.addBranch(duelWatchAct);

        // 12. Children playing (choice: join / watch)
        EventTree playRoot = new EventTree(
            "> Children play a simple game in the square and invite you."
        , false);
        EventTree playJoin = new EventTree("Join them", true);
        EventTree playWatch = new EventTree("Watch them", true);
        EventTree playJoinAct = new EventTree(
            "> You join and feel joy. Your energy is slightly restored.",
            false, player -> {
                player.gainEnergy(1);
                System.out.println("> You feel a small boost of energy.");
            }
        );
        EventTree playWatchAct = new EventTree(
            "> You smile at their antics and move on."
        , false, player -> {});
        playRoot.addBranch(playJoin);
        playRoot.addBranch(playWatch);
        playRoot.addBranch(stopExploring);
        playJoin.addBranch(playJoinAct);
        playWatch.addBranch(playWatchAct);

        // 13. Stray dog (choice: pet / ignore)
        EventTree dogRoot = new EventTree(
            "> A friendly stray dog wags its tail and approaches you."
        , false);
        EventTree dogPet = new EventTree("Pet the dog", true);
        EventTree dogIgnore = new EventTree("Ignore it", true);
        EventTree dogPetAct = new EventTree(
            "> The dog seems grateful and licks your hand. You feel a little happier.",
            false, player -> {
                player.addHP(1);
                System.out.println("> The small gesture restores 1 HP.");
            }
        );
        EventTree dogIgnoreAct = new EventTree(
            "> The dog wanders off."
        , false, player -> {});
        dogRoot.addBranch(dogPet);
        dogRoot.addBranch(dogIgnore);
        dogRoot.addBranch(stopExploring);
        dogPet.addBranch(dogPetAct);
        dogIgnore.addBranch(dogIgnoreAct);

        // 14. Suspicious note (choice: read / leave)
        EventTree noteRoot = new EventTree(
            "> You find a folded note tucked under a doorstep."
        , false);
        EventTree noteRead = new EventTree("Open the note", true);
        EventTree noteLeave = new EventTree("Leave it be", true);
        EventTree noteReadAct = new EventTree(
            "> The note contains a small map with a circled 'X'. You tuck it away for later.",
            false, player -> {
                System.out.println("> You keep the map; it might be useful someday.");
            }
        );
        EventTree noteLeaveAct = new EventTree(
            "> You decide not to meddle and continue exploring.",
            false, player -> {});
        noteRoot.addBranch(noteRead);
        noteRoot.addBranch(noteLeave);
        noteRoot.addBranch(stopExploring);
        noteRead.addBranch(noteReadAct);
        noteLeave.addBranch(noteLeaveAct);

        // 15. Fortune teller (choice: pay / refuse)
        EventTree fateRoot = new EventTree(
            "> A fortune teller beckons you to read your future for a small fee."
        , false);
        EventTree fatePay = new EventTree("Pay the fee", true);
        EventTree fateRefuse = new EventTree("Refuse", true);
        EventTree fatePayAct = new EventTree(
            "> You pay a few coins and receive a small charm.",
            false, player -> {
                int cost = 2;
                player.takeGold(cost);
                player.getInventory().addItem(new Usable("Lucky Charm", "A tiny charm that calms your nerves.", 0, 1, 1));
                System.out.println("> You paid " + cost + " gold and received a Lucky Charm.");
            }
        );
        EventTree fateRefuseAct = new EventTree(
            "> You decline and step away."
        , false, player -> {});
        fateRoot.addBranch(fatePay);
        fateRoot.addBranch(fateRefuse);
        fateRoot.addBranch(stopExploring);
        fatePay.addBranch(fatePayAct);
        fateRefuse.addBranch(fateRefuseAct);

        // gather all roots
        events.add(luckyRoot);
        events.add(perfRoot);
        events.add(childRoot);
        events.add(pickRoot);
        events.add(merchRoot);
        events.add(tavRoot);
        events.add(guardRoot);
        events.add(walletRoot);
        events.add(herbRoot);
        events.add(benchRoot);
        events.add(duelRoot);
        events.add(playRoot);
        events.add(dogRoot);
        events.add(noteRoot);
        events.add(fateRoot);

        return events;
    }
}
