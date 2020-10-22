import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;

public class Client extends JFrame{
    private JPanel pane;
    private JButton runButton;
    private JButton backButton;
    private JTextField port;
    private JTextField server;
    private JLabel error;
    private Socket socket;


    public Client(){
        this.getGraphical();
    }

    public void getGraphical(){

        this.setTitle("Application Streaming Video");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(300,300));
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);

        this.getContentPane().add(pane, BorderLayout.CENTER);
        this.getListener();

    }

    public void getListener(){

        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String strServer = server.getText();
                int intPort = Integer.parseInt(port.getText());
                try {
                    socket = new Socket(strServer, intPort);
                    dispose();
                    new Application(socket, receiveIcons());
                } catch (IOException | ClassNotFoundException ex) {
                    error.setText("Access denied");
                    error.setForeground(Color.red);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Acceuil();
            }
        });

    }

    public List<Icon> receiveIcons() throws IOException, ClassNotFoundException {
        ObjectInputStream inObject = new ObjectInputStream(socket.getInputStream());
        List<Icon> icons = (List<Icon>) inObject.readObject();
        return icons;
    }



}
