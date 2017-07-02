package org.fxapps.ml.api;

import java.util.Arrays;
import java.util.HashSet;

import org.fxapps.ml.api.model.Input;
import org.fxapps.ml.kie.server.client.KieServerMLClient;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;

public class KieServerMLClientTest {

	public static void main(String[] args) {
		KieServicesConfiguration configuration = KieServicesFactory
				.newRestConfiguration(" http://localhost:8080/kie-server/services/rest/server", "kieserver", "kieserver1!");
		configuration.setMarshallingFormat(MarshallingFormat.XSTREAM);
		HashSet<Class<?>> classes = new HashSet<>();
		classes.addAll(Arrays.asList(KieMLConstants.ADDITIONAL_MARSHALLER_CLASSES));
		configuration.addExtraClasses(classes);
		KieServicesClient kieClient = KieServicesFactory.newKieServicesClient(configuration);
		KieServerMLClient mlClient = kieClient.getServicesClient(KieServerMLClient.class);
		System.out.println(mlClient.getModel("teste", "mnist").getResult());
		Input input = new Input("file:/home/wsiqueir/MNIST/mnist_png/testing/2/174.png");
		System.out.println(mlClient.predict("teste", "mnist", input).getResult().getPredictions());

	}

}
