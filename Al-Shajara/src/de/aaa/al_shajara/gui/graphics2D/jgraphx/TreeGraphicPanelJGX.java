package de.aaa.al_shajara.gui.graphics2D.jgraphx;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.view.mxGraph;

import de.aaa.al_shajara.Configuration;
import de.aaa.al_shajara.Logic;
import de.aaa.al_shajara.data.Person;
import de.aaa.al_shajara.gui.GuiHelper;
import de.aaa.al_shajara.gui.PersonCellRenderer;
import de.aaa.al_shajara.gui.graphics2D.TreeGraphicPanel;

public class TreeGraphicPanelJGX extends TreeGraphicPanel {

	private JComboBox centerPersonBox;
	private JCheckBox showAncestorsBox;
	private JCheckBox showDescendantsBox;
	private JCheckBox showDetailsBox;
	private mxGraphComponent graphComp;
	private JPopupMenu popup;
	private mxCell clickedCell;
	private JMenuItem addParentItem;
	private JMenuItem addMotherItem;

	public TreeGraphicPanelJGX(Logic logic, Person centerPerson) {
		super(logic, centerPerson);
		createLayout();
	}

	private void createLayout() {
		this.setLayout(new BorderLayout());
		//		mxGraph g = createEntireFamilyGraph();

		ActionListener refreshActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evn) {
				refresh();
			}
		};
		showAncestorsBox = new JCheckBox("Show ancestors", true);
//		showAncestorsBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		showAncestorsBox.addActionListener(refreshActionListener);
		showDescendantsBox = new JCheckBox("Show descendants", true);
//		showDescendantsBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		showDescendantsBox.addActionListener(refreshActionListener);
		JPanel showAncestorsPanel = new JPanel();
		showAncestorsPanel.setLayout(new BoxLayout(showAncestorsPanel, BoxLayout.LINE_AXIS));
		showAncestorsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		showAncestorsPanel.add(showAncestorsBox);
		showAncestorsPanel.add(showDescendantsBox);
		showDetailsBox = new JCheckBox("Show details", true);
		showDetailsBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		showDetailsBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				listenerManager.fireShowDetails(showDetailsBox.isSelected());
			}
		});
		centerPersonBox = new JComboBox(GuiHelper.appendNullValueAtBeginning(logic.getFamilyTree().getMembers()));
		centerPersonBox.setRenderer(PersonCellRenderer.getInstance());
		centerPersonBox.addActionListener(refreshActionListener);
		JPanel optionsPanel = new JPanel();
		JPanel centerPersonPanel = new JPanel();
		centerPersonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		centerPersonPanel.setLayout(new BorderLayout());
		centerPersonPanel.add(new JLabel("Root Person:  "), BorderLayout.LINE_START);
		centerPersonPanel.add(centerPersonBox, BorderLayout.CENTER);
		BoxLayout boxLayout = new BoxLayout(optionsPanel, BoxLayout.PAGE_AXIS);
		optionsPanel.setLayout(boxLayout);
		optionsPanel.add(centerPersonPanel);
