package org.fxapps.ml.services.resources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.fxapps.ml.services.KieMLExtension;
import org.fxapps.ml.services.KieMLServicesBase;
import org.kie.server.services.api.KieServerApplicationComponentsService;
import org.kie.server.services.api.SupportedTransports;

public class KieMLApplicationComponentsService implements KieServerApplicationComponentsService {

	@Override
	public Collection<Object> getAppComponents(String extension, SupportedTransports type, Object... services) {
		// skip calls from other than owning extension
		if (!KieMLExtension.EXTENSION_NAME.equals(extension)) {
			return Collections.emptyList();
		}

		KieMLServicesBase kieMLServicesBase = null;

		for (Object object : services) {
			if (KieMLServicesBase.class.isAssignableFrom(object.getClass())) {
				kieMLServicesBase = (KieMLServicesBase) object;
				continue;
			} 
		}

		List<Object> components = new ArrayList<>(1);
		if (SupportedTransports.REST.equals(type)) {
			components.add(new KieMLResource(kieMLServicesBase));
		}

		return components;
	}

}
