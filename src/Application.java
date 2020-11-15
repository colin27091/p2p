import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;

import java.security.Key;


public class Application {

    private Socket socket;
    private Key key;
    private AppGUI appGUI;
    private File folderReceive;
    private InputStream inputStream;
    private OutputStream outputStream;

    public Application(Socket socket, Key key) throws IOException {
        this.socket = socket;
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
        this.key = key;

        appGUI = new AppGUI();
        this.getListener();
        receive.start();
        folderReceive = getFolderReceive();
        sendMessage("Connexion établie");

    }


    public void getListener(){
        appGUI.transfer.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e){
                transferFile();
            }
        });

        appGUI.send.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String message = appGUI.input.getText();
                if(!appGUI.input.getText().isEmpty()) {
                    sendMessage(message);
                    appGUI.input.setText("");
                }
            }
        });

        appGUI.input.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == 10){ //entrée -> send
                    String message = appGUI.input.getText();
                    if(!appGUI.input.getText().isEmpty()) {
                        sendMessage(message);
                        appGUI.input.setText("");
                    }
                }
            }
        });
    }

    Thread receive = new Thread(() -> {
        while(true){
            try{
                byte[] bytes = (byte[]) Util.receiveObject(inputStream);
                try{
                    String message = (String) Util.decryptObject(bytes, key);
                    appGUI.addMessageFromOther(message);
                } catch (ClassCastException e){
                    System.out.println(bytes.length);
                    File file = (File) Util.decryptObject(bytes, key);
                    System.out.println(file.length());
                    appGUI.addMessageFromOther("Transfert d'un fichier en cours ...");
                    writeFileInFolder(file);
                }
            } catch(Exception e){
}
        }
    });

    public void transferFile(){
        JFileChooser fileChooser = new JFileChooser("send");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        fileChooser.setDialogTitle("Transferer un fichier");
        fileChooser.showOpenDialog(null);
        File fileSelected = fileChooser.getSelectedFile();
        if(fileSelected != null){
            try {
                byte[] bytes = Util.cryptObject(fileSelected, key);
                System.out.println(bytes);
                Util.sendObject(outputStream, bytes);
                appGUI.addMessageFromMe("Transfert d'un fichier en cours .. ");
                appGUI.input.setText("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message){

        try {
            byte[] bytes = Util.cryptObject(message, key);
            Util.sendObject(outputStream, bytes);
            appGUI.addMessageFromMe(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public File getFolderReceive(){
        JFileChooser fileChooser = new JFileChooser("receive");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        fileChooser.setDialogTitle("Choisir un dossier où stocker les fichiers reçus");
        fileChooser.showOpenDialog(null);
        File fileSelected = fileChooser.getSelectedFile();
        return fileSelected;
    }

    public void writeFileInFolder(File file) throws IOException {
        FileInputStream inputStreamFile = new FileInputStream(file);
        FileOutputStream outputStreamFile = new FileOutputStream(folderReceive.getAbsolutePath()+"\\"+file.getName());
        Thread write = new Thread(() -> {
            try {
                int c;
                while((c= inputStreamFile.read()) != -1){
                    outputStreamFile.write(c);
                }
                inputStreamFile.close();
                outputStreamFile.close();
                sendMessage("Fichier recu");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        write.start();
    }


}
