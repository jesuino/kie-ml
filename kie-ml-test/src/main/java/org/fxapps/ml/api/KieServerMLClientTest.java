package org.fxapps.ml.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.drools.compiler.kproject.ReleaseIdImpl;
import org.fxapps.ml.api.model.Input;
import org.fxapps.ml.kie.server.client.KieServerMLClient;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.KieContainerResource;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.api.model.ReleaseId;


public class KieServerMLClientTest {

	private static final String CONTAINER_ID = "test";
	private static final String GAV = "org.fxapps.ml:kie-server-ml-test-models:0.0.1-SNAPSHOT";

	public static void main(String[] args) throws IOException {
		KieServicesConfiguration configuration = KieServicesFactory
				.newRestConfiguration(" http://localhost:8080/rest/server", "kieserver", "kieserver1!");
		configuration.setMarshallingFormat(MarshallingFormat.XSTREAM);
		HashSet<Class<?>> classes = new HashSet<>();
		classes.addAll(Arrays.asList(KieMLConstants.ADDITIONAL_MARSHALLER_CLASSES));
		configuration.addExtraClasses(classes);
		KieServicesClient client = KieServicesFactory.newKieServicesClient(configuration);
		
		client.listContainers().getResult().getContainers().stream().filter(c -> c.getContainerId().equals(CONTAINER_ID)).findFirst().orElse(createContainer(client));
		
		
		KieServerMLClient mlClient = client.getServicesClient(KieServerMLClient.class);
		System.out.println(mlClient.getModelList(CONTAINER_ID).getResult());
		System.out.println(mlClient.getModel(CONTAINER_ID, "mnist").getResult());
		Input input = new Input("file:/home/wsiqueir/MNIST/mnist_png/testing/2/174.png");
		System.out.println(mlClient.predict(CONTAINER_ID, "mnist", input).getResult().getPredictions());
		
		InputStream is = KieServerMLClientTest.class.getResource("/data/iris2d_test_data.arff").openStream();
		String arffContent = null;
		try (BufferedReader buffer = new BufferedReader(new InputStreamReader(is))) {
			arffContent =  buffer.lines().collect(Collectors.joining("\n"));
        }
		System.out.println("Sending data:" + arffContent);
		System.out.println(mlClient.getModel(CONTAINER_ID, "iris2d").getResult());
		Input input2 = new Input(null, arffContent, null);
		System.out.println(mlClient.predict(CONTAINER_ID, "iris2d", input2).getResult().getPredictions());

	}

	private static KieContainerResource createContainer(KieServicesClient client) {
		System.out.println("== CREATING CONTAINER ==");
		ReleaseId releaseId = new ReleaseId(new ReleaseIdImpl(GAV));
		KieContainerResource kcr = new KieContainerResource(CONTAINER_ID, releaseId);
		client.createContainer(CONTAINER_ID, kcr);
		return 	client.createContainer(CONTAINER_ID, kcr).getResult();
	}

}
