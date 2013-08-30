package cli;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import spellcheck.WordCheck;
import wordnet.Dictionary;

public class WordSpellCheck {
	
	private static final String filename = "input.tsv";

	public static List<String> readWords(String file) {
		BufferedReader buffer;
		List<String> words = new ArrayList <String> ();
		
		try
		{
			buffer = new BufferedReader(new FileReader(file));   
			String line, temp[];

			while ((line = buffer.readLine())!= null){
				temp = line.split(" "); //split spaces
				
				words.add(temp[0]);
			}
			
			buffer.close();
			
		} catch(IOException e){
			System.err.println(e.toString());
		}
		return words;
		
	}

	public static void main(String[] args) {
		
		List<String> words = readWords(filename);
		Dictionary dict = new Dictionary();
		WordCheck wc = new WordCheck(dict);
		for (String str : words) {
			System.out.print(str  +"\t");
			if(! dict.hasWord(str)){
				Map<String, Integer> map = wc.getCorrect(str);
				for (String string : map.keySet()) {
					System.out.print(string+" <"+ map.get(string) +">\t");
				}
				System.out.println();
			} else {
				System.out.println("Correct");
			}	
		}
	}

}
