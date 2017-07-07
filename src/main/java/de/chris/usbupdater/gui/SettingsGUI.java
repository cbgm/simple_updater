package de.chris.usbupdater.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import de.chris.usbupdater.model.UpdateInfo;

public class SettingsGUI {
	
	private JFrame settingsWindow;
	private JPanel configurationPanel;
	private JPanel detailPanel;
	private JList<UpdateInfo> configurationsList;
	private JScrollPane configrationsScollPane;
	private JScrollPane detailScrollPane;
	private JTable detailsTable;
	private JLabel configNameLabel;
	private JLabel searchIntervalLabel;
	//private JLabel autostartLabel;
	private JTextField searchIntervalText;
	//private JCheckBox autostartBox;
	private JButton searchButton;
	private JTextField configNameText;
	private JButton addButton;
	private JButton removeButton;
	private JLabel localFolderLabel;
	private JLabel externalFolderLabel;
	private JFileChooser fileChooser;
	private JButton externalFileButton;
	private JButton localFileButton;
	private JTextField localFolderText;
	private JTextField externalFolderText;
	private ConfigurationPopup configurationPopup;
	private DetailPopup detailPopup;
	private DefaultListModel<UpdateInfo> listModel;
	private DefaultTableModel tableModel;
	
	private static SettingsGUI settingsGUI;
	
	
	private SettingsGUI() {
		init();
	}
	
	public static SettingsGUI getInstance() {
		
		if (settingsGUI == null) {
			settingsGUI = new SettingsGUI();
		}
		return settingsGUI;
	}
	
	private void init() {
		this.settingsWindow = new JFrame("Settings");
		this.settingsWindow.setLayout(new BoxLayout(settingsWindow.getContentPane(), BoxLayout.Y_AXIS));		

		this.configurationPanel = new JPanel();
		//add panel
		this.configurationPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.configNameText = new JTextField(18);
		this.configNameText.setEditable(true);
		this.configNameText.setSize(40, 17);
		this.configNameLabel = new JLabel("Configurations:");
		this.listModel = new DefaultListModel<>();
		this.configurationsList = new JList<>(listModel);
		this.configurationsList.setFixedCellWidth(200);
		this.configurationsList.setSize(40, 80);
		this.configrationsScollPane = new JScrollPane(this.configurationsList);
		this.configrationsScollPane.setMaximumSize(new Dimension(40, 80));
		this.configrationsScollPane.setSize(40,  80);
		this.configrationsScollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.searchIntervalLabel = new JLabel("Search interval:      ");
//		this.autostartLabel = new JLabel("Autostart enabled:");
		this.searchIntervalText = new JTextField(5);
//		this.autostartBox = new JCheckBox();
		
		this.configurationPanel.add(this.configNameLabel);
		this.configurationPanel.add(this.configrationsScollPane);
		this.configurationPanel.add(this.configNameText);
		this.configurationPanel.add(this.searchIntervalLabel);
		this.configurationPanel.add(this.searchIntervalText);
//		this.configurationPanel.add(this.autostartLabel);
//		this.configurationPanel.add(this.autostartBox);
	

		//config panel
		this.detailPanel = new JPanel();
		this.detailPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		String[] columnNames = {/*"C.Nr.",*/ "Local path", "External path"};
		this.tableModel = new DefaultTableModel(columnNames, 0);
		this.detailsTable = new JTable(tableModel);
		this.detailsTable.setDefaultEditor(Object.class, null);
		this.detailsTable.setSelectionModel(new DefaultListSelectionModel());
		this.detailsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//		this.configurationTable.getColumn("C.Nr.").setMaxWidth(40);
		this.detailsTable.setEnabled(true);
		this.detailsTable.setPreferredSize(new Dimension(480, 147));
		this.detailScrollPane = new JScrollPane(detailsTable);
		this.detailScrollPane.setPreferredSize(new Dimension(480, 147));
		this.detailScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		this.localFolderLabel = new JLabel("Local folder:     ");
		this.localFolderText = new JTextField(20);
		this.localFileButton = new JButton("Choose folder");
		this.externalFolderLabel = new JLabel("External folder:");
		this.externalFolderText = new JTextField(20);
		this.externalFileButton = new JButton("Choose folder");
		this.addButton = new JButton("Add - saving detail");
		
		this.detailPanel.add(new JLabel("Configuration saving details:"));
		this.detailPanel.add(this.detailScrollPane);
		this.detailPanel.add(this.localFolderLabel);
		this.detailPanel.add(this.localFolderText);
		this.detailPanel.add(this.localFileButton);
		this.detailPanel.add(this.externalFolderLabel);
		this.detailPanel.add(this.externalFolderText);
		this.detailPanel.add(this.externalFileButton);
		this.detailPanel.add(this.addButton);
		
		
		this.settingsWindow.setDefaultLookAndFeelDecorated(true);
		
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, configurationPanel, detailPanel);
		splitPane.setPreferredSize(new Dimension(700, 280));
		splitPane.setDividerLocation(215);
		splitPane.setDividerSize(1);
	    this.settingsWindow.getContentPane().add(splitPane);
		this.settingsWindow.pack();
		this.settingsWindow.setLocationRelativeTo(null);
		this.settingsWindow.setResizable(false);
		
