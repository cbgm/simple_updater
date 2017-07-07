package de.chris.usbupdater.listener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SettingsKeyListener implements KeyListener {
	
	private SettingsKeyEvent func;
	
	public SettingsKeyListener(SettingsKeyEvent func) {
		this.func = func;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		func.call(e);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
