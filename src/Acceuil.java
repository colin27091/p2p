import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Acceuil extends JFrame{

    private JPanel bord;
    private JButton clientButton;
    private JButton serverButton;

    public Acceuil(){

    this.setTitle("Application Streaming Video");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setPreferredSize(new Dimension(200,200));
    this.pack();
    this.setVisible(true);
    this.setLocationRelativeTo(null);

    this.getContentPane().add(bord, BorderLayout.CENTER);

    clientButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            new Client();
        }
    });
        serverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Server();
            }
        });
    }
}
