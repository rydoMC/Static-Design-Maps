package Rules;

import java.util.ArrayList;
import java.util.HashMap;

import Framework.iRule;
import Model.Declaration;

/**
 * This rule evaluates and scores a declaration based on how many
 * time it is used as a reference type. This rule provides a better
 * picture to how interfaces are used by the system as they are disadvantaged
 * by not being able to be scored by rules that look at object creation or method bodies.
 *
 * @author Ryan McNulty
 * @date 13 Jan 2012
 * @organisation Computer and Information Science, Strathclyde University, Glasgow, Scotland.
 */
public class ReferenceTypeRule implements iRule{
	private int score;
	
	@Override
	public void processRuleOnNode(Declaration beingScored, ArrayList<Declaration> allDecls) {
		score = 0;
		
		for(Declaration currentDecl : allDecls){
			HashMap<String,Integer> refMap = currentDecl.getReferencesToTypes();
			
			if(refMap.containsKey(beingScored.getName())){	
				score += refMap.get(beingScored.getName());
			}
		}
	}

	@Override
	public int getScore() {
		return score;
	}

	public String ruleName(){
		return "Reference Type";
	}
}
