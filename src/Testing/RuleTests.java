package Testing;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import Model.ClassDeclaration;
import Model.Declaration;
import Model.InterfaceDeclaration;
import Model.Method;
import Rules.ConsecutiveExtendRule;
import Rules.CreationEvaluationRule;
import Rules.ReferenceTypeRule;
import Rules.ReturnTypeRule;
import Rules.SuperTypeRule;

public class RuleTests {
// two test cases for each rule

	// ---------------- Consecutive extend rule
	/**
	 * RUL 001
	 * 
	 * Unit: processRuleOnNode (ConsecutiveExtendRule class)
	 * 
	 * Test checks The score of every declaration made.
	 */
	@Test
	public void consecOne(){
		ArrayList<Declaration> allDecls = new ArrayList<Declaration>();
		
		Declaration a = new ClassDeclaration("Sub class one",false);
		Declaration b = new ClassDeclaration("Sub class two", false);
		Declaration c = new ClassDeclaration("Abstract class one",true);
		a.addSuperClass(c.getName());
		b.addSuperClass(c.getName());
		// C is a super class to A and B
		
		Declaration d = new ClassDeclaration("Abstract class two", true);
		Declaration e = new ClassDeclaration("Sub class three", false);
		e.addSuperClass(d.getName());
		// D is a super class to E
		
		Declaration f = new InterfaceDeclaration("Interface One");
		Declaration g = new InterfaceDeclaration("Interface Two");
		Declaration h = new InterfaceDeclaration("Interface Three");
		
		h.addSuperClass(g.getName());
		g.addSuperClass(f.getName());
		// F is super to G and G is super to H
		
		allDecls.add(a);
		allDecls.add(b);
		allDecls.add(c);
		allDecls.add(d);
		allDecls.add(e);
		allDecls.add(f);
		allDecls.add(g);
		allDecls.add(h);
		
		
		ConsecutiveExtendRule rule = new ConsecutiveExtendRule();
		
		// A's score
		rule.processRuleOnNode(a, allDecls);
		assertEquals(-1, rule.getScore());
		
		
		// B's score
		rule.processRuleOnNode(b, allDecls);
		assertEquals(-1, rule.getScore());
		
		rule.processRuleOnNode(c, allDecls);
		assertEquals(0, rule.getScore());
		
		rule.processRuleOnNode(d, allDecls);
		assertEquals(0, rule.getScore());
		
		rule.processRuleOnNode(e, allDecls);
		assertEquals(-1, rule.getScore());
		
		rule.processRuleOnNode(f, allDecls);
		assertEquals(0, rule.getScore());
		
		rule.processRuleOnNode(g, allDecls);
		assertEquals(-1, rule.getScore());

		rule.processRuleOnNode(h, allDecls);
		assertEquals(-2, rule.getScore());
		
		
	}
	
	
	/**
	 * RUL 002
	 * 
	 * Unit: processRuleOnNode (ConsecutiveExtendRule class)
	 * 
	 * Test checks the scores for three class declarations.
	 */
	@Test
	public void consecTwo(){
		ArrayList<Declaration> allDecls = new ArrayList<Declaration>();
		
		Declaration a = new ClassDeclaration("Class one", false);
		Declaration b = new ClassDeclaration("Class two", false);
		Declaration c = new ClassDeclaration("Class three", false);
		Declaration d = new ClassDeclaration("Class four", false);
		Declaration e = new ClassDeclaration("Class five", false);
		Declaration f = new ClassDeclaration("Class six", false);
		
		// Create one long hierarchy
		f.addSuperClass(e.getName());
		e.addSuperClass(d.getName());
		d.addSuperClass(c.getName());
		c.addSuperClass(b.getName());
		b.addSuperClass(a.getName());
		
		// Add all to list
		allDecls.add(f);
		allDecls.add(e);
		allDecls.add(d);
		allDecls.add(c);
		allDecls.add(b);
		allDecls.add(a);
		
		ConsecutiveExtendRule rule = new ConsecutiveExtendRule();
		
		rule.processRuleOnNode(a, allDecls);
		assertEquals(0, rule.getScore());
		
		rule.processRuleOnNode(e, allDecls);
		assertEquals(-4, rule.getScore());
		
		rule.processRuleOnNode(f, allDecls);
		assertEquals(-5, rule.getScore());
	}
	
	

	
	
