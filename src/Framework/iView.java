package Framework;

import java.awt.event.ActionListener;
import java.util.Observer;
/**
 * This interface is used as the reference type for the view inside the controller
 * and should be implemented by ANY view implementation for this project.
 *
 * @author Ryan McNulty
 * @date 24 Feb 2012
 * @organisation Computer and Information Science, Strathclyde University, Glasgow, Scotland.
 */
public interface iView extends Observer, ActionListener {

	/**
	 * Opens the view to show the interface.
	 */
	public void openWindow();
	
	
	/**
	 * Gets the latest source file directory path
	 * indicated by the user through the user interface.
	 * 
	 * @return 
	 */
	public String getSourceFileDirectoryPath();

	/**
	 * This method is required to establish MVC control.
	 * As the view it referenced through an interface it cannot
	 * extend Observable hence this method addresses this issue
	 * by allowing the concrete view to say this.addObserver(c)
	 * 
	 * @param c The observing controller
	 */
	public void addController(iController c);
	
	/**
	 * Loads a window showing the results from
	 * the model.
	 */
	public void showResultsList();
}