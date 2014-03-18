package de.aaa.al_shajara.gui;

import java.awt.Component;

import javax.swing.JSplitPane;

public class SplitPane extends JSplitPane {

	
	private static final long serialVersionUID = -6496195516855194807L;

	public SplitPane(int orientation,double initialDividerPosition,Component c1, Component c2){
		super(orientation, c1, c2);
		setDividerLocation(initialDividerPosition);
		setDividerSize(8);
	}
	
	/**
	 * Does the same as JSplitPane.add, but maintains the position of the divider
	 */
	@Override
	public Component add(Component component){
		int lastDivLoc = getDividerLocation();
		Component res = super.add(component);
		setDividerLocation(lastDivLoc);
		return res;
	}
}
