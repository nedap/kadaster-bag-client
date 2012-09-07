package com.nedap.healthcare.kadasterbagclient.api.util;

/**
 * Set of conversion methods for converting different types of coordinates types.
 * 
 * @author Dusko Vesin
 */
public class CoordinatesConverterUtil {

    // public static final double a01 = 3236.0331637;
    // public static final double a20 = -32.5915821;
    // public static final double a02 = -0.2472814;
    // public static final double a21 = -0.8501341;
    // public static final double a03 = -0.0655238;
    // public static final double a22 = -0.0171137;
    // public static final double a40 = 0.0052771;
    // public static final double a23 = -0.0003859;
    // public static final double a41 = 0.0003314;
    // public static final double a04 = 0.0000371;
    // public static final double a42 = 0.0000143;
    // public static final double a24 = -0.0000090;
    //
    // public static final double b10 = 5261.3028966;
    // public static final double b11 = 105.9780241;
    // public static final double b12 = 2.4576469;
    // public static final double b30 = -0.8192156;
    // public static final double b31 = -0.0560092;
    // public static final double b13 = 0.0560089;
    // public static final double b32 = -.0025614;
    // public static final double b14 = 0.0012770;
    // public static final double b50 = 0.0002574;
    // public static final double b33 = -0.0000973;
    // public static final double b51 = 0.0000293;
    // public static final double b15 = 0.0000291;
    //
    // public static final double c01 = 190066.98903;
    // public static final double c11 = -11830.85831;
    // public static final double c21 = -114.19754;
    // public static final double c03 = -32.38360;
    // public static final double c31 = -2.34078;
    // public static final double c13 = -0.60639;
    // public static final double c23 = 0.15774;
    // public static final double c41 = -0.04158;
    // public static final double c05 = -0.00661;
    //
    // public static final double d10 = 309020.31810;
    // public static final double d02 = 3638.36193;
    // public static final double d12 = -157.95222;
    // public static final double d20 = 72.97141;
    // public static final double d30 = 59.79734;
    // public static final double d22 = -6.43481;
    // public static final double d04 = 0.09351;
    // public static final double d32 = -0.07379;
    // public static final double d14 = -0.05419;
    // public static final double d40 = -0.03444;
    //
    // /**
    // * Convert from Rijksdriehoeksmeting coordinates to Amersfoort coordinates using formulas:
    // *
    // * dX = (X−X0)10−5 ; X0 = 155000
    // *
    // * dY = (Y−Yo)10−5 ; Y0 = 463000
    // *
    // * Converter from Amersfoort coordinates to Bassel coordinates using formulas:
    // *
    // * φ = φ0 + dφ ; φ0 = 187762,178
    // *
    // * λ = λ0 + dλ ; λ0 = 19395,500
    // *
    // *
    // * dφ = a01dY + a20dX^2 + a02dY^2 + a21dX^2dY + a03dY^3 + a20dX^2 + a22dX^2dY^2 +a04dY^4 +a41dX^4dY +a23dX^2dY^3
    // * +a42dX^4dY^2 +a24dX^2dY^4
    // *
    // * dλ = b10dX + b11dXdY + b30dX3 + b12dXdY^2 + b31dX^3dY + b13dXdY^3 + b50dX^5 +b32dX^3dY^2 +b14dXdY^4 +b51dX^5dY
    // * +b33dX^3dY^3+ b15dXdY^5
    // *
    // *
    // * @param coordinates
    // * @return
    // */
    // public static BasselCoordinates transformRijksdriehoeksmetingToBassel(final RDCoordinates coordinates) {
    //
    // double d = coordinates.getX() - 155000;
    // double dX = d * pow(10, -5);
    // double e = coordinates.getY() - 463000;
    // double dY = e * pow(10, -5);
    //
    // double f0 = a01 * dY + a20 * pow(dX, 2) + a02 * pow(dY, 2) + a21 * pow(dX, 2) * dY + a03 * pow(dY, 3) + a20
    // * pow(dX, 2) + a22 * pow(dX, 2) * pow(dY, 2) + a04 * pow(dY, 4) + a41 * pow(dX, 4) * dY + a23
    // * pow(dX, 2) * pow(dY, 3) + a42 * pow(dX, 4) * pow(dY, 2) + a24 * pow(dX, 2) * pow(dY, 4);
    //
    // double a0 = b10 * dX + b11 * dX * dY + b30 * pow(dX, 3) + b12 * dX * pow(dY, 2) + b31 * pow(dX, 3) * dY + b13
    // * dX * pow(dY, 3) + b50 * pow(dX, 5) + b32 * pow(dX, 3) * pow(dY, 2) + b14 * dX * pow(dY, 4) + b51
    // * pow(dX, 5) * dY + b33 * pow(dX, 3) * pow(dY, 3) + b15 * dX * pow(dY, 5);
    //
    // double f = f0 + 187762.178;
    // double a = a0 + 19395.500;
    //
    // return new BasselCoordinates(a, f);
    //
    // }

    // TODO fix this code

    // module 2 coördinatenconversie
    // Stevenhagen Geo Informatica Zoetermeer (c)2004
    // Ed Stevenhagen Velddreef 293 2727CH Zoetermeer

    // ==========================
    // phi Labda pseudo Rdx Rdy x[NL_RD] y[NL_RD]
    // ==========================

    static double[] A = new double[12];
    static double[] a1 = new double[12];
    static double[] a2 = new double[12];
    static double[] B = new double[12];
    static double[] b1 = new double[12];
    static double[] b2 = new double[12];
    static double[] C = new double[12];
    static double[] c1 = new double[12];
    static double[] c2 = new double[12];
    static double[] D = new double[12];
    static double[] d1 = new double[12];
    static double[] d2 = new double[12];

