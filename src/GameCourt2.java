/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
public class GameCourt2 extends JPanel {

	
	private int lives = 100;
	private int money = 1000;
	private List<Projectile> projectiles;
	private Set<Balloon> balloons;
	private List<Collection> gameObjects;
	private ResourceManager res;
	private Point[] bloonPath;

    public boolean playing = false; // whether the game is running 
    private Image background;
    long startTime;
    
    // Game constants
    public static int width = 300; //default values 
    public static int height = 300;
    public static final int SQUARE_VELOCITY = 4;

    // Update interval for timer, in milliseconds
    
    
    public static final int INTERVAL = 16;

    private static long lastTime = 0;
    private long elapsedTime;
    private long numLoops;
    
    public GameCourt2() {
        // creates border around the court area, JComponent method
        //setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // The timer is an object which triggers an action periodically with the given INTERVAL. We
        // register an ActionListener with this timer, whose actionPerformed() method is called each
        // time the timer triggers. We define a helper method called tick() that actually does
        // everything that should be done in a single timestep.

        
        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start(); // MAKE SURE TO START THE TIMER!

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // This key listener allows the square to move as long as an arrow key is pressed, by
        // changing the square's velocity accordingly. (The tick method below actually moves the
        // square.)
//        addKeyListener(new KeyAdapter() {
//            public void keyPressed(KeyEvent e) {
//                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
//                    square.setVx(-SQUARE_VELOCITY);
//                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
//                    square.setVx(SQUARE_VELOCITY);
//                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
//                    square.setVy(SQUARE_VELOCITY);
//                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
//                    square.setVy(-SQUARE_VELOCITY);
//                }
//            }
//
//            public void keyReleased(KeyEvent e) {
//                square.setVx(0);
//                square.setVy(0);
//            }
//        });
        
        Image img = null;
        
        try {
        	img = ImageIO.read(new File("files/Spinny.png"));
            background = img;
            width = background.getWidth(null);
            height = background.getHeight(null);
        } 
        catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
        
        
        
        try {
        	img = ImageIO.read(new File("files/Spinny.png"));
            background = img;
            width = background.getWidth(null);
            height = background.getHeight(null);
        } 
        catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
        
        try {
			bloonPath = DataLoader.readPathData("spinny");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        gameObjects = new LinkedList<>();
        projectiles = new LinkedList<>();
        balloons = new TreeSet<>();
        
        
        gameObjects.add(projectiles);
        gameObjects.add(balloons);
        
        
        BufferedImage dartImage = DataLoader.loadImage(Projectile.DART);
        dartImage = ResourceManager.resizeImage(dartImage, 40, 10);

        Projectile dart = new TargetedProjectile(dartImage, 100, 100, 160, 200, 200);
        
        projectiles.add(dart);
    
        res = new ResourceManager();

        
        BufferedImage ig = res.getImage("stock_bloon");
        try {

            File f = new File("files/stock.png");
            //f.createNewFile();
			ImageIO.write(ig, "png", f);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {

    	
        playing = true;
        //status.setText("Running...");

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
        lastTime = System.currentTimeMillis();
        elapsedTime = 0;
        numLoops = 0;

        
//        Timer timer = new Timer(1000, new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//            	System.out.println("tick");
//                int hp = (int) (Math.random() * 5 + 1);
//
//        			//Image img = DataLoader.loadImage(Projectile.DART).getScaledInstance(40, 10, 0);
//                BufferedImage img = res.getImage("stock_bloon");
//                System.out.println(img);
//        	    Balloon b = new Balloon(img, 100, 100, hp, bloonPath);
//        	    balloons.add(b);
//
//            }
//        });
//        timer.start();
        
    }

    /**
     * This method is called every time the timer defined in the constructor triggers.
     */
    void tick() {
        if (playing) {
//            // advance the square and snitch in their current direction.
//            square.move();
//            snitch.move();
//
//            // make the snitch bounce off walls...
//            snitch.bounce(snitch.hitWall());
//            // ...and the mushroom
//            snitch.bounce(snitch.hitObj(poison));
//
//            // check for the game end conditions
//            if (square.intersects(poison)) {
//                playing = false;
//                //status.setText("You lose!");
//            } else if (square.intersects(snitch)) {
//                playing = false;
//                //status.setText("You win!");
//            }
            
        	if (numLoops % 60 == 0) {
            	System.out.println("tick");
              int hp = (int) (Math.random() * 5 + 1);

      			//Image img = DataLoader.loadImage(Projectile.DART).getScaledInstance(40, 10, 0);
              BufferedImage img = res.getImage("stock_bloon");

      	    Balloon b = new Balloon(img, 100, 100, hp, bloonPath);
            System.out.println(b);
      	    System.out.println(balloons.add(b));
        	}
        		
        	
        	List<GameObject> deaths = new LinkedList<>();
        	
//            for (Collection<GameObject> li  : gameObjects) {
//            	for (GameObject o : li) {
//	            	//o.update(INTERVAL);
//            		o.update((int)(System.currentTimeMillis() - lastTime));
//            		//System.out.println(balloons);
//	            	if (!o.alive()) {
//	            		deaths.add(o);
//	            		}
//            	}
//            }
        
        	
        	int deltaT = (int)(System.currentTimeMillis() - lastTime);
        	
        	for (GameObject o : projectiles) {
        		//o.update(INTERVAL);
        		o.update(deltaT);
        		//System.out.println(balloons);
        		if (!o.alive()) {
        			deaths.add(o);
        		}
        	}
        	
        	
        	for (GameObject o : balloons) {
        		//o.update(INTERVAL);
        		o.update(deltaT);
        		//System.out.println(balloons);
        		if (!o.alive()) {
        			deaths.add(o);
        		}
        	}
        	
            
            for (GameObject o : deaths) {
            	if (o instanceof Projectile)
            		projectiles.remove(o);
            	else if (o instanceof Balloon)
            		balloons.remove(o);
            }

            
            // update the display
            paintImmediately(getBounds());
            elapsedTime += deltaT;
            numLoops++;
            lastTime = System.currentTimeMillis();
            //System.out.println(System.currentTimeMillis() - startTime);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);
        for (Collection<GameObject> li  : gameObjects) {
        	for (GameObject o : li) {
        		o.draw(g);
        	}
        }
    }
         

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }
}