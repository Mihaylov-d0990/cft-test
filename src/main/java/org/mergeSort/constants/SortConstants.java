package org.mergeSort.constants;

import java.util.List;

public class SortConstants {
    public static final String SORT_ASC = "-a";
    public static final String SORT_DESC = "-d";
    public static final String TYPE_STRING = "-s";
    public static final String TYPE_INTEGER = "-i";
    public static List<String> sortFlags = List.of(SORT_ASC, SORT_DESC);
    public static List<String> typeFlags = List.of(TYPE_STRING, TYPE_INTEGER);
    public static List<String> allAvailableFlags = List.of(SORT_ASC, SORT_DESC, TYPE_STRING, TYPE_INTEGER);

    private SortConstants() {}
}
