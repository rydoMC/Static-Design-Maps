package Framework;

import java.awt.event.ActionListener;
import java.util.Observer;
/**
 * This interface is to be implemented by all controllers.
 * 
 * It simply forces the controller to be an observer and action listener and nothing more.
 * This is to ensure that it can play the role of the controller as per MVC.
 *
 * @author Ryan McNulty
 * @date 24 Feb 2012
 * @organisation Computer and Information Science, Strathclyde University, Glasgow, Scotland.
 */
public interface iController extends Observer, ActionListener {}
