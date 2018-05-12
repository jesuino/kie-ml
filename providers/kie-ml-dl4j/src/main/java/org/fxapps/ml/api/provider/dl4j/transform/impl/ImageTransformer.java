package org.fxapps.ml.api.provider.dl4j.transform.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import org.datavec.image.loader.NativeImageLoader;
import org.fxapps.ml.api.model.Input;
import org.fxapps.ml.api.model.ModelParam;
import org.fxapps.ml.api.provider.dl4j.transform.Transformer;
import org.fxapps.ml.api.provider.dl4j.util.ParamsUtil;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;

public class ImageTransformer implements Transformer {

	@Override
	public String getName() {
		return "ImageTransformer";
	}

	@Override
	public INDArray transform(List<ModelParam> params, Input input) {
		int height = ParamsUtil.getRequiredIntParam(params, "height");
		int width = ParamsUtil.getRequiredIntParam(params, "width");
		int channels = ParamsUtil.getRequiredIntParam(params, "channels");
		Optional<String> dataNormalizationStr = ParamsUtil.getStringParam(params, "DataNormalization");
		NativeImageLoader loader = new NativeImageLoader(height, width, channels, true);
		InputStream is;
		try {
			is = new URL(input.getUrl()).openStream();
			INDArray image;
			// must deal with it on a better way... 
			if(channels == 1){ 
				image = loader.asRowVector(is);
			} else {
				image = loader.asMatrix(is);
			}
			if(dataNormalizationStr.isPresent()) {
				String clazz = dataNormalizationStr.get();
				DataNormalization dataNormalization = (DataNormalization) Class.forName(clazz).newInstance();
				dataNormalization.transform(image);
			}
			return image;
		} catch (IOException e) {
			throw new IllegalArgumentException("Error opening input URL", e);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new IllegalArgumentException("Error creating data normalization instance", e);
		}
	}

}
