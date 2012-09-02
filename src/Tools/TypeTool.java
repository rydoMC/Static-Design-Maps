package Tools;


/**
 * This class aims to provide more meaningful information about types within
 * source code.
 * 
 * @author Ryan McNulty
 * @date 3 Feb 2012
 * @organisation Computer and Information Science, Strathclyde University,
 *               Glasgow, Scotland.
 */
public class TypeTool {

	/**
	 * Method evaluates the return type of a method
	 * as it is in source code and returns the 
	 * appropriate enum for the return types structure.
	 * 
	 * @param returnType Return type of the method as it is in its source code
	 * @return The most appropriate structure enum.
	 */
	public StructureType getStructureType(String returnType) {
		// Check for List or Map
		if (returnType.contains("<")) {
			String structureTypes = removeStructure(returnType);

			// Return MAP enum
			if (structureTypes.contains(",")) {
				return StructureType.MAP;
			}
			// Return structure is a collection
			else {
				return StructureType.COLLECTIONTYPE;
			}
		}
		// Check if Array
		else if (returnType.contains("[")) {
			return StructureType.COLLECTIONTYPE;
		}

		// If no if statement is caught then no structure present.
		return StructureType.NONE;
	}

	
	/**
	 * Returns the substring of s such that
	 * the < and > are no longer applied or in case
	 * of an array that its drops the []
	 * 
	 * @param s The string which contains the structure
	 * @return Substring containing parts between < and >.
	 */
	public String removeStructure(String s) {
		// List structure
		if(s.contains("<")){
			return s.substring(s.indexOf("<") + 1, s.indexOf(">"));
		}
		else {
			// Structure is an Array
			//System.out.println(s);
			return s.substring(0,s.indexOf("["));
		}
	}
	
	
	public SimpleReturnType getSimpleType(String returnType){
		// Set default to object as we don't know every class name in java.
		SimpleReturnType sr = SimpleReturnType.OBJECT;

		// Check for number types
		if (returnType.equalsIgnoreCase("double")
				|| returnType.equalsIgnoreCase("int")
				|| returnType.equalsIgnoreCase("integer")
				|| returnType.equalsIgnoreCase("long")) {
			sr = SimpleReturnType.NUMBER;
		}

		// Boolean holds same value as number
		else if (returnType.equalsIgnoreCase("boolean")) {
			sr = SimpleReturnType.NUMBER;
		}

		// Char holds value as number
		else if (returnType.equalsIgnoreCase("char")
				|| returnType.equalsIgnoreCase("character")) {
			sr = SimpleReturnType.NUMBER;
		}

		// Check if string
		else if (returnType.equalsIgnoreCase("string")) {
			sr = SimpleReturnType.STRING;
		}

		// Check for void
		else if (returnType.equalsIgnoreCase("void")) {
			sr = SimpleReturnType.VOID;
		}

		return sr;
	}
}
