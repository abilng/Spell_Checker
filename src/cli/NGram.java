package cli;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import spellcheck.TrigramCheck;
import wordnet.Dictionary;
import corpus.TrainedWords;
import spellcheck.*;
public class NGram {
	static String inputFile="input.tsv";
	static String outputFile="output.tsv";
	static String marker="<EOL>";
	private static String [] trigrams;
	
	public static String addMarker(String sentence)
	{
		
		StringBuilder sb=new StringBuilder(sentence);
		for(int i=0; i<sentence.length();i++)
		{
			if(sentence.charAt(i)=='.')
			{
				sb.deleteCharAt(i);
				sb.insert(i," "+marker);
			}
		}
		return String.valueOf(sb);
		
	}
	
	public static List<String> readWords(String file) {
		BufferedReader buffer;
		List<String> words = new ArrayList <String> ();

		try
		{
			buffer = new BufferedReader(new FileReader(file));   
			String line, temp[];

			while ((line = buffer.readLine())!= null){
				line=addMarker(line);
				temp = line.split(" "); //split spaces

				words.add(temp[0]);
			}

			buffer.close();

		} catch(IOException e){
			System.err.println(e.toString());
		}
		return words;

	}
	
	private static void spellCheck(Dictionary dict, TrigramCheck wc,
			String curr, BufferedWriter buffer, List<String> words, int i) throws IOException {
		trigrams = null;
		if(! dict.hasWord(curr)){
			buffer.write(curr +"\t");
			trigrams[0]=words.get(i-1);
			trigrams[1]=words.get(i-2);
			
			Map<String, Double> map = wc.getCorrect(curr,trigrams);

			for (String string : map.keySet())
				buffer.write(string+"  <"+ map.get(string) +">\t");

			buffer.newLine();
		} else {
			buffer.write(curr +"  *");
			buffer.newLine();
		}
	}
	


	public static void main(String[] args) {

		List<String> words = readWords(inputFile);
		Dictionary dict = new Dictionary();
		TrainedWords trainedWords = new TrainedWords();
		TrigramCheck wc = new TrigramCheck(dict,trainedWords);

		BufferedWriter buffer;
		try {
			buffer = new BufferedWriter(new FileWriter(outputFile));
			for (String str : words) {
				spellCheck(dict, wc, str,buffer,words,words.indexOf(str));	
			}
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
