import java.awt.Image;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class TargetedProjectile extends Projectile {
	
	public static final boolean USE_PRECISE_HITBOX = true;
	
	private double distance;
	private static final int BBOX_SCALE = 0;
	private int damage = 1;
	private int pops = 2;
	private List<Balloon> hits = new LinkedList<>();
	
	public TargetedProjectile(BufferedImage img, double x, double y, int speed, double targetX, double targetY, Collection<Balloon> b, double extra) {
		super(img, x, y, b);
		double dx = targetX - x;
		double dy = targetY - y;
		distance = extra + Math.sqrt(dx * dx + dy * dy);
		double theta = Math.toRadians(align(targetX, targetY) - 90);
		
		setVelX(speed * Math.cos(theta));
		setVelY(speed * Math.sin(theta));
		
		
	}
	
	public static TargetedProjectile makeDart(double x, double y, double tx, double ty, Collection<Balloon> b){
        BufferedImage dartImage = ResourceManager.getInstance().getImage("dart_monkey_dart");
        int speed = 1000;
        TargetedProjectile dart = new TargetedProjectile(dartImage, x, y, speed, tx, ty, b, 25);
        dart.scale(1.1);
        return dart;
	}
	

	@Override
	public int getWidth() {
		return super.getWidth() + BBOX_SCALE;
	}
	
	@Override
	public int getHeight() {
		return super.getHeight() + BBOX_SCALE;
	}
	
	@Override 
	public Polygon getHitBox() {
		if (USE_PRECISE_HITBOX) {
			int w = getWidth();
			int h = getHeight();
			return getPolygon(getX(), getY() + 4 * h / 10, w / 2, h / 5); //make only tip of needle the hitbox
		}
		return super.getHitBox();
	}
	
	@Override
	public Collection<GameObject> update(int time) {
		int velX = getVelX();
		int velY = getVelY();
		
		double dx = velX * (time / 1000.0);
		double dy = velY * (time / 1000.0);
		
		shift(dx, dy);
		
		distance -= Math.sqrt(dx * dx + dy * dy);
		
		if (distance <= 0)
			flagForDeath();
		
		Balloon b = intersectingBalloon(hits);
		
		if (b != null) {
			b.damage(damage);
			distance += 25;
			pops--;
			if (pops <= 0)
				flagForDeath();
			else{
				hits.add(b);
				hits.addAll(b.getChildren());
			}
		}
		

		
		return null;
		
	}

}
