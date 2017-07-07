package de.chris.usbupdater.controller;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import java.io.File;
import java.util.AbstractMap;
import java.util.UUID;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import de.chris.usbupdater.gui.SettingsGUI;
import de.chris.usbupdater.listener.SettingsActionListener;
import de.chris.usbupdater.listener.SettingsFocusListener;
import de.chris.usbupdater.listener.SettingsItemListener;
import de.chris.usbupdater.listener.SettingsKeyListener;
import de.chris.usbupdater.listener.SettingsListSelectionListener;
import de.chris.usbupdater.listener.SettingsMouseListener;
import de.chris.usbupdater.listener.SettingsWindowListener;
import de.chris.usbupdater.model.PathDetail;
import de.chris.usbupdater.model.Settings;
import de.chris.usbupdater.model.UpdateInfo;
import de.chris.usbupdater.update.PrepareRunnable;
import de.chris.usbupdater.update.UpdateMonitor;
import de.chris.usbupdater.util.DriveUtil;
import de.chris.usbupdater.util.SettingsUtil;

public class SettingsController {

	
	private int selectedConfigurationIndex = -1;
	private int selectedDetailIndex = -1;
	private PathDetail selectedDetailItem = null;
	private UpdateInfo selectedConfigurationItem = null;
	
	private SettingsGUI settingsGUI  = null;
	private Settings settings  = null;
	
	private SettingsActionListener localFileButtonListener = null;
	private SettingsActionListener externalFileButtonListener = null;
	private SettingsActionListener addConfigPopupMenuListener = null;
	private SettingsActionListener prepareConfigPopupMenuListener = null;
	private SettingsActionListener addConfigButtonListener = null;
	private SettingsActionListener deleteConfigPopupMenuListener = null;
	private SettingsActionListener updateConfigPopupMenuListener = null;
	private SettingsActionListener deleteDetailPopupMenuListener = null;
	private SettingsListSelectionListener configurationSelectListener = null;
	private SettingsWindowListener windowListener = null;
	private SettingsFocusListener configTextFocusListener = null;
	private SettingsFocusListener searchIntervalTextFocusListener = null;
	private SettingsKeyListener searchIntervalTextKeyListener = null;
	private SettingsKeyListener configTextKeyListener = null;
	private SettingsMouseListener configListMouseListener = null;
//	private SettingsItemListener autoStartListener = null;
	private SettingsListSelectionListener detailSelectionListener = null;
	private SettingsMouseListener detailListMouseListener = null;
	
	public SettingsController(final SettingsGUI settingsGUI, final Settings settings) {
		this.settingsGUI = settingsGUI;
		this.settings = settings;
		this.initView();
	}

	public void initView() {
		this.settingsGUI.getListModel().clear();
		this.updateConfigrations();
		
		if (this.settingsGUI.getConfigurationsList().isSelectionEmpty()) {
			this.disableDetailPanel(true);
		} else {
			this.configurationSelected();
			this.fillDetailTable();
		}
	    this.settingsGUI.getSearchIntervalText().setText(this.settings.getIntervalTime().toString());
//	    this.settingsGUI.getAutostartBox().setSelected(this.settings.isAutoStart());
	}	

	private void fillDetailTable() {
		this.settingsGUI.getTableModel().setRowCount(0);
		
		for (PathDetail pathDetail : this.selectedConfigurationItem.getPathsDetails()) {
			this.settingsGUI.getTableModel().addRow(new Object[] { pathDetail.getLocalPath(), pathDetail.getExternalPath() });
		}
	}

	private void disableDetailPanel(final boolean disable) {
		
		for (Component cp : this.settingsGUI.getDetailPanel().getComponents() ){
	        cp.setEnabled(!disable);
		}
	}
	
	private void prepareDrive() {
	
		String[] driveLetters = DriveUtil.detectUSBDrives();
		
		if (driveLetters != null) {
		
		    String driveLetter = (String) JOptionPane.showInputDialog(null, "Choose now...",
		        "The Choice of a Lifetime", JOptionPane.QUESTION_MESSAGE, null,
		        driveLetters,
		        driveLetters[0]);
		    Thread t = new Thread(new PrepareRunnable(this.selectedConfigurationItem.getKey(), driveLetter));
		    t.start();
		}
	}

