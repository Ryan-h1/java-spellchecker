package ca.uwo.cs2212.group2.model;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import com.google.gson.Gson;

/**
 * Represents and manages various metrics associated with document processing.
 *
 * @author Ryan Hecht
 *     <p>This class tracks a variety of metrics including the number of characters, lines, words,
 *     errors, corrections, and user dictionary words in a document. It provides functionality to
 *     load these metrics from and save them to a file in JSON format.
 *     <p>Metrics include document-based metrics (characters, lines, words), spelling-based metrics
 *     (errors and corrections), and dictionary-based metrics (words in the user dictionary).
 */ 
public class Metrics {

  // Document metrics
  private int numberOfCharacters;
  private int numberOfLines;
  private int numberOfWords;

  // Spelling metrics
  private int totalNumberOfErrors;
  private int currentNumberOfErrors;
  private int totalNumberOfAcceptedCorrections;

  private int totalNumberOfManualCorrections;

  // Dictionary metrics
  private int numberOfWordsInUserDictionary;

  /** Constructor with default values of 0. */
  public Metrics() {
    this.numberOfCharacters = 0;
    this.numberOfLines = 0;
    this.numberOfWords = 0;
    this.totalNumberOfErrors = 0;
    this.currentNumberOfErrors = 0;
    this.totalNumberOfAcceptedCorrections = 0;
    this.totalNumberOfManualCorrections = 0;
    this.numberOfWordsInUserDictionary = 0;
  }

  /**
   * Constructor with known values for each parameter.
   *
   * @param numberOfCharacters the number of characters
   * @param numberOfLines the number of lines
   * @param numberOfWords the number of words
   * @param totalNumberOfErrors the total number of errors
   * @param currentNumberOfErrors the current number of errors
   * @param totalNumberOfAcceptedCorrections the total number of accepted corrections
   * @param totalNumberOfManualCorrections the total number of manual corrections
   * @param numberOfWordsInUserDictionary the number of words in the user dictionary
   */
  public Metrics(
      int numberOfCharacters,
      int numberOfLines,
      int numberOfWords,
      int totalNumberOfErrors,
      int currentNumberOfErrors,
      int totalNumberOfAcceptedCorrections,
      int totalNumberOfManualCorrections,
      int numberOfWordsInUserDictionary) {
    this.numberOfCharacters = numberOfCharacters;
    this.numberOfLines = numberOfLines;
    this.numberOfWords = numberOfWords;
    this.totalNumberOfErrors = totalNumberOfErrors;
    this.currentNumberOfErrors = currentNumberOfErrors;
    this.totalNumberOfAcceptedCorrections = totalNumberOfAcceptedCorrections;
    this.totalNumberOfManualCorrections = totalNumberOfManualCorrections;
    this.numberOfWordsInUserDictionary = numberOfWordsInUserDictionary;
  }

  /**
   * Factory method that creates a Metrics object from a JSON file.
   *
   * @param filename the name of the file to load the Metrics object from.
   * @return the Metrics object created from the JSON file.
   * @throws IOException
   */
  public static Metrics loadFromFile(String filename) throws IOException {
    Gson gson = new Gson();
    try (FileReader reader = new FileReader(filename)) {
      return gson.fromJson(reader, Metrics.class);
    }
  }

  /**
   * Saves the Metrics object to a file in JSON format.
   *
   * @param filename the name of the file to save the Metrics object to.
   * @throws IOException
   */
  public void saveToFile(String filename) throws IOException {
    Gson gson = new Gson();
    try (FileWriter writer = new FileWriter(filename)) {
      gson.toJson(this, writer);
    } catch (IOException e) {
      System.out.println(
          "Error saving Metrics to file named " + filename + " with error: " + e.getMessage());
      throw e;
    }
  }

  /**
   * @return a string representation of the Metrics object.
   */
  @Override
  public String toString() {
    return "Metrics{"
        + "numberOfCharacters="
        + numberOfCharacters
        + ", numberOfLines="
        + numberOfLines
        + ", numberOfWords="
        + numberOfWords
        + ", totalNumberOfErrors="
        + totalNumberOfErrors
        + ", currentNumberOfErrors="
        + currentNumberOfErrors
        + ", totalNumberOfAcceptedCorrections="
        + totalNumberOfAcceptedCorrections
        + ", totalNumberOfManualCorrections="
        + totalNumberOfManualCorrections
        + ", numberOfWordsInUserDictionary="
        + numberOfWordsInUserDictionary
        + '}';
  }

  public int getNumberOfCharacters() {
    return numberOfCharacters;
  }

  public void setNumberOfCharacters(int numberOfCharacters) {
    this.numberOfCharacters = numberOfCharacters;
  }

  public int getNumberOfLines() {
    return numberOfLines;
  }

  public void setNumberOfLines(int numberOfLines) {
    this.numberOfLines = numberOfLines;
  }

  public int getNumberOfWords() {
    return numberOfWords;
  }

  public void setNumberOfWords(int numberOfWords) {
    this.numberOfWords = numberOfWords;
  }

  public int getTotalNumberOfErrors() {
    return totalNumberOfErrors;
  }

  public void setTotalNumberOfErrors(int totalNumberOfErrors) {
    this.totalNumberOfErrors = totalNumberOfErrors;
  }

  public int getCurrentNumberOfErrors() {
    return currentNumberOfErrors;
  }

  public void setCurrentNumberOfErrors(int currentNumberOfErrors) {
    this.currentNumberOfErrors = currentNumberOfErrors;
  }

  public int getTotalNumberOfAcceptedCorrections() {
    return totalNumberOfAcceptedCorrections;
  }

  public void setTotalNumberOfAcceptedCorrections(int totalNumberOfAcceptedCorrections) {
    this.totalNumberOfAcceptedCorrections = totalNumberOfAcceptedCorrections;
  }

  public int getTotalNumberOfManualCorrections() {
    return totalNumberOfManualCorrections;
  }

  public void setTotalNumberOfManualCorrections(int totalNumberOfManualCorrections) {
    this.totalNumberOfManualCorrections = totalNumberOfManualCorrections;
  }

  public int getNumberOfWordsInUserDictionary() {
    return numberOfWordsInUserDictionary;
  }

  public void setNumberOfWordsInUserDictionary(int numberOfWordsInUserDictionary) {
    this.numberOfWordsInUserDictionary = numberOfWordsInUserDictionary;
  }
}
