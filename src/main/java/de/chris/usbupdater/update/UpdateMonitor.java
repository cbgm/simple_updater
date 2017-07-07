package de.chris.usbupdater.update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

import de.chris.usbupdater.gui.UpdatePanel;
import de.chris.usbupdater.model.Settings;
import de.chris.usbupdater.util.DriveUtil;

public class UpdateMonitor implements Runnable {

	private ExecutorService executorService = Executors.newFixedThreadPool(5);
	
	private boolean stop = false;
	
	private volatile List<String> alreadyUpdated = new ArrayList<>();
	
	private List<String> currentDrives;
	
	private static UpdateMonitor updateMonitor = null;
		
	public static UpdateMonitor getInstance() {
		
		if (updateMonitor == null) {
			updateMonitor = new UpdateMonitor();
		}
		return updateMonitor;
	}
	
	private UpdateMonitor() {
		this.currentDrives = DriveUtil.detectDrives();
	}

	@Override
	public void run() {

		while (!stop) {
			List<String> tempDrives = DriveUtil.detectDrives();
			
			if (this.currentDrives.size() != tempDrives.size()) {
				this.currentDrives = tempDrives;
				this.alreadyUpdated = this.alreadyUpdated.stream().filter(d -> this.currentDrives.contains(d)).collect(Collectors.toList());
			}
			HashMap<String, String> driveLetters = DriveUtil.detectPreparedUSBDrives();
			
			if (driveLetters != null) {
				List<Entry<String, String>> entries = driveLetters.entrySet().stream().filter(dl -> alreadyUpdated.contains(dl.getKey())).collect(Collectors.toList());
				
				if (!entries.isEmpty()) {
					entries.forEach(e -> driveLetters.remove(e.getKey()));
				}
				
				if(driveLetters.size() > 0) {
					addAlreadyUpdatedKey(driveLetters.entrySet().iterator().next().getKey());
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							UpdatePanel updatePanel = new UpdatePanel(driveLetters.entrySet().iterator().next());
							executorService.execute(updatePanel.initWorker());
						}
					});

				}
				try {
					Thread.sleep(Settings.getInstance(false).getIntervalTime());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		executorService.shutdown();
	}
	
	public void addDrivetoExecutor(final String key) {
		HashMap<String, String> driveLetters = DriveUtil.detectPreparedUSBDrives();
		if (!driveLetters.containsKey(key)) return;
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				Optional<Entry<String, String>> temp = driveLetters.entrySet().stream().filter(d -> d.getKey().equals(key)).findFirst();
				UpdatePanel updatePanel = new UpdatePanel(temp.get());
				executorService.execute(updatePanel.initWorker());
			}
		});
	}

	private synchronized void addAlreadyUpdatedKey(final String key) {
		this.alreadyUpdated.add(key);
	}
	
	public synchronized void removeAlreadyUpdatedKey(final String key) {
		this.alreadyUpdated.remove(key);
	}
	
	public synchronized void clearAlreadyUpdated() {
		this.alreadyUpdated.clear();;
	}


	public void shouldStop(boolean stop) {
		this.stop = stop;
	}
	
}
