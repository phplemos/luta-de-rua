package controle;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import javax.swing.*;

import modelo.Player;
import visao.Selecao;
import visao.Vencedor;


public class Game extends JFrame implements Runnable {

    private volatile boolean running = true;

    Player player1;
    Boolean keyRight = false, keyLeft = false, keyUp = false, keyNum1 = false, keyNum2 = false, keyNum3 = false;

    Player player2;
    Boolean keyA = false, keyD = false, keyW = false, keyJ = false, keyK = false, keyL = false;

    public JProgressBar healthBarP1 = new JProgressBar();
    public JProgressBar healthBarP2 = new JProgressBar();
    int hp1 = 250, hp2 = 250;

    public JLabel timerLabel = new JLabel("60", SwingConstants.CENTER);
    public JLabel p1WinsLabel = new JLabel("Venceu: 0", SwingConstants.LEFT);
    public JLabel p2WinsLabel = new JLabel("Venceu: 0", SwingConstants.RIGHT);
    public JLabel roundResultLabel = new JLabel("", SwingConstants.CENTER);
    
    int timeLeft = 60;
    int p1Wins = 0, p2Wins = 0;
    long lastTickTime;

    Thread t;
    Integer speed = 8;
    Boolean collision = false;
    
    Boolean p1HasAttacked = false;
    Boolean p2HasAttacked = false;

    private JLabel stage = null;
    private JPanel battlePanel = null;

    public Game() {
        setUndecorated(true);
        initComponents();
        
        java.awt.GraphicsDevice gd = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(this);
        } else {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setVisible(true);
        }
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
        getContentPane().setLayout(new java.awt.GridBagLayout());
        getContentPane().setBackground(Color.BLACK);

