package modelo;

import javax.swing.*;
import java.awt.*;

public class Player extends JLabel {

    public int x;
    public int y = 430;
    private int playerNum;
    private Color color;
    private ImageIcon walkL;
    private ImageIcon walkR;
    private ImageIcon stopped;
    private ImageIcon punch;
    private ImageIcon kick;
    private ImageIcon defense;
    private ImageIcon ultimaImg = null;

    private String champ;

    public Player(int playerNum, String champ, Color color) {
        this.playerNum = playerNum;
        this.x = (playerNum == 1) ? 100 : 600;
        this.champ = champ;
        this.color = color;
    }

    public void setup() {
        if (champ != null) {
            String folderPrefix = getFolderPrefix(champ);
            String folder = folderPrefix + "P" + playerNum;
            String filePrefix = champ.equalsIgnoreCase("chun") ? "chunli" : champ.toLowerCase();

            // Right/Left walk mapping differs based on player orientation
            String walkRightFile = (playerNum == 1) ? filePrefix + "_walk.gif" : filePrefix + "_walkback.gif";
            String walkLeftFile = (playerNum == 1) ? filePrefix + "_walkback.gif" : filePrefix + "_walk.gif";

            String basePath = "/assets/" + folder + "/";
            walkR = loadIcon(basePath + walkRightFile);
            walkL = loadIcon(basePath + walkLeftFile);
            punch = loadIcon(basePath + filePrefix + "_punch.gif");
            kick = loadIcon(basePath + filePrefix + "_kick.gif");
            defense = loadIcon(basePath + filePrefix + "_defense.gif");
            stopped = loadIcon(basePath + filePrefix + "_pd.gif");
        }

        setBounds(x, y, 90, 127);
    }

    private String getFolderPrefix(String champ) {
        switch (champ.toLowerCase()) {
            case "chun":
                return "Chunli";
            case "sheeva":
                return "Sheeva";
            case "akuma":
                return "Akuma";
            case "cable":
                return "Cable";
            case "spider":
                return "Spider";
            case "doom":
                return "Doom";
            default:
                return champ.substring(0, 1).toUpperCase() + champ.substring(1).toLowerCase();
        }
    }

    private ImageIcon loadIcon(String path) {
        java.net.URL res = getClass().getResource(path);
        if (res == null) return null;
        return new ImageIcon(new ImageIcon(res).getImage().getScaledInstance(88, 127, Image.SCALE_DEFAULT));
    }

    public void move() {
        setBounds(x, y, 90, 127);
    }

    public void setIconRight() {
        setIcon(walkR);
        this.ultimaImg = walkR;
    }

    public void setIconLeft() {
        setIcon(walkL);
        this.ultimaImg = walkL;
    }

    public void setIconPunch() {
        setIcon(punch);
        this.ultimaImg = punch;
    }

    public void setIconKick() {
        setIcon(kick);
        this.ultimaImg = kick;
    }

    public void setIconDef() {
        setIcon(defense);
        this.ultimaImg = defense;
    }

    public void setIconStopped() {
        setIcon(stopped);
    }

    public ImageIcon getUltimaImg() {
        return ultimaImg;
    }

    public Color getColor() {
        return color;
    }

    public void setIconRight1() {
        setIconRight();
    }

    public void setIconRight2() {
        setIconRight();
    }

    public void setIconLeft1() {
        setIconLeft();
    }

    public void setIconLeft2() {
        setIconLeft();
    }

    public void setIconPunch1() {
        setIconPunch();
    }

    public void setIconPunch2() {
        setIconPunch();
    }

    public void setIconKick1() {
        setIconKick();
    }

    public void setIconKick2() {
        setIconKick();
    }

    public void setIconDef1() {
        setIconDef();
    }

    public void setIconDef2() {
        setIconDef();
    }

    public void setIconStopped1() {
        setIconStopped();
    }

    public void setIconStopped2() {
        setIconStopped();
    }

    public void move1() {
        move();
    }

    public void move2() {
        move();
    }
}