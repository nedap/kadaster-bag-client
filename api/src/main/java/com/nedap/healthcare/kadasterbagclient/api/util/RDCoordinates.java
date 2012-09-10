package com.nedap.healthcare.kadasterbagclient.api.util;

/**
 * Location coordinates object.
 * 
 * @author Dusko Vesin
 * 
 */
public class RDCoordinates {

    private final Double x;
    private final Double y;

    public RDCoordinates(final Double x, final Double y) {
        super();
        this.x = x;
        this.y = y;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

}
