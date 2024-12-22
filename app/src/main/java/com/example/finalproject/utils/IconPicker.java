package com.example.finalproject.utils;

import com.example.finalproject.R;

public class IconPicker {
    private static final int[] icons = {
            R.drawable.ic_icon_1,
            R.drawable.ic_icon_2,
            R.drawable.ic_icon_3,
            R.drawable.ic_icon_4,
            R.drawable.ic_icon_5,
            R.drawable.ic_icon_6,
            R.drawable.ic_icon_7,
            R.drawable.ic_icon_8
    };
    private static int currentIcon = 0;

    public static int getIcon() {
        currentIcon = (currentIcon + 1) % icons.length;
        return icons[currentIcon];
    }
}
