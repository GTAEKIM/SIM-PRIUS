package imageviewer;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.media.jai.PlanarImage;
import javax.swing.BoxLayout;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.mortennobel.imagescaling.MultiStepRescaleOp;
import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;
import com.sun.media.jai.codec.TIFFEncodeParam;
import com.mortennobel.imagescaling.AdvancedResizeOp;

public class ImgViewer extends Frame implements ActionListener 
{
	private static final long serialVersionUID = 1L;

	public static ImgViewer iv;
	
	Image curImg;
	BufferedImage shwImg;
	BufferedImage shwImg_org;
	Panel p;
	Panel pp = new Panel();
	Panel ppp = new Panel();
	
	Panel p2;
	Panel pp2 = new Panel();
	Panel ppp2 = new Panel();
	GridBagLayout gBag;
	
	public static Dimension d;
	Frame frame;
	
	ChartPanel mc; String mc_name;
	ChartPanel mc2; String mc_name2;
	Canvas barimg; String barimg_name;
	Canvas ch1bar; String ch1bar_name;
	Canvas ch2bar; String ch2bar_name;
	Canvas ch3bar; String ch3bar_name;
	
	public static int ddd=0;
	
	Button b_app;
	Button bb;
	Button nextb;
	
	Button trueb;
	Button falseb;
	Button failb;
	Button exportb;
	
	public static TextField ch1_max;
	public static TextField ch2_max;
	public static TextField ch3_max;
	public static TextField ch1_min;
	public static TextField ch2_min;
	public static TextField ch3_min;
	public static TextField sim1_max;
	public static TextField sim2_max;
	public static TextField sim3_max;
	public static TextField sim1_min;
	public static TextField sim2_min;
	public static TextField sim3_min;
	
	public static double ch1mn = 0.0;
	public static double ch2mn = 0.0;
	public static double ch3mn = 0.0;
	public static double ch1mx = 20000.0;
	public static double ch2mx = 20000.0;
	public static double ch3mx = 20000.0;
	public static double sim1mn = 0.0;
	public static double sim2mn = 0.0;
	public static double sim3mn = 0.0;
	public static double sim1mx = 20000.0;
	public static double sim2mx = 20000.0;
	public static double sim3mx = 20000.0;
	
	public static TextField roi_num;
	public static double roin = 1;
	
	public static TextField truefalse;
	public static int istrue;
	
	public static String proj_pth = "/department/department_public/GTKIM/AT/sample_#6/#6_total_37sections/view/";
	//public static String proj_pth = "";
	public static String low_pth = proj_pth + "01_low_mipmap/";
	public static String sim_pth = proj_pth + "02_transformed_sim/";
	public static String sim_loc_pth = proj_pth + "03_transformed_loc/";
	public static String export_pth = proj_pth + "05_exported/";
	
	int command;
	static final int SHOW_NORM = 0;
	static final int SHOW_DRAG = 1;
	static final int SHOW_LINE = 2;
	static final int SHOW_LOW = 3;
	
	public static int numslc = 35;
	public static int slc = 1;
	
	public static int cvs_x = 0;
	public static int cvs_y = 0;
	public static int cvs_w = 1500;
	public static int cvs_h = 1000;
  
	public static int pointer_x = 0;
	
	
	public static int pointer_y = 0;
	public static int moved_x = 0;
	public static int moved_y = 0;
	public static int dragst_x = 0, dragst_y = 0, draged_x=0, draged_y=0;
	public static int curImg_x = 0;
	public static int curImg_y = 0;
	public static boolean msbtn1 = false;
	public static boolean msbtn2 = false;
	public static boolean msbtn3 = false;
	
	public static boolean cntr_pressed = false;
	public static boolean shft_pressed = false;
	
	public static double[] zoom_ratio_list = {0.25, 0.33, 0.5, 0.75, 1, 1.5, 2, 3, 4, 6, 8, 12, 16, 24, 32, 64, 128, 256};
	public static int zoom_ord = 0;
	public static int mip_level = 2;
	public static int[] zoom_ord_at_mip = {4, 2, 0};
	public static double[][] grid_size;
	public static int[] patch_size = {512, 512};
	public static int img_width = 0, img_height = 0;
	
	public static double[][] centers;
	
	public static boolean show_sim = true;
	@SuppressWarnings("rawtypes")
	public static ArrayList simloc = new ArrayList();
	public static int calix = 0;
	public static int caliy = 0;
	public static int caliw = 0;
	public static int calih = 0;

