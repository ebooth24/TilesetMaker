import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    // Window width and height
    int WIDTH = 750, HEIGHT = 750 + 26;

    public Window()
    {
        Display canvas = new Display(WIDTH, HEIGHT);
        this.add(canvas, BorderLayout.CENTER);

        this.setTitle("Tileset Maker");
        // this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WIDTH, HEIGHT);
        this.setVisible(true);
    }

}