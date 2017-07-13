package org.fxapps.ml.api.provider.transform.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import org.datavec.image.loader.NativeImageLoader;
import org.fxapps.ml.api.model.Input;
import org.fxapps.ml.api.model.ModelParam;
import org.fxapps.ml.api.provider.transform.Transformer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;

public class ImageTransformer implements Transformer {

	@Override
	public String getName() {
		return "ImageTransformer";
	}

	@Override
	public INDArray transform(List<ModelParam> params, Input input) {
		int height = getIntParam(params, "height");
		int width = getIntParam(params, "width");
		int channels = getIntParam(params, "channels");
		String dataNormalizationStr = getStringParam(params, "DataNormalization").get();
		NativeImageLoader loader = new NativeImageLoader(height, width, channels, true);
		InputStream is;
		try {
			is = new URL(input.getUrl()).openStream();
			INDArray image = loader.asRowVector(is);
			if(dataNormalizationStr != null) {
				DataNormalization dataNormalization = (DataNormalization) Class.forName(dataNormalizationStr).newInstance();
				dataNormalization.transform(image);
			}
			return image;
		} catch (IOException e) {
			throw new IllegalArgumentException("Error opening input URL", e);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new IllegalArgumentException("Error creating data normalization instance", e);
		}
	}

	private int getIntParam(List<ModelParam> params, String paramName) {
		return getStringParam(params, paramName).map(Integer::parseInt).orElseThrow(() -> new IllegalArgumentException("Param is required: " + paramName));
	}
	
	private Optional<String> getStringParam(List<ModelParam> params, String paramName) {
		return params.stream().filter(p -> p.getName().equals(paramName)).map(ModelParam::getValue).findFirst();
	}

}
