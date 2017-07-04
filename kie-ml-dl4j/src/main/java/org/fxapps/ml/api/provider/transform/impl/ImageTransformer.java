package org.fxapps.ml.api.provider.transform.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.datavec.image.loader.NativeImageLoader;
import org.fxapps.ml.api.model.Input;
import org.fxapps.ml.api.model.ModelParam;
import org.fxapps.ml.api.provider.transform.Transformer;
import org.nd4j.linalg.api.ndarray.INDArray;

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
		NativeImageLoader loader = new NativeImageLoader(height, width, channels, true);
		// TODO: check other possible ways to transfer the image
		InputStream is;
		try {
			is = new URL(input.getUrl()).openStream();
			return loader.asRowVector(is);
		} catch (IOException e) {
			throw new IllegalArgumentException("Error opening input URL", e);
		}
	}

	private int getIntParam(List<ModelParam> params, String paramName) {
		return params.stream().filter(p -> p.getName().equals(paramName)).map(p -> Integer.parseInt(p.getValue()))
				.findFirst().orElseThrow(() -> new IllegalArgumentException("Param is required: " + paramName));
	}

}
