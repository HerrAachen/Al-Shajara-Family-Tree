package de.aaa.al_shajara.gui.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CloseDialogListener implements ActionListener {
  AbstractDialog dialog;
  
  public CloseDialogListener(AbstractDialog dialog) {
      super();
      this.dialog = dialog;
  }
  @Override
  public void actionPerformed(ActionEvent e) {
      dialog.close();
  }
}