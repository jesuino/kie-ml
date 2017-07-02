package org.fxapps.ml.api.provider.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;

import org.fxapps.ml.api.model.Input;
import org.fxapps.ml.api.model.Model;
import org.fxapps.ml.api.model.Prediction;
import org.fxapps.ml.api.provider.MLProvider;
import org.fxapps.ml.api.runtime.KieMLContainer;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

/**
 * 
 * The Weka provider implementation that can be used to make predictions based
 * on Weka classifiers
 * 
 * @author wsiqueir
 *
 */
public class WekaKieMLProvider implements MLProvider {
	// TODO: THis is a very preliminary weka support as a proof of concept -
	// must be improved
	public String getId() {
		return "weka";
	}

	public Prediction predict(KieMLContainer kc, Model model, Input input) {
		Prediction prediction = new Prediction();
		prediction.setPredictions(new HashMap<>());
		try {
			ClassLoader cl = kc.getKieContainer().getClassLoader();
			InputStream is = cl.getResourceAsStream(model.getModelBinPath());
			// let's only support classifiers at the moment
			Classifier cls = (Classifier) weka.core.SerializationHelper.read(is);
			BufferedReader reader = null;
			if (input.getUrl() != null) {
				URL url = new URL(input.getUrl());
				reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
			} else if (input.getText() != null) {
				reader = new BufferedReader(new StringReader(input.getText()));
			} else {
				throw new IllegalArgumentException("You should provide an URL to ARFF file or the ARFF String content");
			}
			Instances data = new Instances(reader);
			int classIndex = getClassIndex(model, data.size());
			data.setClassIndex(classIndex);
			for (Instance instance : data) {
				instance.setClassValue(0);
				double[] distributionForInstance = cls.distributionForInstance(instance);
				if (distributionForInstance.length > 0) {
					double classification = distributionForInstance[0];
					instance.setClassValue(classification);
					prediction.getPredictions().put(instance.toString(), classification);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error running prediction", e);
		}
		return prediction;
	}

	private int getClassIndex(Model model, int size) {
		int defaultValue = size - 1;
		if (model.getTransformDescriptor() != null && model.getTransformDescriptor().getParams() != null) {
			return model.getTransformDescriptor().getParams().stream().filter(p -> p.getName().equals("classIndex"))
					.map(p -> Integer.parseInt(p.getValue())).findFirst().orElseGet(() -> defaultValue);
		} else {
			return defaultValue;
		}

	}

}
