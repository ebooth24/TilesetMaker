import java.awt.*;
import javax.swing.*;

import java.awt.event.*;
import java.lang.Math;

public class EditorDisplay extends JPanel implements MouseMotionListener, MouseListener
{
    // Window width and height
    int WIDTH, HEIGHT;

    // Tile width and height
    int tW, tH;

    // Grid width and height
    int gW, gH;

    // x and y loc of mouse
    int mouseX, mouseY;

    boolean isDrawing = true;

    Color[][] tiles;

    // brush color
    Color color;

    int brushSize = 1;

    int clicks = 0;

    // previous color of a tile
    Color prevC = new Color(0, 0, 0, 0);

    boolean isRightClick = false;
    
    public EditorDisplay(int w, int h)
    {
        WIDTH = w;
        HEIGHT = h;
        
        tW = 5;
        tH = 5;

        gW = 32;
        gH = 32;

        color = Color.RED;
        
        tiles = new Color[gW][gH];

        // fill grid with blank tiles
        for (int row = 0; row < gW; row++) 
            for (int col = 0; col < gH; col++)
                tiles[row][col] = new Color(0, 0, 0, 0);

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

        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));


        try {

            Color currentColor;
            
            switch (clicks)
            {
                case 1:
                    // save the old tile color
                    prevC = tiles[x / tW][y / tH];

                    // determine new tile color
                    if(isDrawing) currentColor = color;
                    else currentColor = new Color(0, 0, 0, 0);

                    // place tile in grid
                    tiles = draw(x / tW, y / tH, brushSize, tiles, currentColor);
                    break;

                case 2:

                    // revert tile to old tile color
                    // without this, the only tile changing is the current tile
                    tiles = draw(x / tW, y / tH, brushSize, tiles, prevC);

                    // fill bucket
                    fill(tiles, x / tW, y / tH, prevC, color, true);
                    break;

                default:
                    break;
            }    

        } catch (Exception e) {}

        repaint();
    }

    public Color[][] draw(int x, int y, int s, Color[][] tiles, Color c)
    {
        int sF = (int)Math.floor(s / 2.0);
        int sC = (int)Math.ceil(s / 2.0);

        for (int i = x - sF; i < x + sC; i++)
            for (int j = y - sF; j < y + sC; j++)
            {
                try {
                    tiles[i][j] = c; 
                } catch (Exception e) {}
            }

        return tiles;
    }

    public void fill(Color[][] screen, int x, int y, Color prevC, Color newC, boolean isFirst) throws Exception
    {
        if (x < 0 || x >= gW || y < 0 || y >= gH)
            return;

        // if (!isFirst)
            if (!screen[x][y].equals(prevC))
                return;

        screen[x][y] = newC;

        try {
            fill(screen, x + 1, y,      prevC, newC, false);
            fill(screen, x - 1, y,      prevC, newC, false);
            fill(screen, x,     y + 1,  prevC, newC, false);
            fill(screen, x,     y - 1,  prevC, newC, false);
        } catch (Exception e) {}


        return;
    }

    // possible new feature: fill similar colors as well 
    // there would be a slider so the user can adjust how
    // closely the colors have to match
    // (100 = only fill same color)
    // (0 = fill all colors)

    // ISSUE:
    // I want to see how VISUALLY similar 2 colors are.
    // do I compare with RGB or HSL?

    // desaturated colors are easier to compare with RGB
    // 2 very light colors with opposite hues should be similar.
    // HSL would cancel out S & L values with desaturated colors,
    // but the hue would drastically change the similarity result.

    // saturated colors are easier to compare with HSL
    // similar reasons as desaturated colors: 
    // red and orange would have a somewhat low similarity score
    // r value is the same but g value is ~176 away

    // what do I do with a very saturated and a very desaturated color???
    public int getSimilarity(Color c1, Color c2)
    {
        // temporary.
        if (c1.equals(c2))
            return 100;
        return 0;
    }

    @Override
    public void paintComponent(Graphics g)
    {
        // clear canvas
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // background
        for (int row = 0; row < gW; row++) 
            for (int col = 0; col < gH; col++)
            {
                if ((row + col) % 2 == 0)
                    g.setColor(new Color(50, 50, 50));
                else
                    g.setColor(new Color(65, 65, 65));
                g.fillRect(row * tW, col * tH, tW, tH);
            }

     
        for (int row = 0; row < gW; row++) 
            for (int col = 0; col < gH; col++)
            {
                g.setColor(tiles[row][col]);
                g.fillRect(row * tW, col * tH, tW, tH);
            }   
    }
    


    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {}

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        // get mouse coordinates:
        mouseX = e.getX();
        mouseY = e.getY();
        clicks = e.getClickCount();

        isRightClick = SwingUtilities.isRightMouseButton(e) || e.isControlDown();

        if (isRightClick)
            color = tiles[mouseX / tW][mouseY / tH];
        else
        {
            if (tiles[mouseX / tW][mouseY / tH] == color)
                isDrawing = false;
            else
                isDrawing = true;
    
            update(mouseX, mouseY);
        }


        
    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {}

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {}

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {}

    @Override
    public void mouseDragged(java.awt.event.MouseEvent e) {

        // get mouse coordinates:
        mouseX = e.getX();
        mouseY = e.getY();

        // number of consecutive clicks 
        clicks = e.getClickCount();
        
        // detect right click
        isRightClick = SwingUtilities.isRightMouseButton(e) || e.isControlDown();
        if (isRightClick)
            color = tiles[mouseX / tW][mouseY / tH];
        else
        {
            update(mouseX, mouseY);
        }

    }

    @Override
    public void mouseMoved(java.awt.event.MouseEvent e) {}
}