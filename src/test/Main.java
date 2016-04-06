package test;

import assembler.Assembler;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Shawon on 4/4/2016.
 */
public class Main {
    public static void main(String[] args) {
        ArrayList<String> input = new ArrayList<>();
        String x;
        String windowsFilePath = "N:\\Google Drive Loc\\Google Drive\\Class Projects\\CSE332\\Assembler\\files\\input.txt";
        String osXFilePath = "/Users/shawon/Google Drive/Class Projects/CSE332/Assembler/files/input.txt";


        try(FileReader reader = new FileReader(osXFilePath);
            BufferedReader r = new BufferedReader(reader)) {

            while((x = r.readLine()).equals("END") == false) {
                input.add(x);
            }

        } catch (Exception e) {
           //e.printStackTrace();
        }


        String[] data = input.toArray(new String[input.size()]);
        Assembler assembler = new Assembler(data);

        assembler.firstPass();
        assembler.secondPass();
        assembler.dispalyAddSymTable();
        assembler.displayMachineCode();
    }
}
