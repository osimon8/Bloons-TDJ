import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;




public class Balloon extends GameObject implements Comparable<Balloon>{
	
	public static Color RED = new Color(255, 80, 40);
	public static Color BLUE = new Color(40, 180, 210);
	public static Color GREEN = new Color(15, 255, 30);
	public static Color YELLOW = new Color(255, 255, 20);
	public static Color PINK = new Color(230, 160, 210);
	public static Color BLACK = new Color(0, 0, 0);
	
	
	private static double speedScale = 50;
	
	private int hp;
	private Point[] path;
	private int pathPosition;
	
	public Balloon(BufferedImage img, double x, double y, int hp, Point[] path) {
		super(ResourceManager.copy(img), x, y);
		this.hp = hp;
		this.path = path;
		pathPosition = 0;
		if (hp <= 5)
			setStockBloon();
		
		// TODO Auto-generated constructor stub
	}
	
	
	private void setStockBloon() {
		Color color = Color.white;
		
		switch (hp) {
			case (1):
				color = RED;
				break;
			case (2):
				color = BLUE;
				scale(1.1, 1.1);
				break;
			case (3):
				color = GREEN;
				scale(1.2, 1.2);
				break;
			case (4):
				color = YELLOW;
				scale(1.3, 1.3);
				break;
			case (5):
				color = PINK;
				scale(1.4, 1.4);
				break;
		}
		
		setImage(ResourceManager.tint(getImage(), color));
		
	}
	
	public int getHP() {
		return hp;
	}
	
	
	public int damage(int dmg) {
		hp -= dmg;
		
		if (hp < 1) {
			pop();
			return 0;
		}
		
		if (hp <= 5)
			setStockBloon();
		
		return hp;
	}
	
	public void pop() {
		flagForDeath();
		//increment money by hp * scale
	}

	@Override
	public void update(int time) {

		if (alive()) {
		
			double dist = time * speedScale / 1000.0 * hp;
			
			while (dist > 0 && pathPosition < path.length - 1) {
				Point p = path[pathPosition + 1];
				move(p.x, p.y);
				dist -= p.distance(path[pathPosition]);
				pathPosition++;
			}
			
			if (pathPosition == path.length - 1) {
				//decrement life by hp * scale
				flagForDeath();
			}
		
		}

		
		// TODO Auto-generated method stub

	}
	

	@Override
	public int compareTo(Balloon b) {
		int val = b.getHP() - hp;
		if (val != 0)
			return val;
		
		return 1;
		
	}
	

}
