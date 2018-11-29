import java.awt.Image;
import java.awt.Point;
import java.util.List;

public class Balloon extends GameObject {

	private static double speedScale = 50;
	
	private int hp;
	private Point[] path;
	private int pathPosition;
	
	public Balloon(Image img, double x, double y, int hp, Point[] path) {
		super(img, x, y);
		this.hp = hp;
		this.path = path;
		pathPosition = 0;
		// TODO Auto-generated constructor stub
	}
	
	public int getHP() {
		return hp;
	}
	
	public int damage(int dmg) {
		hp -= dmg;
		return hp;
	}
	
	public void pop() {
		flagForDeath();
		//increment money by hp * scale
	}

	@Override
	public void update(int time) {
		if (hp < 1) {
			pop();
			return;
		}
		
		double dist = time * speedScale / 1000 * hp;
		
		while (dist > 0 && pathPosition < path.length - 1) {
			Point p = path[pathPosition + 1];
			move(p.getX(), p.getY());
			dist -= p.distance(path[pathPosition]);
			pathPosition++;
		}
		
		if (pathPosition == path.length - 1) {
			//decrement life by hp * scale
			flagForDeath();
		}
		
		
		// TODO Auto-generated method stub

	}

}
