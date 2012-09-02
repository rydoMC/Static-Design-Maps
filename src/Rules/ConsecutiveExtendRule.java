package Rules;

import java.util.ArrayList;

import Framework.iRule;
import Model.ClassDeclaration;
import Model.Declaration;
import Model.InterfaceDeclaration;
/**
 * This rule evaluates a declaration object and deducts a point
 * for each consecutive extend.
 * 
 * Motivation: This will filter out lots of subclasses and leave the declarations
 *
 * @author Ryan McNulty
 * @date 13 Jan 2012
 * @organisation Computer and Information Science, Strathclyde University, Glasgow, Scotland.
 */
public class ConsecutiveExtendRule implements iRule {
	private int score;
	
	@Override
	public void processRuleOnNode(Declaration beingScored, ArrayList<Declaration> allClasses) {
		score = 0;
		
		// Score set by other method as then it can be made recursive.
		score = searchForExtends(beingScored, allClasses);
	}
	
	private int searchForExtends(Declaration d, ArrayList<Declaration> allDeclarations){
		if(d != null){
			
			if(d.isClass()){
				ClassDeclaration cd = (ClassDeclaration) d;
				if(cd.getSuperClass().equalsIgnoreCase("")) return 0;
				
				else {
					Declaration superClass = getDeclaration(cd.getSuperClass(),allDeclarations);
		
					return -1 + searchForExtends(superClass, allDeclarations);
				}
			}
			
			else if(d.isInterface()){
				InterfaceDeclaration id = (InterfaceDeclaration) d;
				
				// This variable tracks the lowest score for all the super interfaces.
				// e.g. if an interface implements 2 interfaces, then these 2 will be put to the
				// consecutive extend rule and the worst score will be used.
				int score = 0;
				
				for(String superInterfaceName : id.getInterfaces()){
					// Find the super interface
					Declaration superInterface = getDeclaration(superInterfaceName, allDeclarations);
					
					// Set this interface score to minus 1 to count the super interface
					int interfaceConsecScore = -1;
					
					// Add this to any super interface to THIS super interface
					interfaceConsecScore += searchForExtends(superInterface, allDeclarations);
					
					// Reset score to current interface score if this is worse.
					if(interfaceConsecScore < score) score = interfaceConsecScore;
				}
				return score;
			}
		}
		return 0;
	}
	
	/**
	 * This method searches through the list of all declarations
	 * and returns the declaration object which contains the same name.
	 * 
	 * @param name The name of the class
	 * @param allDecls The list of declarations involved in this project
	 * @return Declaration object which has the same name
	 */
	private Declaration getDeclaration(String name, ArrayList<Declaration> allDecls){
		for(Declaration d: allDecls){
			if(d.getName().equalsIgnoreCase(name)) return d;
		}
		
		// ERROR, super class not in decls list.
		return null;
	}

	@Override
	public int getScore() {
		return score;
	}
	
	public String ruleName(){
		return "Consecutive Extend";
	}
}
