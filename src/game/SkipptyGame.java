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
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.*;
import skipptyclient.Client;

/**
 *
 * @author sefercanaydas
 */
public class SkipptyGame extends javax.swing.JFrame {

    public Map<SkipptyNodeType, Integer> skipptyCount = new EnumMap<SkipptyNodeType, Integer>(SkipptyNodeType.class);
    public static SkipptyGame ThisGame;
    SkippityNode clickedButton = null;

    private final SkipptyNodeType nodeTypes[] = {SkipptyNodeType.RED, SkipptyNodeType.ORANGE, SkipptyNodeType.YELLOW, SkipptyNodeType.GREEN, SkipptyNodeType.BLUE};

    public void updateBoard() {
        //Oynanan hamlelelerden sonra buttonların türüne göre butonun arka planını ayarlama işlemleri
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

    public void updateBoard(List<String> strings) {
        //Karşı oyuncudan gelen Oyun tahtasını gerekli işlemlerle parsa ederek bizim oyun tahtamızı update etme işlemi
        strings.get(0);
        for (int row = 0; row < skipptyNodes.length; row++) {
            for (int col = 0; col < skipptyNodes[row].length; col++) {
                int indeks = (row * 10) + col;
                String type = strings.get(indeks);
                switch (type) {
                    case "RED":
                        skipptyNodes[row][col].type = SkipptyNodeType.RED;
                        break;
                    case "ORANGE":
                        skipptyNodes[row][col].type = SkipptyNodeType.ORANGE;
                        break;
                    case "YELLOW":
                        skipptyNodes[row][col].type = SkipptyNodeType.YELLOW;
                        break;
                    case "GREEN":
                        skipptyNodes[row][col].type = SkipptyNodeType.GREEN;
                        break;
                    case "BLUE":
                        skipptyNodes[row][col].type = SkipptyNodeType.BLUE;
                        break;
                    default:
                        skipptyNodes[row][col].type = SkipptyNodeType.NULL;
                }
            }
        }
        updateBoard();
    }

    public static enum SkipptyNodeType {
        NULL, RED, ORANGE, YELLOW, GREEN, BLUE
    }
    public final JPanel gui = new JPanel(new BorderLayout(3, 3));
    //Oyun tahtasını bir matriste tutarak oynan hamleleri kontrol etmek ve karşı cliente göndermek için
    public SkippityNode[][] skipptyNodes = new SkippityNode[10][10];
    public JPanel skipptyBoard;
    public final JLabel message = new JLabel("");
    //Sıranın bizde olup olmadığını göstermek için
    public boolean isYourTurn = false;
    //Oynanan hamlenin ilk olup olmadığını tutmak için
    public boolean isFirstMove = true;

    private class Listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //Kodla eklediğim butonlara eklediğim ActionListener Oyun butonlarına basıldığında buraya düşüyor
            
            //Eğer Bizim sıramızsa işlem yapmak için
            if (isYourTurn) {
                //Eğer İlk Butona basılma işlemi yapılmadıysa basılan butonu Oyun nesnesinin özelliği olarak saklıyoruz
                if (clickedButton == null) {
                    SkippityNode node = (SkippityNode) e.getSource();
                    clickedButton = node;
                    if (clickedButton.type == SkipptyNodeType.NULL) {
                        clickedButton = null;
                        JOptionPane.showMessageDialog(rootPane, "Yanlış Hamle Oynadınız!!");
                    }
                } else {
                    //Eğer daha önce bi butona basıldıysa buraya gelir ve o aldığı ilk tıklama ile ikinci tıklama arasında karşılaştırma yaparak
                    //Eğer oynanan hamle oyuna uygun hamle ise gerekli işlemleri yaptırıp değilse hata verdirir
                    SkippityNode secondClick = (SkippityNode) e.getSource();
                    if (secondClick.type == SkipptyNodeType.NULL) {
                        if (secondClick.row == clickedButton.row + 2 && secondClick.col == clickedButton.col) {
                            if (skipptyNodes[clickedButton.row + 1][clickedButton.col].type != SkipptyNodeType.NULL) {
                                skipptyCount.replace(skipptyNodes[clickedButton.row + 1][clickedButton.col].type, skipptyCount.get(skipptyNodes[clickedButton.row + 1][clickedButton.col].type) + 1);
                                skipptyNodes[clickedButton.row + 1][clickedButton.col].type = SkipptyNodeType.NULL;
                                secondClick.type = clickedButton.type;
                                clickedButton.type = SkipptyNodeType.NULL;
                                clickedButton = secondClick;
                                isFirstMove = false;
                            }
                        } else if (secondClick.row == clickedButton.row - 2 && secondClick.col == clickedButton.col) {
                            if (skipptyNodes[clickedButton.row - 1][clickedButton.col].type != SkipptyNodeType.NULL) {
                                skipptyCount.replace(skipptyNodes[clickedButton.row - 1][clickedButton.col].type, skipptyCount.get(skipptyNodes[clickedButton.row - 1][clickedButton.col].type) + 1);
                                skipptyNodes[clickedButton.row - 1][clickedButton.col].type = SkipptyNodeType.NULL;
                                secondClick.type = clickedButton.type;
                                clickedButton.type = SkipptyNodeType.NULL;
                                clickedButton = secondClick;
                                isFirstMove = false;
                            }
                        } else if (secondClick.col == clickedButton.col + 2 && secondClick.row == clickedButton.row) {
                            if (skipptyNodes[clickedButton.row][clickedButton.col + 1].type != SkipptyNodeType.NULL) {
                                skipptyCount.replace(skipptyNodes[clickedButton.row][clickedButton.col + 1].type, skipptyCount.get(skipptyNodes[clickedButton.row][clickedButton.col + 1].type) + 1);
                                skipptyNodes[clickedButton.row][clickedButton.col + 1].type = SkipptyNodeType.NULL;
                                secondClick.type = clickedButton.type;
                                clickedButton.type = SkipptyNodeType.NULL;
                                clickedButton = secondClick;
                                isFirstMove = false;
                            }
                        } else if (secondClick.col == clickedButton.col - 2 && secondClick.row == clickedButton.row) {
                            if (skipptyNodes[clickedButton.row][clickedButton.col - 1].type != SkipptyNodeType.NULL) {
                                skipptyCount.replace(skipptyNodes[clickedButton.row][clickedButton.col - 1].type, skipptyCount.get(skipptyNodes[clickedButton.row][clickedButton.col - 1].type) + 1);
                                skipptyNodes[clickedButton.row][clickedButton.col - 1].type = SkipptyNodeType.NULL;
                                secondClick.type = clickedButton.type;
                                clickedButton.type = SkipptyNodeType.NULL;
                                clickedButton = secondClick;
                                isFirstMove = false;
                            }
                        } else {
                            if (isFirstMove) {
                                clickedButton = null;
                            }
                            JOptionPane.showMessageDialog(rootPane, "Yanlış Hamle Oynadınız!!");
                        }
                    } else {
                        if (isFirstMove) {
                            clickedButton = null;
                        }
                        JOptionPane.showMessageDialog(rootPane, "Yanlış Hamle Oynadınız!!");
                    }
                }
            } else {
                clickedButton = null;
                JOptionPane.showMessageDialog(rootPane, "Lütfen Sıranızı Bekleyin!!");
            }
            //Oynanan hamlelerden sonra oyun tahtasını ve skor tahtasıı günceller
            updateBoard();
            updateScoreBoard();
        }
    }
    //Buttonları oyun taşı olarak kullanmak için JButton üstüne özellikler ekledim
    public class SkippityNode extends JButton {

        SkipptyNodeType type = SkipptyNodeType.NULL;
        int row, col;
        boolean isClicked = false;
    }

    public final void initializeGui(String gameBoard) {
        // Oyun tahtasını ayarlama işlemi
        //Oyun tahtasını serverdan string olarak alıp formatlı şekilde parse ettikten sonra oyun tahtamızı oluşturuyoruz
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
        tools.add(message);
        skipptyBoard = new JPanel(new GridLayout(0, 10));
        skipptyBoard.setBorder(new LineBorder(Color.BLACK));
        gui.add(skipptyBoard);
        Insets buttonMargin = new Insets(0, 0, 0, 0);
        for (int row = 0; row < skipptyNodes.length; row++) {
            for (int col = 0; col < skipptyNodes[row].length; col++) {
                SkippityNode b = new SkippityNode();
                //Serverdan gelen typelara göre buttonun tipini set etme
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
                skipptyBoard.add(b);
            }
        }
        skipptyNodes[4][4].type = SkipptyNodeType.NULL;
        skipptyNodes[4][5].type = SkipptyNodeType.NULL;
        skipptyNodes[5][4].type = SkipptyNodeType.NULL;
        skipptyNodes[5][5].type = SkipptyNodeType.NULL;
        updateBoard();
    }

    public void sendGameBoard() {
        //Gönderirken Nodelar Serileştirilemediği için string listesi olarak tahtayı karşı tarafa gönderiyoruz
        List<String> nodes = new ArrayList();
        for (int i = 0; i < skipptyNodes.length; i++) {
            for (int j = 0; j < skipptyNodes[i].length; j++) {
                String nodeType = "";
                switch (skipptyNodes[i][j].type) {
                    case NULL:
                        nodeType = "NULL";
                        break;
                    case BLUE:
                        nodeType = "BLUE";
                        break;
                    case GREEN:
                        nodeType = "GREEN";
                        break;
                    case ORANGE:
                        nodeType = "ORANGE";
                        break;
                    case RED:
                        nodeType = "RED";
                        break;
                    case YELLOW:
                        nodeType = "YELLOW";
                        break;
                    default:
                        throw new AssertionError();
                }
                nodes.add(nodeType);
            }
        }
        Message msg = new Message(Message.Message_Type.Move);
        msg.content = nodes;
        Client.Send(msg);
    }

    /**
     * Creates new form SkipptyGame
     */
    public SkipptyGame() {
        initComponents();
        ThisGame = this;
        //Hazırlanan oyun tahtasını Frame ekleme
        this.add(gui);
        //Mapin ilk değerlerini 0 olarak atama
        skipptyCount.put(SkipptyNodeType.RED, 0);
        skipptyCount.put(SkipptyNodeType.BLUE, 0);
        skipptyCount.put(SkipptyNodeType.GREEN, 0);
        skipptyCount.put(SkipptyNodeType.ORANGE, 0);
        skipptyCount.put(SkipptyNodeType.YELLOW, 0);
    }

    public boolean isGameEnd() {
        //Oyun bitmişmi kontrolunu yapmak için tek tek tüm taşların yapılabilecek hamlesi varmı kontrolü
        for (int row = 0; row < skipptyNodes.length; row++) {
            for (int col = 0; col < skipptyNodes[row].length; col++) {
                SkippityNode node = skipptyNodes[row][col];
                if (node.type == SkipptyNodeType.NULL) {
                    continue;
                }
                //Listeye bakarken ilerisine baktığımız için son taşlarda index hatası alabileceğimiz için
                //Tüm blokları try catchlerle çevirdim
                try {
                    if (skipptyNodes[node.row + 1][node.col].type != SkipptyNodeType.NULL) {
                        if (skipptyNodes[node.row + 2][node.col].type == SkipptyNodeType.NULL) {
                            return false;
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                }
                try {
                    if (skipptyNodes[node.row - 1][node.col].type != SkipptyNodeType.NULL) {
                        if (skipptyNodes[node.row - 2][node.col].type == SkipptyNodeType.NULL) {
                            return false;
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                }
                try {
                    if (skipptyNodes[node.row][node.col + 1].type != SkipptyNodeType.NULL) {
                        if (skipptyNodes[node.row][node.col + 2].type == SkipptyNodeType.NULL) {
                            return false;
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                }
                try {
                    if (skipptyNodes[node.row][node.col - 1].type != SkipptyNodeType.NULL) {
                        if (skipptyNodes[node.row][node.col - 2].type == SkipptyNodeType.NULL) {
                            return false;
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }
        return true;
    }

    public void updateScoreBoard() {
        //Scor tablosunu Mapimize göre güncelleme işlemi
        for (Map.Entry<SkipptyNodeType, Integer> entry : skipptyCount.entrySet()) {
            SkipptyNodeType key = entry.getKey();
            Integer value = entry.getValue();
            switch (key) {
                case BLUE:
                    blueCount.setText(String.valueOf(value));
                    break;
                case GREEN:
                    greenCount.setText(String.valueOf(value));
                    break;
                case ORANGE:
                    orangeCount.setText(String.valueOf(value));
                    break;
                case RED:
                    redCount.setText(String.valueOf(value));
                    break;
                case YELLOW:
                    yellowCount.setText(String.valueOf(value));
                    break;
                default:
                    throw new AssertionError();
            }
        }
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
        btn_connect = new javax.swing.JButton();
        txtName = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblRivalName = new javax.swing.JLabel();
        redCount = new javax.swing.JTextField();
        greenCount = new javax.swing.JTextField();
        orangeCount = new javax.swing.JTextField();
        yellowCount = new javax.swing.JTextField();
        blueCount = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        btn_connect.setText("Connect");
        btn_connect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_connectActionPerformed(evt);
            }
        });

        txtName.setText("Name");

        jButton2.setText("Oyna");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setText("Red:");

        jLabel2.setText("Green:");

        jLabel3.setText("Orange:");

        jLabel4.setText("Yellow :");

        jLabel5.setText("Blue :");

        jLabel6.setText("Rival Name :");

        redCount.setText("0");
        redCount.setEnabled(false);

        greenCount.setText("0");
        greenCount.setEnabled(false);

        orangeCount.setText("0");
        orangeCount.setEnabled(false);

        yellowCount.setText("0");
        yellowCount.setEnabled(false);

        blueCount.setText("0");
        blueCount.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btn_connect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtName, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(33, 33, 33)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(blueCount)
                            .addComponent(yellowCount)
                            .addComponent(orangeCount)
                            .addComponent(redCount)
                            .addComponent(greenCount)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(lblRivalName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(lblRivalName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_connect)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(redCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(greenCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(orangeCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(yellowCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(blueCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(109, 109, 109))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(319, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_connectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_connectActionPerformed
        Client.Start("127.0.0.1", 2000);
        btn_connect.setEnabled(false);
    }//GEN-LAST:event_btn_connectActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        //Eğer sıra bizdeyse Oyna Butonu işlem yapar
        if (isYourTurn) {
            //Oyun yapılan hamlelerden sonra bitmişmi kontrolu yapar eğer bittiyse Servera Bitti Mesajı gönderilir
            if (isGameEnd()) {
                int score = Integer.MAX_VALUE;
                //Count mapini dönerek en küçük sayıda hangi taş alınmışsa o kadar seri yapmış oluyoruz
                for (Map.Entry<SkipptyNodeType, Integer> entry : skipptyCount.entrySet()) {
                    SkipptyNodeType key = entry.getKey();
                    Integer value = entry.getValue();
                    if (value < score) {
                        score = value;
                    }
                }
                Message msg = new Message(Message.Message_Type.Bitis);
                msg.content = score;
                Client.Send(msg);
            } else {
                //Oyun bitmediyse oyun tahtasını karşı cliente gönder
                sendGameBoard();
                this.isYourTurn = false;
                this.isFirstMove = true;
                this.clickedButton = null;
                this.message.setText("Rival Turn!!");
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Lütfen Sıranızı Bekleyiniz");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Client.Stop();
    }//GEN-LAST:event_formWindowClosing

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
    private javax.swing.JTextField blueCount;
    private javax.swing.JButton btn_connect;
    private javax.swing.JTextField greenCount;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    public javax.swing.JLabel lblRivalName;
    private javax.swing.JTextField orangeCount;
    private javax.swing.JTextField redCount;
    public javax.swing.JTextField txtName;
    private javax.swing.JTextField yellowCount;
    // End of variables declaration//GEN-END:variables
}
