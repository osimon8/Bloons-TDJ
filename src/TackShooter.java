import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class TackShooter extends Tower {

	private static double viewRad = 200;
	
	public TackShooter(double x, double y, Collection<Balloon> balloons) {
		super(null, x, y, DataLoader.SCALE * viewRad, 0, 0.6, balloons);
		setAnimation(generateImages(), 100);
		// TODO Auto-generated constructor stub
	}
	

	private static List<BufferedImage> generateImages() {
		ResourceManager res = ResourceManager.getInstance();
		List<BufferedImage> anim = res.getAnimation("tack_shooter_firing");
		List<BufferedImage> genAnim = new LinkedList<>();
		
		for (BufferedImage cannon : anim) {
			BufferedImage base = res.getImage("tack_shooter_base");
			BufferedImage logo = res.getImage("tack_shooter_tack_logo");
			//BufferedImage cannon = res.getImage("tack_shooter_firing_01");
			BufferedImage cannonMirror = ResourceManager.mirrorTB(cannon);
			
			BufferedImage img = new BufferedImage(cannonMirror.getWidth(), cannonMirror.getHeight(), base.getType());
			Graphics g = img.getGraphics();
		
		    g.drawImage(cannonMirror, 0, 0, null);

			g.drawImage(base, img.getWidth() - base.getWidth(), img.getWidth() - base.getWidth(), null);
			g.drawImage(logo, img.getWidth() - logo.getWidth(), img.getHeight() / 2 - logo.getHeight() / 2, null);
			genAnim.add(ResourceManager.mirrorLR(img));
		}


		return genAnim;
	}
	
	protected Collection<GameObject> fire(Balloon target, int time) {
		Collection<GameObject> ret = new LinkedList<>(); 
//		double tx = target.getX();
//		double ty = target.getY();
//		Point t = target.getProjectedLocation(time);
//		double tx = t.getX();
//		double ty = t.getY();
		//Tack dummy = new Tack(0, 0, 0, 0, null);
		
		double r = getViewRadius()/2;
		double x = getX();
		double y = getY();
		double rmod = r * Math.sqrt(2) / 2;
		
		ret.add(new Tack(x, y, x, y - r, balloons)); //N
		ret.add(new Tack(x, y, x + r, y, balloons)); //E
		ret.add(new Tack(x, y, x, y + r, balloons)); //S
		ret.add(new Tack(x, y, x - r, y, balloons)); //W
		
		ret.add(new Tack(x, y, x + rmod, y - rmod, balloons)); //NE
		ret.add(new Tack(x, y, x + rmod, y + rmod, balloons)); //SE
		ret.add(new Tack(x, y, x - rmod, y + rmod, balloons)); //SW
		ret.add(new Tack(x, y, x - rmod, y - rmod, balloons)); //NW
		
		//align(tx, ty);
		return ret;
	}
}
