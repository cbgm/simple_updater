package de.chris.usbupdater.listener;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class SettingsFocusListener implements FocusListener {
	
	private Runnable func;
	
	public SettingsFocusListener(Runnable func) {
		this.func = func;
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
		func.run();
	}

}
