import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Bomb extends TargetedProjectile {

	private static int speed = (int) (700 * DataLoader.SCALE);
	
	public Bomb(double x, double y, double targetX, double targetY, Collection<Balloon> b) {
		super(ResourceManager.getInstance().getImage("bomb_tower_bomb_01"), x, y, speed, targetX, 
				targetY, b, 100 * DataLoader.SCALE);
        this.scale(0.75);
	}
	
	@Override
	protected Collection<GameObject> checkCollisions(){
		Balloon b = intersectingBalloon();
		
		if (b != null) {
			flagForDeath();
			List<GameObject> ret = new LinkedList<>();
			ret.add(new Explosion(getX(), getY(), getDamage(), getBalloons()));
			return ret;
		}
		
		return null;
	}

}
