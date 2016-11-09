/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpfo;

/**
 *
 * @author Salah Ait-Mokhtar 
 */
public class Feature implements Comparable<Feature> {
    // le nom du trait
    private String name;
    // l'index du trait dans l'ensemble des traits auquel il appartient
    private int index;
    // fréquence du trait (nombre de fois observé dans les données)
    private int count = 0;
    // Est-ce que la sélection de ce trait dépend de sa fréquence?
    @SuppressWarnings("unused")
	private boolean isFrequencyBased;

    public Feature(String name, int index) {
        this.name = name;
        this.index = index;
        this.count = 0;
    }

    
    // Accessors
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    
    public void incrementCount() {
        this.count++;
    }

    @Override
    public int compareTo(Feature o) {
        if (this.count > o.count) {
            return -1;
        } else if (this.count < o.count) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "(" + index + ")" + name + ":" + count;
    }
    
    
    
}
