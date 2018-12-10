import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.List;

public class Spikes extends Tower {

	private int pops = 10;
	private String last = "";
	
	public Spikes(double x, double y, Collection<Balloon> balloons) {
		super(getStartImage(), x, y, getStartImage().getWidth() / 2, 0, 1000000, 25, "Road Spikes", balloons);
		// TODO Auto-generated constructor stub
	}

	private static BufferedImage getStartImage() {
		return ResourceManager.getInstance().getImage("tacks_01");
	}
	
	@Override
	protected Collection<GameObject> fire(List<Balloon> intersect, int time) {
		Balloon target = selectTarget(intersect);
		pops--;
		target.damage(1);
		return null;
	}

	@Override 
	protected void setFootprint() {
		footprint = new Area(new Ellipse2D.Double(0, 0, 0, 0));
	}
	
	@Override
	public Collection<GameObject> update(int time) {
		if (pops <= 0) {
			flagForDeath();
			return null;
		}
		else {
			String s = "tacks_" + (pops != 1 ? "0" : "") + (11 - pops);
			if (!s.equals(last)) {
				setImage(ResourceManager.getInstance().getImage(s));
				last = s;
			}

			return super.update(time);
		}

	}
	
}
