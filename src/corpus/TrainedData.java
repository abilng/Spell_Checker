package corpus;

public class TrainedData {
	
	Words unigramData;
	Trigram trigramData;
	Bigram bigramData;
	

	public TrainedData() {
		unigramData = new Words();
		trigramData = new Trigram();
		bigramData = new Bigram();
		
	}
	
	public int count(String ngram) {
		
		String [] words=ngram.split(" ");
		if(words.length==3)		
			return trigramData.count(ngram);
		
		if(words.length==2)
			return bigramData.count(ngram);
		else
			return unigramData.count(ngram);
	}
	
	public double prior(String ngram) {
		
		String [] words=ngram.split(" ");
		
		if(words.length==3)		
			return trigramData.prior(ngram);
		if(words.length==2)
			return bigramData.prior(ngram);
		else
			return unigramData.prior(ngram);
	}
		
}
	

