import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;



public abstract class Tower extends GameObject {

	public enum TargetMode {FIRST, LAST, STRONG, WEAK};
	
	private double viewR;
	private double blindR;
	protected Area FOV;
	protected Collection<Balloon> balloons;
	private double fireRate;
	private double fireCooldown;
	private TargetMode mode;
	private boolean selected = false;
	private boolean valid = true;
	private Area footprint;
	private static double baseFireRate = 1;
	
	
	public Tower(BufferedImage img, double x, double y, double vr, double br, double fr, Collection<Balloon> balloons) {
		super(img, x, y);
		this.balloons = balloons;
		viewR = vr;
		blindR = br;
		fireRate = baseFireRate * fr;
		fireCooldown = 500 / fireRate;
		if (img != null)
			setUp();
		mode = TargetMode.FIRST;
		// TODO Auto-generated constructor stub
	}

	
//	public static Tower makeBombTower(double x, double y, Collection<Balloon> balloons) {
//		return new Tower(ResourceManager.getInstance().getImage("bomb_tower_01"), x, y, 350, 0, 0.66, balloons);
//	}
	
	public void setTargetMode(TargetMode m) {
		mode = m;
	}
	
	
	public void select() {
		selected = true;
	}
	
	public void deselect() {
		selected = false;
	}
	
	protected void setFOV() {
		double x = getX();
		double y = getY();
		FOV = new Area(new Ellipse2D.Double(x - getViewRadius(), y - getViewRadius(), 2 * getViewRadius(), 2 * getViewRadius()));
		FOV.subtract(new Area(new Ellipse2D.Double(x - getBlindRadius(), y - getBlindRadius(), 2 * getBlindRadius(), 2 * getBlindRadius())));
	}
	
	private void setFootprint() {
		int r = 3 * Math.min(getWidth(), getHeight()) / 4;
		footprint = new Area(new Ellipse2D.Double(getX() - r / 2, getY() - r / 2, r, r));
	}
	
	private void setUp() {
		setFOV();
		setFootprint();
	}
	
	public Area footprint() {
		return (Area)footprint.clone();
	}
	
	public void invalidate() {
		valid = false;
	}
	
	public void validate() {
		valid = true;
	}
	
	public boolean valid() {
		return valid;
	}
	@Override
	public void move(double x, double y) {
		super.move(x, y);
		setUp();
	}
	
	@Override
	public void setImage(BufferedImage img) {
		super.setImage(img);
		setUp();
	}
	
	protected List<Balloon> intersectBloon() {
		//long t = System.nanoTime();
		List<Balloon> l = new LinkedList<>();
		
		for (Balloon b : balloons) {
			if (true || !(Point.distance(getX(), getY(), b.getX(), b.getY()) > viewR + Math.max(b.getHeight(), b.getWidth()))) {
				Area a = new Area(b.getBounds());
				a.intersect(FOV);
				if (!a.isEmpty())
					l.add(b);
			}

		}
		//System.out.println((System.nanoTime() - t) / 1000000000.0);
		return l;
	}

	
	protected Balloon selectTarget(List<Balloon> bs) {
		Collections.sort(bs);
		switch(mode){
		default:
		case FIRST:
			return bs.get(0);
		case LAST:
			return bs.get(bs.size() - 1);
		case STRONG:
			return Collections.max(bs);
		case WEAK:
			return Collections.min(bs);
			
		}
		
		
	}
	
	
	public double getCooldown() {
		return fireCooldown;
	}
	
	public double getFireRate() {
		return fireRate;
	}

	public void setFireRate(double fireRate) {
		this.fireRate = fireRate;
	}

	public double getViewRadius() {
		return viewR;
	}
	
	public double getBlindRadius() {
		return blindR;
	}
	
	protected abstract Collection<GameObject> fire(List<Balloon> intersect, int time); 
	
	@Override
	public Collection<GameObject> update(int time) {
		progressAnimation(time);
		Collection<GameObject> ret = null;
		
		if (fireCooldown <= 0) {
			List<Balloon> intersect = intersectBloon();
			if (!intersect.isEmpty()) {
				fireCooldown = 1000.0 / fireRate;
				ret = fire(intersect, time);
				animate();
			}
			
			
		}
		else {
			fireCooldown -= time;
		}
		
		
		return ret;

	}
	
	@Override
	public void draw (Graphics g) {
		//Graphics2D gc = (Graphics2D)g;
		
		if (visible() && selected) {
			Color c = g.getColor();
			
			if (valid) {
				g.setColor(new Color(100, 100, 100, 155));
				g.fillOval((int) (getX() - viewR), (int) (getY() - viewR), (int)(2 * viewR), (int)(2 * viewR));
				g.setColor(new Color(255, 0, 0, 70));
				g.fillOval((int) (getX() - blindR), (int) (getY() - blindR), (int)(2 * blindR), (int)(2 * blindR));	
				g.setColor(c);
				super.draw(g);
			}
			else {
				super.draw(g);
				g.setColor(new Color(255, 0, 0, 150));
				g.fillOval((int) (getX() - viewR), (int) (getY() - viewR), (int)(2 * viewR), (int)(2 * viewR));
				g.setColor(c);
			}
			
		}
		else {
			super.draw(g);
		}

		
	}
	
	

}
