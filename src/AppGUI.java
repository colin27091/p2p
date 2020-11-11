import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Collections;
import java.util.List;

public class AppGUI extends JFrame {

    static int width = 600;
    static int height = 600;
    private JPanel pane;

    public AppGUI(){
        this.setTitle("P2P Conversation");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(width, height));
        this.setLayout(null);
        this.setUndecorated(true);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);

        pane = new PaneGUI();
        this.setContentPane(pane);


    }


}
