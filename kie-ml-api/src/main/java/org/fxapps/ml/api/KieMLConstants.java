package org.fxapps.ml.api;

import org.fxapps.ml.api.model.Input;
import org.fxapps.ml.api.model.Model;
import org.fxapps.ml.api.model.ModelList;
import org.fxapps.ml.api.model.Result;

// move it from here?
public class KieMLConstants {
	
    public static Class<?>[] ADDITIONAL_MARSHALLER_CLASSES; 
    
    static {
    	ADDITIONAL_MARSHALLER_CLASSES  = new Class<?>[]{
				Model.class,
				ModelList.class,
				Input.class,
				Result.class
	    };
    }

	
	// REST
	// params
	public static final String PARAM_MODEL_ID = "modelId";
	public static final String PARAM_CONTAINER_ID = "id";
	
	// uris
	public static final String URI_BASE = "containers/{" + PARAM_CONTAINER_ID + "}/kieml";
	public static final String URI_GET_MODELS = "";
	public static final String URI_GET_MODEL = "{" + PARAM_MODEL_ID + "}";
	public static final String URI_PREDICTION = "{" + PARAM_MODEL_ID + "}";

}