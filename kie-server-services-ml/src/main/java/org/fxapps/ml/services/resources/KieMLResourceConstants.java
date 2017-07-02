package org.fxapps.ml.services.resources;

import org.kie.server.api.rest.RestURI;

public class KieMLResourceConstants {
	
	// params
	public static final String MODEL_ID_PARAM = "modelId";
	
	// uris
	public static final String BASE = "server/containers/{" + RestURI.CONTAINER_ID + "}/kieml";
	public static final String GET_MODELS = "";
	public static final String GET_MODEL = "{" + MODEL_ID_PARAM + "}";
	public static final String PREDICTION = "{" + MODEL_ID_PARAM + "}";

}
