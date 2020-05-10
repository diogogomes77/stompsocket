package dev.dgomes.backend.fileupload;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("fileupload")
public class StorageProperties {

	/**
	 * Folder location for storing files
	 */
	private String location = "/media/dgomes/Cenas/uploads_test";

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
