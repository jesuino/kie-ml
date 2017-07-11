package org.fxapps.ml.api.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * The object representation for a model description
 * 
 * @author wsiqueir
 *
 */
@XmlRootElement(name = "model")
@XmlAccessorType(XmlAccessType.FIELD)
public class Model {

	private String id;
	private String name;
	private String provider;
	private String modelBinPath;
	private String modelLabelsPath;
	private InputType inputType;
	private List<String> labels;
	@XmlElement(name="modelParam")
	private List<ModelParam> params;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getModelBinPath() {
		return modelBinPath;
	}

	public void setModelBinPath(String modelBinPath) {
		this.modelBinPath = modelBinPath;
	}

	public String getModelLabelsPath() {
		return modelLabelsPath;
	}

	public void setModelLabelsPath(String modelLabelsPath) {
		this.modelLabelsPath = modelLabelsPath;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}
	
	public List<ModelParam> getParams() {
		return params;
	}

	public void setParams(List<ModelParam> params) {
		this.params = params;
	}
	
	public InputType getInputType() {
		return inputType;
	}

	public void setInputType(InputType inputType) {
		this.inputType = inputType;
	}

	@Override
	public String toString() {
		return "Model [id=" + id + ", name=" + name + ", provider=" + provider + ", modelBinPath=" + modelBinPath
				+ ", modelLabelsPath=" + modelLabelsPath + ", labels=" + labels + ", params=" + params + "]";
	}
	
}
