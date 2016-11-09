/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpfo;


/**
 * Ensemble des classes possibles (représentées avec leur labels)
 * 
 * @author Salah Ait-Mokhtar 
 */
public enum ClassLabel {
    POS,
    NEUT,
    NEG;

    // les vecteurs de représentation des classes (construits une fois
    // pour toutes). Ils représentent les sorties idéales pour chacune
    // des classes.
    static double[][] vectors = {
        // POS
        {1.0, 0.0, 0.0},
        // NEUT
        {0.0, 1.0, 0.0},
        // NEG
        {0.0, 0.0, 1.0}
    };
    
    public static int size() {
        return ClassLabel.values().length;
    }

    public static double[] getVector(ClassLabel label) {
        return vectors[label.ordinal()];
    }

    /**
     * Décodage de la sortie: à partir des valeurs Softmax produites par le RN,
     * trouver la classe ayant la plus grande probabilité et retourner son label
     *
     * @param outputs
     * @return
     */
    public static ClassLabel getLabel(double outputs[]) {
        int imax = 0;
        for (int i = 0; i < outputs.length; i++) {
            if (outputs[i] > outputs[imax]) {
                imax = i;
            }
        }
        return values()[imax];
    }

    public static String getLabels(double outputs[]) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < outputs.length; i++) {
            sb.append(String.format("%s:%.4f, ", values()[i],
                    outputs[i]));
        }
        return sb.toString();
    }
}
