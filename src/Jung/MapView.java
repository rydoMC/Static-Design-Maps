package Jung;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JApplet;

import org.apache.commons.collections15.functors.ConstantTransformer;

import Model.Declaration;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.GradientVertexRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import edu.uci.ics.jung.visualization.renderers.VertexLabelAsShapeRenderer;
import edu.uci.ics.jung.visualization.subLayout.TreeCollapser;
/**
 * This class is the component that shows the grid and contains the graph.
 *
 * @author Ryan McNulty
 * @date 24 Feb 2012
 * @organisation Computer and Information Science, Strathclyde University, Glasgow, Scotland.
 */
public class MapView extends JApplet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4200177471771419915L;
	
	// Jung Components
	private Forest<Vertex, String> graph;
    private VisualizationViewer<Vertex, String> vv;
    private Layout<Vertex, String> layout;
    private TreeCollapser collapser;
    
    private int edgeNo;
    private int noToShow;
    
    
    /**
     * Default Constructor
     */
    public MapView(){
		initJungComponents();
    }
    

    /**
     * Initialise the JUNG components. 
     */
	private void initJungComponents() {
		setGraphLayout();
		setupVisuals();
		
		collapser = new DesignTreeCollapser();
		noToShow = 5;
	}
	
	
	/**
	 * This method creates a default graph
	 * showing how the key works.
	 */
	private void setGraphLayout(){
		resetGraph();
		refreshLayout();
	}
	
	
	/**
	 * Refreshes the map by setting the layout
	 * to the new graph.
	 */
	private void refreshLayout(){
		layout = new FRLayout<Vertex, String>(graph);
		if(vv != null) {
			vv.setGraphLayout(layout);
		}
	}
	
	
	/**
	 * Sets up the visual viewer settings such as
	 * colour of background, colour of edges and the
	 * boxes.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setupVisuals(){
        // Defines the view object some how
        vv =  new VisualizationViewer<Vertex,String>(layout, new Dimension(500,500));
        
        // Puts the vertex in a box
        VertexLabelAsShapeRenderer<Vertex, String> vlasr = new VertexLabelAsShapeRenderer<Vertex,String>(vv.getRenderContext());
   
         //customize the render context
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Vertex>()); // This shows the name
        vv.getRenderContext().setVertexShapeTransformer(vlasr); // This makes them into boxes
        
        DefaultVertexLabelRenderer vlr = new DefaultVertexLabelRenderer(Color.RED);// Set colour of text when selected
        vv.getRenderContext().setVertexLabelRenderer(vlr);
        
        vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Wedge<Vertex, String>(2));
        
        vv.getRenderer().setVertexRenderer(new GradientVertexRenderer<Vertex,String>(Color.WHITE, Color.WHITE, true)); // Set background colour of boxes


        // Set edge attributes
        refreshEdgeRenderer();
        vv.getRenderContext().setArrowFillPaintTransformer(new ConstantTransformer(Color.LIGHT_GRAY));
        vv.getRenderContext().setEdgeStrokeTransformer(new ConstantTransformer(new BasicStroke(2.5f)));
        
        vv.setBackground(Color.white);
        
        // add a listener for ToolTips
        vv.setVertexToolTipTransformer(new ToStringLabeller<Vertex>());
        vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
        
        
        getContentPane().add(vv);
	}


	/**
	 * Re-initialise edge renderer.
	 */
	private void refreshEdgeRenderer() {
		vv.getRenderContext().setEdgeDrawPaintTransformer(new RedColourRenderer());
	}


	/**
	 * The mouse listener to be used on this map. 
	 * 
	 * @param mouseListener The mouse listener
	 */
	public void setMouseController(MouseListener mouseListener) {
		if(vv != null) vv.setGraphMouse((PluggableGraphMouse) mouseListener);
	}


	/**
	 * Refreshes the visuals to now represent the new 
	 * graph deteremined by the passed modellbed objects
	 * 
	 * @param modelledObjs The new model components the map is to represent.
	 */
	public void refreshMap(ArrayList<Declaration> modelledObjs) {
		resetGraph();
		
		noToShow = Math.min(noToShow, modelledObjs.size());
		
		for(int i = 0; i < noToShow; i++){
			createVertex(modelledObjs.get(i).getName(), modelledObjs, null);
		}
		
		refreshLayout();
	}


	@SuppressWarnings("rawtypes")
	private Vertex createVertex(String name, ArrayList<Declaration> modelledObjects, ArrayList<String> alreadyOnCycle){

		// Defensive measure
		if (name != null && modelledObjects != null) {
			if(alreadyOnCycle == null) alreadyOnCycle = new ArrayList<String>();
			
			Vertex toReturn = new Vertex(name);
			Declaration declOfVetx = null;

			// Set declOfVetx
			for (Declaration d : modelledObjects) {
				if (d.getName().equalsIgnoreCase(name)) {
					declOfVetx = d;
					break;
				}
			}

			graph.addVertex(toReturn);
			
			// If this important decl has references then create the vertex representing the reference
			if (declOfVetx.getOrderedReferenceTypes().size() != 0) {
				int relationshipPriority = 1; // Edge attribute for colour indication.

				for (String refToType : declOfVetx.getOrderedReferenceTypes()) {
					
					// If this reference is not the vertexes own name AND
					// has not already been established on the path then create the vertex and add the edge
					if (!refToType.equalsIgnoreCase(name) && !alreadyOnCycle.contains(refToType)) {

						Declaration refType = null;

						for (Declaration d : modelledObjects) {
							if (d.getName().equalsIgnoreCase(refToType)) {
								refType = d;
								break;
							}
						}

						alreadyOnCycle.add(name);
						Vertex refVertex = createVertex(refType.getName(), modelledObjects, alreadyOnCycle);

						// Adding edge automatically adds vertex
						graph.addEdge(relationshipPriority + "_" + edgeNo,toReturn, refVertex);
						
						edgeNo++;
						relationshipPriority++;

						layout.setGraph(graph);

						try {
							collapser.collapse(layout,
									(Forest) layout.getGraph(), refVertex);
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
			}
			return toReturn;
		}
		return null; // Only reached if name and modelled objects == null.
	}
	
	
	/**
	 * Re-initialises the graph object.
	 */
	private void resetGraph() {
		graph = new DesignMapForest<Vertex,String>();
	}

	
	// ----------------- Tree Functionality 
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void collapseNode() {
		Collection picked = new HashSet(vv.getPickedVertexState().getPicked());
        if(picked.size() != 0) {
        	Iterator<Object> it = picked.iterator();
            Forest inGraph = (Forest) layout.getGraph();

            try {
            	collapser.collapse(vv.getGraphLayout(), inGraph, it.next());
            	while(it.hasNext()){
            		collapser.collapse(vv.getGraphLayout(), inGraph, it.next());
            	}
            } 
            catch (InstantiationException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}
            vv.repaint();
        }
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public void expandNode() {
		Collection picked =new HashSet(vv.getPickedVertexState().getPicked());

		if(picked.size() != 0) {
        	Iterator<Object> it = picked.iterator();
            Forest inGraph = (Forest) layout.getGraph();

            while(it.hasNext()){
            	Object b = it.next();
            	if(b instanceof Forest){
            		collapser.expand((Forest) layout.getGraph(), (Forest)  b);
					while(it.hasNext()){
						collapser.expand((Forest) layout.getGraph(), (Forest)  b);
					}
            	}
            }
            vv.repaint();
        }
	}


	/**
	 * Allows the number of the nodes on the map to be changed
	 * 
	 * @param i Number of nodes to show.
	 */
	public void changeNumberOfNodes(int i) {
		if(i < 1) i = 1;
		else{
			noToShow = i;
		}
	}

	
	/**
	 * @return Number of nodes currently on the map
	 */
	public int getNoOfNodes() {
		return noToShow;
	}
}