	public ImgViewer(String s) throws IOException 
	{
		super(s);
		addWindowListener(new myWindowListener());
		addMouseListener(new myMouseListener());
		addMouseMotionListener(new myMouseListener());
		addMouseWheelListener(new myMouseListener());
		addKeyListener(new myKeyListener());
		setFocusable(true);	
		
		load_data();
		
		p = new Panel();
		
		pp.setLayout(new GridLayout(2,12));
		pp.add(new Label("ch1_min")); pp.add(ch1_min = new TextField(6)); 
		pp.add(new Label("ch2_min")); pp.add(ch2_min = new TextField(6)); 
		pp.add(new Label("ch3_min")); pp.add(ch3_min = new TextField(6));
		pp.add(new Label("sim1_min")); pp.add(sim1_min = new TextField(6)); 
		pp.add(new Label("sim2_min")); pp.add(sim2_min = new TextField(6)); 
		pp.add(new Label("sim3_min")); pp.add(sim3_min = new TextField(6));
		pp.add(new Label("ch1_max")); pp.add(ch1_max = new TextField(6)); 
		pp.add(new Label("ch2_max")); pp.add(ch2_max = new TextField(6)); 
		pp.add(new Label("ch3_max")); pp.add(ch3_max = new TextField(6));
		pp.add(new Label("sim1_max")); pp.add(sim1_max = new TextField(6)); 
		pp.add(new Label("sim2_max")); pp.add(sim2_max = new TextField(6)); 
		pp.add(new Label("sim3_max")); pp.add(sim3_max = new TextField(6));
		p.add(pp);
		
		p.add(b_app = new Button("Apply"));
		
		ppp.setLayout(new GridLayout(1,2));
		ppp.add(new Label("Roi number")); ppp.add(roi_num = new TextField(6));
		roi_num.addActionListener(this); roi_num.addKeyListener(new myKeyListener());
		p.add(ppp);
		p.add(bb = new Button("Go"));
		p.add(nextb = new Button("Next"));
		p.add(truefalse = new TextField(6)); truefalse.addKeyListener(new myKeyListener());
		
		add(p, BorderLayout.NORTH);
		
		ch1_min.addActionListener(this); ch1_min.addKeyListener(new myKeyListener());
		ch2_min.addActionListener(this); ch2_min.addKeyListener(new myKeyListener());
		ch3_min.addActionListener(this); ch3_min.addKeyListener(new myKeyListener());
		ch1_max.addActionListener(this); ch1_max.addKeyListener(new myKeyListener());
		ch2_max.addActionListener(this); ch2_max.addKeyListener(new myKeyListener());
		ch3_max.addActionListener(this); ch3_max.addKeyListener(new myKeyListener());
		sim1_min.addActionListener(this); sim1_min.addKeyListener(new myKeyListener());
		sim2_min.addActionListener(this); sim2_min.addKeyListener(new myKeyListener());
		sim3_min.addActionListener(this); sim3_min.addKeyListener(new myKeyListener());
		sim1_max.addActionListener(this); sim1_max.addKeyListener(new myKeyListener());
		sim2_max.addActionListener(this); sim2_max.addKeyListener(new myKeyListener());
		sim3_max.addActionListener(this); sim3_max.addKeyListener(new myKeyListener());
		b_app.addActionListener(this);
		bb.addActionListener(this);
		nextb.addActionListener(this);
		
		ch1_min.setText(String.format("%.0f", ch1mn));
		ch2_min.setText(String.format("%.0f", ch2mn));
		ch3_min.setText(String.format("%.0f", ch3mn));
		ch1_max.setText(String.format("%.0f", ch1mx));
		ch2_max.setText(String.format("%.0f", ch2mx));
		ch3_max.setText(String.format("%.0f", ch3mx));
		sim1_min.setText(String.format("%.0f", sim1mn));
		sim2_min.setText(String.format("%.0f", sim2mn));
		sim3_min.setText(String.format("%.0f", sim3mn));
		sim1_max.setText(String.format("%.0f", sim1mx));
		sim2_max.setText(String.format("%.0f", sim2mx));
		sim3_max.setText(String.format("%.0f", sim3mx));
		
		p2 = new Panel();
		gBag = new GridBagLayout();
		pp2.setLayout(gBag);
		p2.add(pp2);
		
		barimg = new myCanvas(); barimg.setSize(200,50);
		gbinsert2pp2(barimg, 0, 0, 1, 1); gbinsert2pp2(new Label(""), 0, 1, 2, 1);
		gbinsert2pp2(new Label("ch1"), 0, 2, 1, 1); ch1bar = new myCanvas(); ch1bar.setSize(200,50);
		gbinsert2pp2(ch1bar, 1, 2, 1, 1); gbinsert2pp2(new Label(""), 0, 3, 2, 1);
		gbinsert2pp2(new Label("ch2"), 0, 4, 1, 1); ch2bar = new myCanvas(); ch2bar.setSize(200,50);
		gbinsert2pp2(ch2bar, 1, 4, 1, 1); gbinsert2pp2(new Label(""), 0, 5, 2, 1);
		gbinsert2pp2(new Label("ch3"), 0, 6, 1, 1); ch3bar = new myCanvas(); ch3bar.setSize(200,50);
		gbinsert2pp2(ch3bar, 1, 6, 1, 1); gbinsert2pp2(new Label(""), 0, 7, 2, 1);
		gbinsert2pp2(new Label("Intensity Plot"), 0, 8, 1, 1); gbinsert2pp2(new Label(""), 0, 9, 2, 1);
		
		trueb = new Button("True"); gbinsert2pp2(trueb, 0, 13, 1, 1);
		falseb = new Button("False"); gbinsert2pp2(falseb, 1, 13, 1, 1);
		failb = new Button("Fail"); gbinsert2pp2(failb, 2, 13, 1, 1);
		exportb = new Button("Export"); gbinsert2pp2(exportb, 3, 13, 1, 1);
		
		trueb.addActionListener(this);
		falseb.addActionListener(this);
		failb.addActionListener(this);
		exportb.addActionListener(this);
		
		barimg_name = barimg.getName();
		ch1bar_name = ch1bar.getName();
		ch2bar_name = ch2bar.getName();
		ch3bar_name = ch3bar.getName();
		
		add(p2, BorderLayout.EAST);
		
		setSize(cvs_w, cvs_h);
		setVisible(true);
		setSize(cvs_w, cvs_h + p.getHeight());
		caliy = p.getHeight();
		caliw = -p2.getWidth();
		calih = -p.getHeight();
	}
	
	public void gbinsert2pp2(Component c, int x, int y, int w, int h){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill= GridBagConstraints.BOTH;
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = w;
        gbc.gridheight = h;
        gBag.setConstraints(c,gbc);
        pp2.add(c);
    }
  
	@SuppressWarnings("unchecked")
	public void load_data() throws IOException
	{
		grid_size = read_matrix(proj_pth+"grid.txt", 3);
		for(int s=0; s<numslc; s++)
		{
			String fname = sim_loc_pth + String.format("%04d.txt", s+1);
			simloc.add(read_matrix(fname, 4));
		}
		centers = read_matrix(proj_pth+"center_points.txt",4);
		
	}
	public double[][] read_matrix(String filename, int ncols) throws IOException
	{
		String lin = "";
		
		File file = new File(filename);
		FileReader filereader = new FileReader(file);
		BufferedReader bufferedreader = new BufferedReader(filereader);
		int linen = 0;
		while((lin = bufferedreader.readLine()) != null)
		{
			linen++;
		}        
		bufferedreader.close();
		
		double[][] matrix = new double[linen][ncols];
		
		FileReader filereader2 = new FileReader(file);
		BufferedReader bufferedreader2 = new BufferedReader(filereader2);
	   
		int row=0;
		while((lin=bufferedreader2.readLine()) !=null)
		{
			String [] tokens = lin.split(" ");
			for(int j=0; j<tokens.length; j++) 
			{
				matrix[row][j] = Double.parseDouble(tokens[j]);
            }
			row++;
        }
		bufferedreader2.close();
		
		return matrix;
	}

