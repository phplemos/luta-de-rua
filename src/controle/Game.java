package controle;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import javax.swing.*;

import modelo.Player;
import visao.Selecao;


public class Game extends JFrame implements Runnable {

    private volatile boolean running = true;

    Player player1;
    Boolean keyRight = false, keyLeft = false, keyNum1 = false, keyNum2 = false, keyNum3 = false;

    Player player2;
    Boolean keyA = false, keyD = false, keyJ = false, keyK = false, keyL = false;

    public JProgressBar healthBarP1 = new JProgressBar();
    public JProgressBar healthBarP2 = new JProgressBar();
    int hp1 = 250, hp2 = 250;

    Thread t;
    Integer speed = 4;
    Boolean collision = false;
    
    Boolean p1HasAttacked = false;
    Boolean p2HasAttacked = false;

    private JLabel stage = null;

    public Game() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
//        addWindowListener(new java.awt.event.WindowAdapter() {
//            public void windowOpened(java.awt.event.WindowEvent evt) {
//                formWindowOpened(evt);
//            }
//        });

        //Evento de botao
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                formKeyPressed(evt);
            }

            public void keyReleased(KeyEvent evt) {
                formKeyReleased(evt);
            }
        });
        getContentPane().setLayout(null);

        pack();
    }

    public static void main(String[] args) {
        Selecao selecao = new Selecao();

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Game g = new Game();
                g.setBounds(0, 0, 800, 600);
                g.setContentPane(selecao);
                g.setResizable(true);
                g.setVisible(true);
            }
        });
    }

    public void startGame(Player p1, Player p2) {
        getContentPane().removeAll();
        this.player1 = p1;
        this.player2 = p2;

        //Player 1
        player1.setup();

        //Barra de HP Player 1
        healthBarP1.setBounds(30, 30, 300, 15);
        healthBarP2.setMinimum(0);
        healthBarP1.setMaximum(250);
        healthBarP1.setValue(hp1);
        healthBarP1.setForeground(player1.getColor());
        this.add(healthBarP1);

        //Player 2
        player2.setup();

        //Barra de HP Player 2
        healthBarP2.setBounds(450, 30, 300, 15);
        healthBarP2.setMinimum(0);
        healthBarP2.setMaximum(250);
        healthBarP2.setValue(hp2);
        healthBarP2.setForeground(player2.getColor());
        this.add(healthBarP2);

        getContentPane().add(player1);
        getContentPane().add(player2);
        getContentPane().add(getStage());
        revalidate();
        repaint();
        this.setFocusable(true);
        this.requestFocusInWindow();
        t = new Thread(this);
        t.start();
    }

    //Tecla Pressionada
    private void formKeyPressed(KeyEvent evt) {
        switch (evt.getKeyCode()) {
            //Player 1
            case KeyEvent.VK_A:
                keyA = true;
                break;
            case KeyEvent.VK_D:
                keyD = true;
                break;
            case KeyEvent.VK_J:
                keyJ = true;
                break;
            case KeyEvent.VK_K:
                keyK = true;
                break;
            case KeyEvent.VK_L:
                keyL = true;
                break;

            //Player 2
            case KeyEvent.VK_RIGHT:
                keyRight = true;
                break;
            case KeyEvent.VK_LEFT:
                keyLeft = true;
                break;
            case KeyEvent.VK_NUMPAD1:
                keyNum1 = true;
                break;
            case KeyEvent.VK_NUMPAD2:
                keyNum2 = true;
                break;
            case KeyEvent.VK_NUMPAD3:
                keyNum3 = true;
                break;
        }
    }

    //Tecla Liberada
    private void formKeyReleased(KeyEvent evt) {
        switch (evt.getKeyCode()) {
            //Player 1
            case KeyEvent.VK_A:
                keyA = false;
                break;
            case KeyEvent.VK_D:
                keyD = false;
                break;
            case KeyEvent.VK_J:
                keyJ = false;
                break;
            case KeyEvent.VK_K:
                keyK = false;
                break;
            case KeyEvent.VK_L:
                keyL = false;
                break;

            //Player 2
            case KeyEvent.VK_RIGHT:
                keyRight = false;
                break;
            case KeyEvent.VK_LEFT:
                keyLeft = false;
                break;
            case KeyEvent.VK_NUMPAD1:
                keyNum1 = false;
                break;
            case KeyEvent.VK_NUMPAD2:
                keyNum2 = false;
                break;
            case KeyEvent.VK_NUMPAD3:
                keyNum3 = false;
                break;

        }
    }

    public JLabel getStage() {
        if (stage == null) {
            stage = new JLabel(new ImageIcon(getClass().getResource("/assets/Stage.gif")));
            stage.setSize(800, 600);
        }
        return stage;
    }

    //Açoes do P1
    public void updateGameP1() {
        if (keyA) {
            Rectangle nextStep = new Rectangle(player1.x - speed + 20, player1.y, 50, 127);
            if (player1.x >= 0 && !nextStep.intersects(player2.getHitbox())) {
                player1.setIconLeft();
                player1.x -= speed;
            }
        }

        if (keyD) {
            Rectangle nextStep = new Rectangle(player1.x + speed + 20, player1.y, 50, 127);
            if (player1.x <= 706 && !nextStep.intersects(player2.getHitbox())) {
                player1.setIconRight();
                player1.x += speed;
            }
        }

        //Punch
        if (keyJ) {
            player1.setIconPunch();
            if (!p1HasAttacked) {
                collision();
                if (collision) {
                    if (keyNum3 == true) {//Bloqueio do P2 ativo
                        //Punch Damage with defense
                        hp2 -= 1;
                        healthBarP2.setValue(hp2);
                        if (hp2 <= 0) {
                            this.running = false; // Stop game loop first
                            JOptionPane.showMessageDialog(null, "PLAYER 1 VENCE!!");
                        }
                    } else {
                        //Punch Damage without defense
                        hp2 -= 2;
                        healthBarP2.setValue(hp2);
                        if (hp2 <= 0) {
                            this.running = false; // Stop game loop first
                            JOptionPane.showMessageDialog(null, "PLAYER 1 VENCE!!");
                        }
                    }
                }
                p1HasAttacked = true;
            }
        } else if (keyK) {
            player1.setIconKick();
            if (!p1HasAttacked) {
                collision();
                if (collision) {
                    if (keyNum3 == true) {//Bloqueio do P2 ativo
                        //Kick Damage with defense
                        hp2 -= 3;
                        healthBarP2.setValue(hp2);
                        if (hp2 <= 0) {
                            this.running = false; // Stop game loop first
                            JOptionPane.showMessageDialog(null, "PLAYER 1 VENCE!!");
                        }
                    } else {
                        //Kick Damage without defense
                        hp2 -= 4;
                        healthBarP2.setValue(hp2);
                        if (hp2 <= 0) {
                            this.running = false; // Stop game loop first
                            JOptionPane.showMessageDialog(null, "PLAYER 1 VENCE!!");
                        }
                    }
                }
                p1HasAttacked = true;
            }
        } else {
            p1HasAttacked = false;
        }

        //Defense
        if (keyL) {
            player1.setIconDef();
        }

        if (!(keyA || keyD || keyJ || keyK || keyL)) {
            player1.setIconStopped();
        }

        player1.move();
    }

    //Açoes do P2
    public void updateGameP2() {
        if (keyRight) {
            Rectangle nextStep = new Rectangle(player2.x + speed + 20, player2.y, 50, 127);
            if (player2.x <= 706 && !nextStep.intersects(player1.getHitbox())) {
                player2.setIconRight();
                player2.x += speed;
            }
        }

        if (keyLeft) {
            Rectangle nextStep = new Rectangle(player2.x - speed + 20, player2.y, 50, 127);
            if (player2.x >= 0 && !nextStep.intersects(player1.getHitbox())) {
                player2.setIconLeft();
                player2.x -= speed;
            }
        }

        //Punch
        if (keyNum1) {
            player2.setIconPunch();
            if (!p2HasAttacked) {
                collision();
                if (collision) {
                    if (keyL == true) {//Bloqueio do P1 ativo
                        //Punch Damage with defense
                        hp1 -= 1;
                        healthBarP1.setValue((int) hp1);
                        if (hp1 <= 0) {
                            this.running = false; // Stop game loop first
                            JOptionPane.showMessageDialog(null, "PLAYER 2 VENCE!!");
                        }
                    } else {
                        //Punch Damage without defense
                        hp1 -= 2;
                        healthBarP1.setValue((int) hp1);
                        if (hp1 <= 0) {
                            this.running = false; // Stop game loop first
                            JOptionPane.showMessageDialog(null, "PLAYER 2 VENCE!!");
                        }
                    }
                }
                p2HasAttacked = true;
            }
        } else if (keyNum2) {
            player2.setIconKick();
            if (!p2HasAttacked) {
                collision();
                if (collision) {
                    if (keyL == true) {//Bloqueio do P1 ativo
                        //Kick Damage with defense
                        hp1 -= 3;
                        healthBarP1.setValue(hp1);
                        if (hp1 <= 0) {
                            this.running = false; // Stop game loop first
                            JOptionPane.showMessageDialog(null, "PLAYER 2 VENCE!!");
                        }
                    } else {
                        //Kick Damage without defense
                        hp1 -= 4;
                        healthBarP1.setValue(hp1);
                        if (hp1 <= 0) {
                            this.running = false; // Stop game loop first
                            JOptionPane.showMessageDialog(null, "PLAYER 2 VENCE!!");
                        }
                    }
                }
                p2HasAttacked = true;
            }
        } else {
            p2HasAttacked = false;
        }

        //Defense
        if (keyNum3) {
            player2.setIconDef();
        }

        if (!(keyLeft || keyRight || keyNum1 || keyNum2 || keyNum3)) {
            player2.setIconStopped();
        }

        player2.move();
    }

    //Verificar colisaao
    public void collision() {
        Rectangle rectangle1 = player1.getHitbox();
        Rectangle rectangle2 = player2.getHitbox();
        collision = rectangle1.intersects(rectangle2);
    }

    @Override
    public void run() {
        while (running) { // Use a loop condition flag
            try {
                SwingUtilities.invokeLater(() -> {
                    updateGameP1();
                    updateGameP2();
                });
                Thread.sleep(20);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

}
