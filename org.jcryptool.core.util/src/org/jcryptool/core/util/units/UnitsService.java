// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.util.units;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.jcryptool.core.util.units.DefaultByteFormatter.BaseUnit;

/**
 * Utility class for unit conversion / handling provided for all plugins.
 * 
 * Currently supports pretty byte formatting.
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

 */
public class UnitsService {

    private static UnitsService instance;
    private HashMap<String, ByteFormatter> formatterMap = new HashMap<>();
    private final Set<String> defaults = new HashSet<>();
	
	private UnitsService() {
    	formatterMap.put(KEY_DEFAULT, DEFAULT);
    	formatterMap.put(KEY_BASE10, DEFAULT);
    	formatterMap.put(KEY_BASE10_ABBREVIATED, BASE10_ABBREVIATED);
    	formatterMap.put(KEY_BASE2, BASE2);
    	formatterMap.put(KEY_BASE2_ABBREVIATED, BASE2);
    	
    	
		defaults.add(KEY_DEFAULT);
		defaults.add(KEY_BASE10);
		defaults.add(KEY_BASE10_ABBREVIATED);
		defaults.add(KEY_BASE2);
		defaults.add(KEY_BASE2_ABBREVIATED);
	}
	
	/**
	 * Get the JCrypTool UnitsService ByteFormatter functionality
	 * @return the singleton instance
	 */
	public static UnitsService get() {
		if (UnitsService.instance == null) {
			UnitsService.instance = new UnitsService();
		}
		return UnitsService.instance;
	}

	
	private final String KEY_DEFAULT = "__default";
	private final String KEY_BASE10 = "__default";
	private final String KEY_BASE10_ABBREVIATED = "__default_abbreviated";
	private final String KEY_BASE2 = "__default_base2";
	private final String KEY_BASE2_ABBREVIATED = "__default_base2_abbreviated";
	

	/**
	 * Make your own {@linkplain ByteFormatter} available for {@link UnitsService#getFormatter(myFormatter)} and
	 * {@link UnitsService#format(bytes, myFormatter)}}
	 * 
	 * <p>
	 * @implNote Your registered Formatters are available for all JCrypTool plugins. Please keep that in mind
	 *           and do not rely on other's custom formatters.
	 * </p>
	 * @param formatter You can create your own ByteFormatter (with custom parameters like
	 *                  precision, unit, minimum threshold for going to kilobytes) by calling
	 *                  {@linkplain new DefaultByteFormater.Builder().yourParamsHere().build()}
	 * @param key       A unique identifier to store the given {@code formatter}. As you share 
	 *                  this registry with other plugins, try to choose a unique key.
	 *                  You cannot override an existing entry, such a request will do nothing.
	 *                  Don't use keys beginning with "__XYZ".
	 */
	public void registerFormatter(ByteFormatter formatter, String key) {
		if (!formatterMap.containsKey(key)) {
			formatterMap.put(key, formatter);
		}
	}

	/**
	 * De-register a custom Formatter. Calling with non-present keys will do nothing.
	 * 
	 * @implNote The registered Formatters are kept here once for all plugins,
	 *           so you could delete other plugins custom formatters. Please don't do that.
	 * 
	 * @param key The target specified by its String key.
	 */
	public void deregisterFormatter(String key) {
		if (defaults.contains(key)) {
			throw new IllegalArgumentException("Default key " + key + " may not be de-registered");
		}
		formatterMap.remove(key);
	}
	
