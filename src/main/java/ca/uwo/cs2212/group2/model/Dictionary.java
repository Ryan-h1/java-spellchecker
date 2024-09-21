package ca.uwo.cs2212.group2.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Represents and manages the dictionary object.
 *
 * @author Daniel Esemezie, Ryan Hecht, Shaylan Pratt
 *     <p>This class manages the dictionary class and includes methods to create a Dictionary object
 *     from a file, add word, remove word, check if word is in a dictionary, a toString method
 */
public class Dictionary {

  private final Hashtable<String, Boolean> dictionaryHashtable;

  // New data structures to group words by length and starting letter
  private final Map<Integer, List<String>> wordsByLength;
  private final Map<Character, List<String>> wordsByFirstChar;

  /**
   * Constructor
   *
   * @param filename the name of the file that has the dictionary words
   * @param isResource true if the dictionary file is a resource, false if it's a regular file
   */
  public Dictionary(String filename, boolean isResource) {
    dictionaryHashtable = new Hashtable<>();
    wordsByLength = new HashMap<>();
    wordsByFirstChar = new HashMap<>();

    if (isResource) {
      loadDictionaryFromResource(filename);
    } else {
      loadDictionaryFromFile(filename);
    }
  }

  /**
   * Loads the dictionary from a regular file.
   *
   * @param filename the name of a file to load into the dictionary
   */
  private void loadDictionaryFromFile(String filename) {
    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
      String line;
      while ((line = br.readLine()) != null) {
        addWord(line.trim());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Loads the dictionary from a resource file.
   *
   * @param filename the name of a resource file to load into the dictionary
   */
  private void loadDictionaryFromResource(String filename) {
    try (BufferedReader br =
        new BufferedReader(
            new InputStreamReader(
                Objects.requireNonNull(
                    this.getClass().getClassLoader().getResourceAsStream(filename))))) {
      String line;
      while ((line = br.readLine()) != null) {
        addWord(line.trim());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Adds a word to the dictionary.
   *
   * @param word the word you want to add to the dictionary
   */
  public void addWord(String word) {
    String lowerWord = word.toLowerCase();
    dictionaryHashtable.put(lowerWord, true); // Case-insensitive storage

    // Update wordsByLength
    int length = lowerWord.length();
    wordsByLength.computeIfAbsent(length, k -> new ArrayList<>()).add(lowerWord);

    // Update wordsByFirstChar
    if (!lowerWord.isEmpty()) {
      char firstChar = lowerWord.charAt(0);
      wordsByFirstChar.computeIfAbsent(firstChar, k -> new ArrayList<>()).add(lowerWord);
    }
  }

  /**
   * Removes a word from the dictionary.
   *
   * @param word the word you want to remove from the dictionary
   */
  public void removeWord(String word) {
    String lowerWord = word.toLowerCase();
    dictionaryHashtable.remove(lowerWord);

    // Update wordsByLength
    int length = lowerWord.length();
    List<String> wordsOfLength = wordsByLength.get(length);
    if (wordsOfLength != null) {
      wordsOfLength.remove(lowerWord);
      if (wordsOfLength.isEmpty()) {
        wordsByLength.remove(length);
      }
    }

    // Update wordsByFirstChar
    if (!lowerWord.isEmpty()) {
      char firstChar = lowerWord.charAt(0);
      List<String> wordsWithChar = wordsByFirstChar.get(firstChar);
      if (wordsWithChar != null) {
        wordsWithChar.remove(lowerWord);
        if (wordsWithChar.isEmpty()) {
          wordsByFirstChar.remove(firstChar);
        }
      }
    }
  }

  /**
   * Checks if a word is in the dictionary.
   *
   * @param word the word you want to check in the dictionary
   * @return true if the word is in the dictionary, false otherwise
   */
  public boolean checkWord(String word) {
    return dictionaryHashtable.containsKey(word.toLowerCase());
  }

  /**
   * Gets an enumeration of all words in the dictionary.
   *
   * @return an Enumeration of all words in the dictionary
   */
  public Enumeration<String> getKeys() {
    return dictionaryHashtable.keys();
  }

  /**
   * Returns a string representation of the dictionary contents.
   *
   * @return A string containing the dictionary contents, each word on a new line.
   */
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Dictionary Contents:\n");

    Enumeration<String> keys = dictionaryHashtable.keys();
    while (keys.hasMoreElements()) {
      String key = keys.nextElement();
      stringBuilder.append(key).append("\n");
    }

    return stringBuilder.toString();
  }

  // New methods to support efficient candidate retrieval in makeCorrections

  /**
   * Retrieves words whose lengths are within the specified range (inclusive).
   *
   * @param minLength minimum length of words (inclusive)
   * @param maxLength maximum length of words (inclusive)
   * @return a List of words within the specified length range
   */
  public List<String> getWordsByLength(int minLength, int maxLength) {
    List<String> result = new ArrayList<>();
    for (int length = minLength; length <= maxLength; length++) {
      List<String> words = wordsByLength.get(length);
      if (words != null) {
        result.addAll(words);
      }
    }
    return result;
  }

  /**
   * Retrieves words that start with the specified character.
   *
   * @param c the starting character
   * @return a List of words that start with the specified character
   */
  public List<String> getWordsByFirstChar(char c) {
    char lowerChar = Character.toLowerCase(c);
    List<String> words = wordsByFirstChar.get(lowerChar);
    if (words != null) {
      return new ArrayList<>(words);
    } else {
      return Collections.emptyList();
    }
  }
}
