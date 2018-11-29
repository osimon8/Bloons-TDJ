import java.awt.Image;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;



public class DataLoader {

	public DataLoader() {
		// TODO Auto-generated constructor stub
	}
	
	public static Point[] readPathData(String filename) throws IOException {
		Reader re = new FileReader("files/data/levels/" + filename + "/" + filename + "PATH.dat");
		@SuppressWarnings("resource") //it thinks I don't close r, but I clearly do
		BufferedReader r = new BufferedReader(re);
		
		int fidelity = 3; //how smooth the path will be, 1 is smoothest, this is included to increase load speed
		
		String line = r.readLine().trim();
		int size = Integer.parseInt(line);
		Point[] data = new Point[size / fidelity];
		
		for (int i = 0; i < size / fidelity; i ++) {			
			line = r.readLine();
			if (line == null)
				throw new IllegalArgumentException("Not enough points in file");
			line = line.trim();
			Scanner sc = new Scanner(line); 
			sc.useDelimiter(",");
			int x = sc.nextInt();
			int y = sc.nextInt();

			Point p = new Point(x, y);
			data[i] = p;
			sc.close();
			
			for (int j = 1; j < fidelity; j++) {
				r.readLine();
			}
			
		}
		
		r.close();
		
		return data;
		
	}
	
	
	public static Map<String, int[]> parseXML(String filename) throws IOException, XMLStreamException {
//	    SAXParserFactory factory = SAXParserFactory.newInstance();
//	    factory.setValidating(true);
//	    SAXParser saxParser = factory.newSAXParser();
//	    File file = new File("files/inGame.xml");
//	    saxParser.parse(file, new DefaultHandler() {
//
//	    	boolean bfname = false;
//	    	boolean blname = false;
//	    	boolean bnname = false;
//	    	boolean bsalary = false;
//
//	    	public void startElement(String uri, String localName,String qName, 
//	                    Attributes attributes) throws SAXException {
//	    		
//	    		System.out.println("Start Element :" + qName);
//
//	    		if (qName.equalsIgnoreCase("FIRSTNAME")) {
//	    			bfname = true;
//	    		}
//
//	    		if (qName.equalsIgnoreCase("LASTNAME")) {
//	    			blname = true;
//	    		}
//
//	    		if (qName.equalsIgnoreCase("NICKNAME")) {
//	    			bnname = true;
//	    		}
//
//	    		if (qName.equalsIgnoreCase("SALARY")) {
//	    			bsalary = true;
//	    		}
//
//	    	}
//
//	    	public void endElement(String uri, String localName,
//	    		String qName) throws SAXException {
//
//	    		System.out.println("End Element :" + qName);
//
//	    	}
//
//	    	public void characters(char ch[], int start, int length) throws SAXException {
//
//	    		if (bfname) {
//	    			System.out.println("First Name : " + new String(ch, start, length));
//	    			bfname = false;
//	    		}
//
//	    		if (blname) {
//	    			System.out.println("Last Name : " + new String(ch, start, length));
//	    			blname = false;
//	    		}
//
//	    		if (bnname) {
//	    			System.out.println("Nick Name : " + new String(ch, start, length));
//	    			bnname = false;
//	    		}
//
//	    		if (bsalary) {
//	    			System.out.println("Salary : " + new String(ch, start, length));
//	    			bsalary = false;
//	    		}
//
//	    	}
//
//	         });    // specify handler
		
		
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        InputStream in = new FileInputStream(filename);
        XMLStreamReader streamReader = inputFactory.createXMLStreamReader(in);
        streamReader.nextTag(); // Advance to "book" element
        streamReader.nextTag(); // Advance to "person" element

        Map<String, int[]> data = new HashMap<>();
        
        while (streamReader.hasNext()) {
            if (streamReader.isStartElement()) {
                switch (streamReader.getLocalName()) {
                case "Cell": {
                	//Example Cell:
                	// <Cell name="submarine_gun_01" x="679" y="999" w="7" h="15" ax="1" ay="1" aw="8" ah="16"/>
                	int[] temp = new int[8];
                	for (int i = 1; i <= 8; i ++)
                		temp[i] = Integer.parseInt(streamReader.getAttributeValue(i));
                	data.put(streamReader.getAttributeValue(0), temp);
                    break;
                }

                }
            }
            streamReader.next();
        }
        

		return data;
		
	}
	
	
    public static Image loadImage(String path){
        try {
        	return ImageIO.read(new File(path));
        } 
        catch (IOException e) {
            return null;
        }
    }
	

}
