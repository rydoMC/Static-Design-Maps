package Rules;

import java.util.ArrayList;

import Framework.iRule;
import Model.ClassDeclaration;
import Model.Declaration;

/**
 * This rule evaluates a class based on how many times
 * it is used to extend the functionality of another.
 * 
 * The declaration gains a point if that class/interface
 * is used by another class as an extends/implements command.
 *
 * @author Ryan McNulty
 * @date 13 Jan 2012
 * @organisation Computer and Information Science, Strathclyde University, Glasgow, Scotland.
 */
public class SuperTypeRule implements iRule {
	private int score;
	
	@Override
	public void processRuleOnNode(Declaration beingScored,ArrayList<Declaration> allDecls) {
		score = 0;
		
		for(Declaration currentDecl : allDecls){
			
			// If the class being scored is an interface then all interfaces must be checked.
			if(beingScored.isInterface()){
				for(String interfaceName : currentDecl.getInterfaces()){
					if(interfaceName.equalsIgnoreCase(beingScored.getName())){
						score++;
						continue;
					}
				}
			}
			
			// The declaration being scored is a class and therefore can only extend other classes and not interfaces.
			else if(beingScored.isClass() && currentDecl.isClass()){
				ClassDeclaration currentClass = (ClassDeclaration) currentDecl;
				if(currentClass.getSuperClass().equalsIgnoreCase(beingScored.getName())){
					score++;
				}
			}
		}
		
	}

	@Override
	public int getScore() {
		return score;
	}
	
	public String ruleName(){
		return "Super Type";
	}

}
