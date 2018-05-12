package org.fxapps.ml.api.provider.dl4j.transform.impl;

import java.util.HashSet;
import java.util.Set;

import org.fxapps.ml.api.provider.Transformer;
import org.nd4j.linalg.api.ndarray.INDArray;

public class TransformerFactory {

	private static Set<Transformer<INDArray>> transformers;

	static {
		transformers = new HashSet<>();
		transformers.add(new ImageTransformer());
	}

	private TransformerFactory() {
	}

	public static Transformer<INDArray> get(String name) {
		return transformers.stream().filter(t -> t.getName().equals(name)).findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Transformer not found: " + name));
	}
}
