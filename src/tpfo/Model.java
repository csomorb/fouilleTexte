/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpfo;


import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;


/**
 *
 * @author Salah Ait-Mokhtar
 */
public class Model {

    // pour fixer les initialisations aléatoires des poids
    final int seed = 5;

    double maxErr = 0.0001;
    int maxIter = 15;

    // la représentation
    Rep repre;
    // le réseau de neurones
    NN nn;

    /**
     * Créer un modèle de classification des avis, en utilisant une
     * représentation et un réseau de neurones
     *
     * @param repre
     * @param nn
     */
    public Model(Rep repre, NN nn) {
        this.repre = repre;
        this.nn = nn;
    }
    
    public double[] getVector(Review r) {
        return repre.buildFeatures(r);
    }
    
    public String toString(Review r) {
        return repre.toString(getVector(r));
    }

    /**
     * Préparer les données dans le format MLDataSet de la librairie Encog
     * @param trainDataset
     * @return 
     */
    private MLDataSet buildMLDataSet(Dataset trainDataset) {
        MLDataSet mldataset = new BasicMLDataSet();
        for (Review r : trainDataset) {
            double[] input = repre.buildFeatures(r);
            double[] ideal = ClassLabel.getVector(r.getLabel());
            mldataset.add(new BasicMLData(input), new BasicMLData(ideal));
        }
        return mldataset;
    }

    /**
     * Entraîner le modèle
     * 
     * @param trainDataset 
     */
    public void train(Dataset trainDataset) {
        MLDataSet mltrainset = buildMLDataSet(trainDataset);
        nn.train(mltrainset, maxErr, maxIter);
    }

    /**
     * Classifier une instance (un avis)
     * @param r
     * @return 
     */
    public ClassLabel classify(Review r) {
        double[] input = repre.buildFeatures(r);
        double[] output = nn.compute(input);
        return ClassLabel.getLabel(output);
    }

}
