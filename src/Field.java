/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Area;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;


/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact with one another. Take
 * time to understand how the timer interacts with the different methods and how it repaints the GUI
 * on every tick().
 */
@SuppressWarnings("serial")
public class Field extends JPanel {

	
	private int lives = 0;
	private int money = 0;
	private int level = 0;
	private boolean inLevel = false;
	private List<Projectile> projectiles;
	private List<Balloon> balloons;
	private List<Tower> towers;
	private List<Effect> effects;
	private ResourceManager res;
	private Point[] bloonPath;
	private Area placementArea;
	private JPanel bottomHUD;
	private JPanel rightHUD;
	private JFrame frame;
	private JLabel towerPriceLabel;
	private JLabel levelLabel = new JLabel();
	private JLabel livesLabel = new JLabel();
	private JLabel moneyLabel = new JLabel();
	private JButton nextLevel;
	private Timer bloonGenerator;
	private Timer ticker;

    public boolean playing = false; // whether the game is running 
    private Image background;
    long startTime;
    
    // Game constants
    public static int width = 300; //default values 
    public static int height = 300;

    // Update interval for timer, in milliseconds
    
    
    public static final int INTERVAL = 10; //16 is 60 UPS, slower computers need it be lower to keep up

    private static long lastTime = 0;
    
    private Tower selectedTower;
    private Tower placingTower;
    
    public Field(JFrame f) {
        // creates border around the court area, JComponent method
        //setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // The timer is an object which triggers an action periodically with the given INTERVAL. We
        // register an ActionListener with this timer, whose actionPerformed() method is called each
        // time the timer triggers. We define a helper method called tick() that actually does
        // everything that should be done in a single timestep.

        this.frame  = f;
        this.money = 0;
        this.lives = 0;
        
        background = DataLoader.loadImage("files/Spinny.png");
        width = background.getWidth(null);
        height = background.getHeight(null);
        
        try {
			bloonPath = DataLoader.readPathData("spinny");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        ticker = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
    	bottomHUD = new JPanel();
    	rightHUD = new JPanel();
        
    	this.setLayout(new BorderLayout());
    	this.add(rightHUD, BorderLayout.EAST);
    	this.add(bottomHUD, BorderLayout.SOUTH);
    }

    private void place(MouseEvent e) {
    	if (placingTower != null && placingTower.valid()) {
    		towers.add(placingTower);
        	removeArea(placingTower);
        	if (selectedTower != null)
        		selectedTower.deselect();
        	selectedTower = placingTower;
    		placingTower = null;
    	}
    	else {
    		boolean found = false;
    		for (Tower t : towers) {
    			if (t.getBounds().contains(e.getPoint())) {
    				if (selectedTower != null) {
    					selectedTower.deselect();
    				}
    				t.select();
//    				Upgrade u = new Upgrade(ResourceManager.getInstance().getImage("stock_bloon"), "Poison", 400, "this is a thing");
//					bottomHUD.removeAll();
//    				bottomHUD.add(u.getComponent());
    				frame.validate();
    				//u.getComponent().paintImmediately(u.getComponent().getBounds());
    				selectedTower = t;
    				found = true;
    				break;
    			}
    			
    		}
    		if (!found) {
    			if (selectedTower != null) {
    				selectedTower.deselect();
					//bottomHUD.removeAll();
    			}
    			selectedTower = null;
    		}
    	}
    }
    
    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
    	ticker.stop();
    	moneyLabel = new JLabel();
    	livesLabel = new JLabel();

    	lives = 0;
    	money = 0;
    	inLevel = false;
    	
        changeMoney(500);
        changeLives(200);
        level = 0;
    	levelLabel = new JLabel();
        levelLabel.setText("Level " + level + " /50");
    	
    	bottomHUD = new JPanel();
    	rightHUD = new JPanel();
        
    	this.removeAll();
    	
    	this.setLayout(new BorderLayout());
    	this.add(rightHUD, BorderLayout.EAST);
    	this.add(bottomHUD, BorderLayout.SOUTH);

    	
        projectiles = new LinkedList<>();
        effects = new LinkedList<>();
        towers = new LinkedList<>();
        balloons = new LinkedList<>();
        

        res = ResourceManager.getInstance();
        
    	towerPriceLabel = new JLabel();
    	//price.setOpaque(false);
    	towerPriceLabel.setForeground(Color.YELLOW);
    	towerPriceLabel.setFont(towerPriceLabel.getFont().deriveFont(15F));
    	towerPriceLabel.setVisible(true);
    	//towerPriceLabel.se
    	


    	
    	moneyLabel.setFont(moneyLabel.getFont().deriveFont(30F));
    	livesLabel.setFont(livesLabel.getFont().deriveFont(30F));
    	moneyLabel.setForeground(Color.GREEN);
    	livesLabel.setForeground(Color.RED);
    	
    	rightHUD.add(moneyLabel);
    	rightHUD.add(livesLabel);
    	
    	
    	rightHUD.add(towerPriceLabel);
    	
    	bottomHUD.setBackground(new Color(153, 102, 51));
    	//bottomHUD.setLayout(null);
    	bottomHUD.setPreferredSize(new Dimension(width, 100));
    	rightHUD.setBackground(new Color(153, 102, 51));
    	rightHUD.setPreferredSize(new Dimension(200, height));
    	rightHUD.setLayout(new BoxLayout(rightHUD, BoxLayout.Y_AXIS));
    	rightHUD.setVisible(true);
    	bottomHUD.setVisible(true);
    	bottomHUD.setOpaque(true);
    	

    	
    	nextLevel = new JButton("Next Level");
    	nextLevel.setFont(nextLevel.getFont().deriveFont(30F));
    	levelLabel.setFont(levelLabel.getFont().deriveFont(50F));
    	nextLevel.addActionListener(new ActionListener() {
    		@Override
    		public void actionPerformed(ActionEvent e) {
    			if (playing())
    				nextLevel();
    			else
    				reset();
    		}    		
    	});
    	
    	bottomHUD.add(levelLabel);
    	bottomHUD.add(nextLevel);
    	

    	
    	JButton newMonkeyButton = new TowerButton(this, new DartMonkey(0, 0, balloons));
    	rightHUD.add(newMonkeyButton);
        
    	JButton newBombButton = new TowerButton(this, new BombTower(0, 0, balloons));
    	rightHUD.add(newBombButton);
    	
    	JButton newTackButton = new TowerButton(this, new TackShooter(0, 0, balloons));
    	rightHUD.add(newTackButton);
    	
    	JButton newIceButton = new TowerButton(this, new IceTower(0, 0, balloons));
    	rightHUD.add(newIceButton);
    	
    	JButton newSuperMonkeyButton = new TowerButton(this, new SuperMonkey(0, 0, balloons));
    	rightHUD.add(newSuperMonkeyButton);
    	
    	JButton newSpikeButton = new TowerButton(this, new Spikes(0, 0, balloons));
    	rightHUD.add(newSpikeButton);
    	
    	JButton newPineappleButton = new TowerButton(this, new Pineapple(0, 0, balloons));
    	rightHUD.add(newPineappleButton);
    	
		this.addMouseMotionListener(new MouseAdapter() {
			@Override
            public void mouseMoved(MouseEvent e) {
        		towerPriceLabel.setText("");
            	if (placingTower != null) {
            		placingTower.visible(true);
            		Area f = placingTower.footprint();
            		f.intersect(placementArea);
            		
            		if (!f.equals(placingTower.footprint()))
            			placingTower.invalidate();
            		else
            			placingTower.validate();
            		placingTower.move(e.getX(), e.getY());
            	}
			}
			public void mouseDragged(MouseEvent e) {
				place(e);
			}
        });
    	
		this.addMouseListener(new MouseAdapter() {
			@Override
            public void mouseClicked(MouseEvent e) {
					place(e);
			}
        });
		
		this.addKeyListener(new KeyAdapter() {
			@Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { 
                    placingTower = null;
                }
            	}
            });
    	



