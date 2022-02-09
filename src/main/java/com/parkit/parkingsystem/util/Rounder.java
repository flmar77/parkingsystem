package com.parkit.parkingsystem.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class Rounder {
    public static double round(final double value, final int decimals) {
        if (decimals < 0) {
            throw new IllegalArgumentException("invalid decimals");
        }

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(decimals, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
