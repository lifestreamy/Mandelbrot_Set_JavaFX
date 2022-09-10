package com.github.types;

import java.math.BigDecimal;
import java.util.Objects;

public class ViewState {

    public BigDecimal getX1() {
        return x1;
    }

    public BigDecimal getX2() {
        return x2;
    }

    public BigDecimal getY1() {
        return y1;
    }

    public BigDecimal getY2() {
        return y2;
    }

    public int getMaxIter() {
        return maxIter;
    }

    public int getBigDecimalScale() {
        return bigDecimalScale;
    }

    private final BigDecimal x1, x2, y1, y2;

    private final int maxIter, bigDecimalScale;


    public ViewState(BigDecimal x1, BigDecimal x2, BigDecimal y1, BigDecimal y2, int maxIter, int bigDecimalScale) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.maxIter = maxIter;
        this.bigDecimalScale = bigDecimalScale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ViewState newViewState = (ViewState) o;
        return ((newViewState.getX1().toString().equals(x1.toString())))
                && ((newViewState.getY1().toString().equals(y1.toString())))
                && ((newViewState.getX2().toString().equals(x2.toString())))
                && ((newViewState.getY2().toString().equals(y2.toString())))
                && (maxIter == newViewState.maxIter) && (bigDecimalScale == newViewState.bigDecimalScale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x1, x2, y1, y2, maxIter, bigDecimalScale);
    }
}
