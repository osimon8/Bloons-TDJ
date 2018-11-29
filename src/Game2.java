/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.stream.XMLStreamException;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game2 implements Runnable {
    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for local variables.

        // Top-level frame in which game components live
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("Bloons TDJ");
        //frame.setResizable(false);
        frame.setLocation(800, 350);
        
        JPanel screen = new JPanel();
        
        JPanel main_menu = new JPanel();
        main_menu.setPreferredSize(new Dimension(300, 300));
        
        
        
        JPanel instructions = new JPanel();
        instructions.setPreferredSize(new Dimension(300, 300));

        GameCourt2 in_game = new GameCourt2();
        //in_game.setPreferredSize(new Dimension(800, 800));
        
        
        final JButton main_button = new JButton("Main Menu");
        main_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	changeScreen(frame, screen, main_menu);
            }
        });
        instructions.add(main_button);
        
        
        final JButton instruction_button = new JButton("Instructions");
        instruction_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	changeScreen(frame, screen, instructions);
            }
        });
        main_menu.add(instruction_button);
        
        final JButton play_button;
        
        Image img = loadImage("files/poison.png");
        if (img != null) {
            img = img.getScaledInstance(100, 100, 0);
            Icon i = new ImageIcon(img);
            play_button = new JButton(i);
            play_button.setOpaque(false);
            play_button.setContentAreaFilled(false);
            play_button.setBorderPainted(false);
        }
        else {
        	play_button = new JButton("Play");
        }
        
        play_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	changeScreen(frame, screen, in_game);
            	in_game.reset();
                //PathTraceUtility tracer = new PathTraceUtility(screen, "spinny");
                //tracer.init();
            }
        });
        main_menu.add(play_button);
        instructions.add(play_button);
       
        
        screen.add(main_menu);
        
        frame.add(screen);
        
        
        // Status panel
//        final JPanel status_panel = new JPanel();
//        frame.add(status_panel, BorderLayout.SOUTH);
//        final JLabel status = new JLabel("Running...");
//        status_panel.add(status);

        // Main playing area
//        final GameCourt court = new GameCourt(status);
//        frame.add(court, BorderLayout.CENTER);

        // Reset button
//        final JPanel control_panel = new JPanel();
//        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we define it as an
        // anonymous inner class that is an instance of ActionListener with its actionPerformed()
        // method overridden. When the button is pressed, actionPerformed() will be called.
//        final JButton reset = new JButton("Reset");
//        reset.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//               // court.reset();
//            	changeScreen(screen, main_menu);
//            }
//        });
//        control_panel.add(reset);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        //in_game.reset();
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
        SwingUtilities.invokeLater(new Game2());
    }
}