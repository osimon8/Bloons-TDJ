import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class BombTower extends Tower {

	
	public BombTower(double x, double y, Collection<Balloon> balloons) {
		super(null, x, y, 175, 0, 0.66, 400, "Bomb Tower", balloons);
		setAnimation(generateImages(), 100);
		// TODO Auto-generated constructor stub
	}
	

	private static List<BufferedImage> generateImages() {
		ResourceManager res = ResourceManager.getInstance();
		List<BufferedImage> anim = res.getAnimation("bomb_tower");
		List<BufferedImage> genAnim = new LinkedList<>();
		for (BufferedImage body : anim) {
			BufferedImage wheel = res.getImage("bomb_tower_wheel");
			BufferedImage img = new BufferedImage(body.getWidth() + wheel.getWidth() - 2, body.getHeight(), body.getType());
			img.getGraphics().drawImage(body, wheel.getWidth() - 2, 0, null);
			img.getGraphics().drawImage(wheel, 0, 1 * img.getHeight() / 4, null);
			genAnim.add(ResourceManager.mirrorLR(img));
		}
		//BufferedImage body = res.getImage("bomb_tower_0");

		return genAnim;
	}
	
	@Override
	protected Collection<GameObject> fire(List<Balloon> intersect, int time) {
		Balloon target = selectTarget(intersect);
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
