package org.fxapps.ml.api.provider.impl;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.fxapps.ml.api.model.Input;
import org.fxapps.ml.api.model.Model;
import org.fxapps.ml.api.model.ModelParam;
import org.fxapps.ml.api.model.Result;
import org.fxapps.ml.api.provider.MLProvider;
import org.fxapps.ml.api.provider.Transformer;
import org.fxapps.ml.api.provider.dl4j.transform.impl.TransformerFactory;
import org.fxapps.ml.api.provider.dl4j.util.ParamsUtil;
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

	public Result run(KieMLContainer kc, Model model, Input input) {
		Result prediction = null;
		List<ModelParam> params = model.getParams();
		if(params == null) {
			throw new IllegalArgumentException("Parameters to configure the input parsing are required!!");
		}
		String transformerName = ParamsUtil.getRequiredStringParam(params, "transformerName");
		Transformer<INDArray> transformer = TransformerFactory.get(transformerName);
		INDArray image = transformer.transform(params, input);
		InputStream isModel = kc.getModelBinInputStream(model);
		INDArray output = getOutput(isModel, image);
		prediction = new Result();
		prediction.setText(output.toString());
		prediction.setPredictions(new HashMap<>());
		for (int i = 0; i < output.columns(); i++) {
			if(output.getDouble(i) == 0d) { 
				continue;
			}
			prediction.getPredictions().put(model.getLabels().get(i), output.getDouble(i));
		}
		return prediction;
	}

	private INDArray getOutput(InputStream isModel, INDArray image) {
		org.deeplearning4j.nn.api.Model dl4jModel;
		try {
			// won't use the model guesser at the moment because it is trying to load a keras model?
//			dl4jModel = ModelGuesser.loadModelGuess(isModel);
			dl4jModel = loadModel(isModel);
		} catch (Exception e) {
			throw new IllegalArgumentException("Not able to load model.", e);
		}
		if(dl4jModel instanceof MultiLayerNetwork) {
			MultiLayerNetwork multiLayerNetwork = (MultiLayerNetwork) dl4jModel;
			multiLayerNetwork.init();
			return multiLayerNetwork.output(image);
		} else {
			ComputationGraph graph = (ComputationGraph) dl4jModel;
			graph.init();
			return graph.output(image)[0];
		}
	}
	
	private org.deeplearning4j.nn.api.Model loadModel(InputStream is) throws Exception {
		org.deeplearning4j.nn.api.Model model;
        File tmpFile = File.createTempFile("restore", "model");
        tmpFile.deleteOnExit();
        FileUtils.copyInputStreamToFile(is, tmpFile);
		try {
		    model = ModelSerializer.restoreMultiLayerNetwork(tmpFile, true);
	    } catch (Exception e) {
	        model =  ModelSerializer.restoreComputationGraph(tmpFile, true);
		} finally {
			tmpFile.delete();
		}
		return model;
	}

}
