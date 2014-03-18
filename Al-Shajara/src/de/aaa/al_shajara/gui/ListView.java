package de.aaa.al_shajara.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.aaa.al_shajara.Logic;
import de.aaa.al_shajara.data.Person;
import de.aaa.al_shajara.data.TreeChangedListener;

public class ListView extends JPanel {

	private static final long serialVersionUID = -7599850064382636415L;

	private final Logic logic;
	private final JList list;
	private final DefaultListModel listModel;
	private final EditFormView editFormView;
	
	public ListView(Logic logik){
		super();
		this.logic = logik;
		list = new JList();
		listModel = new DefaultListModel();
		list.setCellRenderer(PersonCellRenderer.getInstance());
		list.setModel(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addListSelectionListener(new PersonListListener());
		editFormView = new EditFormView(logic);
		editFormView.listenerManager.addChangedListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				fillList();
				logic.overwritePerson((Person) e.getSource(), e.getActionCommand());
			}
		});
		editFormView.listenerManager.addTreeChangedListener(new TreeChangedListener() {
			@Override
			public void personRemoved(Person removedPerson) {
				fillList();
			}
			
			@Override
			public void personModified(Person modifiedPerson) {
			}
			
			@Override
			public void personAdded(Person addedPerson) {
			}
		});
		fillList();
		createLayout();
	}

	private void createLayout() {
		JSplitPane splitPane = new JSplitPane();
		JScrollPane listScrollPane = new JScrollPane(list);
		splitPane.setLeftComponent(listScrollPane);
		splitPane.setRightComponent(editFormView);
		this.setLayout(new BorderLayout());
		this.add(splitPane);
	}
	
	public void fillList(){
		listModel.clear();
		for(Person p: logic.getFamilyTree().getMembers()){
			listModel.addElement(p);
		}
	}

	public void addChangedListener(ActionListener aL){
		editFormView.listenerManager.addChangedListener(aL);
	}
	
	private class PersonListListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent event) {
			if (event.getValueIsAdjusting()==false){
				int selectedIndex = list.getSelectedIndex();
				if (selectedIndex>=0){
					editFormView.setPerson((Person) listModel.get(selectedIndex));
				}
				
			}
		}
		
	}
}