	private void addPathDetail() {
				
		String tempLocal = this.settingsGUI.getLocalFolderText().getText();
		String tempExternal = this.settingsGUI.getExternalFolderText().getText();
		
		String regexLocal = "^(([a-zA-Z]:\\\\)+(\\w*-*\\\\?)*(\\w*.\\w*)?)+$";
		String regexExternal = "^((\\w+-*\\\\?)+(\\w*.\\w*)?)+$";
		boolean error = false;
		
//		if (Pattern.matches(regexLocal, tempLocal)){//tempLocal.matches(regexLocal)) {
//			this.settingsGUI.getLocalFolderText().setText("Please input a valid path");
//			error = true;
//		}
//		
//		if (!tempExternal.matches(regexExternal)) {
//			this.settingsGUI.getExternalFolderText().setText("Please input a valid path");
//			error = true;
//		}
//		
//		if (error) return;
	
		this.selectedConfigurationItem.getPathsDetails().add(new PathDetail(tempLocal, tempExternal));
		this.settingsGUI.getTableModel().addRow(new Object[] { tempLocal, tempExternal });

		
		this.settingsGUI.getListModel().setElementAt(this.selectedConfigurationItem, this.selectedConfigurationIndex);
		this.settings.getUpdateInfos().set(this.selectedConfigurationIndex, this.selectedConfigurationItem);

	}

	private void chooseLocalFileFolder() {
		int returnValue = this.settingsGUI.getFileChooser().showOpenDialog(null);
		
		if (returnValue == JFileChooser.APPROVE_OPTION) {
	        File selectedFile = this.settingsGUI.getFileChooser().getSelectedFile();
	        this.settingsGUI.getLocalFolderText().setText(selectedFile.getPath());
	    }
	}
	
	private void chooseExternalFileFolder() {
		int returnValue = this.settingsGUI.getFileChooser().showOpenDialog(null);
		
		if (returnValue == JFileChooser.APPROVE_OPTION) {
	        File selectedFile = this.settingsGUI.getFileChooser().getSelectedFile();	        
	        this.settingsGUI.getExternalFolderText().setText(selectedFile.getPath().replaceAll("[a-zA-Z]:\\\\", ""));
	    }
	}

	private void autostartChanged(final ItemEvent e) {
//		settings.setAutoStart(settingsGUI.getAutostartBox().isSelected());			
	}

	private void addConfiguration() {
		resetDetailSelection();
		this.settings.addUpdateInfo(new UpdateInfo("New", UUID.randomUUID().toString()));
		this.settingsGUI.getListModel().addElement(new UpdateInfo("New", UUID.randomUUID().toString()));
		int index = settingsGUI.getConfigurationsList().getModel().getSize() - 1;
		this.settingsGUI.getConfigurationsList().setSelectedIndex(index);
		this.settingsGUI.getConfigNameText().setText(settingsGUI.getListModel().getElementAt(index).getConfigName());
		this.settingsGUI.getConfigNameText().requestFocus();
	}

	private void deleteConfiguration() {
		this.resetDetailSelection();
		this.settingsGUI.getConfigNameText().setText("");
		UpdateInfo temp = this.settingsGUI.getConfigurationsList().getSelectedValue();
		
		if (temp == null) return;
		this.settingsGUI.getListModel().removeElement(temp);
		this.settingsGUI.getConfigurationsList().revalidate();
		this.settings.getUpdateInfos().remove(temp);
		this.disableDetailPanel(true);
	}

	private void configurationSelected() {
		this.resetDetailSelection();
		this.disableDetailPanel(false);
		this.selectedConfigurationItem = this.settingsGUI.getConfigurationsList().getSelectedValue();
		this.selectedConfigurationIndex = this.settingsGUI.getConfigurationsList().getSelectedIndex();
		
		if (this.selectedConfigurationItem == null) return;
		this.settingsGUI.getConfigNameText().setText(this.selectedConfigurationItem.getConfigName());
		this.fillDetailTable();
	}

	public void initController() {
		this.setupListeners();
		this.settingsGUI.show();
	}
	
	private void updateConfigrations() {
//		try {
//		//this.settingsGUI.getListModel().clear();
//		} catch (Exception e) {
//			System.out.println(e.toString());
//		}
		this.settings.sortUpdateInfos();
		
		for (UpdateInfo u : this.settings.getUpdateInfos())
			this.settingsGUI.getListModel().addElement(u);
	}
	
	private void resetDetailSelection() {
		this.settingsGUI.getLocalFolderText().setText("");
		this.settingsGUI.getExternalFolderText().setText("");
		this.settingsGUI.getDetailsTable().clearSelection();
		this.selectedDetailIndex = -1;
		this.selectedDetailItem = null;
	}
	
	private void closeWindow() {
		this.removeListeners();
		SettingsUtil.saveSettings(this.settings);	
	}
	
	private void manageConfigTextFocusGained() {
		
		if (settingsGUI.getConfigurationsList().getSelectedIndex() == -1) return;
		this.selectedConfigurationIndex = settingsGUI.getConfigurationsList().getSelectedIndex();
		this.selectedConfigurationItem = this.settingsGUI.getConfigurationsList().getSelectedValue();
	}
	
