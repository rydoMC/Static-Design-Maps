package Testing;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import Algorithms.DefaultAlgorithm;
import Model.ClassDeclaration;
import Model.Declaration;
import Model.InterfaceDeclaration;
import Model.Model;

public class ModelTests {

	
	/**
	 * MOD 001 && MOD 002
	 * 
	 * Unit: setDataset/checkDataset(ArrayList<Declaration> decls);
	 * 
	 * This test creates an array list suspected to be a list of declarations except
	 * contains nulls instead. It passes it to the model with an instance of default algorithm
	 * to see that the model handles these null references.
	 *
	 */
	@Test
	public void testWithNullDeclsList(){
		ArrayList<Declaration> decls = new ArrayList<Declaration>();
		
		for(int i = 0; i < 20; i++){
			decls.add(null);
		}
		
		assertTrue(decls.size() == 20);
		
		new Model(decls, new DefaultAlgorithm());
		
		assertTrue(decls.size() == 0);
	}
	
	
	/**
	 * MOD 003
	 * 
	 * Unit: checkDataset(ArrayList<Declaration> decls);
	 * 
	 * This unit test checks that the checkDataset does not remove any instances of a declaration object.
	 */
	@Test
	public void testWithNullAndValidDecls(){
		ArrayList<Declaration> decls = new ArrayList<Declaration>();
		
		for(int i = 0; i < 20; i++){
			decls.add(null);
		}
		
		
		//------ Mixture of Class Declarations and Interface Declarations
		for(int i = 1; i < 13; i++){
			decls.add(new ClassDeclaration("Class " + i, i % 2 == 0));
		}
		
		for(int i = 1; i < 9; i++){
			decls.add(new InterfaceDeclaration("Interface " + i));
		}
		
		// ----- End of adding declarations
		
		
		assertTrue(decls.size() == 40);
		
		new Model(decls, new DefaultAlgorithm());
		
		assertTrue(decls.size() == 20);
	}
	
	
	/**
	 * MOD 004
	 * 
	 * Unit: validateModel();
	 * 
	 * This unit tests checks that the validateModel() correctly executes
	 * over a given model data set.
	 * 
	 *  This is done by the algorithm initially containing no results and
	 *  when validateModel() on a valid data set within the mode, passes the
	 *  data set and the algorithms results update as a result to now contain 1.
	 */
	@Test
	public void testWithNullAlgorithm(){
		ArrayList<Declaration> decls = new ArrayList<Declaration>();
		decls.add(new ClassDeclaration("Test Class",false));
		
		Model m = new Model(decls, null);
		
		assertTrue(m.getResults().size() == 0);// pre conditions
		
		m.validateModel();
		
		assertTrue(m.getResults().size() == decls.size()); // output
	}
	
	
	/**
	 * MOD 005
	 * 
	 * Unit: setDataset(ArrayList<Declaration> decls);
	 * 
	 * This unit tests checks that setDataset correctly handles a null value
	 * by not flagging an exception and also by showing the algorithms
	 * results != null as this would only be in the case if the model
	 * passed the algorithm the value null.
	 */
	@Test 
	public void testWithNullDecls(){
		Model m = new Model(null, new DefaultAlgorithm()); // Constructor calls set dataset
		
		m.validateModel();
		
		assertNotNull(m.getResults());
	}

	
	/**
	 * MOD 006
	 * 
	 * Unit: setAlgorithm(iAlgorithm a);
	 * 
	 * Tests the model has handled the null reference for the algorithm.
	 * When null is passed the model should revert to creating an instance
	 * of the default algorithm and using that. To check that this is the case
	 * by calling validate model, it calls execute on algorithm reference which should
	 * not be null.
	 */
	@Test
	public void testWithNullDeclsAndAlgorithm(){
		Model m = new Model(null, null);
		
		m.validateModel();
		
		assertNotNull(m.getResults());
		assertTrue(m.getResults().size()==0);
	}
	

	
	/**
	 * MOD 007
	 * 
	 * Unit: getReferenceMap(String)
	 * 
	 * This method tests the model to try retrieve a reference map 
	 * for a declaration that doesn't exist within the model.
	 * 
	 * Therefore the retrieval should return null and so assertEquals to null is used.
	 */
	@Test
	public void notValidReferenceMapRetrieval(){
		Model m = new Model(null, null);
		
		assertEquals(m.getReferencesMap("Test pull"),null);
	}
	
	
	/**
	 * MOD 008
	 * 
	 * Unit: getReferenceMap(String s)
	 * 
	 * This method combines the retrieval of valid reference map
	 * requests from the model.
	 */
	@Test
	public void validAndNonValidReferenceMapRetrieval(){
		ArrayList<Declaration> decls = new ArrayList<Declaration>();
		
		decls.add(new ClassDeclaration("Class A", false));
		decls.add(new InterfaceDeclaration("Interface A"));
		decls.add(new ClassDeclaration("Class B",false));
		
		Model m = new Model(decls, null);
		
		assertTrue(m.getReferencesMap("Class A").size() == 0);
		assertTrue(m.getReferencesMap("Interface A").size() == 0);
		assertEquals(m.getReferencesMap("Test pull"),null);
	}
	
	
	/**
	 * MOD 009
	 * 
	 * Unit: setDataset(ArrayList<Declaration> decls);
	 * 
	 * This unit tests checks that setDataset correctly handles a
	 * valid empty array list of Declaration objects.
	 * 
	 * This is checked that by the data set being 0 in size
	 * should result if setDataset is correct then the
	 * model should return 0 results.
	 */
	@Test 
	public void testWithDecls(){
		Model m = new Model(new ArrayList<Declaration>(), new DefaultAlgorithm()); // Constructor calls set data set
		
		m.validateModel();
		
		assertEquals(m.getResults(),0);
	}
}