  	public void reDraw(int command) 
  	{
  		this.command = command;
  		repaint();
  	}
  	
	public void actionPerformed(ActionEvent e) 
	{
		String typename = e.getSource().getClass().getTypeName();
		
		if(typename == TextField.class.getName())
		{
			if(e.getSource().equals(roi_num))
				go2roi();
			else
				setmnmx();
		}
		else if(typename == Button.class.getName())
		{
			Button b = (Button) e.getSource();
			if(b.getLabel().equals("Apply"))
				setmnmx();
			else if(b.getLabel().equals("Go"))
				go2roi();
			else if(b.getLabel().equals("Next"))
				next2roi();
			else if(b.getLabel().equals("True"))
			{
				truefalse.setText("True");
				istrue = 0;
			}
			else if(b.getLabel().equals("False"))
			{
				truefalse.setText("False");
				istrue = 1;
			}
			else if(b.getLabel().equals("Fail"))
			{
				truefalse.setText("Fail");
				istrue = 2;
			}
			else if(b.getLabel().equals("Export"))
			{
				export_datas();
			}
		}
		
		reDraw(command);
	}
	
	public void export_datas()
	{
		String roi_ext_pth_roi = export_pth + String.format("%04d", (int) roin) + "/";
		
		File roi_ext_fldr_roi = new File(roi_ext_pth_roi);
		if (!roi_ext_fldr_roi.exists()) { try{ roi_ext_fldr_roi.mkdir(); } catch(Exception e){ e.getStackTrace(); } }else {}
		
		String roi_ext_pth = roi_ext_pth_roi + String.format("%04d", (int) slc) + "/";
		
		File roi_ext_fldr = new File(roi_ext_pth);
		if (!roi_ext_fldr.exists()) { try{ roi_ext_fldr.mkdir(); } catch(Exception e){ e.getStackTrace(); } }else {}
		
		double zoom_factor = zoom_ratio_list[zoom_ord]/zoom_ratio_list[zoom_ord_at_mip[mip_level]];
		double coordx = cvs_x/zoom_factor;
		double coordy = cvs_y/zoom_factor;
		double sizex = cvs_w/zoom_factor;
		double sizey = cvs_h/zoom_factor;
		double lstx = dragst_x/zoom_factor, lsty = dragst_y/zoom_factor, 
  				ledx = (dragst_x+moved_x)/zoom_factor, ledy = (dragst_y+moved_y)/zoom_factor;
		
		String outfile = roi_ext_pth + "datas.txt";
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter(outfile));
			bw.write("Is synepse : " + truefalse.getText()); bw.newLine();
			bw.write("Coordinate : " + String.format("%.3f", coordx) + ", " + String.format("%.3f", coordy)); bw.newLine();
			bw.write("Size : " + String.format("%.3f", sizex) + ", " + String.format("%.3f", sizey)); bw.newLine();
			bw.write("Zoom factor : " + zoom_factor); bw.newLine();
			bw.write("Line start x : " + String.format("%.3f",(coordx+lstx))); bw.newLine();
			bw.write("Line start y : " + String.format("%.3f",(coordx+lsty))); bw.newLine();
			bw.write("Line end x : " + String.format("%.3f",(coordx+ledx))); bw.newLine();
			bw.write("Line end y : " + String.format("%.3f",(coordx+ledy))); bw.newLine();
			
			bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Robot robot = new Robot();
			Rectangle rectangle = new Rectangle();
			rectangle.setBounds(iv.location().x, iv.location().y, iv.getWidth(), iv.getHeight());
			BufferedImage image = robot.createScreenCapture(rectangle);
			
			String outfile_capture = roi_ext_pth + "capture.tif";
			
			TIFFEncodeParam params = new TIFFEncodeParam();
			javax.media.jai.JAI.create("encode", image, new FileOutputStream(outfile_capture), "TIFF", params);
			
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setmnmx()
	{
		command = SHOW_NORM;
		ch1mx = Double.parseDouble(ch1_max.getText()); 
		ch2mx = Double.parseDouble(ch2_max.getText()); 
		ch3mx = Double.parseDouble(ch3_max.getText()); 
		ch1mn = Double.parseDouble(ch1_min.getText()); 
		ch2mn = Double.parseDouble(ch2_min.getText()); 
		ch3mn = Double.parseDouble(ch3_min.getText()); 
		sim1mx = Double.parseDouble(sim1_max.getText()); 
		sim2mx = Double.parseDouble(sim2_max.getText()); 
		sim3mx = Double.parseDouble(sim3_max.getText()); 
		sim1mn = Double.parseDouble(sim1_min.getText()); 
		sim2mn = Double.parseDouble(sim2_min.getText()); 
		sim3mn = Double.parseDouble(sim3_min.getText());
	}
	
	public void go2roi()
	{
		command = SHOW_NORM;
		int winw = iv.getWidth() + caliw;
		int winh = iv.getHeight() + calih;
		
		mip_level = 0;
		roin = Double.parseDouble(roi_num.getText());
		zoom_ord = 16;
		double zoom_factor = zoom_ratio_list[zoom_ord]/zoom_ratio_list[zoom_ord_at_mip[mip_level]];
		slc = (int) centers[(int) (roin-1)][3];
		cvs_x = (int) (centers[(int) (roin-1)][1]);
		cvs_y = (int) (centers[(int) (roin-1)][2]);
		
		cvs_x = (int) (cvs_x*zoom_factor - ((winw)/2.0)) + calix ;
		cvs_y = (int) (cvs_y*zoom_factor - ((winh)/2.0)) + caliy;
	}
	
