package de.chris.usbupdater.listener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SettingsListSelectionListener implements ListSelectionListener {
	
	private Runnable func;
	
	public SettingsListSelectionListener(Runnable func) {
		this.func = func;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		func.run();
		
	}
	
}
