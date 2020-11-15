import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
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
    private InputStream inMessage;
    private OutputStream outMessage;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private Key key;

    public Client(){
        this.getGraphical();
        this.putListener();
    }

    public void getGraphical(){
        this.setTitle("P2P Conversation");
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
            socket = new Socket(strServer, intPort); //fait une demande de connexion au serveur choisi
            inMessage = socket.getInputStream();
            outMessage = socket.getOutputStream();
            Key[] keys = Util.generateKeys(); //genere les clés asymetriques
            this.privateKey = (PrivateKey) keys[0];
            this.publicKey = (PublicKey) keys[1];
            Util.sendObject(outMessage,publicKey); //envoie la clé publique
            key = (Key) Util.decryptObject((byte[]) Util.receiveObject(inMessage), privateKey); //recoit la clé symetrique du serveur et la décrypte
            dispose();
            new Application(socket, key); //lance l'application
        } catch (IOException | ClassNotFoundException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            error.setText("Connexion non établie");
            error.setForeground(Color.red);
        }
    }

}
