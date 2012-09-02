package Testing;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.junit.Test;

import Model.ClassDeclaration;
import Model.Declaration;
import Parsing.JParserConverter;

public class JParserConverterTests {
	// do tests for processFiles, retrieveDeclarations,
	
	/**
	 * JCP001
	 * 
	 * Unit: retrieveDeclarations.
	 * 
	 * Tests unit to ensure it only ever returns a retrieveDeclarations even without processing.
	 */
	@Test
	public void retrieveForNonExistentFile(){
		JParserConverter jp = new JParserConverter("/User/Non existent file path");
		
		assertNotNull(jp.retrieveDeclarations());
		assertTrue(jp.retrieveDeclarations().size()==0);
	}
	

	/**
	 * JCP002
	 * 
	 * Unit retrieveDeclarations
	 * 
	 * Test unit ensures that the retrieveDeclarations returns a list != 0.
	 */
	@Test
	public void retrieveForExistentFile(){
		JParserConverter jp = new JParserConverter("/Users/Ryan/Eclipse/Eclipse Workspace/1st Year/FoxesAndRabbits/src/");
		jp.processFiles();
		
		assertNotNull(jp.retrieveDeclarations());
		assertTrue(jp.retrieveDeclarations().size() != 0);
	}
	
	
	/**
	 * JCP 003
	 * 
	 *  unit: processFiles()
	 *  
	 *  Tests that the converter has successfully converted a java source file to a declaration object by using the 
	 *  knowledge of the source code itself.
	 */
//	@Test 
	public void convertToDeclarationTest(){
		JOptionPane.showMessageDialog(null, "This test involves checking that JParserConverter converts JavaDrawApp.java from JHotDraw correctly\nTherefore to carry out this test select JavaDrawApp.java from JHotDraw provided");
		
		JFileChooser jf = new JFileChooser();
		if(jf.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
		
	
			JParserConverter jp = new JParserConverter(jf.getSelectedFile());
			jp.processFiles();
			
			assertTrue(jp.retrieveDeclarations().size() == 1);
			
			Declaration suspectedJDrawApp = jp.retrieveDeclarations().get(0);

			assertTrue(suspectedJDrawApp.getName().equalsIgnoreCase(
					"javadrawapp"));

			// Import checks
			assertTrue(suspectedJDrawApp.getImports().size() == 29); // check
																		// all
																		// are
																		// accounted
																		// for
			assertTrue(suspectedJDrawApp.getImports().get(0)
					.equalsIgnoreCase("java.awt.event.ActionEvent")); // check
																		// the
																		// first
																		// import
			assertTrue(suspectedJDrawApp.getImports().get(28)
					.equalsIgnoreCase("CH.ifa.draw.util.Animatable")); // check
																		// the
																		// last
																		// one

			// Package name check
			assertTrue(suspectedJDrawApp.getPackageName().equalsIgnoreCase(
					"ch.ifa.draw.samples.javadraw"));

			// Methods check
			assertTrue(suspectedJDrawApp.getMethods().size() == 13);
			assertTrue(suspectedJDrawApp.getMethods().get(0).getMethodName()
					.equalsIgnoreCase("main"));
			assertTrue(suspectedJDrawApp.getMethods().get(0).getParams().get(0)
					.equalsIgnoreCase("String[] args"));

			// Checks super class name
			assertTrue(((ClassDeclaration) suspectedJDrawApp).getSuperClass()
					.equalsIgnoreCase("DrawApplication"));
		}
	}
	

	
	
	/**
	 * This black box test shows that the parser can handle null entries
	 */
	@Test
	public void convertFromEmptyString(){
		JParserConverter jp = new JParserConverter("");
		jp.processFiles();
		
		assertTrue(jp.retrieveDeclarations().size() == 0);
	}
}
