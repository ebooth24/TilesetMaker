import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class Display extends JPanel implements MouseMotionListener, MouseListener
{
    // Window width and height
    int WIDTH, HEIGHT;

    // Tile width and height
    int tW, tH;

    // Grid width and height
    int gW, gH;

    int mouseX, mouseY;

    boolean isDrawing;

    boolean[][] tiles;
    
    public Display(int w, int h)
    {
        WIDTH = w;
        HEIGHT = h;
        
        tW = 50;
        tH = 50;
        
        gW = 15;
        gH = 15;
        
        tiles = new boolean[gH][gW];
        for (int row = 0; row < gH; row++) 
            for (int col = 0; col < gW; col++)
                tiles[row][col] = false;

        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        

        this.setBackground(Color.DARK_GRAY);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setVisible(true);   
    }

    public void update(int x, int y)
    {
        WIDTH = getWidth();
        HEIGHT = getHeight();

        try {
            tiles[x / tW][y / tH] = isDrawing;
        } catch (Exception e) {}

        repaint();
    }

    @Override
    public void paintComponent(Graphics g)
    {
        // clear canvas
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // draw stuff

        
        for (int row = 0; row < gH; row++) 
            for (int col = 0; col < gW; col++)
            {
                if ((row + col) % 2 == 0)
                    g.setColor(new Color(50, 50, 50));
                else
                    g.setColor(new Color(65, 65, 65));
                g.fillRect(row * tW, col * tH, tW, tH);
            }

        
        for (int row = 0; row < gH; row++) 
            for (int col = 0; col < gW; col++)
                if (tiles[row][col])
                {
                    try {
                        GetTile t = new GetTile(tiles, row, col, gW, gH);
                        String tile = t.getImage();
                        try {
                            BufferedImage tileImage = ImageIO.read(new File(tile));
                            g.drawImage(tileImage, row * tW, col * tH, tW, tH, null, null);
                        } catch (Exception e) {}
                    } catch (Exception a) {}
                }
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {            
    }
    
    @Override
    public void mouseDragged(MouseEvent e) 
    {            
        // get mouse coordinates:
        mouseX = e.getX();
        mouseY = e.getY();
    
        // call update, which calls repaint()
        update(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {        
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        // get mouse coordinates:
        mouseX = e.getX();
        mouseY = e.getY();

        isDrawing = !(tiles[mouseX / tW][mouseY / tH]);

        update(mouseX, mouseY);
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {        
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {        
    }

    @Override
    public void mouseExited(MouseEvent e)
    {        
    }
}