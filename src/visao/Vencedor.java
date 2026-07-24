package visao;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import controle.Game;
import controle.AudioPlayer;

public class Vencedor extends JPanel implements ActionListener {
    
    private JButton btnJogarNovamente;
    private JButton btnSair;
    private JLabel lblVencedor;
    private JLabel fundo;
    
    public Vencedor(String winnerMsg, Color winnerColor) {
        setLayout(null);
        
        AudioPlayer.playBGM("bgm_menu.wav");
        
        fundo = new JLabel(new ImageIcon(getClass().getResource("/assets/victory.png")));
        fundo.setBounds(0, 0, 800, 600);
        
        Font labelFont = new Font("Impact", Font.BOLD, 65);

        lblVencedor = new JLabel(winnerMsg);
        lblVencedor.setBounds(2, 100, 800, 80);
        lblVencedor.setFont(labelFont);
        lblVencedor.setForeground(winnerColor);
        lblVencedor.setHorizontalAlignment(SwingConstants.CENTER);
        fundo.add(lblVencedor);

        // Desenha contorno preto em 8 direções para legibilidade máxima
        int[] dx = {-2, 0, 2, -2, 2, -2, 0, 2};
        int[] dy = {-2, -2, -2, 0, 0, 2, 2, 2};
        for (int i = 0; i < dx.length; i++) {
            JLabel shadow = new JLabel(winnerMsg);
            shadow.setBounds(2 + dx[i], 100 + dy[i], 800, 80);
            shadow.setFont(labelFont);
            shadow.setForeground(Color.BLACK);
            shadow.setHorizontalAlignment(SwingConstants.CENTER);
            fundo.add(shadow);
        }
        
        btnJogarNovamente = new JButton("JOGAR NOVAMENTE");
        btnJogarNovamente.setBounds(250, 450, 300, 50);
        btnJogarNovamente.setFont(new Font("Arial", Font.BOLD, 20));
        btnJogarNovamente.setBackground(Color.DARK_GRAY);
        btnJogarNovamente.setForeground(Color.WHITE);
        btnJogarNovamente.setFocusPainted(false);
        btnJogarNovamente.addActionListener(this);
        fundo.add(btnJogarNovamente);
        
        btnSair = new JButton("SAIR DO JOGO");
        btnSair.setBounds(250, 510, 300, 50);
        btnSair.setFont(new Font("Arial", Font.BOLD, 20));
        btnSair.setBackground(Color.RED);
        btnSair.setForeground(Color.WHITE);
        btnSair.setFocusPainted(false);
        btnSair.addActionListener(this);
        fundo.add(btnSair);
        
        add(fundo);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnJogarNovamente) {
            Game gframe = (Game) SwingUtilities.getWindowAncestor(this);
            if (gframe != null) {
                Selecao selecao = new Selecao();
                gframe.setScreen(selecao);
                gframe.revalidate();
                gframe.repaint();
                selecao.requestFocusInWindow();
            }
        } else if (e.getSource() == btnSair) {
            System.exit(0);
        }
    }
}
