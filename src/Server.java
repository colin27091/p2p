
import javax.swing.*;
import java.awt.*;
import java.io.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;


public class Server extends JFrame {

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
        this.openListener();

    }

    public void openListener(){

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
                        //receivePublicKey();
                    } catch (IOException | NoSuchAlgorithmException | ClassNotFoundException e) {
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
        outObject.close();
    }

    /*public void receivePublicKey() throws IOException, ClassNotFoundException {
        ObjectInputStream inputObject = new ObjectInputStream(in);
        this.guestPublicKey = (PublicKey) inputObject.readObject();
        System.out.println(this.guestPublicKey);
        inputObject.close();
    }*/




}
