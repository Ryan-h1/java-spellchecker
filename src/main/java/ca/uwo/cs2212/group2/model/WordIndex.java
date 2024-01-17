package ca.uwo.cs2212.group2.model;

import java.util.Objects;


/**
 * Represents a word with its position in a text.
 */
  public class WordIndex {
  private final String content;
  private final int position;

    
  /**
     * Constructs a WordIndex object with the given content and position.
     *
     * @param content  the content of the word.
     * @param position the position of the word in the text.
     */

  public WordIndex(String content, int position) {
    this.content = content;
    this.position = position;
  }


  /**
     * Compares this WordIndex object with another object for equality.
     *
     * @param o the object to compare with.
     * @return {@code true} if the objects are equal, {@code false} otherwise.
     */

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    WordIndex that = (WordIndex) o;
    return position == that.position && Objects.equals(content, that.content);
  }


  /**
     * Generates a hash code for this WordIndex object.
     *
     * @return a hash code value for this object.
     */
  @Override
  public int hashCode() {
    return Objects.hash(content, position);
  }
}
