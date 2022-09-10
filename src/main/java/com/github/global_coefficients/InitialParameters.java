package com.github.global_coefficients;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.github.utility.BigDecimalFactory.scaled;

public final class InitialParameters {

    private InitialParameters(){
    }

    public static int BIG_DECIMAL_SCALE = 5;
    public static final RoundingMode BIG_DECIMAL_ROUNDING_MODE = RoundingMode.HALF_UP;

    public static final BigDecimal INITIAL_X_1 = scaled(-2.2);
    public static final BigDecimal INITIAL_X_2 = scaled(1.1);
    public static final BigDecimal INITIAL_Y_1 = scaled(-1.2);
    public static final BigDecimal INITIAL_Y_2 = scaled(1.2);
    public static final int INITIAL_MAX_ITER = 40;
}
