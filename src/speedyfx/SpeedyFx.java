/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package speedyfx;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Salah Ait-Mokhtar <Salah.Ait-Mokhtar@xrce.xerox.com>
 */
@SuppressWarnings("unused")
public class SpeedyFx {

    static final int DEFAULT_NBITS = 20;
    static final int DEFAULT_N = (int) Math.pow(2, 20);

    final int N;
    final int Nbits;
    final int mask;
    int[] codeTable;

    public SpeedyFx() {
        this.Nbits = DEFAULT_NBITS;
        this.mask = (1 << Nbits) - 1;
        this.N = (int) Math.pow(2, Nbits);
    }

    public SpeedyFx(int Nbits) {
        this.Nbits = Nbits;
        this.mask = (1 << Nbits) - 1;
        this.N = (int) Math.pow(2, Nbits);
        prepTable(Character.MAX_VALUE);
    }

    /**
     * Gets an unsigned integer from a signed int
     *
     * @param x
     * @return
     */
    public static long getUnsignedInt(int x) {
        return x & 0x00000000ffffffffL;
    }

    private int[] getRandInts(int n) {
        Random random = new Random(1);
        int[] randints = new int[n];
        for (int i = 0; i < n; i++) {
//            randints[i] = random.nextInt();
            while ((randints[i] = random.nextInt()) == 0);
//            while ( (randints[i] = random.nextInt()) <= 0);
        }
        return randints;
    }

    private void prepTable(int size) {
        int[] rand = getRandInts(size);
        codeTable = new int[size];
        for (int c = 0; c < size; c++) {
//            codeTable[c] = Character.isAlphabetic(c)
//                    ? Character.toLowerCase(c) : 0;
            codeTable[c] = Character.isAlphabetic(c)
                    ? rand[Character.toLowerCase(c)] : 0;
        }
    }

    private double euclidianDistance(double[] a, double[] b) {
        double diff_square_sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            diff_square_sum += (a[i] - b[i]) * (a[i] - b[i]);
        }
        return Math.sqrt(diff_square_sum);
    }

    private double euclidianDistance(byte[] a, byte[] b) {
        double diff_square_sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            diff_square_sum += (a[i] - b[i]) * (a[i] - b[i]);
        }
        return Math.sqrt(diff_square_sum);
    }


    byte[] extractWordsetBytes(String text) throws IOException {
        byte fv[] = new byte[N];
        int start = 0;
        int end = 0;
        int wordhash = 0;
        long lhash = 0;
        for (int i = 0; i < text.length(); i++) {
            int code = codeTable[text.charAt(i)];
            if (code != 0) {
                // it's a word char
                wordhash = ((wordhash >> 1) + code);
                end++;
            } else {
                // a sep char
                if (wordhash != 0) {
                    wordhash = wordhash & mask;
                    fv[wordhash % N] = 1;
                    System.out.print(text.substring(start, end));
                    System.out.println("  " + (wordhash % N));
                    String s = String.format("%s\t%d\n", text.substring(
                            start, end), (wordhash % N)).toLowerCase();
//                    Files.write(Paths.get("tt"), s.getBytes("UTF8"),
//                            StandardOpenOption.APPEND);
                    wordhash = 0;
                    start = i;
                }
                start++;
                end = start;
            }
        }
        // last token at end of text ?
        if (wordhash != 0) {
            wordhash = wordhash & mask;
            fv[wordhash % N] = 1;
            wordhash = 0;
        }
        return fv;
    }

    public static void main(String[] args) throws IOException {

        Files.write(Paths.get("tt"), "".getBytes("UTF8"));
        String text1, text2;
        SpeedyFx sfx = new SpeedyFx(20);

        text1 = "Table  1  presents  pseudo-code  that\n"
                + "  performs  text  word  extraction  \n"
                + "for boolean features in a straightforward manner. ";
        text2 = "Table  1  presents  pseudo-code  that\n"
                + "  performs  text   extraction  \n"
                + "for features in a straightforward manner. ";

        byte[] fv1 = sfx.extractWordsetBytes(text1);
        byte[] fv2 = sfx.extractWordsetBytes(text2);

        double d = sfx.euclidianDistance(fv1, fv2);
        System.out.println("\n==> Distance: " + d);
    }

}
