package com.github.global_coefficients;

import java.math.BigDecimal;

import static com.github.global_coefficients.InitialParameters.BIG_DECIMAL_ROUNDING_MODE;
import static com.github.utility.BigDecimalFactory.scaled;

public final class CanvasProperties {

    private CanvasProperties() {
    }

    //    public static BigDecimal SCALING_FACTOR = new BigDecimal("1");
    public static final int CANVAS_WIDTH = 900;
    public static final int CANVAS_HEIGHT = 600;
    public static final Double CANVAS_DIAGONAL_FACTOR = (double) CANVAS_HEIGHT / CANVAS_WIDTH;

    public static final BigDecimal CANVAS_WIDTH_BIG_DECIMAL = scaled(CANVAS_WIDTH);

    public static final BigDecimal CANVAS_HEIGHT_BIG_DECIMAL = scaled(CANVAS_HEIGHT);
    //
    public static final BigDecimal CANVAS_DIAGONAL_FACTOR_BIG_DECIMAL = CANVAS_HEIGHT_BIG_DECIMAL.divide(CANVAS_WIDTH_BIG_DECIMAL, BIG_DECIMAL_ROUNDING_MODE);

}
