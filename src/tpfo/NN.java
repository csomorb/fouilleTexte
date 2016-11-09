/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpfo;

import org.encog.Encog;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.folded.FoldedDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.cross.CrossValidationKFold;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

/**
 *
 * @author Salah Ait-Mokhtar 
 */
public class NN {

    protected final int seed = 5;
    protected BasicNetwork net;
    protected int inputSize;
    protected int outputSize;

    public NN(int inputSize, int outputSize) {
        this.inputSize = inputSize;
        this.outputSize = outputSize;
    }

    
    public void train(MLDataSet mlTrainSet, double maxErr, int maxIter) {
        net.reset(seed);
        final FoldedDataSet folded = new FoldedDataSet(mlTrainSet);
        // Train the neural network
        final ResilientPropagation train = new ResilientPropagation(net,
                folded);
        // laisser Encog décider du nombre de threads
        train.setThreadCount(0);
        // Entraînement par validation croisée : dans notre cas, ce n'est pas
        // une vraie validation croisée car on construit l'ensemble des traits
        // sur tout le corpus d'entraînement, avant de le partager en K 
        // parties pour la validation croisée
        final CrossValidationKFold trainFolded = new CrossValidationKFold(
                train, 4);
        int epoch = 1;

        do {
            trainFolded.iteration();
            System.out.println("Epoch #" + epoch + " Error:"
                    + trainFolded.getError());
            epoch++;
        } while (trainFolded.getError() > maxErr && epoch <= maxIter);
        trainFolded.finishTraining();
        Encog.getInstance().shutdown();

    }
    
    public double[] compute(double[] input) {
        double[] output = new double[outputSize];
        net.compute(input, output);
        return output;
    }


}
