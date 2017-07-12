package org.fxapps.ml.api.model;

import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * The results of a prediction
 * 
 * @author wsiqueir
 *
 */
@XmlRootElement(name = "model-execution-result")
public class Result {
	
	private Map<String, Number> predictions;
	private String text;
	
	public Map<String, Number> getPredictions() {
		return predictions;
	}
	public void setPredictions(Map<String, Number> predictions) {
		this.predictions = predictions;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return "Result [predictions=" + predictions + ", text="
				+ text + "]";
	}
	
}
