package Jung;

import java.util.Collection;
import java.util.HashSet;

import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Tree;
import edu.uci.ics.jung.graph.util.TreeUtils;

public class DesignMapForest<V,E> extends DelegateForest<V,E>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4938920639379067436L;

	@Override
	public Collection<Tree<V, E>> getTrees() {
		Collection<Tree<V,E>> trees = new HashSet<Tree<V,E>>();
		for(V v : getRoots()) {
			Tree<V,E> tree = new DesignMapTree<V,E>(); // This line here is the edit.
			tree.addVertex(v);
			TreeUtils.growSubTree(this, tree, v);
			trees.add(tree);
		}
		return trees;
	}
}
