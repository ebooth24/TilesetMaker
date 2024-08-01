import javax.swing.*;

public class ColorPicker extends JFrame
{    
    JColorChooser cc;

    int WIDTH = 300;
    int HEIGHT = 300 + 56;

    public ColorPicker()
    {
        cc = new JColorChooser();
        this.add(cc);

        this.setTitle("Color Picker");
        this.setSize(WIDTH, HEIGHT);
        this.setVisible(true);
    }
}
