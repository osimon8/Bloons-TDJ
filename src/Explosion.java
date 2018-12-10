import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Explosion extends Effect {

	private Collection<Balloon> balloons;
	private List<Balloon> hits = new LinkedList<>();
	private int damage;
	
	public Explosion(double x, double y, int damage, Collection<Balloon> bloons) {
		super(ResourceManager.getInstance().getAnimation("explosion"), x, y, 2, 100);
		balloons = bloons;
		this.damage = damage;
	}

	protected List<Balloon> intersectBloon() {
		List<Balloon> l = new LinkedList<>();
		double r = Math.max(getWidth(), getHeight()) / 2;
		
		for (Balloon b : balloons) {
			if (!hits.contains(b)) {
				Area a = new Area(b.getBounds());
				a.intersect(new Area(new Ellipse2D.Double(getX() - r, getY() - r, 2 * r, 2 * r)));
				if (!a.isEmpty())
					l.add(b);
			}
		}
		return l;
	}
	
	@Override
	public Collection<GameObject> update(int time) {
		super.update(time);
		List<Balloon> targets = intersectBloon();
		if (targets != null) {
			for (Balloon b : targets) {
				if (!b.blastProof())
					b.damage(damage);
				List<Balloon> children = b.getChildren();
				if (children != null)
					hits.addAll(children);
			}
			hits.addAll(targets);
		}
		
		return null;
	}
	
}
