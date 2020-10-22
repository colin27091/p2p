import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Collections;
import java.util.List;

public class AppGUI extends JFrame {

    private List<Icon> icons;
    static int width = 600;
    static int height = 600;

    public AppGUI(List<Icon> icons){
        this.icons = icons;
        this.setTitle("Application Streaming Video");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(width, height));
        this.setLayout(null);
        this.setUndecorated(true);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    public List<Icon> getIcons() {
        return icons;
    }

    public void setIcons(List<Icon> icons) {
        this.icons = icons;
    }

    public void drawIcons(){
        System.out.println(icons.size());
        for (int i = 0; i < icons.size(); i++) {
            Icon icon = icons.get(i);
            icon.setBounds(100*(i%6), 100*(i/6), 100, 100);
            this.getContentPane().add(icon);
        }
        this.repaint();

    }

}
