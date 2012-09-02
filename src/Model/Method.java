package Model;

import java.util.ArrayList;

/**
 * This class provides the model component for representing
 * a method declaration within the model. It contains the attributes
 * of the method however does not contain the method body itself.
 *
 * @author Ryan McNulty
 * @date 2 Jan 2012
 * @organisation Computer and Information Science, Strathclyde University, Glasgow, Scotland.
 */
public class Method {
	// Method attribute fields.
	private String name;
	private String returnType;
	private ArrayList<String> params;
	
	/**
	 * Default constructor
	 * 
	 * @param name Name of method as in source file.
	 */
	public Method(String name){
		this.name = name;
	}
	
	
	/**
	 * Adds a new parameter used by this method
	 * e.g. 'String name'
	 * @param param String containing the type and name of variable.
	 */
	public void addParam(String param){
		// Params init here to ensure there are not any unused lists e.g. methods that have no params do init their list.
		if(params == null) params = new ArrayList<String>();
		params.add(param);
	}
	
	/**
	 * Sets this methods return type
	 * @param type String containing the type it returns.
	 */
	public void setReturnType(String type){
		if(type != null) returnType = type;
	}
	
	/**
	 * @return Returns all declared parameters
	 */
	public ArrayList<String> getParams(){
		return params;
	}
	
	/**
	 * @return Return type of method
	 */
	public String getReturnType(){
		return returnType;
	}
	
	/**
	 * Returns method name as set at constructor
	 * @return String containing method name as in src code.
	 */
	public String getMethodName(){
		return name;
	}
	
	@Override
	public String toString(){
		String s = returnType;
		
		s+= " " + name +"(";
		
		if(params!=null){
			for(String n: params){
				s+= n + ",";
			}
		}
		
		if(s.charAt(s.length()-1) == ',')	s=s.substring(0,s.length()-1);
		s+= ")";
		
		return s;
	}
}
