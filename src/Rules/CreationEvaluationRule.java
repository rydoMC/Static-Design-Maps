package Rules;

import java.util.ArrayList;

import Framework.iRule;
import Model.ClassDeclaration;
import Model.Declaration;

/**
 * This rule is based on looking at how many objects
 * are created within a class and looks at how many classes
 * create an object of this type. 
 * 
 * The declaration gains a point for each time it creates a new object.
 * The declaration loses a point for each time it is created within another class.
 * 
 * Motivation: Establishes what classes have control over the system.
 *
 * @author Ryan McNulty
 * @date 2 Jan 2012
 * @organisation Computer and Information Science, Strathclyde University, Glasgow, Scotland.
 */
public class CreationEvaluationRule implements iRule {
	private int score;
	
	public void processRuleOnNode(Declaration beingScored, ArrayList<Declaration> collection) {
		score = 0;
		for(Declaration currentDecl: collection){
			
			// If this is class then check for possible refs to the current declaration
			if(beingScored.isClass()){
				ClassDeclaration classBeingScored = (ClassDeclaration) beingScored;
				
				if(classBeingScored.getReferencestoNewObjects().containsKey(currentDecl.getName())){
					score+= classBeingScored.getReferencestoNewObjects().get(currentDecl.getName());
				}
			}
			
			// If the class being reviewed is a class then check its references.
			if(currentDecl.isClass()){
				ClassDeclaration currentClass = (ClassDeclaration) currentDecl;
				
				if(currentClass.getReferencestoNewObjects().containsKey(beingScored.getName())) {
					score-= currentClass.getReferencestoNewObjects().get(beingScored.getName());
				}
			}
		}
	}

	public int getScore() {
		return score;
	}
	
	public String ruleName(){
		return "Creation Evaluation";
	}
}
