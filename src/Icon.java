import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

public class Icon extends JLabel {

    File image;
    boolean disabled;

    public Icon(File image) {
        this.image = image;
        this.disabled = false;

        this.setIcon(new ImageIcon(image.getAbsolutePath()));
        this.setHorizontalAlignment(CENTER);
        this.setVerticalAlignment(CENTER);
        this.setPreferredSize(new Dimension(100,100));
        this.activeListener();

    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void activeListener(){
        this.addMouseListener(new MouseListener(){

            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(image.getAbsolutePath());
            }

            @Override
            public void mousePressed(MouseEvent e) { }
            @Override
            public void mouseReleased(MouseEvent e) { }
            @Override
            public void mouseEntered(MouseEvent e) { }
            @Override
            public void mouseExited(MouseEvent e) { }

        });
    }

}
