package engine.gui.listeners;

import engine.gui.UIPanel;

/**
 * Use this interface to implement algorithm running
 * when a click occurs on a 
 * @author louis
 *
 */
public interface UISelectListener {
	
	/**
	 * Implements this function to give a behavior to your panel
	 * when mouse action is detected on it
	 * @param source panel
	 * @param button of the mouse (RIGHT LEFT)
	 * @param action (PRESS RELEASE)
	 */
	public void onMouseAction(UIPanel source, int button, int action) ;

}
