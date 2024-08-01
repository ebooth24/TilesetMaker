import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;



public class GetTile
{
    boolean[][] tiles;
    int x; 
    int y;
    int gW, gH;

    /**
     * @param tiles 2d array of level
     * @param x current x loc
     * @param y current y loc
     */
    public GetTile(boolean[][] tiles, int x, int y, int gW, int gH)
    {
        this.tiles = tiles;
        this.x = x;
        this.y = y;
        this.gW = gW;
        this.gH = gH;
    }

    @Override
    public String toString()
    {
        boolean n, ne, e, se, s, sw, w, nw;

        n = get(Direction.NORTH);
        ne = get(Direction.NORTH_EAST); 
        e = get(Direction.EAST);
        se = get(Direction.SOUTH_EAST);
        s = get(Direction.SOUTH);
        sw = get(Direction.SOUTH_WEST);
        w = get(Direction.WEST);
        nw = get(Direction.NORTH_WEST);



        // TODO: clean this up, it is very difficult to read
        String a = "";
        if (n) a += 2; else a += 0;
        if (n && e) {if (ne) a += 2; else a += 0;} else a += 1;
        if (e) a += 2; else a += 0;
        if (s && e) {if (se) a += 2; else a += 0;} else a += 1;
        if (s) a += 2; else a += 0;
        if (s && w) {if (sw) a += 2; else a += 0;} else a += 1;
        if (w) a += 2; else a += 0;
        if (n && w) {if (nw) a += 2; else a += 0;} else a += 1;

        return a;
    }

    public void merge(BufferedImage[][] images, String path)
    {
        int w = 0, h = 0;

        if(images.length == 2) {w = 64; h = 64;}
        if(images.length == 3) {w = 256; h = 256;} 

        BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = newImage.createGraphics();

        int x = 0;
        int y = 0;

        for (int i = 0; i < images.length; i++)
        {
            x = 0;
            for (int j = 0; j < images[i].length; j++)
            {
                g2d.drawImage(images[i][j], x, y, null);
                x += images[i][j].getWidth();
            }
            y += images[i][0].getHeight();
        }

        
        g2d.dispose();
        
        try {
            ImageIO.write(newImage, "png", new File(path));
        } catch (IOException e) {}
    }


    public String getImage()
    {
        String path = "tiles/" + toString() + ".png";
        try {
            ImageIO.read(new File(path));
        } catch (Exception e) {
            makeTile(toString());
        }
        return path;
    }

