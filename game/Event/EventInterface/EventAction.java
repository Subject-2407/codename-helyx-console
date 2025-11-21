package game.Event.EventInterface;

import characters.Player;

@FunctionalInterface
public interface EventAction {
    void execute(Player player);
}