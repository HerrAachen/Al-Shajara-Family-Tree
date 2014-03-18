package de.aaa.al_shajara.gui.dialogs;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

public class AbstractDialog extends JDialog {

  private static final long serialVersionUID = 6773214315038487776L;

  public AbstractDialog(Window owner){
    super(owner);
  }

  public AbstractDialog(Frame parent, boolean modal) {
    super(parent, modal);
  }

  /**
   * Sets this dialog location to the center
   * of the parent component.
   */
  protected void centerInParent() {
    // Find out our parent 
    Container myParent = getParent();
    if (myParent==null)
      return;
    Point topLeft = myParent.getLocationOnScreen();
    Dimension parentSize = myParent.getSize();
    centerInside(parentSize,topLeft);
  }
  
  /**
   * Sets the dialog position to the center of the screen
   */
  protected void centerInScreen(){
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    centerInside(screenSize,new Point(0, 0));
  }
  
  /**
   * Centers this dialog inside the component defined by 
   * the specified dimension and position
   */
  private void centerInside(Dimension parentSize, Point parentPosition){
    int x;
    int y;
    
    Dimension mySize = getSize();

    if (parentSize.width > mySize.width) 
      x = ((parentSize.width - mySize.width)/2) + parentPosition.x;
    else 
      x = parentPosition.x;

    if (parentSize.height > mySize.height) 
      y = ((parentSize.height - mySize.height)/2) + parentPosition.y;
    else 
      y = parentPosition.y;

    setLocation (x, y);
  }

  /**
   * makes this dialog invisible and releases the memory of the components
   */
  public void close(){
    this.setVisible(false);
    this.dispose();
  }

  /**
   * overwriting the method to make root pane listen to 
   * escape key strokes
   */
  @Override
  protected JRootPane createRootPane() {
    KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
    JRootPane rootPane = super.createRootPane();
    rootPane.registerKeyboardAction(new CloseDialogListener(this), stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
    return rootPane;
  }
}

