/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Cette classe représente la liste des avis (reviews) disponibles dans le
 * corpus collecté (voir la classe Review)
 */
@SuppressWarnings("serial")
public class Dataset extends ArrayList<Review> {

    // Regex qui permet de séparer les avis dans le corpus
    final static String regexSep = "^=\\d[\\s]+";
    // nombre d'avis par note (index 0 --> note=1, etc.)
    int[] sizePerRating = new int[5];

    /**
     * Charger les données (liste d'avis) à partir du fichier du corpus
     *
     * @param path nom du chemin d'accès au fichier du corpus
     * @return
     */
    static Dataset load(Path path) throws IOException {
        Dataset dataset = new Dataset();
        BufferedReader br;
        br = Files.newBufferedReader(path, StandardCharsets.UTF_8);
        String line, text = "";
        line = br.readLine();
        while (line != null) {
            line += "\n";
            if (line.matches(regexSep)) {
                // fin d'un avis: le stocker dans un objet avis avec la note
                int rating = Integer.parseInt(line.substring(1).trim());
                Review review = new Review(text, rating);
                dataset.add(review);
                dataset.sizePerRating[rating - 1]++;
                text = "";
            } else {
                text += line;
            }
            line = br.readLine();
        }
        Collections.shuffle(dataset, new Random(1));
        return dataset;
    }
    
    public Dataset split(float percentageLeft) {
        int keepCount = Math.round(size()*percentageLeft);
        Dataset otherDataset = new Dataset();
        for(int i=keepCount; i<size(); i++) {
            otherDataset.add(this.get(i));
            otherDataset.sizePerRating[this.get(i).rating - 1]++;
            this.sizePerRating[this.get(i).rating - 1]--;
        }
        while (this.size() > keepCount) {
            this.remove(this.size()-1);
        }
        return otherDataset;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Review review : this) {
            sb.append(review.toString());
            sb.append("\n--------------\n");
        }
        sb.append("\nTaille=").append(size()).append(", par note: ");
        sb.append(Arrays.toString(sizePerRating));
        return sb.toString();
    }

}
