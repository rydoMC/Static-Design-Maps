package Controller;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import Framework.iController;
import Framework.iView;
import Model.Declaration;
import Model.Model;
import Parsing.JParserConverter;
/**
 * This class is the default controller for this project.
 * 
 * When implementing a controller this class should be used as an example.
 * 
 * NB Action performed contains the parser components as its only req'd each time the dataset is changed
 *
 * @author Ryan McNulty
 * @date 9 Feb 2012
 * @organisation Computer and Information Science, Strathclyde University, Glasgow, Scotland.
 */
public class DefaultController implements iController,Observer {
	private iView view;
	private Model model;

	public DefaultController(iView v, Model m) {
		model = m;
		view = v;

		if(v!=null) v.addController(this); // Error check
	}


	
	@Override
	public void update(Observable o, Object arg) {
		if(arg instanceof ActionEvent){
			actionPerformed((ActionEvent) arg);
		}
	}


	/**
	 * This method filters the declarations reference maps
	 * by removing any key on the reference map that is not
	 * the name of any of the declaration in the list.
	 * 
	 * NB this is to show what classes are key in this project
	 * hence it should ignore standard java objects.
	 * 
	 * @param decls The list of modelled objects for this dataset instance
	 */
	private void filterDeclarations(ArrayList<Declaration> decls){
		ArrayList<String> knownDecl = new ArrayList<String>();
		
		// Take all declaration names
		for(Declaration d : decls){
			knownDecl.add(d.getName());
		}
		
		for(Declaration d : decls){
			
			HashMap<String,Integer> refMap = d.getReferencesToTypes();
			ArrayList<String> toRemove = new ArrayList<String>(); // List will contain all names that are to be removed.
			
			for(String key : refMap.keySet()){
				// If this key is not a name of a declaration e.g. java standard object then add to the list to be removed.
				if(!knownDecl.contains(key)){
					toRemove.add(key);
				}
			}
			
			// Remove all keys that are java standard.
			for(String toDelete : toRemove){
				refMap.remove(toDelete);
			}
		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		String actCommand = e.getActionCommand().replace(" ", "").toLowerCase();

		if (actCommand.equalsIgnoreCase("shutdown")) {
			System.exit(0);
		}
		
		if (model != null) {
			if (actCommand.equalsIgnoreCase("process")) {
				String path = view.getSourceFileDirectoryPath();

				if (path != null) {
					JParserConverter p = new JParserConverter(path);
					double start = System.nanoTime();
					p.processFiles();

					ArrayList<Declaration> dCol = p.retrieveDeclarations();
					filterDeclarations(dCol);

					model.setDataset(dCol);
					model.validateModel();
					
					start = System.nanoTime() - start;
					System.out.println("Parsing and Analysis took: " + start/1000000000 + "secs");
				}
			} else if (actCommand.equalsIgnoreCase("listresults")) {
				view.showResultsList();
			}
		}
	}
}
