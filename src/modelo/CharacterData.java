package modelo;

public class CharacterData {
    final String id;
    final String iconFile;
    final int x;
    final int width;

    CharacterData(String id, String iconFile, int x, int width) {
        this.id = id;
        this.iconFile = iconFile;
        this.x = x;
        this.width = width;
    }

    public static final CharacterData[] CHARACTERS = {
            new CharacterData("chun", "play1.gif", 105, 110),
            new CharacterData("sheeva", "play2.gif", 265, 110),
            new CharacterData("akuma", "play3.gif", 425, 110),
            new CharacterData("cable", "play4.gif", 585, 110)
    };

    public int getWidth() {
        return width;
    }

    public int getX() {
        return x;
    }

    public String getId() {
        return id;
    }

    public String getIconFile() {
        return iconFile;
    }
}

