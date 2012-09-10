package com.nedap.healthcare.kadasterbagclient.api.util;

import org.junit.Test;

import com.nedap.healthcare.kadasterbagclient.api.AbstractSpringTest;

/**
 * {@link CoordinatesConverterUtil} test.
 * 
 * @author Dusko Vesin
 * 
 */
public class CoordinatesConverterUtilTest extends AbstractSpringTest {

    /**
     * Testing {@link CoordinatesConverterUtil#transformRijksdriehoeksmetingToBassel(RDCoordinates)}.
     * 
     * RD-coördinaten X = 212345,678 Y = 423456,789
     * 
     * Bessel-coördinaten φ = 51o 47' 51,9044"N φ = 51,79775122 N λ = 06o 13' 08,6659” E λ = 06,21907386 E
     */
    @Test
    public void testTransformRijksdriehoeksmetingToBassel() {

        BasselCoordinates bassel = CoordinatesConverterUtil.transformRijksdriehoeksmetingToBassel(new RDCoordinates(
                212345.678, 423456.789));

        assertEquals(51.79775122, bassel.getF(), 0.00001);
        assertEquals(06.21907386, bassel.getA(), 0.00001);

    }
}
