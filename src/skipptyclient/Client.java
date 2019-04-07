/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skipptyclient;

import game.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import static skipptyclient.Client.sInput;
import game.SkipptyGame;
import java.awt.FlowLayout;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author INSECT
 */
// serverdan gelecek mesajları dinleyen thread
class Listen extends Thread {

    public void run() {
        //soket bağlı olduğu sürece dön
        while (Client.socket.isConnected()) {
            try {
                //mesaj gelmesini bloking olarak dinyelen komut
                Message received = (Message) (sInput.readObject());
                //mesaj gelirse bu satıra geçer
                //mesaj tipine göre yapılacak işlemi ayır.
                switch (received.type) {
                    case YourTurn:
                        SkipptyGame.ThisGame.message.setText("Your Turn !!");
                        SkipptyGame.ThisGame.isYourTurn = (boolean) received.content;
                        break;
                    case Name:
                        String rivalName = received.content.toString();
                        SkipptyGame.ThisGame.lblRivalName.setText(rivalName);
                        break;
                    case RivalConnected:
                        String gameBoard = received.content.toString();
                        SkipptyGame.ThisGame.initializeGui(gameBoard);
                        SkipptyGame.ThisGame.setLayout(new FlowLayout(FlowLayout.CENTER));
                        SkipptyGame.ThisGame.pack();
                        SkipptyGame.ThisGame.setMinimumSize(SkipptyGame.ThisGame.getSize());
                        break;
                    case Disconnect:
                        break;
                    case Move:
                        List<String> array = (List<String>) received.content;
                        SkipptyGame.ThisGame.updateBoard(array);
                        SkipptyGame.ThisGame.isYourTurn = true;
                        SkipptyGame.ThisGame.message.setText("Your Turn!!");
                        break;
                    case Selected:
                        break;
                    case Bitis:
                        Message msg = new Message(Message.Message_Type.BitisRakip);
                        int score = Integer.MAX_VALUE;
                        for (Map.Entry<SkipptyGame.SkipptyNodeType, Integer> entry : SkipptyGame.ThisGame.skipptyCount.entrySet()) {
                            SkipptyGame.SkipptyNodeType key = entry.getKey();
                            Integer value = entry.getValue();
                            if (value < score) {
                                score = value;
                            }
                        }
                        msg.content = score;
                        Client.Send(msg);
                        break;
                    case StartGameBoard:
                        SkipptyGame.ThisGame.skipptyNodes = (SkipptyGame.SkippityNode[][]) received.content;
                        break;
                    case Sonuc:
                        String sonuc = received.content.toString();
                        if (sonuc.equals("Win")) {
                            JOptionPane.showMessageDialog(SkipptyGame.ThisGame, "Kazandınız");
                        } else if (sonuc.equals("Tie")) {
                            JOptionPane.showMessageDialog(SkipptyGame.ThisGame, "Berabere Kaldınız");
                        } else {
                            JOptionPane.showMessageDialog(SkipptyGame.ThisGame, "Kaybettiniz");
                        }
                        Thread.sleep(10000);
                        SkipptyGame.ThisGame.dispose();
                        break;

                }
            } catch (IOException | ClassNotFoundException ex) {

                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                //Client.Stop();
                break;
            } catch (InterruptedException ex) {
                Logger.getLogger(Listen.class.getName()).log(Level.SEVERE, null, ex);
            }
            //Client.Stop();
            //Client.Stop();

        }

    }
}

public class Client {

    //her clientın bir soketi olmalı
    public static Socket socket;

    //verileri almak için gerekli nesne
    public static ObjectInputStream sInput;
    //verileri göndermek için gerekli nesne
    public static ObjectOutputStream sOutput;
    //serverı dinleme thredi 
    public static Listen listenMe;

    public static void Start(String ip, int port) {
        try {
            // Client Soket nesnesi
            Client.socket = new Socket(ip, port);
            Client.Display("Servera bağlandı");
            // input stream
            Client.sInput = new ObjectInputStream(Client.socket.getInputStream());
            // output stream
            Client.sOutput = new ObjectOutputStream(Client.socket.getOutputStream());
            Client.listenMe = new Listen();
            Client.listenMe.start();

            //ilk mesaj olarak isim gönderiyorum
            Message msg = new Message(Message.Message_Type.Name);
            msg.content = SkipptyGame.ThisGame.txtName.getText();
            Client.Send(msg);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //client durdurma fonksiyonu
    public static void Stop() {
        try {
            if (Client.socket != null) {
                Client.listenMe.stop();
                Client.socket.close();
                Client.sOutput.flush();
                Client.sOutput.close();
                Client.sInput.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void Display(String msg) {
        System.out.println(msg);
    }

    //mesaj gönderme fonksiyonu
    public static void Send(Message msg) {
        try {
            Client.sOutput.writeObject(msg);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
