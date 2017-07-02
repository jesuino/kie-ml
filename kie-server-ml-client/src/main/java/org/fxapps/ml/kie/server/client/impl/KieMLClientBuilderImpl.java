package org.fxapps.ml.kie.server.client.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.fxapps.ml.api.KieMLConstants;
import org.fxapps.ml.kie.server.client.KieServerMLClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.helper.KieServicesClientBuilder;

public class KieMLClientBuilderImpl implements KieServicesClientBuilder {

	@Override
	public String getImplementedCapability() {
		return "KieMLCapability";
	}

	@Override
	public Map<Class<?>, Object> build(KieServicesConfiguration configuration, ClassLoader classLoader) {
		Map<Class<?>, Object> services = new HashMap<Class<?>, Object>();
		Set<Class<?>> extraClasses = new HashSet<>();
		extraClasses.addAll(Arrays.asList(KieMLConstants.ADDITIONAL_MARSHALLER_CLASSES));
		configuration.addExtraClasses(extraClasses);
        services.put(KieServerMLClient.class, new KieServerMLClientImpl(configuration, classLoader));
        return services;
	}

}
