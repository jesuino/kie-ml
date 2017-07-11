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
		Result result = MLProviderFactory.getProvider(providerId).run(kc, model, input);
		// we should find some way to filter the prediction map because some datasets may have hundreds of labels
//		HashMap<String, Number> filteredPredictions = new HashMap<>();
//		result.getPredictions().entrySet().stream().filter(e -> e.getValue().doubleValue() != 0.0).forEach(e -> 
//			filteredPredictions.put(e.getKey(), e.getValue())
//		);
		return result;
	}

}