	/**
	 * Get a {@linkplain ByteFormatter} and start formatting.
	 * 
	 * <p>
	 * There are default Formatters available, see below. As a shortcut,
	 * you can also call {@link UnitsService#format(bytes, formatterKey)} with your
	 * registered key.
	 * </p>
	 * 
	 * <p>
	 * You can create your own {@link ByteFormatter} (with custom parameters like
	 * precision, unit, minimum threshold for going to kilobytes, etc.) by calling
	 * {@linkplain new DefaultByteFormater.Builder().yourParamsHere().build()}
	 * </p>
	 * 
	 *@throws IllegalArgumentException on requesting a Formatter key which is not registered.
	 *        You can call {@link UnitsService#isFormatterAvailable(key)} to check beforehand. 
	 
	 * @param   key The target ID 
	 * @return  A {@linkplain ByteFormatter} which can be used to call format() on various integer datatypes.
	 * 
	 * @see UnitsService#registerFormatter(formatter, key)
	 * @see UnitsService#DEFAULT
	 * @see UnitsService#BASE10
	 * @see UnitsService#BASE10_ABBREVIATED
	 * @see UnitsService#BASE2
	 * @see UnitsService#BASE2_ABBREVIATED
	 */
	public ByteFormatter getFormatter(String key) {
		if (formatterMap.containsKey(key)) {
			return formatterMap.get(key);
		}
		throw new IllegalArgumentException("Requested formatter not registered.");
	}
	
	
	/**
	 * Check if a custom Formatter is present in the UnitsService Byteformatter service.
	 * @param key The target Formatter key.
	 * @return    {@code true} when present, {@code false} otherwise.
	 */
	public boolean isFormatterAvailable(String key) {
		return formatterMap.containsKey(key);
	}


	/**
	 * Simply format your bytes without having to mind about locales/languages.
	 * 
	 * <p>
	 * This is a shortcut for {@linkplain UnitsService.DEFAULT.format(bytes)}
	 * </p>
	 * 
	 * <p>
	 * There are other predefined Formatters available, see below. 
	 * If you want to parameterize / adapt your own settings, take a look at {@link DefaultByteFormatter} and
	 * {@link UnitsService#registerFormatter(ByteFormatter, key)}.
	 * There you have plenty of options to customize your settings such as 
	 * precision, unit, minimum threshold for going to kilobytes, etc.
	 * Register them with {@link UnitsService#registerFormatter(ByteFormatter, key)} and use them with
	 * {@link UnitsService#getFormatter(key)} or directly with {@link UnitsService#format(bytes, key)}.
	 * </p>
	 * @param bytes Target number to format.
	 * @return      A nicely formatted String with a Byte unit according to the input size
	 *              (base 10 Kilobytes, Megabytes).
	 *
	 * @see UnitsService#DEFAULT
	 * @see DefaultByteFormatter
	 * @see UnitsService#registerFormatter(ByteFormatter, key)
	 * @see UnitsService#format(bytes, key)
	 */
    public static String format(long bytes) {
    	return DEFAULT.format(bytes);
    }
    
    /**
	 * Simply format your bytes without having to mind about locales/languages.
	 * Specify a custom target formatter with {@code key}, which has to be registered beforehand.
	 * 
	 * <p>
	 * There are predefined Formatters available, see below. 
	 * If you want to register your own Formatter on which you can parameterize / adapt
	 * your own settings, take a look at {@link DefaultByteFormatter} and
	 * {@link UnitsService#registerFormatter(ByteFormatter, key)}.
	 * There you have plenty of options to customize your settings such as 
	 * precision, unit, minimum threshold for going to kilobytes, etc.
	 * Register them with {@link UnitsService#registerFormatter(ByteFormatter, key)} and use them with
	 * {@link UnitsService#getFormatter(key) or UnitsService#format(bytes, key)} directly.
	 * </p>
     * 
	 * <p>
	 * This is a shortcut for {@linkplain UnitsService.get().getFormatter(key).format(bytes)}
	 * </p>
	 * 
     * @param bytes          Target number to format
     * @param formatterKey   A registered Formatter key to use.
     * 
     * @return      A nicely formatted String with a Byte unit as provided
     *              by the specified Formatter class.
	 *
	 * @see UnitsService#registerFormatter(ByteFormatter, key)
	 * @see UnitsService#DEFAULT
	 * @see DefaultByteFormatter
	 * @see UnitsService#format(bytes)
	 */
    public static String format(long bytes, String formatterKey) {
    	return UnitsService.get().getFormatter(formatterKey).format(bytes);
    }
    
    
    /**
     * Nicely format {@code BigInteger} bytes, see {@link UnitsService#format(long bytes)}
     * @param bytes          Target number to format
     * @return      A nicely formatted String with a Byte unit according to the input size
	 *              (base 10 Kilobytes, Megabytes).
     */
    public static String format(BigInteger bytes) {
    	return DEFAULT.format(bytes);
    }
    
