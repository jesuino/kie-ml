package org.fxapps.ml.api.provider.opennlp;

import java.io.InputStream;

public class InputData {
	
	private InputStream modelIs;
	private String inputText;
	private boolean tokenize;
	private String delimiter;
	
	public InputData(InputStream modelIs, String inputText) {
		this(modelIs, inputText, false, null);
	}
	
	public InputData(InputStream modelIs, String inputText, boolean tokenize) {
		this(modelIs, inputText, tokenize, null);
	}
	
	public InputData(InputStream modelIs, String inputText, boolean tokenize, String delimiter) {
		super();
		this.modelIs = modelIs;
		this.inputText = inputText;
		this.tokenize = tokenize;
		this.delimiter = delimiter == null ? "," : delimiter;
	}

	public InputStream getModelIs() {
		return modelIs;
	}

	public String getInputText() {
		return inputText;
	}

	public boolean isTokenize() {
		return tokenize;
	}

	public String getDelimiter() {
		return delimiter;
	}
	
}
