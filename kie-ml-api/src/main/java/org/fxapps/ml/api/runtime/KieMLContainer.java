package org.fxapps.ml.api.runtime;

import org.fxapps.ml.api.model.ModelList;
import org.fxapps.ml.api.service.KieMLService;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;

public interface KieMLContainer {
	
	public KieMLService getService();
	
	public ModelList modelsList();
	
	public KieContainer getKieContainer();
	
	public static KieMLContainer newContainer(String GAV) {
		return new KieMLContainerImpl(GAV);
	}
	
	public static KieMLContainer newContainer(ReleaseId releaseId) {
		return new KieMLContainerImpl(releaseId);
	}
	
	public static KieMLContainer newContainer(KieContainer kc) {
		return new KieMLContainerImpl(kc);
	}

}
