import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Arrays;
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
	private boolean blastProof = false;
	private boolean freezeProof = false;
	private boolean refreshImage = false;
	private int freezeTimer = 0;
	private Bloon type;
	private Field field;
	
	private List<Bloon> children;
	private List<Balloon> generatedChildren;
	
	public Balloon(Bloon b, Point[] path, Field f) {
		super(null);
		this.path = path;
		pathPosition = 0;
		type = b;
		field = f;
		setUp();
		
		
//		if (hp <= 5)
//			setStockBloon();
		
		// TODO Auto-generated constructor stub
	}
	
	public Balloon(Bloon b, Point[] path, int pos, Field f) {
		super(null);
		this.path = path;
		pathPosition = pos;
		type = b;
		field = f;
		setUp();

		
//		if (hp <= 5)
//			setStockBloon();
		
		// TODO Auto-generated constructor stub
	}
	
	private static BufferedImage getStockBloonImage(Color c) {
		ResourceManager res = ResourceManager.getInstance();
		BufferedImage bloon = ResourceManager.copy(res.getImage("stock_bloon"));
		if (c != null)
			bloon = ResourceManager.tint(bloon, c);
		BufferedImage highlight = res.getImage("stock_bloon_highlight");

		BufferedImage img = new BufferedImage(bloon.getWidth(), bloon.getHeight(), bloon.getType());
		Graphics g = img.getGraphics();
	
	    g.drawImage(bloon, 0, 0, null);
		g.drawImage(highlight, (int)(img.getWidth() * .45), (int)(img.getHeight() / 10), null);
		
		return img;
	}
	
	private BufferedImage getBloonImage(Bloon b) {
		ResourceManager res = ResourceManager.getInstance();
		switch(b) {
		case RED:
			return getStockBloonImage(RED);
		case BLUE:
			return getStockBloonImage(BLUE);
		case GREEN:
			return getStockBloonImage(GREEN);
		case YELLOW:
			return getStockBloonImage(YELLOW);
		case PINK:
			return getStockBloonImage(PINK);
		case RAINBOW:
			return ResourceManager.copy(res.getImage("rainbow"));
		case CERAMIC:
			if (hp <= 2) {
				return ResourceManager.copy(res.getImage("ceramic_05"));
			}
			else if (hp <= 4) {
				return ResourceManager.copy(res.getImage("ceramic_04"));
			}
			else if (hp <= 6) {
				return ResourceManager.copy(res.getImage("ceramic_03"));
			}
			else if (hp <= 8) {
				return ResourceManager.copy(res.getImage("ceramic_02"));
			}
			else
				return ResourceManager.copy(res.getImage("ceramic_01"));
		case BLACK:
			return getStockBloonImage(BLACK);
		case WHITE:
			return getStockBloonImage(null);
		case ZEBRA:
			return ResourceManager.copy(res.getImage("zebra"));
		case LEAD:
			return ResourceManager.copy(res.getImage("lead"));
		default:
			return null;
		}	
	}
	
	private void setUp() {
		children = new LinkedList<>();
		Bloon[] newB = new Bloon[] {};
		Bloon b = type;
		if (b.equals(Bloon.CERAMIC))
			hp = 10;
		else
			hp = 1;
		
		setImage(getBloonImage(b));
		
		switch(b) {
		case RED:
			scale(0.75);
			speed = RED_SPEED;
			break;
		case BLUE:
			scale(0.8);
			speed = RED_SPEED * 1.33;
			newB = new Bloon[] {Bloon.RED};
			break;
		case GREEN:
			scale(0.85);
			speed = RED_SPEED * 1.66;
			newB = new Bloon[] {Bloon.BLUE};
			break;
		case YELLOW:
			scale(0.9);
			speed = RED_SPEED * 3.33;
			newB = new Bloon[] {Bloon.GREEN};
			break;
		case PINK:
			scale(0.95);
			//scale(1.4);
			speed = RED_SPEED * 3.66;
			newB = new Bloon[] {Bloon.YELLOW};
			break;
		case RAINBOW:
			//scale(1.5);
			speed = RED_SPEED * 2.33;
			newB = new Bloon[] {Bloon.ZEBRA, Bloon.ZEBRA};
			break;
		case CERAMIC:
			//scale(1.5);
			speed = RED_SPEED * 2.66;
			newB = new Bloon[] {Bloon.RAINBOW, Bloon.RAINBOW};
			break;
		case BLACK:
			scale(0.6);
			speed = RED_SPEED * 1.66;
			newB = new Bloon[] {Bloon.PINK, Bloon.PINK};
			blastProof = true;
			break;
		case WHITE:
			scale(0.6);
			speed = RED_SPEED * 2;
			newB = new Bloon[] {Bloon.PINK, Bloon.PINK};
			freezeProof = true;
			break;
		case ZEBRA:
			//scale(1.5);
			speed = RED_SPEED * 1.66;
			newB = new Bloon[] {Bloon.BLACK, Bloon.WHITE};
			blastProof = true;
			freezeProof = true;
			break;
		case LEAD:
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
		// this starts as null and only gets filled if necessary for efficiency 
		if (generatedChildren == null) {
			generatedChildren = new LinkedList<>();
			for (Bloon b : children) {
				Balloon ball = new Balloon(b, path, 0, field);
				generatedChildren.add(ball);
			}
		}
		int ctr = 0;
		for (Balloon b : generatedChildren) {
			int ind = pathPosition - (21 / DataLoader.FIDELITY) * ctr;
			if (ind < 0)
				ind = 0;
			b.setPathPosition(ind);
			ctr++;
		}
		return generatedChildren;
	}
	
	
	public static Balloon makeBalloon(Bloon b) {
		return null; //TODO: Implement 
	}
	
	
	public int getHP() {
		return hp;
	}
	
	public Bloon type() {
		return type;
	}
	
	public boolean blastProof() {
		return blastProof;
	}
	
	public void freeze(int time) {
		if (!freezeProof) {
			freezeTimer = time;
			refreshImage = true;
		}
	}
	
	public boolean frozen() {
		return freezeTimer > 0;
	}
	
	public int damage(int dmg) {
		hp -= dmg;
		refreshImage = true;
		
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
		move(path[pos].x, path[pos].y);
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
			field.changeMoney(1);
			return ret;
		}
			
		
		
		if (alive()) {
			

			
			//int ctr = 0;
			
			if (frozen()) {
				freezeTimer -= time;
				
				if (refreshImage) {
					BufferedImage base = getBloonImage(type);
					BufferedImage ice = ResourceManager.getInstance().getImage("ice_effect");
					BufferedImage img = new BufferedImage(ice.getWidth(), ice.getHeight(), ice.getType());
					img.getGraphics().drawImage(base, (img.getWidth() - base.getWidth()) / 2 , (img.getHeight() - base.getHeight()) / 2 , null);
					img.getGraphics().drawImage(ice, 0, 0, null);
					setImage(img);
					refreshImage = false;
				}

				
				if (freezeTimer <= 0) {
					freezeTimer = 0;
					refreshImage = true;
				}
			}
			
			else {
			
				if (refreshImage) {
					setImage(getBloonImage(type));
					refreshImage = false;
				}
				
				double dist = time * speed / 1000.0;
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
					field.changeLives(-calculateRBE());
				}
			}
		
		}

		return null;

	}
	
	public int calculateRBE(){
		int rbe = hp;
		for (Balloon b : this.getChildren())
			rbe += b.calculateRBE();
		return rbe;
	}
	
	public Point getProjectedLocation(int time) {
		return path[pathPosition + (int)(20 * time * speed / (Balloon.RED_SPEED * 18))];
	}
	

	@Override
	public int compareTo(Balloon b) {
//		int val = b.getHP() - hp;
//		if (val != 0)
//			return val;
		int pathDiff = pathPosition - b.pathPosition;
		if (pathDiff == 0)
			return this == b ? 0 : 1;
		return -pathPosition + b.pathPosition;
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (blastProof ? 1231 : 1237);
		result = prime * result + ((children == null) ? 0 : children.hashCode());
		result = prime * result + (freezeProof ? 1231 : 1237);
		result = prime * result + ((generatedChildren == null) ? 0 : generatedChildren.hashCode());
		result = prime * result + hp;
		result = prime * result + Arrays.hashCode(path);
		result = prime * result + pathPosition;
		long temp;
		temp = Double.doubleToLongBits(speed);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj;
	}
	
	

}
