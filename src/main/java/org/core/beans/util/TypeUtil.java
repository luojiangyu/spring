package org.core.beans.util;

public class TypeUtil {
	public static final String INT = "java.lang.Integer";
	public static final String DOUBLE = "java.lang.Double";
	public static final String FLOAT = "java.lang.Float";

	public static Object packageData(String type, String input) {
		Object result = null;
		switch (type) {
		case INT:
			result = Integer.valueOf(input);
			break;
		case DOUBLE:
			result = Double.valueOf(input);
			break;
		case FLOAT:
			result = Float.valueOf(input);
			break;
		default:
			result = input;

		}
		return result;
	}
}
