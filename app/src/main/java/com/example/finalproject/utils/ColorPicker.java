package com.example.finalproject.utils;
// Color Chooser class

public class ColorPicker {
    public static String[] colors = {"#FFCC99", "#598adb", "#1eb776", "#EFCC59", "#B981db", "#1Fb726"};
    static int  currentColor = 0;

    public static String getColor(){
        currentColor = (currentColor+1) % colors.length;
        return colors[currentColor];
    }
}