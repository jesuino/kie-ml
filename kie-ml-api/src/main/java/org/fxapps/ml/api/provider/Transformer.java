package org.fxapps.ml.api.provider;

import java.util.List;

import org.fxapps.ml.api.model.Input;
import org.fxapps.ml.api.model.ModelParam;

public interface Transformer<T> {
	
	public String getName();
	
	public T transform(List<ModelParam> params, Input input);

}
