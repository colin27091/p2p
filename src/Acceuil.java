import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Acceuil extends JFrame{

    private JPanel bord;
    private JButton clientButton;
    private JButton serverButton;

    public Acceuil(){

        this.setTitle("P2P Conversation");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(200,200));
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);

        this.getContentPane().add(bord, BorderLayout.CENTER);

        clientButton.addActionListener(e -> {
            dispose();
            new Client();
        });
            serverButton.addActionListener(e -> {
                dispose();
                new Server();
            });
        }
}
