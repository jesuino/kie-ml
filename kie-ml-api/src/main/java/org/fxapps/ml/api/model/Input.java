package org.fxapps.ml.api.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * Represents the data input for prediction
 * 
 * @author wsiqueir
 *
 */
@XmlRootElement(name = "input")
public class Input {
	
	/**
	 * An URL containing the payload to be converted. This accepts Java URL format.
	 */
	private String url;
	
	/**
	 * A text used in the predict
	 */
	private String text;
	
	/**
	 * Raw binary data
	 */
	private byte[] rawData;

	public Input(){
		
	}
	
	public Input(String url) {
		this.url = url;
	}
	public Input(String url, String text, byte[] rawData) {
		this.url = url;
		this.text = text;
		this.rawData = rawData;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public byte[] getRawData() {
		return rawData;
	}

	public void setRawData(byte[] rawData) {
		this.rawData = rawData;
	} 

}
