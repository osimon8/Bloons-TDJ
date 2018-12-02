import java.awt.image.BufferedImage;
import java.util.Collection;

public class Effect extends GameObject {

	private int duration;
	
	public Effect(BufferedImage img, double x, double y, double scale, int duration) {
		super(img, x, y);
		scale(scale);
		this.duration = duration;
	}
	
	public static Effect makePop(double x, double y) {
		return new Effect(ResourceManager.getInstance().getImage("pop"), x, y, 0.75, 60);
	}
	
	@Override
	public Collection<GameObject> update(int time) {
		if (duration <= 0)
			flagForDeath();
		duration -= time;
		return null;
	}

}
