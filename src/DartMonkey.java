import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class DartMonkey extends Tower {

	public DartMonkey(double x, double y, Collection<Balloon> balloons) {
		super(ResourceManager.getInstance().getImage("dart_monkey_body"), x, y, DataLoader.SCALE * 150, 0, 1, 170, "Dart Monkey", balloons);
		// TODO Auto-generated constructor stub
	}
	

	
	
	protected Collection<GameObject> fire(List<Balloon> intersect, int time) {
		Balloon target = selectTarget(intersect);
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
