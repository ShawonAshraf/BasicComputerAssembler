package assembler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shawon on 4/4/2016.
 */
public class Assembler {

    private int lc;
    private boolean sLC;
    private String[] data;
    private Map<String, String> addressSymbolTable;
    private Map<String, String> machineCode;

    public Assembler(String[] data) {
        this.data = data;
        lc = 0;
        addressSymbolTable = new HashMap<>();
        machineCode = new HashMap<>();
    }

    public void firstPass() {
        System.out.println("\n\npass-1: initialized .... .... .... \n\n");
        for(String s : data) {

            sLC = false;
            
            String label = "";
            String statement = "";

            if(s.contains(",") == true) {
                String[] splitted = s.split(",");
                label = splitted[0];
                statement = splitted[1];
            }

            else {
                label = null;
                statement = s;
            }

            System.out.println(String.format("pass-1: line = %s\t, label = %s\t, statement = %s", s, label, statement));


            if(label == null) {
                String args;
                String ins;
                if(statement.contains(" ")) {
                    String[] splitted = statement.split(" ");
                    ins = splitted[0];
                    args = splitted[1];
                }
                else {
                    ins = statement;
                    args = null;
                }

                switch (ins) {
                    case "org":
                    case "ORG":
                        if(isNumeric(args) == true) {
                            lc = Integer.parseInt(args);
                            sLC = true;
                        }
                        break;

                    case "end":
                    case "END":
                        sLC = true;
                        secondPass();

                }
            }

            else {
                if(label.length() <= 3) {
                    addressSymbolTable.put(label, Integer.toBinaryString(lc));
                }
                else {
                    System.out.println("pass-1: WARNING: Invalid label");
                }
            }

            if(sLC == false) lc++;
        }
    }

    public void secondPass() {
        System.out.println("\n\npass-2: initialized .... .... .... \n\n");
        lc = 0;
        int counter = 1;
        for(String s : data) {
            sLC = false;
            Map<String, String> options = getInsType(s);

            String opcode = "";
            String address = "";
            String indirect = "";
            System.out.println("pass-2: current line is of type " + options.get("type"));

            switch(options.get("type")) {
                case "MRI":
                    opcode = HelperDataClass.mri.get(options.get("instruction"));
                    if(addressSymbolTable.get(options.get("arg")) != null || addressSymbolTable.get(options.get("arg")).equals("") == false) {
                        address = addressSymbolTable.get(options.get("arg"));
                    }
                    else {
                        System.out.println("pass-2: error!! : undefined address - " + options.get("arg") + " @ line #" + counter);
                    }
                    indirect = options.get("indirect").equals("I") ? "1" : "0";
                    String tmp = String.format("%s %s", indirect, opcode);
                    System.out.println("pass-2 : opcode = " + opcode + " address = " + address + " I = " + indirect + " @ line #" + counter);
                    machineCode.put(String.format("%d", lc), tmp);
                    break;

                case "PSEUDO":
                    switch(options.get("instruction")) {
                        case "ORG":
                        case "org":
                            sLC = true;
                            if(isNumeric(options.get("arg")) == true) {
                                System.out.println("pass-2: : setting lc to " + options.get("arg"));
                                String m = options.get("arg");
                                lc = Integer.parseInt(m);
                            }
                            break;

                        case "end":
                        case "END":
                            displayMachineCode();
                            break;

                        case "DEC":
                            System.out.println("Inserting " + Integer.toBinaryString(Integer.parseInt(options.get("arg"))) + " at " + lc);
                            machineCode.put(String.format("%d", lc), Integer.toBinaryString(Integer.parseInt(options.get("arg"))));
                            break;
                        case "HEX":
                            System.out.println("Inserting " + Integer.toHexString(Integer.parseInt(options.get("arg"))) + " at " + lc);
                            machineCode.put(String.format("%d", lc), Integer.toHexString(Integer.parseInt(options.get("arg"))));
                            break;

                        case "INVALID":
                            System.out.println("pass-2: error: invalid instruction");
                            break;
                        case "NONMRI":
                            String temp = HelperDataClass.nonMri.get(options.get("instruction"));
                            System.out.println("pass-2: non MRI found = " + options.get("instruction") + "Inserting : " + temp);
                            machineCode.put(String.format("%d", lc), temp);
                            break;
                    }
            }
            counter++;
            if(sLC == true) {
                System.out.println("pass-2: slc = true, no increment");
            }
            else lc++;

        }
    }


