package org.fxapps.ml.api.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TransformDescriptor {
	
	private String name;
	
	@XmlElement(name="param")
	private List<TransformParam> params;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<TransformParam> getParams() {
		return params;
	}
	public void setParams(List<TransformParam> params) {
		this.params = params;
	}

}
