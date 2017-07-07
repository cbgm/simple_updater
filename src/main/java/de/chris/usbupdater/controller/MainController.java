package de.chris.usbupdater.controller;

import java.awt.event.ItemEvent;


import de.chris.usbupdater.gui.MainGUI;
import de.chris.usbupdater.gui.SettingsGUI;
import de.chris.usbupdater.listener.SettingsItemListener;
import de.chris.usbupdater.model.Settings;
import de.chris.usbupdater.update.UpdateMonitor;
import de.chris.usbupdater.util.SettingsUtil;

public class MainController {
	
	private MainGUI mainGUI;
	private Settings settings;
	private UpdateMonitor updateMonitor;
	
	public MainController(final MainGUI mainGUI, final Settings settings) {
		this.settings = settings;
		this.mainGUI = mainGUI;
		this.updateMonitor = UpdateMonitor.getInstance();
		this.initView();
	}
	
	public void initView() {
		this.mainGUI.getCheckEnabledItem().setState(this.settings.isAutoCheck());
	}
	
	public void initController() {
		this.mainGUI.getExitItem().addActionListener(e -> close());
		this.mainGUI.getSettingsItem().addActionListener(e -> showSettings());
		this.mainGUI.getCheckEnabledItem().addItemListener(new SettingsItemListener(this::changeAutoCheck));
		this.mainGUI.Show();
		
		manageNotification(this.settings.isAutoCheck());
	}
	
	private void showSettings() {
		SettingsGUI settingsGUI = SettingsGUI.getInstance();
		SettingsController settingsController = new SettingsController(settingsGUI, this.settings);
		settingsController.initController();
	}

	private void close() {
		SettingsUtil.saveSettings(this.settings);
		this.mainGUI.finalize();
		System.exit(0);
	}
	
	private void changeAutoCheck(final ItemEvent e) {

		if (e.getStateChange() == ItemEvent.SELECTED) {
			this.settings.setAutoCheck(true);
			manageNotification(true);
		} else {
			this.settings.setAutoCheck(false);
			manageNotification(false);
		}
		
	}
	
	private void manageNotification(final boolean enableUpdating) {
		
		if (!enableUpdating) {
			this.updateMonitor.shouldStop(true);
		} else {
			this.updateMonitor.shouldStop(false);
			new Thread(updateMonitor).start();
		}
	}

}
