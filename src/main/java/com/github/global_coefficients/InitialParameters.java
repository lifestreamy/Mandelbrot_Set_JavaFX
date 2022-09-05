package com.github.global_coefficients;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.github.utility.BigDecimalFactory.scaled;

public final class InitialParameters {

    private InitialParameters(){
    }

    public static int BIG_DECIMAL_SCALE = 20;
    public static final RoundingMode BIG_DECIMAL_ROUNDING_MODE = RoundingMode.HALF_UP;

    public static final BigDecimal initialX1 = scaled(-2);
    public static final BigDecimal initialX2 = scaled(1);
    public static final BigDecimal initialY1 = scaled(-1);
    public static final BigDecimal initialY2 = scaled(1);
    public static final int initialMaxIter = 1000;
}
