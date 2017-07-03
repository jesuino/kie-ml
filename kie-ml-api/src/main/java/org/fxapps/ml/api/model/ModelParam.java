package org.fxapps.ml.api.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "modelParam")
public class ModelParam {

	private String name;
	private String value;

	public ModelParam() {
	}

	public ModelParam(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "ModelParam [name=" + name + ", value=" + value + "]";
	}
	
}
