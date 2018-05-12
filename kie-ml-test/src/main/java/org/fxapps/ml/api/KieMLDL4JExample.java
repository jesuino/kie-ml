package org.fxapps.ml.api;

import org.fxapps.ml.api.model.Input;
import org.fxapps.ml.api.runtime.KieMLContainer;
import org.fxapps.ml.api.service.KieMLService;

public class KieMLDL4JExample {
	private static final String FILE_TO_PREDICT = "file:/home/wsiqueir/MNIST/mnist_png/testing/2/174.png";
	private static final String GAV = "org.fxapps.ml:kie-ml-test-models:0.0.1-SNAPSHOT";

	public static void main(String[] args) throws Exception {
		Input input;
		KieMLService service = KieMLContainer.newContainer(GAV).getService();

		input = new Input("https://r.hswstatic.com/w_907/gif/tesla-cat.jpg");
		System.out.println(service.predict("cifar10", input).getPredictions());
		
//		input = new Input(FILE_TO_PREDICT);
//		System.out.println(service.predict("mnist", input).getPredictions());
	}
}
