package de.aaa.al_shajara.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.aaa.al_shajara.Configuration;
import de.aaa.al_shajara.Logic;

public class OptionsDialog extends AbstractDialog {

  private Logic logic;
  
  private JComboBox alphabetBox;
  
  public OptionsDialog(Window owner, Logic logic) {
    super(owner);
    this.logic = logic;
    createLayout();
  }

  private void createLayout() {
    this.setTitle("Options");
    JPanel optionPanel = new JPanel();
    optionPanel.setLayout(new GridLayout(1, 2));
    optionPanel.add(new JLabel("Primary Writing System:"));
    final JButton saveButton = new JButton("OK");
    alphabetBox = new JComboBox(logic.getFamilyTree().getAlphabets().toArray());
    alphabetBox.addKeyListener(new KeyListener() {
		
		@Override
		public void keyTyped(KeyEvent arg0) { }
		
		@Override
		public void keyReleased(KeyEvent arg0) { }
		
		@Override
		public void keyPressed(KeyEvent event) {
			if (event.getKeyCode()==KeyEvent.VK_ENTER)
				saveButton.doClick();
		}
	});
    optionPanel.add(alphabetBox);
    
    this.setLayout(new BorderLayout());
    this.add(optionPanel,BorderLayout.CENTER);
    
    JPanel buttonPanel = new JPanel();
    JButton cancelButton = new JButton("Cancel");
    saveButton.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        save();
        close();
      }
    });
    cancelButton.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent arg0) {
        close();
      }
    });
    buttonPanel.setLayout(new FlowLayout());
    buttonPanel.add(saveButton);
    buttonPanel.add(cancelButton);
    setOptionsToGui();
    this.add(buttonPanel,BorderLayout.PAGE_END);
    this.pack();
    this.centerInParent();
    this.setResizable(false);
  }
  
  private void setOptionsToGui(){
    String primaryAlphabet = Configuration.getPrimaryAlphabet();
    alphabetBox.setSelectedItem(primaryAlphabet);
  }

  protected void save() {
    String primaryAlphabet = (String) alphabetBox.getSelectedItem();
    Configuration.setPrimaryAlphabet(primaryAlphabet);
    //TODO fire listener
  }
}
