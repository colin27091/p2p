import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Application {

    private Socket socket;
    private Key key;
    private AppGUI appGUI;
    private File folderReceive;
    private InputStream inputStream;
    private OutputStream outputStream;


    public Application() throws IOException {
        //folderReceive = getFolderReceive();
        appGUI = new AppGUI();
        this.getListener();
        /*File file = new File("send/music.mp3");
        File file2 = new File("send/music2.mp3");
        this.writeFileInFolder(file);
        this.writeFileInFolder(file2);*/

    }

    public Application(Socket socket, Key key) throws IOException {
        this.socket = socket;
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
        this.key = key;
        appGUI = new AppGUI();
        this.getListener();
        //Scanner word = new Scanner(System.in);
        //sendMessage();
        receive.start();

    }

    public void transferFile(){
        JFileChooser fileChooser = new JFileChooser("send");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        fileChooser.setDialogTitle("Transferer un fichier");
        fileChooser.showOpenDialog(null);
        File fileSelected = fileChooser.getSelectedFile();
        if(fileSelected != null){
            System.out.println(fileSelected.getName());
        }
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
                sendMessage();
            }
        });
    }

    Thread receive = new Thread(() -> {
        while(true){
            try{
                byte[] bytes = (byte[]) Util.receiveObject(inputStream);
                String message = (String) Util.decryptObject(bytes, key);
                appGUI.addMessageFromOther(message);
            } catch (Exception e){

            }
        }
    });

    public void sendMessage(){
        if(!appGUI.input.getText().isEmpty()){
            String message = appGUI.input.getText();
            try {
                byte[] bytes = Util.cryptObject(message, key);
                Util.sendObject(outputStream, bytes);
                appGUI.addMessageFromMe(message);
                appGUI.input.setText("");
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        FileInputStream inputStream = new FileInputStream(file);
        FileOutputStream outputStream = new FileOutputStream(folderReceive.getAbsolutePath()+"\\"+file.getName());
        Thread write = new Thread(() -> {
            try {
                int c;
                while((c= inputStream.read()) != -1){
                    outputStream.write(c);
                }
                System.out.println("Fin "+ file.getName());
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        write.start();
    }


}
