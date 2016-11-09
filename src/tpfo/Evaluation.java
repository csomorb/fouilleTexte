/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpfo;

import java.util.Arrays;

/**
 * Classe pour effectuer l'évaluation : calcul de la précision etc.
 *
 * @author Salah Ait-Mokhtar
 */
public class Evaluation {

    int dataSize;
    int errCount;
    int[] tp = new int[ClassLabel.size()];
    int[] fp = new int[ClassLabel.size()];
    int[] tn = new int[ClassLabel.size()];
    int[] fn = new int[ClassLabel.size()];

    private void reset() {
        dataSize = 0;
        errCount = 0;
        Arrays.fill(tp, 0);
        Arrays.fill(fp, 0);
        Arrays.fill(tn, 0);
        Arrays.fill(fn, 0);
    }

    public void evaluate(Model model, Dataset dataset) {
        reset();
        ClassLabel prediction;
        dataSize = dataset.size();
        for (Review r : dataset) {
            prediction = model.classify(r);
            if (prediction == r.getLabel()) {
                countCorrect(prediction);
                // correct
//                System.out.format("%s\n  -> %s  (ideal=%s)\n",
//                        r.toString(),
//                        prediction,
//                        r.getLabel());
            } else {
                errCount++;
                countIncorrect(prediction, r.getLabel());
//                  System.out.println(r);
//                System.out.println(model.toString(r));
            }
//            System.out.println(r);
 //           System.out.println("Res: " + prediction);
//            System.out.println(model.toString(r));
        }
    }

    private void countCorrect(ClassLabel label) {
        tp[label.ordinal()]++;
        for (int i = 0; i < tn.length; i++) {
            if (i != label.ordinal()) {
                tn[i]++;
            }
        }
    }

    private void countIncorrect(ClassLabel incorrectLabel, ClassLabel trueLabel) {
        fp[incorrectLabel.ordinal()]++;
        fn[trueLabel.ordinal()]++;
    }

    public float getP(ClassLabel label) {
        return ((float) tp[label.ordinal()]
                / (tp[label.ordinal()] + fp[label.ordinal()]));
    }

    public float getR(ClassLabel label) {
        return ((float) tp[label.ordinal()]
                / (tp[label.ordinal()] + fn[label.ordinal()]));
    }

    public float getF(ClassLabel label) {
        return 2 * (getP(label) * getR(label)) / (getP(label) + getR(label));
    }

    public float getP() {
        float x = 0;
        for (ClassLabel c : ClassLabel.values()) {
            x += tp[c.ordinal()];
        }
        float y = 0;
        for (ClassLabel c : ClassLabel.values()) {
            y += tp[c.ordinal()] + fp[c.ordinal()];
        }
        return (x / y);
    }

    public float getR() {
        float x = 0;
        for (ClassLabel c : ClassLabel.values()) {
            x += tp[c.ordinal()];
        }
        float y = 0;
        for (ClassLabel c : ClassLabel.values()) {
            y += tp[c.ordinal()] + fn[c.ordinal()];
        }
        return (x / y);
    }

    public float getF() {
        return 2 * (getP() * getR()) / (getP() + getR());
    }

    //
    public String resultToString() {
        StringBuilder sb = new StringBuilder();
        float p = ((float) (dataSize - errCount)) / dataSize;
        sb.append(String.format(
                "E=%.4f, P=%.4f, R=%.4f,  F=%.4f\n", (1 - p),
                getP(), getR(), getF()
        ));
        for (ClassLabel c : ClassLabel.values()) {
            sb.append(String.format(
                    "%s: P=%.4f, R=%.4f,  F=%.4f\n",
                    c, getP(c), getR(c), getF(c)
            ));
        }
        return sb.toString();
    }

}
