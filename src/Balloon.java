import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;




public class Balloon extends GameObject implements Comparable<Balloon>{
	
	
	public static Color RED = new Color(255, 80, 40);
	public static Color BLUE = new Color(40, 180, 210);
	public static Color GREEN = new Color(15, 255, 30);
	public static Color YELLOW = new Color(255, 255, 20);
	public static Color PINK = new Color(230, 160, 210);
	public static Color BLACK = new Color(0, 0, 0);
	
	
	private static final double RED_SPEED = 150;
	
	private int hp;
	private Point[] path;
	private int pathPosition;
	private double speed;
	private boolean blastProof;
	private boolean freezeProof;
	private Bloon type;
	
	private List<Bloon> children;
	
	
	public Balloon(Bloon b, Point[] path) {
		super(null);
		this.path = path;
		pathPosition = 0;
		type = b;
		setUp();
		
		
//		if (hp <= 5)
//			setStockBloon();
		
		// TODO Auto-generated constructor stub
	}
	
	public Balloon(Bloon b, Point[] path, int pos) {
		super(null);
		this.path = path;
		pathPosition = pos;
		type = b;
		setUp();
		
		
//		if (hp <= 5)
//			setStockBloon();
		
		// TODO Auto-generated constructor stub
	}
	
	private void setUp() {
		BufferedImage image; 
		ResourceManager res = ResourceManager.getInstance();
		hp = 1;
		children = new LinkedList<>();
		Bloon[] newB = new Bloon[] {};
		Bloon b = type;
		
		switch(b) {
		case RED:
			image = ResourceManager.copy(res.getImage("stock_bloon"));
			setImage(ResourceManager.tint(image, RED));
			scale(0.75);
			speed = RED_SPEED;
			break;
		case BLUE:
			image = ResourceManager.copy(res.getImage("stock_bloon"));
			setImage(ResourceManager.tint(image, BLUE));
			scale(0.8);
			speed = RED_SPEED * 1.33;
			newB = new Bloon[] {Bloon.RED};
			break;
		case GREEN:
			image = ResourceManager.copy(res.getImage("stock_bloon"));
			setImage(ResourceManager.tint(image, GREEN));
			scale(0.85);
			speed = RED_SPEED * 1.66;
			newB = new Bloon[] {Bloon.BLUE};
			break;
		case YELLOW:
			image = ResourceManager.copy(res.getImage("stock_bloon"));
			setImage(ResourceManager.tint(image, YELLOW));
			scale(0.9);
			speed = RED_SPEED * 3.33;
			newB = new Bloon[] {Bloon.GREEN};
			break;
		case PINK:
			image = ResourceManager.copy(res.getImage("stock_bloon"));
			setImage(ResourceManager.tint(image, PINK));
			scale(0.95);
			//scale(1.4);
			speed = RED_SPEED * 3.66;
			newB = new Bloon[] {Bloon.YELLOW};
			break;
		case RAINBOW:
			image = ResourceManager.copy(res.getImage("rainbow"));
			setImage(image);
			//scale(1.5);
			speed = RED_SPEED * 2.33;
			newB = new Bloon[] {Bloon.ZEBRA, Bloon.ZEBRA};
			break;
		case CERAMIC:
			image = ResourceManager.copy(res.getImage("ceramic_01"));
			setImage(image);
			//scale(1.5);
			speed = RED_SPEED * 2.66;
			hp = 10;
			newB = new Bloon[] {Bloon.RAINBOW, Bloon.RAINBOW};
			break;
		case BLACK:
			image = ResourceManager.copy(res.getImage("stock_bloon"));
			setImage(ResourceManager.tint(image, BLACK));
			scale(0.6);
			speed = RED_SPEED * 1.66;
			newB = new Bloon[] {Bloon.PINK, Bloon.PINK};
			break;
		case WHITE:
			image = ResourceManager.copy(res.getImage("stock_bloon"));
			setImage(image);
			scale(0.6);
			speed = RED_SPEED * 2;
			newB = new Bloon[] {Bloon.PINK, Bloon.PINK};
			break;
		case ZEBRA:
			image = ResourceManager.copy(res.getImage("zebra"));
			setImage(image);
			//scale(1.5);
			speed = RED_SPEED * 1.66;
			newB = new Bloon[] {Bloon.BLACK, Bloon.WHITE};
			break;
		case LEAD:
			image = ResourceManager.copy(res.getImage("lead"));
			setImage(image);
			scale(0.9);
			speed = RED_SPEED;
			newB = new Bloon[] {Bloon.BLACK, Bloon.BLACK};
			break;
		default:
			break;
		
		}
		
		for (Bloon bl : newB)
			children.add(bl);
		
	}
	
	
	public List<Balloon> getChildren() {
		List<Balloon> bs = new LinkedList<>();
		int ctr = 0;
		for (Bloon b : children) {
			int ind = pathPosition - (21 / DataLoader.FIDELITY) * ctr;
			if (ind < 0)
				ind = 0;
			Balloon ball = new Balloon(b, path, ind);
			bs.add(ball);
			ctr++;
		}
		return bs;
	}
	
	
	public static Balloon makeBalloon(Bloon b) {
		return null; //TODO: Implement 
	}
	
	
	public int getHP() {
		return hp;
	}
	
	
	public int damage(int dmg) {
		hp -= dmg;
		
//		if (hp < 1) {
//			pop();
//			return 0;
//		}
		
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
	
	public int getPathPosition() {
		return pathPosition;
	}
	
	@Override
	public Collection<GameObject> update(int time) {
		List<GameObject> ret = null;
		
		
		if (hp < 1) {
			//animate pop
			//return death balloons
			ret = new LinkedList<>();
			ret.add(Effect.makePop(getX(), getY()));
			for (GameObject o : getChildren())
				ret.add(o);
			flagForDeath();
			return ret;
		}
			
		
		
		if (alive()) {
		
			double dist = time * speed / 1000.0;
			
			//int ctr = 0;
			
			while (dist > 0 && pathPosition < path.length - 1) {
				Point p = path[pathPosition + 1];
				move(p.getX(), p.getY());
				dist -= p.distance(path[pathPosition]);
				pathPosition++;
				//ctr ++;
			}
			
			//System.out.println(type + ": " + ctr);
			
			if (pathPosition == path.length - 1) {
				//decrement life by hp * scale
				flagForDeath();
			}
			else {
				if (type.equals(Bloon.CERAMIC)) {
					ResourceManager res = ResourceManager.getInstance();
					if (hp <= 2) {
						BufferedImage image = ResourceManager.copy(res.getImage("ceramic_05"));
						setImage(image);
					}
					else if (hp <= 4) {
						BufferedImage image = ResourceManager.copy(res.getImage("ceramic_04"));
						setImage(image);
					}
					else if (hp <= 6) {
						BufferedImage image = ResourceManager.copy(res.getImage("ceramic_03"));
						setImage(image);
					}
					else if (hp <= 8) {
						BufferedImage image = ResourceManager.copy(res.getImage("ceramic_02"));
						setImage(image);
					}
						
				}
			}
		
		}

		return null;

	}
	
	public Point getProjectedLocation(int time) {
		return path[pathPosition + (int)(20 * time * speed / (Balloon.RED_SPEED * 18))];
	}
	

	@Override
	public int compareTo(Balloon b) {
//		int val = b.getHP() - hp;
//		if (val != 0)
//			return val;
		
		return -pathPosition + b.pathPosition;
		
	}
	

}