	public void next2roi()
	{
		command = SHOW_NORM;
		int winw = iv.getWidth() + caliw;
		int winh = iv.getHeight() + calih;
		
		mip_level = 0;
		roin = Double.parseDouble(roi_num.getText());
		roin++;
		roi_num.setText(String.format("%d", (int) roin));
		zoom_ord = 16;
		double zoom_factor = zoom_ratio_list[zoom_ord]/zoom_ratio_list[zoom_ord_at_mip[mip_level]];
		slc = (int) centers[(int) (roin-1)][3];
		cvs_x = (int) (centers[(int) (roin-1)][1]);
		cvs_y = (int) (centers[(int) (roin-1)][2]);
		
		cvs_x = (int) (cvs_x*zoom_factor - ((winw)/2.0)) + calix ;
		cvs_y = (int) (cvs_y*zoom_factor - ((winh)/2.0)) + caliy;
	}
	
	public static BufferedImage get_low_img(int slc, int ch, int gx, int gy)
	{	
		String pth = low_pth + "/ch" + String.format("%d", ch) 
			+ "/mip" + String.format("%d", mip_level) + "/" + String.format("%04d", slc) + "/"
			+ String.format("x%02dy%02d.tif", gx, gy);
		
		BufferedImage low_img = null; 
		try 
		{
			SeekableStream stream = new FileSeekableStream(pth);
			String[] names = ImageCodec.getDecoderNames(stream);
			ImageDecoder decoder = ImageCodec.createImageDecoder(names[0], stream, null);
			RenderedImage im = decoder.decodeAsRenderedImage(0);
			low_img = PlanarImage.wrapRenderedImage(im).getAsBufferedImage();
		} catch (IOException e) { e.printStackTrace(); }
		return low_img;
	}
	
	public static BufferedImage merge_ch(int slc, int gx, int gy)
	{
		BufferedImage patch_ch1 = get_low_img(slc, 1, gx, gy);
		BufferedImage patch_ch2 = get_low_img(slc, 2, gx, gy);
		BufferedImage patch_ch3 = get_low_img(slc, 3, gx, gy);
		WritableRaster rstr_ch1 = patch_ch1.getRaster();
		WritableRaster rstr_ch2 = patch_ch2.getRaster();
		WritableRaster rstr_ch3 = patch_ch3.getRaster();
		
		BufferedImage patch = new BufferedImage(patch_size[0], patch_size[1], BufferedImage.TYPE_INT_RGB);
		for(int y=0;y<patch_size[1];y++)
		{
			for(int x=0;x<patch_size[0];x++)
			{
				int smpl1 = rstr_ch1.getSample(x, y, 0);
				int smpl2 = rstr_ch2.getSample(x, y, 0);
				int smpl3 = rstr_ch3.getSample(x, y, 0);
				
				int v1 = (int) ((smpl1-ch1mn)/(ch1mx-ch1mn)*255); v1=v1<0?0:v1; v1=(int)(v1>255?255:v1);
				int v2 = (int) ((smpl2-ch2mn)/(ch2mx-ch2mn)*255); v2=v2<0?0:v2; v2=(int)(v2>255?255:v2);
				int v3 = (int) ((smpl3-ch3mn)/(ch3mx-ch3mn)*255); v3=v3<0?0:v3; v3=(int)(v3>255?255:v3);
		
				int v = (((v1 & 0xFF)<<8) | ((v2 & 0xFF)<<16) | ((v3 & 0xFF)<<0));
				patch.setRGB(x, y, v);
			}
		}
		return patch;
	}
	public static BufferedImage patch2curImg(BufferedImage curImg, BufferedImage patch, int ofs_x, int ofs_y)
	{
		
		for(int y=0;y<patch.getHeight();y++)
		{
			for(int x=0;x<patch.getWidth();x++)
			{
				int v = patch.getRGB(x, y);
				curImg.setRGB(ofs_x+x, ofs_y+y, v);
			}
		}
		return curImg;
	}
	
	@Override
	public void paint(Graphics g) 
	{	
		d = p.getSize();
		double zoom_factor = zoom_ratio_list[zoom_ord]/zoom_ratio_list[zoom_ord_at_mip[mip_level]];
		if(command==SHOW_NORM)
		{
			shwImg = new BufferedImage(cvs_w, cvs_h, BufferedImage.TYPE_INT_RGB);
			Graphics g2 = shwImg.createGraphics();
			
			if(zoom_ord<2)
				mip_level = 2;
			else if(zoom_ord<4)
				mip_level = 1;
			else
				mip_level = 0;
			
			cvs_w = iv.getWidth() + caliw;
			cvs_h = iv.getHeight() + calih;
			
			int pcszx = (int) (patch_size[0]*zoom_ratio_list[zoom_ord]/zoom_ratio_list[zoom_ord_at_mip[mip_level]]);
			int pcszy = (int) (patch_size[1]*zoom_ratio_list[zoom_ord]/zoom_ratio_list[zoom_ord_at_mip[mip_level]]);
			
			int cvs_gx1 = cvs_x/pcszx+1;
			int cvs_gy1 = cvs_y/pcszy+1;
			int cvs_gx2 = (cvs_x+cvs_w)/pcszx+1;
			int cvs_gy2 = (cvs_y+cvs_h)/pcszy+1;
			/*
			curImg =  new BufferedImage(patch_size[0]*(cvs_gx2-cvs_gx1+1), 
					patch_size[1]*(cvs_gy2-cvs_gy1+1), BufferedImage.TYPE_INT_RGB);
			curImg_x = pcszx*(cvs_gx1-1)-cvs_x;
			curImg_y = pcszy*(cvs_gy1-1)-cvs_y;
			*/
			for(int gx=cvs_gx1; gx<=cvs_gx2; gx++)
			{ 
				for(int gy=cvs_gy1; gy<=cvs_gy2; gy++)
				{	
					if(gy<1 | gy>grid_size[mip_level][2] | gx<1 | gx>grid_size[mip_level][1]) 
						continue;
					BufferedImage patch = merge_ch(slc, gx, gy);
					g2.drawImage(patch, pcszx*(gx-1)-cvs_x, pcszy*(gy-1)-cvs_y + d.height, pcszx, pcszy,  this);
					
					//curImg = (Image) patch2curImg((BufferedImage)curImg, patch, (gx-cvs_gx1)*patch_size[0], (gy-cvs_gy1)*patch_size[1]);
				}
			}
			
			if(show_sim==true & zoom_ord>8)
			{	
				int sght_vertex_ax = (int)(cvs_x/zoom_factor);
				int sght_vertex_ay = (int)(cvs_y/zoom_factor);
				int sght_width = (int)(cvs_w/zoom_factor);
				int sght_height = (int)(cvs_h/zoom_factor);
				
				Rectangle sght_rect = new Rectangle(
						sght_vertex_ax, sght_vertex_ay, sght_width, sght_height);
				
				double[][] slcsimloc = (double[][]) simloc.get(slc-1);
				boolean[] isin = get_isin(sght_rect, slcsimloc);
				
				for(int i=0; i<slcsimloc.length; i++)
				{
					if(isin[i]==false)
						continue;
					
					BufferedImage sim = getsim(slc, i);	
					
					g2.drawImage(sim, (int)(slcsimloc[i][0]*zoom_factor-cvs_x + calix), 
							(int)(slcsimloc[i][1]*zoom_factor-cvs_y+caliy), 
							(int)(slcsimloc[i][2]*zoom_factor), 
							(int)(slcsimloc[i][3]*zoom_factor),  this);
				}
			}
			g.drawImage(shwImg, calix, caliy, this);
		}
		
		else if(command==SHOW_DRAG)
		{
			g.drawImage(shwImg, calix + moved_x, caliy+moved_y, this);
			/*
			g.drawImage(curImg, curImg_x + moved_x, curImg_y +d.height+moved_y, 
						(int) (curImg.getWidth(this)*zoom_factor), (int) (curImg.getHeight(this)*zoom_factor), this);
			*/
		}
		if(command==SHOW_LINE)
		{
			g.drawImage(shwImg, calix, caliy, this);
			g.setColor(Color.WHITE);
			int lstx = dragst_x + calix, lsty = dragst_y + caliy, ledx = dragst_x+moved_x + calix, ledy = dragst_y+moved_y + caliy;
			g.drawLine(lstx, lsty, ledx, ledy);
			
		}
		/*
		if(command==SHOW_LOW)
		{
			g.drawImage(shwImg, calix, caliy, this);
			g.setColor(Color.WHITE);
			g.drawLine(calix, (iv.getHeight()+calih)/2 + caliy , iv.getWidth()+calix+caliw, (iv.getHeight()+calih)/2 + caliy);
			g.drawLine((iv.getWidth()+caliw)/2 + calix , caliy, (iv.getWidth()+caliw)/2 + calix , caliy+iv.getHeight()+calih);
		}
		*/
  	}
	
