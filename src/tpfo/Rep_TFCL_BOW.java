/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpfo;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * T = Tokénisation
 * F = Filtrage de certains mots
 * C = Casse (normalisation de la casse)
 * L = Lemmatisation
 * BOW2G = BOW (Bag-of-words = sacs-de-mots) + 2G (bi-grammes)
 * 
 * @author Salah Ait-Mokhtar
 */
public class Rep_TFCL_BOW extends Rep {

    static List<String> catsToKeep = new ArrayList<>(Arrays.asList(
            "nc", "v", "adj", "adv", "advneg", "clneg"));
    static List<String> lemmasToIgnore = new ArrayList<>(Arrays.asList(
            "être", "avoir", "avec", "bref", "alors", "tous", "cela",
            "falloir", "avant", "bientôt", "d'ailleurs", "un", "cla",   
            "pour", "par", "même", "autre", "aussi", "entre",
            //rajout
    		"car","donc","deux","jour","dîner","sembler","filet","août","parisien","moi","pendant"
    		));

    Tokenizer tokenizer;
    Lexicon lex;

    public Rep_TFCL_BOW( Tokenizer tokenizer, Lexicon lex, int maxSize, int minCount) {
        super(tokenizer,lex,maxSize, minCount);
        this.tokenizer = tokenizer;
        this.lex = lex;
        this.fset = new FeatureSet();
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
        // F
        List<String> ftokens = filter(itokens);
        // C + L
        List<String> tokens = normalize(ftokens);
        // créer l'ensemble des traits BOW2G
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            // BOW : sac-de-mots
            setFeature(token, 1, vector);
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


    /////////// Divers traitements pour construire les traits /////////////////
    
    protected List<String> filter(List<String> tokens) {
        List<String> ftokens = new ArrayList<>();
        for (String token : tokens) {
            // garder seulement les noms, adj, verbes, et adv
            // et ignorer certains mots
            if (lex.hasCatIn(token, catsToKeep)
                    && !lex.hasLemmaIn(token, lemmasToIgnore)) {
                ftokens.add(token);
            }
        }
        return ftokens;
    }

    // TOK-F-NT-L-BOW-2G
    private List<String> normalize(List<String> tokens) {
        List<String> ntokens = new ArrayList<>();
        for (String token : tokens) {
            List<String> lemmas = lex.getLemmas(token.toLowerCase());
            if (lemmas == null) {
                ntokens.add(token.toLowerCase());
            } else {
//                for(String l:lemmas) {
                ntokens.add(lemmas.get(0));
//                }
            }
        }
        return ntokens;
    }


}
