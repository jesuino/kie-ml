package org.fxapps.ml.api;

import javax.xml.bind.JAXBContext;

import org.fxapps.ml.api.model.Input;
import org.fxapps.ml.api.model.Prediction;
import org.fxapps.ml.api.runtime.KieMLContainer;

public class KieMLDL4JExample {
	private static final String FILE_TO_PREDICT = "file:/home/wsiqueir/MNIST/mnist_png/testing/2/174.png";
	private static final String GAV = "org.fxapps.ml:kie-server-ml-test-models:0.0.1-SNAPSHOT";

	public static void main(String[] args) throws Exception {
		Input input = new Input(FILE_TO_PREDICT);
		JAXBContext.newInstance(Input.class).createMarshaller().marshal(input, System.out);
		;
		Prediction prediction = KieMLContainer.newContainer(GAV).getService().predict("mnist", input);
		System.out.println(prediction.getPredictions());
	}
}
