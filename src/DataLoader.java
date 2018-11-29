import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

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
	
	
	public static void parse() throws ParserConfigurationException, SAXException, IOException {
	    SAXParserFactory factory = SAXParserFactory.newInstance();
	    factory.setValidating(true);
	    SAXParser saxParser = factory.newSAXParser();
	    File file = new File("files/inGame.xml");
	    saxParser.parse(file, new DefaultHandler() {

	    	boolean bfname = false;
	    	boolean blname = false;
	    	boolean bnname = false;
	    	boolean bsalary = false;

	    	public void startElement(String uri, String localName,String qName, 
	                    Attributes attributes) throws SAXException {
	    		
	    		System.out.println("Start Element :" + qName);

	    		if (qName.equalsIgnoreCase("FIRSTNAME")) {
	    			bfname = true;
	    		}

	    		if (qName.equalsIgnoreCase("LASTNAME")) {
	    			blname = true;
	    		}

	    		if (qName.equalsIgnoreCase("NICKNAME")) {
	    			bnname = true;
	    		}

	    		if (qName.equalsIgnoreCase("SALARY")) {
	    			bsalary = true;
	    		}

	    	}

	    	public void endElement(String uri, String localName,
	    		String qName) throws SAXException {

	    		System.out.println("End Element :" + qName);

	    	}

	    	public void characters(char ch[], int start, int length) throws SAXException {

	    		if (bfname) {
	    			System.out.println("First Name : " + new String(ch, start, length));
	    			bfname = false;
	    		}

	    		if (blname) {
	    			System.out.println("Last Name : " + new String(ch, start, length));
	    			blname = false;
	    		}

	    		if (bnname) {
	    			System.out.println("Nick Name : " + new String(ch, start, length));
	    			bnname = false;
	    		}

	    		if (bsalary) {
	    			System.out.println("Salary : " + new String(ch, start, length));
	    			bsalary = false;
	    		}

	    	}

	         });    // specify handler
	}
	
	

}
