package Jung;

import java.awt.geom.Point2D;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.util.TreeUtils;
import edu.uci.ics.jung.visualization.subLayout.TreeCollapser;

public class DesignTreeCollapser extends TreeCollapser{
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	 public void collapse(Layout layout, Forest tree, Object toCollapse) throws InstantiationException, IllegalAccessException {

		if (tree.containsVertex(toCollapse)) {
			// get a sub tree from subRoot
			Forest subTree = TreeUtils.getSubTree(tree, toCollapse);
			Object parent = null;
	    	Object edge = null;
	    	
	    	if(tree.getPredecessorCount(toCollapse) > 0) {
	    		parent = tree.getPredecessors(toCollapse).iterator().next();
	    		edge = tree.getInEdges(toCollapse).iterator().next();
	    	}	
	    	
	    	tree.removeVertex(toCollapse);
	    	
	    	if(parent != null) {
	    		tree.addEdge(edge, parent, subTree);
	    	} else {
	    		tree.addVertex(subTree);
	    	}
	    	
	    	layout.setLocation(subTree, (Point2D) layout.transform(toCollapse));
		}
	}
	 
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void expand(Forest tree, Forest subTree) {
    	Object parent = null;
    	Object edge = null;
    	
    	if(tree.getPredecessorCount(subTree) > 0) {
    		// The parent of the subtree
    		parent = tree.getPredecessors(subTree).iterator().next();
    		// Edge currently coming into your hidden tree
    		edge = tree.getInEdges(subTree).iterator().next();
    	}
    	
    	tree.removeVertex(subTree);
    	TreeUtils.addSubTree(tree, subTree, parent, edge);
	}
}
