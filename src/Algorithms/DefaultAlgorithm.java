package Algorithms;

import Rules.ConsecutiveExtendRule;
import Rules.CreationEvaluationRule;
import Rules.ReferenceTypeRule;
import Rules.ReturnTypeRule;
import Rules.SuperTypeRule;


/**
 * This class is an example of an algorithm using 
 * a set of rules with applied weights.
 * 
 * @author Ryan McNulty
 * @date 2 Jan 2012
 * @organisation Computer and Information Science, Strathclyde University, Glasgow, Scotland.
 */
public class DefaultAlgorithm extends Algorithm {

	/**
	 * Default Constructor
	 */
	public DefaultAlgorithm() {
		super();
	}

	
	@Override
	public void setRulesAndWeights() {
		addRule(new ConsecutiveExtendRule(), 0.2);
		addRule(new CreationEvaluationRule(), 0.2);
		addRule(new ReferenceTypeRule(), 0.2);
		addRule(new ReturnTypeRule(), 0.2);
		addRule(new SuperTypeRule(), 0.2);
	}
}