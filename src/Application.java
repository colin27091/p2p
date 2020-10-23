import java.net.Socket;
import java.util.List;

public class Application {

    private Socket socket;
    private AppGUI appGUI;

    /*public static void main(String[] args) {
        new Application(new Socket());
    }*/

    public Application(){

        appGUI = new AppGUI();
    }

}
