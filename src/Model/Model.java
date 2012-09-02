package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

import Algorithms.DefaultAlgorithm;
import Framework.iAlgorithm;

/**
 * This method holds the models state by containing all
 * declaration objects and the functionality required over them.
 *
 * @author Ryan McNulty
 * @date 11 Jan 2012
 * @organisation Computer and Information Science, Strathclyde University, Glasgow, Scotland.
 */
public class Model extends Observable {
	private ArrayList<Declaration> allDecls;
	private iAlgorithm algorithm;
	
	/**
	 * Default Constructor
	 */
	public Model(ArrayList<Declaration> modelDecls, iAlgorithm a){
		setAlgorithm(a);
		setDataset(modelDecls);
	}

	/**
	 * Method sets the models algorithm.
	 * If null is passed then algorithm is set
	 * to the default algorithm
	 * 
	 * @param a
	 */
	public void setAlgorithm(iAlgorithm a){
		if(a==null) algorithm = new DefaultAlgorithm();
		else {
			algorithm = a;
		}
		setChanged();
		notifyObservers();
	}
	
	
	/**
	 * Allows the declarations in the model and algorithm
	 * to be set.
	 * 
	 * @param decls Declarations the Model is to carry out
	 */
	public void setDataset(ArrayList<Declaration> decls){
		if(decls!=null){
			checkDataset(decls);
			allDecls = decls;
			algorithm.setDataset(decls);
		}
		else {
			allDecls = new ArrayList<Declaration>();
		}
	}
	
	
	/**
	 * Runs the current set algorithm based on the
	 * models current declarations then notifies
	 * all observers.
	 */
	public void validateModel(){
		if(algorithm != null && allDecls != null){
			algorithm.setDataset(allDecls);
			algorithm.execute();
			setChanged();
			notifyObservers();
		}
	}

	public ArrayList<Declaration> getResults() {
		ArrayList<Declaration> orderedDecls = new ArrayList<Declaration>();
		
		for(String s : algorithm.getResults()){
			orderedDecls.add(getDeclaration(s));
		}
		
		return orderedDecls;
	}
	
	
	/**
	 * Returns the HashMap object used as the reference map
	 * for a given declaration
	 * 
	 * @param declName The name of the declaration that contains the reference map you want
	 * @return Null if that declaration doesn't exist.
	 */
	public HashMap<String,Integer> getReferencesMap(String declName){
		
		for (Declaration d : allDecls) {
			if (declName.equalsIgnoreCase(d.getName())) {
				return d.getReferencesToTypes();
			}
		}
		return null;
	}
	

	private Declaration getDeclaration(String name) {
		if (allDecls != null) {
			for (Declaration d : allDecls) {
				if (d.getName().equalsIgnoreCase(name))
					return d;
			}
		}
		return null;
	}
	
	
	
	private void checkDataset(ArrayList<Declaration> data){
		ArrayList<Declaration> notDeclarations = new ArrayList<Declaration>();
		
		for(Declaration d : data){
			if(d == null) notDeclarations.add(d);
		}
		
		for(Declaration d : notDeclarations){
			data.remove(d);
		}
	}
	
	
//	/**
//	 * Returns the list of declarations in the 
//	 * order of importance the algorithm has
//	 * found.
//	 * 
//	 * @return ArrayList contained within the algorithm.
//	 */
//	public ArrayList<String> getAlgorithmResults(){
//		return algorithm.getResults();
//	}
//	
}
