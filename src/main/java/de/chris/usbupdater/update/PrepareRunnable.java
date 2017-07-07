package de.chris.usbupdater.update;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.chris.usbupdater.util.DriveUtil;

public class PrepareRunnable implements Runnable {
	
	private String key;
	private String drive;
	
	public PrepareRunnable(final String key, final String drive) {
		this.key = key;
		this.drive = drive;
	}

	@Override
	public void run() {
		DriveUtil.perpareDrive(this.drive, this.key);
		//copy file to drive with key and last updated
		//add class because it will be used also when updating is done to update file last updated
		
	}

}
