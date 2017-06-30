package org.fxapps.ml.api.provider;

import org.fxapps.ml.api.model.Input;
import org.fxapps.ml.api.model.Model;
import org.fxapps.ml.api.model.Prediction;
import org.fxapps.ml.api.runtime.KieMLContainer;

/**
 * 
 * The interface for a ML provider implementation. The provider is responsible
 * to transform an input into predictions.
 * 
 * @author wsiqueir
 *
 */
public interface MLProvider {

	/**
	 * This provider ID will be used in models descriptor
	 * 
	 * @return
	 */
	public String getId();

	/**
	 * 
	 * This method is called to make an actual prediction
	 * 
	 * @param kc
	 * @param model
	 * @param input
	 * @return
	 */
	public Prediction predict(KieMLContainer kc, Model model, Input input);

}