	public static BufferedImage getsim(int slc, int i)
	{
		BufferedImage sim_ch1 = readtif(sim_pth + String.format("%04d", slc) 
			+ "/ch1/" + String.format("%04d", i+1) + ".tif");
		BufferedImage sim_ch2 = readtif(sim_pth + String.format("%04d", slc) 
			+ "/ch2/" + String.format("%04d", i+1) + ".tif");
		BufferedImage sim_ch3 = readtif(sim_pth + String.format("%04d", slc) 
			+ "/ch3/" + String.format("%04d", i+1) + ".tif");
		WritableRaster sim_rstr_ch1 = sim_ch1.getRaster();
		WritableRaster sim_rstr_ch2 = sim_ch2.getRaster();
		WritableRaster sim_rstr_ch3 = sim_ch3.getRaster();
		
		BufferedImage sim = new BufferedImage(sim_ch1.getWidth(), sim_ch1.getHeight(), BufferedImage.TYPE_INT_ARGB);
		for(int y=0;y<sim_ch1.getHeight();y++)
		{
			for(int x=0;x<sim_ch1.getWidth();x++)
			{
				int smpl1 = sim_rstr_ch1.getSample(x, y, 0);
				int smpl2 = sim_rstr_ch2.getSample(x, y, 0);
				int smpl3 = sim_rstr_ch3.getSample(x, y, 0);
				
				int v1 = (int) ((smpl1-sim1mn)/(sim1mx-sim1mn)*255); v1=v1<0?0:v1; v1=(int)(v1>255?255:v1);
				int v2 = (int) ((smpl2-sim2mn)/(sim2mx-sim2mn)*255); v2=v2<0?0:v2; v2=(int)(v2>255?255:v2);
				int v3 = (int) ((smpl3-sim3mn)/(sim3mx-sim3mn)*255); v3=v3<0?0:v3; v3=(int)(v3>255?255:v3);
				int va = 255;
		
				if(smpl1==0)
					continue;
				
				int v = (((v1 & 0xFF)<<8) | ((v2 & 0xFF)<<16) | ((v3 & 0xFF)<<0) | ((va & 0xFF)<<24));
				sim.setRGB(x, y, v);
			}
		}
		return sim;
	}
	
	public static BufferedImage readtif(String filepath)
	{
		File tiffFile = new File(filepath);
		BufferedImage img = null;
		try 
		{
			SeekableStream stream = new FileSeekableStream(tiffFile);
			String[] names = ImageCodec.getDecoderNames(stream);
			ImageDecoder decoder = ImageCodec.createImageDecoder(names[0], stream, null);
			RenderedImage im = decoder.decodeAsRenderedImage(0);
			img = PlanarImage.wrapRenderedImage(im).getAsBufferedImage();
		} catch (IOException e) { e.printStackTrace(); }
		return img;
	}
	
	public static boolean[] get_isin(Rectangle sght_rect, double[][] slcsimloc)
	{
		boolean[] isin = new boolean[slcsimloc.length];
		for(int r=0;r<slcsimloc.length;r++)
		{
			Rectangle simrectinslc = new Rectangle((int) slcsimloc[r][0], 
					(int) slcsimloc[r][1], (int) slcsimloc[r][2], (int) slcsimloc[r][3]);
			isin[r] = isOverlapping(sght_rect, simrectinslc);
		}
		return isin;
	}
	
	public static boolean isOverlapping(Rectangle r1, Rectangle r2) {
	    if ((r1.getY()+r1.getHeight()) < r2.getY() 
	      || r1.getY() > (r2.getY()+r2.getHeight())) {
	        return false;
	    }
	    if ((r1.getX()+r1.getWidth()) < r2.getX() 
	      || r1.getX() > (r2.getX()+r2.getWidth())) {
	        return false;
	    }
	    return true;
	}
	
	public static void ext_prf(BufferedImage img, int stx, int sty, int edx, int edy)
	{
		
	}

