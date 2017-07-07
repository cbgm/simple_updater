package de.chris.usbupdater.model;

import java.util.ArrayList;
import java.util.List;

public class UpdateInfo {
	private List<PathDetail> pathsDetails;
	private String key;
	private String configName;

	private int cNr;
	
	public UpdateInfo(final String configName) {
		this.configName = configName;
		this.pathsDetails = new ArrayList<>();
		this.key = "";
	}
	
	public UpdateInfo(final String configName, final String key) {
		this.configName = configName;
		this.pathsDetails = new ArrayList<>();
		this.key = key;
	}
	
	public UpdateInfo() {
		this.pathsDetails = new ArrayList<>();
	}

	public String getKey() {
		return key;
	}

	public void setKey(final String key) {
		this.key = key;
	}

	public int getcNr() {
		return cNr;
	}

	public void setcNr(int cNr) {
		this.cNr = cNr;
	}


	public List<PathDetail> getPathsDetails() {
		return pathsDetails;
	}


	public void setPathsDetails(List<PathDetail> pathsDetails) {
		this.pathsDetails = pathsDetails;
	}	
	
	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}
	
	@Override
	public String toString() {
		return configName;
	}

}
