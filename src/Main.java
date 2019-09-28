/*
 *CS 2852 - 011
 *Fall 2017
 *Lab 4 - Dictionary
 *Name: Donal Moloney
 *Created: 9/29/2017
 */
package Moloneyda;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * This class calculates and displays tests comparing the number of words contained in a document that are not
 * in a dictionary when selecting different document and dictionary combinations
 */
public class Main {
    Dictionary dictionary;
    private String userDictionary;
    private String userDocument;
    private String formatedUserDictionary;
    private String formatedUserDocument;
    private String collectionType;
    private String[] buttons = {"A short story", "List of 100k words", "King James Bible", "Cancel"};
    private ArrayList<String> documentCollection = new ArrayList();
    private long timeToAdd;
    private int notInDictionary;
    private long timeToSpellCheck = 0;
    private int choiceDictionary;
    private int choiceDocument;
    private static final int NANO_TO_SECONDS = 1000000000;

    /**
     * This method creates calls a new constructor to run the program
     */
    public static void main(String args[]) {
        new Main();
    }

    /**
     * This is the Main classes's constructor method it runs the majority of the program when its instantiated
     */
    public Main() {
        String temp;
        printHeader();
        dictionaryChooser();
        documentChooser();
        collectionType = "ArrayList";
        dictionary = new Dictionary(new ArrayList());
        populateDocument();
        run();
        collectionType = "LinkedList";
        dictionary = new Dictionary(new LinkedList<>());
        run();
        collectionType = "SortedArrayList";
        dictionary = new Dictionary(new SortedArrayList<>());
        run();
        temp = userDictionary;
        userDictionary = userDocument;
        userDocument = temp;
        collectionType = "ArrayList";
        dictionary = new Dictionary(new ArrayList());
        run();
        collectionType = "LinkedList";
        dictionary = new Dictionary(new LinkedList<>());
        run();
        collectionType = "SortedArrayList";
        dictionary = new Dictionary(new SortedArrayList<>());
        run();
    }

    /*
     * This method runs a shared majority of each test
     *
     * @return void
     */
    private void run() {
        documentCollection.clear();
        populateDocument();
        timeToAdd = populateDictionary();
        timeToSpellCheck = numberNotInDictionary();
        formatedUserDocument = formatUserDocument(userDocument);
        formatedUserDictionary = formatUserDictionary(userDictionary);
        printValues(notInDictionary, timeToAdd, timeToSpellCheck);
    }

    /**
     * Prints the formatted data values
     * @return void
     */
    private void printValues(int notInDictionary, long timeToAdd, long timeToSpellCheck) {
        float secondsToAdd = (float) (((double)timeToAdd/ NANO_TO_SECONDS)%60);
        long minutesToAdd= (timeToAdd/NANO_TO_SECONDS)/60;
        long minutesToSpellCheck = (timeToSpellCheck/NANO_TO_SECONDS)/60;
        float secondsToSpellCheck = (float) (((double)timeToSpellCheck/ NANO_TO_SECONDS)%60);
        System.out.format("|\t" + formatedUserDictionary + "|\t" + formatedUserDocument + "\t|\t"+collectionType+"\t"+
                "|%20d  | "+minutesToAdd+"mins "+ "%10.10f seconds| "+minutesToSpellCheck+" mins %10.10f seconds|",
                notInDictionary, secondsToAdd, secondsToSpellCheck);
        System.out.println("\n");
    }

    /**
     * Formats the the string userDictionary to remove the path portion of it
     * @return a formated string that has the path portion of the string removed
     */
    private String formatUserDictionary(String userDictionary) {
        return userDictionary.substring(4);
    }

    /**
     * Formats the the string userDocument to remove the path portion of it
     *
     * @return a formated string that has the path portion of the string removed
     */
    private String formatUserDocument(String userDocument) {
        return userDocument.substring(4);
    }

    /**
     * Prints the header detailing the significance of each number
     *
     * @return void
     */
    private void printHeader() {
        System.out.format("  |Dictionary  |  Document  |   Collection   |  Number Not in Dictionary  " +
                "|  Time to add  |  Time to spell-check  |");
        System.out.println("\n");
    }

    /**
     * Obtains the time it takes to populate the dictionary
     *
     * @return nanoseconds it took to populate the dictionary
     */
    private long populateDictionary() {
        long timeToAdd = dictionary.load(userDictionary);
        return timeToAdd;
    }

    /**
     * Allows the user to choose the dictionary he would like to use for the program
     *
     * @return void
     */
    private void dictionaryChooser() {
        choiceDictionary = JOptionPane.showOptionDialog(null, "Choose a file to be your dictionary", "Dictionary",
                JOptionPane.WARNING_MESSAGE, 0, null, buttons, buttons[2]);
        if (choiceDictionary == 0) {
            userDictionary = "src/story.txt";
        } else if (choiceDictionary == 1) {
            userDictionary = "src/words.txt";
        } else if (choiceDictionary == 2) {
            userDictionary = "src/kjv10.txt";
        } else if (choiceDictionary == 3) {
            throw new NullPointerException("A dictionary must be chosen");
        }
    }

    /**
     * Allows the user to choose the document to use in the program
     *
     * @return void
     */
    private void documentChooser() {
        choiceDocument = JOptionPane.showOptionDialog(null, "Choose a DIFFERENT file to be your document ?",
                "Document Chooser", JOptionPane.INFORMATION_MESSAGE, 0, null, buttons, buttons[2]);
        if (choiceDictionary == choiceDocument) {
            throw new InputMismatchException(" The dictionary and document must be different files");
        }
        if (choiceDocument == 0) {
            userDocument = "src/story.txt";
        } else if (choiceDocument == 1) {
            userDocument = "src/words.txt";
        } else if (choiceDocument == 2) {
            userDocument = "src/kjv10.txt";
        } else if (choiceDocument == 3) {
            throw new NullPointerException("A dictionary must be chosen");
        }
    }

    /**
     * This method obtains the number of dictionary words not in the documents collection
     *
     * @return long the number of words in the document not contained by dictionary
     */
    private long numberNotInDictionary() {
        long startTimeToSpellCheck = System.nanoTime();
        notInDictionary = 0;
        Iterator<String> iterator = documentCollection.iterator();
        while (iterator.hasNext()) {
            String word = iterator.next();
            if (!dictionary.contains(word)) {
                notInDictionary++;
            }
        }
        long endTimeToSpellCheck = System.nanoTime();
        return endTimeToSpellCheck - startTimeToSpellCheck;
    }

    /**
     * Populates the document collection with the user document choice
     *
     * @return void
     */
    private void populateDocument() {
        try {
            File file = new File(userDocument);
            Scanner fileData = new Scanner(file);
            String aLine;
            String newWord;
            while (fileData.hasNextLine()) {
                aLine = fileData.nextLine();
                String[] words = aLine.split(" ");
                for (int i = 0; i < words.length; i++) {
                    newWord = words[i];
                    if (newWord.contains(".")) {
                        newWord.replace(".", "");
                    } else if (newWord.contains(",")) {
                        newWord.replace(",", "");
                    }
                    documentCollection.add(newWord);
                }
            }
            fileData.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred populating the document!", "Error!",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
