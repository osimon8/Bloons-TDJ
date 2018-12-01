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
	
	
	public Tower(BufferedImage img, double x, double y, double vr, double br, Collection<Balloon> balloons) {
		super(img, x, y);
		this.balloons = balloons;
		viewR = vr;
		blindR = br;
		fireRate = 1;
		fireCooldown = 0;
		FOV = new Area(new Ellipse2D.Double(x, y, viewR, viewR));
		FOV.subtract(new Area(new Ellipse2D.Double(x, y, blindR, blindR)));
		mode = TargetMode.FIRST;
		// TODO Auto-generated constructor stub
	}

	
	public void setTargetMode(TargetMode m) {
		mode = m;
	}
	
	private List<Balloon> intersectBloon() {
		List<Balloon> l = new LinkedList<>();
		
		for (Balloon b : balloons) {
			if (FOV.intersects(b.getBounds()))
				l.add(b);
		}
		return l;
	}

	
	private Balloon selectTarget(List<Balloon> bs) {
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
				ret =  fire(selectTarget(intersect));
			}
			
			
		}
		else {
			fireCooldown -= time;
		}
		
		
		return ret;

	}
	
	
	private Collection<GameObject> fire(Balloon target) {
		Collection<GameObject> ret = new LinkedList<>(); 
		ret.add(TargetedProjectile.makeDart(getX(), getY(), target.getX(), target.getY()));
		return ret;
	}
	
	
	

}
