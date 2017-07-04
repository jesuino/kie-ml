package org.fxapps.ml.api.provider.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.fxapps.ml.api.model.Result;
import org.fxapps.ml.api.provider.opennlp.InputData;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.Span;

/**
 * First experimental version.
 * 
 * @author wsiqueir
 *
 */
public class ModelFactory {

	private final static Map<String, Function<InputData, Result>> modelsFactory;

	static {
		modelsFactory = new HashMap<>();
		modelsFactory.put("Sentence", ModelFactory::sentenceModel);
		modelsFactory.put("NameFinder", ModelFactory::nameFinderModel);
		modelsFactory.put("Tagger", ModelFactory::taggerModel);
		modelsFactory.put("Parser", ModelFactory::parserModel);
		modelsFactory.put("Categorizer", ModelFactory::categorizerModel);
	}

	private ModelFactory() {
	}

	public static Function<InputData, Result> getModel(String name) {
		return modelsFactory.get(name);
	}

	private static Result sentenceModel(InputData data) {
		StringBuffer sb = new StringBuffer();
		String sentence = data.getInputText();
		SentenceModel model;
		try {
			model = new SentenceModel(data.getModelIs());
		} catch (IOException e) {
			throw new IllegalArgumentException("Error loading model", e);
		}
		SentenceDetectorME detector = new SentenceDetectorME(model);
		String sentences[] = detector.sentDetect(sentence);
		Span[] spans = detector.sentPosDetect(sentence);
		Result result = new Result();
		result.setPredictions(new HashMap<>());
		for (int i = 0; i < sentences.length; i++) {
			sb.append(spans[i]);
			sb.append(":");
			sb.append(sentences[i]);
			result.getPredictions().put(spans[i].toString(), spans[i].getProb());
		}
		result.setResultTxt(sb.toString());
		return result;
	}

	private static Result nameFinderModel(InputData data) {
		TokenNameFinderModel model;
		try {
			model = new TokenNameFinderModel(data.getModelIs());
		} catch (IOException e) {
			throw new IllegalArgumentException("Error loading model", e);
		}
		NameFinderME nameFinder = new NameFinderME(model);
		WhitespaceTokenizer whitespaceTokenizer = WhitespaceTokenizer.INSTANCE;
		String[] inputTokens = getTokenizeData(data);
		Result result = new Result();
		result.setPredictions(new HashMap<>());
		for (String s : inputTokens) {
			String[] tokenized = whitespaceTokenizer.tokenize(s);
			Span[] find = nameFinder.find(tokenized);
			for (Span span : find) {
				result.getPredictions().put(s + ": " + span, span.getProb());
			}
		}
		return result;
	}

	private static Result taggerModel(InputData data) {
		POSModel model;
		try {
			model = new POSModel(data.getModelIs());
		} catch (IOException e) {
			throw new IllegalArgumentException("Error loading model", e);
		}
		POSTaggerME tagger = new POSTaggerME(model);
		WhitespaceTokenizer whitespaceTokenizer = WhitespaceTokenizer.INSTANCE;
		String[] tokens = whitespaceTokenizer.tokenize(data.getInputText());
		String[] tag = tagger.tag(tokens);
		Result result = new Result();
		result.setPredictions(new HashMap<>());
		for (int i = 0; i < tag.length; i++) {
			result.getPredictions().put(tag[i] + ": " + tokens[i], tagger.probs()[i]);
		}
		return result;
	}

	private static Result parserModel(InputData data) {
		ParserModel model;
		Result result = new Result();
		try {
			model = new ParserModel(data.getModelIs());
		} catch (IOException e) {
			throw new IllegalArgumentException("Error loading model", e);
		}
		Parser parser = ParserFactory.create(model);
		Parse[] parsingResult = ParserTool.parseLine(data.getInputText(), parser, 1);
		for (Parse parse : parsingResult) {
			StringBuffer sb = new StringBuffer();
			parse.show(sb);
			String resultStr = result.getResultTxt() + "\n" + parse + ": " + sb.toString();
			result.setPredictionsResult(resultStr);
		}
		return result;
	}

	private static Result categorizerModel(InputData data) {
		DoccatModel m;
		try {
			m = new DoccatModel(data.getModelIs());
		} catch (IOException e) {
			throw new IllegalArgumentException("Error loading model", e);
		}
		DocumentCategorizerME categorizer = new DocumentCategorizerME(m);
		double[] outcomes = categorizer.categorize(getTokenizeData(data));
		String category = categorizer.getBestCategory(outcomes);
		Result result = new Result();
		result.setPredictionsResult(category);
		return result;
	}
	
	private static String[] getTokenizeData(InputData data) {
		String[] inputTokens;
		if (data.isTokenize()) {
			inputTokens = data.getInputText().split(data.getDelimiter());
		} else {
			inputTokens = new String[1];
			inputTokens[0] = data.getInputText();
		}
		return inputTokens;
	}

}
