/*
 * @author Guillermo Castaneda Echegaray
 * @version 1.0
 * @course CEN 3024C-27021 Software Development I
 * @instructor Dr. Lisa Macon
 * Text analyzer that reads a file and outputs statistics about that file. It
 * should output the word frequencies of all words in the file, sorted by the
 * most frequently used word. The output displays a set of pairs, each pair
 * containing a word and how many times it occurred in the file.
 */

package com.gce;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.StringTokenizer;

public class TextAnalyzer {
    // The target URL to parse
    public static String targetUrl;

    /**
     * Fetch the URL to parse
     *
     * @return The buffered URL content
     * @throws IOException
     */
    public static BufferedReader fetchUrlContent() throws IOException {
        return new BufferedReader(new InputStreamReader(new URL(targetUrl).openStream()));
    }

    /**
     * Create a hash map to store the words extracted from the URL and their frequency
     *
     * @param urlContent The buffered URL content
     * @return The wordCount HashMap
     * @throws IOException
     */
    public static HashMap<String, Integer> countWordFrequencies(BufferedReader urlContent) throws IOException {
        // temp string to store each line of the buffered inputUrl
        String inputLine;

        // HashMap stores words as keys and frequency as values
        HashMap<String, Integer> wordCount = new HashMap<String, Integer>();

        // Add words and their frequency to the hash map
        while ((inputLine = urlContent.readLine()) != null) {
            // convert the html formatted line to plain text
            String filteredInputLine = htmlToText(inputLine);

            // extract words from filteredInputLine using StringTokenizer
            StringTokenizer wordsInLine = new StringTokenizer(filteredInputLine);

            // add words and their frequencies to the wordCount HashMap
            while (wordsInLine.hasMoreTokens()) {
                String word = wordsInLine.nextToken();
                Integer currentWordFrequency = wordCount.get(word);
                int newWordFrequency = currentWordFrequency == null ? 1 : currentWordFrequency + 1;
                wordCount.put(word, newWordFrequency);
            }
        }

        // close the stream and release system resources.
        urlContent.close(); // Shout out to Prof. Jeho Park for drilling this into my head!

        return wordCount;
    }

    /**
     * Converts each line of the inputFile from html to plain text
     *
     * TODO:
     * There must be a better, more efficient way to do this.
     * Currently will not properly handle tag properties on
     * multiple lines without a "<" or ">"
     *
     * @param inputLine The string to convert from html to plain text
     * @return The plain text inputLine
     */
    private static String htmlToText(String inputLine) {
        return inputLine
                .toLowerCase()                   // convert to lower case
                .replaceAll(">'", ">")           // strip leading apostrophe after html tag
                .replaceAll("<.*?>", "")         // strip html tags
                .replaceAll("<.*", "")           // hack: strip unclosed html tags
                .replaceAll(".*?>", "")          // hack: strip unopened html tags
                .replaceAll(" '", " ")           // strip leading apostrophe after space
                .replaceAll("[!.,]'", "")           // strip apostrophe after punctuation
                .replaceAll("[|.?!,;:{}()]", "") // strip punctuation except apostrophe
                .replaceAll("--", " ")           // strip multiple double dashes found in the text
                .trim();                         // trim any remaining whitespace around each line
    }

    /**
     * Method to sort the wordCount HashMap by frequency values
     *
     * @param wordCount The HashMap with words and their frequencies
     * @return The sortedWordList
     */
    public static ArrayList<HashMap.Entry<String, Integer>> sortWordsByFrequency(HashMap<String, Integer> wordCount) {
        // create and populate an ArrayList with the words in the wordCount HashMap and their frequencies
        ArrayList<HashMap.Entry<String, Integer>> sortedWordList = new ArrayList<HashMap.Entry<String, Integer>>(wordCount.entrySet());

        // use Comparator to sort the ArrayList
        sortedWordList.sort(new Comparator<HashMap.Entry<String, Integer>>() {
            public int compare(HashMap.Entry<String, Integer> freq1, HashMap.Entry<String, Integer> freq2) {
                return freq2.getValue().compareTo(freq1.getValue());
            }
        });

        return sortedWordList;
    }

    /**
     * Displays the word frequencies table on the console
     *
     * @param sortedWordList The sortedWordList to display
     */
    public static void displayWordRankings(ArrayList<HashMap.Entry<String, Integer>> sortedWordList) {
        int rank = 0;

        for (HashMap.Entry<String, Integer> temp : sortedWordList) {
            rank++;

            // Only display the top 20 words
            if (rank == 20) {
                break;
            }
        }
    }
}

