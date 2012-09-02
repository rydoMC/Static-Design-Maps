package Parsing;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.EnumDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.expr.AssignExpr;
import japa.parser.ast.expr.ObjectCreationExpr;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.ExpressionStmt;
import japa.parser.ast.stmt.ForeachStmt;
import japa.parser.ast.stmt.IfStmt;
import japa.parser.ast.stmt.ReturnStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.stmt.TryStmt;
import japa.parser.ast.stmt.WhileStmt;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.type.Type;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import Framework.iModelConverter;
import IO.FileManagement;
import Model.ClassDeclaration;
import Model.Declaration;
import Model.InterfaceDeclaration;
import Model.Method;

/**
 * This will manage the converting the JParser Tools objects to the logical
 * model components.
 * 
 * @author Ryan McNulty
 * @date 27 Dec 2011
 * @organisation Computer and Information Science, Strathclyde University,
 *               Glasgow, Scotland.
 */
public class JParserConverter implements iModelConverter {

	private File root;
	private FileManagement fp;

	private ArrayList<File> files;
	private ArrayList<Declaration> declarations;

	/**
	 * Default Constructor
	 * 
	 * @param path
	 *            String path of file.
	 */
	public JParserConverter(String path) {
		this(new File(path));
	}

	/**
	 * Alternative constructor
	 * 
	 * @param root
	 *            File referenced by user, directory or individual file.
	 */
	public JParserConverter(File root) {
		initInternal();
		
		if(root != null){
			if(root.exists()){
				this.root = root;

				buildFileList();
			}
		}
	}

	/**
	 * Initialise any internal convert components required for this converter.
	 */
	private void initInternal() {
		fp = new FileManagement();
		declarations = new ArrayList<Declaration>();
		files = new ArrayList<File>();
	}

	/**
	 * Sets files to be processed by analysing the root file. If root file is
	 * directory then all sub directories will be searched and files found will
	 * be added.
	 */
	private void buildFileList() {
		if (root != null) {
			if (root.isDirectory()) {
				files = fp.getAllJavaFiles(root);
			} else {
				files.add(root);
			}
		}
	}

	
	@Override
	public ArrayList<Declaration> retrieveDeclarations() {
		return declarations;
	}
	

