package de.aaa.al_shajara.gui;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.aaa.al_shajara.Logic;
import de.aaa.al_shajara.data.Person;
import de.aaa.al_shajara.data.TreeGuiListener;

public class TreeEditView extends JPanel implements ChangeListener {

	private Logic logic;

	PersonalTreeView personalTreeView;
	EditFormView editFormView;
	
	public TreeEditView(Logic logic){
		this.logic = logic;
		createLayout();
	}

	private void createLayout() {
		personalTreeView = new PersonalTreeView(logic);
		editFormView = new EditFormView(logic);
		personalTreeView.addTreeGuiListener(editFormView);
		editFormView.listenerManager.addTreeChangedListener(personalTreeView);
		editFormView.listenerManager.addChangedListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				logic.overwritePerson((Person) e.getSource(), e.getActionCommand());
			}
		});

		final SplitPane splitPane = new SplitPane(JSplitPane.HORIZONTAL_SPLIT, 0.2, editFormView, personalTreeView);
		this.setLayout(new BorderLayout());
		this.add(splitPane,BorderLayout.CENTER);
		personalTreeView.addTreeGuiListener(new TreeGuiListener() {
			
			@Override
			public void showDetails(boolean show) {
				editFormView.setVisible(show);
				if (show){
					splitPane.setDividerLocation(0.2);
				}
			}
			
			@Override public void personClicked(Person clickedPerson) { }
			
			@Override public void addChildCommand(Person parent) { }

			@Override public void editPerson(Person toEdit) { }

			@Override public void addFatherCommand(Person child) { }

			@Override public void addMotherCommand(Person child) { }

			@Override
			public void deletePerson(Person person) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public void stateChanged(ChangeEvent event) {
		personalTreeView.stateChanged(event);
	}
	
	public Image grabCurrentImage(){
		return personalTreeView.grabCurrentImage();
	}
	
}
