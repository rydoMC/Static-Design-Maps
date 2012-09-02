package Testing;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import Algorithms.Algorithm;
import Algorithms.DefaultAlgorithm;
import Model.ClassDeclaration;
import Model.Declaration;
import Model.InterfaceDeclaration;
import Parsing.JParserConverter;
import Rules.ConsecutiveExtendRule;
import Rules.ReferenceTypeRule;
import Rules.ReturnTypeRule;
import Rules.SuperTypeRule;

public class AlgorithmTests {

	// ----------- addRule tests
	/**
	 * ALGO001
	 * 
	 * This rule tests that a rule of null cannot be added.
	 * 
	 * The white box element here is to test that the private method getMapWeight and public
	 * method validRuleToMapWeight are correct and fit for purpose.
	 */
	@Test
	public void addNullRuleOnly(){
		new Algorithm() {
			
			@Override
			public void setRulesAndWeights() {
				assertFalse(addRule(null, 1)); // Adding of this rule should fail in the method because its null
			}
		};
	}
	
	
	/**
	 * ALGO002.
	 * 
	 * This test checks that the addition of valid rules are actually
	 * added to the algorithms collection of rules.
	 */
	@Test
	public void addValidRules(){
		new Algorithm() {
			
			@Override
			public void setRulesAndWeights() {
				assertTrue(addRule(new ReturnTypeRule(), 0.5));
				assertTrue(addRule(new SuperTypeRule(), 0.3));
				assertTrue(addRule(new ReferenceTypeRule(), 0.2));
			}
		};
	}
	
	
	/**
	 * ALGO003
	 * 
	 * Combinations of valid and non valid rules
	 */
	@Test
	public void addValidAndNonValidRules(){
		new Algorithm() {
			
			@Override
			public void setRulesAndWeights() {
				assertTrue(addRule(new ReturnTypeRule(), 0.5));
				assertTrue(addRule(new SuperTypeRule(), 0.3));
				assertTrue(addRule(new ReferenceTypeRule(), 0.2));
				assertFalse(addRule(null, 0.1));
				assertFalse(addRule(new ConsecutiveExtendRule(),0.2));
			}
		};
	}
	

	
	// ------------------ Weight Map tests
		
	/**
	 * ALGO004.
	 * 
	 * Adds no rules
	 */
	@Test
	public void noRulesTest() {
		Algorithm a = new Algorithm() {

			@Override
			public void setRulesAndWeights() {
				// Intentionally empty
			}
		};

		// No rules and therefore should return false.
		assertFalse(a.validRuleToWeightMap());
	}
		
		
	/**
	 * ALGO005
	 * 
	 * Valid map test one.
	 */
	@Test
	public void validMapToWeightTestOne(){
		Algorithm a = new Algorithm() {
			
			@Override
			public void setRulesAndWeights() {
				assertTrue(addRule(new ReturnTypeRule(), 0.5));
				assertTrue(addRule(new SuperTypeRule(), 0.3));
				assertTrue(addRule(new ReferenceTypeRule(), 0.2));
			}
		};
		
		assertTrue(a.validRuleToWeightMap());
	}
	
	
	/**
	 * ALGO006
	 * 
	 * Valid map test two.
	 */
	@Test
	public void validMapToWeightTestTwo(){
		Algorithm a = new Algorithm() {
			
			@Override
			public void setRulesAndWeights() {
				addRule(new ReturnTypeRule(), 1.0);
			}
		};
		assertTrue(a.validRuleToWeightMap());
	}


	
	
	
	// ------------------ result checking
	