	@Override
	public void processFiles() {
		FileInputStream in;
		CompilationUnit cu;

		for (File currentFile : files) {
			// Initialise JavaParser components.
			try {
				in = new FileInputStream(currentFile);
				cu = JavaParser.parse(in);

				visitor v = new visitor();
				v.visit(cu, null);

				// Check that declaration != null as this is possible
				if (v.getDeclaration() != null) {
					// Pull the declaration object from visitor and add
					// to
					// list
					declarations.add(v.getDeclaration());
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	

	/**
	 * This class its the visitor component as per visitor Design Pattern.
	 * 
	 * It contains all the relevent visit method overrides to each of the object
	 * types we are interested in and acts on them accordingly.
	 * 
	 * @author Ryan McNulty
	 * @date 5 Feb 2012
	 * @organisation Computer and Information Science, Strathclyde University,
	 *               Glasgow, Scotland.
	 */
	private class visitor extends VoidVisitorAdapter<Declaration> {
		private Declaration d; // The reference to the modelled object

		@Override
		public void visit(ClassOrInterfaceDeclaration n, Declaration decl) {
			/*
			 * This is required to handle private classes If d != null then then
			 * N is a another class within this file so the original declaration
			 * must be stored before overwritten.
			 */
			if (d != null) {
				declarations.add(d);
				d.addReferenceToType(n.getName());
				d = null;
			}

			// Establish the basic class details
			if (n.isInterface()) {
				d = new InterfaceDeclaration(n.getName());
			} else {
				d = new ClassDeclaration(n.getName(), ModifierSet.isAbstract(n
						.getModifiers()));
			}

			// If the declaration has super classes/interfaces then add them.
			if (n.getExtends() != null) {
				for (ClassOrInterfaceType ci : n.getExtends()) {
					d.addSuperClass(ci.getName());
				}
			}

			addImplements(n); // Add all interfaces

			// This ensures all components of the class accepts this as a
			// visitor.
			if (n.getMembers() != null) {
				for (BodyDeclaration bd : n.getMembers()) {
					bd.accept(this, d);
				}
			}
		}

		
		/**
		 * Returns the declaration object modelled on the initial Compilation
		 * unit.
		 * 
		 * @return Declaration model object representing CU.
		 */
		public Declaration getDeclaration() {
			return d;
		}

		
		/**
		 * Checks for any interface implements and add them to the Declaration
		 * field d
		 * 
		 * @param n ClassOrInterfaceDecl object which represents d.
		 */
		private void addImplements(ClassOrInterfaceDeclaration n) {
			if (n.getImplements() != null) {
				for (ClassOrInterfaceType t : n.getImplements()) {
					d.addInterface(t.getName());
				}
			}
		}


		@Override
		public void visit(CompilationUnit cu, Declaration decl) {
			// Visit the types to establish
			if (cu.getTypes() != null) {
				for (TypeDeclaration t : cu.getTypes()) {
					t.accept(this, decl);
				}
			}

			// Defensive Measure
			if (d != null) {
				if (cu.getPackage() != null)
					cu.getPackage().accept(this, d);

				// Visit all imports
				if (cu.getImports() != null) {
					for (ImportDeclaration id : cu.getImports()) {
						id.accept(this, d);
					}
				}
			}
		}

		
		@Override
		public void visit(ConstructorDeclaration n, Declaration decl) {
			n.getBlock().accept(this, decl);
		}

		
		@Override
		public void visit(FieldDeclaration n, Declaration decl) {
			n.getType().accept(this, decl);
		}

		
		@Override
		public void visit(MethodDeclaration n, Declaration decl) {
			// Create a method model object
			Method md = new Method(n.getName());

			// Store the return type and allows the typed to be visited.
			md.setReturnType(n.getType().toString());
			n.getType().accept(this, decl);

			if (n.getParameters() != null) {
				for (Parameter param : n.getParameters()) {
					// We don't want to visit params.
					extractParams(param, md);
				}
			}

			// If this is a class then evaluate the method bodies.
			if (decl.isClass()) {
				if (n.getBody() != null) {
					if (n.getBody().getStmts() != null) {
						for (Statement s : n.getBody().getStmts()) {
							s.accept(this, decl);
						}
					}
				}
			}
			// Add the established method to the model object
			decl.addNewMethod(md);
		}

		
		/**
		 * Take the parameter object and extract it to the method. Extracts full
		 * param name. e.g. Figure figureToMove
		 * 
		 * @param n
		 *            Paramter object
		 * @param md
		 *            Method which the parameter is from.
		 */
		public void extractParams(Parameter n, Method md) {
			md.addParam(n.getType().toString() + " " + n.getId().getName());

			// Now visit the type so we note its reference.
			n.getType().accept(this, d);
		}

		
		@Override
		public void visit(ReferenceType n, Declaration d) {
			n.getType().accept(this, d);
		}

		
		@Override
		public void visit(ClassOrInterfaceType n, Declaration d) {
			if (n != null) {
				// Statement only true if n is a data structure
				if (n.getTypeArgs() != null) {
					for (Type t : n.getTypeArgs()) {
						// Get name of types within structure
						d.addReferenceToType(t.toString());
					}
				} else {
					// Adds the reference to type as in source code
					d.addReferenceToType(n.toString());
				}
			}
		}

		
		@Override
		public void visit(PackageDeclaration n, Declaration decl) {
			decl.setPackageName(n.getName().toString());
		}

		
		@Override
		public void visit(ImportDeclaration n, Declaration decl) {
			decl.addImport(n.getName().toString());
		}

		
		@Override
		public void visit(ExpressionStmt n, Declaration decl) {
			n.getExpression().accept(this, decl);
		}

		
		@Override
		public void visit(ForeachStmt n, Declaration decl) {
			n.getBody().accept(this, decl);
		}

		
		@Override
		public void visit(IfStmt n, Declaration decl) {
			n.getThenStmt().accept(this, decl);

			if (n.getElseStmt() != null) {
				n.getElseStmt().accept(this, decl);
			}
		}

		
		@Override
		public void visit(ReturnStmt n, Declaration decl) {
			if (n.getExpr() != null)
				n.getExpr().accept(this, decl);
		}

		
		@Override
		public void visit(WhileStmt n, Declaration decl) {
			n.getBody().accept(this, decl);
		}

		
		@Override
		public void visit(BlockStmt n, Declaration decl) {
			if (n.getStmts() != null) {
				for (Statement st : n.getStmts()) {
					st.accept(this, decl);
				}
			}
		}

		
		@Override
		public void visit(TryStmt n, Declaration decl) {
			n.getTryBlock().accept(this, decl);
			if (n.getFinallyBlock() != null)
				n.getFinallyBlock().accept(this, decl);
		}

		
		@Override
		public void visit(VariableDeclarationExpr n, Declaration decl) {
			n.getType().accept(this, decl);

			for (VariableDeclarator d : n.getVars()) {
				d.accept(this, decl);
			}
		}

		
		@Override
		public void visit(AssignExpr n, Declaration decl) {
			n.getValue().accept(this, decl);
		}

		
		@Override
		public void visit(ObjectCreationExpr n, Declaration decl) {
			// Objects can only be created by ClassDeclarations
			ClassDeclaration cd = (ClassDeclaration) decl;

			// Check for structure and then reference types
			if (n.getType().getTypeArgs() != null) {
				for (Type t : n.getType().getTypeArgs()) {
					cd.addReferenceToType(t.toString());
				}
			} else {
				// Store the reference to a NEW OBJECT
				cd.addReferenceToNewObject(n.getType().toString());
			}
		}
		
		
		 @Override
		 public void visit(EnumDeclaration ed, Declaration decl){
			 /* Empty method - visits to enums are no prohibited as they are constants and contain no value.
			  *  Deleting this method will then mean the super method is visited and that is not wanted.
			  */
		 }
	}
	// ------- END OF PRIVATE CLASS -------
}