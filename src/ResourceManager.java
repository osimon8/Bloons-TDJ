import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import javax.xml.stream.XMLStreamException;


public class ResourceManager {

	
	private Map<String, int[]> sprites;
	private BufferedImage inGame;
	
	public ResourceManager() {
		
		try {
			sprites = DataLoader.parseXML("files/InGame.xml");
			inGame = DataLoader.loadImage("files/InGame.png");
		} catch (IOException | XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// TODO Auto-generated constructor stub
	}
	
	
	public int[] getSprite(String name) {
		return sprites.get(name);
	}
	
	public BufferedImage getImage(String name) {
		
		
		int[] data = getSprite(name);

		BufferedImage img = inGame.getSubimage(data[0], data[1], data[2], data[3]);

		//RED: 207, 0, 0
		//BLUE: 44, 147, 215
		//GREEN: 120, 182, 0
		//YELLOW: 255, 226, 0
		//PINK: 255, 96, 111
		//BLACK: 25, 25, 25
		
		img = tint(img, new Color(50, 220, 255));
		
		//use this for creating new image w/o reference to whole sprite sheet
//		BufferedImage img = image.getSubimage(startX, startY, endX, endY); //fill in the corners of the desired crop location here
//		BufferedImage copyOfImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
//		Graphics g = copyOfImage.createGraphics();
//		g.drawImage(img, 0, 0, null);
		
		
		return img;
		
	}


	public static BufferedImage tint(BufferedImage image, Color color) {
		BufferedImage copy = copy(image);
		
	    for (int x = 0; x < copy.getWidth(); x++) {
	        for (int y = 0; y < copy.getHeight(); y++) {
	            Color pixelColor = new Color(copy.getRGB(x, y), true);
	            int r = (pixelColor.getRed() + color.getRed()) / 2;
	            int g = (pixelColor.getGreen() + color.getGreen()) / 2;
	            int b = (pixelColor.getBlue() + color.getBlue()) / 2;
	            int a = pixelColor.getAlpha();
	            int rgba = (a << 24) | (r << 16) | (g << 8) | b; //not sure exactly how this works, shifts over bits to form integer representing argb values
	            copy.setRGB(x, y, rgba);
	        }
	    }
	    
	    return copy;
	}
	
	public static BufferedImage copy(Image img) {
		BufferedImage copy = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics gc = copy.createGraphics();
		gc.drawImage(img, 0, 0, null);
		return copy;
	}
	
	
	public static BufferedImage resizeImage(BufferedImage original, int newWidth, int newHeight) {
		
		
		BufferedImage resized = new BufferedImage(newWidth, newHeight, original.getType());
		Graphics2D g = resized.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(original, 0, 0, newWidth, newHeight, 0, 0, original.getWidth(),
		    original.getHeight(), null);
		g.dispose();

		return resized;
		
	}
	
	public static BufferedImage scaleImage(BufferedImage original, double widthFactor, double heightFactor) {
		
		int newWidth = new Double(original.getWidth() * widthFactor).intValue();
		int newHeight = new Double(original.getHeight() * heightFactor).intValue();

		return resizeImage(original, newWidth, newHeight);
		
	}
	
	
}



