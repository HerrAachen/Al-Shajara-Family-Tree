package de.aaa.al_shajara.gui.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

/**
 * A listener which simulates the click of a button when the event is fired
 * @author mat
 */
public class ButtonClickListener implements ActionListener {

	private final AbstractButton button;
	
	/**
	 * Pass a button to simulate a click on it
	 */
	public ButtonClickListener(AbstractButton button){
		this.button = button;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		this.button.doClick();
	}
}
