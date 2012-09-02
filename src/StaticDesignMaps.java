import Controller.DefaultController;
import Framework.iView;
import Jung.DefaultView;
import Model.Model;

/**
 * This class is a very simple class that setups the MVC method 
 * and establishes the main components to the program:
 * 
 * 1) Algorithm to use
 * 2) Parser technology
 * 3) GUI package.
 *
 * @author Ryan McNulty
 * @date 4 Jan 2012
 */
public class StaticDesignMaps {
	
	public StaticDesignMaps(){
		// Programs to interfaces, Model should never be changed.
		iView view;
		Model model;

		// Model, View and Controller defined here.
		model = new Model(null, null);
		view = new DefaultView(model);
		new DefaultController(view,model);
		
		view.openWindow();
	}
	

	public static void main(String[] args){
		new StaticDesignMaps();
	}
}