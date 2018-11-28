import java.awt.Image;

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
	
	public TargetedProjectile(Image img, double x, double y, int speed, double targetX, double targetY) {
		super(img, x, y);
		double dx = targetX - x;
		double dy = targetY - y;
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
		rotate(theta);
		setVelX(speed * Math.cos(theta));
		setVelY(speed * Math.sin(theta));
		
		this.targetX = targetX;
		this.targetY = targetY;
		
	}
	

	@Override
	public void update(int time) {
		int velX = getVelX();
		int velY = getVelY();
		
		shift(velX * (time / 1000.0), velY * (time / 1000.0));
		
		if (Math.abs(targetX - getX()) < 1 && Math.abs(targetY - getY()) < 1)
			flagForDeath();
		
		//rotate(1);		
		
	}

}
