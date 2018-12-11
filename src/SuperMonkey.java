import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class SuperMonkey extends Tower {

	public SuperMonkey(double x, double y, Collection<Balloon> balloons) {
		super(generateImage(), x, y, DataLoader.SCALE * 350, 0, 17, 2975, "Super Monkey", balloons);
		// TODO Auto-generated constructor stub
	}

	private static BufferedImage generateImage() {
		ResourceManager res = ResourceManager.getInstance();
		BufferedImage body = res.getImage("supermonkey");
		BufferedImage cape = res.getImage("supermonkey_cape_red");
		BufferedImage logo = res.getImage("supermonkey_cape_logo");
		BufferedImage img = new BufferedImage(body.getWidth(), 
				body.getHeight() + cape.getHeight() / 2,  body.getType());
		img.getGraphics().drawImage(cape, img.getWidth() - cape.getWidth(), 
				img.getHeight() - cape.getHeight(), null);
		img.getGraphics().drawImage(body, 0, 0, null);
		img = ResourceManager.mirrorLR(img);
		img.getGraphics().drawImage(logo, img.getWidth() / 2 - logo.getWidth() / 2, 
				img.getHeight() - cape.getHeight() / 2, null);

		return img;
	}
	
	protected Collection<GameObject> fire(List<Balloon> intersect, int time) {
		Balloon target = selectTarget(intersect);
		Collection<GameObject> ret = new LinkedList<>();         
		Point t = target.getProjectedLocation(time);
		double tx = t.getX();
		double ty = t.getY();
		
		
		ret.add(new Dart(getX(), getY(), tx, ty, 200, balloons));
		align(tx, ty);
		return ret;
	}

	
	
}
