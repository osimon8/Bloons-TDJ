import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class TackShooter extends Tower {

	private static double viewRad = 100;
	
	public TackShooter(double x, double y, Collection<Balloon> balloons) {
		super(null, x, y, DataLoader.SCALE * viewRad, 0, 0.6, 305, "Tack Shooter", balloons);
		setAnimation(generateImages(), 100);
	}
	

	private static List<BufferedImage> generateImages() {
		ResourceManager res = ResourceManager.getInstance();
		List<BufferedImage> anim = res.getAnimation("tack_shooter_firing");
		List<BufferedImage> genAnim = new LinkedList<>();
		
		for (BufferedImage cannon : anim) {
			BufferedImage base = res.getImage("tack_shooter_base");
			BufferedImage logo = res.getImage("tack_shooter_tack_logo");
			BufferedImage cannonMirror = ResourceManager.mirrorTB(cannon);
			
			BufferedImage img = new BufferedImage(cannonMirror.getWidth(), cannonMirror.getHeight(),
					base.getType());
			Graphics g = img.getGraphics();
		
		    g.drawImage(cannonMirror, 0, 0, null);

			g.drawImage(base, img.getWidth() - base.getWidth(), img.getWidth() - base.getWidth(),
					null);
			g.drawImage(logo, img.getWidth() - logo.getWidth(),
					img.getHeight() / 2 - logo.getHeight() / 2, null);
			genAnim.add(ResourceManager.mirrorLR(img));
		}


		return genAnim;
	}
	
	@Override
	protected Collection<GameObject> fire(List<Balloon> intersect, int time) {
		Collection<GameObject> ret = new LinkedList<>(); 
		
		double r = getViewRadius();
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
		
		return ret;
	}
}
