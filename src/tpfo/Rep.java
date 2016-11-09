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
 *
 * @author Salah Ait-Mokhtar
 */
public abstract class Rep {

    static List<String> catsToKeep = new ArrayList<>(Arrays.asList(
            "nc", "v", "adj", "adv", "advneg", "clneg"));
    static List<String> lemmasToIgnore = new ArrayList<>(Arrays.asList(
            "alors", "aussi", "autre", "avant", "avoir", "avec", "bref", "bientôt",
            "cela", "cla", "d'ailleurs", "dedans", "entre", "être", 
            "falloir", "là", "même", 
            "pour", "par", "tous", "un"
    ));

    Tokenizer tokenizer;
    Lexicon lex;

    // L'ensemble des traits : quelle que soit le type de représentation, la
    // définition d'un ensemble de traits sera nécessaire
    protected FeatureSet fset;

    //
    protected int maxSize;
    protected int minCount;

    // Méthodes abstraites à implémenter par les sous-classes possibles
    // Initialisation (définition) de l'ensemble des traits
    public abstract void initializeFeatures(Dataset trainDataset);

    // Sélection des traits pour un avis et construction du vecteur
    public abstract double[] buildFeatures(Review review);

    // Retourne la dimension de l'espace de représentation
    public abstract int getDimension();

    //
    public Rep(Tokenizer tokenizer, Lexicon lex, int maxSize, int minCount) {
        this.maxSize = maxSize;
        this.minCount = minCount;
        this.tokenizer = tokenizer;
        this.lex = lex;
        this.fset = new FeatureSet();
    }

    /**
     * Cette méthode ajoute le trait 'featureName' à l'ensemble des traits si
     * celui-ci n'est pas encore finalisé. S'il est déjà finalisé, elle vérifie
     * si 'featureName' est un trait connu, et si c'est le cas, elle lui assigne
     * la valeur 'value' à la position correcte dans le vecteur 'vector'.
     *
     * @param featureName
     * @param value
     * @param vector
     */
    protected void setFeature(String featureName, double value, double[] vector) {
        if (!fset.isFinalized()) {
            fset.add(featureName);
        } else {
            int index = fset.getIndex(featureName);
            if (index >= 0) {
                // le trait existe : mettre sa valeur dans le vecteur
                vector[index] = value;
            }
        }
    }

    /**
     * Construit une chaine de caractères qui représente de façon facilement
     * lisible un vecteur de traits, en incluant le nom des traits avec leur
     * valeurs (seuls ceux de valeur non nulle seront affichés). Cette méthode
     * sera appelée à chaque fois qu'on voudra afficher de façon lisible un
     * vecteur de traits.
     *
     * @param vect
     * @return
     */
    public String toString(double[] vect) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < fset.size(); i++) {
            if (vect[i] > 0) {
                sb.append(fset.getFeatureName(i)).append(":");
                if (vect[i] == 1) {
                    sb.append((int) vect[i]);
                } else {
                    sb.append(String.format("%.4f", vect[i]));
                }
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /////////// Divers traitements pour construire les traits ////////////////
    /**
     * Cette méthode construit un n-gramme en prenant le token à la position
     * 'position' et en lui concaténant les (n-1) tokens qui le précèdent dans
     * la liste des tokens.
     *
     * @param n
     * @param tokens
     * @param position
     * @return
     */
    protected String getNgram(int n, List<String> tokens, int position) {
        if (position-n+1 < 0) {
            return null;
        }
        String ngram = tokens.get(position).toLowerCase();
        for (int i = 1; i < n; i++) {
            if (position - i >= 0) {
                ngram = tokens.get(position - i).toLowerCase() + "_" + ngram;
            }
        }
        return ngram;
    }

    /**
     * Idem mais remplace le token à la position courante par un terme fourni
     * (mainToken) 
     * @param n
     * @param mainToken
     * @param tokens
     * @param position
     * @return 
     */
    protected String getNgram(int n, String mainToken,
            List<String> tokens, int position) {
        if (position-n+1 < 0) {
            return null;
        }
        String ngram = mainToken.toLowerCase();
        for (int i = 1; i < n; i++) {
            if (position - i >= 0) {
                ngram = tokens.get(position - i).toLowerCase() + "_" + ngram;
            }
        }
        return ngram;
    }

    /**
     * F
     * Filtrage de certains termes
     *
     * @param tokens
     * @return
     */
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

    // C: Normaliser la casse
    protected List<String> normalizeCase(List<String> tokens) {
        List<String> ntokens = new ArrayList<>();
        for (String token : tokens) {
            // mise en minuscule
            ntokens.add(token.toLowerCase());
        }
        return ntokens;
    }

    // L: Lemmatiser
    protected List<String> lemmatize(List<String> tokens) {
        List<String> ntokens = new ArrayList<>();
        for (String token : tokens) {
            List<String> lemmas = lex.getLemmas(token.toLowerCase());
            if (lemmas == null) {
                // le terme n'est pas dans le lexique: le garder tel quel
                ntokens.add(token.toLowerCase());
            } else {
                // utiliser le premier lemme
                ntokens.add(lemmas.get(0));
            }
        }
        return ntokens;
    }

}
