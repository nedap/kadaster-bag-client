package com.nedap.healthcare.kadasterbagclient.api.util;

public class BasselCoordinates {

    private final Double a;
    private final Double f;

    public BasselCoordinates(final Double a, final Double f) {
        super();
        this.a = a;
        this.f = f;
    }

    public Double getA() {
        return a;
    }

    public Double getF() {
        return f;
    }

}
