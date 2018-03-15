package com.example.sell.util;

public class Strings {
    public static String EMPTY = "";
    public static String toString(Object obj){
        return obj == null? EMPTY : obj.toString();
    }
}
