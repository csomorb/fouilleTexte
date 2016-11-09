/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encog;

import org.encog.Encog;
import org.encog.engine.network.activation.ActivationFunction;
import org.encog.engine.network.activation.ActivationLinear;
import org.encog.engine.network.activation.ActivationRamp;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

/**
 *
 */
public class EncogXOR {

    /**
     * Input for XOR.
     */
    public static double XOR_INPUT[][] = {{0.0, 0.0}, {1.0, 0.0},
    {0.0, 1.0}, {1.0, 1.0}};

    /**
     * Ideal output for XOR.
     */
    public static double XOR_IDEAL[][] = {{0.0}, {1.0}, {1.0}, {0.0}};

    public static void main(String args[]) {

        int inputSize = 2;
        int outputSize = 1;
        // Linear activation (does not modify the weighted sum)
        ActivationFunction linear = new ActivationLinear();
        // Ramp threshold activation (activation avec seuil)
        ActivationFunction ramp = new ActivationRamp(0, 0, 1, 0);
        // Sigmoid
        ActivationFunction sigmoid = new ActivationSigmoid();
        // Tanh
        ActivationFunction tanh = new ActivationTANH();

        // Build the network
        BasicNetwork net = new BasicNetwork();
        // input layer
        net.addLayer(new BasicLayer(null, true, inputSize));
        // hidden layer(s)
        net.addLayer(new BasicLayer(tanh, true, 4));
        // ouput layer 
//        net.addLayer(new BasicLayer(linear, false, outputSize));
//        net.addLayer(new BasicLayer(ramp, false, outputSize));
//        net.addLayer(new BasicLayer(tanh, false, outputSize));
        net.addLayer(new BasicLayer(ramp, false, outputSize));

        net.getStructure().finalizeStructure();
        net.reset();

        // Prepare training data
        MLDataSet trainingSet = new BasicMLDataSet(XOR_INPUT, XOR_IDEAL);

        // Train the neural network
        final ResilientPropagation train = new ResilientPropagation(net,
                trainingSet);

        int epoch = 1;

        do {
            train.iteration();
            System.out.println("Epoch #" + epoch + " Error:"
                    + train.getError());
            epoch++;
        } while (train.getError() > 0.001 && epoch <= 10000);
        train.finishTraining();

        // test the neural network
        System.out.println("Results:");
        for (MLDataPair pair : trainingSet) {
            final MLData output = net.compute(pair.getInput());
            System.out.format("%.0f  %.0f  -> %f  (ideal=%.0f)\n",
                    pair.getInput().getData(0),
                    pair.getInput().getData(1),
                    output.getData(0),
                    pair.getIdeal().getData(0));
        }

        Encog.getInstance().shutdown();
    }
}
