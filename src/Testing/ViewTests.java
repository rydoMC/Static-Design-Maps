package Testing;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.swing.JOptionPane;

import org.junit.Test;

import Framework.iView;
import Jung.DefaultView;
import Model.Model;

public class ViewTests {

	/**
	 * VIW 001
	 * 
	 * Class: DefaultView
	 * Unit: Constructor
	 * 
	 * This unit test shows that the view handles a null reference by
	 * closing down the application as no model has been created in the 
	 * MVC setup.
	 */
	//@Test
	public void nullModel(){
		new DefaultView(null);
	}
	
	
	/**
	 * VIW 002
	 * 
	 * Class: DefaultView
	 * Unit: getSourceFileDirectory()
	 * 
	 * This unit test correctly returns null when no file has been selected.
	 * Unit is then tested again to check it correctly returns file file path
	 * when 
	 */
	@Test
	public void checkFile(){
		iView view = new DefaultView(new Model(null, null));
		
		JOptionPane.showMessageDialog(null, "The next test tests what happens when no file is\nselected and cancel is pressed\n\nPlease select the cancel option.");
		assertNull(view.getSourceFileDirectoryPath());
		
		
		JOptionPane.showMessageDialog(null, "The next test involves you selected a file on the next file chooser");
		String s = view.getSourceFileDirectoryPath();
		assertNotNull(s);
		
		File f = new File(s);
		assertTrue(f.exists());
	}
}
