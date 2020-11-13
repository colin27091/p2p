
import javax.swing.*;
import java.awt.*;

import java.io.IOException;


public class AppGUI extends JFrame {

    static int width = 600;
    static int height = 500;
    private JPanel pane;

    private JLabel transfer;
    private JLabel send;
    private JTextField text;


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

}