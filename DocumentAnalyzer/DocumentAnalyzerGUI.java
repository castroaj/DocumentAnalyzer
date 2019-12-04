import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DocumentAnalyzerGUI {

	private final int screenWidth = 800;
	private final int screenHeight = 600;

	private final int unloadedTAFontSize = 22;
	private final int loadedTAFontSize = 12;

	private final int unloadedTAWidth = 27;
	private final int unloadedTAHeight = 14;

	private final int textAreaWidth = 50;
	private final int textAreaHeight = 26;

	private TextFile textFile;

	// Frame
	JFrame screen;

	// Main Panels
	JPanel mainPanel;
	JPanel topPanel;
	JPanel leftPanel;
	JPanel rightPanel;
	JPanel bottomPanel;
	JPanel modePanel;
	JPanel rankingPanel;
	JPanel rankingPanelTop;
	JPanel rankingPanelBottom;
	JPanel wordOptionsPanel;

	// Top Panel
	JPanel menuPanel;
	JPanel searchPanel;
	JPanel upperPanel;
	JPanel currentTextPanel;
	JMenuBar menu;
	JMenu file;
	JMenu help;
	JMenuItem loadText;
	JMenuItem wiki;
	JLabel searchLabel;
	JTextField searchField;
	JButton searchButton;
	JLabel currentTextLabel;
	JLabel currentText;

	// Left Panel
	JPanel frequencyOption;
	JPanel locationOption;
	JPanel contextOption;
	JPanel rankingOption;

	JLabel modeLabel;
	JComboBox<String> mode;

	JLabel rankerLabel;
	JSpinner rankNumberSelecter;
	SpinnerNumberModel rankerModel;
	JButton rankButton;

	JLabel frequencyLabel;
	JLabel locationLabel;
	JLabel contextLabel;
	JLabel rankingLabel;
	JCheckBox frequencyBox;
	JCheckBox locationBox;
	JCheckBox contextBox;
	JCheckBox rankingBox;

	// Right Panel
	JTextArea output;

	public DocumentAnalyzerGUI(String title) {
		screen = new JFrame();
		screen.setTitle(title);
		screen.setLocation(200, 200);
		screen.setSize(screenWidth, screenHeight);
		screen.setResizable(false);

		initalizePanels();
		initalizeComponents();
		makeGUI();

		createGUIStructure();

		screen.add(mainPanel);
		screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		screen.setVisible(true);
	}

	// GUI
	// ======================================================================

	private void initalizePanels() {
		// Main Panels
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.PAGE_AXIS));
		leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
		rightPanel = new JPanel();
		bottomPanel = new JPanel();
		modePanel = new JPanel();
		rankingPanel = new JPanel();
		rankingPanel.setLayout(new BoxLayout(rankingPanel, BoxLayout.PAGE_AXIS));
		wordOptionsPanel = new JPanel();
		wordOptionsPanel.setLayout(new BoxLayout(wordOptionsPanel, BoxLayout.PAGE_AXIS));
		upperPanel = new JPanel();
		upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.PAGE_AXIS));

		// Top Panels
		menuPanel = new JPanel(new BorderLayout());
		searchPanel = new JPanel();
		currentTextPanel = new JPanel();

		// Left Panel
		frequencyOption = new JPanel();
		locationOption = new JPanel();
		contextOption = new JPanel();
		rankingOption = new JPanel();
		rankingPanelTop = new JPanel();
		rankingPanelBottom = new JPanel();
	}

	private void initalizeComponents() {
		// Menu bar
		menu = new JMenuBar();
		file = new JMenu("File");
		help = new JMenu("Help");
		loadText = new JMenuItem("Load Text File");
		wiki = new JMenuItem("Open program Wiki");

		// Search bar
		searchLabel = new JLabel("Search for a word:");
		searchField = new JTextField(20);
		searchButton = new JButton("Search");

		// Current text
		currentText = new JLabel("No text loaded");
		currentTextLabel = new JLabel("Current .txt file: ");

		// Mode selector
		modeLabel = new JLabel("Select a mode for output: ");
		String[] modes = { "--", "Word Search", "Ranking" };
		mode = new JComboBox<String>(modes);
		mode.addActionListener(new DropdownActionListener());

		// Ranker
		rankerLabel = new JLabel("Rank this many words (Up to ? words):");
		rankerModel =  new SpinnerNumberModel();
		rankerModel.setValue(100);
		rankNumberSelecter = new JSpinner(rankerModel);
		rankButton = new JButton("Rank");

		// Options
		frequencyLabel = new JLabel("Frequency: ");
		locationLabel = new JLabel("Location: ");
		contextLabel = new JLabel("Context: ");
		rankingLabel = new JLabel("Ranking: ");
		frequencyBox = new JCheckBox();
		locationBox = new JCheckBox();
		contextBox = new JCheckBox();
		rankingBox = new JCheckBox();

		// Output
		output = new JTextArea(unloadedTAHeight, unloadedTAWidth);
	}

	private void makeGUI() {
		createTopPanel();
		createLeftPanel();
		createRightPanel();
	}

	private void createTopPanel() {
		createMenuPanel();
		createSearchPanel();
		createCurrentTextPanel();

		upperPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15),
				BorderFactory.createEtchedBorder()));
	}

	private void createMenuPanel() {
		file.setFont(new Font("Monospaced", Font.PLAIN, 12));
		help.setFont(new Font("Monospaced", Font.PLAIN, 12));
		loadText.setFont(new Font("Monospaced", Font.PLAIN, 12));
		loadText.addActionListener(new MenuSelectionListener());
		wiki.setFont(new Font("Monospaced", Font.PLAIN, 12));
		wiki.addActionListener(new MenuSelectionListener());

		file.add(loadText);
		help.add(wiki);

		menu.add(file);
		menu.add(help);

		menuPanel.add(menu, BorderLayout.WEST);
	}

	private void createSearchPanel() {
		searchLabel.setFont(new Font("Monospaced", Font.PLAIN, 20));
		searchButton.setFont(new Font("Monospaced", Font.PLAIN, 28));

		searchField.setFont(new Font("Monospaced", Font.PLAIN, 18));

		searchField.setEnabled(false);
		searchButton.setEnabled(false);
		searchButton.addActionListener(new ButtonActionListener());

		searchPanel.add(searchLabel);
		searchPanel.add(searchField);
		searchPanel.add(searchButton);
	}

	private void createCurrentTextPanel() {
		currentTextLabel.setFont(new Font("Monospaced", Font.PLAIN, 16));
		currentText.setFont(new Font("Monospaced", Font.BOLD, 16));

		currentTextPanel.add(currentTextLabel);
		currentTextPanel.add(currentText);
	}

	private void createLeftPanel() {
		createModePanel();
		createRankingPanel();
		createOptionsPanel();
	}

	private void createModePanel() {
		modeLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));

		mode.setEnabled(false);

		modePanel.add(modeLabel);
		modePanel.add(mode);
		modePanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "MODE SELECTION",
						TitledBorder.LEFT, TitledBorder.ABOVE_TOP, new Font("Monospaced", Font.BOLD, 20), Color.BLACK),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));
	}

	private void createRankingPanel() {
		rankerLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
		rankNumberSelecter.setEnabled(false);
		rankNumberSelecter.setValue(100);
		rankButton.setEnabled(false);
		rankButton.setFont(new Font("Monospaced", Font.PLAIN, 14));
		rankButton.addActionListener(new ButtonActionListener());
			
		rankingPanelTop.add(rankerLabel);
		rankingPanelBottom.add(rankNumberSelecter);
		rankingPanelBottom.add(rankButton);
		
		rankingPanel.add(rankingPanelTop);
		rankingPanel.add(rankingPanelBottom);
		
		rankingPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "RANKING MODE", TitledBorder.LEFT,
						TitledBorder.ABOVE_TOP, new Font("Monospaced", Font.BOLD, 20), Color.BLACK),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
	}

	private void createOptionsPanel() {
		frequencyLabel.setFont(new Font("Monospaced", Font.PLAIN, 18));
		locationLabel.setFont(new Font("Monospaced", Font.PLAIN, 18));
		contextLabel.setFont(new Font("Monospaced", Font.PLAIN, 18));
		rankingLabel.setFont(new Font("Monospaced", Font.PLAIN, 18));

		frequencyOption.add(frequencyLabel);
		frequencyOption.add(frequencyBox);
		locationOption.add(locationLabel);
		locationOption.add(locationBox);
		contextOption.add(contextLabel);
		contextOption.add(contextBox);
		rankingOption.add(rankingLabel);
		rankingOption.add(rankingBox);

		frequencyBox.setEnabled(false);
		locationBox.setEnabled(false);
		contextBox.setEnabled(false);
		rankingBox.setEnabled(false);

		wordOptionsPanel.add(frequencyOption);
		wordOptionsPanel.add(locationOption);
		wordOptionsPanel.add(contextOption);
		wordOptionsPanel.add(rankingOption);

		wordOptionsPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "WORD SEARCH OPTIONS", TitledBorder.LEFT,
						TitledBorder.ABOVE_TOP, new Font("Monospaced", Font.BOLD, 20), Color.BLACK),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
	}

	private void createRightPanel() {

		output.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
		output.setEditable(false);

		output.setFont(new Font("Monospaced", Font.PLAIN, unloadedTAFontSize));
		output.setLineWrap(true);
		output.setWrapStyleWord(true);
		output.setText("Please load a .txt file");

		rightPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "OUTPUT", TitledBorder.LEFT,
						TitledBorder.ABOVE_TOP, new Font("Monospaced", Font.BOLD, 20), Color.BLACK),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
	}

	private void createGUIStructure() {
		upperPanel.add(searchPanel);
		upperPanel.add(currentTextPanel);

		topPanel.add(menuPanel);
		topPanel.add(upperPanel);

		leftPanel.add(modePanel);
		leftPanel.add(wordOptionsPanel);
		leftPanel.add(rankingPanel);


		JScrollPane outputScrollPane = new JScrollPane(output);
		rightPanel.add(outputScrollPane);

		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(leftPanel, BorderLayout.WEST);
		mainPanel.add(rightPanel, BorderLayout.EAST);
		mainPanel.add(bottomPanel, BorderLayout.SOUTH);
	}

	// Action Listeners
	// ====================================================================

	private class MenuSelectionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			switch (e.getActionCommand()) {
			case "Load Text File":
				JFileChooser loadFile = new JFileChooser();
				loadFile.setFileFilter(new FileNameExtensionFilter("*.txt", "txt", "TEXT FILES"));
				int loadChoice = loadFile.showOpenDialog(new JFrame());
				if (loadChoice != JFileChooser.APPROVE_OPTION) {
					break;
				}

				File selectedFile = loadFile.getSelectedFile();
				textFile = new TextFile(selectedFile);

				try {
					textFile.setLines(DocumentAnalyzer.readDocument(textFile.getFile()));
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}

				DocumentAnalyzer.generateWords(textFile.getLines(), textFile.getWords(), textFile.getWordFrequency());

				output.setText("<--  Select a Mode");
				mode.setEnabled(true);
				rankerModel.setMaximum(textFile.getWords().size() - 1);
				rankerModel.setStepSize(5);
				if ((textFile.getWords().size() - 1) >= 1000)
				{
					rankerModel.setValue(1000);
				}
				else
				{
					rankerModel.setValue(textFile.getWords().size() - 1);
				}
				rankerLabel.setText("Rank this many words (Up to " + (textFile.getWords().size() - 1) + " words):");
				String[] path = selectedFile.getPath().split("/");
				currentText.setText(path[path.length - 1]);
				output.setFont(new Font("Monospaced", Font.PLAIN, unloadedTAFontSize));
				output.setSize(unloadedTAWidth, unloadedTAWidth);
				break;
			}

		}
	}

	private class ButtonActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			switch (e.getActionCommand()) {
			case "Search":
				determineOutput(searchField.getText());
				break;
			case "Rank":
				DocumentAnalyzer.createRankings(textFile.getRankingList(), textFile.getWords());
				output.setFont(new Font("Monospaced", Font.PLAIN, loadedTAFontSize));
				output.setSize(textAreaWidth, textAreaHeight);
				output.setText(printAllRankings((Integer)rankerModel.getValue()));
				break;
			}
		}

	}

	private class DropdownActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JComboBox<String> box = (JComboBox<String>) e.getSource();
			switch (box.getSelectedIndex()) {
			case 0:
				output.setText("<--  Select a Mode");
				output.setFont(new Font("Monospaced", Font.PLAIN, unloadedTAFontSize));
				output.setSize(unloadedTAWidth, unloadedTAWidth);
				searchField.setEnabled(false);
				searchButton.setEnabled(false);
				frequencyBox.setEnabled(false);
				frequencyBox.setSelected(false);
				locationBox.setEnabled(false);
				contextBox.setEnabled(false);
				rankingBox.setEnabled(false);

				rankNumberSelecter.setEnabled(false);
				rankButton.setEnabled(false);
			case 1:
				output.setText("^  Ready for Word Search\n|\n|");
				output.setFont(new Font("Monospaced", Font.PLAIN, unloadedTAFontSize));
				output.setSize(unloadedTAWidth, unloadedTAWidth);
				searchField.setEnabled(true);
				searchButton.setEnabled(true);
				frequencyBox.setEnabled(true);
				locationBox.setEnabled(true);
				contextBox.setEnabled(true);
				rankingBox.setEnabled(true);

				rankNumberSelecter.setEnabled(false);
				rankButton.setEnabled(false);
				break;
			case 2:
				output.setText("Ready for Ranking");
				output.setFont(new Font("Monospaced", Font.PLAIN, unloadedTAFontSize));
				output.setSize(unloadedTAWidth, unloadedTAWidth);
				rankNumberSelecter.setEnabled(true);
				rankButton.setEnabled(true);

				searchField.setEnabled(false);
				searchButton.setEnabled(false);
				frequencyBox.setEnabled(false);
				frequencyBox.setSelected(false);
				locationBox.setEnabled(false);
				contextBox.setEnabled(false);
				rankingBox.setEnabled(false);
				break;
			}
		}
	}

	// OUTPUT
	// ==============================================================

	private void determineOutput(String givenWord) {
		String finalOutputString = "";
		
		// -f option
		if (frequencyBox.isSelected()) {
			finalOutputString += printFrequencyOfWord(givenWord);
		}

		// -l option
		if (locationBox.isSelected()) {
			finalOutputString += printLocationsOfGivenWord(givenWord);

		}

		// -c option
		if (contextBox.isSelected()) {
			finalOutputString += printLinesOfGivenWord(givenWord);

		}

		// -r option
		if (rankingBox.isSelected()) {
			DocumentAnalyzer.createRankings(textFile.getRankingList(), textFile.getWords());
			finalOutputString += printRankOfGivenWord(givenWord);
		}

		// All options are not used
		if (!frequencyBox.isSelected() && !locationBox.isSelected() && !contextBox.isSelected()
				&& !rankingBox.isSelected()) {
			DocumentAnalyzer.createRankings(textFile.getRankingList(), textFile.getWords());
			finalOutputString += printFrequencyOfWord(givenWord);
			finalOutputString += printRankOfGivenWord(givenWord);
			finalOutputString += printLocationsOfGivenWord(givenWord);
			finalOutputString += printLinesOfGivenWord(givenWord);
		}
		output.setText(finalOutputString);
		output.setFont(new Font("Monospaced", Font.PLAIN, loadedTAFontSize));
	}

	private String printFrequencyOfWord(String givenWord) {
		givenWord = givenWord.toLowerCase();
		String outputString = "";
		outputString += ("Word Frequency:\n");
		outputString += ("===============\n");
		if (textFile.getWordFrequency().containsKey(givenWord)) {
			outputString += ("\"" + givenWord + "\" was found " + textFile.getWordFrequency().get(givenWord).size()
					+ " times\n\n");
		} else {
			outputString += ("\"" + givenWord + "\" was was not found\n\n");
		}
		output.setFont(new Font("Monospaced", Font.PLAIN, loadedTAFontSize));
		return outputString;
	}

	private String printLocationsOfGivenWord(String givenWord) {
		String outputString = "";
		givenWord = givenWord.toLowerCase();
		if (textFile.getWordFrequency().containsKey(givenWord)) {
			outputString += ("Line numbers:\n");
			outputString += ("=============\n");
			outputString += ("\"" + givenWord + "\" was found on the following lines: \n");

			HashSet<Integer> lineNums = new HashSet<Integer>();
			ArrayList<Integer> lineNums2 = new ArrayList<Integer>();
			lineNums.addAll(textFile.getWordFrequency().get(givenWord));
			lineNums2.addAll(lineNums);
			Collections.sort(lineNums2);

			int iterations = 0;

			for (Integer curLineNumber : lineNums2) {
				if (iterations == lineNums2.size() - 1) {
					outputString += (curLineNumber + 1 + "\n");
				} else {
					outputString += (curLineNumber + 1 + ", ");
				}

				if (iterations % 12 == 0) {
					outputString += "\n";
				}

				iterations++;
			}
			outputString += "\n";
		} else {
			outputString += ("Line numbers:\n");
			outputString += ("=============\n");
			outputString += ("\"" + givenWord + "\" was not found in the file\n");
		}
		return outputString;
	}
	
	private String printLinesOfGivenWord(String givenWord) {
		String outputString = "";
		givenWord = givenWord.toLowerCase();
		if (textFile.getWordFrequency().containsKey(givenWord)) {
			HashSet<Integer> lineNums = new HashSet<Integer>();
			ArrayList<Integer> lineNums2 = new ArrayList<Integer>();
			lineNums.addAll(textFile.getWordFrequency().get(givenWord));
			lineNums2.addAll(lineNums);
			Collections.sort(lineNums2);
			outputString += ("Lines with context:\n");
			outputString += ("======================\n");
			for (int line : lineNums2) {
				outputString += ("(" + givenWord + ") " + (line + 1) + ": " + textFile.getLines().get(line) + "\n");
			}
			outputString += ("\n\n");
		}
		return outputString;
	}
	
	private String printRankOfGivenWord(String givenWord) {
		String outputString = "";
		if (textFile.getWords().containsKey(givenWord)) {
			outputString += ("Word rank:\n");
			outputString += ("============\n");
			outputString += ("\"" + givenWord + "\" has the rank of " + (textFile.getWords().get(givenWord).getRank()) + " out of "
					+ textFile.getWords().keySet().size() + " words\n\n");
		}
		return outputString;
	}
	
	private String printAllRankings(int numberOfRanks) {
		String outputString = "";
		outputString += ("Top " + numberOfRanks + " words ranked by frequency:\n");
		outputString += ("========================================\n");
		for (int i = 0; i < numberOfRanks && !(i >= textFile.getWords().keySet().size()); i++) {
			outputString += ((i + 1) + ": \"" + textFile.getRankingList().get(i).getWord() + "\" appears "
					+ textFile.getRankingList().get(i).getFrequency() + " times in the file\n");
		}
		return outputString;
	}

}
