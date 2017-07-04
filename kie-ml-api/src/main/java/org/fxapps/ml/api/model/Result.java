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
	private String predictionsResult;
	private String resultTxt;
	
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
	
	public String getResultTxt() {
		return resultTxt;
	}
	public void setResultTxt(String resultTxt) {
		this.resultTxt = resultTxt;
	}
	
	@Override
	public String toString() {
		return "Result [predictions=" + predictions + ", predictionsResult=" + predictionsResult + ", resultTxt="
				+ resultTxt + "]";
	}
	
}
