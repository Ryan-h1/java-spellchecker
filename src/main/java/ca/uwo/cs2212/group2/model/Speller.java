package ca.uwo.cs2212.group2.model;

import ca.uwo.cs2212.group2.service.SessionSettingsService;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.io.BufferedWriter;
import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Speller {
  private static final int MAX_SUGGESTIONS = 4; // Maximum number of suggestions to generate
  private static final int LENGTH_DIFFERENCE = 3; // Acceptable length difference for candidate words
  private static final int MAX_LEV_DAM_CACHE_SIZE = 10000;

  private static final Map<String, Integer> levDamCache =
      Collections.synchronizedMap(new LRUCache<>(MAX_LEV_DAM_CACHE_SIZE));

  private static Speller instance = null;
  private final TextProcessor usertext;
  private final Dictionary dict;
  private List<Word> incorrectWords;
  private List<Word> previousIncorrectWords;
  private List<Word> allWords;
  private List<Word> uncheckedWords;
  // STATISTICALS
  private final List<Word> midCapped = new ArrayList<Word>();
  private final List<Word> misCapped = new ArrayList<Word>();
  private final List<Word> doubleWords = new ArrayList<Word>();

  // userdict
  private Dictionary userdict;

  /** Singleton instance of Speller */
  private Speller() {
    allWords = new ArrayList<Word>();
    incorrectWords = new ArrayList<Word>();
    usertext = new TextProcessor();
    dict = loadDict();
  }

  /**
   * Public static method that lazily loads speller
   *
   * @return Singleton instance of Speller
   */
  public static Speller getInstance() {
    if (instance == null) {
      instance = new Speller();
    }
    return instance;
  }

  // return the user dict
  public Dictionary getUserDict() {
    return this.userdict;
  }

  public List<Word> getAllwords() {
    return this.allWords;
  }

  public List<Word> getWrongWords() {
    return this.incorrectWords;
  }

  public Dictionary getDict() {
    return this.dict;
  }

  public int[] getStats() {

    if (this.usertext == null) {
      System.out.println("Usertext object not found");
    }
    // linecount, wordcount, charcountnospace, incorrectwords
    int[] result = new int[6];
    result[0] = this.usertext.getLineCount(); // lines
    result[1] = this.usertext.getWordCount(); // words
    result[2] = (int) this.usertext.getCharCountNoSpace(); // chars
    result[3] = misCapped.size(); // number of miscapitalizations
    result[4] = doubleWords.size(); // number of double words
    result[5] = midCapped.size(); // Middle letter capped

    return result;
  }

  public void spellcheck(String inText) {
    // 1. Create textproccessor object out of given text
    try {
      if (SessionSettingsService.getInstance().isHTMLModeTurnedOn()) {
        System.out.println("HELLO");
        usertext.parseHtmlString(inText);
      } else {
        usertext.parseString(inText);
      }
    } catch (Exception x) {
      System.out.printf("User text file %s not found%n", inText);
    }

    // 2. Reset word arrays
    this.resetWrongWords();
    this.resetAllWords();

    // 3. Iterate through all words of textproccessor, calling spellcheck on the word
    this.allWords = usertext.getWords();
    this.uncheckedWords = new ArrayList<>(this.allWords);

    this.loadPreviousIncorrectWordCache();

    // mark doubles
    doubleWords(this.uncheckedWords);

    //
    for (Word w : this.uncheckedWords) {
      // lowercase the word before checking it
      String wc = w.getContent().toLowerCase();
      // Either it is not in the dictionary, OR it is midcapitalized, OR it at start of sentence
      // OR
      // is a double word
      if ((!dict.checkWord(wc)) || isMidcapped(w.getContent()) || isMiscapped(w) || w.getDouble()) {
        // All words considered incorrect and will be spellchecked
        w.setCorrect(false);
        makeCorrections(w, dict);

        // If it is a sentence starter, add it to miscapped
        if (isMiscapped(w)) {
          misCapped.add(w);
        }
        // If it has capitals in the middle, add it to midcapped
        if (isMidcapped(w.getContent())) {
          midCapped.add(w);
        }
        // If it is a double word, add it to doubleWords
        if (w.getDouble()) {
          doubleWords.add(w);
          // Add a blank correctionsuggestion representing deletion
          CorrectionSuggestions blank = new CorrectionSuggestions("", 0);
          w.addOption(blank);
        }

        // add it to incorrectWords
        this.incorrectWords.add(w);
      }
    }
  }

  /**
   * Finds current incorrect words and adds suggestions to them based off the previously cached
   * incorrect words.
   *
   * <p>Why not just load the previous incorrect words into the new incorrect words? First off, they
   * may have been deleted, second, by matching words by their contents we can match words agnostic
   * of their position and use the same suggestions for duplicate words as opposed to having to
   * compute the same suggestions multiple times.
   */
  private void loadPreviousIncorrectWordCache() {
    ConcurrentMap<String, Word> incorrectWordMap = new ConcurrentHashMap<>();

    for (Word incorrectWord : this.previousIncorrectWords) {
      // Only put words in that are not doubles, if they're doubles they'll be added later
      if (!incorrectWord.getDouble()) {
        incorrectWordMap.put(incorrectWord.getContent(), incorrectWord);
      }
    }

    for (Word word : this.allWords) {
      incorrectWordMap.computeIfPresent(
          word.getContent(),
          (key, cachedIncorrectWord) -> {
            word.setOptions(cachedIncorrectWord.getOptions());
            word.setCorrect(cachedIncorrectWord.getCorrect());

            this.incorrectWords.add(word);
            uncheckedWords.remove(word);
            return cachedIncorrectWord; // Return the modified value
          });
    }
  }

  /** Resets the list of incorrect words by moving the current list to the previous list. */
  private void resetWrongWords() {
    this.previousIncorrectWords = this.incorrectWords;
    this.incorrectWords = new ArrayList<Word>();
  }

  private void resetAllWords() {
    this.allWords = new ArrayList<>();
  }

  // Checks if a word has a starting capital letter where it shouldn't
  private static boolean isMiscapped(Word inword) {
    String w = inword.getContent();
    boolean isBeginning = inword.isBeginning();
    // Just ignore it if it is "I"
    if (w.equals("I")) {
      return false;
    }
    // either it is a starting word with no capitalization, or it is a non starter with a
    // capitalization
    if (!isBeginning && Character.isUpperCase(w.charAt(0))
        || isBeginning && !(Character.isUpperCase(w.charAt(0)))) {
      return true;
    }
    return false;
  }

  /**
   * @param inlist list of words
   */
  private static void doubleWords(List<Word> inlist) {
    String prev = "";
    for (Word w : inlist) {
      // if this is a duplicate word
      if (w.getContent().equals(prev)) {
        // tag as double
        w.setDouble(true);
      }
      prev = w.getContent();
    }
  }

  /**
   * @param word word to be checked for capital letters in the middle
   * @return true if word has capital letters in the middle of it
   */
  private static boolean isMidcapped(String word) {
    boolean out = false;
    for (int i = 1; i < word.length(); i++) {
      char c = word.charAt(i);
      if (Character.isUpperCase(c)) {
        out = true;
      }
    }
    return out;
  }

  /**
   * @param inText the name of the input text
   * @return a TextProccessor of that text
   */
  private static TextProcessor process(String inText) {
    // Getting the path for the current directory
    String directoryPath = System.getProperty("user.dir");

    // Path objects for directory and file
    Path directory = Paths.get(directoryPath);
    Path filepath = directory.resolve(inText);
    try {
      return new TextProcessor(filepath.toString());
    } catch (FileNotFoundException x) {
      System.out.printf("Input text %s not found%n", inText);
      x.printStackTrace();
    } catch (Exception s) {
      s.printStackTrace();
    }
    return null;
  }

  /**
   * Generates correction suggestions for an incorrect word.
   *
   * @param w    the word to generate corrections for
   * @param dict the dictionary to compare against
   */
  private static void makeCorrections(Word w, Dictionary dict) {
    String wordContent = w.getContent();
    int wordLength = wordContent.length();
    char firstChar = Character.toLowerCase(wordContent.charAt(0));

    // Calculate acceptable length range
    int minLength = Math.max(1, wordLength - LENGTH_DIFFERENCE);
    int maxLength = wordLength + LENGTH_DIFFERENCE;

    // Get candidate words by length
    List<String> candidatesByLength = dict.getWordsByLength(minLength, maxLength);

    // Further filter candidates by starting letter
    List<String> filteredCandidates = new ArrayList<>();
    for (String candidate : candidatesByLength) {
      if (!candidate.isEmpty() && candidate.charAt(0) == firstChar) {
        filteredCandidates.add(candidate);
      }
    }

    // PriorityQueue to store correction suggestions, ordered by distance
    PriorityQueue<CorrectionSuggestions> options = new PriorityQueue<>();

    for (String dictWord : filteredCandidates) {
      int distance = LevDam(wordContent, dictWord);  // Use memoized LevDam method
      CorrectionSuggestions suggestion = new CorrectionSuggestions(dictWord, distance);

      // Capitalize suggestions if the word is at the beginning of a sentence
      if (w.isBeginning()) {
        suggestion.setWord(capitalize(suggestion.getWord()));
      }

      options.add(suggestion);
    }

    // Extract the top MAX_SUGGESTIONS from the priority queue
    System.out.printf("CORRECTION OPTIONS FOR %s:%n", w.getContent());
    for (int i = 0; i < MAX_SUGGESTIONS && !options.isEmpty(); i++) {
      CorrectionSuggestions cj = options.poll();
      w.addOption(cj);
      // Sanity Check
      System.out.println(cj.getWord());
    }
  }

  /**
   * Capitalizes the first letter of the given string.
   *
   * @param s the string to capitalize
   * @return the capitalized string
   */
  private static String capitalize(String s) {
    if (s == null || s.isEmpty()) {
      return s;
    }
    return s.substring(0, 1).toUpperCase() + s.substring(1);
  }

  /**
   * @param s1 origin string
   * @param s2 string to be compared to
   * @return Levenstein Damerau distance between two strings
   */
  private static int LevDam(String s1, String s2) {
    // Creds to https://github.com/tdebatty/java-string-similarity#damerau-levenshtein
    // Checking for input validity
    if (s1 == null) {
      throw new NullPointerException("s1 must not be null");
    }

    if (s2 == null) {
      throw new NullPointerException("s2 must not be null");
    }

    // No calculation required if it's the same word
    if (s1.equals(s2)) {
      return 0;
    }

    String key = s1.compareTo(s2) <= 0 ? s1 + "|" + s2 : s2 + "|" + s1;

    if (levDamCache.containsKey(key)) {
      return levDamCache.get(key);
    }

    // Calculate max possible distance
    int inf = s1.length() + s2.length();

    // Create and initialize the character array indices (The 2D array)
    ConcurrentMap<Character, Integer> da = new ConcurrentHashMap<>();

    for (int d = 0; d < s1.length(); d++) {
      da.put(s1.charAt(d), 0);
    }

    for (int d = 0; d < s2.length(); d++) {
      da.put(s2.charAt(d), 0);
    }

    // A second matrix
    // Create the distance matrix H[0 .. s1.length+1][0 .. s2.length+1]
    int[][] h = new int[s1.length() + 2][s2.length() + 2];

    // initialize the left and top edges of H
    for (int i = 0; i <= s1.length(); i++) {
      h[i + 1][0] = inf;
      h[i + 1][1] = i;
    }

    for (int j = 0; j <= s2.length(); j++) {
      h[0][j + 1] = inf;
      h[1][j + 1] = j;
    }

    // fill in the distance matrix H
    // look at each character in s1
    for (int i = 1; i <= s1.length(); i++) {
      int db = 0;

      // look at each character in b
      for (int j = 1; j <= s2.length(); j++) {
        int i1 = da.get(s2.charAt(j - 1));
        int j1 = db;

        int cost = 1;
        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
          cost = 0;
          db = j;
        }

        h[i + 1][j + 1] =
            mymin(
                h[i][j] + cost, // substituition
                h[i + 1][j] + 1, // insertion
                h[i][j + 1] + 1, // deletion
                h[i1][j1] + (i - i1 - 1) + 1 + (j - j1 - 1));
      }

      da.put(s1.charAt(i - 1), i);
    }

    int distance = h[s1.length() + 1][s2.length() + 1];
    levDamCache.put(key, distance);

    return distance;
  }

  private static int mymin(int i, int j, int k, int l) {
    return Math.min(Math.min(i, j), Math.min(k, l));
  }

  /**
   * @param inName
   * @return true if the file exists in current directory
   */
  private static boolean fileExists(String inName) {
    // Getting the path for the current directory
    String directoryPath = System.getProperty("user.dir");

    // Path objects for directory and file
    Path directory = Paths.get(directoryPath);
    Path filepath = directory.resolve(inName);

    // return
    return Files.exists(filepath);
  }

  /**
   * @param origin The origin dictionary
   * @param destination The destination dictionary
   */
  // Transfer all words from origin to destination
  private static void transferWords(Dictionary origin, Dictionary destination) {
    Enumeration<String> originKeys = origin.getKeys();
    while (originKeys.hasMoreElements()) {
      String word = originKeys.nextElement();
      destination.addWord(word);
    }
  }

  /**
   * @return merged dictionary if the dictionaries were successfully loaded
   */
  private Dictionary loadDict() {
    // Load the default dictionary from resources
    Dictionary dict = new Dictionary("dict.txt", true); // true indicates it's a resource

    // Handle the user dictionary
    Path userDictPath =
        Paths.get(System.getProperty("user.home"), "group2" + File.separator + "userdict.txt");
    if (Files.exists(userDictPath)) {
      Dictionary userDict =
          new Dictionary(userDictPath.toString(), false); // false for a regular file
      this.userdict = userDict;
      transferWords(userDict, dict);
      System.out.println("userdict found");
    } else {
      createUserDict(); // Create a new, empty user dictionary file
      userdict = new Dictionary(userDictPath.toString(), false);
      System.out.println("Userdict not found, blank userdict created");
    }

    return dict;
  }

  // Get the OS
  public static String getOS() {
    String ret = "";
    String os = System.getProperty("os.name");
    char firstLetter = os.charAt(0);
    System.out.println(firstLetter);
    if (firstLetter == 'W') {
      ret = "windows";
    } else if (firstLetter == 'M') {
      ret = "mac";
    } else {
      ret = "linux";
    }
    System.out.println(ret);
    return ret;
  }

  // make userdirectoryfile
  private static boolean makeUserDirectoryFile(String dirname) {
    // Get the path to the user's home directory
    String userHome = System.getProperty("user.home");

    // Specify the name of the folder you want to create

    // Create a File object representing the folder in the home directory
    File folder = new File(userHome, dirname);

    // Using mkdir() to create a single directory
    return folder.mkdir();
  }

  // Put the userDict in group2/userdict.txt
  private void createUserDict() {

    // Make the group2 folder
    makeUserDirectoryFile("group2");

    // Get the userDict path based on system
    Path userDictPath =
        Paths.get(System.getProperty("user.home"), "group2" + File.separator + "userdict.txt");
    // make the file in the folder
    try {
      Files.createFile(userDictPath);
    } catch (IOException e) {
      System.out.println("something went wrong");
    }
  }

  public void writeLineToFile(String line, boolean append) {
    Path filePath =
        Paths.get(System.getProperty("user.home"), "group2" + File.separator + "userdict.txt");
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toString(), append))) {
      writer.write(line + '\n');
      System.out.println("Line written to file successfully.");
    } catch (IOException e) {
      System.err.println("Error writing to file: " + e.getMessage());
    }
  }

  public void removeWordFromUserDict(String inword) {
    Path filePath =
        Paths.get(System.getProperty("user.home"), "group2" + File.separator + "userdict.txt");

    Dictionary userDict = new Dictionary(filePath.toString(), false);

    // Remove the word from the dict
    userDict.removeWord(inword);

    // overwrite the current userdict with the words
    writeLineToFile("", false);

    // write out all the words of userdict into the file
    Enumeration<String> words = userDict.getKeys();
    while (words.hasMoreElements()) {
      String element = words.nextElement();
      // write to user.txt
      writeLineToFile(element, true);
      System.out.println("word" + element + "removed from branch");
    }
  }

  public void resetCache() {
    this.previousIncorrectWords = new ArrayList<Word>();
    this.incorrectWords = new ArrayList<Word>();
  }
}
