/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for local variables.

        // Top-level frame in which game components live
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("Bloons TDJ");
        frame.setResizable(false);
        frame.setLocation(800, 350);
        
        Field in_game = new Field(frame);
        //in_game.setPreferredSize(new Dimension(800, 800));
        
        JPanel screen = new JPanel();
        screen.setPreferredSize(in_game.getPreferredSize());
        
        JPanel main_menu = new JPanel();
        main_menu.setPreferredSize(in_game.getPreferredSize());
        
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        
        JLabel bloons = new JLabel("Bloons TDJ");
        bloons.setFont(bloons.getFont().deriveFont(50F));
        Dimension d = main_menu.getSize();
        //header.setPreferredSize(new Dimension(d.width, 100));
        
        JLabel owen = new JLabel("Created by Owen Simon");
        owen.setFont(owen.getFont().deriveFont(20F));
        //owen.setPreferredSize(new Dimension(d.width, d.height * 4 / 8));
        
        JPanel instructions = new JPanel();
        instructions.setPreferredSize(in_game.getPreferredSize());

        header.add(bloons);
        header.add(owen);
        
        main_menu.add(header);
        
        
        
        final JButton main_button = new JButton("Main Menu");
        main_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	changeScreen(frame, screen, main_menu);
            }
        });

        
        
        final JButton instruction_button = new JButton("Instructions");
        instruction_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	changeScreen(frame, screen, instructions);
            }
        });
        main_menu.add(instruction_button);
        
        final JButton play_button;
        
        
    	play_button = new JButton("Play");
        
        play_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	changeScreen(frame, screen, in_game);
            	in_game.reset(0, 500, 200);
                //PathTraceUtility tracer = new PathTraceUtility(screen, "spinny");
                //tracer.init();
            }
        });
        main_menu.add(play_button);
        
        String mainText = "Welcome to Bloons TDJ! The objective of the game is to survive as "
        		+ "long as you can (up to a maximum of 50 increasingly-difficult levels) against "
        		+ "a horde of bloons trying to travel across the map by building towers. Bloons start at the top of "
        		+ "the map and follow the track. You start with $500 and 200 lives. Each tower has "
        		+ "a different price, and can be purchased on the right side of the screen by "
        		+ "clicking on the desired tower and then clicking again on where you want to place "
        		+ "the tower. You can click on another tower on the right side of the screen to "
        		+ "switch the type of tower you are placing, click again on the type you are "
        		+ "purchasing to cancel, or press the Escape key to cancel placement. Placed towers "
        		+ "will attack any bloons that enter their vision radius. Click on a tower to see "
        		+ "its vision radius as well as gain the option to sell back that tower for 75% of "
        		+ "its price. You cannot place towers on top of other towers, but you can place them "
        		+ "anywhere else on the map. Each layer of a bloon you pop grants $1, and you will "
        		+ "receive some money at the end of each level. When you pop a layer of a bloon, the "
        		+ "next layers will be unleased and you will have to stop them too. If a bloon "
        		+ "breaks through your defenses, you will loaw lives equal to its Red Bloon Equivalent "
        		+ " (RBE). RBE is the equivalent amount of red bloons that you would need to pop "
        		+ "in order to equal the number of pops you need to fully pop the bloon.";

        String dm = "Dart Monkeys: The cheapest, most basic tower. Dart Monkeys throw sharp"
        		+ " darts that can pop up to 2 consecutive bloons. They have medium attack range and "
        		+ "attack speed.\n\n";
        String ts = "Tack Shooters: A nice specialized upgrade from Dart Monkeys. Tack shooters"
        		+ " spray out 8 sharp tacks in the 8 cardinal directions. They have low attack range and"
        		+ " medium attack speed.\n\n";
        String it = "Ice Towers: Ice Towers are critical support towers. They don't deal damage, but "
        		+ "rather they freeze any bloons that they can see for a brief time. Be warned, sharp "
        		+ "objects can't pop frozen bloons, but explosions can. They have low attack range "
        		+ "and low attack speed. Watch out for White Bloons!\n\n";
        String bt = "Bomb Towers: A heavy-hitting bloon destroyer. Bomb Towers shoot bombs that "
        		+ "explode on impact on pop any bloons caught in the blast. They have large attack "
        		+ "range and medium attack speed. Watch out for Black Bloons.\n\n";
        String sm = "Super Monkeys: The priciest, most advanced tower. These absolute monsters "
        		+ "throw out darts at super speed. They have extreme attack range and attack speed.\n\n";
        String rs = "Road Spikes: A handy stop-gap solution for straggling bloons. They will pop "
        		+ "any bloons that they contact, up to 10 bloons. Road Spikes disappear at the end of "
        		+ "each level. Beware, these cost more money than they yield from popping! Use them "
        		+ "to stop straggling bloons that get through your defenses.\n\n";
        String ep = "Exploding Pineapples: A pineapple that will explode 3 seconds after placement. "
        		+ "These are useful if you need an explosion, but don't have any Bomb Towers.\n\n";
        
        
        
        String bb = "Basic Bloons: These bloons come in 5 tiers - Red, Blue, Green, Yellow, and "
        		+ "Pink. Each tier yields the directly lower tier when popped (Red Bloons"
        		+ " just disappear when popped). Each successive tier is larger and faster than the"
        		+ "previous.\n\n";
        
        String white = "White Bloons: Tiny, medium-speed bloons that are immune to freezing. "
        		+ "When popped, they yield two Pink Bloons.\n\n";
        
        String black = "Black Bloons: Tiny, medium-speed bloons that are immune to explosions. "
        		+ "When popped, they yield two Pink Bloons. NOTE: they appear gray.\n\n";
        
        String lead = "Lead Bloons: Large, slow bloons that are immune to sharp objects. Pop them "
        		+ "with explosions! When popped, they yield two Black Bloons.\n\n";
        
        String zebra = "Zebra Bloons: Tiny, fast bloons that are both White and Black, so they are "
        		+ "immune to both freezing and explosions. When popped, they yield a White Bloon and"
        		+ "a Black Bloon.\n\n";
        
        String rainbow = "Rainbow Bloons: Large, fast bloons. When popped, they yield two Zebra "
        		+ "Bloons. Watch out, Rainbow Bloons can be devestating if they manage to break "
        		+ "through your defenses.\n\n";

        String ceramic = "Ceramic Bloons: Extremely dangerous, large, medium-speed bloons. "
        		+ "Beware, they have an outer ceramic shell that takes 10 hits to pop. When popped, "
        		+ "they yield two Rainbow Bloons. Note that Ceramic Bloons have an RBE of 104, so "
        		+ "a mere two full-health ceramic bloons penetrating your defenses will result in "
        		+ "defeat.\n\n";
        
        JTextArea main = new JTextArea(mainText + "\n\n\n\n" + dm + ts + it + bt + sm + rs + ep + 
        		"\n\n\n\n" + bb + white + black + lead + zebra + rainbow + ceramic);
        
        
        main.setOpaque(false);
        main.setWrapStyleWord(true);
        main.setLineWrap(true);
        main.setFont(main.getFont().deriveFont(15F));
        d = instructions.getPreferredSize();
        main.setPreferredSize(new Dimension(d.width * 7 / 8, d.height * 7 / 8));
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(main_button);
        buttonPanel.add(play_button);
        buttonPanel.setPreferredSize(new Dimension(200, 100));
       
        instructions.add(main);
        instructions.add(buttonPanel);
        
        screen.add(main_menu);
        
        frame.add(screen);
        
        

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
    
    
    public static void changeScreen(JFrame frame, JPanel screen, JPanel news) {
    	screen.removeAll();
        screen.add(news);
        frame.pack();
        frame.setVisible(true);
        screen.repaint();
    }

    
    
    /**
     * Main method run to start and run the game. Initializes the GUI elements specified in Game and
     * runs it. IMPORTANT: Do NOT delete! You MUST include this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}