        setFocusable(true);

        
        placementArea = new Area(new Rectangle(0, 0, width, height));
        

        playing = true;
        inLevel = false;
        requestFocusInWindow();
        lastTime = System.currentTimeMillis();
        
        
        
        
        //Bloon[] bloons = Bloon.values();	
        //Random r = new Random();
        
        //Field screen = this;
        
//        Timer timer = new Timer(250, new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//            	//Random Bloon gen
//            	int n = r.nextInt(bloons.length);
//            	
//        	    Balloon b = new Balloon(bloons[n], bloonPath, screen);
//        	    balloons.add(b);
//        	    b.setPathPosition(0);
//                //System.gc();
//
//            }
//        });
//        timer.start();
     
        
        ticker.start();
        frame.validate();
    }

    /**
     * This method is called every time the timer defined in the constructor triggers.
     */
    void tick() {

    	List<GameObject> deaths = new LinkedList<>();
    	List<GameObject> temp = new LinkedList<>();
    	
    	int deltaT = (int)(System.currentTimeMillis() - lastTime);
    	//System.out.println(deltaT);
    	for (GameObject t : towers) {
    		//o.update(INTERVAL);
    		if (playing) {
	    		Collection<GameObject> newStuff = t.update(deltaT);
	    		if (newStuff != null)
	    			temp.addAll(newStuff);
    		}
    		//System.out.println(balloons);
    		if (!t.alive()) {
    			deaths.add(t);
    		}
    	}
    	
    	addNewGameObjects(temp);
    	
    	for (GameObject o : projectiles) {
    		//o.update(INTERVAL);
    		Collection<GameObject> newStuff = o.update(deltaT);
    		if (newStuff != null)
    			temp.addAll(newStuff);
    		//System.out.println(balloons);
    		if (!o.alive()) {
    			deaths.add(o);
    		}
    	}
    	
    	addNewGameObjects(temp);
    	
    	for (GameObject b : balloons) {
    		//o.update(INTERVAL);
    		Collection<GameObject> newStuff = b.update(deltaT);
    		if (newStuff != null)
    			temp.addAll(newStuff);
    		if (!b.alive()) {
    			deaths.add(b);
    		}
    	}
    	
    	addNewGameObjects(temp);
    	
    	for (GameObject o : effects) {
    		//o.update(INTERVAL);
    		Collection<GameObject> newStuff = o.update(deltaT);
    		if (newStuff != null)
    			temp.addAll(newStuff);
    		//System.out.println(balloons);
    		if (!o.alive()) {
    			deaths.add(o);
    		}
    	}
    	
    	addNewGameObjects(temp);
        
        for (GameObject o : deaths) {
        	if (o instanceof Projectile)
        		projectiles.remove(o);
        	else if (o instanceof Balloon) 
        		balloons.remove(o);
        	else if (o instanceof Tower) 
        		towers.remove(o);
        	else if (o instanceof Effect) 
        		effects.remove(o);
        }
        

        repaint();
        lastTime = System.currentTimeMillis();
        
        if (lives < 0) {
        	lives = 0;
        	changeLives(0);
        }
        if (lives == 0) {
        	playing = false;
        	inLevel = false;
        	levelLabel.setText("YOU LOSE");
        	nextLevel.setText("Replay");
        	bottomHUD = null;
        }
        
        nextLevel.setEnabled(!inLevel);
        
        if (inLevel && !bloonGenerator.isRunning() && balloons.isEmpty()) {
        	inLevel = false;
        	changeMoney(99 + level);
        	for (Tower t : towers) {
        		if (t instanceof Spikes)
        			t.flagForDeath();
        	}
        	
        	if (level == 50) {
            	playing = false;
            	inLevel = false;
        		levelLabel.setText("YOU WIN!!");
            	nextLevel.setText("Replay");
            	bottomHUD = null;
        	}
        }
        
            
        
    }  

    
    private void addNewGameObjects(Collection<GameObject> stuff) {
    	if (stuff != null) {
	        for (GameObject o : stuff) {
	        	if (o instanceof Projectile)
	        		projectiles.add((Projectile) o);
	        	else if (o instanceof Balloon) 
	        		balloons.add((Balloon) o);
	        	else if (o instanceof Tower) 
	        		towers.add((Tower) o);
	        	else if (o instanceof Effect) 
	        		effects.add((Effect) o);
	        }
	        stuff.clear();
    	}
    }
    

	@Override 
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
        g.drawImage(background, 0, 0, null);
    	for (Tower t : towers) {
    		t.draw(g);
    	}
    	for (Balloon b : balloons) {
    		b.draw(g);
    	}
    	for (Projectile p : projectiles) {
    		p.draw(g);
    	}
    	for (Effect e : effects) {
    		e.draw(g);
    	}
    	
    	if (placingTower != null)
    		placingTower.draw(g);
    	
