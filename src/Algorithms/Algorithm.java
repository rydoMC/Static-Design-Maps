package Algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

import Framework.iAlgorithm;
import Framework.iRule;
import Model.Declaration;

/**
 * This class is to be used to extend other 
 * algorithm classes. It provides functionality
 * and fields that is generic and can be used by all
 * algorithms regardless of rules.
 *
 * @author Ryan McNulty
 * @date 4 Jan 2012
 * @organisation Computer and Information Science, Strathclyde University, Glasgow, Scotland.
 */
public abstract class Algorithm extends Observable implements iAlgorithm{
	private HashMap<iRule, Double> RuleWeightMap;
	private ArrayList<HashMap<iRule,HashMap<String,Integer>>> ScoreMaps; 
	private ArrayList<Declaration> dataSet;
	
	// This array-list contains the class names in order of most important to least important.
	private ArrayList<String> results;
	
	/**
	 * Default constructor	
	 */
	public Algorithm(){
		RuleWeightMap = new HashMap<iRule, Double>();
		ScoreMaps = new ArrayList<HashMap<iRule,HashMap<String,Integer>>>();
		results = new ArrayList<String>();
		
		// Force rules to be added to the map
		setRulesAndWeights();
	}
	
	
	@Override
	public boolean validRuleToWeightMap(){
		return getMapWeight() == 1.0;
	}
	
	
	/**
	 * Provides the current rule-weight map
	 * total.
	 * 
	 * @return Double indicating current weight total
	 */
	private double getMapWeight(){
		double total = 0.0;
		
		for(double d: RuleWeightMap.values()){
			total+= d;
		}
		
		return total;
	}
	
	
	/**
	 * Allows the adding of rules to the current rule to weight map.
	 * This method does error checking to ensure that the map
	 * is valid to add the rule to then also checks since adding
	 * that map is still progressing towards a valid state.
	 * 
	 * The rule will not be added if the map becomes invalid as a result.
	 *
	 * @param rule The rule to be added
	 * @param weight The weight that this rule carries
	 * @return True if the rule has been added
	 */
	protected boolean addRule(iRule rule, double weight){
		if (rule != null) {
			if (getMapWeight() < 1.0) {
				// Add rule
				RuleWeightMap.put(rule, weight);

				// Return true if map still valid
				if (getMapWeight() <= 1.0){
					return true;
				}
				// Otherwise map not valid, remove the rule.
				else {
					RuleWeightMap.remove(rule);
				}
			}
		}
		return false;
	}
	
	
	@Override
	public void execute() {
		if(dataSet.size()!=0){
			if(validRuleToWeightMap()){
				executeRules();
				setResults();
				
				setChanged();
				notifyObservers();
			}
			else {
				// This stmt will only be reached by an under 1.0 rule map
				System.out.println("Rule map does not balance < 1.0");
			}
		}
	}
	
	
	/**
	 * This method calculates the results and
	 * saves the results to the results field.
	 */
	private void setResults() {
		results = new ArrayList<String>();

		// Map to track the class name to their overall score
		HashMap<String,Double> finalResultsMap = new HashMap<String, Double>();
		
		
		
		for(HashMap<iRule,HashMap<String,Integer>> ruleToScoreMap : ScoreMaps){
			
			for(iRule currentRule : ruleToScoreMap.keySet()){
				
				// For each rule, get the ordered results
				ArrayList<String> orderedResults = convertMapToListInt(ruleToScoreMap.get(currentRule));
				
				// For each declaration name, add the result to the final map list incorporating the rules weight.
				for(String declName : orderedResults){
					int lowestIndex = findLowestIndex(ruleToScoreMap.get(currentRule), declName, orderedResults);
					
					// Add one due to index of lists starting at 0, multiplying by 0 will always give 0.
					lowestIndex++;
					
					// Use the lowest index as a score
					double currentRuleScore = lowestIndex * RuleWeightMap.get(currentRule);
					
					
					
					if(finalResultsMap.containsKey(declName)){
						// Add current rule score to other rule scores.
						finalResultsMap.put(declName, finalResultsMap.get(declName) + currentRuleScore);
					}
					else {
						// This is the first scoring of this declaration name.
						finalResultsMap.put(declName, currentRuleScore);
					}
				}
			}
		}
		
		// Convert the final results list descending order.
		results = convertMapToList(finalResultsMap);
		
		// Reverse list as the lower the score now is more important.
		results = reverseList(results);
	}
	
	
	/*
	 * NB 
	 * As scoring is done on the index of a decl name in a list,
	 * to ensure the score is fair, if two or more decls share the same
	 * score then the lowest index most be used.
	 */
	/**
	 * Gets the value of declName from the map and then
	 * searches for the lowest index of other declaration names which
	 * score the same value.
	 * 
	 * @param map The declaration TO score map
	 * @param declName The declaration name you are trying to find the lowest index for
	 * @param orderedResults List in which the declaration appears
	 * @return the lowest index for the score which the declName param achieved.
	 */
	private int findLowestIndex(HashMap<String, Integer> map, String declName, ArrayList<String> orderedResults) {
		// The score the declaration got.
		int score = map.get(declName);
		int currentLowestIndex = orderedResults.indexOf(declName);
		
		// Only search for a lower index if this isn't the lowest possible index. PERFORMANCE MEASURE.
		if(currentLowestIndex != 0){
			
			ArrayList<String> declsWithSameScore = new ArrayList<String>();
		
			for(String key : map.keySet()){
				if(map.get(key) == score){
					declsWithSameScore.add(key);
				}
			}
	
		
			/* For each declaration name with the same score,
			 * test if the index is < currentLowest found
			 */ 
			for (String s : declsWithSameScore) {
				if (orderedResults.indexOf(s) < currentLowestIndex) {
					currentLowestIndex = orderedResults.indexOf(s);
				}
			}
		}
		
		return currentLowestIndex;
	}

	
	/**
	 * Method returns a new array-list referencing
	 * the objects in reverse order.
	 * 
	 * @param listToReverse the list to be reversed
	 * @return ArrayList reversed
	 */
	private ArrayList<String> reverseList(ArrayList<String> listToReverse) {
		ArrayList<String> newList = new ArrayList<String>();

		// Add the objects in reverse order.
		for(int i = listToReverse.size()-1; i >= 0; i--){
			newList.add(listToReverse.get(i));
		}
		
		return newList;
	}
	

