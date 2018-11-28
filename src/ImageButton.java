import java.awt.Graphics;
import java.awt.Image;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

public class ImageButton extends JButton {

	private Image img;
	
	public ImageButton(Image img) {
		super("");
		this.img = img;
	}

	public ImageButton(Icon arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public void paint(Graphics g) {
		//this.getIcon().paintIcon(this, g, 0, 0);
		g.drawImage(img, 0, 0, null);
		//this.setBackground();
	}

}
