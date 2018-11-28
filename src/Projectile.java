import java.awt.Graphics;
import java.awt.Image;

public abstract class Projectile extends GameObject{

	public final static String DART = "files/poison.png";
	

	private int velX;
	private int velY;
	
	
	public Projectile(Image img, double x, double y, int velX, int velY) {
		super(img, x, y);
		this.velX = velX;
		this.velY = velY;
	}
	
	public Projectile(Image img, double x, double y) {
		super(img, x, y);
		this.velX = 0;
		this.velY = 0;
	}
	
	public Projectile(Image img) {
		super(img);
		this.velX = 0;
		this.velY = 0;
	}

	public int getVelX() {
		return velX;
	}
			
	public int getVelY() {
		return velY; 
	}
		
		
	public void setVelX(double x) {
		this.velX = (int)x;
	}
		
		
	public void setVelY(double y) {
		this.velY = (int)y;
	}
	
	@Override 
	public void rotate(double angle) {
		super.rotate(angle);
	}
}
