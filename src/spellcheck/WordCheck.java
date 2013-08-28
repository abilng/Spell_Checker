/**
 * 
 */
package spellcheck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import wordnet.Dictionary;

/**
 * @author abilng
 *
 */
public class WordCheck {

	Dictionary dictionary;
	ConfusionMatrix cMatrix;

	public WordCheck(Dictionary dictionary) {
		this.dictionary = dictionary;
		this.cMatrix = new ConfusionMatrix();
	}

	private List<String> edits(final String word) {
		final List<String> wordarray = Collections.synchronizedList(new ArrayList<String>());

		for(int i=0; i < word.length(); ++i){//delete i -th element
			if((i==0 && cMatrix.del('@',word.charAt(i))>0)||
					(i!=0 && cMatrix.del(word.charAt(i-1),word.charAt(i))>0)){   
				String newstr = word.substring(0, i) + word.substring(i+1);
				isValidWord(wordarray,newstr);	
			}
		}
		for(int i=0; i < word.length()-1; ++i){
			if(cMatrix.rev(word.charAt(i),word.charAt(i+1))>0) {
				String newstr = word.substring(0, i) + word.substring(i+1, i+2) +
						word.substring(i, i+1) + word.substring(i+2);
				isValidWord(wordarray,newstr);	
			}
		}

		for(int i=0; i < word.length(); ++i) {
			for(char c='a'; c <= 'z'; ++c) {
				if((i==0 && cMatrix.add('@',c)>0)||
						(i!=0&&cMatrix.add(word.charAt(i-1),c)>0)) {   
					String newstr = word.substring(0, i) + String.valueOf(c) + word.substring(i+1);
					isValidWord(wordarray,newstr);
				}
			}
		}

		for(int i=0; i < word.length(); ++i) {
			for(char c='a'; c <= 'z'; ++c) {
				if(cMatrix.sub(word.charAt(i),c)>0) {
					String newstr = word.substring(0, i) + String.valueOf(c) +
							word.substring(i);
					isValidWord(wordarray,newstr);
				}
			}
		}

		return wordarray;
	}
	private void isValidWord(final List<String> words,final String word) {

		if(dictionary.hasWord(word)){
			words.add(word);
		}
	}

	public List<String> getCorrect(final String word){
		return edits(word);
	}
}
