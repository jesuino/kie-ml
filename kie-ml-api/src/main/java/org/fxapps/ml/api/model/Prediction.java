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
@XmlRootElement(name = "prediction")
public class Prediction {
	
	private Map<String, Number> predictions;
	private String predictionsResult;
	
	public Map<String, Number> getPredictions() {
		return predictions;
	}
	public void setPredictions(Map<String, Number> predictions) {
		this.predictions = predictions;
	}
	public String getPredictionsResult() {
		return predictionsResult;
	}
	public void setPredictionsResult(String predictionsResult) {
		this.predictionsResult = predictionsResult;
	}

}
