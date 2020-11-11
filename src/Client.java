import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.security.*;

public class Client extends JFrame{
    private JPanel pane;
    private JButton runButton;
    private JButton backButton;
    private JTextField port;
    private JTextField server;
    private JLabel error;

    private Socket socket;
    private InputStream in;
    private OutputStream out;

    private PrivateKey privateKey;
    private PublicKey publicKey;

    public Client(){
        this.getGraphical();
        this.putListener();
    }

    public void getGraphical(){
        this.setTitle("Application Streaming Video");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(300,300));
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.getContentPane().add(pane, BorderLayout.CENTER);
    }

    public void putListener(){

        runButton.addActionListener((e) -> {
            try{
                String strServer = server.getText();
                int intPort = Integer.parseInt(port.getText());
                openConnection(strServer, intPort);
            } catch(NumberFormatException ex){
                error.setText("Invalid Input");
                error.setForeground(Color.red);
            }

        });

        backButton.addActionListener(e -> {
            dispose();
            new Acceuil();
        });

    }

    private void openConnection(String strServer, int intPort) {
        try {
            socket = new Socket(strServer, intPort);
            in = socket.getInputStream();
            out = socket.getOutputStream();
            Key[] keys = Util.generateKeys();
            this.privateKey = (PrivateKey) keys[0];
            this.publicKey = (PublicKey) keys[1];
            Util.sendPublicKey(out,publicKey);
            dispose();
        } catch (IOException e) {
            error.setText("Connexion non établie");
            error.setForeground(Color.red);
        }
    }

}
