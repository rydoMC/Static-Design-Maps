package Framework;

import java.util.ArrayList;

import Model.Declaration;

/**
 * This interface provides all necessary method signatures
 * that will be required in order to implement an algorithm
 * that can be used by this tool.
 * 
 * This should be used as a general structure to all algorithms.
 *
 * @author Ryan McNulty
 * @date 2 Jan 2012
 * @organisation Computer and Information Science, Strathclyde University, Glasgow, Scotland.
 */
public interface iAlgorithm {

	/**
	 * This method will create the rule objects
	 * and store them to algorithm object 
	 * along with settings the weights for each rule as well.
	 */
	public void setRulesAndWeights();
	
	/**
	 * This rule evaluates the rules mapping to ensure
	 * it is balanced.
	 * 
	 * @return True if all rule weights add to 1.0
	 */
	public boolean validRuleToWeightMap();
	
	/**
	 * This method contains the steps
	 * in order to carry out the rules
	 * over the declarations
	 * 
	 * @throws ModelExeception 
	 */
	public void execute();

	/**
	 * Set data that the algorithm is to work over.
	 * 
	 * @param declarations 
	 */
	public void setDataset(ArrayList<Declaration> declarations);
	
	
	/**
	 * Returns the results of the algorithm.
	 * 
	 * @return List of class name ordered greatest importance to least.
	 */
	public ArrayList<String> getResults();
}
