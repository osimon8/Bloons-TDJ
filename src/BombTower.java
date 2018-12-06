import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class BombTower extends Tower {

	public BombTower(double x, double y, Collection<Balloon> balloons) {
		super(generateImage(), x, y, 350, 0, 0.66, balloons);
		// TODO Auto-generated constructor stub
	}
	

	private static BufferedImage generateImage() {
		ResourceManager res = ResourceManager.getInstance();
		BufferedImage body = res.getImage("bomb_tower_01");
		BufferedImage wheel = res.getImage("bomb_tower_wheel");
		BufferedImage img = new BufferedImage(body.getWidth() + wheel.getWidth() - 2, body.getHeight(), body.getType());
		img.getGraphics().drawImage(body, wheel.getWidth() - 2, 0, null);
		img.getGraphics().drawImage(wheel, 0, 1 * img.getHeight() / 4, null);
		return ResourceManager.mirrorLR(img);
	}
	
	protected Collection<GameObject> fire(Balloon target, int time) {
		Collection<GameObject> ret = new LinkedList<>(); 
//		double tx = target.getX();
//		double ty = target.getY();
		Point t = target.getProjectedLocation(time);
		double tx = t.getX();
		double ty = t.getY();
		
		
		ret.add(new Bomb(getX(), getY(), tx, ty, balloons));
		align(tx, ty);
		return ret;
	}
	
	
	

}
