/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpfo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Salah Ait-Mokhtar
 */
public class Tokenizer {

    private final String tokenRegex = "[\\p{L}\\d]+[[\\p{L}\\d]\\-']*";
    private final Pattern pattern = Pattern.compile(tokenRegex);
    private final Matcher m = pattern.matcher("");

    private Lexicon lex;

    public Tokenizer() {
    }

    public Tokenizer(Lexicon lex) {
        this.lex = lex;
    }

    public List<String> tokenize(String text) {
        m.reset(text);
        List<String> tokens = new ArrayList<>();
        while (m.find()) {
            String token = m.group();
            if (lex == null || lex.contains(token)) {
                // pas de lexique, ou alors le lexique contient le token
                tokens.add(token);
            } else if (lex.contains(token.toLowerCase())) {
                // Utilisation d'un lexique, on normalise la casse et on
                // vérifie si c'est dans le lexique
                tokens.add(token.toLowerCase());
            } else {
                // vraiment pas dans le lexique : voir si on peut le segmenter
                // en sous-termes (apostrophe): l' , n'  , qu' , etc.
                int index = token.indexOf("'");
                if (index > 0) {
                    String subtoken = token.substring(0, index + 1);
                    if (lex.contains(subtoken.toLowerCase())) {
                        tokens.add(subtoken.toLowerCase());
                        token = token.substring(index + 1);
                    }
                }
                if (lex.contains(token)) {
                    tokens.add(token);
                } else if (lex.contains(token.toLowerCase())) {
                    tokens.add(token.toLowerCase());
                } else {
                    tokens.add(token);
                }
            }
        }
        return tokens;
    }

}
