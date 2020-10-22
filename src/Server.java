
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Server extends JFrame {

    private JPanel pane;
    private JButton runButton;
    private JButton backButton;
    private JTextField port;
    private JLabel error;
    private Socket socket;
    private ServerSocket serverSocket;


    public Server(){
        this.getGraphical();

    }

    public static void main(String[] args) {
        System.out.println("Hello");
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

        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    int intPort = Integer.parseInt(port.getText());
                    openServer(intPort);
                } catch(NumberFormatException ex){
                    error.setText("Invalid Input");
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

    public void openServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            dispose();
            //Message message = new Message("Waiting for client ...");
            Thread getSocket = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        socket = serverSocket.accept();
                        //message.dispose();
                        List<Icon> icons = folderToList();
                        sendIcons(icons);
                        //new Application(socket,icons);
                    } catch (IOException e) {
                    }
                }
            });
            getSocket.start();
        } catch (IOException e) {
            error.setText("Port failed");
            error.setForeground(Color.red);
        }
    }

    public void sendIcons(List<Icon> icons) throws IOException {
        OutputStream out = socket.getOutputStream();
        ObjectOutputStream outObject = new ObjectOutputStream(out);
        outObject.writeObject(icons);
        outObject.flush();
    }

    public List<Icon> folderToList(){
        File folder = new File("icons");
        List<Icon> first = Arrays.asList(folder.listFiles())
                .stream()
                .map(icon -> new Icon(icon))
                .collect(Collectors.toList());
        List<Icon> icons = first;
        icons.addAll(first);
        Collections.shuffle(icons);
        return icons;
    }


}
