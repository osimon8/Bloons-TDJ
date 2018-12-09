import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class TowerButton extends JButton {

	Tower tower;
	
	public TowerButton(Field f, Tower t){
		super(new ImageIcon(t.getImage()));
		tower = t;
    	this.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            		Tower placingTower = (Tower) tower.clone();
	            	f.setPlacingTower(placingTower);
	            	placingTower.visible(false);
	            	placingTower.select();
	            	f.requestFocus();
            }
        });
		
    	this.addMouseMotionListener(new MouseAdapter() {
    		
    		@Override
    		public void mouseMoved(MouseEvent e) {
    			f.setTowerPriceLabel(t.getName() + ": $" + tower.getPrice());
    		}
    		
    	});
    
    	this.setOpaque(false);
        this.setContentAreaFilled(false);
        //this.setBorderPainted(false);
	}
	
	
//	@Override
//	protected void paintComponent(Graphics g) {
//		g.drawImage(tower.getImage(), 0, 0, null);
//		
//		
//	}
//	
//	@Override
//	public Dimension getPreferredSize() {
//		return new Dimension(tower.getImage().getWidth(), tower.getImage().getHeight());
//	}
//	
	
}
