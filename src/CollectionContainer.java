import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CollectionContainer<T> implements Iterable<T> {

	
	private List<Collection<T>> stuff = new ArrayList<>();
	
	public CollectionContainer() {
		// TODO Auto-generated constructor stub
	}
	
	public void add(Collection<T> e) {
		stuff.add(e);
	}

	@Override
	public Iterator<T> iterator() {
		// TODO Auto-generated method stub
		return (new Iterator<T>() {

			int cIndex = 0;
			List<Iterator<T>> its = genIterators();
			
			
			private List<Iterator<T>> genIterators() {
				List<Iterator<T>> its = new ArrayList<>();
				for (Collection<T> c : stuff) {
					its.add(c.iterator());
				}
				return its;
			}
			
			
			@Override
			public boolean hasNext() {
				return cIndex > stuff.size() || its.get(cIndex).hasNext();
			}

			@Override
			public T next() {
				T retVal = its.get(cIndex).next();
				if (!its.get(cIndex).hasNext())
					cIndex++;
				return retVal;
			}
			
		});
	}

	
	
}
