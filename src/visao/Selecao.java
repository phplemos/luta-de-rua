package visao;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import controle.*;
import modelo.CharacterData;
import modelo.Player;

import static modelo.CharacterData.CHARACTERS;

public class Selecao extends JPanel implements ActionListener {

    private final JButton[] playButtons = new JButton[CHARACTERS.length];
    private JButton fight;

    public static String champP1;
    public static String champP2;

    int click = 1;

    private JLabel fundo = null;

    public Selecao() {
        super();
        setLayout(null);

        add(getMenu());
        getFight().addActionListener(this);

    }

    public JLabel getMenu() {
        if (fundo == null) {
            fundo = new JLabel(new ImageIcon(getClass().getResource("/assets/Selecao.png")));
            fundo.setSize(800, 600);
            for (int i = 0; i < CHARACTERS.length; i++) {
                CharacterData cd = CHARACTERS[i];
                playButtons[i] = new JButton(new ImageIcon(getClass().getResource("/assets/"+cd.getIconFile())));
                playButtons[i].setBounds(cd.getX(), 238, cd.getWidth(), 244);
                playButtons[i].addActionListener(this);
                playButtons[i].setBackground(null);
                add(playButtons[i]);
            }
            add(getFight());
        }
        return fundo;
    }

    public JButton getFight() {
        if (fight == null) {
            fight = new JButton(new ImageIcon(getClass().getResource("/assets/fight.png")));
            fight.setBounds(400, 155, 130, 60);
        }
        return fight;
    }

    public static String getChampP1() {
        return champP1;
    }

    public static String getChampP2() {
        return champP2;
    }

    public void actionPerformed(ActionEvent e) {
        // Check character selection buttons
        for (int i = 0; i < CHARACTERS.length; i++) {
            if (e.getSource() == playButtons[i]) {
                if (click == 1) {
                    champP1 = CHARACTERS[i].getId();
                    click = 2;
                    playButtons[i].setBackground(Color.BLUE);
                    playButtons[i].setOpaque(true);
                } else if (click == 2) {
                    champP2 = CHARACTERS[i].getId();
                    playButtons[i].setBackground(Color.RED);
                    playButtons[i].setOpaque(true);
                    click = 3;
                }
                return;
            }
        }

        // Check Fight button
        if (click == 3) {
            if (e.getSource() == getFight()) {
                Game gframe = (Game) SwingUtilities.getWindowAncestor(this);
                if(gframe != null){
                    Player player1 = new Player(1, champP1, Color.BLUE);
                    Player player2 = new Player(2, champP2, Color.RED);
                    gframe.startGame(player1, player2);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Press the button FIGHT to start!!");
            }
        }
    }

}
		
