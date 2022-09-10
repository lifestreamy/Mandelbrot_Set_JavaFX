package com.github.types;

import java.math.BigDecimal;

import static com.github.utility.BigDecimalFactory.halfScaled;

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

    public BigDecimal module_square_half_scaled() {
        return halfScaled(realPart).multiply(halfScaled(realPart)).add(halfScaled(imaginaryPart).multiply(halfScaled(imaginaryPart)));
    }

    public BigDecimal module_square_full_scaled() {
        return realPart.multiply(realPart).add(imaginaryPart.multiply(imaginaryPart));
    }

    public Complex add(Complex complex) {
        realPart = realPart.add(complex.getRealPart());
        imaginaryPart = imaginaryPart.add(complex.getImaginaryPart());
        return this;
    }

    public Complex multiply_half_scaled(Complex complex) {
        BigDecimal real = halfScaled(realPart).multiply(halfScaled(complex.getRealPart())).subtract(halfScaled(imaginaryPart).multiply(halfScaled(complex.getImaginaryPart())));
        imaginaryPart = halfScaled(realPart).multiply(halfScaled(complex.imaginaryPart)).add(halfScaled(imaginaryPart).multiply(halfScaled(complex.getRealPart())));
        realPart = real;
        return this;
    }

    public Complex multiply_full_scaled(Complex complex) {
        BigDecimal real = realPart.multiply(complex.getRealPart()).subtract(imaginaryPart.multiply(complex.getImaginaryPart()));
        imaginaryPart = realPart.multiply(complex.imaginaryPart).add(imaginaryPart.multiply(complex.getRealPart()));
        realPart = real;
        return this;
    }

    @Override
    public Complex clone() {
        return new Complex(realPart, imaginaryPart);
    }
}
