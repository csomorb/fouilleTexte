/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpfo;

import static tpfo.ClassLabel.NEG;
import static tpfo.ClassLabel.NEUT;
import static tpfo.ClassLabel.POS;


/**
 * Avis sur un restaurant
 */
public class Review {
    
    static final int SEUIL_POSITIF = 4;
    static final int SEUIL_NEGATIF = 2;
    

    // Texte de l'avis
    String text;
    // Note sur 5 pour cet avis
    int rating;
    // Label de la classe correcte pour cet avis (POS/NEG/NEUT)
    ClassLabel label;
    
    /////////////////////////////////////////////////
    

    public Review(String texte, int note) {
        this.text = texte;
        this.rating = note;
        if (this.rating >= SEUIL_POSITIF) {
            this.label = POS;
        } else if (this.rating <= SEUIL_NEGATIF) {
            this.label = NEG;
        } else {
            this.label = NEUT;
        }
    }
    
    
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public ClassLabel getLabel() {
        return label;
    }

    //
    public void setLabel(ClassLabel label) {
        this.label = label;
    }

    public String toString() {
        return text + "note=" + rating + ",  label=" + label;
    }
    
}
