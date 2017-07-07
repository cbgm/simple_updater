package de.chris.usbupdater.gui;

import java.awt.Component;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class ConfigurationPopup {
	
	private JPopupMenu popupMenu;
	private JMenuItem addMenuItem;
	private JMenuItem deleteMenuItem;
	private JMenuItem prepareMenuItem;
	private JMenuItem updateMenuItem;

	
	public ConfigurationPopup () {
		initPopup();
	}
	
	private void initPopup() {
		// TODO Auto-generated method stub
		this.popupMenu = new JPopupMenu();
	    this.addMenuItem = new JMenuItem("New");
	    this.deleteMenuItem = new JMenuItem("Delete");
	    this.updateMenuItem = new JMenuItem("Manual update");
	    this.prepareMenuItem = new JMenuItem("Prepare Drive");
	    this.popupMenu.add(addMenuItem);
	    this.popupMenu.add(deleteMenuItem);
	    this.popupMenu.add(prepareMenuItem);
	    this.popupMenu.add(updateMenuItem);
	}

	public void show(Component invoker, final int x, final int y) {
		this.popupMenu.show(invoker, x, y);
	}
	
	public JPopupMenu getPopupMenu() {
		return popupMenu;
	}

	public void setPopupMenu(JPopupMenu popupMenu) {
		this.popupMenu = popupMenu;
	}

	public JMenuItem getAddMenuItem() {
		return addMenuItem;
	}

	public void setAddMenuItem(JMenuItem addMenuItem) {
		this.addMenuItem = addMenuItem;
	}

	public JMenuItem getDeleteMenuItem() {
		return deleteMenuItem;
	}

	public void setDeleteMenuItem(JMenuItem deleteMenuItem) {
		this.deleteMenuItem = deleteMenuItem;
	}

	public JMenuItem getPrepareMenuItem() {
		return prepareMenuItem;
	}

	public void setPrepareMenuItem(JMenuItem prepareMenuItem) {
		this.prepareMenuItem = prepareMenuItem;
	}
	
	public JMenuItem getUpdateMenuItem() {
		return updateMenuItem;
	}

	public void setUpdateMenuItem(JMenuItem updateMenuItem) {
		this.updateMenuItem = updateMenuItem;
	}
	
	public void hideDeleteItem(final boolean hide) {
		this.deleteMenuItem.setVisible(!hide);
	}
	
	public void hideAddItem(final boolean hide) {
		this.addMenuItem.setVisible(!hide);
	}
	
	public void hidePrepareItem(final boolean hide) {
		this.prepareMenuItem.setVisible(!hide);
	}
	
	public void hideUpdateItem(final boolean hide) {
		this.updateMenuItem.setVisible(!hide);
	}
    
}
