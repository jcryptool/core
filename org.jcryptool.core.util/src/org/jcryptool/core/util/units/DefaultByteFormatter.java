package org.jcryptool.core.util.units;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A tool to nicely format bytes in various ways, which can be customized
 * in various flavors for (hopefully) your needs. Automatically fits 
 * JCrypTool's language (English or German) adhering stuff like 
 * thousand group separators and comma symbols.
 * 
 * Use the provided {@linkplain Builder} to adapt the settings with:
 * {@code new DefaultByteFormatter.Builder().yourSettingsHere().build()}
 * <p>
 * Either use the class directly or register it at {@link UnitsService}.
 * </p>
 * 
 * <p>
 * Supported functionality is 
 * <ul>
 *   <li>Base10 and Base2 Units (Kilobyte 1000 vs Kibibyte 1024)</li>
 *   <li>Abbreviated units (Kilobyte vs KB)</li>
 *   <li>Locale adapted thousand separators  (1300 vs 1,300 vs 1 300)</li>
 *   <li>Threshold at which bytes are displayed with a higher prefix
 *   (e.g. set it to 15000 so that 14333 --> 14,333 Bytes and 15672 --> 15.672 Kilobytes</li>
 *   <li>Precision for printing digits (30.2 Megabyte vs 30.21674 Megabyte).</li>
 *   <li>Supports BigIntegers and BigDecimals up to Yottabyte/Yobibyte</li>
 * </ul>
 * <p>
 * 
 * @see UnitsService#registerFormatter(ByteFormatter, key)
 */
public class DefaultByteFormatter implements ByteFormatter {
	
	public static class Builder {
		
		private BaseUnit baseUnit = BaseUnit.DEFAULT;
		private boolean abbreviate = false;
		private boolean useThousandSeparators = true;
		private BigDecimal threshold = BigDecimal.valueOf(-1L);
		private int precision = 3;
		
		public Builder asBase(BaseUnit baseUnit) {
			this.baseUnit = baseUnit;
			return this;
		}
		
		public Builder abbreviateUnit(boolean abbreviate) {
			this.abbreviate = abbreviate;
			return this;
		}
		
		public Builder scaleUpThreshold(long threshold) {
			this.threshold = BigDecimal.valueOf(threshold);
			return this;
		}
		public Builder scaleUpThreshold(BigDecimal threshold) {
			this.threshold = threshold;
			return this;
		}
		public Builder useThousandSeparators(boolean use) {
			this.useThousandSeparators = use;
			return this;
		}

		public Builder precision(int precision) {
			this.precision = precision;
			return this;
		}
		
		public DefaultByteFormatter build() {
			return new DefaultByteFormatter(
					baseUnit, abbreviate, threshold, useThousandSeparators, precision);
		}
		
	}

	public enum BaseUnit {
		BASE_10("base_10"),
		BASE_2("base_2");
		public static final BaseUnit DEFAULT=BASE_10;
		
		private BaseUnit(String style) { }
	}

	public enum Language {
		EN, DE
	}

	private BaseUnit baseUnit;
	private boolean abbreviate;
	private boolean useThousandSeparators;
	private final BigDecimal threshold;
	private final BigDecimal unitBig;
	private final BigDecimal maximumPossibleValue;
	private final BigDecimal naturalLogOfUnit;
	private int precision;
	private int unit;
	private final List<String> unitMapper;
	// Thin space to separate number from unit
	private char unitSeperator = '\u2009';
	
	
	public static final BigDecimal MAXIMUM_BASE_10_NUMBER =  new BigDecimal("1e27").subtract(BigDecimal.ONE);
	// 2^90 - 1 is the highest number representable by a Yobibyte
	public static final BigDecimal MAXIMUM_BASE_2_NUMBER = new BigDecimal("1237940039285380274899124223");
	
	private DecimalFormat formatter;
	
	private final String[] shortUnitsBase10 = new String[] {
			"B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"
	};
	private final String[] shortUnitsBase2 = new String[] {
			"B", "KiB", "MiB", "GiB", "TiB", "PiB", "EiB", "ZiB", "YiB"
	};
	
	private List<String> base10Units;
    private List<String> base2Units;
    private final BigDecimal THOUSAND = BigDecimal.valueOf(1000L);
    private final BigDecimal THOUSAND_TWENTY_FOUR = BigDecimal.valueOf(1024L);

	protected DefaultByteFormatter(
			BaseUnit baseUnit, 
			boolean abbreviate,
			BigDecimal scaleUpThreshold,
			boolean useThousandSeparators,
			int precision
	) {
		this.baseUnit = baseUnit;
		this.abbreviate = abbreviate;
		this.threshold = scaleUpThreshold.compareTo(BigDecimal.ZERO) <= 0 ? THOUSAND : scaleUpThreshold;
		this.useThousandSeparators = useThousandSeparators;
		this.precision = precision;
		initializeStrings();
		unitMapper = selectUnitMapper();
		formatter = createNumberFormatter();
		
		unit = baseUnit == BaseUnit.BASE_10 ? 1000 : 1024;
		unitBig = baseUnit == BaseUnit.BASE_10 ? THOUSAND : THOUSAND_TWENTY_FOUR;
		maximumPossibleValue = baseUnit == BaseUnit.BASE_10 ? MAXIMUM_BASE_10_NUMBER : MAXIMUM_BASE_2_NUMBER;
		naturalLogOfUnit = BigDecimal.valueOf(Math.log(unit));
	}
	
	private DecimalFormat createNumberFormatter() {
		var decimalFormatter = new DecimalFormat("#." + "#".repeat(precision));
		var symbols = decimalFormatter.getDecimalFormatSymbols();
		if (useThousandSeparators) {
			decimalFormatter.setGroupingUsed(true);
			decimalFormatter.setGroupingSize(3);
			symbols.setGroupingSeparator(Messages.LocaleThousandSeparator.charAt(0));
		} else {
			decimalFormatter.setGroupingUsed(false);
		}
		symbols.setDecimalSeparator(Messages.LocaleCommaSeparator.charAt(0));
		decimalFormatter.setDecimalFormatSymbols(symbols);
		return decimalFormatter;
	}
	/**
	 * Select one of the four possibilities of ascending units (Base 10 and Base 2 (IEC), Abbreviated and not)
	 * @return The appropriate List as specified by the settings.
	 */
	private List<String> selectUnitMapper() {
		if (abbreviate) {
			return baseUnit == BaseUnit.BASE_10 ? Arrays.asList(shortUnitsBase10) : Arrays.asList(shortUnitsBase2);
		} else {
			return baseUnit == BaseUnit.BASE_10 ? base10Units : base2Units;
		}
	}

	
	private void initializeStrings() {
		base10Units = new ArrayList<>();
    	base10Units.add(Messages.unitByte);
    	base10Units.add(Messages.unitKiloByte);
    	base10Units.add(Messages.unitMegaByte);
    	base10Units.add(Messages.unitGigaByte);
    	base10Units.add(Messages.unitTeraByte);
    	base10Units.add(Messages.unitPetaByte);
    	base10Units.add(Messages.unitExaByte);
    	base10Units.add(Messages.unitZettaByte);
    	base10Units.add(Messages.unitYottaByte);
	
    	base2Units = new ArrayList<String>();
    	base2Units.add(Messages.unitByte);
    	base2Units.add(Messages.unitKibiByte);
    	base2Units.add(Messages.unitMebiByte);
    	base2Units.add(Messages.unitGibiByte);
    	base2Units.add(Messages.unitTebiByte);
    	base2Units.add(Messages.unitPebiByte);
    	base2Units.add(Messages.unitExbiByte);
    	base2Units.add(Messages.unitZebibyte);
    	base2Units.add(Messages.unitYobiByte);
	}

	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public String humanReadableByteCount(BigDecimal bytes) {
		checkValue(bytes);
		
		if (bytes.compareTo(threshold) < 0) {
			return concat(bytes, THOUSAND, unitMapper.get(0));
		}

		int exp = BigDecimal.valueOf(BigMath.logBigDecimal(bytes)).
				divideToIntegralValue(naturalLogOfUnit).intValue();

		// In the edge case when threshold is set below 1000, set the exp to 1 (=1000)
		if (bytes.compareTo(THOUSAND) < 0) {
			exp = 1;
		}
		BigDecimal unitSize = unitBig.pow(exp);
		BigDecimal convertedBytes = bytes.divide(unitSize);
		return concat(convertedBytes, unitSize, unitMapper.get(exp));
	}
	
	/**
	 * 
	 * @param bytes
	 * @param unitSize
	 * @param unit
	 * @return
	 */
	private String concat(BigDecimal bytes, BigDecimal unitSize, String unit) {
		// Append an s if not abbreviated and either bytes = 1 or exactly divided.
		boolean isOne = bytes.compareTo(BigDecimal.ONE) == 0;
		// Remainder is at position 2 of returned array.
		BigDecimal remainder = bytes.divideAndRemainder(unitSize)[1];
		boolean isOneWithUnit = unitSize.compareTo(BigDecimal.ONE) > 0 &&
				remainder.compareTo(BigDecimal.ZERO) == 0;
		
		// Only append a plural 's' if is not 1 (byte = 1) or divides
		// a unit cleanly (1000 bytes = 1 Kilobyte)
		if (!abbreviate && !isOne && !isOneWithUnit) {
			unit += 's';
		}
		return formatter.format(bytes) + unitSeperator + unit; 
	}
	
	private void checkValue(BigDecimal value) {
		if (value.abs().compareTo(maximumPossibleValue) > 0) {
			throw new IllegalArgumentException(
					String.format(
							"Value too big to format: %s. The maximum allowed value is %s",
							formatter.format(value),
							formatter.format(maximumPossibleValue)
					)
			);
		}
	}

	@Override
	public String format(long bytes) {
		return humanReadableByteCount(BigDecimal.valueOf(bytes));
	}



	@Override
	public String format(int bytes) {
		return humanReadableByteCount(BigDecimal.valueOf(bytes));
	}


	@Override
	public String format(short bytes) {
		return humanReadableByteCount(BigDecimal.valueOf(bytes));
	}


	@Override
	public String format(byte bytes) {
		return humanReadableByteCount(BigDecimal.valueOf(bytes));
	}



	@Override
	public String format(char bytes) {
		return humanReadableByteCount(BigDecimal.valueOf(bytes));
	}

	@Override
	public String format(BigDecimal bytes) {
		return humanReadableByteCount(bytes);
	}
	
	@Override
	public String format(BigInteger bytes) {
		return humanReadableByteCount(new BigDecimal(bytes));
	}


}
