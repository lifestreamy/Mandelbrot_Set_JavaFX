package com.github.types;

import java.math.BigDecimal;

public class Complex implements Cloneable {
    private BigDecimal realPart;
    private BigDecimal imaginaryPart;

    public BigDecimal getRealPart() {
        return realPart;
    }

    public BigDecimal getImaginaryPart() {
        return imaginaryPart;
    }

    public Complex(BigDecimal realPart, BigDecimal imaginaryPart) {
        this.realPart = realPart;
        this.imaginaryPart = imaginaryPart;
    }

    public BigDecimal module_square() {
        return realPart.multiply(realPart).add(imaginaryPart.multiply(imaginaryPart));
    }

    public Complex add(Complex complex) {
        realPart = realPart.add(complex.getRealPart());
        imaginaryPart = imaginaryPart.add(complex.getImaginaryPart());
        return this;
    }

    public Complex multiply(Complex complex) {
        imaginaryPart = realPart.multiply(complex.imaginaryPart).add(imaginaryPart.multiply(complex.getRealPart()));
        realPart = realPart.multiply(complex.getRealPart()).subtract(imaginaryPart.multiply(complex.getImaginaryPart()));
        return this;
    }

    @Override
    public Complex clone() {
        return new Complex(realPart, imaginaryPart);
    }
}
