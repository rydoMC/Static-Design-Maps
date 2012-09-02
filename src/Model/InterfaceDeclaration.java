package Model;

public class InterfaceDeclaration extends SharedDeclaration {

	/**
	 * Default constructor
	 * 
	 * @param name Name of interface as in source code.
	 */
	public InterfaceDeclaration(String name) {
		super(name);
	}

	@Override
	public boolean addSuperClass(String name) {
		return addInterface(name);
	}
}
