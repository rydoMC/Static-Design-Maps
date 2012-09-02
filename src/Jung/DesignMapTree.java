package Jung;

import java.util.Collection;

import edu.uci.ics.jung.graph.DelegateTree;

public class DesignMapTree<V, E> extends DelegateTree<V,E>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7943269238975294316L;

	/**
	 * Overrides the normal to string as they are too long.
	 * Instead returns the root vertex's toString as this
	 * is more meaningful to the user.
	 */
	public String toString(){
		return getRoot().toString();
	}
	
	@Override
	public boolean addChild(E edge, V parent, V child) {
		Collection<V> vertices = delegate.getVertices();
		if(vertices.contains(parent) == false) {
			throw new IllegalArgumentException("Tree must already contain parent "+parent);
		}
		// Other if clause removed here
		
        vertex_depths.put(child, vertex_depths.get(parent) + 1);
		return delegate.addEdge(edge, parent, child);
	}
	
}
