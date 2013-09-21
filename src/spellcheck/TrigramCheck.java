package spellcheck;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import corpus.TrainedData;

public class TrigramCheck {

	private static final int NO_OF_SUGGESTION = 10;
	private static final double LAMBDA1 = 5.0/10;
	private static final double LAMBDA2 = 3.0/10;
	private static final double LAMBDA3 = 2.0/10;

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

		Map<String,Double> trigramProbMap = new HashMap<String,Double>();
		Map<String,Double> bigramProbMap = new HashMap<String,Double>();
		double trigramProb = 0,bigramProb = 0,unigramProb = 0;
		String newWord;
		for (Map.Entry<String, Double> entry : ngrams.entrySet()) {
			String ngram = entry.getKey();
			String [] words=ngram.trim().split(" ");
			if(words.length==3){
				newWord = words[2];
				trigramProb = trainedData.trigramPrior(newWord, words[0] +" " + words[1]);
				trigramProb = trigramProb>Double.MIN_VALUE?trigramProb:Double.MIN_VALUE;
				bigramProb = trainedData.bigramPrior(newWord, words[1]);
				bigramProb = bigramProb>Double.MIN_VALUE?bigramProb:Double.MIN_VALUE;
			} else if(words.length==2){
				newWord = words[1];
				trigramProb = Double.MIN_VALUE;
				bigramProb = trainedData.bigramPrior(newWord, words[0]);
				bigramProb = bigramProb>Double.MIN_VALUE?bigramProb:Double.MIN_VALUE;
			} else {   //if(words.length==1)
				newWord = words[0];
				trigramProb = Double.MIN_VALUE;
				bigramProb = Double.MIN_VALUE;
			}
			trigramProbMap.put(newWord,trigramProb);
			bigramProbMap.put(newWord,bigramProb);
		}
		normalize(bigramProbMap);
		normalize(trigramProbMap);
		System.err.println();
		for (Map.Entry<String, Double> entry : possiableWords.entrySet()) {
			newWord = entry.getKey();
			bigramProb = bigramProbMap.get(newWord);
			trigramProb = trigramProbMap.get(newWord);
			unigramProb = entry.getValue();
			possiableWords.put(newWord,
					LAMBDA1*trigramProb + LAMBDA2*bigramProb + LAMBDA3*unigramProb);
		}
		
		return possiableWords;
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
