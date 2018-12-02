import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.util.Collection;

public abstract class GameObject {

	private double x; 
	private double y;
	private double rotation = 0;
	private BufferedImage img;
	private boolean alive = true;
	private double scaleX = 1;
	private double scaleY = 1;
		
	
	public GameObject(BufferedImage img, double x, double y) {
		this.x = x;
		this.y = y;
		this.img = img;
	}
	
	public GameObject(BufferedImage img) {
		this.x = 0;
		this.y = 0;
		this.img = img;
	}
		

	public double getX() {
		return x;
	}
			
	public double getY() {
		return y; 
	}
		
		
	public void setX(double x) {
		this.x = x;
	}
		
		
	public void setY(double y) {
		this.y = y;
	}
			
	public void move (double x, double y) {	
		this.x = x;
		this.y = y;
	}
	
	public void shift (double dx, double dy) {	
		this.x += dx;
		this.y += dy;
	}
	
	public void setRotation(double angle) {
		this.rotation = angle;
	}
	
	public void rotate(double angle) {
		this.rotation += angle;
	}
	
	public void flagForDeath() {
		alive = false;
	}
	
	public boolean alive() {
		return alive;
	}
			
	public void setImage(BufferedImage img) {
		this.img = img;
	}
	
	public BufferedImage getImage() {
		return ResourceManager.copy(img);
	}
	
	public void scale(double sx, double sy) {
		scaleX = sx;
		scaleY = sy;
	}
	
	public void scale(double s) {
		scale(s, s);
	}
	
	public int getWidth() {
		return (int) (img.getWidth() * scaleX);
	}
	
	public int getHeight() {
		return (int) (img.getHeight() * scaleY);
	}
	
	public Polygon getBounds() {
//		int w = getWidth();
//		int h = getHeight();
//		return new Rectangle((int)x - w /2, (int)y - w/2, w, h);
		int w = getWidth() / 2; 
		int h = getHeight() / 2;
		double t = Math.toRadians(rotation);
		double c = Math.cos(t);
		double s = Math.sin(t);
		
		// (x + w / 2, y - h / 2)
		// (x + w / 2, y + h / 2)
		// (x - w / 2, y + h / 2)
		// (x - w / 2, y - h / 2)
		// (xcost - ysint, xsint + ycost)
		int[] xcoords = new int[] {  (int)(x + (w) * c - (-h) * s),
									(int)(x + (w) * c - (h) * s),
									(int)(x + (-w) * c - (h) * s),
									(int)(x +(-w) * c - (-h) * s)					
		};
		
		int[] ycoords = new int[] { (int)(y + (w) * s + (-h) * c),
				(int)(y + (w) * s + (h) * c),
				(int)(y +(-w) * s + (h) * c),
				(int)(y + (-w) * s + (-h) * c)					
};
		
		Polygon p = new Polygon(xcoords, ycoords, 4);
		return p;
	}
	
	public double align(double tx, double ty) {
		double theta = 90 + Math.atan2(ty - y, tx - x) * 180 / Math.PI;
		setRotation(theta);
		
		return theta;
		
	}
	
	
	public boolean intersects(GameObject o) {
		Area a = new Area(getBounds());
		a.intersect(new Area(o.getBounds()));
		return !a.isEmpty();
		
	}
	
	
	public void drawBoundingBox(Graphics g) {
		Polygon p = getBounds();
		for (int i = 0; i < p.npoints - 1; i++)
			g.drawLine(p.xpoints[i], p.ypoints[i], p.xpoints[i + 1], p.ypoints[i + 1]);
		g.drawLine(p.xpoints[p.npoints - 1], p.ypoints[p.npoints - 1], p.xpoints[0], p.ypoints[0]);
	}

	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		//drawBoundingBox(g2d);
		
		BufferedImage image = ResourceManager.scaleImage(img, scaleX, scaleY);
		
		int w = image.getWidth();
		int h = image.getHeight();
		
		  //Make a backup so that we can reset our graphics object after using it.
	    AffineTransform backup = g2d.getTransform();
	    //rx is the x coordinate for rotation, ry is the y coordinate for rotation, and angle
	    //is the angle to rotate the image. If you want to rotate around the center of an image,
	    //use the image's center x and y coordinates for rx and ry.
	    AffineTransform a = AffineTransform.getRotateInstance(rotation * Math.PI / 180, x, y);
	    //Set our Graphics2D object to the transform
	    g2d.setTransform(a);
	    //Draw our image like normal
	    g2d.drawImage(image, (int)(x - w / 2.0), (int)(y - h / 2.0) , null);
	    //Reset our graphics object so we can draw with it again.
	    g2d.setTransform(backup);
	}
	
	
	
	
	public abstract Collection<GameObject> update(int time);		

	

}
