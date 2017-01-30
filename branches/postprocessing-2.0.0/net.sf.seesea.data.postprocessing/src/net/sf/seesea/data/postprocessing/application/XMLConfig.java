package net.sf.seesea.data.postprocessing.application;

import java.util.Dictionary;
import java.util.Hashtable;

public class XMLConfig {

	private String persistentIdentifier;
	
	private Dictionary<String, Object> properties;

	private String multitonId;
	
	public XMLConfig() {
		properties = new Hashtable<String, Object>();
	}

	public String getConfigID() {
		return persistentIdentifier;
	}

	public void setConfigID(String configID) {
		this.persistentIdentifier = configID;
	}

	public Dictionary<String, Object> getProperties() {
		return properties;
	}

	public String getMultitonId() {
		return multitonId;
	}

	public void setMultitonID(String multitonId) {
		this.multitonId = multitonId;
	}


}
