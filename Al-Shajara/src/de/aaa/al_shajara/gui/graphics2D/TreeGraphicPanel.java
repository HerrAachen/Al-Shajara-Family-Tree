package de.aaa.al_shajara.gui.graphics2D;

import java.awt.Image;

import javax.swing.JPanel;

import de.aaa.al_shajara.Logic;
import de.aaa.al_shajara.data.Person;
import de.aaa.al_shajara.data.TreeChangedListener;
import de.aaa.al_shajara.gui.ListenerManager;

public abstract class TreeGraphicPanel extends JPanel implements TreeChangedListener{

	protected final Logic logic;
	protected Person centerPerson;
	public final ListenerManager listenerManager = new ListenerManager();

	public TreeGraphicPanel(Logic logic, Person centerPerson) {
		super();
		this.logic = logic;
		this.centerPerson = centerPerson;
	}
	
	abstract public void refresh();
	
	abstract public Image grabCurrentImage();
	
	public void personAdded(Person addedPerson){
		refresh();
	}
	
	public void personModified(Person modifiedPerson) {
		refresh();
	}
	
	public void personRemoved(Person removedPerson) {
		refresh();
	}
}
