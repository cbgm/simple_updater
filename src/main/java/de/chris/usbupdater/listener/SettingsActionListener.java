package de.chris.usbupdater.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsActionListener implements ActionListener {
	
	private Runnable func;
	
	public SettingsActionListener(Runnable func) {
		this.func = func;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		func.run();
	}
	
}