	/**
	 * RUL 003
	 * 
	 * Unit: processRuleOnNode(Declaration beingScored, ArrayList<Declaration> allDecls);
	 * 
	 * This unit tests the unit to handle a null value. 
	 * This cannot happen as by this stage of executing rules
	 * the model will have already filtered these.
	 */
	@Test
	public void consecThree(){
		ArrayList<Declaration> allDecls = new ArrayList<Declaration>();
		
		Declaration a = new ClassDeclaration("Class one", false);
		Declaration b = new ClassDeclaration("Class two", false);
		Declaration c = new ClassDeclaration("Class three", false);
		Declaration d = new ClassDeclaration("Class four", false);
		Declaration e = new ClassDeclaration("Class five", false);
		Declaration f = new ClassDeclaration("Class six", false);
		
		// Create one long hierarchy
		f.addSuperClass(e.getName());
		e.addSuperClass(d.getName());
		d.addSuperClass(c.getName());
		c.addSuperClass(b.getName());
		b.addSuperClass(a.getName());
		
		// Add all to list
		allDecls.add(f);
		allDecls.add(e);
		allDecls.add(d);
		allDecls.add(c);
		allDecls.add(b);
		allDecls.add(a);
		
		ConsecutiveExtendRule rule = new ConsecutiveExtendRule();
		
		rule.processRuleOnNode(null, allDecls);
		
		assertEquals(0,rule.getScore());
	}
	
	
	/**
	 * RUL 004
	 * 
	 * Class: CreationEvaluationRule
	 * Unit: processRuleOnNode(Declaration beingScored, ArrayList<Declaration> allDecls)
	 * 
	 * This test creates instance of class declarations and interface declarations.
	 * It adds new object references to two of the class declarations.
	 */
	@Test
	public void creationOne(){
		ArrayList<Declaration> allDecls = new ArrayList<Declaration>();
		
		ClassDeclaration a = new ClassDeclaration("Class A", false);
		ClassDeclaration b = new ClassDeclaration("Class B", false);
		ClassDeclaration c = new ClassDeclaration("Class C", false);
		ClassDeclaration d = new ClassDeclaration("Class D", false);
		
		Declaration e = new InterfaceDeclaration("Interface E");
		Declaration f = new InterfaceDeclaration("Interface F");
		
		// Add all to list
		allDecls.add(f);
		allDecls.add(e);
		allDecls.add(d);
		allDecls.add(c);
		allDecls.add(b);
		allDecls.add(a);
		
		
		// Add four new objects to A
		a.addReferenceToNewObject("Interface E");
		a.addReferenceToNewObject("Interface F");
		a.addReferenceToNewObject("Class B");
		a.addReferenceToNewObject("Class D");
		
		// Add two new objects to C
		c.addReferenceToNewObject("Class A");
		c.addReferenceToNewObject("Interface B"); // Shouldn't happen but can handle.
		
		
		CreationEvaluationRule rule = new CreationEvaluationRule();
		
		
		rule.processRuleOnNode(a, allDecls);
		// A has four new references but is reference new once by C
		assertEquals(3, rule.getScore());
		
		
		rule.processRuleOnNode(d, allDecls);
		// D is created by A but does not create any itself
		assertEquals(-1, rule.getScore());
		
		
		// Should be one as interfaces cannot be 'created' a concrete implementation relies behind it.
		rule.processRuleOnNode(c, allDecls);
		assertEquals(1,rule.getScore());
	}
	
	
	/**
	 * RUL 005
	 * 
	 * Class: CreationEvaluationRule
	 * Unit: processRuleOnNode(Declaration beingScored, ArrayList<Declaration> allDecls)
	 * 
	 * Tests that all interfaces give a score of 0.
	 */
	@Test
	public void creationTwo(){
		ArrayList<Declaration> allDecls = new ArrayList<Declaration>();
		
		ClassDeclaration a = new ClassDeclaration("Class A", false);
		ClassDeclaration b = new ClassDeclaration("Class B", false);
		ClassDeclaration c = new ClassDeclaration("Class C", false);
		ClassDeclaration d = new ClassDeclaration("Class D", false);
		
		Declaration e = new InterfaceDeclaration("Interface E");
		Declaration f = new InterfaceDeclaration("Interface F");
		
		// Add all to list
		allDecls.add(f);
		allDecls.add(e);
		allDecls.add(d);
		allDecls.add(c);
		allDecls.add(b);
		allDecls.add(a);
		
		
		// Add four new objects to A
		a.addReferenceToNewObject("Class B");
		a.addReferenceToNewObject("Class D");
		
		// Add two new objects to C
		c.addReferenceToNewObject("Class A");
		
		
		CreationEvaluationRule rule = new CreationEvaluationRule();
		
		// Score the interfaces to ensure 0.
		rule.processRuleOnNode(e, allDecls);
		assertEquals(0,rule.getScore());
		
		rule.processRuleOnNode(f, allDecls);
		assertEquals(0,rule.getScore());
	}
	
	
	/**
	 * RUL 006
	 * 
	 * Class: ReferenceTypeRule
	 * Unit: processRuleOnNode(Declaration d, ArrayList<Declaration> allDecls);
	 * 
	 * This test checks that the reference type rule works for three different
	 * instances of Class Declaration correctly.
	 */
	@Test
	public void referenceOne(){
		ArrayList<Declaration> allDecls = new ArrayList<Declaration>();
		
		ClassDeclaration a = new ClassDeclaration("Class A", false);
		ClassDeclaration b = new ClassDeclaration("Class B", false);
		ClassDeclaration c = new ClassDeclaration("Class C", false);
		ClassDeclaration d = new ClassDeclaration("Class D", false);
		
		Declaration e = new InterfaceDeclaration("Interface E");
		Declaration f = new InterfaceDeclaration("Interface F");
		
		// Add all to list
		allDecls.add(f);
		allDecls.add(e);
		allDecls.add(d);
		allDecls.add(c);
		allDecls.add(b);
		allDecls.add(a);
		
		
		// Add reference to A and D
		for(Declaration p : allDecls){
			p.addReferenceToType(a.getName());
			p.addReferenceToType(d.getName());
		}
		
		// Add reference to an interface
		for(Declaration p : allDecls){
			p.addReferenceToType(e.getName());
		}
		
		
		ReferenceTypeRule rule = new ReferenceTypeRule();
		
		
		// Both A and D have the same number of references.
		rule.processRuleOnNode(a, allDecls);
		assertEquals(allDecls.size(), rule.getScore());
		
		rule.processRuleOnNode(d, allDecls);
		assertEquals(allDecls.size(), rule.getScore());
		
		
		// Add one more reference to C
		c.addReferenceToNewObject(e.getName());
		
		// Score should be allDecls size + 1
		rule.processRuleOnNode(e, allDecls);
		assertEquals(allDecls.size()+1, rule.getScore());
		
	}
	
	
	/**
	 * RUL 007
	 * 
	 * Class: ReferenceTypeRule
	 * Unit: processRuleOnNode(Declaration d, ArrayList<Declaration> allDecls);
	 * 
	 * This test checks that the unit scores NULL as 0.
	 */
	@Test
	public void referenceTwo(){
		ArrayList<Declaration> allDecls = new ArrayList<Declaration>();
		
		ClassDeclaration a = new ClassDeclaration("Class A", false);
		ClassDeclaration b = new ClassDeclaration("Class B", false);
		ClassDeclaration c = new ClassDeclaration("Class C", false);
		ClassDeclaration d = new ClassDeclaration("Class D", false);
		
		Declaration e = new InterfaceDeclaration("Interface E");
		Declaration f = new InterfaceDeclaration("Interface F");
		
		// Add all to list
		allDecls.add(f);
		allDecls.add(e);
		allDecls.add(d);
		allDecls.add(c);
		allDecls.add(b);
		allDecls.add(a);
		
		
		// Add reference to A and D
		for(Declaration p : allDecls){
			p.addReferenceToType(a.getName());
			p.addReferenceToType(d.getName());
		}
		
		// Add reference to an interface
		for(Declaration p : allDecls){
			p.addReferenceToType(e.getName());
		}
		
		
		ReferenceTypeRule rule = new ReferenceTypeRule();
		
		
		// Check F's score, should be 0 as no other decl references it.
		rule.processRuleOnNode(f, allDecls);
		assertEquals(0,rule.getScore());
	}
	
	
	
