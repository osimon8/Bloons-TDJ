import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class IceTower extends Tower {

	public static int viewRad = 100;
	public static int duration = 500;
	
	public IceTower(double x, double y, Collection<Balloon> balloons) {
		super(generateImage(1), x, y, DataLoader.SCALE * viewRad, 0, 0.5, 255, "Ice Tower",
				balloons);
	}
	

	private static BufferedImage generateImage(int num) {
		ResourceManager res = ResourceManager.getInstance();
		
		BufferedImage base = res.getImage("ice_tower_base_0" + num);
		base = ResourceManager.mirrorLR(base);
		base = ResourceManager.mirrorTB(base);
		BufferedImage monkey = res.getImage("ice_tower_monkey");
			
		BufferedImage img = new BufferedImage(base.getWidth(), base.getHeight(), base.getType());
		Graphics g = img.getGraphics();
		
		g.drawImage(base, 0, 0, null);

		Graphics2D g2d = (Graphics2D) g;
		    
		AffineTransform backup = g2d.getTransform();
		AffineTransform a = AffineTransform.getRotateInstance(180 * Math.PI / 180,
				monkey.getWidth() / 2, monkey.getHeight() / 2);
		g2d.setTransform(a);
		g.drawImage(monkey, ((-img.getWidth() + monkey.getWidth()) / 2), 0, null);
		g2d.setTransform(backup);
		    
		return img;
	}
	
	@Override
	public double getViewRadius() {
		return super.getViewRadius() * 95 / 100;
	}
	
	
	protected Collection<GameObject> fire(List<Balloon> intersect, int time) {
		Collection<GameObject> ret = new LinkedList<>(); 

		
		for (Balloon b : intersect) {
			b.freeze(duration);
		}
	
		ret.add(Effect.makeIceExplosion(getX(), getY()));
		return ret;
	}

}
