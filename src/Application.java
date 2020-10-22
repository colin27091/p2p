import java.net.Socket;
import java.util.List;

public class Application {

    private List<Icon> icons;
    private Socket socket;
    private AppGUI appGUI;

    /*public static void main(String[] args) {
        new Application(new Socket());
    }*/

    public Application(Socket socket, List<Icon> icons){
        this.icons = icons;
        this.socket = socket;

        appGUI = new AppGUI(icons);
        appGUI.drawIcons();
    }

}
