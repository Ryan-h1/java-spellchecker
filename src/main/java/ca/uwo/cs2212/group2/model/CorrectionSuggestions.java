package ca.uwo.cs2212.group2.model;
import java.util.PriorityQueue;

/**
 * Represents the CorrectionSuggestions class
 *
 * @author Shaylan Pratt
 *     <p>This class tracks a CorrectionSuggestions object and its instance variables, word and distance
 *     
 */ 

public class CorrectionSuggestions implements Comparable<CorrectionSuggestions>{
    private String word;
    private int distance;


    /**
   * Constructor
   * @param word
   * @param distance 
   */
    public CorrectionSuggestions(String word, int distance){
        this.word = word;
        this.distance = distance;
    }

    public String getWord(){
        return word;
    }

    public int getDistance(){
        return distance;
    }

    public void setWord(String newWord){
        this.word = newWord;
    }

    public void setDistance(int newDistance){
        this.distance = newDistance;
    }

    /**
    * Compares this CorrectionSuggestions object with another object for order.
    *
    * @param o the CorrectionSuggestions to be compared.
    * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
    * @throws NullPointerException if the specified object is null.
    */
	public int compareTo(CorrectionSuggestions o) {
		if(this.getDistance() > o.getDistance()) return 1;
		if(this.getDistance() < o.getDistance()) return -1;
		return 0;
	}


}