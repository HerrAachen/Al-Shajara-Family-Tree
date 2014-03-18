package de.aaa.al_shajara.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import de.aaa.al_shajara.data.Person;
import de.aaa.al_shajara.data.TreeChangedListener;
import de.aaa.al_shajara.data.TreeGuiListener;

public class ListenerManager {

	private final List<ActionListener> changedListener = new LinkedList<ActionListener>();
	private final List<ActionListener> actionListener = new LinkedList<ActionListener>();
	private final List<TreeChangedListener> treeChangedListeners = new LinkedList<TreeChangedListener>();
	private final List<TreeGuiListener> treeGuiListeners = new LinkedList<TreeGuiListener>();
	
	public List<ActionListener> getChangedListener() {
		return changedListener;
	} 
	
	public void addChangedListener(ActionListener aL){
		changedListener.add(aL);
	}

	public void addActionListener(ActionListener aL){
		actionListener.add(aL);
	}
	
	public void fireChangedEvent(Object object, int id, String action){
		for(ActionListener aL: changedListener){
			aL.actionPerformed(new ActionEvent(object, id, action));
		}
	}

	public void fireActionEvent(Object object, int id, String action){
		for(ActionListener aL: actionListener){
			aL.actionPerformed(new ActionEvent(object, id, action));
		}
	}

	public void addTreeChangedListener(TreeChangedListener listener) {
		treeChangedListeners.add(listener);
	}

	public void addTreeGuiListener(TreeGuiListener listener) {
		treeGuiListeners.add(listener);
	}

	public void firePersonClicked(Person p) {
		for(TreeGuiListener l: treeGuiListeners){
			l.personClicked(p);
		}
	}
	
	public void fireAddChildCommand(Person clickedPerson) {
		for(TreeGuiListener l: treeGuiListeners){
			l.addChildCommand(clickedPerson);
		}
	}
	
	public void firePersonAdded(Person p) {
		for(TreeChangedListener l: treeChangedListeners){
			l.personAdded(p);
		}
	}

	public void firePersonRemoved(Person p) {
		for(TreeChangedListener l: treeChangedListeners){
			l.personRemoved(p);
		}
	}
	
	public void firePersonModified(Person p) {
		for(TreeChangedListener l: treeChangedListeners){
			l.personModified(p);
		}
	}

	public void fireShowDetails(boolean selected) {
		for(TreeGuiListener l: treeGuiListeners){
			l.showDetails(selected);
		}
	}

	public void fireAddFatherCommand(Person clickedPerson) {
		for(TreeGuiListener l: treeGuiListeners){
			l.addFatherCommand(clickedPerson);
		}
	}

	public void fireAddMotherCommand(Person clickedPerson) {
		for(TreeGuiListener l: treeGuiListeners){
			l.addMotherCommand(clickedPerson);
		}
	}

	public void fireEditPerson(Person clickedPerson) {
		for(TreeGuiListener l: treeGuiListeners){
			l.editPerson(clickedPerson);
		}
	}

	public void fireDeletePerson(Person clickedPerson) {
		for(TreeGuiListener l: treeGuiListeners){
			l.deletePerson(clickedPerson);
		}
	}
	
	

}
