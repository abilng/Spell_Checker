package spellcheck;

import java.util.Map;

public class TrigramCheck {
	/**
	 * 
	 */
	
	public TrigramCheck() {
		// TODO Auto-generated constructor stub
		//init trigram 
	}
	/**
	 * 
	 * @param word word to be corrected
	 * @param history  previous words(up to 2)
	 * @return Map of Corrected string and its score 
	 */
	public  Map<String,Double> getCorrect(String word,String history){
		//TODO 
		return null;
		
	}
	
	/**
	 * 
	 * @param trigram
	 * @return score (probability) of Trigram
	 */
	private double getScore(String trigram) {
		
		return 0;
	}
	
}
