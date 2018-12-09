import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.List;

public class Effect extends GameObject {

	protected int duration;
	
	public Effect(BufferedImage img, double x, double y, double scale, int duration) {
		super(img, x, y);
		scale(scale);
		this.duration = duration;
	}
	
	public Effect(List<BufferedImage> animation, double x, double y, double scale, int duration) {
		super(null, x, y);
		setAnimation(animation, duration);
		scale(scale);
		this.duration = duration;
		animate();
	}
	
	public static Effect makePop(double x, double y) {
		return new Effect(ResourceManager.getInstance().getImage("pop"), x, y, 0.75, 60);
	}
	
	public static Effect makeIceExplosion(double x, double y) {
		return new Effect(ResourceManager.getInstance().getAnimation("ice_explosion"), x, y, 0.75, 60);
	}
	
	@Override
	public Collection<GameObject> update(int time) {
		progressAnimation(time);
		if (duration <= 0)
			flagForDeath();
		duration -= time;
		return null;
	}

}
