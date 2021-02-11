package org.jcryptool.core.util.units;



import java.math.*;

/**
 * Provides some mathematical operations on {@code BigDecimal} and {@code BigInteger}.
 * Static methods.
 */
public class BigMath {

    public static final double LOG_2 = Math.log(2.0);
    public static final double LOG_10 = Math.log(10.0);

    // numbers greater than 10^MAX_DIGITS_10 or e^MAX_DIGITS_E are considered unsafe ('too big') for floating point operations
    private static final int MAX_DIGITS_10 = 294;
    private static final int MAX_DIGITS_2 = 977; // ~ MAX_DIGITS_10 * LN(10)/LN(2)
    private static final int MAX_DIGITS_E = 677; // ~ MAX_DIGITS_10 * LN(10)

    /**
     * Computes the natural logarithm of a {@link BigInteger} 
     * <p>
     * Works for really big integers (practically unlimited), even when the argument 
     * falls outside the {@code double} range
     * <p>
     * 
     * 
     * @param val Argument
     * @return Natural logarithm, as in {@link java.lang.Math#log(double)}<br>
     * {@code Nan} if argument is negative, {@code NEGATIVE_INFINITY} if zero.
     */
    public static double logBigInteger(BigInteger val) {
        if (val.signum() < 1)
            return val.signum() < 0 ? Double.NaN : Double.NEGATIVE_INFINITY;
        int blex = val.bitLength() - MAX_DIGITS_2; // any value in 60..1023 works here
        if (blex > 0)
            val = val.shiftRight(blex);
        double res = Math.log(val.doubleValue());
        return blex > 0 ? res + blex * LOG_2 : res;
    }

    /**
     * Computes the natural logarithm of a {@link BigDecimal} 
     * <p>
     * Works for really big (or really small) arguments, even outside the double range.
     * 
     * @param val Argument
     * @return Natural logarithm, as in {@link java.lang.Math#log(double)}<br>
     * {@code Nan} if argument is negative, {@code NEGATIVE_INFINITY} if zero.
     */
    public static double logBigDecimal(BigDecimal val) {
        if (val.signum() < 1)
            return val.signum() < 0 ? Double.NaN : Double.NEGATIVE_INFINITY;
        int digits = val.precision() - val.scale();
        if (digits < MAX_DIGITS_10 && digits > -MAX_DIGITS_10)
            return Math.log(val.doubleValue());
        else
            return logBigInteger(val.unscaledValue()) - val.scale() * LOG_10;
    }

    /**
     * Computes the exponential function, returning a {@link BigDecimal} (precision ~ 16).
     * <p>
     * Works for very big and very small exponents, even when the result 
     * falls outside the double range.
     *
     * @param exponent Any finite value (infinite or {@code Nan} throws {@code IllegalArgumentException})    
     * @return The value of {@code e} (base of the natural logarithms) raised to the given exponent, 
     * as in {@link java.lang.Math#exp(double)}
     */
    public static BigDecimal expBig(double exponent) {
        if (!Double.isFinite(exponent))
            throw new IllegalArgumentException("Infinite not accepted: " + exponent);
        // e^b = e^(b2+c) = e^b2 2^t with e^c = 2^t 
        double bc = MAX_DIGITS_E;
        if (exponent < bc && exponent > -bc)
            return new BigDecimal(Math.exp(exponent), MathContext.DECIMAL64);
        boolean neg = false;
        if (exponent < 0) {
            neg = true;
            exponent = -exponent;
        }
        double b2 = bc;
        double c = exponent - bc;
        int t = (int) Math.ceil(c / LOG_10);
        c = t * LOG_10;
        b2 = exponent - c;
        if (neg) {
            b2 = -b2;
            t = -t;
        }
        return new BigDecimal(Math.exp(b2), MathContext.DECIMAL64).movePointRight(t);
    }

    /**
     * Same as {@link java.lang.Math#pow(double,double)} but returns a {@link BigDecimal} (precision ~ 16).
     * <p>
     * Works even for outputs that fall outside the {@code double} range.
     * <br>
     * The only limitation is that {@code b * log(a)} cannot exceed the {@code double} range. 
     * 
     * @param a Base. Should be non-negative 
     * @param b Exponent. Should be finite (and non-negative if base is zero)
     * @return Returns the value of the first argument raised to the power of the second argument.
     */
    public static BigDecimal powBig(double a, double b) {
        if (!(Double.isFinite(a) && Double.isFinite(b)))
            throw new IllegalArgumentException(
                    Double.isFinite(b) ? "base not finite: a=" + a : "exponent not finite: b=" + b);
        if (b == 0)
            return BigDecimal.ONE;
        else if (b == 1)
            return BigDecimal.valueOf(a);
        if (a <= 0) {
            if (a == 0) {
                if (b >= 0)
                    return BigDecimal.ZERO;
                else
                    throw new IllegalArgumentException("0**negative = infinite b=" + b);
            } else
                throw new IllegalArgumentException("negative base a=" + a);
        }
        double x = b * Math.log(a);
        if (Math.abs(x) < MAX_DIGITS_E)
            return BigDecimal.valueOf(Math.pow(a, b));
        else
            return expBig(x);
    }

}