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
	
	
	private static double redSpeed = 10;
	
	private int hp;
	private Point[] path;
	private int pathPosition;
	private double speed;
	private boolean blastProof;
	private boolean freezeProof;
	
	private ResourceManager res;
	
	public Balloon(Bloon b, Point[] path, ResourceManager res) {
		super(null);
		this.res = res;
		this.path = path;
		pathPosition = 0;
		setUp(b);
		
		
//		if (hp <= 5)
//			setStockBloon();
		
		// TODO Auto-generated constructor stub
	}
	
	private void setUp(Bloon b) {
		BufferedImage image; 
		hp = 1;
		
		switch(b) {
		case RED:
			image = ResourceManager.copy(res.getImage("stock_bloon"));
			setImage(ResourceManager.tint(image, RED));
			scale(0.75);
			speed = redSpeed;
			break;
		case BLACK:
			image = ResourceManager.copy(res.getImage("stock_bloon"));
			setImage(ResourceManager.tint(image, BLACK));
			scale(0.6);
			speed = redSpeed * 1.66;
			break;
		case BLUE:
			image = ResourceManager.copy(res.getImage("stock_bloon"));
			setImage(ResourceManager.tint(image, BLUE));
			scale(0.8);
			speed = redSpeed * 1.33;
			break;
		case CERAMIC:
			image = ResourceManager.copy(res.getImage("ceramic_01"));
			setImage(image);
			//scale(1.5);
			speed = redSpeed * 2.66;
			hp = 10;
			break;
		case GREEN:
			image = ResourceManager.copy(res.getImage("stock_bloon"));
			setImage(ResourceManager.tint(image, GREEN));
			scale(0.85);
			speed = redSpeed * 1.66;
			break;
		case PINK:
			image = ResourceManager.copy(res.getImage("stock_bloon"));
			setImage(ResourceManager.tint(image, PINK));
			scale(0.95);
			//scale(1.4);
			speed = redSpeed * 3.66;
			break;
		case RAINBOW:
			image = ResourceManager.copy(res.getImage("rainbow"));
			setImage(image);
			//scale(1.5);
			speed = redSpeed * 2.33;
			break;
		case WHITE:
			image = ResourceManager.copy(res.getImage("stock_bloon"));
			setImage(image);
			scale(0.6);
			speed = redSpeed * 2;
			break;
		case YELLOW:
			image = ResourceManager.copy(res.getImage("stock_bloon"));
			setImage(ResourceManager.tint(image, YELLOW));
			scale(0.9);
			speed = redSpeed * 3.33;
			break;
		case ZEBRA:
			image = ResourceManager.copy(res.getImage("zebra"));
			setImage(image);
			//scale(1.5);
			speed = redSpeed * 1.66;
			break;
		default:
			break;
		
		}
		
	}
	
	
	public static Balloon makeBalloon(Bloon b) {
		return null; //TODO: Implement 
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
				speed = redSpeed * 1.33;
				break;
			case (3):
				color = GREEN;
				scale(1.2, 1.2);
				speed = redSpeed * 1.66;
				break;
			case (4):
				color = YELLOW;
				scale(1.3, 1.3);
				speed = redSpeed * 3.33;
				break;
			case (5):
				color = PINK;
				scale(1.4, 1.4);
				speed = redSpeed * 3.66;
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
		
//		if (hp <= 5)
//			setStockBloon();
		
		return hp;
	}
	
	public void pop() {
		flagForDeath();
		//increment money by hp * scale
	}
	
	
	public void setPathPosition(int pos) {
		pathPosition = pos;
	}

	@Override
	public void update(int time) {

		if (alive()) {
		
			double dist = time * redSpeed / 1000.0 * speed;
			
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
