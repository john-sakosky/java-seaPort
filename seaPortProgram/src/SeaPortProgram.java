/**
 * SeaPortProgram.java
 * 03/05/2017
 * 
 * SeaPortProgram
 * John Sakosky
 * 
 */

package seaPortProgram;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

/** Main Program */
public class SeaPortProgram extends JFrame {
	private static final long serialVersionUID = 1L;

	static World world;

	private ArrayList<FinalTableModel> statusModels;
	private JPanel mainPanel;
	private JPanel treeControlsPanel;
	private JTextArea jobDetails;
	private JTextArea overviewText;
	private JTextArea resultsText;
	private JTextArea treeText;
	private JTextField searchTerm;
	private JTree dataTree;
	private JRadioButton rbSearchName;
	private JRadioButton rbSearchIndex;
	private JRadioButton rbSearchSkill;
	private JRadioButton rbSortAscend;
	private JRadioButton rbSortDescend;
	private JRadioButton rbSortName;
	private JRadioButton rbSortShipWeight;
	private JRadioButton rbSortShipLength;
	private JRadioButton rbSortShipWidth;
	private JRadioButton rbSortShipDraft;
	private TreeNode lastNode;
	private String currentPage;
	private boolean reset = true;

	/** CONSTANTS */
	private static final int FRAME_WIDTH = 600;
	private static final int FRAME_HEIGHT = 600;
	private static final int JAREA_ROWS = 4;
	private static final int JAREA_COLUMNS = 40;
	private static final int TAREA_ROWS = 30;
	private static final int TAREA_COLUMNS = 40;
	private static final int RAREA_ROWS = 20;
	private static final int RAREA_COLUMNS = 40;
	private static final int SEARCH_WIDTH = 30;

	/** Constructor - Initializes GUI */
	public SeaPortProgram() {
		super("Sea Port Simulator - Project 1");
		setSize(600, 600);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		PaintScreen();
		setVisible(true);
	}

	/** Paint elements to GUI window */
	public void PaintScreen() {
		createMenu();
		createMainPanel();
		changeLayout();
		// display = new JLabel("Select Country:");
		// add(display, BorderLayout.NORTH);

		if (reset)
			reset();

		setSize(FRAME_WIDTH, FRAME_HEIGHT);
	}

	/** Reset GUI to initial state */
	public void reset() {
		setOverviewText();
		resultsText.setText("");
		currentPage = "Overview";
	}

	/** Generate menu options */
	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu menuFile = new JMenu("File");
		JMenu menuGoto = new JMenu("Go to");
		JMenu menuHelp = new JMenu("Help");
		menuBar.add(menuFile);
		menuBar.add(menuGoto);
		menuBar.add(menuHelp);

		JMenuItem menuItemFileOpen = new JMenuItem("Open");
		JMenuItem menuItemFileReset = new JMenuItem("Reset");
		JMenuItem menuItemFileExit = new JMenuItem("Exit");
		JMenuItem menuItemGotoOverview = new JMenuItem("Overview");
		JMenuItem menuItemGotoSearch = new JMenuItem("Search");
		JMenuItem menuItemGotoTree = new JMenuItem("Tree");
		JMenuItem menuItemHelpAbout = new JMenuItem("About");

		menuFile.add(menuItemFileOpen);
		menuFile.add(menuItemFileReset);
		menuFile.add(menuItemFileExit);
		menuGoto.add(menuItemGotoOverview);
		menuGoto.add(menuItemGotoSearch);
		menuGoto.add(menuItemGotoTree);
		menuHelp.add(menuItemHelpAbout);

