package org.fxapps.ml.api.provider.dl4j.transform.impl;

import java.util.HashSet;
import java.util.Set;

import org.fxapps.ml.api.provider.dl4j.transform.Transformer;

public class TransformerFactory {

	private static Set<Transformer> transformers;

	static {
		transformers = new HashSet<>();
		transformers.add(new ImageTransformer());
	}

	private TransformerFactory() {
	}

	public static Transformer get(String name) {
		return transformers.stream().filter(t -> t.getName().equals(name)).findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Transformer not found: " + name));
	}
}
