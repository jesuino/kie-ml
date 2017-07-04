package org.fxapps.ml.services;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.fxapps.ml.api.model.Input;
import org.fxapps.ml.api.model.Model;
import org.fxapps.ml.api.model.ModelList;
import org.fxapps.ml.api.model.Result;
import org.fxapps.ml.api.runtime.KieMLContainer;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.api.model.ServiceResponse.ResponseType;
import org.kie.server.services.api.KieServerRegistry;

public class KieMLServicesBase {

	private Map<String, KieMLContainer> containers = new ConcurrentHashMap<>();

	public KieServerRegistry context;
	
	public KieMLServicesBase(KieServerRegistry context) {
		super();
		this.context = context;
	}

	public ServiceResponse<Result> predict(String containerId, String modelId, Input input) {
		checkContainer(containerId);
		KieMLContainer kieMLContainer = containers.get(containerId);
		Result predict = kieMLContainer.getService().predict(modelId, input);
		return new ServiceResponse<Result>(ResponseType.SUCCESS, "Success Running prediction", predict);
	}

	public ServiceResponse<ModelList> getModels(String containerId) {
		checkContainer(containerId);
		return new ServiceResponse<ModelList>(ResponseType.SUCCESS, "Model List",
				containers.get(containerId).modelsList());
	}

	public ServiceResponse<Model> getModel(String containerId, String modelId) {
		checkContainer(containerId);
		return containers.get(containerId).modelsList().getModels().stream().filter(m -> m.getId().equals(modelId))
				.map(m -> new ServiceResponse<Model>(ResponseType.SUCCESS, "Found model", m)).findFirst()
				.orElse(new ServiceResponse<Model>(ResponseType.FAILURE, "Model Not found: " + modelId));
	}

	public void addContainer(String containerId, KieMLContainer kieMLContainer) {
		if(containers.containsKey(containerId)) {
			throw new IllegalArgumentException("Container already registered with this ID");		
		}
		containers.put(containerId, kieMLContainer);
	}

	public void removeContainer(String containerId) {
			containers.remove(containerId);
		}

	private void checkContainer(String containerId) {
		if (!containers.containsKey(containerId)) {
			throw new IllegalArgumentException("Container not found or does not contain KieMLContainers");
		}
	}

	public KieServerRegistry getContext() {
		return context;
	}
	
}
