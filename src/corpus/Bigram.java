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
import java.util.Hashtable;

import config.Properties;

class Bigram {

	private static final String FILE_NAME = "Data/BigramData.dat";
	private static final String BIGRAM_FILE = Properties.BIGRAM_FILE;

	Hashtable<String,Integer> bigramTable;
	Hashtable<String,Integer> unigramTable;

	public Bigram() {
		try {
			read(FILE_NAME);
		} catch (Exception e) {
			System.err.println("Data File Not Found: (" + e.getMessage() + ")");
			System.err.println("Reading taining file name from Text File..");

			bigramTable = new Hashtable<String, Integer>();
			unigramTable = new Hashtable<String,Integer>();
			train();

			try {
				save(FILE_NAME);
			} catch (IOException e1) {
				e1.printStackTrace();
				System.exit(2);
			}
		}
	}


	@SuppressWarnings("unchecked")
	private void read(String file) throws IOException, ClassNotFoundException {
		InputStream buffer;
		buffer = new BufferedInputStream( new FileInputStream(file));
		ObjectInput input = new ObjectInputStream ( buffer );
		bigramTable = (Hashtable<String, Integer>) input.readObject();
		unigramTable = (Hashtable<String, Integer>) input.readObject();
		input.close();
	}

	private void save(String file) throws IOException {
		OutputStream buffer;
		buffer = new BufferedOutputStream( new FileOutputStream(file));
		ObjectOutput output = new ObjectOutputStream ( buffer );
		output.writeObject(bigramTable);
		output.writeObject(unigramTable);
		output.close();
	}
	private void train() {	
		BufferedReader buffer;
		try
		{

			System.err.println("Opening :" + BIGRAM_FILE +"..");
			buffer = new BufferedReader(new FileReader(BIGRAM_FILE));
			//TODO Update
			buffer.close();

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(2);
		}
	}

	public int count(String word) {
		Integer ret = bigramTable.get(word);
		if(ret == null) return 0;
		return ret;
	}
	private int unigramcount(String word) {
		Integer ret = unigramTable.get(word);
		if(ret == null) return 0;
		return ret;
	}

	public double prior(String ngram) {
		// TODO Auto-generated method stub
		return 0;
	}

}
