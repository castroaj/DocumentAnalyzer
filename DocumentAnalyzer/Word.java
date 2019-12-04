
public class Word implements Comparable<Word> {
	private final String word;
	private int frequency;
	private int rank;
	
	public Word(String word) {
		this.word = word;
		this.frequency = 1;
	}
	
	public String getWord() {
		return word;
	}
	
	public void incrementFrequency()
	{
		this.frequency++;
	}
	
	public int getFrequency()
	{
		return this.frequency;
	}
	
	public int getRank()
	{
		return this.rank;
	}

	public void setRank(int rank)
	{
		this.rank = rank;
	}
	
	@Override
	public int compareTo(Word o) {
		return o.frequency - this.frequency;
	}
}