	/**
	 * Converts a maps key-set to an array-list. The order
	 * of the array-list is based on the value in the map
	 * for that  key. This works in descending order.
	 * 
	 * Key with value of 172 will appear before key with value 98.
	 * 
	 * NB this method uses ConvertMapToList(....) method.
	 * 
	 * @param map The map object to be changed to list
	 * @return ArrayList<String> containing all keys in map.
	 */
	private ArrayList<String> convertMapToListInt(HashMap<String, Integer> map) {
		HashMap<String,Double> convertedMap = new HashMap<String, Double>();
		
		for(String key : map.keySet()){
			convertedMap.put(key, map.get(key).doubleValue());
		}
		
		return convertMapToList(convertedMap);
	}

	
	/**
	 * This method converts a map to an array list by taking the map
	 * going through all keys and values to return a list in descending order
	 * based on the value in the map. 
	 * 
	 * @param mapToChange The map to build list from.
	 * @return Ordered list containing the class names in Descending Order based on rule that was used to generate the map
	 */
	private ArrayList<String> convertMapToList(HashMap<String,Double> mapToChange){
		ArrayList<String> orderedList = new ArrayList<String>();
		
		
		for(String key : mapToChange.keySet()){
			// Add your first element if list is empty
			if(orderedList.size() == 0) orderedList.add(key);
			
			else {
				int i = 0;
				// Increase i until the item at i in the ordered list is greater than the value this key holds in the map.
				while(i < orderedList.size() && mapToChange.get(orderedList.get(i)) > mapToChange.get(key)){
					i++;
				}
				
				orderedList.add(i,key);
			}
		}		
		return orderedList;
	}
	
	
	/**
	 * Set the declarations the algorithm is to execute over.
	 * If null is passed then it will set the data-set to a
	 * new array list.
	 */
	public void setDataset(ArrayList<Declaration> data){
		if(data != null) this.dataSet = data;
		else dataSet = new ArrayList<Declaration>();
	}


	/**
	 * Processes each of the rule added at setRulesAndWeights() 
	 */
	private void executeRules(){
		ScoreMaps = new ArrayList<HashMap<iRule,HashMap<String,Integer>>>();
		
		for(iRule r: RuleWeightMap.keySet()){
			// This map tracks each class name to a score given by a rule below.
			HashMap<String, Integer> currentRuleScores = new HashMap<String,Integer>();
			
			
			// For each of the declarations, apply the rule and add its score to
			// map.
			for (Declaration currentDecl : dataSet) {
				if (currentDecl != null) {
					r.processRuleOnNode(currentDecl, dataSet);
					currentRuleScores.put(currentDecl.getName(), r.getScore());
				}
			}
			
			// hash-map allows the tracking of the classes and their score to a given rule.
			HashMap<iRule, HashMap<String,Integer>> completeRuleScoredMap = new HashMap<iRule,HashMap<String,Integer>>();
			
			// Add the scores for this rule to a hash-map
			completeRuleScoredMap.put(r, currentRuleScores);
			
			
			// Add the completed hash-map to the list.
			ScoreMaps.add(completeRuleScoredMap);
		}
	}
	
	
	@Override
	public ArrayList<String> getResults(){
		return results;
	}
}
