import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class DataLoader {

	
	public static final int FIDELITY = 1; //how smooth the path will be, 1 is smoothest, this is included to increase load speed
	public final static double SCALE = 1;
	
	public DataLoader() {
		// TODO Auto-generated constructor stub
	}
	
	public static Point[] readPathData(String filename) throws IOException {
		Reader re = new FileReader("files/data/levels/" + filename + "/" + filename + "PATH.dat");
		@SuppressWarnings("resource") //it thinks I don't close r, but I clearly do
		BufferedReader r = new BufferedReader(re);
		
		String line = r.readLine().trim();
		int size = Integer.parseInt(line);
		Point[] data = new Point[size / FIDELITY];
		
		for (int i = 0; i < size / FIDELITY; i ++) {			
			line = r.readLine();
			if (line == null)
				throw new IllegalArgumentException("Not enough points in file");
			line = line.trim();
			Scanner sc = new Scanner(line); 
			sc.useDelimiter(",");
			int x = (int)(SCALE * sc.nextInt());
			int y = (int)(SCALE * sc.nextInt());

			Point p = new Point(x, y);
			data[i] = p;
			sc.close();
			
			for (int j = 1; j < FIDELITY; j++) {
				r.readLine();
			}
			
		}
		
		r.close();
		
		return data;
		
	}
	
	
	
	public static Collection<Bloon> readLevelData(int level) throws IOException{
		List<Bloon> ret = new LinkedList<>();
		Reader re = new FileReader("files/data/levelData.dat");
		@SuppressWarnings("resource") //it thinks I don't close r, but I clearly do
		BufferedReader r = new BufferedReader(re);
		int line = 0;
		String ln = "";
		
		while (line != level) {
			ln = r.readLine().trim();
			line++;
		}
		Scanner sc = new Scanner(ln); 
		sc.useDelimiter(", ");
		
		int bound = 0;
		
		String entry = "";
		
		while(sc.hasNext()) {
		
			entry = sc.next();
			Scanner sc2 = new Scanner(entry);
			sc2.useDelimiter(" ");
			bound = sc2.nextInt();
			Bloon b = Bloon.valueOf(sc2.next().trim());
			for (int i = 0; i < bound; i++) {
				ret.add(b);
			}
		
			sc2.close();
		}
		sc.close();
		return ret;
		
	}
	
	
	public static Object[] parseXML(String filename) throws IOException, XMLStreamException {
		
		
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        InputStream in = new FileInputStream(filename);
        XMLStreamReader streamReader = inputFactory.createXMLStreamReader(in);
        streamReader.nextTag(); // Advance to "book" element
        streamReader.nextTag(); // Advance to "person" element

        Map<String, int[]> sprites = new HashMap<>();
        Map<String, List<String>> animations = new HashMap<>();
        boolean inAnimation = false;
        List<String> animList = new LinkedList<>();
        String animName ="";
        
        while (streamReader.hasNext()) {
            if (streamReader.isStartElement()) {
                switch (streamReader.getLocalName()) {
                case "Cell": {
                	//Example Cell:
                	// <Cell name="submarine_gun_01" x="679" y="999" w="7" h="15" ax="1" ay="1" aw="8" ah="16"/>
                	int[] temp = new int[8];
                	for (int i = 1; i <= 8; i ++)
                		temp[i - 1] = Integer.parseInt(streamReader.getAttributeValue(i));
                	String name = streamReader.getAttributeValue(0);
                	sprites.put(name, temp);
                	if (inAnimation) {
                		animList.add(name);
                	}
                    break;
                }
                case "Animation" : {
                	inAnimation = true;
                	animName = streamReader.getAttributeValue(0);
                	break;
                }
                }
            }
            else if (streamReader.isEndElement() && streamReader.getLocalName().equals("Animation")) {
            	inAnimation = false;
            	List<String> newStuff = new LinkedList<>();
            	newStuff.addAll(animList);
            	animations.put(animName, newStuff);
            	animList.clear();
            }
            
            
            streamReader.next();
        }
        
		return new Object[] {sprites, animations};
		
	}
	
	
    public static BufferedImage loadImage(String path){
        try {
        	BufferedImage img = ImageIO.read(new File(path));
        	if (img == null || SCALE == 1)
        		return img;
        	return ResourceManager.scaleImage(img, SCALE, SCALE);
        } 
        catch (IOException e) {
            return null;
        }
    }
    
    public static void saveData(Field field) throws IOException{
    	File file = new File("files/data/saveFile.dat");
    	file.createNewFile();
    	Writer writ = new FileWriter(file);
    	BufferedWriter w = new BufferedWriter(writ);
    	
    	int lvl = field.getLevel();
    	if (field.inLevel())
    		lvl--; //restart level if currently in a level
    	
    	w.write(lvl + "\n"); //write current level
    	w.write(field.getMoney() + "\n"); //write money
    	w.write(field.getLives() + "\n"); //write current lives
    	
    	for (Tower t : field.getTowers()) {
        	w.write(t.getClass().toString() + ";" + t.getX() + ";" + t.getY() + "\n"); 
        	//1 line per tower, "type;x;y" 
    	}
    	
    	w.close();
    	
    }
    
    public static void loadData(Field field) throws IOException{
    	File file = new File("files/data/saveFile.dat");
    	Reader red = new FileReader(file);
    	BufferedReader r = new BufferedReader(red);
    	
    	int level = Integer.parseInt(r.readLine().trim());
    	int money = Integer.parseInt(r.readLine().trim());
    	int lives = Integer.parseInt(r.readLine().trim());
    	
    	List<Tower> towers = new LinkedList<>();
    	
    	String currLine = r.readLine();

    	while (currLine != null) {
    		currLine = currLine.trim();
    		Scanner sc = new Scanner(currLine);
    		sc.useDelimiter(";");
    		String type = sc.next();
    		double x = sc.nextDouble();
    		double y = sc.nextDouble();
    		sc.close();
    		
    		switch (type) {
    			case ("class DartMonkey"):
    				towers.add(new DartMonkey(x, y, null));
    				break;
    			case ("class BombTower"):
    				towers.add(new BombTower(x, y, null));
    				break;
    			case ("class TackShooter"):
    				towers.add(new TackShooter(x, y, null));
    				break;
    			case ("class IceTower"):
    				towers.add(new IceTower(x, y, null));
    				break;
    			case ("class SuperMonkey"):
    				towers.add(new SuperMonkey(x, y, null));
    				break;
    			case ("class Spikes"):
    				towers.add(new Spikes(x, y, null));
    				break;
    			case ("class Pineapple"):
    				towers.add(new Pineapple(x, y, null));
    				break;
    			default:
    				break;
    		}

    		currLine = r.readLine();
    	}
    	r.close();
    	
    	field.reset(level, money, lives);
    	Collection<Balloon> balloons = field.getBalloons();
    	
    	for (Tower t : towers) {
    		t.setBalloons(balloons);
    	}
    	field.setTowers(towers);
    	
    }
    
	

}
