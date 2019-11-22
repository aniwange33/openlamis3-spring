
/**
 * @author aalozie
 */
package org.fhi360.lamis.utility;

public class StringUtil {
    private static final String[] SUFFIX = {"y"};
    private static final String[] REPLACEMENTS = {"s", "ies"};

    public static String pluralize(String str) {
        if (str.endsWith(SUFFIX[0])) {
            str = str.replace(str.substring(str.lastIndexOf(SUFFIX[0])), REPLACEMENTS[1]);
        } else {
            str += "s";
        }
        return str;
    }

    public static String setCountryCode(String phone, String countryCode) {
        return phone.replaceFirst("\\d", countryCode);
    }

    public static String zerorize(String str, int max) {
        String zeros = "";
        if (str.length() < max) {
            for (int i = 0; i < max - str.length(); i++) {
                zeros = zeros + "0";
            }
            str = zeros + str;
        }
        return str;
    }

    public static boolean isInteger(String s) {
        s = s.replace(".", "").replace(",", "");
        int radix = 10;
        if (s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) return false;
                else continue;
            }
            if (Character.digit(s.charAt(i), radix) < 0) return false;
        }
        return true;
    }

    public static String stripCommas(String s) {
        return s.replace(",", "").replace(";", "").replace("'", "");

    }

    public static boolean found(String searchFor, String[] strings) {
        boolean found = false;
        for (String string : strings) {
            if (string.equalsIgnoreCase(searchFor.toLowerCase())) {
                found = true;
            }
        }
        return found;
    }


    public static String toCamelCase(String s) {
        String[] parts = s.split("_");
        String camelCaseString = "";

        for (String part : parts) {
            camelCaseString = camelCaseString + toProperCase(part);

        }
        return camelCaseString.substring(0, 1).toLowerCase() + camelCaseString.substring(1);
    }

    public static String toProperCase(String s) {
        return s.substring(0, 1).toUpperCase() +
                s.substring(1).toLowerCase();
    }
}
