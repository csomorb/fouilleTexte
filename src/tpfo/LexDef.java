/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Définition lexicale = définition d'un terme (un mot) dans le lexique.
 * Pour un même term, une définition peut contenir un ou plusieurs lemmes,
 * et une ou plusieurs catégories syntaxiques (verbe, nom, adjectif, etc.)
 * 
 * @author Salah Ait-Mokhtar 
 */
public class LexDef {
    private List<String> lemmas = new ArrayList<>();
    private List<String> cats = new ArrayList<>();

    public List<String> getLemmas() {
        return lemmas;
    }

    public void setLemmas(List<String> lemmas) {
        this.lemmas = lemmas;
    }

    public List<String> getCats() {
        return cats;
    }

    public void setCats(List<String> cats) {
        this.cats = cats;
    }
    
    void add(String lemma, String cat) {
        if (!lemmas.contains(lemma)) {
            lemmas.add(lemma);
        }
        if (!cats.contains(cat)) {
            cats.add(cat);
        }
    }

    @Override
    public String toString() {
        return lemmas + " -- " + cats;
    }
    
    
    
}
