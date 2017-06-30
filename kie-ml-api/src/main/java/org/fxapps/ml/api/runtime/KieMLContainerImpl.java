package org.fxapps.ml.api.runtime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.drools.compiler.kproject.ReleaseIdImpl;
import org.fxapps.ml.api.model.Model;
import org.fxapps.ml.api.model.ModelList;
import org.fxapps.ml.api.service.KieMLService;
import org.kie.api.KieServices;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;

class KieMLContainerImpl implements KieMLContainer {

	private static final String MODEL_DESCRIPTOR_PATH = "META-INF/models-descriptor.xml";
	private KieContainer kieContainer;
	private ModelList models;
	private KieMLService kieMLService;

	public KieMLContainerImpl(String gAV) {
		this(new ReleaseIdImpl(gAV));
	}

	public KieMLContainerImpl(ReleaseId releaseId) {
		this(loadKieContainer(releaseId));
	}

	public KieMLContainerImpl(KieContainer kc) {
		this.kieContainer = kc;
		loadModels();
		this.kieMLService = KieMLService.get(this);
	}

	@Override
	public KieContainer getKieContainer() {
		return kieContainer;
	}

	@Override
	public ModelList modelsList() {
		return models;
	}

	private static KieContainer loadKieContainer(ReleaseId releaseId) {
		KieServices ks = KieServices.Factory.get();
		KieContainer kieContainer = ks.newKieContainer(releaseId);
		return kieContainer;
	}

	private void loadModels() {
		ClassLoader cl = kieContainer.getClassLoader();
		InputStream modelsIS = cl.getResourceAsStream(MODEL_DESCRIPTOR_PATH);
		if (modelsIS == null) {
			throw new IllegalArgumentException("Not able to read descriptor" + MODEL_DESCRIPTOR_PATH);
		}
		try {
			JAXBContext context = JAXBContext.newInstance(Model.class, ModelList.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			models = (ModelList) unmarshaller.unmarshal(modelsIS);
			models.getModels().forEach(m -> m.setLabels(loadLabelsForMode(m)));
		} catch (JAXBException e) {
			throw new IllegalArgumentException("Not able to unmarshall descriptor" + MODEL_DESCRIPTOR_PATH, e);
		}

	}
	
	public KieMLService getService() {
		return this.kieMLService;
	}

	private List<String> loadLabelsForMode(Model m) {
		List<String> labels = new ArrayList<>();
		InputStream isLabel = kieContainer.getClassLoader().getResourceAsStream(m.getModelLabelsPath());
		BufferedReader reader = new BufferedReader(new InputStreamReader(isLabel));
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				labels.add(line);
			}
		} catch (Exception e) {
			throw new Error(e);
		} finally {
			try {
				isLabel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return labels;
	}

}
