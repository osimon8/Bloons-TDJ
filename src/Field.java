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
import java.util.Random;

import javax.swing.*;


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
	private Point[] bloonPath;
	private Area placementArea;
	private JPanel bottomHUD;
	private JPanel rightHUD;
	private JFrame frame;
	private JLabel towerPriceLabel;
	private JLabel levelLabel;
	private JLabel livesLabel;
	private JLabel moneyLabel;
	private JButton nextLevel;
	private JButton sellButton;
	private Timer bloonGenerator;
	private Timer ticker;

    public boolean playing = false; // whether the game is running 
    private Image background;
    long startTime;
    
    public static int width;
    public static int height;

    
    
    public static final int INTERVAL = 10; //16 is 60 UPS, slower computers need it be lower to keep up

    private static long lastTime = 0;
    
    private Tower selectedTower;
    private Tower placingTower;
    
    public Field(JFrame f) {
        this.frame  = f;
        this.money = 0;
        this.lives = 0;
        
        background = DataLoader.loadImage("files/Spinny.png");
        width = background.getWidth(null);
        height = background.getHeight(null);
        //load background image and set size 
        
        try {
			bloonPath = DataLoader.readPathData("spinny"); //load in path data
		} catch (IOException e1) {
			e1.printStackTrace(); //This should never happen, if it does, the game can't be played
		}

        ticker = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        //initialize update loop
        
    	bottomHUD = new JPanel();
    	rightHUD = new JPanel();
        
    	this.setLayout(new BorderLayout());
    	this.add(rightHUD, BorderLayout.EAST);
    	this.add(bottomHUD, BorderLayout.SOUTH);
    	
    	//add in HUDs
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(screenSize.width / 2 - width / 2, screenSize.height / 2 - height / 2);
    }

    private void place(MouseEvent e) {
    	if (placingTower != null && placingTower.valid()) {
    		towers.add(placingTower);
        	removeArea(placingTower);
        	if (selectedTower != null)
        		selectedTower.deselect();
        	selectedTower = placingTower;
        	changeMoney(-placingTower.getPrice());
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
    				frame.validate();
    				selectedTower = t;
    				found = true;
    				break;
    			}
    			
    		}
    		if (!found) {
    			if (selectedTower != null) {
    				selectedTower.deselect();
    			}
    			selectedTower = null;
    		}
    	}
    }
    
    /**
     * (Re-)set the game to its initial state.
     */
    public void reset(int lvl, int mon, int lves) {

    	//center on screen
        
        
    	ticker.stop(); //stop update loop
    	
    	
    	moneyLabel = new JLabel();
    	livesLabel = new JLabel();

    	lives = 0;
    	money = 0;
    	inLevel = false;
    	
        changeMoney(mon);
        changeLives(lves);
        level = lvl;
    	levelLabel = new JLabel();
        levelLabel.setText("Level " + level + "/50");
    	
    	bottomHUD = new JPanel();
    	rightHUD = new JPanel();
        
    	this.removeAll();
    	
    	this.setLayout(new BorderLayout());
    	this.add(rightHUD, BorderLayout.EAST);
    	this.add(bottomHUD, BorderLayout.SOUTH); //set up layout and labels and stuff

    	
        projectiles = new LinkedList<>();
        effects = new LinkedList<>();
        towers = new LinkedList<>();
        balloons = new LinkedList<>(); //initialize collections of GameObjects
 
        
    	towerPriceLabel = new JLabel();
    	towerPriceLabel.setForeground(Color.YELLOW);
    	towerPriceLabel.setFont(towerPriceLabel.getFont().deriveFont(15F));
    	towerPriceLabel.setVisible(true);

    	
    	moneyLabel.setFont(moneyLabel.getFont().deriveFont(30F));
    	livesLabel.setFont(livesLabel.getFont().deriveFont(30F));
    	moneyLabel.setForeground(Color.GREEN);
    	livesLabel.setForeground(Color.RED);
    	
    	rightHUD.add(moneyLabel);
    	rightHUD.add(livesLabel);
    	
    	
    	rightHUD.add(towerPriceLabel);
    	
    	bottomHUD.setBackground(new Color(153, 102, 51));
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
    	nextLevel.addActionListener(e -> {
    			if (playing())
    				nextLevel();
    			else
    				reset(0, 500, 200);  		
    	});
    	
    	bottomHUD.add(levelLabel);
    	bottomHUD.add(nextLevel);
    	
    	//more labels and button setup and addition to HUDs
    	
    	JButton newMonkeyButton = new TowerButton(this, new DartMonkey(0, 0, balloons));
    	rightHUD.add(newMonkeyButton);
    	
    	JButton newTackButton = new TowerButton(this, new TackShooter(0, 0, balloons));
    	rightHUD.add(newTackButton);
    	
    	JButton newIceButton = new TowerButton(this, new IceTower(0, 0, balloons));
    	rightHUD.add(newIceButton);
    	
    	JButton newBombButton = new TowerButton(this, new BombTower(0, 0, balloons));
    	rightHUD.add(newBombButton);
    	
    	JButton newSuperMonkeyButton = new TowerButton(this, new SuperMonkey(0, 0, balloons));
    	rightHUD.add(newSuperMonkeyButton);
    	
    	JButton newSpikeButton = new TowerButton(this, new Spikes(0, 0, balloons));
    	rightHUD.add(newSpikeButton);
    	
    	JButton newPineappleButton = new TowerButton(this, new Pineapple(0, 0, balloons));
    	rightHUD.add(newPineappleButton);
    	
    	
    	sellButton = new JButton("Sell");
    	sellButton.setFont(sellButton.getFont().deriveFont(15F));
    	sellButton.addActionListener(e -> {
    		selectedTower.flagForDeath();
    		changeMoney((int)(selectedTower.getPrice() * 0.75));
    		selectedTower = null;
    	});

    	rightHUD.add(sellButton);
    	
    	
    	JButton saveButton = new JButton("Save");
    	saveButton.setFont(saveButton.getFont().deriveFont(15F));
    	saveButton.addActionListener(e -> {
    			try {
					DataLoader.saveData(this);
				} catch (IOException e1) {
					System.out.println("Data failed to save");
					e1.printStackTrace();
				}
    	});
    	
    	rightHUD.add(saveButton);
    	
    	
    	JButton loadButton = new JButton("Load");
    	loadButton.setFont(loadButton.getFont().deriveFont(15F));
    	loadButton.addActionListener(e -> {
    			try {
					DataLoader.loadData(this);
				} catch (IOException e1) {
					System.out.println("Data failed to load");
					e1.printStackTrace();
				}
    	});
    	
    	rightHUD.add(loadButton);
    	
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
        
        //genRandomBloons();
        
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
        	else if (o instanceof Tower) { 
        		addArea((Tower)o);
        		towers.remove(o);
        	}
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
        
        if(selectedTower != null && !(selectedTower instanceof Spikes) &&
        		!(selectedTower instanceof Pineapple)) {
        	sellButton.setVisible(true);
        	sellButton.setText("Sell: $" + (int)(0.75 * selectedTower.getPrice()));
        }
        else {
        	sellButton.setVisible(false);
        }
        
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
    
    private void addArea(Tower t) {
    	placementArea.add(t.footprint());
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
    
    public int getLives() {
    	return lives;
    }
    
    public int getLevel() {
    	return level;
    }
    
    public boolean inLevel() {
    	return inLevel;
    }
    
    public boolean playing() {
    	return playing;
    }
    
    public Tower getSelected() {
    	return selectedTower;
    }
    
    public Tower getPlacing() {
    	return placingTower;
    }
    
    public Collection<Tower> getTowers(){
    	return towers;
    }
    
    public Collection<Balloon> getBalloons(){
    	return balloons;
    }
    
    public void setTowers(List<Tower> twrs) {
    	towers = twrs;
    }
    
    @SuppressWarnings("unused")
	private void genRandomBloons() {
        
        Bloon[] bloons = Bloon.values();	
        Random r = new Random();
        
        Field screen = this;
        
        Timer timer = new Timer(250, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	//Random Bloon gen
            	int n = r.nextInt(bloons.length);
            	
        	    Balloon b = new Balloon(bloons[n], bloonPath, screen);
        	    balloons.add(b);
        	    b.setPathPosition(0);

            }
        });
        timer.start();
     
    	
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width + 200, height + 100);
    }
}