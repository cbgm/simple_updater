package de.chris.usbupdater.model;

import java.util.ArrayList;
import java.util.List;

import de.chris.usbupdater.util.SettingsUtil;

public class Settings {
	private List<UpdateInfo> updateInfos;
	private Integer intervalTime;
	private boolean autoStart;
	private boolean autoCheck;
	
	private static Settings settings;
	
	public static Settings getInstance(final boolean isOutdated) {
		
		if (settings == null || isOutdated) {
			settings =  new Settings();
		}
		return settings;
	}
	
	private Settings() {

		this.updateInfos =  new ArrayList<UpdateInfo>();
		this.intervalTime = 1000;
		this.autoStart = true;	
		this.autoCheck = false;
				
	}
	
	public void setSettings() {
		
		try {
			Settings tempSettings  = SettingsUtil.loadSetttings();
			settings = tempSettings;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getStackTrace().toString());
		}		 
	}
	
	public void addUpdateInfo(final UpdateInfo updateInfo) {
		this.updateInfos.add(updateInfo);
	}
	
	public void removeUpdateInfo(final UpdateInfo updateInfo) {
		this.updateInfos.remove(updateInfo);
	}
	
	public List<UpdateInfo> getUpdateInfos() {
		return updateInfos;
	}

	public void setUpdateInfos(List<UpdateInfo> updateInfos) {
		this.updateInfos = updateInfos;
	}

	public Integer getIntervalTime() {
		return intervalTime;
	}

	public void setIntervalTime(Integer intervalTime) {
		this.intervalTime = intervalTime;
	}

	public boolean isAutoStart() {
		return autoStart;
	}

	public void setAutoStart(boolean autoStart) {
		this.autoStart = autoStart;
	}
	
	public boolean isAutoCheck() {
		return autoCheck;
	}

	public void setAutoCheck(boolean autoCheck) {
		this.autoCheck = autoCheck;
	}
	
	public boolean configExists(final String name) {
		
		return updateInfos.stream().anyMatch(info -> info.getConfigName().equals(name));
	}
	
	public void sortUpdateInfos() {
		this.updateInfos.sort((a, b ) -> a.getConfigName().compareTo(b.getConfigName()));
	}

}
