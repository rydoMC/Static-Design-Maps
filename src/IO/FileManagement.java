package IO;
import java.io.File;
import java.util.ArrayList;

/**
 * This class provides the file checking functionality 
 * required for this tool.
 * 
 * NB this does not carry out the parsing of the files.
 * 
 * It simply provides methods that can be used to check
 * that files are valid and retrieve a list of files.
 *
 * @author Ryan McNulty
 * @date 2 Jan 2012
 * @organisation Computer and Information Science, Strathclyde University, Glasgow, Scotland.
 */
public class FileManagement {

	public FileManagement(){}
	
	/**
	 * Returns an array-list of all java files for that file F.
	 * If f is a directory then the sub directories are searched.
	 * Else, an array-list of size 1 will be returned.
	 * 
	 * Hidden files are ignored.
	 * 
	 * @param f File object representing the directory
	 * @return a list of all java files in that directory and sub directories
	 */
	public ArrayList<File> getAllJavaFiles(File f) {
		ArrayList<File> filePaths = new ArrayList<File>();

		if (f != null) {
			if (f.exists()) {

				if (f.isDirectory()) {
					for (File file : f.listFiles()) {
						// recursive call
						filePaths.addAll(getAllJavaFiles(file));
					}
				} else if (!f.isHidden() && isJavaFile(f)) {
					filePaths.add(f);
				}
			}
		}
		return filePaths;
	}
	
	
	/**
	 * Checks to see if given file is a java source file
	 * by checking its file extension.
	 * 
	 * @param f File expected to be a java source file
	 * @return True if file is java source file
	 */
	private boolean isJavaFile(File f) {
		if(f.getName().length() > 5){
			return f.getName().substring(f.getName().length() - 5).equalsIgnoreCase(".java");
		}
		else {
			return false;
		}
	}
}
