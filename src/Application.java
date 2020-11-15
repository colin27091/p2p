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
        this.socket = socket; //recupere la socket établie
        inputStream = socket.getInputStream(); //stocke les input/output
        outputStream = socket.getOutputStream();
        this.key = key; //stocke la clé symétrique
        appGUI = new AppGUI();//lance la vue
        this.getListener(); //prépare les listener
        receive.start(); //lance le thread permettant de recevoir des données
        folderReceive = getFolderReceive(); // ouvre la boîte de dialogue pour choisir le dossier receive
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

    Thread receive = new Thread(() -> { //thread d'écoute
        while(true){
            try{
                byte[] bytes = (byte[]) Util.receiveObject(inputStream); //stocke les byte[] recçu
                try{
                    String message = (String) Util.decryptObject(bytes, key); //decypte les byte[]
                    appGUI.addMessageFromOther(message); //ecrit le message reçu
                } catch (ClassCastException e){ //si l'object n'est pas un string alors c'est un file
                    File file = (File) Util.decryptObject(bytes, key);//decrypte le file
                    appGUI.addMessageFromOther("Transfert d'un fichier en cours ...");
                    writeFileInFolder(file); //écrit le file en dur dans le dossier receive
                }
            } catch(Exception e){
}
        }
    });

    public void transferFile(){
        JFileChooser fileChooser = new JFileChooser("send");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        fileChooser.setDialogTitle("Transferer un fichier");
        fileChooser.showOpenDialog(null); //ouvre une boite de dialogue pour choisir le fichier à envoyer
        File fileSelected = fileChooser.getSelectedFile();
        if(fileSelected != null){
            try {
                byte[] bytes = Util.cryptObject(fileSelected, key);//crypte le fichier choisi
                Util.sendObject(outputStream, bytes); //envoi le fichier crypté sous forme de byte[]
                appGUI.addMessageFromMe("Transfert d'un fichier en cours .. ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message){

        try {
            byte[] bytes = Util.cryptObject(message, key);//crypte le message
            Util.sendObject(outputStream, bytes); //envoie le message
            appGUI.addMessageFromMe(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public File getFolderReceive(){ //choisir les dossier receive
        JFileChooser fileChooser = new JFileChooser("receive");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        fileChooser.setDialogTitle("Choisir un dossier où stocker les fichiers reçus");
        fileChooser.showOpenDialog(null);
        File fileSelected = fileChooser.getSelectedFile();
        return fileSelected;
    }

    public void writeFileInFolder(File file) throws IOException { // ecrit le file reçu dans le dossier receive
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