	/**
	 * RUL 008
	 * 
	 * Class: ReturnTypeRule
	 * Unit: processRuleOnNode(Declaration d, ArrayList<Declaration> allDecls);
	 * 
	 * This test checks that the unit scores correctly for a class declaration instance.
	 */
	@Test
	public void returnOne(){
		Method methodOne = new Method("getName");
		Method methodTwo = new Method("getOwner");
		Method methodThree = new Method("getFriends");
		
		methodOne.setReturnType("String"); // value is 6
		methodTwo.setReturnType("Object"); // value is 10
		methodThree.setReturnType("Object[]"); // value is 10x5 = 50
		
		ClassDeclaration classInstance = new ClassDeclaration("Dog", false);
		classInstance.addNewMethod(methodTwo);
		classInstance.addNewMethod(methodOne);
		classInstance.addNewMethod(methodThree);
		
		
		ReturnTypeRule rule = new ReturnTypeRule();
		
		rule.processRuleOnNode(classInstance, null);
		assertEquals(66,rule.getScore());
	}
	
	/**
	 * RUL 009
	 * 
	 * Class: ReturnTypeRule
	 * Unit: processRuleOnNode(Declaration d, ArrayList<Declaration> allDecls);
	 * 
	 * This test checks that the unit scores correctly for a interface declaration instance.
	 */
	@Test
	public void returnTwo(){
		Method methodOne = new Method("getName");
		Method methodTwo = new Method("getOwner");
		Method methodThree = new Method("getFriends");
		
		methodOne.setReturnType("String"); // value is 6
		methodTwo.setReturnType("String"); // value is 6
		methodThree.setReturnType("HashMap<String,String>"); // value is 10x3 = 30, its an object and is a map
		
		InterfaceDeclaration interfaceInstance = new InterfaceDeclaration("Animal");
		interfaceInstance.addNewMethod(methodTwo);
		interfaceInstance.addNewMethod(methodOne);
		interfaceInstance.addNewMethod(methodThree);
		
		
		ReturnTypeRule rule = new ReturnTypeRule();
		
		rule.processRuleOnNode(interfaceInstance, null);
		assertEquals(42,rule.getScore());
	}
	
	
	/**
	 * RUL 010
	 * 
	 * Class: ReturnTypeRule
	 * Unit: processRuleOnNode(Declaration d, ArrayList<Declaration> allDecls);
	 * 
	 * This test checks that the unit scores correctly for a declaration instance
	 * which contains no methods.
	 */
	@Test
	public void returnThree(){
		InterfaceDeclaration interfaceInstance = new InterfaceDeclaration("Animal");

		ReturnTypeRule rule = new ReturnTypeRule();
		
		rule.processRuleOnNode(interfaceInstance, null);
		assertEquals(0,rule.getScore());
	}
	
	
	/**
	 * RUL 011
	 * 
	 * Class: SuperTypeRule
	 * Unit: processRuleOnNode(Declaration d, ArrayList<Declaration> allDecls);
	 * 
	 * Tests that the unit scores correctly for this class by evaluating three different instances.
	 */
	@Test
	public void superOne(){
		ArrayList<Declaration> allDecls = new ArrayList<Declaration>();
		
		Declaration a = new InterfaceDeclaration("Interface A");
		Declaration b = new InterfaceDeclaration("Interface B");
		Declaration c = new InterfaceDeclaration("Interface C");
		Declaration d = new InterfaceDeclaration("Interface D");
		
		allDecls.add(a);
		allDecls.add(b);
		allDecls.add(c);
		allDecls.add(d);
		
		
		// C and A used as super types here.
		d.addSuperClass(c.getName());
		c.addSuperClass(a.getName());
		
		
		// A used as super type here.
		b.addSuperClass(a.getName());
		

		
		SuperTypeRule rule = new SuperTypeRule();
		
		
		rule.processRuleOnNode(a, allDecls);
		assertEquals(2,rule.getScore());
		
		rule.processRuleOnNode(b, allDecls);
		assertEquals(0,rule.getScore());
		
		rule.processRuleOnNode(c, allDecls);
		assertEquals(1,rule.getScore());
	}
	
	
	/**
	 * RUL 012
	 * 
	 * Class: SuperTypeRule
	 * Unit: processRuleOnNode(Declaration d, ArrayList<Declaration> allDecls);
	 * 
	 * Tests that the unit scores correctly for this class by evaluating three different instances.
	 */
	@Test
	public void superTwo(){
		ArrayList<Declaration> allDecls = new ArrayList<Declaration>();
		
		Declaration a = new ClassDeclaration("Class A",false);
		Declaration b = new ClassDeclaration("Class B",false);
		Declaration c = new ClassDeclaration("Class C",false);
		Declaration d = new ClassDeclaration("Class D",false);
		
		allDecls.add(a);
		allDecls.add(b);
		allDecls.add(c);
		allDecls.add(d);
		
		
		// B and D used as super types here.
		c.addSuperClass(b.getName());
		a.addSuperClass(d.getName());
		
		
		SuperTypeRule rule = new SuperTypeRule();
	
		
		rule.processRuleOnNode(b, allDecls);
		assertEquals(1,rule.getScore());
		
		rule.processRuleOnNode(d, allDecls);
		assertEquals(1,rule.getScore());
	}
}
