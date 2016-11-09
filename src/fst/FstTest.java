/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fst;

import java.io.IOException;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.BytesRefBuilder;
import org.apache.lucene.util.IntsRef;
import org.apache.lucene.util.IntsRefBuilder;
import org.apache.lucene.util.fst.Builder;
import org.apache.lucene.util.fst.FST;
import org.apache.lucene.util.fst.FST.INPUT_TYPE;
import org.apache.lucene.util.fst.PositiveIntOutputs;
import org.apache.lucene.util.fst.Util;

/**
 *
 */
public class FstTest {

    public static void main(String[] args) throws IOException {
// Input values (keys). These must be provided to Builder in Unicode sorted order!
        String inputValues[] = {"cat", "dog", "dogs"};
        long outputValues[] = {5, 7, 12};

        PositiveIntOutputs outputs = PositiveIntOutputs.getSingleton();
        Builder<Long> builder = new Builder<Long>(INPUT_TYPE.BYTE1, outputs);
        BytesRefBuilder scratchBytes = new BytesRefBuilder();
        IntsRefBuilder scratchInts = new IntsRefBuilder();
        for (int i = 0; i < inputValues.length; i++) {
            scratchBytes.copyChars(inputValues[i]);
            builder.add(Util.toIntsRef(scratchBytes.toBytesRef(),
                    scratchInts), outputValues[i]);
        }
        FST<Long> fst = builder.finish();

        Long value = Util.get(fst, new BytesRef("dog"));
        System.out.println(value); // 7

// Only works because outputs are also in sorted order
        IntsRef key = Util.getByOutput(fst, 12);
        System.out.println(Util.toBytesRef(key, scratchBytes).utf8ToString()); // dogs

    }
}
