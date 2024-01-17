package ca.uwo.cs2212.group2.service;

import ca.uwo.cs2212.group2.model.Word;
import ca.uwo.cs2212.group2.model.WordIndex;

import java.util.HashSet;
import java.util.Set;

/**
 * Singleton service that keeps track of words that should be ignored once.
 *
 * @author Ryan Hecht
 */
public class WordsToIgnoreOnceService {
  private static WordsToIgnoreOnceService instance;
  private final Set<WordIndex> wordsToIgnoreOnce;

  /** Constructor for the WordsToIgnoreOnceService. */
  private WordsToIgnoreOnceService() {
    wordsToIgnoreOnce = new HashSet<>();
  }

  /**
   * Lazily loads the singleton instance of the WordsToIgnoreOnceService.
   *
   * @return The instance of the WordsToIgnoreOnceService
   */
  public static WordsToIgnoreOnceService getInstance() {
    if (instance == null) {
      instance = new WordsToIgnoreOnceService();
    }
    return instance;
  }

  /**
   * Adds a word to the list of words to ignore once.
   *
   * @param word The word to ignore once
   */
  public void ignoreWordOnce(Word word) {
    wordsToIgnoreOnce.add(new WordIndex(word.getContent(), word.getPosition()));
  }

  /**
   * Checks if a word should be ignored once.
   *
   * @param word The word to check
   * @return True if the word should be ignored once
   */
  public boolean shouldIgnoreWord(Word word) {
    return wordsToIgnoreOnce.contains(new WordIndex(word.getContent(), word.getPosition()));
  }

  /** Clears the list of words to ignore once. */
  public void clearIgnoredWords() {
    wordsToIgnoreOnce.clear();
  }
}
