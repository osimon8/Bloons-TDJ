import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;


public class Upgrade {

	//NOT IMPLEMENTING
	
	
	private BufferedImage icon;
	private int cost;
	private String description;
	private String name;
	private JComponent comp;
	
	
	public enum Tier{L1, L2, R1, R2};
	
	public Upgrade(BufferedImage img, String name, int cost, String description) {
		this.cost = cost;
		this.icon = img;
		this.description = description;
		this.name = name;
		comp = setComponent();
	}
	
	
	
	public BufferedImage getIcon() {
		return icon;
	}

	public void setIcon(BufferedImage icon) {
		this.icon = icon;
	}


	public int getCost() {
		return cost;
	}


	public void setCost(int cost) {
		this.cost = cost;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public void draw(Graphics g) {
		g.drawImage(icon, 0, 0, null);
	}
	
	private JComponent setComponent() {
		@SuppressWarnings("serial")
		JPanel j  = (new JPanel() {
			@Override 
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.draw3DRect(0, 0, icon.getWidth() + 100 - 1, icon.getHeight() + 25 - 1, false);
				g.setColor(Color.YELLOW);
//				g.setFont(new Font("TimesRoman", Font.PLAIN, 20)); 
				g.setFont(g.getFont().deriveFont(20F));
				g.drawString(name + ": $" + cost, 0,20);
				g.drawImage(icon, 0, 25, null);
			}
			
			@Override 
			public Dimension getPreferredSize() {
				return new Dimension(icon.getWidth() + 100, 25 + icon.getHeight());
			}
			
		});
		j.setToolTipText(description);
		j.setOpaque(false);
		j.setVisible(true);
		//j.setPreferredSize(new Dimension(icon.getWidth(), icon.getHeight()));
		return j;
	}
	
	public JComponent getComponent() {
		return comp;
	}
	
	
}
