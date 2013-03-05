package com.nedap.healthcare.kadasterbagclient.api.util;

/**
 * Set of conversion methods for converting different types of coordinates
 * types.
 * 
 * @author Dusko Vesin
 * @author Srdjan Radulovic
 */
public class CoordinatesConverterUtil {

	private static final double X0 = 155000;
	private static final double Y0 = 463000;
	private static final double LONGITUDE_OFFSET = 5.38720621;
	private static final double LATITUDE_OFFSET = 52.1551744;
	private static final int CONVERSION_CONSTANT = 3600;
	private final static double[][] LATITUDE_COEFICIENTS = new double[][] {
			{ 3235.65389, 0, 1 }, { -32.58297, 2, 0 }, { -0.2475, 0, 2 },
			{ -0.84978, 2, 1 }, { -0.0665, 0, 3 }, { -0.01709, 2, 2 },
			{ -0.00738, 1, 0 }, { 0.0053, 4, 0 }, { -0.00039, 2, 3 },
			{ 0.00033, 4, 1 }, { -0.00012, 1, 1 } };
	private final static double[][] LONGITUDE_COEFICIENTS = new double[][] {
			{ 5260.52916, 1, 0 }, { 105.94684, 1, 1 }, { 2.45656, 1, 2 },
			{ -0.81885, 3, 0 }, { 0.05594, 1, 3 }, { -0.05607, 3, 1 },
			{ 0.01199, 0, 1 }, { -0.00256, 3, 2 }, { 0.00128, 1, 4 },
			{ 0.00022, 0, 2 }, { -0.00022, 2, 0 }, { 0.00026, 5, 0 } };

	/**
	 * Convert from Rijksdriehoeksmeting coordinates to Amersfoort coordinates
	 * using formulas:
	 * 
	 * dX = (X−X0)10−5 ; X0 = 155000
	 * 
	 * dY = (Y−Yo)10−5 ; Y0 = 463000
	 * 
	 * Converter from Amersfoort coordinates to Bassel coordinates using
	 * formulas:
	 * 
	 * φ = φ0 + dφ ; φ0 = 187762,178
	 * 
	 * λ = λ0 + dλ ; λ0 = 19395,500
	 * 
	 * 
	 * dφ = a01dY + a20dX^2 + a02dY^2 + a21dX^2dY + a03dY^3 + a20dX^2 +
	 * a22dX^2dY^2 +a04dY^4 +a41dX^4dY +a23dX^2dY^3 +a42dX^4dY^2 +a24dX^2dY^4
	 * 
	 * dλ = b10dX + b11dXdY + b30dX3 + b12dXdY^2 + b31dX^3dY + b13dXdY^3 +
	 * b50dX^5 +b32dX^3dY^2 +b14dXdY^4 +b51dX^5dY +b33dX^3dY^3+ b15dXdY^5
	 * 
	 * Formulas did not return apropriate result so we used code from javascript
	 * that has been sent to us as a reference.
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
	 * dφ = a01dY + a20dX^2 + a02dY^2 + a21dX^2dY + a03dY^3 + a40dX^4 +
	 * a22dX^2dY^2 +a04dY^4 +a41dX^4dY +a23dX^2dY^3 +a42dX^4dY^2 +a24dX^2dY^4
	 * 
	 * dλ = b10dX + b11dXdY + b30dX3 + b12dXdY^2 + b31dX^3dY + b13dXdY^3 +
	 * b50dX^5 +b32dX^3dY^2 +b14dXdY^4 +b51dX^5dY +b33dX^3dY^3+ b15dXdY^5
	 * 
	 * 
	 * @param coordinates
	 * @return
	 */
	public static BasselCoordinates transformRijksdriehoeksmetingToBassel(
			final RDCoordinates coordinates) {

		final double f = calculateLatitude(coordinates.getX(),
				coordinates.getY());
		final double a = calculateLongitude(coordinates.getX(),
				coordinates.getY());

		return new BasselCoordinates(a, f);

	}

	private static double calculateLatitude(final double b, final double c) {
		double a = 0;
		final double dX = 1E-5 * (b - X0);
		final double dY = 1E-5 * (c - Y0);

		for (int i = 0; 11 > i; i++)
			a += LATITUDE_COEFICIENTS[i][0]
					* Math.pow(dX, LATITUDE_COEFICIENTS[i][1])
					* Math.pow(dY, LATITUDE_COEFICIENTS[i][2]);

		return LATITUDE_OFFSET + a / CONVERSION_CONSTANT;
	}

	private static double calculateLongitude(final double b, final double c) {
		double a = 0;
		final double dX = 1E-5 * (b - X0);
		final double dY = 1E-5 * (c - Y0);

		for (int i = 0; 12 > i; i++)
			a += LONGITUDE_COEFICIENTS[i][0]
					* Math.pow(dX, LONGITUDE_COEFICIENTS[i][1])
					* Math.pow(dY, LONGITUDE_COEFICIENTS[i][2]);

		return LONGITUDE_OFFSET + a / CONVERSION_CONSTANT;
	}

}
