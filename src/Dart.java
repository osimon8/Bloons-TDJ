import java.awt.Polygon;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Dart extends TargetedProjectile {

	private int pops = 2;
	protected List<Balloon> hits = new LinkedList<>();
	private static int speed = (int) (1000 * DataLoader.SCALE);
	private static double extra = 50;
	
	public Dart(double x, double y, double targetX, double targetY, Collection<Balloon> b) {
		super(ResourceManager.getInstance().getImage("dart_monkey_dart"), x, y, speed, targetX,
				targetY, b, extra);
        this.scale(1.1);
	}
	
	public Dart(double x, double y, double targetX, double targetY, double extra, 
			Collection<Balloon> b) {
		super(ResourceManager.getInstance().getImage("dart_monkey_dart"), x, y, speed, targetX, 
				targetY, b, extra);
        this.scale(1.1);
	}
	
	@Override 
	public Polygon getHitBox() {
		if (USE_PRECISE_HITBOX) {
			int w = getWidth();
			int h = getHeight();
			return getPolygon(getX(), getY() + 4 * h / 10, w / 2, h / 5); 
			//make only tip of needle the hitbox
		}
		return super.getHitBox();
	}

	
	
	@Override
	protected Collection<GameObject> checkCollisions(){
		Balloon b = intersectingBalloon(hits);
		
		if (b != null) {
			if (b.type() != Bloon.LEAD && !b.frozen()) {
				b.damage(getDamage());
				increaseDistance(25);
				pops--;
			}
			else {
				pops = 0;
			}
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
