package Framework;

import java.util.ArrayList;

import Model.Declaration;


/**
 * This interface provides all methods which are required by a converter class.
 * 
 * Converter classes are to allow different parsing tools to be used given they adhere
 * to this interface.
 *
 * @author Ryan McNulty
 * @date 27 Dec 2011
 * @organisation Computer and Information Science, Strathclyde University, Glasgow, Scotland.
 */
public interface iModelConverter {

	/**
	 * Returns all declarations found
	 * 
	 * @return ArrayList of Model.Declaration objects
	 */
	public ArrayList<Declaration> retrieveDeclarations();
	
	/**
	 * Processes all the files found by that
	 * converter according to how the parsing
	 * library behind it works.
	 */
	public void processFiles();
}
