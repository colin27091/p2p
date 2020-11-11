import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Collections;
import java.util.List;

public class AppGUI extends JFrame {

    static int width = 900;
    static int height = 700;
    private JPanel pane;
    private JTextField textField1;

    public AppGUI(){

        this.setTitle("P2P Conversation");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(width, height));
        this.setLayout(null);
        //this.setUndecorated(true);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setContentPane(pane);


    }

}