import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Pineapple extends Tower {

	public Pineapple(double x, double y, Collection<Balloon> balloons) {
		super(null, x, y, 50, 0, 1000 / 6, 25, "Exploding Pineapple", balloons);
		setAnimation(getImages(), 3000);
		this.animate();
		// TODO Auto-generated constructor stub
	}


	private static List<BufferedImage> getImages() {
		ResourceManager res = ResourceManager.getInstance();
		List<BufferedImage> genAnim = new LinkedList<>();
		
		BufferedImage pineapple = res.getImage("pineapple");
		genAnim.add(pineapple);
		for (int i = 3; i >= 1; i--) {
			BufferedImage img = new BufferedImage(pineapple.getWidth(), pineapple.getHeight(), pineapple.getType());
			BufferedImage number = res.getImage("pineapple_text_0" + i);
			img.getGraphics().drawImage(pineapple, 0, 0, null);
			img.getGraphics().drawImage(number, (img.getWidth() - number.getWidth()) / 2 - 3, (img.getHeight() - number.getHeight()) * 2 / 3 + 5, null);
			genAnim.add(img);
		}
		return genAnim;

		
	}
	
	@Override
	public Collection<GameObject> update(int time) {
		progressAnimation(time);
		if(!animating())
			return fire(null, 0);
		return null;
	}
	
	@Override
	protected Collection<GameObject> fire(List<Balloon> intersect, int time) {
		List<GameObject> ret = new LinkedList<>();
		Explosion e = new Explosion(getX(), getY(), 1, balloons);
		e.scale(1);
		ret.add(e);
		flagForDeath();
		return ret;
	}

	@Override 
	protected void setFootprint() {
		footprint = new Area(new Ellipse2D.Double(0, 0, 0, 0));
	}
	
}
