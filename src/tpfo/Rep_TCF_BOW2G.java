/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpfo;


import java.util.Arrays;
import java.util.List;

/**
 * T = Tokénisation
 * F = Filtrage de certains mots
 * C = Casse (normalisation de la casse)
 * 
 * @author Salah Ait-Mokhtar
 */
public class Rep_TCF_BOW2G extends Rep {

    public Rep_TCF_BOW2G( Tokenizer tokenizer, Lexicon lex,
            int maxSize, int minCount
           ) {
        super(tokenizer, lex, maxSize, minCount);
    }

    @Override
    public int getDimension() {
        return fset.size();
    }

    @Override
    public double[] buildFeatures(Review review) {
        double[] vector = null;
        if (fset.isFinalized()) {
            // l'ensemble des traits est déjà construit, donc là on construit
            // le vecteur d'un avis
            vector = new double[fset.size()];
            Arrays.fill(vector, 0);
        }
        // T
        List<String> itokens = tokenizer.tokenize(review.text);
        // C
        List<String> ctokens = normalizeCase(itokens);
        // F
        List<String> tokens = filter(ctokens);
        // créer l'ensemble des traits BOW + 2G
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            // BOW : sac-de-mots
            setFeature(token, 1, vector);
            // 2G: bi-grammes
            String bigram = getNgram(2, tokens, i);
            if (bigram!=null) {
                setFeature(bigram, 1, vector);
            }
        }
        return vector;
    }

    @Override
    public void initializeFeatures(Dataset trainDataset) {
        for (Review review : trainDataset) {
            buildFeatures(review);
        }
        fset.selectByCount(minCount);
        fset.selectBySize(maxSize);
        fset.setFinalized(true);
    }



}
