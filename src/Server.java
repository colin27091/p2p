
import javax.crypto.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;


public class Server extends JFrame{

    private JPanel pane;
    private JButton runButton;
    private JButton backButton;
    private JTextField port;
    private JLabel error;
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private ServerSocket serverSocket;

    private KeyGenerator generator;
    private Key key;

    private Cipher cipher;



    public Server(){
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
            Message message = new Message("Waiting for client ...");
            dispose();
            Thread getSocket = new Thread(() -> {
                try {
                    socket = serverSocket.accept();
                    message.dispose();
                    in = socket.getInputStream();
                    out = socket.getOutputStream();
                    PublicKey guestPK = Util.receivePublicKey(in);
                    Key symetricKey = Util.createSymetricKey();

                } catch (IOException | ClassNotFoundException e) {
                    System.err.println(e.getMessage());
                }
            });
            getSocket.start();
        } catch (IOException e) {
            error.setText("Port failed");
            error.setForeground(Color.red);
        }
    }

}
