package org.fxapps.ml.api;

import org.fxapps.ml.api.model.Input;
import org.fxapps.ml.api.runtime.KieMLContainer;
import org.fxapps.ml.api.service.KieMLService;

public class OpenNLPAPIExample {

	static String SENTENCE = "Peter and John went to the library at 8:00 PM, July 10th.";

	private static final String GAV = "org.fxapps.ml:kie-ml-test-models:0.0.1-SNAPSHOT";

	public static void main(String[] args) {
		Input input = new Input(null, SENTENCE, null);
		KieMLContainer container = KieMLContainer.newContainer(GAV);
		KieMLService service = container.getService();
		System.out.println(service.predict("datePos", input).getPredictions());
		System.out.println(service.predict("timePos", input).getPredictions());
		System.out.println(service.predict("namePos", input).getPredictions());
		System.out.println(service.predict("tagger", input).getPredictions());
		System.out.println(service.predict("parser", input));
	}

}
