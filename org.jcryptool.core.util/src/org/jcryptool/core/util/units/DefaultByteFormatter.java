package org.jcryptool.core.util.units;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultByteFormatter implements ByteFormatter {
	
	public static class Builder {
		
		private BaseUnit baseUnit = BaseUnit.DEFAULT;
		private Language language = Language.EN;
		private boolean abbreviate = false;
		private boolean useThousandSeparators = true;
		private long threshold = -1;
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
	private long threshold;
	private int precision;
	private final List<String> unitMapper;
	private char unitSeperator = '\u2009';
	
	
	private static final long MAXIMUM_BASE_10_NUMBER = (long) 1000e11 - 1;
	private static final long MAXIMUM_BASE_2_NUMBER = ((long) Math.pow(1024, 9)) - 1;
	
	private DecimalFormat formatter;
	
	private final String[] shortUnitsBase10 = new String[] {
			"B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"
	};
	private final String[] shortUnitsBase2 = new String[] {
			"B", "KiB", "MiB", "GiB", "TiB", "PiB", "EiB", "ZiB", "YiB"
	};
	
	private List<String> base10Units;
    private List<String> base2Units;

	protected DefaultByteFormatter(
			BaseUnit baseUnit, 
			boolean abbreviate,
			long scaleUpThreshold,
			boolean useThousandSeparators,
			int precision
	) {
		this.baseUnit = baseUnit;
		this.abbreviate = abbreviate;
		this.threshold = scaleUpThreshold <= 0 ? 1000 : scaleUpThreshold;
		this.useThousandSeparators = useThousandSeparators;
		this.precision = precision;
		initializeStrings();
		unitMapper = selectUnitMapper();
		formatter = createNumberFormatter();
	}
	
	private DecimalFormat createNumberFormatter() {
		var decimalFormatter = new DecimalFormat("#." + "#".repeat(precision));
		var symbols = decimalFormatter.getDecimalFormatSymbols();
		if (useThousandSeparators) {
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
	public String humanReadableByteCount(BigInteger bytes) {
		
		int unit = baseUnit == BaseUnit.BASE_10 ? 1000 : 1024;
		
		if (bytes < threshold) {
			return concat(bytes, 1000, unitMapper.get(0));
		}

		int exp = (int) (Math.log(bytes) / Math.log(unit));

		// In the edge case when threshold is set below 1000, set the exp to 1 (=1000)
		if (bytes < 1000) {
			exp = 1;
		}
		double convertedBytes = bytes / Math.pow(unit, exp);
		return concat(convertedBytes, (long) Math.pow(unit, exp), unitMapper.get(exp));
	}
	
	/**
	 * 
	 * @param bytes
	 * @param reference
	 * @param unit
	 * @return
	 */
	private String concat(double bytes, long reference, String unit) {
		// Append an s if not abbreviated and either bytes = 1 or exactly divided.
		boolean isOne = bytes == 1;
		boolean isOneWithUnit = reference > 1 && bytes % reference == 0;
		
		if (!abbreviate && !isOne && !isOneWithUnit) {
			unit += 's';
		}
		return formatter.format(bytes) + unitSeperator + unit; 
	}
	
	private void checkExponent(int exponent) {
		if (exponent >= unitMapper.size()) {
			throw new IllegalArgumentException("");
		}
	}

	@Override
	public String format(long bytes) {
		return humanReadableByteCount(bytes);
	}



	@Override
	public String format(int bytes) {
		return humanReadableByteCount(bytes);
	}


	@Override
	public String format(short bytes) {
		return humanReadableByteCount(bytes);
	}


	@Override
	public String format(byte bytes) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String format(char bytes) {
		// TODO Auto-generated method stub
		return null;
	}


}