		this.configurationPopup = new ConfigurationPopup();
		this.detailPopup = new DetailPopup();
		this.fileChooser = new JFileChooser();
		this.fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	}
	
	
	public ConfigurationPopup getConfigurationPopup() {
		return configurationPopup;
	}

	public void setConfigurationPopup(ConfigurationPopup configurationPopup) {
		this.configurationPopup = configurationPopup;
	}
	
	public DetailPopup getDetailPopup() {
		return detailPopup;
	}

	public void setDetailPopup(DetailPopup detailPopup) {
		this.detailPopup = detailPopup;
	}

	public JPanel getAddPanel() {
		return configurationPanel;
	}

	public void setAddPanel(JPanel addPanel) {
		this.configurationPanel = addPanel;
	}

	public JPanel getConfigPanel() {
		return detailPanel;
	}

	public void setConfigPanel(JPanel configPanel) {
		this.detailPanel = configPanel;
	}

	public JList<UpdateInfo> getConfigurationsList() {
		return configurationsList;
	}

	public void setConfigurationsList(JList<UpdateInfo> configrationsList) {
		this.configurationsList = configrationsList;
	}

	public JScrollPane getConfigurationsScollPane() {
		return configrationsScollPane;
	}

	public void setConfigurationsScollPane(JScrollPane configrationsScollPane) {
		this.configrationsScollPane = configrationsScollPane;
	}

	public JTable getDetailsTable() {
		return detailsTable;
	}

	public void setDetailsTable(JTable detailsTable) {
		this.detailsTable = detailsTable;
	}
	
	public JLabel getConfigNameLabel() {
		return configNameLabel;
	}

	public void setConfigNameLabel(JLabel configNameLabel) {
		this.configNameLabel = configNameLabel;
	}
	
	public void show() {
		this.settingsWindow.setVisible(true);
	}
	

	public JFrame getSettingsWindow() {
		return settingsWindow;
	}

	public void setSettingsWindow(JFrame settingsWindow) {
		this.settingsWindow = settingsWindow;
	}

	public JTextField getSearchIntervalText() {
		return searchIntervalText;
	}

	public void setSearchIntervalText(JTextField searchIntervalText) {
		this.searchIntervalText = searchIntervalText;
	}

//	public JCheckBox getAutostartBox() {
//		return autostartBox;
//	}
//
//	public void setAutostartBox(JCheckBox autostartBox) {
//		this.autostartBox = autostartBox;
//	}
	
	public JButton getSearchButton() {
		return searchButton;
	}

	public void setSearchButton(JButton searchButton) {
		this.searchButton = searchButton;
	}

	public JTextField getConfigNameText() {
		return configNameText;
	}

	public void setConfigNameText(JTextField configNameText) {
		this.configNameText = configNameText;
	}

	public JButton getAddButton() {
		return addButton;
	}

	public void setAddButton(JButton addButton) {
		this.addButton = addButton;
	}

	public JButton getRemoveButton() {
		return removeButton;
	}

	public void setRemoveButton(JButton removeButton) {
		this.removeButton = removeButton;
	}

	public DefaultListModel<UpdateInfo> getListModel() {
		return listModel;
	}

	public void setlistModel(DefaultListModel<UpdateInfo> listModel) {
		this.listModel = listModel;
	}

	public JLabel getExternalFolderLabel() {
		return externalFolderLabel;
	}

	public void setExternalFolderLabel(JLabel externalFolderLabel) {
		this.externalFolderLabel = externalFolderLabel;
	}

	public JFileChooser getFileChooser() {
		return fileChooser;
	}

	public void setFileChooser(JFileChooser fileChooser) {
		this.fileChooser = fileChooser;
	}

	public JButton getExternalFileButton() {
		return externalFileButton;
	}

	public void setExternalFileButton(JButton externalFileButton) {
		this.externalFileButton = externalFileButton;
	}

	public JButton getLocalFileButton() {
		return localFileButton;
	}

	public void setLocalFileButton(JButton localFileButton) {
		this.localFileButton = localFileButton;
	}

	public JTextField getLocalFolderText() {
		return localFolderText;
	}

	public void setLocalFolderText(JTextField localFolderText) {
		this.localFolderText = localFolderText;
	}

	public JTextField getExternalFolderText() {
		return externalFolderText;
	}

	public void setExternalFolderText(JTextField externalFolderText) {
		this.externalFolderText = externalFolderText;
	}
	

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	public void setTableModel(DefaultTableModel tableModel) {
		this.tableModel = tableModel;
	}

	public JPanel getConfigurationPanel() {
		return configurationPanel;
	}

	public void setConfigurationPanel(JPanel configurationPanel) {
		this.configurationPanel = configurationPanel;
	}

	public JPanel getDetailPanel() {
		return detailPanel;
	}

	public void setDetailPanel(JPanel detailPanel) {
		this.detailPanel = detailPanel;
	}
	
	public class DetailSelectionModel extends DefaultListSelectionModel {

	    public DetailSelectionModel () {
	        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    }

	    @Override
	    public void clearSelection() {
	    }

	    @Override
	    public void removeSelectionInterval(int index0, int index1) {
	    }

	}


}