    /**
     * Nicely format {@code BigInteger} bytes, see {@link UnitsService#format(long bytes, String formatterKey)}
     * @param bytes          Target number to format
     * @param formatterKey   A registered Formatter key to use.
     * @return      A nicely formatted String with a Byte unit as provided
     *              by the specified Formatter class.
     */
    public static String format(BigInteger bytes, String formatterKey) {
    	return UnitsService.get().getFormatter(formatterKey).format(bytes);
    }
    
    /**
     * The default byte formatter which uses base10 (Kilobyte, Megabyte) units.
     * It automatically adapts to the locale (English, German).
     * The unit is written out. Numbers are converted as soon as they reach the
     * next unit. The precision defaults to 3, so numbers may be rounded
     * 
     * <p>
     * Use {@link UnitsService.get().registerFormatter()} to register your own ByteFormatter.
     * There is a default implementation available which you can parameterize at {@link DefaultByteFormatter}
     * </p>
     * 
     * <ul>
     * 	<li>986     --> 986 bytes / 986 Bytes</li>
     * 	<li>1024    --> 1.024 kilobytes / 1,024 Kilobytes</li>
     * 	<li>14368   --> 14.368 kilobytes / 14,368 Kilobytes</li>
     * 	<li>5743487 --> 5.743 megabytes / 5,743 Megabytes</li>
     * </ul>
     * 
     * @see UnitsService.registerFormatter
     * @see DefaultByteFormatter
     * @see BASE10_ABBREVIATED
     * @see BASE2
     * @see BASE2_ABBREVIATED
     */
    public static ByteFormatter DEFAULT = new DefaultByteFormatter.Builder().build();
    
