package de.aaa.al_shajara.gui.graphics2D;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.JScrollPane;

import de.aaa.al_shajara.Logic;
import de.aaa.al_shajara.data.Person;

public class TreeGraphicPanelAAA extends TreeGraphicPanel {

	private TreeVisualizationAAA treeVis;
	public TreeGraphicPanelAAA(Logic logic, Person centerPerson) {
		super(logic, centerPerson);
		treeVis = new TreeVisualizationAAA(logic, centerPerson);
		createLayout();
	}
	
	private void createLayout(){
		this.setLayout(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setViewportView(treeVis);
		this.add(scrollPane,BorderLayout.CENTER);
	}

	@Override
	public void refresh() {
		treeVis.refresh();
	}

	@Override
	public Image grabCurrentImage() {
		return treeVis.grabCurrentImage();
	}

}
