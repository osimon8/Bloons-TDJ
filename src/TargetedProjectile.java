import java.awt.Image;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class TargetedProjectile extends Projectile {
	
	private double distance;
	private static final int BBOX_SCALE = 0;

	
	public TargetedProjectile(BufferedImage img, double x, double y, int speed, double targetX, double targetY, Collection<Balloon> b, double extra) {
		super(img, x, y, b);
		double dx = targetX - x;
		double dy = targetY - y;
		distance = extra + Math.sqrt(dx * dx + dy * dy);
		double theta = Math.toRadians(align(targetX, targetY) - 90);
		
		setVelX(speed * Math.cos(theta));
		setVelY(speed * Math.sin(theta));
		
		
	}
	

	@Override
	public int getWidth() {
		return super.getWidth() + BBOX_SCALE;
	}
	
	@Override
	public int getHeight() {
		return super.getHeight() + BBOX_SCALE;
	}
	
	public double increaseDistance(double incr) {
		distance += incr;
		return distance;
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
		
		return checkCollisions();
		
	}
	
	protected Collection<GameObject> checkCollisions(){
		return null;
	}

}