    /**
     * A basic byte formatter which uses base10 (Kilobyte, Megabyte) units.
     * It automatically adapts to the locale (English, German).
     * The unit is written out. Numbers are converted as soon as they reach the
     * next unit. The precision defaults to 3, so numbers may be rounded
     * 
     * <p>
     * Use {@link UnitsService.get().registerFormatter()} to register your own ByteFormatter.
     * There is a default implementation available which you can parameterize at {@link DefaultByteFormatter}
     * </p>
     * 
     * <ul>
     * 	<li>986     --> 986 bytes / 986 Bytes</li>
     * 	<li>1024    --> 1.024 kilobytes / 1,024 Kilobytes</li>
     * 	<li>14368   --> 14.368 kilobytes / 14,368 Kilobytes</li>
     * 	<li>5743487 --> 5.743 megabytes / 5,743 Megabytes</li>
     * </ul>
     * 
     * @see UnitsService.registerFormatter
     * @see DefaultByteFormatter
     * @see DEFAULT
     * @see BASE10_ABBREVIATED
     * @see BASE2
     * @see BASE2_ABBREVIATED
     */
    public static ByteFormatter BASE10 = DEFAULT;
    /**
     * A basic byte formatter which uses base10 (KB, MB) units.
     * It automatically adapts to the locale (English, German).
     * The unit is abbreviated out. Numbers are converted as soon as they reach the
     * next unit. The precision defaults to 3, so numbers may be rounded
     * 
     * <p>
     * Use {@link UnitsService.get().registerFormatter()} to register your own ByteFormatter.
     * There is a default implementation available which you can parameterize at {@link DefaultByteFormatter}
     * </p>
     * 
     * <ul>
     * 	<li>986     --> 986 B / 986 B</li>
     * 	<li>1024    --> 1.024 KB / 1,024 KB</li>
     * 	<li>14368   --> 14.368 KB / 14,368 KB</li>
     * 	<li>5743487 --> 5.743 MB / 5,743</li>
     * </ul>
     * 
     * @see UnitsService.registerFormatter
     * @see DefaultByteFormatter
     * @see DEFAULT
     * @see BASE10
     * @see BASE2
     * @see BASE2_ABBREVIATED
     */
    public static ByteFormatter BASE10_ABBREVIATED =
    		new DefaultByteFormatter.Builder().abbreviateUnit(false).build();
    /**
     * A basic byte formatter which uses written-out base2 (Kibibyte, Mebibyte) IEC units.
     * It automatically adapts to the locale (English, German).
     * The unit is abbreviated out. Numbers are converted as soon as they reach the
     * next unit. The precision defaults to 3, so numbers may be rounded
     * 
     * <p>
     * Use {@link UnitsService.get().registerFormatter()} to register your own ByteFormatter.
     * There is a default implementation available which you can parameterize at {@link DefaultByteFormatter}
     * </p>
     * 
     * <ul>
     * 	<li>986     --> 986 bytes / 986 Bytes</li>
     * 	<li>1024    --> 1 kibibyte / 1 Kibibyte</li>
     * 	<li>143687  --> 14.031 kibibytes /  14,031 Kibibytes</li>
     * 	<li>5743487 --> 5.743 mebibytes / 5,743 Mebibytes</li>
     * </ul>
     * 
     * @see UnitsService.registerFormatter
     * @see DefaultByteFormatter
     * @see DEFAULT
     * @see BASE10
     * @see BASE10_ABBREVIATED
     * @see BASE2_ABBREVIATED
     */
    public static ByteFormatter BASE2 =
    		new DefaultByteFormatter.Builder().asBase(BaseUnit.BASE_2).build();
    /**
     * A basic byte formatter which uses shortened base2 (KiB, MiB) IEC units.
     * It automatically adapts to the locale (English, German).
     * Numbers are converted as soon as they reach the next unit.
     * The precision defaults to 3, so numbers may be rounded.
     * 
     * <p>
     * Use {@link UnitsService.get().registerFormatter()} to register your own ByteFormatter.
     * There is a default implementation available which you can parameterize at {@link DefaultByteFormatter}
     * </p>
     * 
     * <ul>
     * 	<li>986     --> 986 bytes / 986 Bytes</li>
     * 	<li>1024    --> 1 KiB / 1 KiB</li>
     * 	<li>143687  --> 14.031 KiB /  14,031 KiB</li>
     * 	<li>5743487 --> 5.743 MiB / 5,743 MiB</li>
     * </ul>
     * 
     * @see UnitsService.registerFormatter
     * @see DefaultByteFormatter
     * @see DEFAULT
     * @see BASE10
     * @see BASE10_ABBREVIATED
     * @see BASE2
     */
    public static ByteFormatter BASE2_ABBREVIATED =
    		new DefaultByteFormatter.Builder().abbreviateUnit(false).asBase(BaseUnit.BASE_2).build();
    

    /**
     * converts byte count to a human readable string Example output: SI BINARY
     * 
     * 0: 0 B 0 B 27: 27 B 27 B 999: 999 B 999 B 1000: 1.0 KB 1000 B 1023: 1.0 KB 1023 B 1024: 1.0 KB 1.0 KiB 1728: 1.7
     * KB 1.7 KiB 110592: 110.6 KB 108.0 KiB 7077888: 7.1 MB 6.8 MiB 452984832: 453.0 MB 432.0 MiB 28991029248: 29.0 GB
     * 27.0 GiB 1855425871872: 1.9 TB 1.7 TiB 9223372036854775807: 9.2 EB 8.0 EiB (Long.MAX_VALUE)
     * 
     * @return byte count including units as string
     */

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit)
            return bytes + " B"; //$NON-NLS-1$
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre); //$NON-NLS-1$
    }
    
    public static void main(String[] args) {
    	ByteFormatter myByteFormatter = new DefaultByteFormatter.Builder().build();
    	System.out.println(myByteFormatter.format(983));
    }

    
    
}
