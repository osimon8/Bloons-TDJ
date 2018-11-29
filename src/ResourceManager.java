import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

public class ResourceManager {

	
	private Map<String, int[]> sprites;
	private Image inGame;
	
	public ResourceManager() {
		
		try {
			sprites = DataLoader.parseXML("files/InGame.xml");
			inGame = DataLoader.loadImage("InGame.png");
		} catch (IOException | XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// TODO Auto-generated constructor stub
	}
	
	
	public int[] getSprite(String name) {
		return sprites.get(name);
	}
	
	public Image getImage(String name) {
		
		
		int[] sprite = getSprite(name);

		
		
		return null;
		
	}

}
