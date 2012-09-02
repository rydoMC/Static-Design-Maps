package Jung;

/**
 * This class represents a node on the graph for JUNG implementations.
 * 
 * Its purpose is to overcome the allowing multiple nodes to contain the same name.
 * If JUNG uses a graph of String,String then errors are flown when classes to be shown
 * on the map already exist else where on the map.
 *
 * @author Ryan McNulty
 * @date 2 Mar 2012
 * @organisation Computer and Information Science, Strathclyde University, Glasgow, Scotland.
 */
public class Vertex {
	private String name;
	
	public Vertex(String name){
		this.name = name;
	}
	
	
	@Override
	public String toString(){
		return name;
	}
}
