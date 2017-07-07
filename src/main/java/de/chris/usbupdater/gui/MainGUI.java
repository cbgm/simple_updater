package de.chris.usbupdater.gui;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;

public class MainGUI {
	
	private SystemTray tray;
	private Toolkit toolkit;
	private Image image;
	private TrayIcon trayIcon;
	private PopupMenu popupMenu;
	private MenuItem settingsItem;
	private MenuItem exitItem;
	private CheckboxMenuItem checkEnabledItem;
		
	public MainGUI () {
		this.init();
	}
	
	private void init() {

	    if (!SystemTray.isSupported()) {
            System.exit(0);
        }
	    
	    this.tray = SystemTray.getSystemTray();
	    this.toolkit = Toolkit.getDefaultToolkit();
	    this.image = toolkit.getImage(getClass().getResource("/images/refresh-arrows.png"));
	    
        this.popupMenu = new PopupMenu();
         
        // Create a pop-up menu components
        this.settingsItem = new MenuItem("Settings");
        this.checkEnabledItem = new CheckboxMenuItem("Enable check");
        this.exitItem = new MenuItem("Exit");
        //Add components to pop-up menu
        this.popupMenu.add(settingsItem);
        this.popupMenu.addSeparator();
        this.popupMenu.add(checkEnabledItem);    
        this.popupMenu.addSeparator(); 
        this.popupMenu.add(exitItem);
       
        this.trayIcon = new TrayIcon(this.image, "Updater", this.popupMenu);
	    this.trayIcon.setImageAutoSize(true);
       
	}
	
	public void Show() {
 
		try {
			this.tray.add(trayIcon);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			System.exit(0);
		}
	}

	public MenuItem getSettingsItem() {
		return settingsItem;
	}

	public void setSettingsItem(MenuItem settingsItem) {
		this.settingsItem = settingsItem;
	}

	public MenuItem getExitItem() {
		return exitItem;
	}

	public void setExitItem(MenuItem exitItem) {
		this.exitItem = exitItem;
	}

	public CheckboxMenuItem getCheckEnabledItem() {
		return checkEnabledItem;
	}

	public void setCheckEnabledItem(CheckboxMenuItem checkEnabledItem) {
		this.checkEnabledItem = checkEnabledItem;
	}

	public SystemTray getTray() {
		return tray;
	}

	public void finalize() {
		this.tray.remove(trayIcon);
	}

}