    private boolean isNumeric(String s) {
        boolean f = false;

        try{
            if (s != null) {
                int x = Integer.parseInt(s);
                f = true;
            }
        }
        catch (Exception e) {
            f = false;
        }

        return f;
    }

    public void displayMachineCode() {
        System.out.println();
        System.out.println("Machine Code : ");
        machineCode.forEach((key,value) ->
                System.out.println("key = " + key + "\tvalue = " + value)
//                System.out.println("key = " + key + "\tvalue = " + String.format("%016s", value))
        );

    }

    private Map<String, String> getInsType(String ins) {
        Map<String, String> options = new HashMap<>();

        int strLen = ins.length();
        int spaceCount = getSpaceCount(ins);
        boolean isIndirect = false;
        boolean isInstruction = false;
        boolean isOperand = false;
        boolean isType = false;
        boolean isLabel = false;

        String label = "";
        String line = "";
        String instruction = "";
        String operand = "";
        String indirect = "";
        String insType = "";

        if(ins.contains(" ") == true) {
            if(spaceCount == 1) {
                String i = ins;
                if(ins.contains(",") == true) {
                    String[] splitted = ins.split(",");
                    label = splitted[0];
                    line = splitted[1];
                }
                else {
                    label = null;
                    line = ins;
                }
//                String[] tmp_split = line.split(" ");
                String[] tmp_split = line.split(" ");
                instruction = tmp_split[0];
                operand = tmp_split[1].trim();
            }

            else if(spaceCount >= 2 && spaceCount <= 3) {
                if(ins.contains(",") == true) {
                    String[] splitted = ins.split(",");
                    label = splitted[0];
                    line = splitted[1];
                }
                else {
                    label = null;
                    line = ins;
                }
                //line = line.trim();
                int l = getSpaceCount(line);

                if(l == 1) {
                    String[] tmp_split = line.split(" ");
                    instruction = tmp_split[0];
                    operand = tmp_split[1].trim();
                    isIndirect = false;
                }
                else if(l == 2) {
                    String[] tmp_split = line.split(" ");
                    instruction = tmp_split[0];
                    operand = tmp_split[1];
                    indirect = tmp_split[2];
                }
                else {
                    System.out.println("error : instruction with multiple operands are not supported");
                }

                if(isIndirect = false && indirect.equals("I") == false) {
                    System.out.println("error: can't set invalid I bit as " + indirect);
                }
            }
            else {
                instruction = ins;
            }
            if(HelperDataClass.mri.containsKey(instruction) == true) {
                insType = "MRI";
            }
            else if(HelperDataClass.pseudoInstruction.contains(instruction) == true) {
                insType = "PSEUDO";
            }
            else if(HelperDataClass.nonMri.containsKey(instruction) == true) {
                insType = "NONMRI";
            }
            else insType = "INVALID";
        }

        options.put("label", label);
        options.put("indirect", indirect);
        options.put("instruction", instruction);
        options.put("arg", operand);
        options.put("type", insType);

        return options;
    }

    private int getSpaceCount(String s) {
        int r = 0;
        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i) == ' ') r++;
        }

        return r;
    }

    public void dispalyAddSymTable() {
        System.out.println();
        System.out.println("Address sym table : ");
        addressSymbolTable.forEach((key,value) ->
                System.out.println("key = " + key + "\tvalue = " + String.format("%016d", Integer.parseInt(value)))
        );
    }
}
