import java.io.IOException;
import java.net.Socket;
import java.security.Key;
import java.util.List;

public class Application {

    private Socket socket;
    private Key key;
    private AppGUI appGUI;

    /*public static void main(String[] args) {
        new Application(new Socket());
    }*/

    public Application() throws IOException {
        appGUI = new AppGUI();
    }

    public Application(Socket socket, Key key) throws IOException {
        this.socket = socket;
        this.key = key;
        appGUI = new AppGUI();
    }


}
