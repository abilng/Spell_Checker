package corpus;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import config.Properties;

public class TrainedWords {
	private static final String FILE_NAME = "Data/TrigramData.dat";
	private static final String BROWN_TXT_PATH = Properties.BROWN_PATH;
	private static final String CAT_FILE = BROWN_TXT_PATH + "cats.txt";
	
	
	private int trigramCount;
	private int trigramVacabulary;
	private int bigramVacabulary;
	private int bigramCount;
	private int unigramVacabulary;
	private int unigramCount;
	
	Hashtable<String,Integer> trigramTable;
	Hashtable<String, Integer> bigramTable;
	Hashtable<String, Integer> unigramTable;

	public TrainedWords() {
		try {
			read(FILE_NAME);
			trigramVacabulary = trigramTable.size();
			bigramVacabulary = bigramTable.size();
			unigramVacabulary = unigramTable.size();
		} catch (Exception e) {
			System.err.println("Data File Not Found: (" + e.getMessage() + ")");
			System.err.println("Reading taining file name from Text File..");

			trigramTable = new Hashtable<String, Integer>();
			trigramCount = 0;
			bigramTable = new Hashtable<String, Integer>();
			bigramCount = 0;
			unigramTable = new Hashtable<String, Integer>();
			unigramCount = 0;
			train(CAT_FILE);

			try {
				save(FILE_NAME);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void read(String file) throws IOException, ClassNotFoundException {
		InputStream buffer;
		buffer = new BufferedInputStream( new FileInputStream(file));
		ObjectInput input = new ObjectInputStream ( buffer );
		trigramTable = (Hashtable<String, Integer>) input.readObject();
		trigramCount = (Integer) input.readObject();
		bigramTable = (Hashtable<String, Integer>) input.readObject();
		bigramCount = (Integer) input.readObject();
		unigramTable = (Hashtable<String, Integer>) input.readObject();
		unigramCount = (Integer) input.readObject();
		input.close();
	}

	private void save(String file) throws IOException {
		OutputStream buffer;
		buffer = new BufferedOutputStream( new FileOutputStream(file));
		ObjectOutput output = new ObjectOutputStream ( buffer );
		output.writeObject(trigramTable);
		output.writeObject((Integer)trigramCount);
		output.writeObject(bigramTable);
		output.writeObject((Integer)bigramCount);
		output.writeObject(unigramTable);
		output.writeObject((Integer)unigramCount);
		output.close();
	}
	private void train(String catfile) {	
		List<String> file_names = new ArrayList<String>();
		BufferedReader buffer;
		try
		{
			buffer = new BufferedReader(new FileReader(catfile));   
			String line, temp[];

			while ((line = buffer.readLine())!= null)
			{ 
				temp = line.split(" "); //split spaces
				file_names.add(temp[0]);
			}
			buffer.close();
		} catch(Exception ex){
			ex.printStackTrace();
			System.exit(2);
		}

		BrownCorpusReader brownCorpusReader = new BrownCorpusReader();

		for (String filename : file_names) {

			try {
				buffer = new BufferedReader(new FileReader(BROWN_TXT_PATH+filename));
				System.err.println("Opening :" + filename +"..");
				trigramCount+= brownCorpusReader.getNWords(buffer,3,trigramTable);
				buffer.close();
				buffer = new BufferedReader(new FileReader(BROWN_TXT_PATH+filename));
				bigramCount+= brownCorpusReader.getNWords(buffer,2,bigramTable);
				buffer.close();
				buffer = new BufferedReader(new FileReader(BROWN_TXT_PATH+filename));
				bigramCount+= brownCorpusReader.getWords(buffer,unigramTable);
				buffer.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(2);
			}
		}
		trigramVacabulary = trigramTable.size();
		bigramVacabulary = bigramTable.size();
		unigramVacabulary = unigramTable.size();
	}


	public int trigramCount(String trigram) {
		Integer ret = trigramTable.get(trigram);
		if(ret == null) return 0;
		return ret;
	}
	public int bigramCount(String bigram) {
		Integer ret = bigramTable.get(bigram);
		if(ret == null) return 0;
		return ret;
	}
	public int wordCount(String word) {
		Integer ret = unigramTable.get(word);
		if(ret == null) return 0;
		return ret;
	}
	
	public double prior(String ngram) {
		
		String [] words=ngram.split(" ");
		if(words.length==2)		
			return ((bigramCount(ngram)+0.5)/(0.5*bigramVacabulary+bigramCount));
		
		if(words.length==3)
			return ((trigramCount(ngram)+0.5)/(0.5*trigramVacabulary+trigramCount));
		else
			return (wordCount(ngram)+ 0.5)/(0.5*unigramVacabulary + unigramCount);
	}
		
}
	

