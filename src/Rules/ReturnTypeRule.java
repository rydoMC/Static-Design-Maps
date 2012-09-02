package Rules;

import java.util.ArrayList;

import Framework.iRule;
import Model.Declaration;
import Model.Method;
import Tools.StructureType;
import Tools.SimpleReturnType;
import Tools.TypeTool;

/**
 * This rule examines a methods return types.
 * The more complex the return type the more this class is implied to be a resource
 * to itself and other classes and proves to be a communication mechanism to the system.
 *
 * @author Ryan McNulty
 * @date 13 Jan 2012
 * @organisation Computer and Information Science, Strathclyde University, Glasgow, Scotland.
 */
public class ReturnTypeRule implements iRule {
	private int score;
	
	@Override
	public void processRuleOnNode(Declaration beingScored, ArrayList<Declaration> allClasses) {
		score = 0;
		
		for(Method currentMd : beingScored.getMethods()){
			score+= scoreMethod(currentMd);
		}
	}

	/**
	 * Scores a method based on its return type. The values
	 * can be found in SimpleReturnType class.
	 * 
	 * If data structure is used then the value will be multiplied 
	 * as the structure gives more meaning to data.
	 * 
	 * @param mthd The method that is to be scored
	 * @return A score which considers the type plus its structure.
	 */
	private int scoreMethod(Method mthd){
		int methodScore = 0;
		
		// Get return type as in source code.
		String returnType = mthd.getReturnType();
		
		// Establish if data structure is present.
		StructureType structureType = structuredReturnStructureType(returnType);
		
		// Set score method to normal, unstructured value.
		methodScore = evalBasicType(returnType);
		//int oldmethodScore = methodScore;
		
		// If a data structure is present in the return type then re-examine score.	
		if(structureType != StructureType.NONE){
			//methodScore = methodScore * structureType.multiplyFactor(structureType);
			methodScore = methodScore * multiplyFactor(structureType);
		}

		return methodScore;
	}
	
	
	/**
	 * Method takes the return type structure
	 * and returns a multiplication value to consider
	 * that its not one object its multiple objects
	 * 
	 * @param r The known ReturnStructureType enum
	 * @return Integer value
	 */
	private int multiplyFactor(StructureType r){
		if(r == StructureType.COLLECTIONTYPE) return 5;
		else if(r == StructureType.MAP) return 3;
		else {
			return 0;
		}
	}
	/**
	 * Method evaluates a return type in string form.
	 * It return an integer score defined by SimpleReturnType.
	 * 
	 * If structure exists within the string then this is stripped away.
	 * 
	 * NB Method is recursive when req'd
	 * 
	 * @param returnType The return type of method as in source code.
	 * @return Integer value for the object type only.
	 */
	private int evalBasicType(String returnType) {
		returnType = returnType.trim(); // remove any whitespace - defensive measure
		
		TypeTool tt = new TypeTool();
		StructureType struc = tt.getStructureType(returnType);
		
		if(struc != StructureType.NONE){
			return evalBasicType(tt.removeStructure(returnType));
		}
		else {
			SimpleReturnType srt = tt.getSimpleType(returnType);
			
			if(srt == SimpleReturnType.OBJECT) return 10;
			else if(srt == SimpleReturnType.NUMBER || srt == SimpleReturnType.CHAR) return 2;
			else if(srt == SimpleReturnType.VOID) return 1;
			else {
				return 6;
			}
		}
	}

	
	/**
	 * Method evaluates the return type of a method and returns the
	 * appropriate enum indicated what kind of data structure is present.
	 * 
	 * @param returnType The string representation of the methods return type.
	 * @return ENUM indicating what structure is present in this string.
	 */
	private StructureType structuredReturnStructureType(String returnType) {
		return new TypeTool().getStructureType(returnType);
	}


	@Override
	public int getScore() {
		return score;
	}
	
	public String ruleName(){
		return "Return Type";
	}

}
