package Model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This interface provides information hiding to all concrete, abstract and interfaces 
 * declared within the source code.
 * Provides functionality which is shared between all types of source class objects.
 *
 * @author Ryan McNulty
 * @date 27 Dec 2011
 * @organisation Computer and Information Science, Strathclyde University, Glasgow, Scotland.
 */
public interface Declaration {
	
	/**
	 * Retrieves all method contained by this declared type.
	 * 
	 * @return ArrayList of all MethodDeclarations.
	 */
	public ArrayList<Method> getMethods();
	
	
	/**
	 * is this type an interface.
	 * @return True if is interface.
	 */
	public boolean isInterface();
	
	
	/**
	 * is this type a class 
	 * @return True if instance of class decl object
	 */
	public boolean isClass();
	
	
	/**
	 * Adds a MethodDeclaration object to
	 * this declarations method list.
	 * 
	 * @param md MethodDeclaration to be added.
	 */
	public void addNewMethod(Method md);
	
	
	/**
	 * The name of the declared object type.
	 * @return Name of class as in source code.
	 */
	public String getName();
	
	
	/**
	 * Adds a new interface for this declaration.
	 * 
	 * @param name Name of interface
	 */
	public boolean addInterface(String name);

	
	/**
	 * Adds the super classes name
	 * 
	 * @param name Super class name.
	 */
	public boolean addSuperClass(String name);
	
	
	/**
	 * Returns a list of string containing the name
	 * of each of the interfaces declared for this class.
	 * 
	 * @return Names of all declared interfaces
	 */
	public ArrayList<String> getInterfaces();
	
	
	/**
	 * Allows the package name to be set
	 * 
	 * @param pName name of package as in file.
	 */
	public void setPackageName(String pName);
	
	
	/**
	 * Allows the adding of a import declared in the file
	 * 
	 * @param importedName full path of imported type.
	 */
	public boolean addImport(String importedName);
	
	
	/**
	 * Returns all imports
	 * 
	 * @return List of full import paths.
	 */
	public ArrayList<String> getImports();
	
	
	/**
	 * Gets package name of declaration
	 * 
	 * @return String containing package name
	 */
	public String getPackageName();
	
	/**
	 * Allows the addition of new references
	 * 
	 * @param s The name of the class that's being referenced
	 */
	public void addReferenceToType(String s);
	
	/**
	 * Allows access to the reference type map
	 * 
	 * @return HashMap object of the reference map
	 */
	public HashMap<String, Integer> getReferencesToTypes();
	
	/**
	 * Uses the reference hash map to return the keyset
	 * in the order of their greatest value to least.
	 * 
	 * e.g. if Class A is referenced 10 times and Class B is referenced 5 times.
	 * Then a list will return with Class A name before Class B name.
	 * 
	 * @return Collection of class names.
	 */
	public ArrayList<String> getOrderedReferenceTypes();
}
