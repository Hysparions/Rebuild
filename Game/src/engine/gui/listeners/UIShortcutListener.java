package engine.gui.listeners;

public abstract class UIShortcutListener {

	public int[] shortcut;
	
	public UIShortcutListener(int... keys) {
		if(keys.length != 0) {
			shortcut = keys;
		}
	}
	
	public boolean match(int... keys) {
		if(keys != null && shortcut != null) {
			if(keys.length == shortcut.length && keys.length != 0) {
				for(int i=0; i< shortcut.length; i++) {
					if(keys[i] != shortcut[i]) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	
	public abstract void onHit();
}
