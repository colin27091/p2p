import javax.swing.*;
import java.awt.*;

public class Message extends JFrame {


    private JPanel pane;
    private JLabel message;
    private String mes;

    public Message(String mes){
        this.mes = mes;
        this.getGraphical();
    }

    private void getGraphical() {
        this.setPreferredSize(new Dimension(250,50));
        this.setUndecorated(true);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.message.setText(this.mes);
        this.getContentPane().add(pane, BorderLayout.CENTER);
    }

}
