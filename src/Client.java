import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.security.*;

public class Client extends JFrame implements Entity{
    private JPanel pane;
    private JButton runButton;
    private JButton backButton;
    private JTextField port;
    private JTextField server;
    private JLabel error;

    private Socket socket;
    private InputStream in;
    private OutputStream out;

    private KeyPairGenerator keyGen;
    private KeyPair pair;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private PublicKey guestPublicKey;



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
        this.putListener();

    }

    public void putListener(){

        runButton.addActionListener((e) -> {
            String strServer = server.getText();
            int intPort = Integer.parseInt(port.getText());
            try {
                socket = new Socket(strServer, intPort);
                dispose();
                putIO();
                generateKeys();
                receivePublicKey();
                sendPublicKey();
                new Application();
            } catch (IOException | NoSuchAlgorithmException | ClassNotFoundException ex) {
                error.setText("Access denied");
                error.setForeground(Color.red);
            }

        });

        backButton.addActionListener(e -> {
            dispose();
            new Acceuil();
        });

    }

    public void putIO() throws IOException {
        in = socket.getInputStream();
        out = socket.getOutputStream();
    }

    public void generateKeys() throws NoSuchAlgorithmException {
        this.keyGen = KeyPairGenerator.getInstance("RSA");
        this.keyGen.initialize(1024);
        this.pair = this.keyGen.generateKeyPair();
        this.privateKey = this.pair.getPrivate();
        this.publicKey = this.pair.getPublic();
    }

    public void receivePublicKey() throws IOException, ClassNotFoundException {
        ObjectInputStream inputObject = new ObjectInputStream(in);
        this.guestPublicKey = (PublicKey) inputObject.readObject();
        System.out.println(this.guestPublicKey);
    }

    public void sendPublicKey() throws IOException, ClassNotFoundException {
        ObjectOutputStream outObject = new ObjectOutputStream(out);
        outObject.writeObject(this.publicKey);
        outObject.flush();
    }


}
