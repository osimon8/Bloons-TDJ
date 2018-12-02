import java.awt.Image;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.Collection;

public class TargetedProjectile extends Projectile {
	
	private double distance;
	
	public TargetedProjectile(BufferedImage img, double x, double y, int speed, double targetX, double targetY, Collection<Balloon> b) {
		super(img, x, y, b);
		double dx = targetX - x;
		double dy = targetY - y;
		distance = Math.sqrt(dx * dx + dy * dy);
		double theta = Math.toRadians(align(targetX, targetY) - 90);
		
		setVelX(speed * Math.cos(theta));
		setVelY(speed * Math.sin(theta));
		
		
	}
	
	public static TargetedProjectile makeDart(double x, double y, double tx, double ty, Collection<Balloon> b){
        BufferedImage dartImage = ResourceManager.getInstance().getImage("dart_monkey_dart");
        TargetedProjectile dart = new TargetedProjectile(dartImage, x, y, 1800, tx + 50, ty + 50, b);
        dart.scale(1.1);
        return dart;
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
		
		Balloon b = intersectingBalloon();
		System.out.println(b);
		
		if (b != null) {
			b.damage(1);
			flagForDeath();
		}
		
		
		return null;
		//rotate(1);		
		
	}

}
