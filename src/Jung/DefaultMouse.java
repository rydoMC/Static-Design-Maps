package Jung;

import java.awt.event.MouseEvent;

import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin;
/**
 * This class is an implementation of the JUNG graph mouse component. 
 * Allows zooming, selecting and moving in one.
 *
 * @author Ryan McNulty
 * @date 24 Feb 2012
 * @organisation Computer and Information Science, Strathclyde University, Glasgow, Scotland.
 */
public class DefaultMouse extends PluggableGraphMouse {
//public class DefaultMouse extends DefaultModalGraphMouse<String, Integer> {
	private PickingGraphMousePlugin<String, Integer> pickingPlug;
	private TranslatingGraphMousePlugin moveAroundPlug;
	
	public DefaultMouse(){
		pickingPlug = new PickingGraphMousePlugin<String, Integer>();
		moveAroundPlug = new TranslatingGraphMousePlugin(MouseEvent.BUTTON3_MASK);
		
		
		// Comment out one of these for different modes
		createNoModeMouse();
		//createChangeableModeMouse();

		// Allow scaling no matter what mode.
		add(new ScalingGraphMousePlugin(new CrossoverScalingControl(), 0, 1.1f, 0.9f)); 
	}

	
//	private void createChangeableModeMouse() {
//		add(pickingPlug);
//	}


	private void createNoModeMouse(){
		add(pickingPlug);
		add(moveAroundPlug);
	}
	
	
	/**
	 * Changes the mouse mode to move around
	 */
	public void changeToMoveAround(){
		add(moveAroundPlug);
		remove(pickingPlug);
	}
	
	
	/**
	 * Changes the mouse mode to allow picking.
	 */
	public void changeToPickMode(){
		add(pickingPlug);
		remove(moveAroundPlug);
	}
}