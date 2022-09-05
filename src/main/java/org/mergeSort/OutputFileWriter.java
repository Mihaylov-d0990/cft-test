package org.mergeSort;

import org.apache.commons.io.input.ReversedLinesFileReader;
import org.mergeSort.constants.SortConstants;

import javax.management.InvalidAttributeValueException;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class OutputFileWriter {
    private String currentFile = "";
    public String[] inputFileNames;
    public String outputFileName;
    public String dataType;
    public String sortType;
    public OutputFileWriter(InputParams inputParams) {
        this.inputFileNames = inputParams.getInputFileNames();
        this.outputFileName = inputParams.getOutputFileName();
        this.dataType = inputParams.getDataType();
        this.sortType = inputParams.getSortType();
    }

    public void writeOutput() {
        String tempFileName1 = "temp1.txt";
        String tempFileName2 = "temp2.txt";
        File tempFile1 = new File(tempFileName1);
        File tempFile2 = new File(tempFileName2);
        boolean switchTemp = true;

        // Creating temp files

        try {
            tempFile1.createNewFile();
            tempFile2.createNewFile();
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        // Iterating through an array of input files using merge

        for (String file : inputFileNames) {
            mergeFiles(
                    switchTemp ? tempFileName1 : tempFileName2,
                    switchTemp ? tempFileName2 : tempFileName1,
                    file
            );
            switchTemp = !switchTemp;
            System.gc();
        }

        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        ReversedLinesFileReader reversedLinesFileReader = null;

        // Writing merged data to the output file

        try {
            bufferedWriter = new BufferedWriter(new FileWriter(outputFileName));
            if (sortType.equals(SortConstants.SORT_ASC)) {
                bufferedReader = new BufferedReader(new FileReader(
                        switchTemp ? tempFileName2 : tempFileName1
                ));
                String value = bufferedReader.readLine();
                while (value != null) {
                    bufferedWriter.write(value);
                    bufferedWriter.newLine();
                    value = bufferedReader.readLine();
                }
            } else {
                reversedLinesFileReader = new ReversedLinesFileReader(
                        new File(switchTemp ? tempFileName2 : tempFileName1),
                        StandardCharsets.UTF_8
                );
                String value = reversedLinesFileReader.readLine();
                while (value != null) {
                    bufferedWriter.write(value);
                    bufferedWriter.newLine();
                    value = reversedLinesFileReader.readLine();
                }
            }
        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        } finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
                if (bufferedWriter != null) bufferedWriter.close();
                if (reversedLinesFileReader != null) reversedLinesFileReader.close();
            } catch (IOException e) {
                System.err.println("ERROR: " + e.getMessage());
            }
        }

        // Deleting temp files

        tempFile1.delete();
        tempFile2.delete();
        System.out.println("Execution is over");
    }

    // Checks the value for correctness in accordance with the specified parameters

    private String checkValue(BufferedReader br, String prevValue) throws IOException, InvalidAttributeValueException {
        String value = null;
        try {
            value = br.readLine();
            if (value == null) return null;
            if (value.contains(" ")) {
                System.err.println("WARNING: Incorrect data was found in '" + currentFile + "'." +
                        " The merge from this file has been stopped");
                throw new InvalidAttributeValueException("The string '" + value + "' contains a space or spaces");
            }
            if (value.equals("")) {
                System.err.println("WARNING: Incorrect data was found in '" + currentFile + "'." +
                        " The merge from this file has been stopped");
                throw new InvalidAttributeValueException("Found empty line. The line cannot be empty");
            }
        } catch (IOException e) {
            return null;
        }
        if (dataType.equals(SortConstants.TYPE_INTEGER)) {
            try {
                Integer.parseInt(value);
            } catch (NumberFormatException numberFormatException) {
                System.err.println("WARNING: Incorrect data was found in '" + currentFile + "'." +
                        " The merge from this file has been stopped");
                System.err.println("WARNING: The string '" + value + "' cannot be converted to a numerical value");
                try {
                    br.close();
                } catch (IOException ioException) {
                    System.err.println("ERROR: " + ioException.getMessage());
                }
                return null;
            }
        }
        if (prevValue != null && !lessThan(prevValue, value)) {
            System.err.println("WARNING: Incorrect data was found in '" + currentFile + "'." +
                    " The merge from this file has been stopped");
            System.err.println("WARNING: The sorting order in the file is broken. '" + prevValue + "' is " +
                    "bigger than '" + value + "'");
            try {
                br.close();
            } catch (IOException e) {
                System.err.println("ERROR: " + e.getMessage());
            }
            return null;
        }
        return value;
    }

    // Writing current value to the output file and returning new value

    private String writeAndCheck(BufferedWriter bw, BufferedReader br, String value, String prev) throws IOException, InvalidAttributeValueException {
        bw.write(value);
        return checkValue(br, prev);
    }

    private void mergeFiles(String output, String firstFileName, String secondFileName) {
        BufferedReader br1 = null;
        BufferedReader br2 = null;
        BufferedWriter bw = null;
        String leftValue;
        String rightValue;
        String prevLeftValue;
        String prevRightValue;

        try {
            br1 = new BufferedReader(new FileReader(firstFileName));
            br2 = new BufferedReader(new FileReader(secondFileName));
            leftValue = checkValue(br1, null);
            rightValue = checkValue(br2, null);
            bw = new BufferedWriter(new FileWriter(output));
        } catch (IOException | InvalidAttributeValueException e) {
            System.err.println("ERROR: " + e.getMessage());
            return;
        }

        try {
            while (leftValue != null && rightValue != null) {
                if (lessThan(leftValue, rightValue)) {
                    this.currentFile = firstFileName;
                    prevLeftValue = leftValue;
                    leftValue = writeAndCheck(bw, br1, leftValue, prevLeftValue);
                } else {
                    this.currentFile = secondFileName;
                    prevRightValue = rightValue;
                    rightValue = writeAndCheck(bw, br2, rightValue, prevRightValue);
                }
                bw.newLine();
            }
        } catch (IOException | InvalidAttributeValueException e) {
            System.err.println("WARNING: " + e.getMessage());
            try {
                bw.newLine();
            } catch (IOException ioException) {
                System.err.println("ERROR: " + e.getMessage());
            }
        }

        try {
            this.currentFile = firstFileName;
            while (leftValue != null) {
                prevLeftValue = leftValue;
                leftValue = writeAndCheck(bw, br1, leftValue, prevLeftValue);
                bw.newLine();
            }

            this.currentFile = secondFileName;
            while (rightValue != null) {
                prevRightValue = rightValue;
                rightValue = writeAndCheck(bw, br2, rightValue, prevRightValue);
                bw.newLine();
            }
        } catch (IOException | InvalidAttributeValueException e) {
            System.err.println("WARNING: " + e.getMessage());
        }

        try {
            if (br1 != null) br1.close();
            if (br2 != null) br2.close();
            if (bw != null) bw.close();
        } catch (IOException e) {
            System.err.println("ERROR: " + e);
        }
    }

    private boolean lessThan(String first, String second) {
        return dataType.equals(SortConstants.TYPE_INTEGER) ?
                Integer.parseInt(first) <= Integer.parseInt(second):
                (first).compareTo(second) <= 0;
    }
}
