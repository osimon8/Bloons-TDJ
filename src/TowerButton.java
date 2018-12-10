import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

public class TowerButton extends JButton {

	Tower tower;
	
	public TowerButton(Field f, Tower t){
		super(new ImageIcon(t.getImage()));
		tower = t;
    	this.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	//!t.equals(f.getPlacing()) &&
            		if ( f.playing() && f.getMoney() >= tower.getPrice()) {
                		Tower placingTower = (Tower) tower.clone();
    	            	f.setPlacingTower(placingTower);
    	            	placingTower.visible(false);
    	            	placingTower.select();
    	            	f.requestFocus();
            		}

            }
        });
		
    	this.addMouseMotionListener(new MouseAdapter() {
    		
    		@Override
    		public void mouseMoved(MouseEvent e) {
    			JLabel lab = f.getTowerPriceLabel();
    			if (f.getMoney() >= tower.getPrice())
    				lab.setForeground(Color.YELLOW);
    			else 
    				lab.setForeground(new Color(255, 102, 102));
    			lab.setText(t.getName() + ": $" + tower.getPrice());
    		}
    		
    	});
    
    	this.setOpaque(false);
        this.setContentAreaFilled(false);
	}
	
}
