package main.java.cz.pavelkalas.utils;

import java.util.Arrays;

/**
 * Třída pro převody a kontrolu datových typů.
 */
public class DateTypesUtil {
	/**
	 * Parsuje číslo ze Stringu
	 * 
	 * @param val - Hodnota
	 * @return Navrací číslo pokud bylo úspěšně vyparsováno, jinak vrací 0
	 */
	public static int parseInteger(String val) {
		if (isInteger(val)) {
			return Integer.parseInt(val);
		}

		return 0;
	}

	/**
	 * Kontroluje, zda je String typu Integer.
	 * 
	 * @param val - Hodnota
	 * @return Navrací TRUE v případě, že je String typu Integer.
	 */
	public static boolean isInteger(String val) {
		if (val == null) {
			throw new IllegalArgumentException("Value cannot be null.");
		}

		try {
			Integer.parseInt(val);
			return true;
		} catch (Exception excp) {
		}

		return false;
	}

	/**
	 * Kontroluje, zda je String typu float.
	 * 
	 * @param val - Hodnota
	 * @return - Navrací TRUE v případě, že je String typu float.
	 */
	public static boolean isFloat(String val) {
		if (val == null) {
			throw new IllegalArgumentException("Value cannot be null.");
		}

		try {
			Float.parseFloat(val);
			return true;
		} catch (Exception excp) {
		}

		return false;
	}

	/**
	 * Kontroluje, zda je String typu double.
	 * 
	 * @param val - Hodnota
	 * @return - Navrací TRUE v případě, že je String typu double.
	 */
	public static boolean isDouble(String val) {
		if (val == null) {
			throw new IllegalArgumentException("Value cannot be null.");
		}

		try {
			Double.parseDouble(val);
			return true;
		} catch (Exception excp) {
		}

		return false;
	}

	/**
	 * Kontroluje, zda je String boolean.
	 * 
	 * @param val - Hodnota
	 * @return - Navrací TRUE v případě, že string obsahuje "1", jinak 0.
	 */
	public static boolean parseBoolean(String val) {
		if (val == null) {
			throw new IllegalArgumentException("Value cannot be null.");
		}

		if (val.equals("0")) {
			return false;
		} else if (val.equals("1")) {
			return true;
		}

		return false;
	}

	/**
	 * Kontroluje, zda je int typu boolean.
	 * 
	 * @param val - Hodnota
	 * @return - Navrací TRUE pokud je int 1, jinak 0
	 */
	public static boolean parseBooleanStrict(int val) {
		return val == 1 ? true : false;
	}

	/**
	 * Kontroluje, zda je int typu boolean.
	 * 
	 * @param val - Hodnota
	 * @return - Navrací TRUE pokud je hodnota vyšší než 0, jinak FALSE.
	 */
	public static boolean parseBoolean(int val) {
		return val > 0 ? true : false;
	}

	/**
	 * Vyparsuje argumenty[] jako čísla.
	 * 
	 * @param val - Array čísel (String)
	 * @return - Navrací Array čísel (int)
	 */
	public static int[] getNumbersFromString(String[] val) {
		if (val == null) {
			throw new IllegalArgumentException("Value and cannot be null.");
		}

		return Arrays.stream(val).mapToInt(num -> {
			try {
				return Integer.parseInt(num.trim());
			} catch (NumberFormatException e) {
				return 0;
			}
		}).toArray();
	}
}
