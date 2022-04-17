package com.liuxiang.vrp.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MyNumber {
    public static final double MIN_VALUE = 0.0001;
    public static final double MAX_VALUE = 999999999.0;
    public static double round(double value, int decimalPlaces){
        return new BigDecimal(value).setScale(decimalPlaces, RoundingMode.HALF_UP).doubleValue();
    }
}
