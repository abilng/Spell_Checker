/**
 * 
 */
package wordnet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.dictionary.FileBackedDictionary;

/**
 * @author abilng
 *
 */
public class Dictionary {
	private static final String WORDNET_CONFIG_FILE = 
			"/home/abil/workspace/NLP/config/file_properties.xml";
	private net.didion.jwnl.dictionary.Dictionary dict;
	
	public Dictionary() {
		try {
			JWNL.initialize(new FileInputStream(WORDNET_CONFIG_FILE));
		} catch (FileNotFoundException | JWNLException e) {
			e.printStackTrace();
		}
		dict = FileBackedDictionary.getInstance();
	}

	public boolean hasWord(String word){
			int size = 0;
			try {
				size = dict.lookupAllIndexWords(word).size();
			} catch (JWNLException e) {
				e.printStackTrace();
			}	
			return (size != 0);
	}
}
