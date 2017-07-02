package org.fxapps.ml.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import org.fxapps.ml.api.model.Input;
import org.fxapps.ml.api.model.Model;
import org.fxapps.ml.api.model.ModelList;
import org.fxapps.ml.api.model.Prediction;
import org.fxapps.ml.api.runtime.KieMLContainer;
import org.kie.api.runtime.KieContainer;
import org.kie.server.services.api.KieContainerInstance;
import org.kie.server.services.api.KieServerApplicationComponentsService;
import org.kie.server.services.api.KieServerExtension;
import org.kie.server.services.api.KieServerRegistry;
import org.kie.server.services.api.SupportedTransports;
import org.kie.server.services.impl.KieServerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KieMLExtension implements KieServerExtension {
	
    private static final Logger logger = LoggerFactory.getLogger(KieMLExtension.class);
    
    public static final String EXTENSION_NAME = "KieML";
    
    public static Class<?>[] ADDITIONAL_MARSHALLER_CLASSES = {
			Model.class,
			ModelList.class,
			Input.class,
			Prediction.class
    };
    

	private KieMLServicesBase kieMLServicesBase;
	private List<Object> services = new ArrayList<Object>();

	public boolean isActive() {
		return true;
	}

	public void init(KieServerImpl kieServer, KieServerRegistry registry) {
		logger.info("{} init...", this);
		kieMLServicesBase = new KieMLServicesBase(registry);
		this.services.add(kieMLServicesBase);
	}

	public void destroy(KieServerImpl kieServer, KieServerRegistry registry) {
		
	}

	public void createContainer(String id, KieContainerInstance kieContainerInstance, Map<String, Object> parameters) {
		KieContainer kieContainer = kieContainerInstance.getKieContainer();
		try {
			KieMLContainer kieMLContainer = KieMLContainer.newContainer(kieContainer);
			Set<Class<?>> extraClasses = new HashSet<>();
			extraClasses.addAll(Arrays.asList(ADDITIONAL_MARSHALLER_CLASSES));
			kieContainerInstance.addExtraClasses(extraClasses);
			logger.info("Container {} contains KieMLModels. Activating {}", id, this);
			kieMLServicesBase.addContainer(id, kieMLContainer);
		} catch (IllegalArgumentException e) {
			logger.info("Container {} does not include KieMLModels, {} skipped", id, this);
		}
	}

	public void updateContainer(String id, KieContainerInstance kieContainerInstance, Map<String, Object> parameters) {
		disposeContainer(id, kieContainerInstance, parameters);
		createContainer(id, kieContainerInstance, parameters);
	}

	public boolean isUpdateContainerAllowed(String id, KieContainerInstance kieContainerInstance,
			Map<String, Object> parameters) {
		return true;
	}

	public void disposeContainer(String id, KieContainerInstance kieContainerInstance, Map<String, Object> parameters) {
		kieMLServicesBase.removeContainer(id);
	}

	public List<Object> getAppComponents(SupportedTransports type) {
		 ServiceLoader<KieServerApplicationComponentsService> appComponentsServices
         = ServiceLoader.load(KieServerApplicationComponentsService.class);
     List<Object> appComponentsList = new ArrayList<Object>();
     Object [] services = {
            kieMLServicesBase
     };
     for( KieServerApplicationComponentsService appComponentsService : appComponentsServices ) {
        appComponentsList.addAll(appComponentsService.getAppComponents(EXTENSION_NAME, type, services));
     }
     return appComponentsList;
	}

	@SuppressWarnings("unchecked")
	public <T> T getAppComponents(Class<T> serviceType) {
        if (serviceType.isAssignableFrom(kieMLServicesBase.getClass())) {
            return (T) kieMLServicesBase;
        }
        return null;
	}

	public String getImplementedCapability() {
		return EXTENSION_NAME + "Capability";
	}

	public List<Object> getServices() {
		return this.services;
	}

	public String getExtensionName() {
		return EXTENSION_NAME;
	}

	public Integer getStartOrder() {
		return 42;
	}
	
    @Override
    public String toString() {
        return EXTENSION_NAME + " KIE Server extension";
    }
 
}
