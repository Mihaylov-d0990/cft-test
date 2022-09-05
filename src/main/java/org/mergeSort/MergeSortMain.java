package org.mergeSort;

import org.mergeSort.exceptions.UnknownArgumentException;

public class MergeSortMain {
    private InputParams inputParams;
    public MergeSortMain(String[] inputArgs) {
        try {
            inputParams = new InputParams(inputArgs);
            OutputFileWriter inputFileReader = new OutputFileWriter(inputParams);
            inputFileReader.writeOutput();
        } catch (UnknownArgumentException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }
}
