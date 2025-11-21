package items;

import java.util.*;

import characters.Player;
import utilities.UserInterface;

public class Literature {
    private String name;
    private String description;
    private Queue<String> contentQueue;

    public Literature(String name, String description) {
        this.name = name;
        this.description = description;
        this.contentQueue = new LinkedList<>(); 
    }

    public Literature(Literature other) {
        this.name = other.name;
        this.description = other.description;
        this.contentQueue = new LinkedList<>(other.contentQueue);
    }

    public String getName() { return name; }
    public String getDescription() { return description; }

    public void addContent(String content) {
        contentQueue.offer(content);
    }

    public void readContent(Player player) {
        while (!contentQueue.isEmpty()) {
            String content = contentQueue.poll();
            if (content != null) {
                player.displayInfo();
                System.out.println("Reading: " + name + "\n");
                System.out.println(content);
                UserInterface.enterReturn(player.getScanner());
            }
        }
    }
}
