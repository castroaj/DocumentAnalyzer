# DocumentAnalyzer

DocumentAnalyzer

Purpose:
	-Find the frequency of every word within the given file 
	-Find the location of every word within the given file
	-Allows for you to find the location of a particular word throughout the file
	-Allows for you to find the context of each occurance of the given word

Options:
	-f = frequency of the given word
	-l = location (line #s)
	-c = context (prints lines that it appears in)

Example format:    java DocumentAnalyzer (fileToRead) (word) [options] 

If no word, then we print the number of unique words and the frequency of every word in the document.
