package org.seuksa.frmk.tools;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public final class MathUtils {

	/**
	 * zero as a double.
	 */
	private static final double ZEROd = 0.0d;
	
	/**
	 * comparison tolerance.
	 */
	public static double EPSILON_COMPARISON = 0.0000000001d;
	
		
	/**
	 * Tells if the given double equals zero with a tolerance of
	 * {@link #EPSILON_COMPARISON}.
	 * 
	 * @param a operand
	 * @return a equals 0 or not.
	 */
	public boolean equalsZero(double a) {
		return equals(a, ZEROd);
	}
	
	/**
	 * Tells if two doubles are equal with a tolerance of
	 * {@link #EPSILON_COMPARISON}.
	 * 
	 * @param a first operand
	 * @param b second operand
	 * @return a equals b or not.
	 */
	public boolean equals(double a, double b) {
		return (Math.abs(a - b) < EPSILON_COMPARISON);
	}
	
	/**
	 * Improved rounding amount method. <br>
	 * Return double <code>ref</code> rounded to <code>2</code> decimals.
	 * This method overrides the native java {@link java.lang.Math#round} and
	 * {@link java.text.DecimalFormat#format} methods bugs due to the floating
	 * point precision.
	 * 
	 * @param ref reference value
	 * @param nbOfDec number decimal to round to
	 * @return rounded value
	 * @throws NumberFormatException if a number parsing problem occurred
	 */
	public static double roundAmountTo(double ref) throws NumberFormatException {
		return roundTo(ref, 2);
	}
	
	/**
	 * Improved rounding amount method. <br>
	 * Return double <code>ref</code> rounded to <code>2</code> decimals.
	 * This method overrides the native java {@link java.lang.Math#round} and
	 * {@link java.text.DecimalFormat#format} methods bugs due to the floating
	 * point precision.
	 * 
	 * @param ref reference value
	 * @param nbOfDec number decimal to round to
	 * @return rounded value
	 * @throws NumberFormatException if a number parsing problem occurred
	 */
	public static Double roundAmountTo(Double ref) throws NumberFormatException {
		return ref == null ? null : roundTo(ref, 2);
	}
	
	/**
	 * Improved rounding rate method. <br>
	 * Return double <code>ref</code> rounded to <code>4</code> decimals.
	 * This method overrides the native java {@link java.lang.Math#round} and
	 * {@link java.text.DecimalFormat#format} methods bugs due to the floating
	 * point precision.
	 * 
	 * @param ref reference value
	 * @param nbOfDec number decimal to round to
	 * @return rounded value
	 * @throws NumberFormatException if a number parsing problem occurred
	 */
	public static double roundRateTo(double ref) throws NumberFormatException {
		return roundTo(ref, 4);
	}
	
	/**
	 * Improved rounding method. <br>
	 * Return double <code>ref</code> rounded to <code>nbOfDec</code> decimals.
	 * This method overrides the native java {@link java.lang.Math#round} and
	 * {@link java.text.DecimalFormat#format} methods bugs due to the floating
	 * point precision.
	 * 
	 * @param ref reference value
	 * @param nbOfDec number decimal to round to
	 * @return rounded value
	 * @throws NumberFormatException if a number parsing problem occures
	 */
	public static double roundTo(double ref, int nbOfDec) throws NumberFormatException {
		try {
			double usedRef = ref;
			if (Double.isNaN(ref)) {
				usedRef = 0.0;
			}
			if (Double.isInfinite(ref)) {
				if (ref == Double.NEGATIVE_INFINITY) {
					usedRef = Double.MIN_VALUE;
				} else {
					usedRef = Double.MAX_VALUE;
				}
			}

			int pointPos = -1;
			String sRef = "" + usedRef;
			if ((pointPos = sRef.indexOf('.')) > 0) {
				String decPart = sRef.substring(pointPos);
				if (decPart.length() == nbOfDec + 2) {
					usedRef += getTrailingFive(nbOfDec);
				}
			}
			
			DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
			DecimalFormat df = new DecimalFormat("######.##", symbols);
			df.setMaximumFractionDigits(nbOfDec);
			df.setGroupingUsed(false);
			df.getDecimalFormatSymbols().setDecimalSeparator('.');
			return Double.parseDouble(df.format(usedRef));
		} catch (NumberFormatException exp) {
			throw exp;
		}
	}
	
	/**
	 * Returns a trailing 5 at 'offset' + 1 decimal place. <br>
	 * For example, if you specify an offset of 3, 0.0005 will be returned
	 * 
	 * @param offset completion position
	 * @return desired double
	 */
	private static double getTrailingFive(int offset) {
		double res = 5 / Math.pow(10, offset + 2);
		return res;
	}
	
	
}