        pack();
    }

    public static void main(String[] args) {
        Selecao selecao = new Selecao();

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Game g = new Game();
                g.setScreen(selecao);
            }
        });
    }

    public void setScreen(JPanel panel) {
        getContentPane().removeAll();
        panel.setPreferredSize(new java.awt.Dimension(800, 600));
        panel.setMinimumSize(new java.awt.Dimension(800, 600));
        panel.setMaximumSize(new java.awt.Dimension(800, 600));
        getContentPane().add(panel, new java.awt.GridBagConstraints());
        revalidate();
        repaint();
        panel.requestFocusInWindow();
    }

    public void startGame(Player p1, Player p2) {
        this.player1 = p1;
        this.player2 = p2;
        p1Wins = 0;
        p2Wins = 0;
        
        AudioPlayer.playBGM("bgm_fight.wav");

        battlePanel = new JPanel(null);
        battlePanel.setPreferredSize(new java.awt.Dimension(800, 600));
        battlePanel.setBackground(Color.BLACK);

        healthBarP1.setBounds(30, 30, 300, 15);
        healthBarP1.setMinimum(0);
        healthBarP1.setMaximum(250);

        healthBarP2.setBounds(450, 30, 300, 15);
        healthBarP2.setMinimum(0);
        healthBarP2.setMaximum(250);

        timerLabel.setBounds(350, 10, 100, 50);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 40));
        timerLabel.setForeground(Color.WHITE);

        p1WinsLabel.setBounds(30, 10, 100, 20);
        p1WinsLabel.setForeground(Color.WHITE);

        p2WinsLabel.setBounds(650, 10, 100, 20);
        p2WinsLabel.setForeground(Color.WHITE);

        roundResultLabel.setBounds(0, 200, 800, 100);
        roundResultLabel.setFont(new Font("Arial", Font.BOLD, 48));
        roundResultLabel.setForeground(Color.YELLOW);
        roundResultLabel.setVisible(false);

        battlePanel.add(healthBarP1);
        battlePanel.add(healthBarP2);
        battlePanel.add(timerLabel);
        battlePanel.add(p1WinsLabel);
        battlePanel.add(p2WinsLabel);
        battlePanel.add(roundResultLabel);

        battlePanel.add(player1);
        battlePanel.add(player2);
        battlePanel.add(getStage());

        setScreen(battlePanel);

        startRound();
    }

    public void startRound() {
        hp1 = 250;
        hp2 = 250;
        healthBarP1.setValue(hp1);
        healthBarP2.setValue(hp2);
        
        timeLeft = 60;
        timerLabel.setText(String.valueOf(timeLeft));
        lastTickTime = System.currentTimeMillis();
        
        keyA = false; keyD = false; keyW = false; keyJ = false; keyK = false; keyL = false;
        keyRight = false; keyLeft = false; keyUp = false; keyNum1 = false; keyNum2 = false; keyNum3 = false;
        p1HasAttacked = false; p2HasAttacked = false;
        
        player1.x = 100;
        player1.y = 430;
        player1.isJumping = false;
        player1.setIconStopped();
        player1.setup();
        healthBarP1.setForeground(player1.getColor());
        
        player2.x = 600;
        player2.y = 430;
        player2.isJumping = false;
        player2.setIconStopped();
        player2.setup();
        healthBarP2.setForeground(player2.getColor());
        
        p1WinsLabel.setText("Venceu: " + p1Wins);
        p2WinsLabel.setText("Venceu: " + p2Wins);
        
        revalidate();
        repaint();
        this.setFocusable(true);
        this.requestFocusInWindow();
        
        this.running = true;
        t = new Thread(this);
        t.start();
    }

    public void checkRoundEnd(boolean timeOut) {
        if (!running) return;
        running = false;
        
        AudioPlayer.stopBGM();
        AudioPlayer.playSound("ko.wav");
        
        int winner = 0; // 1 = P1, 2 = P2, 0 = Tie
        
        if (timeOut) {
            if (hp1 > hp2) winner = 1;
            else if (hp2 > hp1) winner = 2;
        } else {
            if (hp1 <= 0 && hp2 > 0) winner = 2;
            else if (hp2 <= 0 && hp1 > 0) winner = 1;
        }
        
        if (winner == 1) p1Wins++;
        else if (winner == 2) p2Wins++;
        
        p1WinsLabel.setText("Venceu: " + p1Wins);
        p2WinsLabel.setText("Venceu: " + p2Wins);
        
        if (p1Wins == 2 || p2Wins == 2) {
            String msg = (p1Wins == 2) ? "PLAYER 1 VENCEU!" : "PLAYER 2 VENCEU!";
            Color winnerColor = (p1Wins == 2) ? Color.CYAN : Color.RED;
            Vencedor vencedorPanel = new Vencedor(msg, winnerColor);
            setScreen(vencedorPanel);
            revalidate();
            repaint();
            vencedorPanel.requestFocusInWindow();
        } else {
            String msg = (winner == 1) ? "PLAYER 1 VENCEU O ROUND!" : (winner == 2) ? "PLAYER 2 VENCEU O ROUND!" : "EMPATE!";
            roundResultLabel.setText(msg);
            roundResultLabel.setVisible(true);
            
            javax.swing.Timer timer = new javax.swing.Timer(3000, new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    roundResultLabel.setVisible(false);
                    AudioPlayer.playBGM("bgm_fight.wav");
                    startRound();
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    //Tecla Pressionada
    private void formKeyPressed(KeyEvent evt) {
        switch (evt.getKeyCode()) {
            //Player 1
            case KeyEvent.VK_W:
                keyW = true;
                break;
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
            case KeyEvent.VK_UP:
                keyUp = true;
                break;
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
            case KeyEvent.VK_W:
                keyW = false;
                break;
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
            case KeyEvent.VK_UP:
                keyUp = false;
                break;
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
        if (keyW) {
            player1.jump();
        }

        boolean p1IsAttacking = keyJ || keyK || keyL;

        if (!p1IsAttacking) {
            if (keyA) {
                Rectangle nextStep = new Rectangle(player1.x - speed + 20, player1.y, 50, 127);
                boolean movingAway = player1.x <= player2.x;
                if (player1.x >= 0 && (movingAway || !nextStep.intersects(player2.getHitbox()))) {
                    player1.setIconLeft();
                    player1.x -= speed;
                }
            }

            if (keyD) {
                Rectangle nextStep = new Rectangle(player1.x + speed + 20, player1.y, 50, 127);
                boolean movingAway = player1.x >= player2.x;
                if (player1.x <= 706 && (movingAway || !nextStep.intersects(player2.getHitbox()))) {
                    player1.setIconRight();
                    player1.x += speed;
                }
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
                        hp2 -= 5;
                        healthBarP2.setValue(hp2);
                        AudioPlayer.playSound("block.wav");
                        if (hp2 <= 0) {
                            checkRoundEnd(false);
                            return;
                        }
                    } else {
                        //Punch Damage without defense
                        hp2 -= 15;
                        healthBarP2.setValue(hp2);
                        AudioPlayer.playSound("hit.wav");
                        if (hp2 <= 0) {
                            checkRoundEnd(false);
                            return;
                        }
                    }
                } else {
                    AudioPlayer.playSound("whiff.wav");
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
                        hp2 -= 10;
                        healthBarP2.setValue(hp2);
                        AudioPlayer.playSound("block.wav");
                        if (hp2 <= 0) {
                            checkRoundEnd(false);
                            return;
                        }
                    } else {
                        //Kick Damage without defense
                        hp2 -= 25;
                        healthBarP2.setValue(hp2);
                        AudioPlayer.playSound("hit.wav");
                        if (hp2 <= 0) {
                            checkRoundEnd(false);
                            return;
                        }
                    }
                } else {
                    AudioPlayer.playSound("whiff.wav");
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
        if (keyUp) {
            player2.jump();
        }

        boolean p2IsAttacking = keyNum1 || keyNum2 || keyNum3;

        if (!p2IsAttacking) {
            if (keyRight) {
                Rectangle nextStep = new Rectangle(player2.x + speed + 20, player2.y, 50, 127);
                boolean movingAway = player2.x >= player1.x;
                if (player2.x <= 706 && (movingAway || !nextStep.intersects(player1.getHitbox()))) {
                    player2.setIconRight();
                    player2.x += speed;
                }
            }

            if (keyLeft) {
                Rectangle nextStep = new Rectangle(player2.x - speed + 20, player2.y, 50, 127);
                boolean movingAway = player2.x <= player1.x;
                if (player2.x >= 0 && (movingAway || !nextStep.intersects(player1.getHitbox()))) {
                    player2.setIconLeft();
                    player2.x -= speed;
                }
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
                        hp1 -= 5;
                        healthBarP1.setValue((int) hp1);
                        AudioPlayer.playSound("block.wav");
                        if (hp1 <= 0) {
                            checkRoundEnd(false);
                            return;
                        }
                    } else {
                        //Punch Damage without defense
                        hp1 -= 15;
                        healthBarP1.setValue((int) hp1);
                        AudioPlayer.playSound("hit.wav");
                        if (hp1 <= 0) {
                            checkRoundEnd(false);
                            return;
                        }
                    }
                } else {
                    AudioPlayer.playSound("whiff.wav");
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
                        hp1 -= 10;
                        healthBarP1.setValue(hp1);
                        AudioPlayer.playSound("block.wav");
                        if (hp1 <= 0) {
                            checkRoundEnd(false);
                            return;
                        }
                    } else {
                        //Kick Damage without defense
                        hp1 -= 25;
                        healthBarP1.setValue(hp1);
                        AudioPlayer.playSound("hit.wav");
                        if (hp1 <= 0) {
                            checkRoundEnd(false);
                            return;
                        }
                    }
                } else {
                    AudioPlayer.playSound("whiff.wav");
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
        // Expande o hitbox temporário em 15 pixels para a esquerda e direita
        // garantindo que os ataques alcancem mesmo se a física de bloqueio (corpos sólidos) travar o movimento
        rectangle1.grow(15, 0); 
        collision = rectangle1.intersects(rectangle2);
    }

    @Override
    public void run() {
        while (running) { // Use a loop condition flag
            try {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastTickTime >= 1000) {
                    timeLeft--;
                    lastTickTime = currentTime;
                    SwingUtilities.invokeLater(() -> timerLabel.setText(String.valueOf(timeLeft)));
                    if (timeLeft <= 0) {
                        SwingUtilities.invokeLater(() -> checkRoundEnd(true));
                    }
                }
                
                SwingUtilities.invokeLater(() -> {
                    if (running) {
                        updateGameP1();
                        updateGameP2();
                    }
                });
                Thread.sleep(20);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

}
