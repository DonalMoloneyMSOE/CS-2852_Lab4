/*
 *CS 2852 - 011
 *Fall 2017
 *Lab 4 - Dictionary
 *Name: Donal Moloney
 *Created: 9/29/2017
 */
package Moloneyda;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Scanner;

/**
 * This class creates a dictionary
 */
public class Dictionary {
    private Collection<String> dictionary;

    /**
     * This method is the constructor for the dictionary class
     *
     * @param dictionary a collection that implements the Collections interface methods
     */
    public Dictionary(Collection<String> dictionary) {
        if (dictionary == null) {
            throw new NullPointerException();
        } else {
            if (dictionary.size() != 0) {
                for (int i = 0; i < dictionary.size(); i++) {
                    dictionary.remove(i);
                }
            } else {
                this.dictionary = dictionary;
                dictionary.clear();
            }
        }
    }

    /**
     * This method loads the textfile into the dictionary collection
     *
     * @param userDictionary the path of the textfile
     * @return long the time it takes to load the time in nanoSeconds
     */
    public long load(String userDictionary) {
        long sumNanoTime = 0;
        Scanner lineOfWords;
        String aWord;
        BufferedReader bufferedReader;
        FileReader fileReader;
        try {
            fileReader = new FileReader(userDictionary);
            bufferedReader = new BufferedReader(fileReader);
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                lineOfWords = new Scanner(currentLine);
                while (lineOfWords.hasNext()) {
                    aWord = lineOfWords.next();
                    long nanoStart = System.nanoTime();
                    if (!dictionary.contains(aWord)) {
                        dictionary.add(aWord);
                    }
                    long nanoEnd = System.nanoTime();
                    sumNanoTime += nanoEnd - nanoStart;
                }
            }
            return sumNanoTime;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred reading the file!", "Error!",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        return 0;
    }

    /*
     *Checks to see if the dictionary collection contains the string target
     * @param target the string you are looking for
     * @return true if dictionary contains the target
     */
    public boolean contains(String target) {
        boolean contains = false;
        if (dictionary.contains(target)) {
            contains = true;
        }
        return contains;
    }
}
