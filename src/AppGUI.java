
import javax.swing.*;
import java.awt.*;


import java.io.IOException;


public class AppGUI extends JFrame {

    static int width = 600;
    static int height = 500;
    private JPanel pane;

    public JTextField input;
    public JLabel send;
    public JLabel transfer;

    private JTextArea messages;


    public AppGUI() throws IOException {

        this.setTitle("P2P Conversation");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(width, height));
        this.setLayout(null);
        //this.setUndecorated(true);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);

        this.setContentPane(pane);

        this.transfer.setIcon(new ImageIcon("icons/add.png"));
        this.send.setIcon(new ImageIcon("icons/send.png"));
    }

    public void addMessageFromMe(String message){
        this.messages.setText(this.messages.getText() + "You : " + message + "\n");
    }

    public void addMessageFromOther(String message){
        this.messages.setText(this.messages.getText() + "Other : " + message + "\n");
    }

}