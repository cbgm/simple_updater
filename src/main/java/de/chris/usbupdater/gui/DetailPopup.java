package de.chris.usbupdater.gui;

import java.awt.Component;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class DetailPopup {
	
	private JPopupMenu popupMenu;
	private JMenuItem deleteMenuItem;
	private JMenuItem updateMenuItem;
	
	public DetailPopup () {
		initPopup();
	}
	
	private void initPopup() {
		// TODO Auto-generated method stub
		this.popupMenu = new JPopupMenu();
	    this.deleteMenuItem = new JMenuItem("Delete");
	    this.updateMenuItem = new JMenuItem("Manual Update");
	    this.popupMenu.add(deleteMenuItem);
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

	public JMenuItem getDeleteMenuItem() {
		return deleteMenuItem;
	}

	public void setDeleteMenuItem(JMenuItem deleteMenuItem) {
		this.deleteMenuItem = deleteMenuItem;
	}  

	public JMenuItem getUpdateMenuItem() {
		return updateMenuItem;
	}

	public void setUpdateMenuItem(JMenuItem updateMenuItem) {
		this.updateMenuItem = updateMenuItem;
	}  

}
