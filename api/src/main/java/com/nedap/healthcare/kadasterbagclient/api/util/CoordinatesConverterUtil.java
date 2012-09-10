package com.nedap.healthcare.kadasterbagclient.api.util;

import static java.lang.Math.pow;

/**
 * Set of conversion methods for converting different types of coordinates types.
 * 
 * @author Dusko Vesin
 */
public class CoordinatesConverterUtil {

    public static final double a01 = 3236.0331637;
    public static final double a20 = -32.5915821;
    public static final double a02 = -0.2472814;
    public static final double a21 = -0.8501341;
    public static final double a03 = -0.0655238;
    public static final double a22 = -0.0171137;
    public static final double a40 = 0.0052771;
    public static final double a23 = -0.0003859;
    public static final double a41 = 0.0003314;
    public static final double a04 = 0.0000371;
    public static final double a42 = 0.0000143;
    public static final double a24 = -0.0000090;

    public static final double b10 = 5261.3028966;
    public static final double b11 = 105.9780241;
    public static final double b12 = 2.4576469;
    public static final double b30 = -0.8192156;
    public static final double b31 = -0.0560092;
    public static final double b13 = 0.0560089;
    public static final double b32 = -0.0025614;
    public static final double b14 = 0.0012770;
    public static final double b50 = 0.0002574;
    public static final double b33 = -0.0000973;
    public static final double b51 = 0.0000293;
    public static final double b15 = 0.0000291;

    /**
     * Convert from Rijksdriehoeksmeting coordinates to Amersfoort coordinates using formulas:
     * 
     * dX = (X−X0)10−5 ; X0 = 155000
     * 
     * dY = (Y−Yo)10−5 ; Y0 = 463000
     * 
     * Converter from Amersfoort coordinates to Bassel coordinates using formulas:
     * 
     * φ = φ0 + dφ ; φ0 = 187762,178
     * 
     * λ = λ0 + dλ ; λ0 = 19395,500
     * 
     * 
     * dφ = a01dY + a20dX^2 + a02dY^2 + a21dX^2dY + a03dY^3 + a20dX^2 + a22dX^2dY^2 +a04dY^4 +a41dX^4dY +a23dX^2dY^3
     * +a42dX^4dY^2 +a24dX^2dY^4
     * 
     * dλ = b10dX + b11dXdY + b30dX3 + b12dXdY^2 + b31dX^3dY + b13dXdY^3 + b50dX^5 +b32dX^3dY^2 +b14dXdY^4 +b51dX^5dY
     * +b33dX^3dY^3+ b15dXdY^5
     * 
     * Formulas did not return apropriate result so we used code from javascript that has been sent to us as a
     * reference.
     * 
     * This calculation stayed the same :
     * 
     * dX = (X−X0)10−5 ; X0 = 155000
     * 
     * dY = (Y−Yo)10−5 ; Y0 = 463000
     * 
     * 
     * Here we used functions instead of constant value :
     * 
     * φ = secGrad(dφ) + dmsGrad(52, 9, 22.178) ;
     * 
     * λ = secGrad(dλ) + dmsGrad(5, 23, 15.500) ;
     * 
     * 
     * One parameter is omitted (a20 was used twice), we added missing a40 :
     * 
     * dφ = a01dY + a20dX^2 + a02dY^2 + a21dX^2dY + a03dY^3 + a40dX^4 + a22dX^2dY^2 +a04dY^4 +a41dX^4dY +a23dX^2dY^3
     * +a42dX^4dY^2 +a24dX^2dY^4
     * 
     * dλ = b10dX + b11dXdY + b30dX3 + b12dXdY^2 + b31dX^3dY + b13dXdY^3 + b50dX^5 +b32dX^3dY^2 +b14dXdY^4 +b51dX^5dY
     * +b33dX^3dY^3+ b15dXdY^5
     * 
     * 
     * @param coordinates
     * @return
     */
    public static BasselCoordinates transformRijksdriehoeksmetingToBassel(final RDCoordinates coordinates) {

        double d = coordinates.getX() - 155000;
        double dX = d * pow(10, -5);
        double e = coordinates.getY() - 463000;
        double dY = e * pow(10, -5);

        double f0 = a01 * dY + a20 * pow(dX, 2) + a02 * pow(dY, 2) + a21 * pow(dX, 2) * dY + a03 * pow(dY, 3) + a22
                * pow(dX, 2) * pow(dY, 2) + a40 * pow(dX, 4) + a23 * pow(dX, 2) * pow(dY, 3) + a41 * pow(dX, 4) * dY
                + a04 * pow(dY, 4) + a42 * pow(dX, 4) * pow(dY, 2) + a24 * pow(dX, 2) * pow(dY, 4);

        double a0 = b10 * dX + b11 * dX * dY + b12 * dX * pow(dY, 2) + b30 * pow(dX, 3) + b31 * pow(dX, 3) * dY + b13
                * dX * pow(dY, 3) + b32 * pow(dX, 3) * pow(dY, 2) + b14 * dX * pow(dY, 4) + b50 * pow(dX, 5) + b33
                * pow(dX, 3) * pow(dY, 3) + b51 * pow(dX, 5) * dY + b15 * dX * pow(dY, 5);

        double f = secGrad(f0) + dmsGrad(52, 9, 22.178);
        double a = secGrad(a0) + dmsGrad(5, 23, 15.500);
        // double f = f0 + 187762.178;
        // double a = a0 + 19395.500;

        return new BasselCoordinates(a, f);

    }

    private static double secGrad(Double seconds) {
        return (seconds / 3600);
    }

    private static double dmsGrad(double gra, double min, double sec) {
        return (gra + min / 60 + sec / 3600);
    }

}
