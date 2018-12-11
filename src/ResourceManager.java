import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;


public class ResourceManager {

	
	private Map<String, int[]> sprites;
	private Map<String, List<String>> animations;
 	private BufferedImage inGame;
	private static ResourceManager instance;
	
	@SuppressWarnings("unchecked")
	private ResourceManager() {
		
		try {
			Object[] data = DataLoader.parseXML("files/InGame.xml");
			sprites = (Map<String, int[]>) data[0];
			animations = (Map<String, List<String>>) data[1];
			inGame = DataLoader.loadImage("files/InGame.png");
		} catch (IOException | XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// TODO Auto-generated constructor stub
	}
	
	
    static {
        instance = new ResourceManager();
    }
    
    public static ResourceManager getInstance() {
    	return instance;
    }
	
	public int[] getSprite(String name) {
		return sprites.get(name);
	}
	
	public BufferedImage getImage(String name) {
		
		
		int[] data = getSprite(name);
		
		if (data == null)
			throw new IllegalArgumentException("Error - Sprite name \"" +  name + "\" not found");

		BufferedImage img = inGame.getSubimage((int) (DataLoader.SCALE * data[0]), 
				(int) (DataLoader.SCALE * data[1]), (int) (DataLoader.SCALE * data[2]), 
				(int) (DataLoader.SCALE * data[3]));
		
		
		return img;
		
	}
	
	public List<BufferedImage> getAnimation(String name) {
		
		List<String> cells = animations.get(name);
		List<BufferedImage> anim = new LinkedList<>();
		//List<img>
		
		if (cells == null)
			throw new IllegalArgumentException("Error - Animation name \"" + name + "\" not found");
		
		for (String cell : cells) {
			int[] data = getSprite(cell);
			
			if (data == null)
				throw new IllegalArgumentException("Error - Sprite name \"" + name +"\" not found");

			BufferedImage img = inGame.getSubimage((int) (DataLoader.SCALE * data[0]), 
					(int) (DataLoader.SCALE * data[1]), (int) (DataLoader.SCALE * data[2]), 
					(int) (DataLoader.SCALE * data[3]));
			anim.add(img);

		}
		
		
		//return img;
		return anim;
		
	}
	


	public static BufferedImage tint(BufferedImage image, Color color) {
		BufferedImage copy = copy(image);
		
		
	    for (int x = 0; x < copy.getWidth(); x++) {
	        for (int y = 0; y < copy.getHeight(); y++) {
	            Color pixelColor = new Color(copy.getRGB(x, y), true);
	            int r = (3 * pixelColor.getRed() + color.getRed()) / 4;
	            int g = (3 * pixelColor.getGreen() + color.getGreen()) / 4;
	            int b = (3 * pixelColor.getBlue() + color.getBlue()) / 4;
	            int a = pixelColor.getAlpha();


	            float[] hsb = Color.RGBtoHSB(r, g, b, null);
	             
	            
	            Color n = Color.getHSBColor(hsb[0], hsb[1] + (color.equals(Color.BLACK) ? 0 : 0.5F),
	            		hsb[2]);
	            
	            r = n.getRed();
	            g = n.getGreen();
	            b = n.getBlue();
	            
	            int rgba = (a << 24) | (r << 16) | (g << 8) | b; 
	            //not sure exactly how this works, shifts over bits to form integer representing argb values
	            //I looked this up to figure out how to convert r,g,b,a values to a color
	            
	            copy.setRGB(x, y, rgba);
	            
	        }
	    }
	    
	    return copy;
	}
	
	public static BufferedImage copy(Image img) {
		BufferedImage copy = new BufferedImage(img.getWidth(null), img.getHeight(null),
				BufferedImage.TYPE_INT_ARGB);
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
	
	public static BufferedImage scaleImage(BufferedImage original, double widthFactor, 
			double heightFactor) {
		
		int newWidth = new Double(original.getWidth() * widthFactor).intValue();
		int newHeight = new Double(original.getHeight() * heightFactor).intValue();

		return resizeImage(original, newWidth, newHeight);
		
	}
	
	public static BufferedImage mirrorLR(BufferedImage img) {
		BufferedImage mirrored = new BufferedImage(img.getWidth() * 2, img.getHeight(), 
				img.getType());
		Graphics g = mirrored.getGraphics();
		g.drawImage(img, 0, 0, null);
		g.drawImage(img, 2 * img.getWidth(), 0, -img.getWidth(), img.getHeight(), null);
		return mirrored;
	}
	
	public static BufferedImage mirrorTB(BufferedImage img) {
		BufferedImage mirrored = new BufferedImage(img.getWidth(), img.getHeight() * 2, 
				img.getType());
		Graphics g = mirrored.getGraphics();
		g.drawImage(img, 0, 0, null);
		g.drawImage(img, 0, img.getHeight() * 2, img.getWidth(), -img.getHeight(), null);
		return mirrored;
	}
}