//		optionsPanel.add(showAncestorsBox);
//		optionsPanel.add(showDescendantsBox);
		optionsPanel.add(showAncestorsPanel);
		optionsPanel.add(showDetailsBox);
		this.add(optionsPanel,BorderLayout.PAGE_START);
		mxGraphComponent graphComp = createGraphComponent();
		this.add(graphComp,BorderLayout.CENTER);
		popup = new JPopupMenu();
		JMenuItem editItem = new JMenuItem("Edit");
		JMenuItem addChildItem = new JMenuItem("Add child");
		addParentItem = new JMenuItem("Add father");
		addMotherItem = new JMenuItem("Add mother");
		JMenuItem centerTreeItem = new JMenuItem("Center tree");
		JMenuItem deleteItem = new JMenuItem("Delete person");
		editItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Person clickedPerson = (Person) clickedCell.getValue();
				listenerManager.fireEditPerson(clickedPerson);
			}
		});
		addChildItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Person clickedPerson = (Person) clickedCell.getValue();
				listenerManager.fireAddChildCommand(clickedPerson);
			}
		});
		addParentItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Person clickedPerson = (Person) clickedCell.getValue();
				listenerManager.fireAddFatherCommand(clickedPerson);
			}
		});
		addMotherItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Person clickedPerson = (Person) clickedCell.getValue();
				listenerManager.fireAddMotherCommand(clickedPerson);
			}
		});
		centerTreeItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Person clickedPerson = (Person) clickedCell.getValue();
				centerTreeAroundPerson(clickedPerson);
			}
		});
		deleteItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Person clickedPerson = (Person) clickedCell.getValue();
				listenerManager.fireDeletePerson(clickedPerson);
			}
		});
		
		popup.add(editItem);
		popup.add(addChildItem);
		popup.add(addParentItem);
		popup.add(addMotherItem);
		popup.add(deleteItem);
		popup.add(centerTreeItem);
	}

	private mxGraphComponent createGraphComponent() {
		centerPerson = (Person) centerPersonBox.getSelectedItem();
		boolean showAncestors = showAncestorsBox.isSelected();
		boolean showDescendants = showDescendantsBox.isSelected();
		mxGraph g = createPersonalGraph(showAncestors,showDescendants);
		graphComp = new mxGraphComponent(g);
		graphComp.setToolTips(true);
		graphComp.setDragEnabled(false);
		graphComp.setComponentPopupMenu(popup);
		graphComp.getGraphControl().addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				int y = e.getY();	//absolute position in graph visualization
				int x = e.getX();
				clickedCell = (mxCell) graphComp.getCellAt(x, y);
				if (clickedCell!=null){
					if (e.getButton()==MouseEvent.BUTTON3){
						if (clickedCell!=null && clickedCell.getValue() instanceof Person){
							Person clickedPerson = (Person) clickedCell.getValue();
							addParentItem.setEnabled(clickedPerson.getFather()==null);
							addMotherItem.setEnabled(clickedPerson.getMother()==null);
						}
						else {
							addParentItem.setEnabled(false);
							addMotherItem.setEnabled(false);
						}
						popup.show(graphComp, graphComp.getMousePosition().x, graphComp.getMousePosition().y);//position in current visible section (differs from above when scrolled)
					}
					if (e.getButton()==MouseEvent.BUTTON1){
						Object cellValue = clickedCell.getValue();
						if (e.getClickCount()==1){
							if (cellValue instanceof Person){
								listenerManager.firePersonClicked((Person) cellValue);
							}
						}
						if (e.getClickCount()==2 && !e.isConsumed()){
							e.consume();
							centerTreeAroundPerson((Person) cellValue);
						}
					}
				}
			}
		});
		return graphComp;
	}

	@Override
	public void refresh() {
		centerPerson = (Person) centerPersonBox.getSelectedItem();
		if (centerPerson==null && centerPersonBox.getItemCount()>0){
			centerPersonBox.setSelectedIndex(1);
			centerPerson = (Person) centerPersonBox.getSelectedItem();
		}
		boolean showAncestors = showAncestorsBox.isSelected();
		boolean showDescendants = showDescendantsBox.isSelected();
		if (centerPerson!=null){
			mxGraph g = createPersonalGraph(showAncestors,showDescendants);
			graphComp.setGraph(g);
			graphComp.repaint();
		}
	}
	private mxGraph createPersonalGraph(boolean includeAncestors, boolean includeDescendants){
		logic.getFamilyTree().addChildLinks();
		FamilyGraphJGX graph = new FamilyGraphJGX();
		if (centerPerson!=null){
			Object frameNode = graph.getDefaultParent();
			graph.getModel().beginUpdate();

			try {
				Object vertex = graph.insertVertex(centerPerson);
				if (includeAncestors){
					addParents(centerPerson,vertex,graph);
				}
				if (includeDescendants){
					addChildren(centerPerson,vertex,graph);
				}
				mxGraphLayout layout = new FamilyTreeHierarchicalLayout(graph);
				layout.execute(frameNode);
			}
			finally {
				graph.getModel().endUpdate();
			}
		}
		graph.setCellsEditable(false);
		graph.setCellsMovable(false);
		graph.setCellsResizable(false);
		graph.setEdgeLabelsMovable(false);
		graph.setCellsSelectable(false);
		graph.setCellsDisconnectable(false);
		graph.setCellsLocked(true);
		return graph;
	}

	/**
	 * Recursively adds all parents of the specified person to the graph
	 * @param person
	 * @param personVertex
	 * @param graph
	 */
	private void addParents(Person person, Object personVertex, FamilyGraphJGX graph) {
		Person mother = person.getMother();
		Person father = person.getFather();
		if (Configuration.isUsePseudoNodes()){
			if (mother!=null || father!=null){
				Object pseudoNode = graph.insertPseudoNode();
				graph.insertEdgeToPerson(pseudoNode, personVertex);
				if (mother!=null){
					Object vertex = graph.insertVertex(mother);
					graph.insertEdgeToPseudoNode(vertex, pseudoNode);
					addParents(mother, vertex, graph);
				}
				if (father!=null){
					Object vertex = graph.insertVertex(father);
					graph.insertEdgeToPseudoNode(vertex, pseudoNode);
					addParents(father, vertex, graph);
				}
			}
		}
		else {
			if (mother!=null){
				Object vertex = graph.insertVertex(mother);
				graph.insertEdgeToPerson(vertex, personVertex);
				addParents(mother, vertex, graph);
			}
			if (father!=null){
				Object vertex = graph.insertVertex(father);
				graph.insertEdgeToPerson(vertex, personVertex);
				addParents(father, vertex, graph);
			}
		}
	}

	private void addChildren(Person person, Object personVertex, FamilyGraphJGX graph){
		Map<Person, Person> children2OtherParent = person.children2OtherParent();
		if (Configuration.isUsePseudoNodes()){
			if (!children2OtherParent.isEmpty()){
//				Object pseudoNode = graph.insertPseudoNode();
//				graph.insertEdgeToPseudoNode(personVertex, pseudoNode);
//				Set<Person> newParentsConnected = new HashSet<Person>();//keeps track of the other parents added, to avoid inserting redundant edges
//				for(Map.Entry<Person, Person> child2OtherParent: children2OtherParent.entrySet()){
//					Person child = child2OtherParent.getKey();
//					Person otherParent = child2OtherParent.getValue();
//					Object childVertex = graph.insertVertex(child);
//					if (otherParent!=null){
//						if (!newParentsConnected.contains(otherParent)){
//							Object parentVertex = graph.insertVertex(otherParent);
//							graph.insertEdgeToPseudoNode(parentVertex, pseudoNode);
//							newParentsConnected.add(otherParent);
//						}
//					}
//					graph.insertEdgeToPerson(pseudoNode, childVertex);
//					addChildren(child,childVertex,graph);
//				}
				
//				Object pseudoNode = graph.insertPseudoNode();
//				graph.insertEdgeToPseudoNode(personVertex, pseudoNode);
				Map<Person,Object> parent2PseudoNode = new HashMap<Person,Object>();
				Set<Person> newParentsConnected = new HashSet<Person>();//keeps track of the other parents added, to avoid inserting redundant edges
				for(Map.Entry<Person, Person> child2OtherParent: children2OtherParent.entrySet()){
					Person child = child2OtherParent.getKey();
					Person otherParent = child2OtherParent.getValue();
					Object childVertex = graph.insertVertex(child);
					Object pseudoNode = parent2PseudoNode.get(otherParent);
					if (pseudoNode==null){
						pseudoNode = graph.insertPseudoNode();
						graph.insertEdgeToPseudoNode(personVertex, pseudoNode);
						parent2PseudoNode.put(otherParent, pseudoNode);
					}
					if (otherParent!=null){
						if (!newParentsConnected.contains(otherParent)){
							Object parentVertex = graph.insertVertex(otherParent);
							graph.insertEdgeToPseudoNode(parentVertex, pseudoNode);
							newParentsConnected.add(otherParent);
						}
					}
					graph.insertEdgeToPerson(pseudoNode, childVertex);
					addChildren(child,childVertex,graph);
				}
			}
		}
		else {
			for(Map.Entry<Person, Person> child2OtherParent: children2OtherParent.entrySet()){
				Person child = child2OtherParent.getKey();
				Person otherParent = child2OtherParent.getValue();
				Object childVertex = graph.insertVertex(child);
				if (otherParent!=null){
					Object parentVertex = graph.insertVertex(otherParent);
					graph.insertEdgeToPerson(parentVertex, childVertex);
				}
				graph.insertEdgeToPerson(personVertex, childVertex);
				addChildren(child,childVertex,graph);
			}
		}
	}

	private mxGraph createEntireFamilyGraph(){
		FamilyGraphJGX graph = new FamilyGraphJGX();
		Object parent = graph.getDefaultParent();
		graph.getModel().beginUpdate();
		Map<Person, Object> person2Vertex = new HashMap<Person, Object>();

		try
		{
			for(Person p: logic.getFamilyTree().getMembers()){
				Object vertex = graph.insertVertex(p);
				person2Vertex.put(p, vertex);
			}
			for(Person p: logic.getFamilyTree().getMembers()){
				Person mother = p.getMother();
				Person father = p.getFather();
				if (mother!=null)
					graph.insertEdge(parent, null, "", person2Vertex.get(mother), person2Vertex.get(p));
				if (father!=null)
					graph.insertEdge(parent, null, "", person2Vertex.get(father), person2Vertex.get(p));
			}
			mxGraphLayout layout = new mxHierarchicalLayout(graph);
			layout.execute(parent);
		}
		finally
		{
			graph.getModel().endUpdate();
		}
		return graph;
	}

	@Override
	public Image grabCurrentImage() {
		return mxCellRenderer.createBufferedImage(graphComp.getGraph(), null, 1, Color.WHITE, false, null);
	}

	private void centerTreeAroundPerson(Person clickedPerson) {
		centerPersonBox.setSelectedItem(clickedPerson);
	}

}