	/**
	 * ALGO007
	 * 
	 * Checks that when the algorithms results
	 * do not equal null when algorithm not ran.
	 */
	@Test
	public void checkResultsNotNull(){
		Algorithm a = new DefaultAlgorithm();
		assertNotNull(a.getResults());
	}
	
	
	/**
	 * ALGO008
	 * 
	 * Checks that when the algorithms results
	 * do not equal null when algorithm has been executed.
	 */
	@Test
	public void checkResultsEmpty(){
		Algorithm a = new DefaultAlgorithm();
		a.setDataset(null);
		a.execute();
		assertNotNull(a.getResults());
	}
	
	
	/**
	 * ALGO009
	 * 
	 * This white box test is to testing the setResults method
	 * within the algorithm is ordering the results in the correct way.
	 */
	@Test 
	public void checkResultsInCorrectOrder(){
		Declaration a = new ClassDeclaration("Animal", true);
		Declaration b = new ClassDeclaration("Fox",false);
		Declaration c = new ClassDeclaration("Rabbit", false);
		
		// Set super classes
		b.addSuperClass("Animal");
		c.addSuperClass("Animal");
		
		// ---------- Next data
		Declaration d = new InterfaceDeclaration("Algorithm");
		Declaration e = new InterfaceDeclaration("Test Algorithm");
		
		e.addSuperClass("Algorithm");
		
		ArrayList<Declaration> data = new ArrayList<Declaration>();
		data.add(a);
		data.add(b);
		data.add(c);
		data.add(d);
		data.add(e);
		
		Algorithm algo = new Algorithm() {
			@Override
			public void setRulesAndWeights() {
				assertTrue(addRule(new SuperTypeRule(), 1));
			}
		};
		
		algo.setDataset(data);
		algo.execute();
		
		// animal is used twice as a super class and interface algorithm is used once
		// Animal should thereby score the most and be the first results and Algorithm should be second
		assertTrue(algo.getResults().get(0).equalsIgnoreCase("Animal"));
		assertTrue(algo.getResults().get(1).equalsIgnoreCase("algorithm"));
		assertEquals(algo.getResults().size(), data.size());
	}
	
	
	
	
	
	
	
	
	// ------------- Execution Tests
	
	
	/**
	 * ALGO010
	 * 
	 * Test ensures that the algorithm results the same
	 * number of results that there are declarations being checked.
	 * Ensures that algorithm contains a result for each declaration
	 * it has been given.
	 */
	@Test
	public void validDatasetExecutionTest(){
		Algorithm a = new DefaultAlgorithm();
		ArrayList<Declaration> decls = getTestData();
		a.setDataset(decls);
		
		
		assertTrue(a.getResults().size() == 0);
		
		a.execute();
		
		assertTrue(a.getResults().size() == decls.size());
	}
	
	
	
	/**
	 * ALGO011
	 * 
	 * This test ensures that if the user inputs a non valid map
	 * then they can expect 0 results.
	 */
	@Test
	public void notValidMapExecutionTest(){
		Algorithm a = new Algorithm() {
			@Override
			public void setRulesAndWeights() {
				addRule(new ReturnTypeRule() , 0); // Not valid map weight
			}
		};
		
		ArrayList<Declaration> decls = getTestData();
		a.setDataset(decls);
		a.execute();
		
		assertTrue(a.getResults().size () == 0);
	}
	
	
	/**
	 * This method tests that the algorithm handles a null data set
	 * If the user inputs null then the user can expect 0 results
	 */
	@Test
	public void executeOverNull(){
		Algorithm a = new DefaultAlgorithm();
		
		a.setDataset(null); // If passed null the algorithm catches this and sets instead an empty array list.
		
		assertTrue(a.validRuleToWeightMap()); // Weight should still balance
		
		
		a.execute();
		
		assertTrue(a.getResults().size() == 0);
	}
	
	
	/**
	 * ALGO 013
	 * 
	 * Establishes default algorithm and passes list containing nulls.
	 */
	@Test
	public void executeOverNullDataList(){
		Algorithm a = new DefaultAlgorithm();
		
		ArrayList<Declaration> nullList = new ArrayList<Declaration>();
		
		for(int i = 0; i < 20; i++){
			nullList.add(null);
		}
		
		a.setDataset(nullList);
		
		assertTrue(a.getResults().size() == 0);
		a.execute();
		assertTrue(a.getResults().size() == 0);
	}

	
	
	// ----------------- Retrieval of data for tests
	private ArrayList<Declaration> getTestData(){
		JParserConverter jp = new JParserConverter("/Users/Ryan/Eclipse/Eclipse Workspace/2nd Year/Boggle");
		jp.processFiles();
		
		return jp.retrieveDeclarations();
	}
}
