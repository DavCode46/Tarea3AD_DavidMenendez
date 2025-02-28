package com.davidmb.tarea3ADbase.utils;

import java.text.Normalizer; 

public class StringUtils { 
    public static String normalize(String input) {
        if (input == null) return null;
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                         .replaceAll("\\p{M}", "")
                         .toLowerCase();
    }
}
