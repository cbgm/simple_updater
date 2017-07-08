package de.chris.usbupdater.update;

import java.io.File;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import de.chris.usbupdater.listener.CopyListener;
import de.chris.usbupdater.model.PathDetail;
import de.chris.usbupdater.model.Settings;
import de.chris.usbupdater.model.UpdateInfo;
import de.chris.usbupdater.util.CopyUtil;

public class ProgressWorker extends SwingWorker<Void, String> implements CopyListener {

	private JLabel currentInformationLabel;
	private Entry<String, String> drive;
	private CopyUtil copyUtil;
	private JDialog dialog;
	private int filesCount = 0;
	private int currentFileIndex = 1;
	private int definitionCount = 0;
	private int currentDefinition = 1;
	
	private Mode currentMode = Mode.DELETE;
	
	private enum Mode {
		DELETE,
		UPDATE
	};
	
	public ProgressWorker(final Entry<String, String> drive, final JLabel currentInformationLabel, final JDialog dialog) {
		this.drive = drive;
		this.currentInformationLabel = currentInformationLabel;
		this.dialog = dialog;
		this.copyUtil = new CopyUtil();
		this.copyUtil.setCopyListener(this);
	}
	
	@Override
	protected Void doInBackground() throws Exception {

		try {
			
			UpdateInfo updateInfo = Settings.getInstance(false).getUpdateInfos().stream().filter(u -> u.getKey().equals(this.drive.getKey())).findFirst().orElse(null);
			
			if (updateInfo != null) {
				this.definitionCount = updateInfo.getPathsDetails().size();
				
				for(PathDetail p : updateInfo.getPathsDetails()) {
					//remove no more existing files
					currentMode = Mode.DELETE;
					List <String> temp = this.copyUtil.CompareOldAndNew(new File(p.getLocalPath()), new File(this.drive.getValue() + p.getExternalPath()));
					this.filesCount = temp.size();
					publish(this.filesCount + " files");
					this.copyUtil.removeNonExisting(temp);
					reset();
					//update files
					try {
						currentMode = Mode.UPDATE;
						this.filesCount = this.copyUtil.getFilesCount(new File(p.getLocalPath()));
						publish(this.filesCount + " files");
						this.copyUtil.copyFolder(new File(p.getLocalPath()), new File(this.drive.getValue() + p.getExternalPath()));
					} catch (Exception e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(this.dialog,
							    e.getMessage(),
							    "Error when transfering definition " + currentDefinition,
							    JOptionPane.ERROR_MESSAGE);
					}
					++this.currentDefinition;
					reset();
				}			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	private void reset() {
		this.filesCount = 0;
		this.currentFileIndex = 1;
	}

	@Override
	protected void process(List< String> chunks) {
		String temp;
		if (currentMode == Mode.UPDATE)
			temp = "<html>Definition " + this.currentDefinition  + " of " + this.definitionCount +"<br>Current File: " + chunks.get(chunks.size()-1) +"</html>";
		else
			temp = "<html>Removing deleted files<br>Current File: " + chunks.get(chunks.size()-1) +"</html>";
		this.currentInformationLabel.setText(temp);
    }
	
	@Override
    protected void done() {
		this.currentInformationLabel.setText("Done");
		this.dialog.dispose();
    }

	@Override
	public void updateFileInfo(String pathInfo) {
		String[] ar =pathInfo.split("\\\\");
		publish(ar[ar.length-1]);
		setProgress((int)(100*this.currentFileIndex)/this.filesCount);
		++this.currentFileIndex;
	}
}
