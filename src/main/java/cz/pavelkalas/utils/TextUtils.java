package main.java.cz.pavelkalas.utils;

public class TextUtils {

	/**
	 * Převádí prostý text na hex.
	 * 
	 * @param text - Text
	 * @return - Hex
	 */
	public static String stringToHex(String text) {
		if (text == null) {
			throw new IllegalArgumentException("");
		}

		byte[] b = text.getBytes();
		String result = "";

		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}

		return result;
	}
}