    public void makeTile(String str)
    {

        char n, ne, e, se, s, sw, w, nw; 

        n  = str.charAt(0);
        ne = str.charAt(1);
        e  = str.charAt(2);
        se = str.charAt(3);
        s  = str.charAt(4);
        sw = str.charAt(5);
        w  = str.charAt(6);
        nw = str.charAt(7);

        String[][] chunks = new String[3][3];

        // north edge
        if (n == '0')  chunks[0][1] = "oeN";
        else           chunks[0][1] = "ieNS";

        // east edge
        if (e == '0')  chunks[1][2] = "oeE";
        else           chunks[1][2] = "ieEW";

        // south edge
        if (s == '0')  chunks[2][1] = "oeS";
        else           chunks[2][1] = "ieNS";

        // west edge
        if (w == '0')  chunks[1][0] = "oeW";
        else           chunks[1][0] = "ieEW";

        // northeast corner
        if (n == '2' && e == '2') {
            if (ne == '0')  chunks[0][2] = "icNE";
            else chunks[0][2] = "cc";
        }
        else {
            if (n == '0' && e == '0')  chunks[0][2] = "ocNE";
            else
            {
                if (n == '0')  chunks[0][2] = "ecN";
                else           chunks[0][2] = "ecE";
            }
        }

        // southeast corner
        if (s == '2' && e == '2') {
            if (se == '0')  chunks[2][2] = "icSE";
            else chunks[2][2] = "cc";
        }        else {
            if (s == '0' && e == '0')  chunks[2][2] = "ocSE";
            else
            {
                if (s == '0')  chunks[2][2] = "ecS";
                else           chunks[2][2] = "ecE";
            }
        }

        // southwest corner
        if (s == '2' && w == '2') {
            if (sw == '0')  chunks[2][0] = "icSW";
            else chunks[2][0] = "cc";
        }
        else {
            if (s == '0' && w == '0')  chunks[2][0] = "ocSW";
            else {
                if (s == '0')  chunks[2][0] = "ecS";
                else           chunks[2][0] = "ecW";
            }
        }

        // northwest corner
        if (n == '2' && w == '2') {
            if (nw == '0')  chunks[0][0] = "icNW";
            else chunks[0][0] = "cc";
        }
        else {
            if (n == '0' && w == '0')  chunks[0][0] = "ocNW";
            else {
                if (n == '0')  chunks[0][0] = "ecN";
                else           chunks[0][0] = "ecW";
            }
        }

        boolean NE = chunks[0][2].equals("ocNE");
        boolean SE = chunks[2][2].equals("ocSE");
        boolean SW = chunks[2][0].equals("ocSW");
        boolean NW = chunks[0][0].equals("ocNW");

        if (NE && SE && SW && NW) chunks[1][1] = "c-";
        else {
            if (SE && SW && NW) chunks[1][1] = "c-NE";
            else {
                if (NE && SW && NW) chunks[1][1] = "c-SE";
                else {
                    if (NE && SE && NW) chunks[1][1] = "c-SW";
                    else {
                        if (NE && SE && SW) chunks[1][1] = "c-NW";
                        else {
                            if (NW && NE) chunks[1][1] = "cN";
                            else {
                                if (NE && SE) chunks[1][1] = "cE";
                                else {
                                    if (SE && SW) chunks[1][1] = "cS";
                                    else {
                                        if (SW && NW) chunks[1][1] = "cW";
                                        else {
                                            if (NE && SW) chunks[1][1] = "cNESW";
                                            else {
                                                if (SE && NW) chunks[1][1] = "cSENW";
                                                else {
                                                    if (NE) chunks[1][1] = "cNE";
                                                    else {
                                                        if (SE) chunks[1][1] = "cSE";
                                                        else {
                                                            if (SW) chunks[1][1] = "cSW";
                                                            else {
                                                                if (NW) chunks[1][1] = "cNW";
                                                                else {
                                                                    chunks[1][1] = "c";
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }   
                    }
                }
            }
        }
        
        BufferedImage[][] images = new BufferedImage[3][3];

        for (int a = 0; a < chunks.length; a++)
        {
            for (int b = 0; b < chunks[a].length; b++)
            {
                String path = "chunks/" + chunks[a][b] + ".png"; 
                try {
                    images[a][b] = ImageIO.read(new File(path));
                } catch (Exception e1) {
                    try {
                        makeChunk(path);
                        images[a][b] = ImageIO.read(new File(path));
                    } catch (IOException e2) {}
                }
            }
        }

        try {
            merge(images, "tiles/" + str + ".png");
        } catch (Exception e1) {
            //TODO: handle exception
        }
            
    }

    public void makeChunk(String str) throws IOException
    {
        // rotate the unrotated chunks, as well as stitch together center chunks

        BufferedImage img = ImageIO.read(new File("draw/cc.png"));
        BufferedImage img2;
        
        // center
        if (str.charAt(0) == 'c' && !str.equals("cc")) {
            boolean ne, se, sw, nw;

            if (str.contains("-")) {
                ne = true; se = true; sw = true; nw = true;
            }
            else {
                ne = false; se = false; sw = false; nw = false;
            }
            
            if(str.length() == 2 && str.charAt(1) != '-') {
                if (str.charAt(1) == 'N') {ne = !ne; nw = !nw;}
                if (str.charAt(1) == 'E') {ne = !ne; se = !se;}
                if (str.charAt(1) == 'S') {se = !se; sw = !sw;}
                if (str.charAt(1) == 'W') {sw = !sw; nw = !nw;}
            }
            else {
                if (str.contains("NE")) {ne = !ne;}
                if (str.contains("SE")) {se = !se;}
                if (str.contains("SW")) {sw = !sw;}
                if (str.contains("NW")) {nw = !nw;}
            }

            BufferedImage[][] i = new BufferedImage[2][2];

            BufferedImage blank = ImageIO.read(new File("draw/c.png"));
            BufferedImage filled = ImageIO.read(new File("draw/c1.png"));

            if (ne) i[0][1] = filled; else i[0][1] = blank;
            if (se) i[1][1] = transform(filled, 1); else i[1][1] = blank;
            if (sw) i[1][0] = transform(filled, 2); else i[1][0] = blank;
            if (nw) i[0][0] = transform(filled, 3); else i[0][0] = blank;

            merge(i, "chunks/" + str + ".png");

            img = ImageIO.read(new File("chunks/" + str + ".png"));
        }
        else {
            // corner
            if (str.charAt(1) == 'c') {
                switch (str.charAt(0)) {
                    case 'o':
                        img2 = ImageIO.read(new File("draw/oc.png"));
                        if (str.contains("NE")) {img = img2;}
                        if (str.contains("SE")) {img = transform(img2, 1);}
                        if (str.contains("SW")) {img = transform(img2, 2);}
                        if (str.contains("NW")) {img = transform(img2, 3);}
                        break;

                    case 'e':
                        img2 = ImageIO.read(new File("draw/ec.png"));
                        if (str.contains("N")) {img = img2;}
                        if (str.contains("E")) {img = transform(img2, 1);}
                        if (str.contains("S")) {img = transform(img2, 2);}
                        if (str.contains("W")) {img = transform(img2, 3);}
                        break;
                    
                    case 'i':
                        img2 = ImageIO.read(new File("draw/ic.png"));
                        if (str.contains("SW")) {img = img2;}
                        if (str.contains("NW")) {img = transform(img2, 1);}
                        if (str.contains("NE")) {img = transform(img2, 2);}
                        if (str.contains("SE")) {img = transform(img2, 3);}
                        break;
                    
                    case 'c':
                        img2 = ImageIO.read(new File("draw/cc.png"));
                        img = img2;
                        break;
                
                    default:
                        break;
                }
            }
            // edge
            if (str.charAt(1) == 'e') {
                // oe: default n
                if (str.contains("oe")) {
                    img2 = ImageIO.read(new File("draw/oe.png"));
                    if (str.contains("N")) {img = img2;}
                    if (str.contains("E")) {img = transform(img2, 1);}
                    if (str.contains("S")) {img = transform(img2, 2);}
                    if (str.contains("W")) {img = transform(img2, 3);}

                    
                }
                // ie: default ns
                if (str.contains("ie")) {
                    img2 = ImageIO.read(new File("draw/ie.png"));
                    if (str.contains("NS")) {img = img2;}
                    if (str.contains("EW")) {img = transform(img2, 1);}
                }
            }
        }

        // create image
        try {
            ImageIO.write(img, "png", new File("chunks/" + str + ".png"));
        } catch (IOException e) {}

    }

    // this will be called whenever an image is edited
    // see EditorDisplay (line 70 I think????)
    // TODO: loop through all images in the order makeChunk for all chunks, makeTile for all tiles
    public void redrawAll() throws IOException
    {

        String[] chunkPaths = {
            "ocNE", "ocSE", "ocSW", "ocNW", "ecN", "ecE", "ecS", "ecW", "icNE", "icSE", "icSW", "icNW", "ieNS", "ieEW", "oeN", "oeE", "oeW", "oeS", "cc", "cNE", "cSE", "cSW", "cNW", "c-NE", "c-SE", "c-SW", "c-NW", "cN", "cE", "cS", "cW", "cSENW", "cNESW", "c-", "c"
        };
        String[] tilePaths = {
            "01010101", "01010121", "01012021", "01012101", "01012221", "01202021", "01202101", "01202221", "01210101", "01210121", "01222021", "01222101", "01222221", "20202020", "20202022", "20202101", "20202220", "20202222", "20210101", "20210120", "20210122", "20222020", "20222022", "20222101", "20222220", "20222222", "21010101", "21010120", "21010122", "21012020", "21012022", "21012101", "21012220", "21012222", "22202020", "22202022", "22202101", "22202220", "22202222", "22210101", "22210120", "22210122", "22222020", "22222022", "22222101", "22222220", "22222222"
        };

        for (String path : chunkPaths)
            makeChunk(path);
        
        for (String path : tilePaths)
            makeTile(path);
    }

    // found on stack overflow
    // TODO: try to understand this and maybe edit it
    public static BufferedImage transform(BufferedImage image, int numquadrants) {

        int w0 = image.getWidth();
        int h0 = image.getHeight();

        int w1 = w0;
        int h1 = h0;

        int centerX = w0 / 2;
        int centerY = h0 / 2;

        if (numquadrants % 2 == 1) {
            w1 = h0;
            h1 = w0;

            if (numquadrants % 4 == 1) {
                centerX = Math.max(w0, h0) / 2;
            } else {
                centerX = Math.min(w0, h0) / 2;
            }

            centerY = centerX;
        }

        AffineTransform affineTransform = new AffineTransform();
        affineTransform.setToQuadrantRotation(numquadrants, centerX, centerY);

        AffineTransformOp opRotated = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);

        BufferedImage transformedImage = new BufferedImage(w1, h1, image.getType());

        opRotated.filter(image, transformedImage);
        return transformedImage;
    }


    public boolean get(Direction dir)
    {
        int x2 = x;
        int y2 = y;

        switch (dir) {
            case NORTH:
                y2 -= 1;
                break;

            case NORTH_EAST:
                x2 += 1;
                y2 -= 1;
                break;

            case EAST:
                x2 += 1;
                break;

            case SOUTH_EAST:
                x2 += 1;
                y2 += 1;
                break;

            case SOUTH:
                y2 += 1;
                break;

            case SOUTH_WEST:
                x2 -= 1;
                y2 += 1;
                break;

            case WEST:
                x2 -= 1;
                break;

            case NORTH_WEST:
                x2 -= 1;
                y2 -= 1;
                break;
        }

        if (x2 < 0 || y2 < 0 || x2 > gW - 1 || y2 > gH - 1)
            return true;

        return tiles[x2][y2];
    }
    
}
