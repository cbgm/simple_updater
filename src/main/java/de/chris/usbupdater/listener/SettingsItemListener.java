package de.chris.usbupdater.listener;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class SettingsItemListener implements ItemListener {
	
	private SettingsItemEvent func;
	
	public SettingsItemListener(SettingsItemEvent func) {
		this.func = func;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		func.call(e);
	}
	
}
