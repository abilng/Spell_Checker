package spellcheck;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import corpus.TriGrams;

public class TrigramCheck {
	private static final int NO_OF_SUGGESTION = 10;
	WordCheck wc;
	TriGrams trainedTrigrams;
	
	

	public TrigramCheck() {
		// TODO Auto-generated constructor stub
		trainedTrigrams=new TriGrams();
		wc=new WordCheck();
		
	}
	/**
	 * 
	 * @param word word to be corrected
	 * @param history  previous words(up to 2)
	 * @return Map of Corrected string and its score 
	 */
	public  Map<String,Double> getCorrect(String word,String history){
				
		Map<String, Double> trigramMap = null,bigramMap = null,ngramMap=null;
		Map<String, Double> validWordMap = wc.getCorrect(word);
		String currBigram,currTrigram;
		String [] prevWords=history.split(" ");
		for (Map.Entry<String, Double> entry : validWordMap.entrySet())
		{			
			currTrigram=history+" "+entry.getKey();
			currBigram=prevWords[1]+" "+entry.getKey();
			trigramMap.put(currTrigram, getScore(currTrigram,history));
			bigramMap.put(currBigram, getScore(currBigram,history));
		}
		trigramMap=sortByValue(trigramMap);
		bigramMap=sortByValue(bigramMap);
		ngramMap.putAll(trigramMap);
		ngramMap.putAll(bigramMap);
		return normalize(ngramMap);
	}

	
	/**
	 * 
	 * @param trigram
	 * @param history 
	 * @return score (probability) of Trigram
	 */
	private double getScore(String trigram, String history) {

		return (trainedTrigrams.prior(trigram)/trainedTrigrams.prior(history));
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
