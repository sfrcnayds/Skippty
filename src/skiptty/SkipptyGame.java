/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skiptty;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

/**
 *
 * @author sefercanaydas
 */
public class SkipptyGame extends javax.swing.JFrame {

    SkippityNode clickedButton = null;

    public void updateBoard() {
        for (int row = 0; row < skipptyNodes.length; row++) {
            for (int col = 0; col < skipptyNodes[row].length; col++) {
                SkippityNode b = skipptyNodes[row][col];
                switch (b.type) {
                    case RED:
                        b.setBackground(Color.RED);
                        break;
                    case ORANGE:
                        b.setBackground(Color.ORANGE);
                        break;
                    case YELLOW:
                        b.setBackground(Color.YELLOW);
                        break;
                    case GREEN:
                        b.setBackground(Color.GREEN);
                        break;
                    case BLUE:
                        b.setBackground(Color.BLUE);
                        break;
                    default:
                        b.setBackground(Color.WHITE);
                }
            }
        }
    }

    public enum SkipptyNodeType {
        NULL, RED, ORANGE, YELLOW, GREEN, BLUE
    }
    private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private SkippityNode[][] skipptyNodes = new SkippityNode[10][10];
    private JPanel chessBoard;
    private final JLabel message = new JLabel("Chess Champ is ready to play!");

    private class Listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (clickedButton == null) {
                SkippityNode node = (SkippityNode) e.getSource();
                clickedButton = node;
            } else {
                SkippityNode secondClick = (SkippityNode) e.getSource();
                if (secondClick.type == SkipptyNodeType.NULL) {
                    if ((secondClick.row == clickedButton.row + 1 && secondClick.col == clickedButton.col)
                            || (secondClick.row == clickedButton.row - 1 && secondClick.col == clickedButton.col)
                            || (secondClick.col == clickedButton.col + 1 && secondClick.row == clickedButton.row)
                            || (secondClick.col == clickedButton.col - 1 && secondClick.row == clickedButton.row)) {
                        secondClick.type = clickedButton.type;
                        clickedButton.type = SkipptyNodeType.NULL;
                        clickedButton = null;
                    } else if (secondClick.row == clickedButton.row + 2 && secondClick.col == clickedButton.col) {
                        if (skipptyNodes[clickedButton.row + 1][clickedButton.col].type != SkipptyNodeType.NULL) {
                            skipptyNodes[clickedButton.row + 1][clickedButton.col].type = SkipptyNodeType.NULL;
                            secondClick.type = clickedButton.type;
                            clickedButton.type = SkipptyNodeType.NULL;
                            clickedButton = null;
                        }
                    } else if (secondClick.row == clickedButton.row - 2 && secondClick.col == clickedButton.col) {
                        if (skipptyNodes[clickedButton.row - 1][clickedButton.col].type != SkipptyNodeType.NULL) {
                            skipptyNodes[clickedButton.row - 1][clickedButton.col].type = SkipptyNodeType.NULL;
                            secondClick.type = clickedButton.type;
                            clickedButton.type = SkipptyNodeType.NULL;
                            clickedButton = null;
                        }
                    } else if (secondClick.col == clickedButton.col + 2 && secondClick.row == clickedButton.row) {
                        if (skipptyNodes[clickedButton.row][clickedButton.col + 1].type != SkipptyNodeType.NULL) {
                            skipptyNodes[clickedButton.row][clickedButton.col + 1].type = SkipptyNodeType.NULL;
                            secondClick.type = clickedButton.type;
                            clickedButton.type = SkipptyNodeType.NULL;
                            clickedButton = null;
                        }
                    } else if (secondClick.col == clickedButton.col - 2 && secondClick.row == clickedButton.row) {
                        if (skipptyNodes[clickedButton.row][clickedButton.col -1].type != SkipptyNodeType.NULL) {
                            skipptyNodes[clickedButton.row][clickedButton.col -1].type = SkipptyNodeType.NULL;
                            secondClick.type = clickedButton.type;
                            clickedButton.type = SkipptyNodeType.NULL;
                            clickedButton = null;
                        }
                    } else {
                        clickedButton = null;
                        JOptionPane.showMessageDialog(rootPane, "Yanlış Hamle Oynadınız!!");
                    }
                    updateBoard();
                } else {
                    clickedButton = null;
                    JOptionPane.showMessageDialog(rootPane, "Yanlış Hamle Oynadınız!!");
                }
            }
        }
    }

    private class SkippityNode extends JButton {

        SkipptyNodeType type = SkipptyNodeType.NULL;
        int row, col;
        boolean isClicked = false;
    }

    public final void initializeGui() throws IOException {
        // set up the main GUI
        gui.setBorder(new EmptyBorder(5, 5, 5, 5));
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        gui.add(tools, BorderLayout.PAGE_START);
        tools.add(new JButton("New")); // TODO - add functionality!
        tools.add(new JButton("Save")); // TODO - add functionality!
        tools.add(new JButton("Restore")); // TODO - add functionality!
        tools.addSeparator();
        tools.add(new JButton("Resign")); // TODO - add functionality!
        tools.addSeparator();
        tools.add(message);
        chessBoard = new JPanel(new GridLayout(0, 10));
        chessBoard.setBorder(new LineBorder(Color.BLACK));
        gui.add(chessBoard);

        // create the chess board squares
        Insets buttonMargin = new Insets(0, 0, 0, 0);
        SkipptyNodeType nodeTypes[] = {SkipptyNodeType.RED, SkipptyNodeType.ORANGE, SkipptyNodeType.YELLOW, SkipptyNodeType.GREEN, SkipptyNodeType.BLUE};
        int counter = 0;
        for (int row = 0; row < skipptyNodes.length; row++) {
            for (int col = 0; col < skipptyNodes[row].length; col++) {
                SkippityNode b = new SkippityNode();
                if (row != 4 || col != 5) {
                    b.type = nodeTypes[counter++ % 5];
                } else {
                    counter++;
                }
                b.col = col;
                b.row = row;
                b.addActionListener(new Listener());
                b.setMargin(buttonMargin);
                ImageIcon icon = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
                b.setIcon(icon);
                switch (b.type) {
                    case RED:
                        b.setBackground(Color.RED);
                        break;
                    case ORANGE:
                        b.setBackground(Color.ORANGE);
                        break;
                    case YELLOW:
                        b.setBackground(Color.YELLOW);
                        break;
                    case GREEN:
                        b.setBackground(Color.GREEN);
                        break;
                    case BLUE:
                        b.setBackground(Color.BLUE);
                        break;
                    default:
                        b.setBackground(Color.WHITE);
                }
                skipptyNodes[row][col] = b;
            }
        }
        // fill the black non-pawn piece row
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                chessBoard.add(skipptyNodes[row][col]);
            }
        }
    }

    public final JComponent getChessBoard() {
        return chessBoard;
    }

    public final JComponent getGui() {
        return gui;
    }

    /**
     * Creates new form SkipptyGame
     */
    public SkipptyGame() {
        initComponents();
        try {
            initializeGui();

        } catch (IOException ex) {
            Logger.getLogger(SkipptyGame.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        this.add(gui);
        this.setLocationByPlatform(true);
        this.setLayout(new FlowLayout());
        // ensures the frame is the minimum size it needs to be
        // in order display the components within it
        this.pack();
        // ensures the minimum size is enforced.
        this.setMinimumSize(this.getSize());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Connect");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addContainerGap(436, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addContainerGap(355, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SkipptyGame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SkipptyGame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SkipptyGame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SkipptyGame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SkipptyGame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    // End of variables declaration//GEN-END:variables
}
