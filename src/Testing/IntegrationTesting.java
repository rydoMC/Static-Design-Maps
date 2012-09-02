package Testing;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.event.ActionEvent;
import java.util.HashMap;

import org.junit.Test;

import Controller.DefaultController;
import Framework.iController;
import Framework.iView;
import Jung.DefaultView;
import Model.Model;
import Parsing.JParserConverter;

public class IntegrationTesting {

	/**
	 * INT 001
	 * 
	 * Shows Java parser converter working with the model
	 * to allow the model to take over the declaration set.
	 */
	@Test
	public void testOne(){
		Model m = new Model(null,null);
		assertNull(m.getReferencesMap("figure"));
		// Shows the model has not declaration yet
		
		JParserConverter jpc = new JParserConverter("/Users/Ryan/Eclipse/Eclipse Workspace/4th Year/JHD_2011/");
		jpc.processFiles();
		

		m.setDataset(jpc.retrieveDeclarations());
		
		HashMap<String,Integer> map = m.getReferencesMap("figure");
		
		// Shows that the data from the parser has been passed to the model correctly.
		assertNotNull(map);
		assertTrue(map.get("FigureChangeListener") != 0);
	}
	
	
	/**
	 * This shows all the components working together.
	 * 
	 * The high level view is it shows the model view and controller working together.
	 * 
	 * Behind the scenes the controller is working with the model converter.
	 * Model is working with the algorithm to produce the results.
	 */
	@Test
	public void testTwo(){
		Model m = new Model(null,null);
		iView view = new DefaultView(m){
			@Override
			public String getSourceFileDirectoryPath(){
				return "/Users/Ryan/Eclipse/Eclipse Workspace/4th Year/JHD_2011/";
			}
		};
		
		iController cont = new DefaultController(view,m);
		
		assertTrue(m.getResults().size() == 0);
		
		cont.actionPerformed(new ActionEvent(this, 0, "process"));
		
		assertTrue(m.getResults().size() == 172);
	}
	
}
