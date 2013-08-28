
import java.util.List;

import spellcheck.WordCheck;
import wordnet.Dictionary;


public class Main {

	private static Dictionary dict;


	public static void main(String[] args){
		// TODO Auto-generated method stub\
		String str = "managemnt";
		dict = new Dictionary();
		WordCheck wc = new WordCheck(dict);
		if(! dict.hasWord(str)){
			List<String> myList = wc.getCorrect(str);
			for (String string : myList) {
				System.out.println(string+"  <p>");
			}
		} else {
			System.out.println("Correct");
		}

	}

}
