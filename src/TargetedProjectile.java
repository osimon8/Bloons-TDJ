import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Collection;

public class TargetedProjectile extends Projectile {

//	public TargetedProjectile(Image img, int x, int y, int velX, int velY) {
//		super(img, x, y, velX, velY);
//		// TODO Auto-generated constructor stub
//	}
//
//	public TargetedProjectile(Image img, int x, int y) {
//		super(img, x, y);
//		// TODO Auto-generated constructor stub
//	}
//
//	public TargetedProjectile(Image img) {
//		super(img);
//		// TODO Auto-generated constructor stub
//	}
	
	private double targetX;
	private double targetY;
	private double distance;
	
	public TargetedProjectile(BufferedImage img, double x, double y, int speed, double targetX, double targetY) {
		super(img, x, y);
		double dx = targetX - x;
		double dy = targetY - y;
		distance = Math.sqrt(dx * dx + dy * dy);
		double theta;
		if (dx == 0) {
			theta = Math.PI/2;
			if (y < targetY) {
				theta *= -1;
			}
		}
		else {
			theta = Math.atan(dy / dx);
		}
		align(targetX, targetY);
		setVelX(speed * Math.cos(theta));
		setVelY(speed * Math.sin(theta));
		
		this.targetX = targetX;
		this.targetY = targetY;
		
	}
	
	public static TargetedProjectile makeDart(double x, double y, double tx, double ty){
        BufferedImage dartImage = ResourceManager.getInstance().getImage("dart_monkey_dart");
        TargetedProjectile dart = new TargetedProjectile(dartImage, x, y, 600, tx, ty);
        dart.scale(0.75);
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
		
		
		return null;
		//rotate(1);		
		
	}

}
