
import javax.crypto.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;


public class Server extends JFrame implements Entity{

    private JPanel pane;
    private JButton runButton;
    private JButton backButton;
    private JTextField port;
    private JLabel error;
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private ServerSocket serverSocket;

    private KeyPairGenerator keyGen;
    private KeyPair pair;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private PublicKey guestPublicKey;

    private KeyGenerator generator;
    private Key key;

    private Cipher cipher;



    public Server(){
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

    @Override
    public void putListener() {
        runButton.addActionListener(e -> {
            try{
                int intPort = Integer.parseInt(port.getText());
                openServer(intPort);
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



    public void openServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            dispose();
            Message message = new Message("Waiting for client ...");
            Thread getSocket = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        socket = serverSocket.accept();
                        message.dispose();
                        putIO();
                        generateKeys();
                        sendPublicKey();
                        new Application();
                        receivePublicKey();
                        createSymetricKey();
                        cryptObject(key);
                    } catch (IOException | NoSuchAlgorithmException | ClassNotFoundException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
                        System.err.println(e.getMessage());
                    }
                }
            });
            getSocket.start();
        } catch (IOException e) {
            error.setText("Port failed");
            error.setForeground(Color.red);
        }
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

    public void sendPublicKey() throws IOException, ClassNotFoundException {
        ObjectOutputStream outObject = new ObjectOutputStream(out);
        outObject.writeObject(this.publicKey);
        outObject.flush();
    }

    public void receivePublicKey() throws IOException, ClassNotFoundException {
        ObjectInputStream inputObject = new ObjectInputStream(in);
        this.guestPublicKey = (PublicKey) inputObject.readObject();
        System.out.println(this.guestPublicKey);
    }

    public void createSymetricKey() throws NoSuchAlgorithmException, IOException, NoSuchPaddingException, InvalidKeyException {
        generator = KeyGenerator.getInstance("AES");
        key = generator.generateKey();

    }

    public void cryptObject(Object obj) throws IOException, BadPaddingException, IllegalBlockSizeException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream outKey = new ObjectOutputStream(bos);
        outKey.writeObject(obj);

        byte[] data = cipher.doFinal(bos.toByteArray());

        OutputStream outObject = new ObjectOutputStream(out);
        outObject.write(data);
        outObject.flush();

    }

    public void decryptObject() throws IOException {
        InputStream inObject = new ObjectInputStream(in);
        byte[] data = inObject.readAllBytes();

        //Key p = cipher.unwrap(data,Cip);


    }




}
