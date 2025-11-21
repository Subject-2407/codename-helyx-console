package characters.npcs;

import characters.Inventory;
import characters.NPC;
import game.Event.EventTree;
import items.Equipment;
import items.Usable;
import utilities.UserInterface;

public class Unknown extends NPC{
    private String prefix;

    public Unknown() {
        super("???", UserInterface.PURPLE_BOLD, "A hooded figure standing quietly under the shadows of a crumbled ruin");
        prefix = getPrefixColor() + getName() + UserInterface.RESET;
    }

    public EventTree firstDialogue() {
        EventTree root = new EventTree(prefix + ": Ah... you're finally awake.", false);

        EventTree question = new EventTree(prefix + ": Who I am matters little... at least for now. Curious one, aren't you?", false);
        EventTree dismiss = new EventTree(prefix + ": No words? That's wise. Questions only lead to truths you may not be ready for.", false);

        EventTree askName = new EventTree(
            "Who are you?",
            true
        );
        EventTree staySilent = new EventTree(
            "Remain silent, unsure of what to say.",
            true
        );

        EventTree explanation = new EventTree(prefix + ": Well, no time for questions. The world won't wait for you to understand.", false);

        EventTree giveItems = new EventTree(
            prefix + ": You'll have to start with what little I can spare; a worn sword, tattered armor, and a few potions.\nIt's enough to keep you breathing... for now.",
            false,
            player -> {
                Inventory inventory = player.getInventory();
                Equipment woodenSword = new Equipment("Wooden Sword", "A sword made of wood.", 45, 1, 20, 5);
                Equipment leatherArmor = new Equipment("Leather Tunic", "A scuffed-up leather tunic", 0, 2, 0, 5);
                inventory.addItem(woodenSword);
                inventory.addItem(leatherArmor);
                inventory.addItem(new Usable("Health Potion", "Adds 50 HP.",40, 1, 50));
                inventory.addItem(new Usable("Health Potion", "Adds 50 HP.",40, 1, 50));
                player.equipItem(woodenSword);
                player.equipItem(leatherArmor);
            }
        );

        EventTree finalTip = new EventTree(prefix + ": Tread carefully, adventurer. This world is unforgiving.\nIf your blood spills, " + UserInterface.RED + "there's no one to bring you back." + UserInterface.RESET, false);

        root.addBranch(askName);
        root.addBranch(staySilent);

        askName.addBranch(question);
        question.addBranch(explanation);

        staySilent.addBranch(dismiss);
        dismiss.addBranch(explanation);

        explanation.addBranch(giveItems);
        giveItems.addBranch(finalTip);

        return root;
    }
}
