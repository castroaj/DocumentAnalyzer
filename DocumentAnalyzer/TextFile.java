import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class TextFile {

	private File file;
	private ArrayList<String> lines;
	private HashMap<String, Word> words;
	private HashMap<String, ArrayList<Integer>> wordFrequency;
	private ArrayList<Word> rankingList;

	public TextFile(File file) {
		this.file = file;
		this.lines = new ArrayList<String>();
		this.words = new HashMap<String, Word>();
		this.wordFrequency = new HashMap<String, ArrayList<Integer>>();
		this.rankingList = new ArrayList<Word>();
	}

	public TextFile(File file, ArrayList<String> lines, HashMap<String, Word> words,
			HashMap<String, ArrayList<Integer>> wordFrequency, ArrayList<Word> rankingList) {
		this.lines = lines;
		this.words = words;
		this.wordFrequency = wordFrequency;
		this.rankingList = rankingList;
	}
	
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public ArrayList<String> getLines() {
		return lines;
	}

	public void setLines(ArrayList<String> lines) {
		this.lines = lines;
	}

	public HashMap<String, Word> getWords() {
		return words;
	}

	public void setWords(HashMap<String, Word> words) {
		this.words = words;
	}

	public HashMap<String, ArrayList<Integer>> getWordFrequency() {
		return wordFrequency;
	}

	public void setWordFrequency(HashMap<String, ArrayList<Integer>> wordFrequency) {
		this.wordFrequency = wordFrequency;
	}

	public ArrayList<Word> getRankingList() {
		return rankingList;
	}

	public void setRankingList(ArrayList<Word> rankingList) {
		this.rankingList = rankingList;
	}
	
}
