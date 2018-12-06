import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class DartMonkey extends Tower {

	public DartMonkey(double x, double y, Collection<Balloon> balloons) {
		super(ResourceManager.getInstance().getImage("dart_monkey_body"), x, y, 300, 0, 1, balloons);
		// TODO Auto-generated constructor stub
	}
	

	
	
	protected Collection<GameObject> fire(Balloon target, int time) {
		Collection<GameObject> ret = new LinkedList<>(); 
//		double tx = target.getX();
//		double ty = target.getY();
		Point t = target.getProjectedLocation(time);
		double tx = t.getX();
		double ty = t.getY();
		
		
		ret.add(new Dart(getX(), getY(), tx, ty, balloons));
		align(tx, ty);
		return ret;
	}
	
	
	

}
