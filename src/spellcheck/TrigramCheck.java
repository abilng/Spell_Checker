package spellcheck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import corpus.TrainedData;

public class TrigramCheck {

	private static final int NO_OF_SUGGESTION = 10;
	private static final double LAMBDA1 = 1.0/3;
	private static final double LAMBDA2 = 1.0/3;
	private static final double LAMBDA3 = 1.0/3;

	WordCheck wc;
	TrainedData trainedData;



	public TrigramCheck(TrainedData trainedData) {

		this.trainedData= trainedData;
		wc=new WordCheck(trainedData);

	}
	/**
	 * 
	 * @param word word to be corrected
	 * @param history  previous words(up to 2)
	 * @return Map of Corrected string and its score 
	 */
	public  Map<String,Double> getCorrect(String word,String history){

		System.err.println("["+history + "]|" + word +"|");

		Map<String, Double> possiableWords = wc.getCorrect(word);
		Map<String, Double> trigram = new HashMap<String, Double>();
		Map<String, Double> validWords; 
		for (Map.Entry<String, Double> entry : possiableWords.entrySet())
		{			
			trigram.put(history.trim()+" "+entry.getKey(),Double.MIN_VALUE);
		}
		validWords = getScore(trigram,possiableWords);
		validWords=sortByValue(validWords);
		return normalize(validWords);
	}


	private Map<String, Double> getScore(Map<String, Double> ngrams,
			Map<String, Double> possiableWords) {

		List<Double> trigramProbList = new ArrayList<Double>();
		List<Double> bigramProbList = new ArrayList<Double>();
		double trigramProb = 0,bigramProb = 0,unigramProb = 0;
		for (Map.Entry<String, Double> entry : ngrams.entrySet()) {
			String ngram = entry.getKey();
			String [] words=ngram.trim().split(" ");
				
			if(words.length==3){
				trigramProb = trainedData.trigramPrior(words[2], words[0] +" " + words[1]);
				trigramProb = trigramProb>Double.MIN_VALUE?trigramProb:Double.MIN_VALUE;
				bigramProb = trainedData.bigramPrior(words[2], words[1]);
				bigramProb = bigramProb>Double.MIN_VALUE?bigramProb:Double.MIN_VALUE;
			} else if(words.length==2){
				trigramProb = Double.MIN_VALUE;
				bigramProb = trainedData.bigramPrior(words[1], words[0] );
				bigramProb = bigramProb>Double.MIN_VALUE?bigramProb:Double.MIN_VALUE;
			} else {   //if(words.length==1)
				trigramProb = Double.MIN_VALUE;
				bigramProb = Double.MIN_VALUE;
			}
			
			trigramProbList.add(trigramProb);
			bigramProbList.add(bigramProb);
		}
		normalise(bigramProbList);
		normalise(trigramProbList);
		int i = 0;
		for (Map.Entry<String, Double> entry : possiableWords.entrySet()) {
			bigramProb = bigramProbList.get(i);
			trigramProb = trigramProbList.get(i);
			unigramProb = entry.getValue();

			possiableWords.put(entry.getKey(),
					LAMBDA1*trigramProb + LAMBDA2*bigramProb + LAMBDA3*unigramProb);
			
			i++;
		}


		return possiableWords;
	}

	private void normalise(List<Double> proablities) {
		double sum =0;
		for (Double values : proablities)
			sum+=values;
		for (int i = 0; i < proablities.size(); i++) {
			proablities.set(i, proablities.get(i)/sum);
		}
	}
	/**
	 * Normalize scores
	 * @param correct
	 * @return
	 */
	private Map<String, Double> normalize(Map<String, Double> map) {

		double sum =0;
		for (Double values : map.values())
			sum+=values;
		for (Map.Entry<String, Double> entry : map.entrySet()) {
			map.put(entry.getKey(), entry.getValue()/sum);
		}
		return map;
	}

	/**
	 * For sorting of "Map <String,Integer>" according to  value
	 * @param map -input map
	 * @return sorted map
	 */

	static Map<String,Double> sortByValue(Map<String, Double> map) {
		LinkedList<Entry<String,Double>> list =
				new LinkedList<Entry<String,Double>>(map.entrySet());
		Collections.sort(list, new Comparator<Entry<String,Double>>() {
			@Override
			public int compare(Entry<String,Double> arg0,
					Entry<String,Double> arg1) {
				// reverse Order
				return Double.compare(arg1.getValue(), arg0.getValue());
			}
		});

		Map<String, Double> result = new LinkedHashMap<String,Double>();
		int cResult = 0;
		for (Iterator<?> it = list.iterator(); it.hasNext();) {
			@SuppressWarnings("unchecked")
			Entry<String,Double> entry = 
			(Entry<String, Double>) it.next();
			result.put(entry.getKey(), entry.getValue());
			if ( ++cResult > NO_OF_SUGGESTION ) break;
		}
		return result;
	}
}
