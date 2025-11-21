package registry;

import java.util.ArrayList;

import items.Literature;

public class LiteratureRegistry {
    public static ArrayList<Literature> tomekeepLibrary() {
        ArrayList<Literature> books = new ArrayList<>();

        // twots
        Literature spiral = new Literature("The Whispers of the Spiral", "To know the world is to listen to what sleeps beneath it.");
        spiral.addContent("> 'In the beginning, there was no sun, no moon, no land; only the Spiral.'");
        spiral.addContent("> So begins the oldest surviving myth in Eryndral's scattered histories. \nThe Spiral, a force beyond comprehension, is said to have woven the threads of reality, spinning stars, stone, and soul into being. \nFrom it, the world took form; wild, untamed, and brimming with wonder.");
        spiral.addContent("> Civilizations bloomed where the Spiral whispered loudest. \nKingdoms touched the sky, scholars etched truth into stone, and gods walked among mortals. \nBut nothing born of the Spiral is ever still. All things must move, must change.");
        spiral.addContent("> So came the Great Silence. The gods withdrew. The cities crumbled. Knowledge was lost to time and war. \nAnd though the world endures, the Spiral speaks now only in riddles; through dreams, through relics, through the trembling of the earth before dawn.");
        spiral.addContent("> Today, few believe in its power. Fewer still hear its call. But those who do... those who follow its echo... \nthey say the world is beginning to turn again.");
        spiral.addContent("> They say fate itself is stirring.");

        books.add(spiral);

        return books;
    }
}
