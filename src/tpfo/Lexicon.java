/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Salah Ait-Mokhtar
 */
public class Lexicon implements Serializable {

    private static final long serialVersionUID = 7526472295622776147L;
    Map<String, LexDef> entries = new HashMap<>();

    /**
     * Rajoute un lemme et une catégorie à la definition lexicale d'un terme Si
     * le terme n'existe pas encore, il est ajouté au lexique.
     *
     * @param term
     * @param lemma
     * @param POS
     */
    public void add(String term, String lemma, String POS) {
        LexDef lexdef = entries.get(term);
        if (lexdef == null) {
            lexdef = new LexDef();
            entries.put(term, lexdef);
        }
        lexdef.add(lemma, POS);
    }

    public boolean contains(String term) {
        return (entries.get(term) != null);
    }

    public LexDef getLexDef(String term) {
        return entries.get(term);
    }

    public List<String> getLemmas(String term) {
        LexDef lexdef = getLexDef(term);
        if (lexdef != null) {
            return lexdef.getLemmas();
        } else {
            return null;
        }
    }

    public boolean hasLemmaIn(String term, List<String> lemmas) {
        List<String> termLemmas = getLemmas(term);
        if (termLemmas == null) {
            return false;
        }
        for (String l : termLemmas) {
            if (lemmas.contains(l)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasCatIn(String term, List<String> cats) {
        LexDef lexdef = getLexDef(term);
        if (lexdef == null) {
            return false;
        }
        for (String cat : lexdef.getCats()) {
            if (cats.contains(cat)) {
                return true;
            }
        }
        return false;
    }

    public void load(Path path) throws IOException {
        entries.clear();
        BufferedReader br = Files.newBufferedReader(path,
                StandardCharsets.UTF_8);
        String line;
        while ((line = br.readLine()) != null) {
            String[] elements = line.trim().split("\\t");
            add(elements[0], elements[2], elements[1]);
        }
    }

}