  	public static void main(String args[]) throws IOException 
  	{
  		iv = new ImgViewer("Amni");
  	}
  	
  	class myMouseListener implements MouseListener, MouseMotionListener, MouseWheelListener
  	{	
  		@Override
  		public void mouseDragged(MouseEvent e) 
  		{	
  			if(msbtn1==true)
  			{
  				command = SHOW_DRAG;
  	  	  		pointer_x = e.getPoint().x;
  	  	  		pointer_y = e.getPoint().y - p.getHeight();
  	  	  			
  	  	  		moved_x = pointer_x - dragst_x;
  	  		  	moved_y = pointer_y - dragst_y;
  	  		  		
  	  		  	reDraw(command);
  			}
  			else if(msbtn3==true)
  			{
  				command = SHOW_LINE;
  				
  	  	  		pointer_x = e.getPoint().x;
  	  	  		pointer_y = e.getPoint().y - p.getHeight();
  	  	  			
  	  	  		moved_x = pointer_x - dragst_x;
  	  		  	moved_y = pointer_y - dragst_y;
  	  		  	
  	  		  	reDraw(command);
  			}
  		}

  		@Override
  		public void mouseMoved(MouseEvent e) {
  			pointer_x = e.getPoint().x;
  			pointer_y = e.getPoint().y - p.getHeight();
  		}
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) 
		{	
			pointer_x = e.getPoint().x;
  			pointer_y = e.getPoint().y - p.getHeight();
			
			int wheel_step = e.getWheelRotation();
			if (wheel_step==1 & slc<numslc & cntr_pressed==false) 
			{
				slc++;
				command = SHOW_NORM;
				reDraw(command);
			}
			else if (wheel_step==-1 & slc>1  & cntr_pressed==false)
			{
				slc--;
				command = SHOW_NORM;
				reDraw(command);
			}
			else if (wheel_step==-1 & cntr_pressed==true & zoom_ord<zoom_ratio_list.length-1 )
			{
				double zoom_ratio_bf = zoom_ratio_list[zoom_ord];
				zoom_ord++;
				
				pointer_x = e.getPoint().x;
	  			pointer_y = e.getPoint().y - p.getHeight();
	  			double zoom_factor = zoom_ratio_list[zoom_ord]/zoom_ratio_bf;
	  			cvs_x = (int) (cvs_x*zoom_factor - pointer_x*(1-zoom_factor) );
	  			cvs_y = (int) (cvs_y*zoom_factor - pointer_y*(1-zoom_factor) );
				
				reDraw(command);
			}
			else if (wheel_step==1 & cntr_pressed==true & zoom_ord>0 )
			{
				double zoom_ratio_bf = zoom_ratio_list[zoom_ord];
				zoom_ord--;
				
				pointer_x = e.getPoint().x;
	  			pointer_y = e.getPoint().y - p.getHeight();
	  			double zoom_factor = zoom_ratio_list[zoom_ord]/zoom_ratio_bf;
	  			cvs_x = (int) (cvs_x*zoom_factor - pointer_x*(1-zoom_factor) );
	  			cvs_y = (int) (cvs_y*zoom_factor - pointer_y*(1-zoom_factor) );
	  			
				reDraw(command);
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			iv.requestFocus();
			int btn_num = e.getButton();
			System.out.println(btn_num);
		}
		@Override
		public void mousePressed(MouseEvent e) {
			if(e.getButton()==1)
				msbtn1 = true;
			else if(e.getButton()==2)
				msbtn2 = true;
			else if(e.getButton()==3)
				msbtn3 = true;
			
			moved_x = 0;
			moved_y = 0;
			
			pointer_x = e.getPoint().x;
  			pointer_y = e.getPoint().y - p.getHeight();
			dragst_x = pointer_x;
	  		dragst_y = pointer_y;
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			
			if(msbtn1==true)
			{
				msbtn1 = false;
				cvs_x -= moved_x;
				cvs_y -= moved_y;
				command = SHOW_NORM;
				reDraw(command);
			}
			if(msbtn3==true)
			{
				msbtn3 = false;
				if(zoom_ord>15)
				{
					shwImg_org = new BufferedImage(
							(int)(shwImg.getWidth()/zoom_ord), (int)(shwImg.getHeight()/zoom_ord), BufferedImage.TYPE_INT_ARGB);
					for(int x=0; x<shwImg_org.getWidth(); x++)
					{
						for(int y=0; y<shwImg_org.getHeight(); y++)
						{
							int v = shwImg.getRGB(x*zoom_ord, y*zoom_ord);
							shwImg_org.setRGB(x, y, v);
						}
					}
					
					show_datas();
				}
			}
			
			msbtn2 = false;
			
		}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
  	}
  	
