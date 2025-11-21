package game.Event;

import java.util.ArrayList;
import java.util.List;

import characters.Player;
import game.Event.EventInterface.EventAction;

public class EventTree {
    private String text;
    private List<EventTree> branches = new ArrayList<>();
    private EventAction action;
    private boolean isChoice;

    public EventTree(String text, boolean isChoice) {
        this.text = text;
        this.action = null;
        this.isChoice = isChoice;
    }

    public EventTree(String text, boolean isChoice, EventAction action) {
        this.text = text;
        this.action = action;
        this.isChoice = isChoice;
    }

    public EventTree addBranch(EventTree node) {
        branches.add(node);
        return this;
    }

    public boolean isChoice() {
        return isChoice;
    }

    public String getText() {
        return text;
    }

    public List<EventTree> getBranches() {
        return branches;
    }

    public void performAction(Player player) {
        if (action != null) action.execute(player);
    }
}
