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

    public BigDecimal module_square() {
        return halfScaled(realPart).multiply(halfScaled(realPart)).add(halfScaled(imaginaryPart).multiply(halfScaled(imaginaryPart)));
    }

    public Complex add(Complex complex) {
        realPart = realPart.add(complex.getRealPart());
        imaginaryPart = imaginaryPart.add(complex.getImaginaryPart());
        return this;
    }

    public Complex multiply(Complex complex) {
        imaginaryPart = halfScaled(realPart).multiply(halfScaled(complex.imaginaryPart)).add(halfScaled(imaginaryPart).multiply(halfScaled(complex.getRealPart())));
        realPart = halfScaled(realPart).multiply(halfScaled(complex.getRealPart())).subtract(halfScaled(imaginaryPart).multiply(halfScaled(complex.getImaginaryPart())));
        return this;
    }

    @Override
    public Complex clone() {
        return new Complex(realPart, imaginaryPart);
    }
}
