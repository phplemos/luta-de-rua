package visao;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import controle.Game;

public class Vencedor extends JPanel implements ActionListener {
    
    private JButton btnJogarNovamente;
    private JButton btnSair;
    private JLabel lblVencedor;
    private JLabel fundo;
    
    public Vencedor(String winnerMsg) {
        setLayout(null);
        
        fundo = new JLabel(new ImageIcon(getClass().getResource("/assets/victory.png")));
        fundo.setBounds(0, 0, 800, 600);
        
        lblVencedor = new JLabel(winnerMsg);
        lblVencedor.setBounds(0, 100, 800, 80);
        lblVencedor.setFont(new Font("Arial", Font.BOLD, 60));
        lblVencedor.setForeground(Color.YELLOW);
        lblVencedor.setHorizontalAlignment(SwingConstants.CENTER);
        fundo.add(lblVencedor);
        
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
                gframe.setContentPane(selecao);
                gframe.revalidate();
                gframe.repaint();
                selecao.requestFocusInWindow();
            }
        } else if (e.getSource() == btnSair) {
            System.exit(0);
        }
    }
}
