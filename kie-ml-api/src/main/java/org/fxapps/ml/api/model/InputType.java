package org.fxapps.ml.api.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum
public enum InputType {

	@XmlEnumValue("binary") BINARY, @XmlEnumValue("text") TEXT;

}