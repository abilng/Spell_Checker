/**
 * 
 */
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

/**
 * @author abilng
 *
 */
public class TrainedWords {

	private static final String FILE_NAME = "Data/TranedData.dat";
	private static final String BROWN_TXT_PATH = "TrainData/Brown/";
	private static final String CAT_FILE = BROWN_TXT_PATH + "cats.txt";
	
	private int wordCount;
	private int vacabulary;
	
	Hashtable<String,Integer> dataMap;

	public TrainedWords() {
		try {
			read(FILE_NAME);
			vacabulary = dataMap.size();
		} catch (Exception e) {
			System.err.println("Data File Not Found: (" + e.getMessage() + ")");
			System.err.println("Reading taining file name from Text File..");

			dataMap = new Hashtable<String, Integer>();
			wordCount = 0;
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
		dataMap = (Hashtable<String, Integer>) input.readObject();
		wordCount = (Integer) input.readObject();
		input.close();
	}

	private void save(String file) throws IOException {
		OutputStream buffer;
		buffer = new BufferedOutputStream( new FileOutputStream(file));
		ObjectOutput output = new ObjectOutputStream ( buffer );
		output.writeObject(dataMap);
		output.writeObject((Integer)wordCount);
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
		}

		BrownCorpusReader brownCorpusReader = new BrownCorpusReader();

		for (String filename : file_names) {

			try {
				buffer = new BufferedReader(new FileReader(BROWN_TXT_PATH+filename));
				System.err.println("Opening :" + filename +"..");
				wordCount+= brownCorpusReader.getWords(buffer, dataMap);
				buffer.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		vacabulary = dataMap.size();
	}


	public int count(String word) {
		Integer ret = dataMap.get(word);
		if(ret == null) return 0;
		return ret;
	}
	
	public double prior(String word) {
		return (count(word)+ 0.5)/(0.5*vacabulary + wordCount);
		
	}

}
