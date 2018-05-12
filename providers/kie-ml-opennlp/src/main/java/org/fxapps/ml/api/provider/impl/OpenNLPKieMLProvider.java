package org.fxapps.ml.api.provider.impl;

import java.io.InputStream;

import org.fxapps.ml.api.model.Input;
import org.fxapps.ml.api.model.Model;
import org.fxapps.ml.api.model.Result;
import org.fxapps.ml.api.provider.MLProvider;
import org.fxapps.ml.api.provider.opennlp.InputData;
import org.fxapps.ml.api.runtime.KieMLContainer;

/**
 * 
 * The OpenNLP provider implementation used to process text
 * 
 * @author wsiqueir
 *
 */
public class OpenNLPKieMLProvider implements MLProvider {

	public String getId() {
		return "opennlp";
	}

	public Result run(KieMLContainer kc, Model model, Input input) {
		Result result = null;
		try {
			String modelName = kc.getParamValue(model, "model", true);
			boolean tokenize = Boolean.parseBoolean(kc.getParamValue(model, "tokenize", false));
			String delimiter = kc.getParamValue(model, "delimiter", false);
			InputStream isModel = kc.getModelBinInputStream(model);
			InputData inputData = new InputData(isModel, input.getText(), tokenize, delimiter);
			result = ModelFactory.getModel(modelName).apply(inputData);
		} catch (Exception e) {
			throw new RuntimeException("Unexpected error", e);
		}
		return result;
	}

}
