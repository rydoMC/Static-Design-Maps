package Jung;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Framework.iController;
import Framework.iView;
import Model.Declaration;
import Model.Model;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;

/**
 * This class represents the default view provided
 * in this tool. It uses built in java libraries with
 * help from JUNG framework.
 *
 * @author Ryan McNulty
 * @date 1 Feb 2012
 * @organisation Computer and Information Science, Strathclyde University, Glasgow, Scotland.
 */
public class DefaultView extends Observable implements iView, ChangeListener {

	private Model model;
	private JFrame mainFrame;
	private MapView map;
	
	//private DefaultModalGraphMouse<String, String> mouseController;
	private PluggableGraphMouse mouseController;
	private ArrayList<String> latestResults;
	
	
	/**
	 * Default Constructor
	 */
	public DefaultView(Model m) {
		if (m != null) {
			model = m;

			createMap();
			createMainFrame();

			m.addObserver(this);
		}		
		else {
			JOptionPane.showMessageDialog(null, "Model does not exist\nShutting down.","Error",JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
	}
	
	
	/**
	 * Initialise the map component of the 
	 * view and its mouse controller.
	 */
	private void createMap(){
		map = new MapView();
		
		initMouse();
		
		map.setMouseController(mouseController);
	}
	
	
	/**
	 * Initialise the mouse component that is being used.
	 */
	private void initMouse(){
		mouseController = new DefaultMouse();
		
		// ONLY TO BE USED WHEN USING DEFAULT MODAL GRAPH MOUSE
		//mouseController = new DefaultModalGraphMouse<String, Integer>();
		//mouseController.setMode(Mode.PICKING);
	}

	
	/**
	 * Creates the frame that holds the view.
	 * Adds buttons, menubar, and sets the map inside the frame.
	 */
	private void createMainFrame() {
		mainFrame = createFrame("Static Design Maps",550,550);
		
		mainFrame.getContentPane().setLayout(new BorderLayout());
		mainFrame.getContentPane().add(map,BorderLayout.CENTER);
		//mainFrame.getContentPane().add(modeBox,BorderLayout.SOUTH);

		createMenubar();
		createControlPanel();
	}
	
	
	/**
	 * Returns a frame with the title provided by name.
	 * The frames location is set to centre on screen.
	 * 
	 * @param name String that is to appear as the frames title
	 * @return the frame object.
	 */
	private JFrame createFrame(String name, int width, int height){
		JFrame fr = new JFrame(name);
		fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		fr.setSize(width,height);
		
		fr.setLocationRelativeTo(null);
		return fr;
	}
	
	
	/**
	 * Reuses createFrame(name,x,y) by passing values
	 * of 400 for width and height.
	 * 
	 * @param name Title of frame
	 * @return Frame object.
	 */
	private JFrame createFrame(String name){
		return createFrame(name,400,400);
	}
	
	
	/**
	 * Adds the expand and collapse panel to the frame.
	 */
	private void createControlPanel() {
		JPanel controlPanel = new JPanel(new GridLayout(1,3));
		
		// Mouse mode box - SHOULD ONLY BE USED WHEN USING DEFAULTMODALGRAPHMOUSE
		//JComboBox modeBox = mouseController.getModeComboBox();
        //modeBox.addItemListener(mouseController.getModeListener());
        
        controlPanel.add(createJButton("Expand"));
        controlPanel.add(createJButton("Collapse"));
        //controlPanel.add(modeBox);
        
        mainFrame.getContentPane().add(controlPanel,BorderLayout.SOUTH);
	}
	
	
	/**
	 * Returns a button with the string param passed.
	 * Adds this as an action listener.
	 * 
	 * @param desc The string to be shown on the button
	 * @return The created button
	 */
	private JButton createJButton(String desc){
		JButton but = new JButton(desc);
		but.addActionListener(this);
		return but;
	}


	/**
	 * Adds the menu bar to the main frame.
	 */
	private void createMenubar(){
		MenuBar menuBar = new MenuBar();
		
		Menu dataMenu = new Menu("Menu");
		Menu mapMenu = new Menu("Analysis Settings");
		
		// ----------- Data menu Items
		MenuItem setDataItem = createMenuItem("Set Files and Analyse","process");
		
		MenuItem listResultsItem = createMenuItem("List of Results","listresults");
		
		// adds blank space between commands and quit button
		MenuItem blank = createMenuItem("");
		blank.setEnabled(false);
		
		MenuItem exitItem = createMenuItem("Quit","shutdown");
		
		// ----------- Map menu items
		MenuItem noToShowItem = createMenuItem("Change Number of Nodes on Map", "changenumberofnodes");
		
		
		
		
		// ------------- Data menu controls
		dataMenu.add(setDataItem);
		dataMenu.add(listResultsItem);
		dataMenu.add(blank);
		dataMenu.add(exitItem);
		
		// ------------- Analysis menu controls
		mapMenu.add(noToShowItem);
		
		
		
		menuBar.add(dataMenu);
		menuBar.add(mapMenu);
		
		mainFrame.setMenuBar(menuBar);
	}
	
	
	@Override
	public void showResultsList(){
		JFrame results = createFrame("Results");
		
		// Build Results
		String resultsStr = "";
		
		if (latestResults != null) {
			resultsStr = "Results from Algorithm\n-------------------\n";
			int i = 1;
			for (String s : latestResults) {
				resultsStr = resultsStr + "#" + i + ": " + s + "\n";
				i++;
			}
		}
		else {
			resultsStr = "No Algorithm has ran yet";
		}
		
		JTextArea text = new JTextArea(resultsStr);
		text.setEditable(false);
		
		JScrollPane jsp = new JScrollPane(text);
		
		results.add(jsp);
		results.setVisible(true);
	}
	
	
	/**
	 * This method returns a MenuItem with the name provided by the string.
	 * It also adds this object as an action listener.
	 * 
	 * @param name The word to be shown on the menu item.
	 * @return The menu item.
	 */
	private MenuItem createMenuItem(String name){
		return createMenuItem(name, name);
	}
	
	
	/**
	 * Creates a menu item showing the word defined by name
	 * and sets its action command to actionCmd
	 * 
	 * @param name Name to appear in the menu
	 * @param actionCmd Action command
	 * @return new menuItem
	 */
	private MenuItem createMenuItem(String name, String actionCmd){
		MenuItem it = new MenuItem(name);
		it.setActionCommand(actionCmd);
		it.addActionListener(this);
		return it;
	}
	

	@Override
	public void update(Observable o, Object arg) {
		if(model.getResults().size() != 0){
			map.refreshMap(model.getResults());
			//latestResults = model.getAlgorithmResults();
			
			// --- NEW CODE --- //
			ArrayList<String> declsNames = new ArrayList<String>();
			
			for(Declaration d : model.getResults()){
				declsNames.add(d.getName());
			}
			latestResults = declsNames;
			// --- END NEW CODE --- //
		}
	}

	
	@Override
	public void openWindow() {
		mainFrame.setVisible(true);
	}

	
	@Override
	public void addController(iController c) {
		this.addObserver(c);
	}
	
	
	/**
	 * This method loads a file chooser and
	 * returns the string path to the file 
	 * selected in the chooser window.
	 * 
	 * @return absolute path of file selected.
	 */
	public String getSourceFileDirectoryPath(){
		JFileChooser jf = new JFileChooser();
		jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		if(jf.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION){
			return jf.getSelectedFile().getAbsolutePath();
		}
		else {
			return null;
		}
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		
		if(actionCommand.equalsIgnoreCase("collapse")){
			map.collapseNode();
		}
		else if(actionCommand.equalsIgnoreCase("expand")){
			map.expandNode();
		}
		else if(actionCommand.equalsIgnoreCase("changenumberofnodes")){
			JFrame noNodesFrame = createFrame("Map Attribute");
			JPanel slider = createSlider("Number of nodes to be shown", 0, 20, map.getNoOfNodes());
			
			noNodesFrame.getContentPane().add(slider);
			
			noNodesFrame.setVisible(true);
		}
		else {
			// Notify the observer if the action event is not for this view implementation. 
			setChanged();
			notifyObservers(e);
		}
	}
	
	
	/**
	 * Creates a JPanel object containing a JSlider object.
	 * 
	 * @param desc The title to show next to the slider
	 * @param min Minimum value for the scale
	 * @param max Maximum value for the scale
	 * @param init The value the slider should have initially.
	 * @return JPanel 
	 */
	private JPanel createSlider(String desc, int min, int max, int init){
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(2,1));
		
		JLabel sliderLabel = new JLabel(desc, JLabel.CENTER);
        sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JSlider slider = new JSlider(JSlider.HORIZONTAL,min,max,init);
		
		slider.addChangeListener(this);
		slider.setMajorTickSpacing(5);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
        slider.setPaintLabels(true);
		
        
        // ------ Add to panel
		p.add(sliderLabel);
		p.add(slider);
	
		return p;
	}


	/**
	 * Manages the use of the JSlider for changing the number of nodes.
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() instanceof JSlider){
			JSlider source = (JSlider) e.getSource();
	        if (!source.getValueIsAdjusting()) {
	            int noOfNodes = (int) source.getValue();
	            map.changeNumberOfNodes(noOfNodes);
	        }
	        map.refreshMap(model.getResults());
		}
	}
}