    static {

        A[0] = 3236.0331637;
        a1[0] = 0;
        a2[0] = 1;
        B[0] = 5261.3028966;
        b1[0] = 1;
        b2[0] = 0;
        A[1] = -32.5915821;
        a1[1] = 2;
        a2[1] = 0;
        B[1] = 105.9780241;
        b1[1] = 1;
        b2[1] = 1;
        A[2] = -0.2472814;
        a1[2] = 0;
        a2[2] = 2;
        B[2] = 2.4576469;
        b1[2] = 1;
        b2[2] = 2;
        A[3] = -0.8501341;
        a1[3] = 2;
        a2[3] = 1;
        B[3] = -0.8192156;
        b1[3] = 3;
        b2[3] = 0;
        A[4] = -0.0655238;
        a1[4] = 0;
        a2[4] = 3;
        B[4] = -0.0560092;
        b1[4] = 3;
        b2[4] = 1;
        A[5] = -0.0171137;
        a1[5] = 2;
        a2[5] = 2;
        B[5] = 0.0560089;
        b1[5] = 1;
        b2[5] = 3;
        A[6] = 0.0052771;
        a1[6] = 4;
        a2[6] = 0;
        B[6] = -0.0025614;
        b1[6] = 3;
        b2[6] = 2;
        A[7] = -0.0003859;
        a1[7] = 2;
        a2[7] = 3;
        B[7] = 0.0012770;
        b1[7] = 1;
        b2[7] = 4;
        A[8] = 0.0003314;
        a1[8] = 4;
        a2[8] = 1;
        B[8] = 0.0002574;
        b1[8] = 5;
        b2[8] = 0;
        A[9] = 0.0000371;
        a1[9] = 0;
        a2[9] = 4;
        B[9] = -0.0000973;
        b1[9] = 3;
        b2[9] = 3;
        A[10] = 0.0000143;
        a1[10] = 4;
        a2[10] = 2;
        B[10] = 0.0000293;
        b1[10] = 5;
        b2[10] = 1;
        A[11] = -0.0000090;
        a1[11] = 2;
        a2[11] = 4;
        B[11] = 0.0000291;
        b1[11] = 1;
        b2[11] = 5;

        C[0] = 190066.98903;
        c1[0] = 0;
        c2[0] = 1;
        D[0] = 309020.31810;
        d1[0] = 1;
        d2[0] = 0;
        C[1] = -11830.85831;
        c1[1] = 1;
        c2[1] = 1;
        D[1] = 3638.36193;
        d1[1] = 0;
        d2[1] = 2;
        C[2] = -114.19754;
        c1[2] = 2;
        c2[2] = 1;
        D[2] = -157.95222;
        d1[2] = 1;
        d2[2] = 2;
        C[3] = -32.38360;
        c1[3] = 0;
        c2[3] = 3;
        D[3] = 72.97141;
        d1[3] = 2;
        d2[3] = 0;
        C[4] = -2.34078;
        c1[4] = 3;
        c2[4] = 1;
        D[4] = 59.79734;
        d1[4] = 3;
        d2[4] = 0;
        C[5] = -0.606039;
        c1[5] = 1;
        c2[5] = 3;
        D[5] = -6.43481;
        d1[5] = 2;
        d2[5] = 2;
        C[6] = 0.15774;
        c1[6] = 2;
        c2[6] = 3;
        D[6] = 0.09351;
        d1[6] = 0;
        d2[6] = 4;
        C[7] = -0.04158;
        c1[7] = 4;
        c2[7] = 1;
        D[7] = -0.07379;
        d1[7] = 3;
        d2[7] = 2;
        C[8] = -0.00661;
        c1[8] = 0;
        c2[8] = 5;
        D[8] = -0.05419;
        d1[8] = 1;
        d2[8] = 4;

    }

    // Coëfficienten reeksontwikkeling dubbelprojectie van Schreiber

    public static BasselCoordinates transformRijksdriehoeksmetingToBassel(final RDCoordinates coordinates) {

        double dx = 0; // verschil in RDx met Amersfoort in meter
        double dy = 0; // verschil in RDy met Amersfoort in meter
        double dX = 0; // dx / 100 km
        double dY = 0; // dy / 100 km
        double dPhi = 0; // verschil in breedte met Amersfoort in sec / 10000
        double dLabda = 0; // verschil in lengte met Amersfoort in sec / 10000
        double gamma = 0; // meridiaanconvergentie in mgon

        int i = 0;
        int j = 0;

        dX = (coordinates.getX() - 155000.00) / 100000;
        dY = (coordinates.getY() - 463000.00) / 100000;

        gamma = 1282.3383 * dX + 33.6454 * dX * dY + 0.7564 * dX * dY * dY;
        gamma += -0.2521 * dX * dX * dX + 0.0173 * dX * dY * dY * dY - 0.0173 * dX * dX * dX * dY;

        dPhi = 0;
        dLabda = 0;
        for (i = 0; i < 12; i++) {
            dPhi += A[i] * Math.pow(dX, a1[i]) * Math.pow(dY, a2[i]);
        }

        for (i = 0; i < 12; i++) {
            dLabda += B[i] * Math.pow(dX, b1[i]) * Math.pow(dY, b2[i]);
        }

        dPhi = secGrad(dPhi) + dmsGrad(52, 9, 22.178);
        dLabda = secGrad(dLabda) + dmsGrad(5, 23, 15.500);

        return new BasselCoordinates(dLabda, dPhi);

    }

    private static double secGrad(Double seconds) {
        return (seconds / 3600);
    }

    private static double dmsGrad(double gra, double min, double sec) {
        return (gra + min / 60 + sec / 3600);
    }

}
