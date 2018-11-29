/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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

    private Square square; // the Black Square, keyboard control
    private Circle snitch; // the Golden Snitch, bounces
    private Poison poison; // the Poison Mushroom, doesn't move
	
	private int lives = 100;
	private int money = 1000;
	private List<Projectile> projectiles;
	private List<Balloon> balloons;
	private List<List> gameObjects;
	private ResourceManager res;

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
    
    public GameCourt2() {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

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
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    square.setVx(-SQUARE_VELOCITY);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    square.setVx(SQUARE_VELOCITY);
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    square.setVy(SQUARE_VELOCITY);
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    square.setVy(-SQUARE_VELOCITY);
                }
            }

            public void keyReleased(KeyEvent e) {
                square.setVx(0);
                square.setVy(0);
            }
        });
        
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
        
        gameObjects = new LinkedList<>();
        projectiles = new LinkedList<>();
        balloons = new LinkedList<>();
        
        
        gameObjects.add(projectiles);
        gameObjects.add(balloons);
        

        
        Projectile dart = new TargetedProjectile(DataLoader.loadImage(Projectile.DART).getScaledInstance(40, 10, 0), 100, 100, 160, 200, 200);
        
        projectiles.add(dart);
    
        res = new ResourceManager();



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
        
        
        
        try {
			Point[] path = DataLoader.readPathData("spinny");
			//Image img = DataLoader.loadImage(Projectile.DART).getScaledInstance(40, 10, 0);
			Image img = res.getImage("stock_bloon");
			
			
	        Balloon b = new Balloon(img, 100, 100, 5, path);
	        balloons.add(b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("file isn't here fam");
		}
        
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
            
        	List<GameObject> deaths = new LinkedList<>();
        	
            for (Collection<GameObject> li  : gameObjects) {
            	for (GameObject o : li) {
	            	//o.update(INTERVAL);
            		o.update((int)(System.currentTimeMillis() - lastTime));
	            	if (!o.alive()) {
	            		deaths.add(o);
	            		}
            	}
            }
            
            for (GameObject o : deaths) {
            	projectiles.remove(o);
            	balloons.remove(o);
            }

            
            // update the display
            repaint();
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