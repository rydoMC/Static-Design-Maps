package Model;

import java.util.HashMap;

/**
 * This class is the logical representation of a class declared in the source code.
 * Each class parsed should be then represented as one of these objects.
 *
 * @author Ryan McNulty
 * @date 27 Dec 2011
 * @organisation Computer and Information Science, Strathclyde University, Glasgow, Scotland.
 */
public class ClassDeclaration extends SharedDeclaration {
	private HashMap<String,Integer> referencesToNewObjects; // Tracks all object creation references.
	
	private boolean isAbstract;
	private String superClass;
	
	/**
	 * Default Constructor
	 */
	public ClassDeclaration(String name, boolean isAbstract){
		super(name);
		this.isAbstract = isAbstract;
		init();
	}
	
	
	/**
	 * Carries out any initialising of fields that cannot be null.
	 */
	private void init(){
		referencesToNewObjects = new HashMap<String, Integer>();
	}
	
	
	/**
	 * As this is a concrete or abstract class, references to new
	 * object creation can happen and therefore we note this.
	 * 
	 * NB This method updates the references to types map to keep the 
	 * reference map valid.
	 * 
	 * @param className The name of the class created.
	 */
	public void addReferenceToNewObject(String className){
		// If class name has already been referenced
		if(referencesToNewObjects.get(className) != null){
			referencesToNewObjects.put(className, referencesToNewObjects.get(className)+1);
		}
		
		// Otherwise note first reference.
		else {
			referencesToNewObjects.put(className, 1);
		}
		
		// Update the reference map
		addReferenceToType(className);
	}
	
	/**
	 * @return True if declared abstract in source code.
	 */
	public boolean isAbstract(){
		return isAbstract;
	}
	

	/**
	 * Returns the HashMap object that tracks new object creations
	 * 
	 * @return Map of types names.
	 */
	public HashMap<String, Integer> getReferencestoNewObjects(){
		return referencesToNewObjects;
	}

	
	/**
	 * Override toString to give more meaningful representation
	 */
	public String toString(){
		String s = super.toString();
		
		if(isAbstract) s+= "(This is an Abstract Class)\n" ;
		
		s+= "\n";
		
		if(getInterfaces() != null){
			for(String e : getInterfaces()){
				s+= "Implements: " + e +"\n";
			}
		}
		
		if(superClass != null) s+= "Extends: " + superClass +"\n";
	
		
		if (referencesToNewObjects.size() != 0) {
			s += "\nReference To Object Creation\n";
			s = addKeyAndVal(s, referencesToNewObjects);
		}

		if (referencesToTypes.size() != 0) {
			s += "\nReference To Types\n";
			s = addKeyAndVal(s, referencesToTypes);
		}
		
		s+= "\nThis class contains: " + getMethods().size() + " methods\n";
		

		return s;
	}
	
	
	/**
	 * Adds each map entries key and value to the string with
	 * a space between the key and val then adds a new line to the string.
	 * 
	 * @param s String key/val to be added to
	 * @param mp The map to put to string
	 * @return The original string plus the new keys and vals.
	 */
	private String addKeyAndVal(String s, HashMap<String, Integer> mp){
		for(String key: mp.keySet()){
			s = s + key + " " + (Integer) mp.get(key) +"\n";
		}
		return s;
	}
	
	
	@Override 
	public boolean addSuperClass(String name){
		if(superClass == null) {
			superClass = name;
			return true;
		}
		return false;
	}

	
	/**
	 * Returns the name of the super class to 
	 * this class.
	 * 
	 * If no super class exists then "" is returned.
	 * 
	 * @return Name of super class else ""
	 */
	public String getSuperClass() {
		if(superClass == null) return "";
		else {
			return superClass;
		}
	}
}
