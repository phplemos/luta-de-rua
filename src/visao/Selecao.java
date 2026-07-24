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
    private JLabel p1Label = new JLabel("PLAYER 1: SELECIONANDO...");
    private JLabel p2Label = new JLabel("PLAYER 2: AGUARDANDO...");
    private JLabel matchInfoLabel = new JLabel("LUTA DE RUA - MELHOR DE 3 (60s)");

    public Selecao() {
        super();
        setLayout(null);

        add(getMenu());
        getFight().addActionListener(this);
        
        setupKeyBindings();
        
        AudioPlayer.playBGM("bgm_menu.wav");
    }
    
    private void setupKeyBindings() {
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "undo");
        this.getActionMap().put("undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undoSelection();
            }
        });
    }

    private void undoSelection() {
        if (click == 3) {
            click = 2;
            champP2 = null;
            p2Label.setText("PLAYER 2: SELECIONANDO...");
            for (int i = 0; i < CHARACTERS.length; i++) {
                if (Color.RED.equals(playButtons[i].getBackground())) {
                    playButtons[i].setBackground(null);
                    playButtons[i].setOpaque(false);
                    break;
                }
            }
            AudioPlayer.playSound("select.wav");
        } else if (click == 2) {
            click = 1;
            champP1 = null;
            p1Label.setText("PLAYER 1: SELECIONANDO...");
            p2Label.setText("PLAYER 2: AGUARDANDO...");
            for (int i = 0; i < CHARACTERS.length; i++) {
                if (Color.CYAN.equals(playButtons[i].getBackground())) {
                    playButtons[i].setBackground(null);
                    playButtons[i].setOpaque(false);
                    break;
                }
            }
            AudioPlayer.playSound("select.wav");
        }
    }

    public JLabel getMenu() {
        if (fundo == null) {
            fundo = new JLabel(new ImageIcon(getClass().getResource("/assets/Selecao.png")));
            fundo.setSize(800, 600);
            
            p1Label.setBounds(20, 20, 300, 30);
            p1Label.setFont(new Font("Arial", Font.BOLD, 24));
            p1Label.setForeground(Color.CYAN);
            fundo.add(p1Label);
            
            p2Label.setBounds(480, 20, 300, 30);
            p2Label.setFont(new Font("Arial", Font.BOLD, 24));
            p2Label.setForeground(Color.RED);
            p2Label.setHorizontalAlignment(SwingConstants.RIGHT);
            fundo.add(p2Label);
            
            matchInfoLabel.setBounds(0, 180, 800, 30);
            matchInfoLabel.setFont(new Font("Arial", Font.BOLD, 22));
            matchInfoLabel.setForeground(Color.YELLOW);
            matchInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            fundo.add(matchInfoLabel);

            for (int i = 0; i < CHARACTERS.length; i++) {
                CharacterData cd = CHARACTERS[i];
                playButtons[i] = new JButton(new ImageIcon(getClass().getResource("/assets/"+cd.getIconFile())));
                playButtons[i].setBounds(cd.getX(), 238, cd.getWidth(), 244);
                playButtons[i].addActionListener(this);
                playButtons[i].setBackground(null);
                fundo.add(playButtons[i]);
            }
            fundo.add(getFight());
        }
        return fundo;
    }

    public JButton getFight() {
        if (fight == null) {
            fight = new JButton(new ImageIcon(getClass().getResource("/assets/fight.png")));
            fight.setBounds(325, 20, 150, 150);
            fight.setBorderPainted(false);
            fight.setContentAreaFilled(false);
            fight.setFocusPainted(false);
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
                    playButtons[i].setBackground(Color.CYAN);
                    playButtons[i].setOpaque(true);
                    p1Label.setText("PLAYER 1: " + champP1.toUpperCase());
                    p2Label.setText("PLAYER 2: SELECIONANDO...");
                    AudioPlayer.playSound("select.wav");
                } else if (click == 2) {
                    champP2 = CHARACTERS[i].getId();
                    playButtons[i].setBackground(Color.RED);
                    playButtons[i].setOpaque(true);
                    p2Label.setText("PLAYER 2: " + champP2.toUpperCase());
                    click = 3;
                    AudioPlayer.playSound("select.wav");
                }
                return;
            }
        }

        // Check Fight button
        if (click == 3) {
            if (e.getSource() == getFight()) {
                Game gframe = (Game) SwingUtilities.getWindowAncestor(this);
                if(gframe != null){
                    AudioPlayer.stopBGM();
                    AudioPlayer.playSound("fight.wav");
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
		
