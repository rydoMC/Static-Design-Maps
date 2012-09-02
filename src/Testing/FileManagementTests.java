package Testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import org.junit.Test;

import IO.FileManagement;
/**
 * This unit testing class consists of all available testing that
 * can be applied to the class: FileManagement.java
 *
 * @author Ryan McNulty
 * @date 9 Mar 2012
 * @organisation Computer and Information Science, Strathclyde University, Glasgow, Scotland.
 */
public class FileManagementTests {

	/**
	 * FIL001
	 * 
	 * Unit: getAllJavaFiles
	 * 
	 * Tests how it handles a file that don't exist.
	 */
	@Test
	public void nonExistentFile(){
		FileManagement fm = new FileManagement();
		
		ArrayList<File> files = fm.getAllJavaFiles(new File("/Users/non existent file"));
		
		assertNotNull(files);
		assertEquals(files.size(),0);
	}
	

	/**
	 * FIL002
	 * Unit: getAllJavaFiles
	 * 
	 * Tests how it handles a directory that don't exist.
	 */
	@Test
	public void nonExistentDirectory(){
		FileManagement fm = new FileManagement();
		
		ArrayList<File> files = fm.getAllJavaFiles(new File("/Users/non existent folder/"));
		
		assertNotNull(files);
		assertEquals(files.size(),0);
	}
	

	/**
	 * FIL003 & FIL004
	 * 
	 * Unit: getAllJavaFile
	 * 
	 * Tests how getallJavaFile handles a single file.
	 * FIL003 - selects NON java file
	 * FIL004 - selects Java file
	 */
	@Test
	public void existingFile(){
		JFileChooser jf = new JFileChooser();
		jf.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		if(jf.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
			File f = jf.getSelectedFile();
			
			FileManagement fm = new FileManagement();
			ArrayList<File> files = fm.getAllJavaFiles(f);
			int expected;
			
			// If file name length is less than 5 then it cant be a source file as it must be at least five long (.java)
			if(f.getName().length() < 5){
				if(f.getName().substring(f.getName().length()-5,f.getName().length()).equalsIgnoreCase(".java")){
					expected = 1;
				}
				else {
					expected = 0;
				}
			}
			else {
				expected = 0;
			}
			
			assertTrue(files.size() == expected);
		}
	}
	

	/**
	 * FIL005
	 */
	@Test
	public void existingDirectory(){
		JFileChooser jf = new JFileChooser();
		jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		if(jf.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
			File f = jf.getSelectedFile();
			
			ArrayList<File> allFiles = getAllFiles(f);

			
			int noOfNonJavaFiles = 0;
			
			for (File file : allFiles) {
				if (!file.isHidden()) {
					if(file.getName().length() > 5){
						if (!(file.getName().substring(file.getName().length() - 5,
								file.getName().length()).equalsIgnoreCase(".java"))) {
							noOfNonJavaFiles++;
						}
					}
					else {
						noOfNonJavaFiles++;
					}
				}
			}
			
			ArrayList<File> javaFilesFound = new FileManagement().getAllJavaFiles(f);
			
			assertEquals(noOfNonJavaFiles, (allFiles.size() - javaFilesFound.size()));
		}
	}
	
	
	
	// ------------- Helper methods for testing.
	/**
	 * Returns an array list containing all the files
	 * within that directory and any sub directory.
	 * 
	 * @param f Directory to list files for
	 * @return
	 */
	private ArrayList<File> getAllFiles(File f){
		ArrayList<File> filelist = new ArrayList<File>();
		
		if(f.isDirectory()){
			for(File fileInDirectory : f.listFiles()){
				filelist.addAll(getAllFiles(fileInDirectory));
			}
		}
		else{
			if(!f.isHidden()) filelist.add(f); // FileManagement does not consider hidden files therefore
											   // To ensure proper testing, this method must ignore them also
		}
		
		return filelist;
	}
}
