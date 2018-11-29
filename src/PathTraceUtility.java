import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PathTraceUtility {

	private JPanel screen;
	private boolean tracing;
	private boolean active;
	private String filename;
	private List<Point> data;
	
	public PathTraceUtility(JPanel screen, String filename) {
		// TODO Auto-generated constructor stub
		this.screen = screen;
		tracing = false;
		active = false;
		this.filename = filename;
		data = new LinkedList<>();
    	Graphics g = screen.getGraphics();
    	g.setColor(Color.red);
		
		screen.addMouseListener(new MouseAdapter() {
			
			@Override
            public void mouseClicked(MouseEvent e) {
            	if (active) {
	            	if (tracing) 
	            		close();
	            	else {
	            		System.out.println("tracing started");
	            		screen.requestFocus();
	            	}
	            	
	            	
	            	screen.repaint();
	            	tracing = !tracing;
	            	
	            	
            	}
            	else {
            		System.out.println("inactive");
            	}
            }
			
		});
		
		screen.addMouseMotionListener(new MouseAdapter() {
			@Override
            public void mouseMoved(MouseEvent e) {
            	//System.out.println("movement detected");
            	if (active && tracing) {
            		System.out.println("added point");
            		Point p = e.getPoint();
            		data.add(p);
            		g.fillRect(p.x, p.y, 1, 1);
            		//screen.repaint();
            	}
			}
        });
		
	}
	
	public void init() {
		active = true;
	}
	
	public void close() {
		File f = new File("files/data/levels/" + filename + "/" + filename + "PATH.dat");
		try {
			f.createNewFile();
    		//BufferedReader r = new BufferedReader(new FileReader(f));
			Writer w = new FileWriter(f);
			//w.write("------PATH DATA START------");
			w.write(data.size() + "\n");
			for (Point p : data) {
				String s = "" + (int)p.getX() + "," + (int)p.getY() + "\n";
				w.write(s);
			}
			//w.write("------PATH DATA END------");
			w.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		active = false;
		System.out.println("tracer closed");
	}

}
