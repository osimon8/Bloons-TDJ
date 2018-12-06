import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Collection;

public abstract class Projectile extends GameObject{

	public final static String DART = "files/poison.png";
	

	private int velX;
	private int velY;
	private Collection<Balloon> balloons;
	
	public Projectile(BufferedImage img, double x, double y, int velX, int velY, Collection<Balloon> b) {
		super(img, x, y);
		this.velX = velX;
		this.velY = velY;
		this.balloons = b;
	}
	
	public Projectile(BufferedImage img, double x, double y, Collection<Balloon> b) {
		super(img, x, y);
		this.velX = 0;
		this.velY = 0;
		this.balloons = b;
	}
	
	public Projectile(BufferedImage img, Collection<Balloon> b) {
		super(img);
		this.velX = 0;
		this.velY = 0;
		this.balloons = b;
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
	
	public Collection<Balloon> getBalloons() {
		return balloons;
	}
	
	public Balloon intersectingBalloon(Collection<Balloon> blacklist) {
		for (Balloon b : balloons) {
			if ((blacklist == null || !blacklist.contains(b)) && collides(b))
				return b;
		}
		return null;
		
	}
	
	public Balloon intersectingBalloon() {
		return intersectingBalloon(null);
		
	}

}
