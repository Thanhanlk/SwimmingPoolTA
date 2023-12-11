package com.swimmingpool.common.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NumberUtil {

    public double toDouble(Object obj, Double valDefault) {
        try {
            return Double.parseDouble(obj.toString());
        } catch (Exception ex) {
            return valDefault;
        }
    }

    public double toDouble(Object obj) {
        return toDouble(obj, 0.0);
    }

    public int toInt(Object obj) {
        return Double.valueOf(toDouble(obj)).intValue();
    }
}
