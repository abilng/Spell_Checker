package cli;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import corpus.TrainedData;
import spellcheck.TrigramCheck;
import wordnet.Dictionary;

public class PhraseSpellCheck {
	static String inputFile="phrases_input.tsv";
	static String outputFile="phrases_output.tsv";
	
	
	public static List<String> readPhrases(String file) {
		BufferedReader buffer;
		List<String> phrases = new ArrayList <String> ();

		try
		{
			buffer = new BufferedReader(new FileReader(file));   
			String line;

			while ((line = buffer.readLine())!= null){
				phrases.add(line);
			}

			buffer.close();

		} catch(IOException e){
			e.printStackTrace();
		}
		return phrases;

	}
	
	private static void spellCheck(Dictionary dict, TrainedData trainedData,
			String curr, BufferedWriter buffer) throws IOException {
		
		TrigramCheck trigramCheck = new TrigramCheck(trainedData);
		
		StringBuilder trigrams = new StringBuilder();
		List<String> words = new ArrayList<String>();
		
		for (String word : curr.split("\\s+")) {
			words.add(word);
		}

		for (String curr_word : words) {
			if(! dict.hasWord(curr_word) && ! trainedData.hasWord(curr_word) ){
				buffer.write(curr_word +"\t");
				trigrams.delete(0, trigrams.length());//make empty
				for(int i=words.indexOf(curr_word) -2;i<words.indexOf(curr_word);i++) {
					if(i>=0) trigrams.append(words.get(i)+" ");
				}
				if(trigrams.length()>0)
					trigrams.delete(trigrams.length()-1, trigrams.length());

				Map<String, Double> map = trigramCheck.getCorrect(curr_word,trigrams.toString());

				buffer.write("[");
				for (String string : map.keySet()) {
					String score = String.format("%.2f",map.get(string)*100);
					buffer.write(string+"  <"+ score +">\t");
				}
				buffer.write("]");
				
			} else {
				buffer.write(curr_word +" ");
			}
		}
		buffer.newLine();
		
	}
	


	public static void main(String[] args) {

		Dictionary dict = new Dictionary();
		TrainedData trainedData = new TrainedData();		


		List<String> phrases = readPhrases(inputFile);
		BufferedWriter buffer;
		try {
			buffer = new BufferedWriter(new FileWriter(outputFile));
			for (String str : phrases) {
				spellCheck(dict, trainedData, str,buffer);	
			}
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
