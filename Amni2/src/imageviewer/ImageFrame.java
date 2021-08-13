package imageviewer;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.filechooser.*;

public class ImageFrame  extends JFrame {
    BufferedImage img;
    String title;
    double scl;

    public ImageFrame(BufferedImage img, String title) {
	this.img = img;
	setup(title);
   }

   public ImageFrame(String filename) {
	readImage(filename);
	setup(filename);
   }

   public ImageFrame(File f) {
	  readImage(f);
	  setup(f.getName());
   }

static JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));

static File choose_image() {
            fc.setAcceptAllFileFilterUsed(false);
            fc.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "gif", "bmp"));
            fc.setAcceptAllFileFilterUsed(true);

	int result = fc.showOpenDialog(null);
	if (result== JFileChooser.APPROVE_OPTION)
		return fc.getSelectedFile();
	else return null;	    
}

   public int getImageHeight() {
	return img.getHeight();
   }


   private void setup(String title) {
        setScale();
	this.title = title;
	setTitle(title);
	//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new ImagePanel());
        pack();
	setLocationRelativeTo(null);
        setVisible(true);
   }
   
   public void setScale()
   {
	int w = img.getWidth();
	int h = img.getHeight();

	Toolkit toolkit =  Toolkit.getDefaultToolkit();
   	Dimension dim = toolkit.getScreenSize();
 	//System.out.format("screen size: %d x %d %n",dim.width,dim.height);

	//System.out.format("image size %d x %d%n",w,h);
	scl = 1.0;
	if (w<128) {
		scl = (double) 128/w;
	}
	else {
		scl = Math.min((double) 800/w, (double) 600/h);
		if (scl>1.0) scl = 1.0;
	}
	//System.out.format("scale %g%n",scl);
   }

    public void info() {
       System.out.println("Image: " + title);
       System.out.format("size: %d x %d%n",img.getWidth(),img.getHeight());
   }

   public void readImage(File f) {
       try {
           img = ImageIO.read(f);
       } catch (IOException e) {
		System.out.println(e.getMessage());
		System.exit(-1);
       }
   }	
    

    public void readImage(String filename) {
       try {
           img = ImageIO.read(new File(filename));
       } catch (IOException e) {
		//System.out.println(e); // e.getMessage());
		System.out.println(filename + " not found");
		System.exit(-1);
       }
    }

    public void writeImage(String filename) {
		writeImage(filename,"jpg");
    }

    public void writeImage(String filename, String format) {
		File file = new File(filename + "." + format);
		try {
			ImageIO.write(img, format, file);
		} catch (IOException e) {
			System.out.println("image write failed");
			System.out.println(e);
			System.exit(-1);
		}
	}

    public Dimension getPreferredSize() {
	int w,h;
	w = h = 100;	
        if (img != null) {
	   w = (int) (scl*img.getWidth())+16;
	   h = (int) (scl*img.getHeight())+38;
	}
	//System.out.format("preferred size: %d x %d%n",w,h);
        return new Dimension(w,h);
    }


class ImagePanel extends JPanel {


    public void paintComponent(Graphics g) {
	Graphics2D g2 = (Graphics2D) g;
	super.paintComponent(g2);
        if (scl==1.0) g2.drawImage(img, 0, 0, null);
        else   g2.drawImage(img, AffineTransform.getScaleInstance(scl, scl), null);
    }
}


    public static void main(String[] args) {
	String filename = "blob.png";
	ImageFrame f1 = null;
	JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
	if (args.length>0) {
		filename = args[0];
		f1 = new ImageFrame(filename);
	}
	else {
	    File f = ImageFrame.choose_image();
	    if (f!=null) f1 = new ImageFrame(f);
	    else System.exit(-1);
        }


	f1.info();
	f1.setLocation(100,100);
	f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}
