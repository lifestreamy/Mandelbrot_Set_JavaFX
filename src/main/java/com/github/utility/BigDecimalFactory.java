package com.github.utility;

import java.math.BigDecimal;

import static com.github.global_coefficients.InitialParameters.BIG_DECIMAL_ROUNDING_MODE;
import static com.github.global_coefficients.InitialParameters.BIG_DECIMAL_SCALE;

public final class BigDecimalFactory {

    private BigDecimalFactory() {
    }

    public static BigDecimal scaled(BigDecimal val) {
        return val.setScale(BIG_DECIMAL_SCALE, BIG_DECIMAL_ROUNDING_MODE);
    }

    public static BigDecimal scaled(String val) {
        BigDecimal x = new BigDecimal(val);
        return x.setScale(BIG_DECIMAL_SCALE, BIG_DECIMAL_ROUNDING_MODE);
    }

    public static BigDecimal scaled(int val) {
        BigDecimal x = new BigDecimal(val);
        return x.setScale(BIG_DECIMAL_SCALE, BIG_DECIMAL_ROUNDING_MODE);
    }


    public static BigDecimal halfScaled(BigDecimal val) {
        return val.setScale(BIG_DECIMAL_SCALE/2, BIG_DECIMAL_ROUNDING_MODE);
    }


}
