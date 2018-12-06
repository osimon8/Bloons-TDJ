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



public class Tower extends GameObject {

	public enum TargetMode {FIRST, LAST, STRONG, WEAK};
	
	private double viewR;
	private double blindR;
	private Area FOV;
	private Collection<Balloon> balloons;
	private double fireRate;
	private double fireCooldown;
	private TargetMode mode;
	private boolean selected = false;
	private boolean valid = true;
	
	
	public Tower(BufferedImage img, double x, double y, double vr, double br, Collection<Balloon> balloons) {
		super(img, x, y);
		this.balloons = balloons;
		viewR = vr;
		blindR = br;
		fireRate = 1;
		fireCooldown = 0;
		setFOV();
		mode = TargetMode.FIRST;
		// TODO Auto-generated constructor stub
	}

	public static Tower makeDartMonkey(double x, double y, Collection<Balloon> balloons) {
		return new Tower(ResourceManager.getInstance().getImage("dart_monkey_body"), x, y, 300, 0, balloons);
	}
	
	
	public void setTargetMode(TargetMode m) {
		mode = m;
	}
	
	
	public void select() {
		selected = true;
	}
	
	public void deselect() {
		selected = false;
	}
	
	private void setFOV() {
		double x = getX();
		double y = getY();
		FOV = new Area(new Ellipse2D.Double(x - viewR / 2, y - viewR / 2, viewR, viewR));
		FOV.subtract(new Area(new Ellipse2D.Double(x - blindR / 2, y - blindR / 2, blindR, blindR)));
	}
	
	public Area footprint() {
		int r = Math.min(getWidth(), getHeight());
		return new Area(new Ellipse2D.Double(getX() - r / 2, getY() - r / 2, r, r));
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
		setFOV();
	}
	
	private List<Balloon> intersectBloon() {
		List<Balloon> l = new LinkedList<>();
		
		for (Balloon b : balloons) {
			Area a = new Area(b.getBounds());
			a.intersect(FOV);
			if (!a.isEmpty())
				l.add(b);
		}
		return l;
	}

	
	private Balloon selectTarget(List<Balloon> bs) {
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
	
	@Override
	public Collection<GameObject> update(int time) {
		Collection<GameObject> ret = null;
		
		if (fireCooldown <= 0) {
			List<Balloon> intersect = intersectBloon();
			if (!intersect.isEmpty()) {
				fireCooldown = 1000.0 / fireRate;
				ret = fire(selectTarget(intersect), time);
			}
			
			
		}
		else {
			fireCooldown -= time;
		}
		
		
		return ret;

	}
	
	
	private Collection<GameObject> fire(Balloon target, int time) {
		Collection<GameObject> ret = new LinkedList<>(); 
//		double tx = target.getX();
//		double ty = target.getY();
		Point t = target.getProjectedLocation(time);
		double tx = t.getX();
		double ty = t.getY();
		
		
		ret.add(TargetedProjectile.makeDart(getX(), getY(), tx, ty, balloons));
		align(tx, ty);
		return ret;
	}
	
	
	@Override
	public void draw (Graphics g) {
		//Graphics2D gc = (Graphics2D)g;
		
		if (visible() && selected) {
			Color c = g.getColor();
			
			if (valid) {
				g.setColor(new Color(100, 100, 100, 155));
				g.fillOval((int) (getX() - viewR / 2), (int) (getY() - viewR / 2), (int)viewR, (int)viewR);
				g.setColor(new Color(255, 0, 0, 70));
				g.fillOval((int) (getX() - blindR / 2), (int) (getY() - blindR / 2), (int)blindR, (int)blindR);	
				g.setColor(c);
				super.draw(g);
			}
			else {
				super.draw(g);
				g.setColor(new Color(255, 0, 0, 150));
				g.fillOval((int) (getX() - viewR / 2), (int) (getY() - viewR / 2), (int)viewR, (int)viewR);
				g.setColor(c);
			}
			
		}
		else {
			super.draw(g);
		}

		
	}
	
	

}
