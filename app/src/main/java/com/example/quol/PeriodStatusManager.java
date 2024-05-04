package com.example.quol;

import java.util.HashMap;

public class PeriodStatusManager {
    private static HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>> YearHash = new HashMap<>();

    public static HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>> getYearHash() {
        return YearHash;
    }

    public static void setYearHash(HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>> yearHash) {
        YearHash = yearHash;
    }


}


