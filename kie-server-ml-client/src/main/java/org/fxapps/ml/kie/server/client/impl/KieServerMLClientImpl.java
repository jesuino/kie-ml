package org.fxapps.ml.kie.server.client.impl;

import java.util.HashMap;

import org.fxapps.ml.api.KieMLConstants;
import org.fxapps.ml.api.model.Input;
import org.fxapps.ml.api.model.Model;
import org.fxapps.ml.api.model.ModelList;
import org.fxapps.ml.api.model.Prediction;
import org.fxapps.ml.kie.server.client.KieServerMLClient;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.impl.AbstractKieServicesClientImpl;

public class KieServerMLClientImpl extends AbstractKieServicesClientImpl implements KieServerMLClient {

	private String baseUrl;
	
	public KieServerMLClientImpl(KieServicesConfiguration config, ClassLoader cl) {
		super(config, cl);
		baseUrl = config.getServerUrl();
	}

	@Override
	public ServiceResponse<Prediction> predict(String containerId, String modelId, Input input) {
		String uri = String.join("/", 
				baseUrl,
				KieMLConstants.URI_BASE.replaceFirst("\\{" + KieMLConstants.PARAM_CONTAINER_ID + "\\}", containerId),
				KieMLConstants.URI_PREDICTION.replaceAll("\\{" + KieMLConstants.PARAM_MODEL_ID + "\\}", modelId)
		);
		String body = marshaller.marshall(input);
		return makeHttpPostRequestAndCreateServiceResponse(uri, body, Prediction.class, new HashMap<>());
	}

	@Override
	public ServiceResponse<ModelList> getModelList(String containerId) {
		String uri = String.join("/", 
				baseUrl,
				KieMLConstants.URI_BASE.replaceFirst("\\{" + KieMLConstants.PARAM_CONTAINER_ID + "\\}", containerId),
				KieMLConstants.URI_GET_MODELS
		);
		return makeHttpGetRequestAndCreateServiceResponse(uri, ModelList.class);
	}

	@Override
	public ServiceResponse<Model> getModel(String containerId, String modelId) {
		String uri = String.join("/", 
				baseUrl,
				KieMLConstants.URI_BASE.replaceFirst("\\{" + KieMLConstants.PARAM_CONTAINER_ID + "\\}", containerId),
				KieMLConstants.URI_GET_MODEL.replaceAll("\\{" + KieMLConstants.PARAM_MODEL_ID + "\\}", modelId)
		);
		return makeHttpGetRequestAndCreateServiceResponse(uri, Model.class);
	}

}
