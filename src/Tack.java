import java.awt.Polygon;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Tack extends TargetedProjectile {

	protected List<Balloon> hits = new LinkedList<>();
	private static int speed = (int) (1000 * DataLoader.SCALE);
	
	public Tack(double x, double y, double targetX, double targetY, Collection<Balloon> b) {
		super(ResourceManager.getInstance().getImage("tack_shooter_tack"), x, y, speed, targetX, targetY, b, 0);
        this.scale(1);
	}
	
	@Override 
	public Polygon getHitBox() {
		if (USE_PRECISE_HITBOX) {
			int w = getWidth();
			int h = getHeight();
			return getPolygon(getX(), getY() + 2 * h / 5, w, h / 5); //make only tip of needle the hitbox
		}
		return super.getHitBox();
	}

	
	
	@Override
	protected Collection<GameObject> checkCollisions(){
		Balloon b = intersectingBalloon(hits);
		
		if (b != null) {
			if (b.type() != Bloon.LEAD && !b.frozen()) {
				b.damage(getDamage());
			}
			flagForDeath();
		}
		return null;
	}
	
}
