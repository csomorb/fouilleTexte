/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Salah Ait-Mokhtar
 */
public class FeatureSet {

    private final Map<String, Feature> map = new HashMap<>();
    private final List<Feature> featureList = new ArrayList<>();
    private boolean finalized = false;

    /**
     * Retourne la taille de cet ensemble de traits (i.e. le nombre de traits
     * qu'il contient).
     *
     * @return
     */
    public int size() {
        return featureList.size();
    }

    public boolean isFinalized() {
        return finalized;
    }

    public void setFinalized(boolean finalized) {
        this.finalized = finalized;
    }
    
    

    /**
     * Retourne l'index (entier) du trait feat dans cet ensemble de traits, et
     * -1 si le trait n'existe pas.
     *
     * @param featureName
     * @return
     */
    public int getIndex(String featureName) {
        Feature feat = map.get(featureName);
        if (feat == null) {
            return -1;
        } else {
            return feat.getIndex();
        }
    }
    
    public String getFeatureName(int index) {
        if (index<this.size()) {
            return featureList.get(index).getName();
        } else {
            return null;
        }
    }

    /**
     * Ajoute le trait featureName à cet ensemble, s'il n'existe pas déjà. Dans
     * tous les cas, renvoie l'index du trait. La fréquence du trait est aussi
     * incrémentée.
     *
     * @param featureName
     * @return
     */
    public int add(String featureName) {
        int index = getIndex(featureName);
        if (index < 0) {
            index = featureList.size();
            Feature feat = new Feature(featureName, index);
            map.put(featureName, feat);
            featureList.add(feat);
        }
        featureList.get(index).incrementCount();
        return index;
    }
    


    public void sort() {
        // Trier les traits dans la liste (les plus fréquents en premier)
        Collections.sort(featureList);
        // Mettre à jour les index
        for (int i = 0; i < featureList.size(); i++) {
            featureList.get(i).setIndex(i);
        }
    }

    /**
     * Sélectionner seulement les traits dont la fréquence est d'au moins
     * minCount. Les autres traits sont supprimésde cet ensemble.
     *
     * @param minCount
     */
    public void selectByCount(int minCount) {
        int i = 0;
        while (i < featureList.size()) {
            if (featureList.get(i).getCount() < minCount) {
                // ce trait n'a pas assez d'occurrences pour être gardé
                map.remove(featureList.get(i).getName());
                featureList.remove(i);
            } else {
                i++;
            }
        }
        this.sort();
    }

    /**
     * Sélectionner les les traits les plus fréquents, dont le nombre maximale
     * est maxSize.
     * @param maxSize 
     */
    public void selectBySize(int maxSize) {
        // tri par fréquence
        this.sort();
        // supprimer les traits les moins fréquents jusqu'à atteindre la 
        // taille max demandée
        while (featureList.size() > maxSize) {
            map.remove(featureList.get(featureList.size()-1).getName());
            featureList.remove(featureList.size() - 1);
        }
    }

    @Override
    public String toString() {
        return featureList.toString();
    }

}
