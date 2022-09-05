package org.mergeSort;

import org.mergeSort.constants.SortConstants;
import org.mergeSort.exceptions.UnknownArgumentException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class InputParams {

    private String sortType;
    private String dataType;
    private String outputFileName;
    private String[] inputFileNames;
    public String getSortType() {
        return sortType;
    }
    public String getDataType() {
        return dataType;
    }
    public String getOutputFileName() {
        return outputFileName;
    }
    public String[] getInputFileNames() {
        return inputFileNames;
    }

    public InputParams(String[] inputArgs) throws UnknownArgumentException {

        // Checking the number of passed arguments

        if (inputArgs.length < 3) {
            throw new UnknownArgumentException("Passed " + inputArgs.length +
                    " arguments. There must be at least three arguments.\n" +
                    "The first argument -d or -a is an optional argument. " +
                    "-d sort in descending order, -a sort in ascending order. By default, it is -a.\n" +
                    "The second argument -s or -i is a mandatory argument. " +
                    "-s indicates that the input files will have a string data type, -i indicates that the input files will have an integer data type.\n" +
                    "The third argument is the name of the file to which the execution result will be written. " +
                    "The third argument is mandatory.\n" +
                    "The fourth argument and the following are the names of the files from which data will be taken to execute the program. " +
                    "The argument with the name of the input file must be at least one");
        }

        // Checking first argument

        if (!SortConstants.allAvailableFlags.contains(inputArgs[0])) {
            throw new UnknownArgumentException("An unknown argument was passed in the first position");
        } else {
            if (SortConstants.sortFlags.contains(inputArgs[0])) {
                sortType = inputArgs[0];
            }
            if (SortConstants.typeFlags.contains(inputArgs[0])) {
                dataType = inputArgs[0];
            }
        }

        // Checking second argument

        if (!SortConstants.allAvailableFlags.contains(inputArgs[1])) {
            if (dataType == null) {
                throw new UnknownArgumentException("Data type argument not passed");
            }
            outputFileName = inputArgs[1];
        } else {
            if (SortConstants.sortFlags.contains(inputArgs[1])) {
                if (sortType != null) {
                    throw new UnknownArgumentException("The sorting argument is passed twice in the second position");
                }
                sortType = inputArgs[1];
            }
            if (SortConstants.typeFlags.contains(inputArgs[1])) {
                if (dataType != null) {
                    throw new UnknownArgumentException("The data type argument is passed twice in the second position");
                }
                dataType = inputArgs[1];
            }
            outputFileName = inputArgs[2];
        }

        // Checking for existing of the output file and creating

        File outputFile = new File(outputFileName);
        if (!outputFile.exists()) {
            System.err.println("WARNING: Unable to find the output file: " + outputFileName);
            try {
                if (outputFile.createNewFile()) {
                    System.out.println("Created a file with name: " + outputFileName);
                } else {
                    System.out.println("Failed to create a file with name: " + outputFileName);
                }
            } catch (IOException e) {
                System.err.println(e);
            }
        }

        // Setting the index of the beginning of input file names

        int startPointInputFiles;
        if (sortType == null) {
            sortType = SortConstants.SORT_ASC;
            startPointInputFiles = 2;
        } else {
            startPointInputFiles = 3;
        }

        // Checking that names of input files are passed

        inputFileNames = Arrays.copyOfRange(inputArgs, startPointInputFiles, inputArgs.length);
        if (inputFileNames.length == 0) {
            throw new UnknownArgumentException("Input file names not passed");
        }

        // Checking for existing of the input files, and they are not directories

        ArrayList<String> foundFiles = new ArrayList<String>();
        for (String fileName : inputFileNames) {
            File inputFile = new File(fileName);
            if (!inputFile.exists()) {
                System.err.println("WARNING: Unable to find the input file: " + fileName);
                continue;
            }
            if (inputFile.isDirectory()) {
                System.err.println("WARNING: This is a directory, not a file: " + fileName);
                continue;
            }
            foundFiles.add(fileName);
        }

        inputFileNames = foundFiles.toArray(new String[0]);

        if (foundFiles.isEmpty()) {
            throw new UnknownArgumentException("No input files found");
        }
    }
}
