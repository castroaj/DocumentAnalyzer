import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;

public class DocumentAnalyzer {

	// Command Line Options
	public static Boolean GUI;
	public static Boolean frequency = false;
	public static Boolean location = false;
	public static Boolean context = false;
	public static Boolean ranking = false;
	public static Boolean word = false;

	// Data Structures
	public static ArrayList<String> lines = new ArrayList<String>();
	public static HashMap<String, Word> words = new HashMap<String, Word>();
	public static HashMap<String, ArrayList<Integer>> wordFrequency = new HashMap<String, ArrayList<Integer>>();
	public static ArrayList<Word> rankingList = new ArrayList<Word>();

	public static void main(String[] args) {

		// Read Command line to determine what to do
		if (!parseCommandLine(args)) {
			if (GUI) {
				new DocumentAnalyzerGUI("Document Analyzer");
			} else {
				printHelp();
			}
		} else {

			// Read File
			File file = new File(args[0]);
			try {
				lines = readDocument(file);
			} catch (FileNotFoundException e) {
				printHelp();
			}

			// Create word hashMap
			generateWords(lines, words, wordFrequency);
			if (word) {
				String givenWord = args[1];
				if (givenWord.contains("-")) {
					printHelp();
				} else {
					determineOutput(givenWord);
				}
			} else {
				createRankings(rankingList, words);
				printAllRankings(100);
			}
		}
	}

	public static void determineOutput(String givenWord) {

		// -f option
		if (frequency) {
			printFrequencyOfWord(givenWord);
		}

		// -l option
		if (location) {
			printLocationsOfGivenWord(givenWord);
		}

		// -c option
		if (context) {
			printLinesOfGivenWord(givenWord);
		}

		// -r option
		if (ranking) {
			createRankings(rankingList, words);
			printRankOfGivenWord(givenWord);
		}

		// All options are not used
		if (!frequency && !location && !context && !ranking) {
			createRankings(rankingList, words);
			printFrequencyOfWord(givenWord);
			printRankOfGivenWord(givenWord);
			printLocationsOfGivenWord(givenWord);
			printLinesOfGivenWord(givenWord);
		}
	}

	public static void printLinesOfGivenWord(String givenWord) {
		givenWord = givenWord.toLowerCase();
		if (wordFrequency.containsKey(givenWord)) {
			HashSet<Integer> lineNums = new HashSet<Integer>();
			ArrayList<Integer> lineNums2 = new ArrayList<Integer>();
			lineNums.addAll(wordFrequency.get(givenWord));
			lineNums2.addAll(lineNums);
			Collections.sort(lineNums2);
			System.out.println("Lines with context:");
			System.out.println("======================");
			for (int line : lineNums2) {
				System.out.println("(" + givenWord + ") " + (line + 1) + ": " + lines.get(line));
			}
			System.out.println("\n");
		}
	}

	public static void printRankOfGivenWord(String givenWord) {
		if (words.containsKey(givenWord)) {
			System.out.println("Word rank:");
			System.out.println("============");
			System.out.println("\"" + givenWord + "\" has the rank of " + (words.get(givenWord).getRank()) + " out of "
					+ words.keySet().size() + " words\n");
		}
	}

	public static void printAllRankings(int numberOfRanks) {
		System.out.println("Top " + numberOfRanks + " words ranked by frequency:");
		System.out.println("========================================");
		for (int i = 0; i < numberOfRanks && !(i >= words.keySet().size()); i++) {
			System.out.println((i + 1) + ": \"" + rankingList.get(i).getWord() + "\" appears "
					+ rankingList.get(i).getFrequency() + " times in the file");
		}
	}

	public static void printLocationsOfGivenWord(String givenWord) {
		givenWord = givenWord.toLowerCase();
		if (wordFrequency.containsKey(givenWord)) {
			System.out.println("Line numbers:");
			System.out.println("=============");
			System.out.print("\"" + givenWord + "\" was found on the following lines: ");

			HashSet<Integer> lineNums = new HashSet<Integer>();
			ArrayList<Integer> lineNums2 = new ArrayList<Integer>();
			lineNums.addAll(wordFrequency.get(givenWord));
			lineNums2.addAll(lineNums);
			Collections.sort(lineNums2);

			int iterations = 0;

			for (Integer curLineNumber : lineNums2) {
				if (iterations == lineNums2.size() - 1) {
					System.out.print(curLineNumber + 1 + "\n");
				} else {
					System.out.print(curLineNumber + 1 + ", ");
				}

				if (iterations % 12 == 0) {
					System.out.println();
				}

				iterations++;
			}
			System.out.println();
		} else {
			System.out.println("Line numbers:");
			System.out.println("=============");
			System.out.println("\"" + givenWord + "\" was not found in the file");
		}
	}

	public static void printFrequencyOfWord(String givenWord) {
		givenWord = givenWord.toLowerCase();
		System.out.println("Word Frequency:");
		System.out.println("===============");
		if (wordFrequency.containsKey(givenWord)) {
			System.out.println("\"" + givenWord + "\" was found " + wordFrequency.get(givenWord).size() + " times\n");
		} else {
			System.out.println("\"" + givenWord + "\" was was not found\n");
		}
	}

	public static void createRankings(ArrayList<Word> rankingList, HashMap<String, Word> words) {
		PriorityQueue<Word> queue = new PriorityQueue<Word>();

		for (String key : words.keySet()) {
			queue.add(words.get(key));
		}

		int rankCounter = 1;
		while (!queue.isEmpty()) {
			Word curWord = queue.poll();
			curWord.setRank(rankCounter++);
			rankingList.add(curWord);
		}

	}

	public static void generateWords(ArrayList<String> lines, HashMap<String, Word> words,
			HashMap<String, ArrayList<Integer>> wordFrequency) {
		int lineNumber = 0;
		for (String curLine : lines) {
			curLine = curLine.replaceAll("[^a-zA-Z ]", "").toLowerCase();
			String[] wordsArray = curLine.split(" ");

			for (String word : wordsArray) {
				if (wordFrequency.containsKey(word)) {
					ArrayList<Integer> temp = wordFrequency.get(word);
					temp.add(lineNumber);
					wordFrequency.put(word, temp);
					words.get(word).incrementFrequency();
				} else {
					ArrayList<Integer> temp = new ArrayList<Integer>();
					temp.add(lineNumber);
					wordFrequency.put(word, temp);
					words.put(word, new Word(word));
				}
			}
			lineNumber++;
		}
	}

	public static ArrayList<String> readDocument(File file) throws FileNotFoundException {

		Scanner scan = new Scanner(file);

		while (scan.hasNextLine()) {
			lines.add(scan.nextLine());
		}
		scan.close();

		return lines;
	}

	public static boolean parseCommandLine(String[] args) {

		if (args.length > 0) {
			if (args.length > 1) {
				if (args.length > 2) {
					for (int i = 2; i < args.length; i++) {
						switch (args[i]) {
						case "-f":
							frequency = true;
							break;

						case "-l":
							location = true;
							break;

						case "-c":
							context = true;
							break;
						case "-r":
							ranking = true;
							break;
						default:
							return false;
						}
					}
				}
				word = true;
			}
			return true;
		}
		GUI = true;
		return false;
	}

	public static void printHelp() {
		System.out.println("You have entered the incorrect arguements for this tool");
		System.out.println("Format: java (Filename) (targetWord) (options)");
		System.out.println("-f = frequency of the given word");
		System.out.println("-l = location (lineNumbers)");
		System.out.println("-c = context (prints each line that the word apears in)");
		System.out.println("-r = ranking (prints the frequency ranking that word is within the file)");
	}

}
