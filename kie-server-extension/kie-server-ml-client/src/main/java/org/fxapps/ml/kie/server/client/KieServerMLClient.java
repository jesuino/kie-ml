package org.fxapps.ml.kie.server.client;

import org.fxapps.ml.api.model.Input;
import org.fxapps.ml.api.model.Model;
import org.fxapps.ml.api.model.ModelList;
import org.fxapps.ml.api.model.Result;
import org.kie.server.api.model.ServiceResponse;

public interface KieServerMLClient {
	
	public ServiceResponse<Result> predict(String containerId, String modelId, Input input);
	
	public ServiceResponse<ModelList> getModelList(String containerId);
	
	public ServiceResponse<Model> getModel(String containerId, String modelId);


}
