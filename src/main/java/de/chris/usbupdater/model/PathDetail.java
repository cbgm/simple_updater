package de.chris.usbupdater.model;

public class PathDetail {
	private String localPath;
	private String externalPath;
	
	public PathDetail(final String localPath, final String externalPath) {
		this.localPath = localPath;
		this.externalPath = externalPath;
	}
	
	public PathDetail() {
		// TODO Auto-generated constructor stub
	}

	public String getLocalPath() {
		return localPath;
	}
	public void setLocalPath(final String localPath) {
		this.localPath = localPath;
	}
	public String getExternalPath() {
		return externalPath;
	}
	public void setExternalPath(final String externalPath) {
		this.externalPath = externalPath;
	}
}