		menuItemFileOpen.addActionListener(new MenuItemOpenListener());
		menuItemFileReset.addActionListener(new MenuItemResetListener());
		menuItemFileExit.addActionListener(new MenuItemExitListener());
		menuItemGotoOverview.addActionListener(new MenuItemGotoOverviewListener());
		menuItemGotoSearch.addActionListener(new MenuItemGotoSearchListener());
		menuItemGotoTree.addActionListener(new MenuItemGotoTreeListener());
		menuItemHelpAbout.addActionListener(new MenuItemAboutListener());
	}

	/** Generates content of main panel in GUI */
	private void createMainPanel() {
		// JPanel mainPanelChoose = new JPanel();
		JPanel mainPanelOverview = createPanelOverview();
		JPanel mainPanelJobs = createPanelJobs();
		JPanel mainPanelStatus = createPanelStatus();
		JPanel mainPanelSearch = createPanelSearch();
		JPanel mainPanelTree = createPanelTree();
		JPanel panelNavigation = createPanelNavigation();

		mainPanel = new JPanel(new CardLayout());
		// mainPanel.add(mainPanelChoose, "ChooseFile");
		mainPanel.add(mainPanelOverview, "Overview");
		mainPanel.add(mainPanelStatus, "Status");
		mainPanel.add(mainPanelJobs, "Jobs");
		mainPanel.add(mainPanelSearch, "Search");
		mainPanel.add(mainPanelTree, "Tree");

		add(mainPanel, BorderLayout.CENTER);
		add(panelNavigation, BorderLayout.SOUTH);
	}

	/** Refresh Text in overview */
	public void setOverviewText() {
		overviewText.setText(world.toStr());
	}

	/** Switch visible JPanel */
	public void changeLayout() {
		CardLayout c1 = (CardLayout) (mainPanel.getLayout());
		c1.show(mainPanel, currentPage);
	}

	/** Open File Chooser Dialog to program folder */
	public static String openFileChooser() {
		JFileChooser fileChooser = new JFileChooser(".");
		File selectedFile = null;
		FileNameExtensionFilter filter = new FileNameExtensionFilter("txt files", "txt");
		fileChooser.setFileFilter(filter);

		int returnValue = fileChooser.showOpenDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			selectedFile = fileChooser.getSelectedFile();
			System.out.println("we selected: " + selectedFile);
		}

		return selectedFile.toString();
	}

	public void rebuildDataTree() {
		DefaultTreeModel model = (DefaultTreeModel) dataTree.getModel();
		model.nodeStructureChanged(world.treeNode());
		// model.reload(world.treeNode());

		// dataTree.node
		// dataTree.validate();
		// dataTree.repaint();
	}

	public void updateTreeText() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) dataTree.getLastSelectedPathComponent();
		if (node != null)
			treeText.setText(((TreeNode) node.getUserObject()).description);
	}

	public void treeControlsPanel() {
		if ((dataTree.getLastSelectedPathComponent()) == null)
			return;
		TreeNode node = (TreeNode) ((DefaultMutableTreeNode) dataTree.getLastSelectedPathComponent()).getUserObject();

		treeControlsPanel.removeAll();

		if (node.type == NodeType.Docks || node.type == NodeType.Jobs || node.type == NodeType.Persons
				|| node.type == NodeType.Ports || node.type == NodeType.Que || node.type == NodeType.Ships) {

			rbSortName = new JRadioButton("By Name");
			rbSortName.setSelected(true);
			rbSortShipWeight = new JRadioButton("By Weight");
			rbSortShipLength = new JRadioButton("By Length");
			rbSortShipWidth = new JRadioButton("By Width");
			rbSortShipDraft = new JRadioButton("By Draft");
			rbSortAscend = new JRadioButton("Ascending");
			rbSortAscend.setSelected(true);
			rbSortDescend = new JRadioButton("Descending");

			ButtonGroup group = new ButtonGroup();
			group.add(rbSortName);
			group.add(rbSortShipWeight);
			group.add(rbSortShipLength);
			group.add(rbSortShipWidth);
			group.add(rbSortShipDraft);

			ButtonGroup group2 = new ButtonGroup();
			group2.add(rbSortAscend);
			group2.add(rbSortDescend);

			JPanel panel1 = new JPanel();
			panel1.add(rbSortName);
			if (node.type == NodeType.Ships || node.type == NodeType.Que) {
				panel1.setLayout(new GridLayout(5, 1));
				panel1.add(rbSortShipWeight);
				panel1.add(rbSortShipLength);
				panel1.add(rbSortShipWidth);
				panel1.add(rbSortShipDraft);
			}
			panel1.setBorder(new TitledBorder(new EtchedBorder(), "Sort By:"));

			JPanel panel2 = new JPanel();
			panel2.setLayout(new GridLayout(2, 1));
			panel2.add(rbSortAscend);
			panel2.add(rbSortDescend);
			panel2.setBorder(new TitledBorder(new EtchedBorder(), "Order in:"));

			JPanel panel3 = new JPanel();
			panel3.setLayout(new GridLayout(3, 1));
			panel3.add(panel1);
			panel3.add(panel2);
			treeControlsPanel.add(panel3);
			treeControlsPanel.add(makeActButtonSort("Sort"));
		}
		treeControlsPanel.validate();
		treeControlsPanel.repaint();
		validate();
		repaint();
	}

	public void treeSort() {
		if (dataTree.getLastSelectedPathComponent() != null)
			lastNode = (TreeNode) ((DefaultMutableTreeNode) dataTree.getLastSelectedPathComponent()).getUserObject();

		// Collections.sort(((DefaultMutableTreeNode)dataTree.getLastSelectedPathComponent()).children(),
		// TreeNode.compTreeNode(reset, null));

	}

	public void dataSort() {
		if (dataTree.getLastSelectedPathComponent() != null)
			lastNode = (TreeNode) ((DefaultMutableTreeNode) dataTree.getLastSelectedPathComponent()).getUserObject();
		switch (lastNode.type) {

		case Docks:
			if (rbSortAscend.isSelected())
				((SeaPort) lastNode.object).docks.sort(SeaPort.compByNameAsc());
			else
				((SeaPort) lastNode.object).docks.sort(SeaPort.compByNameDes());
			break;
		case Ports:
			if (rbSortAscend.isSelected())
				((World) lastNode.object).ports.sort(SeaPort.compByNameAsc());
			else
				((World) lastNode.object).ports.sort(SeaPort.compByNameDes());
			break;
		case Que:
			if (rbSortName.isSelected()) {
				if (rbSortAscend.isSelected())
					((SeaPort) lastNode.object).que.sort(Ship.compByNameAsc());
				else
					((SeaPort) lastNode.object).que.sort(Ship.compByNameDes());
			}
			if (rbSortShipWeight.isSelected()) {
				if (rbSortAscend.isSelected())
					((SeaPort) lastNode.object).que.sort(Ship.compByWeightAsc());
				else
					((SeaPort) lastNode.object).que.sort(Ship.compByWeightDes());
			}
			if (rbSortShipLength.isSelected()) {
				if (rbSortAscend.isSelected())
					((SeaPort) lastNode.object).que.sort(Ship.compByLengthAsc());
				else
					((SeaPort) lastNode.object).que.sort(Ship.compByLengthDes());
			}
			if (rbSortShipWidth.isSelected()) {
				if (rbSortAscend.isSelected())
					((SeaPort) lastNode.object).que.sort(Ship.compByWidthAsc());
				else
					((SeaPort) lastNode.object).que.sort(Ship.compByWidthDes());
			}
			if (rbSortShipDraft.isSelected()) {
				if (rbSortAscend.isSelected())
					((SeaPort) lastNode.object).que.sort(Ship.compByDraftAsc());
				else
					((SeaPort) lastNode.object).que.sort(Ship.compByDraftDes());
			}
			break;
		case Ships:
			if (lastNode.map) {
				if (rbSortName.isSelected()) {
					if (rbSortAscend.isSelected())
						((World) lastNode.object).ships.sort(Ship.compByNameAsc());
					else
						((World) lastNode.object).ships.sort(Ship.compByNameDes());
				}
				if (rbSortShipWeight.isSelected()) {
					if (rbSortAscend.isSelected())
						((World) lastNode.object).ships.sort(Ship.compByWeightAsc());
					else
						((World) lastNode.object).ships.sort(Ship.compByWeightDes());
				}
				if (rbSortShipLength.isSelected()) {
					if (rbSortAscend.isSelected())
						((World) lastNode.object).ships.sort(Ship.compByLengthAsc());
					else
						((World) lastNode.object).ships.sort(Ship.compByLengthDes());
				}
				if (rbSortShipWidth.isSelected()) {
					if (rbSortAscend.isSelected())
						((World) lastNode.object).ships.sort(Ship.compByWidthAsc());
					else
						((World) lastNode.object).ships.sort(Ship.compByWidthDes());
				}
				if (rbSortShipDraft.isSelected()) {
					if (rbSortAscend.isSelected())
						((World) lastNode.object).ships.sort(Ship.compByDraftAsc());
					else
						((World) lastNode.object).ships.sort(Ship.compByDraftDes());
				}
			} else {
				if (rbSortName.isSelected()) {
					if (rbSortAscend.isSelected())
						((SeaPort) lastNode.object).ships.sort(Ship.compByNameAsc());
					else
						((SeaPort) lastNode.object).ships.sort(Ship.compByNameDes());
				}
				if (rbSortShipWeight.isSelected()) {
					if (rbSortAscend.isSelected())
						((SeaPort) lastNode.object).ships.sort(Ship.compByWeightAsc());
					else
						((SeaPort) lastNode.object).ships.sort(Ship.compByWeightDes());
				}
				if (rbSortShipLength.isSelected()) {
					if (rbSortAscend.isSelected())
						((SeaPort) lastNode.object).ships.sort(Ship.compByLengthAsc());
					else
						((SeaPort) lastNode.object).ships.sort(Ship.compByLengthDes());
				}
				if (rbSortShipWidth.isSelected()) {
					if (rbSortAscend.isSelected())
						((SeaPort) lastNode.object).ships.sort(Ship.compByWidthAsc());
					else
						((SeaPort) lastNode.object).ships.sort(Ship.compByWidthDes());
				}
				if (rbSortShipDraft.isSelected()) {
					if (rbSortAscend.isSelected())
						((SeaPort) lastNode.object).ships.sort(Ship.compByDraftAsc());
					else
						((SeaPort) lastNode.object).ships.sort(Ship.compByDraftDes());
				}
			}
			break;
		case Persons:
			if (rbSortAscend.isSelected())
				((World) lastNode.object).persons.sort(SeaPort.compByNameAsc());
			else
				((World) lastNode.object).persons.sort(SeaPort.compByNameDes());
			break;
		case Jobs:
			if (rbSortAscend.isSelected())
				((Ship) lastNode.object).jobs.sort(SeaPort.compByNameAsc());
			else
				((Ship) lastNode.object).jobs.sort(SeaPort.compByNameDes());
			break;
		default:
			break;
		}
		rebuildDataTree();

	}

	/**
	 * Makes a back button.
	 * 
	 * @param name
	 *            the digit of the calculator
	 * @return the back button
	 */

	/** Generate navigation button to overview page */
	public JButton makeNavButtonOverview(String name) {
		JButton button = new JButton(name);
		button.addActionListener(new NavButtonOverviewListener());
		return button;
	}

	/** Generate navigation button to job page */
	public JButton makeNavButtonJobs(String name) {
		JButton button = new JButton(name);
		button.addActionListener(new NavButtonJobsListener());
		return button;
	}

	/** Generate navigation button to search page */
	public JButton makeNavButtonSearch(String name) {
		JButton button = new JButton(name);
		button.addActionListener(new NavButtonSearchListener());
		return button;
	}

	/** Generate navigation button to status page */
	public JButton makeNavButtonStatus(String name) {
		JButton button = new JButton(name);
		button.addActionListener(new NavButtonStatusListener());
		return button;
	}

	/** Generate navigation button to overview page */
	public JButton makeNavButtonTree(String name) {
		JButton button = new JButton(name);
		button.addActionListener(new NavButtonTreeListener());
		return button;
	}

	/** Generate action button to open a file */
	public JButton makeActButtonOpen(String name) {
		JButton button = new JButton(name);
		button.addActionListener(new MenuItemOpenListener());
		return button;
	}

	/** Generate action button to run jobs */
	public JButton makeActButtonRunJobs(String name) {
		JButton button = new JButton(name);
		button.addActionListener(new ActButtonRunJobsListener());
		return button;
	}

	/** Generate action button to perform search */
	public JButton makeActButtonSearch(String name) {
		JButton button = new JButton(name);
		button.addActionListener(new ActButtonSearchListener());
		return button;
	}

	/** Generate action button to perform sort */
	public JButton makeActButtonSort(String name) {
		JButton button = new JButton(name);
		button.addActionListener(new ActButtonSortListener());
		return button;
	}

	/** */
	/*
	 * public JButton makeBackButton(String name) { JButton button = new
	 * JButton(name); ActionListener listener = new BackButtonListener();
	 * button.addActionListener(listener); return button; }
	 */

	/** Generate jobs panel */
	public JPanel createPanelJobs() {

		UpdatableTableModel model = new UpdatableTableModel();
		world.model = model;
		for (Job job : world.activeJobs) {
			model.addJob(job);
		}

		JTable table = new JTable();
		table.setModel(model);

		table.getColumn("Status").setCellRenderer(new ProgressCellRender());
		table.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = table.rowAtPoint(evt.getPoint());
				int col = table.columnAtPoint(evt.getPoint());
				if (row >= 0 && col >= 0) {
					jobDetails.setText(model.getJobAt(row).getDetailsFormatted());
				}
			}
		});

		for (Job job : world.activeJobs) {
			new JobStatusWorker(model, job).execute();
		}

		JPanel jobPanel = new JPanel();

		JPanel mainPanelJobs_run = new JPanel();
		mainPanelJobs_run.add(makeActButtonRunJobs("Run Jobs"));

		// searchPanel.setLayout(new GridLayout(3, 1));
		jobPanel.add(mainPanelJobs_run, BorderLayout.NORTH);
		jobPanel.add(new JScrollPane(table), BorderLayout.CENTER);
		jobPanel.add(createPanelJobDetails(), BorderLayout.SOUTH);

		return jobPanel;
	}

	public JPanel createPanelJobDetails() {
		JPanel details = new JPanel();

		jobDetails = new JTextArea(JAREA_ROWS, JAREA_COLUMNS);
		jobDetails.setText("ABC");
		jobDetails.setEditable(false);

		details.add(jobDetails);

		return details;
	}

	/** Generate navigation panel */
	public JPanel createPanelNavigation() {
		JPanel navPanel = new JPanel();

		navPanel.add(makeNavButtonJobs("Jobs"));
		navPanel.add(makeNavButtonStatus("Status"));
		navPanel.add(makeNavButtonOverview("Overview"));
		navPanel.add(makeNavButtonSearch("Search"));
		navPanel.add(makeNavButtonTree("Tree"));

		return navPanel;
	}

	/** Generate overview panel */
	public JPanel createPanelOverview() {
		JPanel panelOverview = new JPanel();

		overviewText = new JTextArea(TAREA_ROWS, TAREA_COLUMNS);
		setOverviewText();
		overviewText.setEditable(false);

		JScrollPane scrollPane = new JScrollPane(overviewText);

		// panelOverview.add(makeNavButtonSearch("Search"));
		panelOverview.add(scrollPane);

		return panelOverview;
	}

	/** Generate search panel */
	public JPanel createPanelSearch() {
		JPanel searchPanel = new JPanel();

		JPanel mainPanelSearch_query = createPanelSearchQuery();
		JPanel mainPanelSearch_results = createPanelSearchResults();
		JPanel mainPanelSearch_nav = new JPanel();
		mainPanelSearch_nav.add(makeNavButtonOverview("Back to Overview"));

		// searchPanel.setLayout(new GridLayout(3, 1));
		searchPanel.add(mainPanelSearch_query, BorderLayout.NORTH);
		searchPanel.add(mainPanelSearch_results, BorderLayout.CENTER);
		searchPanel.add(mainPanelSearch_nav, BorderLayout.SOUTH);

		return searchPanel;
	}

	/** Generate search panel */
	public JPanel createPanelStatus() {

		JTabbedPane status = new JTabbedPane();

		statusModels = new ArrayList<FinalTableModel>();

		for (SeaPort sp : world.ports) {
			status.addTab(sp.name, createPanelStatusTab(sp));
		}

		JPanel statusPanel = new JPanel();

		statusPanel.add(status);

		return statusPanel;
	}

	/** Generate search panel */
	@SuppressWarnings("unchecked")
	public JPanel createPanelStatusTab(SeaPort sp) {
		JPanel statusTab = new JPanel();

		JTable persons = new JTable();
		JTable docks = new JTable();

		statusModels.add(new FinalTableModel((ArrayList<Thing>) ((ArrayList<?>) sp.persons)));
		persons.setModel(statusModels.get(statusModels.size() - 1));

		statusModels.add(new FinalTableModel((ArrayList<Thing>) ((ArrayList<?>) sp.docks)));
		docks.setModel(statusModels.get(statusModels.size() - 1));

		statusTab.add(persons);
		statusTab.add(docks);

		new Thread(new StatusUpdater()).start();

		return statusTab;
	}

	/** Generate tree display panel */
	public JPanel createPanelTree() {
		JPanel treePanel = new JPanel();
		treeControlsPanel = new JPanel();
		treeText = new JTextArea();
		// DefaultMutableTreeNode top = new DefaultMutableTreeNode("The Java
		// Series");

		dataTree = new JTree(world.treeNode());
		dataTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		dataTree.addTreeSelectionListener(new treeItemSelectionListener());

		JScrollPane treeScroll = new JScrollPane(dataTree);

		JSplitPane rightPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		rightPane.setTopComponent(treeText);
		rightPane.setBottomComponent(treeControlsPanel);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setLeftComponent(treeScroll);
		splitPane.setRightComponent(rightPane);

		// Dimension minimumSize = new Dimension(200, 50);
		// htmlView.setMinimumSize(minimumSize);
		// treeView.setMinimumSize(minimumSize);
		splitPane.setDividerLocation(100);
		splitPane.setPreferredSize(new Dimension(500, 500));

		treePanel.add(splitPane);
		treePanel.setMinimumSize(new Dimension(200, 100));

		return treePanel;
	}

	/** Generate query portion of search panel */
	public JPanel createPanelSearchQuery() {
		searchTerm = new JTextField(SEARCH_WIDTH);

		rbSearchName = new JRadioButton("By Name");
		rbSearchName.setSelected(true);
		rbSearchIndex = new JRadioButton("By Index");
		rbSearchSkill = new JRadioButton("By Skill");

		ButtonGroup group = new ButtonGroup();
		group.add(rbSearchName);
		group.add(rbSearchIndex);
		group.add(rbSearchSkill);

		JPanel panel1 = new JPanel();
		panel1.add(searchTerm);
		panel1.add(makeActButtonSearch("Search"));
		panel1.setBorder(new TitledBorder(new EtchedBorder(), "Search For:"));

		JPanel panel2 = new JPanel();
		panel2.add(rbSearchName);
		panel2.add(rbSearchIndex);
		panel2.add(rbSearchSkill);
		panel2.setBorder(new TitledBorder(new EtchedBorder(), "Search By:"));

		JPanel panel3 = new JPanel();
		panel3.setLayout(new GridLayout(2, 1));
		panel3.add(panel1);
		panel3.add(panel2);

		return panel3;
	}

	/** Generate results portion of search panel */
	public JPanel createPanelSearchResults() {
		JPanel results = new JPanel();

		resultsText = new JTextArea(RAREA_ROWS, RAREA_COLUMNS);
		resultsText.setText("");
		resultsText.setEditable(false);

		JScrollPane scrollPane = new JScrollPane(resultsText);
		results.add(scrollPane);

		return results;
	}

	// ---------------------------Action
	// Listeners-------------------------------------------

	/** ActionListener performs search operation */
	class ActButtonRunJobsListener implements ActionListener {
		public ActButtonRunJobsListener() {
		}

		public void actionPerformed(ActionEvent event) {
			if (!world.running) {
				world.running = true;
				world.start();
			}

		}
	}

	/** ActionListener performs search operation */
	class ActButtonSearchListener implements ActionListener {
		public ActButtonSearchListener() {
		}

		public void actionPerformed(ActionEvent event) {
			if (rbSearchIndex.isSelected()) {
				int queryInt;
				try {
					queryInt = Integer.parseInt(searchTerm.getText());
				} catch (Exception e) {
					resultsText.setText("Invalid Entry: Index search may only contain an integer value (ie. 12345)");
					return;
				}
				resultsText.setText(world.searchByIndex(queryInt));
			}
			if (rbSearchName.isSelected())
				resultsText.setText(world.searchByName(searchTerm.getText()));
			if (rbSearchSkill.isSelected())
				resultsText.setText(world.searchBySkill(searchTerm.getText()));

		}
	}

	/** ActionListener navigates to overview page */
	class NavButtonOverviewListener implements ActionListener {
		public NavButtonOverviewListener() {
		}

		public void actionPerformed(ActionEvent event) {
			currentPage = "Overview";
			changeLayout();
		}
	}

	/** ActionListener navigates to jobs page */
	class NavButtonJobsListener implements ActionListener {
		public NavButtonJobsListener() {
		}

		public void actionPerformed(ActionEvent event) {
			currentPage = "Jobs";
			changeLayout();
		}
	}

	/** ActionListener navigates to search page */
	class NavButtonSearchListener implements ActionListener {
		public NavButtonSearchListener() {
		}

		public void actionPerformed(ActionEvent event) {
			currentPage = "Search";
			changeLayout();
		}
	}

	/** ActionListener navigates to search page */
	class NavButtonStatusListener implements ActionListener {
		public NavButtonStatusListener() {
		}

		public void actionPerformed(ActionEvent event) {
			currentPage = "Status";
			changeLayout();
		}
	}

	/** ActionListener navigates to overview page */
	class NavButtonTreeListener implements ActionListener {
		public NavButtonTreeListener() {
		}

		public void actionPerformed(ActionEvent event) {
			currentPage = "Tree";
			changeLayout();
		}
	}

	class ActButtonSortListener implements ActionListener {
		public ActButtonSortListener() {
		}

		public void actionPerformed(ActionEvent event) {
			treeSort();

		}
	}

	/** ActionListener displays "About" message */
	class MenuItemAboutListener implements ActionListener {
		public MenuItemAboutListener() {
		}

		public void actionPerformed(ActionEvent event) {
			JOptionPane.showMessageDialog(mainPanel,
					"Hey, I'm John Sakosky!\nThis is my Sea Port Simulator\n\nProject 4\n.");
		}
	}

	class MenuItemGotoOverviewListener implements ActionListener {
		public MenuItemGotoOverviewListener() {
		}

		public void actionPerformed(ActionEvent event) {
			currentPage = "Overview";
			changeLayout();
		}
	}

	class MenuItemGotoSearchListener implements ActionListener {
		public MenuItemGotoSearchListener() {
		}

		public void actionPerformed(ActionEvent event) {
			currentPage = "Search";
			changeLayout();
		}
	}

	class MenuItemGotoTreeListener implements ActionListener {
		public MenuItemGotoTreeListener() {
		}

		public void actionPerformed(ActionEvent event) {
			currentPage = "Tree";
			changeLayout();
		}
	}

	/** ActionListener open FileChooser dialog and reset view of GUI */
	class MenuItemOpenListener implements ActionListener {
		public MenuItemOpenListener() {
		}

		public void actionPerformed(ActionEvent event) {
			world = new World(openFileChooser());
			reset();
			changeLayout();
		}
	}

	/** ActionListener resets GUI to initial state */
	class MenuItemResetListener implements ActionListener {
		public MenuItemResetListener() {
		}

		public void actionPerformed(ActionEvent event) {
			reset();
			changeLayout();
		}
	}

	/** ActionListener Terminates program */
	class MenuItemExitListener implements ActionListener {
		public MenuItemExitListener() {
		}

		public void actionPerformed(ActionEvent event) {
			System.exit(0);
		}
	}

	public class ProgressCellRender extends JProgressBar implements TableCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			int progress = 0;
			if (value instanceof Float) {
				progress = Math.round(((Float) value) * 100f);
			} else if (value instanceof Integer) {
				progress = (int) value;
			}
			setValue(progress);
			return this;
		}
	}

	class treeItemSelectionListener implements TreeSelectionListener {
		public void valueChanged(TreeSelectionEvent e) {

			updateTreeText();

		}
	}

	public class RowData {

		private Job job;
		private String shipName;
		private String jobName;

		public RowData(Job j) {
			this.job = j;
			shipName = job.myShip.name;
			jobName = job.name;
		}

		public String getShipName() {
			return shipName;
		}

		public String getJobName() {
			return jobName;
		}

		public String getState() {
			return job.status.toString();
		}

		public float getStatus() {
			return job.getProgress();
		}

	}

	public class UpdatableTableModel extends AbstractTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private ArrayList<RowData> rows;
		private HashMap<Job, RowData> mapLookup;

		public UpdatableTableModel() {
			rows = new ArrayList<RowData>(25);
			mapLookup = new HashMap<Job, RowData>(25);
		}

		@Override
		public int getRowCount() {
			return rows.size();
		}

		@Override
		public int getColumnCount() {
			return 4;
		}

		@Override
		public String getColumnName(int column) {
			String name = "??";
			switch (column) {
			case 0:
				name = "ShipName";
				break;
			case 1:
				name = "Job Name";
				break;
			case 2:
				name = "Progress";
				break;
			case 3:
				name = "Status";
				break;
			}
			return name;
		}

		public Job getJobAt(int rowIndex) {
			return rows.get(rowIndex).job;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			RowData rowData = rows.get(rowIndex);
			Object value = null;
			switch (columnIndex) {
			case 0:
				value = rowData.getShipName();
				break;
			case 1:
				value = rowData.getJobName();
				break;
			case 2:
				value = rowData.getState();
				break;
			case 3:
				value = rowData.getStatus();
				break;
			}
			return value;
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			// RowData rowData = rows.get(rowIndex);
			switch (columnIndex) {
			case 3:
				if (aValue instanceof Float) {
					// rowData.setStatus((float) aValue);
				}
				break;
			}
		}

		public void addJob(Job job) {
			RowData rowData = new RowData(job);
			mapLookup.put(job, rowData);
			rows.add(rowData);
			fireTableRowsInserted(rows.size() - 1, rows.size() - 1);
		}

		protected void updateStatus(Job job) {
			RowData rowData = mapLookup.get(job);
			if (rowData != null) {
				int row = rows.indexOf(rowData);
				float p = (float) job.getProgress() / 100f;
				setValueAt(p, row, 3);
				fireTableCellUpdated(row, 3);
			}
		}
	}

	public class FinalTableModel extends AbstractTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private ArrayList<Thing> li = new ArrayList<Thing>();
		private String[] columnNames = { "Name", "ID", "Status" };

		public FinalTableModel(ArrayList<Thing> list) {
			this.li = list;
		}

		@Override
		public String getColumnName(int columnIndex) {
			return columnNames[columnIndex];
		}

		@Override
		public int getRowCount() {
			return li.size();
		}

		@Override
		public int getColumnCount() {
			return 3;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Thing thing = li.get(rowIndex);
			switch (columnIndex) {
			case 0:
				return thing.name;
			case 1:
				return thing.index;
			case 2:
				return thing.status.toString();
			}
			return null;
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			switch (columnIndex) {
			case 0:
				return String.class;
			case 1:
				return Integer.class;
			case 2:
				return String.class;
			}
			return null;
		}
	}

	public class JobStatusWorker extends SwingWorker<Job, Job> {

		private Job currentJob;
		private UpdatableTableModel model;

		public JobStatusWorker(UpdatableTableModel model, Job job) {
			this.currentJob = job;
			this.model = model;

			addPropertyChangeListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					if (evt.getPropertyName().equals("progress")) {
						JobStatusWorker.this.model.updateStatus(currentJob);
					}
				}
			});

		}

		@Override
		protected Job doInBackground() throws Exception {

			int progress = 0;

			while (progress < 100) {
				progress = (int) (100 * currentJob.getProgress());
				setProgress(progress);
				Thread.sleep(25);
			}

			return currentJob;
		}
	}

	public class StatusUpdater implements Runnable {

		@Override
		public void run() {
			boolean run = true;

			while (run) {
				for (FinalTableModel m : statusModels) {
					m.fireTableDataChanged();
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// TODO Auto-generated method stub

		}

	}

	/**
	 * Main Method Initialize world with data from input file and open GUI
	 */
	public static void main(String args[]) {
		// new Testing().testAll();
		world = new World(openFileChooser());
		new SeaPortProgram().setVisible(true);
	}

}
