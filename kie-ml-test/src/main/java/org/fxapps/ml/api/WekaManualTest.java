package org.fxapps.ml.api;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.fxapps.ml.api.model.Input;
import org.fxapps.ml.api.model.Prediction;
import org.fxapps.ml.api.runtime.KieMLContainer;

public class WekaManualTest {

	private static final String GAV = "org.fxapps.ml:kie-ml-test-models:0.0.1-SNAPSHOT";

	public static void main(String[] args) throws Exception {
		InputStream is = WekaManualTest.class.getResource("/data/iris2d_test_data.arff").openStream();
		String arffContent = null;
		try (BufferedReader buffer = new BufferedReader(new InputStreamReader(is))) {
			arffContent =  buffer.lines().collect(Collectors.joining("\n"));
        }
		System.out.println("Sending data:" + arffContent);
		Input input = new Input(null, arffContent, null);
		Prediction prediction = KieMLContainer.newContainer(GAV).getService().predict("iris2d", input);
		System.out.println("PRedictions:" + prediction.getPredictions());
	}
	
}
