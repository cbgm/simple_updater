package de.chris.usbupdater.gui;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map.Entry;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import de.chris.usbupdater.update.ProgressWorker;

public class UpdatePanel extends JPanel {

	private JProgressBar pb;
	private ProgressWorker progressWorker;
	private JDialog dialog;
	private JLabel currentInformationLabel;
	private String lablStr ="";
	private Entry<String, String> drive;



	public UpdatePanel(final Entry<String, String> drive) {
		this.drive = drive;
		dialog = new JDialog();
		dialog.setSize(300, 100);
		//dialog.setUndecorated(true);
		dialog.setTitle("Update drive " + drive.getValue());
		//dialog.setLayout(new GridBagLayout());
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(2, 2, 2, 2);
		gbc.weightx = 1;
		gbc.gridy = 0;
		currentInformationLabel = new JLabel("Getting information...");
		currentInformationLabel.setFont(new Font("", Font.PLAIN, 11));
//		dialog.add(currentInformationLabel, gbc);
//		currentInformationLabel.setHorizontalAlignment(JLabel.CENTER);
//		currentInformationLabel.setVerticalAlignment(JLabel.CENTER);
		dialog.add(currentInformationLabel, BorderLayout.WEST);
		pb = new JProgressBar();
		pb.setStringPainted(true);
		pb.setSize(250, 10);
		gbc.gridy = 1;
		dialog.add(pb, BorderLayout.NORTH);
		//dialog.add(pb, gbc);
		dialog.setResizable(false);
		//dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setModalityType(ModalityType.MODELESS);
		JDialog.setDefaultLookAndFeelDecorated(true); 
		dialog.setVisible(true);

		
	}
	
	public ProgressWorker initWorker() {
		progressWorker = new ProgressWorker(this.drive, this.currentInformationLabel, this.dialog);
		progressWorker.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				// TODO Auto-generated method stub
				if ("progress".equalsIgnoreCase(evt.getPropertyName())) {
					pb.setValue((int)evt.getNewValue());
				}
			}
		});
		return this.progressWorker;
	}
}
