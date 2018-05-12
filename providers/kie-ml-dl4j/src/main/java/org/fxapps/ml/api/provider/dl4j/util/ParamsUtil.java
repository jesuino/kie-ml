package org.fxapps.ml.api.provider.dl4j.util;

import java.util.List;
import java.util.Optional;

import org.fxapps.ml.api.model.ModelParam;

public class ParamsUtil {
	
	private ParamsUtil(){}

	public static int getRequiredIntParam(List<ModelParam> params, String paramName) {
		return Integer.parseInt(getRequiredStringParam(params, paramName));
	}
	
	public static String getRequiredStringParam(List<ModelParam> params, String paramName) {
		return getStringParam(params, paramName).orElseThrow(() -> new IllegalArgumentException("Param is required: " + paramName));
	}
	
	public static Optional<String> getStringParam(List<ModelParam> params, String paramName) {
		return params.stream().filter(p -> p.getName().equals(paramName)).map(ModelParam::getValue).findFirst();
	}

	
}
