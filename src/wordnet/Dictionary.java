/**
 * 
 */
package wordnet;

import java.io.File;
import java.io.IOException;

import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.POS;

/**
 * @author abilng
 *
 */
public class Dictionary {

	private static final String WORDNET_DICT = "dict";
	private IDictionary dict;

	public Dictionary() {
		try {
			dict = new edu.mit.jwi.Dictionary(
					new File(WORDNET_DICT));
			dict.open () ;
		} catch ( IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean hasWord(String word){
		int size = 0;
		for ( POS pos:POS.values()){
			IIndexWord indexWord = dict.getIndexWord(word, pos);
			if(indexWord == null) continue;
			return true;
		}
		return (size != 0);
	}
}
