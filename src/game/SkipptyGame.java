/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.*;
import skipptyclient.Client;

/**
 *
 * @author sefercanaydas
 */
public class SkipptyGame extends javax.swing.JFrame {

    public static SkipptyGame ThisGame;
    SkippityNode clickedButton = null;
    private final SkipptyNodeType nodeTypes[] = {SkipptyNodeType.RED, SkipptyNodeType.ORANGE, SkipptyNodeType.YELLOW, SkipptyNodeType.GREEN, SkipptyNodeType.BLUE};

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

    public static enum SkipptyNodeType {
        NULL, RED, ORANGE, YELLOW, GREEN, BLUE
    }
    public final JPanel gui = new JPanel(new BorderLayout(3, 3));
    public SkippityNode[][] skipptyNodes = new SkippityNode[10][10];
    public JPanel chessBoard;
    public final JLabel message = new JLabel("Chess Champ is ready to play!");

    private class Listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (clickedButton == null) {
                SkippityNode node = (SkippityNode) e.getSource();
                clickedButton = node;
                if (clickedButton.type == SkipptyNodeType.NULL) {
                    clickedButton = null;
                    JOptionPane.showMessageDialog(rootPane, "Yanlış Hamle Oynadınız!!");
                }
            } else {
                SkippityNode secondClick = (SkippityNode) e.getSource();
                if (secondClick.type == SkipptyNodeType.NULL) {
                    if (secondClick.row == clickedButton.row + 2 && secondClick.col == clickedButton.col) {
                        if (skipptyNodes[clickedButton.row + 1][clickedButton.col].type != SkipptyNodeType.NULL) {
                            skipptyNodes[clickedButton.row + 1][clickedButton.col].type = SkipptyNodeType.NULL;
                            secondClick.type = clickedButton.type;
                            clickedButton.type = SkipptyNodeType.NULL;
                        }
                    } else if (secondClick.row == clickedButton.row - 2 && secondClick.col == clickedButton.col) {
                        if (skipptyNodes[clickedButton.row - 1][clickedButton.col].type != SkipptyNodeType.NULL) {
                            skipptyNodes[clickedButton.row - 1][clickedButton.col].type = SkipptyNodeType.NULL;
                            secondClick.type = clickedButton.type;
                            clickedButton.type = SkipptyNodeType.NULL;
                        }
                    } else if (secondClick.col == clickedButton.col + 2 && secondClick.row == clickedButton.row) {
                        if (skipptyNodes[clickedButton.row][clickedButton.col + 1].type != SkipptyNodeType.NULL) {
                            skipptyNodes[clickedButton.row][clickedButton.col + 1].type = SkipptyNodeType.NULL;
                            secondClick.type = clickedButton.type;
                            clickedButton.type = SkipptyNodeType.NULL;
                        }
                    } else if (secondClick.col == clickedButton.col - 2 && secondClick.row == clickedButton.row) {
                        if (skipptyNodes[clickedButton.row][clickedButton.col - 1].type != SkipptyNodeType.NULL) {
                            skipptyNodes[clickedButton.row][clickedButton.col - 1].type = SkipptyNodeType.NULL;
                            secondClick.type = clickedButton.type;
                            clickedButton.type = SkipptyNodeType.NULL;
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

    public class SkippityNode extends JButton {

        SkipptyNodeType type = SkipptyNodeType.NULL;
        int row, col;
        boolean isClicked = false;
    }

    public final void initializeGui(String gameBoard) {
        // set up the main GUI
        int[][] gameBoardInt = new int[10][10];
        String[] rows = gameBoard.split(";");
        for (int i = 0; i < rows.length; i++) {
            String[] cols = rows[i].split(",");
            for (int j = 0; j < cols.length; j++) {
                String col = cols[j];
                gameBoardInt[i][j] = Integer.parseInt(col);
            }
        }
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
        Insets buttonMargin = new Insets(0, 0, 0, 0);
        for (int row = 0; row < skipptyNodes.length; row++) {
            for (int col = 0; col < skipptyNodes[row].length; col++) {
                SkippityNode b = new SkippityNode();
                b.type = nodeTypes[gameBoardInt[row][col]];
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
                chessBoard.add(b);
            }
        }
        skipptyNodes[4][4].type = SkipptyNodeType.NULL;
        skipptyNodes[4][5].type = SkipptyNodeType.NULL;
        skipptyNodes[5][4].type = SkipptyNodeType.NULL;
        skipptyNodes[5][5].type = SkipptyNodeType.NULL;
        updateBoard();
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
        ThisGame = this;
        this.add(gui);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        txtName = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Connect");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        txtName.setText("Name");

        jButton2.setText("Oyna");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(66, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(275, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(284, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        Client.Start("127.0.0.1", 2000);
    }//GEN-LAST:event_jButton1ActionPerformed

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
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    public javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables
}
