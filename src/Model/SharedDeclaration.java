package Model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This basis of this class is to allow common functionality and attributes
 * of both interfaces and classes to be shared to save code duplication.
 *
 * @author Ryan McNulty
 * @date 27 Dec 2011
 * @organisation Computer and Information Science, Strathclyde University, Glasgow, Scotland.
 */
public abstract class SharedDeclaration implements Declaration {
	private String name; // Name of declaration object
	private String packageName;
	
	protected HashMap<String, Integer> referencesToTypes; // Tracks all object type references.
	
	private ArrayList<Method> methodsDeclared;
	
	private ArrayList<String> interfacesDeclared;
	private ArrayList<String> importsDeclared;
	
	public SharedDeclaration(String name){
		this.name = name;
		init();
	}
	
	
	
	private void init(){
		packageName = "";
		
		methodsDeclared = new ArrayList<Method>();
		interfacesDeclared = new ArrayList<String>();
		importsDeclared = new ArrayList<String>();
		
		referencesToTypes = new HashMap<String, Integer>();
	}
	
	/**
	 * Allows the adding of a reference to a type used anywhere
	 * within the class
	 * 
	 * e.g. ArrayList<Figure> figs ......
	 * 
	 * This should give the param 'Figure'
	 */
	public void addReferenceToType(String typeName){
		if(referencesToTypes.get(typeName) != null){
			referencesToTypes.put(typeName, referencesToTypes.get(typeName)+1);
		}
		// Otherwise note first reference.
		else {
			referencesToTypes.put(typeName, 1);
		}
	}
	
	
	/**
	 * Returns the hashmap which tracks declaration names to how many times
	 * that declaration has been used.
	 * 
	 * @return Map of type names to times referenced by this object.
	 */
	public HashMap<String, Integer> getReferencesToTypes(){
		return referencesToTypes;
	}
	
	
	@Override
	public ArrayList<Method> getMethods() {
		return methodsDeclared;
	}

	
	@Override
	public boolean isInterface() {
		return this instanceof InterfaceDeclaration;
	}

	@Override
	public void addNewMethod(Method md) {
		methodsDeclared.add(md);
	}

	
	@Override
	public String getName() {
		return name;
	}
	
	
	@Override
	public boolean isClass(){
		return this instanceof ClassDeclaration;
	}
	
	
	@Override
	public boolean addInterface(String name){
		if(name != null) {
			if(!interfacesDeclared.contains(name)){
				interfacesDeclared.add(name);
				return true;
			}
		}
		return false;
	}

	
	@Override
	public ArrayList<String> getInterfaces(){
		return interfacesDeclared;
	}
	
	
	@Override
	public void setPackageName(String pName){
		if(pName != null){
			this.packageName = pName;
		}
	}
	
	
	@Override
	public boolean addImport(String importName){
		if(importName != null){
			importsDeclared.add(importName);
			return true;
		}
		return false;
	}
	
	
	@Override
	public String getPackageName(){
		return packageName;
	}
	
	
	@Override
	public ArrayList<String> getImports(){
		if(importsDeclared!=null) return importsDeclared;
		else return new ArrayList<String>();
	}
	
	
	@Override
	public String toString(){
		String s = "Name: " + name;

		s+="\n-----------------\n";
		
		
		return s;
	}
	
	
	public ArrayList<String> getOrderedReferenceTypes(){
		ArrayList<String> results = new ArrayList<String>();
		
		for(String key : referencesToTypes.keySet()){
			int i = 0;
			
			while(i < results.size()  && referencesToTypes.get(key) < referencesToTypes.get(results.get(i))){
				i++;
			}
			
			results.add(i, key);
		}
		
		return results;
	}
	
}
