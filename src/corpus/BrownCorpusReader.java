package corpus;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class implements a corpus reader for Brown-style corpera that are
 * stored in a single file.
 */
public class BrownCorpusReader {

	private boolean decapitalizeFirstWord = true;

	private String [] Special_TAGS = {"'","``","''",".","(",")","*","--",",",":",};
	private String [] POS_TAGS = {
			"ABL","ABN","ABX","AP","AP$","AT",
			"BE","BED","BEDZ","BEG","BEM","BEN","BER","BEZ",
			"CC","CD","CD$","CS",
			"DO","DOD","DOZ","DT","DT$","DTI","DTS","DTX",
			"EX","HV","HVD","HVG","HVN","HVZ","IN",
			"JJ","JJ$","JJR","JJS","JJT","MD","NIL",
			"NN","NN$","NNS","NNS$","NP","NP$","NPS",
			"NPS$","NR","NR$","NRS","OD","PN","PN$","PP$",
			"PP$$","PPL","PPLS","PPO","PPS","PPSS","QL","QLP",
			"RB","RB$","RBR","RBT","RN","RP","TO","UH","VB","VBD","VBG",
			"VBN","VBZ","WDT","WP$","WPO","WPS","WQL","WRB"};

	public BrownCorpusReader()
	{

	}

	public List<TaggedWord>  getTaggedWords(String line){
		line = line.trim();
		if (line.length() == 0)
			return null;

		List<TaggedWord> sentence = new ArrayList<TaggedWord>();

		String[] lineParts = line.split("\\s+");
		for (int i = 0; i < lineParts.length; ++i) {
			String wordTag = lineParts[i];

			// Get the word and tag.
			int sepIndex = wordTag.lastIndexOf('/');
			if (sepIndex == -1) {
				System.err.println("Tag is missing in '" +
						wordTag + "'");
				continue;
			}
			String word = wordTag.substring(0, sepIndex);
			String tag = wordTag.substring(sepIndex + 1);

			if (word.length() == 0) {
				System.err.println("Zero-length word in '" +
						wordTag + "'");
				continue;
			}

			if (tag.length() == 0) {
				System.err.println("Zero-length tag in '" +
						wordTag + "'");
				continue;
			}
			if (decapitalizeFirstWord  && i == 0) {
				word = replaceCharAt(word, 0, Character.toLowerCase(word.charAt(0)));
			}

			for(String pos: Special_TAGS){
				if(tag.contains(pos))
					continue;
			}

			sentence.add(new TaggedWord(word, tag));        
		}
		return sentence;


	}
	/**
	 * Remove Modifiers in Tags
	 * @param rawTag
	 * @return
	 */
	public String normalizeTag(String rawTag) {
		String tag = rawTag;
		String startTag = tag;
		// remove plus, default to first
		int splitIndex = tag.indexOf('+');
		if (splitIndex >= 0)
			tag = tag.substring(0,splitIndex);

		int lastHyphen = tag.lastIndexOf('-');
		if (lastHyphen >= 0) {
			String first = tag.substring(0,lastHyphen);
			String suffix = tag.substring(lastHyphen+1);
			if (suffix.equalsIgnoreCase("HL") 
					|| suffix.equalsIgnoreCase("TL")
					|| suffix.equalsIgnoreCase("NC")) {
				tag = first;
			}
		}

		int firstHyphen = tag.indexOf('-');
		if (firstHyphen > 0) {
			String prefix = tag.substring(0,firstHyphen);
			String rest = tag.substring(firstHyphen+1);
			if (prefix.equalsIgnoreCase("FW")
					|| prefix.equalsIgnoreCase("NC")
					|| prefix.equalsIgnoreCase("NP"))
				tag = rest;
		}

		// neg last, and only if not whole thing
		int negIndex = tag.indexOf('*');
		if (negIndex > 0) {
			if (negIndex == tag.length()-1)
				tag = tag.substring(0,negIndex);
			else
				tag = tag.substring(0,negIndex)
				+ tag.substring(negIndex+1);
		}
		// multiple runs to normalize
		return tag.equals(startTag) ? tag : normalizeTag(tag);
	}
	
	public Map<String, Integer> getWords(BufferedReader reader,
			Map<String, Integer> dataMap) throws IOException {
	    String line;
	    while ((line = reader.readLine()) != null) {
	      line = line.trim();
	      
	      if (line.length() == 0)
	        continue;
	      
	      String[] lineParts = line.split("\\s+");
	      for (int i = 0; i < lineParts.length; ++i) {
	        String wordTag = lineParts[i];
	      
	        // Get the word and tag.
	        int sepIndex = wordTag.lastIndexOf('/');

	        	if (sepIndex == -1) {
					System.err.println("Tag is missing in '" +
							wordTag + "'");
					continue;
				}
				String word = wordTag.substring(0, sepIndex);

				if (word.length() == 0) {
					System.err.println("Zero-length word in '" +
							wordTag + "'");
					continue;
				}
				Integer val = dataMap.get(word);
				if(val == null)	dataMap.put(word,1);
				else dataMap.put(word.toLowerCase(),val + 1);
	      }
	    }
		return dataMap;
	}
	
	private static String replaceCharAt(String str, int pos, char c) {
		StringBuilder sb = new StringBuilder();
		sb.append(str.substring(0, pos));
		sb.append(c);
		sb.append(str.substring(pos + 1));
		return sb.toString();
	}
}
