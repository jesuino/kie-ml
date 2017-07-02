package org.fxapps.ml.api.provider.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.fxapps.ml.api.model.Input;
import org.fxapps.ml.api.model.Model;
import org.fxapps.ml.api.model.Prediction;
import org.fxapps.ml.api.provider.MLProvider;
import org.fxapps.ml.api.provider.transform.Transformer;
import org.fxapps.ml.api.provider.transform.impl.TransformerFactory;
import org.fxapps.ml.api.runtime.KieMLContainer;
import org.nd4j.linalg.api.ndarray.INDArray;

/**
 * 
 * The DeepLearning4J provider implementation that can be used to make predictions based on DL4J provider
 * 
 * @author wsiqueir
 *
 */
public class DL4JKieMLProvider implements MLProvider {

	public String getId() {
		return "deeplearning4j";
	}

	public Prediction predict(KieMLContainer kc, Model model, Input input) {
		Prediction prediction = null;
		try {
			Transformer transformer = TransformerFactory.get(model.getTransformDescriptor().getName());
			ClassLoader cl = kc.getKieContainer().getClassLoader();
			InputStream isModel = cl.getResourceAsStream(model.getModelBinPath());
			INDArray image = transformer.transform(model.getTransformDescriptor().getParams(), input);
			MultiLayerNetwork dl4jModel = ModelSerializer.restoreMultiLayerNetwork(isModel);
			INDArray output = dl4jModel.output(image);
			prediction = new Prediction();
			prediction.setPredictionsResult(output.toString());
			prediction.setPredictions(new HashMap<>());
			for (int i = 0; i < output.columns(); i++) {
				prediction.getPredictions().put(model.getLabels().get(i), output.getDouble(i));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prediction;
	}

}
