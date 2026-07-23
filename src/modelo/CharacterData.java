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
            new CharacterData("chun", "play1.gif", 40, 98),
            new CharacterData("sheeva", "play2.gif", 140, 117),
            new CharacterData("akuma", "play3.gif", 259, 120),
            new CharacterData("cable", "play4.gif", 382, 117),
            new CharacterData("spider", "play5.gif", 500, 129),
            new CharacterData("doom", "play6.gif", 629, 129)
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