	private void manageSearchIntervalTextFocusGained() {
		settings.setIntervalTime(Integer.parseInt(this.settingsGUI.getSearchIntervalText().getText()));
	}
	
	private void manageSearchIntervalKeyReleased(final KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.settings.setIntervalTime(Integer.parseInt(this.settingsGUI.getSearchIntervalText().getText()));			
		}
	}
	
	private void manageConfigNameKeyReleased(final KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			String tempKey = settingsGUI.getConfigNameText().getText();
			UpdateInfo tempItem = settingsGUI.getConfigurationsList().getSelectedValue();
			tempItem.setConfigName(tempKey);
			this.settingsGUI.getListModel().setElementAt(tempItem, selectedConfigurationIndex);
			this.settings.getUpdateInfos().get(selectedConfigurationIndex).setConfigName(tempKey);
		} else {
			this.selectedConfigurationItem.setConfigName(this.settingsGUI.getConfigNameText().getText());
			this.settingsGUI.getListModel().setElementAt(this.selectedConfigurationItem, this.selectedConfigurationIndex);
		}
	}
	
	private void manageConfigurationListRightClick(final MouseEvent e) {
		
		if ( SwingUtilities.isRightMouseButton(e) ) {
			
			if (settingsGUI.getConfigurationsList().getSelectedValue() != null) {
				settingsGUI.getConfigurationPopup().hideDeleteItem(false);
				settingsGUI.getConfigurationPopup().hideUpdateItem(false);
				settingsGUI.getConfigurationPopup().hidePrepareItem(false);
		       
			} else {
				settingsGUI.getConfigurationPopup().hideDeleteItem(true);
				settingsGUI.getConfigurationPopup().hidePrepareItem(true);
				settingsGUI.getConfigurationPopup().hideUpdateItem(true);
			}
			settingsGUI.getConfigurationPopup().show(settingsGUI.getConfigurationsList(), e.getX(), e.getY());
        }
	}
	
	private void manageDetailListRightClick(final MouseEvent e) {
		if ( SwingUtilities.isRightMouseButton(e) ) {
			
			if (this.settingsGUI.getDetailsTable().getSelectedRow() != -1) {
				settingsGUI.getDetailPopup().show(settingsGUI.getDetailsTable(), e.getX(), e.getY());
			}
        }
	}
	
	private void detailSelected() {
		if (this.settingsGUI.getDetailsTable().getSelectedRow() == -1) return;
		this.selectedDetailIndex = this.settingsGUI.getDetailsTable().getSelectedRow();
		this.selectedDetailItem = this.settings.getUpdateInfos().get(selectedConfigurationIndex).getPathsDetails().get(selectedDetailIndex);
	}
	
	private void deleteDetail() {
		if (this.selectedDetailItem == null) return;
		this.settingsGUI.getTableModel().removeRow(this.selectedDetailIndex);
		this.settingsGUI.getDetailsTable().revalidate();
		this.settings.getUpdateInfos().get(selectedConfigurationIndex).getPathsDetails().remove(this.selectedDetailIndex);
		this.resetDetailSelection();
	}
	
	private void manualUpdateConfiguration() {
		UpdateInfo temp = this.settings.getUpdateInfos().get(selectedConfigurationIndex);
		UpdateMonitor.getInstance().addDrivetoExecutor(temp.getKey()); 
	}
	
	private void manualUpdateDetail() {
		
	}
	
	private void setupListeners() {
		this.localFileButtonListener = new SettingsActionListener(this::chooseLocalFileFolder);
		this.externalFileButtonListener = new SettingsActionListener(this::chooseExternalFileFolder);
		this.addConfigPopupMenuListener = new SettingsActionListener(this::addConfiguration);
		this.updateConfigPopupMenuListener = new SettingsActionListener(this::manualUpdateConfiguration);
		this.prepareConfigPopupMenuListener = new SettingsActionListener(this::prepareDrive);
		this.addConfigButtonListener = new SettingsActionListener(this::addPathDetail);
		this.deleteConfigPopupMenuListener = new SettingsActionListener(this::deleteConfiguration);
		this.configurationSelectListener = new SettingsListSelectionListener(this::configurationSelected);
		this.windowListener = new SettingsWindowListener(this::closeWindow);
		this.configTextFocusListener = new SettingsFocusListener(this::manageConfigTextFocusGained);
		this.searchIntervalTextFocusListener = new SettingsFocusListener(this::manageSearchIntervalTextFocusGained);
		this.searchIntervalTextKeyListener = new SettingsKeyListener(this::manageSearchIntervalKeyReleased);
		this.configTextKeyListener = new SettingsKeyListener(this::manageConfigNameKeyReleased);
		this.configListMouseListener = new SettingsMouseListener(this::manageConfigurationListRightClick);
//		this.autoStartListener = new SettingsItemListener(this::autostartChanged);
		this.detailSelectionListener = new SettingsListSelectionListener(this::detailSelected);
		this.detailListMouseListener = new SettingsMouseListener(this::manageDetailListRightClick);
		this.deleteDetailPopupMenuListener = new SettingsActionListener(this::deleteDetail);
				
		this.settingsGUI.getConfigurationPopup().getAddMenuItem().addActionListener(this.addConfigPopupMenuListener); 	
		this.settingsGUI.getConfigurationsList().addListSelectionListener(this.configurationSelectListener);      
        this.settingsGUI.getConfigurationPopup().getDeleteMenuItem().addActionListener(this.deleteConfigPopupMenuListener);
        this.settingsGUI.getConfigurationPopup().getUpdateMenuItem().addActionListener(this.updateConfigPopupMenuListener);
	    this.settingsGUI.getConfigurationsList().addMouseListener(this.configListMouseListener);    
	    this.settingsGUI.getConfigNameText().addKeyListener(this.configTextKeyListener);	    
//	    this.settingsGUI.getAutostartBox().addItemListener(this.autoStartListener);
	    this.settingsGUI.getSearchIntervalText().addKeyListener(this.searchIntervalTextKeyListener);
	    this.settingsGUI.getSearchIntervalText().addFocusListener(this.searchIntervalTextFocusListener);
	    this.settingsGUI.getConfigNameText().addFocusListener(this.configTextFocusListener);
		this.settingsGUI.getLocalFileButton().addActionListener(this.localFileButtonListener);
		this.settingsGUI.getExternalFileButton().addActionListener(this.externalFileButtonListener);
		this.settingsGUI.getAddButton().addActionListener(this.addConfigButtonListener);
		this.settingsGUI.getSettingsWindow().addWindowListener(this.windowListener);
		this.settingsGUI.getDetailsTable().getSelectionModel().addListSelectionListener(this.detailSelectionListener);
		this.settingsGUI.getConfigurationPopup().getPrepareMenuItem().addActionListener(this.prepareConfigPopupMenuListener);
	    this.settingsGUI.getDetailsTable().addMouseListener(this.detailListMouseListener); 
        this.settingsGUI.getDetailPopup().getDeleteMenuItem().addActionListener(this.deleteDetailPopupMenuListener);

	}
	

	private void removeListeners() {
		this.settingsGUI.getConfigurationPopup().getAddMenuItem().removeActionListener(this.addConfigPopupMenuListener);
		this.settingsGUI.getConfigurationsList().removeListSelectionListener(this.configurationSelectListener);
		this.settingsGUI.getConfigurationPopup().getDeleteMenuItem().removeActionListener(this.deleteConfigPopupMenuListener);
		this.settingsGUI.getConfigurationPopup().getPrepareMenuItem().removeActionListener(this.prepareConfigPopupMenuListener);
		this.settingsGUI.getConfigurationPopup().getUpdateMenuItem().removeActionListener(this.updateConfigPopupMenuListener);
		this.settingsGUI.getConfigurationsList().removeMouseListener(this.configListMouseListener);
		this.settingsGUI.getConfigNameText().removeKeyListener(this.configTextKeyListener);
//		this.settingsGUI.getAutostartBox().removeItemListener(this.autoStartListener);
		this.settingsGUI.getSearchIntervalText().removeKeyListener(this.searchIntervalTextKeyListener);
		this.settingsGUI.getSearchIntervalText().removeFocusListener(this.searchIntervalTextFocusListener);
		this.settingsGUI.getConfigNameText().removeFocusListener(this.configTextFocusListener);
		this.settingsGUI.getLocalFileButton().removeActionListener(this.localFileButtonListener);
		this.settingsGUI.getExternalFileButton().removeActionListener(this.externalFileButtonListener);
		this.settingsGUI.getAddButton().removeActionListener(this.addConfigButtonListener);
		this.settingsGUI.getSettingsWindow().removeWindowListener(this.windowListener);
		this.settingsGUI.getDetailsTable().getSelectionModel().removeListSelectionListener(this.detailSelectionListener);
		this.settingsGUI.getDetailsTable().removeMouseListener(this.detailListMouseListener);
		this.settingsGUI.getDetailPopup().getDeleteMenuItem().removeActionListener(this.deleteDetailPopupMenuListener);
		
	} 
}
