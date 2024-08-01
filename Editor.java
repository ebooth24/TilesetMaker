import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


public class Editor extends JFrame implements ActionListener, WindowListener, ChangeListener
{
    int addH = 86;

    int WIDTH = 400;
    int HEIGHT = 400 + addH;


    JComboBox<String> imageDropdown;
    JButton cButton;

    Color c;

    EditorDisplay canvas;

    ColorPicker cp;

    BufferedImage image;

    String path;

    int gW, gH;

    JSlider bSlider;


    public Editor()
    {

        JPanel toolbar = new JPanel(new BorderLayout());
        this.add(toolbar, BorderLayout.NORTH);

            // dropdown image picker
            String[] imageNames = {"c", "c1", "cc", "ec", "ic", "ie", "oc", "oe"};
            
            imageDropdown = new JComboBox<String>(imageNames);
            imageDropdown.addActionListener(this);
            imageDropdown.setActionCommand("image");
            toolbar.add(imageDropdown, BorderLayout.CENTER);

            // color picker
            cButton = new JButton();
            cButton.addActionListener(this);
            cButton.setActionCommand("color");
            cButton.setBackground(c);
            cButton.setForeground(c);
            toolbar.add(cButton, BorderLayout.EAST);

            // brush size
            bSlider = new JSlider(1, 25);
            bSlider.addChangeListener(this);
            bSlider.setSnapToTicks(true);
            toolbar.add(bSlider, BorderLayout.SOUTH);

            
        canvas = new EditorDisplay(WIDTH, HEIGHT);
        this.add(canvas, BorderLayout.CENTER);

        path = "draw/" + imageDropdown.getSelectedItem() + ".png";

        try {
            image = ImageIO.read(new File(path));
        } catch (Exception e1) {}

        loadImage(image);

        this.setTitle("Tile Editor");
        // this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WIDTH, HEIGHT);
        this.setVisible(true);
    }

    public void saveImage()
    {
        // save current image
        Color[][] tiles = this.canvas.tiles;
        BufferedImage newImage = new BufferedImage(tiles.length, tiles[0].length, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = newImage.createGraphics();
        for (int i = 0; i < tiles.length; i++)
            for (int j = 0; j < tiles[i].length; j++)
            {
                Color c = new Color( tiles[i][j].getRed(),
                                     tiles[i][j].getGreen(),
                                     tiles[i][j].getBlue(),
                                     tiles[i][j].getAlpha() );
                g2d.setColor(c);
                g2d.drawRect(i, j, 1, 1);;
            }

        try {
            ImageIO.write(newImage, "png", new File(path));
        } catch (IOException e1) {}

        g2d.dispose();

    }

    public void loadImage(BufferedImage img)
    {
        gW = image.getWidth();
        gH = image.getHeight();
                
        this.canvas.gW = gW;
        this.canvas.gH = gH;

        WIDTH = gW * this.canvas.tW;
        HEIGHT = gH * this.canvas.tH + addH;

        this.setSize(WIDTH, HEIGHT);
                
        this.canvas.tiles = new Color[gW][gH];

        for (int row = 0; row < gH; row++) 
            for (int col = 0; col < gW; col++)
            {
                Color c;
                try {
                    c = new Color(image.getRGB(col, row), true);
                } catch (Exception e3) {
                    c = new Color(0, 0, 0, 0);
                }
                this.canvas.tiles[col][row] = c;

            } 
    }


        
    @Override
    public void actionPerformed(ActionEvent e)
    {
        String action = e.getActionCommand();
        switch (action)
        {
            case "image":

                saveImage();

                path = "draw/" + imageDropdown.getSelectedItem() + ".png";
                
                try {
                    image = ImageIO.read(new File(path));
                } catch (Exception e2) {}
            
                loadImage(image);

                // redraw all images
                GetTile g = new GetTile(new boolean[0][0], 0, 0, gW, gH);
                try {
                    g.redrawAll();
                } catch (IOException ie) {}

                break;
                
            case "color":
                cp = new ColorPicker();
                cp.addWindowListener(this);
                this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
                break;

            default:
                break;
        }
    }

    @Override
    public void stateChanged(ChangeEvent e)
    {
        this.canvas.brushSize = bSlider.getValue();
    }

     
    @Override
    public void windowClosing(WindowEvent e)
    {        
        c = cp.cc.getColor();
        canvas.color = c;
            
        cButton.setBackground(c);
        cButton.setOpaque(true);
        cButton.setBorderPainted(false);
    }
        
    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosed(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e) {}
    
}
