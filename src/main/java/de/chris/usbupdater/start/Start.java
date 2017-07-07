package de.chris.usbupdater.start;

import java.io.Console;

import javax.swing.SwingUtilities;

import de.chris.usbupdater.controller.MainController;
import de.chris.usbupdater.gui.MainGUI;
import de.chris.usbupdater.model.Settings;
import de.chris.usbupdater.util.SingleInstanceApplicationUtil;

public class Start {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (SingleInstanceApplicationUtil.lockInstance("temp.file")) {
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					MainGUI mainGUI = new MainGUI();
					Settings settings = Settings.getInstance(false);
					settings.setSettings();
					MainController mainController = new MainController(mainGUI, settings);
					mainController.initController();		
				}
			});
		}
	}

}
