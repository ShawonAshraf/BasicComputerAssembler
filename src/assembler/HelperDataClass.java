package assembler;

import java.util.*;

/**
 * Created by Shawon on 4/4/2016.
 */
public class HelperDataClass {

    public static Map<String, String> mri;
    public static Map<String, String> nonMri;
    public static ArrayList<String> pseudoInstruction;

    static {
        // add to mri table
        mri = new HashMap<>();

        mri.put("AND", "000");
        mri.put("ADD", "001");
        mri.put("LDA", "010");
        mri.put("STA", "011");
        mri.put("BUN", "100");
        mri.put("BSA", "101");
        mri.put("ISZ", "110");

        // add to non mri table
        nonMri = new HashMap<>();

        nonMri.put("CLA", "0 111 100000000000");
        nonMri.put("CLE", "0 111 010000000000");
        nonMri.put("CMA", "0 111 001000000000");
        nonMri.put("CME", "0 111 000100000000");
        nonMri.put("CIR", "0 111 000010000000");
        nonMri.put("CIL", "0 111 000001000000");
        nonMri.put("INC", "0 111 000000100000");
        nonMri.put("SPA", "0 111 000000010000");
        nonMri.put("SNA", "0 111 000000001000");
        nonMri.put("SZA", "0 111 000000000100");
        nonMri.put("SZE", "0 111 000000000010");
        nonMri.put("HLT", "0 111 000000000001");
        nonMri.put("INP", "1 111 100000000000");
        nonMri.put("OUT", "1 111 010000000000");
        nonMri.put("SKI", "1 111 001000000000");
        nonMri.put("SKO", "1 111 000100000000");
        nonMri.put("ION", "1 111 000010000000");
        nonMri.put("IOF", "1 111 000001000000");

        // add to pseudo instruction table
        pseudoInstruction = new ArrayList<>();

        pseudoInstruction.add("ORG");
        pseudoInstruction.add("END");
        pseudoInstruction.add("DEC");
        pseudoInstruction.add("HEX");

    }

    public HelperDataClass() { }

}
