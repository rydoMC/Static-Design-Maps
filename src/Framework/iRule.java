package Framework;

import java.util.ArrayList;

import Model.Declaration;

/**
 * This interface should be implemented by all concrete rule
 * types that are to be used with a ranking algorithm
 * with this tool.
 *
 * @author Ryan McNulty
 * @date 27 Dec 2011
 * @organisation Computer and Information Science, Strathclyde University, Glasgow, Scotland.
 */
public interface iRule {
	
	/**
	 * This method carries out the analysis on a given class/interface
	 * as defined by the rule which lies behind this interface.
	 * 
	 * Scoring should be in such a manner that the greater the score
	 * the more important the declaration.
	 * 
	 * @param beingScored Class/Interface to be analysed.
	 */
	public void processRuleOnNode(Declaration beingScored, ArrayList<Declaration> allClasses);
	
	/**
	 * This method should only be called immediate after processRuleOnNode.
	 * 
	 * @return Integer value scored by this class defined by the rule.
	 */
	public int getScore();
	
	/**
	 * Gets a desc about the rule
	 * 
	 * @return Name of rule
	 */
	public String ruleName();
}
