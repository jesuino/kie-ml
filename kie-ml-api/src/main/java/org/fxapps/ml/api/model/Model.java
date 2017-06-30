package org.fxapps.ml.api.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * The object representation for a model description
 * @author wsiqueir
 *
 */
@XmlRootElement(name = "model")
public class Model {

	private String id;
	private String name;
	private String provider;
	private String modelBinPath;
	private String modelLabelsPath;
	private List<String> labels;
	private TransformDescriptor transformDescriptor;
	
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
	
	public TransformDescriptor getTransformDescriptor() {
		return transformDescriptor;
	}

	public void setTransformDescriptor(TransformDescriptor transformDescriptor) {
		this.transformDescriptor = transformDescriptor;
	}

	@Override
	public String toString() {
		return "Model [id=" + id + ", name=" + name + ", provider=" + provider + ", modelBinPath=" + modelBinPath
				+ ", modelLabelsPath=" + modelLabelsPath + "]";
	}
	
}
