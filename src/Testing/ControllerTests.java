package Testing;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import org.junit.Test;

import Algorithms.DefaultAlgorithm;
import Controller.DefaultController;
import Framework.iController;
import Framework.iView;
import Jung.DefaultView;
import Model.Declaration;
import Model.Model;
import Parsing.JParserConverter;

public class ControllerTests {

	// --------------- White box Tests
	
	/**
	 * CON001
	 * 
	 * This white box test checks the filtering function that is used in the controller
	 * each time new model objects are loaded to the model.
	 * 
	 * Therefore to test this the data must be fed to the model in its un-filtered state.
	 * Then reselect the same data going through the controller and check for the same 
	 * Java standard reference to JTextField.
	 */
	@Test
	public void checkFilterMapsFunction(){
		Model model = new Model(getTestData(), new DefaultAlgorithm());
		iView view = new DefaultView(model){
			@Override
			public String getSourceFileDirectoryPath(){
				return "/Users/Ryan/Eclipse/Eclipse Workspace/4th Year/JHD_2011/";
			}
		};
		iController controller = new DefaultController(view,model);		
		assertTrue("DrawApplication doesn't contains references to JTextField and it should",model.getReferencesMap("DrawApplication").containsKey("JTextField"));
		
		controller.actionPerformed(new ActionEvent(this, 1, "process")); // Involves reselecting the source files
		
		assertFalse("DrawApplication has references to JTextField after its been filtered",model.getReferencesMap("DrawApplication").containsKey("JTextField"));
	}
	
	
	
	
	
	// --------------- Black box tests
	/**
	 * CON002
	 * 
	 * Shows the model process results correctly by refreshing them each time.
	 */
	@Test
	public void checkControllerLoadsNewDeclarations(){
		ArrayList<Declaration> data = getTestData();
		Model model = new Model(data, new DefaultAlgorithm());
		iView view = new DefaultView(model){
			@Override
			public String getSourceFileDirectoryPath(){
				return "/Users/Ryan/Eclipse/Eclipse Workspace/4th Year/JHD_2011/";
			}
		};
		iController controller = new DefaultController(view,model);
		
		controller.actionPerformed(new ActionEvent(this, 1, "process")); // Involves reselecting the same data
		ArrayList<Declaration> resultsOne = model.getResults();
		
		controller.actionPerformed(new ActionEvent(this, 1, "process")); // Involves selecting new data
		ArrayList<Declaration> resultsTwo = model.getResults();
		
		assertNotSame(resultsOne,resultsTwo);
	}
	
	
	
	/**
	 * CON003
	 * 
	 * Checks the controller action performed method to call the view to 
	 * load a results window.
	 */
	@Test
	public void loadListResultsWindow(){
		Model m = new Model(getTestData(), new DefaultAlgorithm());
		DefaultController c = new DefaultController(new DefaultView(m), m);
		
		c.actionPerformed(new ActionEvent(this, 1, "listresults"));
	}
	
	

	/**
	 * CON004
	 * 
	 * Test condition in action performed for shutting down system.
	 */
	//@Test
	public void shutDown(){
		Model m = new Model(getTestData(), new DefaultAlgorithm());
		DefaultController c = new DefaultController(new DefaultView(m), m);
		
		c.actionPerformed(new ActionEvent(this, 1, "shutdown"));
	}
	
	
	
	// ----------------- Retrieval of data for tests
	private ArrayList<Declaration> getTestData(){
		JParserConverter jp = new JParserConverter("/Users/Ryan/Eclipse/Eclipse Workspace/4th Year/JHD_2011/src/");
		jp.processFiles();
		
		return jp.retrieveDeclarations();
	}
}
