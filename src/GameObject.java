import java.awt.Color;
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
import java.util.List;

public abstract class GameObject {

	private double x; 
	private double y;
	private double rotation = 0;
	private BufferedImage img;
	private List<BufferedImage> animation;
	private int animDuration = 0;
	private int animTime = -1;
	private boolean alive = true;
	private double scaleX = 1;
	private double scaleY = 1;
	private boolean visible = true;
		
	
	
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
			
	public boolean visible() {
		return visible;
	}
	
	public void visible(boolean b) {
		visible = b;
	}
	
	public void setImage(BufferedImage img) {
		this.img = img;
	}
	
	public BufferedImage getImage() {
		return ResourceManager.copy(img);
	}
	
	public void setAnimation(List<BufferedImage> imgs, int length) {
		animation = imgs;
		setImage(animation.get(0));
		this.animTime = -1;
		this.animDuration = length;
	}
	
	public void removeAnimation() {
		animation = null;
		this.animTime = -1;
		this.animDuration = 0;
	}
	
	public List<BufferedImage> getAnimation() {
		return animation;
	}
	
	public BufferedImage getAnimation(int i) {
		return animation.get(i);
	}
	
	public int getAnimationTime() {
		return animTime;
	}
	
	public int getAnimationLength() {
		return animDuration;
	}
	
	public boolean animated() {
		return animation != null;
	}
	
	public boolean animating() {
		return animTime > -1;
	}
	
	public void animate() {
		animTime = 0;
	}
	
	public void terminateAnimation() {
		if (animated() && animating()) {
			animTime = -1;
			setImage(animation.get(0));
		}

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
	
	public Polygon getPolygon(double cx, double cy, int width, int height) {
//		int w = getWidth();
//		int h = getHeight();
//		return new Rectangle((int)x - w /2, (int)y - w/2, w, h);
		int w = width / 2; 
		int h = height / 2;
		double t = Math.toRadians(rotation);
		double c = Math.cos(t);
		double s = Math.sin(t);
		
//		cx += x;
//		cy -= y;
		
		double dx = x - cx;
		double dy = y - cy;
		
		
		// (x + w / 2, y - h / 2)
		// (x + w / 2, y + h / 2)
		// (x - w / 2, y + h / 2)
		// (x - w / 2, y - h / 2)
		// (xcost - ysint, xsint + ycost) //rotation by t about origin
		// (cx * (x+y) + xcost - ysint, cy * (x+y) + xsint + ycost) //rotation by t about (cx, cy)
		
		
//		int[] xcoords1 = new int[] {  (int)(cx + (w) * c - (-h) * s), //TR
//									(int)(cx + (w) * c - (h) * s), //BR
//									(int)(cx + (-w) * c - (h) * s), //BL
//									(int)(cx +(-w) * c - (-h) * s) //TL			
//		};
//		
//		int[] ycoords1 = new int[] { (int)(cy + (w) * s + (-h) * c),
//				(int)(cy + (w) * s + (h) * c),
//				(int)(cy +(-w) * s + (h) * c),
//				(int)(cy + (-w) * s + (-h) * c)					
//};
		
		int[] xcoords = new int[] {  (int)(x + (dx + w) * c - (dy - h) * s), //TR
				(int)(x + (dx + w) * c - (dy + h) * s), //BR
				(int)(x + (dx - w) * c - (dy + h) * s), //BL
				(int)(x + (dx - w) * c - (dy - h) * s) //TL			
};

		int[] ycoords = new int[] {  (int)(y + (dx + w) * s + (dy - h) * c), //TR
				(int)(y + (dx + w) * s + (dy + h) * c), //BR
				(int)(y + (dx - w) * s + (dy + h) * c), //BL
				(int)(y + (dx - w) * s + (dy - h) * c) //TL			
};
		
//		for (int i = 0; i < 4; i ++) {
//			System.out.println(i + ": (" + xcoords[i] + ", " + ycoords[i] +") VS (" + + xcoords1[i] + ", " + ycoords1[i] + ")");
//		}
//		
		
		Polygon p = new Polygon(xcoords, ycoords, 4);
		return p;
	}
	
	public Polygon getBounds() {
		return getPolygon(x, y, getWidth(), getHeight());
	}
	
	public Polygon getHitBox() {
		return getBounds();
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
	
	
	public boolean collides(GameObject o) {
		Area a = new Area(getHitBox());
		a.intersect(new Area(o.getHitBox()));
		return !a.isEmpty();
		
	}
	
//	private void drawPolygon(Graphics g, Polygon p) {
//		for (int i = 0; i < p.npoints - 1; i++)
//			g.drawLine(p.xpoints[i], p.ypoints[i], p.xpoints[i + 1], p.ypoints[i + 1]);
//		g.drawLine(p.xpoints[p.npoints - 1], p.ypoints[p.npoints - 1], p.xpoints[0], p.ypoints[0]);
//	}
	
	public void drawBoundingBox(Graphics g) {
		Polygon p = getBounds();
		g.drawPolygon(p.xpoints, p.ypoints, p.npoints);
	}
	
	public void drawHitBox(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.RED);
		Polygon p = getHitBox();
		g.drawPolygon(p.xpoints, p.ypoints, p.npoints);
		g.setColor(c);
	}

	
	public boolean progressAnimation(int time) {
		if (animated()  && animating()) {
			animTime += time;
			if (animTime >= animDuration) {
				terminateAnimation();
			}
			else {
				setImage(animation.get(1 + (int) ((animation.size() - 1) * (1.0 * animTime / animDuration))));
			}
		}
		return animated();

		
	}
	
	public void draw(Graphics g) {
		
		
		
		if (visible) {
			Graphics2D g2d = (Graphics2D) g;
			
//			drawBoundingBox(g2d);
//			drawHitBox(g2d);
			
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
	}
	
	
	
	
	public abstract Collection<GameObject> update(int time);
		
	
		

	

}
