package de.aaa.al_shajara.gui;

import java.awt.BorderLayout;
import java.awt.Image;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.aaa.al_shajara.Logic;
import de.aaa.al_shajara.data.Person;
import de.aaa.al_shajara.data.TreeChangedListener;
import de.aaa.al_shajara.data.TreeGuiListener;
import de.aaa.al_shajara.gui.graphics2D.TreeGraphicPanel;
import de.aaa.al_shajara.gui.graphics2D.TreeGraphicPanelAAA;
import de.aaa.al_shajara.gui.graphics2D.jgraphx.TreeGraphicPanelJGX;

public class PersonalTreeView extends JPanel implements ChangeListener, TreeChangedListener {

	private static final long serialVersionUID = -7338932403138030333L;
	private final Logic logic;
	private Person centerPerson;
	private TreeGraphicPanel graphicPanel;
	public PersonalTreeView(Logic logic) {
		super();
		this.logic = logic;
		//		graphicPanel = new TreeGraphicPanelJung(logic, logic.getFamilyTree().getMemberById("MariaElenaAtalla2011-10-25"));
		//		graphicPanel = new TreeGraphicPanelAAA(logic, logic.getFamilyTree().getMemberById("MariaElenaAtalla2011-10-25"));
		graphicPanel = new TreeGraphicPanelJGX(logic, logic.getFamilyTree().getMemberById("MariaElenaAtalla2011-10-25"));
		createLayout();
	}

	private void createLayout() {
		this.setLayout(new BorderLayout());
		this.add(graphicPanel,BorderLayout.CENTER);
	}

	@Override
	public void stateChanged(ChangeEvent event) {
		graphicPanel.refresh();
	}

	public Image grabCurrentImage(){
		return graphicPanel.grabCurrentImage();
	}

	public void addTreeGuiListener(TreeGuiListener listener) {
		graphicPanel.listenerManager.addTreeGuiListener(listener);
	}

	@Override
	public void personAdded(Person addedPerson) {
		graphicPanel.refresh();
	}

	@Override
	public void personModified(Person modifiedPerson) {
		graphicPanel.refresh();
	}

	@Override
	public void personRemoved(Person removedPerson) {
		graphicPanel.refresh();
	}
}
