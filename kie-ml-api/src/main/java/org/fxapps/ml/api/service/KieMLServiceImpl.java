package org.fxapps.ml.api.service;

import java.util.Optional;

import org.fxapps.ml.api.model.Input;
import org.fxapps.ml.api.model.Model;
import org.fxapps.ml.api.model.Result;
import org.fxapps.ml.api.provider.MLProviderFactory;
import org.fxapps.ml.api.runtime.KieMLContainer;

/**
 * 
 * Default Service implementation
 * 
 * @author wsiqueir
 *
 */
class KieMLServiceImpl implements KieMLService {

	private KieMLContainer kc;

	public KieMLServiceImpl(KieMLContainer kc) {
		this.kc = kc;
	} 

	@Override
	public Result predict(String modelId, Input input) {
		Optional<Model> modelSearch = kc.modelsList().getModels().stream().filter(m -> m.getId().equals(modelId)).findFirst();
		Model model = modelSearch.orElseThrow(() -> new IllegalArgumentException("Model " + modelId + " not found."));
		String providerId = model.getProvider();
		return MLProviderFactory.getProvider(providerId).run(kc, model, input);
	}

}