//    	bottomHUD.repaint();
//    	rightHUD.repaint();
//    	if (bottomHUD != null && bottomHUD.getComponentCount() > 0)
//    		bottomHUD.getComponent(0).repaint();
    	//rightHUD.paintAll(g);    	
	}
    
	public void setPlacingTower(Tower t) {
		placingTower = t;
	}
	
	public JLabel getTowerPriceLabel() {
		return towerPriceLabel;
	}
	
	public void nextLevel() {
		level++;
		if (level > 50) {
			//this shouldn't happen
		}
		else {
			Field screen = this;
			Collection<Bloon> data;
			try {
				data = DataLoader.readLevelData(level);
	        	Iterator<Bloon> dataI = data.iterator();
				bloonGenerator = new Timer(250, new ActionListener() {
		            public void actionPerformed(ActionEvent e) {
		            	if (inLevel && dataI.hasNext()) {
		            		Bloon b = dataI.next();
			        	    Balloon ball = new Balloon(b, bloonPath, screen);
			        	    balloons.add(ball);
			        	    ball.setPathPosition(0);
		            	}
		            	else {
		            		bloonGenerator.stop();
		            	}
		
		            }
		        });
				bloonGenerator.start();
				inLevel = true;
				levelLabel.setText("Level " + level + "/50");
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}
		
	}
    
    private void removeArea(Tower t) {
    	placementArea.subtract(t.footprint());
    }
    
    public void changeLives(int delta) {
    	lives += delta;
    	livesLabel.setText(lives + " Lives");
    }
    
    public void changeMoney(int delta) {
    	money += delta;
    	moneyLabel.setText("$" + money);
    }
    
    public int getMoney() {
    	return money;
    }
    
    public boolean playing() {
    	return playing;
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width + 200, height + 100);
    }
}