  	class myKeyListener implements KeyListener
  	{
		@Override
		public void keyTyped(KeyEvent e) {
			System.out.println(e.getKeyCode());
		}
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode()==17) { cntr_pressed = true; }
			if(e.getKeyCode()==16) 
			{ 
				shft_pressed = true; 
				show_sim = false;
				//command = SHOW_LOW;
				reDraw(command);
			}
		}
		@Override
		public void keyReleased(KeyEvent e) {
			if(e.getKeyCode()==17) { cntr_pressed = false; }
			if(e.getKeyCode()==16) 
			{ 
				shft_pressed = false;
				show_sim = true;
				reDraw(command);
				command = SHOW_NORM;
			}
			
		}
  	}

  	class myWindowListener implements WindowListener
  	{
		@Override
		public void windowOpened(WindowEvent e) {}
		@Override
		public void windowClosing(WindowEvent e) 
		{
			dispose();
			System.exit(0);
		}
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
  	
  	BufferedImage canvasImg_bar;
  	BufferedImage canvasImg_ch1;
  	BufferedImage canvasImg_ch2;
  	BufferedImage canvasImg_ch3;
  	BufferedImage canvasImg_map;
  	
  	@SuppressWarnings("serial")
	class myCanvas extends Canvas {
  		
  		public myCanvas() 
  		{
            setBackground(Color.WHITE);
        }
		
  		@SuppressWarnings("null")
		public void paint(Graphics g) 
  		{
  			//g.clearRect(0, 0,  mc.getWidth(), mc.getHeight());
  			String cvname = this.getName();
  			BufferedImage cvImg = null;
  			if(cvname.matches(barimg_name))
  				cvImg = canvasImg_bar;
  			else if(cvname.matches(ch1bar_name))
  				cvImg = canvasImg_ch1;
  			else if(cvname.matches(ch2bar_name))
  				cvImg = canvasImg_ch2;
  			else if(cvname.matches(ch3bar_name))
  				cvImg = canvasImg_ch3;
  			else if(cvname.matches(mc_name))
  				cvImg = canvasImg_map;
  				
  			g.drawImage(cvImg, 0, 0, this.getWidth(), this.getHeight(), 0, 0, cvImg.getWidth(), cvImg.getHeight(), this);
        }
    }
  	
  	public void show_datas()
  	{
  		int lstx = (int) (dragst_x/(double)zoom_ord), lsty = (int)(dragst_y/(double)zoom_ord), 
  				ledx = (int)((dragst_x+moved_x)/(double)zoom_ord), ledy = (int)((dragst_y+moved_y)/(double)zoom_ord);
			
  		AffineTransform af = new AffineTransform();
		double tht = -Math.atan((ledy-lsty)/(double)(ledx-lstx));
			
		AffineTransform afi = new AffineTransform();
		af.setTransform(Math.cos(tht), Math.sin(tht), -Math.sin(tht), Math.cos(tht), 0, 0);
		try {
			afi = af.createInverse();
			} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
			
		double[] corn1s = {0, 0};
		double[] corn2s = {shwImg_org.getWidth(), 0};
		double[] corn3s = {shwImg_org.getWidth(), shwImg_org.getHeight()};
		double[] corn4s = {0, shwImg_org.getHeight()};
		double[] corn1d = new double[2];
		double[] corn2d = new double[2];
		double[] corn3d = new double[2];
		double[] corn4d = new double[2];
		af.transform(corn1s, 0, corn1d, 0, 1);
		af.transform(corn2s, 0, corn2d, 0, 1);
		af.transform(corn3s, 0, corn3d, 0, 1);
		af.transform(corn4s, 0, corn4d, 0, 1);
		double minx = Math.min(Math.min(Math.min(corn1d[0], corn2d[0]), corn3d[0]), corn4d[0]);
		double miny = Math.min(Math.min(Math.min(corn1d[1], corn2d[1]), corn3d[1]), corn4d[1]);
		double maxx = Math.max(Math.max(Math.max(corn1d[0], corn2d[0]), corn3d[0]), corn4d[0]);
		double maxy = Math.max(Math.max(Math.max(corn1d[1], corn2d[1]), corn3d[1]), corn4d[1]);
			
		int new_w = (int) Math.ceil(maxx-minx);
		int new_h = (int) Math.ceil(maxy-miny);
			
		BufferedImage shwImg_new = new BufferedImage(new_w, new_h, BufferedImage.TYPE_INT_ARGB);
					
			
		for(int x=0; x<shwImg_new.getWidth(); x++)
		{
			for(int y=0; y<shwImg_new.getHeight(); y++)
			{
				double[] xy = {x+minx,y+miny};
				double[] xyxy = new double[2];
				afi.transform(xy, 0, xyxy, 0, 1);
				
				int xx = (int) (xyxy[0]), yy = (int) (xyxy[1]);
				if(xx<0 || xx>=shwImg_org.getWidth() || yy<0 || yy>=shwImg_org.getHeight())
					continue;
				
				int v = shwImg_org.getRGB(xx, yy);
				
				shwImg_new.setRGB((int)(x), (int)(y), v);
			}
		}

		//canvasImg_map = shwImg_new;
		//mc.repaint();
			
		double[] lst = {lstx, lsty};
		double[] led = {ledx, ledy};
		double[] lstt = new double[2];
		double[] ledt = new double[2];
		af.transform(lst, 0, lstt, 0, 1);
		af.transform(led, 0, ledt, 0, 1);
		
		int lstnx = (int)((lstt[0]-minx));
		int lstny = (int)((lstt[1]-miny));
		int lednx = (int)((ledt[0]-minx));
		int ledny = (int)((ledt[1]-miny));
		
		int trs = 0;
		if(lstnx>lednx)
		{
			trs = lstnx; lstnx = lednx; lednx = trs;
			trs = lstny; lstny = ledny; ledny = trs;
		}
		
		int linength = lednx - lstnx;
		
		int biw = barimg.getWidth();
		int bih = barimg.getHeight();
		double bir = bih/(double)biw;
		
		int linvert = (int) (linength*bir/2.0);
		
		BufferedImage barimgimg = new BufferedImage(linength, 2*linvert+1, BufferedImage.TYPE_INT_ARGB);
		BufferedImage ch1barimg = new BufferedImage(linength, 2*linvert+1, BufferedImage.TYPE_INT_ARGB);
		BufferedImage ch2barimg = new BufferedImage(linength, 2*linvert+1, BufferedImage.TYPE_INT_ARGB);
		BufferedImage ch3barimg = new BufferedImage(linength, 2*linvert+1, BufferedImage.TYPE_INT_ARGB);
		int[] clrmap = new int[256];
		for(int i=0; i<256; i++)
		{
			int v2=0, v1=0, v3=0;
			double sumr = 0;
			
			if(i<128)
			{v3=-2*(i-127); v1=2*i; v2=0;}
			else 
			{v3=0; v1=-2*(i-255); v2=2*(i-128);}
			
			sumr = 255/((double) Math.max(Math.max(v1,v2),v3) );
			v1=(int)(v1*sumr);
			v2=(int)(v2*sumr);
			v3=(int)(v3*sumr);
			
			int v = ( ((v1 & 0xFF)<<8) | ((v2 & 0xFF)<<16) | ((v3 & 0xFF)<<0) | ((255 & 0xFF)<<24) );
			clrmap[i] = v;
		}
		
		int min1=255, max1=0, min2=255, max2=0, min3=255, max3=0;
		for(int x=0; x<barimgimg.getWidth(); x++)
		{
			for(int y=0; y<barimgimg.getHeight(); y++)
			{
				int xx = x + lstnx;
				int yy = y + lstny - linvert;
				
				if(xx<0 || xx>=shwImg_new.getWidth() || yy<0 || yy>=shwImg_new.getHeight())
					continue;
				int v = shwImg_new.getRGB(xx, yy);
				int v2 = (v >> 16 ) & 0xFF; min2=min2>v2?v2:min2; max2=max2<v2?v2:max2;
				int v1 = (v >> 8 ) & 0xFF; min1=min1>v1?v1:min1; max1=max1<v1?v1:max1;
				int v3 = (v >> 0 ) & 0xFF; min3=min3>v3?v3:min3; max3=max3<v3?v3:max3;
				
				barimgimg.setRGB((int)(x), (int)(y), v);
			}
		}
		
		XYSeries ch1_srs = new XYSeries("ch1");
		XYSeries ch2_srs = new XYSeries("ch2");
		XYSeries ch3_srs = new XYSeries("ch3");
		XYSeries ch1_srs2 = new XYSeries("ch1");
		XYSeries ch2_srs2 = new XYSeries("ch2");
		XYSeries ch3_srs2 = new XYSeries("ch3");
		
		for(int x=0; x<barimgimg.getWidth(); x++)
		{
			int v1ysum=0, v2ysum=0, v3ysum=0;
			int v1ysum2=0, v2ysum2=0, v3ysum2=0;
			for(int y=0; y<barimgimg.getHeight(); y++)
			{
				int xx = x + lstnx;
				int yy = y + lstny - linvert;
				
				if(xx<0 || xx>=shwImg_new.getWidth() || yy<0 || yy>=shwImg_new.getHeight())
					continue;
				int v = shwImg_new.getRGB(xx, yy);
				int v1=0,v2=0,v3=0;
				int v2o = (v >> 16 ) & 0xFF; v2 = (int)((v2o-min2)/((double)(max2-min2))*255); 
				int v1o = (v >> 8 ) & 0xFF; v1 = (int) ((v1o-min1)/((double)(max1-min1))*255);
				int v3o = (v >> 0 ) & 0xFF; v3 = (int)((v3o-min3)/((double)(max3-min3))*255);
				
				v1=v1<0?0:v1; v1=(int)(v1>255?255:v1);
				v2=v2<0?0:v2; v2=(int)(v2>255?255:v2);
				v3=v3<0?0:v3; v3=(int)(v3>255?255:v3);
				
				ch1barimg.setRGB((int)(x), (int)(y), clrmap[v1]);
				ch2barimg.setRGB((int)(x), (int)(y), clrmap[v2]);
				ch3barimg.setRGB((int)(x), (int)(y), clrmap[v3]);
				
				v1ysum += v1o; v2ysum += v2o; v3ysum += v3o;
				v1ysum2 += v1; v2ysum2 += v2; v3ysum2 += v3;
			}
			v1ysum/=barimgimg.getHeight(); v2ysum/=barimgimg.getHeight(); v3ysum/=barimgimg.getHeight();
			v1ysum2/=barimgimg.getHeight(); v2ysum2/=barimgimg.getHeight(); v3ysum2/=barimgimg.getHeight();
			ch1_srs.add(x, v1ysum); ch2_srs.add(x, v2ysum); ch3_srs.add(x, v3ysum);
			ch1_srs2.add(x, v1ysum2); ch2_srs2.add(x, v2ysum2); ch3_srs2.add(x, v3ysum2);
		}
		
		canvasImg_bar = barimgimg; barimg.repaint();
		canvasImg_ch1 = ch1barimg; ch1bar.repaint();
		canvasImg_ch2 = ch2barimg; ch2bar.repaint();
		canvasImg_ch3 = ch3barimg; ch3bar.repaint();
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(ch1_srs); dataset.addSeries(ch2_srs); dataset.addSeries(ch3_srs);
		
		final JFreeChart chart = 
				ChartFactory.createXYLineChart(
		        "", "Number of pixels from left", "Pixel intensity", 
		        dataset, PlotOrientation.VERTICAL, true, true, false);
		
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.getRenderer().setSeriesPaint(0, new Color(0x00, 0xFF, 0x00));
		plot.getRenderer().setSeriesPaint(1, new Color(0xFF, 0x00, 0x00));
		plot.getRenderer().setSeriesPaint(2, new Color(0x00, 0x00, 0xFF));
		
		mc = new ChartPanel(chart);
		mc.setPreferredSize(new Dimension(400, 300));
		
		gbinsert2pp2(mc, 0, 10, 2, 1);
		iv.setVisible(true);
		
		XYSeriesCollection dataset2 = new XYSeriesCollection();
		dataset2.addSeries(ch1_srs2); dataset2.addSeries(ch2_srs2); dataset2.addSeries(ch3_srs2);
		
		final JFreeChart chart2 = 
				ChartFactory.createXYLineChart(
		        "", "Number of pixels from left", "Pixel intensity", 
		        dataset2, PlotOrientation.VERTICAL, true, true, false);
		
		XYPlot plot2 = (XYPlot) chart2.getPlot();
		plot2.getRenderer().setSeriesPaint(0, new Color(0x00, 0xFF, 0x00));
		plot2.getRenderer().setSeriesPaint(1, new Color(0xFF, 0x00, 0x00));
		plot2.getRenderer().setSeriesPaint(2, new Color(0x00, 0x00, 0xFF));
		
		mc2 = new ChartPanel(chart2);
		mc2.setPreferredSize(new Dimension(400, 300));
		
		gbinsert2pp2(mc2, 0, 12, 2, 1);
		iv.setVisible(true);
		
		
		//Buffered3DObject obj = new Buffered3DObject(200,200,200, Unit.MM); 
		
		
		/*
		double zcvswt = (mc.getWidth()/(double)shwImg_new.getWidth());
		double zcvsht = (mc.getHeight()/(double)shwImg_new.getHeight());
			
		int lstcvx = (int)((lstt[0]-minx)*zcvswt);
		int lstcvy = (int)((lstt[1]-miny)*zcvsht);
		int ledcvx = (int)((ledt[0]-minx)*zcvswt);
		int ledcvy = (int)((ledt[1]-miny)*zcvsht);
			*/
  	}
  	
}






































